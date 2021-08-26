package com.augurit.agmobile.gzps.common.editmap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;

import com.augurit.agmobile.patrolcore.common.file.model.FileResult;
import com.augurit.agmobile.patrolcore.common.file.service.FileService;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableItems;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.model.ProjectTable;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;
import com.augurit.agmobile.patrolcore.editmap.AbsEditlineEditStateMapListener;
import com.augurit.agmobile.patrolcore.editmap.OnGraphicChangedListener;
import com.augurit.agmobile.patrolcore.layer.service.EditLayerService;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.upload.view.ReEditTableActivity;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.geocode.Locator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;

/**
 * 编辑管线状态（以bottomsheeet展示列表）
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/11/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/1
 * @modifyMemo 修改备注：
 */

public class BottomsheetEditLineEditStateMapListener extends AbsEditlineEditStateMapListener {

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
    private List<Component> mComponentQueryResult = new ArrayList<>();


    private BottomSheetBehavior<ViewGroup> mBehavior;
    private View btn_prev;
    private View btn_next;
    private View component_intro;
    private View component_detail_ll;
    private ViewGroup component_detail_container;
    private View btn_upload;
    private ArrayList<TableItem> tableItems = null;
    private ArrayList<Photo> photoList = new ArrayList<>();
    private String projectId;
    private TableViewManager tableViewManager;
    private ProgressDialog progressDialog;
    private DetailAddress mCurrentAddress;
    private String currComponentUrl = LayerUrlConstant.newComponentUrls[0];
    private int currIndex = 0;


    public BottomsheetEditLineEditStateMapListener(MapView mapView, Context context, double initScale, ViewGroup candidatelistContainer) {
        this.mapView = mapView;
        this.mContext = context;
        this.initScale = initScale;
        this.addedPoints = new ArrayList<>();
        this.mCandiatelistContainer = candidatelistContainer;
        initView();
    }


    public void setCurrComponentUrl(String currComponentUrl) {
        this.currComponentUrl = currComponentUrl;
    }


