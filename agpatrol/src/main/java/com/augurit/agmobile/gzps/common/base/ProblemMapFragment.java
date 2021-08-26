package com.augurit.agmobile.gzps.common.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.common.widget.scaleview.MapScaleView;
import com.augurit.agmobile.mapengine.legend.service.OnlineLegendService;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
import com.augurit.agmobile.patrolcore.common.legend.ImageLegendView;
import com.augurit.agmobile.patrolcore.common.legend.LegendPresenter;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.agmobile.patrolcore.project.view.ProjectListActivity;
import com.augurit.agmobile.patrolcore.search.model.CompleteUploadInfo;
import com.augurit.agmobile.patrolcore.search.model.SearchResult;
import com.augurit.agmobile.patrolcore.search.service.PatrolSearchServiceImpl2;
import com.augurit.agmobile.patrolcore.search.view.SearchActivity;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.agmobile.patrolcore.upload.view.ReEditTableActivity;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map
 * @createTime 创建时间 ：2017-10-12
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-10-12
 * @modifyMemo 修改备注：
 */

public class ProblemMapFragment extends Fragment {
    private LocationButton locationButton1;
    private MapView mMapView;
    private GraphicsLayer graphicsLayer;
    private Map<Integer, CompleteUploadInfo> searchResultMap = new HashMap<>();
    private PatrolLayerPresenter layerPresenter;
    /**
     * 是否居中过了
     */
    private boolean ifCenterBefore = false;
    private LegendPresenter legendPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_problemlistmap, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.mapview);
        mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//设置地图背景色、去掉网格线
        //图层按钮
        view.findViewById(R.id.btn_layer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof IDrawerController){
                    final IDrawerController drawerController = (IDrawerController) activity;
                    drawerController.openDrawer(new IDrawerController.OnDrawerOpenListener() {
                        @Override
                        public void onOpened(View drawerView) {
                            showLayerList();
                        }
                    });

                }
            }
        });

        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getActivity(), ProjectListActivity.class), 0x112);
                getActivity().overridePendingTransition(R.anim.in_bottomtotop,0);
            }
        });

        view.findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        Activity activity = getActivity();
        ViewGroup container = null;
        if (activity instanceof IDrawerController) {
            final IDrawerController drawerController = (IDrawerController) activity;
            container =  drawerController.getDrawerContainer();
        }

        PatrolLayerView2 layerView = new PatrolLayerView2(getActivity(), mMapView,container);
        layerPresenter = new PatrolLayerPresenter(layerView);
        layerPresenter.loadLayer();

        /**
         * 比例尺
         */
        final MapScaleView scaleView = (MapScaleView) view.findViewById(com.augurit.agmobile.patrolcore.R.id.scale_view);
        scaleView.setMapView(mMapView);
        mMapView.setOnZoomListener(new OnZoomListener() {
            @Override
            public void preAction(float v, float v1, double v2) {

            }

            @Override
            public void postAction(float v, float v1, double v2) {
                scaleView.refreshScaleView();
            }
        });

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status == STATUS.INITIALIZED){
                    if (PatrolLayerPresenter.initScale != -1 && PatrolLayerPresenter.initScale != 0){
                        mMapView.setScale(PatrolLayerPresenter.initScale);
                        scaleView.setScale(PatrolLayerPresenter.initScale);
                    }
                    loadData();
                }
            }
        });

        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float v, float v1) {
                int[] graphicIDs = graphicsLayer.getGraphicIDs(v, v1, 20);
                //取第0个
                if (graphicIDs != null && graphicIDs.length >= 1){
                    CompleteUploadInfo result = searchResultMap.get(graphicIDs[0]);

                    //跳转到只读界面
                    Intent intent = new Intent(getActivity(), ReEditTableActivity.class);
                    intent.putExtra("tableitems", (ArrayList)result.getTableItems());
                    intent.putExtra("photos", (ArrayList)result.getPhotos());
                    startActivity(intent);
                }
            }
        });


        locationButton1 = (LocationButton) view.findViewById(R.id.location_button);
        locationButton1.setIfShowCallout(false);
        locationButton1.setMapView(mMapView);

        /**
         * 图例
         */
        View ll_legend = view.findViewById(R.id.ll_legend);
        ll_legend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLegendPresenter();
                legendPresenter.showLegends();
            }
        });
    }


    private void initLegendPresenter() {
        if (legendPresenter == null) {
            ILegendView legendView = new ImageLegendView(getActivity());
            legendPresenter = new LegendPresenter(legendView, new OnlineLegendService(getActivity()));
        }

    }

    /**
     * 显示图层列表
     */
    public void showLayerList(){
      if (layerPresenter != null){
          layerPresenter.showLayerList();
      }
    }



    private void loadData(){
        /*try {
            graphicsLayer.removeAll();
        } catch (Exception e) {

        }*/
        Map<String, String> param = new HashMap<>();
//        param.put("projectId", "22d2269f-946a-477b-be9c-622a2a314f65");
        new PatrolSearchServiceImpl2(getActivity()).getHistory(1, 100, param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SearchResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException){
                            ToastUtil.shortToast(getActivity(), "服务器连接超时...");
                        }else {

                        }
                    }

                    @Override
                    public void onNext(List<SearchResult> searchResults) {
                        try {
                            graphicsLayer = new GraphicsLayer();
                            mMapView.addLayer(graphicsLayer);
                        } catch (Exception e) {

                        }
                        for(SearchResult searchResult : searchResults){
                            new PatrolSearchServiceImpl2(getActivity()).getCompleteUploadInfoBySearchResult(searchResult)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<CompleteUploadInfo>() {
                                        @Override
                                        public void onCompleted() {
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            LogUtil.e("巡查模块-查询","查询详细信息失败，原因是： "+ e.getMessage());
                                        }

                                        @Override
                                        public void onNext(CompleteUploadInfo completeUploadInfo) {
                                            showDataOnMap(completeUploadInfo);
                                        }
                                    });
                        }

                    }
                });
    }

    private void showDataOnMap(CompleteUploadInfo completeUploadInfo){
        List<TableItem> mTableItems = completeUploadInfo.getTableItems();
        double x = 0;
        double y = 0;
        for(TableItem tableItem : mTableItems){
            if(tableItem.getField1().toLowerCase().equals("x")){
                x = Double.valueOf(tableItem.getValue());
                continue;
            }
            if(tableItem.getField1().toLowerCase().equals("y")){
                y = Double.valueOf(tableItem.getValue());
                continue;
            }
        }
        if(x != 0 && y != 0){
            Point point = new Point(x, y);
            point.setX(x);
            point.setY(y);
            if (!ifCenterBefore){
                mMapView.centerAt(point,true);
                ifCenterBefore = true;
            }

            Symbol symbol = new SimpleMarkerSymbol(Color.RED,15, SimpleMarkerSymbol.STYLE.CIRCLE);
            Graphic graphic = new Graphic(point,symbol);
            int i = graphicsLayer.addGraphic(graphic);
            searchResultMap.put(i, completeUploadInfo);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //loadData();
    }
}
