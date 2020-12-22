package com.strelizia.arknights.service;

/**
 * @author wangzy
 * @Date 2020/12/14 11:40
 **/
public interface MaterialService {
    String ZhuanJingCaiLiao(String[] args);

    String JingYingHuaCaiLiao(String agent, Integer level);

    String HeChengLuXian(String name);

    String HuoQuTuJing(String name);
}
