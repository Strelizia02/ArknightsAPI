package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.AdminUserMapper;
import com.strelizia.arknights.dao.AgentMapper;
import com.strelizia.arknights.dao.OperatorInfoMapper;
import com.strelizia.arknights.dao.UserFoundMapper;
import com.strelizia.arknights.model.AdminUserInfo;
import com.strelizia.arknights.model.AgentInfo;
import com.strelizia.arknights.model.UserFoundInfo;
import com.strelizia.arknights.service.GroupAdminInfoService;
import com.strelizia.arknights.util.*;
import com.strelizia.arknights.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import java.util.Random;


import static com.strelizia.arknights.util.ImageUtil.replaceEnter;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

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
    @Autowired
    private SendMsgUtil sendMsgUtil;
    @Autowired
    private OperatorInfoMapper operatorInfoMapper;


    @Override
    public String chouKa(String pool, Long qq, String name, Long groupId) {
        return "[ATUSER(" + qq + ")]" + name + "\n抽取" + foundLimit(1, pool, qq, name, groupId);
    }

    @Override
    public String shiLian(String pool, Long qq, String name, Long groupId) {
        return "[ATUSER(" + qq + ")]" + name + "\n抽取" + foundLimit(10, pool, qq, name, groupId);
    }

    @Override
    public String XunFang(String pool, Long qq, String name, Long groupId) {
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        UserFoundInfo userFoundInfo = userFoundMapper.selectUserFoundByQQ(qqMd5);
        Integer limit = groupAdminInfoService.getGroupFoundAdmin(groupId);
        if (userFoundInfo == null) {
            userFoundInfo = new UserFoundInfo();
            //MD5加密
            userFoundInfo.setQq(qqMd5);
            userFoundInfo.setFoundCount(0);
            userFoundInfo.setTodayCount(0);
        }
        Integer search = userFoundMapper.selectTodaySearchByQQ(qqMd5);
        if (search == null) {
            search = 0;
        }
        //去数据库中查询这个人的垫刀数
        Integer sum = userFoundInfo.getFoundCount();
        //今日抽卡数
        Integer today = userFoundInfo.getTodayCount();
        List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
        boolean b = AdminUtil.getFoundAdmin(qqMd5, admins);

        if ((search < 2 && today < limit) || b) {
            //如果没输入卡池名或者卡池不存在
            if (pool == null || agentMapper.selectPoolIsExit(pool).size() == 0) {
                pool = "常规";
            }
            String s = FoundAgentByNum(10, pool, qq, sum, name, groupId);
            s = s.replace(" ", "");

            //干员立绘绘制的序号
            int No = 0;

            //创建图片画布
            BufferedImage image = new BufferedImage(960, 450,
                    BufferedImage.TYPE_INT_BGR);
            Graphics g = image.getGraphics();
            // 画出抽卡背景
            g.drawImage(ImageUtil.Base64ToImageBuffer(operatorInfoMapper.selectOperatorPngById("background")), 0, 0, 960, 450, null);

            String[] agents = s.split("\n");
            for (String agent : agents) {
                String[] split = agent.split("\t");
                String agentName = split[0];

                // 画出角色背景颜色
                int star = split[1].length();
                g.drawImage(ImageUtil.Base64ToImageBuffer(operatorInfoMapper.selectOperatorPngById("star" + star)), 70 + No * 82, 0, 82, 450, null);
                //画出干员立绘
                BufferedImage oldImg = ImageUtil.Base64ToImageBuffer(operatorInfoMapper.selectOperatorPngByName(agentName));
                if (oldImg != null) {
                    int width = 252 * oldImg.getWidth() / oldImg.getHeight();
                    int x = width / 2 - 41;
                    int y = 0;
                    int w = width;
                    int h = 252;
                    BufferedImage newImg = new BufferedImage(w, h, TYPE_INT_ARGB);
                    Graphics2D graphics = newImg.createGraphics();
                    graphics.drawImage(oldImg, 0, 0, w, h, null);
                    graphics.dispose();
                    //裁剪立绘
                    BufferedImage charBase = newImg.getSubimage(x, y, 82, 252);

                    g.drawImage(charBase, 70 + No * 82, 110, 82, 252, null);
                }

                // 画出角色职业图标
                Integer classId = operatorInfoMapper.selectOperatorClassByName(agentName);
                BufferedImage bImage = ImageUtil.Base64ToImageBuffer(operatorInfoMapper.selectOperatorPngById("" + classId));

//                //获取图片的长宽高
//                int width = bImage.getWidth();
//                int height = bImage.getHeight();
//                int minx = bImage.getMinTileX();
//                int miny = bImage.getMinTileY();
//                //遍历图片的所有像素点，并对各个像素点进行判断，是否修改
//                for (int i = minx; i < width; i++) {
//                    for (int j = miny; j < height; j++) {
//                        int pixel = bImage.getRGB(i, j);
//                        bImage.setRGB(i, j, 255 - pixel);
//                    }
//                }

                g.drawImage(bImage, 81 + No * 82, 320, 60, 60, null);
                No++;
            }
            g.setFont(new Font("楷体", Font.BOLD, 20));
            g.setColor(Color.WHITE);
            g.drawString("结果仅供参考，详细代码请见：", 470, 420);
            g.drawString("http://www.angelina-bot.top/", 470, 440);
            g.dispose();
            sendMsgUtil.CallOPQApiSendImg(groupId, "[ATUSER(" + qq + ")]", SendMsgUtil.picBase64Buf,
                    replaceEnter(new BASE64Encoder().encode(TextToImage.imageToBytes(image))), 2);
//            File outputfile = new File("D://image.png");
//
//            try {
//                ImageIO.write(image, "png", outputfile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            userFoundMapper.updateTodaySearch(qqMd5, name, groupId);
            return "";
        }else if (today >= limit && search < 2){
            return "您今日的图片寻访次数已用完，请使用[##十连]进行文字抽卡";
        }else {
            return "今日抽卡机会无了";
        }
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
        Integer allCount = 0;
        Integer allSix = 0;
        Integer todayFive = 0;
        if (userFoundInfo != null) {
            foundCount = userFoundInfo.getFoundCount();
            todayCount = userFoundInfo.getTodayCount();
            allCount = userFoundInfo.getAllCount();
            allSix = userFoundInfo.getAllSix();
            todayFive = userFoundInfo.getTodayFive();
        }
        int sixStar;
        if (foundCount > 50) {
            sixStar = 2 + (foundCount - 50) * 2;
        } else {
            //六星概率默认2%
            sixStar = 2;
        }
        return "[ATUSER(" + qq + ")]" + name + "的当前垫刀数为：" + foundCount + "\n当前六星概率为："
                + sixStar + "%" + "\n今日已抽卡：" + todayCount
                + "次\n累计共抽取了：" + allCount + "次\n累计获得了" + allSix + "个六星和" + todayFive + "个五星干员";
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
            //如果没输入卡池名或者卡池不存在
            if (pool == null || agentMapper.selectPoolIsExit(pool).size() == 0) {
                pool = "常规";
            }
            s = pool + "池：\n" + FoundAgentByNum(count, pool, qq, sum, name, groupId);
        }
        return "[ATUSER(" + qq + ")]" + s;
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
            //是不是限定池 0->普通 1->周年限定 2->联动限定 3->5倍权值 4->新年限定
            int limit = agentMapper.selectPoolLimit(pool);
            int integers = limit == 0 ? 0 : 1;
            if (star == 6) {
                if (r <= 0.5 + 0.2 * integers) {
                    //获取当前卡池三星/四星/五星/六星列表
                    agentList = agentMapper.selectAgentByStar(pool, star);
                } else {
                    agentList = agentMapper.selectAgentByStar("常规", star);
                    if (limit == 1 || limit == 4) {
                        //如果是限定池，就再加上前期可歪的限定干员
                        agentList.addAll(agentMapper.selectLimitAgent(limit));
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
        return s.toString();
    }
}
