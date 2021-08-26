package com.augurit.agmobile.gzps.uploadfacility.view.drainageentity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.MyGridView;
import com.augurit.agmobile.gzps.uploadfacility.model.DrainageEntity;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.common.preview.view.PhotoPagerActivity;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.filepicker.utils.FileTypeUtils;
import com.augurit.am.fw.cache.img.amimageloader.AMImageLoader;
import com.augurit.am.fw.utils.ListUtil;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsh on 2018/2/2.
 */

public class SelectDrainageEntityAdapter extends RecyclerView.Adapter<SelectDrainageEntityAdapter.SelectDrainageEntityHolder> {

    private Context mContext;
    private boolean mSelectMode = true;
    private LayoutInflater layoutInflater;
    private List<DrainageEntity> mDrainageEntityList;
    private ArrayList<DrainageEntity> mSelectedDrainageEntitys;
    private OnRecyclerItemClickListener<DrainageEntity> mOnRecyclerItemClickListener;


    public SelectDrainageEntityAdapter(Context context, boolean selectMode){
        mContext = context;
        mSelectMode = selectMode;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void notifyDataSetChanged(List<DrainageEntity> drainageEntityList, ArrayList<DrainageEntity> selectedDrainageEntitys){
        this.mDrainageEntityList = drainageEntityList;
        this.mSelectedDrainageEntitys = selectedDrainageEntitys;
        notifyDataSetChanged();
    }

    @Override
    public SelectDrainageEntityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectDrainageEntityHolder(layoutInflater.inflate(R.layout.item_select_drainage_entity, null));
    }

