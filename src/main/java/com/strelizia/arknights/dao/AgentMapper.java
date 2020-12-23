package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.AgentInfo;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/7 13:50
 **/
public interface AgentMapper {
    List<AgentInfo> selectAgentByStar(String pool, Integer star);

    List<String> selectPool();

    List<AgentInfo> selectPoolAgent(String pool);
}
