package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.UpdateMapper;
import com.strelizia.arknights.dao.UserFoundMapper;
import com.strelizia.arknights.model.OperatorEvolveInfo;
import com.strelizia.arknights.model.OperatorInfo;
import com.strelizia.arknights.model.OperatorSkillInfo;
import com.strelizia.arknights.model.SkillMaterInfo;
import com.strelizia.arknights.service.UpdateDataService;
import com.strelizia.arknights.util.SendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangzy
 * @Date 2020/12/19 15:46
 **/
@Service
@Slf4j
public class UpdateDataServiceImpl implements UpdateDataService {

    private final String operatorListUrl = "https://andata.somedata.top/data-2020/char/list/";

    private final String enemyListUrl = "https://andata.somedata.top/data-2020/lists/enemy/";

    private final String operatorIdUrl = "https://andata.somedata.top/data-2020/char/data/";

    private final String skillIdUrl = "https://andata.somedata.top/data-2020/skills/";

    private final String enemyIdUrl = "https://andata.somedata.top/data-2020/enemy/";

    @Autowired
    private UserFoundMapper userFoundMapper;

    @Autowired
    private UpdateMapper updateMapper;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Autowired
    protected RestTemplate restTemplate;

    @Override
    public Integer updateAllData(String JsonId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        updateMapper.clearOperatorData();
        List<Long> groups = userFoundMapper.selectAllActiveGroups();

        for (Long groupId:groups){
            String s = "游戏数据闪断更新中，更新期间存在无响应情况，请耐心等待更新完成。\n" +
                    "若十分钟后仍未收到更新完成信息，请联系开发者重新进行更新请求\n--" +
                    sdf.format(new Date());
            sendMsgUtil.CallOPQApiSendMsg(groupId,s,2);
        }

        Integer operatorSize = updateAllOperator(JsonId);

        for (Long groupId:groups){
            String s = "游戏数据更新完成\n--" + sdf.format(new Date());
            sendMsgUtil.CallOPQApiSendMsg(groupId,s,2);
        }
        return operatorSize;
    }

    public Integer updateAllOperator(String JsonId){

        //发送请求，封装所有的干员基础信息列表
        String allOperator = getJsonStringFromUrl(operatorListUrl + JsonId + ".json");

        JSONArray json = new JSONArray(allOperator);
        int length = json.length();
        for (int i = 0; i < length; i++){
            JSONObject operator = json.getJSONObject(i);
            String operatorId = operator.getString("No");
            //发送请求遍历干员详细信息
            String operatorJson = getJsonStringFromUrl(operatorIdUrl + operatorId + ".json");
            updateOperatorByJson(operatorJson);
        }
        log.info("更新完成，共更新了{}个干员信息",length);
        return length;
    }

    public Integer updateAllEnemy(){
        //TODO 更新敌人数据，更新材料数据，更新关卡数据
        //发送请求，封装所有的干员基础信息列表
        String allEnemy = getJsonStringFromUrl(enemyListUrl);
        JSONArray json = new JSONArray(allEnemy);
        return 0;
    }

    //发送url的get请求获取结果json字符串
    public String getJsonStringFromUrl(String url){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("User-Agent","PostmanRuntime/7.26.8");
        httpHeaders.set("Authorization","2");
        httpHeaders.set("Host","andata.somedata.top");
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        String s = restTemplate
                .exchange(url, HttpMethod.GET, httpEntity, String.class).getBody();
        return s;
    }

