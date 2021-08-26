package com.augurit.am.fw.utils;

import android.content.Context;

/**
 * Created by ac on 2016-07-26.
 */
public final class ResourceUtil {
    private ResourceUtil() {
    }

    public static int getIdByName(Context context, String className, String name) {
        String packageName = context.getPackageName();
        Class r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            /**
             * classes== [class com.android.testjar.R$anim, class
             * com.android.testjar.R$attr, class com.android.testjar.R$bool,
             * class com.android.testjar.R$color, class
             * com.android.testjar.R$dimen, class
             * com.android.testjar.R$drawable, class com.android.testjar.R$id,
             * class com.android.testjar.R$integer, class
             * com.android.testjar.R$layout, class com.android.testjar.R$menu,
             * class com.android.testjar.R$string, class
             * com.android.testjar.R$style, class
             * com.android.testjar.R$styleable]
             *
             */
            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return id;
    }

    public static int[] getIdsByName(Context context, String className,
                                     String name) {
        String packageName = context.getPackageName();
        Class r = null;
        int[] ids = null;
        try {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if ((desireClass != null)
                    && (desireClass.getField(name).get(desireClass) != null)
                    && (desireClass.getField(name).get(desireClass).getClass()
                    .isArray()))
                ids = (int[]) desireClass.getField(name).get(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return ids;
    }

    public static int getLayoutId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "layout",
                paramContext.getPackageName());
    }

    public static int getStringId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "string",
                paramContext.getPackageName());
    }

    public static int getDrawableId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "drawable", paramContext.getPackageName());
    }

    public static int getMipMapId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "mipmap", paramContext.getPackageName());
    }

    public static int getStyleId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "style",
                paramContext.getPackageName());
    }

    public static int[] getStyleableIds(Context paramContext, String paramString) {
        return getIdsByName(paramContext, "styleable", paramString);
    }

    public static int getStyleableId(Context paramContext, String paramString) {
        return getIdByName(paramContext, "styleable", paramString);
    }

    public static int getAttrId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "attr",
                paramContext.getPackageName());
    }

    public static int getId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "id",
                paramContext.getPackageName());
    }

    public static int getColorId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "color",
                paramContext.getPackageName());
    }

    public static int getDimenId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "dimen",
                paramContext.getPackageName());
    }

    public static float getDimen(Context paramContext, String paramString) {
        return paramContext.getResources().getDimension(paramContext.getResources().getIdentifier(paramString, "dimen",
                paramContext.getPackageName()));
    }

    public static int getMenuId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "menu",
                paramContext.getPackageName());
    }

    public static int getAnimId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "anim",
                paramContext.getPackageName());
    }

    public static String getString(Context paramContext, String paramString) {
        return paramContext.getString(getStringId(paramContext, paramString));
    }
}
