package com.augurit.agmobile.gzps.doorno.view;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.util.FileBean;
import com.augurit.agmobile.gzps.common.widget.FileOpenViewActivity;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.am.cmpt.widget.filepicker.filter.CompositeFilter;
import com.augurit.am.cmpt.widget.filepicker.filter.PatternFilter;
import com.augurit.am.cmpt.widget.filepicker.utils.FileTypeUtils;
import com.augurit.am.cmpt.widget.filepicker.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

//import com.augurit.agmobile.common.view.filepicker.utils.FileUtils;


/**
 * 微信风格的文件选择Activity 扩展自component中的FilePickerActivity
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agriver.common.ui.filepicker
 * @createTime 创建时间 ：2018-01-10
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2018-01-10
 * @modifyMemo 修改备注：
 */
public class WEUIFilePickerActivity extends BaseActivity implements WEUIDirectoryFragment.FileClickListener {

    public static final String ARG_START_PATH = "arg_start_path";
    public static final String ARG_CURRENT_PATH = "arg_current_path";

    public static final String ARG_FILTER = "arg_filter";

    public static final String STATE_START_PATH = "state_start_path";
    protected static final String STATE_CURRENT_PATH = "state_current_path";

    public static final String RESULT_FILE_PATH = "result_file_path";

    public static final String SELECT_LIMIT = "selectLimit";
    public static final String SINGLE_SELECTION = "singleSelection";
    public static final String CONFIRM_TEXT = "confirmText";

    protected static final int HANDLE_CLICK_DELAY = 150;

    protected String mStartPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    protected String mCurrentPath = mStartPath;

    protected CompositeFilter mFilter;

    protected TextView tv_title;
    protected TextView tv_path;
    protected ViewGroup bottom_bar;
    protected TextView tv_selected;
    protected Button btn_confirm;
    protected ViewGroup ll_dir;
    protected TextView tv_dir;

    protected int mLimit;
    protected boolean mIsSingle;
    protected String mConfirmText;
    protected ArrayList<File> mSelectedFile;
    protected ArrayList<String> mSelectedPath;
    protected ArrayList<FileBean> mSelectedFileBean;

    protected WEUIDirectoryFragment mFragmentCur;

    public static Intent newIntent(Context context, int selectLimit, boolean singleSelection) {
        Intent intent = new Intent(context, WEUIFilePickerActivity.class);
        intent.putExtra(SELECT_LIMIT, selectLimit);
        intent.putExtra(SINGLE_SELECTION, singleSelection);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        overridePendingTransition(R.anim.center_in, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_file_picker);
        initArguments();
        initViews();

        if (savedInstanceState != null) {
            mStartPath = savedInstanceState.getString(STATE_START_PATH);
            mCurrentPath = savedInstanceState.getString(STATE_CURRENT_PATH);
        } else {
            initFragment();
        }

        EventBus.getDefault().register(this);
    }

    protected void initArguments() {
        if (getIntent().hasExtra(ARG_FILTER)) {
            Serializable filter = getIntent().getSerializableExtra(ARG_FILTER);

            if (filter instanceof Pattern) {
                ArrayList<FileFilter> filters = new ArrayList<>();
                filters.add(new PatternFilter((Pattern) filter, false));
                mFilter = new CompositeFilter(filters);
            } else {
                mFilter = (CompositeFilter) filter;
            }
        }

        if (getIntent().hasExtra(ARG_START_PATH)) {
            mStartPath = getIntent().getStringExtra(ARG_START_PATH);
            mCurrentPath = mStartPath;
        }

        if (getIntent().hasExtra(ARG_CURRENT_PATH)) {
            String currentPath = getIntent().getStringExtra(ARG_CURRENT_PATH);

            if (currentPath.startsWith(mStartPath)) {
                mCurrentPath = currentPath;
            }
        }

        if (getIntent().hasExtra(CONFIRM_TEXT)) {
            mConfirmText = getIntent().getStringExtra(CONFIRM_TEXT);
        } else {
            mConfirmText = getString(R.string.validate_check_name);
        }

        mLimit = getIntent().getIntExtra(SELECT_LIMIT, 9);
        mIsSingle = getIntent().getBooleanExtra(SINGLE_SELECTION, false);

        mSelectedFile = new ArrayList<>();
        mSelectedPath = new ArrayList<>();
        mSelectedFileBean = new ArrayList<>();
    }

