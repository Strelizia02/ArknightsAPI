package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.AdminUserInfo;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/26 0:38
 **/
public interface AdminUserMapper {

    //查询所有的管理员用户，以及对应的权限信息
    List<AdminUserInfo> selectAllAdmin();
}
