package com.augurit.agmobile.patrolcore.common.opinion.view.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.agmobile.patrolcore.common.opinion.service.OpinionService;
import com.augurit.agmobile.patrolcore.common.opinion.view.IOpinionTemplateEditView;
import com.augurit.am.cmpt.common.Callback1;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.view.presenter
 * @createTime 创建时间 ：2017-07-18
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-18
 * @modifyMemo 修改备注：
 */

public class OpinionTemplateEditPresenter implements IOpinionTemplateEditPresenter {

    private Context mContext;
    private IOpinionTemplateEditView mOpinionTemplateEditView;
    private OpinionService mOpinionService;
    private Callback1 mBackListener;

    protected ProgressDialog pd;

    public OpinionTemplateEditPresenter(Context context, IOpinionTemplateEditView opinionTemplateEditView){
        this.mContext = context;
        this.mOpinionTemplateEditView = opinionTemplateEditView;
        this.mOpinionTemplateEditView.setPresenter(this);
        mOpinionService = new OpinionService(mContext);
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

    @Override
    public void saveOpinionTemplate(final OpinionTemplate opinionTemplate) {
        mOpinionService.saveOpinionToServer(opinionTemplate.getId(),
                opinionTemplate.getName(), opinionTemplate.getContent(),
                opinionTemplate.getProjectId() ,
                opinionTemplate.getLink(), opinionTemplate.getAuthorization())
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
                        mOpinionTemplateEditView.showMessage("保存失败，请重试");
                    }

                    @Override
                    public void onNext(String s) {
                        pd.dismiss();
                        mOpinionTemplateEditView.showMessage("保存成功");
                        opinionTemplate.setUpload(true);
                        mOpinionService.saveOpinionToLocal(opinionTemplate);
                        if(mBackListener != null){
                            mBackListener.onCallback(null);
                        }
                    }
                });

    }

    public void setBackListener(Callback1 callback){
        this.mBackListener = callback;
    }
}
