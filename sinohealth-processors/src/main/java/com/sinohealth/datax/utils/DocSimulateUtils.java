package com.sinohealth.datax.utils;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.sinohealth.datax.entity.common.BasInspectionKeyword;
import com.sinohealth.datax.entity.common.BasItemAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mingqiang
 * @date 2022/10/23 - 17:28
 * @desc
 */
public class DocSimulateUtils {


    private static final Logger LOGGER = LoggerFactory.getLogger(DocSimulateUtils.class);
    public static List<BasInspectionKeyword> calcuKeyWordSimu(String item, String clause,
                                                              List<BasInspectionKeyword> inspectionKeywords) {
        Map<String, Double> simuMap = new HashMap<>();
        for (BasInspectionKeyword inspectionKeyword : inspectionKeywords) {
            if (clause != null && clause.trim().length() > 0 && inspectionKeyword.getDiseaseSigns() != null
                    && inspectionKeyword.getDiseaseSigns().trim().length() > 0) {
                Map<Integer, int[]> AlgorithmMap = new HashMap<Integer, int[]>();
                // 将两个字符串中的中文字符以及出现的总数封装到，AlgorithmMap中
                for (int i = 0; i < clause.length(); i++) {
                    char d1 = clause.charAt(i);
                    if (isSymbol(d1)) {// 标点和数字不处理
                        int charIndex = getGB2312Id(d1, isHanzi(d1));// 保存字符对应的GB2312编码
                        if (charIndex != -1) {
                            int[] fq = AlgorithmMap.get(charIndex);
                            if (fq != null && fq.length == 2) {
                                fq[0]++;// 已有该字符，加1
                            } else {
                                fq = new int[2];
                                fq[0] = 1;
                                fq[1] = 0;
                                AlgorithmMap.put(charIndex, fq);// 新增字符入map
                            }
                        }
                    }
                }

                for (int i = 0; i < inspectionKeyword.getDiseaseSigns().length(); i++) {
                    char d2 = inspectionKeyword.getDiseaseSigns().charAt(i);
                    if (isSymbol(d2)) {
                        int charIndex = getGB2312Id(d2, isHanzi(d2));
                        if (charIndex != -1) {
                            int[] fq = AlgorithmMap.get(charIndex);
                            if (fq != null && fq.length == 2) {
                                fq[1]++;
                            } else {
                                fq = new int[2];
                                fq[0] = 0;
                                fq[1] = 1;
                                AlgorithmMap.put(charIndex, fq);
                            }
                        }
                    }
                }

                Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
                double sqClause = 0;
                double sqInspectionKeyword = 0;
                double denominator = 0;
                while (iterator.hasNext()) {
                    int[] c = AlgorithmMap.get(iterator.next());
                    denominator += c[0] * c[1];
                    sqClause += c[0] * c[0];
                    sqInspectionKeyword += c[1] * c[1];
                }
                double d = denominator / Math.sqrt(sqClause * sqInspectionKeyword);// 余弦计算
                simuMap.put(inspectionKeyword.getDiseaseSigns(), d);
            } else {
                break;
            }
        }
        if (simuMap.isEmpty()) {
            return null;
        } else {
            // 对simuMap根据相似性大小排序
            simuMap = simuMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue(), (x1, x2) -> x2, LinkedHashMap::new));
//			// 过滤掉没有进行计算的疾病
//			simuMap = simuMap.entrySet().stream().filter(r -> r.getValue() <= 3)
//					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            String mostSimuDiseaseSigns = simuMap.entrySet().iterator().next().getKey();

            List<BasInspectionKeyword> resultList = inspectionKeywords.stream()
                    .filter(ik -> ik.getDiseaseSigns().equals(mostSimuDiseaseSigns)).collect(Collectors.toList());

