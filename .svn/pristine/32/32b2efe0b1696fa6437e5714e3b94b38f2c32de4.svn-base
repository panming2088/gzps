package com.augurit.agmobile.gzps.publicaffair.view.facilityaffair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.publicaffair.model.FacilityAffair;
import com.augurit.agmobile.gzps.publicaffair.model.FacilityAffairResponseBody;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.publicaffair.view.condition.FacilityAffairFilterConditionEvent;
import com.augurit.agmobile.gzps.publicaffair.view.distribute.FacilityAffairDistributeMapActivity;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xcl on 2017/11/17.
 */

public class FacilityAffairFragment extends Fragment {

    private ProgressLinearLayout pb_loading;
    private XRecyclerView rv_result;
    private int page = 1;
    private FacilityAffairAdapter facilityAffairAdapter;
    private FacilityAffairService identificationService;
    private View ll_filter;
    private boolean isShiPaiOrKexuecheng = false;



    /**
     * 筛选的区域
     */
    private String distrct = null;
    /**
     * 设施类型：窨井，雨水口等
     */
    private String facilityType = null;
    /**
     * 上报类型：设施纠错还是设施上报
     */
    private String uploadType = null;

    private Long startTime = null;

    private Long endTime = null;

    /**
     * 加载全部
     */
    private static final int LOAD_ALL_DATA = 1;
    /**
     * 加载过滤后的数据
     */
    private static final int LOAD_AFTER_FILTER_DATA = 2;

    private int loadDataType = LOAD_ALL_DATA;
    private View ll_sum;
    private View ll_modify_sum;
    private View ll_upload_sum;
    private TextView tv_modify_sum;
    private TextView tv_upload_sum;
    private TextView tv_sum;
    private TextView tv_delete_sum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_facilityaffair, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        pb_loading = (ProgressLinearLayout) view.findViewById(R.id.pb_loading);
        rv_result = (XRecyclerView) view.findViewById(R.id.rv_result);
        rv_result.setPullRefreshEnabled(true);
        rv_result.setLoadingMoreEnabled(true);
        rv_result.setLayoutManager(new LinearLayoutManager(getActivity()));
        facilityAffairAdapter = new FacilityAffairAdapter(getActivity(), new ArrayList<FacilityAffair>());
        rv_result.setAdapter(facilityAffairAdapter);
        facilityAffairAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                FacilityAffair facilityAffair = facilityAffairAdapter.getDataList().get(position - 1);
//                if("广州市城市排水有限公司".equals(facilityAffair.getParentOrgName())){
//                    createUploadDetail(facilityAffair);
////                    ToastUtil.shortToast(getActivity(),"暂无详情");
//                    return;
//                }
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("查询详情中");
                progressDialog.show();

                if (facilityAffair.getSource().equals("correct") || facilityAffair.getSource().equals("delete")) {
                    Observable<ModifiedFacility> modifiedDetail;
                    if("广州市城市排水有限公司".equals(facilityAffair.getParentOrgName())
                            && "市排数据".equals(facilityAffair.getCorrectType())){
                        isShiPaiOrKexuecheng = true;
                        modifiedDetail = identificationService.getModifiedSPDetail(facilityAffair.getId(),facilityAffair.getCorrectType(),facilityAffair.getSource(),facilityAffair.getLayerName());
                    }else if("黄埔科学城数据".equals(facilityAffair.getCorrectType())){
                        modifiedDetail = identificationService.getModifiedSPDetail(facilityAffair.getId(),facilityAffair.getCorrectType(),facilityAffair.getSource(),facilityAffair.getLayerName());
                        isShiPaiOrKexuecheng = true;
                    }
                    else{
                        isShiPaiOrKexuecheng = false;
                        modifiedDetail = identificationService.getModifiedDetail(facilityAffair.getId());
                    }

//                    identificationService.getModifiedDetail(facilityAffair.getId())
                    modifiedDetail
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<ModifiedFacility>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("onError","correct",e);
                                    progressDialog.dismiss();
                                    ToastUtil.shortToast(getActivity(), "查询详情失败");
                                }

