package com.augurit.am.cmpt.widget.searchview.util;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.searchview.util.view
 * @createTime 创建时间 ：17/4/11
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/4/11
 * @modifyMemo 修改备注：
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

public class Util {

    private static final String TAG = "Util";

    public static void showSoftKeyboard(final Context context, final EditText editText) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }

    public static void closeSoftKeyboard(Activity activity) {

        View currentFocusView = activity.getCurrentFocus();
        if (currentFocusView != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
        }
    }

    public static int dpToPx(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }

    public static int pxToDp(int px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (px / metrics.density);
    }

    public static int spToPx(int sp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    public static int pxToSp(int px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / (int) metrics.scaledDensity;
    }

    public static int getScreenWidth(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.heightPixels;
    }

    public static void setIconColor(ImageView iconHolder, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(iconHolder.getDrawable());
        DrawableCompat.setTint(wrappedDrawable, color);
        iconHolder.setImageDrawable(wrappedDrawable);
        iconHolder.invalidate();
    }

    /**
     * Gets a reference to a given drawable and prepares it for use with tinting through.
     *
     * @param resId the resource id for the given drawable
     * @return a wrapped drawable ready fo use
     * with {@link android.support.v4.graphics.drawable.DrawableCompat}'s tinting methods
     * @throws Resources.NotFoundException
     */
    public static Drawable getWrappedDrawable(Context context, @DrawableRes int resId) throws Resources.NotFoundException {
        return DrawableCompat.wrap(ResourcesCompat.getDrawable(context.getResources(),
                resId, null));
    }

    public static int getColor(Context context, @ColorRes int resId) throws Resources.NotFoundException {
        return ContextCompat.getColor(context, resId);
    }

    public static void removeGlobalLayoutObserver(View view, ViewTreeObserver.OnGlobalLayoutListener layoutListener) {
        if (Build.VERSION.SDK_INT < 16) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(layoutListener);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
        }
    }
}
