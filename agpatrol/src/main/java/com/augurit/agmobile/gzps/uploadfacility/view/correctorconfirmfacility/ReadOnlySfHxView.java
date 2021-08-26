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
import com.augurit.am.fw.utils.StringUtil;

/**
 * 显示原有属性和纠错后属性的自定义视图；
 *
 * Created by xcl on 2017/11/13.
 */

public class ReadOnlySfHxView {
    private Context mContext;
    private EditText et;
    private CheckBox  cb_yes;
    private  View fCzwsclView;
    private TextView tvName;

    public ReadOnlySfHxView(Context context, String attributekey, String oldValue, String value) {
        this.mContext = context;
        initView(attributekey,oldValue,value);

    }


    public void setReadOnly(boolean isEnable){
        cb_yes.setEnabled(isEnable);
        et.setEnabled(isEnable);
    }

    private void initView(String attributekey, String oldValue, String value) {
        fCzwsclView = LayoutInflater.from(mContext).inflate(R.layout.layout_attribute_sfhx, null);
        et = (EditText) fCzwsclView.findViewById(R.id.et_sfczwscl);
        cb_yes = (CheckBox) fCzwsclView.findViewById(R.id.cb_yes);
        tvName = (TextView) fCzwsclView.findViewById(R.id.tv_name);
        tvName.setText(attributekey);
        setReadOnly(false);
        cb_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et.setEnabled(false);
                } else {
                    et.setEnabled(false);
                }
            }
        });
        if ("0".equals(value)
                || StringUtil.isEmpty(value)) {
            cb_yes.setChecked(false);
        } else {
            cb_yes.setChecked(true);
        }
    }

    public void addTo(ViewGroup viewGroup){
        viewGroup.addView(fCzwsclView);
    }
}
