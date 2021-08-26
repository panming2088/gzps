package com.augurit.agmobile.gzps.common.editmap;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
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
import com.augurit.agmobile.patrolcore.editmap.AbsEditLineReEditStateMapListener;
import com.augurit.agmobile.patrolcore.editmap.OnGraphicChangedListener;
import com.augurit.agmobile.patrolcore.layer.service.EditLayerService;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.upload.view.ReEditTableActivity;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.VibratorUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
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
 * 再次编辑管线状态(采用bottomsheet显示列表)
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/11/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/1
 * @modifyMemo 修改备注：
 */

public class BottomSheetEditLineReEditStateMapListener extends AbsEditLineReEditStateMapListener {


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

    private int currIndex = 0;

    protected ViewGroup mCandiatelistContainer;
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

    /**
     * 注意，要等图层加载完成后再进行new对象
     *
     * @param context
     * @param view
     * @param polyline
     */
    public BottomSheetEditLineReEditStateMapListener(Context context, MapView view, Polyline polyline,
                                                     double initScale, ViewGroup candidateContainer) {
        super(context, view);
        this.mContext = context;
        this.polyline = polyline;
        this.mMapView = view;
        this.initScale = initScale;
        this.mCandiatelistContainer = candidateContainer;
        initData();
        initView();
    }


    public void setCurrComponentUrl(String currComponentUrl) {
        this.currComponentUrl = currComponentUrl;
    }

    @Override
    public void setOnGraphicChangedListener(OnGraphicChangedListener onGraphicChangedListener) {
        this.onGraphicChangedListener = onGraphicChangedListener;
    }

    @Override
    public void clickPoint(Point point) {
        initGLayer();
        hideBottomSheet();
        query(point);
        requestLocation(point.getY(), point.getX());
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
        if (mCandiatelistContainer == null || !(mCandiatelistContainer.getParent() instanceof CoordinatorLayout)) {
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
                    if (mMapView.getCallout().isShowing()) {
                        mMapView.getCallout().hide();
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

        updatePosition((Point) component.getGraphic().getGeometry());

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
            if (geometry.getType() == Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
            }
            return graphicsLayer.addGraphic(graphic);
        }
        return -1;

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
        Symbol symbol = null;
        switch (geometry.getType()) {
            case LINE:
            case POLYLINE:
                symbol = new SimpleLineSymbol(Color.RED, 7);
                break;
            case POINT:
//                symbol = new SimpleMarkerSymbol(Color.RED, 25, SimpleMarkerSymbol.STYLE.CIRCLE);
                symbol = getPointSymbol();
                break;
            default:
                break;
        }

        if (ifRemoveAll) {
            graphicsLayer.removeAll();
        }

        if (symbol != null) {
            Graphic graphic = new Graphic(geometry, symbol);
            graphicsLayer.addGraphic(graphic);
            if (geometry.getType() == Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
            }
        }

        if (ifCenter) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }
    }

    @NonNull
    private Symbol getPointSymbol() {
        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), com.augurit.agmobile.patrolcore.R.mipmap.ic_select_location, null);
        return new PictureMarkerSymbol(mContext, drawable);    // xjx 改为兼容api19的方式获取drawable
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
                if (graphic.getGeometry().getType() == Geometry.Type.POINT) {
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
        initGLayer();
        hideBottomSheet();
        handleTap(point);
        requestLocation(mapPoint.getY(), mapPoint.getX());
        //searchComponent(mapPoint);
        return super.onSingleTap(point);
    }


    private void hideBottomSheet() {
        mCandiatelistContainer.setVisibility(View.GONE);
    }

    /***
     * Handle a tap on the map (or the end of a magnifier long-press event).
     *
     * @param e The point that was tapped.
     */
    private void handleTap(final MotionEvent e) {

        if (mMapView.getCallout().isShowing()) {
            mMapView.getCallout().hide();
        }
        final Point point = mMapView.toMapPoint(e.getX(), e.getY());
        query(point);
    }


    private void query(Point point) {

        if (TextUtils.isEmpty(currComponentUrl)) {
            return;
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
       // final Point point = mMapView.toMapPoint(e.getX(), e.getY());
        final List<LayerInfo> layerInfoList = new ArrayList<>();
        for (String url : LayerUrlConstant.newComponentUrls) {
            LayerInfo layerInfo = new LayerInfo();
            layerInfo.setUrl(url);
            layerInfoList.add(layerInfo);
        }
        Geometry geometry = GeometryEngine.buffer(point, mMapView.getSpatialReference(), 40 * mMapView.getResolution(), null);
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
        updatePosition((Point) componentQueryResult.get(0).getGraphic().getGeometry());
       // drawGeometry(componentQueryResult.get(0).getGraphic().getGeometry(), mGLayer, true, true);
        showBottomSheet(mComponentQueryResult.get(0));

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


    public void clearAllGrapic() {
        mGLayer.removeAll();
    }

    public Graphic getCurrentGraphic() {
        return new Graphic(polyline, new SimpleLineSymbol(Color.RED, 7));
    }


}
