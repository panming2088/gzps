package com.augurit.am.cmpt.permission.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Delegate class to make permission calls based on the 'host' (Fragment, Activity, etc).
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public abstract class PermissionHelper<T> {

    private static final String TAG = "PermissionHelper";

    private T mHost;

    @NonNull
    public static PermissionHelper newInstance(Activity host) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return new LowApiPermissionsHelper(host);
        }

        if (host instanceof AppCompatActivity) {
            return new AppCompatActivityPermissionHelper((AppCompatActivity) host);
        } else {
            return new ActivityPermissionHelper(host);
        }
    }

    @NonNull
    public static PermissionHelper newInstance(Fragment host) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return new LowApiPermissionsHelper(host);
        }

        return new SupportFragmentPermissionHelper(host);
    }

    @NonNull
    public static PermissionHelper newInstance(android.app.Fragment host) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return new LowApiPermissionsHelper(host);
        }

        return new FrameworkFragmentPermissionHelper(host);
    }

    // ============================================================================
    // Public concrete methods
    // ============================================================================

    public PermissionHelper(@NonNull T host) {
        mHost = host;
    }

    public boolean shouldShowRationale(int requestCode, @NonNull String... perms) {
        for (String perm : perms) {
            if (shouldShowRequestPermissionRationale(requestCode, perm)) {
                return true;
            }
        }
        return false;
    }

    public void requestPermissions(@NonNull String rationale,
                                   @StringRes int positiveButton,
                                   @StringRes int negativeButton,
                                   int requestCode,
                                   @NonNull String... perms) {
//        if (shouldShowRationale(requestCode, perms)) {
//            showRequestPermissionRationale(
//                    rationale, positiveButton, negativeButton, requestCode, perms);
//        } else {
            directRequestPermissions(requestCode, perms);
//        }
    }

    public boolean somePermissionPermanentlyDenied(int requestCode, @NonNull List<String> perms) {
        for (String deniedPermission : perms) {
            if (permissionPermanentlyDenied(requestCode, deniedPermission)) {
                return true;
            }
        }

        return false;
    }

    public boolean permissionPermanentlyDenied(int requestCode, @NonNull String perms) {
        return !shouldShowRequestPermissionRationale(requestCode, perms);
    }

    public boolean somePermissionDenied(int requestCode, @NonNull String... perms) {
        return shouldShowRationale(requestCode, perms);
    }

    @NonNull
    public T getHost() {
        return mHost;
    }

    // ============================================================================
    // Public abstract methods
    // ============================================================================

    public abstract void directRequestPermissions(int requestCode, @NonNull String... perms);

    public abstract boolean shouldShowRequestPermissionRationale(int requestCode, @NonNull String perm);

    public abstract void showRequestPermissionRationale(@NonNull String rationale,
                                                        @StringRes int positiveButton,
                                                        @StringRes int negativeButton,
                                                        int requestCode,
                                                        @NonNull String... perms);
    public abstract Context getContext();

}
