package com.strelizia.arknights.service;

/**
 * @author wangzy
 * @Date 2020/12/14 11:40
 **/
public interface MaterialService {
    //查询技能专精材料
    String ZhuanJingCaiLiao(Long groupId, String[] args);

    //查询干员精英化材料
    String JingYingHuaCaiLiao(Long groupId, String agent, Integer level);

    //查询材料合成途径
    String HeChengLuXian(Long groupId, String name);

    //查询材料获取途径
    String HuoQuTuJing(String name);

    //查询干员面板信息
    String selectAgentData(String name);

    //查询地图掉落信息
    String selectMaterByMap(Long groupId, String MapId);

    //查询全部地图列表
    String selectMapList(String zoneName);

    //查询全部章节列表
    String selectZoneList();
}
