package com.strelizia.arknights.service;

import com.strelizia.arknights.model.AgentTagsInfo;

import java.util.List;
import java.util.Map;

/**
 * @author wangzy
 * @Date 2020/12/15 10:40
 **/
public interface TagsfFoundService {
    String FoundAgentByJson(String Json);

    String FoundAgentByArray(String[] array);
}
