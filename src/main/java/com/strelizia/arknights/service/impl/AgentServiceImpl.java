package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.AgentMapper;
import com.strelizia.arknights.dao.UserFoundMapper;
import com.strelizia.arknights.model.AgentInfo;
import com.strelizia.arknights.model.UserFoundInfo;
import com.strelizia.arknights.util.FormatStringUtil;
import com.strelizia.arknights.util.FoundAgent;
import com.strelizia.arknights.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Random;

/**
 * @author wangzy
 * @Date 2020/12/7 14:35
 **/
@Service
@Slf4j
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentMapper agentMapper;
    @Autowired
    private UserFoundMapper userFoundMapper;

    @Value("${userConfig.limit}")
    private Integer limit;

    @Override
    public String chouKa(String pool,Long qq,String name) {
        return name + "抽取" + foundLimit(1,pool,qq);
    }

    @Override
    public String shiLian(String pool,Long qq,String name) {
        return name + "抽取" + foundLimit(10,pool,qq);
    }

    @Override
    public String selectPool() {
        List<String> poolList = agentMapper.selectPool();
        String s = "";
        for (String line : poolList) {
            s = s + "\n" + line;
        }
        //去掉头部换行
        return s.substring(1);
    }

    /**
     * 限制每日的抽卡次数
     * @param count
     * @param pool
     * @param qq
     * @return
     */
    public String foundLimit(int count,String pool,Long qq){
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        UserFoundInfo userFoundInfo = userFoundMapper.selectUserFoundByQQ(qqMd5);
        if (userFoundInfo == null){
            userFoundInfo = new UserFoundInfo();
            //MD5加密
            userFoundInfo.setQq(qqMd5);
            userFoundInfo.setFoundCount(0);
            userFoundInfo.setTodayCount(0);
        }
        //去数据库中查询这个人的垫刀数
        Integer sum = userFoundInfo.getFoundCount();
        //今日抽卡数
        Integer today = userFoundInfo.getTodayCount();
        String UserQq = userFoundInfo.getQq();
        String s = "今日抽卡机会无了";
        //管理员账户1111和我无限抽
        if (today<limit||UserQq.equals(DigestUtils.md5DigestAsHex("1111".getBytes()))||UserQq.equals(DigestUtils.md5DigestAsHex("412459523".getBytes()))) {
            s = FoundAgentByNum(count, pool, qq, sum);
        }
        return s;
    }

    /**
     * 抽卡通用方法
     * @param count 抽几张卡
     * @param pool  卡池名称
     * @param qq    抽取人qq
     * @return
     */
    public String FoundAgentByNum(int count,String pool,Long qq,Integer sum){
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        //如果没输入卡池名或者卡池不存在
        if (pool==null||agentMapper.selectAgentByStar(pool,6).size()==0){
            pool = "常规";
        }
        String s = "";
        //循环抽卡
        for(int j = 0; j < count; j++) {
            //获取干员稀有度
            int star = FoundAgent.FoundOneByMath(qq,sum);
            if(star == 6){
                //抽到六星垫刀归零
                userFoundMapper.updateUserFoundByQQ(qqMd5, 0);
            }else {
                //没有六星垫刀+1
                userFoundMapper.updateUserFoundByQQ(qqMd5, sum++);
            }
            //保存结果集
            List<AgentInfo> agentList;
            //使用不同的方法Math/Random进行随机运算，尽可能取消同一时间戳导致的相同随机数(虽然两个算法本质一样，这样做基本屁用没有)
            double r = Math.random();
            if (r <= 0.5) {
                //获取当前卡池三星/四星/五星/六星列表
                agentList = agentMapper.selectAgentByStar(pool, star);
            }else {
                agentList = agentMapper.selectAgentByStar("常规", star);
            }
            //有可能三星还去up池里找，因为三星不存在up所以报空，重新去常规池里找
            if (agentList.size() == 0){
                agentList = agentMapper.selectAgentByStar("常规", star);
            }
            //随机数种子采用纳秒数+毫秒/qq，尽可能减少时间戳导致的不随机
            Random random = new Random(System.nanoTime() +  System.currentTimeMillis() / qq);
            int i = random.nextInt(agentList.size());
            String levelStar;
            switch (star){
                case 6:
                    levelStar = "★★★★★★";
                    break;
                case 5:
                    levelStar = "★★★★★";
                    break;
                case 4:
                    levelStar = "☆☆☆☆";
                    break;
                case 3:
                    levelStar = "☆☆☆";
                    break;
                default:
                    levelStar = "";
            }
            try {
                s = s + " " + FormatStringUtil.strAppendStr(agentList.get(i).getName(),10,"-") + levelStar + "\n";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pool+"池：\n"+s;
    }
}
