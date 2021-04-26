package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.NickNameMapper;
import com.strelizia.arknights.dao.SkinInfoMapper;
import com.strelizia.arknights.model.SkinInfo;
import com.strelizia.arknights.service.SkinInfoService;
import com.strelizia.arknights.util.SendMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangzy
 * @Date 2021/4/8 11:29
 **/
@Service
public class SkinInfoServiceImpl implements SkinInfoService {

    @Autowired
    private SkinInfoMapper skinInfoMapper;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Autowired
    private NickNameMapper nickNameMapper;


    @Override
    public String getOperatorSkinByInfo(Long groupId, String info) {

        String realName = nickNameMapper.selectNameByNickName(info);
        if (realName != null && !realName.equals(""))
            info = realName;

        List<SkinInfo> skinInfos = skinInfoMapper.selectSkinByInfo(info);
        String s = "没有找到对应的皮肤";
        if (info.equals("艾雅法拉") || info.equals("小羊")) {
            if (skinInfos == null || skinInfos.size() <= 0) {
                return "小羊马上就有泳装了！！！！！";
            }
        } else if (info.equals("蓝毒")) {
            if (skinInfos == null || skinInfos.size() <= 0) {
                return "蓝毒泳装免费送！！！！！";
            }
        }


        if (skinInfos != null && skinInfos.size() > 0) {
            if (skinInfos.size() <= 5) {
                for (SkinInfo skinInfo : skinInfos) {
                    String result = skinInfo.getOperatorName() + " " + skinInfo.getSkinName() +
                            "\n画师：" + skinInfo.getDrawerName() + " " + skinInfo.getSkinGroupName() + "系列\n" +
                            skinInfo.getDialog();
                    sendMsgUtil.CallOPQApiSendImg(groupId, result, SendMsgUtil.picBase64Buf, skinInfo.getSkinBase64(), 2);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return "";
            } else {
                String result = "当前搜索结果过多，如需查看皮肤立绘请缩小搜索范围";
                for (SkinInfo skinInfo : skinInfos) {
                    result += "\n干员名：" + skinInfo.getOperatorName() + " 皮肤名：" + skinInfo.getSkinName() +
                            "\n画师：" + skinInfo.getDrawerName() + " " + skinInfo.getSkinGroupName() + "系列\n" +
                            skinInfo.getDialog() + "\n";
                }
                return result;
            }

        }

        return s;
    }
}
