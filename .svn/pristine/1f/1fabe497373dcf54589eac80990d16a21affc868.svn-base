package com.augurit.agmobile.patrolcore.search.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.search.model.SearchResult;
import com.augurit.agmobile.patrolcore.search.util.PatrolSearchServiceProvider;
import com.augurit.agmobile.patrolcore.search.view.filterview.FilterItem;
import com.augurit.agmobile.patrolcore.search.view.filterview.FilterView;
import com.augurit.agmobile.patrolcore.upload.view.EditTableActivity;
import com.augurit.agmobile.patrolcore.upload.view.ReadTableActivity;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.dropdownmenu.DropDownMenu;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressRelativeLayout;
import com.augurit.am.fw.common.IPresenter;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.MapView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search
 * @createTime 创建时间 ：2017-03-24
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-24
 * @modifyMemo 修改备注：
 */

public class SearchFragmentWithoutMap extends Fragment implements IPatrolSearchView {

    /**
     * 问题上报的项目id
     */
    public static String PROJECT_ID  =  "a215d238-e18b-4f00-baae-e93a201502f8" ;

    private Activity mActivity;
    private XRecyclerView result_list_view;

    //结果列表
    private SearchResultAdapter mSearchResultAdapter;
    //条件筛选列表
    @Deprecated
    private DropDownMenu mDropDownMenu;
    @Deprecated
    private List<View> popupViews = new ArrayList<>();
    private View filterview_container;          // 筛选容器
    private FilterView mFilterView;             // 筛选控件
    private List<TableItem> mKeywordItems;      // 关键字字段
    private Spinner sp_fields;      // 关键字字段选择

    protected IPatrolSearchPresenter mPresenter;
    private ProgressRelativeLayout mProgressRelativeLayout;
    private ProgressDialog mProgressDialog;
    private View root;
    private View mBtn_retry;
    private View mHeaderView; // 结果列表Header
    private TextView tv_header;

    private Callback1<Integer> mOnClickTaskBtnListener;

    public static final String IF_SHOW_BACK_BUTTON_KEY = "ifshow";
    public static final String IF_SHOW_ADD_BUTTON_KEY = "ifshowAddButton";
    private View iv_search;
    //   private View mDropDownMenuRoot;

