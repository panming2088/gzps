package com.augurit.am.cmpt.widget.searchview.util.adapter;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.searchview.util.adapter
 * @createTime 创建时间 ：17/4/11
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/4/11
 * @modifyMemo 修改备注：
 */


import android.text.Editable;
import android.text.TextWatcher;

public abstract class TextWatcherAdapter implements TextWatcher {

    private static final String TAG = "TextWatcherAdapter";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
