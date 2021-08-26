package com.augurit.agmobile.mapengine.common.widget.callout.attribute;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.R;
import com.augurit.agmobile.mapengine.common.DividerItemDecoration;
import com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior;
import com.augurit.am.cmpt.widget.bottomsheet.IBottomSheetBehavior;
import com.augurit.am.fw.utils.DisplayUtil;

/**
 * 候选列表和详情的组合控件
 *
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.am.map.arcgis.identify
 * @createTime 创建时间 ：2016-12-27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：(1) 2017-03-01 加入mHideMoreClickListener
 */
public class CandidateContainer {

    public IBottomSheetBehavior mBehavior;
    protected Context mContext;
    protected ViewGroup mParentViewGroup;//装载候选列表和详情界面的总Parent
    protected ViewGroup mResultListParentViewGroup;//装载候选列表的Parent
    protected ViewGroup mMoreDetailPrentViewGroup;//装载详情界面的Parent
    protected OnHideClickListener mHideParentClickListener;//隐藏组合控件时需要额外响应的事件
    protected OnHideClickListener mHideMoreClickListener;//隐藏更多属性时需要额外的相应事件 xcl 2017-03-01
    /*private View resultListViewTemp;
    private View moreDetailViewTemp;*/

    /**
     * 候选列表及详情构造函数
     *
     * @param context
     * @param viewGroup 显示候选列表及详情的父组件
     */
    public CandidateContainer(Context context, ViewGroup viewGroup) {
        this.mContext = context;
        this.mParentViewGroup = viewGroup;
        initSecondParentView();
    }

    /**
     * 候选列表及详情构造函数
     *
     * @param context
     * @param viewGroup               显示候选列表及详情的父组件
     * @param hideParentClickListener 候选列表关闭时额外事件，可以为空
     */
    public CandidateContainer(Context context, ViewGroup viewGroup, OnHideClickListener hideParentClickListener) {
        this(context, viewGroup);
        this.mHideParentClickListener = hideParentClickListener;
    }

    /**
     * 获取详情View
     *
     * @param context
     * @param backButtonClickListener  设备为手机时返回按钮的点击事件
     * @param closeButtonClickListener 设备为平板时关闭按钮的点击事件
     * @return
     */
    public static View getMoreAttributesView(Context context, View.OnClickListener backButtonClickListener, View.OnClickListener closeButtonClickListener) {

        View moreView = getLayout(context);
        View backView = moreView.findViewById(R.id.ll_attributelist_back);
        backView.setVisibility(View.GONE);
        View btn_close = moreView.findViewById(R.id.btn_close);
        if (backButtonClickListener != null) {
            backView.setVisibility(View.VISIBLE);
            backView.setOnClickListener(backButtonClickListener);
        } else {
            backView.setVisibility(View.GONE);
        }
        if (closeButtonClickListener != null) {
            btn_close.setVisibility(View.VISIBLE);
            btn_close.setOnClickListener(closeButtonClickListener);
        } else {
            btn_close.setVisibility(View.GONE);
            backView.setVisibility(View.VISIBLE);
        }
        RecyclerView rv = (RecyclerView) moreView.findViewById(R.id.rv_listcallout_attributelist);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setItemAnimator(new DefaultItemAnimator());
        return moreView;
    }

    protected static View getLayout(Context context) {
        return View.inflate(context, R.layout.activity_callout_attributelist, null);
    }

    /**
     * 用数据填充详情View
     *
     * @param displayName           标题（显示名称）
     * @param attributesListadapter 列表属性数据适配器
     * @param mMoreDetailView       详情View
     */
    public static void setMoreDetailData(String displayName, RecyclerView.Adapter attributesListadapter, View mMoreDetailView) {
        TextView tv_title = (TextView) mMoreDetailView.findViewById(R.id.tv_title);
        if (TextUtils.isEmpty(displayName.trim())) {
            tv_title.setText("<<空>>");
        } else {
            tv_title.setText(displayName);
        }
        RecyclerView rv = (RecyclerView) mMoreDetailView.findViewById(R.id.rv_listcallout_attributelist);
        rv.setAdapter(attributesListadapter);
    }

