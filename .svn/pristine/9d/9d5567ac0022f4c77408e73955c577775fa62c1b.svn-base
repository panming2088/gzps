package com.augurit.am.fw.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ac on 2016-07-26.
 */
public final class ListUtil {
    private ListUtil() {
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static <T> boolean isEmpty(T... ts) {
        return ts == null || ts.length == 0;
    }

    public static void clear(List list) {
        if (isEmpty(list)) {
            return;
        }
        list.clear();
    }

    public static <T> ArrayList<T> getArrayList() {
        return new ArrayList<T>();
    }

    // public static <T> List<T> getArrayList(T t) {
    // List<T> ts = new ArrayList<T>();
    // if (t != null) {
    // ts.add(t);
    // }
    // return ts;
    // }

    /**
     * Creates an empty {@code ArrayList} instance when it's null.
     * <p>
     * <b>Note:</b> if you only need an <i>immutable</i> empty List, use
     * {@link java.util.Collections#emptyList} instead.
     *
     * @param ts
     * @return
     */
    public static <T> List<T> makeSureInit(List<T> ts) {
        if (ts == null) {
            ts = new ArrayList<T>();
        }
        return ts;
    }

    public static <T> List<T> getList(T... ts) {
        List<T> resultTs = new ArrayList<T>();
        if (!isEmpty(ts)) {
            for (T t : ts) {
                resultTs.add(t);
            }
        }
        return resultTs;
    }

    // public static boolean containString(List<String> strings, String string)
    // {
    // if (ListUtil.isEmpty(strings) || string == null) {
    // return false;
    // }
    // for (String tempString : strings) {
    // if (string.equals(tempString)) {
    // return true;
    // }
    // }
    // return false;
    // }

    public static <T> boolean isContainString(List<T> stringObjects,
                                              Object stringObject) {
        if (ListUtil.isEmpty(stringObjects) || stringObject == null) {
            return false;
        }
        for (T tempStringObject : stringObjects) {
            if (stringObject.toString().equals(tempStringObject.toString())) {
                return true;
            }
        }
        return false;
    }

    public static <T> void addAll(List<T> resultTs, List<T> toAddTs) {
        if (resultTs != null && !ListUtil.isEmpty(toAddTs)) {
            resultTs.addAll(toAddTs);
        }
    }

    public static <T> T[] listToArray(List<T> list) {
        try {
            if (isEmpty(list)) {
                return null;
            }
            T[] array = (T[]) list.toArray(new Object[list.size()]);
            return array;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> arrayToList(T[] array) {
        try {
            if (isEmpty(array)) {
                return null;
            }
            List<T> list = Arrays.asList(array);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
