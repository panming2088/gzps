package com.augurit.agmobile.gzps.uploadfacility.view.feedback;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.BatchInfo;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.fw.utils.ListUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 问题类型，可多选
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadevent.adapter
 * @createTime 创建时间 ：2017-12-11
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-12-11
 * @modifyMemo 修改备注：
 */

public class FilterGroupAdapter extends BaseAdapter{

    private Context mContext;

    private List<BatchInfo.Info> mGroupList;

    private Set<BatchInfo.Info> mSelectedGroupList;

    private OnRecyclerItemClickListener<BatchInfo.Info> mOnItemClickListener;

    public FilterGroupAdapter(Context context){
        this.mContext = context;
        mSelectedGroupList = new HashSet<>();
    }


    public void notifyDataSetChanged(List<BatchInfo.Info> eventTypeList){
        this.mGroupList = eventTypeList;
//        this.mSelectedGroupList.clear();
//        notifyDataSetChanged();
    }

    public void clearSelectedBatch(){
        this.mSelectedGroupList.clear();
    }

    @Override
    public int getCount() {
        if(ListUtil.isEmpty(mGroupList)){
            return 0;
        }
        return mGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGroupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final BatchInfo.Info item = mGroupList.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.gridview_item_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_area_name);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_selected);
        tv.setText(item.getAssignName());
       boolean isContains  = contains(mSelectedGroupList,item);
//        if(mSelectedGroupList.contains(item)){
        if(isContains){
            tv.setBackground(mContext.getResources().getDrawable(R.drawable.corner_color_primary3));
            tv.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, null));
            iv.setVisibility(View.VISIBLE);
        } else {
            tv.setBackground(mContext.getResources().getDrawable(R.drawable.corner_color_write));
            tv.setTextColor(Color.BLACK);
            iv.setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mSelectedGroupList.contains(item)){
                    mSelectedGroupList.remove(item);
                } else {
                    mSelectedGroupList.add(item);
                }
                notifyDataSetChanged();
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(v, position, item);
                }
            }
        });
        return view;
    }

    private boolean contains(Set<BatchInfo.Info> selectedGroupList, BatchInfo.Info item) {
            for(BatchInfo.Info info:selectedGroupList){
                if(info.equals(item)){
                    return true;
                }
        }
        return false;
    }

    public Set<BatchInfo.Info> getSelectedEventTypeList(){
        return mSelectedGroupList;
    }


    public void setSelectedGroupList(String[] eventTypeCodes){
        mSelectedGroupList.clear();
        if(ListUtil.isEmpty(mGroupList)){
            return;
        }
        for(BatchInfo.Info item : mGroupList){
            for(String code : eventTypeCodes){
                if(item.getAssignId().equals(code)){
                    mSelectedGroupList.add(item);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public Set<BatchInfo.Info> getSelectedGroupList() {
        return mSelectedGroupList;
    }

    public void setSelectedGroupList(Set<BatchInfo.Info> selectedGroupList) {
        this.mSelectedGroupList.clear();
        this.mSelectedGroupList.addAll(selectedGroupList);
//        this.mSelectedGroupList = selectedGroupList;
        notifyDataSetChanged();
    }
}
