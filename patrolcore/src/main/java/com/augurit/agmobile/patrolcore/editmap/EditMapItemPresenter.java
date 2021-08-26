package com.augurit.agmobile.patrolcore.editmap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.editmap.util.EditMapConstant;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.ISelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.view.IMapTableItem;
import com.augurit.agmobile.patrolcore.selectlocation.view.MapTableItemPresenter;
import com.augurit.agmobile.patrolcore.selectlocation.view.SelectLocationTableItem2;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.geocode.Locator;

import org.greenrobot.eventbus.Subscribe;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/10/15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/15
 * @modifyMemo 修改备注：
 */

public class EditMapItemPresenter extends MapTableItemPresenter {


    private OnEditMapFeatureCompleteCallback<Geometry> onEditMapFeatureCompleteCallback;
    private Graphic graphic;
    private String editMode;

    public EditMapItemPresenter(Context context, boolean ifEditable, ISelectLocationService mapService, IMapTableItem mapShortCut) {
        super(context, ifEditable, mapService, mapShortCut);
    }

    public EditMapItemPresenter(Context context, ISelectLocationService mapService, IMapTableItem mapShortCut) {
        super(context, mapService, mapShortCut);
    }


    public void setReEditting(Graphic geometry, String address) {
        this.graphic = geometry;
        this.mLastAddress = address;
        mSelectLocationView.setGraphic(geometry);
        mSelectLocationView.showEditableAddress(address);
    }

    @Override
    public void setReadOnly(LatLng location, String address) {
        super.setReadOnly(location, address);
        Graphic graphic = new Graphic(new Point(location.getLongitude(),location.getLatitude()),null,null);
        this.graphic = graphic;
    }

    public void requestLocation(double latitude, double longitude) {
        new SelectLocationService(mContext, Locator.createOnlineLocator()).parseLocation(new LatLng(latitude, longitude), mSelectLocationView.getMapView().getSpatialReference())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DetailAddress>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DetailAddress s) {
                        mSelectLocationView.showEditableAddress(s.getDetailAddress());
                        mLastAddress = s.getDetailAddress();
                    }
                });
    }

    public void setReading(Graphic geometry, String address) {
        this.graphic = geometry;
        this.ifReadOnly = true;
        this.mLastAddress = address;
        mSelectLocationView.setGraphic(geometry);
        mSelectLocationView.showEditableAddress(address);
    }

    public void setEditMode(String editMode) {
        this.editMode = editMode;
    }

    public void setOnEditMapFeatureCompleteCallback(OnEditMapFeatureCompleteCallback<Geometry> onEditMapFeatureCompleteCallback) {
        this.onEditMapFeatureCompleteCallback = onEditMapFeatureCompleteCallback;
    }

    @Override
    public void startMapActivity() {

        Intent intent = new Intent(mSelectLocationView.getApplicationContext(), EditMapFeatureActivity.class);
        if (graphic != null) {
            intent.putExtra(EditMapConstant.BundleKey.GEOMETRY, graphic.getGeometry());
        }
        intent.putExtra(EditMapConstant.BundleKey.IF_READ_ONLY, ifReadOnly);
        intent.putExtra(EditMapConstant.BundleKey.EDIT_MODE, editMode);
        intent.putExtra(EditMapConstant.BundleKey.INIT_SCALE, initialScale);
        intent.putExtra(EditMapConstant.BundleKey.ADDRESS, mLastAddress);
        ((Activity) mSelectLocationView.getContext()).overridePendingTransition(R.anim.slide_from_righthide_to_leftshow,
                R.anim.slide_from_lleftshow_to_lefthide);
        mContext.startActivity(intent);
    }

    @Override
    public void setReEdit(LatLng location, String address) {
        super.setReEdit(location, address);
        if(location != null){
            Graphic graphic = new Graphic(new Point(location.getLongitude(),location.getLatitude()),null,null);
            this.graphic = graphic;
            if (mSelectLocationView instanceof SelectLocationTableItem2){
                ((SelectLocationTableItem2) mSelectLocationView).setReEdit();
            }
        }

    }

    @Subscribe
    public void onReceivedSendMapFeatureEvent(final SendMapFeatureEvent onSelectLocationFinishEvent) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                initialScale = onSelectLocationFinishEvent.getScale();
                mSelectLocationView.hideSelectLocationButton();
                mSelectLocationView.showEditableAddress(onSelectLocationFinishEvent.getAddress().getDetailAddress());
                if (onSelectLocationFinishEvent.getGraphic() != null) {
                    if (initialScale != 0 && initialScale != -1) {
                        mSelectLocationView.setScale(initialScale);
                    }
                    graphic = onSelectLocationFinishEvent.getGraphic();
                    if (mSelectLocationView.getMapView() != null) {
                        mSelectLocationView.getMapView().centerAt(GeometryUtil.getGeometryCenter(graphic.getGeometry()), true);
                    }
                    mSelectLocationView.addGraphic(onSelectLocationFinishEvent.getGraphic(), true);
                }
                if (mSelectLocationView instanceof SelectLocationTableItem2){
                    ((SelectLocationTableItem2) mSelectLocationView).setReEdit();
                }
                if (onEditMapFeatureCompleteCallback != null && onSelectLocationFinishEvent.getGraphic() != null) {
                    onEditMapFeatureCompleteCallback.onFinished(onSelectLocationFinishEvent.getGraphic().getGeometry(),onSelectLocationFinishEvent.getAddress());
                }
            }
        });

    }

}
