package com.augurit.agmobile.mapengine.area.view.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.area.service.AreaServiceImpl;
import com.augurit.agmobile.mapengine.area.view.IAreaView;
import com.augurit.agmobile.mapengine.common.MapListenerManager;
import com.augurit.agmobile.mapengine.common.model.Area;
import com.augurit.agmobile.mapengine.common.model.AreaLocate;
import com.augurit.agmobile.mapengine.common.model.UserArea;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.agmobile.mapengine.common.utils.wktutil.WktUtil;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;

import java.util.List;

/**
 * Created by liangsh on 2016-12-24.
 */
public class AreaPresenter implements IAreaPresenter{
    protected Context mContext;
    protected IAreaView mAreaView;
    protected MapView mMapView;
    protected AreaServiceImpl mAreaServiceImpl;
    protected ProgressDialog pd;
    protected GraphicsLayer graphicsLayer;
    protected View.OnClickListener onAreaClickListener;
    private UserArea mUserArea;
    private ViewGroup mContainer;

    protected boolean showChild = false;
    private Callback1 mCallback;  //退出功能后的回调，以恢复主界面状态

    public AreaPresenter(Context context, MapView mapView, IAreaView areaView){
        this.mContext = context;
        this.mMapView = mapView;
        this.mAreaView = areaView;
        init();
    }

    @Override
    public void init(){
        mAreaServiceImpl = new AreaServiceImpl(mContext);

        pd = new ProgressDialog(mContext);
        pd.setMessage("正在获取区域信息，请稍候...");
        pd.setCancelable(false);
        pd.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode) {
                    pd.dismiss();
                    return true;
                }
                return false;
            }
        });
        //获取区域信息
        mAreaServiceImpl.getUserArea(new Callback2<UserArea>() {
            @Override
            public void onSuccess(UserArea userArea) {
                mUserArea = userArea;
            }

            @Override
            public void onFail(Exception error) {
                mUserArea = null;
            }
        });
    }


    /**
     * 在手机版上点击列表项，则列表下移，显示地图
     */
    protected void onAreaClickBefore(){
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(mContainer);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void locateArea(Area area){
        pd.show();
        onAreaClickBefore();
        graphicsLayer.removeAll();
        mAreaServiceImpl.getAreaLocate(area.getId(), area.getDiscodeLocate().getId(), new Callback2<AreaLocate>() {
            @Override
            public void onSuccess(AreaLocate areaLocate) {
                if (StringUtil.isEmpty(areaLocate.getWkt())
                        || "null".equals(areaLocate.getWkt())) {
                    pd.dismiss();
                    ToastUtil.shortToast(mContext, "获取区域信息失败，请重试！");
                    return;
                }
                Geometry geometry = WktUtil.getPolygonGeometryFromWKT(areaLocate.getWkt(), mMapView.getSpatialReference().getID());
//                Geometry geometry = WktUtil2.getGeometryFromWKT(areaLocate.getWkt());
                //获取区域图形的矩形范围，并取矩形中心点
                Envelope envelope = new Envelope();
                geometry.queryEnvelope(envelope);
                Point centerPoint = envelope.getCenter();
                pd.dismiss();
                FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#aa0000"));
                SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#aa0000"),
                        2, SimpleLineSymbol.STYLE.SOLID);
                fillSymbol.setAlpha(40);
                fillSymbol.setOutline(lineSymbol);
                Graphic graphicBuffer = new Graphic(geometry, fillSymbol);
                graphicsLayer.addGraphic(graphicBuffer);
                //取得地图最小比例尺
                double minResolution = ProjectDataManager.getInstance().getCurrentProject(mContext).getProjectMapParam().getResolution()[0];
                //设置地图为最小比例尺
                mMapView.setResolution(minResolution);
                //设置地图中心点
                mMapView.centerAt(centerPoint, true);
                //高亮区域列表中的选中区域
                setSelectItem();
            }

            @Override
            public void onFail(Exception error) {
                pd.dismiss();
                ToastUtil.shortToast(mContext, "获取区域信息失败，请重试！");
            }
        });
    }

    @Override
    public void setSelectItem(){
//        mAreaView.setSelectItem(false);
    }

    @Override
    public void showChildAreas(Area area){
        showChild = true;
        //取得参数area所代表区域的子区域列表
        List<Area> areaList = mUserArea.getSub().get("" + area.getId());
        mAreaView.setAreaList(areaList, false);
        mAreaView.setTitle(area.getDisname());
    }

    @Override
    public void back(){
        //如果当前列表中显示的是子区域，则返回上一级区域，否则退出区域功能
        if(showChild){
            List<Area> areaList = mUserArea.getSub().get("" + mAreaServiceImpl.getDiscodeId());
            mAreaView.setAreaList(areaList, true);
            mAreaView.setTitle("行政区域");
            showChild = false;
        } else {
            onClose();
            if(mCallback != null){
                mCallback.onCallback(null);
            }
        }
    }

    @Override
    public void setBackListener(Callback1 callback){
        mCallback = callback;
    }


    @Override
    public void startArea(ViewGroup container){
        this.mContainer = container;
        graphicsLayer = new GraphicsLayer();
        mMapView.addLayer(graphicsLayer);
        if(mUserArea != null){
            List<Area> parent = mUserArea.getSub().get("" + mAreaServiceImpl.getDiscodeId());
            mAreaView.setAreaList(parent, true);
        } else {
            pd.show();
            mAreaServiceImpl.getUserArea(new Callback2<UserArea>() {
                @Override
                public void onSuccess(UserArea userArea) {
                    pd.dismiss();
                    mUserArea = userArea;
                    List<Area> parent = mUserArea.getSub().get("" + mAreaServiceImpl.getDiscodeId());
                    mAreaView.setAreaList(parent, true);
                }

                @Override
                public void onFail(Exception error) {
                    pd.dismiss();
                    mUserArea = null;
                    ToastUtil.shortToast(mContext, "获取区域信息失败！");
                }
            });
        }
        mAreaView.show(mContainer);
    }

    @Override
    public void onClose() {
        if (mContainer != null) {
            mContainer.removeAllViews();
            mContainer.setVisibility(View.GONE);
        }
        try {
            graphicsLayer.removeAll();
            mMapView.removeLayer(graphicsLayer);
        } catch (Exception e) {

        }
        //TODO liangsh 点查未迁移，暂时先注释掉
        MapListenerManager manager = MapListenerManager.getInstance(mMapView);
        manager.enableIdentifyListener(null); // 恢复地图点击事件
    }

}
