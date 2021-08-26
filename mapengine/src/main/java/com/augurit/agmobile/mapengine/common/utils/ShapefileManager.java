package com.augurit.agmobile.mapengine.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.ArrayMap;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.MultiPlanVectorLayerBean;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.widget.filepicker.utils.FileUtils;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geodatabase.ShapefileFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * shapeFile管理类
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.utils
 * @createTime 创建时间 ：2017-04-13
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-13
 * @modifyMemo 修改备注：
 */

public class ShapefileManager {

    private Map<String, ShapefileFeatureTable> tableMap = new ArrayMap<>();//key是表名

    /**
     * key是图层的layerName，value是其子图层的tableName
     */
    private Map<String, List<String>> getChildLayerTableNamesByParentLayerName = new HashMap<>();

    private static ShapefileManager sShapefileManager;

    public static ShapefileManager getInstance(Context context) {
        if (sShapefileManager == null) {
            synchronized (ShapefileManager.class) {
                if (sShapefileManager == null) {
                    sShapefileManager = new ShapefileManager(context);
                }
            }
        }
        return sShapefileManager;
    }

    private ShapefileManager(Context context) {
        try {
            initShapeFileMap(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化shapeFile
     *
     * @param context
     */
    private void initShapeFileMap(Context context) {
        String shapeFileSavePath = new FilePathUtil(context).getInternalShapeFilePath(); //默认shapeFile存放路径

        List<File> files = FileUtils.getFileListByDirPath(shapeFileSavePath, new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".shp");
            }
        });

        if (ValidateUtil.isListNull(files)) {
            LogUtil.w(shapeFileSavePath + "路径下找不到shapefile文件");
            return;
        }

        for (File file : files){
            try{
                tableMap.put(file.getName().replace(".shp",""),getShapeFileTable(context,file.getName().replace(".shp","")));
            }catch (Exception e){

            }
        }
    }

    public static FeatureLayer getFeatureLayer(Context context, String fileName) {

        ShapefileFeatureTable shapeFileTable = getShapeFileTable(context, fileName);
        if (shapeFileTable != null) {
            Geometry.Type type = shapeFileTable.getGeometryType();
            FeatureLayer featureLayer = new FeatureLayer(shapeFileTable);
            Symbol symbol = getDefaultSymbol(type);
            SimpleRenderer simpleRenderer = new SimpleRenderer(symbol);
            featureLayer.setRenderer(simpleRenderer);
            return featureLayer;
        }
        return null;
    }

    private static Symbol getDefaultSymbol(Geometry.Type type) {
        Symbol symbol = null;
        switch (type) {
            case POINT:
                SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(Color.BLUE, 5, SimpleMarkerSymbol.STYLE.CIRCLE);
                symbol = simpleMarkerSymbol;
                break;
            case LINE:
            case POLYLINE:
                SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(Color.RED, 2);
                symbol = simpleLineSymbol;
                break;
            case POLYGON:
                SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(Color.YELLOW);
                symbol = simpleFillSymbol;
                break;
            default:
                break;
        }
        return symbol;
    }

