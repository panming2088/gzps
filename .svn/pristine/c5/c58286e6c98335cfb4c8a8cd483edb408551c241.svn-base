package com.augurit.agmobile.gzps.trackmonitor.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.trackmonitor.model.UserOrg;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.fw.utils.ListUtil;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.trackmonitor.view
 * @createTime 创建时间 ：2017-08-21
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-21
 * @modifyMemo 修改备注：
 */

public class SubordinateListAdapter extends RecyclerView.Adapter<SubordinateListAdapter.SubordinateViewHolder> {

    private Context mContext;
    private OnRecyclerItemClickListener<UserOrg> mOnRecyclerItemClickListener;

    private List<UserOrg> userOrgList;

    public SubordinateListAdapter(Context context){
        this.mContext = context;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<UserOrg> userOrgList){
        this.userOrgList = userOrgList;
    }

    @Override
    public SubordinateViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SubordinateViewHolder(View.inflate(mContext, R.layout.subordinate_list_item, null));
    }

    @Override
    public void onBindViewHolder(SubordinateViewHolder subordinateViewHolder, final int i) {
        final UserOrg userOrg = userOrgList.get(i);
        subordinateViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRecyclerItemClickListener != null){
                    mOnRecyclerItemClickListener.onItemClick(v, i, userOrg);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(ListUtil.isEmpty(userOrgList)){
            return 0;
        }
        return userOrgList.size();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<UserOrg> onRecyclerItemClickListener){
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    class SubordinateViewHolder extends RecyclerView.ViewHolder{

        public SubordinateViewHolder(View itemView){
            super(itemView);
        }
    }
}
