package com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.DoubtBean;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * 设施纠错列表
 *
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search.view
 * @createTime 创建时间 ：2017-03-20
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-20
 * @modifyMemo 修改备注：
 */
public class MyDoubtListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, DoubtBean> {

    private Context mContext;

    public MyDoubtListAdapter(Context context, List<DoubtBean> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<DoubtBean> searchResults) {
        mDataList.addAll(searchResults);
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ModifiedIdentificationViewHolder(inflater.inflate(R.layout.item_my_doubt, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, DoubtBean data) {
        if (holder instanceof ModifiedIdentificationViewHolder) {

            ModifiedIdentificationViewHolder viewHolder = (ModifiedIdentificationViewHolder) holder;

            if (data.getStatus() != null) {
                if (data.getStatus().equals("2")) {
                    viewHolder.tv_right_up.setText("已取消");
                    viewHolder.tv_right_up.setTextColor(Color.RED);
                } else if (data.getStatus().equals("3")) {
                    viewHolder.tv_right_up.setText("审核通过");
                    viewHolder.tv_right_up.setTextColor(Color.parseColor("#3EA500"));
                } else if (data.getStatus().equals("1")) {
                    viewHolder.tv_right_up.setText("存在疑问");
                    viewHolder.tv_right_up.setTextColor(Color.parseColor("#FFFFC248"));
                }
            } else {
                viewHolder.tv_right_up.setText("");
            }
            viewHolder.tv_upload_person.setText(data.getMarkPerson());
            if (data.getUpdateTime() != null && data.getUpdateTime() > 0) {
                viewHolder.tv_time.setText("上报时间："+TimeUtil.getStringTimeYMDMChines(new Date(data.getUpdateTime())));
            }else{
                viewHolder.tv_time.setText("上报时间："+TimeUtil.getStringTimeYMDMChines(new Date(data.getMarkTime())));
            }
            viewHolder.tv_description.setText(data.getLayerName() + (!TextUtils.isEmpty(data.getObjectId()) ? "(" + data.getObjectId() + ")" : ""));

            viewHolder.tv_address.setText("质疑原因："+data.getDescription());
        }
    }


    public static class ModifiedIdentificationViewHolder extends BaseRecyclerViewHolder {
        TextView tv_upload_person;//名称
        TextView tv_time; //时间
        TextView tv_description;//点线面
        TextView tv_address;//原因
        TextView tv_right_up;//状态

        public ModifiedIdentificationViewHolder(View itemView) {
            super(itemView);
            tv_upload_person = (TextView) itemView.findViewById(R.id.tv_upload_person);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_right_up = (TextView) itemView.findViewById(R.id.tv_right_up);
        }
    }
}
