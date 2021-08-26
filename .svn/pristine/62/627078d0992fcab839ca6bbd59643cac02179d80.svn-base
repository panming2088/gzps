package com.augurit.agmobile.gzps.track.view.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.StringResult;
import com.augurit.agmobile.gzps.track.model.Track;
import com.augurit.agmobile.gzps.track.service.TrackNetService;
import com.augurit.agmobile.gzps.track.service.TrackNewService;
import com.augurit.agmobile.gzps.track.util.OnTrackListener;
import com.augurit.agmobile.gzps.track.util.TrackConstant;
import com.augurit.agmobile.gzps.track.view.ITrackView;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.agmobile.mapengine.gpsstrace.service.LocalTrackServiceImpl;
import com.augurit.agmobile.mapengine.gpsstrace.util.GPSTraceConstant;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.augurit.agmobile.gzps.track.service.TrackNewService.LOCATING;
import static com.augurit.agmobile.gzps.track.service.TrackNewService.NONE;
import static com.augurit.agmobile.gzps.track.service.TrackNewService.PAUSE;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view.presenter
 * @createTime 创建时间 ：2017-06-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-14
 * @modifyMemo 修改备注：
 */

public class TrackPresenter implements ITrackPresenter {
    private Context mContext;
    private ITrackView mTrackView;
    private Callback1 mBackListener;
    private TrackNewService mTrackService;
    private TrackNetService mTrackNetService;
    private LocalTrackServiceImpl mLocalTrackService;
    private OnTrackListener mOnTrackListener;

    public TrackPresenter(Context context, ITrackView trackView) {
        this.mContext = context;
        this.mTrackView = trackView;
        this.mTrackView.setPresenter(this);

        mTrackNetService = new TrackNetService(mContext);

        mLocalTrackService = new LocalTrackServiceImpl();

        mTrackNetService.initTrackConfig();    //初始化轨迹记录参数

        mOnTrackListener = new OnTrackListener() {
            @Override
            public void onTrack(GPSTrack gpsTrack) {
                //保存坐标点到本地
                mLocalTrackService.saveTrackPoint(gpsTrack);

                //保存轨迹到本地，用以意外关闭恢复
                mTrackService.saveTrackToLocal();

                //更新轨迹界面
                mTrackView.setTrackPoint(gpsTrack);

            }

            @Override
            public void onTime(int second) {
                mTrackView.setTrackTime(second);
            }

            @Override
            public void onLength(double length) {
                mTrackView.setTrackLength(length);
            }
        };

        //绑定定位Servicey以获取Service对象
        bindService();

    }

    public void bindService() {
        if (mTrackService == null) {
            Intent intent = new Intent(mContext, TrackNewService.class);
            mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        }
    }


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mTrackService = ((TrackNewService.MyBinder) service).getService();

            //先移除之前所有监听
            mTrackService.removeLocationListener(null);
            mTrackService.addLocationListener(mOnTrackListener);
            mTrackService.setTimeToUploadGPSTrackCallback(new Callback1() {
                @Override
                public void onCallback(Object o) {
                    uploadAllGPSTrack();
                }
            });

