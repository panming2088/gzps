package com.augurit.agmobile.patrolcore.upload.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.augurit.agmobile.patrolcore.common.ShowBottomBarEvent;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;
import com.augurit.agmobile.patrolcore.common.table.util.TemplateHelper;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.agmobile.patrolcore.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 描述：巡查上报信息再次采集编辑界面
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：
 * @createTime 创建时间 ：17/3/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/7
 * @modifyMemo 修改备注：
 */
public class ReEditTableActivity extends AppCompatActivity {
    private ViewGroup mainView;
    private View btn_save;
    private View btn_apply;
    private TableViewManager tableViewManager;
    private String projectId;
    private String url;
    private String tableKey;
    private String tableName; //行业表名

    private boolean isFromTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_re_edit_table);

        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");
        tableKey = intent.getStringExtra("tableKey");
        tableName = intent.getStringExtra("tableName");//行业表名
        ArrayList<TableItem> tableItems = (ArrayList<TableItem>) getIntent().getSerializableExtra("tableitems");
        ArrayList<Photo> photos = (ArrayList<Photo>) getIntent().getSerializableExtra("photos");

        isFromTemplate = getIntent().getBooleanExtra("isFromTemplate",false);

        if(projectId == null){
            //测试
          //  projectId = "732c6873-369c-4d84-9716-1f611d5449da";
            projectId = tableItems.get(0).getDevice_id();
        }
        String serverUrl = BaseInfoManager.getBaseServerUrl(this);
        url = serverUrl +"rest/report/rptform";

        ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_re_edit_table,null);
        mainView = (ViewGroup) view.findViewById(R.id.mainView);

        //保存
        btn_save = view.findViewById(R.id.btn_save);
        btn_save.setClickable(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableKey == null) {
                    tableViewManager.saveEdited();
                }else if (tableName == null){
                    tableViewManager.saveEdited(tableKey);
                }else {
                    tableViewManager.saveEdited(tableKey,tableName); //保存行业表名
                }
                //exitActivity();
            }
        });

        //上报
        btn_apply = view.findViewById(R.id.btn_apply);
        btn_apply.setClickable(true);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableViewManager.uploadEdit();
                // exitActivity();
            }
        });


        /*
        Map<String,List<Photo>> listMap = new HashMap<>();
        for(Photo photo : photos){
            if(!listMap.containsKey(photo.getType())) /////getTag
            {
                List<Photo> photoList = new ArrayList<>();
               ///// listMap.put(photo.getType(),photoList);
            }
        }

        for(Photo photo : photos){
            listMap.get(photo.getType()).add(photo);

        }
        */

        //动态表单实例
        tableViewManager = new TableViewManager(this, mainView, false, TableState.REEDITNG, tableItems, photos, projectId,tableKey,tableName);
        //tableViewManager.setAddCustomTableItemListener(new TestCustomerTableListener());

        //模板化处理
        if(isFromTemplate){
            TemplateHelper.template(tableViewManager);
        }else {

        }
        setContentView(view);

        //xcl 2017-03-13添加上报的返回按钮事件
        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitActivity();
            }
        });
    }

    private void exitActivity() {
        ReEditTableActivity.this.finish();
        overridePendingTransition(0, R.anim.out_toptobottom); //xcl 2017-03-13 添加退出动画
        EventBus.getDefault().post(new ShowBottomBarEvent()); //发送弹出底部栏事件
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
        exitActivity();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
