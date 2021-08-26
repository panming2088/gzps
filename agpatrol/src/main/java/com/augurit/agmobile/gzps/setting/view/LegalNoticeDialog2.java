package com.augurit.agmobile.gzps.setting.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.service.GzpsService;
import com.augurit.agmobile.gzps.login.PatrolLoginService;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.AMFileUtil;

import org.json.JSONObject;

import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liangsh on 2018/1/3.
 */

public class LegalNoticeDialog2 extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_legal_notice2, container);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        final View btn_sure = view.findViewById(R.id.btn_sure);


        try {
            String htmlStr = AMFileUtil.readStringFromAsset(getContext(), "flsm.html", Charset.defaultCharset());
            tv.setText(Html.fromHtml(htmlStr));
//            tv.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动
        } catch (Exception e) {
            e.printStackTrace();
        }
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        return view;
    }

}
