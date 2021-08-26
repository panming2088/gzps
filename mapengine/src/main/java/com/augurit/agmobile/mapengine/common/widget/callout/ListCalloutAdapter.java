package com.augurit.agmobile.mapengine.common.widget.callout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * @package 包名 ：com.augurit.am.map.arcgis.widget
 * @createTime 创建时间 ：2016-11-16
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-16
 */

public class ListCalloutAdapter extends RecyclerView.Adapter<ListCalloutAdapter.ListViewHolder>{

    private Context mContext;
    private List<String> keys = new ArrayList<>();
    private List<String> values = new ArrayList<>();


    public ListCalloutAdapter(Context context, Map<String,Object> attributes) {
        mContext = context;
        initDatas(attributes);
    }

    private void initDatas(Map<String, Object> attributes) {
        keys.clear();
        values.clear();
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
    public ListCalloutAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListCalloutAdapter.ListViewHolder(View.inflate(mContext, R.layout.item_listcallout, null));
    }

    @Override
    public void onBindViewHolder(ListCalloutAdapter.ListViewHolder holder, int position) {
        if (holder instanceof ListCalloutAdapter.ListViewHolder) {
            ListCalloutAdapter.ListViewHolder viewHolder = holder;
            viewHolder.tv_key.setText(keys.get(position)+":");
            viewHolder.tv_value.setText(values.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_key;
        TextView tv_value;

        ListViewHolder(View itemView) {
            super(itemView);
            tv_key = (TextView) itemView.findViewById(R.id.tv_listcallout_key);
            tv_value = (TextView) itemView.findViewById(R.id.tv_listcallout_value);
        }
    }

}
