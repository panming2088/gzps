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
import com.augurit.agmobile.gzps.common.util.LoadDataConstant;
import com.augurit.agmobile.gzps.common.util.SetCheckStateUtil;
import com.augurit.agmobile.gzps.setting.service.MyUploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;
import com.augurit.agmobile.gzps.uploadfacility.view.facilitydetail.UploadPipeDetailActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.FacilityFilterCondition;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.ModifyFacilitySumEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.MyPipeListAdapter;
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

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 我的新增列表
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.componentmaintenance
 * @createTime 创建时间 ：17/10/14
 * @modifyBy 修改人 ：xuciluan,luob
 * @modifyTime 修改时间 ：17/10/14
 * @modifyMemo 修改备注：
 */

public class MyCheckedPipeListFragment extends Fragment {

    private MyPipeListAdapter mSearchResultAdapter;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_my_add_pipe, null);

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
        mSearchResultAdapter = new MyPipeListAdapter(mContext, new ArrayList<PipeBean>());
        rv_component_list.setAdapter(mSearchResultAdapter);
        mSearchResultAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                int selectPosition = position;
                if (page > 1) {
                    selectPosition = position - mSearchResultAdapter.getDataList().size() - page * LoadDataConstant.LOAD_ITEM_PER_PAGE;
                }
                Intent intent = new Intent(mContext, UploadPipeDetailActivity.class);
                intent.putExtra("data", (Parcelable) mSearchResultAdapter.getDataList().get(position - 1));
                //1表示不显示删除按钮，2表示不显示删除按钮，显示撤销删除按钮
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
        //审核通过
        install_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkState = "5";
                page = 1;
                switchTopTabColor((LinearLayout) view);
                loadDatas(true);
            }
        });
        //审核不通过
        no_install_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkState = "3_4";
                page = 1;
                switchTopTabColor((LinearLayout) view);
                loadDatas(true);
            }
        });
        //审核中
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

        ((TextView) view.findViewById(R.id.tv_title)).setText("我的纠错");

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

        final MyUploadFacilityService identificationService = new MyUploadFacilityService(mContext);
        identificationService.getLinePipeByPerson(page, LoadDataConstant.LOAD_ITEM_PER_PAGE,checkState,2,mUploadedFacilityFilterCondition)
                .map(new Func1<Result3<List<PipeBean>>, List<PipeBean>>() {
                    @Override
                    public List<PipeBean> call(final Result3<List<PipeBean>> listResult2) {
//                        ((Activity) mContext).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                tv_component_counts.setText("一共有：" + listResult2.getUpdate() + "条数据");
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
                                    event.setPipeChecked(0);
                                    event.setStatus(4);
                                    EventBus.getDefault().post(event);
//                                    if (mModifyFacilitySumEvent == null) {
//                                        //如果还没有收到纠错的数量
//                                        isIfReceivedAddFacilitySum = true;
//                                    } else {
//                                        /**
//                                         *  如果此时收到了纠错数量，那么相加得到总数发送给{@link com.augurit.agmobile.gzps.setting.MyUploadStatisticActivity}
//                                         */
//                                        int total = mModifyFacilitySumEvent.getSum();
//                                        EventBus.getDefault().post(new UploadFacilitySumEvent(total));
//                                    }
                                } else {
                                    if (listResult2.getPass() != null && listResult2.getDoubt() != null && listResult2.getNo() != null) {
                                        double sum = Integer.valueOf(listResult2.getPass()) + Integer.valueOf(listResult2.getDoubt()) + Integer.valueOf(listResult2.getNo());
                                        all_count.setText(getString(sum));
                                        install_count.setText(getString(Double.valueOf(listResult2.getPass())));
                                        no_install_count.setText(getString(Double.valueOf(listResult2.getDoubt())));
                                        no_audit_count.setText(getString(Double.valueOf(listResult2.getNo())));
                                        //发送事件通知
                                        UploadFacilitySumEvent event = new UploadFacilitySumEvent();
                                        event.setPipeChecked((int)sum);
                                        event.setStatus(4);
                                        EventBus.getDefault().post(event);
//                                        if (mModifyFacilitySumEvent == null) {
//                                            //如果还没有收到纠错的数量
//                                            isIfReceivedAddFacilitySum = true;
//                                        } else {
//                                            /**
//                                             *  如果此时收到了纠错数量，那么相加得到总数发送给{@link com.augurit.agmobile.gzps.setting.MyUploadStatisticActivity}
//                                             */
//                                            try {
//                                                int total = mModifyFacilitySumEvent.getSum() + (int)sum;
////                                                EventBus.getDefault().post(new UploadFacilitySumEvent(total));
//                                            } catch (Exception e) {
//                                                //这里加try..catch的原因是为了就算发送过程发生错误，也不会影响列表的展示
//                                                e.printStackTrace();
//                                            }
//                                        }
                                    }
                                }
                            }
                        });
                        List<PipeBean> identifications = new ArrayList<>();

                        List<PipeBean> data = listResult2.getData();
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
                .subscribe(new Subscriber<List<PipeBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (page == 1) {
                            showLoadedError(e.getLocalizedMessage());
                            if (mModifyFacilitySumEvent == null) {
                                //如果还没有收到纠错的数量
                                isIfReceivedAddFacilitySum = true;
                            } else {
                                /**
                                 *  如果此时收到了纠错数量，那么相加得到总数发送给{@link com.augurit.agmobile.gzps.setting.MyUploadStatisticActivity}
                                 */
                                int total = mModifyFacilitySumEvent.getSum();
                                EventBus.getDefault().post(new UploadFacilitySumEvent(total));
                            }
                        } else {
                            ToastUtil.shortToast(mContext, "加载更多失败");
                        }
                    }

                    @Override
                    public void onNext(List<PipeBean> modifiedIdentifications) {

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
//                        tv_component_counts.setText("一共有：" + mSearchResultAdapter.getDataList().size() + "条数据");
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
            //还没有收到新增总数
            this.mModifyFacilitySumEvent = sumEvent;
        } else {
            /**
             *  如果此时收到了新增数量，那么相加得到总数发送给{@link com.augurit.agmobile.gzps.setting.MyUploadStatisticActivity}
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

        if (FacilityFilterCondition.MODIFIED_PIPE_LIST.equals(uploadedFacilityFilterCondition.filterListType)){
            page = 1;
            isIfReceivedAddFacilitySum = false;
            this.mUploadedFacilityFilterCondition = uploadedFacilityFilterCondition;
            loadDatas(true);
        }

    }

}
