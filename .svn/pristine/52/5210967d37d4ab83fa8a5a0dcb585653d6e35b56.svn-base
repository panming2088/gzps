package com.augurit.am.cmpt.loc.mgr;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * 包名：com.augurit.am.cmpt.loc.mgr
 * 文件描述：从AGPAD中复制过来的定位管理者，通用
 * 创建人：xuciluan
 * 创建时间：2016-10-11 13:49
 * 修改人：xuciluan
 * 修改时间：2016-10-11 13:49
 * 修改备注：
 */

public class MLocationManager  {

    private LocationManager locationManager;
    // private static MLocationManager instance;
    private String provider;

    private Context mContext;
    private LocationListener mLocationListener;
    private short mMinTime;
    private byte mMinDistance;
    private AlertDialog mAlertDialog;
    private Subscription mSubscription;

    public MLocationManager(Context context) {
        this.mContext = context;
        this.locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        this.initProvider();
        mMinTime = 1000;
        mMinDistance = 1;
    }

    /**
     * 设置监听
     * @param onLocationChangeListener
     */
    public void setOnLocationChangeListener(final LocationListener onLocationChangeListener) {
        //首先需要判断是否打开了GPS
        if (!isGPSOpen()) {
            showSetGPSDialogOrNot();
        }
        this.mLocationListener = onLocationChangeListener;
    }

    /**
     * 开始定位
     */
    public void start() {
        requestBestLocation(mLocationListener, mMinTime, mMinDistance);
        //开启监控网络和GPS的监听，当检测到没有网络或者GPS的时候，将情况记录下来，方便以后上传
        startWatchGPSAndNetwork();
    }


