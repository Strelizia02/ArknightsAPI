package com.strelizia.arknights.dao;

import org.apache.ibatis.annotations.Param;

/**
 * @author wangzy
 * @Date 2020/12/22 16:46
 **/
public interface SeTuMapper {
    Integer selectTodaySeTuByQQ(@Param("qq")String qq);
    Integer updateTodaySeTu(@Param("qq")String qq, @Param("name")String name, @Param("groupId")Long groupId);
}
