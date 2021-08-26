package com.augurit.agmobile.mapengine.marker.util;


import com.augurit.agmobile.mapengine.marker.model.Mark;

import java.util.Comparator;


/**
 * 进行标注的创建日期进行比较标注的大小
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.marker.util
 * @createTime 创建时间 ：2017-02-06
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-06
 */


public class MarkCompator implements Comparator<Mark> {
    @Override
    public int compare(Mark lhs, Mark rhs) {
        if (lhs.getCreateDate() > rhs.getCreateDate()){
            return -1;
        }else {
            return 1;
        }
    }
}
