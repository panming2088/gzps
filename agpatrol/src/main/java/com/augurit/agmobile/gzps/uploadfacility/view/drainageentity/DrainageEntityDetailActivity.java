package com.augurit.agmobile.gzps.uploadfacility.view.drainageentity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.DrainageEntity;
import com.augurit.agmobile.patrolcore.common.preview.view.PhotoPagerActivity;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.filepicker.utils.FileTypeUtils;
import com.augurit.am.fw.cache.img.amimageloader.AMImageLoader;
import com.augurit.am.fw.utils.ListUtil;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

/**
 * Created by lsh on 2018/2/5.
 */

public class DrainageEntityDetailActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drainage_entity_detail);

        final DrainageEntity drainageEntity = (DrainageEntity) getIntent().getSerializableExtra("drainageEntity");

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.tv_title)).setText("排水户详情");

        TextView entity_name = (TextView) findViewById(R.id.entity_name);
        TextView entity_liscode = (TextView) findViewById(R.id.entity_liscode);
        TextView entity_degree = (TextView) findViewById(R.id.entity_degree);
        FlexboxLayout fl_files = (FlexboxLayout) findViewById(R.id.fl_files);

        entity_name.setText("项目名称：" + drainageEntity.getEntry_name());
        entity_liscode.setText("许可证号：" + drainageEntity.getLicense_key());
        entity_degree.setText(drainageEntity.getType());
        if("重点".equals(drainageEntity.getType())){
            entity_degree.setBackgroundResource(R.drawable.location_symbol);
        } else {
            entity_degree.setBackgroundResource(R.drawable.ic_circle);
        }
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
            fileView = LayoutInflater.from(this).inflate(R.layout.layout_drainage_entity_file, null);
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
                            Intent intent = new Intent(DrainageEntityDetailActivity.this, PhotoPagerActivity.class);
                            intent.putExtra("BITMAPLIST", photos);
                            intent.putExtra("POSITION", filePosition);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(DrainageEntityDetailActivity.this, iv, "shareTransition").toBundle());
                            } else {
                                startActivity(intent);
                            }

                        }
                    }
                });
            } else {
                AMImageLoader.loadResId(iv, R.drawable.filepicker_ic_document_box);
            }


            fl_files.addView(fileView);

        }
    }
}
