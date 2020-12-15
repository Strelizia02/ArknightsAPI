package com.strelizia.arknights.util;

import com.strelizia.arknights.model.AgentTagsInfo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangzy
 * @Date 2020/12/14 18:15
 **/
public class TagsUtil {

    public static boolean isHave(String[] str,String s){
        int i = str.length;
        while (i-- > 0){
            if(str[i] == s){
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

    public static void main(String[] args) {
        String a = "远程位,治疗,支援机械,医疗,近战位,支援,支援机械,近卫,近战位,新手,先锋,远程位,新手,术师,远程位,新手,术师,远程位,新手,狙击,近战位,新手,重装,近战位,费用回复,先锋,近战位,费用回复,先锋,近战位,输出,费用回复,先锋,远程位,治疗,医疗,远程位,治疗,医疗,远程位,群攻,术师,远程位,输出,术师,远程位,输出,狙击,远程位,输出,狙击,远程位,减速,辅助,近战位,输出,生存,近卫,近战位,防护,重装,近战位,费用回复,输出,先锋,近战位,输出,费用回复,先锋,远程位,治疗,医疗,远程位,治疗,医疗,削弱,远程位,输出,术师,远程位,群攻,术师,近战位,快速复活,防护,特种,近战位,位移,特种,近战位,位移,特种,远程位,群攻,减速,狙击,远程位,输出,削弱,狙击,远程位,输出,生存,狙击,远程位,减速,辅助,近战位,支援,输出,近卫,近战位,群攻,生存,近卫,近战位,输出,近卫,近战位,减速,输出,近卫,输出,近战";
        String[] split = a.split(",");
        List<String> strings = Arrays.asList(split);
        List<String> newList = new ArrayList<>();
        Set set = new HashSet();
        for (String s:strings){
            if (set.add(s)){
                newList.add(s);
            }
        }
        System.out.println(newList);
    }

}
