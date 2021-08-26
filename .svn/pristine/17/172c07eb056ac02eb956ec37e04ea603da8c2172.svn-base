package com.augurit.agmobile.gzps.common.callback;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * @PROJECT GZZHPS
 * @USER Augurit
 * @CREATE 2020/10/27 16:22
 */
public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private final RecyclerView mRv;
    private final GestureDetectorCompat mGestureDetectorCompat;

    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        mRv = recyclerView;
        mGestureDetectorCompat = new GestureDetectorCompat(mRv.getContext(), new RecyclerViewTouchHelper());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemClick(RecyclerView.ViewHolder viewHolder);

    class RecyclerViewTouchHelper extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            RecyclerView.LayoutManager manager = mRv.getLayoutManager();
            if (manager instanceof LinearLayoutManager) {
                View view = mRv.findChildViewUnder(e.getX(), e.getY());
                if (view != null) {
                    RecyclerView.ViewHolder vh = mRv.getChildViewHolder(view);
                    onItemClick(vh);
                    return true;
                }
            }
            return super.onSingleTapUp(e);
        }
    }
}
