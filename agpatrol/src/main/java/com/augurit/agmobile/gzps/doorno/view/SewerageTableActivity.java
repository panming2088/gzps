package com.augurit.agmobile.gzps.doorno.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.util.CrashReportUtil;
import com.augurit.agmobile.gzps.common.util.FileBean;
import com.augurit.agmobile.gzps.common.util.SetColorUtil;
import com.augurit.agmobile.gzps.common.util.SewerageBeanManger;
import com.augurit.agmobile.gzps.doorno.model.BackPressEvent;
import com.augurit.agmobile.gzps.doorno.model.MyApplication;
import com.augurit.agmobile.gzps.doorno.model.PSHAffairDetail;
import com.augurit.agmobile.gzps.doorno.model.PhotoEvent;
import com.augurit.agmobile.gzps.doorno.model.RefreshDataListEvent;
import com.augurit.agmobile.gzps.doorno.model.SewerageInfoBean;
import com.augurit.agmobile.gzps.doorno.model.SewerageItemBean;
import com.augurit.agmobile.gzps.doorno.model.SewerageRoomClickItemBean;
import com.augurit.agmobile.gzps.doorno.service.UploadUserInfoService;
import com.augurit.agmobile.gzps.uploadevent.util.PhotoUploadType;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.agmobile.gzpssb.SewerageTableActivityData;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.update.utils.CheckUpdateUtils;
import com.augurit.am.cmpt.update.utils.UpdateState;
import com.augurit.am.cmpt.update.view.ApkUpdateManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.log.save.imp.AMCrashWriter;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by xiaoyu on 2018/4/3.
 */

public class SewerageTableActivity extends BaseDataBindingActivity {

    private SewerageTableActivityData sewerageTableActivityData;
    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragments;
    private PSHAffairDetail pshAffairDetail;
    private SewerageItemBean.UnitListBean unitListBeans;
    private SewerageRoomClickItemBean.UnitListBean roomclickunitListBeans;
    private CountDownTimer countDownTimer;
    private boolean fromMyUpload;
    private boolean fromPSHAffair;
    private boolean EDIT_MODE, initFrag0, initFrag1, initFrag2, initFrag3;
    private int curIndex;
    private static final int TIMEOUT = 60;  //???????????????????????????
    private ProgressDialog progressDialog;
    private String isExist;
    private boolean isAddNewDoorNo;
    private boolean isfromQuryAddressMapFragmnet;
    //????????????????????????4????????????
    private boolean isTempStorage;
    private boolean isDialy;
    private boolean isList;
    private boolean isCancel;
    private boolean isAllowSaveLocalDraft;
    private SewerageInfoBean draftBean;
    private boolean isIndustry;


    @Override
    public int initview() {
        return R.layout.activity_seweragetable;
    }

    @Override
    public void initdatabinding() {
        sewerageTableActivityData = getBind();
        sewerageTableActivityData.setSeweragetableonclic(new SewerageTableOnclick());
    }

