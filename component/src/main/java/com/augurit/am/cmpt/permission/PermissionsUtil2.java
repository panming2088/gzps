package com.augurit.am.cmpt.permission;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.augurit.am.fw.utils.log.LogUtil;

import java.util.Arrays;
import java.util.List;


/**
 * 权限申请
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt
 * @createTime 创建时间 ：2017/10/26
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/10/26
 * @modifyMemo 修改备注：
 */

@Deprecated
public class PermissionsUtil2 implements EasyPermissions.PermissionCallbacks {

    private static PermissionsUtil2 instance;
    private OnPermissionsCallback mCallbackCur;  // 当前请求的Callback

    @Deprecated
    public static PermissionsUtil2 getInstance() {
        if (instance == null) {
            synchronized (PermissionsUtil2.class) {
                instance = new PermissionsUtil2();
            }
        }
        return instance;
    }

    /**
     * 申请权限，调用前确保已在host中的onRequestPermissionsResult中
     * 调用了本类的{@link #onRequestPermissionsResult(int, String[], int[])}方法
     * @param host 调用者当前所属的Activity
     * @param rationale 给用户的权限申请描述，将在弹出的对话框中显示
     * @param requestCode requestCode
     * @param onPermissionCallback 权限申请回调
     * @param perms 需要申请的权限
     */
    @Deprecated
    public void requestPermissions(@NonNull Activity host,
                                   @NonNull String rationale,
                                   int requestCode,
                                   OnPermissionsCallback onPermissionCallback,
                                   @NonNull String... perms) {
        mCallbackCur = onPermissionCallback;
        if (EasyPermissions.hasPermissions(host, perms)) {
            mCallbackCur.onPermissionsGranted(Arrays.asList(perms));
            mCallbackCur = null;
        } else {
            EasyPermissions.requestPermissions(host, rationale, requestCode, perms);
        }
    }

    /**
     * 申请权限，调用前确保已在host中的onRequestPermissionsResult中
     * 调用了本类的{@link #onRequestPermissionsResult(int, String[], int[])}方法
     * @param host 调用者当前所属的android.support.v4.app.Fragment
     * @param rationale 给用户的权限申请描述，将在弹出的对话框中显示
     * @param requestCode requestCode
     * @param onPermissionCallback 权限申请回调
     * @param perms 需要申请的权限
     */
    @Deprecated
    public void requestPermissions(@NonNull Fragment host,
                                   @NonNull String rationale,
                                   int requestCode,
                                   OnPermissionsCallback onPermissionCallback,
                                   @NonNull String... perms) {
        mCallbackCur = onPermissionCallback;
        if (EasyPermissions.hasPermissions(host.getContext(), perms)) {
            mCallbackCur.onPermissionsGranted(Arrays.asList(perms));
            mCallbackCur = null;
        } else {
            EasyPermissions.requestPermissions(host, rationale, requestCode, perms);
        }
    }

    /**
     * 申请权限，调用前确保已在host中的onRequestPermissionsResult中
     * 调用了本类的{@link #onRequestPermissionsResult(int, String[], int[])}方法
     * @param host 调用者当前所属的android.app.Fragment
     * @param rationale 给用户的权限申请描述，将在弹出的对话框中显示
     * @param requestCode requestCode
     * @param onPermissionCallback 权限申请回调
     * @param perms 需要申请的权限
     */
    @Deprecated
    public void requestPermissions(@NonNull android.app.Fragment host,
                                   @NonNull String rationale,
                                   int requestCode,
                                   OnPermissionsCallback onPermissionCallback,
                                   @NonNull String... perms) {
        mCallbackCur = onPermissionCallback;
        if (EasyPermissions.hasPermissions(host.getActivity(), perms)) {
            mCallbackCur.onPermissionsGranted(Arrays.asList(perms));
            mCallbackCur = null;
        } else {
            EasyPermissions.requestPermissions(host, rationale, requestCode, perms);
        }
    }

    /**
     * 取得权限，不要手动调用此方法
     * @param perms
     */
    @Override
    public void onPermissionsGranted(List<String> perms) {
        LogUtil.i("PermissionUtil", "onPermissionsGranted");
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
        LogUtil.i("PermissionUtil", "onPermissionsDenied");
        if (mCallbackCur != null) {
            mCallbackCur.onPermissionsDenied(perms);
            mCallbackCur = null;
        }
    }

    /**
     * 接收权限申请结果，请在
     * {@link Activity#onRequestPermissionsResult(int, String[], int[])} 或
     * {@link android.app.Fragment#onRequestPermissionsResult(int, String[], int[])} 或
     * {@link Fragment#onRequestPermissionsResult(int, String[], int[])} 方法中调用
     *
     * @param requestCode requestCode
     * @param permissions 权限
     * @param grantResults 结果
     */
    @Deprecated
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(permissions, grantResults, this);
    }

    @Deprecated
    public static void destroy() {
        instance = null;
    }

    /**
     * 权限申请回调
     */
    public static abstract class OnPermissionsCallback {
        /**
         * 权限申请允许
         * @param perms 权限列表
         */
        public abstract void onPermissionsGranted(List<String> perms);

        /**
         * 权限申请拒绝
         * @param perms 权限列表
         */
        public void onPermissionsDenied(List<String> perms) {
        }
    }
}