            resultList = resultList.stream().filter(ik -> ik.getMethod().equals(item)).collect(Collectors.toList());
            return resultList;
        }
    }

    public static List<BasItemAlias> calcuAliasSimu(String alias, List<BasItemAlias> allItemAlias) {
        Map<String, Double> simuMap = new HashMap<>();
        for (BasItemAlias itemAlias : allItemAlias) {
            if (alias != null && alias.trim().length() > 0 && itemAlias.getAliasName() != null
                    && itemAlias.getAliasName().trim().length() > 0) {
                Map<Integer, int[]> AlgorithmMap = new HashMap<Integer, int[]>();
                // 将两个字符串中的中文字符以及出现的总数封装到，AlgorithmMap中
                for (int i = 0; i < alias.length(); i++) {
                    char d1 = alias.charAt(i);
                    if (isSymbol(d1)) {// 标点和数字不处理
                        int charIndex = getGB2312Id(d1, isHanzi(d1));// 保存字符对应的GB2312编码
                        if (charIndex != -1) {
                            int[] fq = AlgorithmMap.get(charIndex);
                            if (fq != null && fq.length == 2) {
                                fq[0]++;// 已有该字符，加1
                            } else {
                                fq = new int[2];
                                fq[0] = 1;
                                fq[1] = 0;
                                AlgorithmMap.put(charIndex, fq);// 新增字符入map
                            }
                        }
                    }
                }

                for (int i = 0; i < itemAlias.getAliasName().length(); i++) {
                    char d2 = itemAlias.getAliasName().charAt(i);
                    if (isSymbol(d2)) {
                        int charIndex = getGB2312Id(d2, isHanzi(d2));
                        if (charIndex != -1) {
                            int[] fq = AlgorithmMap.get(charIndex);
                            if (fq != null && fq.length == 2) {
                                fq[1]++;
                            } else {
                                fq = new int[2];
                                fq[0] = 0;
                                fq[1] = 1;
                                AlgorithmMap.put(charIndex, fq);
                            }
                        }
                    }
                }

                Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
                double sqClause = 0;
                double sqInspectionKeyword = 0;
                double denominator = 0;
                while (iterator.hasNext()) {
                    int[] c = AlgorithmMap.get(iterator.next());
                    denominator += c[0] * c[1];
                    sqClause += c[0] * c[0];
                    sqInspectionKeyword += c[1] * c[1];
                }
                double d = denominator / Math.sqrt(sqClause * sqInspectionKeyword);// 余弦计算
                simuMap.put(itemAlias.getAliasName(), d);
            } else {
                break;
            }
        }
        if (simuMap.isEmpty()) {
            return null;
        } else {
            // 对simuMap根据相似性大小排序
            simuMap = simuMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue(), (x1, x2) -> x2, LinkedHashMap::new));
//			// 过滤掉没有进行计算的疾病
//			simuMap = simuMap.entrySet().stream().filter(r -> r.getValue() <= 3)
//					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            String mostSimuAlias = simuMap.entrySet().iterator().next().getKey();

            List<BasItemAlias> resultList = allItemAlias.stream().filter(ik -> ik.getAliasName().equals(mostSimuAlias))
                    .collect(Collectors.toList());

            return resultList;
        }
    }

    public static BasItemAlias calcuAliasContain(String itemAlias, List<BasItemAlias> allItemAlias) {
        List<BasItemAlias> resultList = Lists.newArrayList();
        BasItemAlias resultAlias = null;
        for (BasItemAlias basItemAlias : allItemAlias) {
            if (itemAlias.equals(basItemAlias.getAliasName())) {
                resultList.add(basItemAlias);
            }
        }
        if (resultList.isEmpty()) {
            return null;
        } else {
            if (resultList.size() > 1) {
                // 判断哪个在检查项中的索引最近
                for (int i = 0; i < resultList.size() - 1; i++) {
                    int index1 = itemAlias.indexOf(resultList.get(i).getAliasName());
                    int index2 = itemAlias.indexOf(resultList.get(i + 1).getAliasName());
                    resultAlias = index1 > index2 ? resultList.get(i + 1) : resultList.get(i);
                }
                return resultAlias;
            } else {
                return resultList.get(0);
            }
        }
    }

    // 判断字符是否为汉字或字母或数字
    public static boolean isSymbol(char ch) {
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";// 其他需要，直接修改正则表达式就好
        return String.valueOf(ch).matches(regex);
    }

    // 判断是否汉字
    public static boolean isHanzi(char ch) {
        return (ch >= 0x4E00 && ch <= 0x9FA5);
    }

    public static short getGB2312Id(char ch, boolean isHanzi) {
        try {
            byte[] buffer = Character.toString(ch).getBytes("GB2312");
            int b0 = (int) (buffer[0] & 0x0FF) - 161; // 编码从A1开始，因此减去0xA1=161
            int b1 = 0;
            if (isHanzi) {
                if (buffer.length != 2) {
                    // 正常情况下buffer应该是两个字节，否则说明ch不属于GB2312编码，故返回'?'，此时说明不认识该字符
                    return -1;
                }
                b1 = (int) (buffer[1] & 0x0FF) - 161;
            }
            return (short) (b0 * 94 + b1);// 第一个字符和最后一个字符没有汉字，因此每个区只收16*6-2=94个汉字
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("异常",e);
        }
        return -1;
    }
}
