package com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.baiduselectlocation.BaiduSelectLocationActivity;
import com.augurit.agmobile.gzps.common.baiduselectlocation.model.BaiduSelectLocationModel;
import com.augurit.agmobile.gzps.common.selectcomponent.SelectComponentFinishEvent2;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAddressErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.editmap.SendMapFeatureEvent;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 设施位置错误
 * <p>
 * Created by xcl on 2017/11/12.
 */
public class FacilityAddressErrorView {

    private Context mContext;

    private View root;
    private TextItemTableItem mCorrectLocation;
    private View rl_choose_correct_address;
    private Geometry mLastSelectedPosition;
    private double initScale = -1;
    private FacilityAddressErrorModel facilityAddressErrorModel;
    private boolean ifHasModified = false;
    private String originalAddress;
    private String originalRoad;
    private TextItemTableItem tableitem_correct_road;

    public FacilityAddressErrorView(Context context,
                                    Component component) {
        this.mContext = context;
        this.facilityAddressErrorModel = new FacilityAddressErrorModel();
        if (component != null && component.getGraphic() != null) {
            this.mLastSelectedPosition = component.getGraphic().getGeometry();
        }
        initView(component);
        EventBus.getDefault().register(this);
    }

    public FacilityAddressErrorView(Context context,
                                    ModifiedFacility modifiedFacility) {
        this.mContext = context;
        this.facilityAddressErrorModel = new FacilityAddressErrorModel();
        if (modifiedFacility != null && modifiedFacility.getX() != 0 && modifiedFacility.getY() != 0) {
            this.mLastSelectedPosition = new Point(modifiedFacility.getX(), modifiedFacility.getY());
        } else if (modifiedFacility != null && modifiedFacility.getOriginX() != 0 && modifiedFacility.getOriginY() != 0) {
            this.mLastSelectedPosition = new Point(modifiedFacility.getOriginX(), modifiedFacility.getOriginY());
        }

        if (modifiedFacility != null) {
            initView(modifiedFacility);
        }
        EventBus.getDefault().register(this);
    }

    public FacilityAddressErrorView(Context context,
                                    UploadedFacility uploadedFacility) {
        this.mContext = context;
        this.facilityAddressErrorModel = new FacilityAddressErrorModel();
        if (uploadedFacility != null && uploadedFacility.getX() != 0 && uploadedFacility.getY() != 0) {
            this.mLastSelectedPosition = new Point(uploadedFacility.getX(), uploadedFacility.getY());
        }

        if (uploadedFacility != null) {
            initView(uploadedFacility);
            setAddress(uploadedFacility.getAddr(),uploadedFacility.getRoad());
        }
        EventBus.getDefault().register(this);
    }

    public FacilityAddressErrorView(Context context,
                                    DetailAddress detailAddress) {
        this.mContext = context;
        this.facilityAddressErrorModel = new FacilityAddressErrorModel();
        if (detailAddress != null && detailAddress.getX() != 0 && detailAddress.getY() != 0) {
            this.mLastSelectedPosition = new Point(detailAddress.getX(), detailAddress.getY());
        }

        if (detailAddress != null) {
            initView(detailAddress);
            setAddress(detailAddress.getDetailAddress(),detailAddress.getStreet());
        }
        EventBus.getDefault().register(this);
    }



    public void setAddress(String address, String road) {

        if (!TextUtils.isEmpty(address)) {
            this.originalAddress = address;
            mCorrectLocation.setText(address);
            facilityAddressErrorModel.setCorrectAddress(address);
        }

        if (!TextUtils.isEmpty(road)) {
            this.originalRoad = road;
            tableitem_correct_road.setText(road);
            facilityAddressErrorModel.setRoad(road);
        }
    }

    public void changeAddress(String address, String road) {
        if (!TextUtils.isEmpty(address)) {
            mCorrectLocation.setText(address);
            facilityAddressErrorModel.setCorrectAddress(address);
        }
        if (!TextUtils.isEmpty(road)) {
            this.originalRoad = road;
            tableitem_correct_road.setText(road);
            facilityAddressErrorModel.setRoad(road);
        }
    }


