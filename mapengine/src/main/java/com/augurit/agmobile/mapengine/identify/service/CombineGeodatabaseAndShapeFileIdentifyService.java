package com.augurit.agmobile.mapengine.identify.service;

import android.app.Activity;

import com.augurit.agmobile.mapengine.common.utils.FeatureTableUtil;
import com.augurit.agmobile.mapengine.common.utils.GeodatabaseAndShapeFileManagerFactory;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.table.FeatureTable;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 采用shapeFile和Geodatabase进行查询
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.identify.service
 * @createTime 创建时间 ：2017-05-09
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-09
 * @modifyMemo 修改备注：
 */

public class CombineGeodatabaseAndShapeFileIdentifyService extends GeodatabaseIdentifyService {

    @Override
    public void selectedFeature(Activity context,
                                MapView mapView, List<LayerInfo> visibleQueryableLayers,
                                final Geometry geometry,
                                int tolerance, final Callback2<AMFindResult[]> callback) {

        GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getQueryTables(visibleQueryableLayers, context)
                .flatMap(new Func1<List<FeatureTable>, Observable<List<AMFindResult>>>() {
                    @Override
                    public Observable<List<AMFindResult>> call(List<FeatureTable> featureTables) {
                        return FeatureTableUtil.queryFeature(featureTables, geometry, null, 1000);
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
                            callback.onFail(new Exception("点查无数据"));
                        } else {
                            final AMFindResult[] identifyResultsArray = new AMFindResult[findResults.size()];
                            for (int i = 0; i < findResults.size(); i++) {
                                identifyResultsArray[i] = findResults.get(i);
                            }
                            callback.onSuccess(identifyResultsArray);
                        }
                    }
                });

       /* FeatureTableUtil.queryFeature(queryTables, geometry, null, 1000)
                .subscribeOn(Schedulers.io())
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
                            callback.onFail(new Exception("点查无数据"));
                        } else {
                            final AMFindResult[] identifyResultsArray = new AMFindResult[findResults.size()];
                            for (int i = 0; i < findResults.size(); i++) {
                                identifyResultsArray[i] = findResults.get(i);
                            }
                            callback.onSuccess(identifyResultsArray);
                        }
                    }
                });*/
    }
}
