package com.strelizia.arknights.model;

/**
 * @author wangzy
 * @Date 2020/12/16 14:00
 * 日报返回信息
 **/
public class UserGetInfo {
    private String name;
    private Double value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
