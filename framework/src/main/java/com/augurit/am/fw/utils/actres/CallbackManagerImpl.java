package com.augurit.am.fw.utils.actres;

import android.content.Intent;

import com.augurit.am.fw.utils.log.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangsh on 2016-11-09.
 * 用于各个模块Activity启动Activity后获取到返回结果
 * // Make sure the static handler for login is registered if there isn't an explicit callback
 * //要使用的时候进行注册
    CallbackManagerImpl.registerStaticCallback( 123456,
            new CallbackManagerImpl.Callback() {
        @Override
        public boolean onActivityResult(int resultCode, Intent data) {
            //这里就写自己模块对返回结果的处理

        }
    });
 *
 *
 * //在主Activity中
 * 在Activity中:
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        callbackManager = CallbackManager.Factory.create();
    }
 ​
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
 */
public class CallbackManagerImpl implements CallbackManager {


    private static Map<Integer, Callback> staticCallbacks = new HashMap<>();

    /**
     * If there is no explicit callback, but we still need to call the Facebook component,
     * because it's going to update some state, e.g., login, like. Then we should register a
     * static callback that can still handle the response.
     *
     * @param requestCode The request code.
     * @param callback    The callback for the feature.
     */
    public synchronized static void registerStaticCallback(
            int requestCode,
            Callback callback) throws Exception {
//        Validate.notNull(callback, "callback");
        if (callback == null) {
            throw new Exception("callback is null");
        }
        //xcl 2017-01-04 如果存在，直接覆盖
        if (staticCallbacks.containsKey(requestCode)) {
            LogUtil.d("requestCode:"+requestCode +"已经添加过一次了");
        }
        staticCallbacks.put(requestCode, callback);
    }

    public synchronized static void unregisterStaticCallback(int requestCode){
        staticCallbacks.remove(requestCode);
    }

    private static synchronized Callback getStaticCallback(Integer requestCode) {
        return staticCallbacks.get(requestCode);
    }


    private static boolean runStaticCallback(
            int requestCode,
            int resultCode,
            Intent data) {
        Callback callback = getStaticCallback(requestCode);
        if (callback != null) {
            return callback.onActivityResult(resultCode, data);
        }
        return false;
    }


    private Map<Integer, Callback> callbacks = new HashMap<>();


    public void registerCallback(int requestCode, Callback callback) throws Exception {
//        Validate.notNull(callback, "callback");
        if (callback == null) {
            throw new Exception("callback is null");
        }
        if (callbacks.containsKey(requestCode)) {
            throw new Exception("requestCode has been added");
        }
        callbacks.put(requestCode, callback);
    }

    public void unregisterCallback(int requestCode){
        callbacks.remove(requestCode);
    }



    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Callback callback = callbacks.get(requestCode);
        if (callback != null) {
            return callback.onActivityResult(resultCode, data);
        }
        return runStaticCallback(requestCode, resultCode, data);
    }


    public interface Callback {
        boolean onActivityResult(int resultCode, Intent data);
    }

}
