package com.augurit.am.cmpt.widget.drawerlayout;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 描述：侧滑抽屉Layout
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.drawerlayout
 * @createTime 创建时间 ：2017-02-22
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-22
 * @modifyMemo 修改备注：
 */
public class LeftDrawerLayout extends ViewGroup {
    private static final int MIN_DRAWER_MARGIN = 32; // 边缘可拖动抽屉范围 dp
    private static final int MIN_FLING_VELOCITY = 400; // 最小飞出速度 dp/s
    private static final int DRAWER_TOGGLE_WIDTH = 24;  // 抽屉切换按钮宽度 dp

    private int mMinDrawerMargin;   // drawer离父容器右边的最小外边距
    private int mToggleWidth;       // 按钮宽度 px
    private View mLeftMenuView;     // 抽屉View
    private View mContentView;      // 内容View
    private ViewDragHelper mHelper;

    private float mLeftMenuOnScreen;    // drawer显示出来的占自身的百分比
    private boolean mIsDrawerShow = false;      // drawer是否处于显示状态

    public LeftDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        mMinDrawerMargin = (int) (MIN_DRAWER_MARGIN * density + 0.5f);
        mToggleWidth = (int) (DRAWER_TOGGLE_WIDTH * density + 0.5f);

        mHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int newLeft = Math.max(-child.getWidth() + mToggleWidth, Math.min(left, 0));
                return newLeft;
            }

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mLeftMenuView;
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mHelper.captureChildView(mLeftMenuView, pointerId);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                final int childWidth = releasedChild.getWidth();
                float offset = (childWidth + releasedChild.getLeft()) * 1.0f / childWidth;
                mHelper.settleCapturedViewAt(xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -childWidth + mToggleWidth, releasedChild.getTop());
                invalidate();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                final int childWidth = changedView.getWidth();
                float offset = (float) (childWidth + left - mToggleWidth) / childWidth;
                mLeftMenuOnScreen = offset;
                mIsDrawerShow = offset != 0;
//                changedView.setVisibility(offset == 0 ? View.INVISIBLE : View.VISIBLE);
//                invalidate();
                if (mDrawerListener != null) {
                    mDrawerListener.onStatusChanged(mIsDrawerShow);
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mLeftMenuView == child ? child.getWidth() : 0;
            }
        });
        // 设置edge_left track
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        // 设置minVelocity
        mHelper.setMinVelocity(minVel);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);

        View leftMenuView = getChildAt(1);
        MarginLayoutParams lp = (MarginLayoutParams)
                leftMenuView.getLayoutParams();

        final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                mMinDrawerMargin + lp.leftMargin + lp.rightMargin,
                lp.width);
        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                lp.topMargin + lp.bottomMargin,
                lp.height);
        leftMenuView.measure(drawerWidthSpec, drawerHeightSpec);


        View contentView = getChildAt(0);
        lp = (MarginLayoutParams) contentView.getLayoutParams();
        final int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        contentView.measure(contentWidthSpec, contentHeightSpec);

        mLeftMenuView = leftMenuView;
        mContentView = contentView;


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View menuView = mLeftMenuView;
        View contentView = mContentView;

        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();
        contentView.layout(lp.leftMargin, lp.topMargin,
                lp.leftMargin + contentView.getMeasuredWidth(),
                lp.topMargin + contentView.getMeasuredHeight());

        lp = (MarginLayoutParams) menuView.getLayoutParams();

        final int menuWidth = menuView.getMeasuredWidth();
        int childLeft = -menuWidth + (int) (menuWidth * mLeftMenuOnScreen) + mToggleWidth;
        menuView.layout(childLeft, lp.topMargin, childLeft + menuWidth,
                lp.topMargin + menuView.getMeasuredHeight());
    }

    /**
     * 抽屉内容及主内容是否被触摸
     */
    private boolean mIsOtherAreaTouched = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsDrawerShow) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (ev.getX() <= mLeftMenuView.getRight() - mToggleWidth
                        || ev.getX() > mLeftMenuView.getRight()) {
                    mIsOtherAreaTouched = true;
                    return false;
                } else {
                    mIsOtherAreaTouched = false;
                }
            }
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                mHelper.abort();
                mIsOtherAreaTouched = false;
                return false;
            }
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                if (mIsOtherAreaTouched) {
                    return false;
                }
            }
        }
        boolean shouldInterceptTouchEvent = mHelper.shouldInterceptTouchEvent(ev);
        return shouldInterceptTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

    /**
     * 关闭抽屉
     */
    public void closeDrawer() {
        if (mIsDrawerShow) {
            View menuView = mLeftMenuView;
            mLeftMenuOnScreen = 0.f;
            mHelper.smoothSlideViewTo(menuView, -menuView.getWidth() + mToggleWidth, menuView.getTop());
            mIsDrawerShow = false;
            postInvalidate();
            if (mDrawerListener != null) {
                mDrawerListener.onStatusChanged(mIsDrawerShow);
            }
        }
    }

    /**
     * 打开抽屉
     */
    public void openDrawer() {
        if (!mIsDrawerShow) {
            View menuView = mLeftMenuView;
            mLeftMenuOnScreen = 1.0f;
            mHelper.smoothSlideViewTo(menuView, 0, menuView.getTop());
            mIsDrawerShow = true;
            postInvalidate();
            if (mDrawerListener != null) {
                mDrawerListener.onStatusChanged(mIsDrawerShow);
            }
        }
    }

    /**
     * 返回抽屉是否开启
     */
    public boolean isDrawerShow() {
        return mIsDrawerShow;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    /**
     * 抽屉状态监听接口
     */
    public interface DrawerListener {
        void onStatusChanged(boolean isDrawerShow);
    }
    private DrawerListener mDrawerListener;

    /**
     * 设置抽屉状态监听
     */
    public void setDrawerListener(DrawerListener drawerListener) {
        mDrawerListener = drawerListener;
    }
}