package com.augurit.agmobile.gzps.lawsandregulation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.webview.WebViewActivity;
import com.augurit.agmobile.patrolcore.common.preview.view.PhotoPagerActivity;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.ListUtil;

import java.util.ArrayList;

/**
 * Created by liangsh on 2017/11/8.
 */

public class LawsAndRegulationsFragment extends Fragment {

    private Context mContext;

    public static LawsAndRegulationsFragment getInstance(String url) {
        LawsAndRegulationsFragment lawsAndRegulationsFragment = new LawsAndRegulationsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        lawsAndRegulationsFragment.setArguments(bundle);
        return lawsAndRegulationsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context != null) {
            this.mContext = context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null){
            this.mContext = activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }

    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient() {
            //??????shouldOverrideUrlLoading????????????????????????
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings seting = mWebView.getSettings();
        seting.setJavaScriptEnabled(true);//??????webview??????javascript??????
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("xxxoo", url);
                addImageClickListner();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    //                    pg1.setVisibility(View.GONE);//??????????????????????????????
//                } else {
//                    //                    pg1.setVisibility(View.VISIBLE);//????????????????????????????????????
//                    //                    pg1.setProgress(newProgress);//???????????????
//                }

            }

        });
        //??????????????????,???WebView????????????(???????????????Activity???onKeyDown??????)
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {  //??????????????????????????????

                        ((Activity) mContext).finish();
                        // mWebView.goBack();   //??????
                        //webview.goForward();//??????
                        return true;    //?????????
                    }
                }
                return false;
            }
        });
        String url = getArguments().getString("url") + "?userId=" + BaseInfoManager.getUserId
                (getContext());
        mWebView.setDownloadListener(new MyDownloadStart());
        mWebView.addJavascriptInterface(new JavascriptInterface(mContext), "imagelistner");
        mWebView.loadUrl(url);
    }

    class MyDownloadStart implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            //???????????????????????????
//          new HttpThread(url).start();
            //???????????????????????????
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    // ??????js????????????
    private void addImageClickListner() {
        Log.d("TAG", "addImageClickListner: ????????????");

        // ??????js???????????????????????????????????????img??????????????????onclick?????????????????????????????????????????????????????????url??????
        mWebView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\");"
                + "var imgurl=''; "
                + "for(var i=0;i<objs.length;i++)  "
                + "{"
                + "objs[i].pos = i;"
                + "imgurl+=objs[i].src+',';"
                + "    objs[i].onclick=function()  "
                + "    {  "
                + "        imagelistner.openImage(imgurl,this.pos);  "
                + "    }  " + "}" + "})()");
    }

    // js????????????
    public class JavascriptInterface {
        private Context context;
        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface  //?????? ????????????????????????
        public void openImage(String img, int pos) {
            System.out.println(img);
            Log.d("TAG", "openImage-------------------------: " + img);
            String[] imgs = img.split(",");
            ArrayList<Photo> imgsUrl = new ArrayList<Photo>();
            for (String s : imgs) {
                Photo photo = new Photo();
                photo.setPhotoPath(s);
                imgsUrl.add(photo);
                Log.d("TAG", "openImage-------------------------: " + s);
            }
            if (!ListUtil.isEmpty(imgsUrl)) {
                Intent intent = new Intent(mContext, PhotoPagerActivity.class);
                intent.putExtra("BITMAPLIST", imgsUrl);
                intent.putExtra("POSITION", pos);
                startActivity(intent);
            }
        }
    }
}
