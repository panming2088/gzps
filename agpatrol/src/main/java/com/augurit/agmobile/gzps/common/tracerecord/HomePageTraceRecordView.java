package com.augurit.agmobile.gzps.common.tracerecord;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.track.service.TrackService;
import com.augurit.agmobile.gzps.track.util.LengthUtil;
import com.augurit.agmobile.gzps.track.util.TimeUtil;
import com.augurit.agmobile.gzps.track.util.TrackConstant;
import com.augurit.agmobile.gzps.track.util.TrackNotificationManager;
import com.augurit.agmobile.gzps.track.view.ITrackView;
import com.augurit.agmobile.gzps.track.view.ShowTrackActivity;
import com.augurit.agmobile.gzps.track.view.TrackActivity;
import com.augurit.agmobile.gzps.track.view.TrackRecordActivity;
import com.augurit.agmobile.gzps.track.view.presenter.ITrackPresenter;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.common.BaseView;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.augurit.agmobile.gzps.track.service.TrackService.LOCATING;
import static com.augurit.agmobile.gzps.track.service.TrackService.NONE;
import static com.augurit.agmobile.gzps.track.service.TrackService.PAUSE;

/**
 * 移动巡查主界面的轨迹界面，包括：定位按钮、开始按钮、轨迹列表按钮
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.tracerecord
 * @createTime 创建时间 ：2017-03-13
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-13
 * @modifyMemo 修改备注：
 */
@Deprecated
public class HomePageTraceRecordView extends BaseView<ITraceRecordPresenter> implements ITraceRecordView, ITrackView {

    protected View mRoot;
    protected ViewGroup mContainer;

    private ITrackPresenter mTrackPresenter;

    private TrackNotificationManager mTrackNotificationManager;

    private TextView track_time;
    private TextView track_length;
    private ViewGroup track_start;
    private ViewGroup track_stop;

    private ImageView tv_track_start;

    public HomePageTraceRecordView(Context context, ViewGroup container) {
        super(context);
        this.mContainer = container;
        mTrackNotificationManager = TrackNotificationManager.getInstance(mContext);
        mTrackNotificationManager.setStartBtnClickListener(new Callback1() {
            @Override
            public void onCallback(Object o) {
                if(mTrackPresenter.getCurrentTrackState() == LOCATING){
                    switchState(PAUSE);
                } else {
                    switchState(LOCATING);
                }
            }
        });
        initView();
    }

    private void initView() {
        mRoot = View.inflate(mContext, R.layout.tracerecord_view, null);
        /*
        mRoot.findViewById(R.id.rl_start_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(mContext, GPSTraceActivity.class);
                Intent intent = new Intent(mContext, TrackActivity.class);
                mContext.startActivity(intent);

            }
        });
        */
        mRoot.findViewById(R.id.track_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TrackActivity.class);
                mContext.startActivity(intent);
            }
        });
        mRoot.findViewById(R.id.track_locate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTrackRecordOnMapView(null, TrackConstant.TRACK_LOCATE);
            }
        });
        track_time = (TextView) mRoot.findViewById(R.id.track_time);
        track_length = (TextView) mRoot.findViewById(R.id.track_length);
        track_start = (ViewGroup) mRoot.findViewById(R.id.track_start);
        tv_track_start = (ImageView)mRoot.findViewById(R.id.tv_track_start);
        track_stop = (ViewGroup) mRoot.findViewById(R.id.track_stop);
        tv_track_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mTrackPresenter.getCurrentTrackState() == PAUSE
