package com.strelizia.arknights.model;

/**
 * @author wangzy
 * @Date 2020/12/14 15:08
 * 材料获取途径信息
 **/
public class SourcePlace {
    private String zone_name;
    private String code;
    private Double rate;

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
