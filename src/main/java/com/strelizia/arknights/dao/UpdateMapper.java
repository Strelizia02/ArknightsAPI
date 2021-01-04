package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.OperatorEvolveInfo;
import com.strelizia.arknights.model.OperatorInfo;
import com.strelizia.arknights.model.OperatorSkillInfo;
import com.strelizia.arknights.model.SkillMaterInfo;

/**
 * @author wangzy
 * @Date 2020/12/19 18:42
 **/
public interface UpdateMapper {

    //插入一个干员信息
    Integer insertOperator(OperatorInfo operatorInfo);

    //根据名字查询一个干员id
    Integer selectOperatorIdByName(String name);

    //插入一个干员精英化材料信息
    Integer insertOperatorEvolve(OperatorEvolveInfo operatorEvolveInfo);

    //插入一个干员技能信息
    Integer insertOperatorSkill(OperatorSkillInfo operatorSkillInfo);

    //根据技能名获取技能id
    Integer selectSkillIdByName(String SkillName);

    //插入一个技能升级材料信息
    Integer insertSkillMater(SkillMaterInfo skillMaterInfo);

    //清空数据库重新插入
    Integer clearOperatorData();
}
