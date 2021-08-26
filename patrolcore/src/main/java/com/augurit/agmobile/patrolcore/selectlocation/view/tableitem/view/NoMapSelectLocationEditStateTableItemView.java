package com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.editmap.EditMapFeatureActivity;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.ISelectLocationTableItemPresenter;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.ISelectLocationTableItemView;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.common.BaseView;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;

import java.util.List;

/**
 * 不带地图的选点控件(编辑模式 + 再次编辑模式)
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view
 * @createTime 创建时间 ：17/11/2
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/2
 * @modifyMemo 修改备注：
 */

public class NoMapSelectLocationEditStateTableItemView extends BaseView<ISelectLocationTableItemPresenter> implements ISelectLocationTableItemView {

    protected View root;
    protected EditText et_address;
    protected ViewGroup mContainer;
    protected boolean ifEditable = false;
    protected ViewGroup ll_table_item_container;
    protected TextView tv_select_or_check_location;
    protected View.OnClickListener mOnButtonClickListener;
    private View rl_select_location;


    public NoMapSelectLocationEditStateTableItemView(Context context) {
        super(context);
        initView();
    }

    protected void initView() {
        root = View.inflate(mContext, R.layout.view_select_location2,null);

        root.findViewById(R.id.ll_select_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnButtonClickListener == null){
                    startActivity();
                }else {
                    mOnButtonClickListener.onClick(view);
                }
            }
        });

        et_address = (EditText) root.findViewById(R.id.et_);

        ll_table_item_container = (ViewGroup) root.findViewById(R.id.ll_table_item_container);

        tv_select_or_check_location = (TextView) root.findViewById(R.id.tv_select_or_check_location);

        rl_select_location = root.findViewById(R.id.rl_select_location);
    }

    /**
     * 设置新的点击事件，覆盖原有的点击事件
     * @param onButtonClickListener
     */
    public void setOnButtonClickListener(View.OnClickListener onButtonClickListener){
        this.mOnButtonClickListener = onButtonClickListener;
    }

    protected void startActivity() {
        Intent intent = new Intent(mContext, EditMapFeatureActivity.class);
        if (mPresenter != null) {
            mPresenter.completeIntent(intent);
        }
        mContext.startActivity(intent);
    }


    @Override
    public void addTo(ViewGroup container) {
        container.addView(root);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void setAddress(List<String> spinnerList) {
        et_address.setText(spinnerList.get(0));
        tv_select_or_check_location.setText("重新选择位置");
    }

    @Override
    public void setMapCenter(Point center) {

    }

    @Override
    public View getAddressEditTextContainer() {
        return null;
    }

    @Override
    public void hideMapView() {

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
        rl_select_location.setVisibility(View.GONE);
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
    public boolean containsLocation(Location location) {

        return false;
    }

    @Override
    public void addView(View view) {
        ll_table_item_container.addView(view);
    }

    @Override
    public void addGraphic(Geometry geometry, boolean ifClearOtherGraphic) {


    }

    @Override
    public void registerMapViewInitializedCallback(Callback1<Boolean> callback1) {

    }

    @Override
    public boolean ifMapInitialized() {
        return false;
    }

    @Override
    public void loadMap() {

    }

    public void hideCheckLocationButton(){
        rl_select_location.setVisibility(View.GONE);
    }

}
