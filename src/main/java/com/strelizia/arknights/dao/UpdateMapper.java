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
    Integer insertOperator(OperatorInfo operatorInfo);

    Integer selectOperatorIdByName(String name);

    Integer insertOperatorEvolve(OperatorEvolveInfo operatorEvolveInfo);

    Integer insertOperatorSkill(OperatorSkillInfo operatorSkillInfo);

    Integer selectSkillIdByName(String SkillName);

    Integer insertSkillMater(SkillMaterInfo skillMaterInfo);
}
