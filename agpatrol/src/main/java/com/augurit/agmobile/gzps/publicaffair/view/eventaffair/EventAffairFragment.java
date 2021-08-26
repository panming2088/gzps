package com.augurit.agmobile.gzps.publicaffair.view.eventaffair;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.publicaffair.view.condition.EventAffairConditionEvent;
import com.augurit.agmobile.gzps.publicaffair.model.EventAffair;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 事务公开中的问题上报列表
 * Created by liangsh on 2017/11/17.
 */

public class EventAffairFragment extends Fragment{

    private ProgressLinearLayout pb_loading;
    private LinearLayout ll_sum;
    private TextView tv_sum;
    private LinearLayout ll_handling_sum;
    private TextView tv_handling_sum;
    private LinearLayout ll_finished_sum;
    private TextView tv_finished_sum;
    private XRecyclerView mRecyclerView;
    private EventAffairListAdapter adapter;


    private UploadEventService mEventService;

    private int pageNo = 1;
    private final int pageSize = 10;
    private boolean loadMore = true;

    //筛选条件
    private String district = null;  //行政区划
    private String componentTypeCode = null;   //设施类型数据字典编码
    private String eventTypeCode = null;       //问题类型数据字典编码
    private Long eventCode = null;       //问题类型数据字典编码
    private String state = null;               //处理中还是已办结
    private Context mContext;
    private boolean isShow = false;
    private HashMap<String,String> mHashMap = new HashMap<>();
    private Long startTime;
    private Long endTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eventaffair,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        initHashMap();
        isShow = ((Activity)mContext).getIntent().getBooleanExtra("showEventCode",false);
        mEventService = new UploadEventService(getContext());
        ll_sum = (LinearLayout) view.findViewById(R.id.ll_sum);
        tv_sum = (TextView) view.findViewById(R.id.tv_sum);
        ll_handling_sum = (LinearLayout) view.findViewById(R.id.ll_handling_sum);
        tv_handling_sum = (TextView) view.findViewById(R.id.tv_handling_sum);
        ll_finished_sum = (LinearLayout) view.findViewById(R.id.ll_finished_sum);
        tv_finished_sum = (TextView) view.findViewById(R.id.tv_finished_sum);
        pb_loading = (ProgressLinearLayout) view.findViewById(R.id.pb_loading);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.event_list_rv);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setPullRefreshEnabled(false);
        adapter = new EventAffairListAdapter(null, getContext());
        adapter.setShowCode(isShow);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener<EventAffair.EventAffairBean>() {
            @Override
            public void onItemClick(View view, int position, EventAffair.EventAffairBean selectedData) {
                Intent intent = new Intent(getContext(), EventAffairDetailActivity.class);
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

        ll_sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTopTabColor(ll_sum);
                state = null;
                pageNo = 1;
                loadMore = false;
                loadEventList(true);
                mRecyclerView.setNoMore(false);
            }
        });
        ll_handling_sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTopTabColor(ll_handling_sum);
                state = "0";
                pageNo = 1;
                loadMore = false;
                loadEventList(true);
                mRecyclerView.setNoMore(false);
            }
        });
        ll_finished_sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTopTabColor(ll_finished_sum);
                state = "1";
                pageNo = 1;
                loadMore = false;
                loadEventList(true);
                mRecyclerView.setNoMore(false);




            }
        });
        loadEventList(true);
    }

    private void initHashMap() {
        mHashMap.put("全部","全部");
        mHashMap.put("市排水公司","市排水公司");
        mHashMap.put("科学城排水公司","黄埔");
        mHashMap.put("番禺污水公司","番禺");
        mHashMap.put("南沙排水公司","南沙");
        mHashMap.put("花都排水公司","花都");
        mHashMap.put("增城排水公司","增城");
        mHashMap.put("从化排水公司","从化");
        mHashMap.put("其他","其他");

    }

    private void loadEventList(boolean showPb) {
        if(showPb){
            pb_loading.showLoading();
        }
        //根据条件筛选问题上报列表
        mEventService.search(pageNo, pageSize, StringUtil.isEmpty(mHashMap.get(district))?district:mHashMap.get(district), componentTypeCode, eventTypeCode,startTime,endTime, state,eventCode)
                .subscribeOn(Schedulers.io())
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
                        tv_handling_sum.setText("" + s.getHandling());
                        tv_finished_sum.setText("" + s.getFinished());
                        tv_sum.setText("" + (s.getFinished() + s.getHandling()));
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

    private void switchTopTabColor(LinearLayout linearLayout){
        ll_sum.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        ll_handling_sum.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        ll_finished_sum.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        linearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.light_green_alpha));
    }


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


    @Subscribe
    public void onEventConditionChanged(EventAffairConditionEvent eventAffairConditionEvent){
        if(eventAffairConditionEvent.getDistrict() != null
                && eventAffairConditionEvent.getDistrict().contains("净水")){
            this.district = "净水";
        } else {
            this.district = eventAffairConditionEvent.getDistrict();
        }
        this.componentTypeCode = eventAffairConditionEvent.getComponentTypeCode();
        this.eventTypeCode = eventAffairConditionEvent.getEventTypeCode();
        this.eventCode = eventAffairConditionEvent.getCode();
        this.startTime = eventAffairConditionEvent.getStartTime();
        this.endTime = eventAffairConditionEvent.getEndTime();
        pageNo = 1;
        state = null;
        switchTopTabColor(ll_sum);
        loadMore = false;
        loadEventList(true);
        mRecyclerView.setNoMore(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context != null){
            this.mContext = context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null){
            this.mContext = activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }
}
