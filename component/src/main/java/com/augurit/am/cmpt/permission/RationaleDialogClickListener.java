package com.augurit.am.cmpt.permission;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.Fragment;

import com.augurit.am.cmpt.permission.helper.PermissionHelper;

import java.util.Arrays;

/**
 * Click listener for either {@link RationaleDialogFragment} or {@link RationaleDialogFragmentCompat}.
 */
class RationaleDialogClickListener implements Dialog.OnClickListener {

    private Object mHost;
    private RationaleDialogConfig mConfig;
    private EasyPermissions.PermissionCallbacks mCallbacks;

    RationaleDialogClickListener(RationaleDialogFragmentCompat compatDialogFragment,
                                 RationaleDialogConfig config,
                                 EasyPermissions.PermissionCallbacks callbacks) {

        mHost = compatDialogFragment.getParentFragment() != null
                ? compatDialogFragment.getParentFragment()
                : compatDialogFragment.getActivity();

        mConfig = config;
        mCallbacks = callbacks;
    }

    RationaleDialogClickListener(RationaleDialogFragment dialogFragment,
                                 RationaleDialogConfig config,
                                 EasyPermissions.PermissionCallbacks callbacks) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mHost = dialogFragment.getParentFragment() != null ?
                    dialogFragment.getParentFragment() :
                    dialogFragment.getActivity();
        } else {
            mHost = dialogFragment.getActivity();
        }

        mConfig = config;
        mCallbacks = callbacks;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE) {
            if (mHost instanceof Fragment) {
                PermissionHelper.newInstance((Fragment) mHost).directRequestPermissions(
                        mConfig.requestCode, mConfig.permissions);
            } else if (mHost instanceof android.app.Fragment) {
                PermissionHelper.newInstance((android.app.Fragment) mHost).directRequestPermissions(
                        mConfig.requestCode, mConfig.permissions);
            } else if (mHost instanceof Activity) {
                PermissionHelper.newInstance((Activity) mHost).directRequestPermissions(
                        mConfig.requestCode, mConfig.permissions);
            } else {
                throw new RuntimeException("Host must be an Activity or Fragment!");
            }
        } else {
            if (mHost instanceof Fragment) {
                ((Fragment)mHost).getActivity().finish();
            } else if (mHost instanceof android.app.Fragment) {
                ((android.app.Fragment)mHost).getActivity().finish();
            } else if (mHost instanceof Activity) {
                ((Activity)mHost).finish();
            }
            notifyPermissionDenied();
        }
    }

    private void notifyPermissionDenied() {
        if (mCallbacks != null) {
            mCallbacks.onPermissionsDenied(Arrays.asList(mConfig.permissions));
        }
    }
}
