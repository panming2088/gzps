package com.augurit.agmobile.gzps.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.common.widget
 * @createTime 创建时间 ：17/11/8
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/8
 * @modifyMemo 修改备注：
 */

public class SpinnerTableItem extends RelativeLayout {


    private TextView tv_left;
    private EditText et_right;
    private AMSpinner spinner;
    private View tv_requiredTag;

    public SpinnerTableItem(Context context) {
        this(context, null);
    }

    public SpinnerTableItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_spinner_table_item, this);

        tv_left = (TextView) view.findViewById(R.id.tv_);
        tv_requiredTag = view.findViewById(R.id.tv_requiredTag);
        spinner = (AMSpinner) view.findViewById(R.id.spinner);
        et_right = (EditText) view.findViewById(R.id.et_);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpinnerTableItem);

        String textName = a.getString(R.styleable.SpinnerTableItem_spinnertextViewName);
        int resourceId = a.getResourceId(R.styleable.SpinnerTableItem_data, 0);

        if (resourceId != 0) {
            String[] array = getResources().getStringArray(resourceId);
            Map<String, Object> map = new HashMap<>();
            for (String str : array) {
                map.put(str, str);
            }
            setSpinnerData(map);
        }

        tv_left.setText(textName);
        a.recycle();
    }


    public void setTextViewName(String textViewName) {
        tv_left.setText(textViewName);
    }


    public void setSpinnerData(Map<String, Object> spinnerData) {
        spinner.setItemsMap(spinnerData);
    }

    public void setOnSpinnerChangeListener(AMSpinner.OnItemClickListener onSpinnerChangeListener) {
        spinner.setOnItemClickListener(onSpinnerChangeListener);
    }

    public void put(String key, Object value) {
        spinner.addItems(key, value);
    }


    public void selectItem(int position) {
        spinner.selectItem(position);
    }

    /**
     * 选中某个item
     *
     * @param value
     * @return 是否选中成功
     */
    public boolean selectItem(String value) {
        boolean b = spinner.containsKey(value);
        spinner.selectItem(value);
        return b;
    }

    public String getText(){
        return spinner.getText();
    }

    public void setEditable(boolean editable) {
        if (editable) {
            spinner.getBt_dropdown().setVisibility(VISIBLE);
        } else {
            spinner.getBt_dropdown().setVisibility(GONE);
        }
        spinner.setEditable(editable);
    }

    /**
     * 显示"*"标识
     */
    public void setRequereTag() {
        tv_requiredTag.setVisibility(VISIBLE);
    }
}
