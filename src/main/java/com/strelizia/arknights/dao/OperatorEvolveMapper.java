package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.MaterialInfo;
import com.strelizia.arknights.model.OperatorData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/14 11:12
 **/
public interface OperatorEvolveMapper {

    //根据干员名称和精英化等级获取对应的消耗材料列表
    List<MaterialInfo> selectOperatorEvolveByName(@Param("agent") String agent, @Param("level") Integer level);

    //根据干员名获取干员满级面板
    OperatorData selectOperatorData(String name);
}
