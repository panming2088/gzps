package com.augurit.agmobile.gzps.uploadfacility.model;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist
 * @createTime 创建时间 ：18/1/25
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：18/1/25
 * @modifyMemo 修改备注：
 */

public class DoorNoFilterCondition {

    /**
     * 门牌号码输入关键字
     */
    public String doorNo;

    public String doorNoType;
    /**
     * 总数
     */
    public static final String total_doorNo = "0";

    /**
     * 审核通过
     */
    public static final String pass_doorNo = "1";
    /**
     * 存在疑问
     */
    public static final String dobut_doorNo = "2";
    /**
     * 未审核
     */
    public static final String no_check_doorNo = "3";

    public DoorNoFilterCondition(String doorNo, String doorNoType) {
        this.doorNo = doorNo;
        this.doorNoType = doorNoType;
    }
}
