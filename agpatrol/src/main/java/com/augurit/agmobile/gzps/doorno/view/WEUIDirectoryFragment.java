package com.augurit.agmobile.gzps.doorno.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.SuperCheckBox;
import com.augurit.am.cmpt.widget.filepicker.filter.CompositeFilter;
import com.augurit.am.cmpt.widget.filepicker.utils.FileTypeUtils;
import com.augurit.am.cmpt.widget.filepicker.utils.FileUtils;
import com.augurit.am.cmpt.widget.filepicker.widget.EmptyRecyclerView;
import com.augurit.am.fw.utils.view.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 微信风格的文件选择Fragment
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agriver.common.ui.filepicker
 * @createTime 创建时间 ：2018-01-10
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2018-01-10
 * @modifyMemo 修改备注：
 */
public class WEUIDirectoryFragment extends Fragment {
    public interface FileClickListener {
        void onFileClicked(File clickedFile);
    }

    private static final String ARG_FILE_PATH = "arg_file_path";
    private static final String ARG_FILTER = "arg_filter";
    private static final String ARG_FILE_SELECTED = "ARG_FILE_SELECTED";
    private static final String ARG_SEL_ENABLED = "ARG_SEL_ENABLED";
    private static final String ARG_LIMIT = "ARG_LIMIT";

    private View mEmptyView;
    private String mPath;
    private ArrayList<String> mFileSelected;
    private CompositeFilter mFilter;

    private EmptyRecyclerView mDirectoryRecyclerView;
    private WEUIDirectoryAdapter mDirectoryAdapter;
    private FileClickListener mFileClickListener;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFileClickListener = (FileClickListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFileClickListener = null;
    }

