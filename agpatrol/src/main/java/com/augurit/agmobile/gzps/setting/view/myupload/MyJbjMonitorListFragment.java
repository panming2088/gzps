package com.augurit.agmobile.gzps.setting.view.myupload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.util.Attachment2PhotoUtil;
import com.augurit.agmobile.gzps.common.util.LoadDataConstant;
import com.augurit.agmobile.gzps.common.util.SetCheckStateUtil;
import com.augurit.agmobile.gzps.jbjpsdy.WellMonitorRefreshEvent;
import com.augurit.agmobile.gzps.monitor.model.WellMonitorInfo;
import com.augurit.agmobile.gzps.setting.service.MyUploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.view.JbjMonitorListAdapter;
import com.augurit.agmobile.gzps.uploadfacility.view.facilitydetail.UploadJbjMonitorDetailActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.FacilityFilterCondition;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.ModifyFacilitySumEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.RefreshPipeListEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.UploadFacilitySumEvent;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * ??????????????????
 *
 * @author ????????? ???xuciluan
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.gzps.componentmaintenance
 * @createTime ???????????? ???17/10/14
 * @modifyBy ????????? ???xuciluan,luob
 * @modifyTime ???????????? ???17/10/14
 * @modifyMemo ???????????????
 */

public class MyJbjMonitorListFragment extends Fragment {

    private JbjMonitorListAdapter mSearchResultAdapter;
    private ProgressLinearLayout pb_loading;
    private XRecyclerView rv_component_list;
    private TextView tv_component_counts;
    private TextView all_count, install_count, no_install_count, no_audit_count;
    private LinearLayout all_ll, install_ll, no_install_ll, no_audit_ll;
    private String checkState = null;
    private Context mContext;

    private int page = 1;

    private ModifyFacilitySumEvent mModifyFacilitySumEvent;

    private FacilityFilterCondition mUploadedFacilityFilterCondition;

