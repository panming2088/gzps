package com.augurit.agmobile.gzps.track.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;

import com.augurit.agmobile.gzps.track.dao.LocalTrackSQLiteDao;
import com.augurit.agmobile.gzps.track.model.Track;
import com.augurit.agmobile.gzps.track.util.MyLocationUtil;
import com.augurit.agmobile.gzps.track.util.OnTrackListener;
import com.augurit.agmobile.gzps.track.util.TimeUtil;
import com.augurit.agmobile.gzps.track.util.TrackConstant;
import com.augurit.agmobile.mapengine.common.CoordinateManager;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.agmobile.mapengine.gpsstrace.util.GPSTraceConstant;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.loc.ILocationTransform;
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
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.service
 * @createTime 创建时间 ：2017-06-08
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-08
 * @modifyMemo 修改备注：
 */
@Deprecated
public class TrackService {


    public static final long DEFAULT_MIN_DISTANCE = 0;
    public static final long DEFAULT_MIN_TIME = 1000;

    public static final int NONE = 0;
    public static final int LOCATING = 1;
    public static final int PAUSE = 2;

    @IntDef({NONE, LOCATING, PAUSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TrackLocateState {
    }

    private static TrackService instance;

    private String mLoginName;
    private Context mContext;
    private BDLocation mLastKnowLocation; //上传定位坐标点
    private boolean isPause = true;
    private Track mTrack = null;
    private ArrayList<GPSTrack> mGPSTracks = new ArrayList<>();
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

    @TrackLocateState
    private int currentLocateState = NONE;  //当前定位状态

    @TrackLocateState
    private int lastLocateState = NONE;  //上次定位状态


    private TrackService(Context context) {
        this.mContext = context;
        mLoginName = BaseInfoManager.getLoginName(mContext);
        mLocalTrackSQLiteDao = new LocalTrackSQLiteDao();
    }

    public static TrackService getInstance(Context context) {
        if (instance == null) {
            instance = new TrackService(context);
        }
        return instance;
    }

    private static class TrackHandler extends Handler {

        private final WeakReference<TrackService> mTrackService;

        public TrackHandler(TrackService trackService) {
            mTrackService = new WeakReference<TrackService>(trackService);
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

        ILocationTransform iLocationTransform = CoordinateManager.getInstance().getILocationTransform(mContext);
        if (iLocationTransform != null) {
            Coordinate coordinate3 = iLocationTransform.changeWGS84ToCurrentLocation(location.getLongitude(),
                    location.getLatitude());
            //如果坐标转换失败
            if (coordinate3 != null) {
                location.setLatitude(coordinate3.getY());
                location.setLongitude(coordinate3.getX());
            }
        }
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
            mTrack.setStartTime(System.currentTimeMillis());
            mTrack.setLoginName(mLoginName);
        } else {

        }
    }


    public void startLocate() {

        //进行定位
        MyLocationUtil.register(mContext, TrackConstant.locateIntervalTime * 1000,
                TrackConstant.locateIntervalDistance, mLocationChangeListener);
    }


    public void pauseLocate() {
        lastLocateState = currentLocateState;
        currentLocateState = PAUSE;
        isPause = true;
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
        //暂停时把所有未上传的轨迹点上传
        mTimeToUploadGPSTrackCallback.onCallback(null);
    }

    public void stopLocate() {
//        saveTrack();
        MyLocationUtil.unregister(mContext);
        mLastKnowLocation = null;
        lastLocateState = currentLocateState;
        currentLocateState = NONE;
        isPause = true;
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

    public void saveTrack() {
        if (mTrack == null) {
            throw new NullPointerException("mTrack变量为null");
        }
        mTrack.setRecordLength(mTrackLength);
        mTrack.setEndTime(System.currentTimeMillis());
        mLocalTrackSQLiteDao.save(mTrack);
    }

//    public void restoreTrack(List<GPSTrack> gpsTracks) {
//        Collections.sort(gpsTracks, new Comparator<GPSTrack>() {
//            @Override
//            public int compare(GPSTrack o1, GPSTrack o2) {
//                if (o1.getRecordDate() > o2.getRecordDate()) {
//                    return 1;
//                } else {
//                    return -1;
//                }
//            }
//        });
//        this.mGPSTracks.clear();
//        this.mGPSTracks.addAll(gpsTracks);
//        GPSTrack first = mGPSTracks.get(0);
//        GPSTrack last = mGPSTracks.get(mGPSTracks.size() - 1);
//        mTrackId = first.getTrackId();
//        mTrackLength = last.getRecordLength();
//        mTrackTimeSecond = (int) (last.getRecordDate() - first.getRecordDate()) / 1000;
//        lastLocateState = PAUSE;
//        currentLocateState = LOCATING;
//        isPause = false;
//        mLastKnowLocation = new Location(LocationManager.GPS_PROVIDER);
//        mLastKnowLocation.reset();
//        mLastKnowLocation.setProvider(LocationManager.GPS_PROVIDER);
//        mLastKnowLocation.setLongitude(last.getLongitude());
//        mLastKnowLocation.setLatitude(last.getLatitude());
//        startLocate();
//    }


    private MyLocationUtil.OnLocationChangeListener mLocationChangeListener = new MyLocationUtil.OnLocationChangeListener() {
        @Override
        public void getLastKnownLocation(BDLocation location) {
                /*if(!isPause){
                    location = locationTrasform(location);
                    GPSTrack gpsTrace = getGPSTrack(location);
                    mLastKnowLocation = location;
                    EventBus.getDefault().post(new TrackLengthEvent(mTrackLength));
                    locationListener.onSuccess(gpsTrace);
                }*/
        }

        @Override
        public void onLocationChanged(BDLocation location) {
            if (!isPause) {
                if (locationEquals(mLastKnowLocation, location)) {
                    return;
                }
                if (mLastKnowLocation == null || mTimer == null) {
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
                            if (mTimeToUploadSecond * 60 >= TrackConstant.uploadIntervalTime) {
                                Message msg2 = mTrackHandler.obtainMessage();
                                msg2.what = 444;
                                mTrackHandler.sendMessage(msg2);
                                mTimeToUploadSecond = 0;
                            }
                        }
                    }, new Date(), 1000);
                }
                location = locationTrasform(location);
                GPSTrack gpsTrace = getGPSTrack(location);
                mLastKnowLocation = location;
                mGPSTracks.add(gpsTrace);
//                    EventBus.getDefault().post(new TrackLengthEvent(mTrackLength));
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

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };


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
        mOnTrackListeners.remove(onTrackListener);
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

    public ArrayList<GPSTrack> getmGPSTracks() {
        return mGPSTracks;
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
