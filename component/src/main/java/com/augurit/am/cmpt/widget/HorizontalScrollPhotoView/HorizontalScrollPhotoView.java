package com.augurit.am.cmpt.widget.HorizontalScrollPhotoView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.augurit.am.fw.utils.DisplayUtil;
import com.augurit.am.fw.utils.ResourceUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.HorizontalScrollPhotoView
 * @createTime 创建时间 ：17/3/8
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/8
 * @modifyMemo 修改备注：
 */

public class HorizontalScrollPhotoView extends HorizontalScrollView implements View.OnClickListener,View.OnLongClickListener {

    public static final String TAG = "HorizontalScrollPhotoView";
    public final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
    public final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

    private CurrentImageChangeListener mListener;
    private OnItemClickListener mOnClickListener;
    private OnItemLongClickListener mOnLongClickListener;
    // 子View之间的间隔
    private int mChildViewMargin;
    // 每个屏幕最多显示的个数
    private int mCountOneScreen;
    // HorizontalListView中的LinearLayout
    private LinearLayout mContainer;
    // 数据适配器
    private BaseAdapter mAdapter;
    // 子元素的宽度
    private int mChildWidth;
    // 子元素的高度
    private int mChildHeight;
    // 保存View与位置的键值对
    private Map<View, Integer> mViewPos = new HashMap<View, Integer>();
    // 当前最后一张图片的index
    private int mCurrentIndex;
    // 当前第一张图片的下标
    private int mFristIndex;
    private int screenWidth;
    private Context mContext;


    public HorizontalScrollPhotoView(Context context) {
        this(context,null,0);
    }

