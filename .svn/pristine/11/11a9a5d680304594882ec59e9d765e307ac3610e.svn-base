package com.augurit.agmobile.mapengine.panorama.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.panoramagl.PLViewListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.agmobile.mapengine.panorama.utils
 * @createTime 创建时间 ：17/1/18
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 17/1/18
 */

public class PanoViewUtils {
    //单例对象
    public static PanoViewUtils instance;
    public Context mContext;

    //工具集
    public Map<Integer, View.OnClickListener> tools;

    //全景事件监听器
    private PLViewListener plViewListener;

    //工具所在视图容器
    private ViewGroup mainView;

    //工具所在布局id
    private int layoutId;

    //全景类对象
    private Class mPanoClass;

    private PanoViewUtils(Context context){
        this.mContext = context;

        tools = new HashMap<Integer, View.OnClickListener>();
    }

    private PanoViewUtils(Context context,Class clazz){
        this.mContext = context;
        this.mPanoClass = clazz;

        tools = new HashMap<Integer, View.OnClickListener>();
    }

    public static  PanoViewUtils getInstance(Context context,Class clazz){
        if(instance == null){
            synchronized (PanoViewUtils.class){
                if(instance == null){
                    instance = new PanoViewUtils(context,clazz);
                }
            }
        }
        return instance;
    }

    /**
     * 全景工具注册函数
     * @param id 工具控件在布局文件中的id
     * @param listener 工具控件的点击事件
     */
    public void registerTool(int id, View.OnClickListener listener) {
        tools.put(id, listener);
    }

    /**
     * 添加工具事件响应
     */
    public void addToolEvents(ViewGroup mainView) {
        if(this.mainView == null)
            this.mainView = mainView;

        for (Map.Entry<Integer,View.OnClickListener> entry : tools.entrySet()){
            mainView.findViewById(entry.getKey()).setOnClickListener(entry.getValue());
        }
    }

    /**
     * 根据视图id 获取自定义工具控件视图
     * @param id
     * @return
     */
    public View getToolViewById(int id){
        if(this.mainView == null){
            throw new RuntimeException("tool main view not exist !");
        }
        View toolView = null ;
        for(Map.Entry<Integer,View.OnClickListener> entry : tools.entrySet()){
            if(entry.getKey() == id){
                toolView = mainView.findViewById(id);
                break;
            }
        }
        return toolView;
    }

    /**
     * 根据视图id获取自定义工具控件视图点击响应监听器
     * @param id
     * @return
     */
    public View.OnClickListener getToolOnClickListenerById(int id){
        if(this.mainView == null){
            throw new RuntimeException("tool main view not exist !");
        }
        View.OnClickListener listener = null;
        for(Map.Entry<Integer,View.OnClickListener> entry : tools.entrySet()){
            if(entry.getKey()== id){
                listener = entry.getValue();
                break;
            }
        }
        return listener;
    }

    /**
     * 进入全景视图
     */
    public void startPanoView(String url,String name) {
        if(mPanoClass != null) {
            Intent intent = new Intent(mContext,mPanoClass);
            intent.putExtra("pano_url",url);
            intent.putExtra("pano_name",name);
            mContext.startActivity(intent);
        }
    }

    /**
     * 设置工具布局id
     * @param id
     */
    public void setToolLayoutId(int id){
        this.layoutId = id;
    }

    /**
     * 获取工具布局id
     * @return
     */
    public int  getToolLayoutId(){
        return this.layoutId;
    }

    /**
     * 清理内存资源
     */
    public void destory() {
        tools.clear();
        tools=null;
        instance = null;
    }

}
