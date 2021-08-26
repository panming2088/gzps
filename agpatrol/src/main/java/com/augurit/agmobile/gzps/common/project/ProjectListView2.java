package com.augurit.agmobile.gzps.common.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.project.view.IProjectListView;
import com.augurit.agmobile.patrolcore.project.view.ProjectListAdapter;

import java.util.List;

/**
 * 描述：修改跳转的界面
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.setting.problem.view
 * @createTime 创建时间 ：17/3/21
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/21
 * @modifyMemo 修改备注：
 */

public class ProjectListView2 implements IProjectListView{
    private Context context;
    private RecyclerView listView;
    private ProjectListAdapter adapter;

    public ProjectListView2(Context context){
        this.context = context;
    }

    @Override
    public void onShowProjectListView(final List<Project> projects, ViewGroup container) {
        if(projects.size() == 1){
            Project project = projects.get(0);
            Intent intent = new Intent(context, EditTableActivity2.class);
            intent.putExtra("projectId", project.getId());
            intent.putExtra("projectName",project.getName());
            intent.putExtra("tableName",project.getIndustryTableName());
            context.startActivity(intent);
            ((Activity)context).finish();
            return;
        }

        ViewGroup mainView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.project_list_view,null);
        listView = (RecyclerView) mainView.findViewById(R.id.list_view);
        adapter = new ProjectListAdapter(projects);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                Project project = projects.get(position);
                Intent intent = new Intent(context, EditTableActivity2.class);
                intent.putExtra("projectId", project.getId());
                intent.putExtra("projectName",project.getName());
                intent.putExtra("tableName",project.getIndustryTableName());
                context.startActivity(intent);
            }
        });


        container.addView(mainView);
    }
}
