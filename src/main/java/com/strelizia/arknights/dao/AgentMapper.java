package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.AgentInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/7 13:50
 **/
public interface AgentMapper {

    //根据稀有度查询干员列表
    List<AgentInfo> selectAgentByStar(String pool, Integer star);

    //获取可以歪到的限定干员
    List<AgentInfo> selectLimitAgent(Integer limit);

    //五倍权值的干员
    List<AgentInfo> selectLimitAgentByPool(String pool);

    //查询卡池
    List<String> selectPoolIsExit(String pool);

    //获取所有卡池名称
    List<String> selectPool();

    //根据卡池名获取卡池内up干员数量
    List<AgentInfo> selectPoolAgent(String pool);

    //查询这个池子是不是限定池
    Integer selectPoolLimit(String pool);

    Integer insertAgentPool(AgentInfo agentInfo);

    Integer deleteAgentPool(@Param("pool") String pool);
}
