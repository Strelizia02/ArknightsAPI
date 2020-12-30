package com.strelizia.arknights.model;

/**
 * @author wangzy
 * @Date 2020/12/7 17:45
 * 抽卡请求数据封装
 **/
public class FoundInfo {
    private String pool;
    private Long qq;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public Long getQq() {
        return qq;
    }

    public void setQq(Long qq) {
        this.qq = qq;
    }
}
