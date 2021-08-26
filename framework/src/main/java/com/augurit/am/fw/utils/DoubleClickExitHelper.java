package com.augurit.am.fw.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;

import com.augurit.am.fw.utils.view.ToastUtil;

/**
 * 双击退出应用工具类
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.fw.utils
 * @createTime 创建时间 ：2016-11-17
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-17
 */

public class DoubleClickExitHelper {
    private final Activity mActivity;

    private boolean isOnKeyBacking;
    private Handler mHandler;
   // private Snackbar snackbar;

    public DoubleClickExitHelper(Activity activity) {
        mActivity = activity;
        mHandler = new Handler(Looper.getMainLooper());
        AppManager.getAppManager().addActivity(mActivity);
    }

    /**
     * Activity onKeyDown事件
     */
    public boolean onKeyDown(int keyCode, AppExitListener appExitListener) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false;
        }
        if (isOnKeyBacking) {
            mHandler.removeCallbacks(onBackTimeRunnable);
           /* if (snackbar != null) {
                snackbar.dismiss();
            }*/
           if(appExitListener != null){
               appExitListener.beforeExit();
           }
            AppManager.getAppManager().AppExit(mActivity);
            return true;
        } else {
            isOnKeyBacking = true;
           /* if (snackbar == null) {
                snackbar = Snackbar.make(view, "再次点击退出应用", 2000);
            }
            snackbar.show();*/
            ToastUtil.shortToast(mActivity.getApplicationContext(),"再次点击退出应用");
            mHandler.postDelayed(onBackTimeRunnable, 2000);
            return true;
        }
    }

    /**
     * Activity onKeyDown事件
     */
    public boolean onKeyDown(int keyCode) {
        return onKeyDown(keyCode, null);
    }

    private Runnable onBackTimeRunnable = new Runnable() {
        @Override
        public void run() {
            isOnKeyBacking = false;
           /* if (snackbar != null) {
                snackbar.dismiss();
            }*/
        }
    };

    public void clearAppManager(){
        AppManager.clearInstance();
    }

    public interface AppExitListener {
        void beforeExit();
    }

}
