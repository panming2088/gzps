package com.augurit.agmobile.gzps.statistic.view.uploadview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.publicaffair.model.EventAffair;
import com.augurit.agmobile.gzps.statistic.model.UploadYTStatisticBean;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.fw.utils.ListUtil;

import java.text.DecimalFormat;
import java.util.List;


/**
 * @author : taoerxiang
 * @data : 2017-11-11  12:04
 * @des :
 */

public class UploadStatiscAdapter extends RecyclerView.Adapter<UploadStatiscAdapter.MyViewHolder> {

    private Context context;
    private List<UploadYTStatisticBean.ToDayEntity> toDay;
    private List<UploadYTStatisticBean.YestDayEntity> yestDay;
    private OnRecyclerItemClickListener<EventAffair.EventAffairBean> listener;
    private LayoutInflater mInflater;

    public UploadStatiscAdapter(Context context) {
        this.context = context;
    }

    public void refresh(List<UploadYTStatisticBean.YestDayEntity> yestDay,List<UploadYTStatisticBean.ToDayEntity> toDay){
        this.toDay = toDay;
        this.yestDay = yestDay;
        notifyDataSetChanged();
    }


    @Override
    public UploadStatiscAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(context);
        View itemView = mInflater.inflate(R.layout.upload_statisc_lt_item, parent,false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        UploadYTStatisticBean.ToDayEntity toDayEntity = toDay.get(position);
        UploadYTStatisticBean.YestDayEntity yestdayEntity = yestDay.get(position);
        holder.distric_tv.setText(toDayEntity.getName());
        holder.yd_all_tv.setText(getFormatInt((yestdayEntity.getLackCount()+yestdayEntity.getCorrCount()))+"");
        holder.yd_new_tv.setText(getFormatInt(yestdayEntity.getLackCount())+"");
        holder.yd_correct_tv.setText(getFormatInt(yestdayEntity.getCorrCount())+"");
        holder.td_all_tv.setText(getFormatInt(toDayEntity.getLackCount()+toDayEntity.getCorrCount())+"");
        holder.td_new_tv.setText(getFormatInt(toDayEntity.getLackCount())+"");
        holder.td_correct_tv.setText(getFormatInt(toDayEntity.getCorrCount())+"");
    }

    private String getFormatInt(int n){
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(n);
    }

    @Override
    public int getItemCount() {
        return ListUtil.isEmpty(toDay) ? 0 : toDay.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView distric_tv;
        TextView yd_all_tv;
        TextView yd_new_tv;
        TextView yd_correct_tv;
        TextView td_all_tv;
        TextView td_new_tv;
        TextView td_correct_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            distric_tv = (TextView) itemView.findViewById(R.id.upload_statisc_distrc);
            yd_all_tv = (TextView) itemView.findViewById(R.id.yd_all);
            yd_new_tv = (TextView) itemView.findViewById(R.id.yd_new);
            yd_correct_tv = (TextView) itemView.findViewById(R.id.yd_correct);
            td_all_tv = (TextView) itemView.findViewById(R.id.td_all);
            td_new_tv = (TextView) itemView.findViewById(R.id.td_new);
            td_correct_tv = (TextView) itemView.findViewById(R.id.td_correct);
        }
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener<EventAffair.EventAffairBean> listener) {
        this.listener = listener;
    }

}
