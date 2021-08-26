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
import com.augurit.agmobile.gzps.common.widget.PhoneItemTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.am.fw.utils.StringUtil;

/**
 * 显示原有属性和纠错后属性的自定义视图；
 * <p>
 * Created by xcl on 2017/11/13.
 */

public class ReadOnlyGJJDView {
    private boolean fiveItemChecked = true;
    private Context mContext;
    private View mMenPaiView;
    private CheckBox cb_yes;
    private CheckBox cb_no;
    private TextView tv_requiredTag;
    private TextItemTableItem mItem_jdbh;
    private TextItemTableItem mItem_zrr;
    private PhoneItemTableItem mItem_lxdh;
    private View mLl_container;

    public ReadOnlyGJJDView(Context context, String sfgjjd, String bh, String zrr, String lxdh) {
        this.mContext = context;
        initView(sfgjjd, bh, zrr, lxdh);

    }

    public void setReadOnly(boolean isEnable) {
        cb_yes.setEnabled(isEnable);
        cb_no.setEnabled(isEnable);
        tv_requiredTag.setVisibility(View.GONE);
        mItem_jdbh.setReadOnly();
        mItem_zrr.setReadOnly();
        mItem_lxdh.setReadOnly();
    }

    private void initView(String sfgjjd, String bh, String zrr, String lxdh) {
        mMenPaiView = LayoutInflater.from(mContext).inflate(R.layout.layout_attribute_gjjd, null);
        mLl_container = mMenPaiView.findViewById(R.id.ll_container);
        tv_requiredTag = (TextView) mMenPaiView.findViewById(R.id.tv_requiredTag);
        mItem_jdbh = (TextItemTableItem) mMenPaiView.findViewById(R.id.item_jdbh);
        mItem_zrr = (TextItemTableItem) mMenPaiView.findViewById(R.id.item_zrr);
        mItem_lxdh = (PhoneItemTableItem) mMenPaiView.findViewById(R.id.item_lxdh);
        cb_yes = (CheckBox) mMenPaiView.findViewById(R.id.cb_yes);
        cb_no = (CheckBox) mMenPaiView.findViewById(R.id.cb_no);

        setReadOnly(false);
        if(!StringUtil.isEmpty(bh)){
            mItem_jdbh.setText(bh);
        }else{
            mItem_jdbh.setText("");
        }

        if(!StringUtil.isEmpty(zrr)){
            mItem_zrr.setText(zrr);
        }else{
            mItem_zrr.setText("");
        }

        if(!StringUtil.isEmpty(lxdh)){
            mItem_lxdh.setText(lxdh);
        }else{
            mItem_lxdh.setText("");
        }

        cb_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fiveItemChecked = true;
                } else {
                    fiveItemChecked = false;
                }
                cb_no.setChecked(!isChecked);
            }
        });
        cb_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fiveItemChecked = false;
                } else {
                    fiveItemChecked = true;
                }
                cb_yes.setChecked(!isChecked);
            }
        });

        if (!"1".equals(sfgjjd)
                || StringUtil.isEmpty(sfgjjd)) {
            cb_no.setChecked(true);
            mLl_container.setVisibility(View.GONE);
        } else {
            cb_yes.setChecked(true);
            mLl_container.setVisibility(View.VISIBLE);
        }
    }

    public void addTo(ViewGroup viewGroup) {
        viewGroup.addView(mMenPaiView);
    }
}
