package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.AgentTagsMapper;
import com.strelizia.arknights.model.AgentTagsInfo;
import com.strelizia.arknights.service.TagsfFoundService;
import com.strelizia.arknights.util.BaiduAPIUtil;
import com.strelizia.arknights.util.FormatStringUtil;
import com.strelizia.arknights.util.TagsUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangzy
 * @Date 2020/12/15 10:41
 **/
@Service
@Slf4j
public class TagsFoundServiceImpl implements TagsfFoundService {

    @Autowired
    private AgentTagsMapper agentTagsMapper;

    @Value("${baiduConfig.APP_ID}")
    private String APP_ID;

    @Value("${baiduConfig.API_KEY}")
    private String API_KEY;

    @Value("${baiduConfig.SECRET_KEY}")
    private String SECRET_KEY;

    @Override
    public String FoundAgentByJson(String Json) {
        Map<List<String>, List<AgentTagsInfo>> listListMap = FoundTagsByImg(Json);
        String s = MapToString(listListMap);
        return s;
    }

    @Override
    public String FoundAgentByArray(String[] array) {
        Map<List<String>, List<AgentTagsInfo>> listMap = FoundTagResultByArrays(array);
        String s = MapToString(listMap);
        return s;
    }

    public Map<List<String>, List<AgentTagsInfo>> FoundTagsByImg(String Json) {
        //单例模式的百度API实例
        BaiduAPIUtil baiduAPIUtil = BaiduAPIUtil.getInstance(APP_ID, API_KEY, SECRET_KEY);
        //去掉\r等有歧义的转义字符
        String replace = Json.replace("\r", "");

        //获取Json字符串，转Json对象
        JSONObject jsonObj = new JSONObject(replace);
        JSONArray array = new JSONArray(jsonObj.get("GroupPic").toString());
        //获取到图片的URL
        String url = array.getJSONObject(0).getString("Url");

        //调用百度api图片识别
        String[] s = baiduAPIUtil.BaiduOCRGetTags(url);
        log.info("识图获取到tag为：{}",Arrays.asList(s));

        Map<List<String>, List<AgentTagsInfo>> map = FoundTagResultByArrays(s);

        return map;
    }

    public Map<List<String>, List<AgentTagsInfo>> FoundTagResultByArrays(String[] s){

        //把数组进行笛卡尔积组合
        List<List<String>> allCompose = TagsUtil.getAllCompose(Arrays.asList(s));

        //用于保存结果
        Map<List<String>,List<AgentTagsInfo>> result = new HashMap<>();

        //遍历所有的组合
        for(List<String> list:allCompose){
            //没有tag的时候跳出循环
            if (list.size() == 0){
                continue;
            }
            if(TagsUtil.isHave(list,"高级资深干员"))
            {
                result.put(list,agentTagsMapper.selectSixAgentByTag(list));
            }else {
                result.put(list, agentTagsMapper.selectAgentByTag(list));
            }
        }
        return result;
    }

    //把Map转换成特定格式字符串
    public String MapToString(Map<List<String>, List<AgentTagsInfo>> map){
        //保存结果
        String s = "";
        //循环遍历Map
        for (Map.Entry<List<String>, List<AgentTagsInfo>> m:map.entrySet()){
            //获取到Key，Value
            List<String> key = m.getKey();
            List<AgentTagsInfo> value = m.getValue();
            //如果组合中没有值，或者组合中不能确定稀有干员，则直接进行下次循环
            if (value.size() == 0||isThreeOrTwoInMap(value)) continue;
            String tags = "";
            String agents = "";
            //用key的标签组合作为head
            for (String tag:key){
                tags = tags + ","+ tag;
            }
            //用干员名字+星星的列表作为body
            for (AgentTagsInfo agent:value){
                String levelStar;
                Integer star = agent.getStar();
                //不要三星两星的结果
                if (star == 3|| star == 2) break;
                levelStar = FormatStringUtil.FormatStar(star);
                agents = agents + "\n" + agent.getAgentName() + levelStar;
            }
            s = s  + "\n\n" + tags.substring(1) + "\n" + agents.substring(1);
        }
        if (s.equals("")){
            s = "\t\tQAQ没有找到对应的稀有公招结果";
        }
        return s.substring(2);
    }

    //判定里面是否有三星/两星
    public boolean isThreeOrTwoInMap(List<AgentTagsInfo> map){
        for (AgentTagsInfo a:map){
            Integer star = a.getStar();
            if (star ==3|| star ==2){
                return true;
            }
        }
        return false;
    }
}
