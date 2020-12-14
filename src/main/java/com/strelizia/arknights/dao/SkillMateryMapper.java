package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.MaterialInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/14 11:12
 **/
public interface SkillMateryMapper {
    List<MaterialInfo> selectSkillUpBySkillName(@Param("skillName") String skillName, @Param("level") Integer level);

    List<MaterialInfo> selectSkillUpByAgentAndIndex(@Param("agent") String agent, @Param("index") Integer index, @Param("level") Integer level);
}
