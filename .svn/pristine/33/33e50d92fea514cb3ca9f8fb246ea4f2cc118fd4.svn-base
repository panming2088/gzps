package com.augurit.agmobile.patrolcore.localhistory.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;
import com.augurit.agmobile.patrolcore.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.setting.problem.view
 * @createTime 创建时间 ：17/3/21
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/21
 * @modifyMemo 修改备注：
 */

public class ProblemListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder,LocalTable>{

    public ProblemListAdapter(List<LocalTable> mDataList) {
        super(mDataList);
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ProblemListViewHolder(inflater.inflate(R.layout.local_problems_listview_item,null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, LocalTable data) {
        if(holder instanceof ProblemListViewHolder){
            ProblemListViewHolder viewHolder = (ProblemListViewHolder)holder;
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String timeStr=format.format(new Date(data.getTime()));
            viewHolder.date.setText(timeStr);
        }

    }

    public static class ProblemListViewHolder extends BaseRecyclerViewHolder {
        ImageView iv;
        TextView title;
        TextView date;
        TextView addr;
        TextView status;

        public ProblemListViewHolder(View itemView) {
            super(itemView);
            iv = findView(R.id.search_result_item_iv);
            title = (TextView) itemView.findViewById(R.id.search_item_title);
            date = (TextView) itemView.findViewById(R.id.search_item_date);
            addr = (TextView) itemView.findViewById(R.id.search_item_addr);
            status = (TextView) itemView.findViewById(R.id.search_item_status);
        }
    }

}
