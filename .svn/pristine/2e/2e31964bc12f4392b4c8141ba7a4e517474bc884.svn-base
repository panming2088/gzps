package com.augurit.agmobile.gzps.uploadfacility.model;

import android.text.TextUtils;

import com.augurit.agmobile.gzps.uploadfacility.util.PaifangKouAttributeViewUtilNew;
import com.augurit.am.fw.utils.StringUtil;

import java.util.List;

/**
 * 排水口属性
 * Created by xcl on 2017/12/4.
 */

public class PaifangKouAttributesNew {

    /**
     * 属性是否可以为空
     */
    public static final boolean ATTRIBUTE_ONE_CAN_NULL = false;
    public static final boolean ATTRIBUTE_TWO_CAN_NULL = false;
    public static final boolean ATTRIBUTE_THREE_CAN_NULL = false;
    public static final boolean ATTRIBUTE_FOUR_CAN_NULL = false;
    public static final boolean ATTRIBUTE_FIVE_CAN_NULL = false;
    public static final boolean ATTRIBUTE_SIX_CAN_NULL = true;
    public static final boolean ATTRIBUTE_SEVEN_CAN_NULL = false;
    public static final boolean ATTRIBUTE_EIGHT_CAN_NULL = false;
    public static final boolean ATTRIBUTE_NINE_CAN_NULL = false;
    public static final boolean ATTRIBUTE_TEN_CAN_NULL = false;
    public static final boolean ATTRIBUTE_ELEVEN_CAN_NULL = false;
    public static final boolean ATTRIBUTE_TWELVE_CAN_NULL = true;
    public static final boolean ATTRIBUTE_THIRTEEN_CAN_NULL = false;
    public static final boolean ATTRIBUTE_GPBH_CAN_NULL = false;
    private String attributeOne;
    private String attributeTwo;
    private String attributeThree;
    private String attributeFour;
    private String attributeFive;
    private String attributeSix;
    private String attributeSeven;
    private String attributeEight;
    private String attributeNine;
    private String attributeTen;
    private String attributeEleven;
    private String attributeTwelve;
    private String attributeThirteen;



    private String attributeGpbh;
    private double x;
    private double y;
    /**
     * 属性二允许的值
     */
    private List<String> attributeTwoAllowValues;

    /**
     * 属性是否修改过
     */
    private boolean hasModified;

    public String getAttributeFour() {
        return attributeFour;
    }

    public void setAttributeFour(String attributeFour) {
        this.attributeFour = attributeFour;
    }

    public String getAttributeFive() {
        return attributeFive;
    }

    public void setAttributeFive(String attributeFive) {
        this.attributeFive = attributeFive;
    }

    public String getAttributeSix() {
        return attributeSix;
    }

    public void setAttributeSix(String attributeSix) {
        this.attributeSix = attributeSix;
    }

    public String getAttributeSeven() {
        return attributeSeven;
    }

    public void setAttributeSeven(String attributeSeven) {
        this.attributeSeven = attributeSeven;
    }

    public String getAttributeEight() {
        return attributeEight;
    }

    public void setAttributeEight(String attributeEight) {
        this.attributeEight = attributeEight;
    }

    public String getAttributeNine() {
        return attributeNine;
    }

    public void setAttributeNine(String attributeNine) {
        this.attributeNine = attributeNine;
    }

    public String getAttributeTen() {
        return attributeTen;
    }
    public String getAttributeGpbh() {
        return attributeGpbh;
    }

    public void setAttributeGpbh(String attributeGpbh) {
        this.attributeGpbh = attributeGpbh;
    }
    public void setAttributeTen(String attributeTen) {
        this.attributeTen = attributeTen;
    }

    public String getAttributeEleven() {
        return attributeEleven;
    }

    public void setAttributeEleven(String attributeEleven) {
        this.attributeEleven = attributeEleven;
    }

    public String getAttributeTwelve() {
        return attributeTwelve;
    }

    public void setAttributeTwelve(String attributeTwelve) {
        this.attributeTwelve = attributeTwelve;
    }

    public String getAttributeThirteen() {
        return attributeThirteen;
    }

