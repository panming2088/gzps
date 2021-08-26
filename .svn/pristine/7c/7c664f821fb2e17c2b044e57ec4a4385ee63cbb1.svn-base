package com.augurit.agmobile.gzps.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.augurit.agmobile.gzps.BaseActivity;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;

/**
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.statistic
 * @createTime 创建时间 ：2017/8/15
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/8/15
 * @modifyMemo 修改备注：
 */

public class PhotoExampleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo_example);
        initView();
    }

    private void initView() {
        ((TextView)findViewById(R.id.tv_title)).setText("拍照须知");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
