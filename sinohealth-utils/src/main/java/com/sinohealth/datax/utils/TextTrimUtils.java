package com.sinohealth.datax.utils;

import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jason 2017年9月28日 上午11:49:03
 * @ClassName: TextTrimUtils
 * @Description: 文本处理工具
 */
public class TextTrimUtils {

    private static final Logger log = LoggerFactory.getLogger(TextTrimUtils.class);

    /**
     * @param sourceText
     * @return
     * @Title: textTrim
     * @Description: 文本去换行，去空格，去英文逗号
     * @author Jason 2017年9月28日 上午11:49:28
     */
    public static String textTrim(String sourceText) {
        String trimText = StringUtils.EMPTY;
        if (sourceText != null) {
            trimText = sourceText.replaceAll("\\r\\n", "").trim().replaceAll("\\s*", "").replaceAll(",", "，")
                    .replaceAll("　", "").replace("（", "(").replace("）", ")")
                    .replace("（", "(").replace("）",")");
        }
        // trimText = trimText.replaceAll(" ", "");
        // trimText = trimText.replaceAll(" ", "");
        return trimText;
    }

    /**
     * @param sourceText
     * @return
     * @Title: textSWTrim
     * @Description: 文本去换行，去首尾空格，去英文逗号
     * @author Jason 2017年11月28日 上午11:49:28
     */
    public static String textSWTrim(String sourceText) {
        String trimText = StringUtils.EMPTY;
        if (sourceText != null) {
            trimText = sourceText.replaceAll("\\r\\n", "").trim().replaceAll(",", "，");
        }
        return trimText;
    }

    public static List<String> textSplit(String sourceText) {
        List<String> textStr = Lists.newArrayList();
        sourceText = textTrim(sourceText);
        Pattern pattern = Pattern.compile("([1-9]|[１-９]\\d*\\.|[1-9]|[１-９]\\d*\\、|[1-9]|[１-９]\\d*\\。|[1-9]|[１-９]\\d*\\，|[1-9]|[１-９]\\d*\\,)");
        Matcher matcher = pattern.matcher(sourceText);
        if (!NumberUtil.isNumber(sourceText) && matcher.find()) {
            String[] arr = sourceText
                    .split("(。|;|；|!|！|，|,|？|）)([1-9]|[１-９]\\d*\\.|[1-9]|[１-９]\\d*\\、|[1-9]|[１-９]\\d*\\。|[1-9]|[１-９]\\d*\\，|[1-9]|[１-９]\\d*\\,)");
            if (arr[0].length() > 2)
                arr[0] = arr[0].substring(2, arr[0].length());
            Arrays.asList(arr).forEach(block -> {
                String[] arr2 = block.split(
                        "([\\u3007\\u3400-\\u4DB5\\u4E00-\\u9FCB\\uE815-\\uE864]|[\\uD840-\\uD87F][\\uDC00-\\uDFFF])([1-9]\\d*\\.|[1-9]\\d*\\、|[1-9]\\d*\\。|[1-9]\\d*\\，|[1-9]\\d*\\,)([\\u3007\\u3400-\\u4DB5\\u4E00-\\u9FCB\\uE815-\\uE864]|[\\uD840-\\uD87F][\\uDC00-\\uDFFF])");
                Integer formIdx = 0;
                for (String block2 : Arrays.asList(arr2)) {
                    Integer start = block.indexOf(block2, formIdx) - 1;
                    if (start == -1)
                        start = 0;
                    Integer end = start + block2.length() + 1;
                    if (formIdx > 0) {
                        end = end + 1;
                    }
                    if (end >= block.length())
                        end = block.length();
                    String newStr = block.substring(start, end);
                    formIdx = formIdx + newStr.length() - 1;
                    textStr.add(newStr);
                }
            });
        } else {
            textStr.add(sourceText);
        }
        return textStr;
    }

    //一句话按照符号断句
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
}

