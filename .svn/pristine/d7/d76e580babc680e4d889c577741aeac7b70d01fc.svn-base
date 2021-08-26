package com.augurit.agmobile.patrolcore.editmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.identify.service.IIdentifyService;
import com.augurit.agmobile.mapengine.identify.util.IdentifyServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.am.cmpt.common.Callback2;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.geocode.Locator;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 编辑管线状态
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/11/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/1
 * @modifyMemo 修改备注：
 */

public class EditLineEditStateMapListener extends AbsEditlineEditStateMapListener {

    private static final double MIN_DIFFERENCE = 0.000001d;

    private MapView mapView;
    private Context mContext;
    /**
     * 用来绘制最终的图形
     */
    private GraphicsLayer mGLayer;
    /**
     * 用来当用户点击选择列表时在地图上绘制当前选择的点
     */
    private GraphicsLayer mGLayer2;
    private List<Point> addedPoints;
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


    private ViewGroup mCandiatelistContainer;
    private FeatureListAdapter featureListAdapter;
    private RecyclerView rv_list;
    private TextView tv_total;


    public EditLineEditStateMapListener(MapView mapView, Context context, double initScale, ViewGroup candidatelistContainer) {
        this.mapView = mapView;
        this.mContext = context;
        this.initScale = initScale;
        this.addedPoints = new ArrayList<>();
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


    /**
     * 高亮选中的部件
     *
     * @param findResult
     */
    @Override
    public void highlightComponent(final AMFindResult findResult) {
        Geometry geometry = findResult.getGeometry();
        mapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        final Callout callout = mapView.getCallout();
        View view = View.inflate(mContext, R.layout.callout_select_device, null);
        ((TextView) view.findViewById(R.id.tv_listcallout_title)).setText(findResult.getValue());
        view.findViewById(R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGLayer();
                Symbol symbol = null;
                if (addedPoints.size() == 0) {
                    symbol = new PictureMarkerSymbol(mContext, ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_start_point, null));
                } else if (addedPoints.size() == 1) {
                    symbol = new PictureMarkerSymbol(mContext, ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_end_point, null));
                }
                drawGeometry(findResult.getGeometry(), symbol, mGLayer, false, false);
                //删除掉GLayer2上的图层
                mGLayer2.removeAll();
                judgeAndThenDrawPolyline(findResult);

                callout.hide();
                hideComponentList();
            }
        });
        callout.setContent(view);
        if (geometry.getType() == Geometry.Type.POINT) {
            callout.show((Point) geometry);
        } else if (currentPoint != null) {
            callout.show(currentPoint);
        } else {
            callout.show();
        }
        initGLayer2();
        Symbol symbol = null;
        if (addedPoints.size() == 0) {
            symbol = new PictureMarkerSymbol(mContext, ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_start_point, null));
        } else if (addedPoints.size() == 1) {
            symbol = new PictureMarkerSymbol(mContext, ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_end_point, null));
        }
        drawGeometry(geometry, symbol, mGLayer2, true, false);
    }

    @Override
    public void clickPoint(Point point) {
        if(addedPoints.size() == 1
                && Math.abs(addedPoints.get(0).getX() - currentPoint.getX()) < MIN_DIFFERENCE
                && Math.abs(addedPoints.get(0).getY() - currentPoint.getY()) < MIN_DIFFERENCE) {
            //如果两次选择的点是一样的，那么取消选点
            mGLayer.removeAll();
            addedPoints.clear();
            return;
        }
        searchComponent(point);
        if (addedPoints.size() == 1) {
            requestLocation(point.getY(), point.getX(), true, false); //请求第二个点的位置作为具体位置
        }
    }

    @Override
    public void setOnGraphicChangedListener(OnGraphicChangedListener onGraphicChangedListener) {
        this.onGraphicChangedListener = onGraphicChangedListener;
    }


    private void initGLayer2() {
        if (mGLayer2 == null) {
            mGLayer2 = new GraphicsLayer();
            mapView.addLayer(mGLayer2);
        }
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

        if (addedPoints.size() == 1
                && Math.abs(addedPoints.get(0).getX() - currentPoint.getX()) < MIN_DIFFERENCE
                && Math.abs(addedPoints.get(0).getY() - currentPoint.getY()) < MIN_DIFFERENCE) {
            //如果两次选择的点是一样的，那么取消选点
            mGLayer.removeAll();
            addedPoints.clear();
            return;
        }
        searchComponent(currentPoint);
        if (addedPoints.size() == 1) {
            requestLocation(currentPoint.getY(), currentPoint.getX(), true, false); //请求第二个点的位置作为具体位置
        }

    }


    /**
     * 搜索当前点击范围内的部件
     *
     * @param point
     */
    public void searchComponent(final Point point) {

        if (addedPoints.size() == 0) {
            mGLayer.removeAll();
        }

        if (addedPoints.size() == 2) { //如果之前选择过了两个点，此时再次点击，清空界面上所有点
            //清空界面上的所有点
            addedPoints.clear();
            mGLayer.removeAll();
            graphic = null;
            if (onGraphicChangedListener != null) {
                onGraphicChangedListener.onGraphicClear();
            }
        }

        //如果callout显示，隐藏
        if (mapView.getCallout().isShowing()) {
            mapView.getCallout().hide();
        }

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

                        List<AMFindResult> findResults = new ArrayList<AMFindResult>();
                        if (amFindResults != null && amFindResults.length >= 1) {
                            //过滤掉不是点的部件
                            for (AMFindResult findResult : amFindResults) {
                                if (findResult.getGeometry().getType() == Geometry.Type.POINT) {
                                    findResults.add(findResult);
                                }
                            }
                        }

                        if (findResults.size() == 1 || (findResults.size() > 1 && mCandiatelistContainer == null)) {
                            Symbol symbol = null;
                            initGLayer();
                            if (addedPoints.size() == 0) {
                                symbol = new PictureMarkerSymbol(mContext, ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_start_point, null));
                            } else if (addedPoints.size() == 1) {
                                symbol = new PictureMarkerSymbol(mContext, ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_end_point, null));
                            }
                            drawGeometry(findResults.get(0).getGeometry(), symbol, mGLayer, false, false);
                            judgeAndThenDrawPolyline(findResults.get(0));
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
     * 判断是否达到两个点了，如果是，那么将点连成线
     *
     * @param findResult
     */
    private void judgeAndThenDrawPolyline(AMFindResult findResult) {
        //这里强制转换不增加判断是因为，前面已经过滤掉除了点之外的属性
        addedPoints.add((Point) findResult.getGeometry());
        if (addedPoints.size() == 2) {
            hideComponentList();
            //ll_component_list.setVisibility(View.GONE);//隐藏列表框，显示确定按钮
            //连接成线
            Polyline polyline = new Polyline();
            polyline.startPath(addedPoints.get(0));
            polyline.lineTo(addedPoints.get(1));

            initGLayer();
            Symbol symbol = new SimpleLineSymbol(Color.RED, 7);
            drawGeometry(polyline, symbol, mGLayer, false, false);
            // btn_sure.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
     * @param geometry
     */
    private void drawGeometry(Geometry geometry, Symbol symbol, GraphicsLayer graphicsLayer, boolean ifRemoveAll, boolean ifCenter) {

        if (graphicsLayer == null) {
            return;
        }


        if (ifRemoveAll) {
            graphicsLayer.removeAll();
        }

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
//                        if (ifCoverAddressIfCurrentAddressNotNull && mCurrentAddress != null) {
//                            tv_address.setText("当前位置：" + address);
//                        } else if (mCurrentAddress == null) {
//                            tv_address.setText("当前位置：" + address);
//                        }
//
//                        if (ifUserAddress) {
//                            mUserAddress = detailAddress;
//                        }
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
