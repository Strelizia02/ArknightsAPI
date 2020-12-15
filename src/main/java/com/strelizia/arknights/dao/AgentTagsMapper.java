package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.AgentTagsInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/14 18:07
 **/
public interface AgentTagsMapper {
    List<AgentTagsInfo> selectAgentByTag(@Param("tags") List<String> tags);

    List<AgentTagsInfo> selectSixAgentByTag(@Param("tags") List<String> tags);

    List<AgentTagsInfo> selectAgentAll();
}
