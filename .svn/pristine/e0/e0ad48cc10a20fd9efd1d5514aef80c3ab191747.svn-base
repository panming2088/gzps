package com.augurit.agmobile.gzps.uploadevent.view.eventlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.OnRefreshEvent;
import com.augurit.agmobile.gzps.common.constant.GzpsConstant;
import com.augurit.agmobile.gzps.uploadevent.adapter.MyEventListAdapter;
import com.augurit.agmobile.gzps.uploadevent.event.UpdateAfterDeleteEvent;
import com.augurit.agmobile.gzps.uploadevent.event.UpdateAfterRetriveEvent;
import com.augurit.agmobile.gzps.uploadevent.model.EventListItem;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.ListUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 待受理  处理中  已办结
 */
public class EventListActivity extends BaseActivity {

    private ProgressLinearLayout pb_loading;
    private XRecyclerView mRecyclerView;
    private MyEventListAdapter adapter;
    private TextView title_tv;

    private UploadEventService mEventService;

    private int pageNo = 1;
    private final int pageSize = 10;
    private boolean loadMore = true;

    private int eventPState = GzpsConstant.EVENT_P_STATE_HANDLING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        mEventService = new UploadEventService(EventListActivity.this.getApplicationContext());
        initView();
        initData();

        EventBus.getDefault().register(this);
    }

    private void initView() {
        mRecyclerView = (XRecyclerView) findViewById(R.id.event_list_rv);
        title_tv = (TextView) findViewById(R.id.tv_title);
        title_tv.setText("任务列表");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        eventPState = getIntent().getIntExtra(GzpsConstant.EVENT_P_STATE_KEY, GzpsConstant.EVENT_P_STATE_HANDLING);
        setTitle(eventPState);
        adapter = new MyEventListAdapter(null, eventPState, EventListActivity.this);
        pb_loading = (ProgressLinearLayout) findViewById(R.id.pb_loading);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(EventListActivity.this));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener<EventListItem>() {
            @Override
            public void onItemClick(View view, int position, EventListItem selectedData) {
                Intent intent = new Intent(EventListActivity.this, EventDetailActivity.class);
                intent.putExtra("eventList", selectedData);
                intent.putExtra("eventPState", eventPState);

                startActivityForResult(intent, 123);
            }

            @Override
            public void onItemLongClick(View view, int position, EventListItem selectedData) {

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
        loadEventList(true);
    }

    private void loadEventList(boolean ifShowPb) {
        if (ifShowPb) {
            pb_loading.showLoading();
        }
        getEventListObserable(pageNo, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<EventListItem>>() {
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
                        pb_loading.showError("", "加载数据出错", "刷新", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pageNo = 1;
                                loadEventList(true);
                            }
                        });
                    }

                    @Override
                    public void onNext(List<EventListItem> s) {
                        if (ListUtil.isEmpty(s)) {
                            if(pageNo == 1) {
                                pb_loading.showError("", "暂无数据", "刷新", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pageNo = 1;
                                        loadEventList(true);
                                    }
                                });
                            } else {
                                mRecyclerView.loadMoreComplete();
                                mRecyclerView.setNoMore(true);
                            }
                            return;
                        }
                        pb_loading.showContent();
                        if (loadMore) {
                            adapter.loadMore(s);
                            mRecyclerView.loadMoreComplete();
                        } else {
                            adapter.refresh(s);
                            mRecyclerView.refreshComplete();
                        }
                        if(s.size() < pageSize){
                            mRecyclerView.setNoMore(true);
                        }
                    }
                });
    }


    public void setTitle(int eventState){

        if(eventState == GzpsConstant.EVENT_P_STATE_HANDLED){
            //处理中
            title_tv.setText("任务列表(处理中)");
        }else if(eventState == GzpsConstant.EVENT_P_STATE_FINISHED){
            //已办结
            title_tv.setText("任务列表(已办结)");

        }else if(eventState == GzpsConstant.EVENT_P_STATE_HANDLING){
            //待受理
            title_tv.setText("任务列表(待受理)");
        }

    }

    private Observable<List<EventListItem>> getEventListObserable(int pageNo, final int pageSize) {
        Observable<List<EventListItem>> observable = null;
        switch (eventPState) {
            case GzpsConstant.EVENT_P_STATE_HANDLING:
                observable = mEventService.getDZbEventList(pageNo, pageSize);
                break;
            case GzpsConstant.EVENT_P_STATE_HANDLED:
                observable = mEventService.getYbEventList(pageNo, pageSize);
                break;
            case GzpsConstant.EVENT_P_STATE_FINISHED:
                observable = mEventService.getYbjEventList(pageNo, pageSize);
                break;
//            case GzpsConstant.EVENT_P_STATE_UPLOADED:  //该情况下不用当前界面(Activity)
//                break;
            default:
                observable = mEventService.getDZbEventList(pageNo, pageSize);
                break;
        }
        return observable;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 123
                && resultCode == 123){
            loadMore = false;
            pageNo = 1;
            loadEventList(true);
            EventBus.getDefault().post(new OnRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateAfterRetriveEvent(UpdateAfterRetriveEvent event){
        if(event.getProcInstDbId() != null){
            List<EventListItem> pre = adapter.getListData();
            List<EventListItem> cur = new ArrayList<>();
            for(EventListItem item : pre){
                if(!item.getProcInstDbId().equals(event.getProcInstDbId())){
                   cur.add(item);
                }
            }
            adapter.refresh(cur);
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateAfterDeleteEvent(UpdateAfterDeleteEvent event){
        if(event.getProcInstDbId() != null ){
            List<EventListItem> pre = adapter.getListData();
            List<EventListItem> cur = new ArrayList<>();
            for(EventListItem item : pre){
                if(!item.getProcInstDbId().equals(event.getProcInstDbId())){
                    cur.add(item);
                }
            }

            adapter.refresh(cur);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}
