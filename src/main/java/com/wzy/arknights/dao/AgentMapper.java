package com.wzy.arknights.dao;

import com.wzy.arknights.model.AgentInfo;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/7 13:50
 **/
public interface AgentMapper {
    List<AgentInfo> selectAgentByStar(String pool, Integer star);

    List<String> selectPool();
}
