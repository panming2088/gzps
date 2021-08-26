package com.augurit.agmobile.gzps.common.widget;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;

/**
 * 包名：com.augurit.agmobile.gzps.common.widget
 * 类描述：
 * 创建人：luobiao
 * 创建时间：2020/3/25 10:08
 * 修改人：luobiao
 * 修改时间：2020/3/25 10:08
 * 修改备注：
 */
public class NumberDecimalFilter extends DigitsKeyListener {

    public NumberDecimalFilter() {
        super(false, true);
    }

    /**
     * 默认保留 2 位置小数点
     */
    private int digits = 2;

    public NumberDecimalFilter setDigits(int d) {
        digits = d;
        return this;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);
        if (out != null) {
            source = out;
            start = 0;
            end = out.length();
        }
        int len = end - start;
        if (len == 0) {
            return source;
        }

        //以点开始的时候，自动在前面添加0
        if (source.toString().equals(".") && dstart == 0) {
            return "0.";
        }
        //如果起始位置为0,且第二位跟的不是".",则无法后续输入
        if (!source.toString().equals(".") && dest.toString().equals("0")) {
            return "";
        }
        int dlen = dest.length();
        for (int i = 0; i < dstart; i++) {
            if (dest.charAt(i) == '.') {
                return (dlen - (i + 1) + len > digits) ? "" : new SpannableStringBuilder(source, start, end);
            }

        }

        for (int i = start; i < end; ++i) {
            if (source.charAt(i) == '.') {
                if ((dlen - dend) + (end - (i + 1)) > digits)
                    return "";
                else
                    break;
            }
        }
        return new SpannableStringBuilder(source, start, end);
    }
}
