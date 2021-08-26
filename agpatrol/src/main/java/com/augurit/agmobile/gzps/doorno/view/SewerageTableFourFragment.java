package com.augurit.agmobile.gzps.doorno.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.SewerageTableFourData;
import com.augurit.agmobile.gzps.common.util.SewerageBeanManger;
import com.augurit.agmobile.gzps.doorno.model.PSHAffairDetail;
import com.augurit.agmobile.gzps.uploadfacility.util.SetMaxLengthUtils;
import com.augurit.am.fw.utils.view.RadioGroupUtil;


/**
 * Created by xiaoyu on 2018/4/4.
 */

public class SewerageTableFourFragment extends MyBaseFragment {

    public onViewCreatedListener viewCreatedListener;
    private SewerageTableFourData sewerageTableFourData;
    private PSHAffairDetail.DataBean pshAffairDetailData;
    private boolean EDIT_MODE;
    private PSHAffairDetail pshAffairDetail;
    private boolean isAllowSaveLocalDraft;
    public static SewerageTableFourFragment newInstance(Bundle bundle) {
        SewerageTableFourFragment sewerageTableFourFragment = new SewerageTableFourFragment();
        sewerageTableFourFragment.setArguments(bundle);
        return sewerageTableFourFragment;
    }

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
        return R.layout.table_sewerage_four;
    }

    @Override
    protected void initData() {
        super.initData();
        sewerageTableFourData = getBind();
        // setCheckfalse();只读状态
        sewerageTableFourData.setCheckchangelistener(new MyOnCheckChangeListener());
        isAllowSaveLocalDraft = getArguments().getBoolean("isAllowSaveLocalDraft",false);
        pshAffairDetail = (PSHAffairDetail) getArguments().getSerializable("pshAffair");
        setOnEditListener();

        if (pshAffairDetail != null) {
            setView(pshAffairDetail);
            setFocusabledfalse();
        } else {
          /*  sewerageTableFourData.tvNotice.setVisibility(View.VISIBLE);
            sewerageTableFourData.tvNotice2.setVisibility(View.VISIBLE);
            sewerageTableFourData.tvNotice3.setVisibility(View.VISIBLE);
            sewerageTableFourData.tvNotice4.setVisibility(View.VISIBLE);*/
//            sewerageTableFourData.tvSewerageFourName.setRequireTag();
        }

        initTextView();
        if(isAllowSaveLocalDraft){
            setEditMode(true);
        }
    }

    private void setData() {
        if (sewerageTableFourData.rbClose.isChecked()) {
            SewerageBeanManger.getInstance().setFac1("无");
        } else {
            SewerageBeanManger.getInstance().setFac1("有");
        }

        if (sewerageTableFourData.rb2Close.isChecked()) {
            SewerageBeanManger.getInstance().setFac2("无");
        } else {
            SewerageBeanManger.getInstance().setFac2("有");
        }

        if (sewerageTableFourData.rb3Close.isChecked()) {
            SewerageBeanManger.getInstance().setFac3("无");
        } else {
            SewerageBeanManger.getInstance().setFac3("有");
        }

        if (sewerageTableFourData.rb4Close.isChecked()) {
            SewerageBeanManger.getInstance().setFac4(sewerageTableFourData.tvSewerageFourName.getText().trim());//有预处理设施的话，就设置为后面的输入框内容        }
        } else {
            SewerageBeanManger.getInstance().setFac4("否");
        }
    }

    private void initTextView() {
        //阅读模式下面不显示红色的*
        String starStr;
        if(EDIT_MODE||pshAffairDetail==null){
            starStr="*";
        }else{
            starStr="";
        }
        sewerageTableFourData.runOne.setText(Html.fromHtml("运行情况<font color='#FF0000'>"+starStr+"</font>"));
        sewerageTableFourData.protectOne.setText(Html.fromHtml("是否进行养护<font color='#FF0000'>"+starStr+"</font>"));
        sewerageTableFourData.protectRecordOne.setText(Html.fromHtml("养护记录及台账<font color='#FF0000'>"+starStr+"</font>"));

        sewerageTableFourData.runTwo.setText(Html.fromHtml("运行情况<font color='#FF0000'>"+starStr+"</font>"));
        sewerageTableFourData.protectTwo.setText(Html.fromHtml("是否进行养护<font color='#FF0000'>"+starStr+"</font>"));
        sewerageTableFourData.protectRecordTwo.setText(Html.fromHtml("养护记录及台账<font color='#FF0000'>"+starStr+"</font>"));

        sewerageTableFourData.runThree.setText(Html.fromHtml("运行情况<font color='#FF0000'>"+starStr+"</font>"));
        sewerageTableFourData.protectThree.setText(Html.fromHtml("是否进行养护<font color='#FF0000'>"+starStr+"</font>"));
        sewerageTableFourData.protectRecordThree.setText(Html.fromHtml("养护记录及台账<font color='#FF0000'>"+starStr+"</font>"));

        sewerageTableFourData.runFour.setText(Html.fromHtml("运行情况<font color='#FF0000'>"+starStr+"</font>"));
        sewerageTableFourData.protectFour.setText(Html.fromHtml("是否进行养护<font color='#FF0000'>"+starStr+"</font>"));
        sewerageTableFourData.protectRecordFour.setText(Html.fromHtml("养护记录及台账<font color='#FF0000'>"+starStr+"</font>"));

        sewerageTableFourData.tvSewerageFourName.setTextViewName(Html.fromHtml("设施<br>名称<font color='#FF0000'>"+starStr+"</font>"));
    }

    private void setOnEditListener() {
        SetMaxLengthUtils.setMaxLength( sewerageTableFourData.edtTableSeweragefourOne,SetMaxLengthUtils.MAX_LENGTH_50);
        SetMaxLengthUtils.setMaxLength( sewerageTableFourData.edtTableSeweragefourTwo,SetMaxLengthUtils.MAX_LENGTH_50);
        SetMaxLengthUtils.setMaxLength( sewerageTableFourData.edtTableSeweragefourThree,SetMaxLengthUtils.MAX_LENGTH_50);
        SetMaxLengthUtils.setMaxLength( sewerageTableFourData.edtTableSeweragefourFour,SetMaxLengthUtils.MAX_LENGTH_50);
        SetMaxLengthUtils.setMaxLength( sewerageTableFourData.tvSewerageFourName.getEt_right(),SetMaxLengthUtils.MAX_LENGTH_50);

        sewerageTableFourData.edtTableSeweragefourOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                SewerageBeanManger.getInstance().setFac1Cont(editable.toString());
            }
        });


        sewerageTableFourData.edtTableSeweragefourTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SewerageBeanManger.getInstance().setFac2Cont(editable.toString());
            }
        });

        sewerageTableFourData.edtTableSeweragefourThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SewerageBeanManger.getInstance().setFac3Cont(editable.toString());
            }
        });


        sewerageTableFourData.edtTableSeweragefourFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SewerageBeanManger.getInstance().setFac4Cont(editable.toString());
            }
        });


        sewerageTableFourData.tvSewerageFourName.getEt_right().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                SewerageBeanManger.getInstance().setFac4(TextUtils.isEmpty(editable.toString())?"无":editable.toString());
            }
        });
    }

    /**
     * 设置只读状态
     */
    private void setFocusabledfalse() {
        sewerageTableFourData.tvSewerageFourName.setEditable(EDIT_MODE);//设置设施名称不可点击
        if (EDIT_MODE) {
            RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgIsClose);
            RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rg2IsClose);
            RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rg3IsClose);
            RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rg4IsClose);