    @Override
    public Integer updateOperatorByJson(String json) {
        Map<String, Integer> operatorClass = new HashMap<>(8);
        operatorClass.put("PIONEER", 1);
        operatorClass.put("WARRIOR", 2);
        operatorClass.put("TANK", 3);
        operatorClass.put("SNIPER", 4);
        operatorClass.put("CASTER", 5);
        operatorClass.put("SUPPORT", 6);
        operatorClass.put("MEDIC", 7);
        operatorClass.put("SPECIAL", 8);

        JSONObject jsonObj = new JSONObject(json);
        String name = jsonObj.getString("name");
        int rarity = jsonObj.getInt("rarity") + 1;
        jsonObj.getString("position");
        boolean isNotObtainable = jsonObj.getBoolean("isNotObtainable");

        //封装干员信息
        OperatorInfo operatorInfo = new OperatorInfo();
        operatorInfo.setOperator_name(name);
        operatorInfo.setOperator_rarity(rarity);
        if (isNotObtainable == false) {
            operatorInfo.setAvailable(1);
        } else {
            operatorInfo.setAvailable(0);
        }
        operatorInfo.setIn_limit(0);
        operatorInfo.setOperator_class(operatorClass.get(jsonObj.getString("profession")));

        updateMapper.insertOperator(operatorInfo);
        log.info("更新{}干员基础信息",name);
        Integer operatorId = updateMapper.selectOperatorIdByName(name);

        JSONArray phases = jsonObj.getJSONArray("phases");
        if (operatorId != null) {
            //封装干员精英化花费
            for (int i = 1; i < phases.length(); i++) {
                JSONObject array = phases.getJSONObject(i);
                if (array.get("evolveCost") instanceof JSONArray) {
                    JSONArray evolveJson = array.getJSONArray("evolveCost");
                    for (int j = 0; j < evolveJson.length(); j++) {
                        JSONObject evolve = evolveJson.getJSONObject(j);
                        //精英i花费
                        OperatorEvolveInfo operatorEvolveInfo = new OperatorEvolveInfo();
                        operatorEvolveInfo.setOperatorId(operatorId);
                        operatorEvolveInfo.setEvolveLevel(i);
                        operatorEvolveInfo.setUseMaterialId(evolve.getInt("id"));
                        operatorEvolveInfo.setUseNumber(evolve.getInt("count"));
                        updateMapper.insertOperatorEvolve(operatorEvolveInfo);

                    }
                }
                log.info("更新{}干员精英{}信息", name, i);
            }

            //封装干员技能
            JSONArray skills = jsonObj.getJSONArray("skills");
            for (int i = 0; i < skills.length(); i++) {
                OperatorSkillInfo operatorSkillInfo = new OperatorSkillInfo();
                operatorSkillInfo.setOperatorId(operatorId);
                operatorSkillInfo.setSkillIndex(i + 1);
                if (skills.getJSONObject(i).get("skillId") instanceof String) {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.set("User-Agent", "PostmanRuntime/7.26.8");
                    httpHeaders.set("Authorization", "2");
                    httpHeaders.set("Host", "andata.somedata.top");
                    HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
                    //发送请求，封装结果数据
                    String skillName = new JSONObject(restTemplate
                            .exchange(skillIdUrl + skills.getJSONObject(i).getString("skillId") + ".json", HttpMethod.GET, httpEntity, String.class).getBody())
                            .getJSONArray("levels")
                            .getJSONObject(0)
                            .getString("name");
                    ;
                    operatorSkillInfo.setSkillName(skillName);
                    updateMapper.insertOperatorSkill(operatorSkillInfo);
                    log.info("更新{}干员{}技能{}基础信息", name, i + 1, skillName);
                    Integer skillId = updateMapper.selectSkillIdByName(skillName);
                    //获取技能等级列表(专一专二专三)
                    JSONArray levelUpCostCond = skills.getJSONObject(i).getJSONArray("levelUpCostCond");
                    //该技能专j+1的花费
                    for (int j = 0; j < levelUpCostCond.length(); j++) {
                        JSONObject skillCostObj = levelUpCostCond.getJSONObject(j);
                        if (skillCostObj.get("levelUpCost") instanceof JSONArray) {
                            JSONArray levelUpCost = skillCostObj.getJSONArray("levelUpCost");
                            for (int k = 0; k < levelUpCost.length(); k++) {
                                SkillMaterInfo skillMaterInfo = new SkillMaterInfo();
                                skillMaterInfo.setSkillId(skillId);
                                skillMaterInfo.setMaterLevel(j + 1);
                                skillMaterInfo.setUseMaterialId(levelUpCost.getJSONObject(k).getInt("id"));
                                skillMaterInfo.setUseNumber(levelUpCost.getJSONObject(k).getInt("count"));
                                updateMapper.insertSkillMater(skillMaterInfo);
                            }
                        }
                        log.info("更新{}干员{}技能专精{}信息", name, i + 1, j + 1);
                    }
                }
            }
        }
        return 1;
    }
}