    private void initView() {
        if (mCandiatelistContainer == null) {
            return;
        }

        View view = View.inflate(mContext, R.layout.component_detail_layout, null);
        mBehavior = BottomSheetBehavior.from(mCandiatelistContainer);
        btn_prev = view.findViewById(R.id.prev);
        btn_next = view.findViewById(R.id.next);
        component_intro = view.findViewById(R.id.intro);
        component_detail_ll = view.findViewById(R.id.detail_ll);
        component_detail_container = (ViewGroup) view.findViewById(R.id.detail_container);
        btn_upload = view.findViewById(R.id.btn_upload);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                onChangedBottomSheetState(newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currIndex--;
                if (currIndex < 0) {
                    btn_prev.setVisibility(View.GONE);
                    return;
                }
                showBottomSheet(mComponentQueryResult.get(currIndex));
                if (currIndex == 0) {
                    btn_prev.setVisibility(View.GONE);
                }
                if (mComponentQueryResult.size() > 1) {
                    btn_next.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currIndex++;
                if (currIndex > mComponentQueryResult.size()) {
                    btn_next.setVisibility(View.GONE);
                    return;
                }
                showBottomSheet(mComponentQueryResult.get(currIndex));
                if (currIndex == (mComponentQueryResult.size() - 1)) {
                    btn_next.setVisibility(View.GONE);
                }
                if (currIndex > 0) {
                    btn_prev.setVisibility(View.VISIBLE);
                }
            }
        });
        //隐藏提交按钮
        btn_upload.setVisibility(View.GONE);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.ll_close_bottom_sheet).setVisibility(View.VISIBLE);
        view.findViewById(R.id.ll_close_bottom_sheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCandiatelistContainer.setVisibility(View.GONE);
            }
        });


        mCandiatelistContainer.removeAllViews();
        mCandiatelistContainer.addView(view);
    }


    private void onChangedBottomSheetState(int state) {
        if (state == STATE_EXPANDED) {
            if (component_intro.getVisibility() == View.VISIBLE) {
                showBottomSheetContent(component_detail_container.getId());
                showDetail();
            }

        } else if (state == STATE_COLLAPSED) {
            showBottomSheetContent(component_intro.getId());

        }
    }


    private void showDetail() {
        if (tableItems == null
                || projectId == null) {
            return;
        }
        if (tableViewManager == null) {
//            component_detail_container.removeAllViews();
            //动态表单实例
            tableViewManager =
                    new TableViewManager(mContext, component_detail_container,
                            false, TableState.READING, tableItems,
                            photoList, projectId, null, null);
            tableViewManager.setUploadEditCallback(new Callback2() {
                @Override
                public void onSuccess(Object o) {
                    mBehavior.setState(STATE_COLLAPSED);
                    mCandiatelistContainer.setVisibility(View.GONE);
                    if (mapView.getCallout().isShowing()) {
                        mapView.getCallout().hide();
                    }
                }

                @Override
                public void onFail(Exception error) {

                }
            });
            /**
             * 不显示查看位置按钮
             */
            tableViewManager.setDontShowCheckLocationButton();

        }

    }


    public void hideComponentList() {
        if (mCandiatelistContainer != null) {
            mCandiatelistContainer.setVisibility(View.GONE);
        }
    }

    private void showBottomSheetContent(int viewId) {
        if (viewId == component_intro.getId()) {
            component_intro.setVisibility(View.VISIBLE);
            component_detail_container.setVisibility(View.GONE);
        } else if (viewId == component_detail_container.getId()) {
            component_intro.setVisibility(View.GONE);
            component_detail_container.setVisibility(View.VISIBLE);
        }
    }


    private void showBottomSheet(final Component component) {

        //showCallout(component);
        if (!(component.getGraphic().getGeometry() instanceof Point)) {
            return;
        }

        highlightComponent(component);
       // updatePosition((Point) component.getGraphic().getGeometry());

        initGLayer();
        //drawGeometry(component.getGraphic().getGeometry(), mGLayer, true, true);

        String errorInfo = null;
        Object oErrorInfo = component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ERROR_INFO);

        if (oErrorInfo != null) {
            errorInfo = oErrorInfo.toString();
        }

        TextView titleTv = (TextView) mCandiatelistContainer.findViewById(R.id.title);
        TextView dateTv = (TextView) mCandiatelistContainer.findViewById(R.id.date);
        TextView sortTv = (TextView) mCandiatelistContainer.findViewById(R.id.sort);
        TextView subtypeTv = (TextView) mCandiatelistContainer.findViewById(R.id.subtype);
        TextView field1Tv = (TextView) mCandiatelistContainer.findViewById(R.id.field1);
        TextView field2Tv = (TextView) mCandiatelistContainer.findViewById(R.id.field2);
        TextView field3Tv = (TextView) mCandiatelistContainer.findViewById(R.id.field3);
        TextView addrTv = (TextView) mCandiatelistContainer.findViewById(R.id.addr);
