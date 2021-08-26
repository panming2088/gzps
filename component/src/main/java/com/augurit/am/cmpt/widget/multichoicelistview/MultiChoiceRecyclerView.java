package com.augurit.am.cmpt.widget.multichoicelistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Key on 2016/5/3.
 *  支持多选的RecyclerView
 * @createTime 创建时间 ：2016-11-27 14:02
 */
public class MultiChoiceRecyclerView extends android.support.v7.widget.RecyclerView {

    private boolean isEnableMultiChoice = false;

    private ActionModeCallback mCallback;
    private AppCompatActivity mActivity;
    private ChoiceManager mManager;

    public MultiChoiceRecyclerView(Context context) {
        super(context);
        init();
    }

    public MultiChoiceRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiChoiceRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null && adapter instanceof Adapter) {
            ((com.augurit.am.cmpt.widget.multichoicelistview.MultiChoiceAdapter) adapter).init(mManager, new ChoiceCallback());
            ((com.augurit.am.cmpt.widget.multichoicelistview.MultiChoiceAdapter) adapter).setEnableMultiChoice(isEnableMultiChoice);
        }
    }

    private void init() {
        mManager = new ChoiceManager();
    }

    public void attachActivity(AppCompatActivity activity, ActionModeCallback callback) {
        this.mActivity = activity;
        this.mCallback = callback;
    }


    public void setMultiChoiceEnable(boolean enable) {
        isEnableMultiChoice = enable;
        if (getAdapter() != null && getAdapter() instanceof com.augurit.am.cmpt.widget.multichoicelistview.MultiChoiceAdapter) {
            ((com.augurit.am.cmpt.widget.multichoicelistview.MultiChoiceAdapter) getAdapter()).setEnableMultiChoice(isEnableMultiChoice);
        }
    }

    public SparseBooleanArray getSelectedPositions() {
        return mManager.getSelectedPositions().clone();
    }

    private class ChoiceCallback implements MultiChoiceCallback {

        @Override
        public void onStart() {
            if (!mManager.isSelectable() && isEnableMultiChoice && mActivity != null) {
                mActivity.startSupportActionMode(new DefaultActionCallback());
            }
        }
    }

    private class DefaultActionCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mManager.setSelectable(true);
            getAdapter().notifyDataSetChanged();
            if (mCallback != null) {
                return mCallback.onCreateActionMode(mode, menu);
            }
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            if (mCallback != null) {
                return mCallback.onPrepareActionMode(mode, menu);
            }
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (mCallback != null) {
                return mCallback.onActionItemClicked(mode, item);
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mManager.setSelectable(false);
            mManager.clear();
            getAdapter().notifyDataSetChanged();
            if (mCallback != null) {
                mCallback.onDestroyActionMode(mode);
            }
        }
    }

    public final class ChoiceManager {

        private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
        private boolean mSelectable = false;
        private boolean mAll = false;

        public void setItemCheck(int position, boolean isChecked) {
            if (isChecked) {
                mSelectedPositions.put(position, true);
            } else {
                mSelectedPositions.delete(position);
            }
            mSelectedPositions.put(position, isChecked);
            if (mCallback != null) {
                mCallback.onItemCheckedStateChanged(position, isChecked);
            }
        }

        public boolean isAll() {
            return mAll;
        }

        public void setAll(boolean mAll) {
            this.mAll = mAll;
        }

        public boolean isItemCheck(int position) {
            return mSelectedPositions.get(position);
        }

        public void clear() {
            mSelectedPositions.clear();
        }

        public void setSelectable(boolean selectable) {
            this.mSelectable = selectable;
        }

        public boolean isSelectable() {
            return this.mSelectable;
        }

        public SparseBooleanArray getSelectedPositions() {
            return mSelectedPositions;
        }

    }

    public interface MultiChoiceCallback {
        void onStart();
    }

    public interface ActionModeCallback {
        boolean onCreateActionMode(ActionMode mode, Menu menu);
        boolean onPrepareActionMode(ActionMode mode, Menu menu);
        boolean onActionItemClicked(ActionMode mode, MenuItem item);
        void onItemCheckedStateChanged(int position, boolean checked);
        void onDestroyActionMode(ActionMode mode);
    }

}
