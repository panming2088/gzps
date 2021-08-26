package com.augurit.agmobile.patrolcore.common.opinion.view.presenter;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.agmobile.patrolcore.common.opinion.service.OpinionService;
import com.augurit.agmobile.patrolcore.common.opinion.view.IOpinionTemplateListView;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.log.util.NetUtil;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.view.presenter
 * @createTime 创建时间 ：2017-07-17
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-17
 * @modifyMemo 修改备注：
 */

public class OpinionTemplateListPresenter implements IOpinionTemplateListPresenter {

    private Context mContext;
    private OpinionService mOpinionService;
    private IOpinionTemplateListView mOpinionTemplateListView;
    private Callback1<OpinionTemplate> mOnTemplateSelectListener;

    private int curPageNo = 1;
    private final int pageSize = 10;

    public OpinionTemplateListPresenter(Context context, IOpinionTemplateListView opinionTemplateListView){
        this.mContext = context;
        this.mOpinionTemplateListView = opinionTemplateListView;
        mOpinionService = new OpinionService(mContext);
        this.mOpinionTemplateListView.setPresenter(this);
        getOpinionTemplate(curPageNo, pageSize);
    }

    @Override
    public void refresh(){
        mOpinionTemplateListView.setRefreshOrLoadMore(false);
        getOpinionTemplate(1, curPageNo*pageSize);
    }

    @Override
    public void loadMore(){
        curPageNo++;
        getServerOpinionTemplate(curPageNo, pageSize);
    }

    private void getOpinionTemplate(int pageNo, int pageSize){
        final List<OpinionTemplate> notUploadTemplate = getLocalNotUploadOpinionTemplate();
        if(NetUtil.isConnected(mContext)){
            mOpinionService.getOpinion("", "", "", pageNo, pageSize)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<OpinionTemplate>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(List<OpinionTemplate> opinionTemplates) {
                            if(notUploadTemplate == null){
                                mOpinionTemplateListView.showTemplates(opinionTemplates);
                            } else {
                                notUploadTemplate.addAll(opinionTemplates);
                                mOpinionTemplateListView.showTemplates(notUploadTemplate);
                            }
                        }
                    });
        } else {
            mOpinionTemplateListView.showTemplates(notUploadTemplate);
        }
    }

    /**
     * 意见模板查询
     */
    private void getServerOpinionTemplate(int pageNo, int pageSize) {
        if(NetUtil.isConnected(mContext)){
            mOpinionService.getOpinion("", "", "", pageNo, pageSize)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<OpinionTemplate>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(List<OpinionTemplate> opinionTemplates) {
                            mOpinionTemplateListView.showTemplates(opinionTemplates);
                        }
                    });
        }
    }

    /**
     * 查询本地意见模板
     */
    private List<OpinionTemplate> getLocalNotUploadOpinionTemplate() {
        return mOpinionService.getNotUploadOpinions();
    }

    @Override
    public void onTemplateSelect(OpinionTemplate opinionTemplate) {
        if(mOnTemplateSelectListener != null){
            mOnTemplateSelectListener.onCallback(opinionTemplate);
        }
    }

    @Override
    public void setOnTemplateSelectListener(Callback1<OpinionTemplate> callback) {
        this.mOnTemplateSelectListener = callback;
    }
}
