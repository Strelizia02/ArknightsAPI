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

    //更新某用户今日抽卡数，同时更新这个用户所属群号以及群昵称
    //一个小bug，用户是唯一的，有可能同时存在于两个群中，每日日报只能获取最后一次抽卡/涩图的群号
    Integer updateUserFoundByQQ(@Param("qq") String qq, @Param("name") String name, @Param("groupId") Long groupId, @Param("foundCount") Integer foundCount);

    //抽到六星，该用户六星数，每日六星数+1
    Integer updateSixByQq(@Param("qq") String qq);

    //抽到五星，该用户每日五星数+1
    Integer updateFiveByQq(@Param("qq") String qq);

    //查询某人的今日抽卡数，垫刀数
    UserFoundInfo selectUserFoundByQQ(String qq);

    //清空每日抽卡次数
    Integer cleanTodayCount();

    //获取总六星数最多的群友
    UserGetInfo selectAllSixMax(Long groupId);

    //获取今日抽取六星最多的群友
    UserGetInfo selectTodaySixMax(Long groupId);

    //获取去今日垫刀次数最多的群友
    UserGetInfo selectFoundCountMax(Long groupId);

    //获取今天爆率最高的群友
    UserGetInfo selectRateTodayMax(Long groupId);

    //获取今天最非酋的群友
    UserGetInfo selectRateTodayMin(Long groupId);

    //获取总爆率最高的群友
    UserGetInfo selectRateAllMax(Long groupId);

    //获取今天抽卡次数用完，但还没有抽到六星的人数
    //这里采用抽卡数量 = 抽卡上限，刨除无限抽卡的管理员
    Integer foundNoSixToday(@Param("groupId") Long groupId, @Param("todayCount") Integer todayCount);

    //给今天的非酋发十抽
    Integer giveMoreFoundToFeiQiu(@Param("groupId") Long groupId, @Param("todayCount") Integer todayCount);

    //查找所有活跃群，今日没有人抽卡则视为不活跃群，只在活跃群中发送日报
    List<Long> selectAllActiveGroups();

    List<Long> selectAllGroups();
}
