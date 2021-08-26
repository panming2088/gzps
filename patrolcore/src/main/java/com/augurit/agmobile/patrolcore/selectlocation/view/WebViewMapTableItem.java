package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.common.BaseView;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

import java.util.List;

/**
 * 选择位置控件
 *
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map.view
 * @createTime 创建时间 ：2017-07-14
 * @modifyBy 修改人 ：luobiao
 * @modifyTime 修改时间 ：2017-07-14
 * @modifyMemo 修改备注：
 */

public class WebViewMapTableItem extends BaseView<IMapTableItemPresenter>
        implements IMapTableItem {


    private View mRoot;
    protected ViewGroup mViewGroup;
    protected EditText et_shortcut_detail_address;
    //  private ImageView mIv_shortcut;
    protected View mView_change_location;
    protected View mTv_change_location;
    protected LinearLayout ll_address_container;
    protected ViewGroup rl_map;
    protected WebView mMapView;
    protected View mView_above_mapview;
    protected GraphicsLayer mLocationLayer;
    protected ViewGroup spinner_container;
    protected boolean ifEditable = false;  //位置是否是可以编辑的
    private ViewGroup ll_uneditable_addresss_container;
    protected ILayersService mILayersService;

    private boolean ifAddedToMap = false ; //是否添加到底图上
    private boolean ifMapInitialized = false; //地图是否初始化完成
    private String mUrl;
    private ViewGroup ll_table_item_container;

    /**
     * 构造函数
     *
     * @param context
     * @param viewGroup 容器
     */
    public WebViewMapTableItem(Context context, ViewGroup viewGroup) {
        super(context);
        this.mViewGroup = viewGroup;
        mILayersService = LayerServiceFactory.provideLayerService(context);
        initMapShortCut();
    }

    public boolean isIfEditable() {
        return ifEditable;
    }

    @Override
    public void setIfEditable(boolean ifEditable) {
        this.ifEditable = ifEditable;
        if (ifEditable) { //如果可以编辑
            ll_uneditable_addresss_container.setVisibility(View.GONE);
            et_shortcut_detail_address.setVisibility(View.VISIBLE);
        } else {
            ll_uneditable_addresss_container.setVisibility(View.VISIBLE);
            et_shortcut_detail_address.setVisibility(View.GONE);
        }
    }

    protected void initMapShortCut() {
        mRoot = View.inflate(mContext, R.layout.webview_map_shortcut_view, null);
        rl_map = (ViewGroup) mRoot.findViewById(R.id.rl_map);
        //"在底图上标记地点"控件
        mTv_change_location = mRoot.findViewById(R.id.tv_change_location);
        mTv_change_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.startWebViewMapActivity();
                }
            }
        });
        //底部灰色背景
        mView_change_location = mRoot.findViewById(R.id.view_change_location);
        mView_change_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.startWebViewMapActivity();
                }
            }
        });

        et_shortcut_detail_address = (EditText) mRoot.findViewById(R.id.et_);
        ll_address_container = (LinearLayout) mRoot.findViewById(R.id.ll_address_container);

        mMapView = (WebView) mRoot.findViewById(R.id.mapview_small);

        WebSettings webSettings = mMapView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        mMapView.setWebViewClient(new WebViewClient() {
            //            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url

                return false;    //返回true,代表事件已处理,事件流到此终止
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }
        });
        mView_above_mapview = mRoot.findViewById(R.id.view_above_mapview);
        mView_above_mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.startWebViewMapActivity();
                }
            }
        });

        //不可编辑的位置容器
        ll_uneditable_addresss_container = (ViewGroup) mRoot.findViewById(R.id.ll_uneditable_addresss_container);
        //spinner容器
        spinner_container = (ViewGroup) mRoot.findViewById(R.id.spinner_container);

        ll_table_item_container = (ViewGroup) mRoot.findViewById(R.id.ll_table_item_container);
        loadWebview();
    }

    private void loadWebview(){
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        mUrl = sharedPreferencesUtil.getString("firstUrl","");
        if(TextUtils.isEmpty(mUrl)){
            return;
        }
        String url = mUrl+"?userId=" + BaseInfoManager.getUserId(mContext) + "&serverUrl=" + BaseInfoManager.getBaseServerUrl(mContext);
        mMapView.loadUrl(url);
    }


    @Override
    public void addMapShortCutToContainerWithRemoveAllViews() {
        // mViewGroup.removeAllViews();
        mViewGroup.addView(mRoot);
        mViewGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * 显示地址
     *
     * @param detailedAddress
     */
    @Override
    public void showEditableAddress(final String detailedAddress) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_uneditable_addresss_container.setVisibility(View.GONE);
                et_shortcut_detail_address.setVisibility(View.VISIBLE);
                et_shortcut_detail_address.setText(detailedAddress);
                et_shortcut_detail_address.setSelection(et_shortcut_detail_address.getText().length());
            }
        });

    }

    @Override
    public void showEditableMap(LatLng latLng,double scale) {
        String url = "";
        if(!TextUtils.isEmpty(mUrl)) {
            url = mUrl+"?userId=" + BaseInfoManager.getUserId(mContext) + "&serverUrl=" + BaseInfoManager.getBaseServerUrl(mContext)
                    + "&longitude=" + latLng.getLongitude() + "&latitude=" + latLng.getLatitude();
        }
        if(scale != -1){
            url+="&zoom="+scale;
        }
        mMapView.loadUrl(url);
    }


    @Override
    public View getAddressEditTextContainer() {
        return ll_address_container;
    }

    @Override
    public void setMapInvisible() {
        rl_map.setVisibility(View.GONE);
    }

    /**
     * 当加载完图层后在地图上绘制图形。当刚开始进入地图时，不能直接绘制图形，必须等加载完地图才绘制，否则黑屏
     *
     * @param point
     * @param callback1
     */
    @Override
    public void showLocationOnMapWhenLayerLoaded(final Point point, final Callback1 callback1) {

    }

    /**
     * 直接在地图上绘制图形
     *
     * @param point
     */
    @Override
    public void drawLocationOnMap(Point point) {

    }



    @Override
    public MapView getMapView() {
        return null;
    }

    @Override
    public void removeMapView() {
        ((ViewGroup) mMapView.getParent()).removeView(mMapView);
    }

    @Override
    public void hideSelectLocationButton() {
        mView_change_location.setVisibility(View.GONE);
        mTv_change_location.setVisibility(View.GONE);
    }

    @Override
    public void showMapView() {
        mMapView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setScale(double scale) {
    }

    @Override
    public View getRootView() {
        return mRoot.findViewById(R.id.ll_root_container);
    }

    /**
     * 展示模糊查询到的地址列表
     * @param spinnerList 地址列表
     */
    @Override
    public void showUnEditableAddress(final List<String> spinnerList) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View view = View.inflate(mContext, R.layout.selectlocation_spinner, null);
                Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, spinnerList);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        et_shortcut_detail_address.setText(spinnerList.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                spinner.setSelection(0);//默认选中第0个

                et_shortcut_detail_address.setVisibility(View.GONE);
                spinner_container.setVisibility(View.VISIBLE);
                //装入
                spinner_container.removeAllViews();
                spinner_container.addView(view);
            }
        });
    }

    @Override
    public void setReadOnly(String address) {
        showEditableAddress(address);
        et_shortcut_detail_address.setEnabled(false);
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
    public void addGraphic(Graphic graphic,boolean ifClearOtherGraphic) {

    }

    @Override
    public void setGraphic(Graphic graphic) {

    }
}
