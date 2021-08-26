package com.augurit.agmobile.gzps.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.LoginActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.view.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SystemSettingActivity extends BaseActivity {

    private String loginName;
    private TextView versionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("我的设置");
        versionTv = (TextView) findViewById(R.id.version_info);
        versionTv.setText("当前版本 " + getLocalVersion());
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.change_psw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private String getLocalVersion() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // Log.e(TAG, "获取应用程序版本失败，原因：" + e.getMessage());
            return "";
        }

        return info.versionName;
    }

    private void showDialog() {
        loginName = BaseInfoManager.getLoginName(SystemSettingActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(SystemSettingActivity.this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(SystemSettingActivity.this, R.layout.dialog_content, null);
        final EditText old_pwd = (EditText) view.findViewById(R.id.old_pwd);
        final EditText new_pwd = (EditText) view.findViewById(R.id.new_pwd);
        final EditText sure_new_pwd = (EditText) view.findViewById(R.id.sure_new_pwd);
        Button btnok = (Button) view.findViewById(R.id.btn_sure);
        Button btncancel = (Button) view.findViewById(R.id.btn_cancel);

        dialog.setView(view);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPwd = old_pwd.getText().toString();
                String newPwd = new_pwd.getText().toString();
                String sure_newPwd = sure_new_pwd.getText().toString();
                if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty
                        (sure_newPwd)) {
                    ToastUtil.shortToast(SystemSettingActivity.this, "密码不能为空");
                    return;
                } else if (!newPwd.equals(sure_newPwd)) {
                    ToastUtil.shortToast(SystemSettingActivity.this, "两次输入的新密码不一致");
                    return;
                } else {
                    checkFromServer(oldPwd, newPwd);
                }

            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.user_dialog_bg);
        WindowManager.LayoutParams params = window.getAttributes();
        WindowManager windowManager = getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        params.width = (int) (d.getWidth() * 0.75);
        window.setAttributes(params);

    }

    private void checkFromServer(final String oldPwd, final String newPwd) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String url = "http://" + LoginConstant.SUPPORT_URL + LoginConstant.MODIFY_PASSWORD_URL +
                        loginName + "?oldPassWord=" + oldPwd +
                        "&newPassWord=" + newPwd;
                OkHttpClient client = new OkHttpClient.Builder().build();
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext(response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String success = jsonObject.getString("success");
                    if (success.equals("true")) {
                        ToastUtil.shortToast(SystemSettingActivity.this, "密码修改成功 请重新登录！");
                        startActivity(new Intent(SystemSettingActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        ToastUtil.shortToast(SystemSettingActivity.this, "密码修改出错 请联系管理员！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
