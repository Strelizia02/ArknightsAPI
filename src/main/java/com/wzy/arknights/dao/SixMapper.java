package com.wzy.arknights.dao;

import com.wzy.arknights.model.UserFoundInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangzy
 * @Date 2020/12/7 13:50
 **/
public interface SixMapper {

    Integer updateUserFoundByQQ(@Param("qq") Long qq, @Param("six") Integer six);

    UserFoundInfo selectUserFoundByQQ(Long qq);

    Integer cleanTodayCount();

}
