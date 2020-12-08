package com.wzy.arknights.dao;

import org.apache.ibatis.annotations.Param;

/**
 * @author wangzy
 * @Date 2020/12/7 13:50
 **/
public interface SixMapper {
    Integer updateSixByQQ(@Param("qq") Long qq, @Param("six") Integer six);
    Integer selectSixByQQ(Long qq);
}
