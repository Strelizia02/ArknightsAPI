package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.NickNameMapper;
import com.strelizia.arknights.dao.OperatorInfoMapper;
import com.strelizia.arknights.model.OperatorBasicInfo;
import com.strelizia.arknights.model.TalentInfo;
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

    @Autowired
    private NickNameMapper nickNameMapper;


    @Override
    public String getOperatorByInfos(Long qq, String[] infos) {
        List<String> operators = operatorInfoMapper.getAllOperator();
        StringBuilder s = new StringBuilder("符合 ");
        for (int i = 1; i < infos.length; i++) {
            String info = infos[i];
            if (info == null) {
                break;
            }

//            String realName = nickNameMapper.selectNameByNickName(info);
//            if (realName != null && !realName.equals(""))
//                info = realName;

            List<String> operatorNameByInfo = operatorInfoMapper.getOperatorNameByInfo(info);
            operators.retainAll(operatorNameByInfo);
            s.append(info).append(" ");
        }
        s.append("条件的干员为：\n");
        for (String name : operators) {
            s.append(name).append("\n");
        }
        return "[ATUSER(" + qq + ")]" + s.toString();
    }

    @Override
    public String getOperatorInfo(Long qq, String name, String where) {

        String realName = nickNameMapper.selectNameByNickName(name);
        if (realName != null && !realName.equals(""))
            name = realName;

        OperatorBasicInfo operatorInfoByName = operatorInfoMapper.getOperatorInfoByName(name);
        String s = name + "干员的档案为：\n";
        if (where == null) {
            s += operatorInfoByName.toString();
        } else {
            switch (where) {
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
        return "[ATUSER(" + qq + ")]" + s;
    }

    @Override
    public String getCVByName(Long qq, String str) {
        List<String> allCV;
        StringBuilder s = new StringBuilder();
        if (str == null) {
            allCV = operatorInfoMapper.getAllInfoName();
        } else {
            allCV = operatorInfoMapper.getAllInfoNameLikeStr(str);
        }
        for (String name : allCV) {
            s.append(name).append('\n');
        }
        return "[ATUSER(" + qq + ")]" + s.toString();
    }

    @Override
    public String getDrawByName(Long qq, String str) {
        List<String> allDraw;
        StringBuilder s = new StringBuilder();
        if (str == null) {
            allDraw = operatorInfoMapper.getAllDrawName();
        } else {
            allDraw = operatorInfoMapper.getAllDrawNameLikeStr(str);
        }
        for (String name : allDraw) {
            s.append(name).append('\n');
        }
        return "[ATUSER(" + qq + ")]" + s.toString();
    }

    @Override
    public String getTalentByName(Long qq, String name) {

        String realName = nickNameMapper.selectNameByNickName(name);
        if (realName != null && !realName.equals(""))
            name = realName;

        List<TalentInfo> operatorTalent = operatorInfoMapper.getOperatorTalent(name);
        if (operatorTalent != null && operatorTalent.size() > 0) {
            StringBuilder s = new StringBuilder(name).append("干员的天赋为：");
            for (TalentInfo t : operatorTalent) {
                s.append("\n").append(t.getTalentName()).append("\t解锁条件：精英化").append(t.getPhase()).append("等级")
                        .append(t.getLevel()).append("潜能").append(t.getPotential())
                        .append("\n\t").append(t.getDescription());
            }
            return "[ATUSER(" + qq + ")]" + s.toString();
        }
        return "[ATUSER(" + qq + ")]未找到该干员的天赋";
    }


}
