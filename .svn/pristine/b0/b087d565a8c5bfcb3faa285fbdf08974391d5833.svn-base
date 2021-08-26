package com.augurit.agmobile.patrolcore.common.opinion.view.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.agmobile.patrolcore.common.opinion.service.OpinionService;
import com.augurit.agmobile.patrolcore.common.opinion.util.OpinionConstant;
import com.augurit.agmobile.patrolcore.common.opinion.view.IOpinionTemplateView;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.ControlType;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.view.presenter
 * @createTime 创建时间 ：2017-07-11
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-11
 * @modifyMemo 修改备注：
 */

public class OpinionTemplatePresenter implements IOpinionTemplatePresenter {
    private Context mContext;
    private IOpinionTemplateView mOpinionTemplateView;
    private TableViewManager mTableViewManager;
    private OpinionService mOpinionService;

    private String mProjectId;
    private String mLink;
    private OpinionTemplate currOpinionTemplate;  //当前选中的意见模板
    protected ProgressDialog pd;

    public OpinionTemplatePresenter(Context context, IOpinionTemplateView opinionTemplateView, TableViewManager tableViewManager) {
        mContext = context;
        mOpinionTemplateView = opinionTemplateView;
        mTableViewManager = tableViewManager;
        mProjectId = mTableViewManager.getProjectId();
        mLink = mTableViewManager.getLink();
        if(TextUtils.isEmpty(mLink)){
            mLink = OpinionConstant.REPORT_LINK_NAME;
        }
        mOpinionTemplateView.setPresenter(this);
        mOpinionService = new OpinionService(mContext);
        //获取三个意见模板，显示在候选列表中
        mOpinionService.getOpinion("", mProjectId, mLink, 1, 3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OpinionTemplate>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
//                        mOpinionTemplateView.showMessage("查询失败");
                    }

                    @Override
                    public void onNext(List<OpinionTemplate> opinionTemplates) {
                        mOpinionTemplateView.showOptionalTemplates(opinionTemplates);
                    }
                });
        pd = new ProgressDialog(mContext);
        pd.setMessage("保存中...");
        pd.setCancelable(false);
        pd.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode) {
                    pd.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 意见模板模糊查询
     *
     * @param key
     */
    @Override
    public void searchOpinionTemplateWithKey(String key) {
        mOpinionService.getOpinion(key, mProjectId, mLink, 1, 20)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OpinionTemplate>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
//                        mOpinionTemplateView.showMessage("查询失败");
                    }

                    @Override
                    public void onNext(List<OpinionTemplate> opinionTemplates) {
                        mOpinionTemplateView.showTemplates(opinionTemplates);
                    }
                });

    }

    @Override
    public void getAllOpinionTemplate(int pageNo, int pageSize) {
        mOpinionService.getOpinion("", mProjectId, mLink, pageNo, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OpinionTemplate>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
//                        mOpinionTemplateView.showMessage("查询失败");
                    }

                    @Override
                    public void onNext(List<OpinionTemplate> opinionTemplates) {
                        mOpinionTemplateView.onLoadMore(opinionTemplates);
                    }
                });
    }

    @Override
    public void saveOpinionTemplate(final String name, final String opinion, final String auth) {
        final String id = mOpinionService.getUUID();
        mOpinionService.saveOpinionToServer(id, name, opinion, mProjectId, mLink, auth)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        pd.show();
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        pd.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        mOpinionTemplateView.showMessage("保存失败，请重试");
                        mOpinionService.saveOpinionToLocal(id, name, opinion, mProjectId, mLink, auth, false);
                    }

                    @Override
                    public void onNext(String s) {
                        pd.dismiss();
                        mOpinionTemplateView.showMessage("保存成功");
                        mOpinionService.saveOpinionToLocal(id, name, opinion, mProjectId, mLink, auth, true);
                    }
                });

    }

    @Override
    public void setCurrOpinionTemplate(OpinionTemplate currOpinionTemplate) {
        this.currOpinionTemplate = currOpinionTemplate;
    }

    /**
     * 处理模板规则
     *
     * @param template
     * @return
     */
    @Override
    public String handleTemplate(String template) {
        template = handleTemplateExceptField(template);
        template = handleTemplateField(template);
        return template;
    }

    /**
     * 获取表单中用户输入或选中的值
     *
     * @param key 表单名称
     * @return
     */
    private String getInputValue(String key) {
        TableItem tableItem = mTableViewManager.getTableItemByHtmlName(key);
        if (tableItem == null) {
            return "";
        }
        if (tableItem.getControl_type().equals(ControlType.SPINNER)) {
            //表单项为下拉框，value为数据字典编码，从本地数据库中查找对应的显示值
            return mOpinionService.getSpinnerValueByCode(tableItem.getValue());
        } else {
            return tableItem.getValue();
        }
    }

    /**
     * 处理固定的模板标识部份，如日期、时间、用户名等
     *
     * @param template 模板
     * @return 替换后的意见
     */
    private String handleTemplateExceptField(String template) {
        return mOpinionService.handleTemplateExceptField(template);
    }

    /**
     * 处理模板中的表单字段部分
     *
     * @param template 模板
     * @return 替换后的字段
     */
    private String handleTemplateField(String template) {
        if (!template.contains(OpinionConstant.FIELDRULESTART)) {
            return template;
        }
        int startIndex = template.indexOf(OpinionConstant.FIELDRULESTART);
        int entIndex = template.indexOf(OpinionConstant.FIELDRULEEND) + 3;
        final String fieldsTemp = template.substring(startIndex, entIndex);
        String fieldsTempResult = fieldsTemp;
        fieldsTempResult = fieldsTempResult.replace(OpinionConstant.FIELDRULESTART, "");
        fieldsTempResult = fieldsTempResult.replace(OpinionConstant.FIELDRULEEND, "");
        String[] fieldsArr = fieldsTempResult.split("\\+");
        //遍历规则中的字段名，替换为对应地表单值
        for (String field : fieldsArr) {
            String fieldValue = getInputValue(field);
            if (TextUtils.isEmpty(fieldValue)) {
                fieldsTempResult = fieldsTempResult.replace(field, "");
            } else {
                fieldsTempResult = fieldsTempResult.replace(field, fieldValue);
            }
        }
        fieldsTempResult = fieldsTempResult.replace(OpinionConstant.FIELDRULESEP, "");
//        fieldsTempResult = fieldsTempResult.replace("[[[", "");
//        fieldsTempResult = fieldsTempResult.replace("]]]", "");
        //把意见模板中的字段规则替换为处理完成的字符串
        template = template.replace(fieldsTemp, fieldsTempResult);
        return handleTemplateField(template);   //递归处理模板规则
    }

}
