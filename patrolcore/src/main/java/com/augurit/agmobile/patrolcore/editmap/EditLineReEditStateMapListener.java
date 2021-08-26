package com.augurit.agmobile.patrolcore.editmap;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
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
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.fw.utils.VibratorUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.geocode.Locator;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 再次编辑管线状态
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/11/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/1
 * @modifyMemo 修改备注：
 */

public class EditLineReEditStateMapListener extends AbsEditLineReEditStateMapListener {


    private int selectedIndex = -1;
    private Point innerStartPoint = null;
    private Point innerEndPoint = null;
    private Polyline polyline;
    private MapView mMapView;
    private Context mContext;
    private GraphicsLayer mGLayer;

    /**
     * 线中点的个数
     */
    private int pointCount;

    Point startPoint;
    Point endPoint;
    private int startPointId;
    private int endPointId;
    private int polylineId;
    private PictureMarkerSymbol startSymbol;
    private PictureMarkerSymbol endSymbol;
    private double initScale;

    /**
     * 最终画出来的线
     */
    private Graphic graphic;

    private OnGraphicChangedListener onGraphicChangedListener;
    private DetailAddress mCurrentAddress;

    protected ViewGroup mCandiatelistContainer;
    protected FeatureListAdapter featureListAdapter;
    protected RecyclerView rv_list;
    protected TextView tv_total;

    /**
     * 注意，要等图层加载完成后再进行new对象
     *
     * @param context
     * @param view
     * @param polyline
     */
    public EditLineReEditStateMapListener(Context context, MapView view, Polyline polyline, double initScale, ViewGroup candidateContainer) {
        super(context, view);
        this.mContext = context;
        this.polyline = polyline;
        this.mMapView = view;
        this.initScale = initScale;
        this.mCandiatelistContainer = candidateContainer;
        initData();
        initView();
    }

    @Override
    public void setOnGraphicChangedListener(OnGraphicChangedListener onGraphicChangedListener) {
        this.onGraphicChangedListener = onGraphicChangedListener;
    }

    @Override
    public void clickPoint(Point point) {
        searchComponent(point);
    }


    private void initGLayer() {
        if (mGLayer == null) {
            mGLayer = new GraphicsLayer();
            mGLayer.setSelectionColor(Color.YELLOW);
            mMapView.addLayer(mGLayer);
        }
    }

