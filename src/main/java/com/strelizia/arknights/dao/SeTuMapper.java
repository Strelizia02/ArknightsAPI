package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.ImgUrlInfo;
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

    List<ImgUrlInfo> selectSeTuUrl(@Param("type")Integer type);
}
