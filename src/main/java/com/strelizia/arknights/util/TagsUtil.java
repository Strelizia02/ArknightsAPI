package com.strelizia.arknights.util;

import java.util.*;;

/**
 * @author wangzy
 * @Date 2020/12/14 18:15
 **/
public class TagsUtil {

    public static boolean isHave(List<String> str, String s){
        int i = str.size();
        while (i-- > 0){
            if(str.get(i).equals(s)){
                return true;
            }
        }
        return false;
    }

    public static List<List<String>> getAllCompose(List<String> list) {
        List<List<String>> result = new ArrayList<>();
        long n = (long)Math.pow(2,list.size());
        List<String> combine;
        for (long l=0L; l<n; l++) {
            combine = new ArrayList<>();
            for (int i=0; i<list.size(); i++) {
                if ((l>>>i&1) == 1)
                    combine.add(list.get(i));
            }
            result.add(combine);
        }
        return result;
    }

}
