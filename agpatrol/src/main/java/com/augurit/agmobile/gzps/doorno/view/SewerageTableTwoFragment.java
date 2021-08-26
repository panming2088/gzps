package com.augurit.agmobile.gzps.doorno.view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.augurit.agmobile.gzps.BR;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.TableSewerageTwoData;
import com.augurit.agmobile.gzps.common.util.SewerageBeanManger;
import com.augurit.agmobile.gzps.doorno.adapter.BaseRecyleAdapter;
import com.augurit.agmobile.gzps.doorno.adapter.SewerageTableTwoAdapter;
import com.augurit.agmobile.gzps.doorno.model.ItemSewerageItemInfo;
import com.augurit.agmobile.gzps.doorno.model.PSHAffairDetail;
import com.augurit.agmobile.gzps.uploadfacility.util.SetMaxLengthUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by xiaoyu on 2018/4/4.
 */

public class SewerageTableTwoFragment extends MyBaseFragment {
    private TableSewerageTwoData tableSewerageTwoData;
    private List<ItemSewerageItemInfo> userInfo;
    private List<ItemSewerageItemInfo> userInfo2 = new ArrayList<>();
    private SewerageTableTwoAdapter baseRecyleAdapter;
    private GridLayoutManager myGridLayoutManager;
    private GridLayoutManager myGridLayoutManager1;
    SewerageTableTwoAdapter sewerageTableTwoChildAdapter;
    public static final  String lifeWater = "生活排污类";
    private String[] name = {lifeWater , "餐饮排污类", "沉淀物排污类", "有毒有害排污类"};
    private String[] name1 = {"机关企事业单位", "学校", "商场", "居民住宅","其他"};
    private String[] name2 = {"餐饮店", "农家乐", "酒店", "大型食堂", "其他"};
    private String[] name3 = {"洗车、修车档", "沙场", "建筑工地", "养殖场","农贸市场", "垃圾(收集)转运站","其他"};
    private String[] name4 = {"化工", "印染", "电镀、小五金", "医疗", "洗涤","食品加工","制衣、皮革加工","行业管理排水户", "其他"};
    String[] selecet = name1;
    private PSHAffairDetail pshAffairDetail;
    private boolean isAllowSaveLocalDraft;
    public static SewerageTableTwoFragment newInstance(Bundle bundle) {
        SewerageTableTwoFragment sewerageTableTwoFragment = new SewerageTableTwoFragment();
        sewerageTableTwoFragment.setArguments(bundle);
        return sewerageTableTwoFragment;
    }

    boolean EDIT_MODE;//设置true不可编辑，设置false可编辑

