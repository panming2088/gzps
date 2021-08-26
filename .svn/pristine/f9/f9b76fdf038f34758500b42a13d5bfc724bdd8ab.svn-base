package com.augurit.agmobile.gzps.uploadfacility.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.patrolcore.layer.service.AgwebPatrolLayerService2;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.am.fw.utils.StringUtil;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzpssb.service
 * @createTime 创建时间 ：2018-05-04
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-05-04
 * @modifyMemo 修改备注：
 * 门牌上报界面，我的上报控制的是门牌显示与否
 */

public class DoorNoLayerService extends AgwebPatrolLayerService2 {


    private Query query;

    public DoorNoLayerService(Context mContext) {
        super(mContext);
    }

    @Override
    public Observable<List<LayerInfo>> getLayerInfo() {
        return super.getSortedLayerInfos()
                .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                    @Override
                    public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                        //手动加多一个图层
                        LayerInfo info = null;
                        LayerInfo removeInfo = null;
                        for (LayerInfo layerInfo : layerInfos){
//                            if(layerInfo.getLayerName().equals(PatrolLayerPresenter.UPLOAD_WELL_LAYER_NAME)){
//                                layerInfo.setLayerName(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME);
//                                layerInfo.setDefaultVisibility(false);
//                            }
                            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.DOOR_NO_LAYER)){
                                try {
                                    info  = (LayerInfo) layerInfo.clone();
                                    info.setLayerId(1116);
                                    info.setLayerName("我的上报");
                                    info.setLayerOrder(layerInfo.getLayerOrder() + 10);
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                    info = new LayerInfo();
                                    info.setLayerId(1116);
                                    info.setDefaultVisibility(layerInfo.isDefaultVisible());
                                    info.setDirTypeName("我的上报");
                                    info.setChildLayer(layerInfo.getChildLayer());
                                    info.setUrl(layerInfo.getUrl());
                                    info.setOpacity(layerInfo.getOpacity());
                                    info.setType(layerInfo.getType());
                                    info.setLayerName("我的上报");
                                    info.setLayerOrder(layerInfo.getLayerOrder() + 10);
                                }
//                                break;
                            }else if(layerInfo.getLayerName().contains(PatrolLayerPresenter.MY_UPLOAD_LAYER_NAME)){
                            //去掉原来的我的上报图层
                                info.setLayerName(PatrolLayerPresenter.DRAINAGE_UNIT_LAYER);
                            }
                        }

                        if (info != null){
                            layerInfos.add(info);
                        }

                        if(removeInfo !=null){
                            layerInfos.remove(removeInfo);
                        }
                        return layerInfos;
                    }
                });
    }

    public void setQueryByWhere(String where) {
        query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        if (where != null) {
            query.setWhere(where);
        }
    }


    private String objectToString(Object object) {
        if (object == null) {
            return "";
        }
        return StringUtil.getNotNullString(object.toString(),"");
    }

    private double objectToDouble(Object object) {
        if (object == null) {
            return 0;
        }
        return Double.valueOf(object.toString());
    }

    private long objectToLong(Object object) {
        if (object == null) {
            return -1L;
        }
        return Long.valueOf(object.toString());
    }

}
