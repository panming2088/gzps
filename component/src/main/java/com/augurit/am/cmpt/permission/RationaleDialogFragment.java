package com.augurit.am.cmpt.permission;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;

/**
 * {@link DialogFragment} to display rationale for permission requests when the request comes from
 * a Fragment or Activity that can host a Fragment.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class RationaleDialogFragment extends DialogFragment {

    public static final String TAG = "RationaleDialogFragment";

    private EasyPermissions.PermissionCallbacks mPermissionCallbacks;

    public static RationaleDialogFragment newInstance(
            @StringRes int positiveButton, @StringRes int negativeButton,
            @NonNull String rationaleMsg, int requestCode, @NonNull String[] permissions) {

        // Create new Fragment
        RationaleDialogFragment dialogFragment = new RationaleDialogFragment();

        // Initialize configuration as arguments
        RationaleDialogConfig config = new RationaleDialogConfig(
                positiveButton, negativeButton, rationaleMsg, requestCode, permissions);
        dialogFragment.setArguments(config.toBundle());

        return dialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                && getParentFragment() != null
                && getParentFragment() instanceof EasyPermissions.PermissionCallbacks) {
            mPermissionCallbacks = (EasyPermissions.PermissionCallbacks) getParentFragment();
        } else if (context instanceof EasyPermissions.PermissionCallbacks) {
            mPermissionCallbacks = (EasyPermissions.PermissionCallbacks) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPermissionCallbacks = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Rationale dialog should not be cancelable
        setCancelable(false);

        // Get config from arguments, create click listener
        RationaleDialogConfig config = new RationaleDialogConfig(getArguments());
        RationaleDialogClickListener clickListener =
                new RationaleDialogClickListener(this, config, mPermissionCallbacks);

        // Create an AlertDialog
        return config.createFrameworkDialog(getActivity(), clickListener);
    }

}
