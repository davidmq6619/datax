package com.sinohealth.datax.utils;

import com.google.common.collect.Lists;
import com.sinohealth.datax.entity.common.BasItemAlias;

import java.util.List;

/**
 * @author mingqiang
 * @date 2022/9/5 - 14:56
 * @desc 诊断
 */
public class DiagnoseUtils {

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

}
