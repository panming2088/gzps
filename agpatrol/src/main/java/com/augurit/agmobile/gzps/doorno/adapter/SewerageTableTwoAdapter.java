package com.augurit.agmobile.gzps.doorno.adapter;

import android.view.View;


import com.augurit.agmobile.gzps.GVItemSewerageTableData;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiaoyu on 2018/4/4.
 */

public class SewerageTableTwoAdapter extends BaseRecyleAdapter {
    private Set<Integer> selectPosition = new HashSet<>();


    public void setSelectPosition(Set<Integer> selectPosition) {
        this.selectPosition = selectPosition;
    }
    public SewerageTableTwoAdapter(List<?> data, Map<Integer, Integer> hashMap, int position) {
        super(data, hashMap);
        selectPosition.add(position);

    }

    public SewerageTableTwoAdapter(List<?> data, Map<Integer, Integer> hashMap) {
        super(data, hashMap);
        selectPosition.add(0);

    }

    public void setCurSelectPos(int pos){
        selectPosition.clear();
        selectPosition.add(pos);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        GVItemSewerageTableData gvItemSewerageTableData = (GVItemSewerageTableData) holder.dataBinding;
        if (selectPosition.contains(position)) {
            gvItemSewerageTableData.ivSelected.setVisibility(View.VISIBLE);
        } else {
            gvItemSewerageTableData.ivSelected.setVisibility(View.GONE);
        }

        System.out.println("===position=====" + position);
    }

    boolean isadds = false;

    public void setIsadds(boolean isadds) {
        this.isadds = isadds;
    }

    public void onItemClic(View view, int position) {
        System.out.println("===position=====" + position);

        if (!isadds && selectPosition.size() != 0) {
            Iterator<Integer> iterator = selectPosition.iterator();
            Integer next = iterator.next();
            selectPosition.remove(next);
            notifyItemChanged(next);
        }
        if (isadds && selectPosition.contains(position))
            selectPosition.remove(position);
        else
            selectPosition.add(position);
        notifyItemChanged(position);
    }


    public void clearsele() {
        selectPosition.clear();
    }

    /**
     * 获取 当前选中项 集合
     *
     * @return
     */
    public Set<Integer> getSelectPosition() {
        return selectPosition;
    }
}