                                @Override
                                public void onNext(ModifiedFacility modifyFacilityDetail) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getActivity(), PublicAffairModifiedFacilityDetailAcitivty.class);
                                    List<Photo>photos = new ArrayList<>();
                                    List<Photo>photosWell = new ArrayList<>();
                                    if(!ListUtil.isEmpty(modifyFacilityDetail.getPhotos())){
                                        for(Photo photo:modifyFacilityDetail.getPhotos()){
                                            if(photo.getPhotoName().indexOf("well_")!=-1){
                                                photos.add(photo);
                                            }else{
                                                photosWell.add(photo);
                                            }
                                        }
                                    }
                                    modifyFacilityDetail.setWellPhotos(photosWell);
                                    modifyFacilityDetail.setPhotos(photos);
                                    intent.putExtra("data", modifyFacilityDetail);
                                    intent.putExtra("uploadType", uploadType);
                                    intent.putExtra("isShiPaiOrKexuecheng", isShiPaiOrKexuecheng);
                                    intent.putExtra("facilityType", facilityType);
                                    intent.putExtra("startDate", startTime);
                                    intent.putExtra("endDate", endTime);
                                    if (distrct == null) {
                                        FacilityAffairService facilityAffairService = new FacilityAffairService(getActivity());
                                        distrct = facilityAffairService.getParentOrgOfCurrentUser();
                                        //如果是市级用户，那么可以看到全部的区域
                                        boolean ifCurrentUserBelongToCityUser = facilityAffairService.ifCurrentUserBelongToCityUser();
                                        if (ifCurrentUserBelongToCityUser) {
                                            distrct = "全部";
                                        }
                                    }
                                    intent.putExtra("district", distrct);
                                    getActivity().startActivity(intent);
                                }
                            });
                } else {
                    Observable<UploadedFacility> uploadDetail;
                    if("广州市城市排水有限公司".equals(facilityAffair.getParentOrgName())
                            && "市排数据".equals(facilityAffair.getCorrectType())){
                        isShiPaiOrKexuecheng = true;
                        uploadDetail = identificationService.getUploadSPDetail(facilityAffair.getId(),facilityAffair.getCorrectType(),facilityAffair.getSource(),facilityAffair.getLayerName());
                    }
                    else if("黄埔科学城数据".equals(facilityAffair.getCorrectType())){
                        uploadDetail = identificationService.getUploadSPDetail(facilityAffair.getId(),facilityAffair.getCorrectType(),facilityAffair.getSource(),facilityAffair.getLayerName());
                        isShiPaiOrKexuecheng = true;
                    }else{
                        isShiPaiOrKexuecheng = false;
                        uploadDetail = identificationService.getUploadDetail(facilityAffair.getId());
                    }
//                    identificationService.getUploadDetail(facilityAffair.getId())
                    final boolean finalIsShiPaiOrKexuecheng = isShiPaiOrKexuecheng;
                    uploadDetail
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<UploadedFacility>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("onError","lack",e);
                                    progressDialog.dismiss();
                                    ToastUtil.shortToast(getActivity(), "查询详情失败");
                                }

                                @Override
                                public void onNext(UploadedFacility modifyFacilityDetail) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getActivity(), PublicAffairUploadFacilityDetailAcitivty.class);
                                    intent.putExtra("data", (Parcelable) modifyFacilityDetail);
                                    intent.putExtra("uploadType", uploadType);
                                    intent.putExtra("facilityType", facilityType);
                                    intent.putExtra("startDate", startTime);
                                    intent.putExtra("isShiPaiOrKexuecheng", finalIsShiPaiOrKexuecheng);
                                    intent.putExtra("endDate", endTime);
                                    if (distrct == null) {
                                        FacilityAffairService facilityAffairService = new FacilityAffairService(getActivity());
                                        distrct = facilityAffairService.getParentOrgOfCurrentUser();
                                        //如果是市级用户，那么可以看到全部的区域
                                        boolean ifCurrentUserBelongToCityUser = facilityAffairService.ifCurrentUserBelongToCityUser();
                                        if (ifCurrentUserBelongToCityUser) {
                                            distrct = "全部";
                                        }
                                    }
                                    intent.putExtra("district", distrct);
                                    getActivity().startActivity(intent);
                                }
                            });

                }
            }
        });

        rv_result.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadDatas(false);
            }

            @Override
            public void onLoadMore() {
                page++;
                loadDatas(false);
            }
        });

        ll_filter = view.findViewById(R.id.ll_filter);
        ll_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof IDrawerController) {
                    IDrawerController drawerController = (IDrawerController) getActivity();
                    drawerController.openDrawer(new IDrawerController.OnDrawerOpenListener() {
                        @Override
                        public void onOpened(View drawerView) {
                            //todo 如果需要替换筛选视图的话
                        }
                    });
                }
            }
        });


        ll_sum = view.findViewById(R.id.ll_sum);
        tv_sum = (TextView) view.findViewById(R.id.tv_sum);
        ll_modify_sum = view.findViewById(R.id.ll_modify_sum);
        tv_modify_sum = (TextView) view.findViewById(R.id.tv_modify_sum);
        tv_delete_sum = (TextView) view.findViewById(R.id.tv_delete_sum);
        ll_upload_sum = view.findViewById(R.id.ll_upload_sum);
        tv_upload_sum = (TextView) view.findViewById(R.id.tv_upload_sum);
        //ll_sum.setSelected(true);

        identificationService = new FacilityAffairService(getActivity());

        /**
         * 初始化时间
         */
        initStartAndEndDate();

        loadDatas(true);

        //查看地图
        view.findViewById(R.id.ll_see_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FacilityAffairDistributeMapActivity.class);
                intent.putExtra("uploadType", uploadType);
                intent.putExtra("facilityType", facilityType);
                intent.putExtra("startDate", startTime);
                intent.putExtra("endDate", endTime);
                if (distrct == null) {
                    FacilityAffairService facilityAffairService = new FacilityAffairService(getActivity());
                    distrct = facilityAffairService.getParentOrgOfCurrentUser();
                    //如果是市级用户，那么可以看到全部的区域
                    boolean ifCurrentUserBelongToCityUser = facilityAffairService.ifCurrentUserBelongToCityUser();
                    if (ifCurrentUserBelongToCityUser) {
                        distrct = "全部";
                    }
                }
                intent.putExtra("district", distrct);
                startActivity(intent);
            }
        });
    }

    /**
     * 将列表的数据转换成数据上报的详情
     * @param facilityAffair
     */
    private void createUploadDetail(FacilityAffair facilityAffair) {
        if (facilityAffair.getSource().equals("correct")) {
            ModifiedFacility modifiedFacility = new ModifiedFacility();
            modifiedFacility.setReportType("modify");
            modifiedFacility.setLayerName(facilityAffair.getLayerName());
            modifiedFacility.setAddr(facilityAffair.getAddr());
            modifiedFacility.setUserAddr(facilityAffair.getAddr());
            modifiedFacility.setCorrectType(facilityAffair.getCorrectType());
            modifiedFacility.setMarkPerson(facilityAffair.getMarkPerson());
            modifiedFacility.setParentOrgName(facilityAffair.getParentOrgName());
            modifiedFacility.setMarkTime(facilityAffair.getTime());
            modifiedFacility.setUpdateTime(facilityAffair.getUpdateTime());
            Intent intent = new Intent(getActivity(), PublicAffairModifiedFacilityDetailAcitivty.class);

            intent.putExtra("data", modifiedFacility);
            intent.putExtra("uploadType", uploadType);
            intent.putExtra("facilityType", facilityType);
            intent.putExtra("startDate", startTime);
            intent.putExtra("endDate", endTime);
            if (distrct == null) {
                FacilityAffairService facilityAffairService = new FacilityAffairService(getActivity());
                distrct = facilityAffairService.getParentOrgOfCurrentUser();
                //如果是市级用户，那么可以看到全部的区域
                boolean ifCurrentUserBelongToCityUser = facilityAffairService.ifCurrentUserBelongToCityUser();
                if (ifCurrentUserBelongToCityUser) {
                    distrct = "全部";
                }
            }
            intent.putExtra("district", distrct);
            getActivity().startActivity(intent);
        } else {
            UploadedFacility uploadedFacility = new UploadedFacility();
            uploadedFacility.setLayerName(facilityAffair.getLayerName());
            uploadedFacility.setMarkPerson(facilityAffair.getMarkPerson());
            uploadedFacility.setAddr(facilityAffair.getAddr());
            uploadedFacility.setUserAddr(facilityAffair.getAddr());
            uploadedFacility.setParentOrgName(facilityAffair.getParentOrgName());
            uploadedFacility.setMarkTime(facilityAffair.getTime());
            uploadedFacility.setUpdateTime(facilityAffair.getUpdateTime());

            Intent intent = new Intent(getActivity(), PublicAffairUploadFacilityDetailAcitivty.class);
            intent.putExtra("data", (Parcelable) uploadedFacility);
            intent.putExtra("uploadType", uploadType);
            intent.putExtra("facilityType", facilityType);
            intent.putExtra("startDate", startTime);
            intent.putExtra("endDate", endTime);
            if (distrct == null) {
                FacilityAffairService facilityAffairService = new FacilityAffairService(getActivity());
                distrct = facilityAffairService.getParentOrgOfCurrentUser();
                //如果是市级用户，那么可以看到全部的区域
                boolean ifCurrentUserBelongToCityUser = facilityAffairService.ifCurrentUserBelongToCityUser();
                if (ifCurrentUserBelongToCityUser) {
                    distrct = "全部";
                }
            }
            intent.putExtra("district", distrct);
            getActivity().startActivity(intent);
        }
    }



    public void initStartAndEndDate() {
        Calendar cal = Calendar.getInstance();
        endTime = cal.getTimeInMillis();
        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 1);
        cal.set(Calendar.MILLISECOND, 1);
        startTime = cal.getTimeInMillis();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showLoadedError(String errorReason) {
        pb_loading.showError("获取数据失败", errorReason, "重试", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatas(true);
            }
        });
    }

    public void showLoadedEmpty() {
        pb_loading.showError("", "暂无数据", "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatas(true);
            }
        });
    }

    private String getIntegerString(double n) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(n);
    }

    public void loadDatas(boolean ifShowPb) {
        if (ifShowPb) {
            pb_loading.showLoading();
        }


        identificationService.getFacilityAffairList(page, facilityType, distrct, uploadType, startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FacilityAffairResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (page == 1) {
                           if (e instanceof SocketTimeoutException){
                                showLoadedError("连接服务失败....");
                            }else {
                                showLoadedError("获取数据失败");
                            }
                        } else {
                            ToastUtil.shortToast(getActivity(), "加载更多失败");
                            rv_result.loadMoreComplete();
                        }
                    }

                    @Override
                    public void onNext(FacilityAffairResponseBody modifiedIdentifications) {

                        if (ListUtil.isEmpty(modifiedIdentifications.getCode()) && page == 1) {
                            showLoadedEmpty();
                            tv_sum.setText("0");
                            tv_modify_sum.setText("0");
                            tv_upload_sum.setText("0");
                            tv_delete_sum.setText("0");
                            return;
                        }

                        if (ListUtil.isEmpty(modifiedIdentifications.getCode()) && page > 1) {
                            rv_result.setNoMore(true);
                            return;
                        }

                        pb_loading.showContent();
                        rv_result.loadMoreComplete();
                        rv_result.refreshComplete();
                        if (page > 1) {
                            facilityAffairAdapter.addData(modifiedIdentifications.getCode());
                            facilityAffairAdapter.notifyDataSetChanged();
                        } else if (page == 1) {
                            facilityAffairAdapter.notifyDataChanged(modifiedIdentifications.getCode());
                            rv_result.scrollToPosition(0);
                        }

                        if (page == 1) {
                            tv_sum.setText(getIntegerString(modifiedIdentifications.getTotal()));
                            tv_modify_sum.setText(getIntegerString(modifiedIdentifications.getCorrTotal()));
                            tv_upload_sum.setText(getIntegerString(modifiedIdentifications.getLackTotal()));
                            tv_delete_sum.setText(getIntegerString(modifiedIdentifications.getDelete()));
                        }
                    }
                });
    }

    @Subscribe
    public void onReceivedFacilityAffairFilterCondition(FacilityAffairFilterConditionEvent facilityAffairFilterConditionEvent) {
        pb_loading.showLoading();
        page = 1;
        this.facilityType = facilityAffairFilterConditionEvent.getFacilityType();
        this.uploadType = facilityAffairFilterConditionEvent.getUploadType();
        this.distrct = facilityAffairFilterConditionEvent.getQueryDistrict();
        this.startTime = facilityAffairFilterConditionEvent.getStartTime();
        this.endTime = facilityAffairFilterConditionEvent.getEndTime();
        loadDatas(true);
    }
}
