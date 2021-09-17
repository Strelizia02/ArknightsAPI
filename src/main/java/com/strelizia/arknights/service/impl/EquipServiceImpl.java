package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.EquipMapper;
import com.strelizia.arknights.dao.MaterialMadeMapper;
import com.strelizia.arknights.dao.NickNameMapper;
import com.strelizia.arknights.model.EquipBuff;
import com.strelizia.arknights.model.EquipInfo;
import com.strelizia.arknights.model.MaterialInfo;
import com.strelizia.arknights.model.Text;
import com.strelizia.arknights.service.EquipService;
import com.strelizia.arknights.util.ImageUtil;
import com.strelizia.arknights.util.SendMsgUtil;
import com.strelizia.arknights.util.TextToImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.strelizia.arknights.util.ImageUtil.replaceEnter;

@Service
public class EquipServiceImpl implements EquipService {

    @Autowired
    private EquipMapper equipMapper;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Autowired
    private MaterialMadeMapper materialMadeMapper;

    @Autowired
    private NickNameMapper nickNameMapper;

    @Override
    public String getOperatorEquip(Long qq, Long groupId, String name) {
        String realName = nickNameMapper.selectNameByNickName(name);
        if (realName != null && !realName.equals(""))
            name = realName;

        EquipInfo equipInfo = equipMapper.selectEquipByName(name);
        if (equipInfo != null) {
            String equipId = equipInfo.getEquipId();
            List<EquipBuff> equipBuffs = equipMapper.selectEquipBuffById(equipId);
            List<MaterialInfo> materialInfos = equipMapper.selectEquipCostById(equipId);
            List<String> strings = equipMapper.selectEquipMissionById(equipId);
            StringBuilder s = new StringBuilder("");
            s.append("干员").append(name).append("的模组信息为：\n")
                .append("  模组名称： ").append(equipInfo.getEquipName()).append("\n")
                .append("  模组特性： ").append(equipInfo.getDesc()).append("\n")
                .append("  解锁等级： 专精").append(equipInfo.getPhase()).append(" ").append(equipInfo.getLevel()).append("级\n");
            int i = 1;
            s.append("  解锁条件：\n");
            for (String mission : strings){
                   s.append("  ").append(i).append(".").append(mission).append("\n");
                i++;
            }
            for (EquipBuff e : equipBuffs) {
                String value = "";
                if (e.getValue() >= 0){
                    value = "+" + e.getValue();
                }else {
                    value = "-" + e.getValue();
                }
                s.append("  面板变化： ").append(returnBuffName(e.getBuffName())).append(" ").append(value).append("\n");
            }
            s.append("  解锁材料： \n");
            try {
                sendImageWithPic(groupId, materialInfos, s.toString(), qq);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
        else {
            return "未找到干员对应模组信息";
        }
    }

    public String returnBuffName(String BuffId){
        Map<String, String> map = new HashMap<>();
        map.put("max_hp", "生命上限");
        map.put("atk", "攻击");
        map.put("def", "防御");
        map.put("magicResistance", "法术抵抗");
        map.put("cost", "部署费用");
        map.put("blockCnt", "阻挡数");
        map.put("baseAttackTime", "攻击间隔");
        map.put("respawnTime", "再部署");
        map.put("attack_speed", "攻击速度");

        if (map.get(BuffId) != null) {
            return map.get(BuffId);
        }
        else {
            return BuffId;
        }
    }

    public void sendImageWithPic(Long groupId, List<MaterialInfo> materialInfos, String s, Long qq) throws Exception {
        Text t = new Text(s);
        Font font = new Font("楷体", Font.PLAIN, 100);

        // 获取font的样式应用在str上的整个矩形
        int[] arr = TextToImage.getWidthAndHeight(t, font);
        int width = arr[0];
        int height = arr[1] + 100 * materialInfos.size() + 120;
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);//创建图片画布
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE); // 先用白色填充整张图片,也就是背景
        g.fillRect(0, 0, width, height);//画出矩形区域，以便于在矩形区域内写入文字
        g.setColor(Color.black);// 再换成黑色，以便于写入文字
        g.setFont(font);// 设置画笔字体
        String[] rows = t.getText();
        Pattern pattern = Pattern.compile("[0-9]*");
        //记录画笔高度
        int gHeight = 0;
        for (int i = 0; i < t.getRowsNum(); i++) {
            gHeight = (i + 1) * font.getSize() + 1;
            if (rows[i].length() > 0 && pattern.matcher(rows[i].charAt(0) + "").matches()) {
                g.setFont(new Font("楷体", Font.BOLD, font.getSize()));
                g.setColor(Color.BLUE);
                g.drawString(rows[i], 0, gHeight);// 画出一行字符串
                g.setFont(font);
                g.setColor(Color.black);
            } else {
                g.drawString(rows[i], 0, gHeight);// 画出一行字符串
            }
        }
        for (int i = 0; i < materialInfos.size(); i++) {
            MaterialInfo m = materialInfos.get(i);
            String imgBase64 = materialMadeMapper.selectMaterialPicByName(m.getMaterialName());
            g.drawImage(ImageUtil.Base64ToImageBuffer(imgBase64), 200, gHeight, 100, 100, null);// 画出材料图标
            g.drawString(m.getMaterialName() + " * " + m.getMaterialNum() + "个", 300, gHeight + font.getSize());
            gHeight += font.getSize();
        }
        g.dispose();
//        File outFile = new File("D:/a.png");
//        try {
//            ImageIO.write(image, "png", outFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        sendMsgUtil.CallOPQApiSendImg(groupId, "[ATUSER(" + qq + ")]", SendMsgUtil.picBase64Buf,
                replaceEnter(new BASE64Encoder().encode(TextToImage.imageToBytes(image))), 2);
    }
}
