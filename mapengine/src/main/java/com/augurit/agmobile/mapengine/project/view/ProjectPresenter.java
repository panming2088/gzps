package com.augurit.agmobile.mapengine.project.view;

import com.augurit.agmobile.mapengine.layermanage.view.LayerPresenter;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.am.fw.common.IView;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.mapengine.project.service.IProjectService;
import com.augurit.am.fw.utils.log.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.project.view
 * @createTime 创建时间 ：2017-01-16
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-16
 */

public class ProjectPresenter implements IProjectPresenter {


    public static final String TAG = ProjectPresenter.class.getName();

    private IProjectView mIProjectView;

    private OnProjectChangeListener mOnProjectChangeListener;

    private IProjectService mIGetProjectInfoService;

    public ProjectPresenter(IProjectView projectView){
       this.mIProjectView = projectView;
       mIGetProjectInfoService = ProjectDataManager.getInstance().getIGetProjectInfoService();
   }

    public ProjectPresenter(IProjectView projectView,IProjectService getProjectInfoService){
       this.mIProjectView = projectView;
       mIGetProjectInfoService = getProjectInfoService;
   }

    /**
     * 加载专题
     */
    @Override
    public void loadProjects() {

        mIProjectView.showLoadingView();
        OnProjectChangeListener onProjectChangeListener = new OnProjectChangeListener() {
                @Override
                public void onChanged(ProjectInfo projectInfo) {

                    setCurrentProjectById(projectInfo.getProjectId());
                    //使用eventbus发送消息
                    /**
                     * 接受类一个在图层中：{@link LayerPresenter}的onReceivedProjectChangeEvent方法
                     * 一个是点查:{@link com.augurit.agmobile.defaultview.identify.view.IdentifyMapListener}的onProjectChangeEvent方法
                     */
                    EventBus.getDefault().post(new OnProjectChangeEvent(projectInfo));
                    if (mOnProjectChangeListener != null){
                        mOnProjectChangeListener.onChanged(projectInfo);
                    }
                }
            };
        mIProjectView.setOnProjectChangeListener(onProjectChangeListener);
        mIGetProjectInfoService.getProjects(mIProjectView.getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ProjectInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(TAG,"专题加载失败，原因是："+ e.getMessage());
                        mIProjectView.hideLoadingView();
                        mIProjectView.showLoadProjectErrorView();
                    }

                    @Override
                    public void onNext(List<ProjectInfo> projectInfos) {
                        mIProjectView.hideLoadingView();
                        String currentProjectId = mIGetProjectInfoService.getCurrentProjectId(mIProjectView.getApplicationContext());
                        int selectedPosition = getProjectIndex(projectInfos, currentProjectId);
                        mIProjectView.initProjectView(projectInfos,selectedPosition);
                        mIProjectView.addProjectViewToContainer();
                        mIProjectView.showProjectView();
                    }
                });
        /*ProjectDataManager.getInstance().getAllProjectsData(mIProjectView.getApplicationContext(), new Callback<List<ProjectInfo>>() {
            @Override
            public void onSuccess(List<ProjectInfo> projectInfos) {

               // mIProjectView.onProjectLoadedFinished(result);
            }

            @Override
            public void onFail(Exception error) {

               // mIProjectView.onProjectLoadedError(error);
            }
        });*/
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
    public void setCurrentProjectById(String id) {
        mIGetProjectInfoService.setCurrentProject(mIProjectView.getApplicationContext(),id);
    }

    @Override
    public IView getView() {
        return mIProjectView;
    }

    @Override
    public void attachView(IView view) {
        this.mIProjectView = (IProjectView) view;
    }

    @Override
    public void detachView(boolean shouldRetainInstance) {
        mIProjectView = null;
    }


    @Override
    public void setOnProjectChangeListener(OnProjectChangeListener onProjectChangeListener) {
            mOnProjectChangeListener = onProjectChangeListener;
    }
}
