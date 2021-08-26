package com.augurit.agmobile.patrolcore.search.view.filterview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.augurit.agmobile.mapengine.common.DividerItemDecoration;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.table.util.ControlType;
import com.augurit.am.cmpt.widget.drawerlayout.LeftDrawerLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 条件过滤视图
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.search.view.filterview
 * @createTime 创建时间 ：2017/7/11
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/7/11
 * @modifyMemo 修改备注：
 */

public class FilterView extends LinearLayout {

    private static final int MENU_SIZE = 4;

    private FilterItem mHeadItem;
    private List<FilterItem> mItems;
    private RecyclerView rv_menu;
    private FilterMenuAdapter mMenuAdapter;
    private HashMap<FilterItem, PopupWindow> mPopMap;
    private HashMap<FilterItem, String> mFilterMap;     // 过滤值
    private HashMap<FilterItem, View> mDrawerViewMap;
    private LeftDrawerLayout mDrawerLayout;
    private ViewGroup mDrawerContainer;
    private View mDrawerView;
    private OnFilterChangedListener mListener;

    public FilterView(Context context) {
        super(context);
        initView();
    }

    public FilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        // 菜单view
        View view = View.inflate(getContext(), R.layout.filter_menu, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        rv_menu = (RecyclerView) view.findViewById(R.id.rv_menu);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), MENU_SIZE);
        rv_menu.setLayoutManager(layoutManager);
        mMenuAdapter = new FilterMenuAdapter(getContext(), new ArrayList<FilterItem>());
        mMenuAdapter.setOnItemClickListener(mOnMenuItemClickListener);
        rv_menu.setAdapter(mMenuAdapter);
        addView(view);
        mPopMap = new HashMap<>();
        mFilterMap = new HashMap<>();
    }

    /**
     * 设置头选项（项目选项）
     */
    public void setHeadItem(FilterItem headItem) {
        mHeadItem = headItem;
        if (mHeadItem.getSpinnerDatas().size() > 1) {   // 超过一个项目则显示
            mMenuAdapter.setHeadItem(mHeadItem);
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), MENU_SIZE + 1);
            rv_menu.setLayoutManager(layoutManager);
        }
    }

    /**
     * 设置条件项，可能被多次调用
     */
    public void setItems(List<FilterItem> filterItems) {
        mItems = filterItems;
        initMenu();
//        initDrawer();
    }

    public List<FilterItem> getmItems() {
        return mItems;
    }

    /**
     * 菜单项
     */
    private void initMenu() {
        List<FilterItem> menuItems;
        int menuSize = MENU_SIZE;
        if (mItems.size() >= MENU_SIZE) {
            menuItems = mItems.subList(0, MENU_SIZE);
            if (mHeadItem != null && mHeadItem.getSpinnerDatas().size() > 1) {
                menuSize = MENU_SIZE + 1;
            }
        } else {
            menuItems = mItems;
            if (mItems.size() < MENU_SIZE) {
                if (mHeadItem != null && mHeadItem.getSpinnerDatas().size() > 1) {
                    menuSize = mItems.size() + 1;
                } else {
                    menuSize = mItems.size();
                }
            }
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), menuSize);
        rv_menu.setLayoutManager(layoutManager);
        mMenuAdapter.setItems(menuItems);
    }

    /**
     * 抽屉
     */
    private void initDrawer() {
        mDrawerView = View.inflate(getContext(), R.layout.filter_detail_popup, null);
        View btn_positive = mDrawerView.findViewById(R.id.btn_positive);
        ViewGroup container = (ViewGroup) mDrawerView.findViewById(R.id.condition_container);
        List<FilterItem> items = mItems.subList(MENU_SIZE, mItems.size());
        for (FilterItem item : items) {
            switch (item.getType()) {   // TODO SPINNER、时间区间、文本区间
                case ControlType.SPINNER:
                    View spinnerView = View.inflate(getContext(), R.layout.table_combo_item, null);
                    container.addView(spinnerView);
                    break;
            }
        }
        btn_positive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout != null) {
                    mDrawerLayout.closeDrawer();
                }
            }
        });
        if (mDrawerLayout != null) {
            mDrawerContainer.addView(mDrawerView);
        }
    }

    private FilterMenuAdapter.OnItemClickListener mOnMenuItemClickListener = new FilterMenuAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, FilterItem item, boolean isHeadItem) {
            switch (item.getType()) {
                case ControlType.SPINNER:
                    // 显示Spinner下拉
                    showSpinnerItems(item, false);
                    break;
                case ControlType.CUSSTOM:   // 暂时以CUSTOM判断
                    // 筛选
                    if (mItems.size() > MENU_SIZE) {
//                        showDrawer();     // TODO 先不show了
                    }
                default:
                    break;
            }
        }
    };

    private void showSpinnerItems(final FilterItem item, final boolean multiSelect) {
        PopupWindow popupWindow = mPopMap.get(item);
        if (popupWindow == null) {
            View view = View.inflate(getContext(), R.layout.filter_spinner_popup, null);
            ViewGroup container = (ViewGroup) view.findViewById(R.id.view_container);
            RecyclerView recyclerView = new RecyclerView(getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
            FilterSpinnerAdapter adapter = new FilterSpinnerAdapter(getContext(), item.getSpinnerDatas());
            recyclerView.setLayoutParams(params);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(decoration);
            recyclerView.setAdapter(adapter);
            recyclerView.setTag("recyclerView");
            adapter.setOnItemClickListener(new FilterSpinnerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, boolean isChecked, String key, Object value) {
                    mPopMap.get(item).dismiss();
                    String valueStr = value.toString();
                    if (mHeadItem != null && item.getField1().equals("projectId")) {   // 存在headItem为项目id
                        if (mListener != null) {
                            mListener.onHeadItemChanged(valueStr);  // 项目切换
                        }
                    }else {
                        if (isChecked) {
                            mFilterMap.put(item, valueStr);
                        } else {
                            mFilterMap.remove(item);
                        }
                        if (mListener != null) {
                            mListener.onFilterChanged(item.getField1(),key,mFilterMap);
                        }
                    }
                }
            });
            container.addView(recyclerView);

            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.amspinner_bg_window, null);
            popupWindow.setBackgroundDrawable(drawable);
            mPopMap.put(item, popupWindow);
        }
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mMenuAdapter.onFilterDismiss();
            }
        });
        popupWindow.showAsDropDown(this);
    }

    private void showDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.openDrawer();
        }
    }

    public interface OnFilterChangedListener {
        void onHeadItemChanged(String headValue);
        void onFilterChanged(String selectedHeadField,String key,Map<FilterItem, String> filterMap);
    }

    public void setOnFilterChangedListener(OnFilterChangedListener listener) {
        mListener = listener;
    }

    public void setDrawerLayout(LeftDrawerLayout drawerLayout) {
        // TODO
    }

    /**
     * 清除当前选中的筛选条件
     */
    public void clear() {
        for (Map.Entry<FilterItem, PopupWindow> entry : mPopMap.entrySet()) {
            PopupWindow popupWindow = entry.getValue();
            RecyclerView recyclerView = (RecyclerView) popupWindow.getContentView().findViewWithTag("recyclerView");
            FilterSpinnerAdapter adapter = (FilterSpinnerAdapter) recyclerView.getAdapter();
            adapter.clearSelection();
        }
    }
}
