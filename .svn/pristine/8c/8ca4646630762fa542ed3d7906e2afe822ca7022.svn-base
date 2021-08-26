package com.augurit.agmobile.patrolcore.survey.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.ControlType;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;
import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;
import com.augurit.agmobile.patrolcore.survey.util.SurveyConstant;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * 只读界面
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey
 * @createTime 创建时间 ：17/8/28
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/28
 * @modifyMemo 修改备注：
 */

public class CommonReadTableActiivty extends AppCompatActivity {

    private ArrayList<TableItem> tableItems;
    private ViewGroup mainView;


    protected TableViewManager tableViewManager;
    protected String formProjectId;
    protected String tableName;
    protected String patrolcode;
    protected String taskId;
    protected String taskName;
    protected String tableKey;
    protected String dirId;
    protected String recordId;  //记录id
    protected String recordName;//记录名称


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_commonreadtable);

        getBundleData();

        initView();


        loadTable();
    }

    protected void initView() {
        //设置标题
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(tableName);

        mainView = (ViewGroup) findViewById(R.id.mainView);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected void getBundleData() {

        tableName = getIntent().getStringExtra(SurveyConstant.BundleKey.TABLE_NAME); //表单名称
        patrolcode = getIntent().getStringExtra(SurveyConstant.BundleKey.PATROL_CODE); //工单编号

        taskId = getIntent().getStringExtra(SurveyConstant.BundleKey.TASK_ID);
        taskName = getIntent().getStringExtra(SurveyConstant.BundleKey.TASK_NAME);//任务名称
        tableKey = getIntent().getStringExtra(SurveyConstant.BundleKey.TABLE_KEY);//tableKey，用于本地
        dirId = getIntent().getStringExtra(SurveyConstant.BundleKey.DIR_ID);
        recordName = getIntent().getStringExtra(SurveyConstant.BundleKey.RECORD_NAME);

        tableItems = (ArrayList<TableItem>) getIntent().getSerializableExtra(SurveyConstant.BundleKey.TABLE_ITEM);
        recordId = getIntent().getStringExtra(SurveyConstant.BundleKey.RECORD_ID);


        ServerTableRecord serverTableRecord = (ServerTableRecord) getIntent().getSerializableExtra(SurveyConstant.BundleKey.RECORD);

        if (serverTableRecord != null) {
            tableName = serverTableRecord.getProjectName();
            taskId = serverTableRecord.getTaskId();
            taskName = serverTableRecord.getStandardaddress(); //优先采用通过TASK_NAME传递过来的任务名称，因为记录中的任务名称有可能是简要的，而不是完整的
            tableKey = serverTableRecord.getTableKey();
            recordId = serverTableRecord.getRecordId();
            tableItems = serverTableRecord.getItems();
            recordName = serverTableRecord.getName();
            tableKey = serverTableRecord.getTableKey();
        }
    }




    protected void loadTable() {
        //进行拼接图片的url
        List<Photo> photoList = null;
        for (TableItem tableItem : tableItems) {

            if (tableItem.getControl_type().equals(ControlType.IMAGE_PICKER)) {
                String field1 = tableItem.getField1();
                if (!TextUtils.isEmpty(tableItem.getValue())) {
                    photoList = new ArrayList<>();
                    String[] photoNames = tableItem.getValue().split("\\|");

                    for (String photoName : photoNames) {
                        //进行拼接完整的url
                        String photoUrl = BaseInfoManager.getBaseServerUrl(this) + "img/" + photoName;
                        Photo photo = new Photo();
                        photo.setPhotoPath(photoUrl);
                        photo.setField1(field1);
                        photo.setPhotoName(photoName);
                        photoList.add(photo);
                    }
                }
            }
        }

       /* if (ServerTableRecordConstant.UNUPLOAD.equals(recordState)) { //本地保存状态下
            TableDataManager tableDataManager = new TableDataManager(this);
            TableDBService tableDBService = new TableDBService(this);
            LocalServerTableRecord2 localServerTableRecord2 = tableDBService.getLocalServerTableRecordByRecordId(recordId);
            if (localServerTableRecord2 != null){
                String tableKey = localServerTableRecord2.getTableKey();
                LocalTable localTable = tableDBService.getEditedTableItemsByTableKey(tableKey);
                photoList = tableDataManager.getPhotoFormDB(localTable.getKey());
            }
        }*/

        tableViewManager = new TableViewManager(this, mainView, false, TableState.READING,
                tableItems, photoList, null, null);
    }




    /**
     * 主要用于拍照、打开照片、地图浏览等返回Activity的刷新操作
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
//        tableViewManager.onActivityResult(requestCode,resultCode,data);
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
