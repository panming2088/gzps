package com.augurit.agmobile.gzps.doorno.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by fairy on 2017/11/30.
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 */

public class BaseRecyleAdapter extends RecyclerView.Adapter<BaseRecyleAdapter.ViewHolder> {
    public List<?> data;
    Map<Integer, Integer> hashMap;

    public BaseRecyleAdapter(List<?> data, Map<Integer, Integer> hashMap) {
        this.data = data;
        this.hashMap = hashMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        ViewDataBinding inflate = DataBindingUtil.inflate(from, viewType, parent, false);
        return new ViewHolder(inflate);
    }

    /**
     * 默认取值 MAP 第一个Key
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        Set<Integer> integers = hashMap.keySet();
        return integers.iterator().next();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Integer varid = hashMap.get(getItemViewType(position));
        holder.dataBinding.setVariable(varid, data.get(position));

        if (onRecycleitemOnClic != null)
            holder.dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecycleitemOnClic.onItemClic(view, position);
                }
            });
    }

    public OnRecycleitemOnClic getOnRecycleitemOnClic() {
        return onRecycleitemOnClic;
    }

    public void setOnRecycleitemOnClic(OnRecycleitemOnClic onRecycleitemOnClic) {
        this.onRecycleitemOnClic = onRecycleitemOnClic;
    }

    OnRecycleitemOnClic onRecycleitemOnClic;

    public interface OnRecycleitemOnClic {
        void onItemClic(View view, int position);
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewDataBinding dataBinding;

        public ViewHolder(ViewDataBinding itemView) {
            super(itemView.getRoot());
            this.dataBinding = itemView;
        }
    }
}
