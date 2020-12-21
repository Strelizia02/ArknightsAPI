package com.strelizia.arknights.service;

/**
 * @author wangzy
 * @Date 2020/12/19 15:45
 **/
public interface UpdateDataService {
    String updateAllData();

    Integer updateByJson(String json);
}
