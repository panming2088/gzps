package com.augurit.am.fw.utils.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.augurit.am.fw.utils.SdkUtil;


/**
 * Created by ac on 2016-07-26.
 */
public final class TextViewUtil {
    private TextViewUtil() {
    }

    /**
     * Add a mandatory sign
     *
     * @param text
     */
    public static void ItemRequired(TextView text) {
        text.append("*");
        String strs = text.getText().toString();
        SpannableStringBuilder style = new SpannableStringBuilder(strs);
        // style.setSpan(new
        // BackgroundColorSpan(Color.RED),strs.length()-1,strs.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.RED), strs.length() - 1,
                strs.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(style);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setIconForTextView(Context context,
                                          TextView targetTextView, int drawableId) {
        Drawable d = null;
        if (SdkUtil.isHighSdkVerson(Build.VERSION_CODES.LOLLIPOP)) {
            d = context.getDrawable(drawableId);
        } else {
            d = context.getResources().getDrawable(drawableId);
        }
        d.setBounds(0, 0, 20, 20);
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        // ImageSpan span = new ImageSpan(this, R.drawable.ic_user);
        SpannableString spanStr = new SpannableString("pic");
        spanStr.setSpan(span, spanStr.length() - 2, spanStr.length() - 1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        targetTextView.setText(spanStr);
    }
}
