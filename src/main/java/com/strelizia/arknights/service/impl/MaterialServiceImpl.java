package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.MaterialMadeMapper;
import com.strelizia.arknights.dao.OperatorEvolveMapper;
import com.strelizia.arknights.dao.SkillMateryMapper;
import com.strelizia.arknights.model.MaterialInfo;
import com.strelizia.arknights.model.OperatorData;
import com.strelizia.arknights.model.SourcePlace;
import com.strelizia.arknights.service.MaterialService;
import com.strelizia.arknights.util.DescriptionTransformationUtil;
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
    public String ZhuanJingCaiLiao(String[] args) {
        List<MaterialInfo> materialInfos;

        if (args.length == 4){
            //四个参数就是##专精材料-干员—第几技能-专精等级
            Integer index = DescriptionTransformationUtil.ChangeStringToInt(args[2]);
            Integer level = DescriptionTransformationUtil.ChangeStringToInt(args[3]);
            materialInfos = skillMateryMapper.selectSkillUpByAgentAndIndex(args[1], index, level);
        }else if (args.length == 3){
            //三个参数就是##专精材料-技能名-专精等级
            Integer level = DescriptionTransformationUtil.ChangeStringToInt(args[2]);
            materialInfos = skillMateryMapper.selectSkillUpBySkillName(args[1], level);
        }else {
            materialInfos = null;
        }

        String s = "";
        if (materialInfos ==null||materialInfos.size() == 0){
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
        List<MaterialInfo> materialInfos = operatorEvolveMapper.selectOperatorEvolveByName(agent, level);
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

    @Override
    public String selectAgentData(String name) {
        OperatorData operatorData = operatorEvolveMapper.selectOperatorData(name);
        String s = "未找到该干员数据";
        if (operatorData.getAtk() != null) {
            s = name + "满精英化满级，无信赖无潜能面板为：" +
                    "\n生命上限：" + operatorData.getMaxHp() + "\t攻击：" + operatorData.getAtk() +
                    "\n防御：" + operatorData.getDef() + "\t法术抵抗：" + operatorData.getMagicResistance() +
                    "\n部署费用：" + operatorData.getCost() + "\t阻挡数：" + operatorData.getBlockCnt() +
                    "\n攻击间隔：" + operatorData.getBaseAttackTime() + "s\t再部署：" + operatorData.getRespawnTime() + "s";
        }
        return s;
    }
}
