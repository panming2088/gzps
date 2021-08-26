package com.augurit.agmobile.gzps.common.selectcomponent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.utils.GraphicUtil;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.identify.service.IIdentifyService;
import com.augurit.agmobile.mapengine.identify.util.IdentifyServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.editmap.FeatureListAdapter;
import com.augurit.agmobile.patrolcore.editmap.OnGraphicChangedListener;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.am.cmpt.common.Callback2;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.geocode.Locator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 选择部件地图事件
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/11/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/1
 * @modifyMemo 修改备注：
 */

public class SelectModifiedComponentMapListener implements OnSingleTapListener {

    private static final double MIN_DIFFERENCE = 0.000001d;

    private MapView mapView;
    private Context mContext;
    /**
     * 用来绘制最终的图形
     */
    private GraphicsLayer mGLayer;

    private double initScale;
    private DetailAddress mCurrentAddress;
    /**
     * 最终返回的结果
     */
    private Graphic graphic;
    /**
     * 当前点
     */
    private Point currentPoint;

    private OnGraphicChangedListener onGraphicChangedListener;
    /**
     * 点击callout确定按钮事件
     */
    private OnRecyclerItemClickListener<AMFindResult> onCalloutSureButtonClickListener;


    private ViewGroup mCandiatelistContainer;
    private FeatureListAdapter featureListAdapter;
    private RecyclerView rv_list;
    private TextView tv_total;


    public SelectModifiedComponentMapListener(MapView mapView, Context context, double initScale, ViewGroup candidatelistContainer) {
        this.mapView = mapView;
        this.mContext = context;
        this.initScale = initScale;

        this.mCandiatelistContainer = candidatelistContainer;
        initView();
    }

