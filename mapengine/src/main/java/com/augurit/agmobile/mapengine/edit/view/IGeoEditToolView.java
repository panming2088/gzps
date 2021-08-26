package com.augurit.agmobile.mapengine.edit.view;

import android.content.Context;
import android.view.ViewGroup;


/**
 * 提供几何编辑工具界面操作
 *
 * Created by guokunhu on 16/9/20.
 */

public interface IGeoEditToolView {

    void addGeoEditTools(Context context, ViewGroup parentView);

    void setAllBtnVisible(int isVisible);

    void removeGeoEditTools();

    void setMoveAllBtnVisible(boolean isVisible);

}
