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
 * ????????????????????????
 *
 * @author ????????? ???xuciluan
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.patrolcore.editmap
 * @createTime ???????????? ???17/11/1
 * @modifyBy ????????? ???xuciluan
 * @modifyTime ???????????? ???17/11/1
 * @modifyMemo ???????????????
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
     * ??????????????????
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
     * ?????????????????????
     */
    private Graphic graphic;

    private OnGraphicChangedListener onGraphicChangedListener;
    private DetailAddress mCurrentAddress;

    protected ViewGroup mCandiatelistContainer;
    protected FeatureListAdapter featureListAdapter;
    protected RecyclerView rv_list;
    protected TextView tv_total;

    /**
     * ?????????????????????????????????????????????new??????
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
         * ??????polyline????????????????????????????????????????????????????????????????????????????????????,????????????????????????
         */
        if (pointCount > 2) {
            polyline = new Polyline();
            polyline.startPath(startPoint);
            polyline.lineTo(endPoint);
        }

        polylineId = drawGeometry(polyline, new SimpleLineSymbol(Color.RED, 8), mGLayer, true, true);

        //???????????????????????? ??? ??????
        startSymbol = new PictureMarkerSymbol(mContext, ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_start_point, null));
        startPointId = drawGeometry(startPoint, startSymbol, mGLayer, false, false);
        endSymbol = new PictureMarkerSymbol(mContext, ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_end_point, null));
        endPointId = drawGeometry(endPoint, endSymbol, mGLayer, false, false);

        ToastUtil.shortToast(mContext, "????????????????????????,??????????????????");

        if (initScale != 0 && initScale != -1) {
            mMapView.setScale(initScale);
        }
    }


    private void initView() {
        if (mCandiatelistContainer == null) {
            return;
        }
        View view = View.inflate(mContext, R.layout.view_component_list, null);
        //?????????rv_list
        featureListAdapter = new FeatureListAdapter(new ArrayList<AMFindResult>());
        rv_list = (RecyclerView) view.findViewById(R.id.rv_component_list);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        rv_list.setAdapter(featureListAdapter);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        featureListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                /**
                 * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                 */
                if (featureListAdapter.getDataList().get(position).getGeometry() instanceof Point) {
                    updatePosition((Point) featureListAdapter.getDataList().get(position).getGeometry());
                }
            }
        });

        //????????????
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
     * ????????????
     *
     * @param newPosition
     */
    public void updatePosition(Point newPosition) {

        if (selectedIndex == 0) {
            innerStartPoint = newPosition;
            //????????????
            Graphic graphic = new Graphic(newPosition, startSymbol);
            mGLayer.updateGraphic(startPointId, graphic);

            //????????????
            polyline.setPoint(0, newPosition);
            mGLayer.updateGraphic(polylineId, polyline);
            TableViewManager.geometry = polyline;

        } else if (selectedIndex == pointCount - 1) {
            innerEndPoint = newPosition;
            //????????????
            Graphic graphic = new Graphic(newPosition, endSymbol);
            mGLayer.updateGraphic(endPointId, graphic);

            //????????????
            polyline.setPoint(selectedIndex, newPosition);
            mGLayer.updateGraphic(polylineId, polyline);
            TableViewManager.geometry = polyline;
        }

        if (onGraphicChangedListener != null) {
            onGraphicChangedListener.onGraphicChanged(new Graphic(polyline, new SimpleLineSymbol(Color.RED, 7)));
        }
    }


    /**
     * ?????????????????????
     *
     * @param graphicsLayer
     * @param ifRemoveAll   ?????????????????????????????????
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
            if (geometry.getType() == com.esri.core.geometry.Geometry.Type.POLYLINE) { //???????????????????????????????????????????????????????????????
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
                    //????????????
                    //????????????????????????
                    /*PermissionsUtil2.getInstance().requestPermissions((Activity) mContext, "????????????", 140, new PermissionsUtil2.OnPermissionsCallback() {
                        @Override
                        public void onPermissionsGranted(List<String> perms) {
                            VibratorUtil.getInstance(mContext).vibrate(500);
                            ToastUtil.shortToast(mContext, "????????????????????????????????????");
                        }
                    }, Manifest.permission.VIBRATE);*/

                    PermissionsUtil.getInstance().requestPermissions((Activity) mContext, new PermissionsUtil.OnPermissionsCallback() {
                        @Override
                        public void onPermissionsGranted(List<String> perms) {
                            VibratorUtil.getInstance(mContext).vibrate(500);
                            ToastUtil.shortToast(mContext, "????????????????????????????????????");
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
         * ????????????
         */
        Point mapPoint = mMapView.toMapPoint(point.getX(), point.getY());
//        if (selectedIndex == 0) {
//            innerStartPoint = mapPoint;
//            //????????????
//            Graphic graphic = new Graphic(mapPoint, startSymbol);
//            mGLayer.updateGraphic(startPointId, graphic);
//
//            //????????????
//            polyline.setPoint(0, mapPoint);
//            mGLayer.updateGraphic(polylineId, polyline);
//            TableViewManager.geometry = polyline;
//            return true;
//        } else if (selectedIndex == pointCount - 1) {
//            innerEndPoint = mapPoint;
//            //????????????
//            Graphic graphic = new Graphic(mapPoint, endSymbol);
//            mGLayer.updateGraphic(endPointId, graphic);
//
//            //????????????
//            polyline.setPoint(selectedIndex, mapPoint);
//            mGLayer.updateGraphic(polylineId, polyline);
//            TableViewManager.geometry = polyline;
//            return true;
//        }
        searchComponent(mapPoint);
        return super.onSingleTap(point);
    }

    /**
     * ????????????????????????????????????
     *
     * @param mapPoint
     */
    public void searchComponent(final Point mapPoint) {

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("?????????????????????...");
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
                            //???????????????????????????
                            for (AMFindResult findResult : amFindResults) {
                                if (findResult.getGeometry().getType() == Geometry.Type.POINT) {
                                    findResults.add(findResult);
                                }
                            }
                        }

                        if (findResults.size() == 1 || (findResults.size() > 1 && mCandiatelistContainer == null)) {
                            Point component = (Point) findResults.get(0).getGeometry();
                            /**
                             * ????????????????????????
                             */
                            requestLocation(component.getY(), component.getX());
                            /**
                             * ????????????
                             */
                            updatePosition(component);
//                            if (selectedIndex == 0) {
//                                innerStartPoint = component;
//                                //????????????
//                                Graphic graphic = new Graphic(component, startSymbol);
//                                mGLayer.updateGraphic(startPointId, graphic);
//
//                                //????????????
//                                polyline.setPoint(0, component);
//                                mGLayer.updateGraphic(polylineId, polyline);
//                                TableViewManager.geometry = polyline;
//
//                            } else if (selectedIndex == pointCount - 1) {
//                                innerEndPoint = (Point) findResults.get(0).getGraphic();
//                                //????????????
//                                Graphic graphic = new Graphic(component, endSymbol);
//                                mGLayer.updateGraphic(endPointId, graphic);
//
//                                //????????????
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
                        //ToastUtil.shortToast(getActivity(), "???????????????????????????");
                    }
                });
    }


    public void showComponentList(List<AMFindResult> results) {
        if (mCandiatelistContainer != null) {
            mCandiatelistContainer.setVisibility(View.VISIBLE);
            featureListAdapter.notifyDataSetChanged(results);
            tv_total.setText("????????????????????????" + results.size() + "?????????");
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
//         * ????????????????????????
//         */
//        if (selectedIndex == 0 || selectedIndex == pointCount - 1) {
//            //?????????????????????
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
