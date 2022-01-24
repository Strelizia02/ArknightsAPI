package com.strelizia.arknights.controller;

import com.strelizia.arknights.dao.ActivityMapper;
import com.strelizia.arknights.dao.AdminUserMapper;
import com.strelizia.arknights.dao.ModelCountMapper;
import com.strelizia.arknights.model.AdminUserInfo;
import com.strelizia.arknights.model.ClassificationEnum;
import com.strelizia.arknights.model.MessageInfo;
import com.strelizia.arknights.model.ModelCountInfo;
import com.strelizia.arknights.service.*;
import com.strelizia.arknights.util.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author wangzy
 * @Date 2020/11/20 17:02
 * 通用的群聊回复接口
 **/
@RequestMapping("Arknights")
@RestController
@Slf4j
public class ArknightsController {

    @Value("${userConfig.loginQq}")
    private Long loginQq;

    @Autowired
    private AgentService agentService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private TagsfFoundService tagsfFoundService;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Autowired
    private SeTuService seTuService;

    @Autowired
    private BiliListeningService biliListeningService;

    @Autowired
    private EnemyService enemyService;

    @Autowired
    private ExecuteSqlService executeSqlService;

    @Autowired
    private OperatorInfoService operatorInfoService;

    @Autowired
    private BuildingSkillService buildingSkillService;

    @Autowired
    private SkillDescService skillDescService;

    @Autowired
    private SkinInfoService skinInfoService;

    @Autowired
    private ModelCountMapper modelCountMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private UpdateDataService updateDataService;

    @Autowired
    private PetPetService petPetService;

    @Autowired
    private EquipService equipService;

    @Autowired
    private BeastUtil beastUtil;

    @Autowired
    private ActivityMapper activityMapper;

    private final Map<Long, List<Long>> qqMsgRateList = new HashMap<>();


    /**
     * 消息处理总控制器，用于接收消息，并处理分流到不同的service
     */
    @PostMapping("receive")
    public String receive(
            @RequestBody MessageInfo message
    ) {
        //获取发送消息的群友qq
        Long qq = message.getQq();
        //不处理自身发送的消息
        if (!qq.equals(loginQq)) {
            log.info("接受到消息:{}", message.getText());
            activityMapper.insertGetMsg();
            //获取群号、昵称、消息
            Long groupId = message.getGroupId();
            String name = message.getName();
            String text = message.getText();
            message = null;
            //这样开头的消息是图片消息
            if (text.startsWith("{\"Content")) {
                JSONObject jsonObj = new JSONObject(text);
                //提取图片消息中的文字部分，取关键字
                String keyword = jsonObj.getString("Content").split(" ")[0];
                //在json结构前添加关键字信息， 使用波浪线分隔，可以将图片内容和文字内容统一进行处理。
                text = keyword + "\001" + text;
            } else if (text.startsWith("{\"FileID")) {
                //上传文件返回文件的FileID，方便一键更新包
                JSONObject jsonObj = new JSONObject(text);
                String fileID = jsonObj.getString("FileID");
                log.info("获取到文件Id为{}", fileID.substring(1));
                sendMsgUtil.CallOPQApiSendMsg(groupId, fileID.substring(1), 2);
            } else if (text.startsWith("{\"GroupPic")) {
                //纯图片消息，只判断第一张
                JSONObject jsonObj = new JSONObject(text).getJSONArray("GroupPic").getJSONObject(0);
                String Url = jsonObj.getString("Url");
                if(isUrlGongZhao(Url)){
                    String result = tagsfFoundService.FoundAgentByJson(text);
                    if (result != null) {
                        sendMsgUtil.CallOPQApiSendImg(groupId, name, SendMsgUtil.picBase64Buf, result, 2);
                    }else {
                        sendMsgUtil.CallOPQApiSendMsg(groupId, "QAQ没有找到对应的稀有公招结果", 2);
                    }
                }
            } else {
                //split("~")，以防图片信息中多余的空格导致的json结构破坏
                text = text.replace(" ", "\001");
            }
            if (text.startsWith("##") || text.startsWith("洁哥") || text.startsWith("杰哥")) {
                //判断回复效率，以防和其他机器人互动死锁
                if (getMsgLimit(qq, groupId, name)) {
                String messages = text.substring(2);
                return queryKeyword(qq, groupId, name, messages);
                }
            } else if (text.startsWith("安洁莉娜")) {
                //判断回复效率，以防和其他机器人互动死锁
                if (getMsgLimit(qq, groupId, name)) {
                    String messages = text.substring(4);
                    return queryKeyword(qq, groupId, name, messages);
                }
            } else if (text.contains("洁哥") || text.contains("杰哥") || text.contains("安洁莉娜")) {
                //判断回复效率，以防和其他机器人互动死锁
                if (getMsgLimit(qq, groupId, name)) {
                    talkWith(text, groupId, name, qq);
                }
            }
        }
        return null;
    }

