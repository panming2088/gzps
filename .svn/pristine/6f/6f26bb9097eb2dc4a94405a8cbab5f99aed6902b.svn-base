package com.augurit.agmobile.patrolcore.layerdownload.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.model.Area;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.fw.utils.ListUtil;

import java.util.List;

/**
 * Created by liangsh on 2016-12-28.
 */
public class LayerDownloadAreaAdapter extends RecyclerView.Adapter<LayerDownloadAreaAdapter.AreaViewHolder> {

    private Context mContext;
    private List<Area> mAreaList;
    private OnRecyclerItemClickListener onItemClickListener;
    private OnRecyclerItemClickListener onDownloadClickListener;

    public LayerDownloadAreaAdapter(Context context){
        this.mContext = context;
    }

    public void notifyDataSetChanged(List<Area> areaList){
        this.mAreaList = areaList;
        notifyDataSetChanged();
    }

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AreaViewHolder areaViewHolder = new AreaViewHolder(View.inflate(mContext, R.layout.dnl_area_item, null));
        areaViewHolder.setIsRecyclable(false);
        return areaViewHolder;
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final Area area = mAreaList.get(position);
        holder.name.setText(area.getDisname());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v, position, area);
                }
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDownloadClickListener != null){
                    onDownloadClickListener.onItemClick(v, position, area);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(ListUtil.isEmpty(mAreaList)){
            return 0;
        }
        return mAreaList.size();
    }

    protected static class AreaViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        View download;

        public AreaViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.dnl_area_item_name);
            download = itemView.findViewById(R.id.dnl_area_item_dnl);
        }

    }

    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnDownloadClickListener(OnRecyclerItemClickListener onDownloadClickListener){
        this.onDownloadClickListener = onDownloadClickListener;
    }
}
