package com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.StringUtil;

import java.util.List;

/**
 * 显示原有属性和纠错后属性的自定义视图；
 *
 * Created by xcl on 2017/11/13.
 */

public class ReadOnlyGuaPaiNoView {
    private boolean fiveItemChecked = true;
    private Context mContext;
    private View mMenPaiView;
    private EditText et;
    private MultiTakePhotoTableItem takePhotoTableItem;
    private CheckBox  cb_yes;
    private CheckBox cb_no;
    private TextView tv_requiredTag;

    public ReadOnlyGuaPaiNoView(Context context, String attributekey, String attributeValue, List<Photo> wellPhotos) {
        this.mContext = context;
        initView(attributekey,attributeValue,wellPhotos);

    }
    public void setReadOnly(boolean isEnable){
        cb_yes.setEnabled(isEnable);
        cb_no.setEnabled(isEnable);
        takePhotoTableItem.setEnabled(isEnable);
        takePhotoTableItem.setAddPhotoEnable(isEnable);
        takePhotoTableItem.setPhotoNumShow(false,9);
        tv_requiredTag.setVisibility(View.GONE);
        et.setEnabled(isEnable);
    }

    private void initView(String attributekey, String attributeValue, List<Photo> wellPhotos) {
        mMenPaiView = LayoutInflater.from(mContext).inflate(R.layout.layout_attribute_five, null);
//        mMenPaiView = View.inflate(mContext, R.layout.layout_attribute_five, null);
        et = (EditText) mMenPaiView.findViewById(R.id.et_1);
        tv_requiredTag = (TextView) mMenPaiView.findViewById(R.id.tv_requiredTag);
        takePhotoTableItem = (MultiTakePhotoTableItem) mMenPaiView.findViewById(R.id.take_photo_well);
        if(wellPhotos!=null&&wellPhotos.size()>0){
            takePhotoTableItem.setSelectedPhotos(wellPhotos);
            takePhotoTableItem.setVisibility(View.VISIBLE);
        }
        cb_yes  = (CheckBox) mMenPaiView.findViewById(R.id.cb_yes);
        cb_no = (CheckBox) mMenPaiView.findViewById(R.id.cb_no);

        setReadOnly(false);

        cb_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fiveItemChecked = true;
                    et.setEnabled(true);
                    takePhotoTableItem.setVisibility(View.VISIBLE);
                    takePhotoTableItem.setPhotoNumShow(true,9);
                } else {
                    fiveItemChecked = false;
                    et.setEnabled(false);
                    takePhotoTableItem.setVisibility(View.GONE);
                    takePhotoTableItem.setSelectedPhotos(null);
                }
                cb_no.setChecked(!isChecked);
            }
        });
        cb_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fiveItemChecked = false;
                    et.setEnabled(false);
                    takePhotoTableItem.setVisibility(View.GONE);
                    takePhotoTableItem.setSelectedPhotos(null);
                } else {
                    fiveItemChecked = true;
                    et.setEnabled(true);
                    takePhotoTableItem.setVisibility(View.VISIBLE);
                    takePhotoTableItem.setPhotoNumShow(true,9);
                }
                cb_yes.setChecked(!isChecked);
            }
        });

        if ("无".equals(attributeValue)
                || StringUtil.isEmpty(attributeValue)) {
            cb_no.setChecked(true);
        } else {
            et.setText(attributeValue);
            cb_yes.setChecked(true);
        }
    }

    public void addTo(ViewGroup viewGroup){
        viewGroup.addView(mMenPaiView);
    }
}
