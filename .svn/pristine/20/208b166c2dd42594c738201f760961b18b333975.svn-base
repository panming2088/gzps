package com.augurit.agmobile.gzps.common.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.common.editmap.NoMapSelectLocationEditStateTableItemView2;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.RefreshLocalEvent;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;
import com.augurit.am.cmpt.common.base.BaseInfoManager;

import org.greenrobot.eventbus.EventBus;

/**
 * 跟{@link com.augurit.agmobile.patrolcore.upload.view.EditTableActivity}区别在于：修改了地图选点控件
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：
 * @createTime 创建时间 ：17/3/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/7
 * @modifyMemo 修改备注：
 */

public class EditTableActivity2 extends BaseActivity {

    private ViewGroup mainView;
    private View btn_save;
    private View btn_apply;
    private TableViewManager tableViewManager;
    private String projectId = "732c6873-369c-4d84-9716-1f611d5449da";
    private String projectName = "问题填报";
    private String tableName =  ""; //行业表名称
//    private String url = "http://192.168.30.27:8088/agweb/rest/agformc/columnBydivi/732c6873-369c-4d84-9716-1f611d5449da";

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");
        projectName = intent.getStringExtra("projectName");
        tableName = intent.getStringExtra("tableName") ;//xcl 2017-08-14 获取行业表名称
        if(projectId == null){
            //测试
            projectId = "732c6873-369c-4d84-9716-1f611d5449da";
        }
        String serverUrl = BaseInfoManager.getBaseServerUrl(this);
        url = serverUrl +"rest/report/rptform";

        ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_patrol_table,null);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_title);
        tv_name.setText(projectName);

        ViewGroup ll_template = (ViewGroup)view.findViewById(R.id.ll_template);
        ll_template.setVisibility(View.GONE);
        mainView = (ViewGroup) view.findViewById(R.id.mainView);
        setContentView(view);
        //保存
        btn_save = findViewById(R.id.btn_save);
        btn_save.setClickable(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableViewManager.saveEdited();
                //exitActivity();

                EventBus.getDefault().post(new RefreshLocalEvent());
            }
        });
        /*
        findViewById(R.id.iv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableViewManager.saveEdited();
               // exitActivity();
                EventBus.getDefault().post(new RefreshLocalEvent());
            }
        });

        findViewById(R.id.btn_save_to_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableViewManager.saveEdited();
               // exitActivity();
                EventBus.getDefault().post(new RefreshLocalEvent());
            }
        });
        */


        //上报
        btn_apply = findViewById(R.id.btn_apply);
        btn_apply.setClickable(true);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableViewManager.uploadEdit();
                //exitActivity();
            }
        });
        /*
        findViewById(R.id.iv_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableViewManager.uploadEdit();
                //exitActivity();
            }
        });

        findViewById(R.id.btn_upload_to_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableViewManager.uploadEdit();
                //exitActivity();
            }
        });
            */

        //xcl 2017-03-13添加上报的返回按钮事件
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //exitActivity();
                showAlertDialog();
            }

        });

        tableViewManager = new TableViewManager(this,mainView,true, TableState.EDITING,url,projectId,tableName);
        /**
         * 修改地图选点控件
         */
        tableViewManager.changeSelectLocationItemView(new NoMapSelectLocationEditStateTableItemView2(this));
      //  tableViewManager.setAddCustomTableItemListener(new TestCustomerTableListener());


       /* //xcl 2017-03-13添加上报的返回按钮事件
        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitActivity();
            }
        });*/


       // String url = serverUrl + "rest/agformc/columnBydivi/0254fc2f-909f-4394-8c9a-67117a446c75";
      //   url = serverUrl + "rest/agformc/columnBydivi/327f55e8-40a7-4745-992d-79a6fe2e33be";
       // String url = serverUrl + "rest/agformc/columnBydivi/732c6873-369c-4d84-9716-1f611d5449da";


    }

    private void exitActivity() {
        EditTableActivity2.this.finish();
        overridePendingTransition(0, R.anim.out_toptobottom); //xcl 2017-03-13 添加退出动画
        //EventBus.getDefault().post(new ShowBottomBarEvent()); //发送弹出底部栏事件
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
        tableViewManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }
    private void showAlertDialog(){
        new AlertDialog.Builder(this)
                .setMessage("确定退出当前界面吗？")
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                exitActivity();
            }
        }).show();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
