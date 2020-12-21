package com.strelizia.arknights.model;

/**
 * @author wangzy
 * @Date 2020/12/21 16:12
 **/
public class OperatorSkillInfo {
    private Integer operatorId;
    private Integer skillIndex;
    private String skillName;

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getSkillIndex() {
        return skillIndex;
    }

    public void setSkillIndex(Integer skillIndex) {
        this.skillIndex = skillIndex;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}
