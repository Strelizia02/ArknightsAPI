package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.AdminUserMapper;
import com.strelizia.arknights.dao.AgentMapper;
import com.strelizia.arknights.dao.UserFoundMapper;
import com.strelizia.arknights.model.AdminUserInfo;
import com.strelizia.arknights.model.AgentInfo;
import com.strelizia.arknights.model.UserFoundInfo;
import com.strelizia.arknights.service.GroupAdminInfoService;
import com.strelizia.arknights.util.AdminUtil;
import com.strelizia.arknights.util.FormatStringUtil;
import com.strelizia.arknights.util.FoundAgentUtil;
import com.strelizia.arknights.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private AdminUserMapper adminUserMapper;
    @Autowired
    private GroupAdminInfoService groupAdminInfoService;


    @Override
    public String chouKa(String pool, Long qq, String name, Long groupId) {
        return name + "\n抽取" + foundLimit(1, pool, qq, name, groupId);
    }

    @Override
    public String shiLian(String pool, Long qq, String name, Long groupId) {
        return name + "\n抽取" + foundLimit(10, pool, qq, name, groupId);
    }

    @Override
    public String selectPool() {
        List<String> poolList = agentMapper.selectPool();
        StringBuilder str = new StringBuilder();
        for (String line : poolList) {
            str.append("\n").append(line);
        }
        //去掉头部换行
        return str.substring(1);
    }

    @Override
    public String selectFoundCount(Long qq, String name) {
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        UserFoundInfo userFoundInfo = userFoundMapper.selectUserFoundByQQ(qqMd5);
        Integer todayCount = 0;
        Integer foundCount = 0;
        if (userFoundInfo != null) {
            foundCount = userFoundInfo.getFoundCount();
            todayCount = userFoundInfo.getTodayCount();
        }
        int sixStar;
        if (foundCount > 50) {
            sixStar = 2 + (foundCount - 50) * 2;
        } else {
            //六星概率默认2%
            sixStar = 2;
        }
        return name + "的当前垫刀数为：" + foundCount + "\n当前六星概率为：" + sixStar + "%" + "\n今日已抽卡次数：" + todayCount;
    }

    @Override
    public String selectPoolAgent(String pool) {
        if (pool.equals("凭证兑换") || pool.equals("活动") || pool.equals("公招") || pool.equals("初始")) {
            return "没有找到该卡池哦";
        }
        List<AgentInfo> agents = agentMapper.selectPoolAgent(pool);
        StringBuilder s = new StringBuilder("卡池[" + pool + "]中概率up干员为：");
        for (AgentInfo agent : agents) {
            s.append("\n").append(agent.getName()).append(FormatStringUtil.FormatStar(agent.getStar()));
        }
        return s.toString();
    }

    /**
     * 限制每日的抽卡次数
     *
     * @param count
     * @param pool
     * @param qq
     * @return
     */
    public String foundLimit(int count, String pool, Long qq, String name, Long groupId) {
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        UserFoundInfo userFoundInfo = userFoundMapper.selectUserFoundByQQ(qqMd5);
        if (userFoundInfo == null) {
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
        String s = "今日抽卡机会无了";
        List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
        boolean b = AdminUtil.getFoundAdmin(qqMd5, admins);
        Integer limit = groupAdminInfoService.getGroupFoundAdmin(groupId);
        if (today < limit || b) {
            s = FoundAgentByNum(count, pool, qq, sum, name, groupId);
        }
        return s;
    }

    /**
     * 抽卡通用方法
     *
     * @param count 抽几张卡
     * @param pool  卡池名称
     * @param qq    抽取人qq
     * @return
     */
    public String FoundAgentByNum(int count, String pool, Long qq, Integer sum, String name, Long groupId) {
        List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        boolean b = AdminUtil.getSixAdmin(qqMd5, admins);
        //如果没输入卡池名或者卡池不存在
        if (pool == null || agentMapper.selectPoolIsExit(pool).size() == 0) {
            pool = "常规";
        }
        StringBuilder s = new StringBuilder();
        //循环抽卡
        for (int j = 0; j < count; j++) {
            if (b) {
                sum = 100;
            }
            //获取干员稀有度
            int star = FoundAgentUtil.FoundOneByMath(qq, sum);
            if (star == 6) {
                //抽到六星垫刀归零
                sum = 0;
                userFoundMapper.updateUserFoundByQQ(qqMd5, name, groupId, sum);
                userFoundMapper.updateSixByQq(qqMd5);
            } else if (star == 5) {
                //没有六星垫刀+1
                sum = sum + 1;
                userFoundMapper.updateUserFoundByQQ(qqMd5, name, groupId, sum);
                userFoundMapper.updateFiveByQq(qqMd5);
            } else {
                //没有六星垫刀+1
                sum = sum + 1;
                userFoundMapper.updateUserFoundByQQ(qqMd5, name, groupId, sum);
            }
            //保存结果集
            List<AgentInfo> agentList;
            //使用不同的方法Math/Random进行随机运算，尽可能取消同一时间戳导致的相同随机数(虽然两个算法本质一样，这样做基本屁用没有)
            double r = Math.random();
            //是不是限定池
            int integers = agentMapper.selectPoolLimit(pool)==0?0:1;
            if (star == 6) {
                if (r <= 0.5 + 0.2 * integers) {
                    //获取当前卡池三星/四星/五星/六星列表
                    agentList = agentMapper.selectAgentByStar(pool, star);
                } else {
                    agentList = agentMapper.selectAgentByStar("常规", star);
                    if (integers==1){
                        //如果是限定池，就再加上前期可歪的限定干员
                        agentList.addAll(agentMapper.selectLimitAgent());
                        //五倍权值（因为上面加过一个，所以再加四个就可以）
                        List<AgentInfo> fiveLimit = agentMapper.selectLimitAgentByPool(pool);
                        if (fiveLimit.size() > 0) {
                            for (int i = 0; i < 4; i++) {
                                agentList.addAll(fiveLimit);
                            }
                        }
                    }
                }
            } else if (star == 5) {
                if (r <= 0.5) {
                    //获取当前卡池三星/四星/五星/六星列表
                    agentList = agentMapper.selectAgentByStar(pool, star);
                } else {
                    agentList = agentMapper.selectAgentByStar("常规", star);
                }
            } else if (star == 4) {
                if (r <= 0.2) {
                    //获取当前卡池三星/四星/五星/六星列表
                    agentList = agentMapper.selectAgentByStar(pool, star);
                } else {
                    agentList = agentMapper.selectAgentByStar("常规", star);
                }
            } else {
                agentList = agentMapper.selectAgentByStar("常规", star);
            }
            //有可能三星还去up池里找，因为三星不存在up所以报空，重新去常规池里找
            if (agentList.size() == 0) {
                agentList = agentMapper.selectAgentByStar("常规", star);
            }
            //随机数种子采用纳秒数+毫秒/qq，尽可能减少时间戳导致的不随机
            Random random = new Random(System.nanoTime() + System.currentTimeMillis() / qq);
            int i = random.nextInt(agentList.size());
            String levelStar = FormatStringUtil.FormatStar(star);
            try {
                s.append(" ").append(agentList.get(i).getName()).append("\t").append(levelStar).append("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pool + "池：\n" + s;
    }
}
