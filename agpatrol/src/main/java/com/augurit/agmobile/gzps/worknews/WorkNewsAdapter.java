package com.augurit.agmobile.gzps.worknews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.publicaffair.view.eventaffair.EventAffairDetailActivity;
import com.augurit.agmobile.gzps.publicaffair.view.facilityaffair.PublicAffairModifiedFacilityDetailAcitivty;
import com.augurit.agmobile.gzps.publicaffair.view.facilityaffair.PublicAffairUploadFacilityDetailAcitivty;
import com.augurit.agmobile.gzps.publicaffair.model.EventAffair;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.gzps.worknews.model.WorkNews;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xcl on 2017/10/23.
 */

public class WorkNewsAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, WorkNews> {

    private Context mContext;

    public WorkNewsAdapter(Context context, List<WorkNews> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new WorkNewsViewHolder(inflater.inflate(R.layout.item_worknews, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, final WorkNews data) {
        ((WorkNewsViewHolder) holder).tv_name.setText(data.getWorkNewsName(((WorkNewsViewHolder) holder).itemView.getContext()));
        ((WorkNewsViewHolder) holder).tv_time.setText(data.getDateStr());
//        ((WorkNewsViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (data.getType(data.getSource())) {
//                    case WorkNews.UPLOAD_FACILITY:
//                        jumpToUploadFacilityActivity(data);
//                        break;
//                    case WorkNews.MODIFY_FACILITY:
//                        jumpToModifyFacilityActivity(data);
//                        break;
//                    case WorkNews.UPLOAD_PROBLEM:
//                        jumpToUploadEventActivity(data);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
    }

    private FacilityAffairService identificationService;

    private void initService() {
        if (identificationService == null) {
            identificationService = new FacilityAffairService(mContext);
        }
    }

    /**
     * 设施纠错
     *
     * @param workNews
     */
    public void jumpToModifyFacilityActivity(WorkNews workNews) {
        initService();
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("查询详情中");
        progressDialog.show();
        identificationService.getModifiedDetail(workNews.getID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModifiedFacility>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        ToastUtil.shortToast(mContext, "查询详情失败");
                    }

                    @Override
                    public void onNext(ModifiedFacility modifyFacilityDetail) {
                        progressDialog.dismiss();
                        if (modifyFacilityDetail == null){
                            ToastUtil.shortToast(mContext, "查询详情失败");
                            return;
                        }
                        Intent intent = new Intent(mContext, PublicAffairModifiedFacilityDetailAcitivty.class);
                        intent.putExtra("data", modifyFacilityDetail);
                        mContext.startActivity(intent);
                    }
                });
    }

    /**
     *
     * 新增设施/设施确认
     *
     * @param workNews
     */
    public void jumpToUploadFacilityActivity(WorkNews workNews) {
        initService();
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("查询详情中");
        progressDialog.show();
        identificationService.getUploadDetail(workNews.getID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadedFacility>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        ToastUtil.shortToast(mContext, "查询详情失败");
                    }

                    @Override
                    public void onNext(UploadedFacility modifyFacilityDetail) {
                        progressDialog.dismiss();
                        if (modifyFacilityDetail == null){
                            ToastUtil.shortToast(mContext, "查询详情失败");
                            return;
                        }
                        Intent intent = new Intent(mContext, PublicAffairUploadFacilityDetailAcitivty.class);
                        intent.putExtra("data", (Parcelable) modifyFacilityDetail);
                        mContext.startActivity(intent);
                    }
                });
    }

    /**
     * 问题上报
     *
     * @param workNews
     */
    public void jumpToUploadEventActivity(WorkNews workNews) {
        initService();
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("查询详情中");
        progressDialog.show();
        UploadEventService uploadEventService = new UploadEventService(mContext.getApplicationContext());
        uploadEventService.getEventDetail(String.valueOf(workNews.getID()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EventAffair.EventAffairBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        ToastUtil.shortToast(mContext, "查询详情失败");
                    }

                    @Override
                    public void onNext(EventAffair.EventAffairBean eventAffairBean) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(mContext, EventAffairDetailActivity.class);
                        intent.putExtra("eventAffairBean", eventAffairBean);
                        mContext.startActivity(intent);
                    }
                });
    }

    public static class WorkNewsViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder {
        TextView tv_name;
        TextView tv_time;

        public WorkNewsViewHolder(View itemView) {
            super(itemView);
            tv_name = findView(R.id.tv_name);
            tv_time = findView(R.id.tv_time);
        }
    }
}