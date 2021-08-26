package com.augurit.agmobile.patrolcore.layerdownload;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.DividerItemDecoration;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.model.Area;
import com.augurit.agmobile.mapengine.layerdownload.view.ILayerDownloadAreaSelectView;
import com.augurit.agmobile.mapengine.layerdownload.view.presenter.ILayerDownloadAreaSelectPresenter;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.layerdownload.adapter.LayerDownloadAreaAdapter;
import com.augurit.agmobile.patrolcore.layerdownload.presenter.LayerDownloadAreaSelectPresenter;

import java.util.List;

/**
 * 描述：图层下载区域选择视图
 *
 * @author 创建人 ：liangsh
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.defaultview.layerdownload
 * @createTime 创建时间 ：2016-12-27
 * @modifyBy 修改人 ：liangsh
 * @modifyTime 修改时间 ：2016-12-27
 * @modifyMemo 修改备注：
 */
public class LayerDownloadAreaSelectView implements ILayerDownloadAreaSelectView {

    private Context mContext;
    private ILayerDownloadAreaSelectPresenter mPresenter;
    private ViewGroup mContainer;
    private View mView;
    private RecyclerView mAreaRv;
    private LayerDownloadAreaAdapter mAreaAdapter;
    private View btnBack;
    private TextView tvTitle;
    private View tipsll;
    private View contentll;
//    private View backToParentArea;
//    private TextView parentNameTv;
    private boolean isParent = true;

    public LayerDownloadAreaSelectView(Context context, LayerDownloadAreaSelectPresenter presenter){
        this.mContext = context;
        this.mPresenter = presenter;
        init();
    }

    private void init(){
        mView = getLayout();
        initViewAndListener();
    }

    protected View getLayout(){
        return LayoutInflater.from(mContext).inflate(R.layout.dnl_view_area_select, null);
    }

    protected void initViewAndListener(){
        mAreaRv = (RecyclerView) mView.findViewById(R.id.dnl_area_select_list);
        mAreaRv.setLayoutManager(new LinearLayoutManager(mContext));
        btnBack = mView.findViewById(R.id.btn_back);
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        tipsll = mView.findViewById(R.id.dnl_area_select_tips);
        contentll = mView.findViewById(R.id.dnl_area_select_content);
//        backToParentArea = mView.findViewById(R.id.dnl_area_select_backparent);
//        parentNameTv = (TextView) mView.findViewById(R.id.dnl_area_select_parentname);
        mAreaRv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mAreaAdapter = new LayerDownloadAreaAdapter(mContext);
        mAreaRv.setAdapter(mAreaAdapter);
        mAreaAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<Area>() {
            @Override
            public void onItemClick(View view, int position, Area area) {
                if (isParent) {
                    mPresenter.onAreaClick(area);
                }
            }

            @Override
            public void onItemLongClick(View view, int position, Area area) {

            }
        });
        mAreaAdapter.setOnDownloadClickListener(new OnRecyclerItemClickListener<Area>() {
            @Override
            public void onItemClick(View view, int position, Area area) {
                mPresenter.onDownloadClick(area);
            }

            @Override
            public void onItemLongClick(View view, int position, Area area) {

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isParent){
                    mPresenter.goBack();
                } else {
                    mPresenter.backToParentArea();
                }

            }
        });
        /*backToParentArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    @Override
    public void setAreaList(List<Area> areaList, boolean isParent, String parentName){
//        parentNameTv.setText(parentName);

        this.isParent = isParent;
        if(isParent){
            tvTitle.setText("按行政区域下载");
        } else {
            tvTitle.setText(parentName);
        }
        mAreaAdapter.notifyDataSetChanged(areaList);
    }

    @Override
    public void showContent(){
        tipsll.setVisibility(View.GONE);
        contentll.setVisibility(View.VISIBLE);
    }

    @Override
    public void show(ViewGroup container){
        this.mContainer = container;
        mContainer.removeAllViews();
        mContainer.addView(mView);
    }

}
