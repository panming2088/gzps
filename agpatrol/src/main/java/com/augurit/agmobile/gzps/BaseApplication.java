package com.augurit.agmobile.gzps;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.multidex.MultiDex;

import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.common.constant.PersonalizedConfiguration;
import com.augurit.agmobile.gzps.common.util.FontsOverrideUtil;
import com.augurit.agmobile.gzps.im.bean.FriendItem;
import com.augurit.agmobile.gzps.im.bean.GroupInfoBean;
import com.augurit.agmobile.gzps.im.bean.UserInfoBean;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.log.AMLogReport;
import com.augurit.am.fw.utils.JsonUtil;
import com.esri.android.runtime.ArcGISRuntime;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.rong.calllib.RongCallClient;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol
 * @createTime 创建时间 ：2017-03-07
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-07
 * @modifyMemo 修改备注：
 */

//Tag 测试代码合拼 后续删掉  by gkh


public class BaseApplication extends Application implements RongIM.UserInfoProvider, RongIM.GroupInfoProvider,
        RongIM.GroupUserInfoProvider, RongIM.OnReceiveUnreadCountChangedListener {
    // private static DisplayImageOptions options;

    //todo xcl 2017-02-17 位置还需要斟酌
    private static final String CLIENT_ID = "1eFHW78avlnRUPHm";// 地图许可，用于去掉arcgis水印
    private static final String naviServer = "http://139.159.227.100:8082";
    private static final String fileServer = "http://139.159.227.100:8086";
    private static final String cmpUrl = "139.159.243.217:8000";
    private static final String snifferUrl = "139.159.243.217:8000";

    public static Application application;

    @Override
    public Resources getResources() {
        //获取到resources对象
        Resources res = super.getResources();
        //修改configuration的fontScale属性
        res.getConfiguration().fontScale = 1;
        //将修改后的值更新到metrics.scaledDensity属性上
        res.updateConfiguration(null, null);
        return res;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap .
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/

        RongIM.setServerInfo(naviServer, fileServer);
        RongCallClient.setEngineServerInfo(cmpUrl, snifferUrl);
        RongIM.init(this);
        application = this;
        //PermissionCheckSDK.init(this);
        //RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());

        //开发阶段此处屏蔽,避免在开发中上传日志,打包时要打开
//        CrashReport.initCrashReport(getApplicationContext(), "6ee75c2788", false);


        Realm.init(this);
        //开发阶段需要数据库迁移的时候直接就删了数据重新开始
        //后续发布版本将改成DBMigration进行数据库的版本更新
        RealmConfiguration configuration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration);

        //   LogUtil.d("Realm:"+Realm.DEFAULT_REALM_NAME);
        //        Realm realm = Realm.getDefaultInstance();
        //        String path = realm.getConfiguration().getPath();
        //        LogUtil.d("Realm1111111111:" + path);
        RongIM.setUserInfoProvider(this, true);
        RongIM.setGroupInfoProvider(this, true);

        PersonalizedConfiguration.init();
        //由于退出登录会把下面几个Factory的内部静态变量置null，所以下面几句移动LoginActivity初始化
        /*LayerFactoryProvider.injectLayerFactory(new PatrolLayerFactory());
        //动态修改Service
        LayerServiceFactory.injectLayerService(new AgwebPatrolLayerService(this));
        ProjectServiceFactory.injectProjectService(new AgwebProjectService());
        PatrolSearchServiceProvider.injectSearchService(new PatrolSearchServiceImpl2(this));*/

        //图片存储因为历史原因还是用老数据库
        AMDatabase.init(this);

        //        initCrashReport();

        FontsOverrideUtil.init(this);

        //        options = new DisplayImageOptions.Builder()
        //                .showImageForEmptyUri(R.mipmap.default_useravatar)
        //                .showImageOnFail(R.mipmap.default_useravatar)
        //                .showImageOnLoading(R.mipmap.default_useravatar)
        //                .displayer(new FadeInBitmapDisplayer(300))
        //                .cacheInMemory(true)
        //                .cacheOnDisk(true)
        //                .build();
    }

    //    public static DisplayImageOptions getOptions() {
    //        return options;
    //    }

    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;

    }

    private void initCrashReport() {
        AMLogReport.getInstance()
                .setDebug(false)
                .setCacheSize(50 * 1024 * 1024)//支持设置缓存大小，超出后清空，这里设置为50M，默认为30M
                .setLogDir(getApplicationContext(),
                        new FilePathUtil(this).getSavePath() + "/")//定义路径为：sdcard/{应用名}/AMLog/
                // .setEncryption(new AESEncode()) //支持日志到AES加密或者DES加密，默认不开启
                .init(getApplicationContext());
        //        initEmailReporter();
        //        initHttpReporter();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
        //去掉水印
        ArcGISRuntime.setClientId(CLIENT_ID);

    }

    @Override
    public UserInfo getUserInfo(String userId) {
        String url = "http://" + LoginConstant.SERVER_URL + LoginConstant.USER_BY_ID_URL + userId;
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String string = response.body().string();
                JSONObject object = new JSONObject(string);
                Object data = object.get("result");
                if (data == null) {
                    return null;
                }
                UserInfoBean user = JsonUtil.getObject(string, UserInfoBean.class);
                FriendItem result = user.getResult();
                String portraitUri = "http://139.159.243.230:8080/img/portrait/rc_default_portrait" +
                        ".png";
                String showName = result.getUserName() + "(" + result.getOrgName() + result.getTitle() + ")";
                return new UserInfo(result.getId(), showName, Uri.parse
                        (portraitUri));

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Group getGroupInfo(String s) {
        String url = "http://" + LoginConstant.SERVER_URL + "/rest/system/getGroupById/" + s;
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String string = response.body().string();
                JSONObject object = new JSONObject(string);
                Object data = object.get("result");
                if (data == null) {
                    return null;
                }
                GroupInfoBean groupInfo = JsonUtil.getObject(string, GroupInfoBean.class);
                GroupInfoBean.GroupBean result = groupInfo.getResult();
                Group group = new Group(result.getGroupId(), result.getGroupName(), null);
                return group;

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public GroupUserInfo getGroupUserInfo(String s, String s1) {
        return null;
    }

    @Override
    public void onMessageIncreased(int i) {

    }

    /*
    private void initCrashReport(){
        AMLogReport.getInstance()
                .setDebug(false)
                .setCacheSize(50 * 1024 * 1024)//支持设置缓存大小，超出后清空，这里设置为50M，默认为30M
                .setLogDir(getApplicationContext(),
                        new FilePathUtil(this).getSavePath() + "/")//定义路径为：sdcard/{应用名}/AMLog/
                // .setEncryption(new AESEncode()) //支持日志到AES加密或者DES加密，默认不开启
                .init(getApplicationContext());
//        initEmailReporter();
        initHttpReporter();
        AMLogReport.getInstance().uploadAll(this);
    }

    /**
     * 设置上传日志到服务器
     */
    /*
    public void initHttpReporter(){
        AMHttpReporter http = new AMHttpReporter(this);
        http.setUrl("http://192.168.31.7:8080/chapter1/upload");//发送请求的地址
        http.setFileParam("fileName");//文件的参数名
        http.setToParam("to");//收件人参数名
        http.setTo("你的接收邮箱");//收件人
        http.setTitleParam("subject");//标题
        http.setBodyParam("message");//内容
        AMLogReport.getInstance().addUploader(http);
    }
    */

}
