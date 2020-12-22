package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.UpdateMapper;
import com.strelizia.arknights.model.OperatorEvolveInfo;
import com.strelizia.arknights.model.OperatorInfo;
import com.strelizia.arknights.model.OperatorSkillInfo;
import com.strelizia.arknights.model.SkillMaterInfo;
import com.strelizia.arknights.service.UpdateDataService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzy
 * @Date 2020/12/19 15:46
 **/
@Service
@Slf4j
public class UpdateDataServiceImpl implements UpdateDataService {

    @Autowired
    private UpdateMapper updateMapper;

    @Autowired
    protected RestTemplate restTemplate;

    @Override
    public String updateAllData() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type","application/json");
        httpHeaders.set("Authorization","767bf19e16083717928893911e");
        InetSocketAddress host = new InetSocketAddress("118.123.241.137",80);
        httpHeaders.setHost(host);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        //发送请求，封装结果数据
        String body = restTemplate
                .getForObject("https://andata.somedata.top/data-2020/char/list/8989823718061.json", String.class, httpEntity);
        return body;
    }

    @Override
    public Integer updateByJson(String json) {
        Map<String, Integer> operatorClass = new HashMap<>(8);
        operatorClass.put("先锋", 1);
        operatorClass.put("WARRIOR", 2);
        operatorClass.put("重装", 3);
        operatorClass.put("狙击", 4);
        operatorClass.put("术师", 5);
        operatorClass.put("辅助", 6);
        operatorClass.put("医疗", 7);
        operatorClass.put("特种", 8);
        JSONObject jsonObj = new JSONObject(json);
        String name = jsonObj.getString("name");
        int rarity = jsonObj.getInt("rarity") + 1;
        jsonObj.getString("position");
        boolean isNotObtainable = jsonObj.getBoolean("isNotObtainable");

        //封装干员信息
        OperatorInfo operatorInfo = new OperatorInfo();
        operatorInfo.setOperator_name(name);
        operatorInfo.setOperator_rarity(rarity);
        if (isNotObtainable == false) {
            operatorInfo.setAvailable(1);
        } else {
            operatorInfo.setAvailable(0);
        }
        operatorInfo.setIn_limit(0);
        operatorInfo.setOperator_class(operatorClass.get(jsonObj.getString("profession")));
        updateMapper.insertOperator(operatorInfo);
        log.info("更新{}干员基础信息",name);
        Integer operatorId = updateMapper.selectOperatorIdByName(name);

        //封装干员精英化花费
        for (int i = 1; i <= 2; i++) {
        JSONArray evolveJson = jsonObj.getJSONArray("phases").getJSONObject(i).getJSONArray("evolveCost");
        for (int j = 0; j < evolveJson.length(); j++) {
            JSONObject evolve = evolveJson.getJSONObject(j);
            //精英i花费
            OperatorEvolveInfo operatorEvolveInfo = new OperatorEvolveInfo();
            operatorEvolveInfo.setOperatorId(operatorId);
            operatorEvolveInfo.setEvolveLevel(i);
            operatorEvolveInfo.setUseMaterialId(evolve.getInt("id"));
            operatorEvolveInfo.setUseNumber(evolve.getInt("count"));
            updateMapper.insertOperatorEvolve(operatorEvolveInfo);
            log.info("更新{}干员精英{}信息",name,i);
            }
        }

        //封装干员技能
        JSONArray skills = jsonObj.getJSONArray("skills");
        for (int i = 0; i < skills.length(); i++){
            OperatorSkillInfo operatorSkillInfo = new OperatorSkillInfo();
            operatorSkillInfo.setOperatorId(operatorId);
            operatorSkillInfo.setSkillIndex(i+1);
            String skillName = name + (i + 1) + "技能";
            operatorSkillInfo.setSkillName(skillName);
            updateMapper.insertOperatorSkill(operatorSkillInfo);
            log.info("更新{}干员{}技能基础信息，技能名需手动修改",name,i+1);
            Integer skillId = updateMapper.selectSkillIdByName(skillName);
            //获取技能等级列表(专一专二专三)
            JSONArray levelUpCostCond = skills.getJSONObject(i).getJSONArray("levelUpCostCond");
            //该技能专j+1的花费
            for (int j = 0; j < levelUpCostCond.length(); j++){
                JSONArray levelUpCost = levelUpCostCond.getJSONObject(j).getJSONArray("levelUpCost");
                for (int k = 0; k < levelUpCost.length(); k++){
                    SkillMaterInfo skillMaterInfo = new SkillMaterInfo();
                    skillMaterInfo.setSkillId(skillId);
                    skillMaterInfo.setMaterLevel(j+1);
                    skillMaterInfo.setUseMaterialId(levelUpCost.getJSONObject(k).getInt("id"));
                    skillMaterInfo.setUseNumber(levelUpCost.getJSONObject(k).getInt("count"));
                    updateMapper.insertSkillMater(skillMaterInfo);
                    log.info("更新{}干员{}技能专精{}信息",name,i+1,j+1);
                }
            }
        }
        return skills.length();
    }
}
