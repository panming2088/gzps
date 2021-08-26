package com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;

import java.util.List;

/**
 *
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
public class PipeStreamAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, PipeBean> {

    private Context mContext;

    public PipeStreamAdapter(Context context, List<PipeBean> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<PipeBean> searchResults){
        mDataList.addAll(searchResults);
        notifyItemInserted(getItemCount());
    }

    public void deleteData(int position){
        if(position>=mDataList.size()){
            return;
        }else{
            mDataList.remove(position);
            notifyDataSetChanged();
        }
    }

    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_review_stream,parent,false);
        return new ModifiedIdentificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, PipeBean data) {
        if (holder instanceof ModifiedIdentificationViewHolder) {
            ModifiedIdentificationViewHolder viewHolder = (ModifiedIdentificationViewHolder) holder;
            if (!TextUtils.isEmpty(data.getPipeType())) {
                viewHolder.tv_left_down.setText(data.getPipeType());
            }else {
                viewHolder.tv_left_down.setText("");
            }
            viewHolder.tv_right_up.setText("");


//            if (data.getUpdateTime() != null && data.getUpdateTime() > 0){
//                viewHolder.tv_right_down.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getUpdateTime())));
//            }else
                if (data.getDirection() != null) {
                viewHolder.tv_right_down.setText(data.getDirection());
            }

            viewHolder.tv_left_up.setText(data.getComponentType());
        }
    }


    public static class ModifiedIdentificationViewHolder extends BaseRecyclerViewHolder {

        TextView tv_left_up;
        TextView tv_left_down;
        TextView tv_right_up;
        TextView tv_right_down;

        public ModifiedIdentificationViewHolder(View itemView) {
            super(itemView);
            tv_left_up = (TextView) itemView.findViewById(R.id.tv_left_up);
            tv_left_down = (TextView) itemView.findViewById(R.id.tv_left_down);
            tv_right_up = (TextView) itemView.findViewById(R.id.tv_right_up);
            tv_right_down = (TextView) itemView.findViewById(R.id.tv_right_down);
        }
    }
}
