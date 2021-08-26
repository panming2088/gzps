package com.augurit.agmobile.gzps.common.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.util.FileBean;
import com.augurit.agmobile.gzps.doorno.view.WEUIFilePickerActivity;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 创建人 ：yaowang
 * @version 1.0
 * @package 包名 ：com.yaowang.filepickerdemo
 * @createTime 创建时间 ：2018/10/15
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：
 * @modifyMemo 修改备注：
 */
public class AgFilePicker extends LinearLayout implements View.OnClickListener {
    private TextView tvName;
    private TextView tvRequiredTag;
    private TextView tvIndicator;
    private TextView tvError;
    private FileCustomRecyclerView fcRecyclerView;
    private String textName;
    private OnFilePickerClickListener onFilePickerClickListener;
    private LinearLayout llChooseFile;
    private RelativeLayout rlChooseSign;
    private ArrayList<FileBean> fileBeans;
    private TextView tv_hint;
    private boolean isAudioPicker = false;

    private int REQ_PERMISSION = 0x0102;
    private int REQ_FILE_PICKER = 0x0103;
    private int REQ_AUDIO_PICKER = 0x0104;
    private boolean mReadOnly;
    private TextView tv_content;
//    private RxPermissions rxPermissions;

    public AgFilePicker(Context context) {
        this(context, null, false);
    }

