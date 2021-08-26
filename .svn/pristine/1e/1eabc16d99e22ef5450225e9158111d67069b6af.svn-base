package com.augurit.agmobile.gzps.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.augurit.agmobile.gzps.R;

/**
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/12/11 12:39
 */
public class CheckBoxItem extends RelativeLayout {

    private CheckBox mCbItem;

    public CheckBoxItem(Context context) {
        super(context);
        init(context);
    }

    public CheckBoxItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CheckBoxItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setCheck(boolean check) {
        mCbItem.setChecked(check);
    }

    public boolean isCheck() {
        return mCbItem.isChecked();
    }

    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        mCbItem.setEnabled(isEnabled);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_checkbox_item, this);

        mCbItem = findViewById(R.id.cb_1);
    }
}
