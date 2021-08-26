package com.augurit.am.cmpt.loc;


import com.augurit.am.cmpt.loc.cnst.Default;
import com.augurit.am.cmpt.loc.cnst.ProviderType;
import com.augurit.am.cmpt.loc.cnst.ZoomLevel;

/**
 * 定位配置
 */
public class LocationConfiguration {

    private boolean shouldeKeepTracking = false; //是否应该一直跟踪位置
    private boolean askForEnableGPS = true; //当用户没有打开gps定位的时候，是否进行请求打开gps
    private boolean shouldFilterSameLocaiton = true; //过滤掉相同的坐标
    private boolean shouldCenterToLocationPoint = true; //定位后是否居中到定位点
    private boolean gpsOnly = false ;//是否只进行gps定位

    private int zoomLevel ;            //获取到坐标后默认缩放到哪个级别

    private String rationalMessage = "";    //进行提示用户打开gps定位的信息
    private String gpsMessage = "";


    private String[] requiredPermissions = Default.LOCATION_PERMISSIONS; //定位所需要的权限，默认是：ACCESS_FINE_LOCATION和ACCESS_COARSE_LOCATION

    private float minDistance = Default.MIN_ACCURACY; //最小的定位精度，当大于这个精度时，才返回该位置，默认经度是0
    private long minTime = Default.WAIT_PERIOD;  //间隔多久进行请求定位，默认是5s后重新请求定位



    public LocationConfiguration shouldKeepTracking(boolean shouldeKeepTracking) {
        this.shouldeKeepTracking = shouldeKeepTracking;
        return this;
    }
    /**
     * 是否只进行gps定位
     * @param gpsOnly
     * @return
     */
    public LocationConfiguration ifGpsOnly(boolean gpsOnly) {
        this.gpsOnly = gpsOnly;
        return this;
    }

    public LocationConfiguration centerToLocationPoint(boolean shouldCenterToLocationPoint) {
        this.shouldCenterToLocationPoint = shouldCenterToLocationPoint;
        return this;
    }

    public LocationConfiguration shouldFilterSameLocation(boolean shouldFilterSameLocaiton) {
        this.shouldFilterSameLocaiton = shouldFilterSameLocaiton;
        return this;
    }

    /**
     * While trying to requestLocation location from GPS Provider,
     * manager will check whether it is available or not.
     * Then if this flag is on it will ask user to turn it on,
     * if not it will switch directly to Network Provider.
     * <p>
     * Default is True.
     */
    public LocationConfiguration askForEnableGPS(boolean askFor) {
        this.askForEnableGPS = askFor;
        return this;
    }

    public LocationConfiguration setZoomLevelAfterGetLocation(@ZoomLevel.Level int zoomLevelAfterGetLocation) {
        this.zoomLevel = zoomLevelAfterGetLocation;
        return this;
    }


    /**
     * If you need to ask any other permissions beside {@linkplain Default#LOCATION_PERMISSIONS}
     * or you may not need both of those permissions, you can change permissions
     * by calling this method with new permissions' array.
     */
    public LocationConfiguration setPermissions(String[] permissions) {
        this.requiredPermissions = permissions;
        return this;
    }

    /**
     * Indicates what to display when user needs to see a rational dialog for RuntimePermission.
     * There is no default value, so if you do not set this it will create an empty dialog.
     */
    public LocationConfiguration setRationalMessage(String message) {
        this.rationalMessage = message;
        return this;
    }

    /**
     * Indicates what to display to user while asking to turn GPS on.
     * There is no default value, so if you do not set this it will create an empty dialog.
     */
    public LocationConfiguration setGPSMessage(String message) {
        this.gpsMessage = message;
        return this;
    }


    /**
     * Minimum Accuracy that you seek location to be
     * <p>
     * Default is {@linkplain Default#MIN_ACCURACY}
     */
    public LocationConfiguration setMinAccuracy(float minAccuracy) {
        this.minDistance = minAccuracy;
        return this;
    }

    public LocationConfiguration setMinDistance(float minDistance){
        this.minDistance = minDistance;
        return this;
    }

    public LocationConfiguration setMinTime(long minTime){
        this.minTime = minTime;
        return this;
    }


    public boolean shouldFilterSameLocaiton() {
        return shouldFilterSameLocaiton;
    }

    public boolean shouldAskForEnableGPS() {
        return askForEnableGPS;
    }

    public String[] getRequiredPermissions() {
        return requiredPermissions;
    }

    public String getRationalMessage() {
        return rationalMessage;
    }

    public String getGPSMessage() {
        return gpsMessage;
    }

    public float getMinDistance() {
        return minDistance;
    }



    public long getGPSWaitPeriod() {
        return minTime;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }



    public boolean isAskForEnableGPS() {
        return askForEnableGPS;
    }

    public boolean isShouldFilterSameLocaiton() {
        return shouldFilterSameLocaiton;
    }

    public boolean isShouldCenterToLocationPoint() {
        return shouldCenterToLocationPoint;
    }

    public boolean isGpsOnly() {
        return gpsOnly;
    }

    public String getGpsMessage() {
        return gpsMessage;
    }

    public long getMinTime() {
        return minTime;
    }


    public boolean isShouldeKeepTracking() {
        return shouldeKeepTracking;
    }
}