    public static ShapefileFeatureTable getShapeFileTable(Context context, String fileName) {
        String shapePath = new FilePathUtil(context).getInternalShapeFilePath() + fileName + ".shp";
        ShapefileFeatureTable shapefileFeatureTable = null;
        try {
            shapefileFeatureTable = new ShapefileFeatureTable(shapePath);
            return shapefileFeatureTable;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 统计需要查询的表
     *
     * @param layerInfos 要进行查询的图层
     * @param context    上下文
     * @return 需要查询的表集合
     */
    public Observable<List<ShapefileFeatureTable>> getQueryTables(final List<LayerInfo> layerInfos, final Context context) {

        return Observable.fromCallable(new Callable<List<ShapefileFeatureTable>>() {
            @Override
            public List<ShapefileFeatureTable> call() throws Exception {
                List<ShapefileFeatureTable> queryTables = new ArrayList<>();
                for (LayerInfo info : layerInfos) {
                    //先使用表名查，如果查不到，说明它不是单一图层，而是组合图层
                    ShapefileFeatureTable table = tableMap.get(info.getLayerTable());
                    if (table == null) { //如果查不到
                        List<String> childLayerTableNames = getChildLayerTableNamesByParentLayerName.get(info.getLayerName()); //是否之前有保存这个图层的子图层表名
                        if (ValidateUtil.isListNull(childLayerTableNames)) {
                            childLayerTableNames = searchForChildLayer(context, info);
                        }
                        if (!ValidateUtil.isListNull(childLayerTableNames)) {
                            getChildLayerTableNamesByParentLayerName.put(info.getLayerName(), childLayerTableNames);
                            for (String tableName : childLayerTableNames) {
                                if (tableMap.get(tableName) != null){
                                    queryTables.add(tableMap.get(tableName));
                                }
                            }
                        }
                    } else {
                        //如果可以查到
                        queryTables.add(table);
                    }
                }
                return queryTables;
            }
        }).subscribeOn(Schedulers.io());

    }

    /**
     * 填充子图层
     *
     * @param context
     * @param info
     */
    private List<String> searchForChildLayer(Context context, LayerInfo info) {
        AgcomLayerInfo agcomLayerInfo = LayerServiceFactory.provideLayerService(context).getAgcomLayerInfos();
        List<String> childLayer = new ArrayList<>();

        if(agcomLayerInfo == null || ValidateUtil.isListNull(agcomLayerInfo.getVectorLayer())){
            return null;
        }

        for (MultiPlanVectorLayerBean layerInfo : agcomLayerInfo.getVectorLayer()) {
            if (layerInfo.getName().equals(info.getLayerName())
                    && layerInfo.getLayerId() != info.getLayerId()) { //名称相等，但是id不同，说明是它的子图层
                childLayer.add(layerInfo.getLayer_table()); //添加图层表名称
            }
        }
        return childLayer;
    }


    /**
     * 查询属性，注意：回调此时依然处于子线程，请不要直接进行刷新界面
     *
     * @param fileName  shapeFile所在路径
     * @param geometry  要查询的位置
     * @param callback2 回调，注意：回调此时依然处于子线程，请不要直接进行刷新界面
     */
    public static void queryFeature(Context context, String fileName, Geometry geometry,
                                    final Callback2<List<AMFindResult>> callback2) {
        ShapefileFeatureTable shapefileFeatureTable = null;

        shapefileFeatureTable = getShapeFileTable(context, fileName);
        if (shapefileFeatureTable == null) {
            callback2.onFail(new Exception("查无shapeFile"));
            return;
        }
        QueryParameters queryParams = createQueryParams(geometry);
        final List<AMFindResult> finalResults = new ArrayList<>();
        final ShapefileFeatureTable finalShapefileFeatureTable = shapefileFeatureTable;
        shapefileFeatureTable.queryFeatures(queryParams, new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult objects) {
                String displayFieldName = objects.getDisplayFieldName();

                List<AMFindResult> findResults = FeatureResultUtil.transFeatureResultToAgFindResult(objects, displayFieldName, finalShapefileFeatureTable.getFields(),
                        finalShapefileFeatureTable.getTableName(),finalShapefileFeatureTable.getTableName());
                finalResults.addAll(findResults);
                if (ValidateUtil.isListNull(finalResults)) {
                    callback2.onFail(new Exception("数据为空"));
                    return;
                }
                callback2.onSuccess(finalResults);
            }

            @Override
            public void onError(Throwable throwable) {
                callback2.onFail(new Exception(throwable));
            }
        });
    }

    private static QueryParameters createQueryParams(com.esri.core.geometry.Geometry geometry) {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setGeometry(geometry);
        queryParameters.setOutFields(new String[]{"*"});
        queryParameters.setSpatialRelationship(SpatialRelationship.CONTAINS);
        queryParameters.setReturnGeometry(true);
        //        queryParameters.setInSpatialReference(mapView.getSpatialReference());
        //        queryParameters.setOutSpatialReference(mapView.getSpatialReference());
        return queryParameters;
    }

}
