package com.augurit.agmobile.patrolcore.survey.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.AllFormTableItems;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.ProjectTable;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.localhistory.util.ConvertTableUtils;
import com.augurit.agmobile.patrolcore.survey.constant.ServerTableRecordConstant;
import com.augurit.agmobile.patrolcore.survey.constant.ServerTaskConstant;
import com.augurit.agmobile.patrolcore.survey.constant.SurveyDirIdConstant;
import com.augurit.agmobile.patrolcore.survey.dao.SurveyLocalDBDao;
import com.augurit.agmobile.patrolcore.survey.dao.SurveyRemoteDao;

import com.augurit.agmobile.patrolcore.survey.model.GridItem;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTable;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecordConstant;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecord;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecord2;
import com.augurit.agmobile.patrolcore.survey.model.LocalTable2;
import com.augurit.agmobile.patrolcore.survey.model.LocalTableItem2;
import com.augurit.agmobile.patrolcore.survey.model.OfflineTask;
import com.augurit.agmobile.patrolcore.survey.model.ServerTable;
import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;

import com.augurit.agmobile.patrolcore.survey.model.ServerTask;
import com.augurit.agmobile.patrolcore.survey.model.SignTaskResult;
import com.augurit.agmobile.patrolcore.survey.model.SurveyLocation;
import com.augurit.agmobile.patrolcore.survey.util.TableIdConstant;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.log.LogUtil;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.service
 * @createTime 创建时间 ：17/8/22
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/22
 * @modifyMemo 修改备注：
 */

public class SurveyService {

    protected SurveyRemoteDao surveyDao;
    protected SurveyLocalDBDao surveyLocalDao;
    protected Context mContext;

    protected List<LocalServerTableRecord2> allLocalRecords;
    protected List<LocalTable> localTableList;

    public SurveyService(Context context) {

        surveyDao = new SurveyRemoteDao(context);
        mContext = context;
        surveyLocalDao = new SurveyLocalDBDao(context);
    }


    /**
     * 根据任务id，目录id 返回房屋（栋）列表
     *
     * @param taskId 任务id
     * @param dirId  目录id
     * @return 房屋（栋）列表
     */
    public Observable<List<ServerTable>> getDongList(final String dirId, final String taskId) {

        //此处数据库查询因为刷新问题 故现在主线程查询  by gkh
        final TableDBService tableDBService = new TableDBService(mContext);
        allLocalRecords = tableDBService.getAllLocalServerTableRecord();
        LogUtil.d("gkh2222222222222", String.valueOf(allLocalRecords.size()));
        localTableList = tableDBService.getEditedTableItemsFromDB();
        return surveyDao.getDongTableList(dirId, taskId)
                .map(new Func1<List<ServerTable>, List<ServerTable>>() { //手动设置level和taskId
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {

                        for (ServerTable serverTable : serverTables) {
                            serverTable.setLevel("1");
                            ArrayList<ServerTableRecord> records = serverTable.getRecords(); //補充完整taskId,因為後面的replaceServerRecordWithLocalRecord總要用到
                            if (!ListUtil.isEmpty(records)) {
                                for (ServerTableRecord serverTableRecord : records) {
                                    serverTableRecord.setTableId(serverTable.getTableId());
                                    serverTableRecord.setProjectName(serverTable.getProjectName());
                                    if (!TextUtils.isEmpty(taskId)) {
                                        serverTableRecord.setTaskId(taskId);
                                    }
                                }
                            }
                        }
                        return serverTables;
                    }
                })

                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<ServerTable>>>() { //当服务端不行的情况下，从本地读取
                    @Override
                    public Observable<? extends List<ServerTable>> call(Throwable throwable) {
                        return surveyLocalDao.getLocalDongTableObservable(taskId,LocalServerTableRecordConstant.DESIGNATED_BY_LEADER);
                    }
                })
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        //使用本地记录覆盖服务端记录
                        replaceServerRecordWithLocalRecord(serverTables, dirId, taskId);

