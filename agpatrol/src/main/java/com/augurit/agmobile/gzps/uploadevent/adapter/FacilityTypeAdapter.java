package com.augurit.agmobile.gzps.uploadevent.adapter;

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
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.List;

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

public class FacilityTypeAdapter extends BaseAdapter{

    private Context mContext;

    private List<DictionaryItem> mFacilityTypeList;

    private int selectedPosition = -1;

    private boolean enable = true;

    private OnRecyclerItemClickListener<DictionaryItem> mOnItemClickListener;

    public FacilityTypeAdapter(Context context){
        this.mContext = context;
    }


    public void notifyDataSetChanged(List<DictionaryItem> eventTypeList){
        this.mFacilityTypeList = eventTypeList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(ListUtil.isEmpty(mFacilityTypeList)){
            return 0;
        }
        return mFacilityTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFacilityTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DictionaryItem dictionaryItem = mFacilityTypeList.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.gridview_item_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_area_name);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_selected);
        tv.setText(dictionaryItem.getName());

        if(selectedPosition == position){
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
                if(!enable || "排水口".equals(dictionaryItem.getName())){
                    return;
                }
                selectedPosition = position;

                notifyDataSetChanged();
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(v, position, dictionaryItem);
                }
            }
        });
        return view;
    }

    public void selectItem(String code){
        if(ListUtil.isEmpty(mFacilityTypeList)
                || StringUtil.isEmpty(code)){
            return;
        }
        for(int i=0; i<mFacilityTypeList.size(); i++){
            DictionaryItem dictionaryItem = mFacilityTypeList.get(i);
            if(dictionaryItem.getCode().equals(code)){
                selectedPosition = i;
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void selectItemByName(String name){
        if(ListUtil.isEmpty(mFacilityTypeList)
                || StringUtil.isEmpty(name)){
            return;
        }
        for(int i=0; i<mFacilityTypeList.size(); i++){
            DictionaryItem dictionaryItem = mFacilityTypeList.get(i);
            if(dictionaryItem.getName().equals(name)){
                selectedPosition = i;
                break;
            }
        }
        notifyDataSetChanged();
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(null, selectedPosition, mFacilityTypeList.get(selectedPosition));
        }
    }

    public void setEnable(boolean enable){
        this.enable = enable;
    }


    public void setOnItemClickListener(OnRecyclerItemClickListener<DictionaryItem> onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(null, selectedPosition, mFacilityTypeList.get(selectedPosition));
        }
    }
}
