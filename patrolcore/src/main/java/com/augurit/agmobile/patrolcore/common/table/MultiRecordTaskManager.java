package com.augurit.agmobile.patrolcore.common.table;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTaskRecord;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBCallback;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.event.AddSaveRecordEvent;

import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecord2;
import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.RealmList;

/**
 * 描述：多表任务的数据管理类
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table
 * @createTime 创建时间 ：2017/8/24
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/8/24
 * @modifyMemo 修改备注：
 */

public class MultiRecordTaskManager {
    //上传内容ID记录
   // private List<ClientTaskRecord> recordsList;

    //本地记录
    private RealmList<LocalTaskRecord> localTaskRecordList;
    private Context mContext;
    private String baseUrl = "http://192.168.191.1:8081/";

    private String taskID;
    private String taskName;

    private ServerTableRecord serverTableRecord;
    private LocalServerTableRecord2 localServerTableRecord2;
    private String tableKey; //本地存储数据的主键

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public MultiRecordTaskManager(Context context) {
        this.mContext = context;
       // recordsList = new ArrayList<>();
        localTaskRecordList = new RealmList<>();

        EventBus.getDefault().register(this);
    }

    public MultiRecordTaskManager(Context context, ServerTableRecord serverTableRecord) {
        this.mContext = context;
        this.serverTableRecord = serverTableRecord;
        // recordsList = new ArrayList<>();
        localTaskRecordList = new RealmList<>();

        EventBus.getDefault().register(this);
    }

    public MultiRecordTaskManager(Context context, LocalServerTableRecord2 serverTableRecord) {
        this.mContext = context;
        this.localServerTableRecord2 = serverTableRecord;
        // recordsList = new ArrayList<>();
        localTaskRecordList = new RealmList<>();

        EventBus.getDefault().register(this);
    }

    /**
     * 响应上传记录事件
     * @param event
     */
    /*
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onAddUploadRecordEvent(AddUploadRecordEvent event){
        ClientTaskRecord clientTaskRecord = event.getRecordId();
        recordsList.add(clientTaskRecord);
    }
    */

    /**
     * 响应本地保存记录
     * 因为网络问题而上传不了的记录内容先保存下来
     * @param event
     */
    /*
    @Deprecated
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onAddSaveRecordEvent(AddSaveRecordEvent event){
        //localTaskRecordList.add(event.getLocalTaskRecord());

        tableKey = event.getLocalTaskRecord().getKey();
        if(tableKey == null) return;

        //为原先的数据数据绑定本地存储主键
        serverTableRecord.setTableKey(tableKey);
        List<ServerTableRecord> list = new ArrayList<>();
        list.add(serverTableRecord);
        TableDBService tableDBService = new TableDBService(mContext);
        tableDBService.saveServerTableRecord(list);
    }
    */


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onAddSaveRecordEvent(AddSaveRecordEvent event){
        //localTaskRecordList.add(event.getLocalTaskRecord());

        tableKey = event.getLocalTaskRecord().getKey();
        if(tableKey == null) return;

        //为原先的数据数据绑定本地存储主键
        localServerTableRecord2.setTableKey(tableKey);
        /*
        List<ServerTableRecord> list = new ArrayList<>();
        list.add(serverTableRecord);
        */

        TableDBService tableDBService = new TableDBService(mContext);
        tableDBService.saveLocalServerTableRecord(localServerTableRecord2, new TableDBCallback() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    //上传多表任务
    /**
     * 将任务与记录ID数组绑定一起上传
     */
    /*
    public void uploadTask(){
        if(taskID == null) {
            long time = System.currentTimeMillis();
            taskID = String.valueOf(time);//产生任务ID
        }

        //网络不可行 先保存
        if(!NetworkUtil.isNetworkAvailable()){
            TableDBService tableDBService = new TableDBService(mContext);
            tableDBService.setClientTaskRecordToDB(recordsList);
            return;
        }

        ClientTask clientTask = new ClientTask();
        clientTask.setTaskId(taskID);
        if(standardAddress == null) standardAddress = "测试任务";
        clientTask.setTaskName(standardAddress);
       // clientTask.setTaskName(standardAddress);
        clientTask.setRecordIds(recordsList);
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);
        String url = serverUrl + "rest/multitable/saveTask";

        //上传服务器
        TableNetService tableNetService = new TableNetService(mContext,baseUrl);
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "提示", "正在提交任务");
        tableNetService.uploadTableTask(url, clientTask, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                String result = null;
                try {
                    result = ((ResponseBody) data).string();
                } catch (IOException e) {
                    progressDialog.dismiss();
                    ToastUtil.longToast(mContext, e.getLocalizedMessage());
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                UploadTableTaskResult uploadTableItemResult = gson.fromJson(result, UploadTableTaskResult.class);
                if (uploadTableItemResult.getSuccess().equals("true")) {
                    ToastUtil.shortToast(mContext, "提交成功!");
                }

                saveLocalTask();
            }

            @Override
            public void onError(String msg) {
                ToastUtil.shortToast(mContext, msg);

            }
        });

    }
    */

    /**
     * 保存多表任务
     * 该任务已经上传或者更新到服务器的记录是不保存的
     */
    @Deprecated
    public void saveLocalTask(){
        /*
        if(localTaskRecordList.isEmpty()){
            return;
        }
        if(taskID == null) {
            long time = System.currentTimeMillis();
            taskID = String.valueOf(time);//产生任务ID
        }
        TableDBService tableDBService = new TableDBService(mContext);
        LocalTask localTask = new LocalTask();

        //再保存当前该任务最新的状态
        localTask.setKey(taskID);
        if(standardAddress == null) standardAddress ="测试名字";
        localTask.setName(standardAddress);
        if(localTask.getLocalTaskRecords() != null) {
            localTask.getLocalTaskRecords().addAll(localTaskRecordList);
        }else {
            localTask.setLocalTaskRecords(localTaskRecordList);
        }

        //该任务之前保存的记录(如果有的话)
        LocalTask preLocalTask = tableDBService.getLocalTaskByKey(taskID);
        if(preLocalTask != null){
            localTask.getLocalTaskRecords().addAll(preLocalTask.getLocalTaskRecords());
        }

        tableDBService.setLocalTaskToDB(localTask);
        */
    }

    public void destory(){
        localTaskRecordList.clear();
        localTaskRecordList = null;
        EventBus.getDefault().unregister(this);
    }
}
