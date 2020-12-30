package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.MaterialInfo;
import com.strelizia.arknights.model.SourcePlace;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/14 15:05
 **/
public interface MaterialMadeMapper {

    //根据材料名获取该材料的合成列表
    List<MaterialInfo> selectMadeMater(String name);

    //根据材料名获取材料获取的关卡列表
    List<SourcePlace> selectMaterSource(String name);
}
