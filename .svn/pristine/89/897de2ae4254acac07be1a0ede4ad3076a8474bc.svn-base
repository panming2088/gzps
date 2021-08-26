package com.augurit.agmobile.patrolcore.common.opinion.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.PackageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.view
 * @createTime 创建时间 ：2017-07-17
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-17
 * @modifyMemo 修改备注：
 */

public class OpinionTemplateListAdapter extends RecyclerView.Adapter<OpinionTemplateListAdapter.TemplateListHolder> {

    private Context mContext;
    private List<OpinionTemplate> mOpinionTemplateList;
    private OnRecyclerItemClickListener<OpinionTemplate> mOnRecyclerItemClickListener;

    public OpinionTemplateListAdapter(Context context){
        this.mContext = context;
    }

    public void notifyDataSetChanged(List<OpinionTemplate> opinionTemplateList, boolean add){
        if(opinionTemplateList == null){
            mOpinionTemplateList = new ArrayList<>();
        }
        if(add){
            mOpinionTemplateList.addAll(opinionTemplateList);
        } else {
            mOpinionTemplateList = opinionTemplateList;
        }
        notifyDataSetChanged();
    }

    @Override
    public TemplateListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TemplateListHolder(View.inflate(mContext, R.layout.opinion_template_list_item, null));
    }

    @Override
    public void onBindViewHolder(TemplateListHolder holder, final int i) {
        final OpinionTemplate opinionTemplate = mOpinionTemplateList.get(i);
        holder.name.setText(opinionTemplate.getName());
        holder.content.setText(opinionTemplate.getContent());
        if(opinionTemplate.isUpload()){
           holder.local.setVisibility(View.GONE);
        } else {
            holder.local.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRecyclerItemClickListener != null){
                    mOnRecyclerItemClickListener.onItemClick(v, i, opinionTemplate);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(ListUtil.isEmpty(mOpinionTemplateList)){
            return 0;
        }
        return mOpinionTemplateList.size();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<OpinionTemplate> onRecyclerItemClickListener){
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    protected class TemplateListHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView content;
        View local;

        public TemplateListHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            content = (TextView) itemView.findViewById(R.id.content);
            local = itemView.findViewById(R.id.local);
        }
    }
}
