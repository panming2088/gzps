package com.augurit.agmobile.gzps.monitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.util.Attachment2PhotoUtil;
import com.augurit.agmobile.gzps.common.util.LoadDataConstant;
import com.augurit.agmobile.gzps.monitor.adapter.InspectionWellMonitorListAdapter;
import com.augurit.agmobile.gzps.monitor.model.InspectionWellMonitorInfo;
import com.augurit.agmobile.gzps.monitor.service.InspectionWellMonitorService;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.UploadFacilitySumEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 窨井（关键节点）监测列表界面
 *
 * @author 创建人 ：huangchongwu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps
 * @createTime 创建时间 ：2020/09/14
 */

public class InspectionWellMonitorListFragment extends Fragment {

    private InspectionWellMonitorListAdapter mSearchResultAdapter;
    private ProgressLinearLayout pb_loading;
    private XRecyclerView rv_component_list;
    private String checkState = null;
    private Context mContext;
    private InspectionWellMonitorService inspectionWellMonitorService;
    private int page = 1;
    private Button addBtn;
    private String usid,jbjObjectId,type,X,Y;
    
    public static InspectionWellMonitorListFragment newInstance(Bundle bundle) {
        InspectionWellMonitorListFragment wellMonitorListFragment = new InspectionWellMonitorListFragment();
        if (bundle != null) {
            wellMonitorListFragment.setArguments(bundle);
        }
        return wellMonitorListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_well_monitor, null);
        EventBus.getDefault().register(this);
        return inflate;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inspectionWellMonitorService = new InspectionWellMonitorService(getContext());
        rv_component_list = (XRecyclerView) view.findViewById(R.id.rv_component_list);
        pb_loading = (ProgressLinearLayout) view.findViewById(R.id.pb_loading);
        rv_component_list.setPullRefreshEnabled(true);
        rv_component_list.setLoadingMoreEnabled(true);
        rv_component_list.setLayoutManager(new LinearLayoutManager(mContext));
        usid = getArguments().getString("usid");
        jbjObjectId = getArguments().getString("objectid");
        type = getArguments().getString("type");
        X = getArguments().getString("X");
        Y = getArguments().getString("Y");
        mSearchResultAdapter = new InspectionWellMonitorListAdapter(mContext, new ArrayList<InspectionWellMonitorInfo>(),type);
        rv_component_list.setAdapter(mSearchResultAdapter);

        mSearchResultAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                rv_component_list.setEnabled(false);
                int selectPosition = position;
                if (page > 1) {
                    selectPosition = position - mSearchResultAdapter.getDataList().size() - page * LoadDataConstant.LOAD_ITEM_PER_PAGE;
                }
                Intent intent = new Intent(mContext, InspectionWellMonitorEditOrDetailActivity.class);
                InspectionWellMonitorInfo inspectionWellMonitorInfo  = mSearchResultAdapter.getDataList().get(position - 1);
                inspectionWellMonitorInfo.setPhotos(Attachment2PhotoUtil.toPhotos(inspectionWellMonitorInfo.getAttachments()));
                System.out.println(inspectionWellMonitorInfo);
                intent.putExtra("InspectionWellMonitorInfo", (Serializable) inspectionWellMonitorInfo);
                intent.putExtra("type", TextUtils.isEmpty(inspectionWellMonitorInfo.getJbjType())?type:inspectionWellMonitorInfo.getJbjType());
                intent.putExtra("objectid",inspectionWellMonitorInfo.getJbjObjectId());
                intent.putExtra("usid",inspectionWellMonitorInfo.getUsid());
                intent.putExtra("X",X);
                intent.putExtra("Y",Y);
                mContext.startActivity(intent);
            }
        });

        rv_component_list.setLoadingListener(new XRecyclerView.LoadingListener() {
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

        addBtn = (Button) view.findViewById(R.id.btn_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*Intent intent  = new Intent(getContext(),WellMonitorActivity.class);
            intent.putExtra("usid",usid);
            intent.putExtra("objectid",jbjObjectId);
            intent.putExtra("type",type);
            intent.putExtra("X",X);
            intent.putExtra("Y",Y);
//            intent.putExtra("gj",ListUtil.isEmpty(mSearchResultAdapter.getDataList())?
//                    null:mSearchResultAdapter.getDataList().get(0).getGdgj());
            startActivity(intent);*/

            InspectionWellMonitorEditOrDetailActivity.jump(getActivity(), usid, jbjObjectId, type, X, Y);
            }
        });

        loadDatas(true);
    }



    public void showLoadedError(String errorReason) {
        pb_loading.showError("获取数据失败", errorReason, "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatas(true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    if(rv_component_list!=null){
        rv_component_list.setEnabled(true);
       }
    }

    public void showLoadedEmpty() {
        pb_loading.showError("", "暂无数据", "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatas(true);
            }
        });
    }

    public void loadDatas(boolean ifShowPb) {
        if (ifShowPb)
            pb_loading.showLoading();
        inspectionWellMonitorService.getInspectionWellMonitors(page,15,usid,jbjObjectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result3<List<InspectionWellMonitorInfo>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (page == 1) {
                            showLoadedError(e.getLocalizedMessage());
                            }
                        ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "获取数据失败，请稍后重试");
                    }

                    @Override
                    public void onNext(Result3<List<InspectionWellMonitorInfo>> listResult2) {
                        UploadFacilitySumEvent uploadFacilitySumEvent = new UploadFacilitySumEvent();
                        if (listResult2.getCode() == 200) {
                            if(ListUtil.isEmpty(listResult2.getData())){
                                uploadFacilitySumEvent.setSum(0);
                                EventBus.getDefault().post(uploadFacilitySumEvent);
                                showLoadedEmpty();
                                return;
                            }
                            if (ListUtil.isEmpty(listResult2.getData()) && page > 1) {
                                rv_component_list.setNoMore(true);
                                return;
                            }
                            uploadFacilitySumEvent.setSum(Integer.valueOf(listResult2.getTotal()));
                            EventBus.getDefault().post(uploadFacilitySumEvent);
                            pb_loading.showContent();
                            rv_component_list.loadMoreComplete();
                            rv_component_list.refreshComplete();
                            if (page > 1) {
                                mSearchResultAdapter.addData(listResult2.getData());
                                mSearchResultAdapter.notifyDataSetChanged();
                            } else if (page == 1) {
                                mSearchResultAdapter.notifyDataChanged(listResult2.getData());
                                rv_component_list.scrollToPosition(0);
                            }
                        } else {
//                            CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
//                                    BaseInfoManager.getUserName(mContext) + "原因是：" + s.getMessage()));
                            Toast.makeText(mContext, listResult2.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void RefreshMyModificationListEvent(RefreshMyUploadList refreshMyModificationListEvent) {
        page = 1;
        loadDatas(true);
    }
//    @Subscribe
//    public void RefreshMyModificationListEvent1(RefreshMyModificationListEvent refreshMyModificationListEvent) {
//        page = 1;
//        loadDatas(true);
//    }
//
//    @Subscribe
//    public void refreshList1(RefreshMyUploadList refreshMyUploadList) {
//        page = 1;
//        loadDatas(true);
//    }
}
