package com.augurit.agmobile.patrolcore.layer.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.patrolcore.layer.dao.LocalLayerDao;
import com.augurit.agmobile.patrolcore.layer.dao.RemoteLayerDao;
import com.augurit.agmobile.patrolcore.layer.model.LayerList;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.esri.core.geometry.Point;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 林峰新写的接口
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agcollection.layer.service
 * @createTime 创建时间 ：2017-05-25
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-25
 * @modifyMemo 修改备注：
 */

public class PatrolLayerService extends LayersService implements IPatrolLayerService{

    protected RemoteLayerDao mRemoteLayerDao;
    protected LocalLayerDao mLocalLayerDao;

    public static String MAP_EXTENT = ""; //地图范围

    public PatrolLayerService(Context context) {
        super(context);
        mRemoteLayerDao = new RemoteLayerDao(context);
        mLocalLayerDao = new LocalLayerDao(context);
    }


    @Override
    public Observable<LayerList> getLayerList() {
        return mRemoteLayerDao.getLayerList(BaseInfoManager.getUserId(mContext));
    }


    @Override
    public Observable<List<LayerInfo>> getSortedLayerInfos() {

        Observable<List<LayerInfo>> local = mLocalLayerDao.getLayerInfos(BaseInfoManager.getUserId(mContext));

        Observable<List<LayerInfo>> remote = mRemoteLayerDao.getBaseMapFromNewInterface(BaseInfoManager.getUserId(mContext));

        if (local != null){
            return local;
        }else {
            return remote.map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                @Override
                public List<LayerInfo> call(List<LayerInfo> layerInfos) { //保存信息
                    mLocalLayerDao.saveLayerInfos(BaseInfoManager.getUserId(mContext),layerInfos);
                    return layerInfos;
                }
            });
        }
    }

    @Override
    public double getProjectInitialResolution() {
        return 0;
    }

    @Override
    public Point getProjectInitialCenter() {
        return null;
    }


    @Override
    public String getCurrentProjectId() {
        return super.getCurrentProjectId();
    }
}