    /**
     * 获取详情的父容器（暂时可用于判断手机还是平板界面）
     *
     * @return
     */
    public ViewGroup getMoreDetailPrentViewGroup() {
        return mMoreDetailPrentViewGroup;
    }

    /**
     * 获取候选列表View
     *
     * @param context
     * @param closeClickListener 候选列表右上方的关闭按钮点击事件
     * @return
     */
    protected View getResultListView(Context context, View.OnClickListener closeClickListener) {
        View view = View.inflate(context, R.layout.identify_result_view, null);
        View btn_close = view.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(closeClickListener);
        RecyclerView rv_result = (RecyclerView) view.findViewById(R.id.rv_result);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv_result.setLayoutManager(layoutManager);
        rv_result.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        rv_result.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    /**
     * 初始化装载候选列表和详情的父容器
     */
    public void initSecondParentView() {
        mResultListParentViewGroup = (ViewGroup) mParentViewGroup.findViewById(R.id.rl_candidate_list_container);
        mMoreDetailPrentViewGroup = (ViewGroup) mParentViewGroup.findViewById(R.id.rl_more_attribute_container);
        if (mMoreDetailPrentViewGroup == null ) {//暂将此条件用于判断是否是手机版，设备为手机时使用BottomSheet
            mBehavior = BottomSheetBehavior.from(mParentViewGroup);
            int height = DisplayUtil.getWindowHeight(mContext);
            //设置用户执行上划操作后的停止地点是屏幕的1/2处
            mBehavior.setAnchorHeight(height * 2 / 3);
        }
    }

    /**
     * 隐藏所有View
     */
    public void hide() {
        hideParentViewGroup();
        /*removeResultListView();//清除子View且隐藏候选列表
        removeMoreDetailView();//清除子View且隐藏详情*/
        hideResultList();
        hideMoreDetail();
        if (this.mHideParentClickListener != null) {
            this.mHideParentClickListener.onCloseClick();
        }
    }

    /**
     * 清除当前候选列表且隐藏父容器
     */
   /* protected void removeResultListView() {
        if (mResultListParentViewGroup != null && resultListViewTemp != null) {
            mResultListParentViewGroup.removeView(resultListViewTemp);
        }
        hideResultList();
    }*/

    /**
     * 清除当前详情界面且隐藏父容器
     */
    /*protected void removeMoreDetailView() {
        if (mMoreDetailPrentViewGroup != null) {
            mMoreDetailPrentViewGroup.removeView(moreDetailViewTemp);
        }
        hideMoreDetail();
    }*/

    /**
     * 显示候选列表
     */
    public void showResultList() {
        showParentViewGroup();
        if (mResultListParentViewGroup != null) {
            mResultListParentViewGroup.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏候选列表
     */
    public void hideResultList() {
        if (mResultListParentViewGroup != null) {
            mResultListParentViewGroup.setVisibility(View.GONE);
        }
    }

    /**
     * 显示详情
     */
    public void showMoreDetail() {
        showParentViewGroup();
        if (mMoreDetailPrentViewGroup != null) {
            mMoreDetailPrentViewGroup.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏详情
     */
    public void hideMoreDetail() {
        if (mMoreDetailPrentViewGroup != null) {
            mMoreDetailPrentViewGroup.setVisibility(View.GONE);
        }
        if (this.mHideMoreClickListener != null) {
            this.mHideMoreClickListener.onCloseClick();
        }
    }

    /**
     * 显示候选列表和详情界面的总Parent
     */
    protected void showParentViewGroup() {
        if (mParentViewGroup != null) {
            mParentViewGroup.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏总Parent
     */
    protected void hideParentViewGroup() {
        if (mParentViewGroup != null) {
            mParentViewGroup.setVisibility(View.GONE);
        }
    }

    /**
     * 获取候选列表View
     *
     * @return
     */
    public ViewGroup getResultListParentView() {
        return mResultListParentViewGroup;
    }

    /**
     * 获取详情View
     *
     * @return
     */
    public ViewGroup getMoreDetailPrentView() {
        return mMoreDetailPrentViewGroup;
    }

    /**
     * 设置关闭按钮的额外响应
     *
     * @param hideClickListener
     */
    public void setHideClickListener(OnHideClickListener hideClickListener) {
        this.mHideParentClickListener = hideClickListener;
    }
    /**
     * 设置关闭更多属性按钮的额外响应
     *
     * @param hideClickListener
     */
    public void setHideMoreClickListener(OnHideClickListener hideClickListener) {
        this.mHideMoreClickListener = hideClickListener;
    }

    /**
     * 更新候选列表
     *
     * @param resultLength
     * @param resultListadapter
     */
    public void updateResultList(int resultLength, RecyclerView.Adapter resultListadapter) {
        hideMoreDetail();//先隐藏详情，再显示候选列表
        addResultListView();
        setResultListData(resultLength, resultListadapter);
        showResultList();
    }

    /**
     * 在的Parent中添加候选列表
     *
     * @return
     */
    protected void addResultListView() {
        if (mResultListParentViewGroup == null) {
            return;
        }
        if (mResultListParentViewGroup.getChildCount() == 0) {
            View resultListViewTemp = getResultListView(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
            mResultListParentViewGroup.addView(resultListViewTemp);
        }
    }

    /**
     * 用数据填充候选列表
     *
     * @param resultLength
     * @param resultListadapter
     */
    protected void setResultListData(int resultLength, RecyclerView.Adapter resultListadapter) {
        TextView tv_size = (TextView) mResultListParentViewGroup.findViewById(R.id.tv_result);
        tv_size.setText("附近还查询到(" + resultLength + ")个 结果");
        RecyclerView rv_result = (RecyclerView) mResultListParentViewGroup.findViewById(R.id.rv_result);
        rv_result.setAdapter(resultListadapter);
    }

    /**
     * 更新详情界面
     *
     * @param displayName
     * @param attributesListadapter
     */
    public void updateMoreDetail(String displayName, RecyclerView.Adapter attributesListadapter) {
        addMoreDetailView();
        controlMoreDetailButton();
        setMoreDetailData(displayName, attributesListadapter, mMoreDetailPrentViewGroup);
        showMoreDetail();
    }

    /**
     * 控制详情界面的关闭按钮
     */
    protected void controlMoreDetailButton() {
        View btn_close = mMoreDetailPrentViewGroup.findViewById(R.id.btn_close);
        View backView = mMoreDetailPrentViewGroup.findViewById(R.id.ll_attributelist_back);
        if (mResultListParentViewGroup == null || (mResultListParentViewGroup != null && mResultListParentViewGroup.getVisibility() == View.GONE)
                || mResultListParentViewGroup.getChildCount() <= 0) {
            btn_close.setVisibility(View.VISIBLE);
            backView.setVisibility(View.GONE);
        } else {
            btn_close.setVisibility(View.GONE);
            backView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 在的Parent中添加详情界面
     *
     * @return
     */
    protected void addMoreDetailView() {
        if (mMoreDetailPrentViewGroup == null) {
            return;
        }
        if (mMoreDetailPrentViewGroup.getChildCount() == 0) {
            View moreView = getMoreAttributesView(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideMoreDetail();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
            mMoreDetailPrentViewGroup.addView(moreView);
        }
    }

    /**
     * 结果视图点击监听
     */
    public interface OnHideClickListener {
        /**
         * 关闭按钮点击
         */
        void onCloseClick();
    }
}
