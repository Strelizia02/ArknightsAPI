package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.AdminUserInfo;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/26 0:38
 **/
public interface AdminUserMapper {

    List<AdminUserInfo> selectAllAdmin();
}
