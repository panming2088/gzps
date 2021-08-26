package com.augurit.am.cmpt.widget.popupview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.widget.popupview.bean.PopupBean;
import com.augurit.am.cmpt.widget.popupview.view.GridItemView;
import com.augurit.am.cmpt.widget.popupview.view.PopupStyle;

import java.util.List;
import java.util.Map;


/**
  * @date 创建时间: 2016-08-05
  * @author 创建人:taoerxiang，xuciluan
  * @description 功能描述 : Popup中包含的是GridView,这个是GridView的适配器
  */
public class PopupGridViewAdapter extends BaseAdapter {

   private List<PopupBean> mGridViewDataBeen;

    public Map<Integer,GridItemView> itemMap;

   private Context mContext;
   private PopupStyle mProjectStyle;
   public PopupGridViewAdapter(Context context, List<PopupBean> gridViewDataBeen){
       this.mGridViewDataBeen = gridViewDataBeen;
       this.mContext = context;
   }
 public void setProjectStyle(PopupStyle style){
     this.mProjectStyle = style;
 }

   @Override
   public int getCount() {
       return mGridViewDataBeen.size();
   }

   @Override
   public Object getItem(int position) {
       return null;
   }

   @Override
   public long getItemId(int i) {
       return 0;
   }

   @Override
   public View getView(final int position, View convertView, final ViewGroup viewGroup) {

       final GridItemView item;
       item = new GridItemView(mContext);
       if (mProjectStyle != null){
           //设置图片
           if(mProjectStyle.getDefaultDrawableId() != PopupStyle.INVALID_COLOR){
               //如果不是无效，说明已经赋过新的值
               item.setImageView(mProjectStyle.getDefaultDrawableId());
           }

           //设置文字
           if(mProjectStyle.getProjectTextSize() != PopupStyle.INVALID_COLOR && mProjectStyle.getBackTextSize()>0 ){
               //如果不是无效，说明已经赋过新的值
               item.setTextSize(mProjectStyle.getProjectTextSize());
           }

           if(mProjectStyle.getGridTextColor() != PopupStyle.INVALID_COLOR ){
               //如果不是无效，说明已经赋过新的值
               item.setTextColor(mProjectStyle.getGridTextColor());
           }
       }
       item.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,
               AbsListView.LayoutParams.FILL_PARENT));

      /* if (convertView == null) {
           item = new GridItem(mContext);
           item.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,
                   AbsListView.LayoutParams.FILL_PARENT));
       } else {
           item = (GridItem) convertView;
       }*/


           item.setImgResId(mGridViewDataBeen.get(position).getResId() == 0 ? R.drawable.popupview_shp_grid_item_bg :
                   mGridViewDataBeen.get(position).getResId());

      // item.setChecked(true);
       if (selectedPosition == position){
           item.setChecked(true);
       }else {
           item.setChecked(false);
       }
       item.setTextName(mGridViewDataBeen.get(position).getName());
       /*item.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               boolean isSelected = !mGridViewDataBeen.get(position).isSelected();
               item.setChecked(isSelected);
               mGridViewDataBeen.get(position).setSelected(isSelected);
               notifyDataSetChanged();
               if (mOnIOSPopupItemSelectedListener != null){
                    mOnIOSPopupItemSelectedListener.onItemClick(position);
               }
           }
       });*/
       return item;
   }

   private onIOSPopupItemSelectedListener mOnIOSPopupItemSelectedListener;

   public interface onIOSPopupItemSelectedListener{
       void onItemClick(int position);
   }

   public void setOnIOSPopupItemSelectedListener(onIOSPopupItemSelectedListener onIOSPopupItemSelectedListener) {
       mOnIOSPopupItemSelectedListener = onIOSPopupItemSelectedListener;
   }

    private int selectedPosition;
    public void setSelectedPosition(int position){
        this.selectedPosition = position;
    }
}