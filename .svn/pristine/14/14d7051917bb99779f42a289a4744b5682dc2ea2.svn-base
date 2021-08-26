package com.augurit.am.cmpt.permission;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.Arrays;
import java.util.List;

/**
 * 权限申请
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt
 * @createTime 创建时间 ：2017/10/26
 * @modifyBy 修改人 ：liangsh
 * @modifyTime 修改时间 ：2018/01/10
 * @modifyMemo 修改备注：1.不需要再在调用者所在的Activity中回调 onRequestPermissionsResult(int, String[], int[])方法
 *                        2.可提示跳转到系统设置中的APP设置界面
 */

public class PermissionsUtil implements EasyPermissions.PermissionCallbacks {

    private static PermissionsUtil instance;
    private OnPermissionsCallback mCallbackCur;  // 当前请求的Callback
    private boolean mShowSettingDialog = true;

    public synchronized static PermissionsUtil getInstance() {
        if (instance == null) {
            synchronized (PermissionsUtil.class) {
                instance = new PermissionsUtil();
            }
        }
        return instance;
    }


    /**
     * 申请权限，默认弹出提示权限设置的Dialog
     * @param host 调用者当前所属的Activity
     * @param onPermissionCallback 权限申请回调
     * @param perms 需要申请的权限
     */
    public void requestPermissions(@NonNull Activity host,
                                   OnPermissionsCallback onPermissionCallback,
                                   @NonNull String... perms) {
        requestPermissions(host, true, onPermissionCallback, perms);
    }

    /**
     * 申请权限
     * @param host 调用者当前所属的Activity
     * @param onPermissionCallback 权限申请回调
     * @param perms 需要申请的权限
     */
    public void requestPermissions(@NonNull Activity host,
                                   boolean showSettingDialog,
                                   OnPermissionsCallback onPermissionCallback,
                                   @NonNull String... perms) {
        mShowSettingDialog = showSettingDialog;
        mCallbackCur = onPermissionCallback;
        if (EasyPermissions.hasPermissions(host, perms)) {
            mCallbackCur.onPermissionsGranted(Arrays.asList(perms));
            mCallbackCur = null;
        } else {
//            EasyPermissions.requestPermissions(host, rationale, requestCode, perms);
            PermissionsActivity.startPermissionActivity(host, mShowSettingDialog, perms);
        }
    }

    /**
     * 申请权限，默认弹出提示权限设置的Dialog
     * @param host 调用者当前所属的android.support.v4.app.Fragment
     * @param onPermissionCallback 权限申请回调
     * @param perms 需要申请的权限
     */
    public void requestPermissions(@NonNull Fragment host,
                                   OnPermissionsCallback onPermissionCallback,
                                   @NonNull String... perms) {
        requestPermissions(host, true, onPermissionCallback, perms);
    }

    /**
     * 申请权限
     * @param host 调用者当前所属的android.support.v4.app.Fragment
     * @param onPermissionCallback 权限申请回调
     * @param perms 需要申请的权限
     */
    public void requestPermissions(@NonNull Fragment host,
                                   boolean showSettingDialog,
                                   OnPermissionsCallback onPermissionCallback,
                                   @NonNull String... perms) {
        mShowSettingDialog = showSettingDialog;
        mCallbackCur = onPermissionCallback;
        if (EasyPermissions.hasPermissions(host.getContext(), perms)) {
            mCallbackCur.onPermissionsGranted(Arrays.asList(perms));
            mCallbackCur = null;
        } else {
//            EasyPermissions.requestPermissions(host, rationale, 0, perms);
            PermissionsActivity.startPermissionActivity(host.getActivity(), mShowSettingDialog, perms);
        }
    }

    /**
     * 申请权限，默认弹出提示权限设置的Dialog
     * @param host 调用者当前所属的android.app.Fragment
     * @param onPermissionCallback 权限申请回调
     * @param perms 需要申请的权限
     */
    public void requestPermissions(@NonNull android.app.Fragment host,
                                   OnPermissionsCallback onPermissionCallback,
                                   @NonNull String... perms) {
        requestPermissions(host, true, onPermissionCallback, perms);
    }

    /**
     * 申请权限
     * @param host 调用者当前所属的android.app.Fragment
     * @param onPermissionCallback 权限申请回调
     * @param perms 需要申请的权限
     */
    public void requestPermissions(@NonNull android.app.Fragment host,
                                   boolean showSettingDialog,
                                   OnPermissionsCallback onPermissionCallback,
                                   @NonNull String... perms) {
        mShowSettingDialog = showSettingDialog;
        mCallbackCur = onPermissionCallback;
        if (EasyPermissions.hasPermissions(host.getActivity(), perms)) {
            mCallbackCur.onPermissionsGranted(Arrays.asList(perms));
            mCallbackCur = null;
        } else {
            PermissionsActivity.startPermissionActivity(host.getActivity(), mShowSettingDialog, perms);
        }
    }

    /**
     * 取得权限，不要手动调用此方法
     * @param perms
     */
    @Override
    public void onPermissionsGranted(List<String> perms) {
        if (mCallbackCur != null) {
            mCallbackCur.onPermissionsGranted(perms);
            mCallbackCur = null;
        }
    }

    /**
     * 权限拒绝，不要手动调用此方法
     * @param perms
     */
    @Override
    public void onPermissionsDenied(List<String> perms) {
        if (mCallbackCur != null) {
            mCallbackCur.onPermissionsDenied(perms);
            mCallbackCur = null;
        }
    }

    /**
     * 接收权限申请结果，不要手动调用此方法
     *
     * @param permissions 权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(permissions, grantResults, this);
    }

    public static void destroy() {
        instance = null;
    }

    /**
     * 权限申请回调
     */
    public interface OnPermissionsCallback {
        /**
         * 权限申请允许
         * @param perms 权限列表
         */
        void onPermissionsGranted(List<String> perms);

        /**
         * 权限申请拒绝
         * @param perms 权限列表
         */
        void onPermissionsDenied(List<String> perms);
    }
}
