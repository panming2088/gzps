package com.augurit.agmobile.gzps.track.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.track.service.TrackService;
import com.augurit.agmobile.gzps.track.util.TrackConstant;
import com.augurit.agmobile.gzps.track.view.presenter.ITrackPresenter;
import com.augurit.agmobile.mapengine.common.DividerItemDecoration;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 轨迹列表页面
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view
 * @createTime 创建时间 ：2017-06-13
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-13
 * @modifyMemo 修改备注：
 */
public class TrackView implements ITrackView {

    private Context mContext;
    private ViewGroup mContainer;

    private ITrackPresenter mTrackPresenter;
    private View mView;
    TextView tv_sticky;
    private XRecyclerView track_list;
    private TrackListAdapter mTrackListAdapter;

    public TrackView(Context context, ViewGroup container) {
        this.mContext = context;
        this.mContainer = container;
        mView = getLayout();
        init();
    }

    private void init() {
        mView.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrackPresenter.back();
            }
        });
        ((TextView) mView.findViewById(R.id.tv_title)).setText("轨迹记录");

        tv_sticky = (TextView) mView.findViewById(R.id.tv_sticky);
        track_list = (XRecyclerView) mView.findViewById(R.id.track_list);
        mTrackListAdapter = new TrackListAdapter(mContext);
        track_list.setLayoutManager(new LinearLayoutManager(mContext));
        track_list.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        track_list.setAdapter(mTrackListAdapter);
        track_list.setPullRefreshEnabled(false);
        track_list.setLoadingMoreEnabled(false);
        mTrackListAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object selectedData) {
                showTrackRecordOnMapView((ArrayList<GPSTrack>) selectedData, TrackConstant.TRACK_VIEW);
            }

            @Override
            public void onItemLongClick(View view, int position, Object selectedData) {

            }
        });
        track_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 找到RecyclerView的item中，和RecyclerView的getTop 向下相距5个像素的那个item
                // (尝试2、3个像素位置都找不到，所以干脆用了5个像素)，
                // 我们根据这个item，来更新吸顶布局的内容，
                // 因为我们的StickyLayout展示的信息肯定是最上面的那个item的信息.
                View stickyInfoView = recyclerView.findChildViewUnder(tv_sticky.getMeasuredWidth() / 2, 5);
                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    tv_sticky.setText(String.valueOf(stickyInfoView.getContentDescription()));
                }

                // 找到固定在屏幕上方那个FakeStickyLayout下面一个像素位置的RecyclerView的item，
                // 我们根据这个item来更新假的StickyLayout要translate多少距离.
                // 并且只处理HAS_STICKY_VIEW和NONE_STICKY_VIEW这两种tag，
                // 因为第一个item的StickyLayout虽然展示，但是一定不会引起FakeStickyLayout的滚动.
                View transInfoView = recyclerView.findChildViewUnder(
                        tv_sticky.getMeasuredWidth() / 2, tv_sticky.getMeasuredHeight() + 1);

                if (transInfoView != null && transInfoView.getTag() != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - tv_sticky.getMeasuredHeight();

                    // 如果当前item需要展示StickyLayout，
                    // 那么根据这个item的getTop和FakeStickyLayout的高度相差的距离来滚动FakeStickyLayout.
                    // 这里有一处需要注意，如果这个item的getTop已经小于0，也就是滚动出了屏幕，
                    // 那么我们就要把假的StickyLayout恢复原位，来覆盖住这个item对应的吸顶信息.
                    if (transViewStatus == TrackListAdapter.HAS_STICKY_VIEW) {
                        if (transInfoView.getTop() > 0) {
                            tv_sticky.setTranslationY(dealtY);
                        } else {
                            tv_sticky.setTranslationY(0);
                        }
                    } else if (transViewStatus == TrackListAdapter.NONE_STICKY_VIEW) {
                        // 如果当前item不需要展示StickyLayout，那么就不会引起FakeStickyLayout的滚动.
                        tv_sticky.setTranslationY(0);
                    }
                }
            }
        });
        track_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                track_list.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                track_list.loadMoreComplete();
            }
        });
        mContainer.removeAllViews();
        mContainer.addView(mView);
    }

    protected View getLayout() {
        return LayoutInflater.from(mContext).inflate(R.layout.track_view_layout, null);
    }

    @Override
    public void initState(int currentTrackState) {
    }

    public void setPresenter(ITrackPresenter trackPresenter) {
        mTrackPresenter = trackPresenter;
    }

    public void showMessage(String msg) {
        ToastUtil.shortToast(mContext, msg);
    }


    public void showTrackRecordOnMapView(ArrayList<GPSTrack> gpsTrackList, String operation) {
        Intent intent = new Intent(mContext, ShowTrackActivity.class);
        if(!ListUtil.isEmpty(gpsTrackList)){
            intent.putExtra("trackId", "" + gpsTrackList.get(0).getTrackId());
        }
        intent.putExtra("operation", operation);
        mContext.startActivity(intent);
    }

    public void setTrackHistory(List<GPSTrack> trackHistory) {
//        mTrackListAdapter.notifyDataSetChanged(trackHistory);
//        if(ListUtil.isEmpty(trackHistory)){
//            track_list.setNoMore(true);
//            tv_sticky.setVisibility(View.GONE);
//        } else {
//            track_list.setNoMore(false);
//            tv_sticky.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onLoadMore(List<List<GPSTrack>> moreTrack) {
        mTrackListAdapter.loadMore(moreTrack);
        if(ListUtil.isEmpty(moreTrack)){
            track_list.setNoMore(true);
        } else {
            track_list.setNoMore(false);
        }
    }

    @Override
    public void setTrackLength(double trackLength) {
    }

    @Override
    public void setTrackTime(int trackTime) {
    }

    @Override
    public void setTrackPoint(GPSTrack trackPoint) {

    }

    @Override
    public  void switchState(@TrackService.TrackLocateState int trackState) {
    }

}