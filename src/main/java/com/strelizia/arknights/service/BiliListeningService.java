package com.strelizia.arknights.service;

import com.strelizia.arknights.model.DynamicDetail;

/**
 * @author wangzy
 * @Date 2021/1/12 16:29
 **/
public interface BiliListeningService {
    //获取动态列表
    boolean getDynamicList();

    //获取动态详情
    DynamicDetail getDynamicDetail(Long DynamicId);

    //获取最新视频
    String getVideo(Long qq, String name);

    //查询某条动态
    String getDynamic(Long qq, Long groupId, String name, int index);

    //关注列表
    String getBiliList(Long groupId);

    String setGroupBiliRel(Long qq, Long groupId, String uid);

    String removeGroupBiliRel(Long qq, Long groupId, String uid);
}
