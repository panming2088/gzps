package com.augurit.agmobile.patrolcore.survey.dao;

import android.content.Context;
import android.text.TextUtils;


import com.augurit.agmobile.patrolcore.common.opinion.model.TemplateResult;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.AllFormTableItems;
import com.augurit.agmobile.patrolcore.survey.constant.SurveyDirIdConstant;
import com.augurit.agmobile.patrolcore.survey.model.GridItem;
import com.augurit.agmobile.patrolcore.survey.model.ServerTable;

import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;
import com.augurit.agmobile.patrolcore.survey.model.ServerTask;
import com.augurit.agmobile.patrolcore.survey.model.SignTaskResult;
import com.augurit.agmobile.patrolcore.survey.model.SubmitTaskResult;
import com.augurit.agmobile.patrolcore.survey.model.SurveyLocation;
import com.augurit.agmobile.patrolcore.survey.model.SurveyTemplateResult;

import com.augurit.agmobile.patrolcore.survey.model.Template;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.echart.PieEChart;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.realm.RealmList;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.dao
 * @createTime 创建时间 ：17/8/22
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/22
 * @modifyMemo 修改备注：
 */

public class SurveyRemoteDao {


    private Context mContext;

    private AMNetwork mAMNetwork;

    protected SurveyApi tableApi;

    public SurveyRemoteDao(Context context) {
        this.mContext = context;
    }