    public HorizontalScrollPhotoView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public HorizontalScrollPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        mChildViewMargin = attrs.getAttributeIntValue(MATERIALDESIGNXML,"childViewMargin", 5);
        screenWidth = DisplayUtil.getWindowWidth(mContext);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContainer = (LinearLayout) getChildAt(0);
    }

    /**
     * 初始化数据，设置数据适配器并滚动到第一屏
     *
     * @param mAdapter
     */
    public void initDatas(BaseAdapter mAdapter) {
        initialization(mAdapter);
        // 初始化第一屏幕的元素
        initFirstScreenChildren();
    }

    private void initialization(BaseAdapter mAdapter) {
        this.mAdapter = mAdapter;
        mContainer = (LinearLayout) getChildAt(0);
        // 获得适配器中第一个View
        final View view = mAdapter.getView(0, null, mContainer);
        mContainer.addView(view);

        // 强制计算当前View的宽和高
        if (mChildWidth == 0 && mChildHeight == 0) {
            int w = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            int h = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            mChildHeight = view.getMeasuredHeight();
            mChildWidth = view.getMeasuredWidth();
            LogUtil.w(TAG, view.getMeasuredWidth() + "," + view.getMeasuredHeight());
            mChildHeight = view.getMeasuredHeight();

            // 计算每次加载多少个View
            mCountOneScreen = ((screenWidth * 3 / 4) / (mChildWidth + mChildViewMargin * 2)) + 2;
            // if (mCountOneScreen > mAdapter.getCount()) {
            // mCountOneScreen = mAdapter.getCount();
            // }
            LogUtil.w(TAG, "screenWidth*3/4=" + screenWidth * 2 / 3
                    + ",getCount()=" + mAdapter.getCount()
                    + ",mCountOneScreen = " + mCountOneScreen
                    + " ,mChildWidth = " + mChildWidth);
        }

    }

    /**
     * 刷新并滚动到最后一屏
     */
    public void notifyDataSetChanged(HorizontalScrollPhotoViewAdapter mAdapter) {
        initialization(mAdapter);
        initLastScreenChildren();
    }

    /**
     * 加载第一屏的View
     */
    private void initFirstScreenChildren() {
        mContainer = (LinearLayout) getChildAt(0);
        mContainer.removeAllViews();
        mViewPos.clear();
        int maxCount = mCountOneScreen > mAdapter.getCount() ? mAdapter
                .getCount() : mCountOneScreen;
        for (int i = 0; i < maxCount; i++) {
            View view = mAdapter.getView(i, null, mContainer);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            setMargin(view);
            mContainer.addView(view);
            mViewPos.put(view, i);
            mCurrentIndex = i;
        }
        if (mListener != null) {
            notifyCurrentImgChanged();
        }

    }

    private void initLastScreenChildren() {
        mContainer = (LinearLayout) getChildAt(0);
        mContainer.removeAllViews();
        mViewPos.clear();
        int maxCount = mCountOneScreen > mAdapter.getCount() ? mAdapter
                .getCount() : mCountOneScreen;
        mFristIndex = mAdapter.getCount() - maxCount;
        for (int i = (mAdapter.getCount() - maxCount); i < mAdapter.getCount(); i++) {
            View view = mAdapter.getView(i, null, mContainer);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            setMargin(view);
            mContainer.addView(view);
            mViewPos.put(view, i);
            mCurrentIndex = i;
        }

        scrollTo(mChildWidth + getScrollMargin(), 0);
        //        Log.v(TAG, "real_scroll==" + (mChildWidth + getScrollMargin()));
        if (mListener != null) {
            notifyCurrentImgChanged();
        }
    }

    private int getScrollMargin() {
        int maxCount = mCountOneScreen > mAdapter.getCount() ? mAdapter
                .getCount() : mCountOneScreen;
        return mChildWidth
                - ((screenWidth * 3 / 4) - (mChildWidth + mChildViewMargin * 2)
                * (maxCount - 2)) + mChildViewMargin * 2;
    }

    private void setMargin(View view) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view
                .getLayoutParams();
        lp.leftMargin = mChildViewMargin;
        lp.rightMargin = mChildViewMargin;
        view.setLayoutParams(lp);
    }

    /**
     * 滑动时的回调
     */
    public void notifyCurrentImgChanged() {
        // 先清除所有的背景色，点击时会设置为蓝色
        for (int i = 0; i < mContainer.getChildCount(); i++) {
            // mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
            mContainer.getChildAt(i).setBackgroundDrawable(
                    getResources().getDrawable(
                            ResourceUtil.getDrawableId(mContext,
                                    "itme_background_normal")));

        }

        mListener.onCurrentImgChanged(mFristIndex, mContainer.getChildAt(0));

    }

    /**
     * 加载下一张图片
     */
    protected void loadNextImg() {
        // 数组边界值计算
        if (mAdapter == null || mCurrentIndex == mAdapter.getCount() - 1) {
            return;
        }
        // 移除第一张图片，且将水平滚动位置置0
        scrollTo(0, 0);
        mViewPos.remove(mContainer.getChildAt(0));
        mContainer.removeViewAt(0);

        // 获取下一张图片，并且设置onclick事件，且加入容器中
        View view = mAdapter.getView(++mCurrentIndex, null, mContainer);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        setMargin(view);
        mContainer.addView(view);
        mViewPos.put(view, mCurrentIndex);

        // 当前第一张图片小标
        mFristIndex++;
        // 如果设置了滚动监听则触发
        if (mListener != null) {
            notifyCurrentImgChanged();
        }

    }

    /**
     * 加载前一张图片
     */
    protected void loadPreImg() {
        // 如果当前已经是第一张，则返回
        if (mFristIndex == 0)
            return;
        int maxCount = mCountOneScreen > mAdapter.getCount() ? mAdapter
                .getCount() : mCountOneScreen;
        // 获得当前应该显示为第一张图片的下标
        int index = mCurrentIndex - maxCount;
        if (index >= 0) {
            // mContainer = (LinearLayout) getChildAt(0);
            // 移除最后一张
            int oldViewPos = mContainer.getChildCount() - 1;
            mViewPos.remove(mContainer.getChildAt(oldViewPos));
            mContainer.removeViewAt(oldViewPos);

            // 将此View放入第一个位置
            View view = mAdapter.getView(index, null, mContainer);
            mViewPos.put(view, index);
            setMargin(view);
            mContainer.addView(view, 0);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            // 水平滚动位置向左移动view的宽度个像素
            scrollTo(mChildWidth, 0);
            // 当前位置--，当前第一个显示的下标--
            mCurrentIndex--;
            mFristIndex--;
            // 回调
            if (mListener != null) {
                notifyCurrentImgChanged();

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //                Log.v(TAG, getScrollX() + "");

                int scrollX = getScrollX();
                // 如果当前scrollX为view的宽度，加载下一张，移除第一张
                if (scrollX >= mChildWidth) {
                    loadNextImg();
                }
                // 如果当前scrollX = 0， 往前设置一张，移除最后一张
                if (scrollX == 0) {
                    loadPreImg();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    @Override
    public void onClick(View view) {
        if (mOnClickListener != null) {
            for (int i = 0; i < mContainer.getChildCount(); i++) {
                // mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
                mContainer.getChildAt(i).setBackgroundDrawable(
                        getResources().getDrawable(
                                ResourceUtil.getDrawableId(mContext,
                                        "itme_background_normal")));
            }

            mOnClickListener.onItemClick(view, mViewPos.get(view));
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public void setOnItemLongClickListener(
            OnItemLongClickListener mOnLongClickListener) {
        this.mOnLongClickListener = mOnLongClickListener;
    }

    public void setCurrentImageChangeListener(
            CurrentImageChangeListener mListener) {
        this.mListener = mListener;
    }


    @Override
    public boolean onLongClick(View view) {
        if (mOnLongClickListener != null) {
            return mOnLongClickListener.onItemLongClick(view, mViewPos.get(view));
        }
        return false;
    }


    /**
     * 图片滚动时的回调接口
     */
    public interface CurrentImageChangeListener{
        void onCurrentImgChanged(int position,View viewIndicator);
    }

    /**
     * 具体条目点击时的回调接口
     */
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    /**
     * 具体条目长按回调接口
     */
    public interface OnItemLongClickListener{
        boolean onItemLongClick(View view,int position);
    }
}
