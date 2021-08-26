package com.augurit.am.fw.utils.view;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.augurit.am.fw.utils.ListUtil;

import java.util.List;

/**
 * Utilities for getting arrayAdapter for spinner.
 * Created by ac on 2016-07-26.
 */
public final class SpinnerUtil {
    private SpinnerUtil() {
    }

    /**
     * To get Adapter from custom list.
     *
     * @param context
     * @param strings
     * @return
     */
    public static ArrayAdapter<String> getAdapter(Context context,
                                                  List<String> strings) {
        if (ListUtil.isEmpty(strings)) {
            return null;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    /**
     * To get Adapter from array.xml
     *
     * @param context
     * @param arrayId ArrayId in array.xml
     * @return
     */
    public static ArrayAdapter<CharSequence> getAdapter(Context context,
                                                        int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
