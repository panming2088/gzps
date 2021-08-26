package com.augurit.agmobile.patrolcore.editmap;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
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

public abstract class AbsEditlineEditStateMapListener implements OnSingleTapListener {


    public abstract void setOnGraphicChangedListener(OnGraphicChangedListener onGraphicChangedListener);

    public abstract void highlightComponent(final AMFindResult findResult);

    /**
     * 选中某个点时的操作，用于『选择当前位置』时使用
     * @param point
     */
    public abstract void clickPoint(Point point);
}
