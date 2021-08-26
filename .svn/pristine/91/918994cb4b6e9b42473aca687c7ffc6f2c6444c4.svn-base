package com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.ApprovalOpinion;
import com.augurit.agmobile.gzps.uploadfacility.service.ApprovalOpinionService;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.ListUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.net.SocketTimeoutException;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion
 * @createTime 创建时间 ：17/12/26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/26
 * @modifyMemo 修改备注：
 */

public class InspectionOpinionViewManager {


    private View root;

    public InspectionOpinionViewManager(Context context, List<ApprovalOpinion> opinions) {

        initView(context, opinions);
    }

    public InspectionOpinionViewManager(Context context, Long markId, String reportTypeChinese) {

        initView(context, markId, reportTypeChinese);
    }

    private void initView(Context context, List<ApprovalOpinion> opinions) {
        root = LayoutInflater.from(context).inflate(R.layout.view_approval_opinions, null);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ApprovalOpinionAdapter approvalOpinionAdapter = new ApprovalOpinionAdapter(context, opinions);
        recyclerView.setAdapter(approvalOpinionAdapter);
    }


    private void initView(final Context context, Long markId, String reportTypeChinese) {
        root = LayoutInflater.from(context).inflate(R.layout.view_approval_opinions2, null);

        final ProgressLinearLayout progress_linearlayout = (ProgressLinearLayout) root.findViewById(R.id.progress_linearlayout);
        final ViewGroup ll_container = (ViewGroup) root.findViewById(R.id.ll_container);
        progress_linearlayout.showLoading();
        loadData(context, markId, reportTypeChinese, ll_container, progress_linearlayout);
    }


    private void loadData(final Context context,
                          final Long markId,
                          final String reportTypeChinese,
                          final ViewGroup container,
                          final ProgressLinearLayout progress_linearlayout) {

        final ApprovalOpinionService approvalOpinionService = new ApprovalOpinionService(context);
        approvalOpinionService.getOpinions(markId, reportTypeChinese)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ApprovalOpinion>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof SocketTimeoutException) {
                            progress_linearlayout.showError("", "连接服务器失败，请检查网络连接", null, null);
                        } else {
                            progress_linearlayout.showError("", "当前没有审核意见", null, null);
                            CrashReport.postCatchedException(new Exception("审核意见获取失败，当前用户是：" +
                                    BaseInfoManager.getUserName(context) + "原因是：" + e.getMessage()));
                        }

                    }

                    @Override
                    public void onNext(List<ApprovalOpinion> opinions) {
                        progress_linearlayout.showContent();
                        if (ListUtil.isEmpty(opinions)) {
                            progress_linearlayout.showError("", "当前没有审核意见", null, null);
                            return;
                        }
                        for (ApprovalOpinion approvalOpinion : opinions) {
                            InspectionOpinionView approvalOpinionView = new InspectionOpinionView(context, approvalOpinion);
                            approvalOpinionView.addTo(container);
                        }
//                        ApprovalOpinionAdapter approvalOpinionAdapter = new ApprovalOpinionAdapter(context, opinions);
//                        recyclerView.setAdapter(approvalOpinionAdapter);
                    }
                });
    }

    public void addTo(ViewGroup container) {
        if (container == null) {
            return;
        }
        container.addView(root);
    }


//    private void initView(final Context context, Long markId, String reportTypeChinese) {
//        root = LayoutInflater.from(context).inflate(R.layout.view_approval_opinions2, null);
//        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
//        final ProgressLinearLayout progress_linearlayout = (ProgressLinearLayout) root.findViewById(R.id.progress_linearlayout);
//
//
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//
//        progress_linearlayout.showLoading();
//        loadData(context, markId, reportTypeChinese, recyclerView, progress_linearlayout);
//    }

    //    private void loadData(final Context context,
//                          final Long markId,
//                          final String reportTypeChinese,
//                          final RecyclerView recyclerView,
//                          final ProgressLinearLayout progress_linearlayout) {
//
//        ApprovalOpinionService approvalOpinionService = new ApprovalOpinionService(context);
//        approvalOpinionService.getOpinions(markId, reportTypeChinese)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<ApprovalOpinion>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        progress_linearlayout.showError("", "获取数据失败，请检查网络连接", null, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                loadData(context, markId, reportTypeChinese, recyclerView, progress_linearlayout);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onNext(List<ApprovalOpinion> opinions) {
//                        progress_linearlayout.showContent();
//                        if (ListUtil.isEmpty(opinions)) {
//                            progress_linearlayout.showError("", "当前没有审核意见", null, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    loadData(context, markId, reportTypeChinese, recyclerView, progress_linearlayout);
//                                }
//                            });
//                            return;
//                        }
//                        ApprovalOpinionAdapter approvalOpinionAdapter = new ApprovalOpinionAdapter(context, opinions);
//                        recyclerView.setAdapter(approvalOpinionAdapter);
//                    }
//                });
//    }
}
