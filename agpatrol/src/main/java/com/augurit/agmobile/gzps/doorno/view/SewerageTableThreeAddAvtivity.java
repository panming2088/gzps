package com.augurit.agmobile.gzps.doorno.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.augurit.agmobile.gzps.BR;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.SewerageTableThreeClickData;
import com.augurit.agmobile.gzps.doorno.adapter.BaseRecyleAdapter;
import com.augurit.agmobile.gzps.doorno.adapter.SewerageTableTwoAdapter;
import com.augurit.agmobile.gzps.doorno.model.ItemSewerageItemInfo;
import com.augurit.agmobile.gzps.doorno.model.MyApplication;
import com.augurit.agmobile.gzps.doorno.model.PSHAffairDetail;
import com.augurit.agmobile.gzps.doorno.model.SelectComponentFinishEvent;
import com.augurit.agmobile.gzps.doorno.model.SewerageInfoBean;
import com.augurit.agmobile.gzps.doorno.model.WellMessageEvent;
import com.augurit.agmobile.gzps.uploadfacility.util.SetMaxLengthUtils;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.RadioGroupUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Point;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiaoyu on 2018/4/8.
 */

public class SewerageTableThreeAddAvtivity extends BaseDataBindingActivity {
    String[] selecet = null;
    private Component mSelComponent;
    private GridLayoutManager myGridLayoutManager;
    private GridLayoutManager myGridLayoutManager1;
    private GridLayoutManager myGridLayoutManager2;
    private SewerageTableTwoAdapter baseRecyleAdapter;
    private SewerageTableTwoAdapter baseRecyleAdapter1;
    private SewerageTableTwoAdapter baseRecyleAdapter2;
    private List<ItemSewerageItemInfo> userInfo;
    private List<ItemSewerageItemInfo> userInfo3 = new ArrayList<>();
    private List<ItemSewerageItemInfo> userInfo2 = new ArrayList<>();
    private String[] name = {"???????????????", "???????????????", "???????????????", "???????????????", "??????"};
    private String[] name1 = {"??????", "??????", "????????????", "??????"};
    private String[] name2 = {"??????", "??????", "????????????", "??????"};
    private String[] name3 = {"??????", "??????", "??????????????????", "??????"};
    private String[] name5 = {"??????", "??????", "??????????????????", "????????????", "??????"};
    private String[] name4 = {"?????????", "?????????", "????????????", "??????"};
    private boolean EDIT_WELL = false;//?????????
    private boolean EDIT_MODE = false;//????????????
    private boolean fromAffair = false;
    private PSHAffairDetail mPSHAffairDetail;
    private boolean isOther = false;
    private boolean isPipeTypeOther = false;//???????????????

    private boolean isWellTypeOther = false;//???????????????
    private SewerageInfoBean.WellBeen mfromBean;
    private SewerageTableThreeClickData sewerageTableThreeClickData;
    private boolean isAllowSaveLocalDraft;
    private SewerageInfoBean draftBean;

    @Override
    public int initview() {
        return R.layout.activity_seweragetablethreeclick;
    }

