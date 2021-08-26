package com.augurit.agmobile.patrolcore.common.table.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.table.MultiRecordTaskManager;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.event.AddSaveRecordEvent;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;
import com.augurit.agmobile.patrolcore.localhistory.util.ConvertTableUtils;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecord2;
import com.augurit.agmobile.patrolcore.survey.util.SurveyConstant;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 描述：本地保存任务数据再次编辑
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.view
 * @createTime 创建时间 ：2017/9/5
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/9/5
 * @modifyMemo 修改备注：
 */
@Deprecated
public class EditLocalTableActivity extends AppCompatActivity {
    private String recordId;
    private TableDBService tableDBService;
    private LocalServerTableRecord2 serverTableRecord;
    private String tableKey;
    private TableViewManager tableViewManager;
    private ViewGroup mContentView;
    private TextView tv_title;
    private List<TableItem> tableItemList;
    private List<Photo> photoList;
    private String formProjectId;
    private static final int NEXTLINK_REQUEST_CODE = 0x111;
    private MultiRecordTaskManager multiRecordTaskManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_table);
        EventBus.getDefault().register(this);
        recordId = getIntent().getStringExtra(SurveyConstant.BundleKey.RECORD_ID);
        tv_title = (TextView) findViewById(R.id.tv_title);


        mContentView = (ViewGroup) findViewById(R.id.mainView);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadData();
        refresh();
    }

    private void loadData(){
        tableDBService = new TableDBService();
        serverTableRecord = tableDBService.getLocalServerTableRecordByRecordId(recordId);

        tableKey= serverTableRecord.getTableKey();
        //获取本地保存的记录数据
        LocalTable localTable = tableDBService.getEditedTableItemsByTableKey(tableKey);
        tableItemList = ConvertTableUtils.convert(localTable);
        TableDataManager tableDataManager = new TableDataManager(this);
        photoList = tableDataManager.getPhotoFormDB(localTable.getKey());

        formProjectId = serverTableRecord.getTableId();

     //   multiRecordTaskManager = new MultiRecordTaskManager(this, serverTableRecord);

    }


    private void refresh(){
        if(serverTableRecord.getName() != null){
            tv_title.setText(serverTableRecord.getName());
        }

        tableViewManager = new TableViewManager(this, mContentView, false, TableState.REEDITNG, tableItemList, photoList, formProjectId, tableKey, null);

        if (!TextUtils.isEmpty(recordId)) {
            tableViewManager.setRecordId(recordId);
        }

        if (!TextUtils.isEmpty(serverTableRecord.getTaskId())) {
            tableViewManager.setTaskId(serverTableRecord.getTaskId());
        }

        /*
        if (!TextUtils.isEmpty(patrolcode)) {
            tableViewManager.setPatrolCode(patrolcode);
        }
        */

        if (!TextUtils.isEmpty(serverTableRecord.getTaskName())) {
            tableViewManager.setAddressName(serverTableRecord.getTaskName());//任务名称，在番禺四标四实项目中任务 = 地址
        }

        final Callback1 callback1 = new Callback1() {
            @Override
            public void onCallback(Object o) {
                //删掉已经上传记录,并且刷新本地保存界面
                if(tableKey != null) {
                    TableDBService tableDBService = new TableDBService(EditLocalTableActivity.this);
                    tableDBService.deleteLocalServerTableRecord(serverTableRecord);
                    EventBus.getDefault().post(new RefreshLocalTaskEvent());
                    finish();
                }

            }
        };

        //上传
        findViewById(R.id.ll_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableViewManager.uploadEditMultiWithUserName(callback1);//xcl 8.31 传递的参数加多一个username
            }
        });

        //保存
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tableKey == null) {
                    //初次编辑保存
                    tableViewManager.saveEdited();
                } else {
                    //再次编辑保存
                    tableViewManager.saveEdited(tableKey);
                }

                finish();
            }
        });

    }



    /**
     * 主要用于拍照、打开照片、地图浏览等返回Activity的刷新操作
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == NEXTLINK_REQUEST_CODE) {
            if (resultCode == 0x111) {
                finish();
            }
            return;
        }
        tableViewManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        if (multiRecordTaskManager != null) {
            multiRecordTaskManager.destory();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onAddSaveRecordEvent(AddSaveRecordEvent event){
        //localTaskRecordList.add(event.getLocalTaskRecord());

        tableKey = event.getLocalTaskRecord().getKey();
        if(tableKey == null) return;

        /*
        //为原先的数据数据绑定本地存储主键
        localServerTableRecord.setTableKey(tableKey);
        /*
        List<ServerTableRecord> list = new ArrayList<>();
        list.add(serverTableRecord);
        */
        /*
        TableDBService tableDBService = new TableDBService(this);
        tableDBService.saveLocalServerTableRecord(localServerTableRecord, new TableDBCallback() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String msg) {

            }
        });
        */
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
