package com.augurit.am.cmpt.widget.popupview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


/**
  * @date 创建时间: 2016-08-05
  * @author 创建人:taoerxiang
  * @description 功能描述 : 正方形
  */
public class SquareLayout extends LinearLayout{
   public SquareLayout(Context context) {
       super(context);
   }

   public SquareLayout(Context context, AttributeSet attrs) {
       super(context, attrs);
   }

   public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
       super(context, attrs, defStyleAttr);
   }

   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

       // Children are just made to fill our space.
       int childWidthSize = getMeasuredWidth()+5;
       int childHeightSize = getMeasuredHeight()+5;
       //高度和宽度一样
       heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
       super.onMeasure(widthMeasureSpec, heightMeasureSpec);

   }
}
