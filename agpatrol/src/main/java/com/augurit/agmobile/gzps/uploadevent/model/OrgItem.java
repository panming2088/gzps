package com.augurit.agmobile.gzps.uploadevent.model;

/**
 * 描述：组织机构实体项
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadevent.model
 * @createTime 创建时间 ：2017/12/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/12/7
 * @modifyMemo 修改备注：
 */

public class OrgItem {
    private String name; //机构名称
    private String code;//机构编码


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
