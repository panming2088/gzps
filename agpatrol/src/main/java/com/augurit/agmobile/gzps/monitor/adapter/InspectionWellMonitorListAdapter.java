package com.augurit.agmobile.gzps.monitor.adapter;

import android.content.Context;
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
 * 窨井监测列表
 *
 * @author 创建人 ：huangchongwu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps
 * @createTime 创建时间 ：2020-09-14
 */
public class InspectionWellMonitorListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, InspectionWellMonitorInfo> {

    private Context mContext;
    private String type;

    public InspectionWellMonitorListAdapter(Context context, List<InspectionWellMonitorInfo> mDataList, String type) {
        super(mDataList);
        this.mContext = context;
        this.type = type;
    }

    public void addData(List<InspectionWellMonitorInfo> searchResults) {
        mDataList.addAll(searchResults);
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new InspectionWellMonitorViewHolder(inflater.inflate(R.layout.item_inspection_well_monitor, null));
    }


    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, InspectionWellMonitorInfo data) {
        if (holder instanceof InspectionWellMonitorViewHolder) {
            InspectionWellMonitorViewHolder viewHolder = (InspectionWellMonitorViewHolder) holder;
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

            String concentration = "";
            if (data.getAd()!=null && (Double.parseDouble(data.getAd()))>0) {
                concentration = data.getAd()+"mg/L";
            }
            viewHolder.tv_left_mid.setText("氨氮浓度:"+concentration);

            String codConcentration = "";
            try {
                if (!TextUtils.isEmpty(data.getCod()) && (Double.parseDouble(data.getCod()))>0) {
                    codConcentration = data.getCod()+"mg/L";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            viewHolder.tv_left_down.setText("COD浓度:"+codConcentration);
            InspectionSetCheckStateUtil.setCheckState(data.getCheckState(),viewHolder.tv_right_up);
            String monitorTime = "";
            if (data.getJcsj()!=null && data.getJcsj()>0) {
                monitorTime = TimeUtil.getStringTimeMdHmChines(new Date(data.getJcsj()));
            }
            viewHolder.tv_left_up.setText("监测时间:"+monitorTime);

        }
    }


    public static class InspectionWellMonitorViewHolder extends BaseRecyclerViewHolder {

        ImageView iv;
        TextView tv_left_up;
        TextView tv_right_up;
        TextView tv_left_down;
        TextView tv_left_mid;

        public InspectionWellMonitorViewHolder(View itemView) {
            super(itemView);
            iv = findView(R.id.search_result_item_iv);
            tv_left_up = findView(R.id.tv_left_up);
            tv_right_up = findView(R.id.tv_right_up);
            tv_left_down = findView(R.id.tv_left_down);
            tv_left_mid = findView(R.id.tv_left_mid);
        }
    }
}
