package com.augurit.agmobile.gzps.componentmaintenance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.addcomponent.AddComponentFragment2;
import com.augurit.agmobile.gzps.common.base.BaseProblemActivity;
import com.augurit.am.cmpt.permission.PermissionsUtil2;

/**
 * 部件维护界面(修正)
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps
 * @createTime 创建时间 ：17/10/14
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/14
 * @modifyMemo 修改备注：
 */

public class ComponentMaintenanceActivityBase extends BaseProblemActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    AddComponentFragment2 addComponentFragment;

    @Override
    protected String getTab1Title() {
        return "我的修正";
    }

    @Override
    protected String getTab2Title(){
        return "设施选取/定位";
    }

    @Override
    protected void initTabLayout() {

        fragmentManager = getSupportFragmentManager();
        final ComponentMaintenanceListFragment problemListFragment = new ComponentMaintenanceListFragment();
        addComponentFragment  = new AddComponentFragment2();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals(getTab1Title())){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(addComponentFragment).show(problemListFragment);
                    fragmentTransaction.commit();
                } else if(tab.getText().equals(getTab2Title())){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(problemListFragment).show(addComponentFragment);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fg_container, problemListFragment).
                add(R.id.fg_container, addComponentFragment);
        fragmentTransaction.hide(addComponentFragment).show(problemListFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!addComponentFragment.isHidden()) {
            addComponentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed(){
        if(!addComponentFragment.isHidden()){
            addComponentFragment.onBackPressed();
        } else {
            finish();
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
