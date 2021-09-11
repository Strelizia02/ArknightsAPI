package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.BiliCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangzy
 * @Date 2021/1/12 17:13
 **/
public interface BiliMapper {
    //获取所有信息
    List<BiliCount> getBiliCountList();

    List<BiliCount> getBiliCountListByGroupId(@Param("groupId") Long groupId);

    Integer updateNewDynamic(BiliCount bili);

    BiliCount getOneDynamicByName(String name);

    Integer insertGroupBiliRel(@Param("groupId") Long groupId, @Param("uid") Long uid);

    Integer deleteGroupBiliRel(@Param("groupId") Long groupId, @Param("uid") Long uid);

    Integer selectGroupBiliRel(@Param("groupId") Long groupId, @Param("uid") Long uid);

    Integer existBiliUid(@Param("uid") Long uid);

    Integer insertBiliUid(@Param("uid") Long uid);
}