    private void startWatchGPSAndNetwork() {
        //检查GPS和网络是否可用
        mSubscription = Observable.interval(5, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        //检查GPS和网络是否可用
                        if (!canLocate()) {
                            showSetGPSDialogOrNot();
                        }
                        //TODO 在这里执行一些保存操作，比如保存断网期间的内容,记得切换线程

                    }
                });
    }

    /**
     * 进行请求定位，并且返回最精确的位置
     *
     * @param onLocationChangeListener 定位监听
     * @param minTime                  间隔
     * @param minDistance              距离
     */
    private void requestBestLocation(final LocationListener onLocationChangeListener, long minTime, float minDistance) {
        if (this.provider != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //如果有更好的provider
            this.locationManager.requestLocationUpdates(this.provider, minTime, minDistance, getListener(onLocationChangeListener));
        }

        //按道理是不会进行到这一步的
        else {
            boolean providerEnabled = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (providerEnabled) {
                //如果没有，默认使用网络定位
                this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, getListener(onLocationChangeListener));
            }else {
                if (mAlertDialog == null){
                    mAlertDialog = new AlertDialog.Builder(mContext)
                            .setMessage("定位出错，无法使用GPS和基站进行定位")
                            .setPositiveButton("确定",null).create();
                }else if (!mAlertDialog.isShowing()){
                    mAlertDialog.show();
                }
                //如果既没有网络也没有GPS，提醒用户无法定位
                ToastUtil.shortToast(mContext,"无法定位");
            }
        }
    }

    @NonNull
    private LocationListener getListener(final LocationListener onLocationChangeListener) {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LogUtil.e("onLocationChanged");
                Location lastKnownLocation = getLastKnownLocation();
                Location accurateLocation = null;
                if (isBetterLocation(location, lastKnownLocation)) {
                    accurateLocation = location;
                } else {
                    accurateLocation = lastKnownLocation;
                }
                if (onLocationChangeListener != null) {
                    onLocationChangeListener.onLocationChanged(accurateLocation);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LogUtil.e(provider + "  onStatusChanged");
                if (onLocationChangeListener != null) {
                    onLocationChangeListener.onStatusChanged(provider, status, extras);
                }
            }

            @Override
            public void onProviderEnabled(String provider) {

                LogUtil.e(provider + "  onProviderEnabled");
                if (!canLocate()){
                    showSetGPSDialogOrNot();
                }
                if (provider.equals(LocationManager.GPS_PROVIDER)){
                    ToastUtil.shortToast(mContext,"gps已经打开");
                }
                if (onLocationChangeListener != null) {
                    onLocationChangeListener.onProviderEnabled(provider);
                }
            }

            @Override
            public void onProviderDisabled(String provider) {

                LogUtil.e(provider + "  onProviderDisabled");
                if (!canLocate()){
                    showSetGPSDialogOrNot();
                }
                if (provider.equals(LocationManager.GPS_PROVIDER)){
                    ToastUtil.shortToast(mContext,"gps已经关闭");
                }
                if (onLocationChangeListener != null) {
                    onLocationChangeListener.onProviderDisabled(provider);
                }
            }
        };
    }


    /**
     * 停止定位
     */
    public void stop() {
        if (mLocationListener != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            this.locationManager.removeUpdates(mLocationListener);
        }

        if (mSubscription != null){
            if (!mSubscription.isUnsubscribed()){
                mSubscription.unsubscribe();
            }
        }
    }


    private void initProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(1);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(1);
        this.provider = this.locationManager.getBestProvider(criteria, true);
    }

    /**
     * 获取最后的位置
     *
     * @return
     */
    public Location getLastKnownLocation() {
        if (this.provider == null) {
            this.initProvider();
        }

        Location lastKnownLocation;
        //如果是网络定位
        if (this.provider == "network") {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            lastKnownLocation = this.locationManager.getLastKnownLocation(this.provider);
        } else {
            lastKnownLocation = this.locationManager.getLastKnownLocation("network");
            if (this.provider != null) {
                //如果有更好的，使用更好的，否则默认返回使用网络定位的结果
                Location location = this.locationManager.getLastKnownLocation(this.provider);
                if (location != null) {
                    lastKnownLocation = location;
                }
            }
        }
        return lastKnownLocation;
    }

    public final boolean isGPSOpen() {
        boolean gps = this.locationManager.isProviderEnabled("gps");
        return gps;
    }


    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /**
     * 谷歌官方提供的判断两个位置哪个是最佳位置的方法
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private boolean canLocate(){
        return this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 系统位置信息服务未打开，打开设置对话框
     */
    private void showSetGPSDialogOrNot() {
        if (mAlertDialog == null){
            mAlertDialog = new AlertDialog.Builder(mContext)
                    .setMessage("请打开GPS定位提高精确度")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            try {
                                mContext.startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                intent.setAction(Settings.ACTION_SETTINGS);
                                try {
                                    mContext.startActivity(intent);
                                } catch (Exception e) {
                                }
                            }
                        }
                    }).create();
        }
       if (!mAlertDialog.isShowing()){
           mAlertDialog.show();
       }

      /*  SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        boolean isShowGPSNote = sharedPreferencesUtil.getBoolean(LocConstant.IF_SHOW_LOC_DIALOG, true);
        if (isShowGPSNote) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View titleView = DialogTitleViewUtil.getTitleView(mContext,
                    R.string.gps_improve);
            View gpsContentView = inflater.inflate(R.layout.loc_dialog_note_gps,
                    null);
            CheckBox notNoteAgain = (CheckBox) gpsContentView
                    .findViewById(R.id.not_note_again);
            notNoteAgain
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
                            sharedPreferencesUtil.setBoolean(LocConstant.IF_SHOW_LOC_DIALOG, false);
                        }
                    });
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog
                    .setCustomTitle(titleView)
                    .setView(gpsContentView)
                    .setPositiveButton(R.string.set,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    try {
                                        mContext.startActivity(intent);
                                    } catch (ActivityNotFoundException ex) {
                                        intent.setAction(Settings.ACTION_SETTINGS);
                                        try {
                                            mContext.startActivity(intent);
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            }).setCancelable(false).create().show();
        }*/
    }
}
