package com.augurit.agmobile.gzps.common.util;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;

/**
 * Created by xiaoyu on 2017/11/27.
 */

public class SetColorUtil {
    public static void setTextColor(Context context, int index, TextView... textViews) {
        for (int i = 0; i < textViews.length; i++) {
            if (i == index) {
                textViews[i].setTextColor(context.getResources().getColor(R.color.light_green_alpha ));
            } else {
                textViews[i].setTextColor(context.getResources().getColor(R.color.white_alpha));
            }
        }
    }

    public static void setBgColor(Context context, int index, LinearLayout... linearLayouts) {
        for (int i = 0; i < linearLayouts.length; i++) {
            if (i == index) {
                linearLayouts[i].setBackgroundColor(context.getResources().getColor(R.color.light_green_alpha));
            } else {
                linearLayouts[i].setBackgroundColor(context.getResources().getColor(R.color.white_alpha));
            }
        }
    }

}
