package com.augurit.agmobile.patrolcore.selectlocation.view.tableitem;


import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.esri.core.geometry.Geometry;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view.tableitem
 * @createTime 创建时间 ：17/11/2
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/2
 * @modifyMemo 修改备注：
 */

public interface IReceivedSelectLocationListener {

    void onReceivedLocation(Geometry geometry, DetailAddress detailAddress);

}
