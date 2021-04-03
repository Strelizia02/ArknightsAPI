package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.SkillDesc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangzy
 * @Date 2021/4/2 17:17
 **/
public interface SkillDescMapper {

    List<SkillDesc> selectSkillDescByNameAndLevel(@Param("name") String name, @Param("level") Integer level);

}
