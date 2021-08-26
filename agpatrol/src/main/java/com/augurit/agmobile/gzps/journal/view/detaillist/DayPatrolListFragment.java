package com.augurit.agmobile.gzps.journal.view.detaillist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.common.util.LoadDataConstant;
import com.augurit.agmobile.gzps.journal.DayPatrollDetailActivity;
import com.augurit.agmobile.gzps.journal.service.JournalService;
import com.augurit.agmobile.gzps.journal.view.journallist.MyJournalListAdapter;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.publicaffair.view.condition.DayPatrolFilterConditionEvent;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.util.ServerAttachmentToPhotoUtil;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.journal.view
 * @createTime 创建时间 ：2018-07-04
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-07-04
 * @modifyMemo 修改备注：
 */

public class DayPatrolListFragment extends Fragment {

    private ProgressLinearLayout pb_loading;
    private XRecyclerView mRecyclerView;
    private MyJournalListAdapter adapter;

    private JournalService mJournalsService;

    private int pageNo = 1;
    private final int pageSize = 10;
    private boolean loadMore = true;

    //筛选条件
    private String district = null;  //行政区划
    private String facilityType ;
    private long startTime;
    private long endTime;

    private String componentTypeCode = null;   //设施类型数据字典编码
    private String eventTypeCode = null;       //问题类型数据字典编码
    private String state = null;               //处理中还是已办结
    private Component mComponent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_patrol, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        savedInstanceState = getArguments();
        if (savedInstanceState != null) mComponent = (Component) getArguments().get("component");
        pb_loading = (ProgressLinearLayout) view.findViewById(R.id.pb_loading);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.event_list_rv);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setPullRefreshEnabled(false);
        adapter = new MyJournalListAdapter(getContext(), new ArrayList<ModifiedFacility>());
//        startTime = new Date(2018 - 1900, 0, 1).getTime();
//        initEndDate();
        FacilityAffairService facilityAffairService = new FacilityAffairService(this.getContext().getApplicationContext());
        boolean b = facilityAffairService.ifCurrentUserBelongToCityUser();
//        if(b){
//            district = "全部";
//        }else{
//            String[] districts = new String[]{facilityAffairService.getParentOrgOfCurrentUser()};
//            district = districts[0];
//        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                int selectPosition = position;
                if (pageNo > 1) {
                    selectPosition = position - adapter.getDataList().size() - pageNo * LoadDataConstant.LOAD_ITEM_PER_PAGE;
                }
                Intent intent = new Intent(getContext(), DayPatrollDetailActivity.class);
                intent.putExtra("data", adapter.getDataList().get(position - 1));
                getContext().startActivity(intent);