    @Override
    public void initData() {
        SewerageBeanManger.getInstance().clear();
        pshAffairDetail = (PSHAffairDetail) getIntent().getSerializableExtra("pshAffair");
        fromPSHAffair = getIntent().getBooleanExtra("fromPSHAffair", false);
        fromMyUpload = getIntent().getBooleanExtra("fromMyUpload", false);
        isTempStorage = getIntent().getBooleanExtra("isTempStorage", false);
        isIndustry = getIntent().getBooleanExtra("isIndustry",false);
        if(isIndustry){
            MyApplication.clearData();
        }
        isAllowSaveLocalDraft = getIntent().getBooleanExtra("isAllowSaveLocalDraft", false);
        isDialy = getIntent().getBooleanExtra("isDialy", false);
        isList = getIntent().getBooleanExtra("isList", false);
        isCancel = getIntent().getBooleanExtra("isCancel", false);
        draftBean = (SewerageInfoBean) getIntent().getSerializableExtra("draftBean");

        isfromQuryAddressMapFragmnet = getIntent().getBooleanExtra("isfromQuryAddressMapFragmnet", false);
        isExist = getIntent().getStringExtra("isExist");

        if (fromMyUpload && !isCancel) {
            sewerageTableActivityData.btnSeweragetableCommit.setVisibility(View.GONE);
            sewerageTableActivityData.btnSeweragetableEdit.setVisibility(View.VISIBLE);
            if(!isList) {
                sewerageTableActivityData.btnSeweragetableCancel.setVisibility(View.VISIBLE);
            }
            sewerageTableActivityData.btnSeweragetableDel.setVisibility(View.GONE);
            setValue();
        }else if(isCancel){
            sewerageTableActivityData.btnSeweragetableCommit.setVisibility(View.GONE);
        }
        if(isAllowSaveLocalDraft){
            setValue();
        }
        sewerageTableActivityData.btnTempStorage.setVisibility(fromMyUpload ? View.GONE : View.VISIBLE);
        if(isAllowSaveLocalDraft){
            sewerageTableActivityData.btnSaveDraft.setVisibility(View.VISIBLE);
            RxView.clicks(sewerageTableActivityData.btnSaveDraft)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            final SewerageInfoBean infoBean = SewerageBeanManger.getInstance().getInfoBean();
                            if(draftBean!= null){
                                infoBean.setDbid(draftBean.getDbid());
                            }
                            infoBean.setMarkPersonId(BaseInfoManager.getUserId(SewerageTableActivity.this));
                            infoBean.setSaveTime(System.currentTimeMillis());
                            ((SewerageTableOneFragment) fragments.get(0)).getString();//?????????
                            ((SewerageTableOneFragment) fragments.get(0)).getTab1AllParams();
                            if(TextUtils.isEmpty(infoBean.getDoorplateAddressCode())){
                                if(draftBean!= null && !TextUtils.isEmpty(draftBean.getDoorplateAddressCode()) ){
                                    infoBean.setDoorplateAddressCode(draftBean.getDoorplateAddressCode());
                                    infoBean.setX(draftBean.getX());
                                    infoBean.setY(draftBean.getY());
                                }else{
                                    infoBean.setDoorplateAddressCode(MyApplication.GUID);
                                    infoBean.setX(MyApplication.X);
                                    infoBean.setY(MyApplication.Y);
                                    infoBean.setHouseId(getIntent().getStringExtra("HouseId"));
                                    infoBean.setHouseIdFlag(getIntent().getStringExtra("HouseIdFlag"));
                                    infoBean.setUnitId(getIntent().getStringExtra("UnitId"));
                                    if (!TextUtils.isEmpty(isExist) && "1".equals(isExist)) {
                                        infoBean.setAdd_type("0");
                                    }
                                }
                            }
                            AMDatabase.getInstance().save(infoBean);
                            //????????????
                            savePhotos(infoBean.getDbid()+"", infoBean.getPhotos0(),1, PhotoUploadType.UPLOAD_FOR_PHOTOS0);
                            savePhotos(infoBean.getDbid()+"", infoBean.getThumbnailPhotos0(),2, PhotoUploadType.UPLOAD_FOR_PHOTOS0);
                            //????????????????????????
                            savePhotos(infoBean.getDbid()+"", infoBean.getPhotos1(),1, PhotoUploadType.UPLOAD_FOR_PHOTOS1);
                            savePhotos(infoBean.getDbid()+"", infoBean.getThumbnailPhotos1(),2, PhotoUploadType.UPLOAD_FOR_PHOTOS1);
                            //?????????????????????
                            savePhotos(infoBean.getDbid()+"", infoBean.getPhotos2(),1, PhotoUploadType.UPLOAD_FOR_PHOTOS2);
                            savePhotos(infoBean.getDbid()+"", infoBean.getThumbnailPhotos2(),2, PhotoUploadType.UPLOAD_FOR_PHOTOS2);
                            //???????????????
                            savePhotos(infoBean.getDbid()+"", infoBean.getPhotos3(),1, PhotoUploadType.UPLOAD_FOR_PHOTOS3);
                            savePhotos(infoBean.getDbid()+"", infoBean.getThumbnailPhotos3(),2, PhotoUploadType.UPLOAD_FOR_PHOTOS3);
                            //???????????????
                            savePhotos(infoBean.getDbid()+"", infoBean.getPhotos4(),1, PhotoUploadType.UPLOAD_FOR_PHOTOS4);
                            savePhotos(infoBean.getDbid()+"", infoBean.getThumbnailPhotos4(),2, PhotoUploadType.UPLOAD_FOR_PHOTOS4);
                            //?????????
                            savePhotos(infoBean.getDbid()+"", infoBean.getPhotos5(),1, PhotoUploadType.UPLOAD_FOR_PHOTOS5);
                            savePhotos(infoBean.getDbid()+"", infoBean.getThumbnailPhotos5(),2, PhotoUploadType.UPLOAD_FOR_PHOTOS5);
                            //???????????????
                            savePhotos(infoBean.getDbid()+"", infoBean.getPhotos7(),1, PhotoUploadType.UPLOAD_FOR_PHOTOS7);
                            savePhotos(infoBean.getDbid()+"", infoBean.getThumbnailPhotos7(),2, PhotoUploadType.UPLOAD_FOR_PHOTOS7);

                            //?????????pdf??????
                            List<FileBean> fileBeans = infoBean.getFiles();
                            if(!ListUtil.isEmpty(fileBeans)) {
                                String name = null;
                                for (FileBean file : fileBeans) {
                                    file.setProblem_id(infoBean.getDbid() + "");
                                    name = file.getName();
                                    if(!TextUtils.isEmpty(name)){
                                       if(name.indexOf(PhotoUploadType.UPLOAD_FOR_FILES)!=-1){
                                           name = file.getName();
                                       }else{
                                           name = PhotoUploadType.UPLOAD_FOR_FILES + file.getName();
                                       }
                                    }
                                    file.setName(name);
                                    AMDatabase.getInstance().save(file);
                                }
                            }

                            List<SewerageInfoBean.WellBeen> wellBeens = infoBean.getWellBeen();
                            if(!ListUtil.isEmpty(wellBeens)) {
                                for (SewerageInfoBean.WellBeen wb : wellBeens) {
                                    wb.setWiId(wb.getWiId());
                                    wb.setProblem_id(infoBean.getDbid() + "");
                                    AMDatabase.getInstance().save(wb);
                                }
                            }
                            ToastUtil.shortToast(SewerageTableActivity.this, "????????????");
                            setResult(123);
                            finish();
                            }
                    });
           if(isAllowSaveLocalDraft && draftBean != null){
               sewerageTableActivityData.btnTempStorage.setVisibility(View.GONE);
           }
        }

        if (pshAffairDetail != null && fromPSHAffair) {
            //????????????????????????????????????isEdit???true????????????????????????
            if ("true".equals(pshAffairDetail.getIsEdit()) && !isDialy && !isCancel) {
                sewerageTableActivityData.btnTempStorage.setVisibility(View.GONE);
                sewerageTableActivityData.btnSeweragetableCommit.setVisibility(View.GONE);
                sewerageTableActivityData.btnSeweragetableDel.setVisibility(View.GONE);
                sewerageTableActivityData.btnSeweragetableEdit.setVisibility(View.VISIBLE);
                if(!isList) {
                    sewerageTableActivityData.btnSeweragetableCancel.setVisibility(View.VISIBLE);
                }
            } else {
                sewerageTableActivityData.llBottom.setVisibility(View.GONE);
            }
        }
        if(isAllowSaveLocalDraft){
            sewerageTableActivityData.btnSeweragetableEdit.setVisibility(View.GONE);
        }

        setDataTitle("???????????????????????????");
        setRightIsVisiable(View.GONE);
        fragmentManager = getSupportFragmentManager();
        baseInfoData.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pshAffairDetail == null || EDIT_MODE) {
                    //???????????????
                    DialogUtil.MessageBox(context, "??????", "?????????????????????????????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    finish();
                }

            }
        });

        /**
         * ??????????????????????????????
         */
        unitListBeans = (SewerageItemBean.UnitListBean) getIntent().getSerializableExtra("unitListBeans");

        /**
         * ??????????????????????????????
         */
        roomclickunitListBeans = (SewerageRoomClickItemBean.UnitListBean) getIntent().getSerializableExtra("RoomclickunitListBeans");

        initFragment();
        initEvent();

        if(isAllowSaveLocalDraft && draftBean != null ){
            sewerageTableActivityData.btnSeweragetableDel.setVisibility(View.VISIBLE);
            RxView.clicks(sewerageTableActivityData.btnSeweragetableDel)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            DialogUtil.MessageBox(SewerageTableActivity.this, null, "?????????????????????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AMDatabase.getInstance().deleteWhere(FileBean.class, "problem_id", draftBean.getDbid() + "");
                                    AMDatabase.getInstance().deleteWhere(Photo.class, "problem_id", draftBean.getDbid() + "");
                                    AMDatabase.getInstance().deleteWhere(SewerageInfoBean.WellBeen.class, "problem_id", draftBean.getDbid() + "");
                                    AMDatabase.getInstance().delete(draftBean);
                                    setResult(123);
                                    SewerageTableActivity.this.finish();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
        }
    }


    /**
     *   type 1????????????2????????????
     *   dbid??????????????????id
     */
    private void savePhotos(String dbid, List<Photo> photoList1, int type, String preTag) {
        if(!ListUtil.isEmpty(photoList1)) {
            String name = null;
            for (Photo photo : photoList1) {
                name = photo.getPhotoName();
                if(!TextUtils.isEmpty(name)){
                    if(name.indexOf(preTag)!=-1){
                        name = photo.getPhotoName();
                    }else{
                        name = preTag + photo.getPhotoName();
                    }
                }
                photo.setType(type);
                photo.setProblem_id(dbid);
                photo.setPhotoName(name);
                AMDatabase.getInstance().save(photo);
            }
        }
    }

    /**
     * ????????????
     */
    private void initEvent() {
        RxView.clicks(sewerageTableActivityData.btnSeweragetableCommit)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        checkVersionUpdate(isTempStorage, true);
//                     commit(isTempStorage,true);
                    }
                });
        RxView.clicks(sewerageTableActivityData.btnTempStorage)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        checkVersionUpdate(true, false);
