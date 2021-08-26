package com.augurit.agmobile.patrolcore.selectdevice.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.augurit.agmobile.patrolcore.selectdevice.model.Device;
import com.augurit.agmobile.patrolcore.selectdevice.utils.SelectDeviceConstant;
import com.augurit.am.fw.common.BaseView;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Geometry;
import com.augurit.agmobile.patrolcore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择设施名称
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.selectdevice
 * @createTime 创建时间 ：2017-03-29
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-29
 * @modifyMemo 修改备注：
 */

public class PatrolSelectDeviceNameViewImpl extends BaseView<IPatrolSelectDevicePresenter>
        implements IPatrolSelectDeviceView {

    protected ViewGroup mContainer;
    protected EditText mEt_deviceName;
    protected AutoCompleteTextView mAutocompleteView;
    protected ArrayAdapter<String> mAdapter;
    protected View mRoot;
    protected TextView mTv_;
    protected TextView mTv_key;
    protected List<String> mDeviceNames;
    protected Button mBtn_select_device_on_map;
    protected Button btn_show_device_on_map; //在地图上查看选择的设施
    private EditText mTv_readOnly;
    private ProgressDialog mProgressDialog;

    protected String mKey;

    private LinearLayout ll_select_from_map; //底部弹窗中"从地图选择"按钮

    public PatrolSelectDeviceNameViewImpl(Context context, ViewGroup container) {
        super(context);

        mContainer = container;

        initView();
    }

    protected void initView() {
        mRoot = View.inflate(mContext, R.layout.view_select_device,null);

        mBtn_select_device_on_map = (Button) mRoot.findViewById(R.id.btn_select_device_on_map);
        mBtn_select_device_on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 showBottomSheetDialog();
               // jumpToSelectDeviceActivity();
               // mPresenter.jumpToSelectDeviceActivity();
            }
        });

        btn_show_device_on_map = (Button) mRoot.findViewById(R.id.btn_show_device_on_map);
        btn_show_device_on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //  showBottomSheetDialog();
               // jumpToSelectDeviceActivity();
               // mPresenter.jumpToSelectDeviceActivity();
            }
        });

        mEt_deviceName = (EditText) mRoot.findViewById(R.id.et_);

        mAutocompleteView = (AutoCompleteTextView) mRoot.findViewById(R.id.autocompleteView);
        //设置AutoCompleteTextView输入1个字符就进行提示
        mAutocompleteView.setThreshold(1);
        mAutocompleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mAutocompleteView.showDropDown();
            }
        });
        mAutocompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //mPresenter.search(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mTv_key = (TextView) mRoot.findViewById(R.id.tv_);

        mTv_readOnly = (EditText) mRoot.findViewById(R.id.tv_readonly);
    }


    @Override
    public void addSelectDeviceViewToContainerWithoutRemoveAllViews() {
        mContainer.addView(mRoot);
    }

    @Override
    public void setDeviceName(String deviceName) {
        mEt_deviceName.setText(deviceName);
        mAutocompleteView.setText(deviceName);
        mAutocompleteView.setSelection(deviceName.length());
    }

    @Override
    public void showLoading() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在查询设施信息，请稍后...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {

        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void setSuggestions(List<Device> suggestDevice) {

        mDeviceNames = new ArrayList<>();
        for (Device device : suggestDevice){
            mDeviceNames.add(device.getName());
        }

        mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mDeviceNames);
        mAutocompleteView.setAdapter(mAdapter);

    }

    @Override
    public void notifySuggestionDevicesChanged(List<Device> newSuggestDevice) {

    }

    @Override
    public void jumpToSelectDeviceActivity() {
        Intent intent = new Intent(mContext,SelectDeviceActivity.class);
        intent.putExtra(SelectDeviceConstant.DEVICE_KEY_NAME,mKey);
        if (mPresenter != null && mPresenter.getCurrentDevice() != null){
            intent.putExtra(SelectDeviceConstant.DEVICE_GEOMETRY,mPresenter.getCurrentDevice().getGeometry());
            intent.putExtra(SelectDeviceConstant.DEVICE_NAME,mPresenter.getCurrentDevice().getName());
        }
        mContext.startActivity(intent);
    }

    @Override
    public void setKey(String key) {
        mKey = key;
        mTv_key.setText(key);
    }

    @Override
    public void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        View popupWindowView = LayoutInflater.from(mContext).inflate(R.layout.view_selectdevice_bottom, null);
        LinearLayout ll_select_from_map = (LinearLayout) popupWindowView.findViewById(R.id.ll_select_from_map);
        LinearLayout ll_select_from_qr = (LinearLayout) popupWindowView.findViewById(R.id.ll_select_from_qr);
        LinearLayout ll_select_device_by_barcode = (LinearLayout) popupWindowView.findViewById(R.id.ll_select_device_by_barcode);
        ll_select_from_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mPhotoExtra = PhotoButtonUtil.registTakePhotoButton((Activity) mContext, HSPVFileUtil.getPathPhoto());
                jumpToSelectDeviceActivity();
                bottomSheetDialog.dismiss();
            }
        });
        ll_select_from_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.shortToast(mContext,"功能开发中");
              //  mPhotoExtra = PhotoButtonUtil.registOpenPhotoButton((Activity) mContext, HSPVFileUtil.getPathPhoto());
               // bottomSheetDialog.dismiss();
            }
        });
        ll_select_device_by_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.shortToast(mContext,"功能开发中");
              //  mPhotoExtra = PhotoButtonUtil.registOpenPhotoButton((Activity) mContext, HSPVFileUtil.getPathPhoto());
               // bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(popupWindowView);
        bottomSheetDialog.show();
    }

    @Override
    public void setReadOnly(final String deviceName) {
        setDeviceName(deviceName);
        mAutocompleteView.setVisibility(View.GONE);
        mTv_readOnly.setText(deviceName);
        mTv_readOnly.setVisibility(View.VISIBLE);
        mTv_readOnly.setEnabled(false);
        mBtn_select_device_on_map.setVisibility(View.GONE);

        //显示在地图上查看位置的按钮
        btn_show_device_on_map.setVisibility(View.VISIBLE);
        btn_show_device_on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mPresenter.loadDeviceWkt(true);
            }
        });
    }

    @Override
    public String getDeviceName() {
        return mAutocompleteView.getText().toString();
    }

    @Override
    public void jumpToReadOnlyDeviceActivity(String wkt,String deviceName) {
        Intent intent = new Intent(mContext,SelectDeviceActivity.class);
        intent.putExtra(SelectDeviceConstant.IF_DEVICE_READ_ONLY,true);
        intent.putExtra(SelectDeviceConstant.DEVICE_WKT,wkt);
        putCommonPartToIntent(deviceName,intent);
        mContext.startActivity(intent);
    }

    @Override
    public void jumpToReadOnlyDeviceActivity(Geometry geometry,String deviceName) {
        Intent intent = new Intent(mContext,SelectDeviceActivity.class);
        intent.putExtra(SelectDeviceConstant.DEVICE_GEOMETRY,geometry);
        intent.putExtra(SelectDeviceConstant.IF_DEVICE_READ_ONLY,true);
        putCommonPartToIntent(deviceName, intent);
        mContext.startActivity(intent);
    }

    /**
     * 必须要放置到intent的参数
     * @param deviceName
     * @param intent
     */
    private void putCommonPartToIntent(String deviceName, Intent intent) {
        intent.putExtra(SelectDeviceConstant.DEVICE_KEY_NAME,mKey);
        intent.putExtra(SelectDeviceConstant.DEVICE_NAME,deviceName);
    }

    @Override
    public void jumpToReEditDeviceActivity(String wkt,String deviceName) {
        Intent intent = new Intent(mContext,SelectDeviceActivity.class);
        intent.putExtra(SelectDeviceConstant.DEVICE_WKT,wkt);
        intent.putExtra(SelectDeviceConstant.IF_DEVICE_READ_ONLY,false);
        putCommonPartToIntent(deviceName, intent);
        mContext.startActivity(intent);
    }

    @Override
    public void jumpToReEditDeviceActivity(Geometry geometry,String deviceName) {
        Intent intent = new Intent(mContext,SelectDeviceActivity.class);
        intent.putExtra(SelectDeviceConstant.DEVICE_GEOMETRY,geometry);
        intent.putExtra(SelectDeviceConstant.IF_DEVICE_READ_ONLY,false);
        putCommonPartToIntent(deviceName, intent);
        mContext.startActivity(intent);
    }

    @Override
    public void hideJumpToMapButton() {
        mBtn_select_device_on_map.setVisibility(View.GONE);
    }

    /**
     * 重置底部栏点击按钮事件
     */
    @Override
    public void resetBottomsheetClickListener() {
        mBtn_select_device_on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBottomSheetDialog();
                // jumpToSelectDeviceActivity();
                // mPresenter.jumpToSelectDeviceActivity();
            }
        });
    }

    public void resetBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        View popupWindowView = LayoutInflater.from(mContext).inflate(R.layout.view_selectdevice_bottom, null);
        LinearLayout ll_select_from_map = (LinearLayout) popupWindowView.findViewById(R.id.ll_select_from_map);
        LinearLayout ll_select_from_qr = (LinearLayout) popupWindowView.findViewById(R.id.ll_select_from_qr);
        LinearLayout ll_select_device_by_barcode = (LinearLayout) popupWindowView.findViewById(R.id.ll_select_device_by_barcode);
        ll_select_from_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
                mPresenter.loadDeviceWkt(false);
            }
        });
        ll_select_from_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.shortToast(mContext,"功能开发中");

            }
        });
        ll_select_device_by_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.shortToast(mContext,"功能开发中");
            }
        });
        bottomSheetDialog.setContentView(popupWindowView);
        bottomSheetDialog.show();
    }

    @Override
    public void setUnEditable() {
         mEt_deviceName.setEnabled(false);
         mAutocompleteView.setEnabled(false);
    }

}
