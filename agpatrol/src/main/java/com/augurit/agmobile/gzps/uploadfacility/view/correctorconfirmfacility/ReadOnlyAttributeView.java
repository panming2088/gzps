package com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;

/**
 * 显示原有属性和纠错后属性的自定义视图；
 *
 * Created by xcl on 2017/11/13.
 */

public class ReadOnlyAttributeView {

    private Context mContext;
    private View root;
    private TextView tv_attribute_name;
    private EditText et_origin_value;
    private EditText et_correct_value;
    TextItemTableItem textItemTableItem;
    //已挂牌编号
    private MultiTakePhotoTableItem take_photo_item;

    public ReadOnlyAttributeView(Context context,String attributeName,String originValue,String correctValue){
        this.mContext = context;
        initView(attributeName,originValue,correctValue);
    }

    public ReadOnlyAttributeView(Context context,String attributeName,String correctValue){
        this.mContext = context;
        initView(attributeName,correctValue);
    }


    private void initView(String attributeName,String originValue,String correctValue) {
        textItemTableItem = new TextItemTableItem(mContext);
        textItemTableItem.setReadOnly();
        textItemTableItem.setTextViewName(attributeName);

        /*root = LayoutInflater.from(mContext).inflate(R.layout.view_readonly_attr_view2,null);
        tv_attribute_name = (TextView) root.findViewById(R.id.tv_attribute_name);
        et_origin_value = (EditText) root.findViewById(R.id.et_origin_value);
        et_correct_value = (EditText) root.findViewById(R.id.et_correct_value);

        tv_attribute_name.setText(attributeName.replace("修改",""));
        //et_origin_value.setText(originValue);
        if (correctValue == null){
            et_correct_value.setText("无");
        }else{
            et_correct_value.setText(correctValue);
        }*/

        if (TextUtils.isEmpty(correctValue)){
//            textItemTableItem.setText("无");
            textItemTableItem.setText("");
        }else{
            textItemTableItem.setText(correctValue);
        }

        if (!TextUtils.isEmpty(originValue) && !originValue.equals(correctValue)) {
            textItemTableItem.setEditTextColor(Color.RED);
        } else if (TextUtils.isEmpty(originValue) && !TextUtils.isEmpty(correctValue)){
            textItemTableItem.setEditTextColor(Color.RED);
        }
    }


    private void initView(String attributeName,String correctValue) {
        textItemTableItem = new TextItemTableItem(mContext);
        textItemTableItem.setReadOnly();
        textItemTableItem.setTextViewName(attributeName);



        if (TextUtils.isEmpty(correctValue)){
//            textItemTableItem.setText("无");
            textItemTableItem.setText("");
        }else{
            textItemTableItem.setText(correctValue);
        }
    }


    public void addTo(ViewGroup viewGroup){
        viewGroup.addView(textItemTableItem);
    }
}
