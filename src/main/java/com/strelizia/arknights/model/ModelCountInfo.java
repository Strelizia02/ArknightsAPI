package com.strelizia.arknights.model;

/**
 * @author Strelizia
 * @Description
 * @ProjectName arknights
 * @Package com.strelizia.arknights.model
 * @Date 2021/4/21 17:14
 **/
public class ModelCountInfo {
    private String modelName;
    private Long count;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "功能：" + modelName + "已使用" + count + "次";
    }
}
