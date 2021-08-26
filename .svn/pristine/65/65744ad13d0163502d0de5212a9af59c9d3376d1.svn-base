package com.augurit.agmobile.mapengine.marker.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.utils.AttachmentsUtils;
import com.augurit.agmobile.mapengine.marker.dao.IGetMarkStyleDao;
import com.augurit.agmobile.mapengine.marker.dao.LocalDatabaseMarkDao;
import com.augurit.agmobile.mapengine.marker.dao.LocalGetMarkStyleDao;
import com.augurit.agmobile.mapengine.marker.dao.RemoteMarkDao;
import com.augurit.agmobile.mapengine.marker.model.Attachment;
import com.augurit.agmobile.mapengine.marker.model.Mark;
import com.augurit.agmobile.mapengine.marker.model.PointStyle;
import com.augurit.agmobile.mapengine.marker.model.SimpleMarkInfo;
import com.augurit.agmobile.mapengine.marker.util.MarkConstant;
import com.augurit.agmobile.mapengine.marker.util.MarkUtil;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.Graphic;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 标注相关业务默认实现类，比如：
 * （1）保存查询历史
 * （2）在线上传标注，从服务端获取所有标注
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.mark.service
 * @createTime 创建时间 ：2016-12-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-20
 */

public class MarkService implements IMarkService {

    public static final String TAG = MarkService.class.getName();

    protected LocalDatabaseMarkDao mLocalDatabaseMarkDao;
    protected RemoteMarkDao mIQueryFeature;
    protected IGetMarkStyleDao mIQueryStyle;
    protected Context mContext;

    /**
     * 一个任务最多重试次数.
     * 重试次数超过MAX_RETRIES, 任务则最终失败.
     */
    protected static final int MAX_RETRIES = 3;
    /**
     * 当前任务的执行次数记录(当尝试超过MAX_RETRIES时就最终失败)
     */
    protected int runCount;
    protected boolean stopped;

    /**
     * Task排队的队列. 不需要thread-safe
     */
    protected Queue<Observable<Void>> addAttachmentTaskQueue = new LinkedList<Observable<Void>>();
    protected Queue<Observable<Void>> deleteAttachmentTaskQueue = new LinkedList<Observable<Void>>();


    public MarkService(Context context){
        this.mContext = context;
        mLocalDatabaseMarkDao = new LocalDatabaseMarkDao();
        mIQueryFeature = new RemoteMarkDao();
        mIQueryStyle = new LocalGetMarkStyleDao();
    }

    @Override
    public List<String> getMarkQueryHistory() {
        return mLocalDatabaseMarkDao.getQueryHistory();
    }

    @Override
    public List<SimpleMarkInfo> query(String searchText) {
        return mLocalDatabaseMarkDao.query(searchText);
    }

    @Override
    public void saveMarkInfoToDatabase(List<Mark> infos) {
        List<SimpleMarkInfo> markInfos = new ArrayList<SimpleMarkInfo>();
        //保存到数据库
        for (Mark mark : infos){
            SimpleMarkInfo markInfo = new SimpleMarkInfo(mark.getId(),mark.getMarkName());
            markInfos.add(markInfo);
        }
        mLocalDatabaseMarkDao.saveMarkTitlesToDatabase(markInfos);
    }

    @Override
    public void saveQueryHistory(String searchText) {
        mLocalDatabaseMarkDao.saveQueryHistory(searchText);
    }