    //闲聊
    private String talkWith(String text, Long groupId, String name, Long qq) {
        String result = "";
        if (text.contains("爱")){
            String[] replace = {
                    name + "看到那颗孤零零的星星了吗？据说总有一天，它会等来自己的伴星，拥抱，起舞......但，那要等上多久？我又要等上多久，星星......才会明白呢？",
                    name + "别害羞，再靠近点！对对，sorridi~！看，是我和博士的大头贴哦~哼哼，就当做是纪念品吧。当然，这上面确实也有我的......一点点寄托呢。",
                    name + "有没有感觉身体变轻呢？"
            };
            result = replace[new Random().nextInt(replace.length)];
        }else if (text.contains("早上好") || text.contains("早安")) {
            String[] replace = {
                    "早安" + name + "！嗯是的，我要去送些文件什么的。虽然凯尔希医生说过我的能力在战场上很有用，但比起战斗，我更喜欢送信呢！",
                    "早安，" + name
            };
            result = replace[new Random().nextInt(replace.length)];
        }else if (text.contains("晚上好") || text.contains("晚安")){
            result = "晚安，" + name;
        }else if (text.contains("亲亲")){
            String[] replace = {
                    "呜哇~不要这样啦",
                    "呜哇~",
                    "好，好吧，不要让别人看见哦~"
            };
            result = replace[new Random().nextInt(replace.length)];
        }else if (text.contains("抱抱")){
            String[] replace = {
                    "呜哇~不要这样啦",
                    "好，好吧，不要让别人看见哦~",
                    "想不想试试漂浮在空中的感觉？",
                    "轻轻地......嘿~"
            };
            result = replace[new Random().nextInt(replace.length)];
        }else if (text.contains("干嘛")){
            String[] replace = {
                    name + "都几岁了，还那么害羞，我看你是完全不懂喔",
                    name + "身材蛮不错喔，蛮结实的啊。"
            };
            result = replace[new Random().nextInt(replace.length)];
        }else if (text.contains("表白")||text.contains("告白")){
            String[] replace = {
                    "真，真的吗？",
                    "我的头发很漂亮？嗯哼哼，我可是有秘诀的！",
                    "嗯哼哼~♪唔，唇彩果然还是选珊瑚红色比较好吧？"
            };
            result = replace[new Random().nextInt(replace.length)];
        }else if (text.contains("不要")){
            String[] replace = {
                    name + "让我康康！"
            };
            result = replace[new Random().nextInt(replace.length)];
        }else if (text.contains("贴贴")){
            String[] replace = {
                    "博，博士，这样我会害羞的~",
                    "呜哇~不要这样啦",
                    "嗯哼哼~"
            };
            result = replace[new Random().nextInt(replace.length)];
        }else if (text.contains("口球") || text.contains("口我")){
            result = "";
            sendMsgUtil.CallOPQApiShutSomeOne(groupId, qq, new Random().nextInt(5) + 1);
        }
        if (!result.equals("")) {
            sendMsgUtil.CallOPQApiSendMsg(groupId, result, 2);
        }
        return "";
    }