    protected void initViews() {
        tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText(getString(R.string.start_path_name));
        tv_path = (TextView) findViewById(R.id.path);
        bottom_bar = (ViewGroup) findViewById(R.id.bottom_bar);
        tv_selected = (TextView) findViewById(R.id.tv_selected);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        ll_dir = (ViewGroup) findViewById(R.id.ll_dir);
        tv_dir = (TextView) findViewById(R.id.tv_dir);
        btn_confirm.setEnabled(false);

        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResultAndFinish();
            }
        });
    }

    protected void initFragment() {
        mFragmentCur = WEUIDirectoryFragment.getInstance(
                mStartPath, mFilter, mSelectedPath, mSelectedPath.size() < mLimit, mLimit);
        getFragmentManager().beginTransaction()
                .add(R.id.container, mFragmentCur)
                .commit();
    }

    protected void updateTitle() {
        String path = mCurrentPath.isEmpty() ? "/" : mCurrentPath;
        if (path.startsWith(mStartPath)) {
            path = path.replaceFirst(mStartPath, "");
        }
        if (path.equals("/") || TextUtils.isEmpty(path)) {
            tv_path.setVisibility(View.GONE);
        } else {
            tv_path.setVisibility(View.VISIBLE);
        }
        tv_path.setText(path);
    }

    protected void addFragmentToBackStack(String path) {
        mFragmentCur = WEUIDirectoryFragment.getInstance(
                path, mFilter, mSelectedPath, mSelectedPath.size() < mLimit, mLimit);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, mFragmentCur)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            mCurrentPath = FileUtils.cutLastSegmentOfPath(mCurrentPath);
//            mCurrentPath = FileUtils.cutLastSegmentOfPath(mCurrentPath);
            updateTitle();
        } else {
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(0, R.anim.filepicker_center_out);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_CURRENT_PATH, mCurrentPath);
        outState.putString(STATE_START_PATH, mStartPath);
    }

    @Override
    public void onFileClicked(final File clickedFile) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleFileClicked(clickedFile);
            }
        }, HANDLE_CLICK_DELAY);
    }

    protected void handleFileClicked(final File clickedFile) {
        if (clickedFile.isDirectory()) {
            addFragmentToBackStack(clickedFile.getPath());
            mCurrentPath = clickedFile.getPath();
            updateTitle();
        } else {
            //打开文档
            FileOpenViewActivity.getIntent(this, clickedFile.getPath(), new FilePathUtil(this).getTempPath(), clickedFile.getName());
        }
    }

    protected void setResultAndFinish() {
        Intent data = new Intent();
        data.putExtra(RESULT_FILE_PATH, mSelectedFileBean);
        setResult(RESULT_OK, data);
        finish();
    }

    protected void refreshSelected() {
        if (mSelectedPath.isEmpty()) {
            bottom_bar.setVisibility(View.GONE);
            btn_confirm.setText(mConfirmText);
            btn_confirm.setEnabled(false);
        } else {
            bottom_bar.setVisibility(View.VISIBLE);
            String size = getString(R.string.multicheck_select_something);
            long sum = 0;
            for (File file : mSelectedFile) {
                sum += FileUtils.getFileSize(file);
//                sum += FileUtils.getFileSize(file);
            }
            size += FileUtils.formatFileSize(sum);
//            size += FileUtils.formatFileSize(sum);
            tv_selected.setText(size);

            btn_confirm.setEnabled(true);
            if (getIntent().hasExtra(CONFIRM_TEXT)) {
                btn_confirm.setText(String.format(mConfirmText + "(%d/%d)", mSelectedPath.size(), mLimit));
            } else {
                btn_confirm.setText(String.format(getString(R.string.multicheck_select_title), mSelectedPath.size(), mLimit));
            }
        }
        mFragmentCur.setSelectionEnabled(mSelectedPath.size() < mLimit, mLimit);
    }

    @Subscribe
    public void onReceiveFileSelectEvent(FileSelectEvent event) {
        FileBean FileBean = new FileBean();
        FileBean.setIcon(FileTypeUtils.getFileType(event.file).getIcon());
        String yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));

//        22.pdf  20180514101922_hasCert6_pdf.pdf
        FileBean.setName(yyyyMMddHHmmss + "_" + event.file.getName());
        FileBean.setPath(event.file.getPath());
        FileBean.setPreTag(yyyyMMddHHmmss + "_");
        FileBean.setSize(FileUtils.getFileSize(event.file));
//        FileBean.setSize(FileUtils.getFileSize(event.file));
        if (event.isSelect && !mSelectedPath.contains(event.file.getPath())) {
            mSelectedFile.add(event.file);
            mSelectedPath.add(event.file.getPath());
            mSelectedFileBean.add(FileBean);
        } else if (!event.isSelect && mSelectedPath.contains(event.file.getPath())) {
            for (int i = 0; i < mSelectedFileBean.size(); i++) {
                if (event.file.getPath().equals(mSelectedFileBean.get(i).getPath())) {
                    mSelectedFileBean.remove(i);
                }
            }
            mSelectedFile.remove(event.file);
            mSelectedPath.remove(event.file.getPath());

        }
        refreshSelected();
    }

    protected static class FileSelectEvent {
        File file;
        boolean isSelect;

        FileSelectEvent(File file, boolean isSelect) {
            this.file = file;
            this.isSelect = isSelect;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSelectedFile != null) {
            mSelectedFile.clear();
        }
        if (mSelectedPath != null) {
            mSelectedPath.clear();
        }
        if (mSelectedFileBean != null) {
            mSelectedFileBean.clear();
        }
        EventBus.getDefault().unregister(this);
    }
}