    @Override
    public void applyEdit(final Mark mark,final Callback2<Mark> callback) {

        Observable.create(new Observable.OnSubscribe<Mark>() {
            @Override
            public void call(final Subscriber<? super Mark> subscriber) {

                final ArcGISFeatureLayer featureLayer = MarkUtil.getFeatureLayer(mContext, mark.getGeometry());
                if(!featureLayer.isInitialized()){
                    LogUtil.d("MarkService","上传标注失败");
                    subscriber.onError(new Exception("标注服务不存在"));
                    return;
                }
                Map<String, Object> attrs = MarkUtil.getAttributeFromMark(mark);

                Graphic newGraphic = new Graphic(mark.getGeometry(), mark.getSymbol(), attrs);
                featureLayer.applyEdits(null, null,new Graphic[]{newGraphic}, new CallbackListener<FeatureEditResult[][]>() {
                    @Override
                    public void onCallback(FeatureEditResult[][] featureEditResults) {
                        FeatureEditResult featureEditResult = featureEditResults[2][0];
                        LogUtil.v("是否编辑成功："+featureEditResult.isSuccess());
                        if (featureEditResult.isSuccess()){
                           mark.setId((int) featureEditResult.getObjectId());
                            subscriber.onNext(mark);
                            //如果成功，接着上传附件,这个时候不需要进行等待，任务自己在后台进行
                            uploadAttachments(mark, featureLayer);
                            subscriber.onCompleted();
                        }else {
                            LogUtil.d("MarkService","上传标注失败");
                            subscriber.onError(new Exception("上传失败"));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(new Exception(throwable));
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Mark>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(Mark graphic) {
                        callback.onSuccess(graphic);
                    }
                });
    }

    private void uploadAttachments(Mark mark, ArcGISFeatureLayer featureLayer) {

        for (Attachment attachment : mark.getAttachments()){
            Observable<Void> attachmentObservable = getAddAttachmentObservable(featureLayer, attachment, mark.getId());
            addAttachmentTaskQueue.add(attachmentObservable);
        }
        //开始执行任务
        executeTask();
        LogUtil.d(TAG,"执行上传附件任务开始-------------->>>>>>>>>");
    }

    private void executeTask() {
        //取当前队列头的任务, 但不出队列
        Observable<Void> task = addAttachmentTaskQueue.peek();
        if (task == null) {
            //impossible case
            LogUtil.d(TAG, "impossible: NO task in queue, unexpected!");
            return;
        }
        task.subscribe();
        runCount = 1;
    }


    public Observable<Void> getAddAttachmentObservable(final ArcGISFeatureLayer featureLayer, final Attachment attachment,
                                                       final int id){
       return   Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                final File file = new File(attachment.getFilePath());
                AttachmentsUtils.addAttachment(featureLayer, id, file, new CallbackListener<FeatureEditResult>() {
                    @Override
                    public void onCallback(FeatureEditResult featureEditResult) {

                        boolean success = featureEditResult.isSuccess();
                        LogUtil.d("MarkService","附件："+attachment.getFilePath()+ "上传是否成功："+ success);
                        if (success){
                            startNextTask();
                        }else {
                            retryOrFinishTask(file, featureLayer, attachment, id);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        LogUtil.d("MarkService","附件："+attachment.getFilePath()+ "上传失败,原因是："+throwable.getMessage());
                        retryOrFinishTask(file, featureLayer, attachment, id);
                    }
                });

            }
        }).subscribeOn(Schedulers.io());
    }

    private void retryOrFinishTask(File file, ArcGISFeatureLayer featureLayer, Attachment attachment, int id) {
        if (runCount < MAX_RETRIES && !stopped) {
            //可以继续尝试
            LogUtil.d("MarkService", "附件 (" + file.getName() + ") failed, try again. runCount: " + runCount);
            //重新开始任务
            Observable<Void> attachmentObservable = getAddAttachmentObservable(featureLayer, attachment, id);
            attachmentObservable.subscribe();
            runCount++;
        }
        else {
            //最终失败
            LogUtil.d("MarkService", "附件 (" + file.getName() + ") failed, final failed! runCount: " + runCount);
            startNextTask();
        }
    }

    /**
     * 一个任务最终结束(成功或最终失败)后的处理
     */
    private void startNextTask() {
        //出队列
        addAttachmentTaskQueue.poll();

        //启动队列下一个任务
        if (addAttachmentTaskQueue.size() > 0 && !stopped) {
            executeTask();
        }
    }



    @Override
    public void applyDelete(final Mark mark,final Callback2<Mark> callback) {

        Observable.create(new Observable.OnSubscribe<Mark>() {
            @Override
            public void call(final Subscriber<? super Mark> subscriber) {
                ArcGISFeatureLayer featureLayer = MarkUtil.getFeatureLayer(mContext, mark.getGeometry());
                Map<String, Object> attrs = MarkUtil.provideDeleteAttribute(featureLayer, mark.getId());
                Graphic newGraphic = new Graphic(mark.getGeometry(), mark.getSymbol(), attrs);
                featureLayer.applyEdits(null, new Graphic[]{newGraphic},null, new CallbackListener<FeatureEditResult[][]>() {
                    @Override
                    public void onCallback(FeatureEditResult[][] featureEditResults) {
                        FeatureEditResult featureEditResult = featureEditResults[1][0];
                        LogUtil.v("是否删除成功："+featureEditResult.isSuccess());
                        if (featureEditResult.isSuccess()){
                            subscriber.onNext(mark);
                            subscriber.onCompleted();
                        }else {
                            subscriber.onError(new Exception("上传失败"));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(new Exception(throwable));
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Mark>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(Mark graphic) {
                        callback.onSuccess(graphic);
                    }
                });
    }

    @Override
    public void applyAdd(final Mark mark, final Callback2<Mark> callback) {
        Observable.create(new Observable.OnSubscribe<Mark>() {
            @Override
            public void call(final Subscriber<? super Mark> subscriber) {
                final ArcGISFeatureLayer featureLayer = MarkUtil.getFeatureLayer(mContext, mark.getGeometry());
                if(!featureLayer.isInitialized()){
                    subscriber.onError(new Exception("标注服务不存在"));
                    return;
                }
                mark.setId(MarkConstant.DEFAULT_MARK_ID);
                Map<String, Object> attrs = MarkUtil.getAttributeFromMark(mark);
                if (attrs == null){
                    callback.onFail(new Exception("标注的类型未找到"));
                    return;
                }
                Graphic newGraphic = new Graphic(mark.getGeometry(), mark.getSymbol(), attrs);
                featureLayer.applyEdits(new Graphic[]{newGraphic},null, null, new CallbackListener<FeatureEditResult[][]>() {
                    @Override
                    public void onCallback(FeatureEditResult[][] featureEditResults) {
                        FeatureEditResult featureEditResult = featureEditResults[0][0];
                        LogUtil.v(TAG,"是否添加成功："+featureEditResult.isSuccess());
                        if (featureEditResult.isSuccess()){
                            //填充mark
                            mark.setId((int) featureEditResult.getObjectId());
                            subscriber.onNext(mark);
                            //如果成功，接着上传附件,这个时候不需要进行等待，任务自己在后台进行
                            uploadAttachments(mark, featureLayer);
                            subscriber.onCompleted();
                        }else {
                            subscriber.onError(new Exception("上传失败"));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(new Exception(throwable));
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Mark>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(Mark graphic) {
                        callback.onSuccess(graphic);
                    }
                });
    }

    @Override
    public void getAllMark(Context context, MapView mapView, final Callback2<List<Mark>> callback) {
        mIQueryFeature.queryAllMark(context, mapView, new Callback2<List<Mark>>() {

            @Override
            public void onSuccess(List<Mark> marks) {
                callback.onSuccess(marks);
            }

            @Override
            public void onFail(Exception error) {
                callback.onFail(error);
            }

        });
    }

    @Override
    public void getPointStyleList(final Callback2<List<PointStyle>> callback) {
        mIQueryStyle.getPointStyleList(mContext, new Callback2<List<PointStyle>>() {
            @Override
            public void onSuccess(List<PointStyle> pointStyles) {
                callback.onSuccess(pointStyles);
            }

            @Override
            public void onFail(Exception error) {
                callback.onFail(error);
            }

        });
    }

  /*  @Override
    public void queryAllMark(MapView mapView, final Callback2<List<Mark>> listener) {
        mIQueryFeature.queryAllMark(mContext, mapView, new Callback2<List<Mark>>() {
            @Override
            public void onSuccess(List<Mark> marks) {
                listener.onSuccess(marks);
            }

            @Override
            public void onFail(Exception error) {
                listener.onFail(error);
            }

        });
    }*/

    @Override
    public void deleteMark(SimpleMarkInfo simpleMarkInfo) {
         mLocalDatabaseMarkDao.deleteMark(simpleMarkInfo);
    }

    @Override
    public void deletePreviousMarkInfo() {
        mLocalDatabaseMarkDao.clearPreviousData();
    }

    @Override
    public void deleteAttachements(Mark mark, List<Attachment> attachments,
                                   final Callback2<Boolean> callback) {

        int[] ids = new int[attachments.size()];

        for (int i=0; i< attachments.size(); i++){
            ids[i] = attachments.get(i).getAttachmentId();
        }
        ArcGISFeatureLayer featureLayer = MarkUtil.getFeatureLayer(mContext, mark.getGeometry());
        getDeleteAttachmentObservable(featureLayer,mark.getId(),ids)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(Boolean ifSuccess) {
                      callback.onSuccess(true);
                    }
                });
    }

    public Observable<Boolean> getDeleteAttachmentObservable(final ArcGISFeatureLayer featureLayer, final int id, final int[]
                                                          attachmentIds){
        return  Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {

                AttachmentsUtils.deleteAttachments(featureLayer, id, attachmentIds, new CallbackListener<FeatureEditResult[]>() {
                    @Override
                    public void onCallback(FeatureEditResult[] featureEditResults) {
                        boolean success = featureEditResults[0].isSuccess();
                        LogUtil.d(TAG,"附件删除是否成功："+ success);
                        subscriber.onNext(success);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });

            }
        }).subscribeOn(Schedulers.io());
    }

    public void closeAllMarkRequest(){
        if (mIQueryFeature != null){
            mIQueryFeature.closeAllMarkRequest();
        }
    }
}
