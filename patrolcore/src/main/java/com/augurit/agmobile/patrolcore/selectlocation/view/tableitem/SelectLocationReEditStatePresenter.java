package com.augurit.agmobile.patrolcore.selectlocation.view.tableitem;

import android.content.Context;

import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.ISelectLocationService;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.utils.StringUtil;
import com.esri.core.geometry.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * 再次编辑模式下的地图选点
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view.tableitem
 * @createTime 创建时间 ：17/11/2
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/2
 * @modifyMemo 修改备注：
 */

public class SelectLocationReEditStatePresenter extends SelectLocationEditTableItemPresenter {


    /**
     * 构造函数
     *
     * @param context
     * @param mapService              不能为空
     * @param selectLocationTableItem
     */
    public SelectLocationReEditStatePresenter(Context context,
                                              ISelectLocationService mapService,
                                              ISelectLocationTableItemView selectLocationTableItem,
                                              Geometry geometry,
                                              String address) {
        super(context, mapService, selectLocationTableItem);
        this.mDestinationOrLastTimeSelectLocation = geometry;
        DetailAddress detailAddress = new DetailAddress();
        detailAddress.setDetailAddress(StringUtil.getNotNullString(address, ""));
        this.mLastAddress = detailAddress;
        this.setAddress(address);
    }


    @Override
    public void startLocate() {
        //再次编辑模式下不需要进行定位
    }

    /**
     * 当加载地图完成后在地图上进行绘制点
     */
    @Override
    public void loadMap() {
        if (selectLocationTableItem != null) {
            selectLocationTableItem.loadMap();
            if (mDestinationOrLastTimeSelectLocation != null) {
                selectLocationTableItem.registerMapViewInitializedCallback(new Callback1<Boolean>() {
                    @Override
                    public void onCallback(Boolean aBoolean) {
                        selectLocationTableItem.addGraphic(mDestinationOrLastTimeSelectLocation, true);
                        List<String> addresses = new ArrayList<>();
                        addresses.add(mLastAddress.getDetailAddress());
                        selectLocationTableItem.setAddress(addresses);
                    }
                });
            }

        }
    }
}
