package com.augurit.am.cmpt.widget.searchview.util.adapter;
/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.searchview.util.adapter
 * @createTime 创建时间 ：17/4/11
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/4/11
 * @modifyMemo 修改备注：
 */


import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

public abstract class OnItemTouchListenerAdapter implements RecyclerView.OnItemTouchListener {

    private static final String TAG = "OnItemTouchListenerAdapter";

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
