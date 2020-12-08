package com.wzy.arknights.service.impl;

import com.wzy.arknights.dao.ArknightsMapper;
import com.wzy.arknights.model.AccountInfo;
import com.wzy.arknights.service.ArknightsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/11/20 17:03
 **/
@Service
@Slf4j
public class ArknightsServiceImpl implements ArknightsService {

    @Autowired
    private ArknightsMapper arknightsMapper;

    @Override
    public List<AccountInfo> selectAllAccount() {
        List<AccountInfo> accountInfos = arknightsMapper.selectAllAccount();
        return accountInfos;
    }
}
