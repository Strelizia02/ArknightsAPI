package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.GroupAdminInfoMapper;
import com.strelizia.arknights.model.GroupAdminInfo;
import com.strelizia.arknights.service.GroupAdminInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangzy
 * @Date 2021/3/17 18:05
 **/
@Service
public class GroupAdminInfoServiceImpl implements GroupAdminInfoService {

    @Autowired
    private GroupAdminInfoMapper groupAdminInfoMapper;

    @Override
    public Integer getGroupFoundAdmin(Long groupId) {
        GroupAdminInfo groupAdminNum = groupAdminInfoMapper.getGroupAdminNum(groupId);
        if (groupAdminNum == null){
            groupAdminInfoMapper.insertGroupId(groupId);
            groupAdminNum = groupAdminInfoMapper.getGroupAdminNum(groupId);
        }
        return groupAdminNum.getFound();
    }

    @Override
    public Integer getGroupPictureAdmin(Long groupId) {
        GroupAdminInfo groupAdminNum = groupAdminInfoMapper.getGroupAdminNum(groupId);
        if (groupAdminNum == null){
            groupAdminInfoMapper.insertGroupId(groupId);
            groupAdminNum = groupAdminInfoMapper.getGroupAdminNum(groupId);
        }
        return groupAdminNum.getPicture();
    }
}
