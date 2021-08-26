package com.augurit.agmobile.patrolcore.survey.dao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;

import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTable;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecord;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecordConstant;
import com.augurit.agmobile.patrolcore.survey.model.LocalTable2;
import com.augurit.agmobile.patrolcore.survey.model.LocalTableItem2;
import com.augurit.agmobile.patrolcore.survey.model.OfflineTask;
import com.augurit.agmobile.patrolcore.survey.model.OfflineTaskConstant;
import com.augurit.agmobile.patrolcore.survey.model.ServerTable;
import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;
import com.augurit.agmobile.patrolcore.survey.model.ServerTask;
import com.augurit.agmobile.patrolcore.survey.util.TableIdConstant;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.util.SharedPreferencesUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * 将任务进行本地保存的类，表单以及表单中的记录等通过{@link TableDBService}保存；
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.dao
 * @createTime 创建时间 ：17/8/30
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/30
 * @modifyMemo 修改备注：
 */

public class SurveyLocalDBDao {

    private Context mContext;
    private TableDBService tableDBService;


    public SurveyLocalDBDao(Context context) {
        mContext = context;
        tableDBService = new TableDBService(context);
    }


    /**
     * 获取当前用户的已签收任务
     *
     * @return
     */
    public List<ServerTask> getAcceptedTasks() {

        //返回当前用户的所有任务
        return AMDatabase.getInstance().getQueryByWhere(ServerTask.class, "userId", BaseInfoManager.getUserId(mContext));
    }


    /**
     * 保存已签收任务
     */
    public void saveAcceptedTasks(List<ServerTask> serverTasks) {

        for (ServerTask serverTask : serverTasks) {
            serverTask.setUserId(BaseInfoManager.getUserId(mContext));
        }

        //保存该用户的所有任务
        AMDatabase.getInstance().saveAll(serverTasks);
    }


    /**
     * 根据taskId 获取本地保存的该任务下栋表的数据
     *
     * @param taskId 任务id = 地址id
     * @return
     */
    public Observable<List<ServerTable>> getLocalDongTableObservable(final String taskId, final String taskType) {

        return Observable.create(new Observable.OnSubscribe<List<ServerTable>>() {
            @Override
            public void call(final Subscriber<? super List<ServerTable>> subscriber) {

                if (TextUtils.isEmpty(taskId)) {
                    tableDBService.getAllLocalServerTableRecordByTaskType(taskType,getDongCallback(subscriber));
                } else {
                    Map<String,String> map = new LinkedHashMap<String, String>();
                    map.put("taskId",taskId);
                    map.put("taskType",taskType);
                    tableDBService.getLocalServerTableRecordByField(map,getDongCallback(subscriber));
                }
            }
        });
    }


    /**
     * 根据dongId 获取消防表
     *
     * @param dongId 栋的id
     * @return
     */
    public Observable<List<ServerTable>> getLocalXiaofangTable(final String dongId) {

        return Observable.create(new Observable.OnSubscribe<List<ServerTable>>() {
            @Override
            public void call(final Subscriber<? super List<ServerTable>> subscriber) {

                if (TextUtils.isEmpty(dongId)) {
                    tableDBService.getAllLocalServerTableRecord(getXiaofangCallback(subscriber, dongId));
                } else {
                    Map<String,String> map = new LinkedHashMap<String, String>();
                    map.put("dongId",dongId);
                    tableDBService.getLocalServerTableRecordByField(map, getXiaofangCallback(subscriber, dongId));
                }

            }
        });
    }


