package com.augurit.agmobile.mapengine.common.widget.callout.attribute;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.R;
import com.augurit.agmobile.mapengine.common.widget.callout.ListCalloutAdapter;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.android.map.Callout;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.widget
 * @createTime 创建时间 ：2016-11-16
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-16
 */

public class ListAttributeCallout {

    protected IFilterCondition mIFilterCondition;
    protected Callout mCallout;
    protected Context mContext;
    protected boolean ifEnableShow = true;


    public ListAttributeCallout(Context context, MapView mapView, IFilterCondition iFilterCondition) {
        mIFilterCondition = iFilterCondition;
        mCallout = mapView.getCallout();
        this.mContext = context;
    }

    public ListAttributeCallout(Context context, MapView mapView) {
        this(context, mapView, new DefaultFilterCondition());
    }

    public IFilterCondition getIFilterCondition() {
        return mIFilterCondition;
    }

    public void setData(View view) {
        ifEnableShow = true;
        mCallout.setContent(view);
        mCallout.setMaxHeightDp(350);
        mCallout.setMaxWidthDp(350);
        mCallout.setStyle(R.xml.callout_style);
    }

    public Map<String, Object> getAfterFilterAttributes(AMFindResult agFindResult) {
        Map<String, Object> attributes = agFindResult.getAttributes();
        attributes = mIFilterCondition.filter(attributes);
        return attributes;
    }

    public void setData(AMFindResult agFindResult, View.OnClickListener showMoreClickListener) {
        if (agFindResult == null) {
            return;
        }
        ifEnableShow = true;
        Map<String, Object> attributes = getAfterFilterAttributes(agFindResult);
        ListCalloutAdapter adapter;
        //        attributes.remove(result.getDisplayFieldName());
        if (ListUtil.isEmpty(attributes)) { //只显示标题
            ifEnableShow = false;
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.listcallout_text_view, null);
            TextView tv_onlyTitle = (TextView) view1.findViewById(R.id.tv_listcallout_title);
            tv_onlyTitle.setText(agFindResult.getValue());
            mCallout.setContent(view1);
         /*   mCallout.setMaxHeightDp(150);
            mCallout.setMaxWidthDp(150);*/
            mCallout.setStyle(R.xml.callout_style);
        } else {  //可以显示属性

            ViewGroup view = getListCalloutView();
            /*view.setMinimumHeight(300);
            view.setMinimumWidth(300);*/
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            if (TextUtils.isEmpty(agFindResult.getValue().trim())) {
                tv_title.setVisibility(View.GONE);
            } else {
                tv_title.setText(agFindResult.getValue());
                tv_title.setVisibility(View.VISIBLE);
            }
            TextView tv_moreAttr = (TextView) view.findViewById(R.id.tv_moreattribute);
            RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_listcallout_attributelist);
            if (attributes.size() > 1) {
                //如果大于1条，那么显示“更多属性”
                Map<String, Object> newAttribute = new ListOrderedMap<>();
                Set<Map.Entry<String, Object>> entries = attributes.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    if (newAttribute.size() <= 1) {
                        newAttribute.put(entry.getKey(), entry.getValue());
                    } else {
                        break;
                    }
                }
                adapter = new ListCalloutAdapter(mContext, newAttribute);
                if (showMoreClickListener != null) {
                    tv_moreAttr.setOnClickListener(showMoreClickListener);
                }
            } else {
                tv_moreAttr.setVisibility(View.GONE);
                adapter = new ListCalloutAdapter(mContext, attributes);
            }
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(mContext));
            rv.setItemAnimator(new DefaultItemAnimator());

           // ListViewUtil.setListViewBasedOnChildren(view);
            mCallout.setContent(view);
          /*  mCallout.setMaxHeightDp(350);
            mCallout.setMaxWidthDp(350);*/
       /* CalloutStyle style = new CalloutStyle();
        style.setBackgroundColor(Color.WHITE);
        mCallout.setStyle(style);*/
            // 设置Callout样式
            mCallout.setStyle(getCallout_style());
        }
    }

    protected int getCallout_style() {
        return R.xml.callout_style;
    }

    protected ViewGroup getListCalloutView() {
        return (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.view_listcallout, null);
    }


    /*public void showMoreAttributes(IContainerView moreAttributeContainer, HashMap<String, Object> finalAttributes) {
        if (moreAttributeContainer != null) {
            View moreView = View.inflate(mContext, R.layout.activity_callout_attributelist, null);
            moreView.findViewById(R.id.iv_attributelist_back).setVisibility(View.GONE);
            TextView tv_title = (TextView) moreView.findViewById(R.id.tv_title);
            RecyclerView rv = (RecyclerView) moreView.findViewById(R.id.rv_listcallout_attributelist);
            SimpleListAdapter adapter;

            adapter = new SimpleListAdapter(mContext, finalAttributes);
            tv_title.setText(AttributeUtil.getDisplayName(mContext, "", finalAttributes));
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(mContext));
            rv.setItemAnimator(new DefaultItemAnimator());
            moreAttributeContainer.removeAllViews();
            moreAttributeContainer.addView(moreView);
            moreAttributeContainer.showContainer();
        } else {
            Intent intent = new Intent(mContext, AttributeListActivity.class);
            intent.putExtra(INTENT_ATTRIBUTE_KEY, finalAttributes);
            intent.putExtra(INTENT_FIELDNAME_KEY, AttributeUtil.getDisplayName(mContext, "", finalAttributes));
            mContext.startActivity(intent);
        }
    }*/


    /**
     * 在锚点位置显示Callout
     *
     * @param anchor 锚点
     */
    public void show(Point anchor) {
        if (mCallout != null) {
            if (ifEnableShow) {
                mCallout.show(anchor);
            }
        }
    }

    public void setEnableShow() {
        ifEnableShow = true;
    }


    /**
     * 是否显示
     *
     * @return Callout显示状态
     */
    public boolean isShowing() {
        return mCallout.isShowing();
    }

    /**
     * 隐藏Callout
     */
    public void dismiss() {
        if (mCallout != null) {
            mCallout.hide();
        }
    }

    /**
     * 显示正在加载中
     */
    public void showLoading(Point anchor) {
        View view = View.inflate(mContext, R.layout.view_listcallout_loading, null);
        mCallout.setContent(view);
        mCallout.setMaxHeight(250);
        mCallout.setMaxWidthDp(250);
        // 设置Callout样式
        mCallout.setStyle(R.xml.callout_style);
        mCallout.show(anchor);
    }

    /**
     * 显示查询失败
     */
    public void showSearchError(Point anchor) {
        View view = View.inflate(mContext, R.layout.view_listcallout_error, null);
        mCallout.setContent(view);
        mCallout.setMaxHeight(250);
        mCallout.setMaxWidthDp(250);
        // 设置Callout样式
        mCallout.setStyle(R.xml.callout_style);
        mCallout.show(anchor);
    }

    public void moveTo(Point newAnchor) {
        mCallout.move(newAnchor);
    }

    /**
     * 显示查询无数据
     */
    public void showEmptyResult(Point anchor) {
        View view = View.inflate(mContext, R.layout.view_listcallout_emptyresult, null);
        mCallout.setContent(view);
        mCallout.setMaxHeight(250);
        mCallout.setMaxWidthDp(250);
        // 设置Callout样式
        mCallout.setStyle(R.xml.callout_style);
        mCallout.show(anchor);
    }

    public interface OnListCalloutItemClick {
        void onItemClick(int position);
    }

}
