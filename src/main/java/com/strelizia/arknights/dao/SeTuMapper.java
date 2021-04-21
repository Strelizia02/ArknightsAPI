package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.ImgUrlInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/22 16:46
 **/
public interface SeTuMapper {

    //根据qq的MD5加密获取某人的今日涩图请求数
    Integer selectTodaySeTuByQQ(@Param("qq") String qq);

    //某人的涩图请求数+1
    Integer updateTodaySeTu(@Param("qq") String qq, @Param("name") String name, @Param("groupId") Long groupId);

    //增加一张涩图
    //（曾经是存url，url存在过期和失效的情况，后来改为了base64存储，但是base64导致一个字段内容过长，影响mysql性能，尚无更好的免费解决方法，对象存储系统都要钱）
    Integer insertSeTuUrl(@Param("url") String url, @Param("type") Integer type);

    //根据图片类型查找随机图片，目前图片类型只有1涩图，0表情包
    ImgUrlInfo selectOneSeTuUrl(@Param("type") Integer type);

    //根据图片类型查找所有图片
    List<Integer> selectAllSeTuUrl(@Param("type") Integer type);

    Integer deleteSeTuById(Integer id);

    ImgUrlInfo selectOneSeTuUrlById(Integer id);
}
