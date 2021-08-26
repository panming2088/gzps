package com.augurit.agmobile.patrolcore.layer.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.utils.LayerUrlUtil;
import com.augurit.agmobile.patrolcore.common.file.model.FileResult;
import com.augurit.agmobile.patrolcore.common.file.service.FileService;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.StringUtil;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


/**
 * 专业修补测
 *
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.layer.service
 * @createTime 创建时间 ：2017-10-23
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-10-23
 * @modifyMemo 修改备注：
 */

public class EditLayerService2 {


    /**:
     * 保存设施信息，包括新增和修正
     * @param context
     * @param newLayerUrl   新数据图层URL
     * @param oldGraphic    历史数据图层URL
     * @param oldLayerObjectIdFieldInNewLayer   历史数据的objectId的值在新图层中的字段名
     * @param geometry                          设施地理位置
     * @param valueMap                          设施值列表
     * @param photoList                         图片列表
     * @param callback                          操作完成回调
     */
    public static void applyEdit(final Context context,
                                 final String newLayerUrl,
                                 final Graphic oldGraphic,
                                 final String oldLayerObjectIdFieldInNewLayer,
                                 final Geometry geometry,
                                 final Map<String, String> valueMap,
                                 final List<Photo> photoList,
                                 final CallbackListener<FeatureEditResult[][]> callback){

        final ArcGISFeatureLayer arcGISFeatureLayer = new ArcGISFeatureLayer(newLayerUrl, ArcGISFeatureLayer.MODE.ONDEMAND);
        arcGISFeatureLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status == STATUS.INITIALIZED){
                    final CallbackListener<FeatureEditResult[][]> callbackListener = new CallbackListener<FeatureEditResult[][]>() {
                        @Override
                        public void onCallback(FeatureEditResult[][] featureEditResults) {
                            //设施保存到图层服务后的回调
                            if(featureEditResults == null
                                    || featureEditResults.length == 0
                                    || (featureEditResults[0].length == 0
                                    && featureEditResults[2].length == 0)){
                                callback.onCallback(new FeatureEditResult[0][0]);
                                return;
                            }
                            long oid;
                            try {
                                //获取objectId，如果是修改，objectId在featureEditResults[2]
                                //如果是新增，objectId在featureEditResults[0][0]
                                if (oldGraphic != null
                                        && featureEditResults[2].length > 0
                                        && featureEditResults[2][0] != null) {
                                    oid = featureEditResults[2][0].getObjectId();
                                } else {
                                    oid = featureEditResults[0][0].getObjectId();
                                }
                            } catch (Exception e) {
                                callback.onCallback(new FeatureEditResult[0][0]);
                                return;
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
                            //上传附件
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

                    //判断设施是否存在于新图层中
                    EditLayerService2.isGraphicInNewLayer(oldGraphic, newLayerUrl, oldLayerObjectIdFieldInNewLayer, new Callback2<Graphic>() {
                        @Override
                        public void onSuccess(Graphic newLayerGraphic) {
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
                            try {
                                attrs.put("REPAIR_COM", new LoginService(context, AMDatabase.getInstance()).getUser().getUserName());
                            } catch (Exception e){
                                e.printStackTrace();
                                callback.onError(new Exception("获取用户名失败"));
                                return;
                            }
                            EditLayerService2 editLayerService = new EditLayerService2();
                            Graphic newGraphic = null;
                            if(oldGraphic == null){ // 新增
                                if(geometry == null){
                                    callback.onError(new NullPointerException("新增Graphic时geometry不能为null"));
                                }
                                attrs.put("FLAG_", "2");
                                newGraphic = new Graphic(geometry, null, attrs);
                                editLayerService.addGraphic(newLayerUrl, newGraphic, callbackListener);
                            } else if(newLayerGraphic == null){  //在历史图层修改，但在新图层中没有这个数据
                                attrs.put("FLAG_", "1");
                                String OBJECTIDFieldName = arcGISFeatureLayer.getObjectIdField();
                                attrs.put(oldLayerObjectIdFieldInNewLayer, oldGraphic.getAttributeValue(OBJECTIDFieldName));
                                newGraphic = new Graphic(geometry, null, attrs);
                                editLayerService.addGraphic(newLayerUrl, newGraphic, callbackListener);
                            } else {//在历史图层修改，但在新图层中已经有这个数据
                                Object flag_ = valueMap.get("FLAG_");
                                if(flag_ == null){
                                    flag_ = valueMap.get("flag_");
                                }
                                if(StringUtil.isEmpty(flag_)
                                        || !flag_.equals("2")){
                                    attrs.put("FLAG_", "1");
                                }
                                Map<String, Object> uploadAttr = newLayerGraphic.getAttributes();
                                for(String key : attrs.keySet()){
                                    uploadAttr.put(key, attrs.get(key));
                                }
                                String OBJECTIDFieldName = arcGISFeatureLayer.getObjectIdField();
                                uploadAttr.put(oldLayerObjectIdFieldInNewLayer, oldGraphic.getAttributeValue(OBJECTIDFieldName));
                                newGraphic = new Graphic(geometry, null, uploadAttr);
                                editLayerService.updateGraphic(newLayerUrl, newGraphic, callbackListener);
                            }
                        }

                        @Override
                        public void onFail(Exception error) {
                            callback.onError(error);
                        }
                    });



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


    public static void isGraphicInNewLayer(final Graphic oldGraphic, String newLayerUrl,
                                    final String oldLayerObjectIdFieldInNewLayer,
                                    final Callback2<Graphic> callback){
        if(oldGraphic == null
                || StringUtil.isEmpty(newLayerUrl)){
            callback.onSuccess(null);
            return;
        }
        final ArcGISFeatureLayer arcGISFeatureLayer
                = new ArcGISFeatureLayer(newLayerUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
        arcGISFeatureLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status == STATUS.INITIALIZED){
                    String objectIdField = arcGISFeatureLayer.getObjectIdField();
                    String oldObjectId = oldGraphic.getAttributes().get(objectIdField).toString();
                    Query query = new Query();
                    query.setOutFields(new String[]{"*"});
                    query.setWhere(oldLayerObjectIdFieldInNewLayer + "=" + oldObjectId + " or " + objectIdField + "=" + oldObjectId);
                    arcGISFeatureLayer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                        @Override
                        public void onCallback(FeatureSet featureSet) {
                            if(featureSet == null
                                    || featureSet.getGraphics().length == 0){
                                callback.onSuccess(null);
                            }
                            callback.onSuccess(featureSet.getGraphics()[0]);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            callback.onFail(null);
                        }
                    });
                }
            }
        });
    }


}
