package com.sinohealth.datax.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.StringTokenizer;

/**
 * @author mingqiang
 * @date 2022/8/30 - 14:55
 * @desc
 */
public class TextUtils {

    /**
     * 去除空格换行中文逗号
     * @param sourceText
     * @return
     */
    public static String textTrim(String sourceText) {
        String trimText = StringUtils.EMPTY;
        if (sourceText != null) {
            trimText = sourceText.replaceAll("\\r\\n", "")
                    .trim().replaceAll("\\s*", "")
                    .replaceAll(",", "，")
                    .replaceAll(";", "；");
        }
        return trimText;
    }

    public static String[] splitSignsToArrByParam(String s, String splitStr) {
        StringTokenizer str1 = new StringTokenizer(s, splitStr);
        int count = 0;
        String[] temp = new String[str1.countTokens()];

        while (str1.hasMoreTokens()) {
            temp[count] = str1.nextToken();
            count++;
        }
        return temp;
    }

    /*
     * 预处理语句：特殊处理部位针对新一部部位的检测前未加标点的问题，如肝囊肿肾：肾结石
     * 处理方式：根据列表中的名词，若文本中检索到如肾【肾：】则在名词前添加一个逗号，如【，肾：】
     */
    public static String specialAddSigns(String itemResults) {
        if (StrUtil.isNotBlank(itemResults)) {
            /*for (int j = 0; j < EtlConst.specialPartList.size(); j++) {
                if (itemResults.contains(EtlConst.specialPartList.get(j))){
                    itemResults = itemResults.replaceAll(EtlConst.specialPartList.get(j), "。"+EtlConst.specialPartList.get(j));
                }
            }*/
            for (int j = 0; j < EtlConst.specialPartList.size(); j++) {
                if (itemResults.contains(EtlConst.specialPartList.get(j) + ":")
                        || itemResults.contains(EtlConst.specialPartList.get(j) + "：")) {
                    int index = itemResults.indexOf(EtlConst.specialPartList.get(j) + ":") > -1
                            ? itemResults.indexOf(EtlConst.specialPartList.get(j) + ":")
                            : itemResults.indexOf(EtlConst.specialPartList.get(j) + "：");
                    itemResults = addSigns(itemResults, index);
                }
            }
        }
        return itemResults;
    }

    public static String addSigns(String itemResults, int index) {
        StringBuffer stringBuffer = new StringBuffer(itemResults);
        return stringBuffer.insert(index, ",").toString();
    }
}
