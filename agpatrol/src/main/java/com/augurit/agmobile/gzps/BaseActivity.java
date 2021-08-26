package com.augurit.agmobile.gzps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by xiaoyu on 2018/3/12.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("=====ActivityName===========" + getClass().getName() + "\n" +
                "====package===" + getPackageName());
    }
}
