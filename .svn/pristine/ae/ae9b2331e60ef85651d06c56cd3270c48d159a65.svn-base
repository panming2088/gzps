package com.augurit.agmobile.gzps.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by xiaoyu on 2018/3/12.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected List<T> data;
    protected Context context;

    public BaseListAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}