                        return serverTables;
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    /**
     * 根据栋的Id 获取消防表
     *
     * @param dongId 栋id
     * @return 消防表
     */
    public Observable<List<ServerTable>> getXiaofangTable(final String dongId) {

        //此处数据库查询因为刷新问题 故现在主线程查询  by gkh
        final TableDBService tableDBService = new TableDBService(mContext);
        allLocalRecords = tableDBService.getAllLocalServerTableRecord();
        LogUtil.d("gkh2222222222222", String.valueOf(allLocalRecords.size()));
        localTableList = tableDBService.getEditedTableItemsFromDB();

        return surveyDao.getXiaofang(dongId)
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<ServerTable>>>() { //当服务端不行的情况下，从本地读取
                    @Override
                    public Observable<? extends List<ServerTable>> call(Throwable throwable) {
                        return surveyLocalDao.getLocalXiaofangTable(dongId);
                    }
                })
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        for (ServerTable serverTable : serverTables) {
                            ArrayList<ServerTableRecord> records = serverTable.getRecords();
                            if (!ListUtil.isEmpty(records)) {
                                for (ServerTableRecord serverTableRecord : records) {
                                    serverTableRecord.setTableId(serverTable.getTableId());
                                }
                            }
                        }
                        return serverTables;
                    }
                })
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        //使用本地记录覆盖服务端记录
                        replaceServerRecordWithLocalRecord(serverTables, SurveyDirIdConstant.XIAO_FANG_BIAO, dongId);

                        return serverTables;
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    /**
     * 获取未分配给该用户的栋列表
     *
     * @return 房屋（栋）列表
     */
    public Observable<List<ServerTable>> getNotDesignatedDongList(int page, int rows) {

        //此处数据库查询因为刷新问题 故现在主线程查询  by gkh
        TableDBService tableDBService = new TableDBService(mContext);
        allLocalRecords = tableDBService.getAllLocalServerTableRecord();
        LogUtil.d("gkh2222222222222", String.valueOf(allLocalRecords.size()));
        localTableList = tableDBService.getEditedTableItemsFromDB();
        return surveyDao.getNotDesignatedDongTableList(SurveyDirIdConstant.FANG_WU_DONG, page, rows)
                .map(new Func1<List<ServerTable>, List<ServerTable>>() { //手动设置level和taskId
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {

                        for (ServerTable serverTable : serverTables) {
                            //serverTable.setLevel("1");
                            ArrayList<ServerTableRecord> records = serverTable.getRecords(); //補充完整taskId,因為後面的replaceServerRecordWithLocalRecord總要用到
                            if (!ListUtil.isEmpty(records)) {
                                for (ServerTableRecord serverTableRecord : records) {
                                    serverTableRecord.setTableId(serverTable.getTableId());
                                    serverTableRecord.setProjectName(serverTable.getProjectName());
                                }
                            }
                        }
                        return serverTables;

                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<ServerTable>>>() {
                    @Override
                    public Observable<? extends List<ServerTable>> call(Throwable throwable) {
                        return surveyLocalDao.getLocalDongTableObservable("",LocalServerTableRecordConstant.BY_SELF);
                    }
                })
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        //使用本地记录覆盖服务端记录
                        replaceServerRecordWithLocalRecord(serverTables, SurveyDirIdConstant.FANG_WU_DONG, null);

                        return serverTables;
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    /**
     * 使用本地记录覆盖服务端记录
     *
     * @param recordId     记录的id，可以传入null
     * @param dirId        目录id
     * @param serverTables 服务端数据
     */
    private void replaceServerRecordWithLocalRecord(List<ServerTable> serverTables, String dirId, String recordId) {

        /*if (TextUtils.isEmpty(recordId)) {
            return;
        }*/

        TableDBService tableDBService = new TableDBService(mContext);

        for (ServerTable serverTable : serverTables) {


            ArrayList<ServerTableRecord> records = serverTable.getRecords();

            if (allLocalRecords == null) {
                continue;
            }

            LogUtil.d("gkh1", String.valueOf(allLocalRecords.size()));

            //判断现在的记录是栋记录，套记录，单位记录，还是人口记录,进行过滤掉allLocalRecords中不符合条件的记录
            List<LocalServerTableRecord2> allLocalServerTableRecord2 = filterAllLocalServerTableRecord(dirId, recordId, serverTable);

            LogUtil.d("gkh2 taskId", recordId);
            LogUtil.d("gkh3 tableId", serverTable.getTableId());

            LogUtil.d("gkh4", String.valueOf(allLocalServerTableRecord2.size()));

            Map<String, LocalServerTableRecord2> localRecords = new LinkedHashMap<>();

            for (LocalServerTableRecord2 localServerTableRecord2 : allLocalServerTableRecord2) {
                //由于人口表，境外人口表和流动人口表存在recordId一样的情况，所以，Key采用tableId + recordId 的形式
                String key = localServerTableRecord2.getTableId() + "+" + localServerTableRecord2.getRecordId();
                localRecords.put(key, localServerTableRecord2);
            }
            LogUtil.d("gkh5", String.valueOf(localRecords.size()));

            if (!ListUtil.isEmpty(records)) {

                for (ServerTableRecord serverTableRecord : records) {

                    String key = serverTableRecord.getTableId() + "+" + serverTableRecord.getRecordId();

                    LocalServerTableRecord2 localRecord = localRecords.get(key);

                    if (localRecord != null) { //如果本地有保存数据，拿本地的覆盖掉服务端的
                        String tableKey = localRecord.getTableKey();
                        LocalTable localTable = tableDBService.getEditedTableItemsByTableKey(tableKey);
                        if (localTable != null) {
                            ArrayList<TableItem> localItems = ConvertTableUtils.convert(localTable);

                            if (!ListUtil.isEmpty(serverTableRecord)) {
                                // RealmList<TableItem> items = serverTableRecordBytableKey.get(0).getItems();
                                serverTableRecord.setName(localRecord.getName());
                                serverTableRecord.setItems(localItems);
                                serverTableRecord.setState(ServerTableRecordConstant.UNUPLOAD);
                                serverTableRecord.setTableKey(localTable.getKey());
                                if (localRecord.getLastModifyTime() != 0L) {
                                    serverTableRecord.setLastModifyTime(TimeUtil.getStringTimeYMDMChines(new Date(localRecord.getLastModifyTime())));
                                }
                            }
                            //移除掉该项
                            localRecords.remove(key);
                        }
                    }
                }
            }

            LogUtil.d("gkh6", String.valueOf(localRecords.size()));


            //最后如果有剩余，将剩余的添加到最后
            if (localRecords.size() != 0) {
                Set<Map.Entry<String, LocalServerTableRecord2>> entries = localRecords.entrySet();
                for (Map.Entry<String, LocalServerTableRecord2> entry : entries) {


                    String tableKey = entry.getValue().getTableKey();
                    //   LocalTable localTable = tableDBService.getEditedTableItemsByTableKey(tableKey);
                    LocalTable localTable = null;
                    for (LocalTable item : localTableList) {
                        if (item.getKey().equals(tableKey)) {
                            localTable = item;
                            break;
                        }
                    }
                    LogUtil.d("gkh7", "localTable before");
                    if (localTable != null) {
                        LogUtil.d("gkh7", localTable.getKey());
                        ServerTableRecord serverTableRecord = new ServerTableRecord();
                        serverTableRecord.setName(entry.getValue().getName());
                        serverTableRecord.setRecordId(entry.getValue().getRecordId());
                        switch (dirId) {
                            case SurveyDirIdConstant.FANG_WU_DONG:
                                serverTableRecord.setTaskId(recordId);
                                break;
                            case SurveyDirIdConstant.FANGW_WU_TAO:
                                serverTableRecord.setDongId(recordId);
                                break;
                        }

                        serverTableRecord.setTableId(entry.getValue().getTableId());
                        ArrayList<TableItem> tableItemList = ConvertTableUtils.convert(localTable);

                        LogUtil.d("gkh7", "serverTableRecord before");
                        LogUtil.d("gkh7", serverTableRecord.toString());
                        serverTableRecord.setTaskName(entry.getValue().getTaskName());
                        serverTableRecord.setTaskId(entry.getValue().getTaskId());
                        serverTableRecord.setItems(tableItemList);
                        serverTableRecord.setState(ServerTableRecordConstant.UNUPLOAD);
                        serverTableRecord.setTableKey(tableKey);
                        serverTableRecord.setStandardaddress(entry.getValue().getStandardAddress());
                        if (entry.getValue().getLastModifyTime() != 0L) {
                            serverTableRecord.setLastModifyTime(TimeUtil.getStringTimeYMDMChines(new Date(entry.getValue().getLastModifyTime())));
                        }
                        if (serverTable.getRecords() == null) {
                            ArrayList<ServerTableRecord> serverTableRecords = new ArrayList<>();
                            serverTable.setRecords(serverTableRecords);
                        }
                        serverTable.getRecords().add(serverTableRecord); //将本地独有的记录添加到服务端记录之后
                    }
                }
            }

        }
    }

    /**
     * 根据dirId， recordId进行过滤掉所有本地记录中不符合条件的记录
     *
     * @param dirId       根据dirId判断当前是要获取什么记录，如果dirId是房屋栋，那么此时要获取的是房屋栋记录，此时的recordId表示的是taskId，以此类推
     * @param recordId    上一级的记录id
     * @param serverTable 从服务端获取到的或者离线下载下来的数据
     * @return
     */
    @NonNull
    private List<LocalServerTableRecord2> filterAllLocalServerTableRecord(String dirId, String recordId, ServerTable serverTable) {
        List<LocalServerTableRecord2> allLocalServerTableRecord2 = new ArrayList<>();
        switch (dirId) {
            case SurveyDirIdConstant.FANG_WU_DONG: //房屋栋，那么此时recordId = taskId
                for (LocalServerTableRecord2 item : allLocalRecords) {

                    if (TextUtils.isEmpty(recordId)) { //如果recordId等于空，表示返回的当前表的所有记录
                        if (item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }
                    } else {
                        if (recordId.equals(item.getTaskId()) && item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }
                    }

                }
                break;

            case SurveyDirIdConstant.FANGW_WU_TAO: //房屋套，那么此时recordId = dongId
                for (LocalServerTableRecord2 item : allLocalRecords) {
                    if (TextUtils.isEmpty(recordId)) { //如果recordId等于空，表示返回的当前表的所有记录
                        if (item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }
                    } else {
                        if (recordId.equals(item.getDongId()) && item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }
                    }

                }
                break;
            case SurveyDirIdConstant.REN_KOU_LEI_BEI: //人口类别，那么此时recordId = taoId / recordId = dongId
            case SurveyDirIdConstant.SHI_YOU_DAN_WEI://实有单位，那么此时recordId = taoId / recordId = dongId
                for (LocalServerTableRecord2 item : allLocalRecords) {

                    if (TextUtils.isEmpty(recordId)) { //如果recordId等于空，表示返回的当前表的所有记录

                        if (item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }
                        if (item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }
                    } else {
                        if (recordId.equals(item.getTaoId()) && item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }
                        if (recordId.equals(item.getDongId()) && item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }
                    }


                }
                break;
            case SurveyDirIdConstant.SHI_YOU_DAN_WEI_REN_KOU: //实有单位人口，那么此时recordId = danweiId
                for (LocalServerTableRecord2 item : allLocalRecords) {

                    if (TextUtils.isEmpty(recordId)) { //如果recordId等于空，表示返回的当前表的所有记录

                        if (item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }
                    } else {
                        if (recordId.equals(item.getDanweiId()) && item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }
                    }

                }

            case SurveyDirIdConstant.XIAO_FANG_BIAO: //如果是消防表,此时recordId = dongId；
                for (LocalServerTableRecord2 item : allLocalRecords) {

                    if (TextUtils.isEmpty(recordId)) { //如果recordId等于空，表示返回的当前表的所有记录

                        if (item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }

                    } else {
                        if (recordId.equals(item.getDongId()) && item.getTableId().equals(serverTable.getTableId())) {
                            allLocalServerTableRecord2.add(item);
                        }
                    }

                }
                break;

        }
        return allLocalServerTableRecord2;
    }

    /**
     * 根据任务id，目录id , 房屋（栋）记录的id ，返回房屋（套）列表
     *
     * @param dirId    目录id
     * @param recordId 房屋（栋）记录的id
     * @return 房屋（套）列表
     */
    public Observable<List<ServerTable>> getTaoList(final String dirId, final String recordId) {

        //此处数据库查询因为刷新问题 故现在主线程查询  by gkh
        TableDBService tableDBService = new TableDBService(mContext);
        allLocalRecords = tableDBService.getAllLocalServerTableRecord();
        LogUtil.d("gkh2222222222222", String.valueOf(allLocalRecords.size()));
        localTableList = tableDBService.getEditedTableItemsFromDB();
        return surveyDao
                .getTaoList(dirId, recordId)
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        for (ServerTable serverTable : serverTables) {
                            ArrayList<ServerTableRecord> records = serverTable.getRecords();
                            if (!ListUtil.isEmpty(records)) {
                                for (ServerTableRecord serverTableRecord : records) {
                                    serverTableRecord.setTableId(serverTable.getTableId());
                                    serverTableRecord.setProjectName(serverTable.getProjectName());
                                }
                            }
                        }
                        return serverTables;
                    }
                })

                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<ServerTable>>>() {
                    @Override
                    public Observable<? extends List<ServerTable>> call(Throwable throwable) {

                        return surveyLocalDao.getLocalTaoTable(recordId,LocalServerTableRecordConstant.DESIGNATED_BY_LEADER);

                    }
                })
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        //使用本地记录覆盖服务端记录
                        replaceServerRecordWithLocalRecord(serverTables, dirId, recordId);

                        return serverTables;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取未分配给该用户的房屋（套）列表
     *
     * @param dirId    目录id
     * @param recordId 房屋（栋）记录的id
     * @return 房屋（套）列表
     */
    public Observable<List<ServerTable>> getNotDesignatedTaoList(final String dirId, final String recordId) {

        //此处数据库查询因为刷新问题 故现在主线程查询  by gkh
        TableDBService tableDBService = new TableDBService(mContext);
        allLocalRecords = tableDBService.getAllLocalServerTableRecord();
        LogUtil.d("gkh2222222222222", String.valueOf(allLocalRecords.size()));
        localTableList = tableDBService.getEditedTableItemsFromDB();
        return surveyDao
                .getNotDesignatedTaoList(dirId, recordId)
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        for (ServerTable serverTable : serverTables) {
                            ArrayList<ServerTableRecord> records = serverTable.getRecords();
                            if (!ListUtil.isEmpty(records)) {
                                for (ServerTableRecord serverTableRecord : records) {
                                    serverTableRecord.setTableId(serverTable.getTableId());
                                    serverTableRecord.setProjectName(serverTable.getProjectName());

                                }
                            }
                        }
                        return serverTables;
                    }
                })

                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<ServerTable>>>() {
                    @Override
                    public Observable<? extends List<ServerTable>> call(Throwable throwable) {
                     return surveyLocalDao.getLocalTaoTable(recordId,LocalServerTableRecordConstant.BY_SELF);
                    }
                })
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        //使用本地记录覆盖服务端记录
                        replaceServerRecordWithLocalRecord(serverTables, dirId, recordId);

                        return serverTables;
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    /**
     * 根据任务id，目录id , 房屋（套）记录的id ，返回人口或单位的表单以及表单记录
     *
     * @param dirId    目录id
     * @param recordId 房屋（套）记录的id
     * @param ifDong   传入的recordId是栋id还是套id，true表示record是栋id，false表示是套id
     * @return 剩余表单以及表单记录
     */
    public Observable<List<ServerTable>> getRenkou(final String dirId, final String recordId, boolean ifDong) {

        //此处数据库查询因为刷新问题 故现在主线程查询  by gkh
        TableDBService tableDBService = new TableDBService(mContext);
        allLocalRecords = tableDBService.getAllLocalServerTableRecord();
        LogUtil.d("gkh2222222222222", String.valueOf(allLocalRecords.size()));
        localTableList = tableDBService.getEditedTableItemsFromDB();

        return surveyDao
                .getRenkou(dirId, recordId, ifDong)
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        for (ServerTable serverTable : serverTables) {
                            ArrayList<ServerTableRecord> records = serverTable.getRecords();
                            if (!ListUtil.isEmpty(records)) {
                                for (ServerTableRecord serverTableRecord : records) {
                                    serverTableRecord.setTableId(serverTable.getTableId());
                                    serverTableRecord.setProjectName(serverTable.getProjectName());

                                }
                            }
                        }
                        return serverTables;
                    }
                })

                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<ServerTable>>>() {
                    @Override
                    public Observable<? extends List<ServerTable>> call(Throwable throwable) {
                        return surveyLocalDao.getLocalRenkouTable(recordId);
                    }
                })
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        //使用本地记录覆盖服务端记录
                        replaceServerRecordWithLocalRecord(serverTables, dirId, recordId);

                        return serverTables;
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    /**
     * 只返回单位列表
     *
     * @param recordId 房屋（套）记录的id
     * @return 剩余表单以及表单记录
     */
    public Observable<List<ServerTable>> getOnlyDanwei(final String recordId, final boolean ifDong) {

        //此处数据库查询因为刷新问题 故现在主线程查询  by gkh
        TableDBService tableDBService = new TableDBService(mContext);
        allLocalRecords = tableDBService.getAllLocalServerTableRecord();
        LogUtil.d("gkh2222222222222", String.valueOf(allLocalRecords.size()));
        localTableList = tableDBService.getEditedTableItemsFromDB();

        return surveyDao
                .getOnlyDanwei(recordId, ifDong)
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        for (ServerTable serverTable : serverTables) {
                            ArrayList<ServerTableRecord> records = serverTable.getRecords();
                            if (!ListUtil.isEmpty(records)) {
                                for (ServerTableRecord serverTableRecord : records) {
                                    serverTableRecord.setTableId(serverTable.getTableId());
                                    serverTableRecord.setProjectName(serverTable.getProjectName());
                                }
                            }
                        }
                        return serverTables;
                    }
                })

                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<ServerTable>>>() {
                    @Override
                    public Observable<? extends List<ServerTable>> call(Throwable throwable) {
                        return surveyLocalDao.getLocalDanweiTable(recordId, ifDong);
                    }
                })
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {

                        replaceServerRecordWithLocalRecord(serverTables, SurveyDirIdConstant.SHI_YOU_DAN_WEI, recordId);

                        LogUtil.d("xcl111111111", "过滤后剩下的数据：" + serverTables.size() + "条");
                        return serverTables;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 只返回网格列表
     *
     * @return 网格列表
     */
    public Observable<List<ServerTable>> getWangge() {

        //此处数据库查询因为刷新问题 故现在主线程查询  by gkh
        TableDBService tableDBService = new TableDBService(mContext);
        allLocalRecords = tableDBService.getAllLocalServerTableRecord();
        LogUtil.d("gkh2222222222222", String.valueOf(allLocalRecords.size()));
        localTableList = tableDBService.getEditedTableItemsFromDB();

        return surveyDao
                .getWangge()
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        for (ServerTable serverTable : serverTables) {
                            ArrayList<ServerTableRecord> records = serverTable.getRecords();
                            if (!ListUtil.isEmpty(records)) {
                                for (ServerTableRecord serverTableRecord : records) {
                                    serverTableRecord.setTableId(serverTable.getTableId());
                                    serverTableRecord.setProjectName(serverTable.getProjectName());
                                }
                            }
                        }
                        return serverTables;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取地址核查中的候选网格信息
     * 只返回当前登录用户下的网格
     *
     * @return
     */
    public Observable<List<GridItem>> getGridItems() {
        return surveyDao.getGridItems().subscribeOn(Schedulers.io());
    }

    /**
     * 获取地址核查中待完善地址
     *
     * @return
     */
    public Observable<List<SurveyLocation>> getSurveyLocations(int page, int rows) {
        return surveyDao.getSurveyLocation(page, rows).subscribeOn(Schedulers.io());
    }

    /**
     * 返回某单位下的人口列表(学生或者从业人员)
     *
     * @param recordId   单位的id
     * @param ifXuesheng 是否返回学生列表，true表示返回学生列表;False表示返回从业人员列表
     * @return 某单位下的人口列表
     */
    public Observable<List<ServerTable>> getCongyeRenYuanOrXuesheng(final String recordId, final boolean ifXuesheng) {

        //此处数据库查询因为刷新问题 故现在主线程查询  by gkh
        final TableDBService tableDBService = new TableDBService(mContext);
        allLocalRecords = tableDBService.getAllLocalServerTableRecord();
        LogUtil.d("gkh2222222222222", String.valueOf(allLocalRecords.size()));
        localTableList = tableDBService.getEditedTableItemsFromDB();

        return surveyDao
                .getOnlyRenkou(recordId, ifXuesheng)
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {
                        for (ServerTable serverTable : serverTables) {
                            ArrayList<ServerTableRecord> records = serverTable.getRecords();
                            if (!ListUtil.isEmpty(records)) {
                                for (ServerTableRecord serverTableRecord : records) {
                                    serverTableRecord.setTableId(serverTable.getTableId());
                                    serverTableRecord.setProjectName(serverTable.getProjectName());
                                }
                            }
                        }
                        return serverTables;
                    }
                })

                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<ServerTable>>>() {
                    @Override
                    public Observable<? extends List<ServerTable>> call(Throwable throwable) {

                        if (ifXuesheng) {
                            return surveyLocalDao.getLocalXueshengInfoTable(recordId);
                        }

                        return surveyLocalDao.getLocalCongyeRenyuanTable(recordId);

                    }
                })
                .map(new Func1<List<ServerTable>, List<ServerTable>>() {
                    @Override
                    public List<ServerTable> call(List<ServerTable> serverTables) {

                        replaceServerRecordWithLocalRecord(serverTables, SurveyDirIdConstant.SHI_YOU_DAN_WEI_REN_KOU, recordId);
                        return serverTables;
                    }
                })

                .subscribeOn(Schedulers.io());
    }


    /**
     * 获取已签收任务列表
     *
     * @return
     */
    public Observable<List<ServerTask>> getAcceptedTasks(int page, int row, LatLng location) {

        return surveyDao.getAcceptedTasks()
                .map(new Func1<List<ServerTask>, List<ServerTask>>() {
                    @Override
                    public List<ServerTask> call(List<ServerTask> serverTasks) {
                        for (ServerTask serverTask : serverTasks) {
                            if (serverTask.getPublishTime() != null && serverTask.getPublishTime().contains(".")) {
                                String[] split = serverTask.getPublishTime().split("\\.");
                                serverTask.setPublishTime(split[0]);
                            }
                        }
                        return serverTasks;
                    }
                })
                .doOnNext(new Action1<List<ServerTask>>() {
                    @Override
                    public void call(List<ServerTask> serverTasks) {
                        surveyLocalDao.saveAcceptedTasks(serverTasks); //保存任务列表到本地
                    }
                })
                .onErrorReturn(new Func1<Throwable, List<ServerTask>>() {
                    @Override
                    public List<ServerTask> call(Throwable throwable) {
                        //保存到本地
                        return surveyLocalDao.getAcceptedTasks(); //当出错时从本地读取
                    }
                }).subscribeOn(Schedulers.io());
    }

    /**
     * 获取可以进行下载的任务
     *
     * @return
     */
    public Observable<List<ServerTask>> getDownloadableTasks(int page, int row, LatLng location) {

        return getAcceptedTasks(page, row, location)
                .map(new Func1<List<ServerTask>, List<ServerTask>>() {
                    @Override
                    public List<ServerTask> call(List<ServerTask> serverTasks) {

                        for (ServerTask serverTask : serverTasks) {
                            List<OfflineTask> offlineTaskById = getOfflineTaskById(serverTask.getId());
                            if (!ListUtil.isEmpty(offlineTaskById)) {
                                serverTask.setOfflineSaveTime(TimeUtil.getStringTimeYMDChines(new Date(offlineTaskById.get(0).getSaveTime())));
                            }
                        }

                        return serverTasks;
                    }
                });
    }

    /**
     * 获取所有的任务列表
     *
     * @return
     */
    public Observable<List<ServerTask>> getAllTasks() {

        return Observable.zip(getAcceptedTasks(0, 10, null), getUnAcceptedTasks(0, 10), getFinishedTasks(0, 10), new Func3<List<ServerTask>, List<ServerTask>, List<ServerTask>, List<ServerTask>>() {
            @Override
            public List<ServerTask> call(List<ServerTask> serverTasks, List<ServerTask> serverTasks2, List<ServerTask> serverTasks3) {


                for (ServerTask serverTask : serverTasks) {
                    serverTask.setStat(ServerTaskConstant.CHECKED);
                }

                for (ServerTask serverTask : serverTasks2) {
                    serverTask.setStat(ServerTaskConstant.UNCHECK);
                }

                for (ServerTask serverTask : serverTasks3) {
                    serverTask.setStat(ServerTaskConstant.FINISHED);
                }

                serverTasks.addAll(serverTasks2);
                serverTasks.addAll(serverTasks3);
                return serverTasks;
            }
        }).subscribeOn(Schedulers.io());

    }


    /**
     * 获取已经审核完成的任务
     *
     * @return
     */
    public Observable<List<ServerTask>> getFinishedTasks(int page, int row) {

        return surveyDao.getFinishedTask()
                .subscribeOn(Schedulers.io());
    }


    /**
     * 获取未签收任务列表
     *
     * @return
     */
    public Observable<List<ServerTask>> getUnAcceptedTasks(int page, int row) {

        return surveyDao.getUnAcceptedTasks();
    }

    /**
     * 获取未签收任务列表
     *
     * @return
     */
    public Observable<List<ServerTask>> getNoDesignatedTaskList(int page, int row) {

        return surveyDao.getNoDesignatedTaskList();
    }

    /**
     * 签收单个任务
     *
     * @param task
     * @return
     */
    public Observable<SignTaskResult> signTask(final ServerTask task) {
        return surveyDao.signTask(task.getId());
    }


    /**
     * 遍历进行签收任务
     *
     * @param callback
     */
    public void signAllTasks(final Callback2 callback) {
        getUnAcceptedTasks(0, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ServerTask>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<ServerTask> serverTasks) {
                        Observable.from(serverTasks)
                                .flatMap(new Func1<ServerTask, Observable<SignTaskResult>>() {
                                    @Override
                                    public Observable<SignTaskResult> call(ServerTask serverTask) {
                                        return signTask(serverTask);
                                    }
                                })
                                .toList()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<List<SignTaskResult>>() {
                                    @Override
                                    public void onCompleted() {
                                        callback.onSuccess(null);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        callback.onFail(new Exception(e));
                                    }

                                    @Override
                                    public void onNext(List<SignTaskResult> signTaskResults) {

                                    }
                                });
                    }
                });
    }

    /**
     * 下载任务
     *
     * @param selectedTasks 要进行下载的任务数据
     * @param callback2
     */
    public void downloadTask(List<ServerTask> selectedTasks, Callback2<Boolean> callback2) {
        //保存本次更新时间
        surveyLocalDao.saveSyncTime();
        //删除掉本地保存的数据
        //TableDBService tableDBService = new TableDBService(mContext);
        // tableDBService.deleteAllLocalServerTableRecord();
        // tableDBService.deleteAllLocalServerTable();
        downloadProjectTemplate();


        Observable<List<ServerTable>> offlineTasks = surveyDao.getOfflineTasks(selectedTasks);

        //进行本地保存
        saveOfflineTask(callback2, offlineTasks,LocalServerTableRecordConstant.DESIGNATED_BY_LEADER);
    }

    /**
     * 获取特定的离线任务数据
     *
     * @param selectedTasks 要进行下载的任务数据
     */
    public void saveOfflineTask(List<ServerTask> selectedTasks) {

        Long saveTime = System.currentTimeMillis();
        List<OfflineTask> offlineTasks = new ArrayList<>();

        for (ServerTask serverTask : selectedTasks) {
            OfflineTask offlineTask = new OfflineTask();
            offlineTask.setSaveTime(saveTime);
            offlineTask.setTaskId(serverTask.getId());
            offlineTasks.add(offlineTask);
        }

        surveyLocalDao.saveOffLineTask(offlineTasks);
    }

    /**
     * 获取特定的离线任务数据
     *
     * @param
     */
    public List<OfflineTask> getOfflineTask() {

        return surveyLocalDao.getOfflineTask();
    }

    /**
     * 获取特定的离线任务数据
     *
     * @param
     */
    public List<OfflineTask> getOfflineTaskById(String taskId) {

        return surveyLocalDao.getOfflineTaskById(taskId);
    }

    /**
     * 获取所有任务的离线数据
     */
    public void downloadAllTasks(final Callback2<Boolean> callback2) {

        //保存本次更新时间
        surveyLocalDao.saveSyncTime();
        //删除掉本地保存的数据
        //TableDBService tableDBService = new TableDBService(mContext);
        // tableDBService.deleteAllLocalServerTableRecord();
        // tableDBService.deleteAllLocalServerTable();
        downloadProjectTemplate();


        Observable<List<ServerTable>> allOfflineTasks = surveyDao.getAllOfflineTasks();

        //进行本地保存
        saveOfflineTask(callback2, allOfflineTasks,LocalServerTableRecordConstant.DESIGNATED_BY_LEADER);
    }


    /**
     * 保存离线任务数据
     *
     * @param callback2
     * @param allOfflineTasks
     */
    private void saveOfflineTask(final Callback2<Boolean> callback2, Observable<List<ServerTable>> allOfflineTasks, final String taskType) {
        allOfflineTasks
                .map(new Func1<List<ServerTable>, OffLineData>() {
                    @Override
                    public OffLineData call(List<ServerTable> serverTables) {
                        List<LocalServerTableRecord> localServerTableRecords = new ArrayList<LocalServerTableRecord>();
                        List<LocalServerTable> localServerTables = new ArrayList<LocalServerTable>();

                        for (ServerTable serverTable : serverTables) {

                            String tableType = getTableType(serverTable);

                            //取出记录进行保存
                            String tableId = serverTable.getTableId();
                            String projectName = serverTable.getProjectName();
                            ArrayList<ServerTableRecord> records = serverTable.getRecords();

                            LocalServerTable localServerTable = new LocalServerTable();
                            localServerTable.setTableId(tableId);
                            localServerTable.setTaskId(serverTable.getTaskId());
                            localServerTable.setProjectName(projectName);
                            localServerTable.setTableType(tableType);

                            localServerTables.add(localServerTable);

                            for (ServerTableRecord serverTableRecord : records) {

                                LocalServerTableRecord localServerTableRecord = new LocalServerTableRecord();
                                localServerTableRecord.setDongId(serverTableRecord.getDongId());
                                localServerTableRecord.setDongName(serverTableRecord.getDongName());
                                //对基本人口信息表，流动人口表，境外人口表要进行特殊处理,因为这三张表会出现recordId一样的情况
                                if (tableType.equals(LocalServerTableRecordConstant.LIU_DONG_REN_KOU) ||
                                        tableType.equals(LocalServerTableRecordConstant.REN_KOU_BASIC_INFO) ||
                                        tableType.equals(LocalServerTableRecordConstant.JING_WAI_REN_KOU)) {

                                    String combinedRecordId = tableId + "+" + serverTableRecord.getRecordId();

                                    localServerTableRecord.setRecordId(combinedRecordId);
                                } else {
                                    localServerTableRecord.setRecordId(serverTableRecord.getRecordId());
                                }

                                localServerTableRecord.setTaskId(serverTableRecord.getTaskId());
                                localServerTableRecord.setName(serverTableRecord.getName());
                                localServerTableRecord.setTableId(tableId);
                                localServerTableRecord.setTaskName(serverTableRecord.getTaskName());
                                localServerTableRecord.setTaoId(serverTableRecord.getTaoId());
                                localServerTableRecord.setTaoName(serverTableRecord.getTaoName());
                                localServerTableRecord.setDanweiId(serverTableRecord.getDanweiId());
                                localServerTableRecord.setStandardAddress(serverTableRecord.getStandardaddress());
                                localServerTableRecord.setTableType(tableType);
                                localServerTableRecord.setState(serverTableRecord.getState());
                                localServerTableRecord.setTaskType(taskType);

                                ArrayList<TableItem> items = serverTableRecord.getItems();
                                RealmList<TableItem> tableItems = new RealmList<TableItem>();

                                /*for (int i= 0; i< items.size() ;  i++){
                                    TableItem item = items.get(i);
                                    long time = System.currentTimeMillis();
                                    item.setId(String.valueOf(time + i));

                                    tableItems.add(item);
                                }*/
                                //保存成LocalTable2，并通过tableKey进行关联
                                String tableKey = saveTableItem(items, tableId, serverTableRecord.getRecordId());
                                localServerTableRecord.setTableKey(tableKey);
                                // localServerTableRecord.setItems(tableItems);
                                localServerTableRecords.add(localServerTableRecord);
                            }

                        }

                        return new OffLineData(localServerTables, localServerTableRecords);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OffLineData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback2.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(final OffLineData offLineData) {
                        //本地保存
                        final TableDBService tableDBService = new TableDBService(mContext);
                        tableDBService.saveLocalServerTable(offLineData.getServerTables(), new TableNetCallback() {
                            @Override
                            public void onSuccess(Object data) {

                                tableDBService.saveLocalServerTableRecord(offLineData.getLocalServerTableRecords(), new TableNetCallback() {
                                    @Override
                                    public void onSuccess(Object data) {


                                        callback2.onSuccess(true);
                                    }

                                    @Override
                                    public void onError(String msg) {
                                        callback2.onFail(new Exception(msg));
                                    }
                                });
                            }

                            @Override
                            public void onError(String msg) {
                                callback2.onFail(new Exception(msg));
                            }
                        });

                    }
                });
    }

    /**
     * 下载所有表单模板
     */
    private void downloadProjectTemplate() {
        //下载所有表单模板
        surveyDao.getAllProjectTemplate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AllFormTableItems>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AllFormTableItems allFormTableItems) {

                        if (allFormTableItems.getSuccess().equals("true")) {
                            List<AllFormTableItems.ProjectItem> projectItemList = allFormTableItems.getResult();
                            TableDataManager tableDataManager = new TableDataManager(mContext);
                            for (AllFormTableItems.ProjectItem item : projectItemList) {
                                List<TableItem> columns = item.getColumns();
                                RealmList<TableItem> items = new RealmList<TableItem>();
                                for (TableItem tableItem : columns) {
                                    items.add(tableItem);
                                }

                                ProjectTable projectTable = new ProjectTable();
                                projectTable.setId(item.getProjectId());
                                projectTable.setTableItems(items);
                                tableDataManager.setProjectTableToDB(projectTable);
                            }
                            //TableDBService tableDbService = new TableDBService(mContext);
                            //tableDbService.setTableItemsToDB(allTableItems);
                        }
                    }
                });
    }


    private String getTableType(ServerTable serverTable) {

        String tableType = "";
        String tableId = serverTable.getTableId();

        if (tableId.equals(TableIdConstant.FANG_WU_DONG_TABLE_ID)) { //栋表
            tableType = LocalServerTableRecordConstant.DONG;

        } else if (tableId.equals(TableIdConstant.FANG_WU_TAO_TABLE_ID)) {//套表
            tableType = LocalServerTableRecordConstant.TAO;

        } else if (tableId.equals(TableIdConstant.BASIC_RENKOU_INFO_TABLE_ID)) { //人口基本信息
            tableType = LocalServerTableRecordConstant.REN_KOU_BASIC_INFO;

        } else if (tableId.equals(TableIdConstant.JING_WAI_REN_KOU_TABLE_ID)) { //境外人员
            tableType = LocalServerTableRecordConstant.JING_WAI_REN_KOU;

        } else if (tableId.equals(TableIdConstant.LIUDONG_RENKOU_TABLE_ID)) { //流动
            tableType = LocalServerTableRecordConstant.LIU_DONG_REN_KOU;

        } else if (tableId.equals(TableIdConstant.SHI_TOU_DAN_WEI_TABLE_ID)) {//实有单位
            tableType = LocalServerTableRecordConstant.SHI_YOU_DAN_WEI;

        } else if (tableId.equals(TableIdConstant.XUESHENG_INFO_TABLE_ID)) { //学生信息
            tableType = LocalServerTableRecordConstant.XUE_SHENG_INFO;

        } else if (tableId.equals(TableIdConstant.CONGYE_INFO_TABLE_ID)) { //从业
            tableType = LocalServerTableRecordConstant.CONGYE_RENYUAN_INFO;

        } else if (tableId.equals(TableIdConstant.XIAO_FANG_TABLE_ID)) { //消防
            tableType = LocalServerTableRecordConstant.XIAO_FANG;
        }
        return tableType;
    }


    public String saveTableItem(List<TableItem> tableItems, String projectId, String recordId) {
        RealmList<LocalTableItem2> localTableItems = new RealmList<>();
        long time = System.currentTimeMillis();

        //将表模板子项转换为本地存储的类型
        long i = 0;
        for (TableItem tableItem : tableItems) {
            i++;
            LocalTableItem2 localTableItem = new LocalTableItem2();
            localTableItem.setKey(String.valueOf(time + i)); //key值得保证唯一 否则会被覆盖掉已有数据
            localTableItem.setProjectId(projectId);
            localTableItem.setDic_code(tableItem.getDic_code());
            localTableItem.setBase_table(tableItem.getBase_table());
            localTableItem.setColum_num(tableItem.getColum_num());
            localTableItem.setControl_type(tableItem.getControl_type());
            localTableItem.setDevice_id(tableItem.getDevice_id());
            localTableItem.setField1(tableItem.getField1());
            localTableItem.setField2(tableItem.getField2());
            localTableItem.setValue(tableItem.getValue());
            localTableItem.setHtml_name(tableItem.getHtml_name());
            localTableItem.setId(tableItem.getId());
            localTableItem.setId_edit(tableItem.getIs_edit());
            localTableItem.setIf_hidden(tableItem.getIf_hidden());
            localTableItem.setIndustry_code(tableItem.getIndustry_code());
            localTableItem.setIndustry_table(tableItem.getIndustry_table());
            localTableItem.setRow_num(tableItem.getRow_num());
            localTableItem.setIs_form_field(tableItem.getIs_form_field());
            localTableItem.setRegex(tableItem.getRegex());
            localTableItem.setValidate_type(tableItem.getValidate_type());
            localTableItem.setChildren_code(tableItem.getChildren_code());
            localTableItem.setDisplay_order(tableItem.getDisplay_order());
            localTableItem.setFirst_correlation(tableItem.getFirst_correlation());
            localTableItem.setThree_correlation(tableItem.getThree_correlation());
            localTableItem.setIf_required(tableItem.getIf_required());
            localTableItems.add(localTableItem);
        }

        //创建本地存储的表类实体
        LocalTable2 localTable = new LocalTable2();
        localTable.setList(localTableItems);
        localTable.setId(projectId);
        if (!TextUtils.isEmpty(recordId)) {
            localTable.setRecordId(recordId);
        }

        // localTable.setUserId(userId);
        localTable.setTime(time);
        String tablekey = String.valueOf(time);
        localTable.setKey(tablekey);


        //存进数据库
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(localTable);
        realm.commitTransaction();

        return tablekey;
    }

    public class OffLineData {

        private List<LocalServerTable> serverTables;
        private List<LocalServerTableRecord> localServerTableRecords;

        public OffLineData(List<LocalServerTable> serverTables, List<LocalServerTableRecord> localServerTableRecords) {
            this.serverTables = serverTables;
            this.localServerTableRecords = localServerTableRecords;
        }

        public List<LocalServerTable> getServerTables() {
            return serverTables;
        }

        public List<LocalServerTableRecord> getLocalServerTableRecords() {
            return localServerTableRecords;
        }
    }

}
