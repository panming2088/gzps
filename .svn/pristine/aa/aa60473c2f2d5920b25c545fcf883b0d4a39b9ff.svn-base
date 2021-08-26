package com.augurit.agmobile.mapengine.panorama.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.mapengine.panorama.utils.LoadType;
import com.panoramagl.PLView;
import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.loaders.PLILoader;
import com.panoramagl.loaders.PLJSONLoader;
import com.panoramagl.transitions.PLTransitionBlend;

/**
 * 全景视图抽象类
 * Created by GuoKunHu on 2016-06-07.
 */
public abstract class BasePanoActivity extends PLView {

    //全景数据地址
    public String url ;

    //全景名称
    public String name;

    public TextView tvPanoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置全景中的事件监听器
     //   this.setListener(PanoViewPresenter.getInstance(this).getPlViewListener());

    }

    //响应全景视图创建，可从中添加其他视图控件
    @Override
    protected View onContentViewCreated(View contentView) {
        url = getIntent().getStringExtra("pano_url");
        name = getIntent().getStringExtra("pano_name");

        //加载布局
        ViewGroup mainView = (ViewGroup) this.getLayoutInflater().inflate(getLayout(), null);
        //添加全景视图
        mainView.addView(contentView, 0);

        //从布局获取视图实例
        initViews(mainView);

       //为视图添加事件响应
        addEventListeners();

        loadPanoramaFromNet(LoadType.HIGH,url);

        return super.onContentViewCreated(mainView);
    }

    /**
     * 从网络上加载全景数据并且显示
     *
     * @param index  1为清晰度低  2 为清晰度高
     * @param url   全景数据的地址
     */
    public void loadPanoramaFromNet(int index, String url){
        try {
            PLILoader loader = null;
            switch (index) {
                case LoadType.LOWER:
                    // loader = new PLJSONLoader("http://192.168.19.96:8080/raw/json_spherical2.data");
                    loader = new PLJSONLoader(url);
                    break;
                case LoadType.HIGH:
                    //   loader = new PLJSONLoader("http://192.168.19.96:8080/raw/json_spherical.data");
                    loader = new PLJSONLoader(url);
                    loader.setCallback(new PLHotspot.Callback() {
                        @Override
                        public void onClickHotspot(final String s) {
                            if(tvPanoName != null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvPanoName.setText(s);
                                    }
                                });

                            }
                        }
                    });
                    break;
                default:
                    break;
            }
            if(loader != null){
                this.load(loader,true,new PLTransitionBlend(2.0f));
            }
        }catch (Throwable e){
            e.getLocalizedMessage();
        }
    }


    /**
     * 从本地加载全景数据文件
     * @param index 数据序号
     */
    public void loadPanoramaFromLocal(int index){
        try
        {
            PLILoader loader = null;
            switch(index)
            {
                case 0:
                    loader = new PLJSONLoader("res://raw/json_cubic");
                    break;
                case 1:
                    loader = new PLJSONLoader("res://raw/json_spherical2");
                    break;
                case 2:
                    loader = new PLJSONLoader("res://raw/json_spherical");
                    break;
                case 3:
                    loader = new PLJSONLoader("res://raw/json_cylindrical");
                    break;
                default:
                    break;
            }
            if(loader != null)
                this.load(loader, true, new PLTransitionBlend(2.0f));
        }

        catch(Throwable e)
        {
            Toast.makeText(this.getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

     //初始化全景图上面控件
    public abstract void initViews(ViewGroup mainView);


     // 控件事件处理
    public abstract void addEventListeners();


     // 获取布局资源
    public abstract int getLayout();


    /**
     * Load panorama image using JSON protocol
     *
     * @param index Spinner position where 0 = cubic, 1 = spherical2, 2 = spherical, 3 = cylindrical
     */
    /*
    @Deprecated
    private void loadPanoramaFromJSON(int index) {
        try {
            PLILoader loader = null;
            switch (index) {
                case 0:
                    loader = new PLJSONLoader("http://192.168.1.103:8080/panoramagl/server/json_cubic.data");
                    break;
                case 1:
                    loader = new PLJSONLoader("http://192.168.1.103:8080/panoramagl/server/json_spherical2.data");
                    break;
                case 2:
                    loader = new PLJSONLoader("http://192.168.1.103:8080/panoramagl/server/json_spherical.data");
                    break;
                case 3:
                    loader = new PLJSONLoader("http://192.168.1.103:8080/panoramagl/server/json_cylindrical.data");
                    break;
                default:
                    break;
            }
            if (loader != null)
                this.load(loader, true, new PLTransitionBlend(2.0f));
        } catch (Throwable e) {
            Toast.makeText(this.getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    */



}
