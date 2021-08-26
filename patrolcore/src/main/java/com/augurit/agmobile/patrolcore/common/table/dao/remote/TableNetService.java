package com.augurit.agmobile.patrolcore.common.table.dao.remote;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.table.dao.remote.api.GetAllDicsApi;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.api.GetAllFormsApi;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.api.GetAllProjectApi;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.api.TableChildItemsApi;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.api.TableItemsDataApi;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.api.UploadAllRecordApi;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.api.UploadLocalStoreTaskApi;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.api.UploadPhotoApi;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.api.UploadTableItemsApi;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.api.UploadTableTaskApi;
import com.augurit.agmobile.patrolcore.common.table.model.ClientTask;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.google.gson.Gson;

import org.apache.commons.collections4.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 描述：网络逻辑
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model.manager.remote
 * @createTime 创建时间 ：17/3/6
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/6
 * @modifyMemo 修改备注：
 */

public class TableNetService {
    private Context mContext;
    private AMNetwork mAMNetwork;

    public TableNetService(Context context, String baseUrl) {
        mContext = context;
        mAMNetwork = new AMNetwork(baseUrl);
        mAMNetwork.addLogPrint();
        //  mAMNetwork.addRxjavaConverterFactory();
        mAMNetwork.build();
        mAMNetwork.registerApi(UploadTableItemsApi.class);

    }
    /*
    public void getTableItemsNetData(String project_id, final TableNetCallback callback) {
        TableItemsDataApi tableItemsDataApi = (TableItemsDataApi) mAMNetwork.getServiceApi(TableItemsDataApi.class);
        Observable<List<TableItem>> observable = tableItemsDataApi.getTableItemsData(project_id);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TableItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                        ToastUtil.longToast(mContext, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<TableItem> tableItemsData) {
                        LogUtil.d("tableItemsData size:" + tableItemsData.size());
                        callback.onSuccess(tableItemsData);
                        ToastUtil.longToast(mContext, "数据加载成功");
                    }
                });
    }
    */

