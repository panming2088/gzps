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
            //覆写shouldOverrideUrlLoading实现内部显示网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings seting = mWebView.getSettings();
        seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本
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
//                    //                    pg1.setVisibility(View.GONE);//加载完网页进度条消失
//                } else {
//                    //                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
//                    //                    pg1.setProgress(newProgress);//设置进度值
//                }

            }

        });
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {  //表示按返回键时的操作

                        ((Activity) mContext).finish();
                        // mWebView.goBack();   //后退
                        //webview.goForward();//前进
                        return true;    //已处理
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
            //调用自己的下载方式
//          new HttpThread(url).start();
            //调用系统浏览器下载
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    // 注入js函数监听
    private void addImageClickListner() {
        Log.d("TAG", "addImageClickListner: 注入代码");

        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
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

    // js通信接口
    public class JavascriptInterface {
        private Context context;
        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface  //注解 很重要，必不可少
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
