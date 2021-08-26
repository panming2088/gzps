package com.augurit.am.fw.utils.common;

import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 检验是否有效的工具类
 */
public final class ValidateUtil {
    private ValidateUtil() {
    }

    /**
     * 检查object是否为空
     *
     * @param object
     * @return
     */
    public static boolean isObjectNull(Object object) {
        return object == null;
    }

    /**
     * 检验list是否为空
     *
     * @param list
     * @return 是否为空
     */
    public static boolean isListNull(List list) {
        if (list == null) {
            return true;
        } else {
            if (list.size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 确认对象不为空，注意：如果此时对象为空将抛出异常
     *
     * @param reference 要确认的对象
     * @param <T>       要确认的对象
     * @return 要确认的对象
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * 确认对象不为空，注意：如果此时对象为空将抛出异常
     *
     * @param reference    要确认的对象
     * @param errorMessage 抛出的异常提示信息
     * @param <T>          要确认的对象
     * @return 要确认的对象
     */
    public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * 判断map是否为空
     *
     * @param map
     * @return map是否为空
     */
    public static boolean isMapNull(Map map) {
        if (map == null) {
            return true;
        } else {
            if (map.isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
