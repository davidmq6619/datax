package com.sinohealth.datax.processors;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.sinohealth.datax.common.CommonData;
import com.sinohealth.datax.common.Processor;
import com.sinohealth.datax.entity.source.BasCustomer;
import com.sinohealth.datax.entity.source.BasMnCustomer;
import com.sinohealth.datax.entity.source.RegMnCustomer;
import com.sinohealth.datax.entity.source.StandardCustomerRecord;
import com.sinohealth.datax.entity.zktarget.StandardCustomerRecordList;
import com.sinohealth.datax.utils.EtlSTConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;


/**
 * @author mingqiang
 * @date 2022/08/29
 * 获取用户基本信息
 **/
public class RegCustomerProcessor implements Processor<BasMnCustomer, StandardCustomerRecordList> {

    Logger logger = LoggerFactory.getLogger(RegCustomerProcessor.class);
    @Override
    public StandardCustomerRecordList dataProcess(BasMnCustomer customer, StandardCustomerRecordList list, CommonData commonData) {
        ArrayList<RegMnCustomer> listRecord = new ArrayList<>();
        RegMnCustomer standardCustomerRecord = new RegMnCustomer();
        standardCustomerRecord.setVid(customer.getVid());
        standardCustomerRecord.setName(customer.getName());
        standardCustomerRecord.setAge(customer.getAge());
        standardCustomerRecord.setBirthDate(customer.getBirthDate());
        standardCustomerRecord.setBookTime(customer.getBookTime());
        standardCustomerRecord.setIdCard(customer.getIdCard());
        standardCustomerRecord.setSex(customer.getSex());
        standardCustomerRecord.setShopNo(customer.getShopNo());
        StringBuilder str = new StringBuilder();
        try {
            if (standardCustomerRecord.getBirthDate() != null && standardCustomerRecord.getBookTime() != null) {
                try {
                    long age = DateUtil.betweenYear(standardCustomerRecord.getBirthDate()
                            , standardCustomerRecord.getBookTime(), true);
                    standardCustomerRecord.setAge(Integer.valueOf((int) age));
                    if (age < 0 || age > 120) {
                        str.append("年龄范围异常。");
                    }
                } catch (Exception e) {
                    str.append("年龄计数异常。");
                }
            }
            standardCustomerRecord.setStatus(1);
        } catch (Exception e) {
            logger.error("用户数据清洗异常,用户入参数据【{}】，异常【{}】", JSONUtil.toJsonStr(customer), e.getMessage(), e);
        }
        listRecord.add(standardCustomerRecord);
        list.setList(listRecord);
        return list;
    }
}
