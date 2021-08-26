package com.augurit.agmobile.mapengine.common;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.base.BaseMapListener;
import com.esri.android.map.MapView;

/**
 * 地图操作事件管理者，在使用前请使用它进行注册监听事件
 *
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.map
 * @createTime 创建时间 ：2016-11-17
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-17
 */

public class MapListenerManager {

    //   Stack<MapOnTouchListener> mListeners = new Stack<>();

    private static MapListenerManager sMapListenerManager;
    private static BaseMapListener mBaseMapListener;
    private MapView mMapView;

    private MapListenerManager(MapView mapView) {
        this.mMapView = mapView;
    }

    public static MapListenerManager getInstance(MapView mapView) {
        if (sMapListenerManager == null) {
            synchronized (MapListenerManager.class) {
                sMapListenerManager = new MapListenerManager(mapView);
            }
        }
        return sMapListenerManager;
    }
    //      private Map<Class, BaseMapListener> mListenerMap = new ArrayMap<>();

      /*public void registerListener(BaseMapListener mapOnTouchListener){
           // mMapOnTouchListeners.add(mapOnTouchListener);
            if(mapOnTouchListener == null){
                  return;
            }
            if (!mListenerMap.containsValue(mapOnTouchListener)) {
                  mListenerMap.put(mapOnTouchListener.getClass(), mapOnTouchListener);
            }
            this.mMapView.setOnTouchListener(mapOnTouchListener);
      }*/

    public static void clearInstance() {
        if(mBaseMapListener!=null) {
            mBaseMapListener.onDestroy();
        }
        mBaseMapListener = null;
        sMapListenerManager = null;
    }

    public void registerIdentifyListener(BaseMapListener rxIdentifyMapListener) {
        mBaseMapListener = rxIdentifyMapListener;
        this.mMapView.setOnTouchListener(mBaseMapListener);
    }

    /**
     * 允许点查监听
     *
     * @param context
     */
    public void enableIdentifyListener(Context context) {
        if(mBaseMapListener!=null){
            mMapView.setOnTouchListener(mBaseMapListener);
            mBaseMapListener.enableRealMapTouch();
        }
    }

    /**
     * 禁止点查监听
     *
     * @param context
     */
    public void disableIdentifyListener(Context context) {
        if(mBaseMapListener != null){
            mBaseMapListener.disableRealMapTouch();
        }
    }
      /*public BaseMapListener getRegisteredListener(Class listenerClass) {
            return mListenerMap.get(listenerClass);
      }*/

    public void clearMap() {
        if(mBaseMapListener != null){
            mBaseMapListener.clearMap();
        }
    }
}