    public void getDictionaryByNet(String url, final TableNetCallback callback) {
        GetAllDicsApi getAllDicsApi = (GetAllDicsApi) mAMNetwork.getServiceApi(GetAllDicsApi.class);
        Observable<List<DictionaryItem>> observable = getAllDicsApi.getDictionary(url);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DictionaryItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                        ToastUtil.longToast(mContext, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<DictionaryItem> dictionaryItems) {
                        callback.onSuccess(dictionaryItems);
//                        ToastUtil.longToast(mContext, "字段数据加载成功");
                    }
                });
    }

    public void getAllFormTableItemsByNet(String url, final TableNetCallback callback) {
        GetAllFormsApi getAllFormsApi = (GetAllFormsApi) mAMNetwork.getServiceApi(GetAllFormsApi.class);
        Observable<AllFormTableItems> observable = getAllFormsApi.getAllForms(url);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AllFormTableItems>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                        ToastUtil.longToast(mContext, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(AllFormTableItems allFormTableItems) {
                        callback.onSuccess(allFormTableItems);
//                        ToastUtil.longToast(mContext, "字段数据加载成功");
                    }
                });
    }


    public void getProjectDataByNet(String url, final TableNetCallback callback) {
        GetAllProjectApi getAllProjectApi = (GetAllProjectApi) mAMNetwork.getServiceApi(GetAllProjectApi.class);
        Observable<List<Project>> observable = getAllProjectApi.getAllProject(url);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Project>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                        ToastUtil.longToast(mContext, "获取项目列表失败！");
                    }

                    @Override
                    public void onNext(List<Project> projects) {
                        callback.onSuccess(projects);
                        //                        ToastUtil.longToast(mContext, "项目列表加载成功");
                    }
                });
    }

    public void getTableChildItemsByNet(String url, final TableNetCallback callback) {

        TableChildItemsApi tableChildItemsApi = (TableChildItemsApi) mAMNetwork.getServiceApi(TableChildItemsApi.class);
        Observable<TableChildItems> observable = tableChildItemsApi.getTableChildItemsByCode(url);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TableChildItems>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                        ToastUtil.longToast(mContext, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(TableChildItems tableChildItems) {
                        LogUtil.d("tableChildItems:" + tableChildItems.getPageSize());
                        callback.onSuccess(tableChildItems);
//                        ToastUtil.longToast(mContext, "数据加载成功");
                    }
                });
    }

    public void uploadTableItems(String url, String project_id, String loginName, List<TableItem> list, Map<String, String> valueMap, final TableNetCallback callback) {
        // MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        FormBody.Builder builder = new FormBody.Builder();

        //  String project_id = "732c6873-369c-4d84-9716-1f611d5449da";
        builder.add("PROJECT_ID", project_id);
        builder.add("loginName", loginName);
        for (TableItem tableItem : list) {
            if (valueMap.get(tableItem.getField1()) != null) {
                //   if (tableItem.getField2().toUpperCase().equals("PATROL_CODE")) {
                builder.add(tableItem.getField1(), valueMap.get(tableItem.getField1()));
                //   } else {
                //       builder.add(tableItem.getField2().toLowerCase(), valueMap.get(tableItem.getField2()));
                //   }

            }
        }
        RequestBody requestBody = builder.build();

        UploadTableItemsApi uploadTableItemsApi = (UploadTableItemsApi) mAMNetwork.getServiceApi(UploadTableItemsApi.class);
        Observable<ResponseBody> observable = uploadTableItemsApi.getTableItemsData(url, requestBody);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        callback.onSuccess(responseBody);
                    }
                });
    }


    public void uploadTableItems(String url, String recordId, String project_id, String loginName, List<TableItem> list, Map<String, String> valueMap, final TableNetCallback callback) {
        // MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        FormBody.Builder builder = new FormBody.Builder();

        //  String project_id = "732c6873-369c-4d84-9716-1f611d5449da";
        builder.add("PROJECT_ID", project_id);
        builder.add("loginName", loginName);
        builder.add("recordId", recordId);
        for (TableItem tableItem : list) {
            if (valueMap.get(tableItem.getField1()) != null) {
                //   if (tableItem.getField2().toUpperCase().equals("PATROL_CODE")) {
                builder.add(tableItem.getField1(), valueMap.get(tableItem.getField1()));
                //   } else {
                //       builder.add(tableItem.getField2().toLowerCase(), valueMap.get(tableItem.getField2()));
                //   }

            }
        }
        RequestBody requestBody = builder.build();

        UploadTableItemsApi uploadTableItemsApi = (UploadTableItemsApi) mAMNetwork.getServiceApi(UploadTableItemsApi.class);
        Observable<ResponseBody> observable = uploadTableItemsApi.getTableItemsData(url, requestBody);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        callback.onSuccess(responseBody);
                    }
                });
    }


    public void uploadTableItems(String url, String taskId, String recordId, String project_id, String loginName, List<TableItem> list, Map<String, String> valueMap, final TableNetCallback callback) {
        // MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        FormBody.Builder builder = new FormBody.Builder();

        //  String project_id = "732c6873-369c-4d84-9716-1f611d5449da";
        builder.add("PROJECT_ID", project_id);
        builder.add("loginName", loginName);
        builder.add("recordId", recordId);
        builder.add("taskId", taskId);
        for (TableItem tableItem : list) {
            if (valueMap.get(tableItem.getField1()) != null) {
                //   if (tableItem.getField2().toUpperCase().equals("PATROL_CODE")) {
                builder.add(tableItem.getField1(), valueMap.get(tableItem.getField1()));
                //   } else {
                //       builder.add(tableItem.getField2().toLowerCase(), valueMap.get(tableItem.getField2()));
                //   }

            }
        }
        RequestBody requestBody = builder.build();

        UploadTableItemsApi uploadTableItemsApi = (UploadTableItemsApi) mAMNetwork.getServiceApi(UploadTableItemsApi.class);
        Observable<ResponseBody> observable = uploadTableItemsApi.getTableItemsData(url, requestBody);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        callback.onSuccess(responseBody);
                    }
                });
    }

    /**
     * 传递的参数加多一个username xcl 8.31
     *
     * @param url
     * @param taskId
     * @param recordId
     * @param project_id
     * @param loginName
     * @param list
     * @param valueMap
     * @param photoNameMap
     * @param callback
     */
    public void uploadTableItems(String url, String userName, String taskId, String recordId, String project_id, String loginName, List<TableItem> list, Map<String, String> valueMap, Map<String, String> photoNameMap, final TableNetCallback callback) {
        // MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        FormBody.Builder builder = new FormBody.Builder();

        //  String project_id = "732c6873-369c-4d84-9716-1f611d5449da";
        builder.add("PROJECT_ID", project_id);
       // builder.add("loginName", loginName);
        builder.add("recordId", recordId);
       // builder.add("taskId", taskId);
       // builder.add("userName", userName);
        for (TableItem tableItem : list) {
            if (valueMap.get(tableItem.getField1()) != null) {
                //   if (tableItem.getField2().toUpperCase().equals("PATROL_CODE")) {
                //   if(photoNameMap.containsKey(tableItem.getField1())){
                //上传图片名字(多张图片进行拼接)
                //        builder.add(tableItem.getField1(),photoNameMap.get(tableItem.getField1()));
                //    }else {
                builder.add(tableItem.getField1(), valueMap.get(tableItem.getField1()));
                //   }
                //   } else {
                //       builder.add(tableItem.getField2().toLowerCase(), valueMap.get(tableItem.getField2()));
                //   }

            }
        }


        if (photoNameMap!=null) { //9.3 加入非空判断
            for (Map.Entry<String, String> entry : photoNameMap.entrySet()) {
                //  if(entry.getValue() != null) {
                builder.add(entry.getKey(), StringUtil.getNotNullString(entry.getValue(), ""));
                //  }
            }
        }


        RequestBody requestBody = builder.build();

        UploadTableItemsApi uploadTableItemsApi = (UploadTableItemsApi) mAMNetwork.getServiceApi(UploadTableItemsApi.class);
        Observable<ResponseBody> observable = uploadTableItemsApi.getTableItemsData(url, requestBody);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        callback.onSuccess(responseBody);
                    }
                });
    }


    /**
     * 上传保存到本地的任务
     *
     * @param url
     * @param taskId   任务ID
     * @param map      服务器记录ID与数据集映射
     * @param map2     服务器记录ID与projectId映射
     * @param callback
     */
    @Deprecated
    public void uploadLocalStoreTask(String url,
                                     String taskId,
                                     Map<String, List<TableItem>> map,
                                     Map<String, String> map2,
                                     final TableNetCallback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("taskId", taskId);
        StringBuilder records = new StringBuilder();
        records.append("[");
        for (Map.Entry<String, List<TableItem>> entry : map.entrySet()) {
            StringBuilder entryStr = new StringBuilder();
            String projectId = map2.get(entry.getKey());
            entryStr.append("{ ");
            entryStr.append("PROJECT_ID : ");
            entryStr.append(projectId + ", ");
            entryStr.append("recordId : ");
            entryStr.append(entry.getKey() + " , ");
            for (TableItem tableItem : entry.getValue()) {
                entryStr.append(tableItem.getField1() + " : " + tableItem.getValue() + " ,");
            }
            entryStr.append(" }");
            records.append(entryStr + ",");
        }
        records.append(" ]");
        builder.add("records", records.toString());

        RequestBody requestBody = builder.build();
        UploadLocalStoreTaskApi uploadLocalStoreTaskApi = (UploadLocalStoreTaskApi) mAMNetwork.getServiceApi(UploadLocalStoreTaskApi.class);
        Observable<ResponseBody> observable = uploadLocalStoreTaskApi.uploadTableTask(url, requestBody);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        callback.onSuccess(responseBody);
                    }
                });

    }

    public void uploadPhotos(String url, String patrolCode, List<Photo> photos, final TableNetCallback callback) {
        UploadPhotoApi uploadPhotoApi = (UploadPhotoApi) mAMNetwork.getServiceApi(UploadPhotoApi.class);
        HashMap<String, RequestBody> map = new HashMap<>();

        int i = 0;
        for (Photo photo : photos) {
            File file = new File(photo.getLocalPath());
            map.put("file" + i + "\"; filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

           /* RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file",path + photo.getPhotoName(), RequestBody.create(MediaType.parse("image*//*"),file))
                    .build();
            map.put(photo.getPhotoName(),requestBody);*/
            i++;
        }


       /* MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Photo photo : photos) {
            File file = new File(photo.getLocalPath());
            builder.addFormDataPart("file", photo.getPhotoName(), RequestBody.create(MediaType.parse("image*//*"), file));
        }
        RequestBody requestBody = builder.build();*/
        //  String url = "";
        Observable<ResponseBody> observable = uploadPhotoApi.uploadFiles(url, map);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public void uploadPhotos(final String url, Map<String, List<Photo>> photosMap, String prefix, final TableNetCallback callback) {
        final UploadPhotoApi uploadPhotoApi = (UploadPhotoApi) mAMNetwork.getServiceApi(UploadPhotoApi.class);
        final HashMap<String, HashMap<String, RequestBody>> map = new HashMap<>();    //
        for (Map.Entry<String, List<Photo>> entry : photosMap.entrySet()) {
            List<Photo> photos = entry.getValue();
            if (photos != null && !photos.isEmpty()) {
                HashMap<String, RequestBody> requestMap = new HashMap<>();
                int i = 0;
                for (Photo photo : photos) {
                    if (photo.getLocalPath() != null) { //如果之前上传过了，不需要再上传该图片
                        File file = new File(photo.getLocalPath());
                        requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                        i++;
                    }
                }
                if(!requestMap.isEmpty()) {
                    map.put(entry.getKey(), requestMap);
                }
            }
        }
        if (map.isEmpty()) {
            LogUtil.e("TableNetService", "上传图片列表为空");
            callback.onSuccess(0);
            return;
        }
        Observable.from(map.entrySet())
                .flatMap(new Func1<Map.Entry<String, HashMap<String, RequestBody>>, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> call(Map.Entry<String, HashMap<String, RequestBody>> entry) {
                        String key = entry.getKey();
                        HashMap<String, RequestBody> requestMap = entry.getValue();
                        //   if (map.size() == 1) {
                        //         return uploadPhotoApi.uploadFiles(url, requestMap); // 针对老版本处理
                        //    } else {
                        return uploadPhotoApi.uploadFiles(url, key, requestMap);
                        //    }
                    }
                })
                .collect(new Func0<List<ResponseBody>>() {
                    @Override
                    public List<ResponseBody> call() {
                        return new ArrayList<ResponseBody>();
                    }
                }, new Action2<List<ResponseBody>, ResponseBody>() {
                    @Override
                    public void call(List<ResponseBody> responseBodies, ResponseBody responseBody) {
                        responseBodies.add(responseBody);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ResponseBody>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<ResponseBody> responseBodies) {
                        callback.onSuccess(responseBodies);
                    }
                });
    }

    public void getTableItemsNetData(String projectId, String url, final TableNetCallback callback) {
        TableItemsDataApi tableItemsDataApi = (TableItemsDataApi) mAMNetwork.getServiceApi(TableItemsDataApi.class);
        Observable<TableItems> observable = tableItemsDataApi.getTableItemsData(url, projectId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TableItems>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                        ToastUtil.longToast(mContext, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(TableItems tableItemsData) {
                        // LogUtil.d("tableItemsData size:" + tableItemsData.size());
                        callback.onSuccess(tableItemsData);
//                        ToastUtil.longToast(mContext, "数据加载成功");
                    }
                });
    }


    /**
     * 批量提交接口
     *
     * @param url
     */
    public void uploadAllRecords(String url, String param, final TableNetCallback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("param", param);
        RequestBody requestBody = builder.build();
        UploadAllRecordApi uploadAllRecordApi = (UploadAllRecordApi) mAMNetwork.getServiceApi(UploadAllRecordApi.class);
        Observable<ResponseBody> observable = uploadAllRecordApi.uploadAllRecords(url, requestBody);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        callback.onSuccess(responseBody);
                    }
                });


    }

}
