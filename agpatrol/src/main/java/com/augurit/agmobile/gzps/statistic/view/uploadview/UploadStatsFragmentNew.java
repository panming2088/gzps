package com.augurit.agmobile.gzps.statistic.view.uploadview;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;

import static com.augurit.agmobile.gzps.common.constant.LoginConstant.GZPS_AGSUPPORT;

/**
 * Created by liangsh on 2017/11/6.
 */

public class UploadStatsFragmentNew extends Fragment implements View.OnClickListener {

    private TextView checkTv, problemTv, journalTv, lineTv, pipeTv, hookTv, monitorTv, hookWellTv, hookPsdyTv, jcWellTv, jcGjjdTv,tvResponsible;
    private WebView mWebView;
    private final static int UPLOAD_TYPE_CHECK = 1;//点击了复核统计
    private final static int UPLOAD_TYPE_PROBLEM = 2;//点击了问题统计
    private final static int UPLOAD_TYPE_JOURNAL = 3;//点击了巡检统计
    private final static int UPLOAD_TYPE_LINE = 4;//点击了管线统计
    private final static int UPLOAD_TYPE_PIPE = 5;//点击了管井统计
    private final static int UPLOAD_TYPE_MONITOR = 6;//点击了监测统计
    private final static int UPLOAD_TYPE_HOOK = 7;//点击了挂接统计
    private final static int UPLOAD_TYPE_HOOK_WELL = 8;//点击了按接驳井统计
    private final static int UPLOAD_TYPE_HOOK_PSDY = 9;//点击了按排水单元统计
    private final static int UPLOAD_TYPE_GJJD = 10;//点击了按关键节点统计（监测统计）
    private final static int UPLOAD_TYPE_JBJ = 11;//点击了按接驳井统计（监测统计）
    private final static int UPLOAD_TYPE_RESPONSIBLE = 12;//点击了按接驳井统计（监测统计）
    private int UPLOAD_TYPE = 1;
    private View check_ll, hook_ll, jc_ll;
    //管线统计
    private final static String LINE_STATS = "http://" + GZPS_AGSUPPORT + "/event/wellStatisticsV2.html";
    //管井统计
    private final static String PIPT_STATS = "http://" + GZPS_AGSUPPORT + "/event/pipeStatisticsV2.html";
    //问题统计
    private final static String PROBLEM_STATS = "http://" + GZPS_AGSUPPORT + "/event/problemStatisticsV2.html";
    //上报统计
    private final static String JOURNAL_STATS = "http://" + GZPS_AGSUPPORT + "/event/patrolStatisticsV2.html";
    //挂接统计(按接驳井)
    private final static String HOOK_WELL_STATS = "http://" + GZPS_AGSUPPORT + "/event/connectWelllStatisticsV2.html";
    //挂接统计(按排水单元)
    private final static String HOOK_PSDY_STATS = "http://" + GZPS_AGSUPPORT + "/event/drainageUnitStatisticsV2.html";
    //按接驳井监测统计
    private final static String MONITOR_STATS = "http://" + GZPS_AGSUPPORT + "/event/monitorStatisticsV2.html";
    //按关键节点监测统计
    private final static String GJJD_STATS = "http://" + GZPS_AGSUPPORT + "/event/monitorStatisticsGjjd.html";
    //责任人统计
    private final static String RESPONSIBLE_STATS = "http://" + GZPS_AGSUPPORT + "/event/wellLibStatisticsV2.html";

    private String checkUrl = LINE_STATS;
    private String hookUrl = HOOK_WELL_STATS;
    private String jcUrl = MONITOR_STATS;
    private int primaryColor;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uploadstats_new, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        primaryColor = getResources().getColor(R.color.colorPrimary);

