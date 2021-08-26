package com.augurit.agmobile.mapengine.layerquery.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.utils.GeodatabaseManager;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.layerquery.NoQueryableLayerException;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.agmobile.mapengine.project.service.IProjectService;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.router.LayerRouter;
import com.augurit.agmobile.mapengine.layerquery.dao.AgcomLayerQueryRestDao;
import com.augurit.agmobile.mapengine.layerquery.dao.RemoteArcgisLayerQueryDao;
import com.augurit.agmobile.mapengine.layerquery.model.LayerQueryKey;
import com.augurit.agmobile.mapengine.project.router.ProjectRouter;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.db.AMQueryBuilder;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 图层查询业务层，包含以下业务：
 * （1）根据选择的图层和关键字进行查询
 * （2）获取查询历史
 * （3）保存查询关键字
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.layerq.newarc.service
 * @createTime 创建时间 ：2016-11-30
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-30
 */

public class LayerQueryService implements ILayerQueryService {

    protected RemoteArcgisLayerQueryDao mRemoteArcGisDao;

    public LayerQueryService(){
        mRemoteArcGisDao = new RemoteArcgisLayerQueryDao();
    }

    @Override
    public void queryLayer(final Context context, final String seachKey, final SpatialReference spatialReference, final Geometry geometry,
                           final List<LayerInfo> queryLayers, int maxCount, final Callback2<List<AMFindResult>> callback) {

        if (ValidateUtil.isListNull(queryLayers)){
            callback.onFail(new NoQueryableLayerException("无可查询图层"));
            return;
        }

        Observable.create(new Observable.OnSubscribe<List<AMFindResult>>() {
            @Override
            public void call(Subscriber<? super List<AMFindResult>> subscriber) {

                List<AMFindResult> featureResults =
                        mRemoteArcGisDao.queryLayer(context,seachKey,queryLayers, geometry,spatialReference);

                subscriber.onNext(featureResults);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AMFindResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast(context,"查询图层失败");
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<AMFindResult> featureResults) {
                        callback.onSuccess(featureResults);
                    }
                });
    }

    @Override
    public void queryLayer(Context context, String seachKey, MapView mapView, Geometry geometry, List<LayerInfo> queryLayers, int maxCount, Callback2<List<AMFindResult>> callback) {

        queryLayer(context,seachKey,mapView.getSpatialReference(),geometry,queryLayers,maxCount,callback);

    }

    @Override
    public void queryLayer(Context context, String seachKey, String fieldName, MapView mapView,
                           Geometry geometry,
                           List<LayerInfo> queryLayers,
                           int maxCount,
                           Callback2<List<AMFindResult>> callback) {
        //todo 按字段查询

    }

    /**
     * 获取查询历史
     * @return
     */
    @Override
    public List<String> getQueryHistory() {
        List<LayerQueryKey> queryKeys = AMDatabase.getInstance().getQueryAll(LayerQueryKey.class);
        List<String> history = new ArrayList<>();
        for (LayerQueryKey layerQueryKey : queryKeys){
            history.add(layerQueryKey.getHistory());
        }
        return  history;
    }

    @Override
    public Observable<List<LayerInfo>> getQueryableLayers(Context context) throws IOException {
        AgcomLayerQueryRestDao agcomLayerQueryRestDao = new AgcomLayerQueryRestDao();

        //userid
        String userId = BaseInfoManager.getUserId(context);

        //专题id
        ProjectRouter projectRouter = new ProjectRouter();
        String projectId = projectRouter.getCurrentProjectId(context, userId);

        LayerRouter layerRouter = new LayerRouter();
        List<LayerInfo> layerInfos = layerRouter.getLayerInfos(context, projectId, userId);
        return  agcomLayerQueryRestDao.getQueryableLayer(context, layerInfos, projectId, userId);
    }

    @Override
    public void saveQueryKey(String key) {
        LayerQueryKey layerQueryKey = new LayerQueryKey();
        layerQueryKey.setHistory(key);
        AMDatabase.getInstance().save(layerQueryKey);
    }

    @Override
    public List<String> suggest(String key) {
        AMQueryBuilder<LayerQueryKey> builder = new AMQueryBuilder<>(LayerQueryKey.class);
        builder.where("history like"+" '%"+key.trim()+"%'");
        ArrayList<LayerQueryKey> result = AMDatabase.getInstance().query(builder);
        List<String> completeData = new ArrayList<>();
        for (LayerQueryKey simpleMarkInfo: result){
            completeData.add(simpleMarkInfo.getHistory());
        }
        return completeData;
    }

    @Override
    public List<Field> getAllFields(Context context,LayerInfo layerInfo) {
        return null;
    }

    @Override
    public double getBestResolutionForReadingIfItHas(Context context) throws IOException {
        IProjectService projectService = ProjectDataManager.getInstance().getIGetProjectInfoService();
        return projectService.getBestResolutionForReadingIfItHas(context);
    }


}
