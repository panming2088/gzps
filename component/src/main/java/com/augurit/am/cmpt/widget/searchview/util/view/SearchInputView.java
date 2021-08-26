package com.augurit.am.cmpt.widget.searchview.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.searchview.util.view
 * @createTime 创建时间 ：17/4/11
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/4/11
 * @modifyMemo 修改备注：
 */


public class SearchInputView extends EditText {

    private OnKeyboardSearchKeyClickListener mSearchKeyListener;

    private OnKeyboardDismissedListener mOnKeyboardDismissedListener;

    private OnKeyListener mOnKeyListener = new OnKeyListener() {
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

            if (keyCode == KeyEvent.KEYCODE_ENTER && mSearchKeyListener != null) {
                mSearchKeyListener.onSearchKeyClicked();
                return true;
            }
            return false;
        }
    };

    public SearchInputView(Context context) {
        super(context);
        init();
    }

    public SearchInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnKeyListener(mOnKeyListener);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent ev) {
        if (ev.getKeyCode() == KeyEvent.KEYCODE_BACK && mOnKeyboardDismissedListener != null) {
            mOnKeyboardDismissedListener.onKeyboardDismissed();
        }
        return super.onKeyPreIme(keyCode, ev);
    }

    public void setOnKeyboardDismissedListener(OnKeyboardDismissedListener onKeyboardDismissedListener) {
        mOnKeyboardDismissedListener = onKeyboardDismissedListener;
    }

    public void setOnSearchKeyListener(OnKeyboardSearchKeyClickListener searchKeyListener) {
        mSearchKeyListener = searchKeyListener;
    }

    public interface OnKeyboardDismissedListener {
        void onKeyboardDismissed();
    }

    public interface OnKeyboardSearchKeyClickListener {
        void onSearchKeyClicked();
    }
}
