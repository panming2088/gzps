package com.augurit.agmobile.patrolcore.editmap;

import android.content.Context;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/11/6
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/6
 * @modifyMemo 修改备注：
 */

public abstract class AbsEditLineReEditStateMapListener extends MapOnTouchListener {

    public AbsEditLineReEditStateMapListener(Context context, MapView view) {
        super(context, view);
    }

    /**
     * 当移动位置发生变化时的回调
     * @param onGraphicChangedListener
     */
    public abstract void setOnGraphicChangedListener(OnGraphicChangedListener onGraphicChangedListener);

    /**
     * 选中某个点时的操作，用于『选择当前位置』时使用
     * @param point
     */
    public abstract void clickPoint(Point point);
}
