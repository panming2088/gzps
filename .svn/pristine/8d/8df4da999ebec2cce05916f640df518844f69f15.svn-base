package com.augurit.agmobile.patrolcore.common.action.dao.remote;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.action.model.ActionConfigureModel;
import com.augurit.agmobile.patrolcore.common.action.model.ActionModel;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.ControlType;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 网络获取
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.common.action.service
 * @createTime 创建时间 ：2017-06-12
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-06-12
 * @modifyMemo 修改备注：
 */
public class ActionNetLogic {

    private Context mContext;
    private AMNetwork mAMNetwork;
    private TableDBService mTableDBService;

    public ActionNetLogic(Context context, String baseUrl) {
        mContext = context;
        initAMNetwork(baseUrl);
        mTableDBService = new TableDBService(context);
    }

    /**
     * 获取角色功能列表接口
     *
     * @return
     */
    public Observable<List<ActionModel>> getAllfeatures() {
        String roleId = "d58da66b-bf2a-4ccd-abb0-1d2e6e6b307b";//todo 之后在baseInfoManager中加入获取角色id的方法
        BaseInfoManager manager = new BaseInfoManager();
        ActionsApi getAllDicsApi = (ActionsApi) mAMNetwork.getServiceApi(ActionsApi.class);
        return getAllDicsApi.getAllfeatures(BaseInfoManager.getUserId(mContext)).subscribeOn(Schedulers.io());
        //  return getAllDicsApi.getAllfeatures("1176").subscribeOn(Schedulers.io());
    }

    public void initAMNetwork(String url) {
        if (mAMNetwork == null) {
            mAMNetwork = new AMNetwork(url);
            mAMNetwork.addLogPrint();
            mAMNetwork.build();
            mAMNetwork.registerApi(ActionsApi.class);
        }
    }

    /**
     * 获取某个功能的操作项接口
     *
     * @return
     */
    public Observable<List<TableItem>> getOperationItemByCode(String featureCode, String actionNo) {
        ActionsApi getAllDicsApi = (ActionsApi) mAMNetwork.getServiceApi(ActionsApi.class);
        return getAllDicsApi.getOperationItemByCode(featureCode,actionNo);
    }

    /**
     * 更新数据集
     *
     * @return
     */
    public Observable<List<DictionaryItem>> updateDataSet() {
        ActionsApi getAllDicsApi = (ActionsApi) mAMNetwork.getServiceApi(ActionsApi.class);
        return getAllDicsApi.getAllDic();
    }


    private Observable<List<TableItem>> getOperationItemsByFeatureCode(final String featureCode, final String actionId) {
        final ActionsApi getAllDicsApi = (ActionsApi) mAMNetwork.getServiceApi(ActionsApi.class);
        return getAllDicsApi.getOperationItemByCode(featureCode,actionId)
                .map(new Func1<List<TableItem>, List<TableItem>>() {
                    @Override
                    public List<TableItem> call(List<TableItem> actionConfigureItems) {
                        for (TableItem tableItem : actionConfigureItems) {
                            //如果是复选框或者下拉框，那么是采用数据字典，将数据字典的json对象返回，具体功能模块自己进行解析成需要的对象；
                            if (tableItem.getControl_type().equals(ControlType.CHEXK_BOX) || tableItem.getControl_type().equals(ControlType.SPINNER)) {
                                List<DictionaryItem> dictionaryByCode = mTableDBService.getDictionaryByTypecodeInDB(tableItem.getValue());
                                tableItem.setValue(JsonUtil.getJson(dictionaryByCode));
                            }
                            tableItem.setFeatureCode(featureCode);
                            tableItem.setId(actionId);
                        }
                        return actionConfigureItems;
                    }
                }).subscribeOn(Schedulers.io());
    }


    /**
     * 更新所有功能模块的配置
     */
    public Observable<List<TableItem>> updateConfigure() {
        ActionsApi getAllDicsApi = (ActionsApi) mAMNetwork.getServiceApi(ActionsApi.class);
        return getAllDicsApi.getAllOperationItems(BaseInfoManager.getUserId(mContext))
                .map(new Func1<List<ActionConfigureModel>, List<TableItem>>() {
                    @Override
                    public List<TableItem> call(List<ActionConfigureModel> actionConfigureItems) {

                        List<TableItem> modelConfigure = new ArrayList<TableItem>();
                        for (ActionConfigureModel actionConfigureModel : actionConfigureItems) {
                            List<TableItem> operationItems = actionConfigureModel.getOperationItems();
                            if (!ListUtil.isEmpty(operationItems)) {
                                int i = 0;
                                for (TableItem tableItem : operationItems) {
                                    //如果是复选框或者下拉框，那么是采用数据字典，将数据字典的json对象返回，具体功能模块自己进行解析成需要的对象；
                                    if (tableItem.getControl_type().equals(ControlType.CHEXK_BOX) || tableItem.getControl_type().equals(ControlType.SPINNER)) {
                                        List<DictionaryItem> dictionaryByCode = mTableDBService.getDictionaryByTypecodeInDB(tableItem.getValue());
                                        tableItem.setValue(JsonUtil.getJson(dictionaryByCode));
                                    }
                                    tableItem.setFeatureCode(actionConfigureModel.getAction_code());
                                    tableItem.setDevice_id(actionConfigureModel.getAction_no());
                                    tableItem.setId(i++ +"");
                                }
                                modelConfigure.addAll(operationItems);
                            }

                        }

                        return modelConfigure;
                    }
                }).subscribeOn(Schedulers.io());
    }

}
