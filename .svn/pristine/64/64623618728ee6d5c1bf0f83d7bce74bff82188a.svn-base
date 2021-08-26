package com.augurit.agmobile.gzps;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.augurit.agmobile.gzps.common.constant.PersonalizedConfiguration;
import com.augurit.agmobile.gzps.common.util.FontsOverrideUtil;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.util.LayerFactoryProvider;
import com.augurit.agmobile.mapengine.project.service.AgwebProjectService;
import com.augurit.agmobile.mapengine.project.util.ProjectServiceFactory;
import com.augurit.agmobile.patrolcore.layer.service.AgwebPatrolLayerService;
import com.augurit.agmobile.patrolcore.layer.util.PatrolLayerFactory;
import com.augurit.agmobile.patrolcore.search.service.PatrolSearchServiceImpl2;
import com.augurit.agmobile.patrolcore.search.util.PatrolSearchServiceProvider;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.log.AMLogReport;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.runtime.ArcGISRuntime;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 *
 * Created by long on 2017/10/28.
 */
public class InitializeService extends IntentService {

    private static final String ACTION_INIT_WHEN_APP_CREATE = "com.anly.githubapp.service.action.INIT";

    //todo xcl 2017-02-17 位置还需要斟酌
    private static final String CLIENT_ID = "1eFHW78avlnRUPHm";// 地图许可，用于去掉arcgis水印

    public InitializeService() {
        super("com.augurit.agmobile.gzps.InitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit();
            }
        }
    }

    private void performInit() {

        Realm.init(this);
        //开发阶段需要数据库迁移的时候直接就删了数据重新开始
        //后续发布版本将改成DBMigration进行数据库的版本更新
        RealmConfiguration configuration= new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration);

        //   LogUtil.d("Realm:"+Realm.DEFAULT_REALM_NAME);
        Realm realm = Realm.getDefaultInstance();
        String path = realm.getConfiguration().getPath();
        LogUtil.d("Realm1111111111:" + path);

        PersonalizedConfiguration.init();
        LayerFactoryProvider.injectLayerFactory(new PatrolLayerFactory());
        //CoordinateManager.getInstance().setIReverseTransFormCoordinate(new XiamenReverseLocationTransform()); //设置厦门坐标转84坐标的实现类
        //动态修改Service
        LayerServiceFactory.injectLayerService(new AgwebPatrolLayerService(this));
        // LayerServiceFactory.injectLayerService(new TestLayerService(this));
        ProjectServiceFactory.injectProjectService(new AgwebProjectService());
        PatrolSearchServiceProvider.injectSearchService(new PatrolSearchServiceImpl2(this));

        //   initCrashReport();

        //图片存储因为历史原因还是用老数据库
        AMDatabase.init(this);

        initCrashReport();

        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
        FontsOverrideUtil.init(this);

        MultiDex.install(this);
        //去掉水印
        ArcGISRuntime.setClientId(CLIENT_ID);
    }

    private void initCrashReport(){
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
}