    public static WEUIDirectoryFragment getInstance(
            String path, CompositeFilter filter, ArrayList<String> selected, boolean selectionEnabled, int limit) {
        WEUIDirectoryFragment instance = new WEUIDirectoryFragment();

        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, path);
        args.putSerializable(ARG_FILTER, filter);
        args.putStringArrayList(ARG_FILE_SELECTED, selected);
        args.putBoolean(ARG_SEL_ENABLED, selectionEnabled);
        args.putInt(ARG_LIMIT, limit);
        instance.setArguments(args);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directory, container, false);
        mDirectoryRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.directory_recycler_view);
        mEmptyView = view.findViewById(R.id.directory_empty_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initArgs();
        initFilesList();
    }

    private void initFilesList() {
        mDirectoryAdapter = new WEUIDirectoryAdapter(getActivity(),
                FileUtils.getFileListByDirPath(mPath, mFilter), mFileSelected);

        mDirectoryAdapter.setOnItemClickListener(new WEUIDirectoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mFileClickListener != null) {
                    mFileClickListener.onFileClicked(mDirectoryAdapter.getModel(position));
                }
            }
        });
        mDirectoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDirectoryRecyclerView.setAdapter(mDirectoryAdapter);
        mDirectoryRecyclerView.setEmptyView(mEmptyView);

        boolean selEnabled = getArguments().getBoolean(ARG_SEL_ENABLED, true);
        int limit = getArguments().getInt(ARG_LIMIT);
        mDirectoryAdapter.setSelectionEnabled(selEnabled, limit);
    }

    @SuppressWarnings("unchecked")
    private void initArgs() {
        if (getArguments().getString(ARG_FILE_PATH) != null) {
            mPath = getArguments().getString(ARG_FILE_PATH);
        }

        mFilter = (CompositeFilter) getArguments().getSerializable(ARG_FILTER);
        mFileSelected = getArguments().getStringArrayList(ARG_FILE_SELECTED);
    }

    public void setSelectionEnabled(boolean enabled, int limit) {
        mDirectoryAdapter.setSelectionEnabled(enabled, limit);
    }

    /**
     * 微信风格的文件选择Adapter
     * @author 创建人 ：xiejiexin
     * @version 1.0
     * @package 包名 ：com.augurit.agriver.common.ui.filepicker
     * @createTime 创建时间 ：2018-01-10
     * @modifyBy 修改人 ：xiejiexin
     * @modifyTime 修改时间 ：2018-01-10
     * @modifyMemo 修改备注：
     */

    public static class WEUIDirectoryAdapter extends RecyclerView.Adapter<WEUIDirectoryAdapter.DirectoryViewHolder> {
        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        public class DirectoryViewHolder extends RecyclerView.ViewHolder {
            private ImageView mFileImage;
            private TextView mFileTitle;
            private TextView mFileSubtitle;
            private View mCheckView;
            private SuperCheckBox mCheckBox;

            public DirectoryViewHolder(View itemView, final OnItemClickListener clickListener) {
                super(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onItemClick(view, getAdapterPosition());
                    }
                });

                mFileImage = (ImageView) itemView.findViewById(R.id.item_file_image);
                mFileTitle = (TextView) itemView.findViewById(R.id.item_file_title);
                mFileSubtitle = (TextView) itemView.findViewById(R.id.item_file_subtitle);
                mCheckView = itemView.findViewById(R.id.view_check);
                mCheckBox = (SuperCheckBox) itemView.findViewById(R.id.cb_check);

                mCheckView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCheckBox.performClick();
                    }
                });
            }
        }

        private List<File> mFiles;
        private List<String> mSelected; // 已选中的文件
        private Context mContext;
        private OnItemClickListener mOnItemClickListener;
        private SparseBooleanArray mSelection;
        private boolean mSelectionEnabled;
        private int mLimit = 9;

        public WEUIDirectoryAdapter(Context context, List<File> files, List<String> selected) {
            mContext = context;
            mFiles = files;
            mSelected = selected;
            initSelection();
        }

        private void initSelection() {
            if (mSelection == null) mSelection = new SparseBooleanArray();
            mSelection.clear();
            for (int i = 0; i < getItemCount(); i++) {
                mSelection.put(i, false);
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mOnItemClickListener = listener;
        }

        @Override
        public DirectoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_filepicker_item_file, parent, false);
            return new DirectoryViewHolder(view, mOnItemClickListener);
        }

        @Override
        public void onBindViewHolder(final DirectoryViewHolder holder, final int position) {
            final File currentFile = mFiles.get(position);

            String description = "";
            if (currentFile.isDirectory() && currentFile.list()!=null) {
                description = "文件：" + currentFile.list().length;
            } else if (currentFile.isFile()) {
                long fileSize = FileUtils.getFileSize(currentFile);
    //            long fileSize = FileUtils.getFileSize(currentFile);
                description += FileUtils.formatFileSize(fileSize) + " ";
                Date date = new Date(currentFile.lastModified());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int fileYear = calendar.get(Calendar.YEAR);
                calendar.setTime(new Date());
                int curYear = calendar.get(Calendar.YEAR);
                SimpleDateFormat format;
                if (fileYear == curYear) {
                    format = new SimpleDateFormat("MM月dd日");
                } else {
                    format = new SimpleDateFormat("yyyy年MM月dd日");
                }
                description += format.format(date);
            }

            FileTypeUtils.FileType fileType = FileTypeUtils.getFileType(currentFile);
            holder.mFileImage.setImageResource(fileType.getIcon());
            holder.mFileSubtitle.setText(description);
            holder.mFileTitle.setText(currentFile.getName());

            if (currentFile.isDirectory()) {
                holder.mCheckView.setVisibility(View.GONE);
            } else {
                holder.mCheckView.setVisibility(View.VISIBLE);
            }

            if (mSelected != null && mSelected.contains(currentFile.getPath())) {
                mSelection.put(position, true);
            }

            holder.mCheckBox.setChecked(mSelection.get(position));
            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mSelectionEnabled && holder.mCheckBox.isChecked()) {
                        holder.mCheckBox.setChecked(false);
    //                    ToastUtil.shortToast(mContext, "最多只能选择" + mLimit + "个文件");
                        ToastUtil.shortToast(mContext,mContext.getString(R.string.validate_file_number_full));
                    } else {
                        long fileSize = FileUtils.getFileSize(currentFile);
                        if (fileSize > 10 * 1024 * 1024) {
                            holder.mCheckBox.setChecked(false);
                            ToastUtil.shortToast(mContext, "单张pdf文件不能超过10M");
                            return;
                        }
                        mSelection.put(position, holder.mCheckBox.isChecked());
                        EventBus.getDefault().post(new WEUIFilePickerActivity.FileSelectEvent(currentFile,
                                holder.mCheckBox.isChecked()));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mFiles.size();
        }

        public void setSelectionEnabled(boolean enabled, int limit) {
            mSelectionEnabled = enabled;
            mLimit = limit;
        }

        public File getModel(int index) {
            return mFiles.get(index);
        }
    }
}
