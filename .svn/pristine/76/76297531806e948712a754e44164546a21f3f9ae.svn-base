package com.augurit.agmobile.patrolcore.editmap;


import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.esri.core.map.Graphic;

/**
 * 当选择部件完成时发送的事件
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/10/15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/15
 * @modifyMemo 修改备注：
 */

public class SendMapFeatureEvent {


    private Graphic geometry;
    private double scale;
    private DetailAddress address;

    public SendMapFeatureEvent(Graphic geometry,double scale,DetailAddress address) {
        this.geometry = geometry;
        this.scale = scale;
        this.address = address;
    }

    public Graphic getGraphic() {
        return geometry;
    }

    public double getScale() {
        return scale;
    }

    public DetailAddress getAddress() {
        return address;
    }
}
