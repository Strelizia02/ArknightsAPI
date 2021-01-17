package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.EnemyMapper;
import com.strelizia.arknights.model.EnemyInfo;
import com.strelizia.arknights.service.EnemyService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
    public String getEnemyInfoByName(String name) {
        List<EnemyInfo> enemyInfo = enemyMapper.selectEnemyByName(name);
        int size = enemyInfo.size();
        String s = "";
        if (size == 0){
            s = "未找到该敌人的信息";
        }else if (size == 1){
            s = enemyInfo.get(0).toString();
        }else {
            for (int i = 0; i < size; i++){
                s += "\n" + enemyInfo.get(i).toString();
            }
        }
        return s.substring(1);
    }

    @Override
    public String getEnemyListByName(String name) {
        if (name == null){
            name = "";
        }
        List<String> nameList = enemyMapper.selectEnemyListByName(name);
        Set<String> names = new TreeSet<>(nameList);
        String s = "搜索到的敌人名称为：";
        for (String enemyName:names){
            s += "\n" + enemyName;
        }
        return s;
    }
}
