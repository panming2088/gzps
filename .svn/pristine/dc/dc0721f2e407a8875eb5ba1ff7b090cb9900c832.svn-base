package com.augurit.agmobile.gzps.common.service;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.action.dao.remote.ActionNetLogic;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.common.configure.service
 * @createTime 创建时间 ：2017-05-17
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-17
 * @modifyMemo 修改备注：
 */

public class TestConfigureService {

    private TableDBService tableDbService;
    private TableNetService tableNetService;
    private String serverUrl = "http://192.168.30.27:8088/";

    private Context mContext;

    public TestConfigureService(Context context) {
        this.mContext = context;
        tableDbService = new TableDBService(context);
    }

    public void updateFromNet(final String url, final ConfigureNetCallback callback) {
        final ActionNetLogic service = new ActionNetLogic(mContext, url);
        service.updateDataSet() //更新数据集
                .map(new Func1<List<DictionaryItem>, Boolean>() {
                    @Override
                    public Boolean call(List<DictionaryItem> dictionaryItems) {
                        //保存到本地数据库
                        TableDBService tableDataManager = new TableDBService();
                        tableDataManager.setDictionaryToDB(dictionaryItems);
                        return true;
                    }
                }).flatMap(new Func1<Boolean, Observable<List<TableItem>>>() {
            @Override
            public Observable<List<TableItem>> call(Boolean aBoolean) { //进行更新配置项
                return service.updateConfigure();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TableItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError("同步数据字典失败！");
                    }

                    @Override
                    public void onNext(List<TableItem> tableItems) {
                        TableDBService tableDataManager = new TableDBService();
                        tableDataManager.setTableItemsToDB(tableItems);
                        callback.onSuccess(tableItems);
                    }
                });

    }

}