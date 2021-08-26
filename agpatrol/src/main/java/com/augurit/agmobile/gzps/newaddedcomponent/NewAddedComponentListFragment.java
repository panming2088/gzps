package com.augurit.agmobile.gzps.newaddedcomponent;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.componentmaintenance.ComponentMaintenanceListFragment;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentTypeConstant;
import com.augurit.agmobile.gzps.common.project.ProjectListActivity2;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.am.fw.utils.StringUtil;

/**
 * 新增部件列表界面
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.newaddedcomponent
 * @createTime 创建时间 ：17/10/14
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/14
 * @modifyMemo 修改备注：
 */

public class NewAddedComponentListFragment extends ComponentMaintenanceListFragment {

    @Override
    protected String getComponentType(){
        return ComponentTypeConstant.NEW_ADDED;
    }

    @Override
    public boolean showAddButton(){
        return true;
    }

    @Override
    public void onAddButtonClick(){
        showChoice();
    }

    public void showChoice() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .create();
        View dialog = View.inflate(getActivity(), R.layout.dialog_choose_addcomponent_type, null);
        dialog.findViewById(R.id.iv_add_yinjin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                String componentName = "窨井";
                String featureLayerUrl = LayerUrlConstant.getNewLayerUrlByLayerName(componentName);
                TableViewManager.isEditingFeatureLayer = true;
                TableViewManager.graphic = null;
                TableViewManager.geometry = null;
                TableViewManager.featueLayerUrl = featureLayerUrl;
                addComponent(componentName);
            }
        });
        dialog.findViewById(R.id.iv_add_yushuikou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                String componentName = "雨水口";
                String featureLayerUrl = LayerUrlConstant.getNewLayerUrlByLayerName(componentName);
                TableViewManager.isEditingFeatureLayer = true;
                TableViewManager.graphic = null;
                TableViewManager.geometry = null;
                TableViewManager.featueLayerUrl = featureLayerUrl;
                addComponent(componentName);
            }
        });
        alertDialog.setView(dialog);
        alertDialog.show();
    }

    private void addComponent(String componentName) {
        if (StringUtil.isEmpty(componentName)) {
            return;
        }
        Intent intent = new Intent(getActivity(), ProjectListActivity2.class);
        //        Intent intent = new Intent(getActivity(), AddComponentActivity2.class);
        intent.putExtra("componentName", componentName);
        startActivity(intent);
    }
}
