package com.strelizia.arknights.util;

import com.strelizia.arknights.model.AdminUserInfo;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/29 14:34
 **/
public class AdminUtil {
    /**
     * 返回用户无限抽卡权限
     *
     * @param qq     qqMD5加密字符串
     * @param admins 权限列表
     * @return
     */
    public static boolean getFoundAdmin(String qq, List<AdminUserInfo> admins) {
        for (AdminUserInfo admin : admins) {
            if (admin.getQq().equals(qq) && admin.getFound() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回用户有无无限涩图权限
     *
     * @param qq     qqMD5加密字符串
     * @param admins 权限列表
     * @return
     */
    public static boolean getImgAdmin(String qq, List<AdminUserInfo> admins) {
        for (AdminUserInfo admin : admins) {
            if (admin.getQq().equals(qq) && admin.getImg() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回用户有无爆率拉满权限
     *
     * @param qq     qqMD5加密字符串
     * @param admins 权限列表
     * @return
     */
    public static boolean getSixAdmin(String qq, List<AdminUserInfo> admins) {
        for (AdminUserInfo admin : admins) {
            if (admin.getQq().equals(qq) && admin.getSix() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回用户有无sql权限
     *
     * @param qq     qqMD5加密字符串
     * @param admins 权限列表
     * @return
     */
    public static boolean getSqlAdmin(String qq, List<AdminUserInfo> admins) {
        for (AdminUserInfo admin : admins) {
            if (admin.getQq().equals(qq) && admin.getSql() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回用户有无涩图管理权限
     *
     * @param qq     qqMD5加密字符串
     * @param admins 权限列表
     * @return
     */
    public static boolean getupLoadAdmin(String qq, List<AdminUserInfo> admins) {
        for (AdminUserInfo admin : admins) {
            if (admin.getQq().equals(qq) && admin.getUpload() == 1) {
                return true;
            }
        }
        return false;
    }
}