    /**
     * 获取栋列表的回调
     *
     * @param subscriber
     * @param dongId 栋的id
     * @return
     */
    @NonNull
    private TableNetCallback<List<LocalServerTableRecord>> getXiaofangCallback(final Subscriber<? super List<ServerTable>> subscriber, final String dongId) {
        return new TableNetCallback<List<LocalServerTableRecord>>() {
            @Override
            public void onSuccess(List<LocalServerTableRecord> data) {

                if (ListUtil.isEmpty(data)) {
                    //返回只包含tableId的ServerTable
                    getServerTable(LocalServerTableRecordConstant.XIAO_FANG, subscriber);
                } else {
                    List<ServerTable> serverTables = new ArrayList<ServerTable>();
                    ArrayList<ServerTableRecord> serverTableRecords = getServerTableRecordList(data, LocalServerTableRecordConstant.XIAO_FANG);
                    if (!ListUtil.isEmpty(serverTableRecords)){
                        ServerTable serverTable = new ServerTable();
                        serverTable.setTableId(TableIdConstant.XIAO_FANG_TABLE_ID);
                        serverTable.setRecords(serverTableRecords);
                        serverTables.add(serverTable);
                        subscriber.onNext(serverTables);
                        subscriber.onCompleted();
                    }else {
                        getServerTable(LocalServerTableRecordConstant.XIAO_FANG,subscriber);
                    }

                }

            }

            @Override
            public void onError(String msg) {
                subscriber.onError(new Exception(msg));
            }
        };
    }

    /**
     * 获取栋列表的回调
     *
     * @param subscriber
     * @param taskId
     * @return
     */
    @NonNull
    private TableNetCallback<List<LocalServerTableRecord>> getDongCallback(final Subscriber<? super List<ServerTable>> subscriber) {
        return new TableNetCallback<List<LocalServerTableRecord>>() {
            @Override
            public void onSuccess(List<LocalServerTableRecord> data) {

                if (ListUtil.isEmpty(data)) {
                    //返回只包含tableId的ServerTable
                    getServerTable(LocalServerTableRecordConstant.DONG, subscriber);
                } else {
                    List<ServerTable> serverTables = new ArrayList<ServerTable>();
                    ArrayList<ServerTableRecord> serverTableRecords = getServerTableRecordList(data, LocalServerTableRecordConstant.DONG);
                    ServerTable serverTable = new ServerTable();
                    serverTable.setTableId(TableIdConstant.FANG_WU_DONG_TABLE_ID);
                    serverTable.setRecords(serverTableRecords);
                    serverTables.add(serverTable);
                    subscriber.onNext(serverTables);
                    subscriber.onCompleted();
                }

            }

            @Override
            public void onError(String msg) {
                subscriber.onError(new Exception(msg));
            }
        };
    }

    /**
     * 根据tableType是栋，套还是人口从LocalServerTable 中拼凑出 ServerTable
     *
     * @param tableType
     * @param subscriber
     */
    private void getServerTable(final String tableType, final Subscriber<? super List<ServerTable>> subscriber) {
        tableDBService.getAllLocalServerTable(new TableNetCallback<List<LocalServerTable>>() {
            @Override
            public void onSuccess(List<LocalServerTable> data) {
                for (LocalServerTable localServerTable : data) {
                    if (tableType.equals(localServerTable.getTableType())) {
                        List<ServerTable> serverTables = new ArrayList<ServerTable>();
                        ServerTable serverTable = new ServerTable();
                        serverTable.setTableId(localServerTable.getTableId());
                        serverTable.setProjectName(localServerTable.getProjectName());
                        serverTables.add(serverTable);
                        subscriber.onNext(serverTables);
                        subscriber.onCompleted();
                    }
                }
            }

            @Override
            public void onError(String msg) {
                subscriber.onError(new Exception(msg));
            }
        });
    }

    /**
     * 根据tableType 进行过滤 LocalServerTableRecord
     *
     * @param data
     * @param tableType 表格类型，参考{@link LocalServerTableRecordConstant}
     * @return
     */
    private ArrayList<ServerTableRecord> getServerTableRecordList(List<LocalServerTableRecord> data, String tableType) {

        ArrayList<ServerTableRecord> serverTableRecords = new ArrayList<ServerTableRecord>();
        for (LocalServerTableRecord localServerTableRecord : data) {
            if (tableType.equals(localServerTableRecord.getTableType())) {
                ServerTableRecord serverTableRecord = convertLocalServerTableRecordToServerTableRecord(localServerTableRecord);

                serverTableRecords.add(serverTableRecord);
            }

        }
        return serverTableRecords;
    }

