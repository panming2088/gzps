package com.augurit.agmobile.gzps.uploadevent.view.eventflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadevent.model.EventAdvice;
import com.augurit.agmobile.gzps.uploadevent.model.EventDetail;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;

/**
 * 意见
 *
 * Created by xcl on 2017/11/11.
 */

public class AdviceView {
    private Context mContext;
    private TextView tv_organization;
    private TextView tv_user_id;
    private TextView tv_advice;
    private View root;

    public AdviceView(Context context){
        this.mContext = context;
    }

    public void initView(EventDetail.OpinionBean eventAdvice) {
        root = LayoutInflater.from(mContext).inflate(R.layout.item_handle_advice, null);
        tv_organization = (TextView) root.findViewById(R.id.tv_organization);
        tv_user_id = (TextView) root.findViewById(R.id.tv_user_id);
        tv_advice = (TextView) root.findViewById(R.id.tv_advice);
        TextView tv_time = (TextView) root.findViewById(R.id.tv_time);

        tv_organization.setText(eventAdvice.getUserName() + "：");
//        tv_user_id.setText(eventAdvice.getLoginName());
        tv_advice.setText("      " + eventAdvice.getOpinion());
        tv_time.setText(TimeUtil.getStringTimeYMDS(new Date(eventAdvice.getTime())));
    }

    public void addTo(ViewGroup container){
        container.addView(root);
    }
}
