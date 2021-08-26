package com.augurit.agmobile.gzps.journal.view.journallist;


import android.app.Activity;
import android.content.Context;
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

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.common.util.LoadDataConstant;
import com.augurit.agmobile.gzps.journal.DayPatrollDetailActivity;
import com.augurit.agmobile.gzps.journal.service.JournalService;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.util.ServerAttachmentToPhotoUtil;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.RefreshMyModificationListEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.FacilityFilterCondition;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.ModifyFacilitySumEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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

public class MyJournalListFragment extends Fragment {

    private MyJournalListAdapter mSearchResultAdapter;
    private ProgressLinearLayout pb_loading;
    private XRecyclerView rv_component_list;
    private TextView tv_component_counts;
    private Context mContext;
    private FacilityFilterCondition mFacilityFilterCondition = null;
    private int page = 1;
    private String checkState = null;


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
        mSearchResultAdapter = new MyJournalListAdapter(mContext, new ArrayList<ModifiedFacility>());
        rv_component_list.setAdapter(mSearchResultAdapter);
        mSearchResultAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                int selectPosition = position;
                if (page > 1) {
                    selectPosition = position - mSearchResultAdapter.getDataList().size() - page * LoadDataConstant.LOAD_ITEM_PER_PAGE;
                }
                Intent intent = new Intent(mContext, DayPatrollDetailActivity.class);
                intent.putExtra("data", mSearchResultAdapter.getDataList().get(position - 1));
                intent.putExtra("ISEDIT", true);
                mContext.startActivity(intent);
//                if (mContext instanceof IChangeTabListener) {
//                    IChangeTabListener tabListener = (IChangeTabListener) mContext;
//                    tabListener.changeToTab(1); //跳到地图界面
//                    org.greenrobot.eventbus.EventBus.getDefault().post(new SendModifiedFacilityEvent(mSearchResultAdapter.getDataList().get(position - 1), page, selectPosition - 1));
//                }
                // ((ModifiedIdentificationActivity3) mContext).showMapFragment((ArrayList<ModifiedFacility>) onePageData, page, selectPosition);
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

        final JournalService identificationService = new JournalService(mContext);
        identificationService.getMyModifications(page, LoadDataConstant.LOAD_ITEM_PER_PAGE, mFacilityFilterCondition)
                //获取列表（不包含附件）
                .map(new Func1<Result3<List<ModifiedFacility>>, List<ModifiedFacility>>() {
                    @Override
                    public List<ModifiedFacility> call(final Result3<List<ModifiedFacility>> modifiedFacilityResult2) {
                        List<ModifiedFacility> identifications = new ArrayList<>();
                        List<ModifiedFacility> data = modifiedFacilityResult2.getData();
                        if (!ListUtil.isEmpty(data)) {
                            int i = 0;
                            for (ModifiedFacility modifiedFacility : data) {
                                modifiedFacility.setOrder(i);
                                modifiedFacility.setMarkTime(modifiedFacility.getRecordTime());
                                if(!StringUtil.isEmpty(modifiedFacility.getWriterName())) {
                                    modifiedFacility.setMarkPerson(modifiedFacility.getWriterName());
                                }
                                if(!StringUtil.isEmpty(modifiedFacility.getWriterId())) {
                                    modifiedFacility.setMarkPersonId(modifiedFacility.getWriterId());
                                }
                                identifications.add(modifiedFacility);
                                i++;
                            }
                        }
                        return identifications;
                    }
                })
                .flatMap(new Func1<List<ModifiedFacility>, Observable<ModifiedFacility>>() {
                    @Override
                    public Observable<ModifiedFacility> call(List<ModifiedFacility> modifiedIdentifications) {
                        return Observable.from(modifiedIdentifications);
                    }
                })
                //获取附件
                .flatMap(new Func1<ModifiedFacility, Observable<ModifiedFacility>>() {
                    @Override
                    public Observable<ModifiedFacility> call(final ModifiedFacility modifiedIdentification) {
                        return identificationService.getMyModificationAttachments(modifiedIdentification.getId())
                                .map(new Func1<ServerAttachment, ModifiedFacility>() {
                                    @Override
                                    public ModifiedFacility call(ServerAttachment serverIdentificationAttachment) {
                                        List<ServerAttachment.ServerAttachmentDataBean> data = serverIdentificationAttachment.getData();
                                        if (!ListUtil.isEmpty(data)) {
                                            List<Photo> photos = new ArrayList<>();
                                            for (ServerAttachment.ServerAttachmentDataBean dataBean : data) {
                                                Photo photo = ServerAttachmentToPhotoUtil.getPhoto(dataBean);
                                                photos.add(photo);
                                            }
                                            modifiedIdentification.setPhotos(photos);
                                        }
                                        return modifiedIdentification;
                                    }
                                });
                    }
                })
                .toList()
                //进行手动排序
                .map(new Func1<List<ModifiedFacility>, List<ModifiedFacility>>() {
                    @Override
                    public List<ModifiedFacility> call(List<ModifiedFacility> modifiedIdentifications) {
                        Collections.sort(modifiedIdentifications, new Comparator<ModifiedFacility>() {
                            @Override
                            public int compare(ModifiedFacility modifiedIdentification, ModifiedFacility t1) {
                                if (modifiedIdentification.getOrder() > t1.getOrder()) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        });
                        return modifiedIdentifications;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ModifiedFacility>>() {
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
                    public void onNext(List<ModifiedFacility> modifiedIdentifications) {

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
                        tv_component_counts.setText("一共有：" + mSearchResultAdapter.getDataList().size() + "条数据");
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
        if (FacilityFilterCondition.MODIFIED_LIST.equals(facilityFilterCondition.filterListType)) {
            this.mFacilityFilterCondition = facilityFilterCondition;
            this.page = 1;
            loadDatas(true);
        }
    }

}
