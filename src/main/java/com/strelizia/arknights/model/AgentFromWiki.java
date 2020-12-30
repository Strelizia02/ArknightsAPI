package com.strelizia.arknights.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wangzy
 * @Date 2020/12/19 15:56
 * wiki的干员信息json封装
 **/
public class AgentFromWiki {
    private String name;

    private String en;

    private String[] tags;

    private String class_name;

    private String logo;

    private String gkzm;

    private String No;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @JsonProperty(value = "class")
    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getGkzm() {
        return gkzm;
    }

    public void setGkzm(String gkzm) {
        this.gkzm = gkzm;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }
}
