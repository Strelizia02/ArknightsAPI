package com.wzy.arknights.dao;

import com.wzy.arknights.model.UserFoundInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangzy
 * @Date 2020/12/7 13:50
 **/
public interface UserFoundMapper {

    Integer updateUserFoundByQQ(@Param("qq") String qq, @Param("six") Integer six);

    UserFoundInfo selectUserFoundByQQ(String qq);

    Integer cleanTodayCount();

}
