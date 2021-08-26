package com.augurit.agmobile.patrolcore.upload.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.augurit.agmobile.patrolcore.common.action.dao.local.ActionDBLogic;
import com.augurit.agmobile.patrolcore.common.action.model.ActionModel;
import com.augurit.agmobile.patrolcore.common.action.util.ActionNameConstant;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;
import com.augurit.agmobile.patrolcore.search.view.IPatrolSearchView;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.agmobile.patrolcore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：巡查上报历史数据详情查看界面
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol
 * @createTime 创建时间 ：17/3/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/7
 * @modifyMemo 修改备注：
 */

public class ReadTableActivity extends AppCompatActivity {

    private ViewGroup mainView;
    private View btn_save;
    private View btn_apply;
    private TableViewManager tableViewManager;

    private TextView tv_title;
    private String title = "问题详情";

    private ViewGroup ll_save_apply;
    private ViewGroup ll_template;

    private boolean isForTemplate;

    private  ArrayList<TableItem> tableItems;
    private  ArrayList<Photo> photos;
    private ActionDBLogic mLocalMenuStorageDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tableItems = (ArrayList<TableItem>) getIntent().getSerializableExtra("tableitems");
        photos = (ArrayList<Photo>) getIntent().getSerializableExtra("photos");

        isForTemplate = getIntent().getBooleanExtra("isForTemplate",true);

        ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_patrol_table, null);
        mainView = (ViewGroup) view.findViewById(R.id.mainView);
        View ll_bottom = view.findViewById(R.id.ll_bottom);
       // ll_bottom.setVisibility(View.GONE);
        ll_save_apply = (ViewGroup)view.findViewById(R.id.ll_save_apply);
        ll_template = (ViewGroup)view.findViewById(R.id.ll_template);

        if(isForTemplate){
            ll_save_apply.setVisibility(View.GONE);
            ll_template.setVisibility(View.VISIBLE);

            //加入权限控制
            mLocalMenuStorageDao = new ActionDBLogic();
            boolean isVisibility = false;
            List<ActionModel> allMenus = mLocalMenuStorageDao.getMenuItemsForUserId(BaseInfoManager.getUserId(this));
            for(ActionModel model:allMenus){
                if(model.getFeaturecode().equals(ActionNameConstant.UPLOAD)){
                    isVisibility = true;
                }
            }
            if(!isVisibility){
                ll_bottom.setVisibility(View.GONE);
            }
        }else {
            ll_bottom.setVisibility(View.GONE);
        }

        //保存
        btn_save = view.findViewById(R.id.btn_save);
        btn_save.setClickable(true);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableViewManager.saveEdited();
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

        tv_title =(TextView)view.findViewById(R.id.tv_title);
        tv_title.setText(title);

        //存为模板
        ll_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableViewManager.isEditingFeatureLayer = false;
                Intent intent = new Intent(ReadTableActivity.this, ReEditTableActivity.class);
                intent.putExtra(IPatrolSearchView.TABLE_ITEMS, tableItems);
            //    intent.putExtra(IPatrolSearchView.PHOTOS, photos);
                startActivity(intent);
                finish();
            }
        });


      //  tableViewManager = new TableViewManager(this, mainView, false, TableState.READING, tableItems, photos, null);
        tableViewManager = new TableViewManager(this, mainView, false, TableState.READING,
                tableItems, photos, null, null);
        //tableViewManager.setAddCustomTableItemListener(new TestCustomerTableListener());
        setContentView(view);

        //xcl 2017-03-13添加上报的返回按钮事件
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitActivity();
            }
        });
    }

    private void exitActivity() {
        ReadTableActivity.this.finish();
        overridePendingTransition(0, R.anim.out_toptobottom); //xcl 2017-03-13 添加退出动画
//        EventBus.getDefault().post(new ShowBottomBarEvent()); //发送弹出底部栏事件
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