    private boolean isIfReceivedAddFacilitySum = false;
    private TextView tv_sum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_my_add_hook, null);

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
        ((TextView)view.findViewById(R.id.check_ing)).setText(SetCheckStateUtil.CHECK_ING);
        ((TextView)view.findViewById(R.id.check_no_pass)).setText(SetCheckStateUtil.CHECK_NO_PASS);
        ((TextView)view.findViewById(R.id.check_pass)).setText(SetCheckStateUtil.CHECK_PASS);
        pb_loading = (ProgressLinearLayout) view.findViewById(R.id.pb_loading);
        tv_sum = (TextView) view.findViewById(R.id.tv_sum);
        all_count = (TextView) view.findViewById(R.id.all_install_count);
        install_count = (TextView) view.findViewById(R.id.install_count);
        no_install_count = (TextView) view.findViewById(R.id.no_install_count);
        no_audit_count = (TextView) view.findViewById(R.id.no_audited_count);

        all_ll = (LinearLayout) view.findViewById(R.id.all_install_ll);
        install_ll = (LinearLayout) view.findViewById(R.id.install_ll);
        no_install_ll = (LinearLayout) view.findViewById(R.id.no_install_ll);
        no_audit_ll = (LinearLayout) view.findViewById(R.id.no_audited_ll);

        rv_component_list = (XRecyclerView) view.findViewById(R.id.rv_component_list);
        rv_component_list.setPullRefreshEnabled(true);
        rv_component_list.setLoadingMoreEnabled(true);
        rv_component_list.setLayoutManager(new LinearLayoutManager(mContext));
        mSearchResultAdapter = new JbjMonitorListAdapter(mContext, new ArrayList<WellMonitorInfo>());
        rv_component_list.setAdapter(mSearchResultAdapter);
        mSearchResultAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                int selectPosition = position;
                if (page > 1) {
                    selectPosition = position - mSearchResultAdapter.getDataList().size() - page * LoadDataConstant.LOAD_ITEM_PER_PAGE;
                }
                WellMonitorInfo wellMonitorInfo = mSearchResultAdapter.getDataList().get(position - 1);
                wellMonitorInfo.setPhotos(Attachment2PhotoUtil.toPhotos(wellMonitorInfo.getAttachments()));
                Intent intent = new Intent(mContext, UploadJbjMonitorDetailActivity.class);
                intent.putExtra("data", (Parcelable) wellMonitorInfo);
                //1??????????????????????????????2??????????????????????????????????????????????????????
                intent.putExtra("isShowCancelDeleteButton", 2);
                mContext.startActivity(intent);
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

        all_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkState = null;
                page = 1;
                switchTopTabColor((LinearLayout) view);
                loadDatas(true);
            }
        });
        //????????????
        install_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkState = "5";
                page = 1;
                switchTopTabColor((LinearLayout) view);
                loadDatas(true);
            }
        });
        //???????????????
        no_install_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkState = "3_4";
                page = 1;
                switchTopTabColor((LinearLayout) view);
                loadDatas(true);
            }
        });
        //?????????
        no_audit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkState = "1_2";
                page = 1;
                switchTopTabColor((LinearLayout) view);
                loadDatas(true);
            }
        });


        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });

        ((TextView) view.findViewById(R.id.tv_title)).setText("????????????");

        view.findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
        ImageView iv_open_search = (ImageView) view.findViewById(R.id.iv_open_search);
        iv_open_search.setImageResource(R.mipmap.ic_map);
        loadDatas(true);
        EventBus.getDefault().register(this);
    }

    private void switchTopTabColor(LinearLayout linearLayout) {
        all_ll.setBackgroundColor(mContext.getResources().getColor(R.color.white_alpha));
        install_ll.setBackgroundColor(mContext.getResources().getColor(R.color.white_alpha));
        no_install_ll.setBackgroundColor(mContext.getResources().getColor(R.color.white_alpha));
        no_audit_ll.setBackgroundColor(mContext.getResources().getColor(R.color.white_alpha));
        linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.light_green_alpha));
    }


    public void showLoadedError(String errorReason) {
        pb_loading.showError("??????????????????", errorReason, "??????", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatas(true);
            }
        });
    }

    public void showLoadedEmpty() {
        pb_loading.showError("", "????????????", "??????", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatas(true);
            }
        });
    }

    public void loadDatas(boolean ifShowPb) {
        if (ifShowPb)
            pb_loading.showLoading();

        final MyUploadFacilityService identificationService = new MyUploadFacilityService(mContext);
        identificationService.getJbjJcCorrect(page, LoadDataConstant.LOAD_ITEM_PER_PAGE,checkState,2,mUploadedFacilityFilterCondition)
                .map(new Func1<Result3<List<WellMonitorInfo>>, List<WellMonitorInfo>>() {
                    @Override
                    public List<WellMonitorInfo> call(final Result3<List<WellMonitorInfo>> listResult2) {
//                        ((Activity) mContext).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                tv_component_counts.setText("????????????" + listResult2.getUpdate() + "?????????");
//                            }});
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (listResult2 == null) {
                                    all_count.setText(0);
                                    install_count.setText(0);
                                    no_install_count.setText(0);
                                    no_audit_count.setText(0);
                                    UploadFacilitySumEvent event = new UploadFacilitySumEvent();
                                    event.setfChecked(0);
                                    event.setStatus(2);
                                    EventBus.getDefault().post(event);
//                                    if (mModifyFacilitySumEvent == null) {
//                                        //????????????????????????????????????
//                                        isIfReceivedAddFacilitySum = true;
//                                    } else {
//                                        /**
//                                         *  ?????????????????????????????????????????????????????????????????????{@link com.augurit.agmobile.gzps.setting.MyUploadStatisticActivity}
//                                         */
//                                        int total = mModifyFacilitySumEvent.getSum();
//                                        EventBus.getDefault().post(new UploadFacilitySumEvent(total));
//                                    }
                                } else {
                                    UploadFacilitySumEvent event = new UploadFacilitySumEvent();
                                    event.setStatus(2);
                                    if (listResult2.getTotal() != null) {
                                        int sum = Integer.valueOf(listResult2.getTotal());
                                        tv_sum.setText("??????????????????" + sum + "???");
                                        event.setfChecked(sum);
                                    } else {
                                        tv_sum.setText("??????????????????0???");
                                        event.setfChecked(0);
                                    }
                                    EventBus.getDefault().post(event);
//                                    if (listResult2.getPass() != null && listResult2.getDoubt() != null && listResult2.getNo() != null) {
//                                        double sum = Integer.valueOf(listResult2.getPass()) + Integer.valueOf(listResult2.getDoubt()) + Integer.valueOf(listResult2.getNo());
//                                        all_count.setText(getString(sum));
//                                        install_count.setText(getString(Double.valueOf(listResult2.getPass())));
//                                        no_install_count.setText(getString(Double.valueOf(listResult2.getDoubt())));
//                                        no_audit_count.setText(getString(Double.valueOf(listResult2.getNo())));
//                                        //??????????????????
//                                        UploadFacilitySumEvent event = new UploadFacilitySumEvent();
//                                        event.setPipeChecked((int)sum);
//                                        event.setStatus(4);
//                                        EventBus.getDefault().post(event);
////                                        if (mModifyFacilitySumEvent == null) {
////                                            //????????????????????????????????????
////                                            isIfReceivedAddFacilitySum = true;
////                                        } else {
////                                            /**
////                                             *  ?????????????????????????????????????????????????????????????????????{@link com.augurit.agmobile.gzps.setting.MyUploadStatisticActivity}
////                                             */
////                                            try {
////                                                int total = mModifyFacilitySumEvent.getSum() + (int)sum;
//////                                                EventBus.getDefault().post(new UploadFacilitySumEvent(total));
////                                            } catch (Exception e) {
////                                                //?????????try..catch?????????????????????????????????????????????????????????????????????????????????
////                                                e.printStackTrace();
////                                            }
////                                        }
//                                    }
                                }
                            }
                        });
                        List<WellMonitorInfo> identifications = new ArrayList<>();

                        List<WellMonitorInfo> data = listResult2.getData();
//                        if (!ListUtil.isEmpty(data)) {
//                            int i = 0;
//                            for (PipeBean modifiedFacility : data) {
//                                modifiedFacility.setOrder(i);
//                                identifications.add(modifiedFacility);
//                                i++;
//                            }
//                        }
                        return data;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Observer<? super List<WellMonitorInfo>>) new Subscriber<List<WellMonitorInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (page == 1) {
                            showLoadedError(e.getLocalizedMessage());
                            if (mModifyFacilitySumEvent == null) {
                                //????????????????????????????????????
                                isIfReceivedAddFacilitySum = true;
                            } else {
                                /**
                                 *  ?????????????????????????????????????????????????????????????????????{@link com.augurit.agmobile.gzps.setting.MyUploadStatisticActivity}
                                 */
                                int total = mModifyFacilitySumEvent.getSum();
                                EventBus.getDefault().post(new UploadFacilitySumEvent(total));
                            }
                        } else {
                            ToastUtil.shortToast(mContext, "??????????????????");
                        }
                    }

                    @Override
                    public void onNext(List<WellMonitorInfo> modifiedIdentifications) {

                        if (ListUtil.isEmpty(modifiedIdentifications) && page == 1) {
                            showLoadedEmpty();
                            return;
                        }

                        if (ListUtil.isEmpty(modifiedIdentifications) && page > 1) {
                            rv_component_list.setNoMore(true);
                            return;
                        }

                        pb_loading.showContent();
                        rv_component_list.loadMoreComplete();
                        rv_component_list.refreshComplete();
                        if (page > 1) {
                            mSearchResultAdapter.addData(modifiedIdentifications);
                            mSearchResultAdapter.notifyDataSetChanged();
                        } else if (page == 1) {
                            mSearchResultAdapter.notifyDataChanged(modifiedIdentifications);
                            rv_component_list.scrollToPosition(0);
                        }
                        tv_component_counts.setVisibility(View.GONE);
//                        tv_component_counts.setText("????????????" + mSearchResultAdapter.getDataList().size() + "?????????");
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
    public void RefreshMyModificationListEvent(RefreshPipeListEvent refreshMyModificationListEvent) {
        if(refreshMyModificationListEvent != null && refreshMyModificationListEvent.getType() == 2) {
            page = 1;
            isIfReceivedAddFacilitySum = false;
            loadDatas(true);
        }
    }


    @Subscribe
    public void onReceivedModifiedFacilitySumEvent(ModifyFacilitySumEvent sumEvent) {
        if (!isIfReceivedAddFacilitySum) {
            //???????????????????????????
            this.mModifyFacilitySumEvent = sumEvent;
        } else {
            /**
             *  ?????????????????????????????????????????????????????????????????????{@link com.augurit.agmobile.gzps.setting.MyUploadStatisticActivity}
             */
            this.mModifyFacilitySumEvent = sumEvent;
            if (all_count != null && all_count.getText() != null
                    && all_count.getText().toString() != null) {
                String s = all_count.getText().toString().replace(",", "");
                try {
                    Integer sum = Integer.valueOf(s);
                    int total = sumEvent.getSum() + sum;
                    EventBus.getDefault().post(new UploadFacilitySumEvent(total));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Subscribe
    public void refreshList(FacilityFilterCondition uploadedFacilityFilterCondition){

        if (FacilityFilterCondition.MONITOR_LIST.equals(uploadedFacilityFilterCondition.filterListType)) {
            page = 1;
            isIfReceivedAddFacilitySum = false;
            this.mUploadedFacilityFilterCondition = uploadedFacilityFilterCondition;
            loadDatas(true);
        }

    }

    @Subscribe
    public void refreshList(WellMonitorRefreshEvent refreshEvent){
            page = 1;
            loadDatas(true);
        }
}
