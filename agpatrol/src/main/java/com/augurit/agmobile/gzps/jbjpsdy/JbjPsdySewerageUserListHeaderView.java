package com.augurit.agmobile.gzps.jbjpsdy;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;

/**
 * 挂接单元排水户列表Tab
 *
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/11/9 9:48
 */
public class JbjPsdySewerageUserListHeaderView extends LinearLayout implements View.OnClickListener {

    /**
     * 总数
     */
    private View mTabTotal;
    private TextView mTvTabTotal;
    /**
     * 餐饮
     */
    private View mTabCateringTrade;
    private TextView mTvTabCateringTrade;
    /**
     * 沉淀物
     */
    private View mTabPrecipitate;
    private TextView mTvTabPrecipitate;
    /**
     * 有毒有害
     */
    private View mTabDanger;
    private TextView mTvTabDanger;
    /**
     * 生活
     */
    private View mTabLife;
    private TextView mTvTabLife;

    /**
     * 当前选择的Tab
     */
    private View mCurrSelectedTabView;
    private int mCurrSelectedType;
    private String mCurrSelectedTypeStr;

    private OnTabSelectedListener mOnTabSelectedListener;

    private int mTotalCount = 0;
    private int mCateringTradeCount = 0;
    private int mPrecipitateCount = 0;
    private int mDangerCount = 0;
    private int mLifeCount = 0;

    private boolean mSelectable = true;

    public JbjPsdySewerageUserListHeaderView(Context context) {
        super(context);
        init(context);
    }

    public JbjPsdySewerageUserListHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JbjPsdySewerageUserListHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 设置每种类型的数据的数量
     */
    public void setCount(int type, int count) {
        switch (type) {
            case SewerageUserEntity.TYPE_CATERING_TRADE:
                setCateringTradeCount(count);
                break;
            case SewerageUserEntity.TYPE_PRECIPITATE:
                setPrecipitateCount(count);
                break;
            case SewerageUserEntity.TYPE_DANGER:
                setDangerCount(count);
                break;
            case SewerageUserEntity.TYPE_LIFE:
                setLifeCount(count);
                break;
            case SewerageUserEntity.TYPE_UNKNOW:
                setTotalCount(count);
                break;
            default:
                break;
        }
    }

    public int getSelectedTypeCount() {
        return getCountByType(mCurrSelectedType);
    }

    public int getCountByType(int type) {
        int count = 0;
        switch (type) {
            case SewerageUserEntity.TYPE_CATERING_TRADE:
                count = mCateringTradeCount;
                break;
            case SewerageUserEntity.TYPE_PRECIPITATE:
                count = mPrecipitateCount;
                break;
            case SewerageUserEntity.TYPE_DANGER:
                count = mDangerCount;
                break;
            case SewerageUserEntity.TYPE_LIFE:
                count = mLifeCount;
                break;
            case SewerageUserEntity.TYPE_UNKNOW:
            default:
                count = mTotalCount;
                break;
        }
        return count;
    }

