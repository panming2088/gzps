package com.augurit.agmobile.gzps.trackmonitor.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.trackmonitor.model.UserLocation;
import com.augurit.agmobile.gzps.trackmonitor.view.presenter.IOnlineMonitorPresenter;
import com.augurit.agmobile.mapengine.common.agmobilelayer.TdtLayer;
import com.augurit.agmobile.mapengine.layermanage.util.NonEncryptTileCacheHelper;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.trackmonitor.view
 * @createTime 创建时间 ：2017-08-21
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-21
 * @modifyMemo 修改备注：
 */

public class OnlineMonitorView implements IOnlineMonitorView {

    private Context mContext;
    private ViewGroup mContainer;
    private View mView;
    private IOnlineMonitorPresenter mPresenter;

    private MapView mMapView;
    private GraphicsLayer mPointLayer;
    private GraphicsLayer mTextLayer;

    public OnlineMonitorView(Context context, ViewGroup container) {
        this.mContext = context;
        this.mContainer = container;
        mView = getLayout();
        init();
    }

    protected View getLayout() {
        return LayoutInflater.from(mContext).inflate(R.layout.online_monitor, mContainer, false);
    }

    private void init() {
        mMapView = (MapView) mView.findViewById(R.id.mapview);
        mView.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.back();
            }
        });
        mView.findViewById(R.id.btn_all_subordinate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showSubordinateList();
            }
        });

        TdtLayer tdtLayer = new TdtLayer("789", "http://services.tianditugd.com/gdvec2014/wmts",
                new NonEncryptTileCacheHelper(mContext, String.valueOf(789)), true);
        TdtLayer tdtLayer2 = new TdtLayer("456", "http://t0.tianditu.com/vec_c/wmts",
                new NonEncryptTileCacheHelper(mContext, String.valueOf(456)), true);
        TdtLayer tdtLayer3 = new TdtLayer("789", "http://services.tianditugd.com/gdvec2014/wmts",
                new NonEncryptTileCacheHelper(mContext, String.valueOf(789)), true);
        mMapView.addLayer(tdtLayer);
        mMapView.addLayer(tdtLayer2);
        mMapView.addLayer(tdtLayer3);

        mPointLayer = new GraphicsLayer();
        mTextLayer = new GraphicsLayer();
        mMapView.addLayer(mPointLayer);
        mMapView.addLayer(mTextLayer);

        mContainer.addView(mView);
    }

    @Override
    public void setPresenter(IOnlineMonitorPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showOnlineSubordinateLocation(List<UserLocation> onlineSubordinate) {
        mPointLayer.removeAll();
        mTextLayer.removeAll();
        if (ListUtil.isEmpty(onlineSubordinate)) {
            return;
        }
        for (UserLocation userLocation : onlineSubordinate) {
            Point point = new Point(userLocation.getX(), userLocation.getY());
            Symbol pointSymbol = new SimpleMarkerSymbol(Color.BLUE, 20, SimpleMarkerSymbol.STYLE.CIRCLE);
            Graphic pointGraphic = new Graphic(point, pointSymbol);
            Geometry textPoint = point.copy();
            TextSymbol textSymbol = new TextSymbol(15, userLocation.getDept() + userLocation.getName(), Color.BLUE);
            Graphic textGraphic = new Graphic(textPoint, textSymbol);
            mPointLayer.addGraphic(pointGraphic);
            mTextLayer.addGraphic(textGraphic);
        }
    }
}
