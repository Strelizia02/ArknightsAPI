package com.strelizia.arknights.service;

import org.json.JSONObject;

/**
 * @author wangzy
 * @Date 2020/12/19 15:45
 **/
public interface UpdateDataService {
    /**
     * checkUpdate是否检查版本更新
     *      —— 是，检查版本更新，版本不一致才进行更新
     *      —— 否，不进行版本检查，强制更新
     */
    void updateAllData(boolean checkUpdate);

    void updateSkin();

    void updateOperatorByJson();

    void updateItemAndFormula();
}
