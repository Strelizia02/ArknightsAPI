package com.strelizia.arknights.model;

/**
 * @author wangzy
 * @Date 2020/12/29 14:38
 * 管理员权限信息
 **/
public class AdminUserInfo {
    //管理员qq
    private String qq;
    //管理员昵称，用于肉眼识别
    private String name;
    //无限抽卡权限
    private Integer found;
    //无限涩图权限
    private Integer img;
    //爆率拉满权限
    private Integer six;
    //SQL查询权限
    private Integer sql;

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFound() {
        return found;
    }

    public void setFound(Integer found) {
        this.found = found;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public Integer getSix() {
        return six;
    }

    public void setSix(Integer six) {
        this.six = six;
    }

    public Integer getSql() {
        return sql;
    }

    public void setSql(Integer sql) {
        this.sql = sql;
    }
}
