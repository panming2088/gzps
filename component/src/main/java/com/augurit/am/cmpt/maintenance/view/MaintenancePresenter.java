package com.augurit.am.cmpt.maintenance.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.common.Callback1;

/**
 * 描述：运维统计Presenter
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.stats.view
 * @createTime 创建时间 ：2017-02-24
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-24
 * @modifyMemo 修改备注：
 */
public class MaintenancePresenter {

    private View mView;
    private ViewGroup fl_content;   // 子功能视图容器
    private MemoryStatisticsView mMemoryView;   // 内存统计
    private StorageManagerView mStorageView;    // 存储管理
    private TrafficStatisticsView mTrafficView; // 流量统计
    private View mFuncViewCur;      // 当前显示的子功能视图
    private Callback1<Void> mBackCallback;
    private Animation mAnimIn;
    private Animation mAnimOut;

    public MaintenancePresenter(Context context) {
        init(context);
    }

    private void init(Context context) {
        mView = View.inflate(context, R.layout.maintenance_menu_view, null);
        fl_content = (ViewGroup) mView.findViewById(R.id.fl_content);
        mView.findViewById(R.id.btn_back).setOnClickListener(mOnClickListener);
        mView.findViewById(R.id.menu_storage).setOnClickListener(mOnClickListener);
        mView.findViewById(R.id.menu_memory).setOnClickListener(mOnClickListener);
        mView.findViewById(R.id.menu_traffic).setOnClickListener(mOnClickListener);
        mMemoryView = new MemoryStatisticsView(context, this);
        mStorageView = new StorageManagerView(context, this);
        mTrafficView = new TrafficStatisticsView(context, this);
        mAnimIn = AnimationUtils.loadAnimation(context, R.anim.fragment_scale_in);
        mAnimIn.setInterpolator(new DecelerateInterpolator());
        mAnimOut = AnimationUtils.loadAnimation(context, R.anim.fragment_scale_out);
        mAnimOut.setInterpolator(new DecelerateInterpolator());
    }

    public void show(ViewGroup container) {
        container.removeAllViews();
        container.addView(mView);
        container.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_back) {
                if (mBackCallback != null) {
                    mBackCallback.onCallback(null);
                }
                return;
            }
            if (id == R.id.menu_storage) {
                mFuncViewCur = mStorageView.getView();
            }
            if (id == R.id.menu_memory) {
                mFuncViewCur = mMemoryView.getView();
            }
            if (id == R.id.menu_traffic) {
                mFuncViewCur = mTrafficView.getView();
            }
            if (mFuncViewCur != null) {
                fl_content.removeAllViews();
                fl_content.addView(mFuncViewCur);
                mFuncViewCur.startAnimation(mAnimIn);
            }
        }
    };

    void showMenu() {
        mFuncViewCur.startAnimation(mAnimOut);
        fl_content.removeAllViews();
    }

    public void setBackListener(Callback1<Void> backListener) {
        mBackCallback = backListener;
    }
}