//        addrTv.setVisibility(View.GONE);
        TextView tv_errorinfo = (TextView) mCandiatelistContainer.findViewById(R.id.tv_errorinfo);

        String name = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.NAME));

        String type = LayerUrlConstant.getLayerNameByUnknownLayerUrl(component.getLayerUrl());

        String sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SORT));

        String subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));

        String title = StringUtil.getNotNullString(name, "") + "  " + StringUtil.getNotNullString(type, "");
        titleTv.setText(title);

        String date = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.UPDATEDATE));
        String formatDate = "";
        try {
            formatDate = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(date)));
        } catch (Exception e) {

        }
        dateTv.setText(StringUtil.getNotNullString(formatDate, ""));

        sortTv.setText(StringUtil.getNotNullString(sort, ""));

        subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));

        String listFields = ComponentFieldKeyConstant.getListFieldsByLayerName(component.getLayerName());
        String[] listFieldArray = listFields.split(",");
        if (!StringUtil.isEmpty(listFields)
                && listFieldArray.length == 3) {
            String field1_1 = listFieldArray[0].split(":")[0];
            String field1_2 = listFieldArray[0].split(":")[1];
            String field1 = String.valueOf(component.getGraphic().getAttributes().get(field1_2));
            field1Tv.setText(field1_1 + "：" + StringUtil.getNotNullString(field1, ""));

            String field2_1 = listFieldArray[1].split(":")[0];
            String field2_2 = listFieldArray[1].split(":")[1];
            String field2 = String.valueOf(component.getGraphic().getAttributes().get(field2_2));
            field2Tv.setText(field2_1 + "：" + StringUtil.getNotNullString(field2, ""));

            String field3_1 = listFieldArray[2].split(":")[0];
            String field3_2 = listFieldArray[2].split(":")[1];
            String field3 = String.valueOf(component.getGraphic().getAttributes().get(field3_2));
            field3Tv.setText(field3_1 + "：" + StringUtil.getNotNullString(field3, ""));
        }

        String address = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR));
        addrTv.setText(StringUtil.getNotNullString(address, ""));


        if (errorInfo == null) {
            tv_errorinfo.setVisibility(View.GONE);
        } else {
            tv_errorinfo.setVisibility(View.VISIBLE);
            tv_errorinfo.setText(errorInfo);
        }
        showBottomSheetContent(component_intro.getId());
        mCandiatelistContainer.setVisibility(View.VISIBLE);
        mBehavior.setState(STATE_COLLAPSED);
        loadCompleteDataAsync(component);
    }

    private void loadCompleteDataAsync(final Component component) {
        tableItems = null;
        projectId = null;
        tableViewManager = null;
        component_detail_container.removeAllViews();
        photoList.clear();
        final TableDataManager tableDataManager = new TableDataManager(mContext.getApplicationContext());
        List<Project> projects = tableDataManager.getProjectFromDB();
        Project project = null;
        for (Project p : projects) {
            if (p.getName().equals(component.getLayerName())) {
                project = p;
            }
        }
        if (project == null) {
            ToastUtil.shortToast(mContext, "加载详细信息失败！");
            return;
        }
        projectId = project.getId();
        String getFormStructureUrl = BaseInfoManager.getBaseServerUrl(mContext) + "rest/report/rptform";
        tableDataManager.getTableItemsFromNet(project.getId(), getFormStructureUrl, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                TableItems tmp = (TableItems) data;
                if (tmp.getResult() != null) {
                    tableItems = new ArrayList<TableItem>();
                    tableItems.addAll(tmp.getResult());
                    //   tableDataManager.setTableItemsToDB(tableItems);
                    //缓存在数据库中
                    Realm realm = Realm.getDefaultInstance();
                    ProjectTable projectTable = new ProjectTable();
                    projectTable.setId(projectId);
                    realm.beginTransaction();
                    projectTable.setTableItems(new RealmList<TableItem>(tableItems.toArray(new TableItem[tableItems.size()])));
                    realm.commitTransaction();
                    tableDataManager.setProjectTableToDB(projectTable);
                    ArrayList<TableItem> completeTableItems = EditLayerService.getCompleteTableItem(component.getGraphic(), tableItems);
                    TableViewManager.isEditingFeatureLayer = true;
                    TableViewManager.graphic = component.getGraphic();
                    TableViewManager.geometry = component.getGraphic().getGeometry();
                    TableViewManager.featueLayerUrl = component.getLayerUrl();
                    if (false) {
                        Intent intent = new Intent(mContext, ReEditTableActivity.class);
                        intent.putExtra("tableitems", completeTableItems);
                        intent.putExtra("projectId", projectId);
                        mContext.startActivity(intent);
                    } else {
                        queryAttachmentInfosAsync(component.getLayerUrl(), component.getGraphic(), completeTableItems, projectId);
                    }
                } else {
                }
            }

            @Override
            public void onError(String msg) {
            }
        });
    }

    private void queryAttachmentInfosAsync(String layerUrl, final Graphic graphic, final ArrayList<TableItem> completeTableItems, final String projectId) {
        final ArcGISFeatureLayer arcGISFeatureLayer = new ArcGISFeatureLayer(layerUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
        arcGISFeatureLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                final int objectid = Integer.valueOf(graphic.getAttributes().get(arcGISFeatureLayer.getObjectIdField()).toString());
                FileService fileService = new FileService(mContext);
                fileService.getList(arcGISFeatureLayer.getName(), objectid + "")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<FileResult>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }

                            @Override
                            public void onNext(List<FileResult> fileResults) {
                                if (ListUtil.isEmpty(fileResults)) {
                                    return;
                                }
                                Map<String, Integer> map = new HashMap<>();
                                for (FileResult fileResult : fileResults) {
                                    if (map.containsKey(fileResult.getAttachName())) {
                                        continue;
                                    }
                                    if (!fileResult.getType().contains("image")) {
                                        continue;
                                    }
                                    Photo photo = new Photo();
                                    photo.setPhotoPath(fileResult.getFilePath());
                                    photo.setField1("photo");
                                    photo.setHasBeenUp(1);
                                    photoList.add(photo);
                                    map.put(fileResult.getAttachName(), fileResult.getId());
                                }
                            }
                        });
            }
        });
    }



    /**
     * 高亮选中的部件
     *
     * @param component
     */
    public void highlightComponent(final Component component) {
        Geometry geometry = component.getGraphic().getGeometry();
        mapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        final Callout callout = mapView.getCallout();
        View view = View.inflate(mContext, R.layout.callout_select_device, null);
        String title = "";
        if (component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.NAME) != null){
            String name = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.NAME));
            String type = LayerUrlConstant.getLayerNameByUnknownLayerUrl(component.getLayerUrl());
            title = StringUtil.getNotNullString(name, "") + "  " + StringUtil.getNotNullString(type, "");
        }

        ((TextView) view.findViewById(R.id.tv_listcallout_title)).setText(title);
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
                drawGeometry(component.getGraphic().getGeometry(), symbol, mGLayer, false, false);
                //删除掉GLayer2上的图层
                mGLayer2.removeAll();
                judgeAndThenDrawPolyline(component);

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
    public void setOnGraphicChangedListener(OnGraphicChangedListener onGraphicChangedListener) {
        this.onGraphicChangedListener = onGraphicChangedListener;
    }

    @Override
    public void highlightComponent(AMFindResult findResult) {
        Component component = new Component();
        component.setDisplayFieldName(findResult.getDisplayFieldName());
//        component.setFieldAlias(findResult.getAttributes());
        component.setGraphic(new Graphic(findResult.getGeometry(),null));
        highlightComponent(component);
    }

    @Override
    public void clickPoint(Point point) {
        if (addedPoints.size() == 1
                && Math.abs(addedPoints.get(0).getX() - currentPoint.getX()) < MIN_DIFFERENCE
                && Math.abs(addedPoints.get(0).getY() - currentPoint.getY()) < MIN_DIFFERENCE) {
            //如果两次选择的点是一样的，那么取消选点
            mGLayer.removeAll();
            addedPoints.clear();
            return;
        }

        //searchComponent(currentPoint);
        initGLayer();
        hideBottomSheet();
        handleTap(point);

        if (addedPoints.size() == 1) {
            requestLocation(point.getY(), point.getX(), true, false); //请求第二个点的位置作为具体位置
        }
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

        //searchComponent(currentPoint);
        initGLayer();
        hideBottomSheet();
        handleTap(currentPoint);

        if (addedPoints.size() == 1) {
            requestLocation(currentPoint.getY(), currentPoint.getX(), true, false); //请求第二个点的位置作为具体位置
        }

    }


