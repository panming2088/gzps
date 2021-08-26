package com.augurit.am.cmpt.widget.popupview.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.widget.popupview.adapter.PopupGridViewAdapter;
import com.augurit.am.cmpt.widget.popupview.bean.PopupBean;

import java.util.List;


/**
  * @date 创建时间: 2016-08-05
  * @author 创建人:taoerxiang
  * @description 功能描述 : Popup视图
  */
public class PopupView implements AbsListView.MultiChoiceModeListener {

   private GridView mGridView;
   private Button loadBtn;
   private View mRootView;
   private PopupWindow mPop;
   private PopupGridViewAdapter mGridAdapter;
   private List<PopupBean> mGridViewDataBeens;
   private Button mSureBtn;
   private PopupStyle mProjectStyle;
   private Button mBackBtn;
   private TextView mTv_projectList;

   public PopupView(Context context, List<PopupBean> datas, View rootViewOfLayout){
       initView(context);
       initDatas(context,datas);
       this.mRootView = rootViewOfLayout;
       this.mGridViewDataBeens = datas;
   }
   private OnIOSPopupClickListener mOnIOSPopupClickListener;
   public interface OnIOSPopupClickListener{
       void onPopupItemClick(int position);
   }

   public void setOnIOSPopupClickListener(OnIOSPopupClickListener onIOSPopupClickListener) {
       mOnIOSPopupClickListener = onIOSPopupClickListener;
   }

   private void initDatas(Context context, List<PopupBean> datas) {
       mGridAdapter = new PopupGridViewAdapter(context,datas);
       mGridView.setAdapter(mGridAdapter);
       mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               mGridViewDataBeens.get(position).setSelected(
                       !mGridViewDataBeens.get(position).isSelected());
               mGridAdapter.notifyDataSetChanged();
               if (mOnIOSPopupClickListener!= null){
                   mOnIOSPopupClickListener.onPopupItemClick(position);
               }
               mPop.dismiss();

           }
       });
     /*  mGridAdapter.setOnIOSPopupItemSelectedListener(new PopupGridViewAdapter.onIOSPopupItemSelectedListener() {
           @Override
           public void onItemClick(int position) {
               if (mOnIOSPopupItemSelectedListener != null){
                   mOnIOSPopupItemSelectedListener.onItemClick(position);
               }
               mPop.dismiss();
           }
       });*/
   }

   public void show(){
       if (mPop != null){
           mPop.showAsDropDown(mRootView);
       }
   }
   private void initView(Context context) {
       View popContent = View.inflate(context, R.layout.popupview_widget, null);
       mGridView = (GridView) popContent.findViewById(R.id.gv);
       mGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
       mGridView.setMultiChoiceModeListener(this);

       mSureBtn = (Button) popContent.findViewById(R.id.sure);
       mBackBtn = (Button) popContent.findViewById(R.id.back);

       mPop = new PopupWindow(popContent, LinearLayout.LayoutParams.MATCH_PARENT,
               LinearLayout
                       .LayoutParams.MATCH_PARENT, true);
       mPop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
       mPop.setAnimationStyle(R.style.AGMobile_Animation_PopupView);

       mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               mSureBtn.setVisibility(View.VISIBLE);
           }
       });
       mSureBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mPop.dismiss();
           }
       });
       mBackBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mPop.dismiss();
           }
       });

       mTv_projectList = (TextView) popContent.findViewById(R.id.tv_content_main_projectlist);

   }

   @Override
   public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

   }

   @Override
   public boolean onCreateActionMode(ActionMode mode, Menu menu) {
       return true;
   }

   @Override
   public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
       return true;
   }

   @Override
   public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

       return true;
   }

   @Override
   public void onDestroyActionMode(ActionMode mode) {

   }

   public void setIOSPopupStyle(PopupStyle style){
       this.mProjectStyle = style;
        notifyForChange();
   }
   public void notifyForChange(){
       mGridAdapter.setProjectStyle(mProjectStyle);
       //设置”返回“文字样式
       if (mProjectStyle != null){
           //设置返回按钮的背景
           if(mProjectStyle.getBackTextColor() != PopupStyle.INVALID_COLOR){
               //如果不是无效，说明已经赋过新的值
               mBackBtn.setTextColor(mProjectStyle.getBackTextColor());
           }

           //设置返回按钮的文字大小
           if(mProjectStyle.getBackTextSize() != PopupStyle.INVALID_COLOR && mProjectStyle.getBackTextSize()>0 ){
               //如果不是无效，说明已经赋过新的值
               mBackBtn.setTextSize(mProjectStyle.getBackTextSize());
           }
       }

       //设置”专题列表“文字样式
       if (mProjectStyle != null){
           //设置返回按钮的背景
           if(mProjectStyle.getProjectTextColor() != PopupStyle.INVALID_COLOR){
               //如果不是无效，说明已经赋过新的值
               mTv_projectList.setTextColor(mProjectStyle.getBackTextColor());
           }

           //设置返回按钮的文字大小
           if(mProjectStyle.getProjectTextSize() != PopupStyle.INVALID_COLOR && mProjectStyle.getProjectTextSize()>0 ){
               //如果不是无效，说明已经赋过新的值
               mTv_projectList.setTextSize(mProjectStyle.getBackTextSize());
           }
       }

       //设置popup的进入退出动画
       if (mProjectStyle != null){
           //设置返回按钮的背景
           if(mProjectStyle.getPopupAnimateStyle() != PopupStyle.INVALID_COLOR){
               //如果不是无效，说明已经赋过新的值
               mPop.setAnimationStyle(mProjectStyle.getPopupAnimateStyle());
           }

       }

   }

   public void onDestroy(){
       if (mPop.isShowing()){
           mPop.dismiss();
           mPop = null;
       }
         mGridView = null;
         loadBtn  = null;
         mRootView  = null;
         mGridAdapter  = null;
   }
}
