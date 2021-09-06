package com.strelizia.arknights.service;

import com.strelizia.arknights.model.OperatorBasicInfo;

import java.util.List;
import java.util.Set;

/**
 * @author wangzy
 * @Date 2021/3/29 17:07
 **/
public interface OperatorInfoService {

    //根据多条件查找符合干员
    String getOperatorByInfos(Long qq, String[] infos);

    //根据干员名查询对应档案
    String getOperatorInfo(Long qq, String name, String where);

    //根据关键字查找声优
    String getCVByName(Long qq, String str);

    //根据关键字查找画师
    String getDrawByName(Long qq, String str);

    //根据干员名查找天赋
    String getTalentByName(Long qq, String name);

}
