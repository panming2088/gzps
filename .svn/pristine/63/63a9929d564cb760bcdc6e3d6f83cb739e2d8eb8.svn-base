package com.augurit.agmobile.gzps.track.view;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.track.view.presenter.IShowTrackPresenter;
import com.augurit.agmobile.mapengine.common.agmobilelayer.TdtLayer;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.agmobile.mapengine.layermanage.util.NonEncryptTileCacheHelper;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view
 * @createTime 创建时间 ：2017-06-15
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-15
 * @modifyMemo 修改备注：
 */

public class ShowTrackView implements IShowTrackView {

    private Context mContext;
    private ViewGroup mContainer;

    private View mView;
    private MapView mMapView;
    private IShowTrackPresenter mShowTrackPresenter;

    public ShowTrackView(Context context, ViewGroup container) {
        mContext = context;
        mContainer = container;
        mView = getLayout();
        init();
    }

    public void init() {
        mView.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ((TextView) mView.findViewById(R.id.tv_title)).setText("查看轨迹");
        mMapView = (MapView) mView.findViewById(R.id.mapview);
        TdtLayer tdtLayer = new TdtLayer("456","http://t0.tianditu.com/vec_c/wmts",
                new NonEncryptTileCacheHelper(mContext, String.valueOf(456)), true);
        mMapView.addLayer(tdtLayer);
        mContainer.addView(mView);
    }

    protected View getLayout() {
        return LayoutInflater.from(mContext).inflate(R.layout.show_track_view, null);
    }

    @Override
    public void setPresenter(IShowTrackPresenter showTrackPresenter) {
        this.mShowTrackPresenter = showTrackPresenter;
    }

    @Override
    public void showTrack(List<GPSTrack> track) {
        if (ListUtil.isEmpty(track)) {
            //            locate();
            return;
        }
        Polyline line = new Polyline();
        GPSTrack firstGPSTrack = track.get(0);
        Point firstPoint = new Point(firstGPSTrack.getLatitude(), firstGPSTrack.getLongitude());
        line.startPath(firstPoint);
        for (GPSTrack gpsTrack : track) {
            Point point = new Point(gpsTrack.getLatitude(), gpsTrack.getLongitude());
            line.lineTo(point);
        }
        GraphicsLayer graphicsLayer = new GraphicsLayer();
        Graphic graphic = new Graphic(line, new SimpleLineSymbol(Color.RED, 4));
        int i = graphicsLayer.addGraphic(graphic);
        mMapView.setExtent(line);
    }

    private void locate() {
        LocationDisplayManager locationDisplayManager = mMapView.getLocationDisplayManager();
        locationDisplayManager.setLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Point point = new Point(location.getLatitude(), location.getLongitude());
                //生成缓冲区
                Geometry bufferGeometry = GeometryEngine.buffer(point, mMapView.getSpatialReference(),
                        200, null);
                //                mMapView.setExtent(bufferGeometry);
                //                mMapView.centerAt(point, true);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        locationDisplayManager.start();
    }

}
