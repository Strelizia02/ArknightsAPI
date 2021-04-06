package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.GroupAdminInfo;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangzy
 * @Date 2021/3/17 17:52
 **/
public interface GroupAdminInfoMapper {

    GroupAdminInfo getGroupAdminNum(Long groupId);

    Integer insertGroupId(Long groupId);

    Integer updatePictureAdmin(@Param("groupId") Long groupId, @Param("picture") Integer picture);
}