//                      commit(true,false);
                    }
                });

        RxView.clicks(sewerageTableActivityData.btnSeweragetableDel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDeleteDialog();
                    }
                });
        RxView.clicks(sewerageTableActivityData.btnSeweragetableEdit)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (fromMyUpload && pshAffairDetail != null && "2".equals(pshAffairDetail.getData().getState())) {
                            //????????????????????????????????????????????? ?????????????????? ????????????????????????????????????????????????????????????????????????????????????????????????  ??????????????????????????????????????????
                            DialogUtil.MessageBox(SewerageTableActivity.this, "????????????", "????????????????????????????????????????????????????????????????????????????????????",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            setEditMode();
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dismissDialog();
                                        }
                                    });
                        } else {
                            setEditMode();
                        }
                    }
                });
        RxView.clicks(sewerageTableActivityData.btnSeweragetableCancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //????????????????????????????????????????????? ?????????????????? ????????????????????????????????????????????????????????????????????????????????????????????????  ??????????????????????????????????????????
                        DialogUtil.MessageBox(SewerageTableActivity.this, "??????", "????????????????????????",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        cancel();
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dismissDialog();
                                    }
                                });
                    }
                });

    }

    private void setValue() {
        if (pshAffairDetail == null || pshAffairDetail.getData() == null) {
            return;
        }
        PSHAffairDetail.DataBean data = pshAffairDetail.getData();
        SewerageBeanManger.getInstance().setJzwcode(data.getJzwcode());
        SewerageBeanManger.getInstance().setLoginName(data.getLoginName());
        SewerageBeanManger.getInstance().setDirectOrgId(data.getDirectOrgId());
        SewerageBeanManger.getInstance().setDirectOrgName(data.getDirectOrgName());
        SewerageBeanManger.getInstance().setTeamOrgId(data.getTeamOrgId());
        SewerageBeanManger.getInstance().setTeamOrgName(data.getTeamOrgName());
        SewerageBeanManger.getInstance().setParentOrgId(data.getParentOrgId());
        SewerageBeanManger.getInstance().setParentOrgName(data.getParentOrgName());
        SewerageBeanManger.getInstance().setId(data.getId());
        SewerageBeanManger.getInstance().setDoorplateAddressCode(data.getDoorplateAddressCode());
        SewerageBeanManger.getInstance().setHouseIdFlag(data.getHouseIdFlag());
        SewerageBeanManger.getInstance().setHouseId(data.getHouseId());
        SewerageBeanManger.getInstance().setMarkPersonId(data.getMarkPersonId());
        SewerageBeanManger.getInstance().setMarkPerson(data.getMarkPerson());
//        SewerageBeanManger.getInstance().setMarkTime(new Date(data.getMarkTime()));
        SewerageBeanManger.getInstance().setArea(data.getArea());
        SewerageBeanManger.getInstance().setVillage(data.getVillage());
        SewerageBeanManger.getInstance().setStreet(data.getStreet());
        SewerageBeanManger.getInstance().setDischargerType1(data.getDischargerType1());
        SewerageBeanManger.getInstance().setDischargerType2(data.getDischargerType2());
        SewerageBeanManger.getInstance().setPsxkFzrq(data.getPsxkFzrq());
        SewerageBeanManger.getInstance().setPsxkJzrq(data.getPsxkJzrq());
        SewerageBeanManger.getInstance().setFac1(data.getFac1());
        SewerageBeanManger.getInstance().setFac1Cont(data.getFac1Cont());
        SewerageBeanManger.getInstance().setFac1Main(data.getFac1Main());
        SewerageBeanManger.getInstance().setFac1Record(data.getFac1Record());
        SewerageBeanManger.getInstance().setFac2(data.getFac2());
        SewerageBeanManger.getInstance().setFac2Cont(data.getFac2Cont());
        SewerageBeanManger.getInstance().setFac2Main(data.getFac2Main());
        SewerageBeanManger.getInstance().setFac2Record(data.getFac2Record());
        SewerageBeanManger.getInstance().setFac3(data.getFac3());
        SewerageBeanManger.getInstance().setFac3Cont(data.getFac3Cont());
        SewerageBeanManger.getInstance().setFac3Main(data.getFac3Main());
        SewerageBeanManger.getInstance().setFac3Record(data.getFac3Record());
        SewerageBeanManger.getInstance().setFac4(data.getFac4());
        SewerageBeanManger.getInstance().setFac4Cont(data.getFac4Cont());
        SewerageBeanManger.getInstance().setFac4Main(data.getFac4Main());
        SewerageBeanManger.getInstance().setFac4Record(data.getFac4Record());
        SewerageBeanManger.getInstance().setState(data.getState());
        SewerageBeanManger.getInstance().setCheckPersonId(data.getCheckPersonId());
        SewerageBeanManger.getInstance().setCheckPerson(data.getCheckPerson());
//        SewerageBeanManger.getInstance().setCheckTime(new Data);
        SewerageBeanManger.getInstance().setCheckDesription(data.getCheckDesription());
        SewerageBeanManger.getInstance().setWellBeen(data.getWellBeen());
        SewerageBeanManger.getInstance().setX(data.getX());
        SewerageBeanManger.getInstance().setY(data.getY());
        SewerageBeanManger.getInstance().setUnitId(data.getUnitId());
        SewerageBeanManger.getInstance().setDescription(data.getDescription());
        SewerageBeanManger.getInstance().setWaterNo(data.getWaterNo());
        SewerageBeanManger.getInstance().setSfgypsh(data.isSfgypsh());

//        SewerageBeanManger.getInstance().setAddType(data.getad());
    }


    /**
     * ??????????????????
     */
    private void initFragment() {
        fragments = new ArrayList<Fragment>();
        Bundle bundle = new Bundle();
        bundle.putSerializable("pshAffair", pshAffairDetail);
        bundle.putBoolean("fromMyUpload", fromMyUpload);
        bundle.putBoolean("fromPSHAffair", fromPSHAffair);
        bundle.putSerializable("unitListBeans", unitListBeans);
        bundle.putSerializable("draftBean",draftBean);
        bundle.putBoolean("isAllowSaveLocalDraft",isAllowSaveLocalDraft);
        bundle.putBoolean("isTempStorage", isTempStorage);
        bundle.putBoolean("isIndustry", isIndustry);
        bundle.putSerializable("roomclickunitListBeans", roomclickunitListBeans);
        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        bundle.putBoolean("isfromQuryAddressMapFragmnet", isfromQuryAddressMapFragmnet);

        fragments.add(SewerageTableOneFragment.newInstance(bundle));
        fragments.add(SewerageTableTwoFragment.newInstance(bundle));
        fragments.add(SewerageTableThreeFragment.newInstance(bundle));
        fragments.add(SewerageTableFourFragment.newInstance(bundle));

        ((SewerageTableOneFragment) fragments.get(0)).setViewCreatedListener(new SewerageTableOneFragment.onViewCreatedListener() {
            @Override
            public void onViewCreated() {
                initFrag0 = true;
                if (EDIT_MODE) {
                    ((SewerageTableOneFragment) fragments.get(0)).setEditMode(true);
                }
            }
        });

        ((SewerageTableTwoFragment) fragments.get(1)).setViewCreatedListener(new SewerageTableTwoFragment.onViewCreatedListener() {
            @Override
            public void onViewCreated() {

                initFrag1 = true;
                if (EDIT_MODE) {
                    ((SewerageTableTwoFragment) fragments.get(1)).setEditMode(true);
                }
            }
        });

        ((SewerageTableThreeFragment) fragments.get(2)).setViewCreatedListener(new SewerageTableThreeFragment.onViewCreatedListener() {
            @Override
            public void onViewCreated() {
                initFrag2 = true;
                if (EDIT_MODE) {
                    ((SewerageTableThreeFragment) fragments.get(2)).setEditMode(true);
                }
            }
        });
        ((SewerageTableFourFragment) fragments.get(3)).setViewCreatedListener(new SewerageTableFourFragment.onViewCreatedListener() {
            @Override
            public void onViewCreated() {

                initFrag3 = true;
                if (EDIT_MODE) {
                    ((SewerageTableFourFragment) fragments.get(3)).setEditMode(true);
                }
            }
        });

        showFragment(fragments.get(0));

    }

    /**
     * ??????fragment
     *
     * @param fragment ????????????fragment
     */
    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.ll_container, fragment, fragment.getClass().getName());
        }
        transaction.commit();
    }

    /**
     * ????????????fragment
     *
     * @param transaction ?????????
     */
    private void hideFragment(FragmentTransaction transaction) {
        for (int i = 0; fragments.size() > i; i++) {
            if (fragments.get(i).isVisible()) {
                transaction.hide(fragments.get(i));
            }
        }
    }


    private class SewerageTableOnclick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_fg_seweragetable_one:
                    setCurChooseTab(0);
                    break;
                case R.id.ll_fg_seweragetable_two:
                    setCurChooseTab(1);
                    break;
                case R.id.ll_fg_seweragetable_three:
                    setCurChooseTab(2);
                    break;
                case R.id.ll_fg_seweragetable_four:
                    setCurChooseTab(3);
                    break;
            }

        }
    }

    private void showDeleteDialog() {
        DialogUtil.MessageBox(this, "??????", "????????????????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    private void setEditMode() {
        ((SewerageTableOneFragment) fragments.get(0)).hideBoottomSheet();
        ((SewerageTableOneFragment) fragments.get(0)).hideLlstate();
        sewerageTableActivityData.btnSeweragetableCommit.setVisibility(View.VISIBLE);
        sewerageTableActivityData.btnSeweragetableEdit.setVisibility(View.GONE);
        sewerageTableActivityData.btnSeweragetableCancel.setVisibility(View.GONE);
        sewerageTableActivityData.btnSeweragetableDel.setVisibility("true".equals(pshAffairDetail.getIsEdit()) ? View.GONE : View.VISIBLE);
        sewerageTableActivityData.btnTempStorage.setVisibility((fromMyUpload && !isTempStorage) || "true".equals(pshAffairDetail.getIsEdit()) ? View.GONE : View.VISIBLE);
        EDIT_MODE = true;
        if (initFrag0) {
            ((SewerageTableOneFragment) fragments.get(0)).setEditMode(true);
        }
        if (initFrag1) {
            ((SewerageTableTwoFragment) fragments.get(1)).setEditMode(true);
        }
        if (initFrag2) {
            ((SewerageTableThreeFragment) fragments.get(2)).setEditMode(true);
        }
        if (initFrag3) {
            ((SewerageTableFourFragment) fragments.get(3)).setEditMode(true);
        }
    }

    private void delete() {
        if (pshAffairDetail != null) {
            new UploadUserInfoService(SewerageTableActivity.this)
                    .toDeleteCollect(pshAffairDetail.getData().getId())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.shortToast(SewerageTableActivity.this, "????????????");
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            if (responseBody != null && responseBody.getCode() == 200) {
                                ToastUtil.shortToast(SewerageTableActivity.this, "????????????");
                                EventBus.getDefault().post(new RefreshMyUploadList());
                                SewerageTableActivity.this.finish();
                            } else {
                                ToastUtil.shortToast(SewerageTableActivity.this, "????????????");
                            }
                        }
                    });
        }

    }

    private void cancel() {
        if (pshAffairDetail != null) {
            new UploadUserInfoService(SewerageTableActivity.this)
                    .toCancelCollect(pshAffairDetail.getData().getId())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.shortToast(SewerageTableActivity.this, "????????????");
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            if (responseBody != null && responseBody.getCode() == 200) {
                                ToastUtil.shortToast(SewerageTableActivity.this, "????????????");
                                EventBus.getDefault().post(new RefreshMyUploadList());
                                SewerageTableActivity.this.finish();
                            } else {
                                ToastUtil.shortToast(SewerageTableActivity.this, "????????????");
                            }
                        }
                    });
        }

    }

    private void setCurChooseTab(int tabIndex) {
        SetColorUtil.setBgColor(SewerageTableActivity.this, tabIndex,
                sewerageTableActivityData.llFgSeweragetableOne, sewerageTableActivityData.llFgSeweragetableTwo,
                sewerageTableActivityData.llFgSeweragetableThree, sewerageTableActivityData.llFgSeweragetableFour
        );
        curIndex = tabIndex;
        showFragment(fragments.get(tabIndex));

    }

    /**
     * ????????????
     *
     * @param isTempStorage
     * @param isComit
     */
    private void checkVersionUpdate(boolean isTempStorage, boolean isComit) {
//        checkVersionUpdateWithPermissonCheck(this, LoginConstant.APP_UPDATE_URL_ARR, isTempStorage, isComit);
    }

    private void checkVersionUpdateWithPermissonCheck(Context context, String appUpdateUrlArr[], final boolean isTempStorage, final boolean isComit) {
//        String updateUrl = appUpdateUrlArr[new Random().nextInt(appUpdateUrlArr.length)];
//        //String updateUrl = "http://" + LoginConstant.BASE_GZPS_URL + "/appFile/apk_version.json";
//        CheckUpdateUtils.setServerUrl(updateUrl);
//        showWaitDialog();
//        new ApkUpdateManager(context, UpdateState.INNER_UPDATE, new ApkUpdateManager.NoneUpdateCallback() {
//            @Override
//            public void onFinish(boolean isNeedUpdate) {
//                if (!isNeedUpdate) {
//                    commit(isTempStorage, isComit);
//                } else {
//                    dismissWaitDialog();
//                }
//            }
//        }).checkUpdate();
    }

    /**
     * @param isTempStorage ???????????????
     * @param isCommit      ?????????????????????
     */
    private void commit(final boolean isTempStorage, final boolean isCommit) {

        final SewerageInfoBean infoBean = SewerageBeanManger.getInstance().getInfoBean();
        ((SewerageTableOneFragment) fragments.get(0)).getString();//?????????
        ((SewerageTableOneFragment) fragments.get(0)).getTab1AllParams();

        /*    ((SewerageTableTwoFragment) fragments.get(1)).setDefault();*/
        if (!check(infoBean)) {
            if (!isCommit) {
                SewerageBeanManger.getInstance().setSaveType(1);
                SewerageBeanManger.getInstance().setState("4");
            } else {
                SewerageBeanManger.getInstance().setState("1");
                SewerageBeanManger.getInstance().setSaveType(0);
            }

            if (isCommit) {
                if (isTempStorage) {
                    DialogUtil.MessageBox(SewerageTableActivity.this, "????????????", "?????????????????????????????????????????????????????????",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    commitData(infoBean, isCommit);
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dismissDialog();
                                }
                            });
                } else {
                    commitData(infoBean, isCommit);
                }
            } else {
                DialogUtil.MessageBox(SewerageTableActivity.this, "????????????", "???????????????????????????????????????????????????",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                commitData(infoBean, isCommit);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dismissDialog();
                            }
                        });
            }
