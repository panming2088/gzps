package com.augurit.agmobile.patrolcore.common.legend;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.augurit.agmobile.mapengine.legend.model.Legend;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;

import java.util.List;

/**
 * Created by xcl on 2017/10/30.
 */

public class ImagePmLegendDialog extends DialogFragment {

    public static final String KEY = "data";

    private ViewGroup ll_legend_tree_container;
    private ProgressLinearLayout progressLinearlayout_legend;


    public static ImagePmLegendDialog newInstance() {

        ImagePmLegendDialog legendDialog = new ImagePmLegendDialog();
        return legendDialog;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.legend_pm_image_dialog,container);
        view.findViewById(R.id.iv_mark_closeLegend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void showLoading() {


    }

    public void showError(String message, View.OnClickListener retryListener) {

    }

    public void showEmpty(View.OnClickListener retryListener) {

    }

    public void hideLoading() {


    }

    /**
     * 显示图例列表
     * @param legends
     */
    public void showLegends(List<Legend> legends) {

    }
}
