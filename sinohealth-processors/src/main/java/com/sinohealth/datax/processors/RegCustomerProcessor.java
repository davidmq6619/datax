package com.sinohealth.datax.processors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.sinohealth.datax.common.CommonData;
import com.sinohealth.datax.common.Processor;
import com.sinohealth.datax.entity.source.BasCustomer;
import com.sinohealth.datax.entity.source.RegCustomer;
import com.sinohealth.datax.entity.source.StandardCustomerRecord;
import com.sinohealth.datax.entity.zktarget.StandardCustomerRecordList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author mingqiang
 * @date 2022/08/29
 * 获取用户基本信息
 **/
public class RegCustomerProcessor implements Processor<BasCustomer, StandardCustomerRecordList> {

    Logger logger = LoggerFactory.getLogger(RegCustomerProcessor.class);
    @Override
    public StandardCustomerRecordList dataProcess(BasCustomer customer, StandardCustomerRecordList list, CommonData commonData) {
        ArrayList<StandardCustomerRecord> listRecord = new ArrayList<>();
        StandardCustomerRecord standardCustomerRecord = new StandardCustomerRecord();
        standardCustomerRecord.setCleanTime(new Date());
        standardCustomerRecord.setVid(customer.getMemberId());
        standardCustomerRecord.setName(customer.getName());
        standardCustomerRecord.setCustomerSfzh(customer.getSfzh());
        standardCustomerRecord.setAddress(customer.getAddress());
        standardCustomerRecord.setMaritalStatus(customer.getMaritalStatus());
        standardCustomerRecord.setNation(customer.getNation());
        StringBuilder str = new StringBuilder();
        try {
            if (customer.getCheckTime() != null) {
                standardCustomerRecord.setApplyTime(customer.getCheckTime());
            }else {
                standardCustomerRecord.setApplyTime(null);
                str.append("体检日期为空。");
            }
            String tel = customer.getTel();
            if (StrUtil.isNotEmpty(tel)) {
                standardCustomerRecord.setMobile(tel);
            } else {
                str.append("手机号码为空或者 无效。");
            }
            String sfzh = customer.getSfzh();
            if (customer.getBirthday() != null) {
                standardCustomerRecord.setCustomerCsrq(customer.getBirthday());
            } else if (StrUtil.isNotBlank(sfzh)) {
                boolean validCard = IdcardUtil.isValidCard(sfzh);
                if (validCard) {
                    DateTime birthDate = IdcardUtil.getBirthDate(sfzh);
                    standardCustomerRecord.setCustomerCsrq(birthDate);
                }
            } else {
                str.append("出生日期为空。");
            }
            if (standardCustomerRecord.getCustomerCsrq() != null && standardCustomerRecord.getApplyTime() != null) {
                try {
                    long age = DateUtil.betweenYear(standardCustomerRecord.getCustomerCsrq()
                            , standardCustomerRecord.getApplyTime(), true);
                    standardCustomerRecord.setAge(String.valueOf(age));
                    if (age < 0 || age > 120) {
                        str.append("年龄范围异常。");
                    }
                } catch (Exception e) {
                    str.append("年龄计数异常。");
                }
            }
            String sexStr = customer.getSex();
            if (StrUtil.isNotBlank(sexStr)) {
                sexStr = sexStr.trim();
                if ("男".equals(sexStr)) {
                    standardCustomerRecord.setSex("1");
                } else if ("女".equals(sexStr)) {
                    standardCustomerRecord.setSex("0");
                } else if ("1".equals(sexStr)) {
                    standardCustomerRecord.setSex("1");
                } else if ("0".equals(sexStr)) {
                    standardCustomerRecord.setSex("0");
                } else {
                    str.append("性别异常。");
                }
            } else {
                str.append("性别异常。");
            }
            String remark = str.toString();
            if (StrUtil.isNotBlank(remark)) {
                standardCustomerRecord.setRemark(remark);
                standardCustomerRecord.setCleanStatus(6);
            } else {
                standardCustomerRecord.setCleanStatus(1);
            }
        } catch (Exception e) {
            logger.error("用户数据清洗异常,用户入参数据【{}】，异常【{}】", JSONUtil.toJsonStr(customer), e.getMessage(), e);
        }
        listRecord.add(standardCustomerRecord);
        list.setList(listRecord);
        return list;
    }
}
