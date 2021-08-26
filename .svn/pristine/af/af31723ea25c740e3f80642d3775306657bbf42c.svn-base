package com.augurit.agmobile.gzps.common.util;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by xcl on 2017/10/26.
 */
public class FontsOverrideUtil {

        private enum DefaultFont {
            DEFAULT("DEFAULT"),
            DEFAULT_BOLD("DEFAULT_BOLD"),
            MONOSPACE("MONOSPACE"),
            SERIF("SERIF"),
            SANS_SERIF("SANS_SERIF");

            private String value;

            DefaultFont(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }
        }

        public static final String REPLACE_FONT = "fonts/SourceHanSansCN-Medium.ttf";

        /**
         * @param context
         */
        public static final void init(Context context) {
            final Typeface replace = Typeface.createFromAsset(context.getAssets(), REPLACE_FONT);
            for (DefaultFont defaultFont : DefaultFont.values()) {
                replaceFont(defaultFont.getValue(), replace);
            }
        }

        public static final void replaceFont(String fontName, Typeface typeface) {
            try {
                final Field staticField = Typeface.class.getDeclaredField(fontName);
                staticField.setAccessible(true);
                staticField.set(null, typeface);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
 }
