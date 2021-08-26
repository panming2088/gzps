package com.augurit.am.fw.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 打开或关闭软键盘
 *
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.fw.utils
 * @createTime 创建时间 ：2016-11-17
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-17
 */

public final class KeyBoardUtils {
    private KeyBoardUtils() {
    }

    /**
     * 打卡软键盘
     *
     * @param editText 输入框
     * @param context  上下文
     */
    public static void openKeyboard(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param editText 输入框
     * @param context  上下文
     */
    public static void closeKeybord(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
