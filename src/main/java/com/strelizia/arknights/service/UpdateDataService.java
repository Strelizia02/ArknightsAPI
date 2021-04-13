package com.strelizia.arknights.service;

/**
 * @author wangzy
 * @Date 2020/12/19 15:45
 **/
public interface UpdateDataService {
    void updateAllData(boolean checkUpdate);

    Integer updateOperatorByJson(String json);

    void updateSkin();

    void updateItemAndFormula();
}
