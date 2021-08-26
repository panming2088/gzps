package com.augurit.agmobile.mapengine.layerquery.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.utils.FeatureTableUtil;
import com.augurit.agmobile.mapengine.common.utils.GeodatabaseAndShapeFileManagerFactory;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.table.FeatureTable;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 进行查询shapeFile和Geodatabase
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerquery.service
 * @createTime 创建时间 ：2017-05-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-11
 * @modifyMemo 修改备注：
 */

public class CombineGeodatabaseAndShapeFileLayerQueryService extends OfflineLayerQueryService {

    @Override
    public void queryLayer(Context context,
                           final String searchKey,
                           SpatialReference spatialReference,
                           final Geometry geometry,
                           List<LayerInfo> queryLayers,
                           int maxCount,
                           final Callback2<List<AMFindResult>> callback) {

        GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getQueryTables(queryLayers, context)
                .flatMap(new Func1<List<FeatureTable>, Observable<List<AMFindResult>>>() {
                    @Override
                    public Observable<List<AMFindResult>> call(List<FeatureTable> featureTables) {
                        return FeatureTableUtil.queryFeature(featureTables, geometry, searchKey, 500);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AMFindResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<AMFindResult> findResults) {
                        if (ValidateUtil.isListNull(findResults)) {
                            callback.onFail(new Exception("未找到与"+searchKey+"相关的信息"));
                        } else {
                           /* final AMFindResult[] identifyResultsArray = new AMFindResult[findResults.size()];
                            for (int i = 0; i < findResults.size(); i++) {
                                identifyResultsArray[i] = findResults.get(i);
                            }*/
                            callback.onSuccess(findResults);
                        }
                    }
                });
    }
}
