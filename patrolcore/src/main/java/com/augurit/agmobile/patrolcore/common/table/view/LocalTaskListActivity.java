package com.augurit.agmobile.patrolcore.common.table.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTask;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTaskRecord;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetService;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.localhistory.util.ConvertTableUtils;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecord2;
import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;
import com.augurit.agmobile.patrolcore.survey.util.SurveyConstant;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地未上传任务记录列表
 */
@Deprecated
public class LocalTaskListActivity extends AppCompatActivity {
    protected XRecyclerView xRecyclerView;
    private LocalStoreTaskAdapter localStoreTaskAdapter;
    private TableDBService tableDBService;
    private TableNetService tableNetService;
    private List<LocalTask> localTasks;
    private String baseUrl = "http://192.168.191.1:8081/";

    private List<ServerTableRecord> serverTableRecordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_task_list);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView)findViewById(R.id.tv_title)).setText("待上传");
        ((TextView)findViewById(R.id.tv_return)).setText("主页");
        findViewById(R.id.tv_return).setVisibility(View.VISIBLE);

        EventBus.getDefault().register(this);

        tableDBService= new TableDBService(this);
        tableNetService = new TableNetService(this,baseUrl);
        xRecyclerView = (XRecyclerView)findViewById(R.id.rv_tasklist);
        localStoreTaskAdapter = new LocalStoreTaskAdapter(new ArrayList<LocalServerTableRecord2>());
        xRecyclerView.setAdapter(localStoreTaskAdapter);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setItemAnimator(new DefaultItemAnimator());
        xRecyclerView.setLoadingMoreEnabled(false); //不允许加载更多
        xRecyclerView.setPullRefreshEnabled(false); //不允许更新
        //xRecyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL_LIST));
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh();
            }

            @Override
            public void onLoadMore() {
               // loadMore();
            }
        });
        localStoreTaskAdapter.setOnStoreTaskClickListener(new OnRecyclerItemClickListener<LocalServerTableRecord2>() {
            @Override
            public void onItemClick(View view, int position, LocalServerTableRecord2 selectedData) {
               // showDialogForUpload(selectedData,position);
                LocalServerTableRecord2 serverTableRecord = selectedData;
                //跳转到具体的表格内容
               // String tableKey = serverTableRecord.getTableKey();
              //  String formProjectId = serverTableRecord.getTableId();
                String recordId = serverTableRecord.getRecordId();
                Intent intent = new Intent(LocalTaskListActivity.this,EditLocalTableActivity.class);
                intent.putExtra(SurveyConstant.BundleKey.RECORD_ID,recordId);
                startActivity(intent);


            }

            @Override
            public void onItemLongClick(View view, int position, LocalServerTableRecord2 selectedData) {

            }
        });

        initData();


    }

    @Deprecated
    private void showDialogForUpload(final LocalTask localTask, final int position){
        new AlertDialog.Builder(LocalTaskListActivity.this).setTitle("应用提示:")
                .setMessage("是否上传该项?")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       uploadLocalTask(localTask,position);
                    }
                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }


    private void initData(){
      //  localTasks = tableDBService.getLocalTaskFromDB();
        List<LocalServerTableRecord2> list = tableDBService.getAllLocalServerTableRecord();
        /*
        serverTableRecordList = new ArrayList<>();
        for(ServerTableRecord item : list){
            if(item.getTableKey() != null){
                serverTableRecordList.add(item);
            }
        }
        */
        localStoreTaskAdapter.notifyDatasetChanged(list);

    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRefreshLocalTaskEvent(RefreshLocalTaskEvent event){
       initData();
    }

    /**
     * 上传本地记录的任务
     * @param localTask
     */
    @Deprecated
    public void uploadLocalTask(final LocalTask localTask, final int position){
        final ProgressDialog progressDialog = ProgressDialog.show(this, "提示", "正在上传任务数据");
        String taskId = localTask.getKey();

        //如果该任务之前已经上传成功部分数据了,则该list不为空
      //  List<ClientTaskRecord> list = tableDBService.getClientTaskRecordFromDB(taskId);

        //获取记录项ID数值 主要是表格ID的集合
        List<LocalTaskRecord> localTaskRecordList = localTask.getLocalTaskRecords();

        //根据记录项去把每一项记录内容拿下来
        Map<String,List<TableItem>> map = new HashMap<>();

        //记录ID与项目类型ID(projectId)映射关系
        //若是服务器已有的记录,则tablekey是记录ID (recordId),否则tableKey只是本地产生的存储键而已
        Map<String,String> map2 = new HashMap<>();
        for(LocalTaskRecord item : localTaskRecordList){
            String tableKey = item.getKey();
            LocalTable localTable = tableDBService.getEditedTableItemsByTableKey(tableKey);
            String projectId = localTable.getId();
            List<TableItem> tableItemList = ConvertTableUtils.convert(localTable);
            map.put(tableKey,tableItemList);
            map2.put(tableKey,projectId);
        }

        String serverUrl = BaseInfoManager.getBaseServerUrl(this);
        String url = serverUrl + "rest/multitable/saveAllRecords";
        tableNetService.uploadLocalStoreTask(url, taskId,  map,map2, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }


                localStoreTaskAdapter.getDataList().remove(position-1);
                deleteLocalTask(localTask);
                localStoreTaskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(LocalTaskListActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 上传成功后删除保存的对应任务项
     * @param localTask
     */
    public void deleteLocalTask(LocalTask localTask){
        //获取记录项ID数值 主要是表格ID的集合
        List<LocalTaskRecord> localTaskRecordList = localTask.getLocalTaskRecords();
        //遍历删除该任务所涉及的记录
        for(LocalTaskRecord item : localTaskRecordList){
            String tableKey = item.getKey();
            //再删除具体任务记录数据
            tableDBService.deleteEditedTableItemsFromBD(tableKey,null);
        }
        tableDBService.deleteLocalTaskFromDB(localTask);

    }
    /*
    private ArrayList<TableItem> convert(LocalTable localTable) {
        ArrayList<TableItem> tableItems = new ArrayList<>();
        for (LocalTableItem editedTableItem : localTable.getList()) {
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
            tableItem.setIf_required(editedTableItem.getIf_required());
            tableItems.add(tableItem);
        }
        return tableItems;
    }
    */

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}
