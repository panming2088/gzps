package com.augurit.agmobile.gzps.uploadevent.view.eventlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.publicaffair.model.EventAffair;
import com.augurit.agmobile.gzps.publicaffair.view.eventaffair.EventAffairDetailActivity;
import com.augurit.agmobile.gzps.publicaffair.view.eventaffair.EventAffairListAdapter;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 已上报
 * Created by liangsh on 2017/11/25.
 */

public class MyUploadEventListActivity extends BaseActivity {

    private TextView title_tv;
    private ProgressLinearLayout pb_loading;
    private XRecyclerView mRecyclerView;
    private EventAffairListAdapter adapter;

    private UploadEventService mEventService;

    private int pageNo = 1;
    private final int pageSize = 10;
    private boolean loadMore = true;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_uploadevent_list);
        mEventService = new UploadEventService(this.getApplicationContext());
        initView();
        loadEventList(true);
    }

    private void initView(){
        mRecyclerView = (XRecyclerView) findViewById(R.id.event_list_rv);
        title_tv = (TextView) findViewById(R.id.tv_title);
        title_tv.setText("任务列表(已上报)");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new EventAffairListAdapter(null, this);
        pb_loading = (ProgressLinearLayout) findViewById(R.id.pb_loading);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setPullRefreshEnabled(false);
        adapter = new EventAffairListAdapter(null, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener<EventAffair.EventAffairBean>() {
            @Override
            public void onItemClick(View view, int position, EventAffair.EventAffairBean selectedData) {
                Intent intent = new Intent(MyUploadEventListActivity.this, EventAffairDetailActivity.class);
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
    }

    private void loadEventList(boolean showPb) {
        if(showPb){
            pb_loading.showLoading();
        }

        User user = new LoginService(this.getApplicationContext(), AMDatabase.getInstance()).getUser();
        mEventService.search(pageNo, pageSize, user.getUserName())
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
}
