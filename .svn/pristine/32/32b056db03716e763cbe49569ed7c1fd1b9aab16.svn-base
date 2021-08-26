package com.augurit.agmobile.gzps.uploadfacility.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.PipeStreamAdapter;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.esri.android.map.MapView;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名：com.augurit.agmobile.gzps.review.view
 * 类描述：
 * 创建人：luobiao
 * 创建时间：2019/6/6 16:50
 * 修改人：luobiao
 * 修改时间：2019/6/6 16:50
 * 修改备注：
 */
public class PipeStreamView {
    private MapView mapView;
    private Context mContext;
    private View mReview;
    private ViewGroup mViewGroup;
    private View tv_upstream_add;
    private View tv_error_correct;
    private View tv_downstream_add;
    private onAddStreamListener mOnAddStreamListener;
    private RecyclerView mRecyclerViewUp;
    private RecyclerView mRecyclerViewDown;
    private PipeStreamAdapter mSearchResultAdapter;
    //    private PipeStreamAdapter mDownAdapter;
    private TextView mTitle;

    public PipeStreamView(MapView mapManager,
                          ViewGroup actionContainerBehavior, Context context) {
        mContext = context;
        this.mapView = mapManager;
        this.mViewGroup = actionContainerBehavior;
        onCreateView();
    }

    private void onCreateView() {
        initView();
        if (mViewGroup != null) {
            mViewGroup.removeAllViews();
            mViewGroup.addView(mReview, new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private void initView() {
        mReview = LayoutInflater.from(mContext).inflate(R.layout.review_map_layout, null, false);
        //背景半透明
        mReview.getBackground().setAlpha(230);
        tv_upstream_add = mReview.findViewById(R.id.tv_upstream_add);
        mTitle = (TextView) mReview.findViewById(R.id.title);
        tv_error_correct = mReview.findViewById(R.id.btn_upload_event_journal);
        mRecyclerViewUp = (RecyclerView) mReview.findViewById(R.id.rv_upstream);
        mRecyclerViewUp.setHasFixedSize(true);
        mRecyclerViewUp.setNestedScrollingEnabled(true);
        LinearLayoutManager upLayoutManager = new LinearLayoutManager(mContext);
        upLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerViewUp.setLayoutManager(upLayoutManager);
        mSearchResultAdapter = new PipeStreamAdapter(mContext, new ArrayList<PipeBean>());
        mRecyclerViewUp.setAdapter(mSearchResultAdapter);
        mRecyclerViewDown = (RecyclerView) mReview.findViewById(R.id.rv_downstream);
        mRecyclerViewDown.setHasFixedSize(true);
        mRecyclerViewDown.setNestedScrollingEnabled(true);
        LinearLayoutManager downLayoutManager = new LinearLayoutManager(mContext);
        downLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerViewDown.setLayoutManager(downLayoutManager);
//        mDownAdapter = new PipeStreamAdapter(mContext, new ArrayList<PipeBean>());
//        mRecyclerViewDown.setAdapter(mDownAdapter);
        tv_upstream_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加上游窨井
                if (mOnAddStreamListener != null) {
                    mOnAddStreamListener.onSelectStream(true);
                }
            }
        });
        tv_downstream_add = mReview.findViewById(R.id.tv_downstream_add);
        tv_downstream_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加下游窨井
                mOnAddStreamListener.onSelectStream(false);
            }
        });
        tv_error_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交按钮
                uploadReview();
            }
        });
        mSearchResultAdapter.setOnItemLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position, long id) {
                showAlertDialog(position);
                return false;
            }
        });
//        mDownAdapter.setOnItemLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(View view, int position, long id) {
//                showAlertDialog(position);
//                return false;
//            }
//        });
    }

    /**
     * 提交
     */
    private void uploadReview() {
//        mReviewService.upload(null);
    }

    public void showAlertDialog(final int position) {
        DialogUtil.MessageBox(mContext, "提示", "是否删除？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSearchResultAdapter.deleteData(position);
                if (mOnAddStreamListener != null) {
                    mOnAddStreamListener.onDeleteStream(mSearchResultAdapter.getDataList());
                }


            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    public void setOnAddStreamListener(onAddStreamListener onAddStreamListener) {
        mOnAddStreamListener = onAddStreamListener;
    }

    public interface onAddStreamListener {
        void onSelectStream(boolean isUpOrDownStream);

        void onDeleteStream(List<PipeBean> upStreams);
    }

    public void addData(List<PipeBean> pipeBeans) {
        mSearchResultAdapter.clear();
        mSearchResultAdapter.addData(pipeBeans);
    }

    public void refreshData() {
        mRecyclerViewDown.scrollToPosition(0);
    }

    public void clear() {
        mSearchResultAdapter.clear();
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

}