    public void destroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void addTo(ViewGroup container) {
        container.addView(root);
    }


    private void initView(final ModifiedFacility modifiedFacility) {

        root = View.inflate(mContext, R.layout.view_component_address_error2, null);
        mCorrectLocation = (TextItemTableItem) root.findViewById(R.id.tableitem_correct_location);
        tableitem_correct_road = (TextItemTableItem) root.findViewById(R.id.tableitem_correct_road);
        tableitem_correct_road.setRequireTag();
        mCorrectLocation.setRequireTag();
//        if (modifiedFacility.getCorrectType().equals("数据确认")){
//            //如果是数据确认的话，那么参照地址是我们自动定位到的百度地址
//            this.originalAddress = modifiedFacility.getAddr();
//            this.originalRoad = modifiedFacility.getRoad();
//        }else {
//           //否则，参照地址就是原设施地址
        this.originalAddress = modifiedFacility.getOriginAddr();
        this.originalRoad = modifiedFacility.getOriginRoad();
//        }
        if (!TextUtils.isEmpty(modifiedFacility.getAddr())) {

            mCorrectLocation.setText(modifiedFacility.getAddr());
        }

        if (!TextUtils.isEmpty(modifiedFacility.getRoad())) {
            tableitem_correct_road.setText(modifiedFacility.getRoad());
        }

        /**
         * 选择正确位置
         */
        root.findViewById(R.id.ll_select_correct_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, BaiduSelectLocationActivity.class);

                BaiduSelectLocationModel baiduSelectLocationModel = new BaiduSelectLocationModel();
                baiduSelectLocationModel.setInitScale(initScale);
                String address = null;
                Geometry facilityLocation = null;
                if (modifiedFacility != null && modifiedFacility.getOriginAddr() != null) {
                    if (TextUtils.isEmpty(mCorrectLocation.getText())) {
                        address = modifiedFacility.getOriginAddr();
                    } else {
                        address = mCorrectLocation.getText();
                    }
                    if (modifiedFacility.getX() != 0 && modifiedFacility.getY() != 0) {
                        facilityLocation = new Point(modifiedFacility.getX(), modifiedFacility.getY());
                    } else if (modifiedFacility.getOriginX() != 0 && modifiedFacility.getOriginY() != 0) {
                        facilityLocation = new Point(modifiedFacility.getOriginX(), modifiedFacility.getOriginY());
                    }
                }
                baiduSelectLocationModel.setLastSelectAddress(address);
                baiduSelectLocationModel.setFacilityOriginLocation(facilityLocation);
                baiduSelectLocationModel.setLastSelectLocation(mLastSelectedPosition);
                intent.putExtra("baiduselectlocation", baiduSelectLocationModel);
                mContext.startActivity(intent);
            }
        });

        rl_choose_correct_address = root.findViewById(R.id.rl_choose_correct_address);
        rl_choose_correct_address.setVisibility(View.VISIBLE);
    }

    private void initView(final UploadedFacility uploadedFacility) {

        root = View.inflate(mContext, R.layout.view_component_address_error2, null);
        mCorrectLocation = (TextItemTableItem) root.findViewById(R.id.tableitem_correct_location);
        tableitem_correct_road = (TextItemTableItem) root.findViewById(R.id.tableitem_correct_road);

        /**
         * 选择正确位置
         */
        root.findViewById(R.id.ll_select_correct_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, BaiduSelectLocationActivity.class);

                BaiduSelectLocationModel baiduSelectLocationModel = new BaiduSelectLocationModel();
                baiduSelectLocationModel.setInitScale(initScale);
                String address = null;
                Geometry facilityLocation = null;
                if (uploadedFacility != null && uploadedFacility.getAddr() != null) {
                    if (TextUtils.isEmpty(mCorrectLocation.getText())) {
                        address = uploadedFacility.getAddr();
                    } else {
                        address = mCorrectLocation.getText();
                    }
                    if (uploadedFacility.getX() != 0 && uploadedFacility.getY() != 0) {
                        facilityLocation = new Point(uploadedFacility.getX(), uploadedFacility.getY());
                    }
                }
                baiduSelectLocationModel.setLastSelectAddress(address);
                baiduSelectLocationModel.setFacilityOriginLocation(facilityLocation);
                baiduSelectLocationModel.setLastSelectLocation(mLastSelectedPosition);
                intent.putExtra("baiduselectlocation", baiduSelectLocationModel);
                mContext.startActivity(intent);
            }
        });

        rl_choose_correct_address = root.findViewById(R.id.rl_choose_correct_address);
        rl_choose_correct_address.setVisibility(View.VISIBLE);
    }


    private void initView(final Component component) {

        root = View.inflate(mContext, R.layout.view_component_address_error2, null);
        mCorrectLocation = (TextItemTableItem) root.findViewById(R.id.tableitem_correct_location);
        mCorrectLocation.setRequireTag();
        tableitem_correct_road = (TextItemTableItem) root.findViewById(R.id.tableitem_correct_road);
        tableitem_correct_road.setRequireTag();


        /**
         * 选择正确位置
         */
        root.findViewById(R.id.ll_select_correct_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, BaiduSelectLocationActivity.class);

                BaiduSelectLocationModel baiduSelectLocationModel = new BaiduSelectLocationModel();
                baiduSelectLocationModel.setInitScale(initScale);
                String address = null;
                Geometry facilityLocation = null;
                if (component != null && component.getGraphic() != null
                        && component.getGraphic().getAttributes() != null
                        && component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR) != null) {
                    if (TextUtils.isEmpty(mCorrectLocation.getText())) {
                        address = component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR).toString();
                    } else {
                        address = mCorrectLocation.getText();
                    }
                    facilityLocation = component.getGraphic().getGeometry();
                }
                baiduSelectLocationModel.setLastSelectAddress(address);
                baiduSelectLocationModel.setFacilityOriginLocation(facilityLocation);
                baiduSelectLocationModel.setLastSelectLocation(mLastSelectedPosition);
                intent.putExtra("baiduselectlocation", baiduSelectLocationModel);
                mContext.startActivity(intent);
            }
        });

        rl_choose_correct_address = root.findViewById(R.id.rl_choose_correct_address);
        rl_choose_correct_address.setVisibility(View.VISIBLE);
    }

    private void initView(final DetailAddress detailAddress) {

        root = View.inflate(mContext, R.layout.view_maintain_address_error2, null);
        mCorrectLocation = (TextItemTableItem) root.findViewById(R.id.tableitem_correct_location);
        tableitem_correct_road = (TextItemTableItem) root.findViewById(R.id.tableitem_correct_road);
        tableitem_correct_road.setRequireTag();
        mCorrectLocation.setRequireTag();
//        if (modifiedFacility.getCorrectType().equals("数据确认")){
//            //如果是数据确认的话，那么参照地址是我们自动定位到的百度地址
//            this.originalAddress = modifiedFacility.getAddr();
//            this.originalRoad = modifiedFacility.getRoad();
//        }else {
//           //否则，参照地址就是原设施地址
        this.originalAddress =detailAddress.getDetailAddress();
        this.originalRoad = detailAddress.getStreet();
//        }
        if (!TextUtils.isEmpty(originalAddress)) {

            mCorrectLocation.setText(originalAddress);
        }

        if (!TextUtils.isEmpty(originalRoad)) {
            tableitem_correct_road.setText(originalRoad);
        }

        /**
         * 选择正确位置
         */
        root.findViewById(R.id.ll_select_correct_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, BaiduSelectLocationActivity.class);

                BaiduSelectLocationModel baiduSelectLocationModel = new BaiduSelectLocationModel();
                baiduSelectLocationModel.setInitScale(initScale);
                baiduSelectLocationModel.setIfReadOnly(true);
                String address = null;
                Geometry facilityLocation = null;
                if (detailAddress != null && detailAddress.getDetailAddress() != null) {
                    if (TextUtils.isEmpty(mCorrectLocation.getText())) {
                        address = detailAddress.getDetailAddress();
                    } else {
                        address = mCorrectLocation.getText();
                    }
                    if (detailAddress.getX() != 0 && detailAddress.getY() != 0) {
                        facilityLocation = new Point(detailAddress.getX(), detailAddress.getY());
                    }
                }
                baiduSelectLocationModel.setLastSelectAddress(address);
                baiduSelectLocationModel.setFacilityOriginLocation(facilityLocation);
                baiduSelectLocationModel.setLastSelectLocation(mLastSelectedPosition);
                intent.putExtra("baiduselectlocation", baiduSelectLocationModel);
                mContext.startActivity(intent);
            }
        });

        rl_choose_correct_address = root.findViewById(R.id.rl_choose_correct_address);
        rl_choose_correct_address.setVisibility(View.VISIBLE);
    }


    @Subscribe
    public void onReceivedSendMapFeatureEvent(SendMapFeatureEvent sendMapFeatureEvent) {
        ifHasModified = true;
        mLastSelectedPosition = sendMapFeatureEvent.getGraphic().getGeometry();
        initScale = sendMapFeatureEvent.getScale();
        DetailAddress detailAddress = sendMapFeatureEvent.getAddress();
        mCorrectLocation.setVisibility(View.VISIBLE);
        mCorrectLocation.setText(detailAddress.getDetailAddress());
        facilityAddressErrorModel.setCorrectAddress(detailAddress.getDetailAddress());
        facilityAddressErrorModel.setRoad(detailAddress.getStreet());
        tableitem_correct_road.setText(detailAddress.getStreet());
        if (sendMapFeatureEvent.getGraphic().getGeometry() instanceof com.esri.core.geometry.Point) {
            com.esri.core.geometry.Point point = (com.esri.core.geometry.Point) sendMapFeatureEvent.getGraphic().getGeometry();
            facilityAddressErrorModel.setCorrectX(point.getX());
            facilityAddressErrorModel.setCorrectY(point.getY());
        }
    }

    /**
     * 从url中截取出图层的id
     *
     * @param selectComponentFinishEvent
     * @return
     */
    @NonNull
    private String getLayerId(SelectComponentFinishEvent2 selectComponentFinishEvent) {
        int i = selectComponentFinishEvent.getFindResult().getLayerUrl().lastIndexOf("/");
        return selectComponentFinishEvent.getFindResult().getLayerUrl().substring(i + 1);
    }


    public FacilityAddressErrorModel getFacilityAddressErrorModel() {

        if (!mCorrectLocation.getText().equals(originalAddress)
                || !tableitem_correct_road.getText().equals(originalRoad)) {
            ifHasModified = true;
        }

        facilityAddressErrorModel.setRoad(tableitem_correct_road.getText());
        facilityAddressErrorModel.setCorrectAddress(mCorrectLocation.getText());

        /**
         * 如果没有改变过位置，那么将原设施的位置上传
         */
        if (facilityAddressErrorModel.getCorrectX() == 0 || facilityAddressErrorModel.getCorrectY() == 0) {
            Point point = GeometryUtil.getMarkGeometryCenter(mLastSelectedPosition);
            if (point != null) {
                facilityAddressErrorModel.setCorrectX(point.getX());
                facilityAddressErrorModel.setCorrectY(point.getY());
            }
        }

        facilityAddressErrorModel.setHasModified(ifHasModified);
        return facilityAddressErrorModel;
    }

    public String getOriginalAddress() {
        return originalAddress;
    }

    public String getOriginalRoad() {
        return originalRoad;
    }

    public boolean ifAllowUpload() {
        if (TextUtils.isEmpty(mCorrectLocation.getText())) {
            return false;
        }

        if (TextUtils.isEmpty(tableitem_correct_road.getText())) {
            return false;
        }

        return true;
    }

    public String getNotAllowUploadReason() {

        if (TextUtils.isEmpty(mCorrectLocation.getText())) {
            return "设施位置不允许为空";
        }
        if (TextUtils.isEmpty(tableitem_correct_road.getText())) {
            return "所在道路不允许为空";
        }

        return "";
    }
}
