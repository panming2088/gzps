package com.augurit.agmobile.mapengine.common.widget.callout;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.widget.callout
 * @createTime 创建时间 ：2016-12-04
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-24 加入字段的高亮
 */

public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.ListViewHolder>{

    protected Context mContext;
    protected List<String> keys = new ArrayList<>();
    protected List<String> values = new ArrayList<>();
    protected String mHighLightText; //需要高亮的字眼

    public SimpleListAdapter(Context context, Map<String,Object> attributes) {
        mContext = context;
        initDatas(attributes);
    }

    public SimpleListAdapter(Context context,Map<String,Object> attributes ,String hightText) {
        mContext = context;
        initDatas(attributes);
        mHighLightText = hightText;
    }

    public void setHighLightText(String highLightText) {
        mHighLightText = highLightText;
    }

    private void initDatas(Map<String, Object> attributes) {
        keys.clear();
        values.clear();
        if(attributes == null){
            return;
        }
        Set<Map.Entry<String, Object>> entries = attributes.entrySet();
        for (Map.Entry<String, Object> entry: entries){
            keys.add(entry.getKey());
            if (entry.getValue() == null){
                values.add("<字段为空>");
            }else {
                values.add(entry.getValue().toString());
            }
        }
    }

    @Override
    public SimpleListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleListAdapter.ListViewHolder(View.inflate(mContext, R.layout.item_listcallout_big, null));
    }

    @Override
    public void onBindViewHolder(SimpleListAdapter.ListViewHolder holder, int position) {
        if (holder instanceof SimpleListAdapter.ListViewHolder) {
            SimpleListAdapter.ListViewHolder viewHolder = holder;
            viewHolder.tv_key.setText(keys.get(position)+":");
            String result = values.get(position) + ":";
            //xcl 2017-02-22  加入对某字段进行高亮的功能
            if (!TextUtils.isEmpty(mHighLightText) && result.contains(mHighLightText)){
                SpannableStringBuilder spannable = new SpannableStringBuilder(result);
                int i = result.indexOf(mHighLightText);
                spannable.setSpan(new ForegroundColorSpan(Color.RED),i, i + mHighLightText.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.tv_value.setText(spannable);
            }else {
                viewHolder.tv_value.setText(values.get(position));
            }
            // viewHolder.tv_value.setText(values.requestLocation(position));
        }
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    protected class ListViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_key;
        public TextView tv_value;

        public ListViewHolder(View itemView) {
            super(itemView);
            tv_key = (TextView) itemView.findViewById(R.id.tv_listcallout_key);
            tv_value = (TextView) itemView.findViewById(R.id.tv_listcallout_value);
        }
    }

}