    @Override
    public void initdatabinding() {
        setDataTitle("???????????????");
        sewerageTableThreeClickData = getBind();
        setRightIsVisiable(View.GONE);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mfromBean = (SewerageInfoBean.WellBeen) intent.getSerializableExtra("wellInfo");
        fromAffair = intent.getBooleanExtra("fromAffair", false);
        EDIT_MODE = intent.getBooleanExtra("editmode", false);
        mPSHAffairDetail = (PSHAffairDetail) intent.getSerializableExtra("pshAffair");
        isAllowSaveLocalDraft = intent.getBooleanExtra("isAllowSaveLocalDraft", false);
        draftBean = (SewerageInfoBean) intent.getSerializableExtra("draftBean");
        EDIT_WELL = null == mfromBean;
//        if(fromAffair && mPSHAffairDetail!=null){
//            //????????????
//            sewerageTableThreeClickData.btnSeweragetablethreeDel.setVisibility(View.GONE);
//            sewerageTableThreeClickData.btnSeweragetablethreeSave.setVisibility(View.GONE);
//        }else if(isAllowSaveLocalDraft){
//            //????????????????????????
//            if(mfromBean!=null){
//                sewerageTableThreeClickData.btnSeweragetablethreeDel.setVisibility(View.VISIBLE);
//                sewerageTableThreeClickData.btnSeweragetablethreeSave.setVisibility(View.VISIBLE);
//            }else{
//                sewerageTableThreeClickData.btnSeweragetablethreeDel.setVisibility(View.GONE);
//                sewerageTableThreeClickData.btnSeweragetablethreeSave.setVisibility(View.VISIBLE);
//            }
//        }else if(!fromAffair && mPSHAffairDetail!=null){
//            //????????????????????????
//            if(mfromBean!=null){
//                sewerageTableThreeClickData.btnSeweragetablethreeDel.setVisibility(View.GONE);
//                sewerageTableThreeClickData.btnSeweragetablethreeSave.setVisibility(View.GONE);
//            }else{
//                sewerageTableThreeClickData.btnSeweragetablethreeDel.setVisibility(View.GONE);
//                sewerageTableThreeClickData.btnSeweragetablethreeSave.setVisibility(View.VISIBLE);
//            }
//        }
        userInfo = new ArrayList<>();
        setList();
        setList2();
        EventBus.getDefault().register(this);

        sewerageTableThreeClickData.rgSewersgetablethreeclick.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.direction_item1:
                    case R.id.direction_item:
                        sewerageTableThreeClickData.editDirectionOther.setEnabled(false);
                        break;
                    case R.id.direction_item2:
                        sewerageTableThreeClickData.editDirectionOther.setEnabled(true);
                        break;
                }
            }
        });

        myGridLayoutManager = new GridLayoutManager(this, 2);
        sewerageTableThreeClickData.rvSeweragetablethreeclick1.addItemDecoration(new SpacesItemDecoration(10));
        sewerageTableThreeClickData.rvSeweragetablethreeclick1.setLayoutManager(myGridLayoutManager);
        Map<Integer, Integer> map = new HashMap<>();
        map.put(R.layout.gv_item_sewersgetable_two, BR.tableinfo);
        //String[] array = (String[])list.toArray(new String[size]);
        baseRecyleAdapter = new SewerageTableTwoAdapter(userInfo, map, -1);
        sewerageTableThreeClickData.rvSeweragetablethreeclick1.setAdapter(baseRecyleAdapter);

//        setView();

        myGridLayoutManager2 = new GridLayoutManager(this, 3);
        sewerageTableThreeClickData.rvSeweragetablethreeclick3.addItemDecoration(new SpacesItemDecoration(10));
        sewerageTableThreeClickData.rvSeweragetablethreeclick3.setLayoutManager(myGridLayoutManager2);
        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(R.layout.gv_item_sewersgetable_two, BR.tableinfo);
        baseRecyleAdapter2 = new SewerageTableTwoAdapter(userInfo3, map2, -1);
        sewerageTableThreeClickData.rvSeweragetablethreeclick3.setAdapter(baseRecyleAdapter2);
        baseRecyleAdapter2.setOnRecycleitemOnClic(new BaseRecyleAdapter.OnRecycleitemOnClic() {
            @Override
            public void onItemClic(View view, int position) {
                if (fromAffair && !EDIT_MODE) {
                    return;
                }
                baseRecyleAdapter2.onItemClic(view, position);
                if ("??????".equals(userInfo3.get(position).getName())) {
                    isPipeTypeOther = true;
                } else {
                    isPipeTypeOther = false;
                }
                if (isPipeTypeOther) {
                    sewerageTableThreeClickData.edtPipeTypeOther.setVisibility(View.VISIBLE);
                } else {
                    sewerageTableThreeClickData.edtPipeTypeOther.setVisibility(View.GONE);
                }
            }
        });
        selecet = name1;
        baseRecyleAdapter.setOnRecycleitemOnClic(new BaseRecyleAdapter.OnRecycleitemOnClic() {
            @Override
            public void onItemClic(View view, int position) {
                if (fromAffair && !EDIT_MODE) {
                    return;
                }


                sewerageTableThreeClickData.edtOtherTwo.setVisibility(View.GONE);
                isOther = false;
                baseRecyleAdapter.onItemClic(view, position);
                switch (position) {
                    case 0:
                        selecet = name1;
                        break;
                    case 1:
                        selecet = name2;
                        break;
                    case 2:
                        selecet = name3;
                        break;
                    case 3:
                    case 4:
                        selecet = name5;
                        break;
                }
                Set<Integer> selectPosition = baseRecyleAdapter.getSelectPosition();
                Iterator<Integer> iterator = selectPosition.iterator();
                while (iterator.hasNext()) {
                    int i = (Integer) iterator.next();
                    if (selecet[i].equals("??????")) {
                        isWellTypeOther = true;
                        break;
                    } else {
                        isWellTypeOther = false;
                    }
                }
                if (isWellTypeOther) {
                    sewerageTableThreeClickData.editWellTypeOther.setVisibility(View.VISIBLE);
                } else {
                    sewerageTableThreeClickData.editWellTypeOther.setVisibility(View.GONE);
                }
                if ("??????".equals(userInfo.get(position).getName())) {
                    isWellTypeOther = true;
                } else {
                    isWellTypeOther = false;
                }
                if (isWellTypeOther) {
                    sewerageTableThreeClickData.editWellTypeOther.setVisibility(View.VISIBLE);
                } else {
                    sewerageTableThreeClickData.editWellTypeOther.setVisibility(View.GONE);
                }
                if (userInfo2.contains(selecet[0]))
                    return;
                setList1(selecet);
                baseRecyleAdapter1.clearsele();
                baseRecyleAdapter1.notifyDataSetChanged();

            }
        });


        setList1(name1);
        myGridLayoutManager1 = new GridLayoutManager(this, 3);
        sewerageTableThreeClickData.rvSeweragetablethreeclick2.addItemDecoration(new SpacesItemDecoration(10));
        sewerageTableThreeClickData.rvSeweragetablethreeclick2.setLayoutManager(myGridLayoutManager1);
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(R.layout.gv_item_sewersgetable_two, BR.tableinfo);
        baseRecyleAdapter1 = new SewerageTableTwoAdapter(userInfo2, map1);
        baseRecyleAdapter1.clearsele();
        baseRecyleAdapter1.setIsadds(true);//??????true?????????false?????????
        sewerageTableThreeClickData.rvSeweragetablethreeclick2.setAdapter(baseRecyleAdapter1);

        baseRecyleAdapter1.setOnRecycleitemOnClic(new BaseRecyleAdapter.OnRecycleitemOnClic() {
            @Override
            public void onItemClic(View view, int position) {
                if (fromAffair && !EDIT_MODE) {
                    return;
                }
                baseRecyleAdapter1.onItemClic(view, position);
                Set<Integer> selectPosition = baseRecyleAdapter1.getSelectPosition();
                Iterator<Integer> iterator = selectPosition.iterator();
                while (iterator.hasNext()) {
                    int i = (Integer) iterator.next();
                    if (selecet[i].equals("??????")) {
                        isOther = true;
                        break;
                    } else {
                        isOther = false;
                    }
                }
                if (isOther) {
                    sewerageTableThreeClickData.edtOtherTwo.setVisibility(View.VISIBLE);
                } else {
                    sewerageTableThreeClickData.edtOtherTwo.setVisibility(View.GONE);
                }
            }
        });

        sewerageTableThreeClickData.rlSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SewerageTableThreeAddAvtivity.this, SewerageSelectWellActivity.class);
                Bundle bundle = new Bundle();
                if (mSelComponent != null) {
                    intent.putExtra("geometry", mSelComponent.getGraphic().getGeometry());
                }
                intent.putExtra("editmode", !(fromAffair && !EDIT_WELL && !EDIT_MODE));
