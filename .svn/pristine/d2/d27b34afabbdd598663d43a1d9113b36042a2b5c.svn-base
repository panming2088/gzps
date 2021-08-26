package com.augurit.agmobile.mapengine.legend.dao;

import com.augurit.agmobile.mapengine.legend.model.Legend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import rx.Observable;

/**
 * 缓存图例
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.legend.dao
 * @createTime 创建时间 ：17/7/21
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/21
 * @modifyMemo 修改备注：
 */

public class CacheLegendDao {



  //  public Map<String,List<Legend>> cacheLegends = new HashMap<>(); //key是专题id，value是对应的图例

    public List<Legend> allLegends;


  /*  public void putLegend(String proejctId,List<Legend> legends){
        cacheLegends.put(proejctId,legends);
    }


    public List<Legend> getLegendsByProejctId(String projectId){
        return  cacheLegends.get(projectId);
    }



    public Observable<List<Legend>> getObservableLegendsByProejctId(final String projectId){

        if (cacheLegends.get(projectId) == null){
            return null;
        }

        return  Observable.fromCallable(new Callable<List<Legend>>() {
            @Override
            public List<Legend> call() throws Exception {
                return cacheLegends.get(projectId);
            }
        });
    }*/


    public Observable<List<Legend>> getAllLegends(){

        if (allLegends == null){
            return null;
        }
        return Observable.fromCallable(new Callable<List<Legend>>() {
            @Override
            public List<Legend> call() throws Exception {
                return allLegends;
            }
        });
    }

    public void putAllLegends(List<Legend> legends){
        allLegends = legends;
    }
}
