package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.LoginUser;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/7 13:50
 **/
public interface LoginMapper {

    //根据用户名查密码的md5
    LoginUser getloginUser(String userName);

    //更新token
    Integer setToken(String userName, String token);

    //验证token是否可用
    List<String> getToken(String token);

    //退出登录清除token
    Integer truancateToken(String userName);

    Integer refreshToken(String token);

}
