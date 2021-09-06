package com.strelizia.arknights.service;

/**
 * @author wangzy
 * @Date 2021/1/17 16:31
 **/
public interface EnemyService {

    String getEnemyInfoByName(Long qq, String name);

    String getEnemyListByName(Long qq, String name);
}
