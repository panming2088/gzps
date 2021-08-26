package com.augurit.agmobile.gzps.jbjpsdy;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.jbjpsdy.service.JbjPsdyService;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/11/9 11:15
 */
public class JbjPsdySewerageUserListView extends LinearLayout {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_SETTING_DATA = 2;

    public static final int PAGE_SIZE = 10;

    private JbjPsdySewerageUserListHeaderView mHeader;
    private XRecyclerView mRvSwerageUserList;
    private JbjPsdySewerageUserListAdapter mAdptSwerageUserList;
    private JbjPsdySewerageUserListAdapter.OnSewerageUserListItemClickListener mOnItemClickListener;

//    private View mEmptyView;
    private ProgressLinearLayout mProgressBar;

    private Component mCurrComponent;
    private JbjPsdyService mRequestService;

    private final ArrayList<SewerageUserEntity> mAllSewerageUsers = new ArrayList<>();
    private final ArrayList<SewerageUserEntity> mCateringTradeSewerageUsers = new ArrayList<>();
    private final ArrayList<SewerageUserEntity> mPrecipitateSewerageUsers = new ArrayList<>();
    private final ArrayList<SewerageUserEntity> mDangerSewerageUsers = new ArrayList<>();
    private final ArrayList<SewerageUserEntity> mLifeSewerageUsers = new ArrayList<>();
    private ArrayList<SewerageUserEntity> mShowSewerageUsers;

    private int mAllSewerageUserFirstVisiblePosition = 0;
    private int mCateringTradeSewerageFirstVisiblePosition = 0;
    private int mPrecipitateSewerageUserFirstVisiblePosition = 0;
    private int mDangerSewerageUserFirstVisiblePosition = 0;
    private int mLifeSewerageUserFirstVisiblePosition = 0;

    private int mCurrState = STATE_NORMAL;

    /**
     * 控制列表跳转到指定位置
     * 一般只有在
     */
    private boolean mIsNeedChangePosition = false;

    /**
     * 正在加载中的数据的类型，避免切换了以后，数据还在加载中，导致错乱。
     */
    private int mCurrTypeOfLoadingData = SewerageUserEntity.TYPE_UNKNOW;
    private boolean mIsFirstLoadData = true;
    private View mBottomView;

    public JbjPsdySewerageUserListView(Context context) {
        super(context);
        init(context);
    }

    public JbjPsdySewerageUserListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JbjPsdySewerageUserListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setCurrentComponent(Component component) {
//        if (mCurrComponent == component) {
//            return;
//        }
        mCurrComponent = component;
        reset();
        // 接下来应该要开始进行数据查询了
        // 不用了，上面默认了会重新点击“全部”Tab，会在回调那里去重新获取数据的。
//        loadDataByType();
    }

    private void reset() {
        mAllSewerageUsers.clear();
        mCateringTradeSewerageUsers.clear();
        mPrecipitateSewerageUsers.clear();
        mDangerSewerageUsers.clear();
        mLifeSewerageUsers.clear();
        mAdptSwerageUserList.update(null);
        mShowSewerageUsers = mAllSewerageUsers;
        mIsFirstLoadData = true;
        // emmm.....这个要不要重置为false呢。。。按理说，这个在数据请求结束完毕以后，会自动置为false的。
//        mIsLoading = false;
        mAllSewerageUserFirstVisiblePosition = 0;
        mCateringTradeSewerageFirstVisiblePosition = 0;
        mDangerSewerageUserFirstVisiblePosition = 0;
        mLifeSewerageUserFirstVisiblePosition = 0;
        mPrecipitateSewerageUserFirstVisiblePosition = 0;
        mHeader.reset();
    }

