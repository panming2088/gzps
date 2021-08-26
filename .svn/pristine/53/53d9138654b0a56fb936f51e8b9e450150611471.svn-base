package com.augurit.agmobile.gzps.track.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.track.view.presenter.ITrackPresenter;
import com.augurit.agmobile.gzps.track.view.presenter.TrackPresenter;
import com.augurit.am.cmpt.common.Callback1;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view
 * @createTime 创建时间 ：2017-06-13
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-13
 * @modifyMemo 修改备注：
 */
public class TrackRecordActivity extends BaseActivity {

    private TrackRecordView mTrackView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        // setupWindowAnimations();
        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        //TODO
        mTrackView = new TrackRecordView(this, container);

        ITrackPresenter trackPresenter = new TrackPresenter(this, mTrackView);
        trackPresenter.setBackListener(new Callback1() {
            @Override
            public void onCallback(Object o) {
                finish();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        mTrackView.onPressBack();
////        super.onBackPressed();
//    }

    /*private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);

            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setReturnTransition(slide);
        }
    }*/

}