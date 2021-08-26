package com.augurit.am.fw.net.api;



import com.augurit.am.fw.net.AMRetrofitManager;

import java.util.HashMap;
import java.util.Map;


/**
 * api 管理类
 * @author 创建人 ：GuoKunhu
 * @package 包名 ：com.augurit.am.fw.net.api
 * @createTime 创建时间 ：2017-02-15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-15
 * @modifyMemo 修改备注：(1)2017-02-15 修改类名，原来是AMApiManager
 * @version 1.0
 */

public class ApiManager {
    //单例对象
    private static ApiManager instance;

    //api 管理集
    Map<Class,Object> apiMap= new HashMap<>();

    private ApiManager(){}

    /**
     * 单例对象获取方法
     * @return
     */
    public static ApiManager getInstance(){
        if(instance == null){
            synchronized (ApiManager.class){
                if(instance == null){
                    instance = new ApiManager();
                }
            }
        }
        return instance;
    }

    /**
     * 注册对应接口API
     * @param clazz
     */
    public void registerApi(Class clazz){
        if(apiMap.containsKey(clazz)){
            return;
        }
        Object serviceObject = AMRetrofitManager.getInstance().getRetrofit().create(clazz);
        if(serviceObject != null){
            apiMap.put(clazz,serviceObject);
        }
        else {
            throw new RuntimeException("get Service api from Retrofit fail!");
        }

    }

    /**
     * 检测是否已经注册了该API接口
     * @param clazz
     * @return
     */
    public boolean isContainsApi(Class clazz){
        return apiMap.containsKey(clazz);
    }

    /**
     * 取消注册对应接口API，网络请求结束后要取消注册，避免内存泄露
     * @param clazz
     */
    public void unregisterApi(Class clazz){
        if(!apiMap.containsKey(clazz)){
            return;
        }
        apiMap.remove(clazz);
    }

    /**
     * 获取对应接口API类型的服务对象
     * @param clazz
     * @return
     */
    public Object getServiceApi(Class clazz){
        if(!apiMap.containsKey(clazz)){
            Object serviceObject = AMRetrofitManager.getInstance().getRetrofit().create(clazz);
            if(serviceObject != null) {
                apiMap.put(clazz, serviceObject);
                return serviceObject;
            }else {
                return null;
            }
        }
        return apiMap.get(clazz);
    }


    /**
     * 清除所有已注册的API接口
     */
    public void clearApi(){
        apiMap.clear();

    }

    /**
     * 清除当前单例对象
     */
    public void clearInstance(){
        instance = null;
    }

}
