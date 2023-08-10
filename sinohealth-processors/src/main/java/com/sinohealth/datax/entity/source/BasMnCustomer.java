package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

/**
 * @author mingqiang
 * @date 2023/8/9 - 15:42
 * @desc
 */
public class BasMnCustomer {

    private String vid;

    private String name;

    private Integer sex;

    @db_alias("birth_date")
    private Date birthDate;

    @db_alias("book_time")
    private Date bookTime;

    @db_alias("shop_no")
    private String shopNo;

    private Integer age;

    @db_alias("id_card")
    private String idCard;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
