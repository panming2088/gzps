package com.augurit.agmobile.gzps.track.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.augurit.agmobile.gzps.track.dao.LocalTrackSQLiteDao;
import com.augurit.agmobile.gzps.track.model.Track;
import com.augurit.agmobile.gzps.track.util.BaiduLoactionManager;
import com.augurit.agmobile.gzps.track.util.OnTrackListener;
import com.augurit.agmobile.gzps.track.util.TimeUtil;
import com.augurit.agmobile.gzps.track.util.TrackConstant;
import com.augurit.agmobile.mapengine.common.baidutransform.pointer.BDPointer;
import com.augurit.agmobile.mapengine.common.baidutransform.pointer.WGSPointer;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.agmobile.mapengine.gpsstrace.util.GPSTraceConstant;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * com.augurit.agmobile.gzps.track.view
 * Created by sdb on 2018/7/9  11:10.
 * Desc：
 */

public class TrackNewService extends Service {


    public static final long DEFAULT_MIN_DISTANCE = 0;
    public static final long DEFAULT_MIN_TIME = 1000;

    public static final int NONE = 0;
    public static final int LOCATING = 1;
    public static final int PAUSE = 2;
    private OnLocationChangeListener mOnLocationChangeListener;
    private boolean isFirstLoc = true;