//                        || mTrackPresenter.getCurrentTrackState() == NONE){
//                    if(isGpsOpen()){
//                        switchState(LOCATING);
//                    } else {
//                        showMessage("请先打开GPS！");
//                        //下面括号内的字符串也可以改为“gps”
//                        mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
//                    }
//
//                } else {
//                    switchState(TrackService.PAUSE);
//                }
                Intent intent = new Intent(mContext, TrackRecordActivity.class);
                mContext.startActivity(intent);
            }
        });
        track_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switchState(TrackService.NONE);
                String tips = null;
                if(!checkCurrentTrackPointAmount()){
                    tips = "已记录轨迹点少于" + TrackConstant.minPointAmount + "个";
                }
                if(!checkCurrentTrackLength()){
                    if(tips != null){
                        tips = tips + "、";
                    } else {
                        tips = "";
                    }
                    tips = tips + "轨迹总长度不足" + TrackConstant.minLength + "米";
                }
                if(!checkCurrentTrackTime()){
                    if(tips != null){
                        tips = tips + "、";
                    } else {
                        tips = "";
                    }
                    tips = tips + "轨迹记录时间不足" + TrackConstant.minTime + "分钟";
                }
                if(tips == null){
                    switchState(TrackService.NONE);
                } else {
                    mTrackPresenter.switchState(PAUSE);
                    askStopTrack(tips, TrackService.NONE);
                }
            }
        });
    }

    private boolean checkCurrentTrackPointAmount(){
        List<GPSTrack> gpsTracks = mTrackPresenter.getGPSTracks();
        return !(ListUtil.isEmpty(gpsTracks)
                || gpsTracks.size() < TrackConstant.minPointAmount);
    }

    private boolean checkCurrentTrackLength(){
        double length = mTrackPresenter.getTrackLength();
        return length >= TrackConstant.minLength;
    }

    private boolean checkCurrentTrackTime(){
        int time = mTrackPresenter.getTrackTime();
        time = time / 60;
        return time >= TrackConstant.minTime;
    }

    private void askStopTrack(String tips, final int trackState) {
        new AlertDialog.Builder(mContext)
                .setTitle("对话框")
                .setMessage("当前" + tips +
                        "，若停止将不会保存当前轨迹，确定停止吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switchState(trackState);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTrackPresenter.switchState(LOCATING);
                    }
                })
                .create().show();
    }

    @Override
    public void initState(int currentTrackState) {
        if(currentTrackState == LOCATING){
//            track_locate.setText("查看当前轨迹");
           // tv_track_start.setText("暂停");
            track_stop.setEnabled(true);
            startFlick(tv_track_start);
            tv_track_start.setImageResource(R.drawable.stop_bg);
        } else if(currentTrackState == PAUSE){
          //  tv_track_start.setText("开始");
            track_stop.setEnabled(true);
//            showMessage("已暂停");
            stopFlick(tv_track_start);
            tv_track_start.setImageResource(R.drawable.start);
        } else if(currentTrackState == NONE){
          //  tv_track_start.setText("开始");
            stopFlick(tv_track_start);
            track_stop.setEnabled(false);
            tv_track_start.setImageResource(R.drawable.start);
        }
    }

    private void stopFlick(View view){
        if(view == null){
            return;
        }
        view.clearAnimation();
    }

    private void startFlick(View view){
        if(view == null){
            return;
        }

        Animation alphaAnimation = new AlphaAnimation( 1, 0.5f );
        alphaAnimation.setDuration( 1000 );
        alphaAnimation.setInterpolator( new LinearInterpolator( ) );
        alphaAnimation.setRepeatCount( Animation.INFINITE );
        alphaAnimation.setRepeatMode( Animation.REVERSE );
        view.startAnimation( alphaAnimation );
    }

    public void switchState(@TrackService.TrackLocateState int trackState){
        mTrackPresenter.switchState(trackState);
        switch (trackState){
            case LOCATING:
                mTrackNotificationManager.showNotification(mTrackPresenter.getCurrentTrackState());
//                track_locate.setText("查看当前轨迹");
            //    tv_track_start.setText("暂停");
                track_stop.setEnabled(true);
                showMessage("开始记录运动轨迹");
                break;
            case PAUSE:
            //    tv_track_start.setText("开始");
                showMessage("已暂停");
                mTrackNotificationManager.changeNotificationBtnImg(mTrackPresenter.getCurrentTrackState());
                break;
            case NONE:
                mTrackNotificationManager.dismissNotification();
              //  tv_track_start.setText("开始");
//                track_locate.setText("定位");
                track_stop.setEnabled(false);
                showMessage("停止记录运动轨迹");
                break;
            default:
                break;
        }

    }

    public boolean isGpsOpen(){
        LocationManager locationManager
                = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps;

    }


    public void showMessage(String msg) {
        ToastUtil.shortToast(mContext, msg);
    }

    @Override
    public void setPresenter(ITrackPresenter trackPresenter) {
        this.mTrackPresenter = trackPresenter;
    }

    @Override
    public void showTrackRecordOnMapView(ArrayList<GPSTrack> gpsTrackList, String operation) {
        Intent intent = new Intent(mContext, ShowTrackActivity.class);
        intent.putExtra("track", gpsTrackList);
        mContext.startActivity(intent);
    }

    @Override
    public void onLoadMore(List<List<GPSTrack>> moreTrack) {

    }

    @Override
    public void setTrackHistory(List<GPSTrack> trackHistory) {

    }

    @Override
    public void setTrackLength(double trackLength) {
        String length = LengthUtil.formatLength(trackLength);
        track_length.setText(length);
        mTrackNotificationManager.setTrackLength(length);
    }

    @Override
    public void setTrackTime(int trackTime) {
        String time = TimeUtil.formatSecond(trackTime);
        track_time.setText(time);
        mTrackNotificationManager.setTrackTime(time);
    }

    @Override
    public void addTraceRecordViewToContainer() {
        mContainer.removeAllViews();
        mContainer.addView(mRoot);
    }

    @Override
    public void showTraceRecordView() {
        mContainer.setVisibility(View.VISIBLE);
    }

    public View getmRoot() {
        return mRoot;
    }

    @Override
    public void setTrackPoint(GPSTrack trackPoint) {

    }
}
