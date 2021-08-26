package com.augurit.agmobile.gzps.common.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.project.view.IProjectListView;
import com.augurit.agmobile.patrolcore.project.view.IProjectPresenter;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.util.NetworkUtil;
import com.augurit.am.fw.utils.StringUtil;

import java.util.ArrayList;
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

public class ProjectPresenter2 implements IProjectPresenter {
    private Context context;
    private IProjectListView localProblemView;
    private ViewGroup container;

    private String componentName;

    public ProjectPresenter2(Context context, ViewGroup container, String componentName){
        this.context = context;
        this.container = container;
        this.componentName = componentName;
        localProblemView = new ProjectListView2(context);
        showProjectListView();
    }

    @Override
    public void showProjectListView() {
        final TableDataManager tableDataManager = new TableDataManager(context);
       // List<LocalTable> problems = tableDataManager.getEditedTableItemsFromDB();
        String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        String userId =BaseInfoManager.getUserId(context);
        //旧接口，已废弃
        //String  url = serverUrl +"rest/patrol/getAllProject?userId=" + userId;
        String url = serverUrl + "rest/patrol/getAllProject/" + userId;
        NetworkUtil.setContext(context);
        if(NetworkUtil.isNetworkAvailable()) {
            final ProgressDialog progressDialog = ProgressDialog.show(context, "提示", "正在更新数据中");
            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (KeyEvent.KEYCODE_BACK == keyCode) {
                        progressDialog.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            tableDataManager.getAllProjectByNet(url, new TableNetCallback() {
                @Override
                public void onSuccess(Object data) {
                    List<Project> projects = (List<Project>) data;
                    List<Project> oneProject = new ArrayList<Project>();
                    tableDataManager.setProjectToDB(projects);
                    if(!StringUtil.isEmpty(componentName)){
                        Project p = null;
                        for(Project project : projects){
                            if(project.getName().equals(componentName)){
                                p = project;
                                break;
                            }
                        }
                        if(p != null){
                            oneProject.add(p);
                            localProblemView.onShowProjectListView(oneProject, container);
                        } else {
                            localProblemView.onShowProjectListView(projects, container);
                        }
                    } else {
                        localProblemView.onShowProjectListView(projects, container);
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onError(String msg) {
                    progressDialog.dismiss();
                }
            });
        }else {
            List<Project> projects = tableDataManager.getProjectFromDB();
            List<Project> oneProject = new ArrayList<Project>();
            if(!StringUtil.isEmpty(componentName)){
                Project p = null;
                for(Project project : projects){
                    if(project.getName().equals(componentName)){
                        p = project;
                        break;
                    }
                }
                if(p != null){
                    oneProject.add(p);
                    localProblemView.onShowProjectListView(oneProject, container);
                } else {
                    localProblemView.onShowProjectListView(projects, container);
                }
            } else {
                localProblemView.onShowProjectListView(projects, container);
            }
        }

    }
}
