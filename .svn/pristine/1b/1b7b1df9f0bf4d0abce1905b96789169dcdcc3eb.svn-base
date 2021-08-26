package com.augurit.agmobile.patrolcore.editmap;

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

public class EditMapFeaturePresenter implements IEditMapFeaturePresenter {

    protected ISelectLocationService mSelectLocationService;
    protected IEditMapFeatureView mSelectLocationView;

    public EditMapFeaturePresenter(ISelectLocationService selectLocationService, IEditMapFeatureView selectLocationView){

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
                        if (mSelectLocationView != null){
                            mSelectLocationView.showAddressNotFound();
                        }

                    }

                    @Override
                    public void onNext(DetailAddress s) {
                        if (mSelectLocationView != null){
                            mSelectLocationView.showAddress(s);
                        }
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

    @Override
    public void clearInstance() {
        mSelectLocationView.stopLocate();
        mSelectLocationView.recycleMapView();
    }
}
