package com.augurit.agmobile.mapengine.marker.view;

import com.augurit.agmobile.mapengine.marker.model.MarkStyle;
import com.augurit.am.fw.common.IPresenter;
import com.augurit.agmobile.mapengine.marker.model.Attachment;
import com.augurit.agmobile.mapengine.marker.model.Mark;
import com.augurit.agmobile.mapengine.marker.model.SimpleMarkInfo;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.Graphic;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.marker.view
 * @createTime 创建时间 ：2017-02-25
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-25
 * @modifyMemo 修改备注：
 */

public interface IMarkerPresenter extends IPresenter {

    /********给用户调用的方法********************/
    void startMarker();

    void clearMap();

    void exitMarker();
    /******************************************/

    /**
     * 恢复默认的标注点击事件
     */
    void restoreDefaultMarkListener();

    /**
     * 绘制标注图形结束
     */
    void finishDrawMarker(Geometry geometry);

    void applyAdd(Mark mark);

    /**
     * 显示标注列表
     */
    void showMarkList();

    /**********************搜索事件**************************************/
    List<String> getQueryHistory();

    List<SimpleMarkInfo> query(String key);

    void saveQueryText(String text);

    /**
     * 显示满足搜索条件的标注
     * @param markInfos
     */
    void showSearchResult(List<SimpleMarkInfo> markInfos);

    /**
     * 退出搜索
     */
    void onCloseQuery();

    /**
     * popup的编辑按钮被点击
     * @param mark
     */
    void onCalloutEditButtonClick(Mark mark);

    void applyDelete(Mark mark);

 //   void onMarkListItemClick(Mark mark,int position);

    void applyEdit(Mark mark);

    void deleteAttachments(Mark mark, List<Attachment> attachments);

    Mark getCurrentMark();

    void onGraphicSelected(int graphicId );

    void onGraphicNotFound();

    void applyMarkerLocationChanged(Mark mark);
}
