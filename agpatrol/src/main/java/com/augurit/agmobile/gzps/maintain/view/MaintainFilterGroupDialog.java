package com.augurit.agmobile.gzps.maintain.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.maintain.model.MaintainBatchInfo;
import com.augurit.agmobile.gzps.maintain.view.adapter.MaintainFilterGroupAdapter;
import com.augurit.agmobile.gzps.uploadfacility.view.feedback.UploadFeedbackMapFragment;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.fw.utils.DeviceUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 养护计划
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.feedback
 * @createTime 创建时间 ：2018-06-01
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-06-01
 * @modifyMemo 修改备注：
 */

public class MaintainFilterGroupDialog {
    private Context mContext;
    private MaintainFilterGroupAdapter adapter;
    private View view;
    private RecyclerView gv;
    private PopupWindow pw;
    private MaintainFilterGroupListener mFilterGroupListener;
    private Button yesButton, nextPageButton, prePageButton, searchButton;
    Set<MaintainBatchInfo.RowsBean> lastSelectGroupList = new HashSet<>();
    ;
    //上次选中的批次记录
    private Set<MaintainBatchInfo.RowsBean> selectedGroupList = new HashSet<>();
    private EditText searchEt;
    private TextView mEmptyTv;
    private View root;
    private TextItemTableItem tableitem_name;
    private TextItemTableItem textitem_content;
    private TextItemTableItem textitem_start_date;
    private TextItemTableItem textitem_end_date;
    private TextItemTableItem textitem_type;
    private TextItemTableItem textitem_remark;
    private TextItemTableItem textitem_user;
    private TextItemTableItem textitem_phone;

    public MaintainFilterGroupDialog(Context context) {
        this.mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.maintain_select_group_dialog, null);
        gv = (RecyclerView) view.findViewById(R.id.gv_group);
        gv.setLayoutManager(new LinearLayoutManager(mContext));
        ((TextView)view.findViewById(R.id.tv_event_type)).setText("选择养护计划");
        adapter = new MaintainFilterGroupAdapter(mContext,new ArrayList<MaintainBatchInfo.RowsBean>());
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
                MaintainBatchInfo.RowsBean item = (MaintainBatchInfo.RowsBean) selectedData;

//                if(lastSelectGroupList.contains(item)){
//                    lastSelectGroupList.remove(item);
//                }else{
//                    lastSelectGroupList.add(item);
//                }

                MaintainBatchInfo.RowsBean info = contains(selectedGroupList, item);
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
        adapter.setOnItemShowListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object selectedData) {
                MaintainBatchInfo.RowsBean item = (MaintainBatchInfo.RowsBean) selectedData;
                showDialog(item);
            }

            @Override
            public void onItemLongClick(View view, int position, Object selectedData) {

            }
        });
        gv.setAdapter(adapter);
    }

    private void showDialog(MaintainBatchInfo.RowsBean item) {
        root = LayoutInflater.from(mContext).inflate(R.layout.view_maintain_plan_view, null);
        tableitem_name = (TextItemTableItem) root.findViewById(R.id.tableitem_name); //计划名称
        tableitem_name.setTextViewNameSize(15);
        tableitem_name.setText(item.getName());
        tableitem_name.setReadOnly();
        textitem_content = (TextItemTableItem) root.findViewById(R.id.textitem_content); //内容
        textitem_content.setTextViewNameSize(15);
        textitem_content.setText(item.getContent());
        textitem_content.setReadOnly();
        textitem_start_date = (TextItemTableItem) root.findViewById(R.id.textitem_start_date); //开始时间
        textitem_start_date.setTextViewNameSize(15);
        textitem_start_date.setText(TimeUtil.getStringTimeYMD(new Date(item.getStartDate())));
        textitem_start_date.setReadOnly();
        textitem_end_date = (TextItemTableItem) root.findViewById(R.id.textitem_end_date); //结束时间
        textitem_end_date.setTextViewNameSize(15);
        textitem_end_date.setText(TimeUtil.getStringTimeYMD(new Date(item.getEndDate())));
        textitem_end_date.setReadOnly();
        textitem_type = (TextItemTableItem) root.findViewById(R.id.textitem_type); //类型
        textitem_type.setTextViewNameSize(15);
        textitem_type.setText(item.getPipeType());
        textitem_type.setReadOnly();
        textitem_remark = (TextItemTableItem) root.findViewById(R.id.textitem_remark); //备注
        textitem_remark.setTextViewNameSize(15);
        textitem_remark.setText(item.getRemark());
        textitem_remark.setReadOnly();
        textitem_user = (TextItemTableItem) root.findViewById(R.id.textitem_user); //上报人
        textitem_user.setTextViewNameSize(15);
        textitem_user.setText(item.getMarkPerson());
        textitem_user.setReadOnly();
        textitem_phone = (TextItemTableItem) root.findViewById(R.id.textitem_phone); //联系电话
        textitem_phone.setTextViewNameSize(15);
        textitem_phone.setText(item.getMarkPersonCode());
        textitem_phone.setReadOnly();
        DialogUtil.MessageBox(mContext, "计划详情", root, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        },null);
    }

    private MaintainBatchInfo.RowsBean contains(Set<MaintainBatchInfo.RowsBean> selectedGroupList, MaintainBatchInfo.RowsBean item) {
        for (MaintainBatchInfo.RowsBean info : selectedGroupList) {
            if (info.equals(item)) {
                return info;
            }
        }
        return null;
    }

    public void setSelectedBatch() {
        Set<MaintainBatchInfo.RowsBean> selectedList = selectedGroupList;
        adapter.setSelectedGroupList(selectedList);

    }

    public void showDialog(View ll_select_group, MaintainBatchInfo info, int currentPage) {

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
        if(info != null) {
            adapter.setSelectedGroupList(info.getRows());
        }else{
            prePageButton.setVisibility(View.GONE);
            nextPageButton.setVisibility(View.GONE);
            return;
        }

        setSelectedBatch();

        prePageButton.setVisibility(currentPage > 1 ? View.VISIBLE : View.GONE);
        nextPageButton.setVisibility(currentPage * UploadFeedbackMapFragment.COUNT_PER_PAGE - info.getTotal() < 0 ? View.VISIBLE : View.GONE);
        setListener(info.getRows());
    }

    private void setListener(final List<MaintainBatchInfo.RowsBean> groupList) {
        RxView.clicks(yesButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        pw.dismiss();
                        Set<MaintainBatchInfo.RowsBean> selectedList = adapter.getSelectedEventTypeList();
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

    public void setFilterGroupListener(MaintainFilterGroupListener filterGroupListener) {
        mFilterGroupListener = filterGroupListener;
    }

    public void setSelectFirstInStete(MaintainBatchInfo info) {
        if(info ==null || ListUtil.isEmpty(info.getRows())) return ;
        selectedGroupList.add(info.getRows().get(0));
        setSelectedBatch();
        lastSelectGroupList.add(info.getRows().get(0));
        mFilterGroupListener.filterByIds(selectedGroupList);
    }
}
