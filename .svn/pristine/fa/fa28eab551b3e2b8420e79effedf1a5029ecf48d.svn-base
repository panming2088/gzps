package com.augurit.am.cmpt.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;

import com.augurit.am.cmpt.R;

/**
 * 权限获取页面
 */
public class PermissionsActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    private static final String EXTRA_PERMISSIONS = "PERMISSION_REQUEST"; // 权限参数
    private static final String SHOW_SETTING_DIALOG = "SHOW_SETTING_DIALOG";
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    private boolean mShowSettingDialog = true;

    private boolean isRequireCheck; // 是否需要系统权限检测, 防止和系统提示框重叠
    private boolean returnFromSetting = false; //从系统设置界面返回

    // 启动当前权限页面的公开接口
    public static void startPermissionActivity(Activity activity, boolean showSettingDialog, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        intent.putExtra(SHOW_SETTING_DIALOG, showSettingDialog);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("启动PermissionsActivity时缺少必要参数!");
        }
        setContentView(R.layout.activity_permissions);
        mShowSettingDialog = getIntent().getBooleanExtra(SHOW_SETTING_DIALOG, true);
        isRequireCheck = true;
        setFinishOnTouchOutside(false);
        /*设置窗口样式activity宽高start*/
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6);   //高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7);    //宽度设置为屏幕的0.7
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0f;      //设置窗口外黑暗度
        getWindow().setAttributes(p);
 /*设置窗口样式activity宽高end*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (returnFromSetting) {
            String[] permissions = getPermissions();
            int[] grantResults = EasyPermissions.checkPermissions2(this, permissions);
            returnFromSetting = false;
            finish();
            PermissionsUtil.getInstance().onRequestPermissionsResult(PERMISSION_REQUEST_CODE, permissions, grantResults);
            return;
        }
        if (isRequireCheck) {
            String[] permissions = getPermissions();
            if (EasyPermissions.hasPermissions(this, permissions)) {
                int[] grantResults = new int[permissions.length];
                for (int i = 0; i < grantResults.length; i++) {
                    grantResults[i] = PackageManager.PERMISSION_GRANTED;
                }
                finish();
                PermissionsUtil.getInstance().onRequestPermissionsResult(PERMISSION_REQUEST_CODE, permissions, grantResults);
            } else {
                EasyPermissions.requestPermissions(this, "APP需要权限才能正常工作，请点击确定授予权限", PERMISSION_REQUEST_CODE, permissions);
            }
        } else {
            isRequireCheck = true;
        }
    }

    // 返回传递的权限参数
    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            finish();
            isRequireCheck = true;
            PermissionsUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            isRequireCheck = false;
            if(mShowSettingDialog) {
                showMissingPermissionDialog(permissions, grantResults);
            } else {
                PermissionsUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
                finish();
            }
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog(final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
        builder.setTitle("权限未授予");
        String appName = getApplicationName();
        builder.setMessage("        " + appName + "缺少必要权限。为保证正常使用，请点击\"设置\"-\"权限\"-允许所有权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionsUtil.getInstance().onRequestPermissionsResult(PERMISSION_REQUEST_CODE, permissions, grantResults);
                finish();
            }
        });

        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isRequireCheck = false;
                returnFromSetting = true;
                startAppSettings();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }
}
