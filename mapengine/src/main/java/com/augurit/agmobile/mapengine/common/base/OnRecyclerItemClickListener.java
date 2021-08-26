package com.augurit.agmobile.mapengine.common.base;

import android.support.annotation.Keep;
import android.view.View;

/**
 * 通用的RecyclerView中的点击事件
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.common.base
 * @createTime 创建时间 ：2017-02-17
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-17
 * @modifyMemo 修改备注：
 * @version 1.0
 */

@Keep
public interface OnRecyclerItemClickListener<T> {

    void onItemClick(View view, int position, T selectedData);

    void onItemLongClick(View view, int position, T selectedData);
}
