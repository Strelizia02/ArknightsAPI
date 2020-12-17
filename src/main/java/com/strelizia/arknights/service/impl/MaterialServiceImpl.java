package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.MaterialMadeMapper;
import com.strelizia.arknights.dao.OperatorEvolveMapper;
import com.strelizia.arknights.dao.SkillMateryMapper;
import com.strelizia.arknights.model.MaterialInfo;
import com.strelizia.arknights.model.SourcePlace;
import com.strelizia.arknights.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/14 11:43
 **/
@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private OperatorEvolveMapper operatorEvolveMapper;

    @Autowired
    private SkillMateryMapper skillMateryMapper;

    @Autowired
    private MaterialMadeMapper materialMadeMapper;

    @Override
    public String ZhuanJingCaiLiao(String skillName, Integer level) {
        List<MaterialInfo> materialInfos = skillMateryMapper.selectSkillUpBySkillName(skillName, level);
        String s = "";
        if (materialInfos.size() == 0){
            s = "找不到查询的内容";
        }else {
            for (MaterialInfo m:materialInfos){
                s = s + m.getMaterialName() + "*" + m.getMaterialNum() + "个\n";
            }
        }
        return s;
    }

    @Override
    public String ZhuanJingCaiLiao(String agent, Integer index, Integer level) {
        List<MaterialInfo> materialInfos = skillMateryMapper.selectSkillUpByAgentAndIndex(agent, index, level);
        String s = "";
        if (materialInfos.size() == 0){
            s = "找不到查询的内容";
        }else {
            for (MaterialInfo m:materialInfos){
                s = s + m.getMaterialName() + "*" + m.getMaterialNum() + "个\n";
            }
        }
        return s;
    }

    @Override
    public String JingYingHuaCaiLiao(String agent, Integer level) {
        List<MaterialInfo> materialInfos = operatorEvolveMapper.selectOperatorEvolveBySkillName(agent, level);
        String s = "";
        if (materialInfos.size() == 0){
            s = "找不到查询的材料";
        }else {
            for (MaterialInfo m:materialInfos){
                s = s + m.getMaterialName() + "*" + m.getMaterialNum() + "个\n";
            }
        }
        return s;
    }

    @Override
    public String HeChengLuXian(String name) {
        List<MaterialInfo> materialInfos = materialMadeMapper.selectMadeMater(name);
        String s = "";
        if (materialInfos.size() == 0){
            s = "找不到该材料的合成路线";
        }else {
            for (MaterialInfo m:materialInfos){
                s = s + m.getMaterialName() + "*" + m.getMaterialNum() + "个\n";
            }
        }
        return s;
    }

    @Override
    public String HuoQuTuJing(String name) {
        List<SourcePlace> sourcePlaces = materialMadeMapper.selectMaterSource(name);
        String s = "";
        if (sourcePlaces.size() == 0){
            s = "找不到该材料的获取关卡";
        }else {
            for (SourcePlace p:sourcePlaces){
                Integer i = p.getSourceRate();
                String rate = "";
                switch (i){
                    case 1:
                        rate = "罕见";
                        break;
                    case 2:
                        rate = "小概率";
                        break;
                    case 3:
                        rate = "概率掉落";
                        break;
                    case 4:
                        rate = "大概率";
                        break;
                    case 5:
                        rate = "固定掉落";
                        break;
                }
                s = s + p.getSourcePlace() + "--" + rate + "\n";
            }
        }
        return s;
    }
}