    private void initView() {
        if (mCandiatelistContainer == null) {
            return;
        }
        View view = View.inflate(mContext, R.layout.view_component_list, null);
        //初始化rv_list
        featureListAdapter = new FeatureListAdapter(new ArrayList<AMFindResult>());
        rv_list = (RecyclerView) view.findViewById(R.id.rv_component_list);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        rv_list.setAdapter(featureListAdapter);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        featureListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                highlightComponent(featureListAdapter.getDataList().get(position));
            }
        });

        //关闭列表
        view.findViewById(R.id.iv_close_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCandiatelistContainer.setVisibility(View.GONE);
            }
        });

        tv_total = (TextView) view.findViewById(R.id.tv_total);
        mCandiatelistContainer.addView(view);
    }


    public void hideComponentList() {
        if (mCandiatelistContainer != null) {
            mCandiatelistContainer.setVisibility(View.GONE);
        }
    }


    public void showComponentList(List<AMFindResult> results) {
        if (mCandiatelistContainer != null) {
            mCandiatelistContainer.setVisibility(View.VISIBLE);
            featureListAdapter.notifyDataSetChanged(results);
            tv_total.setText("当前位置共查到：" + results.size() + "个部件");
        }
    }


    public void setOnCalloutSureButtonClickListener(OnRecyclerItemClickListener<AMFindResult> onCalloutSureButtonClickListener) {
        this.onCalloutSureButtonClickListener = onCalloutSureButtonClickListener;
    }


    /**
     * 高亮选中的部件
     *
     * @param findResult
     */
    public void highlightComponent(final AMFindResult findResult) {
        Geometry geometry = findResult.getGeometry();
        mapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        final Callout callout = mapView.getCallout();
        View view = View.inflate(mContext, R.layout.callout_select_device, null);
        ((TextView) view.findViewById(R.id.tv_listcallout_title)).setText(findResult.getValue());

        view.findViewById(R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCalloutSureButtonClickListener != null) {
                    onCalloutSureButtonClickListener.onItemClick(view, 0, findResult);
                }
            }
        });
        callout.setContent(view);
        callout.show(GeometryUtil.getGeometryCenter(geometry));

        drawGeometry(geometry, mGLayer, true, false);
    }

    public void setOnGraphicChangedListener(OnGraphicChangedListener onGraphicChangedListener) {
        this.onGraphicChangedListener = onGraphicChangedListener;
    }


    private void initGLayer() {
        if (mGLayer == null) {
            mGLayer = new GraphicsLayer();
            mapView.addLayer(mGLayer);
        }
    }

    @Override
    public void onSingleTap(float v, float v1) {
        //点击之后在地图添加点
        initGLayer();
        currentPoint = mapView.toMapPoint(v, v1);

        searchComponent(currentPoint);
        requestLocation(currentPoint.getY(), currentPoint.getX(), true, false); //请求第二个点的位置作为具体位置
    }


    /**
     * 搜索当前点击范围内的部件
     *
     * @param point
     */
    public void searchComponent(final Point point) {

        mGLayer.removeAll();
        graphic = null;
        if (onGraphicChangedListener != null) {
            onGraphicChangedListener.onGraphicClear();
        }


        //如果callout显示，隐藏
//        if (mapView.getCallout().isShowing()) {
//            mapView.getCallout().hide();
//        }

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("查询周围部件中...");
        //progressDialog.setCancelable(false);
        progressDialog.show();
        IIdentifyService identifyService = IdentifyServiceFactory.provideLayerService();
        identifyService.selectedFeature((Activity) mContext, mapView,
                LayerServiceFactory.provideLayerService(mContext).getVisibleQueryableLayers(), point, 25, new Callback2<AMFindResult[]>() {
                    @Override
                    public void onSuccess(final AMFindResult[] amFindResults) {
                        progressDialog.dismiss();

                        List<AMFindResult> findResults = Arrays.asList(amFindResults);

                        if (findResults.size() == 1 || (findResults.size() > 1 && mCandiatelistContainer == null)) {

                            initGLayer();
                            highlightComponent(findResults.get(0));

                            // drawGeometry(findResults.get(0).getGeometry(), mGLayer, false, false);

                        } else if (findResults.size() > 1) {

                            showComponentList(findResults);
                            //  ll_component_list.setVisibility(View.VISIBLE);

                        } else {

                        }
                    }

                    @Override
                    public void onFail(Exception error) {
                        progressDialog.dismiss();
                        //ToastUtil.shortToast(getActivity(), "抱歉，附近查无部件");
                    }
                });
    }


    /**
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
     * @param geometry
     */
    private void drawGeometry(Geometry geometry, GraphicsLayer graphicsLayer, boolean ifRemoveAll, boolean ifCenter) {

        if (graphicsLayer == null) {
            return;
        }


        if (ifRemoveAll) {
            graphicsLayer.removeAll();
        }

        Symbol symbol = GraphicUtil.getSymbol(mContext, geometry);

        if (symbol != null) {
            Graphic graphic = new Graphic(geometry, symbol);
            graphicsLayer.addGraphic(graphic);
            if (onGraphicChangedListener != null) {
                onGraphicChangedListener.onGraphicChanged(graphic);
            }
            if (geometry.getType() == Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
            }
        }

        if (ifCenter) {
            mapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
            if (initScale != 0 && initScale != -1) {
                mapView.setScale(initScale);
            }
        }

    }


    /**
     * @param latitude
     * @param longitude
     * @param ifCoverAddressIfCurrentAddressNotNull 当当前位置不为空时，是否覆盖
     * @param ifUserAddress                         传入的经纬度是否是当前用户的位置
     */
    public void requestLocation(double latitude, double longitude, final boolean ifCoverAddressIfCurrentAddressNotNull, final boolean ifUserAddress) {
        new SelectLocationService(mContext, Locator.createOnlineLocator()).parseLocation(new LatLng(latitude, longitude), mapView.getSpatialReference())
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
                    public void onNext(DetailAddress detailAddress) {
                        String address = detailAddress.getDetailAddress();
                        mCurrentAddress = detailAddress;
                        if (onGraphicChangedListener != null) {
                            onGraphicChangedListener.onAddressChanged(detailAddress);
                        }
                    }
                });
    }


    public Graphic getCurrentGraphic() {
        return graphic;
    }

    public DetailAddress getCurrentAddress() {
        return mCurrentAddress;
    }

}