    public void setOnItemClickListener(JbjPsdySewerageUserListAdapter.OnSewerageUserListItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 处理请求接口返回的结果
     */
    public void handleSewerageUserResultEntity(SewerageUserResultEntity resultEntity) {
        if (mHeader.getSelectedType() == SewerageUserEntity.TYPE_UNKNOW) {
            mHeader.setCount(SewerageUserEntity.TYPE_UNKNOW, resultEntity.getTotalCount());
            mHeader.setCount(SewerageUserEntity.TYPE_CATERING_TRADE, resultEntity.getCateringTradeCount());
            mHeader.setCount(SewerageUserEntity.TYPE_PRECIPITATE, resultEntity.getPrecipitateCount());
            mHeader.setCount(SewerageUserEntity.TYPE_DANGER, resultEntity.getDangerCount());
            mHeader.setCount(SewerageUserEntity.TYPE_LIFE, resultEntity.getLifeCount());
        }
        mShowSewerageUsers.addAll(resultEntity.getSewerageUserEntities());
        if (mCurrTypeOfLoadingData == mHeader.getSelectedType()) {
//            mAdptSwerageUserList.update(mShowSewerageUsers);
            showDataOfSelectedType();
        }
        mCurrState = STATE_NORMAL;
        if(mShowSewerageUsers == null || mShowSewerageUsers.isEmpty()){
            showLoadedEmpty();
        }else{
            mProgressBar.showContent();
        }
        mHeader.setSelectable(true);
//        mEmptyView.setVisibility((mShowSewerageUsers == null || mShowSewerageUsers.isEmpty()) ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 显示当前选择类型的排水户数据
     */
    public void showDataOfSelectedType() {
        if (mShowSewerageUsers == null) {
            return;
        }
        if (mShowSewerageUsers.isEmpty()) {
            showLoadedEmpty();
//            mEmptyView.setVisibility(View.VISIBLE);
            loadDataByType();
        } else {
//            mEmptyView.setVisibility(View.INVISIBLE);
            mProgressBar.showContent();
            mRvSwerageUserList.reset();
            mAdptSwerageUserList.update(mShowSewerageUsers);
            if (mIsNeedChangePosition) {
                mIsNeedChangePosition = false;
                int firstVisibleVisiblePosition = getFirstVisibleVisiblePosition();
                if (firstVisibleVisiblePosition < 0 || firstVisibleVisiblePosition >= mShowSewerageUsers.size()) {
                    return;
                }
                mRvSwerageUserList.scrollToPosition(firstVisibleVisiblePosition);
            }
        }
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.drainage_view_jbjpsdy_sewerage_user_list, this);
        mBottomView = View.inflate(context, R.layout.drainage_bottom_list_view,null);
        mRequestService = new JbjPsdyService(context);
        initView();
    }

    private void initView() {
        mHeader = (JbjPsdySewerageUserListHeaderView) findViewById(R.id.tabHeader);
        mHeader.setOnTabSelectedListener(new JbjPsdySewerageUserListHeaderView.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int type) {
                switch (type) {
                    case SewerageUserEntity.TYPE_UNKNOW:
                        mShowSewerageUsers = mAllSewerageUsers;
                        break;
                    case SewerageUserEntity.TYPE_CATERING_TRADE:
                        mShowSewerageUsers = mCateringTradeSewerageUsers;
                        break;
                    case SewerageUserEntity.TYPE_PRECIPITATE:
                        mShowSewerageUsers = mPrecipitateSewerageUsers;
                        break;
                    case SewerageUserEntity.TYPE_DANGER:
                        mShowSewerageUsers = mDangerSewerageUsers;
                        break;
                    case SewerageUserEntity.TYPE_LIFE:
                        mShowSewerageUsers = mLifeSewerageUsers;
                        break;
                    default:
                        mShowSewerageUsers = null;
                        break;
                }
                mIsNeedChangePosition = true;
                showDataOfSelectedType();
            }
        });

        mRvSwerageUserList = (XRecyclerView) findViewById(R.id.rvList);
        mRvSwerageUserList.setNestedScrollingEnabled(true);
        mRvSwerageUserList.setLoadingMoreEnabled(true);
        mRvSwerageUserList.setPullRefreshEnabled(false);
//        mRvSwerageUserList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mRvSwerageUserList.setHasFixedSize(true);
        mRvSwerageUserList.getItemAnimator().setChangeDuration(0);
        mRvSwerageUserList.getItemAnimator().setRemoveDuration(0);
        mRvSwerageUserList.getItemAnimator().setMoveDuration(0);
        mRvSwerageUserList.getItemAnimator().setAddDuration(0);
        mRvSwerageUserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                // 保存当前类型的滑动位置
                RecyclerView.LayoutManager layoutManager = mRvSwerageUserList.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    int firstVisiblelyVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    setFirstVisiblePosition(firstVisiblelyVisibleItemPosition);
                }

                // 停止/启动图片加载，避免滑动状态下，还对图片进行加载。
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    mAdptSwerageUserList.startLoadImg();
                    // 启动图片加载时，已加载完的图片，是不会再次加载的，所以，在这里刷新一下可视区域内所有的数据。
