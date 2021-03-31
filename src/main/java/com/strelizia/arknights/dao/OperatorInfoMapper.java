package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.OperatorBasicInfo;

import java.util.List;
import java.util.Set;

/**
 * @author wangzy
 * @Date 2021/3/29 17:07
 **/
public interface OperatorInfoMapper {
    //根据各种信息查找对应干员
    List<String> getOperatorNameByInfo(String Info);

    //获取全部干员列表
    List<String> getAllOperator();

    //查找干员档案
    OperatorBasicInfo getOperatorInfoByName(String name);

    //查找全部画师
    List<String> getAllDrawName();

    //条件模糊查询画师
    List<String> getAllDrawNameLikeStr(String str);

    //查找全部声优
    List<String> getAllInfoName();

    //条件模糊查询声优
    List<String> getAllInfoNameLikeStr(String str);

    //根据生日查找干员
    List<String> getOperatorByBirthday(String birthday);

}