        checkTv = (TextView) view.findViewById(R.id.check_tv);
        tvResponsible = (TextView) view.findViewById(R.id.tvResponsible);
        problemTv = (TextView) view.findViewById(R.id.problem_tv);
        journalTv = (TextView) view.findViewById(R.id.journal_tv);
        lineTv = (TextView) view.findViewById(R.id.line_tv);
        pipeTv = (TextView) view.findViewById(R.id.pipe_tv);
        hookTv = (TextView) view.findViewById(R.id.hook_tv);
        monitorTv = (TextView) view.findViewById(R.id.monitor_tv);
        hookWellTv = (TextView) view.findViewById(R.id.hook_well_tv);
        hookPsdyTv = (TextView) view.findViewById(R.id.hook_psdy_tv);
        jcWellTv = (TextView) view.findViewById(R.id.jc_well_tv);
        jcGjjdTv = (TextView) view.findViewById(R.id.jc_gjjd_tv);
        check_ll = view.findViewById(R.id.check_ll);
        hook_ll = view.findViewById(R.id.hook_ll);
        jc_ll = view.findViewById(R.id.jc_ll);
        mWebView = (WebView) view.findViewById(R.id.webview);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }
        });
        checkTv.setOnClickListener(this);
        tvResponsible.setOnClickListener(this);
        problemTv.setOnClickListener(this);
        journalTv.setOnClickListener(this);
        lineTv.setOnClickListener(this);
        pipeTv.setOnClickListener(this);
        hookTv.setOnClickListener(this);
        monitorTv.setOnClickListener(this);
        hookWellTv.setOnClickListener(this);
        hookPsdyTv.setOnClickListener(this);
        jcGjjdTv.setOnClickListener(this);
        jcWellTv.setOnClickListener(this);

        check_ll.setOnClickListener(this);
        loadUI(LINE_STATS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_tv:
                UPLOAD_TYPE = UPLOAD_TYPE_CHECK;
                setTitleState(UPLOAD_TYPE_CHECK);
                loadUI(checkUrl);
                break;
            case R.id.problem_tv:
                UPLOAD_TYPE = UPLOAD_TYPE_PROBLEM;
                setTitleState(UPLOAD_TYPE);
                loadUI(PROBLEM_STATS);
                break;
            case R.id.tvResponsible:
                UPLOAD_TYPE = UPLOAD_TYPE_RESPONSIBLE;
                setTitleState(UPLOAD_TYPE);
                loadUI(RESPONSIBLE_STATS);
                break;
            case R.id.journal_tv:
                UPLOAD_TYPE = UPLOAD_TYPE_JOURNAL;
                setTitleState(UPLOAD_TYPE);
                loadUI(JOURNAL_STATS);
                break;
            case R.id.line_tv:
                UPLOAD_TYPE = UPLOAD_TYPE_LINE;
                setTitleState(UPLOAD_TYPE_LINE);
                loadUI(LINE_STATS);
                checkUrl = LINE_STATS;
                break;
            case R.id.pipe_tv:
                UPLOAD_TYPE = UPLOAD_TYPE_PIPE;
                setTitleState(UPLOAD_TYPE_PIPE);
                loadUI(PIPT_STATS);
                checkUrl = PIPT_STATS;
                break;
            case R.id.hook_tv:
                UPLOAD_TYPE = UPLOAD_TYPE_HOOK;
                setTitleState(UPLOAD_TYPE_HOOK);
//                hookUrl = HOOK_WELL_STATS;
                loadUI(hookUrl);
                break;
            case R.id.monitor_tv:
                UPLOAD_TYPE = UPLOAD_TYPE_MONITOR;
                setTitleState(UPLOAD_TYPE_MONITOR);
                loadUI(jcUrl);
                break;
            case R.id.hook_well_tv:
                UPLOAD_TYPE = UPLOAD_TYPE_HOOK_WELL;
                setTitleState(UPLOAD_TYPE_HOOK_WELL);
                hookUrl = HOOK_WELL_STATS;
                loadUI(hookUrl);
                break;
            case R.id.hook_psdy_tv:
                UPLOAD_TYPE = UPLOAD_TYPE_HOOK_PSDY;
                setTitleState(UPLOAD_TYPE_HOOK_PSDY);
                hookUrl = HOOK_PSDY_STATS;
                loadUI(hookUrl);
                break;
            case R.id.jc_well_tv:
                UPLOAD_TYPE = UPLOAD_TYPE_JBJ;
                setTitleState(UPLOAD_TYPE_JBJ);
                jcUrl = MONITOR_STATS;
                loadUI(jcUrl);
                break;
            case R.id.jc_gjjd_tv:
                UPLOAD_TYPE = UPLOAD_TYPE_GJJD;
                setTitleState(UPLOAD_TYPE_GJJD);
                jcUrl = GJJD_STATS;
                loadUI(jcUrl);
                break;
            default:
                break;
        }
    }

    protected void setTitleState(int type) {
//        checkTv.setBackgroundColor(Color.WHITE);
//        checkTv.setTextColor(primaryColor);
//        problemTv.setBackgroundColor(Color.WHITE);
//        problemTv.setTextColor(primaryColor);
//        journalTv.setBackgroundColor(Color.WHITE);
//        journalTv.setTextColor(primaryColor);
//        hookTv.setBackgroundColor(Color.WHITE);
//        hookTv.setTextColor(primaryColor);
//        monitorTv.setBackgroundColor(Color.WHITE);
//        monitorTv.setTextColor(primaryColor);
//        hookWellTv.setBackgroundColor(Color.WHITE);
//        hookWellTv.setTextColor(primaryColor);
//        hookPsdyTv.setBackgroundColor(Color.WHITE);
//        hookPsdyTv.setTextColor(primaryColor);
//        jcWellTv.setBackgroundColor(Color.WHITE);
//        jcWellTv.setTextColor(primaryColor);
//        jcGjjdTv.setBackgroundColor(Color.WHITE);
//        jcGjjdTv.setTextColor(primaryColor);
        hook_ll.setVisibility(View.GONE);
        jc_ll.setVisibility(View.GONE);
        check_ll.setVisibility(View.GONE);

        if (type == UPLOAD_TYPE_CHECK) {
            check_ll.setVisibility(View.VISIBLE);
            checkTv.setBackgroundColor(primaryColor);
            checkTv.setTextColor(Color.WHITE);
            problemTv.setBackgroundColor(Color.WHITE);
            problemTv.setTextColor(primaryColor);
            journalTv.setBackgroundColor(Color.WHITE);
            journalTv.setTextColor(primaryColor);
            hookTv.setBackgroundColor(Color.WHITE);
            hookTv.setTextColor(primaryColor);
            monitorTv.setBackgroundColor(Color.WHITE);
            monitorTv.setTextColor(primaryColor);
            tvResponsible.setBackgroundColor(Color.WHITE);
            tvResponsible.setTextColor(primaryColor);
//            lineTv.setTextColor(Color.WHITE);
//            lineTv.setBackgroundColor(primaryColor);
//            pipeTv.setTextColor(primaryColor);
//            pipeTv.setBackgroundColor(Color.WHITE);
        } else if (type == UPLOAD_TYPE_PROBLEM) {
            check_ll.setVisibility(View.GONE);
            problemTv.setBackgroundColor(primaryColor);
            problemTv.setTextColor(Color.WHITE);
            checkTv.setBackgroundColor(Color.WHITE);
            checkTv.setTextColor(primaryColor);
            journalTv.setBackgroundColor(Color.WHITE);
            journalTv.setTextColor(primaryColor);
            hookTv.setBackgroundColor(Color.WHITE);
            hookTv.setTextColor(primaryColor);
            monitorTv.setBackgroundColor(Color.WHITE);
            monitorTv.setTextColor(primaryColor);
            tvResponsible.setBackgroundColor(Color.WHITE);
            tvResponsible.setTextColor(primaryColor);
        } else if (type == UPLOAD_TYPE_JOURNAL) {
            check_ll.setVisibility(View.GONE);
            journalTv.setBackgroundColor(primaryColor);
            journalTv.setTextColor(Color.WHITE);
            checkTv.setBackgroundColor(Color.WHITE);
            checkTv.setTextColor(primaryColor);
            problemTv.setBackgroundColor(Color.WHITE);
            problemTv.setTextColor(primaryColor);
            hookTv.setBackgroundColor(Color.WHITE);
            hookTv.setTextColor(primaryColor);
            monitorTv.setBackgroundColor(Color.WHITE);
            monitorTv.setTextColor(primaryColor);
            tvResponsible.setBackgroundColor(Color.WHITE);
            tvResponsible.setTextColor(primaryColor);
        } else if (type == UPLOAD_TYPE_HOOK) {
            check_ll.setVisibility(View.GONE);
            hook_ll.setVisibility(View.VISIBLE);
            hookTv.setBackgroundColor(primaryColor);
            hookTv.setTextColor(Color.WHITE);
            checkTv.setBackgroundColor(Color.WHITE);
            checkTv.setTextColor(primaryColor);
            problemTv.setBackgroundColor(Color.WHITE);
            problemTv.setTextColor(primaryColor);
            journalTv.setBackgroundColor(Color.WHITE);
            journalTv.setTextColor(primaryColor);
            monitorTv.setBackgroundColor(Color.WHITE);
            monitorTv.setTextColor(primaryColor);
            tvResponsible.setBackgroundColor(Color.WHITE);
            tvResponsible.setTextColor(primaryColor);
//            hookWellTv.setTextColor(Color.WHITE);
//            hookWellTv.setBackgroundColor(primaryColor);
//            hookPsdyTv.setTextColor(primaryColor);
//            hookPsdyTv.setBackgroundColor(Color.WHITE);
        }else if(type == UPLOAD_TYPE_RESPONSIBLE){
            check_ll.setVisibility(View.GONE);
            hook_ll.setVisibility(View.GONE);
            hookTv.setBackgroundColor(Color.WHITE);
            hookTv.setTextColor(primaryColor);
            checkTv.setBackgroundColor(Color.WHITE);
            checkTv.setTextColor(primaryColor);
            problemTv.setBackgroundColor(Color.WHITE);
            problemTv.setTextColor(primaryColor);
            journalTv.setBackgroundColor(Color.WHITE);
            journalTv.setTextColor(primaryColor);
            monitorTv.setBackgroundColor(Color.WHITE);
            monitorTv.setTextColor(primaryColor);
            tvResponsible.setBackgroundColor(primaryColor);
            tvResponsible.setTextColor(Color.WHITE);
        } else if (type == UPLOAD_TYPE_HOOK_PSDY) {
            hook_ll.setVisibility(View.VISIBLE);
            hookPsdyTv.setBackgroundColor(primaryColor);
            hookPsdyTv.setTextColor(Color.WHITE);
            hookWellTv.setBackgroundColor(Color.WHITE);
            hookWellTv.setTextColor(primaryColor);
        } else if (type == UPLOAD_TYPE_HOOK_WELL) {
            hook_ll.setVisibility(View.VISIBLE);
            hookWellTv.setBackgroundColor(primaryColor);
            hookWellTv.setTextColor(Color.WHITE);
            hookPsdyTv.setBackgroundColor(Color.WHITE);
            hookPsdyTv.setTextColor(primaryColor);
        } else if (type == UPLOAD_TYPE_MONITOR) {
            check_ll.setVisibility(View.GONE);
            jc_ll.setVisibility(View.VISIBLE);
            monitorTv.setBackgroundColor(primaryColor);
            monitorTv.setTextColor(Color.WHITE);
            hookTv.setBackgroundColor(Color.WHITE);
            hookTv.setTextColor(primaryColor);
            checkTv.setBackgroundColor(Color.WHITE);
            checkTv.setTextColor(primaryColor);
            problemTv.setBackgroundColor(Color.WHITE);
            problemTv.setTextColor(primaryColor);
            journalTv.setBackgroundColor(Color.WHITE);
            journalTv.setTextColor(primaryColor);
            tvResponsible.setBackgroundColor(Color.WHITE);
            tvResponsible.setTextColor(primaryColor);
        } else if (type == UPLOAD_TYPE_JBJ) {
            check_ll.setVisibility(View.GONE);
            jc_ll.setVisibility(View.VISIBLE);
            jcWellTv.setBackgroundColor(primaryColor);
            jcWellTv.setTextColor(Color.WHITE);
            jcGjjdTv.setTextColor(primaryColor);
            jcGjjdTv.setBackgroundColor(Color.WHITE);
        } else if (type == UPLOAD_TYPE_GJJD) {
            check_ll.setVisibility(View.GONE);
            jc_ll.setVisibility(View.VISIBLE);
            jcWellTv.setBackgroundColor(Color.WHITE);
            jcWellTv.setTextColor(primaryColor);
            jcGjjdTv.setTextColor(Color.WHITE);
            jcGjjdTv.setBackgroundColor(primaryColor);
        } else {//点了管线统计和管径统计按钮
            check_ll.setVisibility(View.VISIBLE);
            lineTv.setBackgroundColor(type == UPLOAD_TYPE_LINE ? primaryColor : Color.WHITE);
            lineTv.setTextColor(type == UPLOAD_TYPE_LINE ? Color.WHITE : primaryColor);
            pipeTv.setBackgroundColor(type == UPLOAD_TYPE_LINE ? Color.WHITE : primaryColor);
            pipeTv.setTextColor(type == UPLOAD_TYPE_LINE ? primaryColor : Color.WHITE);

            checkTv.setBackgroundColor(primaryColor);
            checkTv.setTextColor(Color.WHITE);
        }
    }


    protected void loadUI(String url){
        mWebView.loadUrl(url+"?time="+System.currentTimeMillis());
    }
}