//                    if (layoutManager instanceof LinearLayoutManager) {
//                        int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
//                        int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
//                        int itemCount = lastVisibleItemPosition - firstVisibleItemPosition;
//                        if (firstVisibleItemPosition >= 0) {
//                            mAdptSwerageUserList.notifyItemRangeChanged(firstVisibleItemPosition, itemCount + 1);
//                        }
//                    }
                } else {
                    mAdptSwerageUserList.stopLoadImg();
                }
            }

//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                RecyclerView.LayoutManager layoutManager = mRvSwerageUserList.getLayoutManager();
//                if (layoutManager instanceof LinearLayoutManager) {
//                    int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
//                    if (lastCompletelyVisibleItemPosition >= mShowSewerageUsers.size() - 1) {
//                        // 到达倒数最后一个的时候，才开始加载下一页数据
//                        loadDataByType();
//                    }
//                }
//            }
        });
        mAdptSwerageUserList = new JbjPsdySewerageUserListAdapter(getContext(),new ArrayList<SewerageUserEntity>());
        mRvSwerageUserList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvSwerageUserList.setAdapter(mAdptSwerageUserList);
        mAdptSwerageUserList.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                SewerageUserEntity entity = mAdptSwerageUserList.getDataList().get(position - 1);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, null, entity);
                }
            }
        });
        mAdptSwerageUserList.setOnPshItemClickListener(new JbjPsdySewerageUserListAdapter.OnSewerageUserListItemClickListener() {
            @Override
            public void onItemClick(View v, RecyclerView.ViewHolder holder, SewerageUserEntity entity) {
//                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onItemClick(v, holder, entity);
//                }
            }

            @Override
            public void onLocation(View v, RecyclerView.ViewHolder holder, SewerageUserEntity entity) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onLocation(v, holder, entity);
                }
            }
        });