    public AgFilePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public AgFilePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        this(context, attrs, false);
    }

    public AgFilePicker(Context context, boolean isAudioPicker){
        this(context, null, isAudioPicker);
    }

    public AgFilePicker(Context context, @Nullable AttributeSet attrs, boolean isAudioPicker) {
        super(context, attrs);
        this.isAudioPicker = isAudioPicker;
        initDefaultValue();
        initView(context);
        initCustomValue(context,attrs);
        afterInitValue();
    }

    public ArrayList<FileBean> getSelectFiles(){
        return this.fileBeans;
    }

    private void initDefaultValue() {
        fileBeans = new ArrayList<>();
//        rxPermissions = new RxPermissions((FragmentActivity) getContext());
    }

    private void initView(Context context) {
//        View view = LayoutInflater.from(context).inflate(R.layout.view_filepicker, this);
        View view = View.inflate(context, R.layout.view_filepicker, this);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvRequiredTag = (TextView) view.findViewById(R.id.tv_requiredTag);
        tvIndicator = (TextView) view.findViewById(R.id.tv_indicator);
        tvError = (TextView) view.findViewById(R.id.tv_error);
        tv_hint = (TextView) view.findViewById(R.id.tv_hint);
        fcRecyclerView = (FileCustomRecyclerView) view.findViewById(R.id.rv_files);
        llChooseFile = (LinearLayout) view.findViewById(R.id.ll_choose_file);
        rlChooseSign = (RelativeLayout) view.findViewById(R.id.rl_content);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
    }

    private void initCustomValue(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AGFilePicker);
        textName = a.getString(R.styleable.AGFilePicker_FilePickerTextViewName);
        a.recycle();
    }

    private void afterInitValue() {
        tvName.setText(textName);
        fcRecyclerView.setOnHandle(handle);
        fcRecyclerView.setNestedScrollingEnabled(false);
        llChooseFile.setOnClickListener(this);
    }


    private FileCustomRecyclerView.OnHandle handle = new FileCustomRecyclerView.OnHandle() {
        @Override
        public void onItemClick(View itemView, int position, FileBean data) {
            if(onFilePickerClickListener!=null){
                onFilePickerClickListener.onItemClick(itemView,position,data);
            }else {
                //打开文件
                openTBSFile(data);
            }
        }

        @Override
        public void onDeleteFile(int position, FileBean data) {
            if(onFilePickerClickListener!=null){
                onFilePickerClickListener.onDeleteButtonClick(position,data);
                removeFile(position);
            }else {
                removeFile(position);
            }
        }
    };

    public void openTBSFile(FileBean data) {
        FileOpenViewActivity.getIntent(getContext(),data.getPath(), Environment.getExternalStorageDirectory().getAbsolutePath(),data.getName());
    }

    public void removeFile(int position) {
        this.fileBeans.remove(position);
        updateLengthIndicator(this.fileBeans.size());
        fcRecyclerView.removeItem(position);
    }

    private void jumpToFile(Activity host) {
        Intent intent = new Intent(getContext(), WEUIFilePickerActivity.class);
        intent.putExtra(WEUIFilePickerActivity.SELECT_LIMIT,fcRecyclerView.getMaxItemCount()-fcRecyclerView.getItemCount());
        if (host != null) {
            host.startActivityForResult(intent, REQ_FILE_PICKER);
        }
    }

    /**
     * 添加图片的requestCode
     *
     * @param requestCode
     */
    public void setPickerRequestCode(int requestCode) {
        if(!isAudioPicker){
            REQ_FILE_PICKER = requestCode;
        }else {
            REQ_AUDIO_PICKER = requestCode;
        }
    }

    public void setOnFilePickerListener(OnFilePickerClickListener onImagePickerListener) {
        this.onFilePickerClickListener = onImagePickerListener;
    }

    public void setTextViewName(String textViewName) {
        tvName.setText(textViewName);
    }

    public void setTitleTextColor(int color) {
        tvName.setTextColor(ResourcesCompat.getColor(getResources(),color,null));
    }

    //设置右边的选择文件样式
    public void setTextViewContentTextAndColor(String text ,int color) {
        tv_content.setTextColor(color);
        tv_content.setText(text);
    }

    public void setTitleTextSize(int textSize) {
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
    }


    public void setMaxLimit(int maxLimit) {
//        tvIndicator.setVisibility(VISIBLE);
        tvIndicator.setText(fileBeans.size()> 0 ? (fileBeans.size()+"/"+ maxLimit):("0/" + maxLimit));
        //最大可选择图片数
        fcRecyclerView.setMaxItemCount(maxLimit);
    }

    public void setHint(String hint) {
        tv_hint.setText(hint);
    }

    public void setHintTextColor(int color) {
        tv_hint.setTextColor(ResourcesCompat.getColor(getResources(),color,null));
    }

    public void setHintTextSize(int size) {
        tv_hint.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void showHint(boolean isShow) {
        tv_hint.setVisibility(isShow? VISIBLE : GONE);
    }

    public void showRequireTag(boolean isShow) {
        if (isShow) {
            tvRequiredTag.setVisibility(VISIBLE);
        } else {
            tvRequiredTag.setVisibility(GONE);
        }
    }

    public void setErrorText(String errorText) {
        tvError.setText(errorText);
    }

    public void showErrorText(boolean isShow) {
        if (isShow) {
            tvError.setVisibility(VISIBLE);
            tv_hint.setVisibility(GONE);
        } else {
            tvError.setVisibility(GONE);
            if (fcRecyclerView.isEditable()) {
                tv_hint.setVisibility(VISIBLE);
            }
        }
    }

    public void setEnable(boolean isEnable) {
        llChooseFile.setEnabled(isEnable);
        //设置是否可选
        fcRecyclerView.setEditable(isEnable);
    }

    public void setReadOnly(boolean readOnly) {
        mReadOnly = readOnly;
        if(readOnly){
            llChooseFile.setVisibility(GONE);
        }else {
            llChooseFile.setVisibility(VISIBLE);
        }
        updateLengthIndicator(this.fileBeans.size(), false);
    }

    public View getView() {
        return this;
    }

    public ArrayList<FileBean> getValue() {
        return fileBeans;
    }

    public void setValue(List<FileBean> value) {
        if (value == null) return;
        this.fileBeans.clear();
        this.fileBeans.addAll(value);
        updateLengthIndicator(this.fileBeans.size());
        if (ListUtil.isEmpty(value)) {
            fcRecyclerView.setData(new ArrayList<FileBean>());
            return;
        }
        ArrayList<FileBean> files = new ArrayList<>();
        for (FileBean fileBean : value) {
            if (!StringUtil.isEmpty(fileBean.getPath())) {
                files.add(fileBean);
            }
        }
        fcRecyclerView.setData(files);
    }

    private void updateLengthIndicator(int currentFileCount) {
        updateLengthIndicator(currentFileCount, true);
    }

//    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_FILE_PICKER) {
            if (resultCode == WEUIFilePickerActivity.RESULT_OK) {
                List<FileBean> FileBeans = data.getParcelableArrayListExtra(WEUIFilePickerActivity.RESULT_FILE_PATH);
                this.fileBeans.addAll(FileBeans);
                onFilePickerClickListener.onAddFile(fileBeans);
                fcRecyclerView.addMoreData(FileBeans);
                updateLengthIndicator(this.fileBeans.size());
            }
        }
    }

    private void updateLengthIndicator(int currentFileCount, boolean shouldValidate) {
        if (mReadOnly) {
            tvIndicator.setVisibility(GONE);
            return;
        }
        if(currentFileCount>0){
            tvIndicator.setVisibility(VISIBLE);
        }else {
            tvIndicator.setVisibility(GONE);
        }
        tvIndicator.setText(currentFileCount + "/" + fcRecyclerView.getMaxItemCount());
//        if (shouldValidate && mIHelpValidate != null && fcRecyclerView.isEditable()) {
//            mIHelpValidate.validate();
//        }
    }

