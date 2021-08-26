package com.augurit.agmobile.patrolcore.selectlocation.view;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.ISelectLocationService;
import com.esri.core.geometry.SpatialReference;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 位置选择
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime 创建时间 ：17/7/27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/27
 * @modifyMemo 修改备注：
 */

public class SelectLocationPresenter implements ISelectLocationPresenter {

    protected ISelectLocationService mSelectLocationService;
    protected ISelectLocationView mSelectLocationView;

    public SelectLocationPresenter(ISelectLocationService selectLocationService,ISelectLocationView selectLocationView){

        this.mSelectLocationService = selectLocationService;
        this.mSelectLocationView = selectLocationView;
        this.mSelectLocationView.setPresenter(this);
    }

    @Override
    public void requestLocation(double longitude, double latitude, SpatialReference spatialReference) {
        mSelectLocationService.parseLocation(new LatLng(latitude, longitude),spatialReference)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DetailAddress>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSelectLocationView.showAddressNotFound();
                    }

                    @Override
                    public void onNext(DetailAddress s) {
                       mSelectLocationView.showAddress(s.getDetailAddress());
                    }
                });
    }

    @Override
    public void loadMap() {
        mSelectLocationView.addViewToContaner();
        mSelectLocationView.loadMap();
    }

    @Override
    public void turnOffStartLocateWhenMapLoaded() {
        mSelectLocationView.turnOffStartLocateWhenMapLoaded();
    }
}
