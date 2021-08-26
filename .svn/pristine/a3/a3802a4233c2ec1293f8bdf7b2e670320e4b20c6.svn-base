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
import com.augurit.agmobile.gzps.uploadevent.model.Assigneers;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.fw.utils.ListUtil;

import java.util.List;

/**
 * Created by long on 2017/11/23.
 */

public class NextLinkAssigneersAdapter extends BaseAdapter {

    private Context mContext;
    private List<Assigneers.Assigneer> assigneerList;
    private int selectedPosition = -1;// 选中的位置

    private OnRecyclerItemClickListener<Assigneers.Assigneer> mOnItemClickListener;

    public NextLinkAssigneersAdapter(Context context){
        this.mContext = context;
    }

    public void notifyDatasetChanged(List<Assigneers.Assigneer> assigneerList){
        this.assigneerList = assigneerList;
        notifyDataSetChanged();
    }

    public void notifyDatasetChanged(List<Assigneers.Assigneer> assigneerList, int selPosition){
        this.assigneerList = assigneerList;
        selectedPosition = selPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtil.isEmpty(assigneerList) ? 0 : assigneerList.size();
    }

    @Override
    public Object getItem(int position) {
        return assigneerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gridview_item_layout, null);
        TextView area_name = (TextView) view.findViewById(R.id.tv_area_name);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_selected);
        final Assigneers.Assigneer assigneer = assigneerList.get(position);
        area_name.setText(assigneer.getUserName());
        if (selectedPosition == position) {
            area_name.setBackground(mContext.getResources().getDrawable(R.drawable.corner_color_primary3));
            area_name.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, null));
            iv.setVisibility(View.VISIBLE);
        } else {
            area_name.setBackground(mContext.getResources().getDrawable(R.drawable.corner_color_write));
            area_name.setTextColor(Color.BLACK);
            iv.setVisibility(View.GONE);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(v, position, assigneer);
                }
            }
        });
        return view;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener<Assigneers.Assigneer> onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
}
