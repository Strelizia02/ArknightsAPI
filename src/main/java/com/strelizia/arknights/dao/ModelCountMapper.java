package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.ModelCountInfo;

import java.util.List;

/**
 * @author Strelizia
 * @Description
 * @ProjectName arknights
 * @Package com.strelizia.arknights.dao
 * @Date 2021/4/21 17:07
 **/
public interface ModelCountMapper {

    Integer insertDuplicateCount(String c);

    List<ModelCountInfo> selectModelCount();
}
