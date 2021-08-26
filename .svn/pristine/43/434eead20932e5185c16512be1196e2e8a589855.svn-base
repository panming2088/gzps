package com.augurit.am.cmpt.widget.swipemenulistview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 在{@link SwipeMenuAdapter}的基础上修改为RecyclerView的Adapter
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.baoyz.swipemenulistview
 * @createTime 创建时间 ：2016-11-27 14:02
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2016-11-27 14:02
 */

class SwipeMenuRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
                                      implements SwipeMenuView.OnSwipeItemClickListener{

    private Context mContext;
    private RecyclerView.Adapter mAdapter;
    private SwipeMenuRecyclerView.OnMenuItemClickListener mOnMenuItemClickListener;

    SwipeMenuRecyclerAdapter(Context context, RecyclerView.Adapter adapter) {
        mContext = context;
        mAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SwipeMenuLayout itemView;
        RecyclerView.ViewHolder childHolder = mAdapter.onCreateViewHolder(parent, viewType);
        SwipeMenu menu = new SwipeMenu(mContext);
        createMenu(menu);
        SwipeMenuView menuView = new SwipeMenuView(menu);
        menuView.setOnSwipeItemClickListener(this);
        itemView = new SwipeMenuLayout(childHolder.itemView, menuView);
        return new MyViewHolder(itemView, childHolder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            mAdapter.onBindViewHolder(myViewHolder.childHolder, position);
            SwipeMenuLayout layout = (SwipeMenuLayout) myViewHolder.itemView;
            layout.closeMenu();
            layout.setPosition(position);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {

    }

    public void createMenu(SwipeMenu menu) {

    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView.ViewHolder childHolder;

        MyViewHolder(View itemView, RecyclerView.ViewHolder childHolder) {
            super(itemView);
            this.childHolder = childHolder;
        }
    }
}
