package com.augurit.agmobile.patrolcore.common.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.fw.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.view
 * @createTime 创建时间 ：2017-08-01
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-01
 * @modifyMemo 修改备注：
 */

public class StringAdapter extends RecyclerView.Adapter<StringAdapter.StringHolder> {

    private Context mContext;
    private List<String> stringList;
    private OnRecyclerItemClickListener<String> mOnRecyclerItemClickListener;

    public StringAdapter(Context context){
        mContext = context;
    }

    public void notifyDataSetChanged(List<String> stringList){
        this.stringList = stringList;
        notifyDataSetChanged();
    }

    public void loadMore(List<String> stringList){
        if(ListUtil.isEmpty(this.stringList)){
            this.stringList = new ArrayList<>();
        }
        if(!ListUtil.isEmpty(stringList)){
            this.stringList.addAll(stringList);
        }
        notifyDataSetChanged();
    }

    @Override
    public StringHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new StringHolder(View.inflate(mContext, R.layout.string_item, null));
    }

    @Override
    public void onBindViewHolder(StringHolder stringHolder, final int position) {
        final String string = stringList.get(position);
        stringHolder.textView.setText(string);
        stringHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRecyclerItemClickListener != null){
                    mOnRecyclerItemClickListener.onItemClick(v, position, string);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(ListUtil.isEmpty(stringList)){
            return 0;
        }
        return stringList.size();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<String> onRecyclerItemClickListener){
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    protected class StringHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public StringHolder(View itemView){
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
