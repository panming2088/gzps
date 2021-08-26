package com.augurit.agmobile.gzps.track.view;

import android.Manifest;
import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.track.service.TrackNewService;
import com.augurit.agmobile.gzps.track.util.DrawGPSHelper;
import com.augurit.agmobile.gzps.track.util.LengthUtil;
import com.augurit.agmobile.gzps.track.util.TimeUtil;
import com.augurit.agmobile.gzps.track.util.TrackConstant;
import com.augurit.agmobile.gzps.track.util.TrackNotificationManager;
import com.augurit.agmobile.gzps.track.view.presenter.ITrackPresenter;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

import static com.augurit.agmobile.gzps.track.service.TrackNewService.LOCATING;
import static com.augurit.agmobile.gzps.track.service.TrackNewService.NONE;
import static com.augurit.agmobile.gzps.track.service.TrackNewService.PAUSE;

/**
 * 开始，暂停，停止轨迹记录的界面；
 *
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view
 * @createTime 创建时间 ：2017-06-13
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-13
 * @modifyMemo 修改备注：
 */
public class TrackRecordView implements ITrackView, View.OnClickListener {

    private DrawGPSHelper mDrawGPSHelper;
    private Context mContext;
//    private ViewGroup mContainer;

    private TrackNotificationManager mTrackNotificationManager;

    private ITrackPresenter mTrackPresenter;
    private View mView;
    TextView track_time;
    TextView track_length;
    LinearLayout track_start;
    //    View track_pause;
    View track_stop;
    //    View track_cancle;
    TextView track_supend;
    //    Button track_locate;

    //    private ImageView iv_start_record;
    private MapView mapView;
    private TextView tv_trackrecord_countdown;

    /**
     * 记录时间的容器
     */
//    private View trace_time_containt;
    /**
     * 文本"0米"的容器
     */
    private View rl_trace_lenght_container;
    /**
     * 停止按钮的容器
     */
    private View ll_trace_stop_container;
//    private View ll_trace_cancle_container;
    private Location lastLocation;
    private View revealView;
    private LocationDisplayManager mLocationDisplayManager;
    private int mTrackState = NONE;

    private int locationGraphicId = -1;
    private GraphicsLayer mGLayerFroDrawLocation;
    private List<GpsStatusReceiver> mGpsStatusReceivers;
    // private View rl_controller_container;

    public TrackRecordView(Context context, View rootView) {
        this.mContext = context;
        this.mView = rootView;

        initView();
    }


    private void initView() {
//        mView.findViewById(R.id.btn_trace_map).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mLocationDisplayManager != null && mLocationDisplayManager.isStarted()) {
//                    mLocationDisplayManager.stop();
//                }
////                mTrackPresenter.directFinish(false);
//                mTrackPresenter.back();
//            }
//        });
        track_time = (TextView) mView.findViewById(R.id.track_time);
        track_length = (TextView) mView.findViewById(R.id.track_length);
        track_start = (LinearLayout) mView.findViewById(R.id.track_start);
        track_supend = (TextView) mView.findViewById(R.id.txt_track_suspend);
//        iv_start_record = (ImageView) mView.findViewById(R.id.iv_start_record);//开始
//        track_cancle = mView.findViewById(R.id.track_cancle);//开始
        track_stop = mView.findViewById(R.id.track_stop);
//        mView.findViewById(R.id.txt_track_locate).setOnClickListener(this);
        track_start.setOnClickListener(this);
        track_stop.setOnClickListener(this);
//        track_cancle.setOnClickListener(this);
//        mContainer.removeAllViews();
//        mContainer.addView(mView);


        //记录时间的容器
//        trace_time_containt = mView.findViewById(R.id.trace_time_containt);

        //文本"0米"的容器
        rl_trace_lenght_container = mView.findViewById(R.id.rl_trace_lenght_container);

        //停止按钮的容器
        ll_trace_stop_container = mView.findViewById(R.id.ll_trace_stop_container);
//        ll_trace_cancle_container = mView.findViewById(R.id.ll_trace_cancle_container);

        mapView = (MapView) mView.findViewById(R.id.mapview);
//        addLayer();

        //倒数计时的文本
        tv_trackrecord_countdown = (TextView) mView.findViewById(R.id.tv_trackrecord_countdown);

        //揭露视图
        revealView = mView.findViewById(R.id.second);

        //画轨迹线类
        mDrawGPSHelper = new DrawGPSHelper(mContext, mapView);
        //通知管理类
        mTrackNotificationManager = TrackNotificationManager.getInstance(mContext);

        mGpsStatusReceivers = new ArrayList<>();

    }

