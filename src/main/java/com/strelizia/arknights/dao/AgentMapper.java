package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.AgentInfo;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/7 13:50
 **/
public interface AgentMapper {

    //根据稀有度查询干员列表
    List<AgentInfo> selectAgentByStar(String pool, Integer star);

    //获取所有卡池名称
    List<String> selectPool();

    //根据卡池名获取卡池内up干员数量
    List<AgentInfo> selectPoolAgent(String pool);

    //查询这个池子是不是限定池
    Integer selectPoolLimit(String pool);
}
