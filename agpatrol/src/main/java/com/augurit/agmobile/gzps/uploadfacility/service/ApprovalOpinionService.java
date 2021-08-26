package com.augurit.agmobile.gzps.uploadfacility.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.uploadfacility.dao.ApprovalOpinionApi;
import com.augurit.agmobile.gzps.uploadfacility.model.ApprovalOpinion;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 审核意见相关方法
 * Created by xcl on 2017/12/7.
 */

public class ApprovalOpinionService {

    private Context mContext;

    private AMNetwork amNetwork;
    private ApprovalOpinionApi uploadLayerApi;

    public ApprovalOpinionService(Context mContext) {
        this.mContext = mContext;
    }


    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(ApprovalOpinionApi.class);
            this.uploadLayerApi = (ApprovalOpinionApi) this.amNetwork.getServiceApi(ApprovalOpinionApi.class);
        }
    }

    /**
     * 获取审核意见列表
     * @param markId
     * @param reportTypeChinese
     * @return
     */
    public Observable<List<ApprovalOpinion>> getOpinions(Long markId, String reportTypeChinese) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String reportType = "";
        if(reportTypeChinese!=null) {
            if (reportTypeChinese.contains("确认") || reportTypeChinese.equals(UploadLayerFieldKeyConstant.CONFIRM)) {
                reportType = UploadLayerFieldKeyConstant.CONFIRM;
            } else if (reportTypeChinese.contains("新增") || reportTypeChinese.equals(UploadLayerFieldKeyConstant.ADD)) {
                reportType = UploadLayerFieldKeyConstant.ADD;
            } else if (reportTypeChinese.contains("纠错") || reportTypeChinese.equals(UploadLayerFieldKeyConstant.CORRECT_ERROR)) {
                reportType = UploadLayerFieldKeyConstant.CORRECT_ERROR;
            }else{
                reportType = reportTypeChinese;
            }
        }

        return uploadLayerApi.getOpinion(markId, reportType)
                .map(new Func1<Result2<List<ApprovalOpinion>>, List<ApprovalOpinion>>() {
                    @Override
                    public List<ApprovalOpinion> call(Result2<List<ApprovalOpinion>> listResult2) {
                        return listResult2.getData();
                    }
                }).subscribeOn(Schedulers.io());
//                .onErrorReturn(new Func1<Throwable, List<ApprovalOpinion>>() {
//                    @Override
//                    public List<ApprovalOpinion> call(Throwable throwable) {
//                        return new ArrayList<>();
//                    }
//                });
    }

    /**
     * 获取审核意见列表
     * @param markId
     * @return
     */
    public Observable<List<ApprovalOpinion>> getlinePipeCheckRecord(Long markId) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        return uploadLayerApi.getlinePipeCheckRecord(markId)
                .map(new Func1<Result2<List<ApprovalOpinion>>, List<ApprovalOpinion>>() {
                    @Override
                    public List<ApprovalOpinion> call(Result2<List<ApprovalOpinion>> listResult2) {
                        return listResult2.getData();
                    }
                }).subscribeOn(Schedulers.io());
    }

}
