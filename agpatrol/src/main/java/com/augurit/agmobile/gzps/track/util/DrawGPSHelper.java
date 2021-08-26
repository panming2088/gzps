package com.augurit.agmobile.gzps.track.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.agmobile.mapengine.gpsstrace.util.GPSTraceConstant;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


/**
 * 这个类的功能：（1）实时定位并绘制轨迹（2）回放轨迹，包括开始回放，暂停回放，停止回放
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.gpstrace.helper
 * @createTime 创建时间 ：2016-10-18 14:22
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-18 14:22
 */
public class DrawGPSHelper {

    private GraphicsLayer mGraphicsLayer; //用于绘制轨迹的层
    private Polyline mTrackPolyline = null;      //轨迹图形
    private int mTrackGeoId = -1;              //轨迹图形Id
    private GraphicsLayer mPlaybackLayer; //用于进行轨迹回放的图层
    private Polyline mPlaybackPolyline = null;   //轨迹回放图形
    private int mPlaybackGeoId = -1;           //轨迹回放图形Id
    private MapView mMapView;
    private Point startPoint;
    private Point endPoint;
    private Location mLocationCur;      // 当前的位置
    private int mLocationGeoId = -1;
    private double mTrackLength;    // 当前轨迹的总长度，单位：千米
    //运动轨迹
    private static ArrayList<Point> sTrackPoint = new ArrayList<>();
    private boolean mShouldCenterToLocation = true; //设置是否应该居中到定位位置
    private Context mContext;
    private Subscription mSubscription;
    private int mCurrentIndex = 0; // 播放停止的帧
    private OnGPSPlayBackFinish mOnGPSPlayBackFinish;

    public DrawGPSHelper(Context context, MapView mapView) {
        this.mMapView = mapView;
        this.mContext = context;
    }


    /**
     * 将轨迹连接成线
     */
    private Polyline drawPathOnMap() {
        removeGrapchicLayer(mGraphicsLayer);
        mGraphicsLayer = new GraphicsLayer();
        mMapView.addLayer(mGraphicsLayer);
        mTrackPolyline = new Polyline();
        Graphic graphic = null;
        if (sTrackPoint.size() > 1) {
            startPoint = sTrackPoint.get(0);
            endPoint = sTrackPoint.get(sTrackPoint.size() - 1);
            mTrackPolyline.startPath(sTrackPoint.get(0));

            for (int i = 1; i < sTrackPoint.size(); i++) {
                mTrackPolyline.lineTo(sTrackPoint.get(i));
            }
            graphic = new Graphic(mTrackPolyline, new SimpleLineSymbol(Color.BLACK, 4));
            mTrackGeoId = mGraphicsLayer.addGraphic(graphic);
        }
        return mTrackPolyline;
    }


