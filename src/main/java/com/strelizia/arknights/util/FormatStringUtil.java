package com.strelizia.arknights.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzy
 * @Date 2020/12/10 18:01
 **/
public class FormatStringUtil {
    /**
     * 设置字符长度  不足者 右侧添加 指定字符
     * @param str1 元字符
     * @param lenth 指定长度
     * @param st2 指定字符
     * @return
     * @throws Exception
     */
    public static String strAppendStr(String str1, int lenth, String st2) throws Exception {
        StringBuilder strb1 = new StringBuilder(str1);
        lenth = lenth - getChineseLength(str1, "utf-8");
        while (lenth >= 0) {
            lenth--;
            strb1.append(st2);
        }
        return strb1.toString();
    }

    /**
     * 计算中文字符长度
     * @param name 字符
     * @param endcoding 编码方式
     * @return
     * @throws Exception
     */
    public static int getChineseLength(String name, String endcoding) throws Exception {
        int len = 0; //定义返回的字符串长度
        int j = 0;
        //按照指定编码得到byte[]
        byte[] b_name = name.getBytes(endcoding);
        do {
            short tmpst = (short) (b_name[j] & 0xF0);
            if (tmpst >= 0xB0) {
                if (tmpst < 0xC0) {
                    j += 2;
                    len += 2;
                } else if ((tmpst == 0xC0) || (tmpst == 0xD0)) {
                    j += 2;
                    len += 2;
                } else if (tmpst == 0xE0) {
                    j += 3;
                    len += 2;
                } else {
                    short tmpst0 = (short) (((short) b_name[j]) & 0x0F);
                    if (tmpst0 == 0) {
                        j += 4;
                        len += 2;
                    } else if (tmpst0 < 12) {
                        j += 5;
                        len += 2;
                    } else {
                        j += 6;
                        len += 2;
                    }
                }
            } else {
                j += 1;
                len += 1;
            }
        } while (j <= b_name.length - 1);
        return len;
    }

    public static String FormatStar(int star){
        Map<Integer, String> map = new HashMap<>();
        map.put(6, "★★★★★★");
        map.put(5, "★★★★★");
        map.put(4,"☆☆☆☆");
        map.put(3,"☆☆☆");
        map.put(2,"☆☆");
        map.put(1,"☆");
        String s = map.get(star);
        return s;
    }
}