    public void setAttributeThirteen(String attributeThirteen) {
        this.attributeThirteen = attributeThirteen;
    }

    /**
     * 属性是否允许删除  当属性填充不完整时为false;
     */
    private boolean ifAllowUpload;

    /**
     * 填写错误的属性名称
     */
    private String invalidAttributeName;

    /**
     * 错误原因
     */
    private String notAllowUploadReason;

    public boolean isHasModified() {
        return hasModified;
    }

    public void setHasModified(boolean hasModified) {
        this.hasModified = hasModified;
    }

    public String getAttributeOne() {
        return attributeOne;
    }

    public void setAttributeOne(String attributeOne) {
        this.attributeOne = attributeOne;
    }

    public String getAttributeTwo() {
        return attributeTwo;
    }

    public void setAttributeTwo(String attributeTwo) {
        this.attributeTwo = attributeTwo;
    }

    public String getAttributeThree() {
        return attributeThree;
    }

    public void setAttributeThree(String attributeThree) {
        this.attributeThree = attributeThree;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * 属性是否允许上报  当属性填充不完整时为false;
     *
     * @return
     */
    public boolean ifAllowUpload() {
        this.notAllowUploadReason = null;
        ifAllowUpload = true;

        /**
         * 判断该字段是否允许为空，如果为空，进行非空判断
         */
        if (!ATTRIBUTE_ONE_CAN_NULL) {
            if (attributeOne == null || TextUtils.isEmpty(this.attributeOne.replace(" ", ""))) {
                ifAllowUpload = false;
                this.invalidAttributeName = PaifangKouAttributeViewUtilNew.ATTRIBUTE_ONE;
            }
        }

        if (!ATTRIBUTE_FOUR_CAN_NULL && ifAllowUpload) {
            if (attributeFour == null || TextUtils.isEmpty(this.attributeFour.replace(" ", ""))) {
                ifAllowUpload = false;
                this.invalidAttributeName = "排水口岸别";
            }
        }
        if (!ATTRIBUTE_GPBH_CAN_NULL && ifAllowUpload){
            if (attributeGpbh == null || TextUtils.isEmpty(this.attributeGpbh.replace(" ",""))){
                ifAllowUpload = false;
                this.invalidAttributeName = "挂牌编号";
            }
        }


        if (ifAllowUpload) {
            if (attributeFive == null || TextUtils.isEmpty(this.attributeFive.replace(" ", "")) || this.attributeFive.equals("其他")) {
                ifAllowUpload = false;
                this.invalidAttributeName = "入河方式";
            }
        }

        if (ifAllowUpload) {
            if (attributeSix == null || TextUtils.isEmpty(this.attributeSix.replace(" ", ""))|| this.attributeSix.equals("其他")) {
                ifAllowUpload = false;
                this.invalidAttributeName = "排水口设施类型";
            }
        }
        if (ifAllowUpload) {
            if (attributeSeven == null || TextUtils.isEmpty(this.attributeSeven.replace(" ", ""))) {
                ifAllowUpload = false;
                this.invalidAttributeName = "排污口管径";
            }
        }

        if (!ATTRIBUTE_THREE_CAN_NULL && ifAllowUpload) {
            if (attributeThree == null || TextUtils.isEmpty(this.attributeThree.replace(" ", ""))|| this.attributeThree.equals("其他")) {
                ifAllowUpload = false;
                this.invalidAttributeName = PaifangKouAttributeViewUtilNew.ATTRIBUTE_THREE;
            }
        }
        return ifAllowUpload;
    }

    public String getNotAllowUploadReason() {
        if (ifAllowUpload) {
            return "";
        }
        if (StringUtil.isEmpty(this.notAllowUploadReason)) {
            return this.invalidAttributeName + "不能为空";
        } else {
            return this.invalidAttributeName + this.notAllowUploadReason;
        }
    }

    public List<String> getAttributeTwoAllowValues() {
        return attributeTwoAllowValues;
    }

    public void setAttributeTwoAllowValues(List<String> attributeTwoAllowValues) {
        this.attributeTwoAllowValues = attributeTwoAllowValues;
    }
}
