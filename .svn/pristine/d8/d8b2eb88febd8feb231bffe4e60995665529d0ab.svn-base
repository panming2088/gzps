package com.augurit.am.fw.utils.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by ac on 2016-07-26.
 */
public final class ListViewUtil {
    private ListViewUtil() {
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem.getLayoutParams() == null) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 动态调整控件的宽度
     *
     * @param view
     */
    public static void setListViewBasedOnChildren(ViewGroup view) {
        //获取子view个数
        int count = view.getChildCount();
        int maxWidth = 0;
        int totalHeight = 100;
        for (int i = 0; i < count; i++) {
            View childView = view.getChildAt(i);
            childView.measure(0, 0);
            if (maxWidth < childView.getMeasuredWidth()) {
                maxWidth = childView.getMeasuredWidth();
            }
        }
        view.setLayoutParams(new ViewGroup.LayoutParams(maxWidth, totalHeight));
    }

    /**
     * To update Listview's height after BaseAdapter getting data.
     *
     * @param listview
     * @param adapter
     */
    public static void updateListView(ListView listview, BaseAdapter adapter) {
        ListViewUtil.setListViewHeightBasedOnChildren(listview);
        adapter.notifyDataSetChanged();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView,
                                                        int notLessThanHeight) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int maxWidth = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            int itemWidth = listItem.getMeasuredWidth();
            if (maxWidth < itemWidth) {
                maxWidth = itemWidth;
            }
        }
        int targetHeight = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        if (targetHeight < notLessThanHeight) {
            targetHeight = notLessThanHeight;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = targetHeight;
        params.width = maxWidth + 10;
        // System.out.println("setListViewHeightBasedOnChildren" +
        // targetHeight);
        listView.setLayoutParams(params);
    }

    public static int[] setListViewHeightBasedOnChildrenForWidthAndHeight(
            ListView listView, int notLessThanWidth, int notLessThanHeight) {
        int[] widthAndHeight = new int[2];
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            widthAndHeight[0] = notLessThanWidth;
            widthAndHeight[1] = notLessThanHeight;
            return widthAndHeight;
        }
        int totalHeight = 0;
        int targetWidth = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            int itemWidth = listItem.getMeasuredWidth();
            if (targetWidth < itemWidth) {
                targetWidth = itemWidth;
            }
        }
        int targetHeight = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        if (targetHeight < notLessThanHeight) {
            targetHeight = notLessThanHeight;
        }
        if (targetWidth < notLessThanWidth) {
            targetWidth = notLessThanWidth;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(0, 0);
            listView.setLayoutParams(params);
        }
        params.height = targetHeight;
        params.width = targetWidth + 10;
        listView.setLayoutParams(params);
        widthAndHeight[0] = targetWidth + 20;
        widthAndHeight[1] = targetHeight + 20;
        // System.out.println("setListViewHeightBasedOnChildren" +
        // targetHeight);
        return widthAndHeight;
    }
}