    //消息分流方法，使用switch进行模式匹配，具体消息类型有枚举类。
    public String queryKeyword(Long qq, Long groupId, String name, String text) {
        String[] a = text.split("\001");
        /*
         * 用一个固定长度10的数组承接a的内容，防止数组溢出
         * 当需要数组内某个值的时候，选择s[i]，10位以内不会存在数组溢出
         * 当需要数组本身的时候选择a，原始长度数组
         */
        String[] s = new String[10];
        for (int i = 0; i < a.length; i++) {
            s[i] = a[i];
        }
        String result;
        ClassificationEnum c = ClassificationUtil.GetClass(s[0]);
        modelCountMapper.insertDuplicateCount(c.name());
        switch (c) {
            case CaiDan:
                result =
                        "0.详细菜单：{##详细菜单}查看完整菜单以及使用示例\n" +
                                "1.模拟寻访：{##十连 卡池名}或{##单抽 卡池名}或{##十连寻访 卡池名}\n" +
                                "2.卡池列表：{##卡池}\n" +
                                "3.卡池清单：{##卡池清单 卡池名}\n" +
                                "4.查询个人垫刀数{##垫刀查询}\n" +
                                "5.查询技能专精材料{##专精材料 干员名 第几技能 专精等级}或{##专精材料 技能名 专精等级}\n" +
                                "6.精英化材料查询：{##精一材料 干员名}或{精二材料 干员名}\n" +
                                "7.查询材料合成路线：{##合成路线 材料名}\n" +
                                "8.查询材料掉落关卡：{##材料获取 材料名}\n" +
                                "9.公招结果查询：{##公招截图 [图片]}\n" +
                                "10.公开招募tag组合查询：{##公开招募 [tag1],[tag2]}\n" +
                                "11.涩图功能：{##涩图 [编号]} --涩图会导致洁哥封号，已停用\n" +
                                "12.上传涩图：{##给你涩图[图片]}或私聊{[图片]} --涩图会导致洁哥封号，已停用\n" +
                                "13.干员面板：{##干员面板 干员名}\n" +
                                "14.章节列表：{##章节列表}\n" +
                                "15.地图列表：{##地图列表 章节名}\n" +
                                "16.地图掉落：{##地图掉落 地图ID}\n" +
                                "17.动态查询：{##动态查询 用户昵称 编号}\n" +
                                "18.关注列表：{##关注列表}\n" +
                                "19.最新投稿：{##最新投稿 用户昵称}\n" +
                                "20.敌人清单：{##敌人清单 关键字}\n" +
                                "21.敌人信息：{##敌人信息 敌人名称}\n" +
                                "22.干员列表：{##干员列表 [条件1] [条件2]}\n" +
                                "23.干员档案：{##干员档案 干员名 [档案名]}\n" +
                                "24.声优列表：{##声优列表 关键字}\n" +
                                "24.画师列表：{##画师列表 关键字}\n" +
                                "25.基建技能：{##基建技能 干员名}\n" +
                                "26.技能详情：{##技能详情 干员名/技能名 [等级]}\n" +
                                "27.删除涩图：{##删除涩图 [编号]} --涩图会导致洁哥封号，已停用\n" +
                                "28.打开涩图：{##打开涩图} --涩图会导致洁哥封号，已停用\n" +
                                "29.关闭涩图：{##关闭涩图} --涩图会导致洁哥封号，已停用\n" +
                                "30.时装查询：{##皮肤查询 [条件]}\n" +
                                "31.天赋查询：{##干员天赋 干员名}\n" +
                                "32.数据更新：{##更新}\n" +
                                "33.图标更新：{##图标更新}\n" +
                                "34.材料更新：{##材料更新}\n" +
                                "35.摸头动图：{##摸头}\n" +
                                "36.源码查询：{##源码}\n" +
                                "37.兽音解密：{##兽音解密}\n" +
                                "38.兽音加密：{##兽音加密}\n" +
                                "39.模组查询：{##模组查询 [干员名]}" +
                                "过大的涩图将导致回复缓慢，请不要上传不能过审的图片";
                break;
            case XiangXiCaiDan:
                result =
                        "1.模拟寻访：\n" +
                                "\t使用方法：输入{##十连 卡池名}或{##单抽 卡池名}\n" +
                                "\t例1：##十连 无拘熔火\n" +
                                "\t例2：##十连寻访\n" +
                                "\t注：十连寻访为生成游戏内抽卡截图，每日限定两次\n" +
                                "2.卡池列表：\n" +
                                "\t使用方法：输入{##卡池}\n" +
                                "3.卡池清单：\n" +
                                "\t使用方法：输入{##卡池清单 卡池名}\n" +
                                "4.查询个人垫刀数、今日抽卡数、当前六星概率：\n" +
                                "\t使用方法：输入{##垫刀查询}\n" +
                                "5.查询技能专精材料：\n" +
                                "\t使用方法：输入{##专精材料 干员名 第几技能 专精等级}或{##专精材料 技能名 专精等级}\n" +
                                "\t例1：##专精材料 艾雅法拉 3 3\n" +
                                "\t例2：##专精材料 艾雅法拉 三技能 专三\n" +
                                "\t例3：##专精材料 火山 3\n" +
                                "6.精英化材料查询：\n" +
                                "\t使用方法：输入{##精一材料 干员名}或{精二材料 干员名}\n" +
                                "\t例1：##精一材料 克洛丝\n" +
                                "\t例2：##精二材料 陈\n" +
                                "7.查询材料合成路线：\n" +
                                "\t使用方法：输入{##合成路线 材料名}\n" +
                                "\t例：##合成路线 D32钢\n" +
                                "8.查询材料掉落关卡：\n" +
                                "\t使用方法：输入{##材料获取 材料名}\n" +
                                "\t例1：##材料获取 研磨石\n" +
                                "\t例2：##材料获取 研磨石-all\n" +
                                "9.公招结果查询：\n" +
                                "\t使用方法：输入{##公招截图 [图片]}，自动识图并返回结果\n" +
                                "10.公开招募tag组合查询：\n" +
                                "\t使用方法：输入{##公开招募 [tag1],[tag2]}\n" +
                                "\t例：##公开招募 爆发,近战位,高级资深干员\n" +
                                "11.涩图功能： --涩图会导致洁哥封号，已停用\n" +
                                "\t使用方法：输入{##涩图}\n" +
                                "\t例：##涩图 1\n" +
                                "\t由于涩图大小原因，回复会产生一定的延迟，若涩图无响应请等待至少10秒钟再次请求\n" +
                                "12.上传涩图： --涩图会导致洁哥封号，已停用\n" +
                                "\t使用方法：输入{##给你涩图[图片]}或纯图片私聊{[图片]}\n" +
                                "\t上传涩图需要权限，请不要上传过大的涩图，更不要上传不能过审的涩图\n" +
                                "13.干员面板：\n" +
                                "\t使用方法：输入{##干员面板 干员名}\n" +
                                "\t例：##干员面板 伊芙利特\n" +
                                "14.章节列表：\n" +
                                "\t使用方法：输入{##章节列表}\n" +
                                "15.地图列表：\n" +
                                "\t使用方法：{##地图列表 章节名}\n" +
                                "\t例：##地图列表 骑兵与猎人\n" +
                                "16.地图掉落：\n" +
                                "\t使用方法：{##地图掉落 地图ID}\n" +
                                "\t例：##地图掉落 1-7\n" +
                                "17.动态查询：\n" +
                                "\t使用方法：{##动态查询 用户昵称 编号}或{##最新动态 用户昵称}\n" +
                                "\t例1：##动态查询 明日方舟 1\n" +
                                "\t例2：##最新动态 明日方舟\n" +
                                "18.关注列表：\n" +
                                "\t使用方法：{##关注列表}\n" +
                                "19.最新投稿：\n" +
                                "\t使用方法：{##最新投稿 用户昵称}\n" +
                                "20.敌人清单：\n" +
                                "\t使用方法：{##敌人清单}或{##敌人清单 关键字}\n" +
                                "21.敌人信息：\n" +
                                "\t使用方法：{##敌人信息 敌人名称}\n" +
                                "22.干员列表：\n" +
                                "\t使用方法：{##干员列表 [条件1] [条件2]}\n" +
                                "\t例1：##干员列表\n" +
                                "\t例2：##干员列表 女 瓦伊凡\n" +
                                "23.干员档案：\n" +
                                "\t使用方法：{##干员档案 干员名 [档案名]}\n" +
                                "\t例1：##干员档案 风笛\n" +
                                "\t例2：##干员档案 安洁莉娜 基础档案\n" +
                                "24.声优列表：\n" +
                                "\t使用方法：{##声优列表 关键字}\n" +
                                "24.画师列表：\n" +
                                "\t使用方法：{##画师列表 关键字}\n" +
                                "25.基建技能：\n" +
                                "\t使用方法：{##基建技能 干员名}\n" +
                                "\t例1：##基建技能 玫兰莎\n" +
                                "\t例2：##基建技能 玫兰莎 制造站\n" +
                                "26.技能详情：\n" +
                                "\t使用方法：{##技能详情 干员名/技能名 [等级]}\n" +
                                "\t注：使用干员名查询会返回该干员所有技能\n" +
                                "\t等级可不填，也可3-10这样给一个范围，不填默认返回专一、专二、专三等级\n" +
                                "\t例1：##技能详情 银灰\n" +
                                "\t例2：##技能详情 真银斩 10\n" +
                                "\t例3：##技能详情 艾雅法拉 7-专三\n" +
                                "27.删除涩图： --涩图会导致洁哥封号，已停用\n" +
                                "\t使用方法：{##删除涩图 [编号]}\n" +
                                "\t注：删除涩图需要找开发者py权限\n" +
                                "28.打开涩图： --涩图会导致洁哥封号，已停用\n" +
                                "\t使用方法：{##打开涩图}\n" +
                                "\t注：打开涩图功能需要找开发者py权限\n" +
                                "29.关闭涩图： --涩图会导致洁哥封号，已停用\n" +
                                "\t使用方法：{##关闭涩图}\n" +
                                "\t注：关闭涩图功能需要找开发者py权限\n" +
                                "30.时装查询：\n" +
                                "\t使用方法：{##皮肤查询 [条件]}\n" +
                                "\t例1：{##皮肤查询 艾雅法拉}\n" +
                                "\t例2：{##时装查询 珊瑚海岸}\n" +
                                "31.天赋查询：\n" +
                                "\t使用方法：{##干员天赋 干员名}\n" +
                                "\t例：##干员天赋 塞雷娅\n" +
                                "32.数据更新：\n" +
                                "\t使用方法：{##更新}\n" +
                                "33.图标更新：\n" +
                                "\t使用方法：{##图标更新}\n" +
                                "34.材料更新：\n" +
                                "\t使用方法：{##材料更新}\n" +
                                "35.摸头动图：\n" +
                                "\t使用方法：{##摸头}\n" +
                                "36.源码查询：\n" +
                                "\t使用方法：{##源码}\n" +
                                "37.兽音解密：\n" +
                                "\t使用方法：{##兽音解密 [密文]}\n" +
                                "\t或：{##兽音翻译 [密文]}\n" +
                                "38.兽音加密：\n" +
                                "\t使用方法：{##兽音加密 [文本]}\n" +
                                "\t或：{##嗷呜 [文本]}\n" +
                                "39.模组查询：\n" +
                                "\t使用方法：{##模组查询 [干员名]}\n" +
                                "\t例：{##模组查询 麦哲伦}\n" +
                                "注：本项目需严格按照格式输入，自然语言处理功能将在后期优化";
                break;
            case ShiLian:
                result = agentService.shiLian(s[1], qq, name, groupId);
                break;
            case ChouKa:
                result = agentService.chouKa(s[1], qq, name, groupId);
                break;
            case KaChi:
                result = agentService.selectPool();
                break;
            case KaChiQingdan:
                result = agentService.selectPoolAgent(s[1]);
                break;
            case DianDaoChaXun:
                result = agentService.selectFoundCount(qq, name);
                break;
            case ZhuanJingCaiLiao:
                result = materialService.ZhuanJingCaiLiao(qq, groupId, a);
                break;
            case JingYiCaiLiao:
                result = materialService.JingYingHuaCaiLiao(qq, groupId, s[1], 1);
                break;
            case JingErCaiLiao:
                result = materialService.JingYingHuaCaiLiao(qq, groupId, s[1], 2);
                break;
            case HeChengLuXian:
                result = materialService.HeChengLuXian(qq, groupId, s[1]);
                break;
            case CaiLiaoHuoQu:
                result = materialService.HuoQuTuJing(qq, s[1]);
                break;
            case GongZhaoJieTu:
                result = "";
                String base64 = tagsfFoundService.FoundAgentByJson(s[1]);
                if (base64 != null) {
                    sendMsgUtil.CallOPQApiSendImg(groupId, name, SendMsgUtil.picBase64Buf, base64, 2);
                }else {
                    result = "QAQ没有找到对应的稀有公招结果";
                }
                break;
            case GongKaiZhaoMu:
                result = "";
                String base641 = tagsfFoundService.FoundAgentByArray(s[1].split("[,，]"));
                if (base641 != null) {
                    sendMsgUtil.CallOPQApiSendImg(groupId, name, SendMsgUtil.picBase64Buf, base641, 2);
                }else {
                    result = "QAQ没有找到对应的稀有公招结果";
                }
                break;
            case SeTu:
                result = seTuService.sendImageByType(qq, groupId, 1, name, s[1]);
                break;
            case GeiNiSeTu:
                result = seTuService.getImageIntoDb(s[1], 1, name, qq);
                break;
            case XiTongXinXi:
                result = new ServerSystemUtil().getSystemInfo();
                break;
            case GanYuanMianBan:
                result = materialService.selectAgentData(s[1]);
                break;
            case DiTuDiaoLuo:
                result = materialService.selectMaterByMap(qq, groupId, s[1]);
                break;
            case ZhangJieLieBiao:
                result = materialService.selectZoneList(qq);
                break;
            case DiTuLieBiao:
                result = materialService.selectMapList(qq, s[1]);
                break;
            case DongTaiChaXun:
                biliListeningService.getDynamic(qq, groupId, s[1], Integer.parseInt(s[2]));
                result = "";
                break;
            case GuanZhuLieBiao:
                result = biliListeningService.getBiliList(groupId);
                break;
            case ZuiXinTouGao:
                result = biliListeningService.getVideo(qq, s[1]);
                break;
            case ZuiXinDongTai:
                biliListeningService.getDynamic(qq, groupId, s[1], 1);
                result = "";
                break;
            case DiRenXinXi:
                result = enemyService.getEnemyInfoByName(qq, s[1]);
                break;
            case DiRenQuanMing:
                result = enemyService.getEnemyListByName(qq, s[1]);
                break;
            case SQL:
                result = executeSqlService.ExecuteSql(qq, text);
                break;
            case WeiBoZhengWen:
                result = "";
                break;
            case GanYuanLieBiao:
                result = operatorInfoService.getOperatorByInfos(qq, s);
                break;
            case GanYuanDangAn:
                result = operatorInfoService.getOperatorInfo(qq, s[1], s[2]);
                break;
            case HuaShiLieBiao:
                result = operatorInfoService.getDrawByName(qq, s[1]);
                break;
            case ShengYouLieBiao:
                result = operatorInfoService.getCVByName(qq, s[1]);
                break;
            case QunFaXiaoXi:
                result = executeSqlService.sendGroupMessage(qq, text);
                break;
            case JiJianJiNeng:
                result = buildingSkillService.getBuildSkillNameServiceByInfos(qq, s);
                break;
            case JiNengXiangQing:
                result = skillDescService.getSkillDescByInfo(qq, s);
                break;
            case ShanChuSeTu:
                result = seTuService.deleteSeTuById(qq, groupId, Integer.parseInt(s[1]));
                break;
            case DaKaiSeTu:
                result = seTuService.changePictureStat(qq, groupId, 5);
                break;
            case GuanBiSeTu:
                result = seTuService.changePictureStat(qq, groupId, 0);
                break;
            case PiFuChaXun:
                result = skinInfoService.getOperatorSkinByInfo(qq, groupId, s[1]);
                break;
            case TianFuChaXun:
                result = operatorInfoService.getTalentByName(qq, s[1]);
                break;
            case GongNengTongJi:
                StringBuilder sb = new StringBuilder("[ATUSER(" + qq + ")]全局功能使用次数为：\n");
                for (ModelCountInfo m : modelCountMapper.selectModelCount()) {
                    sb.append(m.toString()).append("\n");
                }
                result = sb.toString();
                break;
            case ShiLianXunFang:
                result = agentService.XunFang(s[1], qq, name, groupId);
                break;
            case QuanBuGengXin: {
                List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
                String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
                if (!AdminUtil.getSqlAdmin(qqMd5, admins)) {
                    result = "您无更新权限";
                } else {
                    result = "";
                    updateDataService.updateAllData(false);
                    updateDataService.updateSkin();
                }
                break;
            }
            case TuBiaoGengXin: {
                List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
                String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
                if (!AdminUtil.getSqlAdmin(qqMd5, admins)) {
                    result = "您无更新权限";
                } else {
                    result = "";
                    updateDataService.updateSkin();
                }
                break;
            }
            case CaiLiaoGengXin:{
                List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
                String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
                if (!AdminUtil.getSqlAdmin(qqMd5, admins)) {
                    result = "您无更新权限";
                } else {
                    result = "";
                    updateDataService.updateItemAndFormula();
                }
                break;
            }
            case MoTou:
                petPetService.PetPet(groupId, qq, null);
                result = "";
                break;
            case YuanMa:
                result = "源码地址：https://github.com/Strelizia02/ArknightsAPI/\n" +
                        "教学视频：https://www.bilibili.com/video/BV1hw411f7a4/\n" +
                        "洁哥官网：https://www.angelina-bot.top/（开发中）\n" +
                        "喜欢安洁莉娜的博士麻烦在github点一个star，或者给视频点赞，感谢！";
                break;
            case ShouYuFanYi:
                String str = s[1];
                if (str.startsWith("{\"Content")){
                    JSONObject json = new JSONObject(str);
                    str = json.getString("Content").split(" ")[1];
                }

                result = "[ATUSER(" + qq + ")]" + beastUtil.FromBeast(str);
                break;
            case ShouYuJiaMi:
                result = "[ATUSER(" + qq + ")]" + beastUtil.ToBeast(s[1]);
                break;
            case MoZuChaXun:
                result = equipService.getOperatorEquip(qq, groupId, s[1]);
                break;
            case GuanZhu:
                result = biliListeningService.setGroupBiliRel(qq, groupId, s[1]);
                break;
            case QuXiaoGuanZhu:
                result = biliListeningService.removeGroupBiliRel(qq, groupId, s[1]);
                break;
            case XianLiao:
                result = talkWith(text, groupId, name, qq);
                break;
            default:
                result = "";
        }
        //返回空字符串则不发送信息
        if (!result.equals("")) {
            sendMsgUtil.CallOPQApiSendMsg(groupId, result, 2);
        }
        return result;
    }

