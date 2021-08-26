package com.augurit.am.fw.utils.view;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.augurit.am.fw.utils.SdkUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * //TODO view测量工具类，并不完善，请自行将写好并测试过的类添加到其中
 */
public class ViewUtils {

    /**
     * scrollview嵌套listview显示不全解决
     *
     * @param listView
     */
    public static int setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        Map<Integer,Integer> height = new HashMap<>();
        Map<Integer,Integer> width = new HashMap<>();

        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        int  temp = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            int viewType = listAdapter.getItemViewType(i);
            if (height.containsKey(viewType)){
                 temp = height.get(viewType);
            }else {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                temp = listItem.getMeasuredHeight();
                //保存
                height.put(viewType,temp);
            }
            totalHeight += temp;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        return totalHeight;
    }

    public void measureListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
      // int listViewWidth = screedWidth - dip2px(this, 10);//listView在布局时的宽度
       int listViewWidth = 0;
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
           listItem.measure(widthSpec, 0);

            int itemHeight = listItem.getMeasuredHeight();
            totalHeight += itemHeight;
        }
        // 减掉底部分割线的高度
        int historyHeight = totalHeight
                + (listView.getDividerHeight() * listAdapter.getCount() - 1);
    }

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                resources.getDisplayMetrics());
        return (int) px;
    }

    public static int getRelativeTop(View myView) {
        // if (myView.getParent() == myView.getRootView())
        if (myView.getId() == android.R.id.content)
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    public static int getRelativeLeft(View myView) {
        // if (myView.getParent() == myView.getRootView())
        if (myView.getId() == android.R.id.content)
            return myView.getLeft();
        else
            return myView.getLeft()
                    + getRelativeLeft((View) myView.getParent());
    }

    /**
     * Determine the version number since functions that used by the low version
     * is different from the higher ones.
     */
    public static boolean isJellyBeanOrHigher() {
        return SdkUtil
                .isHighSdkVerson(android.os.Build.VERSION_CODES.JELLY_BEAN);
    }
}
