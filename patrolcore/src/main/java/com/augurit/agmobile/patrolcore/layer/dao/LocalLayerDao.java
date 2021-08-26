package com.augurit.agmobile.patrolcore.layer.dao;

import android.content.Context;
import android.support.annotation.Nullable;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.EncryptFileCacheHelper;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.IFileCacheHelper;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.patrolcore.layer.model.PatrolLayerInfo2;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.layer.dao
 * @createTime 创建时间 ：17/7/31
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/31
 * @modifyMemo 修改备注：
 */

public class LocalLayerDao {

    protected Context mContext;

    public LocalLayerDao(Context context) {

        this.mContext = context;
    }


    public Observable<List<LayerInfo>> getLayerInfos(String userId) {
        String projectFileSavePath = getProjectConfigFileStoragePath(mContext, userId);
        IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
        final byte[] bytes = amFileHelper2.getOfflineCacheFile(projectFileSavePath);
        if (ValidateUtil.isObjectNull(bytes)) {
            return null;
        }
        //LogUtil.d("从" + projectFileSavePath + "中读取图层数据，图层数据不为空");
        return Observable.fromCallable(new Callable<List<LayerInfo>>() {
            @Override
            public List<LayerInfo> call() throws Exception {
                String result = null;
                result = new String(bytes);
                JsonReader reader = new JsonReader(new StringReader(result));
                reader.setLenient(true);
                return JsonUtil.getObject(reader, new TypeToken<List<PatrolLayerInfo2>>() {
                }.getType());
            }
        });
    }


    @Nullable
    private String getProjectConfigFileStoragePath(Context context, String userId) {
        FilePathUtil manager = new FilePathUtil(context);
        String topPath = manager.getProjectFileSavePath();
        String projectFileSavePath = null;
        if (!ValidateUtil.isObjectNull(topPath)) {
            projectFileSavePath = topPath + "/layer-" + userId + ".txt";
        }
        return projectFileSavePath;
    }


    public void saveLayerInfos(String userId, List<LayerInfo> layerInfos) {

        String projectFileSavePath = getProjectConfigFileStoragePath(mContext, userId);

        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        String toJson = gson.toJson(layerInfos);
        IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
        amFileHelper2.addOfflineCacheFile(toJson.getBytes(), projectFileSavePath);
    }
}