//                if (mContext instanceof IChangeTabListener) {
//                    IChangeTabListener tabListener = (IChangeTabListener) mContext;
//                    tabListener.changeToTab(1); //跳到地图界面
//                    org.greenrobot.eventbus.EventBus.getDefault().post(new SendModifiedFacilityEvent(mSearchResultAdapter.getDataList().get(position - 1), page, selectPosition - 1));
//                }
                // ((ModifiedIdentificationActivity3) mContext).showMapFragment((ArrayList<ModifiedFacility>) onePageData, page, selectPosition);
            }
        });
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                loadMore = false;
                loadEventList(false);
                mRecyclerView.setNoMore(false);
            }

            @Override
            public void onLoadMore() {
                pageNo++;
                loadMore = true;
                loadEventList(false);
            }
        });

        loadEventList(true);
    }

    private void initEndDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        int month = cal.get(Calendar.MONTH) + 1;   //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);
        endTime = new Date(year - 1900, month - 1, day).getTime();
    }

    private void loadEventList(boolean showPb) {
        mJournalsService = new JournalService(getContext());
        if (showPb) {
            pb_loading.showLoading();
        }
        //根据条件筛选问题上报列表
        Observable<Result3<List<ModifiedFacility>>> observable = null;
        if (mComponent != null) {
            String usid = (String) mComponent.getGraphic().getAttributes().get("US_ID");
            //不过滤情况
            observable = mJournalsService.getDiayList1(pageNo, pageSize, TextUtils.isEmpty(usid) || "null".equals(usid) ? mComponent.getObjectId() + "" : "", usid, "true");
        } else {
            observable = mJournalsService.getDiayList(pageNo, pageSize, facilityType, district, startTime+"", endTime+"", "false");
        }

        observable.map(new Func1<Result3<List<ModifiedFacility>>, List<ModifiedFacility>>() {
            @Override
            public List<ModifiedFacility> call(final Result3<List<ModifiedFacility>> modifiedFacilityResult2) {
                List<ModifiedFacility> identifications = new ArrayList<>();
                List<ModifiedFacility> data = modifiedFacilityResult2.getData();
                if (!ListUtil.isEmpty(data)) {
                    int i = 0;
                    for (ModifiedFacility modifiedFacility : data) {
                        modifiedFacility.setOrder(i);
                        modifiedFacility.setMarkTime(modifiedFacility.getRecordTime());
                        if (!StringUtil.isEmpty(modifiedFacility.getWriterName())) {
                            modifiedFacility.setMarkPerson(modifiedFacility.getWriterName());
                        }
                        if (!StringUtil.isEmpty(modifiedFacility.getWriterId())) {
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
                        return mJournalsService.getMyModificationAttachments(modifiedIdentification.getId())
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
                        if (pageNo == 1) {
                            showLoadedError(e.getLocalizedMessage());
                        } else {
                            ToastUtil.shortToast(getActivity(), "加载更多失败");
                        }
                    }

                    @Override
                    public void onNext(List<ModifiedFacility> modifiedIdentifications) {

                        if (ListUtil.isEmpty(modifiedIdentifications) && pageNo == 1) {
                            showLoadedEmpty();
                            return;
                        }

                        if (ListUtil.isEmpty(modifiedIdentifications) && pageNo > 1) {
                            mRecyclerView.setNoMore(true);
                            return;
                        }


                        pb_loading.showContent();
                        mRecyclerView.loadMoreComplete();
                        mRecyclerView.refreshComplete();
                        if (pageNo > 1) {
                            adapter.addData(modifiedIdentifications);
                            adapter.notifyDataSetChanged();
                        } else if (pageNo == 1) {
                            adapter.notifyDataChanged(modifiedIdentifications);
                            mRecyclerView.scrollToPosition(0);
                        }
                        mRecyclerView.setVisibility(View.VISIBLE);
//                        tv_component_counts.setText("一共有：" + mSearchResultAdapter.getDataList().size() + "条数据");
                    }
                });
    }


    public void showLoadedError(String errorReason) {
        pb_loading.showError("获取数据失败", errorReason, "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEventList(true);
            }
        });
    }

    public void showLoadedEmpty() {
        pb_loading.showError("", "暂无数据", "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEventList(true);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
//    @Subscribe
//    public void onEventConditionChanged(EventAffairConditionEvent eventAffairConditionEvent){
//        if(eventAffairConditionEvent.getDistrict() != null
//                && eventAffairConditionEvent.getDistrict().contains("净水")){
//            this.district = "净水";
//        } else {
//            this.district = eventAffairConditionEvent.getDistrict();
//        }
//        this.componentTypeCode = eventAffairConditionEvent.getComponentTypeCode();
//        this.eventTypeCode = eventAffairConditionEvent.getEventTypeCode();
//        pageNo = 1;
//        state = null;
//        loadMore = false;
//        loadEventList(true);
//        mRecyclerView.setNoMore(false);
//    }

    @Subscribe
    public void onReceivedDayPatrolFilterCondition(DayPatrolFilterConditionEvent dayPatrolFilterConditionEvent) {
        pb_loading.showLoading();
        pageNo = 1;
        this.facilityType = dayPatrolFilterConditionEvent.getFacilityType();
        this.district = dayPatrolFilterConditionEvent.getQueryDistrict();
        if(this.district.indexOf("净水公司")!=-1){
            this.district = "净水有限公司";
        }
        this.startTime = dayPatrolFilterConditionEvent.getStartTime();
        this.endTime = dayPatrolFilterConditionEvent.getEndTime();
        loadEventList(true);
    }

}