    @Override
    public void onBindViewHolder(final SelectDrainageEntityHolder holder, final int position) {
        final DrainageEntity drainageEntity = mDrainageEntityList.get(position);

        holder.name.setText("项目名称：" + drainageEntity.getEntry_name());
        holder.liscode.setText("许可证号：" + drainageEntity.getLicense_key());
        holder.degree.setText(drainageEntity.getType());
        if("重点".equals(drainageEntity.getType())){
            holder.degree.setBackgroundResource(R.drawable.location_symbol);
        } else {
            holder.degree.setBackgroundResource(R.drawable.ic_circle);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true){
                    Intent intent = new Intent(mContext, DrainageEntityDetailActivity.class);
                    intent.putExtra("drainageEntity", drainageEntity);
                    mContext.startActivity(intent);
                    return;
                }

                if(holder.ll_files.getVisibility() == View.VISIBLE){
                    holder.ll_files.setVisibility(View.GONE);
                    return;
                }
                holder.ll_files.setVisibility(View.VISIBLE);
                holder.fl_files.removeAllViews();
                final ArrayList<Photo> photos= new ArrayList<>();
                for(int i=0; i<drainageEntity.getFiles().size(); i++){
                    final int filePosition = i;
                    DrainageEntity.FilesBean file = drainageEntity.getFiles().get(i);
                    /*FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(DensityUtil.dp2px(mContext, 60), DensityUtil.dp2px(mContext, 60));
                    lp.leftMargin = DensityUtil.dp2px(mContext, 8);
                    lp.rightMargin = DensityUtil.dp2px(mContext, 8);
                    ImageView imageView = new ImageView(mContext);
                    imageView.setLayoutParams(lp);
                    TextView textView = new TextView(mContext);
                    textView.setText(photo.getPhotoName());
                    holder.fl_files.addView(imageView);*/
                    View fileView = null;
                    fileView = LayoutInflater.from(mContext).inflate(R.layout.layout_drainage_entity_file, null);
                    ImageView iv = (ImageView) fileView.findViewById(R.id.iv);
                    TextView tv = (TextView) fileView.findViewById(R.id.tv);
                    tv.setText(file.getAtt_name());
                    if(FileTypeUtils.FileType.IMAGE == FileTypeUtils.getFileType(file.getAtt_path())){
                        Photo photo = new Photo();
                        photo.setPhotoName(file.getAtt_name());
                        photo.setPhotoName(file.getAtt_path());
                        photo.setThumbPath(file.getThum_path());
                        photos.add(photo);
                        AMImageLoader.loadStringRes(iv, file.getThum_path());
                        fileView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View iv) {
                                if (!ListUtil.isEmpty(drainageEntity.getFiles())) {
                                    Intent intent = new Intent(mContext, PhotoPagerActivity.class);
                                    intent.putExtra("BITMAPLIST", photos);
                                    intent.putExtra("POSITION", filePosition);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, iv, "shareTransition").toBundle());
                                    } else {
                                        mContext.startActivity(intent);
                                    }

                                }
                            }
                        });
                    } else {
                        AMImageLoader.loadResId(iv, R.drawable.filepicker_ic_document_box);
                    }

                    holder.fl_files.addView(fileView);

                }

                /*DrainageEntityFileAdapter fileAdapter = new DrainageEntityFileAdapter(mContext);
                holder.gridView.setAdapter(fileAdapter);
                fileAdapter.notifyDataSetChanged(drainageEntity.getFiles());
                fileAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener<Photo>() {
                    @Override
                    public void onItemClick(View view, int position, Photo selectedData) {

                    }

                    @Override
                    public void onItemLongClick(View view, int position, Photo selectedData) {

                    }
                });*/
            }
        });
        holder.btn_sel.setVisibility(View.GONE);
        holder.cb_sel.setVisibility(View.VISIBLE);

        holder.btn_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRecyclerItemClickListener != null){
                    mOnRecyclerItemClickListener.onItemClick(v, position, drainageEntity);
                }
            }
        });
        holder.cb_sel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mSelectedDrainageEntitys != null){
                    if(isChecked && !mSelectedDrainageEntitys.contains(drainageEntity)){
                        mSelectedDrainageEntitys.add(drainageEntity);
                    } else {
                        mSelectedDrainageEntitys.remove(drainageEntity);
                    }
                }
            }
        });

        if(mSelectedDrainageEntitys != null
                && mSelectedDrainageEntitys.contains(drainageEntity)){
            holder.btn_sel.setTextColor(Color.BLUE);
            holder.btn_sel.setText("已选");
            holder.cb_sel.setChecked(true);
        } else {
            holder.cb_sel.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        if (ListUtil.isEmpty(mDrainageEntityList)) {
            return 0;
        }
        return mDrainageEntityList.size();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<DrainageEntity> onRecyclerItemClickListener) {
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public ArrayList<DrainageEntity> getSelectedDrainageEntitys() {
        return mSelectedDrainageEntitys;
    }

    public void setSelectedDrainageEntitys(ArrayList<DrainageEntity> selectedDrainageEntitys) {
        this.mSelectedDrainageEntitys = selectedDrainageEntitys;
    }

    class SelectDrainageEntityHolder extends RecyclerView.ViewHolder{

        View ll_intro;
        TextView name;
        TextView liscode;
        TextView degree;
        Button btn_sel;
        CheckBox cb_sel;
        View ll_files;
        FlexboxLayout fl_files;
        MyGridView gridView;

        public SelectDrainageEntityHolder(View itemView){
            super(itemView);
            ll_intro = itemView.findViewById(R.id.ll_intro);
            name = (TextView) itemView.findViewById(R.id.entity_name);
            liscode = (TextView) itemView.findViewById(R.id.entity_liscode);
            degree = (TextView) itemView.findViewById(R.id.entity_degree);
            cb_sel = (CheckBox) itemView.findViewById(R.id.cb_sel);
            btn_sel = (Button) itemView.findViewById(R.id.btn_sel);
            ll_files = itemView.findViewById(R.id.ll_files);
            fl_files = (FlexboxLayout) itemView.findViewById(R.id.fl_files);
//            gridView = (MyGridView) itemView.findViewById(R.id.gridview);
        }
    }
}
