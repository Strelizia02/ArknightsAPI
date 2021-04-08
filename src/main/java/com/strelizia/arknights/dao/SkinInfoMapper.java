package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.SkinInfo;

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
}
