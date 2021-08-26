package com.augurit.agmobile.gzps.statistic.service;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.constant.GzpsConstant;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentTypeConstant;
import com.augurit.agmobile.gzps.statistic.model.StatisticResult2;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.statistic.service
 * @createTime 创建时间 ：17/11/2
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/2
 * @modifyMemo 修改备注：
 */

public class StatisticService2 {

    /**
     * 分组字段
     */
    private static final String GROUP_FIELD = "DISTRICT";//ADDR

    private static final String STATISTIC_FIELD = "OBJECTID";


    /**
     * 统计修改过的部件，按行政区划进行分组
     *
     * @param layerUrl
     * @return
     * @throws Exception
     */
//    public List<Map<String, Object>> statisticOldComponents(String layerUrl) throws Exception {
//        QueryParameters qParameters = new QueryParameters();
//        // qParameters.setGeometry(statisticsParam.getGeometry());
//        // qParameters.setOutSpatialReference(statisticsParam.getSpatialReference());
//        qParameters.setReturnGeometry(true);
//        qParameters.setWhere(ComponentTypeConstant.COMPONENT_TYPE_KEY + "='" + ComponentTypeConstant.OLD_COMPONENT2 + "'");
//        qParameters.setGroupByFieldsForStatistics(new String[]{"DISTRICT"});
//        qParameters.setOutStatistics(new OutStatistics[]{new OutStatistics(OutStatistics.Type.COUNT, "OBJECTID", "新增部件个数")});
//        qParameters.setOutFields(new String[]{"OBJECTID"});
//        QueryTask qTask = new QueryTask(layerUrl);
//        FeatureResult featureResult = qTask.execute(qParameters);
//        List<Map<String, Object>> result = new ArrayList<>();
//        Iterator iterator = featureResult.iterator();
//        while (iterator.hasNext()) {
//            Object o = iterator.next();
//            if (o instanceof Map) {
//                Map<String, Object> element = (Map<String, Object>) o;
//                result.add(element);
//            }
//        }
//        return result;
//    }
    public Map<String,List<StatisticResult2>> statisticComponents(String layerUrl,String type) throws Exception {
        QueryParameters qParameters = new QueryParameters();
        qParameters.setReturnGeometry(true);
        qParameters.setWhere(ComponentTypeConstant.COMPONENT_TYPE_KEY + "='" + type + "'");
        qParameters.setOutFields(new String[]{GROUP_FIELD,"OBJECTID"});
        QueryTask qTask = new QueryTask(layerUrl);
        FeatureResult featureResult = qTask.execute(qParameters);
        Iterator iterator = featureResult.iterator();
        Map<String,List<StatisticResult2>> statisticResult = new LinkedHashMap<>();

        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof Feature) {

               String district = (String) ((Feature) o).getAttributes().get(GROUP_FIELD);
               StatisticResult2 statisticResult2 = new StatisticResult2();
               statisticResult2.setAttrs(((Feature) o).getAttributes());
               statisticResult2.setId(((Feature) o).getId());
               statisticResult2.setLayerUrl(layerUrl);
               statisticResult2.setGeometry(((Feature) o).getGeometry());
               statisticResult2.setSymbol(((Feature) o).getSymbol());

               if (statisticResult.get(district) == null){
                   List<StatisticResult2> features = new ArrayList<>();
                   features.add(statisticResult2);
                   statisticResult.put(district,features);
               }else {
                   statisticResult.get(district).add(statisticResult2);
               }
            }
        }
        return statisticResult;
    }

    /**
     * 按照行政区域统计部件
     * @param type 部件类型，有新增和修改过，{@link ComponentTypeConstant#NEW_ADDED_COMPONENT_VALUE}以及{@link ComponentTypeConstant#OLD_COMPONENT_VALUE2}
     */
    public Observable<Map<String,List<StatisticResult2>>> statisticByDistrict(final String type) {

        return Observable.fromCallable(new Callable<Map<String,List<StatisticResult2>>>() {
            @Override
            public Map<String,List<StatisticResult2>> call() throws Exception {

                Map<String,List<StatisticResult2>> maps = new LinkedHashMap<>();
                String[] districts = GzpsConstant.districts;
                districts = reverse(districts);
                for(String district : districts){
                    if(!maps.containsKey(district)){
                        maps.put(district, new ArrayList<StatisticResult2>());
                    }
                }
                String[] componentUrls = LayerUrlConstant.newComponentUrls;
                if (componentUrls != null && componentUrls.length > 0){
                    for (String url : componentUrls){
                        Map<String,List<StatisticResult2>> submap = statisticComponents(url,type);
                        for(String key : submap.keySet()){
                            if(maps.containsKey(key)){
                                maps.get(key).addAll(submap.get(key));
                            } else {
                                maps.put(key, submap.get(key));
                            }
                        }
                    }
                }

                return maps;
            }
        }).subscribeOn(Schedulers.io());
    }


    public String[] reverse(String[] array){
        int length = array.length;
        String[] re = new String[length];
        for(int i=0; i<length; i++){
            re[length-i-1] = array[i];
        }
        return re;
    }

}
