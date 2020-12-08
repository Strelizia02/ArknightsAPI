package com.wzy.arknights.dao;

import com.wzy.arknights.model.AccountInfo;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/11/20 17:02
 **/
public interface ArknightsMapper {
    List<AccountInfo> selectAllAccount();
}