    /**
     * 获取房屋（栋）列表
     *
     * @param dirId
     * @param taskId
     * @return
     */
    public Observable<List<ServerTable>> getDongTableList(String dirId, final String taskId) {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        String url = "";

        if (!TextUtils.isEmpty(taskId)) { //如果taskId为空，表示请求所有任务的所有栋

            url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" + dirId + "&recordId=" + taskId;

        } else {

            url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" + dirId;
        }


        LogUtil.d("获取房屋（栋）列表的url：" + url);

        return tableApi.getBuildTableList(url)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取未分配给该用户的栋列表
     *
     * @param dirId
     * @return
     */
    public Observable<List<ServerTable>> getNotDesignatedDongTableList(String dirId, int page, int rows) {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        String url =  BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginNameOrg?acceptUser=" + userName
                + "&dirId=" + dirId + "&page=" + page + "&rows=" + rows;


        LogUtil.d("获取未分配给该用户的栋列表的url：" + url);

        return tableApi.getBuildTableList(url)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 删除记录
     *
     * @param recordId
     * @return
     */
    public Observable<String> deleteRecord(String recordId) {

        initAMNetwork();
        String url = BaseInfoManager.getBaseServerUrl(mContext) + "rest/multitable/deleteRecord?taskId=" + recordId;

        LogUtil.d("获取房屋（栋）列表的url：" + url);

        return tableApi.deleteRecord(url)
                .subscribeOn(Schedulers.io());
    }


    /**
     * 获取房屋（套）列表
     *
     * @param dirId    目录id
     * @param recordId 房屋（栋）id
     * @return 房屋（套）列表
     */
    public Observable<List<ServerTable>> getTaoList(String dirId, String recordId) {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        String url = "";

        if (TextUtils.isEmpty(recordId)) { //没有记录,那么默认会返回所有地址下的所有套；

            url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" + dirId;

        } else { //有记录，那么返回某个栋下的所有套

            url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" + dirId + "&recordId=" + recordId;

        }


        LogUtil.d("获取房屋（套）列表的url：" + url);

        return tableApi.getSuiteList(url)
                .subscribeOn(Schedulers.io());
    }


    /**
     * 获取未分配给该用户的房屋（套）列表
     *
     * @param dirId    目录id
     * @param recordId 房屋（栋）id
     * @return 房屋（套）列表
     */
    public Observable<List<ServerTable>> getNotDesignatedTaoList(String dirId, String recordId) {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        String url = "";

        if (TextUtils.isEmpty(recordId)) { //没有记录,那么默认会返回所有地址下的所有套；

            url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginNameOrg?acceptUser=" + userName + "&dirId=" + dirId;

        } else { //有记录，那么返回某个栋下的所有套

            url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginNameOrg?acceptUser=" + userName + "&dirId=" + dirId + "&recordId=" + recordId;

        }


        LogUtil.d("获取未分配给该用户的房屋（套）列表的url：" + url);

        return tableApi.getSuiteList(url)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取人口或者单位信息表
     *
     * @param dirId    目录id
     * @param recordId 房屋（套）记录id
     * @param ifDong   recordId是栋的id还是套的id，true表示栋，false表示套
     * @return 人口或者单位表
     */
    public Observable<List<ServerTable>> getRenkou(String dirId, String recordId, boolean ifDong) {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        String url = null;

        if (SurveyDirIdConstant.SHI_YOU_DAN_WEI.equals(dirId)) { //如果是实有单位，那么要判断recordId到底是栋id还是套id,如果是栋id，可以不传recordType；如果是套，必须传
            if (ifDong) {

                url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" + dirId + "&recordId=" + recordId;

            } else {

                url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" + dirId + "&recordId=" + recordId
                        + "&recordType=1";
            }
        } else { //如果是人口
            url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" + dirId + "&recordId=" + recordId;
        }


        LogUtil.d("获取人口或者单位信息表的url：" + url);

        return tableApi.getRemainingTableList(url)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取人口或者单位表
     *
     * @param recordId 房屋（套）记录id
     * @param ifDong   recordId是栋的id还是套的id，true表示栋，false表示套
     * @return 人口或者单位表
     */
    public Observable<List<ServerTable>> getOnlyDanwei(String recordId, boolean ifDong) {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        String dirId = SurveyDirIdConstant.SHI_YOU_DAN_WEI;

        String url = null;

        if (SurveyDirIdConstant.SHI_YOU_DAN_WEI.equals(dirId)) { //如果是实有单位，那么要判断recordId到底是栋id还是套id,如果是栋id，可以不传recordType；如果是套，必须传
            if (ifDong) {

                url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" + dirId + "&recordId=" + recordId;

            } else {

                url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" + dirId + "&recordId=" + recordId
                        + "&recordType=1";
            }
        } else { //如果是人口
            url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" + dirId + "&recordId=" + recordId;
        }


        LogUtil.d("剩余信息表的url：" + url);

        return tableApi.getOnlyDanwei(url)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 只获取人口表
     *
     * @param recordId   单位记录的id
     * @param ifXueSheng 返回从业人员的记录还是学生的记录，true表示返回学生记录；false表示返回从业人员记录；
     * @return 人口表
     */
    public Observable<List<ServerTable>> getOnlyRenkou(String recordId, boolean ifXueSheng) {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        String dirId = SurveyDirIdConstant.SHI_YOU_DAN_WEI_REN_KOU;

        String url = null;

        if (ifXueSheng) {

            url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" +
                    dirId + "&recordId=" + recordId; //学生

        } else {

            url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" + dirId + "&recordId=" + recordId
                    + "&recordType=2"; //从业人员
        }


        LogUtil.d("剩余信息表的url：" + url);

        return tableApi.getOnlyDanwei(url)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 只获取网格表
     *
     * @return 人口表
     */
    public Observable<List<ServerTable>> getWangge() {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        String dirId = SurveyDirIdConstant.WANG_GE_XIN_XI;

        String url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getSignTaskTDetalByloginName?acceptUser=" + userName + "&dirId=" +
                dirId;


        LogUtil.d("网格表的url：" + url);

        return tableApi.getWangge(url)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取地址核查中的候选网格
     * @return
     */
    public Observable<List<GridItem>> getGridItems() {
        initAMNetwork();
        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        String url = BaseInfoManager.getBaseServerUrl(mContext) + "am/task/getCheckZz?loginName=" + userName;
        LogUtil.d("候选网格的url：" + url);
        return tableApi.getGridItem(url).subscribeOn(Schedulers.io());
    }

    /**
     * 获取地址核查中待完善地址
     * @return
     */
    public Observable<List<SurveyLocation>> getSurveyLocation(int page, int rows) {
        initAMNetwork();
        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        String url = BaseInfoManager.getBaseServerUrl(mContext) + "am/task/checkAddress?loginName=" + userName
                + "&page=" + page
                + "&rows=" + rows;
        LogUtil.d("待完善地址的url：" + url);
        return tableApi.getSurveyLocations(url).subscribeOn(Schedulers.io());
    }

    /**
     * 待签收任务
     *
     * @return
     */
    public Observable<List<ServerTask>> getUnAcceptedTasks() {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        String url = BaseInfoManager.getBaseServerUrl(mContext) + "am/task/getTaskHy?acceptUser=" + userName +
                "&stata=1";

        LogUtil.d("待签收任务的url：" + url);
        return tableApi.getUnAcceptedTasks(url);

    }

    /**
     * 待签收任务
     *
     * @return
     */
    public Observable<List<ServerTask>> getNoDesignatedTaskList() {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        return tableApi.getNoDesignatedTaskList(userName);
    }


    /**
     * 已签收任务
     *
     * @return
     */
    public Observable<List<ServerTask>> getAcceptedTasks() {

        initAMNetwork();
        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        String url = BaseInfoManager.getBaseServerUrl(mContext) + "am/task/getTaskHy?acceptUser=" + userName +
                "&stata=2";
        LogUtil.d("已签收任务的url：" + url);

        return tableApi.getAcceptedTasks(url);
    }

    /**
     * 已审核任务
     *
     * @return
     */
    public Observable<List<ServerTask>> getFinishedTask() {

        initAMNetwork();
        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        String url = BaseInfoManager.getBaseServerUrl(mContext) + "am/task/getTaskHy?acceptUser=" + userName +
                "&stata=3";

        LogUtil.d("已审核任务的url：" + url);
        //return taskApi.getAcceptedTasks(BaseInfoManager.getUserId(mContext));
        return tableApi.getAcceptedTasks(url);
    }

    /**
     * 签收任务
     *
     * @param id
     * @return
     */
    public Observable<SignTaskResult> signTask(String id) {
        initAMNetwork();


        return tableApi.signTask(id);
    }

    public Observable<SubmitTaskResult> sumbitTask(String id) {
        initAMNetwork();

        return tableApi.submitTask(id);
    }

    /**
     * 签收全部任务
     *
     * @return
     */
    public Observable<SignTaskResult> signAllTask() {
        initAMNetwork();

        return tableApi.signAllTask();
    }


    /**
     * 下载离线数据，包括：任务里面的栋，套，人口，单位等；
     *
     * @return
     */
    public Observable<List<ServerTable>> getAllOfflineTasks() {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

       // String url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getTasksOneOffLineByAcceptUser?acceptUser=zhangjf&ids=PCS3707201708160000000308639093";
        return tableApi.getAllOfflineTasks(userName);
        //return tableApi.downloadAllTasks(url);

    }

    /**
     * 下载特定任务的离线数据，包括：任务里面的栋，套，人口，单位等；
     *
     * @return
     */
    public Observable<List<ServerTable>> getOfflineTasks(List<ServerTask> serverTasks) {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        String ids = "";

        for (int i = 0; i < serverTasks.size(); i++) {
            if (i == serverTasks.size() - 1) {
                ids += serverTasks.get(i).getId();
            } else {
                ids += serverTasks.get(i).getId() + ",";
            }
        }

        //String url = BaseInfoManager.getBaseServerUrl(mContext) + "am/multitable/getTasksOneOffLineByAcceptUser?acceptUser=zhangjf&ids=PCS3707201708160000000308639093";
        //return tableApi.downloadAllTasks(userName);
        return tableApi.getOfflineTasks(userName,ids);

    }
    /**
     * 获取消防表数据
     *
     * @return
     */
    public Observable<List<ServerTable>> getXiaofang(String dongId) {

        initAMNetwork();

        String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        return tableApi.getXiaofang(dongId);

    }


    /**
     * 下载离线数据，包括：任务里面的栋，套，人口，单位等；
     *
     * @return
     */
    public Observable<AllFormTableItems> getAllProjectTemplate() {

        initAMNetwork();

        //String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        return tableApi.getAllProjectTemplate();

    }


    public void initAMNetwork() {
        if (mAMNetwork == null) {
            if (BaseInfoManager.getBaseServerUrl(mContext).equals("http:///")) {
                mAMNetwork = new AMNetwork("http://192.168.32.21:8084/agweb/");
            } else {
                mAMNetwork = new AMNetwork(BaseInfoManager.getBaseServerUrl(mContext));
            }
            // mAMNetwork.addLogPrint();
            mAMNetwork.setConnectTime(10000);
            mAMNetwork.addRxjavaConverterFactory();
            mAMNetwork.addLogPrint();
            mAMNetwork.build();
            mAMNetwork.registerApi(SurveyApi.class);
            tableApi = (SurveyApi) mAMNetwork.getServiceApi(SurveyApi.class);
        }

    }

}
