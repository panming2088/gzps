package com.augurit.agmobile.gzps.publicaffair.service;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.uploadfacility.util.FacilityAffairQueryUtil;
import com.augurit.agmobile.mapengine.common.agmobilelayer.TileLayer;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.XORDecryptTileCacheReader;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.layermanage.dao.RemoteLayerRestDao;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.util.FutureThreadPool;
import com.augurit.agmobile.patrolcore.layer.util.PatrolLayerFactory;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.Layer;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 我的数据上报图层权限控制规则更改：不再根据用户名进行过滤，而是根据所在区域进行过滤
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.publicaffair
 * @createTime 创建时间 ：17/12/13
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/13
 * @modifyMemo 修改备注：
 */

public class FacilityAffairLayerFactory extends PatrolLayerFactory {


    private String uploadType;
    private String facilityType;
    private String district;
    private Long startTime;
    private Long endTime;

    public FacilityAffairLayerFactory(String uploadType, String facilityType, String district
    ,Long startTime,Long endTime) {
        this.uploadType = uploadType;
        this.facilityType = facilityType;
        this.district = district;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Nullable
    @Override
    protected Layer getDynamicLayer(Context context, LayerInfo layerInfo, Layer layer, String url, boolean ifNetworkAvailable) {
        String bundlePath = new FilePathUtil(context).getSavePath() + "/tile/" + layerInfo.getLayerName() + "/Layers";
        if (new File(bundlePath).exists()) {
            try {
                XORDecryptTileCacheReader tileCacheReader = new XORDecryptTileCacheReader(bundlePath);
                layer = new TileLayer(bundlePath, tileCacheReader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (layer == null && ifNetworkAvailable && urlIsReachable(url.replace("FeatureServer", "MapServer"))) { //只有在线状态才加载动态图层
            layer = new ArcGISDynamicMapServiceLayer(url.replace("FeatureServer", "MapServer"));
            /**
             * 上报图层
             */
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.UPLOAD_LAYER_NAME)) {
                List<LayerInfo> childLayer = layerInfo.getChildLayer();
                /**
                 * 在可以联网的情况下再次尝试补充子图层数据，如果依旧不行的话，那么只能不添加过滤条件
                 */
                if (ListUtil.isEmpty(childLayer)){
                    layerInfo = completeChildLayerInfo(context,layerInfo);
                    LogUtil.d("okhttp","成功取回数据");
                    childLayer = layerInfo.getChildLayer();
                }
                //进行权限控制
                HashMap<Integer, String> layerDefs = new HashMap<Integer, String>(16);
                String where = FacilityAffairQueryUtil.getWhere(uploadType, facilityType, district, startTime, endTime);
                if (!TextUtils.isEmpty(where) && !ListUtil.isEmpty(childLayer)){
                    for (LayerInfo child : childLayer) {
                        layerDefs.put(child.getLayerId(), where);
                    }
                    ((ArcGISDynamicMapServiceLayer) layer).setLayerDefinitions(layerDefs);
                }
            }

            /**
             * 设施图层加入权限控制
             */
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.COMPONENT_LAYER)) {
                String userOrg = BaseInfoManager.getUserOrg(context);
                //市级可以查看全部管线
                if (userOrg.contains("市")) {
                    return layer;
                }
                //如果是净水公司，只能看中心六区 + 番禺区
                String where = null;
                if (userOrg.contains("净水公司")) {
                    where = "DISTRICT Like '%天河%' or DISTRICT Like '%黄埔%' or DISTRICT Like '%海珠%'or DISTRICT Like '%荔湾%'" +
                            "or DISTRICT Like '%白云%' or DISTRICT Like '%越秀%' or DISTRICT Like '%番禺%'";
                } else {
                    where = "DISTRICT like '%" + userOrg + "%'";
                }
                List<LayerInfo> childLayer = layerInfo.getChildLayer();
                if (!ListUtil.isEmpty(childLayer) && !TextUtils.isEmpty(userOrg)) {
                    //进行权限控制
                    HashMap<Integer, String> layerDefs = new HashMap<Integer, String>(16);
                    for (LayerInfo child : childLayer) {
                        if (child.getLayerName().contains("排水管")) {
                            layerDefs.put(child.getLayerId(), where);
                        }
                    }
                    ((ArcGISDynamicMapServiceLayer) layer).setLayerDefinitions(layerDefs);
                }
            }
        }
        return layer;
    }


    protected static LayerInfo completeChildLayerInfo(final Context context, final LayerInfo layerInfo) {

        FutureTask<LayerInfo> futureTask = FutureThreadPool.getInstance().executeTask(new Callable<LayerInfo>() {
            @Override
            public LayerInfo call() throws Exception {
                RemoteLayerRestDao remoteLayerRestDao = new RemoteLayerRestDao();
                return remoteLayerRestDao.completeChildAMLayerInfo(context,layerInfo);
            }
        });
        try {
            return futureTask.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return layerInfo;
    }
}
