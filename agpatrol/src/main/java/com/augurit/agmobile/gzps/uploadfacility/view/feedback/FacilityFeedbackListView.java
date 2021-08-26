package com.augurit.agmobile.gzps.uploadfacility.view.feedback;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.uploadfacility.model.FeedbackInfo;
import com.augurit.agmobile.gzps.uploadfacility.service.FeedbackFacilityService;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.utils.ListUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.feedback
 * @createTime 创建时间 ：2018-03-08
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2018-03-08
 * @modifyMemo 修改备注：
 */

public class FacilityFeedbackListView {

    private Context mContext;
    private ViewGroup mContainer;
    private long mAid;
    private String mTableType;

    private View mView;
    private XRecyclerView recyclerView;
    private FacilityFeedbackAdapter feedbackAdapter;
    private FeedbackFacilityService mFeedbackFacilityService;
    private int pageNo = 1;
    private final int pageSize = 15;

    private Callback1<Integer> mLoadCompletedListener;

    /**
     *
     * @param context
     * @param container
     * @param aid        数据上报记录id
     * @param tableType  数据上报类型（新增、校核）
     */
    public FacilityFeedbackListView(Context context, ViewGroup container, long aid, String tableType){
        this.mContext = context;
        this.mContainer = container;
        this.mAid = aid;
        this.mTableType = tableType;
        mFeedbackFacilityService = new FeedbackFacilityService(mContext);
        init();
    }

    public void init(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_facility_feedback_list, null);
        recyclerView = (XRecyclerView) mView.findViewById(R.id.recycler_view);
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        feedbackAdapter = new FacilityFeedbackAdapter(mContext);
        recyclerView.setAdapter(feedbackAdapter);
        feedbackAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener<FeedbackInfo>() {
            @Override
            public void onItemClick(View view, int position, FeedbackInfo selectedData) {

            }

            @Override
            public void onItemLongClick(View view, int position, FeedbackInfo selectedData) {

            }
        });
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        mContainer.addView(mView);

//        loadData();
    }

    public void loadData(){
        feedbackAdapter.notifyDataSetChanged(new ArrayList<FeedbackInfo>());
        mFeedbackFacilityService.getFeedbackInfos(mAid, mTableType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result2<List<FeedbackInfo>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mLoadCompletedListener != null){
                            mLoadCompletedListener.onCallback(0);
                        }
                        recyclerView.loadMoreComplete();
                    }

                    @Override
                    public void onNext(Result2<List<FeedbackInfo>> listResult2) {
                        recyclerView.loadMoreComplete();
                        List<FeedbackInfo> feedbackInfoList = listResult2.getData();
                        if(ListUtil.isEmpty(feedbackInfoList)){
                            feedbackAdapter.notifyDataSetChanged(new ArrayList<FeedbackInfo>());
                            if(mLoadCompletedListener != null){
                                mLoadCompletedListener.onCallback(0);
                            }
                            return;
                        }
                        if(feedbackInfoList.size() < pageSize){
                            recyclerView.setLoadingMoreEnabled(false);
                        }
                        feedbackAdapter.notifyDataSetChanged(feedbackInfoList);
                        if(mLoadCompletedListener != null){
                            mLoadCompletedListener.onCallback(feedbackInfoList.size());
                        }
                    }
                });
    }

    public void setLoadCompletedListener(Callback1<Integer> callback){
        this.mLoadCompletedListener = callback;
    }
}
