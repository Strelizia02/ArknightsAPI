package com.wzy.arknights.service;

import com.wzy.arknights.model.AccountInfo;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/11/20 17:01
 **/
public interface ArknightsService {
    List<AccountInfo> selectAllAccount();
}