    /**
     * 将轨迹连接成线
     */
    private Polyline drawPathOnMapWithStartMark() {
        removeGrapchicLayer(mGraphicsLayer);
        mGraphicsLayer = new GraphicsLayer();
        mMapView.addLayer(mGraphicsLayer);
        mTrackPolyline = new Polyline();
        Graphic graphic = null;
        if (sTrackPoint.size() > 0) {
            startPoint = sTrackPoint.get(0);
//            endPoint = sTrackPoint.get(sTrackPoint.size() - 1);
            mTrackPolyline.startPath(sTrackPoint.get(0));

            //标记起点
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.track_start);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            PictureMarkerSymbol pictureMarkerSymbol =
                    new PictureMarkerSymbol(drawable);
            Graphic startGraphic = new Graphic(startPoint, pictureMarkerSymbol);
            mGraphicsLayer.addGraphic(startGraphic);

            if (sTrackPoint.size() > 1) {
                for (int i = 1; i < sTrackPoint.size(); i++) {
                    mTrackPolyline.lineTo(sTrackPoint.get(i));
                }
            }
            graphic = new Graphic(mTrackPolyline, new SimpleLineSymbol(Color.BLACK, 4));
            mTrackGeoId = mGraphicsLayer.addGraphic(graphic);
        }
        return mTrackPolyline;
    }

    public void markStartPoint(int resId) {
        //标记起点
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
//        mContext.getResources().getDrawable(R.drawable.track_begin);
        PictureMarkerSymbol pictureMarkerSymbol =
                new PictureMarkerSymbol(drawable);
        Graphic startGraphic = new Graphic(startPoint, pictureMarkerSymbol);
        mGraphicsLayer.addGraphic(startGraphic);
    }

    public void markEndPoint(int resId) {
        //标记终点
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
//        mContext.getResources().getDrawable(R.drawable.track_begin);
        PictureMarkerSymbol pictureMarkerSymbol =
                new PictureMarkerSymbol(drawable);
        Graphic startGraphic = new Graphic(endPoint, pictureMarkerSymbol);
        mGraphicsLayer.addGraphic(startGraphic);
    }

    public void removeGrapchicLayer(GraphicsLayer graphicsLayer) {
        if (graphicsLayer != null) {
            graphicsLayer.removeAll();
//            graphicsLayer.recycle();
            try {
                mMapView.removeLayer(graphicsLayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 设置定位后是否要居中
     *
     * @param shouldCenterToLocation true 表示定位后居中到定位点；false 表示不居中
     */
    public void setShouldCenterToLocation(boolean shouldCenterToLocation) {
        mShouldCenterToLocation = shouldCenterToLocation;
    }


    /**
     * 停止定位和绘制轨迹
     */
    public void stopDrawGPSTrack() {
        mLocationCur = null;
        // 移除地图轨迹
        sTrackPoint.clear();
        //移除两个绘制轨迹的图层
        if (mGraphicsLayer != null) {
            mGraphicsLayer.removeAll();
            mMapView.removeLayer(mGraphicsLayer);
        }

        if (mPlaybackLayer != null) {
            mPlaybackLayer.removeAll();
            mMapView.removeLayer(mPlaybackLayer);
        }

    }


    /**
     * 在地图上绘制GPS轨迹
     *
     * @param gpsTracks
     */
    public void drawGPSTrackOnMap(List<GPSTrack> gpsTracks) {
        if (ListUtil.isEmpty(gpsTracks)
                || gpsTracks.size() < 2) {
            ToastUtil.shortToast(mContext, "轨迹点不能少于2个");
            return;
        }
        sTrackPoint.clear();
        for (GPSTrack gpsTrack : gpsTracks) {
            if (gpsTrack.getLatitude() == -1 && gpsTrack.getLongitude() == -1) {
                //如果是异常点，不进行绘制
            } else {
                Point point = new Point();
                point.setX(gpsTrack.getLongitude());
                point.setY(gpsTrack.getLatitude());
                sTrackPoint.add(point);
            }
        }
        // 绘制轨迹，同时将地图缩放到轨迹范围
        drawPathOnMap();
        Envelope envelope = new Envelope();
        mTrackPolyline.queryEnvelope(envelope);
        mMapView.setExtent(envelope);
    }


    /**
     * 在地图上绘制GPS轨迹
     *
     * @param gpsTracks
     */
    public void drawGPSTrackOnMapWithStartMark(List<GPSTrack> gpsTracks) {
        if (ListUtil.isEmpty(gpsTracks)) {

            return;
        }
        sTrackPoint.clear();
        for (GPSTrack gpsTrack : gpsTracks) {
            if (gpsTrack.getLatitude() == -1 && gpsTrack.getLongitude() == -1) {
                //如果是异常点，不进行绘制
            } else {
                Point point = new Point();
                point.setX(gpsTrack.getLongitude());
                point.setY(gpsTrack.getLatitude());
                sTrackPoint.add(point);
            }
        }
        // 绘制轨迹，同时将地图缩放到轨迹范围
        drawPathOnMapWithStartMark();
        Envelope envelope = new Envelope();
        mTrackPolyline.queryEnvelope(envelope);
        mMapView.setExtent(envelope);
    }

    public void drawPointAndMark(GPSTrack gpsTrack, int resId) {
        drawGPSTrackOnMap(gpsTrack);
        Point point = new Point();
        point.setX(gpsTrack.getLongitude());
        point.setY(gpsTrack.getLatitude());
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        PictureMarkerSymbol pictureMarkerSymbol =
                new PictureMarkerSymbol(drawable);
        Graphic graphic = new Graphic(point, pictureMarkerSymbol);
        if (mLocationGeoId != -1) {
            mGraphicsLayer.updateGraphic(mLocationGeoId, point);
        } else {
            mLocationGeoId = mGraphicsLayer.addGraphic(graphic);
        }
    }

    public void drawGPSTrackOnMap(GPSTrack gpsTrack) {
        Point point = new Point();
        point.setX(gpsTrack.getLongitude());
        point.setY(gpsTrack.getLatitude());
        sTrackPoint.add(point);
        mTrackPolyline.lineTo(point);
        mGraphicsLayer.updateGraphic(mTrackGeoId, mTrackPolyline);
        Envelope envelope = new Envelope();
        mTrackPolyline.queryEnvelope(envelope);
        mMapView.setExtent(envelope);
    }


    public void drawPointLineOnMap(GPSTrack gpsTrack) {
        if (mGraphicsLayer == null) {
            mGraphicsLayer = new GraphicsLayer();
            mMapView.addLayer(mGraphicsLayer);
        }

        if (mTrackGeoId == -1) {

            mTrackPolyline = new Polyline();

            Graphic graphic = new Graphic(mTrackPolyline, new SimpleLineSymbol(Color.BLACK, 4));
            mTrackGeoId = mGraphicsLayer.addGraphic(graphic);
        }


        Point point = new Point();
        point.setX(gpsTrack.getLongitude());
        point.setY(gpsTrack.getLatitude());
        if (gpsTrack.getPointState() == GPSTraceConstant.POINT_STAT_START) {
            //标记起点
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.track_start);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            PictureMarkerSymbol pictureMarkerSymbol =
                    new PictureMarkerSymbol(drawable);
            Graphic startGraphic = new Graphic(point, pictureMarkerSymbol);
            mGraphicsLayer.addGraphic(startGraphic);

            mTrackPolyline.startPath(point);
        } else {
            mTrackPolyline.lineTo(point);
        }
        mGraphicsLayer.updateGraphic(mTrackGeoId, mTrackPolyline);
        Envelope envelope = new Envelope();
        mTrackPolyline.queryEnvelope(envelope);
//        mMapView.setExtent(envelope);
    }

    /**
     * 清除GPS轨迹
     */
    public void clearTrack() {

        if (mGraphicsLayer != null) {
            mGraphicsLayer.removeAll();
            mTrackGeoId = -1;
        }
    }

    /**
     * 清除回放轨迹
     */
    public void clearPlaybackTrack() {
        if (mPlaybackLayer != null) {
            mPlaybackLayer.removeAll();
        }
    }

    /**
     * 移除回放图层
     */
    public void removePlaybackGraphicLayer() {
        removeGrapchicLayer(mPlaybackLayer);
    }

    /**
     * 移除轨迹图层
     */
    public void removeTrackGraphicLayer() {
        removeGrapchicLayer(mGraphicsLayer);
    }


    /**
     * 回放轨迹,前提是轨迹已经绘制在
     *
     * @param finish 回放结束监听，当播放结束后如果要改变界面的视图，请实现这个回调。如果传入Null,默认什么都不做
     */
    public void playRoute(OnGPSPlayBackFinish finish) {

        if (mOnGPSPlayBackFinish == null) {
            mOnGPSPlayBackFinish = finish;
        }

        if (sTrackPoint.size() <= 0) {
            return;
        }
        if (mPlaybackLayer != null) {
            removeGrapchicLayer(mPlaybackLayer);
        }
//        mGraphicsLayer.removeAll();
        mPlaybackLayer = new GraphicsLayer();
        mMapView.addLayer(mPlaybackLayer);
        mPlaybackLayer.removeAll();
        mCurrentIndex = 0;
        mPlaybackPolyline = new Polyline();
        play();
    }

    /**
     * 进行回放轨迹
     */
    private void play() {

        if (mSubscription != null) {
            if (!mSubscription.isUnsubscribed()) {
                mSubscription.unsubscribe();
            }
        }

        mSubscription = Observable.interval(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).
                subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                        if (mCurrentIndex < sTrackPoint.size()) {
                            if (mCurrentIndex == 0) {
                                mPlaybackPolyline.startPath(sTrackPoint.get(0));
                                Graphic graphic = new Graphic(mPlaybackPolyline, new SimpleLineSymbol(Color.RED, 4));
                                mPlaybackGeoId = mPlaybackLayer.addGraphic(graphic);
                            } else {
                                mPlaybackPolyline.lineTo(sTrackPoint.get(mCurrentIndex));
                                mPlaybackLayer.updateGraphic(mPlaybackGeoId, mPlaybackPolyline);
                            }
                            mCurrentIndex++;
                        } else {
                            // 回放结束，清0
                            mCurrentIndex = 0;
                            //停止播放
                            pausePlay();
                            //当播放结束后，在这里进行重置视图的操作
                            if (mOnGPSPlayBackFinish != null) {
                                mOnGPSPlayBackFinish.finish();
                            }
                          /*  //重置视图
                            mTraceView.resetToolView();*/
                        }
                    }
                });
    }

    /**
     * 暂停回放轨迹
     */
    public void pausePlay() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    /**
     * 继续回放轨迹
     */
    public void continuePlay() {
        play();
    }

    /**
     * 终止回放轨迹
     */
    public void stopPlay() {
        // 回放结束，清0
        mCurrentIndex = 0;
        //清除回放轨迹
        mPlaybackLayer.removeAll();
        mPlaybackPolyline = null;
        mPlaybackGeoId = 0;
        //停止播放
        pausePlay();
    }

    /**
     * 是否正在进行轨迹回放
     *
     * @return true表示正在进行轨迹回放，false 表示正在轨迹回放结束
     */
    public boolean isPlayingBackRoute() {
        return mSubscription != null && !mSubscription.isUnsubscribed();
    }

    /**
     * 返回当前轨迹长度
     *
     * @return
     */
    public double getCurrentTrackLength() {
        return mTrackLength;
    }


    public void setPlaybackLayer(GraphicsLayer playbackLayer) {
        mPlaybackLayer = playbackLayer;
    }

    public void setDrawTrackGraphicsLayer(GraphicsLayer graphicsLayer) {
        mGraphicsLayer = graphicsLayer;
    }
}
