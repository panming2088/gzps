package com.augurit.agmobile.gzps.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.am.cmpt.common.base.BaseInfoManager;

public class UserInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("个人资料");

        TextView tv_name = (TextView) findViewById(R.id.usr_name);
        tv_name.setText(BaseInfoManager.getUserName(this));

        TextView tv_role_name = (TextView) findViewById(R.id.usr_role_name);
        tv_role_name.setText(BaseInfoManager.getRoleName(this));

        TextView tv_login_name = (TextView) findViewById(R.id.usr_login_name);
        tv_login_name.setText(BaseInfoManager.getLoginName(this));

        TextView tv_phone = (TextView) findViewById(R.id.usr_phone);
        if (TextUtils.isEmpty(BaseInfoManager.getUserPhone(this))) {
            tv_phone.setText("无");
        } else {
            tv_phone.setText(BaseInfoManager.getUserPhone(this));
        }

        TextView tv_duty = (TextView) findViewById(R.id.usr_duty);
        if (TextUtils.isEmpty(BaseInfoManager.getUserDuty(this))) {
            tv_duty.setText("无");
        } else {
            tv_duty.setText(BaseInfoManager.getUserDuty(this));
        }

        TextView tv_org = (TextView) findViewById(R.id.usr_org);
        tv_org.setText(BaseInfoManager.getUserOrg(this));

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
