package com.augurit.agmobile.mapengine.marker.view;

import com.augurit.agmobile.mapengine.marker.model.Mark;

/**
 * 描述：标注列表点击事件监听
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.marker.view
 * @createTime 创建时间 ：2017-02-06
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-02-06
 */

public interface OnMarkListClickListener {

    void onClick(int position, Mark selectedMark);
}
