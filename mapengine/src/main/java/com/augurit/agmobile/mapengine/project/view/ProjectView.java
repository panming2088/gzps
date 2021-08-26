package com.augurit.agmobile.mapengine.project.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.augurit.agmobile.mapengine.R;
import com.augurit.agmobile.mapengine.common.DividerItemDecoration;
import com.augurit.am.fw.common.IPresenter;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.List;

/**
  *  默认的专题视图
  * @author 创建人 ：xuciluan
  * @version
  * @package 包名 ：com.augurit.am.map.arcgis.proj.view.impl
  * @createTime 创建时间 ：2016-11-01
  * @modifyBy 修改人 ：
  * @modifyTime 修改时间 ：2016-11-01
  */
public class ProjectView implements IProjectView {

    protected IProjectPresenter mIProjectPresenter;
    protected ViewGroup mProjectContainer;


    protected Context context;
    protected RecyclerView mRecyclerView;
    protected ProgressDialog mProgressDialog;
    protected View mProjectView;

    protected IProjectPresenter.OnProjectChangeListener mOnProjectChangeListener;

    public ProjectView(Context context,ViewGroup projectViewContainer) {
        this.context = context;
        this.mProjectContainer = projectViewContainer;
    }


    @Override
    public Context getApplicationContext() {
        return context.getApplicationContext();
    }

    public void setPresenter(IPresenter presenter) {
        this.mIProjectPresenter = (IProjectPresenter) presenter;
    }

    private void initDatas(Context context, final List<ProjectInfo> datas, int selectedPosition) {

        final ProjectViewAdapter adapter = getProjectViewAdapter(context, datas);
        adapter.setSelectedPosition(selectedPosition);  // 设置选中项
        adapter.setOnItemClickListener(new ProjectViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //设置当前专题
               // mIProjectPresenter.setCurrentProjectById(datas.requestLocation(position).getProjectId());
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                mOnProjectChangeListener.onChanged(datas.get(position));
               // mIProjectPresenter.onProjectChanged(datas.requestLocation(position));
                /*if (mOnIOSPopupClickListener!= null){
                    mOnIOSPopupClickListener.onPopupItemClick(position);
                }*/
            }
        });
        mRecyclerView.setAdapter(adapter);

    }

    @NonNull
    protected ProjectViewAdapter getProjectViewAdapter(Context context, List<ProjectInfo> datas) {
        return new ProjectViewAdapter(context, datas);
    }

    int getProjectIndex(List<ProjectInfo> projects, String id){
        for (int i = 0; i< projects.size() ;i++){
            ProjectInfo project = projects.get(i);
            if (project.getProjectId().equals(id)){
                return i;
            }
        }
        return 0;
    }

    @Override
    public void addProjectViewToContainer() {
        mProjectContainer.removeAllViews();
        mProjectContainer.addView(mProjectView);
    }

    @Override
    public void initProjectView(List<ProjectInfo> projectInfos,int selectedPosition) {
        mProjectView = getView();
        mRecyclerView = (RecyclerView) mProjectView.findViewById(R.id.rv_project);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        int selectedId = selectedPosition;
        initDatas(context,projectInfos, selectedId);

        if (mProjectContainer != null){
            mProjectContainer.setVisibility(View.VISIBLE);
            mProjectContainer.removeAllViews();
            mProjectContainer.addView(mProjectView);
        }else {
            //如果此时依然为空，说明不是继承在BaseActivity，需要自行把容器传递进来
            //提醒开发者是否忘记调用set方法了
            LogUtil.w("专题视图的容器为空，是否忘记调用setProjectListContainer方法？？");
        }
    }

    protected View getView() {
        return View.inflate(context, R.layout.proj_list_view, null);
    }

    @Override
    public void refreshProjectView(List<ProjectInfo> projectInfos) {

    }

    @Override
    public void showLoadingView() {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("正在加载专题信息");
        mProgressDialog.show();
    }

    @Override
    public void hideLoadingView() {
        mProgressDialog.dismiss();
    }

    @Override
    public void hideProjectView() {
        mProjectView.setVisibility(View.GONE);
    }

    @Override
    public void showProjectView() {
        mProjectContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeProjectView() {
        mProjectContainer.removeAllViews();
    }

    @Override
    public void hideContainer() {
        mProjectContainer.setVisibility(View.GONE);
    }

    @Override
    public void showLoadProjectErrorView() {
        ToastUtil.shortToast(getApplicationContext(),"专题更新失败");
    }

    @Override
    public void setOnProjectChangeListener(IProjectPresenter.OnProjectChangeListener onProjectChangeListener) {
        mOnProjectChangeListener = onProjectChangeListener;
    }
}
