package com.augurit.agmobile.gzps.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.patrolcore.common.util.MaxLengthInputFilter;
import com.augurit.am.cmpt.widget.searchview.util.adapter.TextWatcherAdapter;

import java.io.UnsupportedEncodingException;

/**
 * 表单大量文本项
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.common.widget
 * @createTime 创建时间 ：17/11/7
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/7
 * @modifyMemo 修改备注：
 */

public class TextFieldTableItem extends RelativeLayout {


    private TextView tv_left;
    private EditText et_right;
    private TextView tv_size;

    public TextFieldTableItem(Context context) {
        this(context, null);
    }

    public TextFieldTableItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_table_field, this);
        tv_left = (TextView) view.findViewById(R.id.tv_);
        et_right = (EditText) view.findViewById(R.id.et_);
        tv_size = (TextView) view.findViewById(R.id.tv_size);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextFieldTableItem);
        String textName = a.getString(R.styleable.TextFieldTableItem_fieldtextViewName);
        String hint = a.getString(R.styleable.TextFieldTableItem_fieldeditTextHint);
        int maxLength = a.getInt(R.styleable.TextFieldTableItem_fieldeditMaxLength, -1);

        if (maxLength != -1) {
            setMaxLength(maxLength);
        }
        tv_left.setText(textName);
        et_right.setHint(hint);

        a.recycle();
    }

    public void setMaxLength(final int maxLength) {
        tv_size.setVisibility(VISIBLE);
        tv_size.setText("0/" + maxLength);
        et_right.setFilters(new InputFilter[]{new MaxLengthInputFilter(maxLength,
                null, et_right, "长度不能超过" + maxLength + "个字").setDismissErrorDelay(1500)});
        et_right.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                try {
                    String inputText = s.toString();
                    if (TextUtils.isEmpty(inputText)) {
                        tv_size.setText("0/" + maxLength);
                        return;
                    }
                    tv_size.setText(inputText.getBytes("GB2312").length / 2 + "/" + maxLength);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

public void setTvSizeVisible(int enable){
        tv_size.setVisibility(enable);
}

    public void setText(String text) {
        et_right.setText(text);
    }

    public void setEnableEdit(boolean isenable) {
        et_right.setFocusable(isenable);
        et_right.setEnabled(isenable);
        et_right.setFocusableInTouchMode(isenable);
    }

    public String getText() {
        return et_right.getText().toString();
    }

    public void setReadOnly() {
        et_right.setEnabled(false);
    }


}