    /**
     * 将 LocalServerTableRecord 转成 ServerTableRecord
     *
     * @param localServerTableRecord
     * @return
     */
    @NonNull
    private ServerTableRecord convertLocalServerTableRecordToServerTableRecord(LocalServerTableRecord localServerTableRecord) {
        ServerTableRecord serverTableRecord = new ServerTableRecord();
        serverTableRecord.setTaskId(localServerTableRecord.getTaskId());
        serverTableRecord.setRecordId(localServerTableRecord.getRecordId());
        serverTableRecord.setStandardaddress(localServerTableRecord.getStandardAddress());
        serverTableRecord.setName(localServerTableRecord.getName());
        serverTableRecord.setTableId(localServerTableRecord.getTableId());
        serverTableRecord.setTaskName(localServerTableRecord.getTaskName());
        serverTableRecord.setDanweiId(localServerTableRecord.getDanweiId());
        serverTableRecord.setTaoId(localServerTableRecord.getTaoId());
        serverTableRecord.setProjectName(localServerTableRecord.getProjectName());
        serverTableRecord.setDongName(localServerTableRecord.getDongName());
        serverTableRecord.setDongId(localServerTableRecord.getDongId());
        serverTableRecord.setState(localServerTableRecord.getState());

      /*  RealmList<TableItem> items = localServerTableRecord.getItems();
        ArrayList<TableItem> tableItems = new ArrayList<>();
        for (TableItem tableItem : items) {
            tableItems.add(tableItem);
        }
        serverTableRecord.setItems(tableItems);*/


        //根据tableKey 找到 LocalTable
        String tableKey = localServerTableRecord.getTableKey();
        LocalTable2 localTable = getLocalTable2ByTableKey(tableKey);
        if (localTable != null) {
            ArrayList<TableItem> tableItems = convert(localTable);
            serverTableRecord.setItems(tableItems);
        }

        return serverTableRecord;
    }


    /**
     * 将本地保存的表记录项转换为原始表格项集合
     *
     * @param localTable
     * @return
     */
    public static ArrayList<TableItem> convert(LocalTable2 localTable) {
        ArrayList<TableItem> tableItems = new ArrayList<>();
        for (LocalTableItem2 editedTableItem : localTable.getList()) {
            TableItem tableItem = new TableItem();
            tableItem.setDic_code(editedTableItem.getDic_code());
            tableItem.setBase_table(editedTableItem.getBase_table());
            tableItem.setColum_num(editedTableItem.getColum_num());
            tableItem.setControl_type(editedTableItem.getControl_type());
            tableItem.setDevice_id(editedTableItem.getDevice_id());
            tableItem.setField1(editedTableItem.getField1());
            tableItem.setField2(editedTableItem.getField2());
            tableItem.setHtml_name(editedTableItem.getHtml_name());
            tableItem.setValue(editedTableItem.getValue());
            tableItem.setId(editedTableItem.getId());
            tableItem.setIs_edit(editedTableItem.getId_edit());
            tableItem.setIf_hidden(editedTableItem.getIf_hidden());
            tableItem.setIndustry_code(editedTableItem.getIndustry_code());
            tableItem.setIndustry_table(editedTableItem.getIndustry_table());
            tableItem.setRow_num(editedTableItem.getRow_num());
            tableItem.setIs_form_field(editedTableItem.getIs_form_field());
            tableItem.setRegex(editedTableItem.getRegex());
            tableItem.setValidate_type(editedTableItem.getValidate_type());
            tableItem.setFirst_correlation(editedTableItem.getFirst_correlation());
            tableItem.setThree_correlation(editedTableItem.getThree_correlation());
            tableItem.setIf_required(editedTableItem.getIf_required());
            tableItems.add(tableItem);
        }
        return tableItems;
    }


    /**
     * 根据表格键ID获取具体的某表实体记录
     *
     * @param tableKey
     * @return
     */
    public LocalTable2 getLocalTable2ByTableKey(String tableKey) {
        Realm realm = Realm.getDefaultInstance();
        LocalTable2 localTable = realm.where(LocalTable2.class).equalTo("key", tableKey)
                .findFirst();
        LocalTable2 result = null;
        if (localTable != null) {
            result = realm.copyFromRealm(localTable);
        }
        realm.close();
        return result;
    }


