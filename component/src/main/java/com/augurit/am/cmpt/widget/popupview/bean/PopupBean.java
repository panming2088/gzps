package com.augurit.am.cmpt.widget.popupview.bean;


 /**
   * @date 创建时间: 2016-08-05
   * @author 创建人:taoerxiang
   * @description 功能描述 : Popup能接收的Javabean对象
   */
public class PopupBean {
    private String name;
    private boolean isSelected;
    private int resId;

    public PopupBean(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
