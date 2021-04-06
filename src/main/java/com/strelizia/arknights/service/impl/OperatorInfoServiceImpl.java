package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.OperatorInfoMapper;
import com.strelizia.arknights.model.OperatorBasicInfo;
import com.strelizia.arknights.service.OperatorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangzy
 * @Date 2021/3/29 17:36
 **/
@Service
public class OperatorInfoServiceImpl implements OperatorInfoService {

    @Autowired
    private OperatorInfoMapper operatorInfoMapper;


    @Override
    public String getOperatorByInfos(String[] infos) {
        List<String> operators = operatorInfoMapper.getAllOperator();
        StringBuilder s = new StringBuilder("符合 ");
        for (int i = 1; i < infos.length; i++){
            String info = infos[i];
            if (info == null)
            {
                break;
            }
            List<String> operatorNameByInfo = operatorInfoMapper.getOperatorNameByInfo(info);
            operators.retainAll(operatorNameByInfo);
            s.append(info).append(" ");
        }
        s.append("条件的干员为：\n");
        for (String name: operators){
            s.append(name).append("\n");
        }
        return s.toString();
    }

    @Override
    public String getOperatorInfo(String name, String where) {
        OperatorBasicInfo operatorInfoByName = operatorInfoMapper.getOperatorInfoByName(name);
        String s = name + "干员的档案为：\n";
        if (where == null){
            s += operatorInfoByName.toString();
        }else {
            switch (where){
                case "基础档案":
                    s += "基础档案：\n" +
                            "画师：" + operatorInfoByName.getDrawName() + '\t' +
                            "声优：" + operatorInfoByName.getInfoName() + '\n' +
                            "代号：" + operatorInfoByName.getCodeName() + '\t' +
                            "性别：" + operatorInfoByName.getSex() + '\t' +
                            "出身地：" + operatorInfoByName.getComeFrom() + '\n' +
                            "生日：" + operatorInfoByName.getBirthday() + '\t' +
                            "种族：" + operatorInfoByName.getRace() + '\t' +
                            "身高：" + operatorInfoByName.getHeight() + '\n' +
                            "矿石病感染情况：" + operatorInfoByName.getInfection();
                    break;
                case "综合体检测试":
                    s += operatorInfoByName.getComprehensiveTest();
                    break;
                case "客观履历":
                    s += operatorInfoByName.getObjectiveResume();
                    break;
                case "临床诊断分析":
                    s += operatorInfoByName.getClinicalDiagnosis();
                    break;
                case "档案资料一":
                    s += operatorInfoByName.getArchives1();
                    break;
                case "档案资料二":
                    s += operatorInfoByName.getArchives2();
                    break;
                case "档案资料三":
                    s += operatorInfoByName.getArchives3();
                    break;
                case "档案资料四":
                    s += operatorInfoByName.getArchives4();
                    break;
                case "晋升记录":
                case "晋升资料":
                    s += operatorInfoByName.getPromotionInfo();
                    break;
            }
        }
        return s;
    }

    @Override
    public String getCVByName(String str) {
        List<String> allCV;
                StringBuilder s = new StringBuilder();
        if (str == null){
            allCV = operatorInfoMapper.getAllInfoName();
        }else {
            allCV = operatorInfoMapper.getAllInfoNameLikeStr(str);
        }
        for (String name: allCV){
            s.append(name).append('\n');
        }
        return s.toString();
    }

    @Override
    public String getDrawByName(String str) {
        List<String> allDraw;
        StringBuilder s = new StringBuilder();
        if (str == null){
            allDraw = operatorInfoMapper.getAllDrawName();
        }else {
            allDraw = operatorInfoMapper.getAllDrawNameLikeStr(str);
        }
        for (String name: allDraw){
            s.append(name).append('\n');
        }
        return s.toString();
    }


}
