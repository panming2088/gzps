package com.augurit.agmobile.mapengine.project.service;

import android.content.Context;


import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.mapengine.project.router.ProjectRouter;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.proj.service
 * @createTime 创建时间 ：2016-11-27
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-27
 */

public class ProjectServiceImpl implements IProjectService {


    protected ProjectRouter mRouter;

    public ProjectServiceImpl() {

        mRouter = new ProjectRouter();
    }


    @Override
    public Observable<List<ProjectInfo>> getProjects(final Context context) {
        return Observable.fromCallable(new Callable<List<ProjectInfo>>() {
            @Override
            public List<ProjectInfo> call() throws Exception {
                BaseInfoManager baseInfoManager = new BaseInfoManager();
                List<ProjectInfo> projects = mRouter.getProjects(context, BaseInfoManager.getUserId(context));
                //设置默认专题
                if (getCurrentProjectId(context) == null) {
                    setCurrentProject(context, projects.get(0).getProjectId());
                }
                return projects;
            }
        }).subscribeOn(Schedulers.io());
        /*return Observable.create(new Observable.OnSubscribe<List<ProjectInfo>>() {
            @Override
            public void call(Subscriber<? super List<ProjectInfo>> subscriber) {
                    BaseInfoManager baseInfoManager = new BaseInfoManager();
                    List<ProjectInfo> projects = mRouter.getProjects(context, BaseInfoManager.getUserName(context));
                    //设置默认专题
                    if (getCurrentProjectId(context) == null){
                       setCurrentProject(context,projects.get(0).getProjectId());
                    }
                    LogUtil.d("成功返回专题信息，共有："+projects.size()+"个专题");
                    subscriber.onNext(projects);
                    subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());*/
    }

    @Override
    public void getCurrentProjectName(final Context context, final Callback2<String> callback) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    BaseInfoManager baseInfoManager = new BaseInfoManager();
                    String projectName = mRouter.getCurrentProjectName(context, BaseInfoManager.getUserId(context));
                    subscriber.onNext(projectName);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    // subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(String s) {
                        callback.onSuccess(s);
                    }
                });
    }

    @Override
    public String getCurrentProjectId(Context context) {
        String userId = BaseInfoManager.getUserId(context);
        return mRouter.getCurrentProjectId(context, userId);
    }

    @Override
    public void getCurrentProject(final Context context, final Callback2<ProjectInfo> callback) {
        Observable.create(new Observable.OnSubscribe<ProjectInfo>() {
            @Override
            public void call(Subscriber<? super ProjectInfo> subscriber) {
                try {
                    BaseInfoManager baseInfoManager = new BaseInfoManager();
                    ProjectInfo currentProject = mRouter.getCurrentProject(context, BaseInfoManager.getUserId(context));
                    subscriber.onNext(currentProject);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    //  subscriber.onError(e);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ProjectInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(ProjectInfo s) {
                        callback.onSuccess(s);
                    }
                });
    }

    @Override
    public void getProjectDataById(final Context context, final String projectId, final Callback2<ProjectInfo> callback) {

        Observable.create(new Observable.OnSubscribe<ProjectInfo>() {
            @Override
            public void call(Subscriber<? super ProjectInfo> subscriber) {
                try {
                    BaseInfoManager baseInfoManager = new BaseInfoManager();
                    ProjectInfo currentProject = mRouter.getProjectDataById(context, BaseInfoManager.getUserId(context), projectId);
                    subscriber.onNext(currentProject);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    // subscriber.onError(e);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ProjectInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(ProjectInfo s) {
                        callback.onSuccess(s);
                    }
                });
    }

    @Override
    public void setCurrentProject(Context context, String projectId) {
        mRouter.setCurrentProjectId(BaseInfoManager.getUserId(context), context, projectId);
    }

    @Override
    public void forceUpdate(final Context context, final Callback2<List<ProjectInfo>> callback) {
        Observable.fromCallable(new Callable<List<ProjectInfo>>() {
            @Override
            public List<ProjectInfo> call() throws Exception {
                //删除所有专题文件
                BaseInfoManager baseInfoManager = new BaseInfoManager();
                mRouter.deleteAllProjects(context, BaseInfoManager.getUserId(context));
                List<ProjectInfo> projects = mRouter.getProjects(context, BaseInfoManager.getUserId(context));
                return projects;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<ProjectInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<ProjectInfo> projectInfos) {
                        callback.onSuccess(projectInfos);
                    }
                });
        /*Observable.create(new Observable.OnSubscribe<List<ProjectInfo>>() {
            @Override
            public void call(Subscriber<? super List<ProjectInfo>> subscriber) {
                    //删除所有专题文件
                    BaseInfoManager baseInfoManager = new BaseInfoManager();
                    mRouter.deleteAllProjects(context, BaseInfoManager.getUserName(context));
                    List<ProjectInfo> projects = mRouter.getProjects(context, BaseInfoManager.getUserName(context));
                    subscriber.onNext(projects);
                    subscriber.onCompleted();

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<ProjectInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<ProjectInfo> projectInfos) {
                        callback.onSuccess(projectInfos);
                    }
                });*/
    }

    @Override
    public void clearInstance() {
        mRouter.clearInstance();
    }

    /**
     * 默认最佳缩放比例就是最大比例减少2个级别
     *
     * @param context
     * @return 最佳缩放比例
     */
    @Override
    public double getBestResolutionForReadingIfItHas(Context context) throws IOException {
        ProjectInfo localProject = mRouter.getCurrentProjectFromLocal(context, BaseInfoManager.getUserId(context));
        if (localProject != null) {
            String mapExtent = localProject.getProjectMapParam().getMapExtent();
            String[] extents = mapExtent.split(",");
            double[] resolution = new double[extents.length + 1];
            if (resolution.length >= 2) {
                return resolution[resolution.length - 2];
            }
        }
        return -1;
    }

    @Override
    public ProjectRouter getProjectRouter() {
        return mRouter;
    }

    @Override
    public void setProjectRouter(ProjectRouter projectRouter) {
        this.mRouter = projectRouter;
    }
}
