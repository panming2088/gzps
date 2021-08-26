package com.augurit.am.cmpt.widget.popupview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.augurit.am.cmpt.R;


/**
  * @date 创建时间: 2016-08-05
  * @author 创建人:taoerxiang
  * @description 功能描述 : Popup中包含的是GridView，这个是GridView中的子Item视图
  */
public class GridItemView extends RelativeLayout {

   private Context mContext;
   private boolean mChecked;
   private ImageView mImgView = null;
   private ImageView mSecletView = null;
   private TextView tv;
   public GridItemView(Context context) {
       this(context, null, 0);
   }

   public GridItemView(Context context, AttributeSet attrs) {
       this(context, attrs, 0);
   }

   public GridItemView(Context context, AttributeSet attrs, int defStyle) {
       super(context, attrs, defStyle);

       mContext = context;
       LayoutInflater.from(mContext).inflate(R.layout.popupview_griditem, this);
       mImgView = (ImageView) findViewById(R.id.gv_iv);
       mSecletView = (ImageView) findViewById(R.id.select);
       tv = (TextView) findViewById(R.id.gv_tv);
   }


   public void setChecked(boolean checked) {

       mChecked = checked;
//        setBackgroundDrawable(checked ? getResources().getDrawable(
//                R.drawable.background) : null);
       mSecletView.setVisibility(checked ? View.VISIBLE : View.GONE);
   }


   public boolean isChecked() {

       return mChecked;
   }


   public void toggle() {

       setChecked(!mChecked);
   }

   public void setImgResId(int resId) {
       if (mImgView != null) {
           mImgView.setBackgroundResource(resId);
       }
   }

   public void setTextName(String s) {
       tv.setText(s);
   }


   public void setImageView(int drawable){
       mImgView.setBackgroundResource(drawable);
   }
   public void setTextSize(int textSize){
       tv.setTextSize(textSize);
   }
   public void setTextColor(int textColor){
       tv.setTextColor(textColor);
   }
}