//        mEmptyView = findViewById(R.id.emptyView);
//        ((TextView) findViewById(R.id.text_title)).setText("查无数据");
////        findViewById(R.id.text_content).setVisibility(View.GONE);
//        Button mBtnRefresh = findViewById(R.id.button_retry);
//        mBtnRefresh.setVisibility(View.VISIBLE);
//        mBtnRefresh.setText("刷新");
//        mBtnRefresh.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadDataByType();
//            }
//        });
        mProgressBar = findViewById(R.id.progressbar);
        mRvSwerageUserList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                loadDataByType();
            }
        });
    }

    /**
     * 根据不同类型加载数据
     */
    private void loadDataByType() {
        if (mCurrState != STATE_NORMAL) {
            return;
        }
        String type = mHeader.getSelectedTypeStr();
        if (mShowSewerageUsers == null) return;
        if (!mIsFirstLoadData && mShowSewerageUsers.size() >= mHeader.getSelectedTypeCount()) {
            // 如果不是首次加载，且已获取所有数据的最大值，则不再获取数据
            mRvSwerageUserList.setNoMore(true);
            mAdptSwerageUserList.update(mShowSewerageUsers);
            mRvSwerageUserList.loadMoreComplete();
            return;
        }
        mHeader.setSelectable(false);
        mRvSwerageUserList.setLoadingMoreEnabled(true);
        mCurrState = STATE_LOADING;
//        mProgressBar.setVisibility(View.VISIBLE);
//        mEmptyView.setVisibility(View.INVISIBLE);
        mCurrTypeOfLoadingData = mHeader.getSelectedType();
        final int pageNo = getPageNo(mShowSewerageUsers.size());
        if(pageNo == 1){
            mProgressBar.showLoading();
        }
        // testcode
//        mRequestService.getSewerageUsersByType(pageNo, PAGE_SIZE, "2618", type)
        mRequestService.getSewerageUsersByType(pageNo, PAGE_SIZE, String.valueOf(mCurrComponent.getObjectId()), type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SewerageUserResultEntity>() {
                    @Override
                    public void onCompleted() {
//                        mProgressBar.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mProgressBar.setVisibility(View.INVISIBLE);
//                            }
//                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showLoadedError(e.getMessage());
                        mHeader.setSelectable(true);
//                        mProgressBar.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mProgressBar.setVisibility(View.INVISIBLE);
//                            }
//                        });
//                        ToastUtil.shortToast(getContext().getApplicationContext(), "获取排水户失败");
                        mCurrState = STATE_NORMAL;
                        if (pageNo == 1) {
                            showLoadedError(e.getLocalizedMessage());
                        } else {
                            ToastUtil.shortToast(getContext().getApplicationContext(), "加载更多失败");
                        }
//                        mEmptyView.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mEmptyView.setVisibility((mShowSewerageUsers == null || mShowSewerageUsers.isEmpty()) ? View.VISIBLE : View.INVISIBLE);
//                            }
//                        });
                    }

                    @Override
                    public void onNext(SewerageUserResultEntity sewerageUserResultEntity) {
                        if (mIsFirstLoadData) {
                            mIsFirstLoadData = false;
                        }
                        // 设置数据
                        mCurrState = STATE_SETTING_DATA;
                        if (sewerageUserResultEntity == null || ListUtil.isEmpty(sewerageUserResultEntity.getSewerageUserEntities()) && pageNo > 1) {
                            mRvSwerageUserList.setNoMore(true);
//                            mRvSwerageUserList.loadMoreComplete();
                            mHeader.setSelectable(true);
                            return;
                        }
                        mRvSwerageUserList.setNoMore(false);
                        handleSewerageUserResultEntity(sewerageUserResultEntity);
                    }
                });
    }

    private int getPageNo(int currTotal) {
//        return currTotal == 0 ? 1 : (int) Math.ceil(currTotal / PAGE_SIZE);
        return currTotal / PAGE_SIZE + 1;
    }

    private void setFirstVisiblePosition(int position) {
        switch (mHeader.getSelectedType()) {
            case SewerageUserEntity.TYPE_CATERING_TRADE:
                mCateringTradeSewerageFirstVisiblePosition = position;
                break;
            case SewerageUserEntity.TYPE_PRECIPITATE:
                mPrecipitateSewerageUserFirstVisiblePosition = position;
                break;
            case SewerageUserEntity.TYPE_DANGER:
                mDangerSewerageUserFirstVisiblePosition = position;
                break;
            case SewerageUserEntity.TYPE_LIFE:
                mLifeSewerageUserFirstVisiblePosition = position;
                break;
            case SewerageUserEntity.TYPE_UNKNOW:
            default:
                mAllSewerageUserFirstVisiblePosition = position;
                break;
        }
    }

    private int getFirstVisibleVisiblePosition() {
        int position = -1;
        switch (mHeader.getSelectedType()) {
            case SewerageUserEntity.TYPE_CATERING_TRADE:
                position = mCateringTradeSewerageFirstVisiblePosition;
                break;
            case SewerageUserEntity.TYPE_PRECIPITATE:
                position = mPrecipitateSewerageUserFirstVisiblePosition;
                break;
            case SewerageUserEntity.TYPE_DANGER:
                position = mDangerSewerageUserFirstVisiblePosition;
                break;
            case SewerageUserEntity.TYPE_LIFE:
                position = mLifeSewerageUserFirstVisiblePosition;
                break;
            case SewerageUserEntity.TYPE_UNKNOW:
            default:
                position = mAllSewerageUserFirstVisiblePosition;
                break;
        }
        return position;
    }

    public void showLoadedEmpty() {
        mProgressBar.showError("", "暂无数据", "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDataByType();
            }
        });
    }

    public void showLoadedError(String errorReason) {
        mProgressBar.showError("获取数据失败", errorReason, "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDataByType();
            }
        });
    }
}
