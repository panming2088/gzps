package com.augurit.agmobile.gzps.uploadfacility.model;

import android.text.TextUtils;

import com.augurit.agmobile.gzps.uploadfacility.util.PaifangKouAttributeViewUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 排水口属性
 * Created by xcl on 2017/12/4.
 */

public class PaifangKouAttributes {

    /**
     * 属性是否可以为空
     */
    public static final boolean ATTRIBUTE_ONE_CAN_NULL = false;
    public static final boolean ATTRIBUTE_TWO_CAN_NULL = false;
    public static final boolean ATTRIBUTE_THREE_CAN_NULL = false;


    private String attributeOne;
    private String attributeTwo;
    private String attributeThree;
    private String attributeFour;
    private String attributeFive;
    private String attributeSix;
    private String attributeSeven;

    /**
     * 属性二允许的值
     */
    private List<String> attributeTwoAllowValues;

    /**
     * 属性是否修改过
     */
    private boolean hasModified;


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

    /**
     * 属性是否允许上报  当属性填充不完整时为false;
     * @return
     */
    public boolean ifAllowUpload() {
        this.notAllowUploadReason = null;
        ifAllowUpload = true;

        Map<String,List<String>> allowValues = new LinkedHashMap<>(3);
        allowValues.put(PaifangKouAttributeViewUtil.ATTRIBUTE_TWO, attributeTwoAllowValues);

        Map<String,String> acturalValues = new HashMap<>(3);
        acturalValues.put(PaifangKouAttributeViewUtil.ATTRIBUTE_TWO, attributeTwo);


        for (String key : allowValues.keySet()){
            if (!ifAllowUpload){
                break;
            }
            List<String> allowValue = allowValues.get(key);
            String currentAttribute = acturalValues.get(key);
            //判断属性的值是否在允许范围内
            if(!ListUtil.isEmpty(allowValue)
                    && !StringUtil.isEmpty(currentAttribute)) {
                boolean attrValid = false;
                for (String value : allowValue) {
                    if (currentAttribute.equals(value)) {
                        attrValid = true;
                        break;
                    }
                }
                if (!attrValid) {
                    ifAllowUpload = false;
                    invalidAttributeName = key;
                    this.notAllowUploadReason = "不正确，请重新选择！";
                }
            }
        }


        /**
         * 判断该字段是否允许为空，如果为空，进行非空判断
         */
        if (!ATTRIBUTE_ONE_CAN_NULL){
            if (attributeOne == null || TextUtils.isEmpty(this.attributeOne.replace(" ",""))){
                ifAllowUpload = false;
                this.invalidAttributeName = PaifangKouAttributeViewUtil.ATTRIBUTE_ONE;
            }
        }

        if (!ATTRIBUTE_TWO_CAN_NULL && ifAllowUpload){
            if (attributeTwo == null || TextUtils.isEmpty(this.attributeTwo.replace(" ",""))){
                ifAllowUpload = false;
                this.invalidAttributeName = PaifangKouAttributeViewUtil.ATTRIBUTE_TWO;
            }
        }

        if (!ATTRIBUTE_THREE_CAN_NULL && ifAllowUpload){
            if (attributeThree == null || TextUtils.isEmpty(this.attributeThree.replace(" ",""))){
                ifAllowUpload = false;
                this.invalidAttributeName = PaifangKouAttributeViewUtil.ATTRIBUTE_THREE;
            }
        }
        return ifAllowUpload;
    }

    public String getNotAllowUploadReason(){
        if (ifAllowUpload){
            return "";
        }
        if(StringUtil.isEmpty(this.notAllowUploadReason)){
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
