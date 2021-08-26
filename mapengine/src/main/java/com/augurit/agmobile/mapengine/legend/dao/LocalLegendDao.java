package com.augurit.agmobile.mapengine.legend.dao;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.EncryptFileCacheHelper;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.IFileCacheHelper;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.legend.model.Legend;
import com.augurit.am.fw.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.legend.dao
 * @createTime 创建时间 ：17/7/19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/19
 * @modifyMemo 修改备注：
 */

public class LocalLegendDao {

    protected Context mContext;

    public LocalLegendDao(Context context) {
        this.mContext = context;
    }

    /**
     * 从本地获取图例
     *
     * @return
     */
    public Observable<List<Legend>> getAllLegends() {

        FilePathUtil filePathUtil = new FilePathUtil(mContext);
        String legendSavedPath = filePathUtil.getLegendSavedPath();
        IFileCacheHelper fileHelper2 = new EncryptFileCacheHelper();
        final byte[] offlineCacheFile = fileHelper2.getOfflineCacheFile(legendSavedPath);

        if (offlineCacheFile == null) {
            return null;

        } else {

            return Observable.fromCallable(new Callable<List<Legend>>() {
                @Override
                public List<Legend> call() throws Exception {
                    String json = new String(offlineCacheFile);
                    return JsonUtil.getObject(json, new TypeToken<List<Legend>>() {
                    }.getType());
                }
            }).subscribeOn(Schedulers.io());
        }

    }

    /**
     * 保存图例到本地
     *
     * @param legends 要保存的图例
     */
    public void saveToLocal(List<Legend> legends) {
        FilePathUtil filePathUtil = new FilePathUtil(mContext);
        String legendSavedPath = filePathUtil.getLegendSavedPath();
        byte[] bytes = JsonUtil.getJson(legends).getBytes();
        IFileCacheHelper fileHelper2 = new EncryptFileCacheHelper();
        fileHelper2.addOfflineCacheFile(bytes, legendSavedPath);
    }
}
