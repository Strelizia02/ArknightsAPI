package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.UserFoundInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangzy
 * @Date 2020/12/7 13:50
 **/
public interface UserFoundMapper {

    Integer updateUserFoundByQQ(@Param("qq") String qq, @Param("foundCount") Integer foundCount);

    UserFoundInfo selectUserFoundByQQ(String qq);

    Integer cleanTodayCount();

}
