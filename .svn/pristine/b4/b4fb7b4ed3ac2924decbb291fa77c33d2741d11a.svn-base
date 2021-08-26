package com.augurit.agmobile.patrolcore.localhistory.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.ViewGroup;


import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBCallback;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.UploadTableItemResult;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.ControlState;
import com.augurit.agmobile.patrolcore.common.table.util.ControlType;
import com.augurit.agmobile.patrolcore.common.table.util.RequireState;
import com.augurit.agmobile.patrolcore.localhistory.util.ConvertTableUtils;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.setting.problem.view
 * @createTime 创建时间 ：17/3/21
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/21
 * @modifyMemo 修改备注：
 */

public class LocalProblemPresenter implements ILocalProblemPresenter {
    private Context context;
    private ILocalProblemView localProblemView;
    private ViewGroup container;
    private ViewGroup menuContainer; //操作按钮容器
    private ViewGroup multiContainer; //全选点击响应
    private TableDataManager tableDataManager;
    private List<LocalTable> mList;
    private ProgressDialog mProgressDialog;

    public LocalProblemPresenter(Context context,ViewGroup container,ViewGroup menuContainer,ViewGroup multiContainer){
        this.context = context;
        this.container = container;
        this.menuContainer = menuContainer;
        this.multiContainer = multiContainer;
        localProblemView = new LocalProblemView(context,this);
        tableDataManager = new TableDataManager(context);

        showLocalProblemListView();

    }

    @Override
    public void showLocalProblemListView() {
     //   TableDataManager tableDataManager = new TableDataManager(context);
        List<LocalTable> problems = tableDataManager.getEditedTableItemsFromDB();
        localProblemView.onShowLocalProblemListView(problems,container,menuContainer,multiContainer);
    }

    @Override
    public void deleteLocalTable(String key,TableDBCallback callback) {
      //  TableDataManager tableDataManager = new TableDataManager(context);
        tableDataManager.deleteEditedTableItemsFromBD(key, callback);
    }

    @Override
    public void uploadAllTable(List<LocalTable> list) {
       // List<LocalTable> list = tableDataManager.getEditedTableItemsFromDB();
        mList = list;
        if(mList == null || mList.size() <= 0) return;
        int uploadSize = mList.size();
        //显示提示框
        LocalTable temp = mList.get(0); //取出第一项保存的
        List<TableItem> tableItemList =  ConvertTableUtils.convert(temp);
        ArrayList<Photo> photos =(ArrayList<Photo>) tableDataManager.getPhotoFormDB(temp.getKey());
        String projectId = temp.getId();
        String tableKey = temp.getKey();
        Map<String, String> valueMap = new HashMap<>();
        for (TableItem tableItem : tableItemList){
            if(tableItem.getValue() != null){
                valueMap.put(tableItem.getField1(), tableItem.getValue());
            }
        }
        uploadEdit(uploadSize,tableKey,projectId,valueMap,tableItemList,photos);
    }

    public void uploadEdit(final int total,
                           final String tableKey,
                           String projectId,
                           Map<String, String> valueMap, List<TableItem> list,
                           final List<Photo> photos) {

        //一有存在空值,则马上停止批量上传
        if(checkIfEmpty(valueMap,list)){
            return;
        }

        String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        String url = serverUrl + "rest/report/save";


        final ProgressDialog progressDialog = ProgressDialog.show(context, "提示:还剩下"+total+"项", "正在提交数据");
        tableDataManager.uploadTableItems(url, projectId, valueMap, list, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                String result = null;
                try {
                    result = ((ResponseBody) data).string();
                } catch (IOException e) {
                    progressDialog.dismiss();
                    ToastUtil.longToast(context, e.getLocalizedMessage());
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                UploadTableItemResult uploadTableItemResult = gson.fromJson(result, UploadTableItemResult.class);
                if (uploadTableItemResult.getSuccess().equals("true")) {
                    ToastUtil.shortToast(context, "提交成功!");
                }
                progressDialog.dismiss();
                if (photos == null || photos.size() <= 0) {
                    afterUpload(tableKey);
                   // ((Activity) context).finish();
                } else {
                    if (uploadTableItemResult.getPatrolId() != null) {
                        uploadFiles(total,uploadTableItemResult.getPatrolId(),tableKey,photos);
                    }
                }
            }

            @Override
            public void onError(String msg) {
                progressDialog.dismiss();
                ToastUtil.longToast(context, msg);
            }
        });
    }

    /**
     * 校验是否存在必填项为空(此处没有校验图片)
     * @param valueMap
     * @param list
     * @return
     */
    public boolean checkIfEmpty( Map<String, String> valueMap, List<TableItem> list){
        boolean isEmpty = false;

        //先检查一般表格模板项
        for (TableItem tableItem : list) {
            if (tableItem.getIf_required() != null) {
                if (tableItem.getIf_required().equals(RequireState.REQUIRE)) {
                    if (valueMap.containsKey(tableItem.getField1())) {
                        String value = valueMap.get(tableItem.getField1());
                        if (value == null || value.isEmpty()) {
                            ToastUtil.longToast(context, tableItem.getHtml_name() + "填写值为空,请完善后继续!");
                            isEmpty = true;
                            break;
                        }
                    }
                }
            }
        }

        return isEmpty;
    }


    /**
     * 是否显示特殊表格模板项
     *
     * @param controlType
     * @return
     */
    public boolean isShowSpecilTableItem(String controlType,List<TableItem> list) {
        boolean isShow = false;
        if (list != null) {
            for (TableItem tableItem : list) {
                if (tableItem.getControl_type() != null && tableItem.getIf_hidden() != null) {
                    if (tableItem.getControl_type().equals(controlType)) {
                        if (tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                            isShow = true;
                        }
                    }
                }
            }
        }
        return isShow;
    }


    /**
     * 上传图片
     */
    public void uploadFiles(int total,String patrolCode, final String tableKey, List<Photo> photos) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        String url = serverUrl + "rest/upload/add";
        url = url + "/" + patrolCode;
        //   http://210.21.98.71:8088/agweb14/rest/upload/add/{patrolCode}
        if (photos == null || photos.size() <= 0) {
            ((Activity) context).finish();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(context, "提示:还剩下"+total+"项", "正在提交照片");
        tableDataManager.uploadPhotos(url, patrolCode, photos, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                // ToastUtil.shortToast(mContext,((ResponseBody)data).toString());
                ToastUtil.shortToast(context, "照片上传成功!");
                afterUpload(tableKey);

                progressDialog.dismiss();
              //  ((Activity) context).finish();
            }

            @Override
            public void onError(String msg) {
                progressDialog.dismiss();
                ToastUtil.shortToast(context, msg);
            }
        });
    }

    /**
     * 提交后删除本地保存数据
     */
    public void afterUpload(String tableKey) {
        if (tableKey == null) return;

        tableDataManager.deleteEditedTableItemsFromBD(tableKey, new TableDBCallback() {
            @Override
            public void onSuccess(Object data) {
                localProblemView.refreshListView(mList.get(0));

                if(mList.size() > 0) {
                    mList.remove(0);
                }
                uploadAllTable(mList);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

}
