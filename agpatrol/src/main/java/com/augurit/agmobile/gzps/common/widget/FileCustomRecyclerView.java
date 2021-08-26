package com.augurit.agmobile.gzps.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;

import com.augurit.agmobile.gzps.common.util.ClickUtil;
import com.augurit.agmobile.gzps.common.util.FileBean;
import com.augurit.agmobile.gzps.uploadevent.util.PhotoUploadType;
import com.augurit.am.cmpt.widget.filepicker.utils.FileUtils;

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
public class FileCustomRecyclerView extends RecyclerView {
    private int mDeleteDrawableResId;
    private LinearLayoutManager linearLayoutManager;
    private FileAdapter mFileAdapter;
    private boolean mEditable;
    private int mMaxItemCount;

    public FileCustomRecyclerView(@NonNull Context context) {
        this(context,null);
    }

    public FileCustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDefaultValue();
        initCustomValue(context,attrs);
        afterInitValue();
    }

    private void initDefaultValue() {
        mDeleteDrawableResId = R.mipmap.bga_pp_ic_delete;
        mEditable = true;
        mMaxItemCount = 9;
    }

    private void initCustomValue(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FileCustomRecyclerView);
        mDeleteDrawableResId = typedArray.getInt(R.styleable.FileCustomRecyclerView_file_deleteDrawable,mDeleteDrawableResId);
        typedArray.recycle();
    }

    private void afterInitValue() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(linearLayoutManager);
        mFileAdapter = new FileAdapter(getContext());
        setAdapter(mFileAdapter);
    }


    /**
     * 设置是否可编辑，默认值为 true
     *
     * @param editable
     */
    public void setEditable(boolean editable) {
        mEditable = editable;
        mFileAdapter.notifyDataSetChanged();
    }

    /**
     * 获取是否可编辑
     *
     * @return
     */
    public boolean isEditable() {
        return mEditable;
    }


    /**
     * 获取选择的文件的最大数量
     *
     * @return
     */
    public int getMaxItemCount() {
        return mMaxItemCount;
    }

    /**
     * 设置可选择图片的总张数,默认值为 9
     *
     * @param maxItemCount
     */
    public void setMaxItemCount(int maxItemCount) {
        if(maxItemCount < 0){
            return;
        }
        mMaxItemCount = maxItemCount;
    }

    public void setData(List<FileBean> datas){
        mFileAdapter.setDatas(datas);
    }

    /**
     * 删除指定索引位置的图片
     *
     * @param position
     */
    public void removeItem(int position) {
        mFileAdapter.removeItem(position);
    }

    /**
     * 获取图片总数
     *
     * @return
     */
    public int getItemCount() {
        return mFileAdapter.getData().size();
    }


    /**
     * 在集合尾部添加更多数据集合
     *
     * @param files
     */
    public void addMoreData(List<FileBean> files) {
        if (files != null) {
            mFileAdapter.getData().addAll(files);
            mFileAdapter.notifyDataSetChanged();
        }
    }



    private class FileAdapter extends Adapter<FileViewHolder>{
        private Context context;
        private List<FileBean> mDatas;
        private LayoutInflater mInflater;


        /**
         * 获取数据集合
         *
         * @return
         */
        public List<FileBean> getData() {
            return mDatas;
        }


        public FileAdapter(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
            mDatas = new ArrayList<>();
        }

        /**
         * 设置数据，将会调用{@link #notifyDataSetChanged()}
         * @param datas
         */
        public void setDatas(List<FileBean> datas) {
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }


        /**
         * 删除指定索引数据条目
         *
         * @param position
         */
        public void removeItem(int position) {
            mDatas.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,mDatas.size());
        }



        @NonNull
        @Override
        public FileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = mInflater.inflate(R.layout.item_file_list, viewGroup, false);
            FileViewHolder holder = new FileViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final FileViewHolder viewHolder, final int i) {
            ClickUtil.bind(viewHolder.itemView, new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnHandle!=null){
                        mOnHandle.onItemClick(viewHolder.itemView,i,mDatas.get(i));
                    }
                }
            });

            viewHolder.ivClose.setVisibility( isEditable()?VISIBLE:GONE);

            ClickUtil.bind(viewHolder.ivClose, new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnHandle!=null){
                        mOnHandle.onDeleteFile(i,mDatas.get(i));
                    }
                }
            });
            String name = mDatas.get(i).getName();
            if(name.endsWith(".pdf")){
                viewHolder.ivIcon.setImageResource(R.drawable.pdf);
            }else {
                viewHolder.ivIcon.setImageResource(mDatas.get(i).getIcon());
            }

            //去掉文件名的前缀，防止显示的名称太长
            if(name.indexOf("_hasCert6_")!=-1){
                //事务公开或者我的上报列表附件名称包含_hasCert6_
                name = mDatas.get(i).getName().split("_hasCert6_")[1];
            }else if(mDatas.get(i).getId()==0){
                //新增排水户id为0
                if(!TextUtils.isEmpty(mDatas.get(i).getPreTag()))
                name = name.replace(mDatas.get(i).getPreTag(),"");
            }else{
                //草稿列表过来id不为0
                name = name.replace(PhotoUploadType.UPLOAD_FOR_FILES + mDatas.get(i).getPreTag(),"");
            }
            viewHolder.tvName.setText(name);
            viewHolder.tvSize.setText(FileUtils.formatFileSize(mDatas.get(i).getSize()));
//            viewHolder.tvSize.setText(FileUtils.formatFileSize(mDatas.get(i).getSize()));
            if(isEditable()){
                viewHolder.ivClose.setVisibility(VISIBLE);
            }else {
                viewHolder.ivClose.setVisibility(GONE);
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }


    }


    class FileViewHolder extends ViewHolder {
        private ImageView ivIcon;
        private TextView tvName;
        private TextView tvSize;
        private ImageView ivClose;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_file_imageView);
            tvName = (TextView) itemView.findViewById(R.id.tv_file_name);
            tvSize = (TextView) itemView.findViewById(R.id.tv_file_size);
            ivClose = (ImageView) itemView.findViewById(R.id.iv_file_delete);
        }
    }

    protected OnHandle mOnHandle;

    public interface OnHandle {
        void onItemClick(View itemView, int position, FileBean data);

        void onDeleteFile(int position, FileBean data);
    }

    public void setOnHandle(OnHandle onItemClickListener) {
        this.mOnHandle = onItemClickListener;
    }





}
