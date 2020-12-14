package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.MaterialInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/14 11:12
 **/
public interface OperatorEvolveMapper {
    List<MaterialInfo> selectOperatorEvolveBySkillName(@Param("agent") String agent, @Param("level") Integer level);
}
