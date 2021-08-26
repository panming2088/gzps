package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.common.BaseView;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

import java.util.List;

/**
 * 不带地图的tableItem
 * Created by xcl on 2017/10/25.
 */

public class SelectLocationTableItem2  extends BaseView<IMapTableItemPresenter>
        implements IMapTableItem {

    private View root;
    private EditText et_address;
    private ViewGroup mContainer;
    private boolean ifEditable = false;
    private ViewGroup ll_table_item_container;
    private TextView tv_select_or_check_location;
    private View.OnClickListener mOnButtonClickListener;

    public SelectLocationTableItem2(Context context, ViewGroup container) {
        super(context);
        this.mContainer = container;
        initView();
    }

    protected void initView() {
        root = View.inflate(mContext, R.layout.view_select_location2,null);

        root.findViewById(R.id.ll_select_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnButtonClickListener == null){
                    if (mPresenter != null) {
                        mPresenter.startMapActivity();
                    }
                }else {
                    mOnButtonClickListener.onClick(view);
                }
            }
        });

        et_address = (EditText) root.findViewById(R.id.et_);

        ll_table_item_container = (ViewGroup) root.findViewById(R.id.ll_table_item_container);

        tv_select_or_check_location = (TextView) root.findViewById(R.id.tv_select_or_check_location);
    }

    /**
     * 设置新的点击事件，覆盖原有的点击事件
     * @param onButtonClickListener
     */
    public void setOnButtonClickListener(View.OnClickListener onButtonClickListener){
        this.mOnButtonClickListener = onButtonClickListener;
    }


    @Override
    public void addMapShortCutToContainerWithRemoveAllViews() {
        mContainer.addView(root);
        mContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void setIfEditable(boolean ifEditable) {
        this.ifEditable = ifEditable;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showEditableAddress(String detailedAddress) {
       // et_address.setVisibility(View.VISIBLE);
        et_address.setText(detailedAddress);
        et_address.setSelection(et_address.getText().length());
    }

    @Override
    public void showEditableMap(LatLng latLng, double scale) {

    }

    @Override
    public View getAddressEditTextContainer() {
        return null;
    }

    @Override
    public void setMapInvisible() {

    }

    @Override
    public void showLocationOnMapWhenLayerLoaded(Point point, Callback1 callback1) {

    }

    @Override
    public void drawLocationOnMap(Point point) {

    }

    @Override
    public MapView getMapView() {
        return null;
    }

    @Override
    public void removeMapView() {

    }

    @Override
    public void hideSelectLocationButton() {

    }

    @Override
    public void showMapView() {

    }

    @Override
    public void setScale(double scale) {

    }

    @Override
    public View getRootView() {
        return root;
    }

    @Override
    public void showUnEditableAddress(List<String> spinnerList) {

    }

    @Override
    public void setReadOnly(String address) {
        showEditableAddress(address);
        et_address.setEnabled(false);
        tv_select_or_check_location.setText("查看在地图上的位置");
    }

    public void setReEdit(){
        tv_select_or_check_location.setText("重新选择位置");
    }


    @Override
    public boolean containsLocation(Location location) {
        return false;
    }

    @Override
    public void addTableItemToMapItem(View view) {
        ll_table_item_container.addView(view);
    }

    @Override
    public void addGraphic(Graphic graphic, boolean ifClearOtherGraphic) {

    }

    @Override
    public void setGraphic(Graphic graphic) {

    }
}
