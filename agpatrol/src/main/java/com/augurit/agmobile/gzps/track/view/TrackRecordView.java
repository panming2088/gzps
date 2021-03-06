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
 * ????????????????????????????????????????????????
 *
 * @author ????????? ???liangshenghong
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.agpatrol.track.view
 * @createTime ???????????? ???2017-06-13
 * @modifyBy ????????? ???liangshenghong
 * @modifyTime ???????????? ???2017-06-13
 * @modifyMemo ???????????????
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
     * ?????????????????????
     */
//    private View trace_time_containt;
    /**
     * ??????"0???"?????????
     */
    private View rl_trace_lenght_container;
    /**
     * ?????????????????????
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
//        iv_start_record = (ImageView) mView.findViewById(R.id.iv_start_record);//??????
//        track_cancle = mView.findViewById(R.id.track_cancle);//??????
        track_stop = mView.findViewById(R.id.track_stop);
//        mView.findViewById(R.id.txt_track_locate).setOnClickListener(this);
        track_start.setOnClickListener(this);
        track_stop.setOnClickListener(this);
//        track_cancle.setOnClickListener(this);
//        mContainer.removeAllViews();
//        mContainer.addView(mView);


        //?????????????????????
//        trace_time_containt = mView.findViewById(R.id.trace_time_containt);

        //??????"0???"?????????
        rl_trace_lenght_container = mView.findViewById(R.id.rl_trace_lenght_container);

        //?????????????????????
        ll_trace_stop_container = mView.findViewById(R.id.ll_trace_stop_container);
//        ll_trace_cancle_container = mView.findViewById(R.id.ll_trace_cancle_container);

        mapView = (MapView) mView.findViewById(R.id.mapview);
//        addLayer();

        //?????????????????????
        tv_trackrecord_countdown = (TextView) mView.findViewById(R.id.tv_trackrecord_countdown);

        //????????????
        revealView = mView.findViewById(R.id.second);

        //???????????????
        mDrawGPSHelper = new DrawGPSHelper(mContext, mapView);
        //???????????????
        mTrackNotificationManager = TrackNotificationManager.getInstance(mContext);

        mGpsStatusReceivers = new ArrayList<>();

    }

    @Override
    public void setTrackPoint(GPSTrack gpsTrack) {

        Log.e("??????", "getLongitude==" + gpsTrack.getLongitude() + "-----" + "getLatitude" + gpsTrack.getLatitude());

        //????????????????????????????????????
        mDrawGPSHelper.drawPointLineOnMap(gpsTrack);

        //?????????????????????????????????????????????
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
            //??????????????????
            // Graphic graphic = new Graphic(point, pictureMarkerSymbol);
            mGLayerFroDrawLocation.updateGraphic(locationGraphicId, point);
        } else {
            //???????????????
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
//            track_locate.setText("??????????????????");
//            iv_start_record.setImageResource(R.mipmap.trace_supend_in);
            track_supend.setText("??????");
            track_supend.setTextColor(Color.WHITE);
            track_stop.setEnabled(true);
            //??????????????????
            track_start.setBackgroundResource(R.drawable.orange_round_bg);
            revealView.setVisibility(View.INVISIBLE);
            tv_trackrecord_countdown.setVisibility(View.GONE);
            //??????????????????????????????????????????
            ll_trace_stop_container.setVisibility(View.GONE);

        } else if (currentTrackState == PAUSE) {

//            iv_start_record.setImageResource(R.mipmap.trace_ic_start_record);

//            ll_trace_cancle_container.setVisibility(View.GONE);
            //?????????????????????????????????
            ll_trace_stop_container.setVisibility(View.VISIBLE);

            track_supend.setText("??????");

            showMessage("?????????");
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
                    //?????????????????????????????????
                    //(1)??????????????????????????????
                    //(2)SDK????????????23
                    //(3)?????????????????????
                    if (mTrackPresenter.getCurrentTrackState() == NONE && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && tv_trackrecord_countdown != null) {
                        //???????????????????????????
                        revealAnimationShow(v);
                    } else {
                        //??????????????????
                        switchState(LOCATING);
                    }
                } else {
                    showMessage("????????????GPS???");
                    //?????????????????????????????????????????????gps???
                    mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                }

            } else {
                switchState(TrackNewService.PAUSE);
            }
        } else if (id == R.id.track_stop) {
//            switchState(TrackService.NONE);
            String tips = null;
            if (!checkCurrentTrackPointAmount()) {
                tips = "????????????????????????" + TrackConstant.minPointAmount + "???";
            }
            if (!checkCurrentTrackLength()) {
                if (tips != null) {
                    tips = tips + "???";
                } else {
                    tips = "";
                }
                tips = tips + "?????????????????????" + TrackConstant.minLength ;
            }
            if (!checkCurrentTrackTime()) {
                if (tips != null) {
                    tips = tips + "???";
                } else {
                    tips = "";
                }
                tips = tips + "????????????????????????" + TrackConstant.minTime + "??????";
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
     * ????????????
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

        // ?????????????????????
        int finalRadius = Math.max(revealView.getWidth(), revealView.getHeight());
        // ??????????????????
        //mView.setBackgroundColor(mContext.getResources().getColor(R.color.agpatrol_blue));
        Animator anim = ViewAnimationUtils.createCircularReveal(
                revealView, centerX, centerY, 0, finalRadius);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //??????????????????
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
                //??????????????????,??????????????????????????????????????????
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
     * ??????????????????
     */
    public void countDownAnimation(final View view) {
        countdown(3)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        //???????????????????????????
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
     * ?????????
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
                .setTitle("?????????")
                .setMessage("??????" + tips +
                        "????????????????????????????????????????????????????????????")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switchState(trackState);
                    }
                })
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
//                track_locate.setText("??????????????????");
//                iv_start_record.setImageResource(R.mipmap.trace_supend_in);
                track_supend.setText("??????");
                track_supend.setTextColor(Color.WHITE);