//            sewerageTableActivityData.pbLoading.showLoading();
        } else {
            dismissWaitDialog();
        }
    }

    private void commitData(final SewerageInfoBean infoBean, final boolean isCommit) {
        Observable<ResponseBody> responseBodyObservable;
        //????????????????????????????????????????????????
        if ((fromMyUpload || pshAffairDetail != null) && !isAllowSaveLocalDraft || isIndustry) {
            //???????????????????????????????????????
            SewerageBeanManger.getInstance().setId(pshAffairDetail != null && pshAffairDetail.getData() != null ? pshAffairDetail.getData().getId() : "");
            if(isIndustry){
                PSHAffairDetail.DataBean dataBean =  pshAffairDetail.getData();
                SewerageBeanManger.getInstance().setDoorplateAddressCode(TextUtils.isEmpty(MyApplication.GUID)?dataBean.getDoorplateAddressCode():MyApplication.GUID);
                SewerageBeanManger.getInstance().setX(MyApplication.X==0?dataBean.getX():MyApplication.X);
                SewerageBeanManger.getInstance().setY(MyApplication.Y==0?dataBean.getY():MyApplication.Y);
                //TODO ?????????????????????????????????
                infoBean.setHouseId(TextUtils.isEmpty(MyApplication.buildId)? dataBean.getHouseId():MyApplication.buildId);
                infoBean.setHouseIdFlag("0");
                infoBean.setUnitId("");
                infoBean.setSfdrpsh("1");
                infoBean.setYsUnitid(dataBean.getUnitId());
                String isExist = null;
                infoBean.setIndustry(true);
                if(MyApplication.doorBean != null){
                    isExist = MyApplication.doorBean.getIsExist();
                    if (!TextUtils.isEmpty(isExist) && "1".equals(isExist)) {
                        infoBean.setAdd_type("0");
                    }
                }else{
                }
            }else {
                infoBean.setIndustry(false);
            }

            responseBodyObservable = new UploadUserInfoService(SewerageTableActivity.this).toUpdateCollect(infoBean);
        } else {
            if(isAllowSaveLocalDraft && draftBean!=null){
                //????????????????????????????????????????????????????????????null???
            }else{
                SewerageBeanManger.getInstance().setDoorplateAddressCode(MyApplication.GUID);
                SewerageBeanManger.getInstance().setX(MyApplication.X);
                SewerageBeanManger.getInstance().setY(MyApplication.Y);

                    infoBean.setHouseId(getIntent().getStringExtra("HouseId"));
                    isAddNewDoorNo = getIntent().getBooleanExtra("isAddNewDoorNo", false);
                    infoBean.setHouseIdFlag(getIntent().getStringExtra("HouseIdFlag"));
                    infoBean.setUnitId(getIntent().getStringExtra("UnitId"));

            }
            if (!TextUtils.isEmpty(isExist) && "1".equals(isExist)) {
                infoBean.setAdd_type("0");
            }
            infoBean.setDraft(isAllowSaveLocalDraft);
            responseBodyObservable = new UploadUserInfoService(SewerageTableActivity.this).uploadUserInfo(infoBean);
        }
        showDialog();

        responseBodyObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable error) {
                dismissDialog();
                ToastUtil.shortToast(SewerageTableActivity.this, !isCommit ? "????????????" : "????????????");
                AMCrashWriter.getInstance(getApplicationContext()).writeCrash(error, "?????????????????????????????????");
                CrashReportUtil.reportBugMsg(SewerageTableActivity.this, error, "?????????????????????????????????");
//                    CrashReport.postCatchedException(new Exception("?????????????????????????????????--????????????"+
//                            new LoginRouter(SewerageTableActivity.this, AMDatabase.getInstance()).getUser().getLoginName()+ error.getMessage()));
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                dismissDialog();
                if (responseBody != null && responseBody.getCode() == 200) {
                    if (!isCommit) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "????????????");
                    } else if (!isTempStorage && fromMyUpload) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "????????????");
                    } else {
                        ToastUtil.shortToast(SewerageTableActivity.this, "????????????");
                    }

                    if(isAllowSaveLocalDraft && draftBean!= null){
                        //TODO ????????????????????????????????????????????????????????????????????????
                        AMDatabase.getInstance().deleteWhere(FileBean.class, "problem_id", draftBean.getDbid() + "");
                        AMDatabase.getInstance().deleteWhere(Photo.class, "problem_id", draftBean.getDbid() + "");
                        AMDatabase.getInstance().deleteWhere(SewerageInfoBean.WellBeen.class, "problem_id", draftBean.getDbid() + "");
                        AMDatabase.getInstance().delete(draftBean);
                        setResult(123);
                        SewerageTableActivity.this.finish();
                        return;
                    }

                    if (fromMyUpload) {
                        EventBus.getDefault().post(new RefreshMyUploadList());
                        if (null != MyApplication.doorBean && MyApplication.doorBean.getS_guid().equals(infoBean.getDoorplateAddressCode())) {
                            MyApplication.doorBean.setISTATUE("2");
                            SewerageTableActivity.this.finish();
//                            SewerageCompanyFragment.isSearch = false;
//                            SewerageActivity.isSearch = false;
                            SewerageTableActivity.this.finish();
                            return;
                        }
                    } else {
                        if (isCommit) {
                            EventBus.getDefault().post(new RefreshDataListEvent(MyApplication.refreshListType));
                            if (null != MyApplication.doorBean && MyApplication.doorBean.getS_guid().equals(infoBean.getDoorplateAddressCode())) {
                                MyApplication.doorBean.setISTATUE("2");
                            }
                        } else {

                        }
                    }