//    public String getValidateName() {
//        return getContext().getString(R.string.validate_file_name);
//    }
//
//    public String getValidateUnit() {
//        return getContext().getString(R.string.validate_edit_text_unit);
//    }
//
//    public void helpValidate(IHelpValidate iHelpValidate) {
//        this.mIHelpValidate = iHelpValidate;
//    }

    public void setRequestCode(int requestCode) {
        this.setPickerRequestCode(requestCode);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQ_FILE_PICKER) {
//            if (resultCode == WEUIFilePickerActivity.RESULT_OK) {
//                List<FileBean> FileBeans = data.getParcelableArrayListExtra(WEUIFilePickerActivity.RESULT_FILE_PATH);
//                this.fileBeans.addAll(FileBeans);
//                fcRecyclerView.addMoreData(FileBeans);
//                updateLengthIndicator(this.fileBeans.size());
//            }
//        }else if(requestCode == REQ_AUDIO_PICKER){
//            if(resultCode == AudioFilePickerActivity.RESULT_OK){
//                List<FileBean> FileBeans = data.getParcelableArrayListExtra(AudioFilePickerActivity.RESULT_FILEBEAN_LIST);
//                this.fileBeans.addAll(FileBeans);
//                fcRecyclerView.addMoreData(FileBeans);
//                updateLengthIndicator(this.fileBeans.size());
//            }
//        }
//    }

//    @Override
//    public void onDestroy() {
//
//    }


    @SuppressLint("CheckResult")
    @Override
    public void onClick(View view) {
        Activity host = null;
        if (getContext() instanceof Activity) {
            host = (Activity) getContext();
        } else if (getContext() instanceof ContextWrapper) {
            Context context = ((ContextWrapper) getContext()).getBaseContext();
            if (context instanceof Activity) {
                host = (Activity) context;
            }
        }
        if(isAudioPicker){
//            jumpToAudio(host);
        }else {
            jumpToFile(host);
        }
    }

//    private void jumpToAudio(Activity host) {
//        Intent intent = new Intent(getContext(), AudioFilePickerActivity.class);
//        intent.putExtra(AudioFilePickerActivity.AUDIO_SELECT_LIMIT,fcRecyclerView.getMaxItemCount()-fcRecyclerView.getItemCount());
//        if (host != null) {
//            host.startActivityForResult(intent, REQ_AUDIO_PICKER);
//        }
//    }


    public interface OnFilePickerClickListener {
        /**
         * 删除按钮点击回调
         *
         * @param position
         * @param data
         */
        void onDeleteButtonClick(int position, FileBean data);

        /**
         * 图片点击回调
         *
         * @param view
         * @param position
         * @param data
         */
        void onItemClick(View view, int position, FileBean data);

        /**
         * 新增附件的回调
         */
        void onAddFile(List<FileBean> datas);
    }
}