    //消息回复限速机制
    private boolean getMsgLimit(Long qq, Long groupId, String name){
        boolean flag = true;
        //每10秒限制三条消息,10秒内超过5条就不再提示
        int length = 3;
        int maxTips = 5;
        int second = 10;
        if (!qqMsgRateList.containsKey(qq)){
            List<Long> msgList = new ArrayList<>(maxTips);
            msgList.add(System.currentTimeMillis());
            qqMsgRateList.put(qq, msgList);
        }
        List<Long> limit = qqMsgRateList.get(qq);
        if (limit.size() <= length) {
            //队列未满三条，直接返回消息
            limit.add(System.currentTimeMillis());
        }else {
            if (getSecondDiff(limit.get(0), second)){
                //队列长度超过三条，但是距离首条消息已经大于10秒
                limit.remove(0);
                //把后面两次提示的时间戳删掉
                while (limit.size() > 3){
                    limit.remove(3);
                }
                limit.add(System.currentTimeMillis());
            }else {
                if (limit.size() <= maxTips){
                    //队列长度在3~5之间，并且距离首条消息不足10秒，发出提示
                    log.warn("{}超出单人回复速率,{}", name, limit.size());
                    sendMsgUtil.CallOPQApiSendMsg(groupId, name + "说话太快了，请稍后再试", 2);
                    limit.add(System.currentTimeMillis());
                }else {
                    //队列长度等于5，直接忽略消息
                    log.warn("{}连续请求,已拒绝消息", name);
                }
                flag = false;
            }
        }
        //对队列进行垃圾回收
        gcMsgLimitRate();

        return flag;
    }

    public boolean getSecondDiff(Long timestamp, int second){
        return (System.currentTimeMillis() - timestamp) / 1000 > second;
    }

    public void gcMsgLimitRate() {
        //大于2048个队列的时候进行垃圾回收,大概占用24k
        if (qqMsgRateList.size() > 256){
            log.warn("开始对消息速率队列进行回收，当前map长度为：{}", qqMsgRateList.size());
            //回收所有超过30秒的会话
            qqMsgRateList.entrySet().removeIf(entry -> getSecondDiff(entry.getValue().get(0), 30));
            log.info("消息速率队列回收结束，当前map长度为：{}", qqMsgRateList.size());
        }
    }

    private boolean isUrlGongZhao(String url){
        String dHash = DHashUtil.getDHash(url);
        return DHashUtil.getHammingDistance(dHash,
                "0001111110100110001111010010001100100011001001110010011100101101") < 5 ||
                DHashUtil.getHammingDistance(dHash,
                "0001111101100111001101010110001101110011001001110010011100101111") < 5;
    }
}
