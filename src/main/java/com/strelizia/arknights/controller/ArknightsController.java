package com.strelizia.arknights.controller;

import com.strelizia.arknights.model.ClassificationEnum;
import com.strelizia.arknights.model.MessageInfo;
import com.strelizia.arknights.service.*;
import com.strelizia.arknights.util.ClassificationUtil;
import com.strelizia.arknights.util.SendMsgUtil;
import com.strelizia.arknights.util.ServerSystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 消息处理总控制器，用于接收消息，并处理分流到不同的service
     */
    @PostMapping("receive")
    public String receive(
            @RequestBody MessageInfo message
    ){
        //获取发送消息的群友qq
        Long qq = message.getQq();
        //不处理自身发送的消息
        if (qq != loginQq) {
            log.info("接受到消息:{}",message.getText());
            //获取群号、昵称、消息
            Long groupId = message.getGroupId();
            String name = message.getName();
            String text = message.getText();
            //这样开头的消息是图片消息
            if (text.startsWith("{\"Content")) {
                JSONObject jsonObj = new JSONObject(text);
                //提取图片消息中的文字部分，取关键字
                String keyword = jsonObj.getString("Content").split(" ")[0];
                //在json结构前添加关键字信息， 使用波浪线分隔，可以将图片内容和文字内容统一进行处理。
                text = keyword + "~" + text;
            }else if (text.startsWith("{\"FileID")){
                JSONObject jsonObj = new JSONObject(text);
                String fileID = jsonObj.getString("FileID");
                log.info("获取到文件Id为{}",fileID);
                sendMsgUtil.CallOPQApiSendMsg(groupId, fileID,2);
            }else {
                //split("~")，以防图片信息中多余的空格导致的json结构破坏
                text = text.replace(" ","~");
            }
            //触发关键字是##，目前机器人还没名字，本来就是功能性的。
            if (text.startsWith("##")) {
                String messages = text.substring(2);
                String s = queryKeyword(qq, groupId, name, messages);
                return s;
            }
        }
        return null;
    }

    //消息分流方法，使用switch进行模式匹配，具体消息类型有枚举类。
    public String queryKeyword(Long qq, Long groupId,String name, String text){
        String[] a = text.split("~");
        /**
         * 用一个固定长度10的数组承接a的内容，防止数组溢出
         * 当需要数组内某个值的时候，选择s[i]，10位以内不会存在数组溢出
         * 当需要数组本身的时候选择a，原始长度数组
         */
        String[] s = new String[10];
        for (int i = 0; i < a.length; i++){
            s[i] = a[i];
        }
        String result;
        ClassificationEnum c = ClassificationUtil.GetClass(s[0]);
        switch (c){
            case CaiDan:
                result =
                        "0.详细菜单：{##详细菜单}查看完整菜单以及使用示例\n" +
                        "1.模拟寻访：{##十连 卡池名}或{##单抽 卡池名}\n" +
                        "2.卡池列表：{##卡池}\n" +
                        "3.卡池清单：{##卡池清单 卡池名}\n" +
                        "4.查询个人垫刀数{##垫刀查询}\n" +
                        "5.查询技能专精材料{##专精材料 干员名 第几技能 专精等级}或{##专精材料 技能名 专精等级}\n" +
                        "6.精英化材料查询：{##精一材料 干员名}或{精二材料 干员名}\n" +
                        "7.查询材料合成路线：{##合成路线 材料名}\n" +
                        "8.查询材料掉落关卡：{##材料获取 材料名}\n" +
                        "9.公招结果查询：{##公招截图 [图片]}\n" +
                        "10.公开招募tag组合查询：{##公开招募 [tag1],[tag2]}\n" +
                        "11.涩图功能：{##涩图}\n" +
                        "12.上传涩图：{##给你涩图[图片]}或私聊{[图片]}\n" +
                        "13.干员面板：{##干员面板 干员名}\n" +
                        "14.章节列表：{##章节列表}\n" +
                        "15.地图列表：{##地图列表 章节名}\n" +
                        "16.地图掉落：{##地图掉落 地图ID}\n" +
                        "17.动态查询：{##动态查询 用户昵称 编号}\n" +
                        "18.关注列表：{##关注列表}\n" +
                        "过大的涩图将导致回复缓慢，请不要上传不能过审的图片";
                break;
            case XiangXiCaiDan:
                result =
                        "1.模拟寻访：\n" +
                                "\t使用方法：输入{##十连 卡池名}或{##单抽 卡池名}\n" +
                                "\t例：##卡池 无拘熔火\n" +
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
                                "11.涩图功能：\n" +
                                "\t使用方法：输入{##涩图}\n" +
                                "\t由于涩图大小原因，回复会产生一定的延迟，若涩图无响应请等待至少10秒钟再次请求\n" +
                                "12.上传涩图：\n" +
                                "\t使用方法：输入{##给你涩图[图片]}或纯图片私聊{[图片]}\n" +
                                "\t请不要上传过大的涩图，更不要上传不能过审的涩图\n" +
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
                                "\t使用方法：{##动态查询 用户昵称 编号}\n" +
                                "\t例：##动态查询 明日方舟 1\n" +
                                "18.关注列表：\n" +
                                "\t使用方法：{##关注列表}\n" +
                                "注：本项目需严格按照格式输入，自然语言处理功能将在后期优化";
                break;
            case ShiLian:
                result = agentService.shiLian(s[1], qq, name,groupId);
                break;
            case ChouKa:
                result = agentService.chouKa(s[1],qq,name,groupId);
                break;
            case KaChi:
                result = agentService.selectPool();
                break;
            case KaChiQingdan:
                result = agentService.selectPoolAgent(s[1]);
                break;
            case DianDaoChaXun:
                result = agentService.selectFoundCount(qq,name);
                break;
            case ZhuanJingCaiLiao:
                result = materialService.ZhuanJingCaiLiao(a);
                break;
            case JingYiCaiLiao:
                result = materialService.JingYingHuaCaiLiao(s[1], 1);
                break;
            case JingErCaiLiao:
                result = materialService.JingYingHuaCaiLiao(s[1], 2);
                break;
            case HeChengLuXian:
                result = materialService.HeChengLuXian(s[1]);
                break;
            case CaiLiaoHuoQu:
                result = materialService.HuoQuTuJing(s[1]);
                break;
            case GongZhaoJieTu:
                result = name + ":\n" + tagsfFoundService.FoundAgentByJson(s[1]);
                break;
            case GongKaiZhaoMu:
                result = name + "\n" + tagsfFoundService.FoundAgentByArray(s[1].split(",|，"));
                break;
            case SeTu:
                result = seTuService.sendImageByType(qq,groupId,1,name);
                break;
            case GeiNiSeTu:
                result = seTuService.getImageIntoDb(s[1],1,name);
                break;
            case XiTongXinXi:
                result = new ServerSystemUtil().getSystemInfo();
                break;
            case GanYuanMianBan:
                result = materialService.selectAgentData(s[1]);
                break;
            case DiTuDiaoLuo:
                result = materialService.selectMaterByMap(s[1]);
                break;
            case ZhangJieLieBiao:
                result = materialService.selectZoneList();
                break;
            case DiTuLieBiao:
                result = materialService.selectMapList(s[1]);
                break;
            case DongTaiChaXun:
                biliListeningService.getDynamic(groupId, s[1], Integer.parseInt(s[2]));
                result = "";
                break;
            case GuanZhuLieBiao:
                result = biliListeningService.getBiliList();
                break;
            default:
                result = "俺不晓得你在锁啥子";
        }
        //返回空字符串则不发送信息
        if (!result.equals("")) {
            sendMsgUtil.CallOPQApiSendMsg(groupId, result,2);
        }
        return result;
    }
}
