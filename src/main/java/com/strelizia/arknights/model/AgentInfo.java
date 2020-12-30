package com.strelizia.arknights.model;

/**
 * @author wangzy
 * @Date 2020/12/7 13:58
 * 干员信息
 **/
public class AgentInfo {
    private String name;
    private Integer star;
    private String pool;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }
}