//                intent.putExtra("editwell", EDIT_MODE || !EDIT_WELL);
                if (mfromBean != null) {
                    if (mSelComponent != null) {
                        mfromBean.setWellId(mSelComponent.getObjectId() + "");
                        mfromBean.setX(mSelComponent.getGraphic().getAttributes().get("X") + "");
                        mfromBean.setY(mSelComponent.getGraphic().getAttributes().get("Y") + "");
                    }
                    bundle.putSerializable("wellInfo", mfromBean);
                } else {
                    bundle.putBoolean("addWell", true);
                }
                if (MyApplication.doorBean != null) {
                    bundle.putSerializable("doorBean", MyApplication.doorBean);
                }
                if (mPSHAffairDetail != null) {
                    bundle.putSerializable("pshAffair", mPSHAffairDetail);
                }
//                DoorNOBean doorBean  = new DoorNOBean();
//                doorBean.setX(mPSHAffairDetail.getData().getX());
//                doorBean.setY(mPSHAffairDetail.getData().getY());
//                bundle.putSerializable("doorBean", doorBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        sewerageTableThreeClickData.btnSeweragetablethreeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        sewerageTableThreeClickData.btnSeweragetablethreeDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mfromBean != null) {
                    if (isAllowSaveLocalDraft && draftBean != null) {
                        AMDatabase.getInstance().deleteWhere(SewerageInfoBean.WellBeen.class, "wiId", mfromBean.getWiId() + "");
                    }
                    EventBus.getDefault().post(mfromBean);
                    SewerageTableThreeAddAvtivity.this.onBackPressed();
                } else {
                    ToastUtil.shortToast(SewerageTableThreeAddAvtivity.this, "????????????????????????");
                }
            }
        });
        baseInfoData.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        setEditModeState();
    }

