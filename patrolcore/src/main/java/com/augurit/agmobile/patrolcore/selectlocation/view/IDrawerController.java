package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime 创建时间 ：17/8/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/1
 * @modifyMemo 修改备注：
 */

public interface IDrawerController {

    void openDrawer(OnDrawerOpenListener listener);

    void closeDrawer();

    /**
     * 获取drawer容器，等同于onOpened(View drawerView)回调中的drawerView；
     * @return
     */
    ViewGroup getDrawerContainer();

    interface OnDrawerOpenListener{
        /**
         * 在判断
         * @param drawerView
         */
        void onOpened(View drawerView);
    }
}
