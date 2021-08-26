package com.augurit.agmobile.patrolcore.common.opinion.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.common.DividerItemDecoration;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.agmobile.patrolcore.common.opinion.view.presenter.IOpinionTemplateListPresenter;
import com.augurit.am.fw.utils.ListUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.view
 * @createTime 创建时间 ：2017-07-17
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-17
 * @modifyMemo 修改备注：
 */

public class OpinionTemplateListView implements IOpinionTemplateListView {

    private Context mContext;
    private ViewGroup mContainer;
    private View mView;
    private IOpinionTemplateListPresenter mPresenter;
    private XRecyclerView rv_templates;
    private OpinionTemplateListAdapter mOpinionTemplateListAdapter;
    private boolean refreshOrLoadMore = false;  //false为刷新，true为加载更多

    public OpinionTemplateListView(Context context, ViewGroup container){
        this.mContext = context;
        this.mContainer = container;
        mView = getLayout();
        init();
    }

    private void init(){
        rv_templates = (XRecyclerView) mView.findViewById(R.id.rv_templates);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_templates.setLayoutManager(layoutManager);
        mOpinionTemplateListAdapter = new OpinionTemplateListAdapter(mContext);
        rv_templates.setAdapter(mOpinionTemplateListAdapter);
//        rv_templates.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        rv_templates.setPullRefreshEnabled(false);
        rv_templates.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//                refreshOrLoadMore = false;
//                mPresenter.refresh();
            }

            @Override
            public void onLoadMore() {
                refreshOrLoadMore = true;
                mPresenter.loadMore();
            }
        });
        mOpinionTemplateListAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener<OpinionTemplate>() {
            @Override
            public void onItemClick(View view, int position, OpinionTemplate selectedData) {
                if(selectedData == null){
                    return;
                }
                mPresenter.onTemplateSelect(selectedData);
            }

            @Override
            public void onItemLongClick(View view, int position, OpinionTemplate selectedData) {

            }
        });
        mContainer.addView(mView);
    }

    protected View getLayout() {
        return LayoutInflater.from(mContext).inflate(R.layout.opinion_template_listview, null);
    }

    public void setPresenter(IOpinionTemplateListPresenter presenter){
        this.mPresenter = presenter;
    }

    @Override
    public void showTemplates(List<OpinionTemplate> templateList) {
        if(ListUtil.isEmpty(templateList)){
            rv_templates.setNoMore(true);
            rv_templates.refreshComplete();
            rv_templates.loadMoreComplete();
            return;
        }
        if(refreshOrLoadMore){
            rv_templates.loadMoreComplete();
            mOpinionTemplateListAdapter.notifyDataSetChanged(templateList, true);
        } else {
            rv_templates.refreshComplete();
            mOpinionTemplateListAdapter.notifyDataSetChanged(templateList, false);
        }
        if(templateList.size() < 10){
            rv_templates.setNoMore(true);
        } else {
            rv_templates.setNoMore(false);
        }
    }

    @Override
    public void setRefreshOrLoadMore(boolean loadMore) {
        refreshOrLoadMore = loadMore;
    }
}
