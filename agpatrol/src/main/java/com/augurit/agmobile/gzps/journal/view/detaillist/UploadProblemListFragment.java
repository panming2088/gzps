package com.augurit.agmobile.gzps.journal.view.detaillist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.journal.adapter.UploadProblemListAdapter;
import com.augurit.agmobile.gzps.publicaffair.model.EventAffair;
import com.augurit.agmobile.gzps.publicaffair.view.condition.EventAffairConditionEvent;
import com.augurit.agmobile.gzps.publicaffair.view.eventaffair.EventAffairDetailActivity;
import com.augurit.agmobile.gzps.publicaffair.view.eventaffair.EventAffairListAdapter;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.ListUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.journal.view
 * @createTime 创建时间 ：2018-07-04
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-07-04
 * @modifyMemo 修改备注：
 */

public class UploadProblemListFragment extends Fragment{

    private ProgressLinearLayout pb_loading;
    //    private LinearLayout ll_sum;//总数
//    private TextView tv_sum;
//    private LinearLayout ll_pass_sum;//达标
//    private TextView tv_pass_sum;
//    private LinearLayout ll_timeouting_sum;//快超时
//    private TextView tv_timeouting_sum;
//    private LinearLayout ll_unpass_sum;//不达标
//    private TextView tv_unpass_sum;
    private XRecyclerView mRecyclerView;
    private UploadProblemListAdapter adapter;
    //TODO
    private UploadEventService mEventService;

    private int pageNo = 1;
    private final int pageSize = 10;
    private boolean loadMore = true;

    //筛选条件
    private String district = null;  //行政区划
    private String componentTypeCode = null;   //设施类型数据字典编码
    private String eventTypeCode = null;       //问题类型数据字典编码
    private String state = null;               //处理中还是已办结
    private Component mComponent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload_problem,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        EventB us.getDefault().register(this);
        mEventService = new UploadEventService(getContext());
        savedInstanceState = getArguments();
        mComponent = (Component) savedInstanceState.get("component");
//        ll_sum = (LinearLayout) view.findViewById(R.id.ll_sum);
//        tv_sum = (TextView) view.findViewById(R.id.tv_sum);
//        ll_pass_sum = (LinearLayout) view.findViewById(R.id.ll_pass_sum);
//        tv_pass_sum = (TextView) view.findViewById(R.id.tv_pass_sum);
//        ll_timeouting_sum = (LinearLayout) view.findViewById(R.id.ll_timeouting_sum);
//        tv_timeouting_sum = (TextView) view.findViewById(R.id.tv_timeouting_sum);
//        ll_unpass_sum = (LinearLayout) view.findViewById(R.id.ll_unpass_sum);
//        tv_unpass_sum = (TextView) view.findViewById(R.id.tv_unpass_sum);

        pb_loading = (ProgressLinearLayout) view.findViewById(R.id.pb_loading);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.event_list_rv);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setPullRefreshEnabled(false);
        adapter = new UploadProblemListAdapter(null, getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener<EventAffair.EventAffairBean>() {
            @Override
            public void onItemClick(View view, int position, EventAffair.EventAffairBean selectedData) {
                Intent intent = new Intent(getContext(), EventAffairDetailActivity.class);
//                if(TextUtils.isEmpty(selectedData.getYjgcl())){
//                    selectedData.setYjgcl((String) mComponent.getGraphic().getAttributes().get("ATTR_THREE"));
//                }
                intent.putExtra("from_journal",true);
                selectedData.setLayerurl(mComponent.getLayerUrl());
                intent.putExtra("eventAffairBean", selectedData);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position, EventAffair.EventAffairBean selectedData) {

            }
        });
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                loadMore = false;
                loadEventList(false);
                mRecyclerView.setNoMore(false);
            }

            @Override
            public void onLoadMore() {
                pageNo++;
                loadMore = true;
                loadEventList(false);
            }
        });

