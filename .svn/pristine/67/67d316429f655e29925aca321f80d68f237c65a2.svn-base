package com.augurit.agmobile.gzps.trackmonitor.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.trackmonitor.model.UserOrg;
import com.augurit.agmobile.gzps.trackmonitor.view.presenter.ISubordinateListPresenter;
import com.augurit.agmobile.mapengine.common.DividerItemDecoration;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.trackmonitor.view
 * @createTime 创建时间 ：2017-08-21
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-21
 * @modifyMemo 修改备注：
 */

public class SubordinateListView implements ISubordinateListView{

    private Context mContext;
    private ViewGroup mContainer;
    private View mView;

    private RecyclerView rv_subordinate;
    private SubordinateListAdapter mSubordinateListAdapter;

    private ISubordinateListPresenter mPresenter;

    public SubordinateListView(Context context, ViewGroup container){
        this.mContext = context;
        this.mContainer = container;
        mView = getLayout();
        init();
    }

    protected View getLayout(){
        return LayoutInflater.from(mContext).inflate(R.layout.subordinate_list, mContainer, false);
    }

    private void init(){
        TextView tv_title = (TextView) mView.findViewById(R.id.tv_title);
        tv_title.setText("部门人员列表");
        mView.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rv_subordinate = (RecyclerView) mView.findViewById(R.id.rv_subordinate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_subordinate.setLayoutManager(layoutManager);
        rv_subordinate.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mSubordinateListAdapter = new SubordinateListAdapter(mContext);
        rv_subordinate.setAdapter(mSubordinateListAdapter);
        mSubordinateListAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener<UserOrg>() {
            @Override
            public void onItemClick(View view, int position, UserOrg selectedData) {
                mPresenter.enter(selectedData);
            }

            @Override
            public void onItemLongClick(View view, int position, UserOrg selectedData) {

            }
        });
        mContainer.addView(mView);
    }

    public void setPresenter(ISubordinateListPresenter subordinateListPresenter){
        mPresenter = subordinateListPresenter;
    }

    @Override
    public void showSubordinateList(List<UserOrg> subordinateList) {
        mSubordinateListAdapter.notifyDataSetChanged(subordinateList);
    }
}
