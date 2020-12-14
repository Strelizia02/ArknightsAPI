package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.MaterialInfo;
import com.strelizia.arknights.model.SourcePlace;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/14 15:05
 **/
public interface MaterialMadeMapper {
    List<MaterialInfo> selectMadeMater(String name);

    List<SourcePlace> selectMaterSource(String name);
}
