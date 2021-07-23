package com.strelizia.arknights.model;

public class QuestionInfo {
    private Integer id;
    private String description;
    private String answer;
    private Integer attr;
    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getAttr() {
        return attr;
    }

    public void setAttr(Integer attr) {
        this.attr = attr;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
