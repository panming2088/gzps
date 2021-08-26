package com.augurit.agmobile.gzps.doorno.view;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.augurit.agmobile.gzps.BR;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.SewerageTableThreeData;
import com.augurit.agmobile.gzps.common.util.SewerageBeanManger;
import com.augurit.agmobile.gzps.doorno.adapter.BaseRecyleAdapter;
import com.augurit.agmobile.gzps.doorno.adapter.SewerageTableThreeAdapter;
import com.augurit.agmobile.gzps.doorno.model.PSHAffairDetail;
import com.augurit.agmobile.gzps.doorno.model.SewerageInfoBean;
import com.augurit.agmobile.gzps.doorno.model.WellMessageEvent;
import com.augurit.am.fw.utils.ListUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoyu on 2018/4/4.
 */

public class SewerageTableThreeFragment extends MyBaseFragment {


    private SewerageTableThreeData sewerageTableThreeData;
    private MyLinearLayoutManager linearLayoutManager;
//    private BaseRecyleAdapter baseRecyleAdapter;
    private SewerageTableThreeAdapter baseRecyleAdapter;
    private List<SewerageInfoBean.WellBeen> userInfos = new ArrayList<>();//初始化在set值之前;
    private SewerageInfoBean.WellBeen userInfo;
    private SewerageInfoBean.WellBeen mWellBeen;
    private int index = -1;
    private PSHAffairDetail pshAffairDetail;
    private boolean EDIT_MODE;
    private boolean isAllowSaveLocalDraft;
    private boolean fromPSHAffair;
    private SewerageInfoBean draftBean;

    public static SewerageTableThreeFragment newInstance(Bundle bundle) {
        SewerageTableThreeFragment sewerageTableThreeFragment = new SewerageTableThreeFragment();
        sewerageTableThreeFragment.setArguments(bundle);
        return sewerageTableThreeFragment;
    }


    public interface onViewCreatedListener {
        void onViewCreated();
    }

    public onViewCreatedListener viewCreatedListener;

    public void setViewCreatedListener(onViewCreatedListener viewCreatedListener) {
        this.viewCreatedListener = viewCreatedListener;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (viewCreatedListener != null) {
            viewCreatedListener.onViewCreated();
        }

    }

    @Override
    public int initview() {
        return R.layout.table_sewerage_three;
    }

