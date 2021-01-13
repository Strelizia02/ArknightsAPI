package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.BiliCount;

import java.util.List;

/**
 * @author wangzy
 * @Date 2021/1/12 17:13
 **/
public interface BiliMapper {
    //获取所有信息
    List<BiliCount> getBiliCountList();

    Integer updateNewDynamic(BiliCount bili);

    BiliCount getOneDynamicByName(String name);
}
