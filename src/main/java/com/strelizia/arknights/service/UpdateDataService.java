package com.strelizia.arknights.service;

/**
 * @author wangzy
 * @Date 2020/12/19 15:45
 **/
public interface UpdateDataService {
    Integer updateAllData();

    Integer updateByJson(String json);
}
