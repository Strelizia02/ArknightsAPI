package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.service.PetPetService;
import com.strelizia.arknights.util.ImageUtil;
import com.strelizia.arknights.util.PetPetUtil;
import com.strelizia.arknights.util.SendMsgUtil;
import com.strelizia.arknights.util.TextToImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

import static com.strelizia.arknights.util.ImageUtil.replaceEnter;

@Service
public class PetPetServiceImpl implements PetPetService {

    @Autowired
    private PetPetUtil petPetUtil;

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Override
    public void PetPet(Long groupId, Long qq) {
        BufferedImage userImage = ImageUtil.Base64ToImageBuffer(
                imageUtil.getImageBase64ByUrl("http://q.qlogo.cn/headimg_dl?dst_uin=" + qq + "&spec=100"));
        String path = "frame.gif";
        petPetUtil.getGif(path, userImage);
        byte[] data;
        try {
            InputStream in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
            String base = new BASE64Encoder().encode(Objects.requireNonNull(data));
            sendMsgUtil.CallOPQApiSendImg(groupId, null, SendMsgUtil.picBase64Buf, base, 2);
        } catch (IOException ignored) {
        }
            }
}
