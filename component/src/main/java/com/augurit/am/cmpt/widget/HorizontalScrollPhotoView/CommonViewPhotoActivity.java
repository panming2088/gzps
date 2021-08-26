package com.augurit.am.cmpt.widget.HorizontalScrollPhotoView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.augurit.am.cmpt.R;
import com.augurit.am.fw.utils.constant.BaseBundleKeyConstant;
import com.bumptech.glide.Glide;

public class CommonViewPhotoActivity extends AppCompatActivity {

    private Photo photo;
//    private ImageView imageView;
    private PinchImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_view_photo);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonViewPhotoActivity.this.finish();
            }
        });

//        imageView = (ImageView)findViewById(R.id.img);
        imageView = (PinchImageView) findViewById(R.id.img);

        Intent intent = getIntent();
        if(intent.getSerializableExtra(BaseBundleKeyConstant.INTENT_SERIALIZABLE) != null){
            photo =(Photo)intent.getSerializableExtra(BaseBundleKeyConstant.INTENT_SERIALIZABLE);
            Glide.with(this).load(photo.getPhotoPath()).into(imageView);
        }
    }
}