    @IntDef({NONE, LOCATING, PAUSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TrackLocateState {
    }

    private String mLoginName;
    private BDLocation mLastKnowLocation; //上传定位坐标点
    private boolean isPause = true;
    private Track mTrack = null;
    private List<GPSTrack> mGPSTracks = new ArrayList<>();
    private long mTrackId = -1;
    private String mTrackName;
    private double mTrackLength = 0.0;   //当前轨迹总长度
    private int mTrackTimeSecond = 0;    //当前轨迹总时间
    private Timer mTimer;    //定时器

    private List<OnTrackListener> mOnTrackListeners = new ArrayList<OnTrackListener>();
    private final TrackHandler mTrackHandler = new TrackHandler(this);

    private int mTimeToUploadSecond = 0;
    private Callback1 mTimeToUploadGPSTrackCallback;

    private LocalTrackSQLiteDao mLocalTrackSQLiteDao;

    @TrackNewService.TrackLocateState
    private int currentLocateState = NONE;  //当前定位状态

    @TrackNewService.TrackLocateState
    private int lastLocateState = NONE;  //上次定位状态


    public class MyBinder extends Binder {
        public TrackNewService getService() {
            return TrackNewService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new Notification();
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        startForeground(-1213, notification);//开启前台服务，防止服务被杀死

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mLoginName = BaseInfoManager.getLoginName(this);
        mLocalTrackSQLiteDao = new LocalTrackSQLiteDao();
    }

    @Override
    public void onDestroy() {
        BaiduLoactionManager.getInstance().stopLocate();
        stopForeground(true);

        super.onDestroy();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    private static class TrackHandler extends Handler {

        private final WeakReference<TrackNewService> mTrackService;

        public TrackHandler(TrackNewService trackService) {
            mTrackService = new WeakReference<TrackNewService>(trackService);
        }

        @Override
        public void handleMessage(Message msg) {
            List<OnTrackListener> onTrackListeners = mTrackService.get().getLocationListener();
            for (OnTrackListener onTrackListener : onTrackListeners) {
                if (msg.what == 111) {
                    int second = msg.getData().getInt("time");
                    onTrackListener.onTime(second);
                } else if (msg.what == 222) {
                    double length = msg.getData().getDouble("length");
                    onTrackListener.onLength(length);
                } else if (msg.what == 333) {
                    GPSTrack gpsTrack = (GPSTrack) msg.getData().getSerializable("track");
                    onTrackListener.onTrack(gpsTrack);
                } else if (msg.what == 444) {
                    if (mTrackService.get().getTimeToUploadGPSTrackCallback() == null) {
                        mTrackService.get().getTimeToUploadGPSTrackCallback().onCallback(null);
                    }
                }
            }
        }
    }

    public BDLocation locationTrasform(BDLocation location) {

        //坐标转换  百度转成WGS84
        BDPointer bdPointer = new BDPointer(location.getLatitude(), location.getLongitude());
        WGSPointer wgsPointer = bdPointer.toWGSPointer();
        location.setLatitude(wgsPointer.getLatitude());
        location.setLongitude(wgsPointer.getLongtitude());


        return location;
    }

    public GPSTrack getGPSTrack(BDLocation location) {
        GPSTrack gpsTrack = new GPSTrack();
        long id = TimeUtil.getTimestampRamdon();
        gpsTrack.setId(id);
        //填充轨迹的名称和id
        gpsTrack.setTrackId(mTrackId);
        gpsTrack.setUserName(mLoginName);
        gpsTrack.setRecordDate(System.currentTimeMillis());
        gpsTrack.setLatitude(location.getLatitude());
        gpsTrack.setLongitude(location.getLongitude());
        if (mLastKnowLocation == null) {
            gpsTrack.setPointState(GPSTraceConstant.POINT_STAT_START);    // 该点为开始点
            gpsTrack.setRecordLength(0);
        } else {
            gpsTrack.setPointState(GPSTraceConstant.POINT_STAT_MIDDLE);    // 该点为中间点
            // 计算与当前点与上一个定位点的距离，在距离判断及长度纪录中用到
            Point point = new Point(location.getLatitude(), location.getLongitude());
            Point lastPoint = new Point(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());
            Polyline line = new Polyline();
            line.startPath(lastPoint);
            line.lineTo(point);
            double distance = GeometryEngine.geodesicLength(line, SpatialReference.create(4326),
                    new LinearUnit(LinearUnit.Code.METER));   // 目前参数设置中使用单位为米
            mTrackLength += distance;
            gpsTrack.setRecordLength((int) mTrackLength);
        }
        return gpsTrack;
    }


    public void setTrackIdFromServer(long trackId) {
        if (mTrack != null) {
            mTrack.setId(trackId);
            mTrackId = trackId;
        }
    }


    //在startLocate之前调用
    public void initLocate() {
        lastLocateState = currentLocateState;
        currentLocateState = LOCATING;
        isPause = false;
        if (lastLocateState == NONE) {
//            mTrackId = System.currentTimeMillis();
//            mTrackId = TimeUtil.getTimestampRamdon();
            mLastKnowLocation = null;
            mTrack = new Track();
            mTrack.setId(mTrackId);
            mTrack.setTrackName(mTrackName);
//            mTrack.setStartTime(System.currentTimeMillis());
            mTrack.setLoginName(mLoginName);
        } else {

        }
    }


    public void startLocate() {
        if (mOnLocationChangeListener == null) {
            mOnLocationChangeListener = new OnLocationChangeListener();
        }

        ToastUtil.longToast(this, "正在校准位置，请稍等...");

        //进行定位
        BaiduLoactionManager.getInstance().startLocate(this, TrackConstant.locateIntervalTime * 1000,
                TrackConstant.locateIntervalDistance, mOnLocationChangeListener);
    }


    public void pauseLocate() {

        BaiduLoactionManager.getInstance().pauseLocate();
        lastLocateState = currentLocateState;
        currentLocateState = PAUSE;
        isPause = true;
        isFirstLoc = true;//设置成第一次定位
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
        //暂停时把所有未上传的轨迹点上传
        mTimeToUploadGPSTrackCallback.onCallback(null);
    }

    public void stopLocate() {
//        saveTrack();
//        MyLocationUtil.unregister(this);
        BaiduLoactionManager.getInstance().stopLocate();
        mLastKnowLocation = null;
        lastLocateState = currentLocateState;
        currentLocateState = NONE;
        isPause = true;
        isFirstLoc = true;//设置成第一次定位
        mTrackLength = 0;
        mTrackTimeSecond = 0;
        mTrackId = -1;
        mGPSTracks.clear();
        mTrack = null;
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
        //停止时把所有未上传的轨迹点上传
        mTimeToUploadGPSTrackCallback.onCallback(null);
    }

    public void saveTrackToLocal() {
        if (mTrack == null) {
            ToastUtil.shortToast(this, "保存轨迹失败");
            return;
        }
        mTrack.setTrackTime(mTrackTimeSecond);
        mTrack.setRecordLength(mTrackLength);
//        mTrack.setEndTime(System.currentTimeMillis());

        //保存最后定位的有效坐标，用以恢复
        if (!ListUtil.isEmpty(mGPSTracks)) {
            GPSTrack gpsTrack = mGPSTracks.get(mGPSTracks.size() - 1);
            mTrack.setLastLon(gpsTrack.getLongitude());
            mTrack.setLastLat(gpsTrack.getLatitude());
        }
        mLocalTrackSQLiteDao.save(mTrack);
    }

    //删除本地保存的轨迹
    public void deleteAllTrackFromLocal() {
        mLocalTrackSQLiteDao.deleteAll();
    }

    public Track getTrackFromLocal() {
        List<Track> tracks = mLocalTrackSQLiteDao.queryAll();
        if (!ListUtil.isEmpty(tracks)) {
            return tracks.get(tracks.size() - 1);
        }
        return null;
    }

    public void setRestoreState(Track trackFromLocal, List<GPSTrack> restoreTrackPoints, int locateState) {
        currentLocateState = locateState;
        mTrackTimeSecond = trackFromLocal.getTrackTime();
        mTrackLength = trackFromLocal.getRecordLength();
        mTrackId = trackFromLocal.getId();

        if (restoreTrackPoints != null) {
            mGPSTracks = restoreTrackPoints;
        }

        mTrack = new Track();
        mTrack.setId(mTrackId);
        mTrack.setLoginName(mLoginName);
        mTrack.setRecordLength(mTrackLength);

        mLastKnowLocation = new BDLocation();
        mLastKnowLocation.setLongitude(trackFromLocal.getLastLon());
        mLastKnowLocation.setLatitude(trackFromLocal.getLastLat());
    }


    public class OnLocationChangeListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (mTimer == null) {
                mTimer = new Timer();
                mTimer.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        mTrackTimeSecond++;
                        Message msg = mTrackHandler.obtainMessage();
                        msg.what = 111;
                        msg.getData().putInt("time", mTrackTimeSecond);
                        mTrackHandler.sendMessage(msg);
                        //上传轨迹点时间间隔
                        mTimeToUploadSecond++;
                        if (mTimeToUploadSecond >= TrackConstant.uploadIntervalTime * 60) {
                            Message msg2 = mTrackHandler.obtainMessage();
                            msg2.what = 444;
                            mTrackHandler.sendMessage(msg2);
                            mTimeToUploadSecond = 0;
                        }
                    }
                }, new Date(), 1000);
            }

            //gps位置精度大于50米的点直接弃用
            if (location.getRadius() > 50|| location.getRadius() == 0.0 || location.getLongitude() <= 0 || location.getLatitude() <= 0) {
                return;
            }

            location = locationTrasform(location); //转换坐标

            /**第一个点很重要，决定了轨迹的效果，gps刚接收到信号时返回的一些点精度不高，
             * 尽量选一个精度相对较高的起始点，这个过程大概从gps刚接收到信号后5-10秒就可以完成，不影响效果。
             * 注：gps接收卫星信号少则十几秒钟，多则几分钟，
             * 如果长时间手机收不到gps，退出，重启手机再试，这是硬件的原因
             */
            if (isFirstLoc) {
                BDLocation mostAccuracyLocation = getMostAccuracyLocation(location);
                if (mostAccuracyLocation == null) {
                    return;
                }

                isFirstLoc = false;

                ToastUtil.longToast(TrackNewService.this, "校准位置完成，正在定位中...");
                BaiduLoactionManager.getInstance().completeCorrectGps(TrackConstant.locateIntervalTime * 1000);

            }

            double distance = 0;
            if (mLastKnowLocation != null) {
                distance = sumDistance(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude(),
                        location.getLatitude(), location.getLongitude());
            }

            GPSTrack gpsTrace = getGPSTrack(location);
            mLastKnowLocation = location;

            //当前是第一个点又或者是两点距离大于服务器的值小于500米才上传
            if (gpsTrace.getPointState() == GPSTraceConstant.POINT_STAT_START ||
                    (distance >= TrackConstant.locateIntervalDistance && distance <= 500)) {


                mGPSTracks.add(gpsTrace);

                Message msg = mTrackHandler.obtainMessage();
                msg.what = 222;
                msg.getData().putDouble("length", gpsTrace.getRecordLength());
                mTrackHandler.sendMessage(msg);

                Message msg2 = mTrackHandler.obtainMessage();
                msg2.what = 333;
                msg2.getData().putSerializable("track", gpsTrace);
                mTrackHandler.sendMessage(msg2);
            }

        }

    }

    private List<BDLocation> points = new ArrayList<>();
    private BDLocation latestFirstLocation;

    //首次定位很重要，选一个精度相对较高的起始点
    private BDLocation getMostAccuracyLocation(final BDLocation location) {

        double distance;
        if (latestFirstLocation == null) {
            distance = sumDistance(0, 0, location.getLatitude(), location.getLongitude());
        } else {
            distance = sumDistance(latestFirstLocation.getLatitude(), latestFirstLocation.getLongitude(),
                    location.getLatitude(), location.getLongitude());
        }

        latestFirstLocation = location;
        if (distance > 10) {//有两点位置大于10米，重新来过
            points.clear();
            return null;
        }

        points.add(location);

        //有5个连续的点之间的距离肖羽10米，认为gps已稳定，以最新的点为起始点
        if (points.size() >= 5) {
            points.clear();
            return location;
        }
        return null;
    }

    public double sumDistance(double lat1, double lng1, double lat2, double lng2) {
        double a = Rad(lat1) - Rad(lat2);
        double b = Rad(lng1) - Rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(Rad(lat1)) * Math.cos(Rad(lat2)) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// WGS1984坐标系：6378137.0；现在80坐标系： 6378140.0 ；北京54坐标系：6378245.0;
        s = Math.round(s * 10000.0f) / 10000.0f;
        return s;
    }

    private static double Rad(double d) {
        return d * Math.PI / 180.0;
    }


    private boolean locationEquals(BDLocation location1, BDLocation location2) {
        if (location1 == null
                || location2 == null) {
            return false;
        }
        location1.equals(location2);
        return location1.getLongitude() == location2.getLongitude()
                && location1.getLatitude() == location2.getLatitude();
    }

    public void addLocationListener(OnTrackListener onTrackListener) {
        mOnTrackListeners.add(onTrackListener);
    }


    public List<OnTrackListener> getLocationListener() {
        return mOnTrackListeners;
    }

    public void removeLocationListener(OnTrackListener onTrackListener) {
        if (onTrackListener != null) {
            mOnTrackListeners.remove(onTrackListener);
        } else {
            mOnTrackListeners.clear();
        }
    }

    public void setTimeToUploadGPSTrackCallback(Callback1 timeToUploadGPSTrackCallback) {
        this.mTimeToUploadGPSTrackCallback = timeToUploadGPSTrackCallback;
    }

    public Callback1 getTimeToUploadGPSTrackCallback() {
        return mTimeToUploadGPSTrackCallback;
    }

    public Track getTrack() {
        return mTrack;
    }

    public void setTrack(Track mTrack) {
        this.mTrack = mTrack;
    }

    public List<GPSTrack> getmGPSTracks() {
        return mGPSTracks;
    }

    public void clearmGPSTracks() {
        mGPSTracks.clear();
    }

    public void setmGPSTracks(ArrayList<GPSTrack> mGPSTracks) {
        this.mGPSTracks = mGPSTracks;
    }

    public int getCurrentLocateState() {
        return currentLocateState;
    }

    /*public void setCurrentLocateState(@TrackLocateState int currentLocateState) {
        this.currentLocateState = currentLocateState;
    }*/

    public int getLastLocateState() {
        return lastLocateState;
    }

    public double getmTrackLength() {
        return mTrackLength;
    }

    public int getmTrackTimeSecond() {
        return mTrackTimeSecond;
    }

    public long getCurrentTrackId() {
        return mTrackId;
    }

}
