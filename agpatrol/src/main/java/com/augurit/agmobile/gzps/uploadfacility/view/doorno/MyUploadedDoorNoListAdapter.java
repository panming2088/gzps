package com.augurit.agmobile.gzps.uploadfacility.view.doorno;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadDoorNoDetailBean;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.fw.utils.TimeUtil;

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
public class MyUploadedDoorNoListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, UploadDoorNoDetailBean> {

    private Context mContext;

    public MyUploadedDoorNoListAdapter(Context context, List<UploadDoorNoDetailBean> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<UploadDoorNoDetailBean> searchResults) {
        mDataList.addAll(searchResults);
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ModifiedIdentificationViewHolder(inflater.inflate(R.layout.item_doorno_identification, null));
    }


    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, UploadDoorNoDetailBean data) {
        if (holder instanceof ModifiedIdentificationViewHolder) {
            ModifiedIdentificationViewHolder viewHolder = (ModifiedIdentificationViewHolder) holder;
            if (!TextUtils.isEmpty(data.getDzqc())) {
                viewHolder.tv_left_down.setText(data.getDzqc());
            } else {
                viewHolder.tv_left_down.setText("");
            }

//            if (!TextUtils.isEmpty(data.getMpwzhm())) {
//                viewHolder.tv_left_up.setText(data.getSsxzqh());
//            } else {
//                viewHolder.tv_left_up.setText("");
//            }

            if (data.getState() == 2) {
                viewHolder.tv_right_up.setText("审核通过");
                viewHolder.tv_right_up.setTextColor(Color.parseColor("#3EA500"));
            } else if (data.getState() == 1) {
                viewHolder.tv_right_up.setText("未审核");
                viewHolder.tv_right_up.setTextColor(Color.RED);
            } else if (data.getState() == 3) {
                viewHolder.tv_right_up.setText("存在疑问");
                viewHolder.tv_right_up.setTextColor(Color.parseColor("#FFFFC248"));
            } else {
                viewHolder.tv_right_up.setText("");
            }

//            if (data.getUpdateTime() != null && data.getUpdateTime() > 0){
//                viewHolder.tv_right_down.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getUpdateTime())));
//            }else
            if (data.getMarkTime() != null) {
                viewHolder.tv_right_down.setText(TimeUtil.getStringTimeMdHmChines(new Date(Long.valueOf(data.getMarkTime()))));
            }

            viewHolder.tv_mark_id.setText("上报编号：" + data.getObjectId());

//            viewHolder.tv_left_up.setText(data.getDzdm());
        }
    }


    public static class ModifiedIdentificationViewHolder extends BaseRecyclerViewHolder {

        ImageView iv;
        TextView tv_left_up;
        TextView tv_left_down;
        TextView tv_right_up;
        TextView tv_right_down;
        TextView tv_mark_id;

        public ModifiedIdentificationViewHolder(View itemView) {
            super(itemView);
            iv = findView(R.id.search_result_item_iv);
//            tv_left_up = (TextView) itemView.findViewById(R.id.tv_left_up);
            tv_left_down = (TextView) itemView.findViewById(R.id.tv_left_down);
            tv_right_up = (TextView) itemView.findViewById(R.id.tv_right_up);
            tv_right_down = (TextView) itemView.findViewById(R.id.tv_right_down);
            tv_mark_id = (TextView) itemView.findViewById(R.id.tv_mark_id);
        }
    }
}
