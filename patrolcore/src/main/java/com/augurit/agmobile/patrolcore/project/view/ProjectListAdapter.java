package com.augurit.agmobile.patrolcore.project.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.R;

import java.util.List;

/**
 * 描述：项目
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.setting.problem.view
 * @createTime 创建时间 ：17/3/21
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/21
 * @modifyMemo 修改备注：
 */

public class ProjectListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder,Project>{

    public ProjectListAdapter(List<Project> projects) {
        super(projects);
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ProblemListViewHolder(inflater.inflate(R.layout.project_listview_item,parent,false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, Project data) {
        if(holder instanceof ProblemListViewHolder){
            ProblemListViewHolder viewHolder = (ProblemListViewHolder)holder;
            viewHolder.date.setText(String.valueOf(data.getName()));
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
