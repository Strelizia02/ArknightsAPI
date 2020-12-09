package com.wzy.arknights.model;

/**
 * @author wangzy
 * @Date 2020/12/8 14:07
 **/
public class UserFoundInfo {
    private String qq;
    private Integer foundCount;
    private Integer todayCount;

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Integer getFoundCount() {
        return foundCount;
    }

    public void setFoundCount(Integer foundCount) {
        this.foundCount = foundCount;
    }

    public Integer getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(Integer todayCount) {
        this.todayCount = todayCount;
    }
}
