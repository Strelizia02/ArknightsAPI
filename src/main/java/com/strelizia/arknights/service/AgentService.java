package com.strelizia.arknights.service;

/**
 * @author wangzy
 * @Date 2020/12/7 14:34
 **/
public interface AgentService {
    String chouKa(String pool, Long qq, String name, Long groupId);

    String shiLian(String pool, Long qq, String name, Long groupId);

    String XunFang(String pool, Long qq, String name, Long groupId);

    String selectPool();

    String selectFoundCount(Long qq, String name);

    String selectPoolAgent(String pool);
}
