package com.strelizia.arknights.service;

/**
 * @author wangzy
 * @Date 2021/3/17 18:03
 **/
public interface GroupAdminInfoService {
    Integer getGroupFoundAdmin(Long groupId);

    Integer getGroupPictureAdmin(Long groupId);
}
