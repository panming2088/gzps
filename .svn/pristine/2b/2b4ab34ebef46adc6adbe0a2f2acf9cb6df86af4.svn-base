package com.augurit.agmobile.gzps.common.constant;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.mapengine.layermanage.dao.RemoteLayerRestDao;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.patrolcore.layer.service.PatrolLayerService;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps
 * @createTime 创建时间 ：2017-10-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-10-14
 * @modifyMemo 修改备注：
 */

public class LayerUrlConstant {

    public static final double MIN_QUERY_SCALE = 4915;   //最大可点查级别

    public static final String COMPONENT_LAYER_TAG = "部件图层";    //历史数据图层

    public static final String NEW_COMPONENT_LAYER_TAG = "部件图层_新";   //新增及修正数据的图层



    //图层服务URL根据agweb获取，图层名必须配上面所示的两个名字，否则无法区配到
    //历史数据图层
    public static String mapServerUrl = "";
    //修正和新增部件时操作的图层
    public static String newMapServerUrl = "";
    //数据上报图层
    public static final String UPLOAD_LAYER_NAME = "数据上报";
    public static String uploadServerUrl = "";




    /**
     * 部件名称
     */
    public static final String[] componentNames = new String[]{
            "窨井",
            "雨水口",
            "排水口",
            "拍门",
            "阀门",
            "溢流堰",
            "排水管道",
            "排水沟渠"
    };


    /**
     * 历史数据的部件url
     */
    public static String[] componentUrls = new String[componentNames.length];


    /**
     * 历史数据的部件图层ID
     */
    public static int[] componentIds = new int[componentNames.length];

    /**
     * 新增和修正的部件url
     */
    public static String[] newComponentUrls = new String[componentNames.length];


    /**
     * 新增和修正的部件图层ID
     */
    public static int[] newComponentIds = new int[componentNames.length];


    /**
     * 根据图层名，获取历史数据层URL
     * @param layerName
     * @return
     */
    public static final String getLayerUrlByLayerName(String layerName){
        String layerUrl = "";
        for(int i = 0; i < LayerUrlConstant.componentNames.length; ++i){
            if(LayerUrlConstant.componentNames[i].equals(layerName)){
                layerUrl = LayerUrlConstant.componentUrls[i];
                break;
            }
        }
        return layerUrl;
    }

    /**
     * 根据图层名，获取新增和修正的数据层URL
     * @param layerName
     * @return
     */
    public static final String getNewLayerUrlByLayerName(String layerName){
        String layerUrl = "";
        for(int i = 0; i < LayerUrlConstant.componentNames.length; ++i){
            if(LayerUrlConstant.componentNames[i].equals(layerName)){
                layerUrl = LayerUrlConstant.newComponentUrls[i];
                break;
            }
        }
        return layerUrl;
    }

    /**
     * 根据历史数据图层URL，获取图层名
     * @param layerUrl
     * @return
     */
    public static final String getLayerNameByLayerUrl(String layerUrl){
        String layerName = "";
        for(int i = 0; i < LayerUrlConstant.componentUrls.length; ++i){
            if(LayerUrlConstant.componentUrls[i].equals(layerUrl)){
                layerName =  LayerUrlConstant.componentNames[i];
                break;
            }
        }
        return layerName;
    }

    /**
     * 根据新增和修正的数据图层URL，获取图层名
     * @param layerUrl
     * @return
     */
    public static final String getLayerNameByNewLayerUrl(String layerUrl){
        if(TextUtils.isEmpty(layerUrl)){
            return "";
        }
        String layerName = "";
        for(int i = 0; i < LayerUrlConstant.newComponentUrls.length; ++i){
            if(LayerUrlConstant.newComponentUrls[i].equals(layerUrl)){
                layerName =  LayerUrlConstant.componentNames[i];
                break;
            }
        }
        return layerName;
    }

    /**
     * 判断图层URL的类型，返回新增和修正的图层URL
     * @param layerUrl
     * @return
     */
    public static final String getNewLayerUrlByUnknownLayerUrl(String layerUrl){
        String newLayerUrl = "";
        for(int i = 0; i < LayerUrlConstant.componentUrls.length; ++i){
            if(LayerUrlConstant.componentUrls[i].equals(layerUrl)){
                newLayerUrl =  LayerUrlConstant.newComponentUrls[i];
                break;
            }
        }
        if(newLayerUrl.equals("")){
            newLayerUrl = layerUrl;
        }
        return newLayerUrl;
    }