//        ll_sum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchTopTabColor(ll_sum);
//                state = null;
//                pageNo = 1;
//                loadMore = false;
//                loadEventList(true);
//                mRecyclerView.setNoMore(false);
//            }
//        });
//        ll_pass_sum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchTopTabColor(ll_pass_sum);
//                state = "0";
//                pageNo = 1;
//                loadMore = false;
//                loadEventList(true);
//                mRecyclerView.setNoMore(false);
//            }
//        });
//        ll_unpass_sum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchTopTabColor(ll_unpass_sum);
//                state = "1";
//                pageNo = 1;
//                loadMore = false;
//                loadEventList(true);
//                mRecyclerView.setNoMore(false);
//            }
//        });
//
//        ll_timeouting_sum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchTopTabColor(ll_timeouting_sum);
//                state = "1";
//                pageNo = 1;
//                loadMore = false;
//                loadEventList(true);
//                mRecyclerView.setNoMore(false);
//            }
//        });
        loadEventList(true);
    }

    private void loadEventList(boolean showPb) {
        if(showPb){
            pb_loading.showLoading();
        }
        Observable<EventAffair> observable = null;

        //TODO 根据条件筛选问题上报列表

        if(mComponent!=null){
            String usid = (String) mComponent.getGraphic().getAttributes().get("US_ID");
            observable =   mEventService.searchByObjectId(pageNo, pageSize, TextUtils.isEmpty(usid)|| "null".equals(usid) ? mComponent.getObjectId()+"":"",usid);
        }else{
            observable =   mEventService.search(pageNo, pageSize, district, componentTypeCode, eventTypeCode, null,null,state,null);
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EventAffair>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (loadMore) {
                            mRecyclerView.loadMoreComplete();
                        } else {
                            mRecyclerView.refreshComplete();
                        }
                        if (pageNo == 1){
                            showLoadedError("");
                        }
                    }

                    @Override
                    public void onNext(EventAffair s) {
                        if (s == null){
                            showLoadedError("");
                            return;
                        }
                        List<EventAffair.EventAffairBean> eventAffairBeaListn = s.getList();
//                        tv_pass_sum.setText("" + s.getHandling());
//                        tv_unpass_sum.setText("" + s.getHandling());
//                        tv_timeouting_sum.setText("" + s.getHandling());
//                        tv_sum.setText("" + (s.getFinished() + s.getHandling()));
                        if (ListUtil.isEmpty(eventAffairBeaListn)) {
                            if(pageNo == 1) {
                                showLoadedEmpty();
                            } else {
                                mRecyclerView.loadMoreComplete();
                                mRecyclerView.setNoMore(true);
                            }
                            return;
                        }
                        pb_loading.showContent();
                        if (loadMore) {
                            adapter.loadMore(eventAffairBeaListn);
                            mRecyclerView.loadMoreComplete();
                        } else {
                            adapter.refresh(eventAffairBeaListn);
                            mRecyclerView.refreshComplete();
                        }
                        if(eventAffairBeaListn.size() < pageSize){
                            mRecyclerView.setNoMore(true);
                        }

                    }
                });
    }

//    private void switchTopTabColor(LinearLayout linearLayout){
//        ll_sum.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
//        ll_pass_sum.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
//        ll_timeouting_sum.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
//        ll_unpass_sum.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
//        linearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.light_green_alpha));
//    }


    public void showLoadedError(String errorReason) {
        pb_loading.showError("获取数据失败", errorReason, "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEventList(true);
            }
        });
    }

    public void showLoadedEmpty() {
        pb_loading.showError("", "暂无数据", "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEventList(true);
            }
        });
    }


//    @Subscribe
//    public void onEventConditionChanged(EventAffairConditionEvent eventAffairConditionEvent){
//        if(eventAffairConditionEvent.getDistrict() != null
//                && eventAffairConditionEvent.getDistrict().contains("净水")){
//            this.district = "净水";
//        } else {
//            this.district = eventAffairConditionEvent.getDistrict();
//        }
//        this.componentTypeCode = eventAffairConditionEvent.getComponentTypeCode();
//        this.eventTypeCode = eventAffairConditionEvent.getEventTypeCode();
//        pageNo = 1;
//        state = null;
//        switchTopTabColor(ll_sum);
//        loadMore = false;
//        loadEventList(true);
//        mRecyclerView.setNoMore(false);
//    }


}
