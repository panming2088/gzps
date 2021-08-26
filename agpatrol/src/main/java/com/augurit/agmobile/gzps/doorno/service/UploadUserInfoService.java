package com.augurit.agmobile.gzps.doorno.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.model.ResponseBody;

import com.augurit.agmobile.gzps.common.util.FileBean;
import com.augurit.agmobile.gzps.common.util.PortSelectUtil;
import com.augurit.agmobile.gzps.doorno.model.SewerageInfoBean;
import com.augurit.agmobile.gzps.uploadevent.util.PhotoUploadType;

import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.componentmaintenance
 * @createTime 创建时间 ：17/10/14
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/14
 * @modifyMemo 修改备注：排水户信息接口
 */

public class UploadUserInfoService {

    private Context mContext;
    public static final int TIMEOUT = 100;  //网络超时时间（秒）
    private AMNetwork amNetwork;
    private MyRetroService mMyRetroService;
    private MyRetroService mMyRetroService1;

    public UploadUserInfoService(Context mContext) {
        this.mContext = mContext;
    }


    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(MyRetroService.class);
            this.mMyRetroService = (MyRetroService) this.amNetwork.getServiceApi(MyRetroService.class);
        }
    }

    private HashMap<String, RequestBody> getParams(List<Photo> photos, String tag) {
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        int i = 0;
        if (!ListUtil.isEmpty(photos)) {
            for (Photo photo : photos) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    String name = photo.getPhotoName();
//                    if(name.indexOf("_xc_")!=-1){
//                        name = name.replace("_xc_","");
//                    }
                    requestMap.put("file" + i + "\"; filename=\"" + name.replaceFirst("_", tag), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }
        }
        return requestMap;
    }


    //pdf文件的上传
    private HashMap<String, RequestBody> getFileParams(SewerageInfoBean bean, String tag) {
        List<FileBean> fileBeans = bean.getFiles();
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        int i = 0;
        if (!ListUtil.isEmpty(fileBeans)) {
            for (FileBean fileBean : fileBeans) {
                if(fileBean.getId()>0 && !bean.isDraft()){
                    continue;
                }
                if (fileBean.getPath() != null) {
                    File file = new File(fileBean.getPath());
                    String name = fileBean.getName();
                    if(name.indexOf(PhotoUploadType.UPLOAD_FOR_FILES)!=-1){
                        name =  name.replace(PhotoUploadType.UPLOAD_FOR_FILES,"");
                    }
                    requestMap.put("file" + i + "\"; filename=\"" + name.replaceFirst("_", tag), RequestBody.create(MediaType.parse("pdf/*"), file));
                    i++;
                }
            }
        }
        return requestMap;
    }

    public Observable<ResponseBody> uploadUserInfo(SewerageInfoBean bean) {

//        String port = LoginConstant.pshUploadPort;//默认的端口
//        TableDBService dbService = new TableDBService(mContext);
//        List<DictionaryItem> dictionaryItems = dbService.getDictionaryByTypecodeInDB("A181");//数据字典端口
//        if (!ListUtil.isEmpty(dictionaryItems)) {
//            List<String> ports = new ArrayList<>();
//            for (int i = 0; i < dictionaryItems.size(); i++) {
//                DictionaryItem dictionaryItem = dictionaryItems.get(i);
//                if (!StringUtil.isEmpty(dictionaryItem.getName())
//                        && ValidateUtils.isInteger(dictionaryItem.getName())) {
//                    ports.add(":" + dictionaryItem.getName());
//                }
//            }
//            if (!ListUtil.isEmpty(ports)) {
//                port = ports.get(new Random().nextInt(ports.size()));
//            }
//        }
//        String supportUrl = LoginConstant.UPLOAD_AGSUPPORT + port + LoginConstant.UPLOAD_POSTFIX + "/";//TODO

        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
        initAMNetworkWithRandomURL(supportUrl);
        List<SewerageInfoBean.WellBeen> wellBeen = bean.getWellBeen();
        if(!ListUtil.isEmpty(wellBeen)){
            for(SewerageInfoBean.WellBeen wellBeen1:wellBeen){
                if(wellBeen1.getId() !=null && wellBeen1.getId()<1){
                    wellBeen1.setId(null);
                }
            }
            bean.setWellBeen(wellBeen);
        }
        return upload(bean);
    }


    /**
     * 提交排水户信息
     * @param bean
     * @return
     */
    public Observable<ResponseBody> upload(SewerageInfoBean bean) {
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        bean.setLoginName(loginName);
//        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
//        initAMNetwork(supportUrl);
        String str =  JsonUtil.getJson(bean);

        HashMap<String, RequestBody> requestMap = new HashMap<>();

        HashMap<String, RequestBody> requestMap1 = getParams(bean.getPhotos0(),"_sewerageUser_");
        HashMap<String, RequestBody> requestMap2 = getParams(bean.getPhotos1(),"_hasCert1_");
        HashMap<String, RequestBody> requestMap3 = getParams(bean.getPhotos2(),"_hasCert2_");
        HashMap<String, RequestBody> requestMap4 = getParams(bean.getPhotos3(),"_hasCert3_");
        HashMap<String, RequestBody> requestMap5 = getParams(bean.getPhotos4(),"_hasCert4_");
        HashMap<String, RequestBody> requestMap11 = getParams(bean.getPhotos5(),"_hasCert5_");
        HashMap<String, RequestBody> requestMap13 = getFileParams(bean,"_hasCert6_");
        HashMap<String, RequestBody> requestMap14 = getParams(bean.getPhotos7(),"_hasCert7_");

        HashMap<String, RequestBody> requestMap6 = getParams(bean.getThumbnailPhotos0(),"_sewerageUserthumbnail_");
        HashMap<String, RequestBody> requestMap7 = getParams(bean.getThumbnailPhotos1(),"_hasCert1thumbnail_");
        HashMap<String, RequestBody> requestMap8 = getParams(bean.getThumbnailPhotos2(),"_hasCert2thumbnail_");
        HashMap<String, RequestBody> requestMap9 = getParams(bean.getThumbnailPhotos3(),"_hasCert3thumbnail_");
        HashMap<String, RequestBody> requestMap10 = getParams(bean.getThumbnailPhotos4(),"_hasCert4thumbnail_");
        HashMap<String, RequestBody> requestMap12 = getParams(bean.getThumbnailPhotos5(),"_hasCert5thumbnail_");
        HashMap<String, RequestBody> requestMap15 = getParams(bean.getThumbnailPhotos7(),"_hasCert7thumbnail_");


        requestMap.putAll(requestMap1);
        requestMap.putAll(requestMap2);
        requestMap.putAll(requestMap3);
        requestMap.putAll(requestMap4);
        requestMap.putAll(requestMap5);
        requestMap.putAll(requestMap6);
        requestMap.putAll(requestMap7);
        requestMap.putAll(requestMap8);
        requestMap.putAll(requestMap9);
        requestMap.putAll(requestMap10);
        requestMap.putAll(requestMap11);
        requestMap.putAll(requestMap12);
        requestMap.putAll(requestMap13);
        requestMap.putAll(requestMap14);
        requestMap.putAll(requestMap15);

        return mMyRetroService1.uploadUserInfo(
                str,
                requestMap);

    }

    private void initAMNetworkWithRandomURL(String serverUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        if (BuildConfig.DEBUG) {
//            // Log信息拦截器
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
//            //设置 Debug Log 模式
//            builder.addInterceptor(loggingInterceptor);
//        }

        OkHttpClient client = builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS).
                readTimeout(TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(TIMEOUT, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                //使用自定义的mGsonConverterFactory
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(serverUrl)
                .build();
        mMyRetroService1 = retrofit.create(MyRetroService.class);

    }


    /**
     * 编辑排水户信息
     * @param bean
     * @return
     */
    public Observable<ResponseBody> toUpdateCollect(SewerageInfoBean bean) {
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        bean.setLoginName(loginName);
//        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
        initAMNetwork(supportUrl);
        String str = JsonUtil.getJson(bean);
        if (ListUtil.isEmpty(bean.getPhotos0()) && ListUtil.isEmpty(bean.getPhotos1()) && ListUtil.isEmpty(bean.getPhotos2())
                && ListUtil.isEmpty(bean.getPhotos3()) && ListUtil.isEmpty(bean.getPhotos4())&& ListUtil.isEmpty(bean.getPhotos5()) && ListUtil.isEmpty(bean.getThumbnailPhotos0())
                && ListUtil.isEmpty(bean.getThumbnailPhotos1()) && ListUtil.isEmpty(bean.getThumbnailPhotos2()) && ListUtil.isEmpty(bean.getThumbnailPhotos3())
                && ListUtil.isEmpty(bean.getThumbnailPhotos4())&& ListUtil.isEmpty(bean.getThumbnailPhotos5())&& ListUtil.isEmpty(bean.getFiles())
                && ListUtil.isEmpty(bean.getPhotos7()) && ListUtil.isEmpty(bean.getThumbnailPhotos7()) ) {
          if(bean.isIndustry()){
              return  mMyRetroService.toUpdateCollectGY(str);
          }else{
              return  mMyRetroService.toUpdateCollect(str);
          }
        }else{

            HashMap<String, RequestBody> requestMap = new HashMap<>();

            HashMap<String, RequestBody> requestMap1 = getParams(bean.getPhotos0(), "_sewerageUser_");
            HashMap<String, RequestBody> requestMap2 = getParams(bean.getPhotos1(), "_hasCert1_");
            HashMap<String, RequestBody> requestMap3 = getParams(bean.getPhotos2(), "_hasCert2_");
            HashMap<String, RequestBody> requestMap4 = getParams(bean.getPhotos3(), "_hasCert3_");
            HashMap<String, RequestBody> requestMap5 = getParams(bean.getPhotos4(), "_hasCert4_");
            HashMap<String, RequestBody> requestMap11 = getParams(bean.getPhotos5(), "_hasCert5_");
            HashMap<String, RequestBody> requestMap14 = getParams(bean.getPhotos7(), "_hasCert7_");
            HashMap<String, RequestBody> requestMap13 = getFileParams(bean, "_hasCert6_");
            HashMap<String, RequestBody> requestMap6 = getParams(bean.getThumbnailPhotos0(), "_sewerageUserthumbnail_");
            HashMap<String, RequestBody> requestMap7 = getParams(bean.getThumbnailPhotos1(), "_hasCert1thumbnail_");
            HashMap<String, RequestBody> requestMap8 = getParams(bean.getThumbnailPhotos2(), "_hasCert2thumbnail_");
            HashMap<String, RequestBody> requestMap9 = getParams(bean.getThumbnailPhotos3(), "_hasCert3thumbnail_");
            HashMap<String, RequestBody> requestMap10 = getParams(bean.getThumbnailPhotos4(), "_hasCert4thumbnail_");
            HashMap<String, RequestBody> requestMap12 = getParams(bean.getThumbnailPhotos5(), "_hasCert5thumbnail_");
            HashMap<String, RequestBody> requestMap15 = getParams(bean.getThumbnailPhotos7(), "_hasCert7thumbnail_");

            requestMap.putAll(requestMap1);
            requestMap.putAll(requestMap2);
            requestMap.putAll(requestMap3);
            requestMap.putAll(requestMap4);
            requestMap.putAll(requestMap5);
            requestMap.putAll(requestMap6);
            requestMap.putAll(requestMap7);
            requestMap.putAll(requestMap8);
            requestMap.putAll(requestMap9);
            requestMap.putAll(requestMap10);
            requestMap.putAll(requestMap11);
            requestMap.putAll(requestMap12);
            requestMap.putAll(requestMap13);
            requestMap.putAll(requestMap14);
            requestMap.putAll(requestMap15);

            if(bean.isIndustry()){
                return  mMyRetroService.toUpdateCollectGY(str,requestMap);
            }else{
                return mMyRetroService.toUpdateCollect(str,requestMap);
            }
        }
    }

    /**
     * 删除排水户信息
     * @param id
     * @return
     */
    public Observable<ResponseBody> toDeleteCollect(String id) {
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
//        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
        initAMNetwork(supportUrl);

        return mMyRetroService.toDeleteCollect(
                id, loginName);
    }

    /**
     * 注销排水户
     * @param id
     * @return
     */
    public Observable<ResponseBody> toCancelCollect(String id) {
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
//        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
        initAMNetwork(supportUrl);

        return mMyRetroService.toCancelCollect(
                id, loginName);
    }
}
