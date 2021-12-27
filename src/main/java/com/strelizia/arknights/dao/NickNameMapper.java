package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.NickName;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Strelizia
 * @Description
 * @ProjectName arknights
 * @Package com.strelizia.arknights.dao
 * @Date 2021/4/20 17:34
 **/
public interface NickNameMapper {
    String selectNameByNickName(String name);

    List<NickName> selectAllNickName(@Param("current") Integer current, @Param("keyWord") String keyWord);

    Integer selectAllNickNameCount();

    Integer deleteNickName(String nickName);

    Integer insertNickName(NickName nickName);
}
