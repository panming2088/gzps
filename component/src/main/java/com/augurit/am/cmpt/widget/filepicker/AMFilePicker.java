package com.augurit.am.cmpt.widget.filepicker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;


import com.augurit.am.cmpt.widget.filepicker.filter.CompositeFilter;
import com.augurit.am.cmpt.widget.filepicker.filter.HiddenFilter;
import com.augurit.am.cmpt.widget.filepicker.filter.PatternFilter;
import com.augurit.am.cmpt.widget.filepicker.ui.FilePickerActivity;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * 文件选择器
 * 弹出界面 来选择具体文件
 * 可用于选择文件
 *
 * Material File Picker builder
 */
public class AMFilePicker {
    private Activity mActivity;
    private Fragment mFragment;
    private android.support.v4.app.Fragment mSupportFragment;

    private Integer mRequestCode;
    private Pattern mFileFilter;
    private Boolean mDirectoriesFilter = false;
    private String mRootPath;
    private String mCurrentPath;
    private Boolean mShowHidden = false;

    public AMFilePicker() {
    }


    /**
     * Specifies activity, which will be used to
     * start file picker
     */
    public AMFilePicker withActivity(Activity activity) {
        if (mSupportFragment != null || mFragment != null) {
            throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
        }

        mActivity = activity;
        return this;
    }

    /**
     * Specifies fragment, which will be used to
     * start file picker
     */
    public AMFilePicker withFragment(Fragment fragment) {
        if (mSupportFragment != null || mActivity != null) {
            throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
        }

        mFragment = fragment;
        return this;
    }

    /**
     * Specifies support fragment which will be used to
     * start file picker
     */
    public AMFilePicker withSupportFragment(android.support.v4.app.Fragment fragment) {
        if (mActivity != null || mFragment != null) {
            throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
        }

        mSupportFragment = fragment;
        return this;
    }

    /**
     * Specifies request code that used in activity result
     *
     * @see <a href="https://developer.android.com/training/basics/intents/result.html">Getting a Result from an Activity</a>
     */
    public AMFilePicker withRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }


    /**
     * Hides files that matched by specified regular expression.
     * Use {@link AMFilePicker#withFilterDirectories withFilterDirectories} method
     * to enable directories filtering
     */
    public AMFilePicker withFilter(Pattern pattern) {
        mFileFilter = pattern;
        return this;
    }

    /**
     * If directoriesFilter is true directories will also be affected by filter,
     * the default value of directories filter is false
     *
     * @see AMFilePicker#withFilter
     */
    public AMFilePicker withFilterDirectories(boolean directoriesFilter) {
        mDirectoriesFilter = directoriesFilter;
        return this;
    }

    /**
     * Specifies root directory for picker,
     * user can't go upper that specified path
     */
    public AMFilePicker withRootPath(String rootPath) {
        mRootPath = rootPath;
        return this;
    }

    /**
     * Specifies start directory for picker,
     * which will be shown to user at the beginning
     */
    public AMFilePicker withPath(String path) {
        mCurrentPath = path;
        return this;
    }


    /**
     * Show or hide hidden files in picker
     */
    public AMFilePicker withHiddenFiles(boolean show) {
        mShowHidden = show;
        return this;
    }

    public CompositeFilter getFilter() {
        ArrayList<FileFilter> filters = new ArrayList<>();

        if (!mShowHidden) {
            filters.add(new HiddenFilter());
        }

        if (mFileFilter != null) {
            filters.add(new PatternFilter(mFileFilter, mDirectoriesFilter));
        }

        return new CompositeFilter(filters);
    }


    /**
     * @return Intent that can be used to start Material File Picker
     */
    public Intent getIntent() {
        CompositeFilter filter = getFilter();

        Activity activity = null;
        if (mActivity != null) {
            activity = mActivity;
        } else if (mFragment != null) {
            activity = mFragment.getActivity();
        } else if (mSupportFragment != null) {
            activity = mSupportFragment.getActivity();
        }

        Intent intent = new Intent(activity, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.ARG_FILTER, filter);

        if (mRootPath != null) {
            intent.putExtra(FilePickerActivity.ARG_START_PATH, mRootPath);
        }

        if (mCurrentPath != null) {
            intent.putExtra(FilePickerActivity.ARG_CURRENT_PATH, mCurrentPath);
        }

        return intent;
    }

    /**
     * Open Material File Picker activity.
     * You should set Activity or Fragment before calling this method
     * @see AMFilePicker#withActivity(Activity)
     * @see AMFilePicker#withFragment(Fragment)
     * @see AMFilePicker#withSupportFragment(android.support.v4.app.Fragment)
     */
    public void start() {
        if (mActivity == null && mFragment == null && mSupportFragment == null) {
            throw new RuntimeException("You must pass Activity/Fragment by calling withActivity/withFragment/withSupportFragment method");
        }

        if (mRequestCode == null) {
            throw new RuntimeException("You must pass request code by calling withRequestCode method");
        }

        Intent intent = getIntent();

        if (mActivity != null) {
            mActivity.startActivityForResult(intent, mRequestCode);
        } else if (mFragment != null) {
            mFragment.startActivityForResult(intent, mRequestCode);
        } else {
            mSupportFragment.startActivityForResult(intent, mRequestCode);
        }
    }
}
