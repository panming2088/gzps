package com.augurit.agmobile.patrolcore.common.file.service;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.file.dao.RemoteFileRestDao;
import com.augurit.agmobile.patrolcore.common.file.model.FileResponse;
import com.augurit.agmobile.patrolcore.common.file.model.FileResult;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.ListUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by long on 2017/10/30.
 */

public class FileService {

    private RemoteFileRestDao mRemoteFileRestDao;

    public FileService(Context context){
        mRemoteFileRestDao = new RemoteFileRestDao(context);
    }

    public Observable<String> upload(final String layerName, final String objectid, final List<FileResult> fileList){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (ListUtil.isEmpty(fileList)) {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }
                HashMap<String, RequestBody> requestMap = new HashMap<>();
                int i = 0;
                for (FileResult fileResult : fileList) {
                    if (fileResult.getState() == 0) { //如果之前没上传过，才要上传该图片
                        File file = new File(fileResult.getLocalPath());
                        requestMap.put("file" + i + "\"; filename=\"" + file.getName(), RequestBody.create(MediaType.parse(fileResult.getType()), file));
                        i++;
                    }
                }
                String result = mRemoteFileRestDao.upload(layerName, objectid, requestMap);
                if(!result.contains("200")){
                    subscriber.onError(new Exception("上传图片失败！"));
                } else {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                }
            }
        })
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<FileResult>> getList(final String layerName, final String objectid){
         return Observable.create(new Observable.OnSubscribe<List<FileResult>>() {
             @Override
             public void call(Subscriber<? super List<FileResult>> subscriber) {
                 FileResponse fileResponse = mRemoteFileRestDao.getList(layerName, objectid);
                 if(!fileResponse.getStatus().equals("200")){
                     subscriber.onError(new Exception(fileResponse.getMessage()));
                 } else {
                     subscriber.onNext(fileResponse.getRows());
                     subscriber.onCompleted();
                 }
             }
         })
                 .subscribeOn(Schedulers.io());
    }

    public Observable<String> delete(final String layerName, final String objectid, final String fileName){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String result = mRemoteFileRestDao.delete(layerName, objectid, fileName);
                if(!result.equals("200")){
                    subscriber.onError(new Exception(""));
                } else {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }
            }
        })
                .subscribeOn(Schedulers.io());
    }
}
