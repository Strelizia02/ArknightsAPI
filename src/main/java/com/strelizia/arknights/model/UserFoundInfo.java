package com.strelizia.arknights.model;

/**
 * @author wangzy
 * @Date 2020/12/8 14:07
 * 用户抽卡垫刀信息
 **/
public class UserFoundInfo {
    private String qq;
    private Integer foundCount;
    private Integer todayCount;
    private Integer allCount;
    private Integer allSix;
    private Integer todayFive;

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

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getAllSix() {
        return allSix;
    }

    public void setAllSix(Integer allSix) {
        this.allSix = allSix;
    }

    public Integer getTodayFive() {
        return todayFive;
    }

    public void setTodayFive(Integer todayFive) {
        this.todayFive = todayFive;
    }
}
