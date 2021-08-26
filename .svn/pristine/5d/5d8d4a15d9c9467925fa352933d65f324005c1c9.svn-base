package com.augurit.agmobile.gzps.common.service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.augurit.agmobile.gzps.common.model.BannerUrl;
import com.augurit.agmobile.gzps.common.model.OUser;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liangsh on 2017/11/10.
 */

public class GzpsService {

    private Context mContext;
    private AMNetwork amNetwork;
    private GzpsApi mGzpsApi;

    private static GzpsService instance = null;

    public GzpsService(Context context){
        mContext = context;
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
    }

    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null){
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(GzpsApi.class);
            this.mGzpsApi = (GzpsApi) this.amNetwork.getServiceApi(GzpsApi.class);
        }
    }

    public Observable<List<OUser>> getTeamMember(){
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mGzpsApi.getTeamMember(user.getLoginName()).map(new Func1<Result2<List<OUser>>, List<OUser>>() {
            @Override
            public List<OUser> call(Result2<List<OUser>> oUserResult2) {
                return oUserResult2.getData();
            }
        });
    }

    public Observable<Result2<Long>> getServerTimestamp(){
        return mGzpsApi.getServerTimestamp();
    }

    public Observable<Result2<String>> AESEncode(String content){
        return mGzpsApi.AESEncode(content);
    }

    public Observable<okhttp3.ResponseBody> columnsReadInfo(String userId, String infoType){
        return mGzpsApi.columnsReadInfo(userId, infoType);
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public Observable<okhttp3.ResponseBody> uploadUserLog(String content){
        String loginName = BaseInfoManager.getLoginName(mContext);
        String appVersion = android.os.Build.BRAND+";"+android.os.Build.MODEL
                +";"+android.os.Build.VERSION.RELEASE+";"+getLocalVersionName(mContext);
        return mGzpsApi.userLog(loginName, System.currentTimeMillis() + "");
    }

    /**
     * 记录用户操作日志
     * @param context
     * @param content
     */
    public static void userLog(Context context, String content){
        if(instance == null){
            instance = new GzpsService(context);
        }
        instance.uploadUserLog(content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<okhttp3.ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(okhttp3.ResponseBody responseBody) {

                    }
                });
    }


    public Observable<Result2<List<BannerUrl>>> banner(){
        return mGzpsApi.banner(null);
    }

}