    public static SearchFragmentWithoutMap newInstance(String text, boolean ifShowBackButton) {

        SearchFragmentWithoutMap searchFragmentWithoutMap = new SearchFragmentWithoutMap();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IF_SHOW_BACK_BUTTON_KEY, ifShowBackButton);
        bundle.putBoolean(IF_SHOW_ADD_BUTTON_KEY, false);
        searchFragmentWithoutMap.setArguments(bundle);
        return searchFragmentWithoutMap;
    }

    public static SearchFragmentWithoutMap newInstance(String text, boolean ifShowBackButton,boolean ifShowAddButton) {

        SearchFragmentWithoutMap searchFragmentWithoutMap = new SearchFragmentWithoutMap();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IF_SHOW_BACK_BUTTON_KEY, ifShowBackButton);
        bundle.putBoolean(IF_SHOW_ADD_BUTTON_KEY, ifShowAddButton);
        searchFragmentWithoutMap.setArguments(bundle);
        return searchFragmentWithoutMap;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean ifShow = getArguments().getBoolean(IF_SHOW_BACK_BUTTON_KEY);
        boolean ifShowAddButton = getArguments().getBoolean(IF_SHOW_ADD_BUTTON_KEY);
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_searchwithoumap, container, false);
            View btn_back = root.findViewById(R.id.btn_back);               // xjx 暂时保留原标题栏与搜索栏，目前只在xml中控制搜索栏显示
            View btn_back_search = root.findViewById(R.id.btn_back_search);
            root.findViewById(R.id.lv_search).setVisibility(View.GONE);
            View btn_add = root.findViewById(R.id.btn_add); //添加新记录
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableViewManager.isEditingFeatureLayer = false;
                    Intent intent = new Intent(getActivity(), EditTableActivity.class); //Todo 10.15 只能问题上报
                    intent.putExtra("projectId",PROJECT_ID);
                    intent.putExtra("projectName","问题上报");
                    startActivity(intent);
                }
            });
            View.OnClickListener backListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            };
            btn_back.setOnClickListener(backListener);
            btn_back_search.setOnClickListener(backListener);
            if (!ifShow) {
                btn_back.setVisibility(View.GONE);
                btn_back_search.setVisibility(View.GONE);
            }

            if (!ifShowAddButton){
                btn_add.setVisibility(View.GONE);
            }

            View tv_title = root.findViewById(R.id.tv_title);
            if (tv_title != null) {  // 2017-7-10 加入搜索栏处理
                tv_title.setOnClickListener(new View.OnClickListener() { //点击标题可以回到第一条
                    @Override
                    public void onClick(View view) {
                        if (result_list_view != null && mSearchResultAdapter != null &&
                                !ValidateUtil.isListNull(mSearchResultAdapter.getDataList())) {
                            result_list_view.scrollToPosition(0);
                        }
                    }
                });
            }
        }
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initDatas();
        initListener();
    }

    private void initListener() {
        mBtn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPresenter != null) {
                    mPresenter.loadData();
                }
            }
        });
    }

    protected void initDatas() {
        mPresenter = new PatrolSearchPresenter(this, null, PatrolSearchServiceProvider.provideSearchService(getApplicationContext()));
        mPresenter.loadData();
        mPresenter.initFilterView();  // 初始化筛选控件
        // LayerPresenter layerPresenter = new LayerPresenter(new LayerView(mActivity,mMapView,null),new LayerService2(mActivity));
        // layerPresenter.loadLayer();
    }

    private void initView() {

        mProgressRelativeLayout = (ProgressRelativeLayout) getView().findViewById(R.id.fr_search_progressrl);
        result_list_view = (XRecyclerView) getView().findViewById(R.id.search_result_list_view);
        filterview_container = getView().findViewById(R.id.filterview_container);
        mFilterView = (FilterView) getView().findViewById(R.id.filter_view);
        mBtn_retry = getView().findViewById(R.id.btn_retry);
        //   mDropDownMenuRoot = getView().findViewById(R.id.dropdownmenuroot);
        //  mHolder = getView().findViewById(R.id.view_holder);

        //  mIv_filter = (ImageView) getView().findViewById(R.id.iv_filter);
        // mMapView = (MapView) getView().findViewById(R.id.mapview);
        //  mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENR.id.dropdownmenurootT, 0f, 0f);//设置地图背景色、去掉网格线


        getView().findViewById(R.id.btn_unaccept_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickTaskBtnListener != null){
                    mOnClickTaskBtnListener.onCallback(0);
                }
            }
        });
        getView().findViewById(R.id.btn_accepted_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickTaskBtnListener != null){
                    mOnClickTaskBtnListener.onCallback(1);
                }
            }
        });


        // 搜索框
        View btn_search = getView().findViewById(R.id.btn_search);
        View iv_close = getView().findViewById(R.id.iv_close);
        final EditText et_search = (EditText) getView().findViewById(R.id.et_search);
        sp_fields = (Spinner) getView().findViewById(R.id.sp_fileds);   // 关键字字段选择
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                                && KeyEvent.ACTION_DOWN == event.getAction())) {
                    String keyWord = et_search.getText().toString();
                    mFilterView.clear();        // 清空当前筛选条件
                    mPresenter.search(keyWord);
                    return true;
                }
                return false;
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyWord = et_search.getText().toString();
                mFilterView.clear();        // 清空当前筛选条件
                mPresenter.search(keyWord);
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });
        sp_fields.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 切换关键字查询字段
                if (mKeywordItems != null && mKeywordItems.size() > position) {
                    String field = mKeywordItems.get(position).getField1();
                    mPresenter.setKeywordField(field);
                    // 同时进行一次搜索
                    String keyWord = et_search.getText().toString();
                    if (!keyWord.isEmpty()) {
                        mPresenter.search(keyWord);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        iv_search = getView().findViewById(R.id.iv_search);
    }


    @Override
    public MapView getMapView() {
        return null;
    }

    @Override
    public void showLocationCallout(Location location) {
    }

    @Override
    public void showUploadHistory(final List<SearchResult> searchHistories) {

        mBtn_retry.setVisibility(View.GONE);

        result_list_view.setPullRefreshEnabled(true);
        result_list_view.setLoadingMoreEnabled(true);
        //    result_list_view.setNoMore(true);

        result_list_view.setLayoutManager(new LinearLayoutManager(mActivity));
        mSearchResultAdapter = new SearchResultAdapter(mActivity, searchHistories);
        result_list_view.setAdapter(mSearchResultAdapter);
        mSearchResultAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                mPresenter.jumpToDetailPage(position, searchHistories.get(position));
            }
        });

        result_list_view.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshData();
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });
        // result_list_view.setNoMore(true);
        if (!searchHistories.isEmpty()) {   // 显示搜索结果数
            if (mHeaderView == null) {
                mHeaderView = View.inflate(getContext(), R.layout.search_result_list_header, null);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tv_header = (TextView) mHeaderView.findViewById(R.id.tv_header_text);
                mHeaderView.setLayoutParams(layoutParams);
                result_list_view.addHeaderView(mHeaderView);
            }
            tv_header.setText("共找到" + searchHistories.get(0).getTotal() + "条结果");
        }
    }

    @Override
    public Context getApplicationContext() {
        return mActivity.getApplicationContext();
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        mPresenter = (IPatrolSearchPresenter) presenter;
    }

    @Override
    public void showLoading() {
        mProgressRelativeLayout.showLoading();
    }

    @Override
    public void hideLoading() {
        mProgressRelativeLayout.showContent();
        mBtn_retry.setVisibility(View.GONE);
    }

    @Override
    public void showLoadedError(String errorReason) {
        mProgressRelativeLayout.showError(null, "获取数据失败", errorReason, "重试", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadData();
            }
        });

    }

    @Override
    public void showLoadedEmpty() {

        List<Integer> ids = new ArrayList<>();
        ids.add(R.id.btn_retry);

        mProgressRelativeLayout.showEmpty(ContextCompat.getDrawable(getActivity(), R.mipmap.common_ic_load_empty),
                "查无上报记录", "查无上报记录", ids);
        mBtn_retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void initFilterView(FilterItem headItem, List<FilterItem> filterItems, List<TableItem> keywordItems) {
        if (headItem != null) { // 第一次调用设置headItem（项目选项）
            mFilterView.setHeadItem(headItem);
            // 筛选条件变化时刷新
            mFilterView.setOnFilterChangedListener(new FilterView.OnFilterChangedListener() {

                @Override
                public void onHeadItemChanged(String headValue) {
                    // 切换至该项目的筛选条件
                    mPresenter.getFilterConditions(headValue);
                }

                @Override
                public void onFilterChanged(String field1,String key,Map<FilterItem, String> filterMap) {
                    // 变更筛选条件
                    HashMap<String, String> conditionMap = new HashMap<String, String>();
                    for (Map.Entry<FilterItem, String> entry : filterMap.entrySet()) {
                        FilterItem item = entry.getKey();
                        conditionMap.put(item.getField1(), entry.getValue());
                    }
                    mPresenter.setFilterParams(conditionMap);
                }
            });
        }
        if (filterItems != null) {    // 之后调用改变筛选条件
            mFilterView.setItems(filterItems);
            filterview_container.setVisibility(View.GONE); //todo 10.15 暂时隐藏 原先是：filterview_container.setVisibility(View.VISIBLE);
        }
        if (keywordItems != null) {   // 改变关键字查询条件
            mKeywordItems = keywordItems;
            List<String> fields = new ArrayList<>();
            for (TableItem keywordItem : mKeywordItems) {
                fields.add(keywordItem.getHtml_name());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, fields);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_fields.setAdapter(arrayAdapter);

//            sp_fields.setVisibility(View.VISIBLE); //xcl 2017-08-18 显示spinner的时候要隐藏查询图标
//            iv_search.setVisibility(View.GONE);     //           TODO 暂时隐藏

            sp_fields.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoadingCompleteInfo() {
        showProgressDialog();
    }

    @Override
    public void hideLoadingCompleteInfo() {
        hideProgressDialog();
    }

    @Override
    public void showLoadingCompleteInfoFailed() {
        ToastUtil.shortToast(getApplicationContext(), "加载图片信息失败");
    }

    @Override
    public void jumpToDetailedUploadInfoPage(List<TableItem> tableItems, List<Photo> photos) {
        Intent intent = new Intent(mActivity, ReadTableActivity.class);
        intent.putExtra(IPatrolSearchView.TABLE_ITEMS, (ArrayList) tableItems);
        intent.putExtra(IPatrolSearchView.PHOTOS, (ArrayList) photos);
        mActivity.startActivity(intent);
    }

    @Override
    public void loadMoreFinished(List<SearchResult> newAddedData, boolean ifHasMore) {
        result_list_view.loadMoreComplete();
        result_list_view.setNoMore(ifHasMore);
        mSearchResultAdapter.addData(newAddedData);
        mSearchResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshFinished(List<SearchResult> newData) {
        result_list_view.refreshComplete();
        //result_list_view.setNoMore(true);
        if (ValidateUtil.isListNull(newData)) {
            ToastUtil.shortToast(getActivity(), "已经是最新数据");
        } else {
            mSearchResultAdapter.notifyDataChanged(newData);
            tv_header.setText("共找到" + newData.get(0).getTotal() + "条结果");
            ToastUtil.shortToast(getActivity(), "刷新数据成功");
        }
        mBtn_retry.setVisibility(View.GONE);
    }

    @Override
    public void loadMoreFailed(String message) {
        result_list_view.loadMoreComplete();
        ToastUtil.shortToast(getActivity(), "加载数据失败，原因是：" + message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage("正在查询详细信息，请稍后");
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    public void setOnClickTaskBtnListener(Callback1<Integer> callback){
        mOnClickTaskBtnListener = callback;
    }


    @Override
    public void onResume() {
        super.onResume();

        //initDatas();
    }
}