    @Override
    public void setTrackPoint(GPSTrack gpsTrack) {

        Log.e("坐标", "getLongitude==" + gpsTrack.getLongitude() + "-----" + "getLatitude" + gpsTrack.getLatitude());

        //根据返回的坐标点实时画线
        mDrawGPSHelper.drawPointLineOnMap(gpsTrack);

        //根据返回的坐标点实时画点和定位
        Point point = new Point(gpsTrack.getLongitude(), gpsTrack.getLatitude());
        drawLocationOnMap(point);
        mapView.centerAt(point, true);

    }

    private void drawLocationOnMap(Point point) {

        if (mGLayerFroDrawLocation == null) {
            mGLayerFroDrawLocation = new GraphicsLayer();
            mapView.addLayer(mGLayerFroDrawLocation);
        }
        if (locationGraphicId != -1) {
            //之前已经存在
            // Graphic graphic = new Graphic(point, pictureMarkerSymbol);
            mGLayerFroDrawLocation.updateGraphic(locationGraphicId, point);
        } else {
            //之前不存在
            PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext,
                    mContext.getResources().getDrawable(com.augurit.agmobile.patrolcore.R.mipmap.patrol_location_symbol));
            mGLayerFroDrawLocation.removeAll();
            Graphic graphic = new Graphic(point, pictureMarkerSymbol);
            locationGraphicId = mGLayerFroDrawLocation.addGraphic(graphic);
        }
    }


    @Override
    public void initState(int currentTrackState) {

        mTrackState = currentTrackState;

        if (currentTrackState == LOCATING) {
//            track_locate.setText("查看当前轨迹");
//            iv_start_record.setImageResource(R.mipmap.trace_supend_in);
            track_supend.setText("暂停");
            track_supend.setTextColor(Color.WHITE);
            track_stop.setEnabled(true);
            //改变背景颜色
            track_start.setBackgroundResource(R.drawable.orange_round_bg);
            revealView.setVisibility(View.INVISIBLE);
            tv_trackrecord_countdown.setVisibility(View.GONE);
            //当处于定位状态时隐藏结束按钮
            ll_trace_stop_container.setVisibility(View.GONE);

        } else if (currentTrackState == PAUSE) {

//            iv_start_record.setImageResource(R.mipmap.trace_ic_start_record);

//            ll_trace_cancle_container.setVisibility(View.GONE);
            //当暂停时，显示停止按钮
            ll_trace_stop_container.setVisibility(View.VISIBLE);

            track_supend.setText("继续");

            showMessage("已暂停");
        }
    }

    public void setPresenter(ITrackPresenter trackPresenter) {
        mTrackPresenter = trackPresenter;
    }

    public void showMessage(String msg) {
        ToastUtil.shortToast(mContext, msg);
    }

    @Override
    public void onClick(final View v) {
        PermissionsUtil.getInstance()
                .requestPermissions((Activity) mContext,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                onClickWithCheck(v);
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {

                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void onClickWithCheck(View v) {
        int id = v.getId();
        if (id == R.id.track_start) {
            if (mTrackPresenter.getCurrentTrackState() == PAUSE
                    || mTrackPresenter.getCurrentTrackState() == NONE) {
                if (isGpsOpen()) {
                    //满足以下条件执行动画：
                    //(1)处于第一次进来的状态
                    //(2)SDK版本大于23
                    //(3)倒数文字不为空
                    if (mTrackPresenter.getCurrentTrackState() == NONE && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && tv_trackrecord_countdown != null) {
                        //执行向外扩散的动画
                        revealAnimationShow(v);
                    } else {
                        //直接切换状态
                        switchState(LOCATING);
                    }
                } else {
                    showMessage("请先打开GPS！");
                    //下面括号内的字符串也可以改为“gps”
                    mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                }

            } else {
                switchState(TrackNewService.PAUSE);
            }
        } else if (id == R.id.track_stop) {
//            switchState(TrackService.NONE);
            String tips = null;
            if (!checkCurrentTrackPointAmount()) {
                tips = "已记录轨迹点少于" + TrackConstant.minPointAmount + "个";
            }
            if (!checkCurrentTrackLength()) {
                if (tips != null) {
                    tips = tips + "、";
                } else {
                    tips = "";
                }
                tips = tips + "轨迹总长度不足" + TrackConstant.minLength ;
            }
            if (!checkCurrentTrackTime()) {
                if (tips != null) {
                    tips = tips + "、";
                } else {
                    tips = "";
                }
                tips = tips + "轨迹记录时间不足" + TrackConstant.minTime + "分钟";
            }
            if (tips == null) {
                switchState(TrackNewService.NONE);
            } else {
                mTrackPresenter.switchState(PAUSE);
                askStopTrack(tips, TrackNewService.NONE);
            }
        }
//        else if (id == R.id.txt_track_locate) {
//            mTrackPresenter.locate();
//        }

//        else if (id == R.id.track_cancle) {
//            mTrackPresenter.back();
//        }
    }

    /**
     * 扩散动画
     *
     * @param view
     */
    @TargetApi(23)
    public void revealAnimationShow(final View view) {

        revealView.setVisibility(View.VISIBLE);

        int[] position = new int[2];

        view.getLocationOnScreen(position);

        int centerX = position[0] + view.getWidth() / 2;
        int centerY = position[1] + view.getHeight() / 2;

        // 获取扩散的半径
        int finalRadius = Math.max(revealView.getWidth(), revealView.getHeight());
        // 定义揭露动画
        //mView.setBackgroundColor(mContext.getResources().getColor(R.color.agpatrol_blue));
        Animator anim = ViewAnimationUtils.createCircularReveal(
                revealView, centerX, centerY, 0, finalRadius);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //执行倒数动画
                tv_trackrecord_countdown.setVisibility(View.VISIBLE);
                countDownAnimation(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }


    @TargetApi(23)
    public void revealAnimationHide(View view) {

        int[] position = new int[2];

        view.getLocationOnScreen(position);

        int cx = 0;
        int cy = 0;
        int initialRadius = revealView.getWidth();

        cx = position[0] + view.getWidth() / 2;
        cy = position[1] + view.getHeight() / 2;

        Animator anim = ViewAnimationUtils.createCircularReveal(revealView, cx, cy, initialRadius, 0);
        // mView.setBackgroundColor(mContext.getResources().getColor(R.color.agmobile_white));
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //改变背景颜色,不然等到结束后再改变有点突兀
                track_start.setBackgroundResource(R.drawable.orange_round_bg);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                switchState(LOCATING);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    /**
     * 倒数计时动画
     */
    public void countDownAnimation(final View view) {
        countdown(3)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        //执行向里收缩的动画
                        revealAnimationHide(view);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer == 0) {
                            tv_trackrecord_countdown.setText("GO");
                        } else {
                            tv_trackrecord_countdown.setText(integer + "");
                        }
                    }
                });
    }

    /**
     * 倒计时
     *
     * @param time
     * @return
     */
    public Observable<Integer> countdown(int time) {
        if (time < 1) time = 1;

        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);

    }


    private boolean checkCurrentTrackPointAmount() {
        List<GPSTrack> gpsTracks = mTrackPresenter.getGPSTracks();
        return !(ListUtil.isEmpty(gpsTracks)
                || gpsTracks.size() < TrackConstant.minPointAmount);
    }

    private boolean checkCurrentTrackLength() {
        double length = mTrackPresenter.getTrackLength();
        return length >= TrackConstant.minLength;
    }

    private boolean checkCurrentTrackTime() {
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
//                        mTrackPresenter.switchState(LOCATING);

                    }
                })
                .create().show();
    }

    @Override
    public void switchState(@TrackNewService.TrackLocateState int trackState) {
        mTrackState = trackState;
        mTrackPresenter.switchState(trackState);
        switch (mTrackState) {
            case LOCATING:

                checkGPSOpen();

                mTrackNotificationManager.showNotification(mTrackPresenter.getCurrentTrackState());
//                track_locate.setText("查看当前轨迹");
//                iv_start_record.setImageResource(R.mipmap.trace_supend_in);
                track_supend.setText("暂停");
                track_supend.setTextColor(Color.WHITE);
//                track_stop.setEnabled(true);

                //改变背景颜色
                track_start.setBackgroundResource(R.drawable.orange_round_bg);
                revealView.setVisibility(View.INVISIBLE);
                tv_trackrecord_countdown.setVisibility(View.GONE);
                //当处于定位状态时隐藏结束按钮
                ll_trace_stop_container.setVisibility(View.GONE);
//                ll_trace_cancle_container.setVisibility(View.GONE);

                rl_trace_lenght_container.setVisibility(View.VISIBLE);
//                trace_time_containt.setVisibility(View.VISIBLE);
                // rl_controller_container.setBackgroundColor(mContext.getResources().getColor(R.color.agpatrol_blue));

                showMessage("开始记录运动轨迹");
                break;
            case PAUSE:
//                iv_start_record.setImageResource(R.mipmap.trace_ic_start_record);

                //改变背景颜色
                track_start.setBackgroundResource(R.drawable.blue_round_bg);

                //当暂停时，显示停止按钮
                //animateButtonsIn(ll_trace_stop_container);
                ll_trace_stop_container.setVisibility(View.VISIBLE);
//                ll_trace_cancle_container.setVisibility(View.GONE);

                track_supend.setText("继续");
                // track_supend.setTextColor(mContext.getResources().getColor(R.color.agpatrol_blue));

                showMessage("已暂停");

                mTrackNotificationManager.changeNotificationBtnImg(mTrackPresenter.getCurrentTrackState());

                break;

            case NONE:

                removeGPSCheck();

                track_supend.setText("开始");

                //改变背景颜色
                track_start.setBackgroundResource(R.drawable.blue_round_bg);

                //  track_supend.setTextColor(mContext.getResources().getColor(R.color.agpatrol_blue));
//                track_locate.setText("定位");
//                track_stop.setEnabled(false);

                track_length.setText("0米");
                track_time.setText("00:00:00");

                ll_trace_stop_container.setVisibility(View.GONE);
//                ll_trace_cancle_container.setVisibility(View.VISIBLE);
                rl_trace_lenght_container.setVisibility(View.VISIBLE);
//                trace_time_containt.setVisibility(View.VISIBLE);
                // rl_controller_container.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));

                mDrawGPSHelper.clearTrack();
                if (mGLayerFroDrawLocation != null) {
                    mGLayerFroDrawLocation.removeAll();
                    locationGraphicId = -1;
                }

                mTrackNotificationManager.dismissNotification();

                showMessage("停止记录运动轨迹");
                break;
            default:
                break;
        }
    }

    public void showTrackRecordOnMapView(ArrayList<GPSTrack> gpsTrackList, String operation) {
        Intent intent = new Intent(mContext, ShowTrackActivity.class);
        if (!ListUtil.isEmpty(gpsTrackList)) {
            intent.putExtra("trackId", "" + gpsTrackList.get(0).getTrackId());
        }
        intent.putExtra("operation", operation);
        mContext.startActivity(intent);
    }

    public void setTrackHistory(List<GPSTrack> trackHistory) {
        mDrawGPSHelper.drawGPSTrackOnMapWithStartMark(trackHistory);
        if (!ListUtil.isEmpty(trackHistory)) {
            GPSTrack gpsTrack = trackHistory.get(trackHistory.size() - 1);
            //根据返回的坐标点实时画点和定位
            Point point = new Point(gpsTrack.getLongitude(), gpsTrack.getLatitude());

            mapView.centerAt(point, true);
            drawLocationOnMap(point);
        }

    }

    @Override
    public void onLoadMore(List<List<GPSTrack>> moreTrack) {

    }

    @Override
    public void setTrackLength(double trackLength) {
        String length = LengthUtil.formatLength(trackLength);
        track_length.setText(String.valueOf(length));
        mTrackNotificationManager.setTrackLength(length);
    }

    @Override
    public void setTrackTime(int trackTime) {
        String time = TimeUtil.formatSecond(trackTime);
        track_time.setText(time);
        mTrackNotificationManager.setTrackTime(time);
    }

    public boolean isGpsOpen() {
        LocationManager locationManager
                = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = false;
        if (locationManager != null) {
            gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps;

    }


    //GPS监听
    private void checkGPSOpen() {
        if (mGpsStatusReceivers.isEmpty()) {
            GpsStatusReceiver gpsStatusReceiver = new GpsStatusReceiver();
            mGpsStatusReceivers.add(gpsStatusReceiver);
            IntentFilter filter = new IntentFilter();
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
            mContext.registerReceiver(gpsStatusReceiver, filter);
        }
    }


    //移除GPS监听
    private void removeGPSCheck() {
        try {
            for (GpsStatusReceiver gpsStatusReceiver : mGpsStatusReceivers) {
                mContext.unregisterReceiver(gpsStatusReceiver);
            }
            mGpsStatusReceivers.clear();
        } catch (Exception e) {
            e.printStackTrace();
            mGpsStatusReceivers.clear();
        }
    }

    /**
     * 监听GPS 状态变化广播
     */
    private class GpsStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(action)) {
                if (!isGpsOpen()) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("对话框")
                            .setMessage("请打开GPS，否则巡检不可用")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create().show();
                }
            }
        }
    }


}