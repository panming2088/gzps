package com.augurit.agmobile.mapengine.marker.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.marker.model.Attachment;
import com.augurit.agmobile.mapengine.marker.model.Mark;
import com.augurit.agmobile.mapengine.marker.model.PointStyle;
import com.augurit.agmobile.mapengine.marker.model.SimpleMarkInfo;
import com.augurit.am.cmpt.common.Callback2;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Point;

import java.util.List;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.mark.service
 * @createTime 创建时间 ：2016-12-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-20
 */

public interface IMarkService {

    List<String> getMarkQueryHistory();

    List<SimpleMarkInfo> query(String searchText);

    void saveMarkInfoToDatabase(List<Mark> infos);

    void saveQueryHistory(String seachText);

    void applyEdit(Mark mark,  Callback2<Mark> callback);

    void applyDelete(Mark mark, Callback2<Mark> callback);

    void applyAdd(Mark mark, Callback2<Mark> callback);

    void getAllMark(Context context, MapView mapView, Callback2<List<Mark>> callback);

    void getPointStyleList(Callback2<List<PointStyle>> callback);

   // void queryAllMark(MapView mapView, Callback2<List<Mark>> listener);

    void deleteMark(SimpleMarkInfo simpleMarkInfo);

    void deletePreviousMarkInfo();

   // void getFeatureLayerObjectId(String url, Callback2<Integer> callback);

    void deleteAttachements(Mark mark, List<Attachment> attachments,
                            Callback2<Boolean> callback);

    void closeAllMarkRequest();
}

