package com.strelizia.arknights.model;

import io.swagger.models.auth.In;

/**
 * @author wangzy
 * @Date 2021/1/9 20:01
 **/
public class MapMatrixInfo {
    private String material_name;
    private Double rate;
    private Integer quantity;
    private Integer times;

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
