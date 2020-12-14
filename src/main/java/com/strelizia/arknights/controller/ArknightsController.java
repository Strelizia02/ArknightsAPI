package com.strelizia.arknights.controller;

import com.strelizia.arknights.model.MessageInfo;
import com.strelizia.arknights.service.AgentService;
import com.strelizia.arknights.service.MaterialService;
import com.strelizia.arknights.util.SendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author wangzy
 * @Date 2020/11/20 17:02
 **/
@RequestMapping("Arknights")
@RestController
@Slf4j
public class ArknightsController {

    @Autowired
    private AgentService agentService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private ThreadPoolTaskExecutor poolTaskExecutor;

    @Autowired
    protected RestTemplate restTemplate;

    @Value("${userConfig.loginQq}")
    private Long loginQq;

    @Value("${userConfig.OPQUrl}")
    private String OPQUrl;

    private String sendTextMsgApi = "/v1/LuaApiCaller";

    //返回单抽结果
    @PostMapping("receive")
    public String receive(
            @RequestBody MessageInfo message
    ){
        log.info("接受到消息:{}",message.getText());
        Long qq = message.getQq();
        String name = message.getName();
        String[] a = message.getText().split(" ");
        String[] s = new String[5];
        for (int i = 0;i<a.length;i++){
            s[i] = a[i];
        }
        Long groupId = message.getGroupId();
        String result;
        switch (s[0]){
            case "菜单":
                result = "1.模拟寻访：\n" +
                        "\t使用方法：输入{##十连 卡池名}或{##单抽 卡池名}\n" +
                        "\t例：##卡池 无拘熔火\n" +
                        "2.卡池清单：\n" +
                        "\t使用方法：输入{##卡池}\n" +
                        "3.查询个人垫刀数、今日抽卡数、当前六星概率：\n" +
                        "\t使用方法：输入{##垫刀查询}\n" +
                        "4.查询技能专精材料：\n" +
                        "\t使用方法：输入{##专精材料 干员名 第几技能 专精等级}或{##专精材料 技能名 专精等级}\n" +
                        "\t例1：##专精材料 艾雅法拉 3 3\n" +
                        "\t例2：##专精材料 火山 3\n" +
                        "5.精英化材料查询：\n" +
                        "\t使用方法：输入{##精一材料 干员名}或{精二材料 干员名}\n" +
                        "\t例1：##精一材料 克洛丝\n" +
                        "\t例2：##精二材料 陈\n" +
                        "6.查询材料合成路线：\n" +
                        "\t使用方法：输入{##合成路线 材料名}\n" +
                        "\t例：##合成路线 D32钢\n" +
                        "7.查询材料掉落关卡：\n" +
                        "\t使用方法：输入{##材料获取 材料名}\n" +
                        "\t例：##材料获取 研磨石\n" +
                        "注：本项目需严格按照格式输入，自然语言处理功能将在后期优化";
                break;
            case "十连":
                result = agentService.shiLian(s[1], qq, name);
                break;
            case "抽卡":
                result = agentService.chouKa(s[1],qq,name);
                break;
            case "卡池":
                result = agentService.selectPool();
                break;
            case "垫刀查询":
                result = agentService.selectFoundCount(qq,name);
                break;
            case "专精材料":
                int index = 1;
                int level = 1;
                try {
                    switch (s[2]) {
                        case "一技能":
                            index = 1;
                            break;
                        case "二技能":
                            index = 2;
                            break;
                        case "三技能":
                            index = 3;
                            break;
                        default:
                            index = Integer.parseInt(s[2]);
                    }
                }catch (Exception e){
                    result = "技能等级最好输入阿拉伯数字1-2-3";
                    break;
                }
                if (a.length == 4) {
                    try {
                        switch (s[3]) {
                            case "专一":
                                level = 1;
                                break;
                            case "专二":
                                level = 2;
                                break;
                            case "专三":
                                level = 3;
                                break;
                            default:
                                level = Integer.parseInt(s[3]);
                        }
                    }catch (Exception e){
                        result = "技能索引请输入输入阿拉伯数字1-2-3";
                        break;
                    }
                    result = materialService.ZhuanJingCaiLiao(s[1], index, level);
                } else if (a.length == 3){
                    result = materialService.ZhuanJingCaiLiao(s[1], index);
                }else {
                    result = "找不到查询的内容";
                }
                break;
            case "精一材料":
                result = materialService.JingYingHuaCaiLiao(s[1], 1);
                break;
            case "精二材料":
                result = materialService.JingYingHuaCaiLiao(s[1], 2);
                break;
            case "合成路线":
                result = materialService.HeChengLuXian(s[1]);
                break;
            case "材料获取":
                result = materialService.HuoQuTuJing(s[1]);
                break;
            default:
                result = "俺不晓得你在锁啥子";
        }
        CallOPQApiSendMsg(groupId,result);
        return result;
    }

    public void CallOPQApiSendMsg(Long groupId, String s){
        poolTaskExecutor.execute(() -> SendMsgUtil.sendTextMsgToGroup(restTemplate, groupId, s,
                "http://" + OPQUrl + ":8888" + sendTextMsgApi + "?qq=" +  loginQq + "&funcname=SendMsg"));
    }
}
