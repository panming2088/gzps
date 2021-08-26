package com.augurit.agmobile.patrolcore.layer.service;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.file.model.FileResponse;
import com.augurit.agmobile.patrolcore.common.file.model.FileResult;
import com.augurit.agmobile.patrolcore.common.file.service.FileService;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.layer.service
 * @createTime 创建时间 ：2017-10-15
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-10-15
 * @modifyMemo 修改备注：
 */


public class EditLayerService {


    public static void applyEdit(final Context context,
                                 final String layerUrl, final Graphic oldGraphic,
                                 final Geometry geometry,
                                 final Map<String, String> valueMap,
                                 final List<Photo> photoList,
                                 final CallbackListener<FeatureEditResult[][]> callback){

        final ArcGISFeatureLayer arcGISFeatureLayer = new ArcGISFeatureLayer(layerUrl, ArcGISFeatureLayer.MODE.ONDEMAND);
        arcGISFeatureLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status == STATUS.INITIALIZED){
                    final CallbackListener<FeatureEditResult[][]> callbackListener = new CallbackListener<FeatureEditResult[][]>() {
                        @Override
                        public void onCallback(FeatureEditResult[][] featureEditResults) {
                            if(featureEditResults == null
                                    || featureEditResults.length == 0
                                    || (featureEditResults[0].length == 0
                                    && featureEditResults[2].length == 0)
                                    || ListUtil.isEmpty(photoList)){
                                callback.onCallback(new FeatureEditResult[0][0]);
                                return;
                            }
                            boolean haveUploadFile = false;
                            for(Photo photo : photoList){
                                if(photo.getHasBeenUp() == 0){
                                    haveUploadFile = true;
                                    break;
                                }
                            }
                            if(!haveUploadFile){
                                callback.onCallback(new FeatureEditResult[0][0]);
                                return;
                            }
                            long oid;
                            if (oldGraphic != null) {
                                oid = featureEditResults[2][0].getObjectId();
                            } else {
                                oid = featureEditResults[0][0].getObjectId();
                            }
                            // prepare oid as int for FeatureLayer
                            int objectID = (int) oid;
                            List<FileResult> fileResultList = new ArrayList<>();
                            for(Photo photo : photoList){
                                if(photo.getHasBeenUp() == 1
                                        || StringUtil.isEmpty(photo.getLocalPath())){
                                    continue;
                                }
                                FileResult fileResult = new FileResult();
                                fileResult.setLocalPath(photo.getLocalPath());
                                fileResult.setObjectid(objectID + "");
                                fileResult.setLayerName(arcGISFeatureLayer.getName());
                                fileResult.setAttachName(photo.getPhotoName());
                                fileResult.setType("image/jpeg");
                                fileResult.setState(0);
                                fileResultList.add(fileResult);
                                /*arcGISFeatureLayer.addAttachment(objectID,
                                        new File(photo.getLocalPath()),
                                        "image/jpeg",
                                        new CallbackListener<FeatureEditResult>() {
                                            @Override
                                            public void onCallback(FeatureEditResult featureEditResult) {
                                                FeatureEditResult[][] results = new FeatureEditResult[1][1];
                                                results[0][0] = featureEditResult;
                                                callback.onCallback(results);
                                            }

                                            @Override
                                            public void onError(Throwable throwable) {
                                                callback.onCallback(null);
                                            }
                                        });*/
                            }
                            FileService fileService = new FileService(context);
                            fileService.upload(arcGISFeatureLayer.getName(), objectID + "", fileResultList)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<String>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable throwable) {
                                            callback.onCallback(null);
                                        }

                                        @Override
                                        public void onNext(String s) {
                                            callback.onCallback(null);
                                        }
                                    });
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            callback.onError(throwable);
                        }
                    };


                    final Map<String, Object> attrs = new HashMap<>();
                    Field[] fields = arcGISFeatureLayer.getFields();
                    for(Field field : fields){
                        String fieldName = field.getName();
                        String value = valueMap.get(fieldName);
                        if(StringUtil.isEmpty(value)){
                            value = valueMap.get(fieldName.toLowerCase());
                        }
                        if(StringUtil.isEmpty(value)){
                            value = valueMap.get(fieldName.toUpperCase());
                        }
                        if(!StringUtil.isEmpty(value)){
//                            FeatureLayerUtils.setAttribute(attrs, field, value, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
                            attrs.put(field.getName(), value);
                        }
                    }
                    attrs.put("REPAIR_COM", new LoginService(context, AMDatabase.getInstance()).getUser().getUserName());
                    EditLayerService editLayerService = new EditLayerService();
                    Graphic newGraphic = null;
                    if(oldGraphic == null){
                        if(geometry == null){
                            callback.onError(new NullPointerException("新增Graphic时geometry不能为null"));
                            return;
                        }
                        attrs.put("FLAG_", "2");
                        newGraphic = new Graphic(geometry, null, attrs);
                        editLayerService.addGraphic(layerUrl, newGraphic, callbackListener);
                    } else {
                        Object flag_ = valueMap.get("FLAG_");
                        if(flag_ == null){
                            flag_ = valueMap.get("flag_");
                        }
                        /*if(flag_ == null
                                || StringUtil.isEmpty(flag_.toString().trim())){
                            attrs.put("FLAG_", "2");
                        } else if(flag_.toString().equals("0")){
                            attrs.put("FLAG_", "1");
                        }*/
                        if(StringUtil.isEmpty(flag_)
                                || !flag_.equals("2")){
                            attrs.put("FLAG_", "1");
                        }
                        Map<String, Object> uploadAttr = oldGraphic.getAttributes();
                        for(String key : attrs.keySet()){
                            uploadAttr.put(key, attrs.get(key));
                        }
                        newGraphic = new Graphic(geometry, null, uploadAttr);
                        editLayerService.updateGraphic(layerUrl, newGraphic, callbackListener);
                    }
                }
            }
        });

    }

    public void applyEdits(String layerUrl, final Graphic[] adds, final Graphic[] deletes, final Graphic[] updates, final CallbackListener<FeatureEditResult[][]> callback){
        final ArcGISFeatureLayer arcGISFeatureLayer = new ArcGISFeatureLayer(layerUrl, ArcGISFeatureLayer.MODE.ONDEMAND);
        arcGISFeatureLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status == STATUS.INITIALIZED){
                    arcGISFeatureLayer.applyEdits(adds, deletes, updates, callback);
                }
            }
        });
    }

    public void addGraphic(String layerUrl, Graphic graphic, CallbackListener<FeatureEditResult[][]> callback){
        if(callback == null){
            callback = new CallbackListener<FeatureEditResult[][]>() {
                @Override
                public void onCallback(FeatureEditResult[][] featureEditResults) {

                }

                @Override
                public void onError(Throwable throwable) {

                }
            };
        }
        applyEdits(layerUrl, new Graphic[]{graphic}, new Graphic[0], new Graphic[0], callback);
    }

    public void updateGraphic(String layerUrl, Graphic graphic, CallbackListener<FeatureEditResult[][]> callback){
        if(callback == null){
            callback = new CallbackListener<FeatureEditResult[][]>() {
                @Override
                public void onCallback(FeatureEditResult[][] featureEditResults) {

                }

                @Override
                public void onError(Throwable throwable) {

                }
            };
        }
//        attrs.put(arcGISFeatureLayer.getObjectIdField(), oldGraphidc.getAttributeValue(arcGISFeatureLayer.getObjectIdField()));
        applyEdits(layerUrl, new Graphic[0], new Graphic[0], new Graphic[]{graphic}, callback);
    }

    public void deleteGraphic(String layerUrl, Graphic graphic, CallbackListener<FeatureEditResult[][]> callback){
        if(callback == null){
            callback = new CallbackListener<FeatureEditResult[][]>() {
                @Override
                public void onCallback(FeatureEditResult[][] featureEditResults) {

                }

                @Override
                public void onError(Throwable throwable) {

                }
            };
        }
        applyEdits(layerUrl, new Graphic[0], new Graphic[]{graphic}, new Graphic[0], callback);
    }


    public static ArrayList<TableItem> getCompleteTableItem(Graphic graphic, List<TableItem> tableItems){
        Map<String, Object> attrs = graphic.getAttributes();
        ArrayList<TableItem> tableItemList = new ArrayList<>();
        for(TableItem tableItem : tableItems){
            Object value = attrs.get(tableItem.getField1());
            if(value == null){
                value = attrs.get(tableItem.getField1().toLowerCase());
            }
            if(value == null){
                value = attrs.get(tableItem.getField1().toUpperCase());
            }
            if(value == null){
                for(String key : attrs.keySet()){
                    if(key.toLowerCase().equals(tableItem.getField1().toLowerCase())){
                         value = attrs.get(key);
                    }
                }
            }
            if(value != null){
                tableItem.setValue(value.toString());
            }
            tableItemList.add(tableItem);
        }
        return tableItemList;
    }
}
