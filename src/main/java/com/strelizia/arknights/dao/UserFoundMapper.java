package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.UserFoundInfo;
import com.strelizia.arknights.model.UserGetInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/7 13:50
 **/
public interface UserFoundMapper {

    Integer updateUserFoundByQQ(@Param("qq") String qq, @Param("name")String name, @Param("groupId")Long groupId, @Param("foundCount") Integer foundCount);

    Integer updateSixByQq(@Param("qq") String qq);

    Integer updateFiveByQq(@Param("qq") String qq);

    UserFoundInfo selectUserFoundByQQ(String qq);

    Integer cleanTodayCount();

    UserGetInfo selectAllSixMax(Long groupId);

    UserGetInfo selectTodaySixMax(Long groupId);

    UserGetInfo selectFoundCountMax(Long groupId);

    UserGetInfo selectRateTodayMax(Long groupId);

    UserGetInfo selectRateTodayMin(Long groupId);

    UserGetInfo selectRateAllMax(Long groupId);

    Integer foundNoSixToday(@Param("groupId") Long groupId, @Param("todayCount") Integer todayCount);

    Integer giveMoreFoundToFeiQiu(@Param("todayCount") Integer todayCount);

    List<Long> selectAllActiveGroups();

}
