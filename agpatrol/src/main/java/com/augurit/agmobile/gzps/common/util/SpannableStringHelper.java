package com.augurit.agmobile.gzps.common.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.augurit.am.fw.utils.StringUtil;

/**
 * 包名：com.augurit.agmobile.gzps.common.util
 * 类描述：
 * 创建人：luobiao
 * 创建时间：2020/11/25 15:59
 * 修改人：luobiao
 * 修改时间：2020/11/25 15:59
 * 修改备注：
 */
public class SpannableStringHelper {


    public static SpannableStringBuilder builderString(String text, int start, int end, String colorString){
        SpannableStringBuilder sMStringBuilder;
        if(StringUtil.isEmpty(colorString)){
            return new SpannableStringBuilder(text);
        }
        try {
            sMStringBuilder = new SpannableStringBuilder(text);
            sMStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(colorString)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }catch (Exception ignored){
            sMStringBuilder = new SpannableStringBuilder(text);
        }
        return sMStringBuilder;
    }
}
