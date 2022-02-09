package com.strelizia.arknights.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 差异哈希算法
 */
public class DHashUtil {
    /**
     * 计算dHash方法
     *
     * @param url 文件路径
     * @return hash
     */
    public static String getDHash(String url) {
        //读取文件
        BufferedImage srcImage;
        try {
            URL u = new URL(url);
            srcImage = ImageIO.read(u);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        //文件转成9*8像素，为算法比较通用的长宽
        BufferedImage buffImg = new BufferedImage(9, 8, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = buffImg.getGraphics();
        graphics.drawImage(srcImage.getScaledInstance(9, 8, Image.SCALE_SMOOTH), 0, 0, null);
        graphics.dispose();
        int width = buffImg.getWidth();
        int height = buffImg.getHeight();
        int[][] grayPix = new int[width][height];
        StringBuilder figure = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //图片灰度化
                int rgb = buffImg.getRGB(x, y);
                int r = rgb >> 16 & 0xff;
                int g = rgb >> 8 & 0xff;
                int b = rgb & 0xff;
                int gray = (r * 30 + g * 59 + b * 11) / 100;
                grayPix[x][y] = gray;

                //开始计算dHash 总共有9*8像素 每行相对有8个差异值 总共有 8*8=64 个
                if(x != 0) {
                    long bit = grayPix[x-1][y] > grayPix[x][y] ? 1 : 0;
                    figure.append(bit);
                }
            }
        }
        buffImg = null;
        return figure.toString();
    }

    /**
     * 计算海明距离
     * <p>
     * 原本用于编码的检错和纠错的一个算法
     * 现在拿来计算相似度，如果差异值小于一定阈值则相似，一般经验值小于5为同一张图片
     *
     * @param str1
     * @param str2
     * @return 距离
     */
    public static long getHammingDistance(String str1, String str2) {
        int distance;
        if (str1 == null || str2 == null || str1.length() != str2.length()) {
            distance = -1;
        } else {
            distance = 0;
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) != str2.charAt(i)) {
                    distance++;
                }
            }
        }
        return distance;
    }

    //DHashUtil 参数值为待处理文件夹
//    public static void main(String[] args) {
//        String dHash1 = getDHash("http://gchat.qpic.cn/gchatpic_new/412459523/901158551-2534335053-31F9DEF76F48683FDFBBE58CB129F85B/0?vuin=2398095263&term=255&pictype=0");
//        System.out.println("图片1hash值："+ dHash1);
//        String dHash2 = getDHash("http://gchat.qpic.cn/gchatpic_new/412459523/901158551-2534335053-502A7BE94A252EC7FC7B6E497AA2DDE2/0?vuin=3022645754\\u0026term=255\\u0026pictype=0");
//        System.out.println("图片2hash值："+ dHash2);
//        getHammingDistance(dHash1, "0001111110100110001111010010001100100011001001110010011100101101");
//        getHammingDistance(dHash2, "0001111110100110001111010010001100110011001101110010011100101101");
//    }
}
