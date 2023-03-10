package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;

/**
 * 检验结果Result
 *
 * @author mingqiang
 * @version 1766517
 */
public class BasCheckItemTemp extends BasTestItemTemp implements Serializable {

    @db_alias("image_describe")
    public String imageDescribe;                //影像描述
    @db_alias("image_diagnose")                 //影像结论
    public String imageDiagnose;

    public String getImageDescribe() {
        return imageDescribe;
    }

    public void setImageDescribe(String imageDescribe) {
        this.imageDescribe = imageDescribe;
    }

    public String getImageDiagnose() {
        return imageDiagnose;
    }

    public void setImageDiagnose(String imageDiagnose) {
        this.imageDiagnose = imageDiagnose;
    }
}