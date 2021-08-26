package com.augurit.agmobile.gzps.jbjpsdy;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * @PROJECT MyApplication
 * @USER Augurit
 * @CREATE 2020/11/12 15:45
 */
public class CustomTextView extends AppCompatTextView {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBoldStyle(int style) {
        TextPaint paint = getPaint();
        if (style == 1) {
            paint.setFakeBoldText(true);
        } else {
            paint.setFakeBoldText(false);
        }
    }

    public int getBoldStyle() {
        return getPaint().isFakeBoldText() ? 1 : 0;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setBoldStyle(selected ? 1 : 0);
        if (selected) {
//            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ObjectAnimator.ofFloat(this, "textSize", 13, 16).setDuration(100).start();
        } else {
//            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            ObjectAnimator.ofFloat(this, "textSize", 16, 13).setDuration(100).start();
        }
    }
}
