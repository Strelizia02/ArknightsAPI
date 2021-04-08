package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.SkinInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangzy
 * @Date 2021/4/7 17:14
 **/
public interface SkinInfoMapper {
    List<SkinInfo> selectSkinByInfo(String Info);

    Integer selectMaxId();

    Integer insertBySkinInfo(SkinInfo skinInfo);

    String selectSkinById(Integer id);

    List<Integer> selectBase64IsUrl();

    Integer updateBaseStrById(@Param("id") Integer id, @Param("skinBase64") String skinBase64);
}
