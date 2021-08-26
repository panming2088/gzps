package com.augurit.agmobile.gzps.addcomponent;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView;
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
@Deprecated
public class MapFragment  extends Fragment{

    private MapView mMapView;
    private GraphicsLayer graphicsLayer;
    private Map<Integer, CompleteUploadInfo> searchResultMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_psmap, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.mapview);
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
                            showLayerList(drawerController.getDrawerContainer());
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

        PatrolLayerView2 layerView = new PatrolLayerView2(getActivity(), mMapView, null);
        PatrolLayerPresenter layerPresenter = new PatrolLayerPresenter(layerView);
        layerPresenter.loadLayer();

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status == STATUS.INITIALIZED){
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
        //graphicsLayer = new GraphicsLayer();
    }

    /**
     * 显示图层列表
     * @param container
     */
    public void showLayerList(ViewGroup container){
        PatrolLayerView layerView = new PatrolLayerView(getActivity(), mMapView, container);
        PatrolLayerPresenter layerPresenter = new PatrolLayerPresenter(layerView);
        layerPresenter.showLayerList();
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

            Symbol symbol = new SimpleMarkerSymbol(Color.RED,25, SimpleMarkerSymbol.STYLE.CIRCLE);
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
