package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.EnemyMapper;
import com.strelizia.arknights.model.EnemyInfo;
import com.strelizia.arknights.service.EnemyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author wangzy
 * @Date 2021/1/17 16:33
 **/
@Service
public class EnemyServiceImpl implements EnemyService {

    @Autowired
    private EnemyMapper enemyMapper;

    @Override
    public String getEnemyInfoByName(Long qq, String name) {
        List<EnemyInfo> enemyInfo = enemyMapper.selectEnemyByName(name);
        int size = enemyInfo.size();
        StringBuilder s = new StringBuilder();
        if (name.contains("霜星")){
            s.append("霜星是我们罗德岛的干员哦。\n");
        }
        if (size == 0) {
            s = new StringBuilder("[ATUSER(" + qq + ")]未找到该敌人的信息");
        } else if (size == 1) {
            s = new StringBuilder("[ATUSER(" + qq + ")]" + enemyInfo.get(0).toString());
        } else {
            for (EnemyInfo info : enemyInfo) {
                s.append("\n").append(info.toString());
            }
        }
        return s.toString();
    }

    @Override
    public String getEnemyListByName(Long qq, String name) {
        if (name == null) {
            name = "";
        }
        List<String> nameList = enemyMapper.selectEnemyListByName(name);
        Set<String> names = new TreeSet<>(nameList);
        StringBuilder s = new StringBuilder("[ATUSER(" + qq + ")]搜索到的敌人名称为：");
        for (String enemyName : names) {
            s.append("\n").append(enemyName);
        }
        return s.toString();
    }
}
