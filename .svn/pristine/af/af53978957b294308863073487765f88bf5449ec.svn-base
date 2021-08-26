package com.augurit.agmobile.patrolcore.survey.dao;


import com.augurit.agmobile.patrolcore.common.table.dao.remote.AllFormTableItems;
import com.augurit.agmobile.patrolcore.survey.model.GridItem;
import com.augurit.agmobile.patrolcore.survey.model.ServerTable;

import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;
import com.augurit.agmobile.patrolcore.survey.model.ServerTask;
import com.augurit.agmobile.patrolcore.survey.model.SignTaskResult;
import com.augurit.agmobile.patrolcore.survey.model.SubmitTaskResult;
import com.augurit.agmobile.patrolcore.survey.model.SurveyLocation;
import com.augurit.agmobile.patrolcore.survey.model.SurveyTemplateResult;


import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.dao
 * @createTime 创建时间 ：17/8/22
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/22
 * @modifyMemo 修改备注：
 */

public interface SurveyApi {


    /**
     * 过滤
     *
     * @param map 请求参数
     * @return
     */
    //@GET("am/multitable/getTaskByTaskId")
   // Observable<List<ServerTable>> filter(@QueryMap Map<String, String> map);

    /**
     * 获取所有表单
     *
     * @param url
     * @return
     */
   // @GET
    //Observable<SurveyTemplateResult> getProjectList(@Url String url);


   // @GET
    //Observable<List<ServerTable>> getRecords(@Url String url);

    /**
     * 获取房屋（栋）列表
     *
     * @param url
     * @return
     */
    @GET
    Observable<List<ServerTable>> getBuildTableList(@Url String url);

    /**
     * 删除记录
     *
     * @param url
     * @return
     */
    @GET
    Observable<String> deleteRecord(@Url String url);

    /**
     * 获取实有人口
     *
     * @param url
     * @return
     */
    @GET
    Observable<List<ServerTable>> getShiyouRenkouList(@Url String url);


    /**
     * 获取消防表数据
     *
     * @param dongId 栋的id
     * @return
     */
    @GET("am/multitable/getFireMeter")
    Observable<List<ServerTable>> getXiaofang(@Query("recordId") String dongId);

    /**
     * 获取实有单位
     *
     * @param url
     * @return
     */
    @GET
    Observable<List<ServerTable>> getShiyouDanwei(@Url String url);

    /**
     * 获取房屋（套）列表
     *
     * @param url
     * @return
     */
    @GET
    Observable<List<ServerTable>> getSuiteList(@Url String url);

    /**
     * 获取剩下的信息表
     *
     * @param url
     * @return
     */
    @GET
    Observable<List<ServerTable>> getRemainingTableList(@Url String url);

    /**
     * @param url
     * @return
     */
    @GET
    Observable<List<ServerTable>> getOnlyDanwei(@Url String url);

    /**
     * 获取网格
     * @param url
     * @return
     */
    @GET
    Observable<List<ServerTable>> getWangge(@Url String url);

    /**
     * 获取地址核查中的候选网格
     * @param url
     * @return
     */
    @GET
    Observable<List<GridItem>> getGridItem(@Url String url);

    /**
     * 获取地址核查中待完善地址
     * @param url
     * @return
     */
    @GET
    Observable<List<SurveyLocation>> getSurveyLocations(@Url String url);

    /**
     * 待签收任务列表
     *
     * @return
     */
    @GET()
    Observable<List<ServerTask>> getUnAcceptedTasks(@Url String url);

    /**
     * 未指派的任务列表
     *
     * @return
     */
    @GET("am/task/getAddressByNleader")
    Observable<List<ServerTask>> getNoDesignatedTaskList(@Query("loginName") String loginName);

    /**
     * 下载离线数据
     *
     * @return
     */
    @GET("am/multitable/getTasksOffLineByAcceptUser")
    Observable<List<ServerTable>> getAllOfflineTasks(@Query("acceptUser") String acceptUser);

    /**
     * 获取指定任务的离线数据
     * @param acceptUser
     * @param ids 指定的任务id，用","分割
     * @return
     */
    @GET("am/multitable/getTasksOneOffLineByAcceptUser")
    Observable<List<ServerTable>> getOfflineTasks(@Query("acceptUser") String acceptUser, @Query("ids") String ids);

    /**
     * 已签收任务列表
     *
     * @param url
     * @return
     */
    @GET()
    Observable<List<ServerTask>> getAcceptedTasks(@Url String url);


    @POST("am/task/signTaskHy")
    Observable<SignTaskResult> signTask(@Query("id") String taskId);

    @GET("am/task/signTaskAll")
    Observable<SignTaskResult> signAllTask();

    /**
     * 获取所有任务的所有记录
     *
     * @return
     */
    @GET()
    Observable<List<ServerTable>> getAllReords(@Url String url);


    /**
     * 将任务由已签收变成已核实
     *
     * @param taskId
     * @return
     */
    @POST("am/task/submitTask")
    Observable<SubmitTaskResult> submitTask(@Query("id") String taskId);


    /**
     * 获取所有已完成的实有房屋
     *
     * @param url
     * @return
     */
    @GET("")
    Observable<List<ServerTable>> getFinishedBuildings(@Url String url);

    /**
     * 获取所有已完成的实有人口
     *
     * @param url
     * @return
     */
    @GET("")
    Observable<List<ServerTable>> getFinishedRenkous(@Url String url);

    /**
     * 获取所有已完成的实有单位
     *
     * @param url
     * @return
     */
    @GET("")
    Observable<List<ServerTable>> getFinishedDanweis(@Url String url);


    /**
     * 获取所有表单模板
     *
     * @param url
     * @return
     */
    @POST("am/report/allForms2")
    Observable<AllFormTableItems> getAllProjectTemplate();

}
