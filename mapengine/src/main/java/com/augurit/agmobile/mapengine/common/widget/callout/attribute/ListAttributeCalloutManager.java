package com.augurit.agmobile.mapengine.common.widget.callout.attribute;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.augurit.agmobile.mapengine.common.utils.AttributeUtil;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.widget.callout.SimpleListAdapter;
import com.augurit.am.fw.utils.ListUtil;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.tasks.identify.IdentifyResult;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 组合ListAttributeCallout和CandidateContainer展示点击地图之后的属性展示
 *
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.am.map.arcgis.widget.callout
 * @createTime 创建时间 ：2016-12-28
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-28 18:51
 */
public class ListAttributeCalloutManager {
    public static final String INTENT_FIELDNAME_KEY = "INTENT_FIELDNAME_KEY";//显示的名称的键名
    public static final String INTENT_ATTRIBUTE_KEY = "INTENT_ATTRIBUTE_KEY";//显示的属性的键名
    public static final String INTENT_HIGHLIGHT_KEY = "INTENT_HIGHLIGHT_KEY";//高亮的文字的键名

    protected MapView mMapView;

    protected ListAttributeCallout mListAttributeCallout;
    protected CandidateContainer mCandidateContainer;

    /**
     * ListAttributeCalloutManager的构造函数，传入属性过滤器IFilterCondition
     *
     * @param mapView
     * @param candidateContainer 候选列表
     * @param iFilterCondition   属性过滤器
     */
    public ListAttributeCalloutManager(@NonNull MapView mapView, @NonNull CandidateContainer candidateContainer, IFilterCondition iFilterCondition) {
        init(mapView, candidateContainer, iFilterCondition);
    }

    /**
     * ListAttributeCalloutManager的构造函数，使用属性过滤器IFilterCondition
     *
     * @param mapView
     * @param candidateContainer 候选列表
     */
    public ListAttributeCalloutManager(@NonNull MapView mapView, @NonNull CandidateContainer candidateContainer) {
        this(mapView, candidateContainer, new DefaultFilterCondition());
    }

    /**
     * 初始化气泡和候选列表
     *
     * @param mapView
     * @param candidateContainer
     * @param iFilterCondition   属性筛选器，主要赋予给ListAttributeCallout进行过滤
     */
    protected void init(@NonNull MapView mapView, @NonNull CandidateContainer candidateContainer, IFilterCondition iFilterCondition) {
        mMapView = mapView;
        mCandidateContainer = candidateContainer;
        mListAttributeCallout = new ListAttributeCallout(mMapView.getContext(), mMapView, iFilterCondition);
    }

    /**
     * 显示更多的点击事件
     *
     * @param agFindResult 要被显示在详情界面的属性
     */
    public void moreClick(String highlightText,AMFindResult agFindResult) {
        if (mCandidateContainer == null || agFindResult == null) {
            return;
        }
        Map<String, Object> attributes = null;
        try {
            attributes = mListAttributeCallout.getAfterFilterAttributes(agFindResult);
            if (mCandidateContainer.getMoreDetailPrentViewGroup() == null) {//手机界面的时候跳转
                Intent intent = new Intent(mMapView.getContext(), AttributeListActivity.class);
                ListOrderedMap listOrderedMap = ListOrderedMap.listOrderedMap(attributes);
                AttributeListActivity.attributes = listOrderedMap;
                intent.putExtra(INTENT_ATTRIBUTE_KEY, listOrderedMap);
                intent.putExtra(INTENT_FIELDNAME_KEY, agFindResult.getValue());
                mMapView.getContext().startActivity(intent);
            } else {//平板界面时在当前MoreDetail中显示
                SimpleListAdapter moreDetailAdapter = getListAdapterForPad(highlightText, attributes);
                mCandidateContainer.updateMoreDetail(agFindResult.getValue(), moreDetailAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    protected SimpleListAdapter getListAdapterForPad(String highlightText, Map<String, Object> attributes) {
        return new SimpleListAdapter(mMapView.getContext(), attributes,highlightText);
    }

    /**
     * 给气泡赋值，并展示出来
     * @param highLightText 需要高亮的字眼
     * @param agFindResult   气泡上的属性值
     * @param onShowListener 气泡显示时的额外事件，比如高亮处理
     */
    public void showCallout(final String highLightText, final AMFindResult agFindResult, OnShowListener onShowListener) {
        mListAttributeCallout.setData(agFindResult,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moreClick(highLightText,agFindResult);
                    }
                });
        //移动地图中心以及移动气泡至Geometry的中心点
        Point centerPoint = centerMap(agFindResult.getGeometry());

        //展示气泡
        if (!mListAttributeCallout.isShowing()) {
            mListAttributeCallout.show(centerPoint);
        }

        //高亮处理
        if (onShowListener != null) {
            onShowListener.doShow(agFindResult);
        }
    }

    public boolean isCallOutShowing() {
        if (mListAttributeCallout != null) {
            return mListAttributeCallout.isShowing();
        }
        return false;
    }

    /**
     * 给候选列表赋值，并展示出来
     *
     * @param agFindResultArray 候选数据组
     * @param onShowListener    主要赋予列表点击事件，比如高亮处理
     */
    protected void showCandidateContainer(final String highlightText, AMFindResult[] agFindResultArray, final OnShowListener onShowListener) {
        AgFindResultAdapter resultListAdapter = new AgFindResultAdapter(mMapView.getContext(), agFindResultArray);
        resultListAdapter.setOnItemClickListener(new AgFindResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, AMFindResult agFindResult) {  // 列表项点击
                showCallout(highlightText,agFindResult, onShowListener);
            }
        });
        if (agFindResultArray.length > 1) {
            mCandidateContainer.updateResultList(agFindResultArray.length, resultListAdapter);
        }
    }

