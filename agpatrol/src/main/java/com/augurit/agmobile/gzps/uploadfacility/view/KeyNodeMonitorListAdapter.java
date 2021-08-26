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
import com.augurit.agmobile.gzps.monitor.model.InspectionWellMonitorInfo;
import com.augurit.agmobile.gzps.monitor.model.WellMonitorInfo;
import com.augurit.agmobile.gzps.uploadfacility.util.InspectionSetCheckStateUtil;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.search.view.GlideRoundTransform;
import com.augurit.am.fw.utils.ListUtil;
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
public class KeyNodeMonitorListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, InspectionWellMonitorInfo> {

    private Context mContext;
    public KeyNodeMonitorListAdapter(Context context, List<InspectionWellMonitorInfo> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<InspectionWellMonitorInfo> searchResults) {
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
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, InspectionWellMonitorInfo data) {
        if (holder instanceof ModifiedIdentificationViewHolder) {
            ModifiedIdentificationViewHolder viewHolder = (ModifiedIdentificationViewHolder) holder;
            String imgPath = data.getImgPath();
            if (TextUtils.isEmpty(imgPath)) {
                List<WellMonitorInfo.HasCertAttachmentBean> attachments = data.getAttachments();
                if (!ListUtil.isEmpty(attachments)) {
                    imgPath = attachments.get(0).getAttPath();
                }
            }

            //使用Glide进行加载图片
            if (TextUtils.isEmpty(imgPath)) {
                Glide.with(mContext)
                        .load(R.mipmap.patrol_ic_empty)
                        .error(R.mipmap.patrol_ic_load_fail)
                        .transform(new GlideRoundTransform(mContext))
                        .into(viewHolder.iv);
            } else {
                Glide.with(mContext)
                        .load(imgPath)
                        .error(R.mipmap.patrol_ic_load_fail)
                        .transform(new GlideRoundTransform(mContext))
                        .into(viewHolder.iv);
            }
            InspectionSetCheckStateUtil.setCheckState(data.getCheckState(),viewHolder.tv_right_up);
//            viewHolder.tv_right_up.setText("窨井编号："+data.getJbjObjectId());
            viewHolder.tv_mark_id.setText("窨井编号："+data.getJbjObjectId());
//            if (data.getJbjType() != null) {
//                switch (data.getJbjType()) {
//                    case "污水":
//                        viewHolder.tv_right_mid.setText("污水");
//                        viewHolder.tv_right_mid.setTextColor(Color.RED);
//                        break;
//                    case "雨水":
//                        viewHolder.tv_right_mid.setText("雨水");
//                        viewHolder.tv_right_mid.setTextColor(Color.parseColor("#3EA500"));
//                        break;
//                    case "雨污合流":
//                        viewHolder.tv_right_mid.setText("雨污合流");
//                        viewHolder.tv_right_mid.setTextColor(Color.parseColor("#C794D9"));
//                        break;
//                    default:
//                        viewHolder.tv_right_mid.setText("");
//                        break;
//                }
//            } else {
//                viewHolder.tv_right_mid.setText("");
//            }

            //挂接时间
            if (data.getUpdateTime() != null) {
                viewHolder.tv_right_down.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getUpdateTime())));
            }else if(data.getMarkTime() != null){
                viewHolder.tv_right_down.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getMarkTime())));
            } else {
                viewHolder.tv_right_down.setText("");
            }

            viewHolder.tv_left_up.setText("窨井");

            String concentration = "";
            if (data.getAd()!=null && (Double.parseDouble(data.getAd()))>0) {
                concentration = data.getAd()+"mg/L";
            }
            viewHolder.tv_left_down.setText("氨氮浓度:"+concentration);

            String codConcentration = "";
            if (data.getCod()!=null && (Double.parseDouble(data.getCod()))>0) {
                codConcentration = data.getCod()+"mg/L";
            }
            viewHolder.tv_right_mid.setText("COD浓度:"+codConcentration);

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
