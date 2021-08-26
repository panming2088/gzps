package com.augurit.agmobile.gzps.common.selectcomponent;

import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;

/**
 *
 * 选择部件结束，用于标识模块
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.common.selectcomponent
 * @createTime 创建时间 ：17/11/3
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/3
 * @modifyMemo 修改备注：
 */

public class SelectComponentFinishEvent2 {

    private Component findResult;
    private DetailAddress detailAddress;

    private double scale;

    public SelectComponentFinishEvent2(Component findResult,DetailAddress detailAddress, double scale) {
        this.findResult = findResult;
        this.scale = scale;
        this.detailAddress = detailAddress;
    }

    public DetailAddress getDetailAddress() {
        return detailAddress;
    }

    public Component getFindResult() {
        return findResult;
    }

    public void setFindResult(Component findResult) {
        this.findResult = findResult;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

}