//    @Override
//    public boolean onSingleTap(MotionEvent point) {
//        /**
//         * 更新位置
//         */
//        Point mapPoint = mMapView.toMapPoint(point.getX(), point.getY());
//
//
//        requestLocation(mapPoint.getY(), mapPoint.getX());
//        //searchComponent(mapPoint);
//        return super.onSingleTap(point);
//    }


    private void hideBottomSheet() {
        mCandiatelistContainer.setVisibility(View.GONE);
    }

    /***
     * Handle a tap on the map (or the end of a magnifier long-press event).
     *
     * @param e The point that was tapped.
     */
    private void handleTap(Point e) {

        if (mapView.getCallout().isShowing()) {
            mapView.getCallout().hide();
        }

        query(e);
    }


    private void query(Point point) {

        if (TextUtils.isEmpty(currComponentUrl)) {
            return;
        }

        if (addedPoints.size() == 0) {
            mGLayer.removeAll();
        }

        if (addedPoints.size() >= 2) { //如果之前选择过了两个点，此时再次点击，清空界面上所有点
            //清空界面上的所有点
            addedPoints.clear();
            mGLayer.removeAll();
            graphic = null;
            if (onGraphicChangedListener != null) {
                onGraphicChangedListener.onGraphicClear();
            }
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("正在查询中.....");
        }

        progressDialog.show();

        mComponentQueryResult.clear();
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;
        //final Point point = mMapView.toMapPoint(e.getX(), e.getY());
        final List<LayerInfo> layerInfoList = new ArrayList<>();
        for (String url : LayerUrlConstant.newComponentUrls) {
            LayerInfo layerInfo = new LayerInfo();
            layerInfo.setUrl(url);
            layerInfoList.add(layerInfo);
        }
        Geometry geometry = GeometryEngine.buffer(point, mapView.getSpatialReference(), 40 * mapView.getResolution(), null);
        ComponentService componentMaintenanceService = new ComponentService(mContext.getApplicationContext());
        String oldLayerUrl = LayerUrlConstant.getLayerUrlByLayerName(LayerUrlConstant.getLayerNameByNewLayerUrl(currComponentUrl));
        componentMaintenanceService.queryComponents(geometry, oldLayerUrl, currComponentUrl, new Callback2<List<QueryFeatureSet>>() {
            @Override
            public void onSuccess(List<QueryFeatureSet> queryFeatureSetList) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (ListUtil.isEmpty(queryFeatureSetList)) {
                    return;
                }
                mComponentQueryResult = new ArrayList<Component>();
                for (QueryFeatureSet queryFeatureSet : queryFeatureSetList) {
                    FeatureSet featureSet = queryFeatureSet.getFeatureSet();
                    Graphic[] graphics = featureSet.getGraphics();
                    if (graphics == null
                            || graphics.length <= 0) {
                        continue;
                    }

                    for (Graphic graphic : graphics) {
                        Component component = new Component();
                        component.setLayerUrl(queryFeatureSet.getLayerUrl());
                        component.setLayerName(queryFeatureSet.getLayerName());
                        component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                        component.setFieldAlias(featureSet.getFieldAliases());
//                        component.setFields(featureSet.getFields());
                        component.setGraphic(graphic);
                        Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                        if (o != null && o instanceof Integer) {
                            component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                        }
                        mComponentQueryResult.add(component);
                    }
                }
                showComponentsOnBottomSheet(mComponentQueryResult);

            }

            @Override
            public void onFail(Exception error) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }



    private void showComponentsOnBottomSheet(List<Component> componentQueryResult) {
        if (componentQueryResult.size() > 1) {
            btn_next.setVisibility(View.VISIBLE);
        }
        initGLayer();
        highlightComponent(componentQueryResult.get(0));
        //updatePosition((Point) componentQueryResult.get(0).getGraphic().getGeometry());
        // drawGeometry(componentQueryResult.get(0).getGraphic().getGeometry(), mGLayer, true, true);
        showBottomSheet(mComponentQueryResult.get(0));

    }



    /**
     * 判断是否达到两个点了，如果是，那么将点连成线
     *
     * @param component
     */
    private void judgeAndThenDrawPolyline(Component component) {
        //这里强制转换不增加判断是因为，前面已经过滤掉除了点之外的属性
        addedPoints.add((Point) component.getGraphic().getGeometry());
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
