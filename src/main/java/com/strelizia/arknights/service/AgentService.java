package com.strelizia.arknights.service;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/7 14:34
 **/
public interface AgentService {
    String chouKa(String pool, Long qq, String name, Long groupId);

    String shiLian(String pool, Long qq, String name, Long groupId);

    String selectPool(Long groupId);
}
