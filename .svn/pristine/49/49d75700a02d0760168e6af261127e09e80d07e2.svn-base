package com.augurit.am.cmpt.update.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.update.utils.CheckUpdateUtils;
import com.augurit.am.cmpt.update.utils.UpdateState;


public class TestUpdateActivity extends AppCompatActivity {

    private AlertDialog.Builder mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //版本更新实例代码
        String serverUrl = "http://192.168.31.10:8080/chapter1/apk_version.json";
        CheckUpdateUtils.setServerUrl(serverUrl);

        /*
        new ApkUpdateManager(this, UpdateState.INNER_UPDATE, new ApkUpdateManager.NoneUpdateCallback() {
            @Override
            public void onFinish() {

            }
        }).checkUpdate();
        */
        new ApkUpdateManager(this,UpdateState.WIFI_AUTO_UPDATE_NOW).checkUpdate();
    }

}