    public void setEditMode(boolean isEdit) {
        if (isEdit != EDIT_MODE) {
            EDIT_MODE = isEdit;
            setSewerageData(false);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        sewerageTableThreeData = getBind();
        pshAffairDetail = (PSHAffairDetail) getArguments().getSerializable("pshAffair");
        isAllowSaveLocalDraft = getArguments().getBoolean("isAllowSaveLocalDraft",false);
        fromPSHAffair  = getArguments().getBoolean("fromPSHAffair",false);
        draftBean  = (SewerageInfoBean) getArguments().getSerializable("draftBean");
        setSewerageData(true);
        /**
         * 加载adapter
         */
        linearLayoutManager = new MyLinearLayoutManager(getActivity());
        sewerageTableThreeData.rvSewersgetableThree.setLayoutManager(linearLayoutManager);
        //     sewerageTableThreeData.rvSewersgetableThree.addItemDecoration(new MyDividerDecoration(getActivity()));
        Map<Integer, Integer> map = new HashMap<>();
        map.put(R.layout.item_seweragetable_three, BR.wellbean);
        baseRecyleAdapter = new SewerageTableThreeAdapter(userInfos, map,getContext());


        sewerageTableThreeData.rvSewersgetableThree.setAdapter(baseRecyleAdapter);
        sewerageTableThreeData.setSeweragetablethreeonclic(new SewerageTableThreeOnclick());
        baseRecyleAdapter.setOnRecycleitemOnClic(new SewerageOnRecycleitemOnClic());
        sewerageTableThreeData.rvSewersgetableThree.setLoadingMoreEnabled(false);
        sewerageTableThreeData.rvSewersgetableThree.setPullRefreshEnabled(false);

//        setEditMode(true);
    }

    /**
     * 隐藏添加管
     * @param isInitDta
     */
    private void setAddHiden(boolean isInitDta) {
        if(isAllowSaveLocalDraft){
            sewerageTableThreeData.tvSereragetableThreeAdd.setVisibility(View.VISIBLE);
        }else if(fromPSHAffair && isInitDta){
            sewerageTableThreeData.tvSereragetableThreeAdd.setVisibility(View.GONE);
        }else if(isInitDta && pshAffairDetail!=null){
            sewerageTableThreeData.tvSereragetableThreeAdd.setVisibility(View.GONE);
        }else{
            sewerageTableThreeData.tvSereragetableThreeAdd.setVisibility(EDIT_MODE ?  View.VISIBLE: View.GONE);
        }
    }

    private void setSewerageData(boolean isInitDta) {
        if (pshAffairDetail != null) {
            setAddHiden(isInitDta); //只读状态隐藏添加按钮
            setView();
        }

        if (pshAffairDetail == null) {
            sewerageTableThreeData.flEmpty.setVisibility(View.VISIBLE);
        }

    }

    private void setView() {
        //解决腾讯bugly上的空指针崩溃问题
        if(pshAffairDetail==null || pshAffairDetail.getData()==null){
            return ;
        }
        List<SewerageInfoBean.WellBeen> wellBeen = pshAffairDetail.getData().getWellBeen();
        if (!ListUtil.isEmpty(wellBeen)) {
            userInfos.clear();
            for (int i = 0; i < wellBeen.size(); i++) {
                SewerageInfoBean.WellBeen wellBeenBean = wellBeen.get(i);
                userInfo = new SewerageInfoBean.WellBeen();
                userInfo.setDrainPro(wellBeenBean.getDrainPro());
                if (!TextUtils.isEmpty(wellBeenBean.getDrainPro())) {
                    String pros[] = wellBeenBean.getDrainPro().split("#");
                    String newDrainPro = "";
                    for (int j = 0; j < pros.length; j++) {
                        if (!TextUtils.isEmpty(pros[j])) {
                            if (j != pros.length - 1) {
                                newDrainPro = newDrainPro + pros[j] + "、";
                            } else {
                                newDrainPro = newDrainPro + pros[j];
                            }
                        }
                    }
                    userInfo.setNewDrainPro(newDrainPro);
                }
                userInfo.setWellDir(wellBeenBean.getWellDir());
                userInfo.setWellType(wellBeenBean.getWellType());
                userInfo.setWellPro(wellBeenBean.getWellPro());
                userInfo.setWellId(wellBeenBean.getWellId());
                userInfo.setPipeType(wellBeenBean.getPipeType());
                userInfo.setId(wellBeenBean.getId());
                userInfo.setLxId(wellBeenBean.getLxId());
                userInfo.setWiId(wellBeenBean.getWiId());
                userInfo.setX(wellBeenBean.getX());
                userInfo.setY(wellBeenBean.getY());
                userInfo.setOnlyid(wellBeenBean.getOnlyid());
                this.userInfos.add(userInfo);
            }
            sewerageTableThreeData.flEmpty.setVisibility(View.GONE);
        } else {
            sewerageTableThreeData.flEmpty.setVisibility(View.VISIBLE);
        }
        SewerageBeanManger.getInstance().setWellBeen(userInfos);
    }

    private class SewerageTableThreeOnclick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_sereragetable_three_add:
//                    Intent intent = new Intent(getActivity(), SewerageTableThreeAddAvtivity.class);
//                    view.getContext().startActivity(intent);

                    break;
            }
        }
    }

    public class SewerageOnRecycleitemOnClic implements BaseRecyleAdapter.OnRecycleitemOnClic {
        @Override
        public void onItemClic(View view, int position) {
            Intent intent = new Intent(getActivity(), SewerageTableThreeAddAvtivity.class);
            intent.putExtra("wellInfo", userInfos.get(position));
//            intent.putExtra("fromPSHAffair", fromPSHAffair);
            intent.putExtra("fromAffair", pshAffairDetail != null);
            intent.putExtra("pshAffair", pshAffairDetail);
            intent.putExtra("editmode", isAllowSaveLocalDraft? true:EDIT_MODE);
            intent.putExtra("draftBean", draftBean);
            intent.putExtra("isAllowSaveLocalDraft", isAllowSaveLocalDraft);

            index = position;
            view.getContext().startActivity(intent);
        }
    }

    @Subscribe
    public void onEventMainThread(WellMessageEvent event) {
        mWellBeen = event.getWellBeen();

        if (event.getTag() == 0) {
            this.userInfos.add(mWellBeen);
        } else if (event.getTag() == 1) {
            userInfos.remove(index);
            userInfos.add(index, mWellBeen);
        }
        SewerageBeanManger.getInstance().setWellBeen(userInfos);
        baseRecyleAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEventMainThread(SewerageInfoBean.WellBeen event) {
        if (event != null) {
            for (SewerageInfoBean.WellBeen wellBeen : userInfos) {
                if (wellBeen.getId() != null && event.getId() != null && wellBeen.getId() != 0 && event.getId() != 0) {
                    if (wellBeen.getId().equals(event.getId())) {
                        userInfos.remove(wellBeen);
                        SewerageBeanManger.getInstance().setDelWell(event.getId() + "");
                        break;
                    }
                } else if (!TextUtils.isEmpty(wellBeen.getOnlyid()) && !TextUtils.isEmpty(event.getOnlyid())) {
                    if (wellBeen.getOnlyid().equals(event.getOnlyid())) {
                        userInfos.remove(wellBeen);
                        break;
                    }
//                    break;
                }
            }
        }

        SewerageBeanManger.getInstance().setWellBeen(userInfos);
        baseRecyleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        sewerageTableThreeData.flEmpty.setVisibility((userInfos != null && userInfos.size() > 0) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);//反注册EventBus
        }
    }
}