    /**
     * 显示查询中气泡
     *
     * @param point
     */
    public void showLoading(MotionEvent point) {
        final Point mapPoint = mMapView.toMapPoint(point.getX(), point.getY());//当前点击坐标转换程地图坐标
        mListAttributeCallout.showLoading(mapPoint);
    }

    /**
     * 显示“查询无数据”气泡
     *
     * @param point
     */
    public void showSearchError(MotionEvent point) {
        final Point mapPoint = mMapView.toMapPoint(point.getX(), point.getY());//当前点击坐标转换程地图坐标
        mListAttributeCallout.showSearchError(mapPoint);
    }

    /**
     * 传入AgFindResult格式的数组，展示气泡和候选列表(通用所有场景，只要转成AgFindResult数组就行)
     *
     * @param agFindResultArray 要显示的所有候选数据
     * @param onShowListener    气泡显示时的额外事件，比如高亮处理
     */
    public void show(String highlightText,AMFindResult[] agFindResultArray, OnShowListener onShowListener) {
        if (ListUtil.isEmpty(agFindResultArray)) {
            hide();//零条属性时隐藏气泡和候选列表
            return;
        }

        //给气泡填充第一条记录的值且展示气泡
        AMFindResult firstResult = agFindResultArray[0];
        showCallout(highlightText,firstResult, onShowListener);

        //给候选列表填充数据且展示出来
        showCandidateContainer(highlightText,agFindResultArray, onShowListener);
    }

    /**
     * 传入IdentifyResult格式的数组，展示气泡和候选列表(一般用于Identify点查)
     *
     * @param identifyResultArray 要显示的所有候选数据
     * @param onShowListener      气泡显示时的额外事件，比如高亮处理
     */
    public void show(IdentifyResult[] identifyResultArray, OnShowListener onShowListener) {
        show(null,AttributeUtil.covertIdentifyResultToAgFindResult(identifyResultArray), onShowListener);
    }

    /**
     * 地图中心与气泡均移到Geometry的中心
     *
     * @param geometry
     */
    protected Point centerMap(Geometry geometry) {
        if (geometry == null) {
            throw new NullPointerException("targetGeometry");
        }
        Point centerPoint = caculateCenterGeometry(geometry);
        mMapView.centerAt(centerPoint, true);
        mListAttributeCallout.moveTo(centerPoint);
        return centerPoint;
    }

    /**
     * 计算Geometry中心点
     *
     * @param geometry
     * @return
     */
    protected Point caculateCenterGeometry(Geometry geometry) {
        Point centerPoint = null;
        if (geometry.getType() == Geometry.Type.POLYGON) {
            centerPoint = GeometryUtil.getPolygonCenterPoint((Polygon) geometry);
        } else if (geometry.getType() == Geometry.Type.POLYLINE) {
            centerPoint = GeometryUtil.getLineCenterPoint((Polyline) geometry);
        } else {
            centerPoint = (Point) geometry;
        }
        return centerPoint;
    }

    /**
     * 隐藏气泡和候选列表
     */
    public void hide() {
        mListAttributeCallout.dismiss();
        mCandidateContainer.hide();
    }

    /**
     * 显示气泡和候选列表时的额外动作（比如高亮处理）
     */
    public interface OnShowListener {
        void doShow(AMFindResult agFindResult);
    }

    public void setListAttributeCallout(ListAttributeCallout listAttributeCallout) { //xcl 2017-04-27 动态改变气泡
        mListAttributeCallout = listAttributeCallout;
    }
}
