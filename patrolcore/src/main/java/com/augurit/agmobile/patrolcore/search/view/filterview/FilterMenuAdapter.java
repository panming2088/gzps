package com.augurit.agmobile.patrolcore.search.view.filterview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;

import java.util.List;

/**
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.search.view.filterview
 * @createTime 创建时间 ：2017/7/11
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/7/11
 * @modifyMemo 修改备注：
 */

class FilterMenuAdapter extends RecyclerView.Adapter {

    private static final int VIEWTYPE_HEAD = 0;
    private static final int VIEWTYPE_ITEM = 1;

    private FilterItem mHeadItem;
    private List<FilterItem> mItems;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private MenuItemHolder mSelectedHolder;
    private boolean mIsHeadShow = false;

    FilterMenuAdapter(Context mContext, List<FilterItem> mItems) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.filter_menu_item, null);
        return new MenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MenuItemHolder) {
            final MenuItemHolder myHoler = (MenuItemHolder) holder;
            final FilterItem item = mItems.get(position);
            myHoler.tv_name.setText(item.getName());
            myHoler.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        boolean isHeadItem = (position == 0 && mHeadItem != null);
                        mOnItemClickListener.onItemClick(position, item, isHeadItem);
                    }
                    mSelectedHolder = myHoler;
                    rotateArrow(myHoler, true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private void rotateArrow(final MenuItemHolder holder, boolean reverse) {
        ValueAnimator animator;
        if (reverse) {
            animator = ValueAnimator.ofFloat(0, 180);
        } else {
            animator = ValueAnimator.ofFloat(180, 0);
        }
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                holder.iv_arrow.setRotation(value);
            }
        });
        animator.start();
    }

    void onFilterDismiss() {
        if (mSelectedHolder != null) {
            rotateArrow(mSelectedHolder, false);
        }
    }

    private class MenuItemHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        ImageView iv_arrow;

        MenuItemHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_arrow = (ImageView) itemView.findViewById(R.id.iv_arrow_down);
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position, FilterItem item, boolean isHeadItem);
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    void setHeadItem(FilterItem filterItem) {
        mHeadItem = filterItem;
    }

    void setItems(List<FilterItem> filterItems) {
        mItems.clear();
        if (mHeadItem != null) {
            mItems.add(mHeadItem);
        }
        mItems.addAll(filterItems);
        notifyDataSetChanged();
    }
}
