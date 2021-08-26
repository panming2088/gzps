package com.augurit.am.cmpt.widget.togglebutton.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.augurit.am.cmpt.R;
import com.github.johnkil.print.PrintView;


public final class MeasureToolView {
    private MeasureToolView() {
    }

    /**
     * @param context             上下文
     * @param color               控件主颜色
     * @param onToggleListener    switch控件切换监听
     * @param clearClickListener  清除按钮单击监听
     * @param cancleClickListener 撤回按钮单击监听
     * @param closeClickListener  关闭按钮单击监听
     * @return
     */
    public static View createView(Context context,
                                  int color,
                                  AMSpringSwitchButton.OnToggleListener onToggleListener,
                                  View.OnClickListener clearClickListener,
                                  View.OnClickListener cancleClickListener,
                                  View.OnClickListener closeClickListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View measureToolLayout = layoutInflater.inflate(R.layout.togglebutton_widget, null);
        AMSpringSwitchButton mySpringSwitchButton = (AMSpringSwitchButton) measureToolLayout.findViewById(R.id.msb);
        PrintView clearIcon = (PrintView) measureToolLayout.findViewById(R.id.clear_icon);
        PrintView cancelIcon = (PrintView) measureToolLayout.findViewById(R.id.cancel_icon);
        PrintView closeIcon = (PrintView) measureToolLayout.findViewById(R.id.close_icon);
        mySpringSwitchButton.setBackRectColor(context.getResources().getColor(color));
        mySpringSwitchButton.setTextUnCheckedColor(context.getResources().getColor(color));
        clearIcon.setIconColor(color);
        cancelIcon.setIconColor(color);
        closeIcon.setIconColor(color);
        mySpringSwitchButton.setOnToggleListener(onToggleListener);
        clearIcon.setOnClickListener(clearClickListener);
        cancelIcon.setOnClickListener(cancleClickListener);
        closeIcon.setOnClickListener(closeClickListener);
        return measureToolLayout;
    }

    public static View createView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View measureToolLayout = layoutInflater.inflate(R.layout.togglebutton_widget, null);
        return measureToolLayout;
    }

    /**
     * @param context 上下文
     * @param color   控件主颜色
     * @return
     */
    public static View createView(Context context,
                                  int color) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View measureToolLayout = layoutInflater.inflate(R.layout.togglebutton_widget, null);
        AMSpringSwitchButton mySpringSwitchButton = (AMSpringSwitchButton) measureToolLayout.findViewById(R.id.msb);
        PrintView clearIcon = (PrintView) measureToolLayout.findViewById(R.id.clear_icon);
        PrintView cancelIcon = (PrintView) measureToolLayout.findViewById(R.id.cancel_icon);
        PrintView closeIcon = (PrintView) measureToolLayout.findViewById(R.id.close_icon);
        mySpringSwitchButton.setBackRectColor(context.getResources().getColor(color));
        mySpringSwitchButton.setTextUnCheckedColor(context.getResources().getColor(color));
        clearIcon.setIconColor(color);
        cancelIcon.setIconColor(color);
        closeIcon.setIconColor(color);
        return measureToolLayout;
    }
}
