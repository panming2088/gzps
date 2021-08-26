package com.augurit.agmobile.gzps.statistic.view;

import android.app.ProgressDialog;
import android.content.Context;

import com.augurit.agmobile.gzps.statistic.model.StatisticResult;
import com.augurit.agmobile.gzps.statistic.service.StatisticService;
import com.augurit.agmobile.gzps.statistic.view.conditionview.ConditionItem;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.model.TableChildItem;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.common.IPresenter;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.statistic.view
 * @createTime 创建时间 ：2017/8/15
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/8/15
 * @modifyMemo 修改备注：
 */

public class StatisticPresenter implements IPresenter {

    protected Context mContext;
    protected StatisticView mView;
    protected StatisticService mService;
    protected TableDataManager mTableDataManager;

//    private static final String BASE_URL_TEST = "http://192.168.31.7:8080/agweb/";  // TODO 临时测试用BaseUrl
    private static final String BASE_URL_TEST = "http://192.168.32.7:3001/patrol/";

    public StatisticPresenter(Context context, StatisticView statisticView, StatisticService service) {
        mContext = context;
        mView = statisticView;
        mService = service;
        mTableDataManager = new TableDataManager(context);
        mView.setPresenter(this);
    }

    public void start() {
        // 获取项目并显示项目选择
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);
        String userId =BaseInfoManager.getUserId(mContext);
        String  url = serverUrl +"rest/patrol/getAllProject?userId=" + userId;
        mTableDataManager.getAllProjectByNet(url, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                if(data == null){
                    LogUtil.e("StatisticPresenter", "没有获取到项目");
                    return;
                }
                List<Project> projects = (List<Project>) data;
                if(ListUtil.isEmpty(projects)){
                    LogUtil.e("StatisticPresenter", "没有获取到项目");
                    return;
                }
                // TODO 显示

                // 获取第一个项目的统计条件
                String projectId = projects.get(0).getId();
                getFields(projectId);
            }

            @Override
            public void onError(String msg) {
                LogUtil.e("StatisticPresenter", "获取项目失败");
                // 显示提示界面
            }
        });

    }

    protected void getFields(String projectId) {
//        String baseUrl = BASE_URL_TEST;
        String baseUrl = BaseInfoManager.getBaseServerUrl(mContext);
        String statisticFieldsUrl = baseUrl + "rest/statistic/getStatisticFields";
        String groupFieldsUrl = baseUrl + "rest/statistic/getGroupFields";
        Observable.zip(mService.getStatisticFields(statisticFieldsUrl, projectId),
                mService.getGroupFields(groupFieldsUrl, projectId),
                new Func2<List<TableItem>, List<TableItem>, Map<Integer, List<TableItem>>>() {
                    @Override
                    public Map<Integer, List<TableItem>> call(List<TableItem> tableItems, List<TableItem> tableItems2) {
                        Map<Integer, List<TableItem>> map = new HashMap<Integer, List<TableItem>>();
                        map.put(0, tableItems);     // 统计字段
                        map.put(1, tableItems2);    // 分组字段
                        return map;
                    }
                })
                .map(new Func1<Map<Integer,List<TableItem>>, Map<Integer, List<ConditionItem>>>() {
                    @Override
                    public Map<Integer, List<ConditionItem>> call(Map<Integer, List<TableItem>> tableItemMap) {
                        // 转换为ConditionItem
                        Map<Integer, List<ConditionItem>> conditionMap = new HashMap<Integer, List<ConditionItem>>();
                        for (Map.Entry<Integer, List<TableItem>> entry : tableItemMap.entrySet()) {
                            List<TableItem> tableItems = entry.getValue();
                            List<ConditionItem> conditionList = new ArrayList<ConditionItem>();
                            for (TableItem tableItem : tableItems) {
                                ConditionItem conditionItem = new ConditionItem(tableItem.getId(),
                                        tableItem.getField1(),
                                        tableItem.getHtml_name(),
                                        tableItem.getControl_type(),
                                        tableItem.getDic_code());
                                // 获取数据字典
                                if (tableItem.getDic_code() != null) {
                                    List<TableChildItem> tableChildItems = mTableDataManager.getTableChildItemsByTypeCode(tableItem.getDic_code());
                                    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                                    for (TableChildItem tableChildItem : tableChildItems) {
                                        String key = tableChildItem.getName();
//                                        String value = tableChildItem.getValue();
                                        String value = tableChildItem.getCode();
                                        if (value == null || value.isEmpty()) { // 如果没有value则使用key值
                                            value = key;
                                        }
                                        map.put(key, value);
                                    }
                                    conditionItem.setConditionValues(map);
                                    conditionList.add(conditionItem);   // TODO 目前必须为有数据字典项
                                }
                            }
                            conditionMap.put(entry.getKey(), conditionList);
                        }
                        return conditionMap;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Map<Integer, List<ConditionItem>>>() {
                    @Override
                    public void call(Map<Integer, List<ConditionItem>> map) {
                        // 显示到视图
                        mView.showConditionView(map.get(0), map.get(1));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    public void statistic(String conditionJson) {
//        String baseUrl = BASE_URL_TEST;
        String baseUrl = BaseInfoManager.getBaseServerUrl(mContext);
        String url = baseUrl + "rest/statistic/statistic";
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage("统计中，请稍候...");
        dialog.show();
        mService.statistic(url, conditionJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<StatisticResult>>() {
                    @Override
                    public void call(List<StatisticResult> results) {
                        dialog.dismiss();
                        LogUtil.i("Statistic", "size:" + results.size());
                        if (results.size() != 0) {
                            mView.showStatisticResult(results);
                        } else {
                            ToastUtil.shortToast(mContext, "没有符合条件的统计结果");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }
}
