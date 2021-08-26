package com.augurit.agmobile.gzps.uploadfacility.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.monitor.model.WellMonitorInfo;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.search.view.GlideRoundTransform;
import com.augurit.am.fw.utils.TimeUtil;
import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

/**
 * 设施新增列表Adapter
 *
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search.view
 * @createTime 创建时间 ：2017-03-20
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-20
 * @modifyMemo 修改备注：
 */
public class JbjMonitorListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, WellMonitorInfo> {

    private Context mContext;
    public JbjMonitorListAdapter(Context context, List<WellMonitorInfo> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<WellMonitorInfo> searchResults) {
        mDataList.addAll(searchResults);
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ModifiedIdentificationViewHolder(inflater.inflate(R.layout.item_well_monitor, null));
    }


    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, WellMonitorInfo data) {
        if (holder instanceof ModifiedIdentificationViewHolder) {
            ModifiedIdentificationViewHolder viewHolder = (ModifiedIdentificationViewHolder) holder;
            String imgPath = data.getImgPath();
            if (!TextUtils.isEmpty(imgPath)){
                Glide.with(mContext)
                        .load(imgPath)
                        .error(R.mipmap.patrol_ic_load_fail)
                        .transform(new GlideRoundTransform(mContext))
                        .into(viewHolder.iv);
            }else {
                imgPath = data.getAttachments().get(0).getAttPath();
                //使用Glide进行加载图片
                Glide.with(mContext)
                        .load(imgPath)
                        .error(R.mipmap.patrol_ic_load_fail)
                        .transform(new GlideRoundTransform(mContext))
                        .into(viewHolder.iv);
            }
//            if(!TextUtils.isEmpty(data.getJbjType()) && data.getJbjType().indexOf("雨水")!=-1){
//                //雨水井
//                viewHolder.tv_left_up.setText("晴天是否有水流动:"+ ("1".equals(data.getQtsfys())?"是":"否"));
//                viewHolder.tv_left_down.setText("接驳井是否被堵塞:"+ ("1".equals(data.getSfds())?"是":"否"));
//                if (!TextUtils.isEmpty(data.getCod()) && (Double.valueOf(data.getAd()))>0) {
//                    viewHolder.tv_right_mid.setText("COD浓度:"+data.getCod()+"mg/L");
//                }else{
//                    viewHolder.tv_right_mid.setText("COD浓度:");
//                }
//            }else{
//                //污水井或者雨污合流井
//                viewHolder.tv_left_up.setText("接驳井是否被堵塞:"+ ("1".equals(data.getSfds())?"是":"否"));
//                viewHolder.tv_left_down.setText("接驳井管径:"+ data.getGdgj()+"mm");
//                viewHolder.tv_right_mid.setText("日污水流量:"+data.getRwsll()+"吨");
//            }
//
//            if (data.getAd()!=null && (Double.valueOf(data.getAd()))>0) {
//                viewHolder.tv_right_up.setText("氨氮浓度:"+data.getAd()+"mg/L");
//            }else{
//                viewHolder.tv_right_up.setText("氨氮浓度:");
//            }
            if (data.getJbjType() != null) {
                if ("污水".equals(data.getJbjType())) {
                    viewHolder.tv_right_up.setText("污水");
                    viewHolder.tv_right_up.setTextColor(Color.RED);
                } else if ("雨水".equals(data.getJbjType())) {
                    viewHolder.tv_right_up.setText("雨水");
                    viewHolder.tv_right_up.setTextColor(Color.parseColor("#3EA500"));
                } else if ("雨污合流".equals(data.getJbjType())) {
                    viewHolder.tv_right_up.setText("雨污合流");
                    viewHolder.tv_right_up.setTextColor(Color.parseColor("#C794D9"));
                }
            } else {
                viewHolder.tv_right_up.setText("");
            }
            viewHolder.tv_left_up.setText("接驳井");
            viewHolder.tv_left_down.setText(data.getJbjAddr());
            viewHolder.tv_right_down.setText("");
            viewHolder.tv_mark_id.setText("接驳井编号："+data.getJbjObjectId());

            //挂接时间
            if (data.getUpdateTime() != null) {
                viewHolder.tv_right_down.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getUpdateTime())));
            }else if(data.getMarkTime() != null){
                viewHolder.tv_right_down.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getMarkTime())));
            }

        }
    }


    public static class ModifiedIdentificationViewHolder extends BaseRecyclerViewHolder {

        ImageView iv;
        TextView tv_left_up;
        TextView tv_left_down;
        TextView tv_right_up;
        TextView tv_right_mid;
        TextView tv_right_down;
        TextView tv_mark_id;

        public ModifiedIdentificationViewHolder(View itemView) {
            super(itemView);
            iv = findView(R.id.search_result_item_iv);
            tv_left_up = (TextView) itemView.findViewById(R.id.tv_left_up);
            tv_left_down = (TextView) itemView.findViewById(R.id.tv_left_down);
            tv_right_up = (TextView) itemView.findViewById(R.id.tv_right_up);
            tv_right_mid = (TextView) itemView.findViewById(R.id.tv_right_mid);
            tv_right_down = (TextView) itemView.findViewById(R.id.tv_right_down);
            tv_mark_id = (TextView) itemView.findViewById(R.id.tv_mark_id);
        }
    }
}
