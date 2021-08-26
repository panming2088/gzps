package com.augurit.agmobile.gzps.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.am.cmpt.widget.popupview.util.DensityUtils;
import com.augurit.am.fw.utils.ListUtil;
import com.google.android.flexbox.FlexboxLayout;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xcl on 2017/11/20.
 */

public class MultiSelectTableItlem extends RelativeLayout {


    private EditText et_right;
    private TextView tv_left;
    private FlexboxLayout flexbox_layout;
    private Map<String,Object> selectedItems = null;

    public MultiSelectTableItlem(Context context) {
        this(context,null);
    }

    public MultiSelectTableItlem(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_multiselect_table_item, this);
        tv_left = (TextView) view.findViewById(R.id.tv_1);
        et_right = (EditText) view.findViewById(R.id.et_1);
        flexbox_layout = (FlexboxLayout) view.findViewById(R.id.flexbox_layout);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiSelectTableItlem);
        String textName = a.getString(R.styleable.MultiSelectTableItlem_multiselecttextViewName);

        tv_left.setText(textName);
        a.recycle();
    }



    public void setTextViewName(String textViewName){
        tv_left.setText(textViewName);
    }

    public String getTextViewName(){

        return tv_left.getText().toString();
    }

    /**
     *
     * @param choices 多选项
     * @param defaultCheckedItem 默认勾选的选项
     */
    public void setMultiChoice(Map<String,Object> choices,Map<String,Object> defaultCheckedItem){
        et_right.setVisibility(GONE);
        flexbox_layout.setVisibility(VISIBLE);

        selectedItems = new LinkedHashMap<>();
        float margin = DensityUtils.dp2px(getContext(), 60);
        float width = DensityUtils.getWidth(getContext());
        Set<Map.Entry<String, Object>> entries = choices.entrySet();
        for (final Map.Entry<String, Object> entry : entries){
            final CheckBox rb = (CheckBox) LayoutInflater.from(getContext()).inflate(R.layout.item_checkbox, null);
            rb.setText(entry.getKey());
            if (defaultCheckedItem != null && defaultCheckedItem.get(entry.getKey()) != null){
                  rb.setChecked(true);
                selectedItems.put(rb.getText().toString(),entry.getValue());
            }
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b && selectedItems.get(compoundButton.getText().toString()) == null){
                        selectedItems.put(compoundButton.getText().toString(),entry.getValue());
                    }else if (b && selectedItems.get(compoundButton.getText().toString()) != null){
                        compoundButton.setChecked(false);
                    }else if (!b){
                        selectedItems.remove(compoundButton.getText().toString());
                    }
                }
            });
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams((int) (width - margin) / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
            rb.setLayoutParams(lp);
            flexbox_layout.addView(rb);
        }
    }

    public void setReadOnly(List<String> choices){
        flexbox_layout.setVisibility(GONE);
        et_right.setVisibility(VISIBLE);
        if (!ListUtil.isEmpty(choices)){
            StringBuilder stringBuilder = new StringBuilder();
            for (String entry : choices){
                stringBuilder.append(entry);
                stringBuilder.append(",");
            }
            et_right.setText(stringBuilder.toString());
        }
    }



    public Map<String,Object> getSelectedItems(){
        return selectedItems;
    }
}