//                    //??????????????????????????????????????????????????????????????????????????????
//                    if (isfromQuryAddressMapFragmnet && isAddNewDoorNo) {
//                        Intent intent = new Intent(SewerageTableActivity.this, SewerageActivity.class);
//                        //????????????1???????????????????????????????????????
//                        if (isCommit) {
//                            MyApplication.doorBean.setISTATUE("2");
//                        }
//                        intent.putExtra("doorBean", MyApplication.doorBean);
//                        startActivity(intent);
//                        SewerageTableActivity.this.finish();
//                        return;
//                    }
//
//                    if (isAddNewDoorNo) {
//                        Intent intent = new Intent(SewerageTableActivity.this, SewerageActivity.class);
//                        MyApplication.doorBean.setISTATUE("2");
//                        intent.putExtra("doorBean", MyApplication.doorBean);
//                        startActivity(intent);
//                    }
                    SewerageTableActivity.this.finish();
//                    SewerageCompanyFragment.isSearch = true;
//                    SewerageActivity.isSearch = true;
                    Bundle bundle = new Bundle();
                    bundle.putString("refresh", isCommit ? "1" : "");
                    EventBus.getDefault().post(bundle);
                } else {
                    ToastUtil.shortToast(SewerageTableActivity.this, responseBody != null && !TextUtils.isEmpty(responseBody.getMessage()) ? responseBody.getMessage() : "????????????");
//                    showLoadedError("");
                }
            }
        });
    }


    private void showDialog() {
        showWaitDialog();

        //20???????????????
        countDownTimer = new CountDownTimer(SewerageTableActivity.TIMEOUT * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                if (progressDialog != null && !SewerageTableActivity.this.isFinishing()) {
                    progressDialog.setMessage("???????????????????????????   " + time + "s");
                    if (time == 1) {
                        if (progressDialog != null && progressDialog.isShowing() && !SewerageTableActivity.this.isFinishing()) {
                            progressDialog.dismiss();
                            ToastUtil.shortToast(SewerageTableActivity.this, "????????????");
                        }
                    }
                }
                if (time % 40 == 0) {
                    ToastUtil.longToast(SewerageTableActivity.this, "?????????????????????");
                }
            }

            @Override
            public void onFinish() {

            }
        };

        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    private void dismissWaitDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    private void showWaitDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(SewerageTableActivity.this);
            progressDialog.setMessage("????????????????????????");
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isShowing() && !this.isFinishing()) {
            progressDialog.show();
        }
    }

    private boolean check(SewerageInfoBean sewerageInfoBean) {
        SewerageTableOneFragment sewerageTableActivity = ((SewerageTableOneFragment) fragments.get(0));
        boolean isNull = false;
        try {
            if (LoginConstant.isCheckOut) {//????????????
                List<SewerageInfoBean.WellBeen> wellBeen = sewerageInfoBean.getWellBeen();//?????????
             /*   if (ListUtil.isEmpty(sewerageInfoBean.getPhotos0())&&!EDIT_MODE) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????????????????");
                    isNull = true;
                    return isNull;
                }*/

                if (ListUtil.isEmpty(sewerageTableActivity.getPhotoItem().getSelectedPhotos()) || sewerageTableActivity.getPhotoItem().getSelectedPhotos().size() < 2) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????????????????");
                    isNull = true;
                    return isNull;
                }

                if (TextUtils.isEmpty(sewerageInfoBean.getName())) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????");
                    isNull = true;
                    return isNull;
                }
                if (TextUtils.isEmpty(sewerageInfoBean.getTown())) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????????????????");
                    isNull = true;
                    return isNull;
                }
                if (TextUtils.isEmpty(sewerageInfoBean.getMph())) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????????????????");
                    isNull = true;
                    return isNull;
                }
              /*  if (TextUtils.isEmpty(sewerageInfoBean.getVolume())) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????");
                    isNull = true;
                    return isNull;
                }*/
                if(sewerageInfoBean.isSfgypsh()){
                    //????????????????????????
//                    if(TextUtils.isEmpty(sewerageInfoBean.getWaterNo())){
//                        ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????");
//                        isNull = true;
//                        return isNull;
//                    }

                    //????????????????????????  ?????????????????????
                    if ("0".equals(sewerageInfoBean.getHasCert5())){
                        ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????????????????????????????pdf??????");
                        isNull = true;
                        return isNull;
                    }
//
//
//                            || (ListUtil.isEmpty(sewerageTableActivity.getTakephotoSewerageoneFive().getSelectedPhotos())
//                            || sewerageTableActivity.getTakephotoSewerageoneFive().getSelectedPhotos().size() < 1)
//                            || sewerageTableActivity.getAgFilePicker().getSelectFiles().size()<1) {
//                        ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????????????????????????????pdf??????");
//                        isNull = true;
//                        return isNull;
//                    }

                    //????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if(!"?????????????????????".equals(sewerageInfoBean.getDischargerType1())){
                        ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }

                    //????????????????????????  ?????????????????????????????????????????????
                    if (ListUtil.isEmpty(wellBeen)) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    //???????????????????????????
                    if(TextUtils.isEmpty(sewerageInfoBean.getFac1()) || TextUtils.isEmpty(sewerageInfoBean.getFac2())
                            ||TextUtils.isEmpty(sewerageInfoBean.getFac3()) || TextUtils.isEmpty(sewerageInfoBean.getFac4())){
                        ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }

                }


                if (TextUtils.isEmpty(sewerageInfoBean.getAddr())) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????");
                    isNull = true;
                    return isNull;
                }





              /*  if (sewerageInfoBean.getHasCert1() == null) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????????????????");
                    isNull = true;
                    return isNull;
                }*/

                if ("1".equals(sewerageInfoBean.getHasCert1())) {
                    if (TextUtils.isEmpty(sewerageInfoBean.getCert1Code())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    if (ListUtil.isEmpty(sewerageTableActivity.getTakephotoSewerageoneOne().getSelectedPhotos())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                }
                if ("1".equals(sewerageInfoBean.getHasCert2())) {
                    if (ListUtil.isEmpty(sewerageTableActivity.getTakephotoSewerageoneTwo().getSelectedPhotos())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                }

                if ("1".equals(sewerageInfoBean.getHasCert7())) {
                    if (ListUtil.isEmpty(sewerageTableActivity.getTakephotoSewerageoneSeven().getSelectedPhotos())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                }
                if ("1".equals(sewerageInfoBean.getHasCert3())) {
                    if (TextUtils.isEmpty(sewerageInfoBean.getCert3Code())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }

                    if (TextUtils.isEmpty(sewerageInfoBean.getPsxkLx())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????");
                        isNull = true;
                        return isNull;
                    }

                    if (TextUtils.isEmpty(sewerageInfoBean.getPsxkFzrq())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    if (TextUtils.isEmpty(sewerageInfoBean.getPsxkJzrq())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????");
                        isNull = true;
                        return isNull;
                    }

                    if ("0".equals(sewerageInfoBean.getIsRight())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }

                    if (ListUtil.isEmpty(sewerageTableActivity.getTakephotoSewerageoneThree().getSelectedPhotos())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }

                }
                if ("1".equals(sewerageInfoBean.getHasCert4())) {

                    if (TextUtils.isEmpty(sewerageInfoBean.getCert4Code())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    if (ListUtil.isEmpty(sewerageTableActivity.getTakephotoSewerageoneFour().getSelectedPhotos())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                }


                if ("1".equals(sewerageInfoBean.getHasCert5())) {
                    if (ListUtil.isEmpty(sewerageTableActivity.getTakephotoSewerageoneFive().getSelectedPhotos())
                       && ListUtil.isEmpty(sewerageTableActivity.getAgFilePicker().getSelectFiles())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????????????????pdf??????");
                        isNull = true;
                        return isNull;
                    }
                }

                if (!ListUtil.isEmpty(sewerageInfoBean.getFiles())) {
                    for(FileBean fileBean:sewerageInfoBean.getFiles()){
                        if(!fileBean.getName().endsWith(".pdf")){
                            ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????pdf??????");
                            isNull = true;
                            return isNull;
                        }
                    }
                }
                //????????????????????????????????????
//                if (TextUtils.isEmpty(sewerageInfoBean.getDischargerType1())) {
//                    ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????");
//                    isNull = true;
//                    return isNull;
//                }
                if (TextUtils.isEmpty(sewerageInfoBean.getDischargerType2())) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????");
                    isNull = true;
                    return isNull;
                }
                if ("??????".equals(sewerageInfoBean.getDischargerType2()) || "?????????".equals(sewerageInfoBean.getDischargerType2()) && "".equals(((SewerageTableTwoFragment) fragments.get(1)).getEditOther().getText().toString().trim())) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "????????????-??????????????????");
                    isNull = true;
                    return isNull;
                }
                /*else if(!TextUtils.isEmpty(sewerageInfoBean.getDischargerType2())){
                //??????????????????,??????????????????????????????
                if(((SewerageTableTwoFragment) fragments.get(1)).checkOtherIsNull()){
                    ToastUtil.shortToast(SewerageTableActivity.this,"??????????????????????????????????????????????????????????????????");
                    isNull=true;
                }
            } */
//                if (!ListUtil.isEmpty(wellBeen)) {
//                    for (int i = 0; i < wellBeen.size(); i++) {
//                        if (TextUtils.isEmpty(wellBeen.get(i).getPipeType())) {
//                            ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????");
//                            isNull = true;
//                            return isNull;
//                        }
//                        if (TextUtils.isEmpty(wellBeen.get(i).getWellType())) {
//                            ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????");
//                            isNull = true;
//                            return isNull;
//                        }
//                        if (TextUtils.isEmpty(wellBeen.get(i).getWellDir())) {
//                            ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????");
//                            isNull = true;
//                            return isNull;
//                        }
//                    }
//
//                }

                if (!sewerageInfoBean.getDischargerType1().equals(SewerageTableTwoFragment.lifeWater)) {
                    //??????????????????????????????
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac1()) && TextUtils.isEmpty(sewerageInfoBean.getFac2())
                            && TextUtils.isEmpty(sewerageInfoBean.getFac3()) && TextUtils.isEmpty(sewerageInfoBean.getFac4())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????????????????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                }
             /*   if (sewerageInfoBean.getFac1() == null) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????????????????");
                    isNull = true;
                    return isNull;
                }*/
                if ("???".equals(sewerageInfoBean.getFac1())) {
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac1Cont())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac1Main())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac1Record())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                }


            /*    if (sewerageInfoBean.getFac2() == null) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????");
                    isNull = true;
                    return isNull;
                }*/
                if ("???".equals(sewerageInfoBean.getFac2())) {
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac2Cont())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac2Main())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac2Record())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                }
              /*  if (sewerageInfoBean.getFac3() == null) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "????????????????????????????????????");
                    isNull = true;
                    return isNull;
                }*/
                if ("???".equals(sewerageInfoBean.getFac3())) {
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac3Cont())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac3Main())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????????????????");
                        isNull = true;

                        return isNull;
                    }
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac3Record())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                }
               /* if (sewerageInfoBean.getFac4() == null) {
                    ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????????????????????????????");
                    isNull = true;
                    return isNull;
                }*/

                if ("???".equals(sewerageInfoBean.getFac4())) {
                    isNull = true;
                    return isNull;
                }
                if (sewerageInfoBean.getFac4() == null) {
                    isNull = false;
                    return isNull;
                }
                if (!"???".equals(sewerageInfoBean.getFac4())) {
                    if ("".equals(sewerageInfoBean.getFac4())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac4Cont())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "?????????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac4Main())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "???????????????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                    if (TextUtils.isEmpty(sewerageInfoBean.getFac4Record())) {
                        ToastUtil.shortToast(SewerageTableActivity.this, "??????????????????????????????????????????????????????");
                        isNull = true;
                        return isNull;
                    }
                }
            }
        } catch (Exception e) {
            dismissWaitDialog();
            Log.e("TAG", "Exception:" + e.toString());
        }

        return isNull;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EventBus.getDefault().post(new PhotoEvent(requestCode, resultCode, data));//??????????????????????????????   ????????????????????????
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    private void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (EDIT_MODE || pshAffairDetail == null) {
            DialogUtil.MessageBox(SewerageTableActivity.this, "??????", "?????????????????????????????????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EventBus.getDefault().post(new BackPressEvent());
                    finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            EventBus.getDefault().post(new BackPressEvent());
            finish();
        }
    }
}