/*

            sewerageTableFourData.tvNotice.setVisibility(View.VISIBLE);
            sewerageTableFourData.tvNotice2.setVisibility(View.VISIBLE);
            sewerageTableFourData.tvNotice3.setVisibility(View.VISIBLE);
            sewerageTableFourData.tvNotice4.setVisibility(View.VISIBLE);
*/

//            sewerageTableFourData.tvSewerageFourName.setRequireTag();

        } else {
            RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgIsClose);
            RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rg2IsClose);
            RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rg3IsClose);
            RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rg4IsClose);

        }

    }

    private void setView(PSHAffairDetail pshAffairDetail) {
        pshAffairDetailData = pshAffairDetail.getData();
        if(pshAffairDetailData!=null){
            sewerageTableFourData.tvSewerageFourName.setText("无".equals(pshAffairDetailData.getFac4())?"":pshAffairDetailData.getFac4());
       /* sewerageTableFourData.rgIsClose.check("有".equals(pshAffairDetailData.getFac1()) ?
                sewerageTableFourData.rbOpen.getId() : sewerageTableFourData.rbClose.getId());*/

            if("有".equals(pshAffairDetailData.getFac1())){
                sewerageTableFourData.rgIsClose.check( sewerageTableFourData.rbOpen.getId());
            }else if("无".equals(pshAffairDetailData.getFac1())){
                sewerageTableFourData.rgIsClose.check( sewerageTableFourData.rbClose.getId());
            }


            if("有".equals(pshAffairDetailData.getFac2())){
                sewerageTableFourData.rg2IsClose.check(sewerageTableFourData.rb2Open.getId());
            }else if("无".equals(pshAffairDetailData.getFac2())){
                sewerageTableFourData.rg2IsClose.check(sewerageTableFourData.rb2Close.getId());
            }
        /*sewerageTableFourData.rg2IsClose.check("有".equals(pshAffairDetailData.getFac2()) ?
                sewerageTableFourData.rb2Open.getId() :-1);*/

      /*  if(TextUtils.isEmpty(pshAffairDetailData.getFac2())){
            sewerageTableFourData.rg2IsClose.clearCheck();
        }*/
      /*  sewerageTableFourData.rg3IsClose.check("有".equals(pshAffairDetailData.getFac3()) ?
                sewerageTableFourData.rb3Open.getId() : sewerageTableFourData.rb3Close.getId());*/


           /* sewerageTableFourData.rg3IsClose.check("有".equals(pshAffairDetailData.getFac3()) ?
                    sewerageTableFourData.rb3Open.getId() : -1);*/
            if("有".equals(pshAffairDetailData.getFac3())){
                sewerageTableFourData.rg3IsClose.check(sewerageTableFourData.rb3Open.getId());
            }else if("无".equals(pshAffairDetailData.getFac3())){
                sewerageTableFourData.rg3IsClose.check(sewerageTableFourData.rb3Close.getId());
            }
/*
        if(TextUtils.isEmpty(pshAffairDetailData.getFac3())){
            sewerageTableFourData.rg3IsClose.clearCheck();
        }*/

      /*  sewerageTableFourData.rg4IsClose.check(("无".equals(pshAffairDetailData.getFac4()) || TextUtils.isEmpty(pshAffairDetailData.getFac4())) ?
                sewerageTableFourData.rb4Close.getId() : sewerageTableFourData.rb4Open.getId());*/
       /* sewerageTableFourData.rg4IsClose.check(("无".equals(pshAffairDetailData.getFac4()) || TextUtils.isEmpty(pshAffairDetailData.getFac4())) ?
                sewerageTableFourData.rb4Close.getId() :-1);*/

            if(!"无".equals(pshAffairDetailData.getFac4()) && !TextUtils.isEmpty(pshAffairDetailData.getFac4())){
                sewerageTableFourData.rg4IsClose.check(  sewerageTableFourData.rb4Open.getId());
            }else if("无".equals(pshAffairDetailData.getFac4())){
                sewerageTableFourData.rg4IsClose.check(  sewerageTableFourData.rb4Close.getId());
            }
      /*  if(TextUtils.isEmpty(pshAffairDetailData.getFac4())){
            sewerageTableFourData.rg4IsClose.clearCheck();
        }*/
            sewerageTableFourData.llRb1.setVisibility("有".equals(pshAffairDetailData.getFac1()) ? View.VISIBLE : View.GONE);
            sewerageTableFourData.llRb2.setVisibility("有".equals(pshAffairDetailData.getFac2()) ? View.VISIBLE : View.GONE);
            sewerageTableFourData.llRb3.setVisibility("有".equals(pshAffairDetailData.getFac3()) ? View.VISIBLE : View.GONE);
            sewerageTableFourData.llRb4.setVisibility(("无".equals(pshAffairDetailData.getFac4()) ||TextUtils.isEmpty(pshAffairDetailData.getFac4())) ? View.GONE : View.VISIBLE);

            //草稿
            if(isAllowSaveLocalDraft){
                if(!TextUtils.isEmpty(pshAffairDetailData.getFac4Cont()) || !TextUtils.isEmpty(pshAffairDetailData.getFac4Main())
                        || !TextUtils.isEmpty(pshAffairDetailData.getFac4Record())){
                    sewerageTableFourData.rg4IsClose.check(  sewerageTableFourData.rb4Open.getId());
                    sewerageTableFourData.llRb4.setVisibility(View.VISIBLE);
                }
            }


            String fac1Cont = pshAffairDetailData.getFac1Cont();//运行情况
            if ("开启".equals(fac1Cont)) {
                sewerageTableFourData.rgTableSeweragefourOne.check(sewerageTableFourData.rbTableSeweragefourOneOne.getId());
            } else if ("关闭".equals(fac1Cont)) {
                sewerageTableFourData.rgTableSeweragefourOne.check(sewerageTableFourData.rbTableSeweragefourOneTwo.getId());
            } else if ("故障".equals(fac1Cont)) {
                sewerageTableFourData.rgTableSeweragefourOne.check(sewerageTableFourData.rbTableSeweragefourOneThree.getId());
            } else if (!TextUtils.isEmpty(fac1Cont)) {
                sewerageTableFourData.rgTableSeweragefourOne.check(sewerageTableFourData.rbTableSeweragefourOneFour.getId());
            } else {
                if (!EDIT_MODE) {
                    RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourOne);
                } else {
                    RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourOne);
                }
            }
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourOne, sewerageTableFourData.rgTableSeweragefourOne.getCheckedRadioButtonId());
            selectRadioButton(sewerageTableFourData.rgTableSeweragefourOne);

            String fac2Cont = pshAffairDetailData.getFac2Cont();//运行情况
            if ("开启".equals(fac2Cont)) {
                sewerageTableFourData.rgTableSeweragefourTwo.check(sewerageTableFourData.rbTableSeweragefourTwoOne.getId());
            } else if ("关闭".equals(fac2Cont)) {
                sewerageTableFourData.rgTableSeweragefourTwo.check(sewerageTableFourData.rbTableSeweragefourTwoTwo.getId());
            } else if ("故障".equals(fac2Cont)) {
                sewerageTableFourData.rgTableSeweragefourTwo.check(sewerageTableFourData.rbTableSeweragefourTwoThree.getId());
            } else if (!TextUtils.isEmpty(fac2Cont)) {
                sewerageTableFourData.rgTableSeweragefourTwo.check(sewerageTableFourData.rbTableSeweragefourTwoFour.getId());
            } else {
                if (!EDIT_MODE) {
                    RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourTwo);
                } else {
                    RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourTwo);
                }
            }
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourTwo, sewerageTableFourData.rgTableSeweragefourTwo.getCheckedRadioButtonId());
            selectRadioButton(sewerageTableFourData.rgTableSeweragefourTwo);

            String fac3Cont = pshAffairDetailData.getFac3Cont();//运行情况
            if ("开启".equals(fac3Cont)) {
                sewerageTableFourData.rgTableSeweragefourThree.check(sewerageTableFourData.rbTableSeweragefourThreeOne.getId());
            } else if ("关闭".equals(fac3Cont)) {
                sewerageTableFourData.rgTableSeweragefourThree.check(sewerageTableFourData.rbTableSeweragefourThreeTwo.getId());
            } else if ("故障".equals(fac3Cont)) {
                sewerageTableFourData.rgTableSeweragefourThree.check(sewerageTableFourData.rbTableSeweragefourThreeThree.getId());
            } else if (!TextUtils.isEmpty(fac3Cont)) {
                sewerageTableFourData.rgTableSeweragefourThree.check(sewerageTableFourData.rbTableSeweragefourThreeFour.getId());
            } else {
                if (!EDIT_MODE) {
                    RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourThree);
                } else {
                    RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourThree);
                }
            }
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourThree, sewerageTableFourData.rgTableSeweragefourThree.getCheckedRadioButtonId());
            selectRadioButton(sewerageTableFourData.rgTableSeweragefourThree);

            String fac4Cont = pshAffairDetailData.getFac4Cont();//运行情况
            if ("开启".equals(fac4Cont)) {
                sewerageTableFourData.rgTableSeweragefourFour.check(sewerageTableFourData.rbTableSeweragefourFourOne.getId());
            } else if ("关闭".equals(fac4Cont)) {
                sewerageTableFourData.rgTableSeweragefourFour.check(sewerageTableFourData.rbTableSeweragefourFourTwo.getId());
            } else if ("故障".equals(fac4Cont)) {
                sewerageTableFourData.rgTableSeweragefourFour.check(sewerageTableFourData.rbTableSeweragefourFourThree.getId());
            } else if (!TextUtils.isEmpty(fac4Cont)) {
                sewerageTableFourData.rgTableSeweragefourFour.check(sewerageTableFourData.rbTableSeweragefourFourFour.getId());
            } else {
                if (!EDIT_MODE) {
                    RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourFour);
                } else {
                    RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourFour);
                }
            }
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourFour, sewerageTableFourData.rgTableSeweragefourFour.getCheckedRadioButtonId());
            selectRadioButton(sewerageTableFourData.rgTableSeweragefourFour);

            setRadioYanghuGroupSelectChild(sewerageTableFourData.rgTableSeweragefourYanghuOne, "是".equals(pshAffairDetailData.getFac1Main()) ? 0 : 1);
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourYanghuOne, -1);

            setRadioYanghuGroupSelectChild(sewerageTableFourData.rgTableSeweragefourYanghuTwo, "是".equals(pshAffairDetailData.getFac2Main()) ? 0 : 1);
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourYanghuTwo, -1);

            setRadioYanghuGroupSelectChild(sewerageTableFourData.rgTableSeweragefourYanghuThree, "是".equals(pshAffairDetailData.getFac3Main()) ? 0 : 1);
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourYanghuThree, -1);

            setRadioYanghuGroupSelectChild(sewerageTableFourData.rgTableSeweragefourYanghuFour, "是".equals(pshAffairDetailData.getFac4Main()) ? 0 : 1);
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourYanghuFour, -1);

            setRadioYanghuJiLuGroupSelectChild(sewerageTableFourData.rgTableSeweragefourYanghujiluOne, "有".equals(pshAffairDetailData.getFac1Record()) ? 0 : 1);
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourYanghujiluOne, -1);

            setRadioYanghuJiLuGroupSelectChild(sewerageTableFourData.rgTableSeweragefourYanghujiluTwo, "有".equals(pshAffairDetailData.getFac2Record()) ? 0 : 1);
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourYanghujiluTwo, -1);

            setRadioYanghuJiLuGroupSelectChild(sewerageTableFourData.rgTableSeweragefourYanghujiluThree, "有".equals(pshAffairDetailData.getFac3Record()) ? 0 : 1);
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourYanghujiluThree, -1);

            setRadioYanghuJiLuGroupSelectChild(sewerageTableFourData.rgTableSeweragefourYanghujiluFour, "有".equals(pshAffairDetailData.getFac4Record()) ? 0 : 1);
            selectDefaultCheck(sewerageTableFourData.rgTableSeweragefourYanghujiluFour, -1);

        }


    }

    private void setRadioYanghuGroupSelectChild(RadioGroup rg, int pos) {
        if (rg != null) {
            for (int i = 0; i < rg.getChildCount(); i++) {
                RadioButton childAt = (RadioButton) rg.getChildAt(i);
                if (i == pos) {
                    childAt.setChecked(true);
                }
            }
        }
        setYangyu(rg);
    }

    private void setRadioYanghuJiLuGroupSelectChild(RadioGroup rg, int pos) {
        if (rg != null) {
            for (int i = 0; i < rg.getChildCount(); i++) {
                RadioButton childAt = (RadioButton) rg.getChildAt(i);
                if (i == pos) {
                    childAt.setChecked(true);
                }
            }
        }
        setCuringRecord(rg);
    }

    /**
     * 设置是否要打开edittext输入框，设置radiogroup中所有radiobutton不可点击
     *
     * @param radioGroup
     * @param radiobuttonId
     */
    private void selectDefaultCheck(RadioGroup radioGroup, int radiobuttonId) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
            if (rb.isChecked()) {
                if (radioGroup == sewerageTableFourData.rgTableSeweragefourOne) {
                    switch (radiobuttonId) {
                        case R.id.rb_table_seweragefour_one_one:
                        case R.id.rb_table_seweragefour_one_two:
                        case R.id.rb_table_seweragefour_one_three:
                            sewerageTableFourData.edtTableSeweragefourOne.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_seweragefour_one_four:
                            sewerageTableFourData.edtTableSeweragefourOne.setVisibility(View.VISIBLE);
                            sewerageTableFourData.edtTableSeweragefourOne.setEnabled(EDIT_MODE);
                            sewerageTableFourData.edtTableSeweragefourOne.setText(pshAffairDetailData.getFac1Cont());
                            break;
                    }
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourOne);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourOne);
                    }
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourTwo) {
                    switch (radiobuttonId) {
                        case R.id.rb_table_seweragefour_two_one:
                        case R.id.rb_table_seweragefour_two_two:
                        case R.id.rb_table_seweragefour_two_three:
                            sewerageTableFourData.edtTableSeweragefourTwo.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_seweragefour_two_four:
                            sewerageTableFourData.edtTableSeweragefourTwo.setVisibility(View.VISIBLE);
                            sewerageTableFourData.edtTableSeweragefourTwo.setEnabled(EDIT_MODE);
                            sewerageTableFourData.edtTableSeweragefourTwo.setText(pshAffairDetailData.getFac2Cont());
                            break;
                    }
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourTwo);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourTwo);
                    }
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourThree) {
                    switch (radiobuttonId) {
                        case R.id.rb_table_seweragefour_three_one:
                        case R.id.rb_table_seweragefour_three_two:
                        case R.id.rb_table_seweragefour_three_three:
                            sewerageTableFourData.edtTableSeweragefourThree.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_seweragefour_three_four:
                            sewerageTableFourData.edtTableSeweragefourThree.setVisibility(View.VISIBLE);
                            sewerageTableFourData.edtTableSeweragefourThree.setEnabled(EDIT_MODE);
                            sewerageTableFourData.edtTableSeweragefourThree.setText(pshAffairDetailData.getFac3Cont());
                            break;
                    }
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourThree);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourThree);
                    }
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourFour) {
                    switch (radiobuttonId) {
                        case R.id.rb_table_seweragefour_four_one:
                        case R.id.rb_table_seweragefour_four_two:
                        case R.id.rb_table_seweragefour_four_three:
                            sewerageTableFourData.edtTableSeweragefourFour.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_seweragefour_four_four:
                            sewerageTableFourData.edtTableSeweragefourFour.setVisibility(View.VISIBLE);
                            sewerageTableFourData.edtTableSeweragefourFour.setEnabled(EDIT_MODE);
                            sewerageTableFourData.edtTableSeweragefourFour.setText(pshAffairDetailData.getFac4Cont());
                            break;
                    }
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourFour);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourFour);
                    }
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghuOne) {
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghuOne);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghuOne);
                    }
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghuTwo) {
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghuTwo);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghuTwo);
                    }
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghuThree) {
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghuThree);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghuThree);
                    }
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghuFour) {
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghuFour);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghuFour);
                    }
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghujiluOne) {
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghujiluOne);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghujiluOne);
                    }
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghujiluTwo) {
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghujiluTwo);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghujiluTwo);
                    }
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghujiluThree) {
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghujiluThree);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghujiluThree);
                    }
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghujiluFour) {
                    if (!EDIT_MODE) {
                        RadioGroupUtil.disableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghujiluFour);
                    } else {
                        RadioGroupUtil.enableRadioGroup(sewerageTableFourData.rgTableSeweragefourYanghujiluFour);
                    }
                }
                break;
            }
        }
    }

    /**
     * 设置运行情况选中
     *
     * @param radioGroup
     */
    private void selectRadioButton(RadioGroup radioGroup) {
        RadioButton lastRB = (RadioButton) radioGroup.getChildAt(radioGroup.getChildCount() - 1);
        boolean isLastChecked = lastRB.isChecked();

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
            if (rb.isChecked()) {
                if (radioGroup == sewerageTableFourData.rgTableSeweragefourOne) {
                    SewerageBeanManger.getInstance().setFac1Cont(isLastChecked ? sewerageTableFourData.edtTableSeweragefourOne.getText().toString() : rb.getText().toString());
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourTwo) {
                    SewerageBeanManger.getInstance().setFac2Cont(isLastChecked ? sewerageTableFourData.edtTableSeweragefourTwo.getText().toString() : rb.getText().toString());
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourThree) {
                    SewerageBeanManger.getInstance().setFac3Cont(isLastChecked ? sewerageTableFourData.edtTableSeweragefourThree.getText().toString() : rb.getText().toString());
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourFour) {
                    SewerageBeanManger.getInstance().setFac4Cont(isLastChecked ? sewerageTableFourData.edtTableSeweragefourFour.getText().toString() : rb.getText().toString());
                }
                break;
            }
        }
    }

    /**
     * 设置是否进行养护
     */
    private void setYangyu(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
            if (rb.isChecked()) {
                if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghuOne) {
                    SewerageBeanManger.getInstance().setFac1Main(rb.getText().toString());
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghuTwo) {
                    SewerageBeanManger.getInstance().setFac2Main(rb.getText().toString());
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghuThree) {
                    SewerageBeanManger.getInstance().setFac3Main(rb.getText().toString());
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghuFour) {
                    SewerageBeanManger.getInstance().setFac4Main(rb.getText().toString());
                }
                break;
            }
        }
    }

    /**
     * 设置养护记录及台账
     */
    private void setCuringRecord(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
            if (rb.isChecked()) {
                if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghujiluOne) {
                    SewerageBeanManger.getInstance().setFac1Record(rb.getText().toString());
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghujiluTwo) {
                    SewerageBeanManger.getInstance().setFac2Record(rb.getText().toString());
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghujiluThree) {
                    SewerageBeanManger.getInstance().setFac3Record(rb.getText().toString());
                } else if (radioGroup == sewerageTableFourData.rgTableSeweragefourYanghujiluFour) {
                    SewerageBeanManger.getInstance().setFac4Record(rb.getText().toString());
                }
                break;
            }
        }
    }

    public void setEditMode(boolean isEdit) {
        if (isEdit != EDIT_MODE) {
            EDIT_MODE = isEdit;
            setFocusabledfalse();
            if (pshAffairDetail != null) {
                setView(pshAffairDetail);
                initTextView();
            }
        }
    }

    public interface onViewCreatedListener {
        void onViewCreated();
    }

    private class MyOnCheckChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (radioGroup.getId()) {
                case R.id.rg_table_seweragefour_one:
                    switch (i) {
                        case R.id.rb_table_seweragefour_one_one:
                        case R.id.rb_table_seweragefour_one_two:
                        case R.id.rb_table_seweragefour_one_three:
                            sewerageTableFourData.edtTableSeweragefourOne.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_seweragefour_one_four:
                            sewerageTableFourData.edtTableSeweragefourOne.setVisibility(View.VISIBLE);
                            break;
                    }
                    selectRadioButton(sewerageTableFourData.rgTableSeweragefourOne);
                    break;
                case R.id.rg_table_seweragefour_two:
                    switch (i) {
                        case R.id.rb_table_seweragefour_two_one:
                        case R.id.rb_table_seweragefour_two_two:
                        case R.id.rb_table_seweragefour_two_three:
                            sewerageTableFourData.edtTableSeweragefourTwo.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_seweragefour_two_four:
                            sewerageTableFourData.edtTableSeweragefourTwo.setVisibility(View.VISIBLE);
                            break;
                    }
                    selectRadioButton(sewerageTableFourData.rgTableSeweragefourTwo);
                    break;
                case R.id.rg_table_seweragefour_three:
                    switch (i) {
                        case R.id.rb_table_seweragefour_three_one:
                        case R.id.rb_table_seweragefour_three_two:
                        case R.id.rb_table_seweragefour_three_three:
                            sewerageTableFourData.edtTableSeweragefourThree.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_seweragefour_three_four:
                            sewerageTableFourData.edtTableSeweragefourThree.setVisibility(View.VISIBLE);
                            break;
                    }
                    selectRadioButton(sewerageTableFourData.rgTableSeweragefourThree);
                    break;
                case R.id.rg_table_seweragefour_four:
                    switch (i) {
                        case R.id.rb_table_seweragefour_four_one:
                        case R.id.rb_table_seweragefour_four_two:
                        case R.id.rb_table_seweragefour_four_three:
                            sewerageTableFourData.edtTableSeweragefourFour.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_seweragefour_four_four:
                            sewerageTableFourData.edtTableSeweragefourFour.setVisibility(View.VISIBLE);
                            break;
                    }
                    selectRadioButton(sewerageTableFourData.rgTableSeweragefourFour);
                    break;
                case R.id.rg_table_seweragefour_yanghu_one:
                    setYangyu(sewerageTableFourData.rgTableSeweragefourYanghuOne);
                    break;
                case R.id.rg_table_seweragefour_yanghu_two:
                    setYangyu(sewerageTableFourData.rgTableSeweragefourYanghuTwo);
                    break;
                case R.id.rg_table_seweragefour_yanghu_three:
                    setYangyu(sewerageTableFourData.rgTableSeweragefourYanghuThree);
                    break;
                case R.id.rg_table_seweragefour_yanghu_four:
                    setYangyu(sewerageTableFourData.rgTableSeweragefourYanghuFour);
                    break;
                case R.id.rg_table_seweragefour_yanghujilu_one:
                    setCuringRecord(sewerageTableFourData.rgTableSeweragefourYanghujiluOne);
                    break;
                case R.id.rg_table_seweragefour_yanghujilu_two:
                    setCuringRecord(sewerageTableFourData.rgTableSeweragefourYanghujiluTwo);
                    break;
                case R.id.rg_table_seweragefour_yanghujilu_three:
                    setCuringRecord(sewerageTableFourData.rgTableSeweragefourYanghujiluThree);
                    break;
                case R.id.rg_table_seweragefour_yanghujilu_four:
                    setCuringRecord(sewerageTableFourData.rgTableSeweragefourYanghujiluFour);
                    break;
                case R.id.rg_isClose:
                    switch (i) {
                        case R.id.rb_open:
                           SewerageBeanManger.getInstance().setFac1("有");
                            sewerageTableFourData.llRb1.setVisibility(View.VISIBLE);
                            break;
                        case R.id.rb_close:
                            sewerageTableFourData.llRb1.setVisibility(View.GONE);
                            SewerageBeanManger.getInstance().setFac1("无");
                            SewerageBeanManger.getInstance().setFac1Cont("");
                            SewerageBeanManger.getInstance().setFac1Main("");
                            SewerageBeanManger.getInstance().setFac1Record("");
                            break;
                    }
                case R.id.rg2_isClose:
                    switch (i) {
                        case R.id.rb2_open:
                            SewerageBeanManger.getInstance().setFac2("有");
                            sewerageTableFourData.llRb2.setVisibility(View.VISIBLE);
                            break;
                        case R.id.rb2_close:
                            sewerageTableFourData.llRb2.setVisibility(View.GONE);
                            SewerageBeanManger.getInstance().setFac2("无");
                            SewerageBeanManger.getInstance().setFac2Cont("");
                            SewerageBeanManger.getInstance().setFac2Main("");
                            SewerageBeanManger.getInstance().setFac2Record("");
                            break;
                    }
                case R.id.rg3_isClose:
                    switch (i) {
                        case R.id.rb3_open:
                            SewerageBeanManger.getInstance().setFac3("有");
                            sewerageTableFourData.llRb3.setVisibility(View.VISIBLE);
                            break;
                        case R.id.rb3_close:
                            SewerageBeanManger.getInstance().setFac3("无");
                            sewerageTableFourData.llRb3.setVisibility(View.GONE);
                            SewerageBeanManger.getInstance().setFac3Cont("");
                            SewerageBeanManger.getInstance().setFac3Main("");
                            SewerageBeanManger.getInstance().setFac3Record("");
                            break;
                    }
                case R.id.rg4_isClose:
                    switch (i) {
                        case R.id.rb4_open:
//                            SewerageBeanManger.getInstance().setFac4("有");//有预处理设施的话，就设置为后面的输入框内容
                          SewerageBeanManger.getInstance().setFac4(sewerageTableFourData.tvSewerageFourName.getText().trim());//有预处理设施的话，就设置为后面的输入框内容
                            sewerageTableFourData.llRb4.setVisibility(View.VISIBLE);
                            break;
                        case R.id.rb4_close:
                            sewerageTableFourData.llRb4.setVisibility(View.GONE);
                            SewerageBeanManger.getInstance().setFac4("无");
                            SewerageBeanManger.getInstance().setFac4Cont("");
                            SewerageBeanManger.getInstance().setFac4Main("");
                            SewerageBeanManger.getInstance().setFac4Record("");
                            break;
                    }
            }
        }
    }

}
