package com.augurit.agmobile.patrolcore.common.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

/**
 * 带有提示的输入框最大长度过滤器
 * 在输入已达到最大长度，用户继续输入时进行提示
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.util
 * @createTime 创建时间 ：2016-11-23 16:55
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2016-11-23 16:55
 */
public class MaxLengthInputFilter implements InputFilter{

    private int mMaxLength;     // 最大长度
    private EditText mEditText; // 输入框
    private TextInputLayout mTil;   // TextInputLayout
    private String mErrorText;  // 错误提示信息
    private int mDismissErrorDelay;  // 延时消除提示时长
    private boolean mIsChangeBackground;     // TextInputLayout提示错误是否改变EditText背景颜色
    private Drawable mBackgroundDrawable;  // 原始的EditText背景

    /**
     * EditText提示构造方法
     * @param maxLength 输入的最大长度
     * @param editText 输入框
     * @param errorText 错误信息
     */
    public MaxLengthInputFilter(int maxLength, EditText editText, String errorText){
        mMaxLength = maxLength;
        mEditText = editText;
        mErrorText = errorText;
        mDismissErrorDelay = 0;
    }

    /**
     * TextInputLayout提示构造方法
     * @param maxLength 输入的最大长度
     * @param textInputLayout TextInputLayout
     * @param editText 输入框
     * @param errorText 错误信息
     */
    public MaxLengthInputFilter(int maxLength, TextInputLayout textInputLayout, EditText editText, String errorText){
        mMaxLength = maxLength*2;
        mTil = textInputLayout;
        mEditText = editText;
        mErrorText = errorText;
        mDismissErrorDelay = 0;
        mIsChangeBackground = true;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 最大长度过滤
        String strDest = dest.toString();
        int remain = 0;   // 计算还能输入的字符数量
        try {
            end = source.toString().getBytes("GB2312").length;
            remain = mMaxLength - (strDest.getBytes("GB2312").length - (dend - dstart));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (remain < (end - start)){  // end-start为当前输入的字符长度，输入量超出该长度则提示错误
            if (mTil != null) {     // TextInputLayout提示
                mTil.setError(mErrorText);
                if (!mIsChangeBackground) {     // 不改变背景则要进行还原
                    restoreBackground(mEditText, mBackgroundDrawable);
                }
                if (mDismissErrorDelay != 0) {  // 延时消除
                    mTil.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mTil.setError(null);
                            restoreBackground(mEditText, mBackgroundDrawable);
                        }
                    }, mDismissErrorDelay);
                }
            } else if (mEditText != null) {     // EditText提示
                mEditText.setError(mErrorText);
                if (mDismissErrorDelay != 0) {
                    mEditText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mEditText.setError(null);
                        }
                    }, mDismissErrorDelay);
                }
            }
        } else {
            if (mTil != null) {
                mTil.setError(null);
                restoreBackground(mEditText, mBackgroundDrawable);
            } else if (mEditText != null) {
                mEditText.setError(null);
            }
        }
        if (remain <= 0) {
            return "";  // 已达到最大长度则不能再输入了
        } else if (remain >= (end - start)) {
            return null;    // 当前输入字符长度未超过最大长度，通过返回null来放行输入
        } else {
            String sour = source.toString();
            try {
                int len = sour.getBytes("GB2312").length-remain;
                int i = source.length()-len/2;
                while(sour.getBytes("GB2312").length>remain && i>0){
                    sour = sour.substring(start,start+i--);
                }
                if(sour.getBytes("GB2312").length>remain && i == 0){
                    sour="";
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
//            return source.subSequence(start, start + remain);     // 截下可输入的部分
            return sour;    // 截下可输入的部sour.getBytes("GB2312分截34
        }
    }

    /**
     * 设置延时消除错误提示
     * 默认为0
     * @param delay 延时时长，单位毫秒，0为不延时消除
     */
    public MaxLengthInputFilter setDismissErrorDelay(int delay) {
        mDismissErrorDelay = delay;
        return this;
    }

    /**
     * TextInputLayout显示错误时是否改变EditText的背景颜色
     * 默认为true，如果自定义了EditText的背景，建议设为false
     * @param isChange 是否改变
     */
    public void setChangeBackgroundEnabled(boolean isChange) {
        mIsChangeBackground = isChange;
    }

    /**
     * 设置原始的EditText背景
     * 5.0版本以下TextInputLayout改变自定义背景会导致原来背景消失
     * @param drawable 背景
     */
    public void setBackgroundDrawable(Drawable drawable) {
        mBackgroundDrawable = drawable;
    }

    /**
     * 恢复被TextInputLayout改变的背景
     * @param editText EditText
     * @param background  原本的背景
     */
    private void restoreBackground(EditText editText, Drawable background) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && mBackgroundDrawable != null) {
            int paddingLeft = mEditText.getPaddingLeft();
            int paddingTop = mEditText.getPaddingTop();
            int paddingRight = mEditText.getPaddingRight();
            int paddingBottom = mEditText.getPaddingBottom();
            mEditText.setBackground(mBackgroundDrawable);
            mEditText.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        } else {
            mEditText.getBackground().clearColorFilter();
        }
    }
}
