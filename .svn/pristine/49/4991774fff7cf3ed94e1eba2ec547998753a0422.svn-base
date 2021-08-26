package com.augurit.agmobile.gzps.uploadfacility.view.feedback;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.MyGridView;
import com.augurit.agmobile.gzps.uploadfacility.model.BatchInfo;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.fw.utils.DeviceUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.feedback
 * @createTime 创建时间 ：2018-06-01
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-06-01
 * @modifyMemo 修改备注：
 */

public class FilterGroupDialog {
    private Context mContext;
    private FilterGroupAdapter adapter;
    private View view;
    private MyGridView gv;
    private PopupWindow pw;
    private FilterGroupListener mFilterGroupListener;
    private Button yesButton, nextPageButton, prePageButton, searchButton;
    Set<BatchInfo.Info> lastSelectGroupList = new HashSet<>();
    ;
    //上次选中的批次记录
    private Set<BatchInfo.Info> selectedGroupList = new HashSet<>();
    private EditText searchEt;
    private TextView mEmptyTv;

    public FilterGroupDialog(Context context) {
        this.mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.select_group_dialog, null);
        gv = (MyGridView) view.findViewById(R.id.gv_group);
        adapter = new FilterGroupAdapter(mContext);
        pw = new PopupWindow(view, (int) (DeviceUtil.getScreenWidth(mContext) / 1.2), ViewGroup.LayoutParams.WRAP_CONTENT);
        // 使其聚集
        pw.setFocusable(true);
        // 设置允许在外点击消失
        pw.setOutsideTouchable(true);
        yesButton = (Button) view.findViewById(R.id.filter_group);
        nextPageButton = (Button) view.findViewById(R.id.next_page);
        prePageButton = (Button) view.findViewById(R.id.pre_page);
        searchButton = (Button) view.findViewById(R.id.search_btn);
        searchEt = (EditText) view.findViewById(R.id.search_et);
        mEmptyTv = (TextView) view.findViewById(R.id.empty_text);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object selectedData) {
                BatchInfo.Info item = (BatchInfo.Info) selectedData;

//                if(lastSelectGroupList.contains(item)){
//                    lastSelectGroupList.remove(item);
//                }else{
//                    lastSelectGroupList.add(item);
//                }

                BatchInfo.Info info = contains(selectedGroupList, item);
                if (info != null) {
                    selectedGroupList.remove(info);
                } else {
                    selectedGroupList.add(item);
                }
                setSelectedBatch();
            }

            @Override
            public void onItemLongClick(View view, int position, Object selectedData) {
            }
        });
        gv.setAdapter(adapter);
    }

    private BatchInfo.Info contains(Set<BatchInfo.Info> selectedGroupList, BatchInfo.Info item) {
        for (BatchInfo.Info info : selectedGroupList) {
            if (info.equals(item)) {
                return info;
            }
        }
        return null;
    }

    public void setSelectedBatch() {
        Set<BatchInfo.Info> selectedList = selectedGroupList;
        adapter.setSelectedGroupList(selectedList);

    }

    public void showDialog(View ll_select_group, BatchInfo info, int currentPage) {

        if (info == null || ListUtil.isEmpty(info.getRows())) {
            mEmptyTv.setVisibility(View.VISIBLE);
            yesButton.setVisibility(View.GONE);
        } else {
            mEmptyTv.setVisibility(View.GONE);
            yesButton.setVisibility(View.VISIBLE);
        }


        if (!pw.isShowing()) {
            pw.showAsDropDown(ll_select_group, (int) ll_select_group.getX(), 0);
        }
//        gv.setAdapter(adapter);
        if(info == null){
            return;
        }
        adapter.notifyDataSetChanged(info.getRows());


        setSelectedBatch();

        prePageButton.setVisibility(currentPage > 1 ? View.VISIBLE : View.GONE);
        nextPageButton.setVisibility(currentPage * UploadFeedbackMapFragment.COUNT_PER_PAGE - info.getTotal() < 0 ? View.VISIBLE : View.GONE);
        setListener(info.getRows());
    }

    private void setListener(final List<BatchInfo.Info> groupList) {
        RxView.clicks(yesButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        pw.dismiss();
                        Set<BatchInfo.Info> selectedList = adapter.getSelectedEventTypeList();
                        if (lastSelectGroupList != null && selectedList != null && lastSelectGroupList.containsAll(selectedList) && selectedList.containsAll(lastSelectGroupList)) {
                            return;
                        }
                        mFilterGroupListener.filterByIds(selectedList);
                        if (lastSelectGroupList != null) {
                            lastSelectGroupList.clear();
                            lastSelectGroupList.addAll(selectedList);
                        }
                    }
                });


        RxView.clicks(prePageButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mFilterGroupListener.getPrePage();
                    }
                });


        RxView.clicks(nextPageButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        if(!ListUtil.isEmpty(groupList) && groupList.size()<10){
//                          ToastUtil.shortToast(mContext,"没有更多待办批次了");
//                            return ;
//                        }
                        mFilterGroupListener.getNextPage();
                    }
                });

        RxView.clicks(searchButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (TextUtils.isEmpty(searchEt.getText().toString())) {
                            ToastUtil.shortToast(mContext, "请输入搜索内容！");
                            return;
                        }
                        mFilterGroupListener.searchFor(searchEt.getText().toString());

                    }
                });
    }

    public void setFilterGroupListener(FilterGroupListener filterGroupListener) {
        mFilterGroupListener = filterGroupListener;
    }

    public void setSelectFirstInStete(BatchInfo info) {
        if(info ==null || ListUtil.isEmpty(info.getRows())) return ;
        selectedGroupList.add(info.getRows().get(0));
        setSelectedBatch();
        lastSelectGroupList.add(info.getRows().get(0));
        mFilterGroupListener.filterByIds(selectedGroupList);
    }
}
