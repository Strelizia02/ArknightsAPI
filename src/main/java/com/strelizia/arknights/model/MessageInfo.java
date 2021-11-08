package com.strelizia.arknights.model;

import java.io.Serializable;

/**
 * @author wangzy
 * @Date 2020/12/10 16:37
 * qq消息封装
 **/
public class MessageInfo implements Serializable {
    private String text;
    private Long qq;
    private String name;
    private Long groupId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getQq() {
        return qq;
    }

    public void setQq(Long qq) {
        this.qq = qq;
    }
}
