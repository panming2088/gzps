package com.augurit.agmobile.gzps.uploadevent.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.fw.utils.ListUtil;
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

public class SimpleTypeAdapter extends BaseAdapter{

    private Context mContext;

    private List<String> mTypeList;
    private boolean isShowEditText;
    public String getSelectedType() {
        return mSelectedType;
    }

    public void setSelectedType(String selectedType) {
        mSelectedType = selectedType;
        notifyDataSetChanged();
    }

    private String mSelectedType;

    private OnRecyclerItemClickListener<String> mOnItemClickListener;

    public SimpleTypeAdapter(Context context,boolean showEditText){
        this.mContext = context;
        this.isShowEditText = showEditText;
        mTypeList = new ArrayList<>();
    }


    public void notifyDataSetChanged(List<String> eventTypeList){
        this.mTypeList = eventTypeList;
        this.mSelectedType =null;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(ListUtil.isEmpty(mTypeList)){
            return 0;
        }
        return mTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String type = mTypeList.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.gridview_item_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_area_name);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_selected);
        EditText other  = (EditText) view.findViewById(R.id.et_other);

        tv.setText(type);
        if(type.equals(mSelectedType)){
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
                if(type.equals(mSelectedType)){
                   mSelectedType = null;
                } else {
                    mSelectedType = type;
                }
                notifyDataSetChanged();
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(v, position, mSelectedType);
                }
            }
        });
        return view;
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
}