    /**
     * 判断图层URL的类型，返回图层名
     * @param layerUrl
     * @return
     */
    public static final String getLayerNameByUnknownLayerUrl(String layerUrl){
        String layerName = "";
        for(int i = 0; i < LayerUrlConstant.componentUrls.length; ++i){
            if(LayerUrlConstant.componentUrls[i] != null && LayerUrlConstant.componentUrls[i].equals(layerUrl)){
                layerName =  LayerUrlConstant.componentNames[i];
                break;
            }
        }
        if(layerName.equals("")){
            for(int i = 0; i < LayerUrlConstant.newComponentUrls.length; ++i){
                if(LayerUrlConstant.newComponentUrls[i] != null && LayerUrlConstant.newComponentUrls[i].equals(layerUrl)){
                    layerName =  LayerUrlConstant.componentNames[i];
                    break;
                }
            }
        }
        return layerName;
    }

    public static final int getIndexByLayerUrl(String layerUrl){
        int index = 0;
        for(int i = 0; i < LayerUrlConstant.componentUrls.length; ++i){
            if(LayerUrlConstant.componentUrls[i].equals(layerUrl)){
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 返回图层URL在数组中的位置
     * @param layerUrl
     * @return
     */
    public static final int getIndexByUnknowsLayerUrl(String layerUrl){
        int index = -1;
        for(int i = 0; i < LayerUrlConstant.componentUrls.length; ++i){
            if(LayerUrlConstant.componentUrls[i] != null && LayerUrlConstant.componentUrls[i].equals(layerUrl)){
                index = i;
                break;
            }
        }
        if(index == -1){
            for(int i = 0; i < LayerUrlConstant.newComponentUrls.length; ++i){
                if(LayerUrlConstant.newComponentUrls[i] != null && LayerUrlConstant.newComponentUrls[i].equals(layerUrl)){
                    index = i;
                    break;
                }
            }
        }
        return index;
    }


    /**
     * 返回图层名在数组中的位置
     * @param layerName
     * @return
     */
    public static final int getIndexlByLayerName(String layerName){
        int index = 0;
        for(int i = 0; i < LayerUrlConstant.componentNames.length; ++i){
            if(LayerUrlConstant.componentNames[i].equals(layerName)){
                index = i;
                break;
            }
        }
        return index;
    }

    public static boolean ifLayerUrlInitSuccess(){
        if(StringUtil.isEmpty(LayerUrlConstant.componentUrls[0])
                || StringUtil.isEmpty(LayerUrlConstant.componentUrls[1])
                || StringUtil.isEmpty(LayerUrlConstant.componentUrls[2])){
            return false;
        }
        return true;
    }
    public static boolean ifNewLayerUrlInitSuccess(){
        if(StringUtil.isEmpty(LayerUrlConstant.newComponentUrls[0])
                || StringUtil.isEmpty(LayerUrlConstant.newComponentUrls[1])
                || StringUtil.isEmpty(LayerUrlConstant.newComponentUrls[2])){
            return false;
        }
        return true;
    }



    /**
     * 初始化设施图层URL，默认不初始化新设施图层
     * @param context
     */
    public static void initComponentLayers(final Context context, Callback2<String[]> callback){
        initComponentLayers(context, true, callback);
    }

    /**
     * 初始化设施图层URL
     * @param context
     * @param checkNewLayerUrl 是否初始化“新增、修正图层”的URL
     */
    public static void initComponentLayers(final Context context, final boolean checkNewLayerUrl, final Callback2<String[]> callback) {
        final ILayersService layersService = LayerServiceFactory.provideLayerService(context);
        Observable<List<LayerInfo>> observable = layersService.getSortedLayerInfos();
        if (observable == null) {
            if(callback != null){
                callback.onFail(new Exception("LayersService创建Observable失败"));
            }
            EventBus.getDefault().post(new OnInitLayerUrlFinishEvent(false));
            return;
        }
        observable.map(new Func1<List<LayerInfo>, String[]>() {
            @Override
            public String[] call(List<LayerInfo> layerInfos) {
                String[] result = new String[2];
                if (ListUtil.isEmpty(layerInfos)) {
                    layersService.deleteProjectFolder();
                    result[0] = "false";
                    result[1] = "图层列表为空";
                    return result;
                }
                String componentLayerUrl = "";
                String newComponentLayerUrl = "";
                for (LayerInfo layerInfo : layerInfos) {
                    if (LayerUrlConstant.COMPONENT_LAYER_TAG.equals(layerInfo.getLayerName())) {
                        componentLayerUrl = layerInfo.getUrl();
                    } else if (LayerUrlConstant.NEW_COMPONENT_LAYER_TAG.equals(layerInfo.getLayerName())) {
                        newComponentLayerUrl = layerInfo.getUrl();
                    }
                    if (LayerUrlConstant.UPLOAD_LAYER_NAME.equals(layerInfo.getLayerName())) {
                        LayerUrlConstant.uploadServerUrl = layerInfo.getUrl();
                    }
                }
                if (StringUtil.isEmpty(componentLayerUrl)) {
                    result[0] = "false";
                    result[1] = "图层列表中没有历史设施图层";
                    return result;
                }
                if(checkNewLayerUrl
                        && StringUtil.isEmpty(newComponentLayerUrl)){
                    result[0] = "false";
                    result[1] = "图层列表中没有新增设施图层";
                    return result;
                }
                LayerUrlConstant.mapServerUrl = componentLayerUrl;
                LayerUrlConstant.newMapServerUrl = newComponentLayerUrl;
                initOldLayerUrl(context);
                if(!ifLayerUrlInitSuccess()){
                    //若初始化失败，可能是专题图层信息错误，清除本地缓存
                    layersService.deleteProjectFolder();
                    result[0] = "false";
                    result[1] = "无法初始化历史设施图层";
                    return result;
                }
                if(checkNewLayerUrl){
                    initNewLayerUrl(context);
                    if(ifNewLayerUrlInitSuccess()){
                        //若初始化失败，可能是专题图层信息错误，清除本地缓存
                        layersService.deleteProjectFolder();
                        result[0] = "false";
                        result[1] = "无法初始化新增设施图层";
                        return result;
                    }
                }
                result[0] = "true";
                result[1] = "初始化设施图层成功";
                return result;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(callback != null){
                            callback.onFail(new Exception(e));
                        }
                        EventBus.getDefault().post(new OnInitLayerUrlFinishEvent(false));
                    }

                    @Override
                    public void onNext(String[] result) {
                        if(callback != null){
                            callback.onSuccess(result);
                        }
                        EventBus.getDefault().post(new OnInitLayerUrlFinishEvent(Boolean.valueOf(result[0])));
                    }
                });

    }

    private static void initOldLayerUrl(Context context) {
        LayerInfo layerInfo = new LayerInfo();
        layerInfo.setType(LayerType.DynamicLayer);
        layerInfo.setUrl(LayerUrlConstant.mapServerUrl);
        layerInfo.setLayerId(123456789);
        LayerInfo layerInfoWithChild = new RemoteLayerRestDao().completeChildAMLayerInfo(context, layerInfo);
        if (layerInfoWithChild == null
                || ListUtil.isEmpty(layerInfoWithChild.getChildLayer())) {
            return;
        }
        for (LayerInfo child : layerInfoWithChild.getChildLayer()) {
            for (int i = 0; i < LayerUrlConstant.componentNames.length; ++i) {
                if (LayerUrlConstant.componentNames[i].equals(child.getLayerName())) {
                    LayerUrlConstant.componentUrls[i] = LayerUrlConstant.mapServerUrl + "/" + child.getLayerId();
                    LayerUrlConstant.componentIds[i] = child.getLayerId();
                    //                            LayerUrlConstant.componentUrls[i] = LayerUrlConstant.componentUrls[i].replace("MapServer", "FeatureServer");
                    break;
                }
            }
        }
    }

    private static void initNewLayerUrl(Context context) {
        LayerInfo layerInfo = new LayerInfo();
        layerInfo.setType(LayerType.DynamicLayer);
        layerInfo.setUrl(LayerUrlConstant.newMapServerUrl);
        layerInfo.setLayerId(123123);
        LayerInfo layerInfoWithChild = new RemoteLayerRestDao().completeChildAMLayerInfo(context, layerInfo);
        if (layerInfoWithChild == null
                || ListUtil.isEmpty(layerInfoWithChild.getChildLayer())) {
            return;
        }
        for (LayerInfo child : layerInfoWithChild.getChildLayer()) {
            for (int i = 0; i < LayerUrlConstant.componentNames.length; ++i) {
                if (LayerUrlConstant.componentNames[i].equals(child.getLayerName())) {
                    LayerUrlConstant.newComponentUrls[i] = LayerUrlConstant.newMapServerUrl + "/" + child.getLayerId();
                    LayerUrlConstant.newComponentUrls[i] = LayerUrlConstant.newComponentUrls[i].replace("MapServer", "FeatureServer");
                    LayerUrlConstant.newComponentIds[i] = child.getLayerId();
                    break;
                }
            }
        }
    }
}
