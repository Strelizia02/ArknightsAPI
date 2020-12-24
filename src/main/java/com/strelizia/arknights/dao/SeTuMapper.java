package com.strelizia.arknights.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/22 16:46
 **/
public interface SeTuMapper {
    Integer selectTodaySeTuByQQ(@Param("qq")String qq);

    Integer updateTodaySeTu(@Param("qq")String qq, @Param("name")String name, @Param("groupId")Long groupId);

    Integer insertSeTuUrl(@Param("url")String url, @Param("type")Integer type);

    String selectSeTuUrl(@Param("type")Integer type);
}