    /**
     * 设置点击监听
     */
    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        mOnTabSelectedListener = listener;
    }

    public int getSelectedType() {
        return mCurrSelectedType;
    }

    public String getSelectedTypeStr() {
        return mCurrSelectedTypeStr;
    }

    public void reset() {
        setCount(SewerageUserEntity.TYPE_UNKNOW, 0);
        setCount(SewerageUserEntity.TYPE_CATERING_TRADE, 0);
        setCount(SewerageUserEntity.TYPE_PRECIPITATE, 0);
        setCount(SewerageUserEntity.TYPE_DANGER, 0);
        setCount(SewerageUserEntity.TYPE_LIFE, 0);
        setSelectedIndex(0);
    }

    public void setSelectedIndex(int position) {
        if (mCurrSelectedTabView != null) {
            mCurrSelectedTabView.setSelected(false);
        }
        mCurrSelectedTabView = null;
        mCurrSelectedType = SewerageUserEntity.TYPE_UNKNOW;
        mCurrSelectedTypeStr = null;
        switch (position) {
            case 0:
                mTabTotal.performClick();
                break;
            case 1:
                mTabCateringTrade.performClick();
                break;
            case 2:
                mTabPrecipitate.performClick();
                break;
            case 3:
                mTabDanger.performClick();
                break;
            case 4:
                mTabLife.performClick();
                break;
        }
    }

    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }

    @Override
    public void onClick(View v) {
        if (!mSelectable) {
            return;
        }
        if (v == mTabTotal) {
            mCurrSelectedType = SewerageUserEntity.TYPE_UNKNOW;
            mCurrSelectedTypeStr = null;
        } else if (v == mTabCateringTrade) {
            mCurrSelectedType = SewerageUserEntity.TYPE_CATERING_TRADE;
            mCurrSelectedTypeStr = SewerageUserEntity.TYPE_CATERING_TRADE_STR;
        } else if (v == mTabPrecipitate) {
            mCurrSelectedType = SewerageUserEntity.TYPE_PRECIPITATE;
            mCurrSelectedTypeStr = SewerageUserEntity.TYPE_PRECIPITATE_STR;
        } else if (v == mTabDanger) {
            mCurrSelectedType = SewerageUserEntity.TYPE_DANGER;
            mCurrSelectedTypeStr = SewerageUserEntity.TYPE_DANGER_STR;
        } else if (v == mTabLife) {
            mCurrSelectedType = SewerageUserEntity.TYPE_LIFE;
            mCurrSelectedTypeStr = SewerageUserEntity.TYPE_LIFE_STR;
        } else {
            mCurrSelectedType = SewerageUserEntity.TYPE_UNKNOW;
            mCurrSelectedTypeStr = null;
        }
        setSelected(v);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.drainage_view_jbjpsdy_sewerage_user_list_header, this);
        initView();
    }

    private void initView() {
        mTabTotal = findViewById(R.id.tabTotal);
        mTvTabTotal = (TextView) findViewById(R.id.tabTotalValue);
        mTabCateringTrade = findViewById(R.id.tabCateringTrade);
        mTvTabCateringTrade = (TextView) findViewById(R.id.tabCateringTradeValue);
        mTabPrecipitate = findViewById(R.id.tabPrecipitate);
        mTvTabPrecipitate = (TextView) findViewById(R.id.tabPrecipitateValue);
        mTabDanger = findViewById(R.id.tabDanger);
        mTvTabDanger = (TextView) findViewById(R.id.tabDangerValue);
        mTabLife = findViewById(R.id.tabLife);
        mTvTabLife = (TextView) findViewById(R.id.tabLifeValue);

        mTabTotal.setOnClickListener(this);
        mTabCateringTrade.setOnClickListener(this);
        mTabPrecipitate.setOnClickListener(this);
        mTabDanger.setOnClickListener(this);
        mTabLife.setOnClickListener(this);
    }

    /**
     * 设置当前选中的tab
     */
    private void setSelected(View selected) {
        if (selected == mCurrSelectedTabView) {
            return;
        }
        if (mCurrSelectedTabView != null) {
            mCurrSelectedTabView.setSelected(false);
        }
        mCurrSelectedTabView = selected;
        mCurrSelectedTabView.setSelected(true);
        if (mOnTabSelectedListener != null) {
            mOnTabSelectedListener.onTabSelected(mCurrSelectedType);
        }
    }

    /**
     * 设置餐饮数量
     */
    private void setCateringTradeCount(int count) {
        mCateringTradeCount = count;
        mTvTabCateringTrade.setText(String.valueOf(count));
//        calculateTotalCount();
    }

    /**
     * 设置沉淀物数量
     */
    private void setPrecipitateCount(int count) {
        mPrecipitateCount = count;
        mTvTabPrecipitate.setText(String.valueOf(count));
//        calculateTotalCount();
    }

    /**
     * 设置有害有毒物数量
     */
    private void setDangerCount(int count) {
        mDangerCount = count;
        mTvTabDanger.setText(String.valueOf(count));
//        calculateTotalCount();
    }

    /**
     * 设置生活数量
     */
    private void setLifeCount(int count) {
        mLifeCount = count;
        mTvTabLife.setText(String.valueOf(count));
//        calculateTotalCount();
    }

    /**
     * 设置总数
     */
    private void setTotalCount(int count) {
        mTotalCount = count;
        mTvTabTotal.setText(String.valueOf(mTotalCount));
    }

    /**
     * 计算总数
     */
    private void calculateTotalCount() {
        mTotalCount = mCateringTradeCount + mPrecipitateCount + mDangerCount + mLifeCount;
        mTvTabTotal.setText(String.valueOf(mTotalCount));
    }

    public interface OnTabSelectedListener {
        void onTabSelected(int type);
    }

}
