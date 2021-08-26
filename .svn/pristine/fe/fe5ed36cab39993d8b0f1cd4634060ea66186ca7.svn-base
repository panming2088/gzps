package com.augurit.am.fw.utils;

import android.graphics.Color;

import java.math.BigDecimal;

/**
 * Created by ac on 2016-07-26.
 */
public final class ColorUtil {
    private ColorUtil() {
    }

    public static int getColorWithAlpha(int alpha, int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int getColorWithOutAlpha(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb(red, green, blue);
    }

    public static int getColorInt(String colorString) {
        if (colorString.contains("#")) {
            return Color.parseColor(colorString);
        } else {
            return Color.parseColor("#" + colorString);
        }
    }

    public static String getColorString(int color) {
        String r = Integer.toHexString(Color.red(color));
        r = r.length() < 2 ? ('0' + r) : r;
        String g = Integer.toHexString(Color.green(color));
        g = g.length() < 2 ? ('0' + g) : g;
        String b = Integer.toHexString(Color.blue(color));
        b = b.length() < 2 ? ('0' + b) : b;
        return '#' + r + g + b;
    }

    public static float getAlphaDegree(int alpha) {
        float alphaDegree =
                // (255 -
                alpha
                        // )
                        / 255.0f;
        BigDecimal bigDecimal = new BigDecimal(alphaDegree);
        return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        // return (255 - alpha) / 255.0f;
    }

    public static int getAlpha(float alphaDegree) {
        return (int) (
                // (
                // 1 -
                alphaDegree
                        // )
                        * 255);
    }

    // public static int getAlphaFromAgainst(float againstAlphaDegree) {
    // return (int) (againstAlphaDegree * 255);
    // }
}
