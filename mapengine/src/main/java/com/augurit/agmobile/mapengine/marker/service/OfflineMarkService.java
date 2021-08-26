package com.augurit.agmobile.mapengine.marker.service;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;


import com.augurit.agmobile.mapengine.common.utils.wktutil.model.LineString;
import com.augurit.agmobile.mapengine.marker.model.Attachment;
import com.augurit.agmobile.mapengine.marker.model.LocalMark;
import com.augurit.agmobile.mapengine.marker.model.Mark;
import com.augurit.agmobile.mapengine.marker.util.MarkConstant;
import com.augurit.agmobile.mapengine.marker.util.MarkUtil;
import com.augurit.am.cmpt.common.Callback2;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.marker.service
 * @createTime 创建时间 ：2017-04-12
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-12
 * @modifyMemo 修改备注：
 */

public class OfflineMarkService extends MarkService {


    public OfflineMarkService(Context context) {
        super(context);
       // mLocalDatabaseMarkDao = new LocalDatabaseMarkDao();
    }


    @Override
    public void applyEdit(Mark mark,Callback2<Mark> callback) {

        LocalMark localMark = transFromMarkToLocalMark(mark);
        localMark.setId(mark.getId());
        try {
            mLocalDatabaseMarkDao.updateMark(localMark);
            callback.onSuccess(mark);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail(e);
        }
    }

    @Override
    public void applyDelete(Mark mark,Callback2<Mark> callback) {
        LocalMark localMark = new LocalMark();
        localMark.setId(mark.getId());
        mLocalDatabaseMarkDao.deleteMark(localMark);
        callback.onSuccess(mark);
    }

    @Override
    public void applyAdd(Mark mark,Callback2<Mark> callback) {
        List<LocalMark> marks = mLocalDatabaseMarkDao.getMarks();
        LocalMark localMark = transFromMarkToLocalMark(mark);
         //id 数据库自动增加，不需要设置
        try {
            mark.setId(marks.size());
            mLocalDatabaseMarkDao.addMark(localMark);
            callback.onSuccess(mark);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail(e);
        }
    }

    @NonNull
    private LocalMark transFromMarkToLocalMark(Mark mark) {
        LocalMark localMark = new LocalMark();
        if(mark.getCreateDate() == null){
            mark.setCreateDate(System.currentTimeMillis());
        }
        localMark.setCreateDate(mark.getCreateDate());
        localMark.setGeometry(mark.getGeometry());
        localMark.setMarkName(mark.getMarkName());
        localMark.setMarkMemo(mark.getMarkMemo());
        localMark.setCreatePerson("userid");
        switch (mark.getGeometry().getType()){
            case POINT:
                String styleStr = mark.getPointDrawableName();
                if (styleStr != null){
                    localMark.setPointDrawableName(styleStr);
                }
                localMark.setInColor("#"); //进行填充假数据
                localMark.setLineColor("#");
                localMark.setLineWidth(0);
                break;
            case POLYLINE:
            case LINE:
                String polylineColor = "#"+ Integer.toHexString(((LineSymbol)mark.getSymbol()).getColor()); //获取到线的颜色
                int lineWidth = (int) ((LineSymbol)mark.getSymbol()).getWidth(); //获取到线的宽度
                if (polylineColor != null){
                    localMark.setLineColor(polylineColor);
                }
                if (lineWidth != 0){
                    localMark.setLineWidth(lineWidth);
                }

                localMark.setInColor("#");//进行填充假数据
                localMark.setPointDrawableName("*");
                break;
            case POLYGON:
                String lineColor = "#"+ Integer.toHexString(((FillSymbol)mark.getSymbol()).getOutline().getColor());
                String inColor = "#"+ Integer.toHexString(((FillSymbol)mark.getSymbol()).getColor());
                if (lineColor != null){
                    localMark.setLineColor(lineColor);
                }
               if (inColor != null){
                   localMark.setInColor(inColor);
               }
                localMark.setLineWidth(5);
                localMark.setPointDrawableName("*");//进行填充假数据
                break;
        }
        return localMark;
    }

    @Override
    public void getAllMark(Context context, MapView mapView, Callback2<List<Mark>> callback) {
        List<LocalMark> localMarks = mLocalDatabaseMarkDao.getMarks();
        List<Mark> marks = new ArrayList<>();
        for (LocalMark localMark : localMarks){
            Mark mark  = new Mark();
            mark.setCreateDate(localMark.getCreateDate());
            mark.setGeometry(localMark.getGeometry());
            mark.setMarkName(localMark.getMarkName());
            mark.setMarkMemo(localMark.getMarkMemo());
            mark.setId(localMark.getId());
            mark.setCreatePerson(localMark.getCreatePerson());
            Symbol symbol = null;
            switch (localMark.getGeometry().getType()){
                case POINT:
                    symbol = MarkUtil.getPointSymbolFromLocalMark(context, localMark);
                    mark.setPointDrawableName(localMark.getPointDrawableName());
                    break;
                case LINE:
                case POLYLINE:
                    symbol = MarkUtil.getLineSymbolFromLocalMark(localMark);
                    break;
                case POLYGON:
                    symbol = MarkUtil.getPolygonSymbolFromLocalMark(localMark);
                    break;
            }
            mark.setSymbol(symbol);
            marks.add(mark);
        }
        //进行逆序，最后添加的在上面
        Collections.reverse(marks);
       /* for (Mark mark: localMarks){
            Symbol defaultSymbol = getDefaultSymbol(mark.getGeometry().getType());
            mark.setSymbol(defaultSymbol);
        }*/
        callback.onSuccess(marks);
    }



    @Override
    public void deleteAttachements(Mark mark, List<Attachment> attachments, Callback2<Boolean> callback) {
        for (Attachment attachment : attachments) {
            boolean contains = mark.getAttachments().contains(attachment);
            if (contains) {
                mark.getAttachments().remove(attachment);
            }
        }
        callback.onSuccess(true);
    }

    @Override
    public void closeAllMarkRequest() {

    }
}