//                track_stop.setEnabled(true);

                //??????????????????
                track_start.setBackgroundResource(R.drawable.orange_round_bg);
                revealView.setVisibility(View.INVISIBLE);
                tv_trackrecord_countdown.setVisibility(View.GONE);
                //??????????????????????????????????????????
                ll_trace_stop_container.setVisibility(View.GONE);
//                ll_trace_cancle_container.setVisibility(View.GONE);

                rl_trace_lenght_container.setVisibility(View.VISIBLE);
//                trace_time_containt.setVisibility(View.VISIBLE);
                // rl_controller_container.setBackgroundColor(mContext.getResources().getColor(R.color.agpatrol_blue));

                showMessage("????????????????????????");
                break;
            case PAUSE:
//                iv_start_record.setImageResource(R.mipmap.trace_ic_start_record);

                //??????????????????
                track_start.setBackgroundResource(R.drawable.blue_round_bg);

                //?????????????????????????????????
                //animateButtonsIn(ll_trace_stop_container);
                ll_trace_stop_container.setVisibility(View.VISIBLE);
//                ll_trace_cancle_container.setVisibility(View.GONE);

                track_supend.setText("??????");
                // track_supend.setTextColor(mContext.getResources().getColor(R.color.agpatrol_blue));

                showMessage("?????????");

                mTrackNotificationManager.changeNotificationBtnImg(mTrackPresenter.getCurrentTrackState());

                break;

            case NONE:

                removeGPSCheck();

                track_supend.setText("??????");

                //??????????????????
                track_start.setBackgroundResource(R.drawable.blue_round_bg);

                //  track_supend.setTextColor(mContext.getResources().getColor(R.color.agpatrol_blue));
//                track_locate.setText("??????");
//                track_stop.setEnabled(false);

                track_length.setText("0???");
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

                showMessage("????????????????????????");
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
            //?????????????????????????????????????????????
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
        // ??????GPS??????????????????????????????????????????????????????24????????????????????????????????????????????????????????????????????????
        boolean gps = false;
        if (locationManager != null) {
            gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        // ??????WLAN???????????????(3G/2G)???????????????????????????AGPS?????????GPS??????????????????????????????????????????????????????????????????????????????????????????????????????
//        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps;

    }


    //GPS??????
    private void checkGPSOpen() {
        if (mGpsStatusReceivers.isEmpty()) {
            GpsStatusReceiver gpsStatusReceiver = new GpsStatusReceiver();
            mGpsStatusReceivers.add(gpsStatusReceiver);
            IntentFilter filter = new IntentFilter();
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
            mContext.registerReceiver(gpsStatusReceiver, filter);
        }
    }


    //??????GPS??????
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
     * ??????GPS ??????????????????
     */
    private class GpsStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(action)) {
                if (!isGpsOpen()) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("?????????")
                            .setMessage("?????????GPS????????????????????????")
                            .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNegativeButton("??????", new DialogInterface.OnClickListener() {
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