    public void initData() {

        initGLayer();
        pointCount = polyline.getPointCount();
        startPoint = polyline.getPoint(0);
        endPoint = polyline.getPoint(pointCount - 1);

        /**
         * 判断polyline是否只有两个点，如果超过两个点，那么在更新线的时候会出错,所以自己手动重画
         */
        if (pointCount > 2) {
            polyline = new Polyline();
            polyline.startPath(startPoint);
            polyline.lineTo(endPoint);
        }

        polylineId = drawGeometry(polyline, new SimpleLineSymbol(Color.RED, 8), mGLayer, true, true);

        //在地图上标出起点 和 终点
        startSymbol = new PictureMarkerSymbol(mContext, ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_start_point, null));
        startPointId = drawGeometry(startPoint, startSymbol, mGLayer, false, false);
        endSymbol = new PictureMarkerSymbol(mContext, ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_end_point, null));
        endPointId = drawGeometry(endPoint, endSymbol, mGLayer, false, false);

        ToastUtil.shortToast(mContext, "长按移动点的位置,双击完成编辑");

        if (initScale != 0 && initScale != -1) {
            mMapView.setScale(initScale);
        }
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
                /**
                 * 本来这里可以进行强转不需要判断的，因为在之前已经进行过过滤，保证列表里只有点，为了保险，加入判断
                 */
                if (featureListAdapter.getDataList().get(position).getGeometry() instanceof Point) {
                    updatePosition((Point) featureListAdapter.getDataList().get(position).getGeometry());
                }
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
        mCandiatelistContainer.removeAllViews();
        mCandiatelistContainer.addView(view);
    }

    /**
     * 更新位置
     *
     * @param newPosition
     */
    public void updatePosition(Point newPosition) {

        if (selectedIndex == 0) {
            innerStartPoint = newPosition;
            //更新起点
            Graphic graphic = new Graphic(newPosition, startSymbol);
            mGLayer.updateGraphic(startPointId, graphic);

            //更新线段
            polyline.setPoint(0, newPosition);
            mGLayer.updateGraphic(polylineId, polyline);
            TableViewManager.geometry = polyline;

        } else if (selectedIndex == pointCount - 1) {
            innerEndPoint = newPosition;
            //更新终点
            Graphic graphic = new Graphic(newPosition, endSymbol);
            mGLayer.updateGraphic(endPointId, graphic);

            //更新线段
            polyline.setPoint(selectedIndex, newPosition);
            mGLayer.updateGraphic(polylineId, polyline);
            TableViewManager.geometry = polyline;
        }

        if (onGraphicChangedListener != null) {
            onGraphicChangedListener.onGraphicChanged(new Graphic(polyline, new SimpleLineSymbol(Color.RED, 7)));
        }
    }


    /**
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
     * @param geometry
     */
    private int drawGeometry(Geometry geometry, Symbol symbol, GraphicsLayer graphicsLayer, boolean ifRemoveAll, boolean ifCenter) {

        if (graphicsLayer == null) {
            return -1;
        }

        if (ifRemoveAll) {
            graphicsLayer.removeAll();
        }

        if (ifCenter) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }

        if (symbol != null) {
            Graphic graphic = new Graphic(geometry, symbol);
            if (geometry.getType() == com.esri.core.geometry.Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
            }
            return graphicsLayer.addGraphic(graphic);
        }
        return -1;

    }

    @Override
    public void onLongPress(MotionEvent point) {
        super.onLongPress(point);

        if (innerStartPoint == null) {
            innerStartPoint = startPoint;
        }

        if (innerEndPoint == null) {
            innerEndPoint = endPoint;
        }

        int[] graphicIDs = mGLayer.getGraphicIDs(point.getX(), point.getY(), 15);
        if (graphicIDs != null) {
            for (Integer id : graphicIDs) {
                Graphic graphic = mGLayer.getGraphic(id);
                if (graphic.getGeometry().getType() == com.esri.core.geometry.Geometry.Type.POINT) {
                    if (Math.abs(
                            ((Point) graphic.getGeometry()).getX() - innerStartPoint.getX()) < 0.00001d
                            && Math.abs(
                            ((Point) graphic.getGeometry()).getY() - innerStartPoint.getY()) < 0.00001d) {
                        selectedIndex = 0;
                        mGLayer.clearSelection();
                        mGLayer.setSelectedGraphics(new int[]{id}, true);
                    } else if (Math.abs(
                            ((Point) graphic.getGeometry()).getX() - innerEndPoint.getX()) < 0.00001d
                            && Math.abs(
                            ((Point) graphic.getGeometry()).getY() - innerEndPoint.getY()) < 0.00001d) {
                        selectedIndex = pointCount - 1;
                        mGLayer.clearSelection();
                        mGLayer.setSelectedGraphics(new int[]{id}, true);
                    } else {
                        continue;
                    }
                    //只需要点
                    //振动提醒已经选中
                    /*PermissionsUtil2.getInstance().requestPermissions((Activity) mContext, "请求振动", 140, new PermissionsUtil2.OnPermissionsCallback() {
                        @Override
                        public void onPermissionsGranted(List<String> perms) {
                            VibratorUtil.getInstance(mContext).vibrate(500);
                            ToastUtil.shortToast(mContext, "已选中管点，点击进行移动");
                        }
                    }, Manifest.permission.VIBRATE);*/

                    PermissionsUtil.getInstance().requestPermissions((Activity) mContext, new PermissionsUtil.OnPermissionsCallback() {
                        @Override
                        public void onPermissionsGranted(List<String> perms) {
                            VibratorUtil.getInstance(mContext).vibrate(500);
                            ToastUtil.shortToast(mContext, "已选中管点，点击进行移动");
                        }

                        @Override
                        public void onPermissionsDenied(List<String> perms) {

                        }
                    }, Manifest.permission.VIBRATE);
                }
            }
        }
    }


    @Override
    public boolean onSingleTap(MotionEvent point) {
        /**
         * 更新位置
         */
        Point mapPoint = mMapView.toMapPoint(point.getX(), point.getY());
//        if (selectedIndex == 0) {
//            innerStartPoint = mapPoint;
//            //更新起点
//            Graphic graphic = new Graphic(mapPoint, startSymbol);
//            mGLayer.updateGraphic(startPointId, graphic);
//
//            //更新线段
//            polyline.setPoint(0, mapPoint);
//            mGLayer.updateGraphic(polylineId, polyline);
//            TableViewManager.geometry = polyline;
//            return true;
//        } else if (selectedIndex == pointCount - 1) {
//            innerEndPoint = mapPoint;
//            //更新终点
//            Graphic graphic = new Graphic(mapPoint, endSymbol);
//            mGLayer.updateGraphic(endPointId, graphic);
//
//            //更新线段
//            polyline.setPoint(selectedIndex, mapPoint);
//            mGLayer.updateGraphic(polylineId, polyline);
//            TableViewManager.geometry = polyline;
//            return true;
//        }
        searchComponent(mapPoint);
        return super.onSingleTap(point);
    }

    /**
     * 搜索当前点击范围内的部件
     *
     * @param mapPoint
     */
    public void searchComponent(final Point mapPoint) {

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("查询周围部件中...");
        //progressDialog.setCancelable(false);
        progressDialog.show();
        IIdentifyService identifyService = IdentifyServiceFactory.provideLayerService();
        identifyService.selectedFeature((Activity) mContext, mMapView,
                LayerServiceFactory.provideLayerService(mContext).getVisibleQueryableLayers(), mapPoint, 25, new Callback2<AMFindResult[]>() {
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
                            Point component = (Point) findResults.get(0).getGeometry();
                            /**
                             * 进行请求当前位置
                             */
                            requestLocation(component.getY(), component.getX());
                            /**
                             * 更新位置
                             */
                            updatePosition(component);
//                            if (selectedIndex == 0) {
//                                innerStartPoint = component;
//                                //更新起点
//                                Graphic graphic = new Graphic(component, startSymbol);
//                                mGLayer.updateGraphic(startPointId, graphic);
//
//                                //更新线段
//                                polyline.setPoint(0, component);
//                                mGLayer.updateGraphic(polylineId, polyline);
//                                TableViewManager.geometry = polyline;
//
//                            } else if (selectedIndex == pointCount - 1) {
//                                innerEndPoint = (Point) findResults.get(0).getGraphic();
//                                //更新终点
//                                Graphic graphic = new Graphic(component, endSymbol);
//                                mGLayer.updateGraphic(endPointId, graphic);
//
//                                //更新线段
//                                polyline.setPoint(selectedIndex, component);
//                                mGLayer.updateGraphic(polylineId, polyline);
//                                TableViewManager.geometry = polyline;
//                            }
//
//                            if (onGraphicChangedListener != null) {
//                                onGraphicChangedListener.onGraphicChanged(new Graphic(polyline, new SimpleLineSymbol(Color.RED, 7)));
//                            }

                        } else if (findResults.size() > 1 && mCandiatelistContainer != null) {
                            showComponentList(findResults);
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


    public void showComponentList(List<AMFindResult> results) {
        if (mCandiatelistContainer != null) {
            mCandiatelistContainer.setVisibility(View.VISIBLE);
            featureListAdapter.notifyDataSetChanged(results);
            tv_total.setText("当前位置共查到：" + results.size() + "个部件");
        }
    }

    /**
     * @param latitude
     * @param longitude
     */
    public void requestLocation(double latitude, double longitude) {
        new SelectLocationService(mContext, Locator.createOnlineLocator()).parseLocation(new LatLng(latitude, longitude), mMapView.getSpatialReference())
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

//
//    @Override
//    public boolean onDoubleTap(MotionEvent point) {
//        /**
//         * 进行过编辑，更新
//         */
//        if (selectedIndex == 0 || selectedIndex == pointCount - 1) {
//            //移除起点，终点
//            mGLayer.removeGraphic(startPointId);
//            mGLayer.removeGraphic(endPointId);
//            if (onGraphicChangedListener != null) {
//                Graphic graphic = new Graphic(polyline, new SimpleLineSymbol(Color.RED, 7));
//                onGraphicChangedListener.onFinished(graphic, null);
//            }
//
//            return true;
//
//        }
//        return super.onDoubleTap(point);
//
//    }
//

    public void clearAllGrapic() {
        mGLayer.removeAll();
    }

    public Graphic getCurrentGraphic() {
        return new Graphic(polyline, new SimpleLineSymbol(Color.RED, 7));
    }


}
