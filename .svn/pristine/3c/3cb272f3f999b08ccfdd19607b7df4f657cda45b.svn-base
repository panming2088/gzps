package com.augurit.agmobile.mapengine.common.utils;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geodatabase.ShapefileFeatureTable;
import com.esri.core.table.FeatureTable;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.utils
 * @createTime 创建时间 ：2017-04-18
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-18
 * @modifyMemo 修改备注：
 */

public class GeodatabaseAndShapeFileManager {

    public FeatureTable getTable(Context context,String tableName){
        //优先查shapeFile
        ShapefileFeatureTable shapeFileTable = ShapefileManager.getShapeFileTable(context, tableName);
        if (shapeFileTable == null){
            return GeodatabaseManager.getInstance(context).getGeodatabaseFeatureTable(tableName);
        }
        return shapeFileTable;
    }

    public Observable<List<FeatureTable>> getQueryTables(List<LayerInfo> layerInfos, Context context){
       return Observable.zip(ShapefileManager.getInstance(context).getQueryTables(layerInfos, context),
               GeodatabaseManager.getInstance(context).getQueryTables(layerInfos, context),
                new Func2<List<ShapefileFeatureTable>, List<GeodatabaseFeatureTable>, List<FeatureTable>>() {
                    @Override
                    public List<FeatureTable> call(List<ShapefileFeatureTable> shapefileFeatureTables,
                                                   List<GeodatabaseFeatureTable> tables) {
                        List<FeatureTable> featureTables = new ArrayList<>();
                        featureTables.addAll(shapefileFeatureTables);
                        featureTables.addAll(tables);
                        return featureTables;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

       /* List<FeatureTable> featureTables = new ArrayList<>();

        List<ShapefileFeatureTable> queryTables = ShapefileManager.getInstance(context).getQueryTables(layerInfos, context);
        featureTables.addAll(queryTables);

        List<GeodatabaseFeatureTable> list = GeodatabaseManager.getInstance(context).getQueryTables(layerInfos, context);
        featureTables.addAll(list);
        return featureTables;*/
    }


    public  void clearInstance(Context context){
        getGeodatabaseManager(context).closeGeodatabase();
    }

    public GeodatabaseManager getGeodatabaseManager(Context context){
        return GeodatabaseManager.getInstance(context);
    }

    public ShapefileManager getShapefileManager(Context context){
        return ShapefileManager.getInstance(context);
    }

}
