package com.strelizia.arknights.model;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzy
 * @Date 2020/12/22 14:16
 **/
@Component
public enum ClassificationEnum {
    CaiDan,
    XiangXiCaiDan,
    ChouKa,
    ShiLian,
    KaChi,
    DianDaoChaXun,
    ZhuanJingCaiLiao,
    JingYiCaiLiao,
    JingErCaiLiao,
    HeChengLuXian,
    CaiLiaoHuoQu,
    GongZhaoJieTu,
    GongKaiZhaoMu,
    SeTu;

    public ClassificationEnum GetClass(String s){
        Map<String,ClassificationEnum> map = new HashMap<>();
        map.put("菜单",CaiDan);
        map.put("详细菜单",XiangXiCaiDan);
        map.put("抽卡",ChouKa);
        map.put("十连",ShiLian);
        map.put("卡池",KaChi);
        map.put("垫刀查询",DianDaoChaXun);
        map.put("专精材料",ZhuanJingCaiLiao);
        map.put("精一材料",JingYiCaiLiao);
        map.put("精二材料",JingErCaiLiao);
        map.put("合成路线",HeChengLuXian);
        map.put("材料获取",CaiLiaoHuoQu);
        map.put("公招截图",GongZhaoJieTu);
        map.put("公开招募",GongKaiZhaoMu);
        map.put("涩图",SeTu);
        return map.get(s);
    }
}