            if (getCurrentTrackState() == NONE) {
                checkTrackIsSubmit();
            } else {
                continueTrack();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mTrackService = null;
        }
    };

    private void continueTrack() {
        String mLoginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        Track trackFromLocal = mTrackService.getTrackFromLocal();
        if(NONE != mTrackService.getCurrentLocateState()){
            mTrackView.initState(getCurrentTrackState());
            if(trackFromLocal != null) {
                mTrackView.setTrackLength(trackFromLocal.getRecordLength());
            }
        }
        if (trackFromLocal != null && mLoginName.equals(trackFromLocal.getLoginName()) && getCurrentTrackState() == NONE) {
            restoreTrack(trackFromLocal, getCurrentTrackState());
        }
    }

    private void restoreTrack(Track trackFromLocal, int locateState) {
        mTrackView.initState(locateState);
        mTrackView.setTrackTime(trackFromLocal.getTrackTime());
        mTrackView.setTrackLength(trackFromLocal.getRecordLength());

        List<GPSTrack> restoreTrackPoints = mLocalTrackService
                .getGPSTracksByTrackId(trackFromLocal.getId() + "");
        mTrackView.setTrackHistory(restoreTrackPoints);
        mTrackService.setRestoreState(trackFromLocal, restoreTrackPoints, locateState);
        uploadAllGPSTrack();
    }

    private void checkTrackIsSubmit() {

        String mLoginName = BaseInfoManager.getLoginName(mContext);

        //检查上一次是否有没完成的巡检轨迹
        final Track trackFromLocal = mTrackService.getTrackFromLocal();
        if (trackFromLocal != null && mLoginName.equals(trackFromLocal.getLoginName())) {
            new AlertDialog.Builder(mContext)
                    .setTitle("对话框")
                    .setMessage("检测到本地存在未上传的轨迹，是否恢复上一次巡检？")
                    .setPositiveButton("恢复", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtil.shortToast(mContext, "正在恢复中...");

                            mTrackView.initState(PAUSE);
                            mTrackView.setTrackTime(trackFromLocal.getTrackTime());
                            mTrackView.setTrackLength(trackFromLocal.getRecordLength());

                            List<GPSTrack> restoreTrackPoints = mLocalTrackService
                                    .getGPSTracksByTrackId(trackFromLocal.getId() + "");

                            mTrackView.setTrackHistory(restoreTrackPoints);

                            mTrackService.setRestoreState(trackFromLocal, restoreTrackPoints, PAUSE);

                            uploadAllGPSTrack();

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

//                            deleteTrackAndStop(trackFromLocal.getId(), false);
                            saveTrackToServer(trackFromLocal,NONE);
                        }
                    })
                    .create().show();
        }
    }


    public void switchState(@TrackNewService.TrackLocateState int trackState) {

        if (mTrackService == null) {
            return;
        }

        switch (trackState) {
            case LOCATING:

                mTrackService.initLocate();
                if (mTrackService.getTrack().getId() == -1) {//第一次开始创建轨迹
                    saveTrackToServer(mTrackService.getTrack(), trackState);
                } else {
                    mTrackService.startLocate();
                }

                break;
            case PAUSE:
                mTrackService.pauseLocate();
                break;
            case NONE:
                List<GPSTrack> gpsTracks = mTrackService.getmGPSTracks();
                if (ListUtil.isEmpty(gpsTracks)
                        || gpsTracks.size() < TrackConstant.minPointAmount
//                        || getTrackLength() == 0
                        || getTrackLength() < TrackConstant.minLength
//                        || getTrackTime() == 0
                        || getTrackTime() / 60 < TrackConstant.minTime) {
                    mLocalTrackService.deleteGPSTrackSByTrackId("" + mTrackService.getCurrentTrackId());
                    deleteTrackAndStop(mTrackService.getTrack().getId(), true);
                } else {
//                    mLocalTrackService.saveTrackPoint(gpsTracks);
                    GPSTrack lastestGPSTrack = gpsTracks.get(gpsTracks.size() - 1);
                    lastestGPSTrack.setPointState(GPSTraceConstant.POINT_STAT_END);
                    lastestGPSTrack.setRecordDate(System.currentTimeMillis());
                    mLocalTrackService.saveTrackPoint(lastestGPSTrack);
//                    mTrackService.saveTrack();
                    saveTrackToServer(mTrackService.getTrack(), trackState);
                }

                break;
            default:
                break;
        }
    }


    public void stopService() {

        mTrackService.stopLocate();

        //停止后台服务
        Intent service = new Intent(mContext, TrackNewService.class);
        mContext.stopService(service);

//        back();
    }


    @Override
    public void locate() {

        if (mTrackService == null || mTrackService.getCurrentLocateState() == NONE) {
            mTrackView.showTrackRecordOnMapView(null, TrackConstant.TRACK_LOCATE);
            return;
        }
        if (getGPSTracks().size() < 2) {
            mTrackView.showMessage("当前轨迹点少于两个，无法查看轨迹！");
        } else {
            mTrackView.showTrackRecordOnMapView(null, TrackConstant.TRACK_FOLLOW);
        }
    }

    //保存轨迹到服务器 上传trackid为-1时是第一次保存，返回trackid作为这条轨迹的id，结束时也上传该trackid修改
    private void saveTrackToServer(Track track, final int trackState) {

        mTrackNetService.saveTraceLine(track)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result2<Long>>() {
                    @Override
                    public void onCompleted() {

                        if (trackState == NONE) {
                            stopService();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast(mContext, trackState == LOCATING ? "创建轨迹记录失败" : "上传轨迹记录失败，再次点击巡检恢复");

                        if (trackState == LOCATING) {
                            mTrackView.switchState(NONE);
                        }

                        onCompleted();

                    }

                    @Override
                    public void onNext(Result2<Long> result) {
                        if (trackState == LOCATING) {
                            if (result != null) {
                                mTrackService.setTrackIdFromServer(result.getData());

                                //开启后台服务
                                Intent service = new Intent(mContext, TrackNewService.class);
                                mContext.startService(service);

                                mTrackService.startLocate();

                            } else {

                                ToastUtil.shortToast(mContext, "创建轨迹记录失败");
                                mTrackView.switchState(NONE);

                            }
                        } else if (trackState == NONE) {

                            ToastUtil.shortToast(mContext, "轨迹上传成功");

                            //删除本地保存的轨迹
                            mTrackService.deleteAllTrackFromLocal();

                        }
                    }
                });
    }

    public long getCurrentTrackId() {
        return mTrackService.getCurrentTrackId();
    }

    private void uploadAllGPSTrack() {
        if (mTrackService != null) {
//            final long currentTrackId = mTrackService.getCurrentTrackId();

            final long currentTrackId = getCurrentTrackId();
            new Thread() {
                public void run() {
                    List<GPSTrack> needToUploadTrackList = mLocalTrackService.getUnUploadGPSTrack(currentTrackId);
                    for (GPSTrack gpsTrack : needToUploadTrackList) {
                        uploadGPSTrack(gpsTrack);
                    }
                }
            }.start();
        }
    }

    private void uploadGPSTrack(final GPSTrack gpsTrack) {
        mTrackNetService.saveTracePoint(gpsTrack)
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<Result2<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Result2<String> stringResult) {
                        if (stringResult.getCode() == 200 || stringResult.isSuccess()) {
                            gpsTrack.setUploadState(1);
                            mLocalTrackService.saveTrackPoint(gpsTrack);
                        }
                    }
                });
    }

    private void deleteTrackAndStop(long trackId, final boolean isStopLoc) {
        mTrackNetService.deleteTraceLine(trackId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StringResult>() {
                    @Override
                    public void onCompleted() {

                        //删除本地保存的轨迹
                        mTrackService.deleteAllTrackFromLocal();

                        if (isStopLoc) {
                            stopService();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.shortToast(mContext, "删除轨迹失败");
                        onCompleted();
                    }

                    @Override
                    public void onNext(StringResult stringResult) {

                    }
                });
    }

    @Override
    public int getCurrentTrackState() {
        if (mTrackService != null) {
            return mTrackService.getCurrentLocateState();
        } else {
            return NONE;
        }
    }

    @Override
    public List<GPSTrack> getGPSTracks() {
//        if (mTrackService != null) {
//            return mLocalTrackService.getGPSTracksByTrackId(mTrackService.getTrack().getId() + "");
//        }
//        return null;
        if (mTrackService != null) {
            return mTrackService.getmGPSTracks();
        }
        return null;
    }

    @Override
    public double getTrackLength() {
        return mTrackService.getmTrackLength();
    }

    @Override
    public int getTrackTime() {
        return mTrackService.getmTrackTimeSecond();
    }

    public void back() {

        if (mTrackService != null) {
            mContext.unbindService(conn);
//            mTrackService = null;
        }

        if (mBackListener != null) {
            mBackListener.onCallback(null);
        }
    }

    public void setBackListener(Callback1 backListener) {
        this.mBackListener = backListener;
    }


}