    /**
     * 根据dongId 获取本地保存的该任务下套表的数据
     *
     * @param dongId 栋记录的id
     * @return 套记录
     */
    public Observable<List<ServerTable>> getLocalTaoTable(final String dongId, final String taskType) {

        return Observable.create(new Observable.OnSubscribe<List<ServerTable>>() {
            @Override
            public void call(final Subscriber<? super List<ServerTable>> subscriber) {

                if (TextUtils.isEmpty(dongId)) {
                    tableDBService.getAllLocalServerTableRecord(getTaoCallback(subscriber, dongId));
                } else {
                    Map<String,String> map = new LinkedHashMap<String, String>();
                    map.put("dongId",dongId);
                    map.put("taskType",taskType);
                    tableDBService.getLocalServerTableRecordByField(map, getTaoCallback(subscriber, dongId));
                }

            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }




    /**
     * 套列表的回调
     *
     * @param subscriber
     * @param dongId
     * @return
     */
    @NonNull
    private TableNetCallback<List<LocalServerTableRecord>> getTaoCallback(final Subscriber<? super List<ServerTable>> subscriber, final String dongId) {
        return new TableNetCallback<List<LocalServerTableRecord>>() {
            @Override
            public void onSuccess(List<LocalServerTableRecord> data) {

                if (ListUtil.isEmpty(data)) {
                    //返回只包含tableId的ServerTable
                    getServerTable(LocalServerTableRecordConstant.TAO, subscriber);
                } else {
                    List<ServerTable> serverTables = new ArrayList<ServerTable>();
                    ArrayList<ServerTableRecord> serverTableRecords = getServerTableRecordList(data, LocalServerTableRecordConstant.TAO);
                    ServerTable serverTable = new ServerTable();
                    serverTable.setTableId(TableIdConstant.FANG_WU_TAO_TABLE_ID);
                    serverTable.setRecords(serverTableRecords);
                    serverTables.add(serverTable);
                    subscriber.onNext(serverTables);
                    subscriber.onCompleted();
                }
            }

            @Override
            public void onError(String msg) {
                subscriber.onError(new Exception(msg));
            }
        };
    }

    /**
     * 根据套的id 获取本地保存的该任务下单位记录
     *
     * @param recordId 套的id
     * @return 单位记录
     */
    public Observable<List<ServerTable>> getLocalDanweiTable(final String recordId, final boolean ifDong) {

        return Observable.create(new Observable.OnSubscribe<List<ServerTable>>() {
            @Override
            public void call(final Subscriber<? super List<ServerTable>> subscriber) {

                if (ifDong) { //recordId = 栋id
                    Map<String,String> map = new LinkedHashMap<String, String>();
                    map.put("dongId",recordId);
                    tableDBService.getLocalServerTableRecordByField(map, new TableNetCallback<List<LocalServerTableRecord>>() {
                        @Override
                        public void onSuccess(List<LocalServerTableRecord> data) {

                            if (ListUtil.isEmpty(data)) {
                                //返回只包含tableId的ServerTable
                                getServerTable(LocalServerTableRecordConstant.SHI_YOU_DAN_WEI, subscriber);
                            } else {
                                List<ServerTable> serverTables = new ArrayList<ServerTable>();
                                ArrayList<ServerTableRecord> serverTableRecords = getServerTableRecordList(data, LocalServerTableRecordConstant.SHI_YOU_DAN_WEI);
                                ServerTable serverTable = new ServerTable();
                                serverTable.setTableId(TableIdConstant.SHI_TOU_DAN_WEI_TABLE_ID);
                                serverTable.setRecords(serverTableRecords);
                                serverTables.add(serverTable);
                                subscriber.onNext(serverTables);
                                subscriber.onCompleted();
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            subscriber.onError(new Exception(msg));
                        }
                    });
                } else { //recordId = 套id

                    Map<String,String> map = new LinkedHashMap<String, String>();
                    map.put("taoId",recordId);

                    tableDBService.getLocalServerTableRecordByField( map,new TableNetCallback<List<LocalServerTableRecord>>() {
                        @Override
                        public void onSuccess(List<LocalServerTableRecord> data) {

                            if (ListUtil.isEmpty(data)) {
                                //返回只包含tableId的ServerTable
                                getServerTable(LocalServerTableRecordConstant.SHI_YOU_DAN_WEI, subscriber);
                            } else {
                                List<ServerTable> serverTables = new ArrayList<ServerTable>();
                                ArrayList<ServerTableRecord> serverTableRecords = getServerTableRecordList(data, LocalServerTableRecordConstant.SHI_YOU_DAN_WEI);
                                ServerTable serverTable = new ServerTable();
                                serverTable.setTableId(TableIdConstant.SHI_TOU_DAN_WEI_TABLE_ID);
                                serverTable.setRecords(serverTableRecords);
                                serverTables.add(serverTable);
                                subscriber.onNext(serverTables);
                                subscriber.onCompleted();
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            subscriber.onError(new Exception(msg));
                        }
                    });
                }
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 根据taoId 获取本地保存的该任务下人口基本表、流动人口表、境外人口表的数据(共3张表)
     *
     * @param taoId 套的id
     * @return 人口基本表、流动人口表、境外人口表的数据
     */
    public Observable<List<ServerTable>> getLocalRenkouTable(final String taoId) {

        return Observable.create(new Observable.OnSubscribe<List<ServerTable>>() {
            @Override
            public void call(final Subscriber<? super List<ServerTable>> subscriber) {

                final TableDBService tableDBService = new TableDBService(mContext);
                Map<String,String> map = new LinkedHashMap<String, String>();
                map.put("taoId",taoId);

                tableDBService.getLocalServerTableRecordByField(map, new TableNetCallback<List<LocalServerTableRecord>>() {
                    @Override
                    public void onSuccess(List<LocalServerTableRecord> data) {

                        //本地有这三张表的记录
                        Map<String, ServerTable> serverTableMap = new HashMap<String, ServerTable>();
                        Map<String, ArrayList<ServerTableRecord>> serverTableListMap = new HashMap<String, ArrayList<ServerTableRecord>>();
                        //过滤出属于这三张表的记录
                        for (LocalServerTableRecord localServerTableRecord : data) {

                            if (LocalServerTableRecordConstant.REN_KOU_BASIC_INFO.equals(localServerTableRecord.getTableType())
                                    || LocalServerTableRecordConstant.LIU_DONG_REN_KOU.equals(localServerTableRecord.getTableType())
                                    || LocalServerTableRecordConstant.JING_WAI_REN_KOU.equals(localServerTableRecord.getTableType())) {

                                ServerTableRecord serverTableRecord = convertLocalServerTableRecordToServerTableRecord(localServerTableRecord);
                                ArrayList<ServerTableRecord> serverTableRecords1 = serverTableListMap.get(localServerTableRecord.getTableId());

                                if (serverTableRecords1 == null) {
                                    serverTableRecords1 = new ArrayList<ServerTableRecord>();
                                    serverTableRecords1.add(serverTableRecord);
                                    serverTableListMap.put(localServerTableRecord.getTableId(), serverTableRecords1);
                                } else {
                                    serverTableRecords1.add(serverTableRecord);
                                    serverTableListMap.put(localServerTableRecord.getTableId(), serverTableRecords1);
                                }

                                if (serverTableMap.get(localServerTableRecord.getTableId()) == null) {
                                    ServerTable serverTable = new ServerTable();
                                    serverTable.setTableId(localServerTableRecord.getTableId());
                                    serverTable.setProjectName(localServerTableRecord.getProjectName()); //因为之后是根据表的名称判断他们是否『基本人口表』还是『流动人口表』，所以这里必须要传入名称
                                    serverTableMap.put(localServerTableRecord.getTableId(), serverTable);
                                }

                            }
                        }

                        if (serverTableListMap.size() == 0) {
                            //如果本地并没有这三张表的记录
                            //查询三张表的tableId和tableName返回
                            tableDBService.getAllLocalServerTable(new TableNetCallback<List<LocalServerTable>>() {
                                @Override
                                public void onSuccess(List<LocalServerTable> data) {

                                    List<ServerTable> serverTables = new ArrayList<ServerTable>();

                                    for (LocalServerTable localServerTable : data) {
                                        if (LocalServerTableRecordConstant.REN_KOU_BASIC_INFO.equals(localServerTable.getTableType())
                                                || LocalServerTableRecordConstant.LIU_DONG_REN_KOU.equals(localServerTable.getTableType())
                                                || LocalServerTableRecordConstant.JING_WAI_REN_KOU.equals(localServerTable.getTableType())) {

                                            ServerTable serverTable = new ServerTable();
                                            serverTable.setProjectName(localServerTable.getProjectName());
                                            serverTable.setTableId(localServerTable.getTableId());
                                            serverTables.add(serverTable);
                                        }
                                    }

                                    subscriber.onNext(serverTables);
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onError(String msg) {
                                    subscriber.onError(new Exception(msg));
                                }
                            });

                            return;
                        }

                        //有这三张表的记录，将记录归到对应的表下
                        final List<ServerTable> serverTables = new ArrayList<ServerTable>();
                        Set<Map.Entry<String, ServerTable>> entries = serverTableMap.entrySet();
                        for (Map.Entry<String, ServerTable> entry : entries) {
                            String tableId = entry.getKey();
                            ArrayList<ServerTableRecord> serverTableRecords = serverTableListMap.get(tableId);
                            ServerTable serverTable = entry.getValue();

                            //此时的recordId还是 表id + recordId的组合，要去掉表id得到它原来真实的id
                            for (ServerTableRecord serverTableRecord : serverTableRecords) {
                                String replacePart = tableId + "+";
                                String originRecordId = serverTableRecord.getRecordId().replace(replacePart, "");
                                serverTableRecord.setRecordId(originRecordId);
                            }
                            serverTable.setRecords(serverTableRecords);
                            serverTables.add(serverTable);
                        }

                        //还需要判断是否是三张表，可能存在只有一张表，或者只有两张表的情况，这时需要补充另外几张表的基本信息
                        if (serverTables.size() != 3) {

                            final Map<String, ServerTable> existedTableIds = new HashMap<String, ServerTable>();

                            for (ServerTable serverTable : serverTables) {
                                existedTableIds.put(serverTable.getTableId(), serverTable);
                            }

                            tableDBService.getAllLocalServerTable(new TableNetCallback<List<LocalServerTable>>() {
                                @Override
                                public void onSuccess(List<LocalServerTable> data) {

                                    List<ServerTable> finalResult = new ArrayList<ServerTable>();

                                    for (LocalServerTable localServerTable : data) {
                                        if (LocalServerTableRecordConstant.REN_KOU_BASIC_INFO.equals(localServerTable.getTableType())
                                                || LocalServerTableRecordConstant.LIU_DONG_REN_KOU.equals(localServerTable.getTableType())
                                                || LocalServerTableRecordConstant.JING_WAI_REN_KOU.equals(localServerTable.getTableType())) {

                                            if (existedTableIds.get(localServerTable.getTableId()) == null) {
                                                ServerTable newAddedTables = new ServerTable();
                                                newAddedTables.setTableId(localServerTable.getTableId());
                                                newAddedTables.setProjectName(localServerTable.getProjectName());
                                                finalResult.add(newAddedTables);
                                            } else {
                                                //填充表名称，必须
                                                existedTableIds.get(localServerTable.getTableId()).setProjectName(localServerTable.getProjectName());
                                            }
                                        }
                                    }

                                    finalResult.addAll(serverTables);
                                    subscriber.onNext(finalResult);
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onError(String msg) {
                                    subscriber.onError(new Exception(msg));
                                }
                            });
                        } else {
                            subscriber.onNext(serverTables);
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        subscriber.onError(new Exception(msg));
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 根据单位的id 获取本地保存的 从业人员信息
     *
     * @param danweiId 单位的id
     * @return 从业人员信息
     */
    public Observable<List<ServerTable>> getLocalCongyeRenyuanTable(final String danweiId) {

        return Observable.create(new Observable.OnSubscribe<List<ServerTable>>() {
            @Override
            public void call(final Subscriber<? super List<ServerTable>> subscriber) {

                Map<String,String> map = new LinkedHashMap<String, String>();
                map.put("danweiId",danweiId);
                tableDBService.getLocalServerTableRecordByField(map, new TableNetCallback<List<LocalServerTableRecord>>() {
                    @Override
                    public void onSuccess(List<LocalServerTableRecord> data) {


                        if (ListUtil.isEmpty(data)) {
                            //返回只包含tableId的ServerTable
                            getServerTable(LocalServerTableRecordConstant.CONGYE_RENYUAN_INFO, subscriber);
                        } else {
                            List<ServerTable> serverTables = new ArrayList<ServerTable>();
                            ArrayList<ServerTableRecord> serverTableRecords = getServerTableRecordList(data, LocalServerTableRecordConstant.CONGYE_RENYUAN_INFO);
                            ServerTable serverTable = new ServerTable();
                            serverTable.setTableId(TableIdConstant.CONGYE_INFO_TABLE_ID);
                            serverTable.setRecords(serverTableRecords);
                            serverTables.add(serverTable);
                            subscriber.onNext(serverTables);
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        subscriber.onError(new Exception(msg));
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 根据单位的id 获取本地保存的 学生人员信息
     *
     * @param danweiId 单位的id
     * @return 学生人员信息
     */
    public Observable<List<ServerTable>> getLocalXueshengInfoTable(final String danweiId) {

        return Observable.create(new Observable.OnSubscribe<List<ServerTable>>() {
            @Override
            public void call(final Subscriber<? super List<ServerTable>> subscriber) {

                Map<String,String> map = new LinkedHashMap<String, String>();
                map.put("danweiId",danweiId);

                tableDBService.getLocalServerTableRecordByField(map , new TableNetCallback<List<LocalServerTableRecord>>() {
                    @Override
                    public void onSuccess(List<LocalServerTableRecord> data) {

                        if (ListUtil.isEmpty(data)) {
                            //返回只包含tableId的ServerTable
                            getServerTable(LocalServerTableRecordConstant.XUE_SHENG_INFO, subscriber);
                        } else {
                            List<ServerTable> serverTables = new ArrayList<ServerTable>();
                            ArrayList<ServerTableRecord> serverTableRecords = getServerTableRecordList(data, LocalServerTableRecordConstant.XUE_SHENG_INFO);
                            ServerTable serverTable = new ServerTable();
                            serverTable.setTableId(TableIdConstant.XUESHENG_INFO_TABLE_ID);
                            serverTable.setRecords(serverTableRecords);
                            serverTables.add(serverTable);
                            subscriber.onNext(serverTables);
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        subscriber.onError(new Exception(msg));
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 保存下载离线数据的时间
     */
    public void saveSyncTime() {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        sharedPreferencesUtil.setLong("lastSyncTime", System.currentTimeMillis());//本地更新时间
    }

    /**
     * 保存下载过的离线数据
     */
    public void saveOffLineTask(List<OfflineTask> offlineTasks) {

        AMDatabase.getInstance().saveAll(offlineTasks);

    }

     /**
     * 获取下载过的离线数据
     */
    public List<OfflineTask> getOfflineTask() {

        return AMDatabase.getInstance().getQueryAll(OfflineTask.class);
    }

    /**
     * 获取下载过的离线数据
     */
    public List<OfflineTask> getOfflineTaskById(String taskId) {

        return AMDatabase.getInstance().getQueryByWhere(OfflineTask.class,"taskId",taskId);
    }

    /**
     * 获取上次下载离线数据的时间
     */
    public Long getLastSyncTime() {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        return sharedPreferencesUtil.getLong("lastSyncTime", 0l);//最新更新时间
    }
}