    @Override
    public int initview() {
        return R.layout.table_sewerage_two;
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

    private void setView(PSHAffairDetail pshAffairDetail) {

        if (pshAffairDetail == null) {
            return;
        }

        //不选中大类别默认是餐饮类别，此处要重置大类别
        for (int i = 0; i < name.length; i++) {
            if (!TextUtils.isEmpty(pshAffairDetail.getData().getDischargerType1())
            && name[i].contains(pshAffairDetail.getData().getDischargerType1())) {
                selecet = i==0?name1:i==1?name2:i==2?name3:name4;
                break;
            }
        }
        if(!EDIT_MODE) {
            tableSewerageTwoData.edtOtherTwo.setEnabled(false);
            tableSewerageTwoData.tvNotice.setVisibility(View.GONE);
            tableSewerageTwoData.tvNotice2.setVisibility(View.GONE);
        }else{
            tableSewerageTwoData.edtOtherTwo.setEnabled(true);
            tableSewerageTwoData.tvNotice.setVisibility(View.VISIBLE);
            tableSewerageTwoData.tvNotice2.setVisibility(View.VISIBLE);
        }

        PSHAffairDetail.DataBean data = pshAffairDetail.getData();
        if(data!=null){
            String dischargerType1 = data.getDischargerType1();
            String dischargerType2 = data.getDischargerType2();
            SewerageBeanManger.getInstance().setDischargerType1(dischargerType1);
            SewerageBeanManger.getInstance().setDischargerType2(dischargerType2);
            int bigTypePos = -1;
            for (int i = 0; i < name.length; i++) {
                if (!TextUtils.isEmpty(dischargerType1) && name[i].contains(dischargerType1)) {
                    bigTypePos = i;
                    break;
                }
            }

            baseRecyleAdapter.setCurSelectPos(bigTypePos);

            int smallTypePos = -1;
            String[] smallTypename = null;
            switch (bigTypePos) {
                case 0:
                    smallTypename = name1;
                    break;
                case 1:
                    smallTypename = name2;
                    break;
                case 2:
                    smallTypename = name3;
                    break;
                case 3:
                    smallTypename = name4;
                    break;
            }

            if (smallTypename != null) {
                for (int i = 0; i < smallTypename.length; i++) {
                    if (!TextUtils.isEmpty(dischargerType2) && dischargerType2.contains(smallTypename[i])) {
                        smallTypePos = i;
                        break;
                    }

                    if (i == smallTypename.length - 1 && !TextUtils.isEmpty(dischargerType2)) {
                        smallTypePos = smallTypename.length - 1;
                    }
                }

                setList1(smallTypename);
                if(!TextUtils.isEmpty(dischargerType2) &&dischargerType2.contains("其他：")){
                    String other = dischargerType2.substring(3);
                    tableSewerageTwoData.edtOtherTwo.setVisibility(View.VISIBLE);
                    tableSewerageTwoData.edtOtherTwo.setText(other);
                }
                sewerageTableTwoChildAdapter.clearsele();
                sewerageTableTwoChildAdapter.setCurSelectPos(smallTypePos);
            }

        }



    }

    public EditText getEditOther(){
        return tableSewerageTwoData.edtOtherTwo;
    }

    public void setEditMode(boolean isEdit) {
        if (isEdit != EDIT_MODE) {
            EDIT_MODE = isEdit;

            setView(pshAffairDetail);
        }
    }


    @Override
    protected void initData() {
        super.initData();

        pshAffairDetail = (PSHAffairDetail) getArguments().getSerializable("pshAffair");
        isAllowSaveLocalDraft = getArguments().getBoolean("isAllowSaveLocalDraft",false);
        tableSewerageTwoData = getBind();
        userInfo = new ArrayList<>();
        setList();
        myGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        tableSewerageTwoData.rvTable1.addItemDecoration(new SpacesItemDecoration(10));
        tableSewerageTwoData.rvTable1.setLayoutManager(myGridLayoutManager);
        Map<Integer, Integer> map = new HashMap<>();
        map.put(R.layout.gv_item_sewersgetable_two, BR.tableinfo);

        baseRecyleAdapter = new SewerageTableTwoAdapter(userInfo, map,-1);

        tableSewerageTwoData.rvTable1.setAdapter(baseRecyleAdapter);
        baseRecyleAdapter.setCurSelectPos(0);


        baseRecyleAdapter.setOnRecycleitemOnClic(new BaseRecyleAdapter.OnRecycleitemOnClic() {
            @Override
            public void onItemClic(View view, int position) {
                if (!EDIT_MODE && pshAffairDetail != null) {
                    return;
                }
                tableSewerageTwoData.edtOtherTwo.setVisibility(View.GONE);
                baseRecyleAdapter.onItemClic(view, position);
                selecet = null;
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
                        selecet = name4;
                        break;
                }
                if (userInfo2.contains(selecet[0]))
                    return;

                setList1(selecet);
                sewerageTableTwoChildAdapter.clearsele();
                sewerageTableTwoChildAdapter.notifyDataSetChanged();

                SewerageBeanManger.getInstance().setDischargerType1(name[position]);
                SewerageBeanManger.getInstance().setDischargerType2("");


            }
        });

//        setList1(name1);
        myGridLayoutManager1 = new GridLayoutManager(getActivity(), 3);
        tableSewerageTwoData.rvTable2.addItemDecoration(new SpacesItemDecoration(10));
        tableSewerageTwoData.rvTable2.setLayoutManager(myGridLayoutManager1);
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(R.layout.gv_item_sewersgetable_two, BR.tableinfo);

        tableSewerageTwoData.edtOtherTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString().trim();
                SewerageBeanManger.getInstance().setDischargerType2("其他："+text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //走到这调用清除，即默认不选中小类
        selecet =  name1;
        setList1(selecet);
        sewerageTableTwoChildAdapter = new SewerageTableTwoAdapter(userInfo2, map1);
        sewerageTableTwoChildAdapter.clearsele();
        SewerageBeanManger.getInstance().setDischargerType1(name[0]);
        // sewerageTableTwoChildAdapter.setIsadds(false);//设置true多选，false为单选
        tableSewerageTwoData.rvTable2.setAdapter(sewerageTableTwoChildAdapter);
        sewerageTableTwoChildAdapter.setOnRecycleitemOnClic(new BaseRecyleAdapter.OnRecycleitemOnClic() {
            @Override
            public void onItemClic(View view, int position) {
                if (!EDIT_MODE && pshAffairDetail != null) {
                    return;
                }
                sewerageTableTwoChildAdapter.onItemClic(view, position);
                if(selecet[position].equals("其他")){
                    tableSewerageTwoData.edtOtherTwo.setVisibility(View.VISIBLE);
                    SewerageBeanManger.getInstance().setDischargerType2(selecet[position]+tableSewerageTwoData.edtOtherTwo.getText().toString().trim());
                }else{
                    tableSewerageTwoData.edtOtherTwo.setVisibility(View.GONE);
                }
                SewerageBeanManger.getInstance().setDischargerType2(selecet[position]);
            }
        });

        if (pshAffairDetail != null) {
            setView(pshAffairDetail);
        }else{
            tableSewerageTwoData.tvNotice.setVisibility(View.VISIBLE);
            tableSewerageTwoData.tvNotice2.setVisibility(View.VISIBLE);
        }
        SetMaxLengthUtils.setMaxLength(tableSewerageTwoData.edtOtherTwo,50);
        if(isAllowSaveLocalDraft){
            setEditMode(true);
        }
    }
   /* public void setDefault(){
        SewerageBeanManger.getInstance().setDischargerType1(name[0]);
    }*/

    /*   public boolean checkOtherIsNull(){
           boolean isNull=false;
           if(TextUtils.isEmpty(tableSewerageTwoData.edtOtherTwo.getText().toString())){
               isNull=true;
           }
           return isNull;
       }
   */
    private void setList() {
        for (int i = 0; i < 4; i++) {
            ItemSewerageItemInfo userInfo = new ItemSewerageItemInfo();
            userInfo.setName(name[i]);
            this.userInfo.add(userInfo);
        }
    }


    private void setList1(String[] childname) {
        userInfo2.clear();
        for (int i = 0; i < childname.length; i++) {
            ItemSewerageItemInfo userInfo = new ItemSewerageItemInfo();
            userInfo.setName(childname[i]);
            this.userInfo2.add(userInfo);
        }
    }

}

