package com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.util.LoadDataConstant;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadevent.view.eventlist.EventDetailMapActivity;
import com.augurit.agmobile.gzps.uploadfacility.model.DoubtBean;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.RefreshMyModificationListEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的纠错列表
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.componentmaintenance
 * @createTime 创建时间 ：17/10/14
 * @modifyBy 修改人 ：xuciluan,luobiao
 * @modifyTime 修改时间 ：17/10/14
 * @modifyMemo 修改备注：
 */

public class MyDoubtListFragment extends Fragment {

    private MyDoubtListAdapter mSearchResultAdapter;
    private ProgressLinearLayout pb_loading;
    private XRecyclerView rv_component_list;
    private TextView tv_component_counts;
    private Context mContext;
    private FacilityFilterCondition mFacilityFilterCondition = null;
    private int page = 1;
    private String checkState = null;
    private List<DoubtBean> mDoubtBeans;
    private UploadFacilityService mIdentificationService;
    private ProgressDialog mDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_my_journallist, null);

        return inflate;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  onePageData = new ArrayList<>();

        pb_loading = (ProgressLinearLayout) view.findViewById(R.id.pb_loading);

        rv_component_list = (XRecyclerView) view.findViewById(R.id.rv_component_list);
        rv_component_list.setPullRefreshEnabled(true);
        rv_component_list.setLoadingMoreEnabled(true);
        rv_component_list.setLayoutManager(new LinearLayoutManager(mContext));
        mSearchResultAdapter = new MyDoubtListAdapter(mContext, new ArrayList<DoubtBean>());
        rv_component_list.setAdapter(mSearchResultAdapter);
        mSearchResultAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
               DoubtBean doubtBean = mSearchResultAdapter.getDataList().get(position-1);
                String layerUrl = "";
                if(doubtBean.getLayerName().contains("排水管道")){
                    layerUrl = getLayerUrlForName("排水管道")+"/0";
                }else{
                    layerUrl = getLayerUrlForName("排水管井")+"/0";
                }
                String objectId = doubtBean.getObjectId();
                if("/0".equals(layerUrl)){
                    ToastUtil.shortToast(mContext,"layerUrl为空");
                    return;
                }
                Intent intent = new Intent(mContext, EventDetailMapActivity.class);
                intent.putExtra("layerUrl", layerUrl);
                intent.putExtra("objectId", objectId);
                intent.putExtra("layerName", doubtBean.getLayerName());
                startActivity(intent);
            }
        });
        mSearchResultAdapter.setOnItemLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, final int position, long id) {
                final DoubtBean doubtBean = mSearchResultAdapter.getDataList().get(position-1);
                if(doubtBean.getStatus().equals("2")){
                    return false;
                }
                DialogUtil.MessageBox(mContext, "提示", "确定取消该记录的存疑状态？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelDoubt(doubtBean);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                return false;
            }
        });
        rv_component_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadDatas(false);
            }

            @Override
            public void onLoadMore() {
                page++;
                loadDatas(false);
            }
        });

        tv_component_counts = (TextView) view.findViewById(R.id.tv_component_counts);

        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });

        ((TextView) view.findViewById(R.id.tv_title)).setText("我的纠错");

        view.findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
        ImageView iv_open_search = (ImageView) view.findViewById(R.id.iv_open_search);
        iv_open_search.setImageResource(R.mipmap.ic_map);
        loadDatas(true);
        EventBus.getDefault().register(this);
    }

    private void cancelDoubt(DoubtBean doubtBean) {
        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("取消存疑中.....");
        mDialog.show();
        if(mIdentificationService == null) {
            mIdentificationService = new UploadFacilityService(mContext);
        }
        mIdentificationService.partsDoubt(doubtBean.getId())
                //获取列表（不包含附件）
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result2<String>>() {
                    @Override
                    public void onCompleted() {
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "存疑取消失败，请稍后重试");
                    }

                    @Override
                    public void onNext(Result2<String> s) {
                        if (s.getCode() == 200) {
                            Toast.makeText(mContext, "存疑取消成功", Toast.LENGTH_SHORT).show();
                            loadDatas(true);
                        } else {
//                            CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
//                                    BaseInfoManager.getUserName(mContext) + "原因是：" + s.getMessage()));
                            Toast.makeText(mContext, s.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void showLoadedError(String errorReason) {
        pb_loading.showError("获取数据失败", errorReason, "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatas(true);
            }
        });
    }

    public void showLoadedEmpty() {
        pb_loading.showError("", "暂无数据", "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatas(true);
            }
        });
    }

    public void loadDatas(boolean ifShowPb) {
        if (ifShowPb)
            pb_loading.showLoading();
        if(mIdentificationService == null) {
            mIdentificationService = new UploadFacilityService(mContext);
        }
        mIdentificationService.getDoubtMark(page, LoadDataConstant.LOAD_ITEM_PER_PAGE,null,4, mFacilityFilterCondition)
                //获取列表（不包含附件）
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result3<List<DoubtBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (page == 1) {
                            showLoadedError(e.getLocalizedMessage());
                        } else {
                            ToastUtil.shortToast(mContext, "加载更多失败");
                        }
                    }

                    @Override
                    public void onNext(Result3<List<DoubtBean>> listResult3) {
                        if(listResult3 != null && listResult3.getCode() == 200){
                            mDoubtBeans = listResult3.getData();
                            if (ListUtil.isEmpty(mDoubtBeans) && page == 1) {
                                showLoadedEmpty();
                                return;
                            }

                            if (ListUtil.isEmpty(mDoubtBeans) && page > 1) {
                                rv_component_list.setNoMore(true);
                                return;
                            }


                            pb_loading.showContent();
                            rv_component_list.loadMoreComplete();
                            rv_component_list.refreshComplete();
                            if (page > 1) {
                                mSearchResultAdapter.addData(mDoubtBeans);
                                mSearchResultAdapter.notifyDataSetChanged();
                            } else if (page == 1) {
                                mSearchResultAdapter.notifyDataChanged(mDoubtBeans);
                                rv_component_list.scrollToPosition(0);
                            }
                            tv_component_counts.setVisibility(View.GONE);
//                            tv_component_counts.setText("一共有：" + mSearchResultAdapter.getDataList().size() + "条数据");
                        }
                    }
                });
    }


    private String getString(double n) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(n);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe
    public void RefreshRefreshMyUploadListEvent(RefreshMyUploadList refreshMyModificationListEvent) {
        if (mSearchResultAdapter != null) {
            EventBus.getDefault().post(new ModifyFacilitySumEvent(mSearchResultAdapter.getItemCount()));
        }
    }

    @Subscribe
    public void RefreshMyModificationListEvent(RefreshMyModificationListEvent refreshMyModificationListEvent) {
        page = 1;
        loadDatas(true);
    }

    @Subscribe
    public void onRefreshList(FacilityFilterCondition facilityFilterCondition) {
        if (FacilityFilterCondition.DOUBT_WELL_LIST.equals(facilityFilterCondition.filterListType)) {
            this.mFacilityFilterCondition = facilityFilterCondition;
            this.page = 1;
            loadDatas(true);
        }
    }

    /**
     * 通过图层名来获取图层URL
     * @param name
     * @return
     */
    private String getLayerUrlForName(String name) {
        List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        if (ListUtil.isEmpty(layerInfosFromLocal)) {
            return null;
        }
        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().equals(name)) {
                return layerInfo.getUrl();
            }
        }
        return "";
    }
}