//    private void setView() {
//
//        sewerageTableThreeClickData.directionItem.setChecked(true);//???????????????????????????
//        sewerageTableThreeClickData.checkItem1.setChecked(true);
//    }

    private void save() {
        SewerageInfoBean.WellBeen wellBeen = new SewerageInfoBean.WellBeen();
        Set<Integer> set1 = baseRecyleAdapter2.getSelectPosition();
        Iterator it1 = set1.iterator();
        int index1 = 0;

        String edtPipeTypeOther = sewerageTableThreeClickData.edtPipeTypeOther.getText().toString().trim();
        String pipeType = "";
        while (it1.hasNext()) {
            index1 = (int) it1.next();
        }
        if (index1 != -1) {
            if (name4[index1].equals("??????")) {
                pipeType = pipeType + edtPipeTypeOther + ",";
            } else {
                pipeType = pipeType + name4[index1] + ",";
            }
            if (!TextUtils.isEmpty(pipeType)) {
                wellBeen.setPipeType(pipeType.substring(0, pipeType.length() - 1));
            }
        }

        String editOther = sewerageTableThreeClickData.edtOtherTwo.getText().toString().trim();
        Set<Integer> set2 = baseRecyleAdapter1.getSelectPosition();
        Iterator it2 = set2.iterator();
        int index2 = 0;
        StringBuffer sb1 = new StringBuffer();
        while (it2.hasNext()) {
            index2 = (int) it2.next();
            if (selecet[index2].equals("??????")) {
                isOther = true;
                sb1.append("?????????" + editOther + ",");
            } else {
                sb1.append(selecet[index2] + ",");
                isOther = false;
            }
        }
        if (!TextUtils.isEmpty(sb1.toString())) {
            wellBeen.setWellPro(sb1.toString().substring(0, sb1.toString().length() - 1));
        }


        Set<Integer> set3 = baseRecyleAdapter.getSelectPosition();
        Iterator it3 = set3.iterator();
        String str = "";
        int index3 = 0;
        while (it3.hasNext()) {
            index3 = (int) it3.next();
        }
        if (index3 != -1) {
            if ("??????".equals(name[index3])) {
                str = str + sewerageTableThreeClickData.editWellTypeOther.getText().toString().trim() + ",";
            } else {
                str = str + name[index3] + ",";
            }


            if (!TextUtils.isEmpty(str)) {
                wellBeen.setWellType(str.substring(0, str.length() - 1));
            }
        }


        //????????????
        if (sewerageTableThreeClickData.directionItem.isChecked()) {
            wellBeen.setWellDir("????????????");
        } else if (sewerageTableThreeClickData.directionItem1.isChecked()) {
            wellBeen.setWellDir("????????????");
        } else if (sewerageTableThreeClickData.directionItem2.isChecked()) {
            //??????
            if (!TextUtils.isEmpty(sewerageTableThreeClickData.editDirectionOther.getText().toString())) {
                wellBeen.setWellDir(sewerageTableThreeClickData.editDirectionOther.getText().toString().trim());
            }
        }

//        sewerageTableThreeClickData.directionItem.isChecked();


        if (mSelComponent != null) {
            wellBeen.setWellId(mSelComponent.getObjectId() + "");
        } else if (!EDIT_WELL) {
            wellBeen.setWellId(mfromBean.getWellId());
        } else if (isAllowSaveLocalDraft && draftBean != null) {
            wellBeen.setWellId(mfromBean.getWellId());
        }

        String editText1 = sewerageTableThreeClickData.editItem1.getText().toString();
        String editText2 = sewerageTableThreeClickData.editItem2.getText().toString();

        StringBuffer sb = new StringBuffer();
        sb.append(sewerageTableThreeClickData.checkItem1.isChecked() ? "????????????#" : "#")

                .append(sewerageTableThreeClickData.checkItem3.isChecked() ? "??????#" : "#")

                .append(sewerageTableThreeClickData.checkItem2.isChecked() ? !TextUtils.isEmpty(editText1) ? editText1 + "#" : "#" : "#")

                .append(sewerageTableThreeClickData.checkItem4.isChecked() ? !TextUtils.isEmpty(editText2) ? editText2 + "#" : "#" : "#");
        wellBeen.setDrainPro(sb.toString());
        if (!TextUtils.isEmpty(wellBeen.getDrainPro())) {
            String pros[] = wellBeen.getDrainPro().split("#");
            String newDrainPro = "";
            for (int j = 0; j < pros.length; j++) {
                if (!TextUtils.isEmpty(pros[j])) {
                    if (j != pros.length - 1) {
                        newDrainPro = newDrainPro + pros[j] + "???";
                    } else {
                        newDrainPro = newDrainPro + pros[j];
                    }
                }
            }
            wellBeen.setNewDrainPro(newDrainPro);
        }

        if (EDIT_WELL) {
            wellBeen.setOnlyid(System.currentTimeMillis() + "");
        } else if (isAllowSaveLocalDraft && draftBean != null) {
            wellBeen.setId(null);
        } else if (isAllowSaveLocalDraft) {
            wellBeen.setId(null);
        } else {
            wellBeen.setId(mfromBean.getId());
        }
        //??????????????????????????????????????????
        if (TextUtils.isEmpty(sewerageTableThreeClickData.edtWellLocation.getText())) {
            ToastUtil.shortToast(SewerageTableThreeAddAvtivity.this, "???????????????????????????");
            return;
        }

        //?????????????????????????????????????????????????????????????????????????????????
//        if (index1 != -1) {
//            if (name4[index1].equals("??????") && TextUtils.isEmpty(sewerageTableThreeClickData.edtPipeTypeOther.getText().toString().trim())) {
//                ToastUtil.shortToast(SewerageTableThreeAddAvtivity.this, "?????????-??????????????????");
//                return;
//            }
//        }

//        if (TextUtils.isEmpty(wellBeen.getPipeType())) {
//            ToastUtil.shortToast(SewerageTableThreeAddAvtivity.this, "?????????????????????");
//            return;
//        }
//        if (index3 != -1) {
//            if (name[index3].equals("??????") && TextUtils.isEmpty(sewerageTableThreeClickData.editWellTypeOther.getText().toString().trim())) {
//                ToastUtil.shortToast(SewerageTableThreeAddAvtivity.this, "?????????-??????????????????");
//                return;
//            }
//        }
//        if (TextUtils.isEmpty(wellBeen.getWellType())) {
//            ToastUtil.shortToast(SewerageTableThreeAddAvtivity.this, "?????????????????????");
//            return;
//        }
        if (index2 != -1) {
            if (isOther && TextUtils.isEmpty(sewerageTableThreeClickData.edtOtherTwo.getText().toString().trim())) {
                ToastUtil.shortToast(SewerageTableThreeAddAvtivity.this, "???????????????-??????????????????");
                return;
            }
        }

        if (sewerageTableThreeClickData.directionItem2.isChecked() && TextUtils.isEmpty(sewerageTableThreeClickData.editDirectionOther.getText().toString().trim())) {
            ToastUtil.shortToast(SewerageTableThreeAddAvtivity.this, "????????????-??????????????????");
            return;
        }
//        if (TextUtils.isEmpty(wellBeen.getWellDir())) {
//            ToastUtil.shortToast(SewerageTableThreeAddAvtivity.this, "????????????????????????");
//            return;
//        }

        if (sewerageTableThreeClickData.checkItem2.isChecked() && TextUtils.isEmpty(sewerageTableThreeClickData.editItem1.getText().toString().trim())) {
            ToastUtil.shortToast(SewerageTableThreeAddAvtivity.this, "??????????????????????????????");
            return;
        }
        if (sewerageTableThreeClickData.checkItem4.isChecked() && TextUtils.isEmpty(sewerageTableThreeClickData.editItem2.getText().toString().trim())) {
            ToastUtil.shortToast(SewerageTableThreeAddAvtivity.this, "??????????????????");
            return;
        }


        if (isAllowSaveLocalDraft && mPSHAffairDetail != null) {
            //TODO ????????????????????????
            wellBeen.setLxId(mfromBean.getLxId());
            wellBeen.setWiId(mfromBean.getWiId());

            if (mSelComponent != null) {
                Point point = GeometryUtil.getGeometryCenter(mSelComponent.getGraphic().getGeometry());
                wellBeen.setX(point.getX() + "");
                wellBeen.setY(point.getY() + "");
            } else {
                wellBeen.setX(mfromBean.getX() + "");
                wellBeen.setY(mfromBean.getY() + "");
            }
            EventBus.getDefault().post(new WellMessageEvent(wellBeen, 1));
            SewerageTableThreeAddAvtivity.this.onBackPressed();
//                wellBeen.setProblem_id(draftBean.getDbid());
        } else if (EDIT_MODE && fromAffair && mPSHAffairDetail != null) {
            //??????????????? mSelComponent????????????
            wellBeen.setLxId(mfromBean.getLxId());
            EventBus.getDefault().post(new WellMessageEvent(wellBeen, 1));
            SewerageTableThreeAddAvtivity.this.onBackPressed();
        } else {
            if (mSelComponent != null) {
                Point point = GeometryUtil.getGeometryCenter(mSelComponent.getGraphic().getGeometry());
                wellBeen.setX(point.getX() + "");
                wellBeen.setY(point.getY() + "");
            }
            wellBeen.setLxId(mfromBean != null ? mfromBean.getLxId() : "");
            EventBus.getDefault().post(new WellMessageEvent(wellBeen, EDIT_WELL ? 0 : 1));
            SewerageTableThreeAddAvtivity.this.onBackPressed();
        }
    }


    private void setEditModeState() {
        sewerageTableThreeClickData.editDirectionOther.setEnabled(false);
        if (!EDIT_MODE && !fromAffair) {
//            sewerageTableThreeClickData.tvNotice.setVisibility(View.VISIBLE);
//            sewerageTableThreeClickData.tvNotice2.setVisibility(View.VISIBLE);
//            sewerageTableThreeClickData.tvNotice3.setVisibility(View.VISIBLE);
            sewerageTableThreeClickData.tvNotice4.setVisibility(View.VISIBLE);
        }
        if (!fromAffair && EDIT_WELL && EDIT_MODE) {
//            sewerageTableThreeClickData.tvNotice.setVisibility(View.VISIBLE);
//            sewerageTableThreeClickData.tvNotice2.setVisibility(View.VISIBLE);
//            sewerageTableThreeClickData.tvNotice3.setVisibility(View.VISIBLE);
            sewerageTableThreeClickData.tvNotice4.setVisibility(View.VISIBLE);
            sewerageTableThreeClickData.editDirectionOther.setEnabled(true);
        }
        if (EDIT_MODE && fromAffair && !EDIT_WELL) {
//            sewerageTableThreeClickData.tvNotice.setVisibility(View.VISIBLE);
//            sewerageTableThreeClickData.tvNotice2.setVisibility(View.VISIBLE);
//            sewerageTableThreeClickData.tvNotice3.setVisibility(View.VISIBLE);
            sewerageTableThreeClickData.tvNotice4.setVisibility(View.VISIBLE);
            sewerageTableThreeClickData.editDirectionOther.setEnabled(true);
        }

        if (fromAffair && !EDIT_WELL && !EDIT_MODE) {
            //????????????????????????
            sewerageTableThreeClickData.rvSeweragetablethreeclick1.setEnabled(false);
            sewerageTableThreeClickData.rvSeweragetablethreeclick2.setEnabled(false);
            sewerageTableThreeClickData.rvSeweragetablethreeclick3.setEnabled(false);
            RadioGroupUtil.disableRadioGroup(sewerageTableThreeClickData.rgSewersgetablethreeclick);

            sewerageTableThreeClickData.checkItem1.setEnabled(false);
            sewerageTableThreeClickData.checkItem2.setEnabled(false);
            sewerageTableThreeClickData.checkItem3.setEnabled(false);
            sewerageTableThreeClickData.checkItem4.setEnabled(false);
            sewerageTableThreeClickData.editItem1.setEnabled(false);
            sewerageTableThreeClickData.editItem2.setEnabled(false);
            sewerageTableThreeClickData.edtWellLocation.setEnabled(false);
            sewerageTableThreeClickData.edtOtherTwo.setEnabled(false);

            sewerageTableThreeClickData.editDirectionOther.setEnabled(false);
            sewerageTableThreeClickData.editWellTypeOther.setEnabled(false);
            sewerageTableThreeClickData.edtPipeTypeOther.setEnabled(false);

            sewerageTableThreeClickData.llBottom.setVisibility(View.GONE);
        }

        if (EDIT_MODE || !EDIT_WELL) {
            sewerageTableThreeClickData.btnSeweragetablethreeDel.setVisibility(View.VISIBLE);

        }


        if (mfromBean != null && !TextUtils.isEmpty(mfromBean.getWellId())) {
//            sewerageTableThreeClickData.edtWellLocation.setText(mfromBean.getPipeType() + "(" + mfromBean.getWellId() + ")");
            sewerageTableThreeClickData.edtWellLocation.setText("??????" + "(" + mfromBean.getWellId() + ")");
        }
        if (!EDIT_WELL) {
            for (int i = 0; i < name4.length; i++) {
                if (name4[i].equals(mfromBean.getPipeType())) {
                    HashSet<Integer> set = new HashSet<>();
                    set.add(i);
                    baseRecyleAdapter2.setSelectPosition(set);
                    baseRecyleAdapter2.notifyDataSetChanged();
                }
            }
            int indexWellType = -1;
            for (int i = 0; i < name.length; i++) {
                if (name[i].equals(mfromBean.getWellType())) {
                    HashSet<Integer> set = new HashSet<>();
                    set.add(i);
                    baseRecyleAdapter.setSelectPosition(set);
                    baseRecyleAdapter.notifyDataSetChanged();
                    indexWellType = i;
                }
            }
            selecet = indexWellType == -1 ? name5 : indexWellType == 0 ? name1 : indexWellType == 1 ? name2 : name3;
            setList1(selecet);
            //?????????
            HashSet<Integer> pipeTypeSet = new HashSet<>();
            for (int i = 0; i < name4.length; i++) {
                if (!TextUtils.isEmpty(mfromBean.getPipeType())) {
                    if (!mfromBean.getPipeType().contains(name4[i])) {
                        continue;
                    } else {
                        pipeTypeSet.add(i);
                        isPipeTypeOther = true;
                    }
                }
            }
            if (!isPipeTypeOther) {
                pipeTypeSet.add(name4.length - 1);
                sewerageTableThreeClickData.edtPipeTypeOther.setVisibility(View.VISIBLE);
                sewerageTableThreeClickData.edtPipeTypeOther.setText(mfromBean.getPipeType());
            }

            baseRecyleAdapter2.setSelectPosition(pipeTypeSet);
            baseRecyleAdapter2.notifyDataSetChanged();

            //?????????
            HashSet<Integer> wellTypeSet = new HashSet<>();
            //    boolean match=false;//????????????
            for (int i = 0; i < name.length; i++) {
                if (!TextUtils.isEmpty(mfromBean.getWellType())) {
                    if (!mfromBean.getWellType().equals(name[i])) {
                        continue;//?????????????????????
                    } else {//mfromBean.getWellType()??????name[]????????????
                        wellTypeSet.add(i);
                        isWellTypeOther = true;//??????????????????????????????
                        // match=true;//???????????????
                    }
                }
            }
            if (!isWellTypeOther) {
                //???????????????
                wellTypeSet.add(name.length - 1);
                sewerageTableThreeClickData.editWellTypeOther.setVisibility(View.VISIBLE);
                sewerageTableThreeClickData.editWellTypeOther.setText(mfromBean.getWellType());
            }
            baseRecyleAdapter.setSelectPosition(wellTypeSet);
            baseRecyleAdapter.notifyDataSetChanged();


            //??????????????????
            HashSet<Integer> wellProSet = new HashSet<>();
            for (int i = 0; i < selecet.length; i++) {
                if (!TextUtils.isEmpty(mfromBean.getWellPro()) && mfromBean.getWellPro().contains(selecet[i])) {
                    if (selecet[i].equals("??????")) {
                        isOther = true;
                    }
                    wellProSet.add(i);
                }
            }
            if (isOther) {
                String array[] = mfromBean.getWellPro().split(",");
                for (String str : array) {
                    if (str.contains("?????????")) {
                        sewerageTableThreeClickData.edtOtherTwo.setVisibility(View.VISIBLE);
                        sewerageTableThreeClickData.edtOtherTwo.setText(str.substring(3));
                    }
                }
            } else {
                sewerageTableThreeClickData.edtOtherTwo.setVisibility(View.GONE);
            }
            baseRecyleAdapter1.setSelectPosition(wellProSet);
            baseRecyleAdapter1.notifyDataSetChanged();

            //????????????
            if (sewerageTableThreeClickData.directionItem.getText().equals(mfromBean.getWellDir())) {
                sewerageTableThreeClickData.directionItem.setChecked(true);
                sewerageTableThreeClickData.directionItem1.setChecked(false);
                sewerageTableThreeClickData.directionItem2.setChecked(false);
            } else if (!TextUtils.isEmpty(mfromBean.getWellDir()) && !mfromBean.getWellDir().contains(sewerageTableThreeClickData.directionItem.getText())
                    && !mfromBean.getWellDir().contains(sewerageTableThreeClickData.directionItem1.getText())) {
                //??????
                sewerageTableThreeClickData.editDirectionOther.setEnabled(false);
                sewerageTableThreeClickData.editDirectionOther.setText(mfromBean.getWellDir());
                sewerageTableThreeClickData.directionItem.setChecked(false);
                sewerageTableThreeClickData.directionItem1.setChecked(false);
                sewerageTableThreeClickData.directionItem2.setChecked(true);
            } else if (sewerageTableThreeClickData.directionItem1.getText().equals(mfromBean.getWellDir())) {
                sewerageTableThreeClickData.directionItem1.setChecked(true);
                sewerageTableThreeClickData.directionItem.setChecked(false);
                sewerageTableThreeClickData.directionItem2.setChecked(false);
            } else {
                sewerageTableThreeClickData.directionItem.setChecked(false);
                sewerageTableThreeClickData.directionItem1.setChecked(false);
                sewerageTableThreeClickData.directionItem2.setChecked(false);
            }
            //???????????????
            sewerageTableThreeClickData.checkItem1.setChecked(mfromBean.getDrainPro().contains("????????????"));
            sewerageTableThreeClickData.checkItem3.setChecked(mfromBean.getDrainPro().contains("??????"));
            String drainPro = mfromBean.getDrainPro();

            if (!TextUtils.isEmpty(drainPro)) {
                String pro2 = drainPro.substring(drainPro.indexOf("#") + 1);//??????????????????
                pro2 = pro2.substring(pro2.indexOf("#") + 1);//??????????????????
                String pro4 = pro2;
                pro2 = pro2.substring(0, pro2.indexOf("#"));
                pro4 = pro4.substring(pro4.indexOf("#") + 1, pro4.lastIndexOf("#"));
//                String pro2 = mfromBean.getDrainPro().substring(drainPro.indexOf("#"));
//                pro2 = pro2.substring(drainPro.indexOf("#"));
//                pro2 = pro2.substring(0, drainPro.indexOf("#"));
//                String pro4 = pro2;
//                pro2 = pro2.substring(0, pro2.indexOf("#"));
                if (!TextUtils.isEmpty(pro2)) {
                    sewerageTableThreeClickData.checkItem2.setChecked(true);
                    sewerageTableThreeClickData.editItem1.setText(pro2);
                }
//                pro4 = pro4.substring(pro4.indexOf("#")+1, pro4.lastIndexOf("#"));
                if (!TextUtils.isEmpty(pro4)) {
                    sewerageTableThreeClickData.checkItem4.setChecked(true);
                    sewerageTableThreeClickData.editItem2.setText(pro4);
                }
            }
        }


        //???????????????????????????????????????????????????????????????????????????????????????checkbox?????????????????????
        sewerageTableThreeClickData.editItem2.setEnabled(false);
        sewerageTableThreeClickData.editItem1.setEnabled(false);
        sewerageTableThreeClickData.editDirectionOther.setEnabled(false);

        sewerageTableThreeClickData.checkItem2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sewerageTableThreeClickData.editItem1.setEnabled(b);
            }
        });

        sewerageTableThreeClickData.checkItem4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sewerageTableThreeClickData.editItem2.setEnabled(b);
            }
        });


        SetMaxLengthUtils.setMaxLength(sewerageTableThreeClickData.editItem1, SetMaxLengthUtils.MAX_LENGTH_30);
        SetMaxLengthUtils.setMaxLength(sewerageTableThreeClickData.editItem2, SetMaxLengthUtils.MAX_LENGTH_30);
        SetMaxLengthUtils.setMaxLength(sewerageTableThreeClickData.edtOtherTwo, SetMaxLengthUtils.MAX_LENGTH_30);
        SetMaxLengthUtils.setMaxLength(sewerageTableThreeClickData.editDirectionOther, SetMaxLengthUtils.MAX_LENGTH_30);
        SetMaxLengthUtils.setMaxLength(sewerageTableThreeClickData.editWellTypeOther, 5);
        SetMaxLengthUtils.setMaxLength(sewerageTableThreeClickData.edtPipeTypeOther, SetMaxLengthUtils.MAX_LENGTH_30);


    }

    private void setList() {
        ItemSewerageItemInfo userInfo;
        int length = name.length;
        for (int i = 0; i < length; i++) {
            userInfo = new ItemSewerageItemInfo();
            userInfo.setName(name[i]);
            this.userInfo.add(userInfo);
        }
    }

    private void setList2() {
        ItemSewerageItemInfo userInfo;
        int length = name4.length;

        for (int i = 0; i < length; i++) {
            userInfo = new ItemSewerageItemInfo();
            userInfo.setName(name4[i]);
            this.userInfo3.add(userInfo);
        }
    }

    private void setList1(String[] childname) {
        userInfo2.clear();
        ItemSewerageItemInfo userInfo;
        int length = childname.length;

        for (int i = 0; i < length; i++) {
            userInfo = new ItemSewerageItemInfo();
            userInfo.setName(childname[i]);
            this.userInfo2.add(userInfo);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param selectComponentFinishEvent
     */
    @Subscribe
    public void onReceivedFinishedSelectEvent2(SelectComponentFinishEvent selectComponentFinishEvent) {
        Component component = selectComponentFinishEvent.getFindResult();
        mSelComponent = component;
        String name = String.valueOf(component.getGraphic().getAttributes().get(component.getDisplayFieldName()));
        String usid = String.valueOf(component.getGraphic().getAttributes().get("OBJECTID"));
        String title = StringUtil.getNotNullString(name, "") + "(" + usid + ")";
        sewerageTableThreeClickData.edtWellLocation.setText(title);
    }
}
