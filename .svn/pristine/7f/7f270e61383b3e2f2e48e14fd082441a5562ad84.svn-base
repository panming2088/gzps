package com.augurit.am.fw.utils.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.am.fw.R;
import com.augurit.am.fw.utils.JsonUtil;


/**
 * Created by ac on 2016-07-26.
 */
public class ToastUtil {

    public final static String FROM = ">>>";
    public final static String DIVIDER = "--";
    public final static String PRIFIX = "::";
    public static final String URLDOT = ".";

    public static void shortToast(Context context, int StringId) {
        Toast.makeText(context, StringId, Toast.LENGTH_SHORT).show();
    }

    public static void iconLongToast(Context context,int mipmapId,int Stringid){

        if (context == null){
            return;
        }
        //使用带图标的Toast提示显示
        Toast toast = new Toast(context.getApplicationContext());
        //设置Tosat的属性，如显示时间
        toast.setDuration(Toast.LENGTH_LONG);
        View view1 = View.inflate(context, R.layout.view_toast,null);
        ((TextView)view1.findViewById(R.id.tv_toast_text)).setText(Stringid);
        ((ImageView)view1.findViewById(R.id.iv_icon)).setImageResource(mipmapId);
        //将布局管理器添加进Toast
        toast.setView(view1);
        //显示提示
        toast.show();
    }
    public static void iconLongToast(Context context,int mipmapId,String toastString){
        if (context == null){
            return;
        }
        //使用带图标的Toast提示显示
        Toast toast = new Toast(context.getApplicationContext());
        //设置Tosat的属性，如显示时间
        toast.setDuration(Toast.LENGTH_LONG);
        View view1 = View.inflate(context, R.layout.view_toast,null);
        ((TextView)view1.findViewById(R.id.tv_toast_text)).setText(toastString);
        ((ImageView)view1.findViewById(R.id.iv_icon)).setImageResource(mipmapId);
        //将布局管理器添加进Toast
        toast.setView(view1);
        //显示提示
        toast.show();
    }

    public static void iconShortToast(Context context,int mipmapId,String toastString){
        if (context == null){
            return;
        }
        //使用带图标的Toast提示显示
        Toast toast = new Toast(context.getApplicationContext());
        //设置Tosat的属性，如显示时间
        toast.setDuration(Toast.LENGTH_SHORT);
        View view1 = View.inflate(context, R.layout.view_toast,null);
        ((TextView)view1.findViewById(R.id.tv_toast_text)).setText(toastString);
        ((ImageView)view1.findViewById(R.id.iv_icon)).setImageResource(mipmapId);
        //将布局管理器添加进Toast
        toast.setView(view1);
        //显示提示
        toast.show();
    }

    public static void shortToast(Context context, String toastString) {
        if (context == null){
            return;
        }
        //使用带图标的Toast提示显示
        Toast toast = new Toast(context.getApplicationContext());
        //设置Tosat的属性，如显示时间
        toast.setDuration(Toast.LENGTH_SHORT);
        View view1 = View.inflate(context, R.layout.view_toast,null);
        ((TextView)view1.findViewById(R.id.tv_toast_text)).setText(toastString);
        ((ImageView)view1.findViewById(R.id.iv_icon)).setImageResource(R.mipmap.ic_alert_yellow);
        //将布局管理器添加进Toast
        toast.setView(view1);
        //显示提示
        toast.show();
    }

    public static void longToast(Context context, String toastString) {
        if (context == null){
            return;
        }
        //使用带图标的Toast提示显示
        Toast toast = new Toast(context.getApplicationContext());
        //设置Tosat的属性，如显示时间
        toast.setDuration(Toast.LENGTH_LONG);
        View view1 = View.inflate(context, R.layout.view_toast,null);
        ((TextView)view1.findViewById(R.id.tv_toast_text)).setText(toastString);
        ((ImageView)view1.findViewById(R.id.iv_icon)).setImageResource(R.mipmap.ic_alert_yellow);
        //将布局管理器添加进Toast
        toast.setView(view1);
        //显示提示
        toast.show();
    }

    public static void longToast(Context context, int StringId) {
        if (context == null){
            return;
        }
        Toast.makeText(context, StringId, Toast.LENGTH_LONG).show();
    }

    public static void shortToast(Context context, Object informationObject) {
        if (context == null){
            return;
        }
        // String sdfsd = LogUtil.getLogString(informationObject);
        // System.out.println(sdfsd);
        shortToast(context, getLogString(informationObject));
    }


    public static void shortToast(Context context, Object prefixObject,
                                  // , String informationPrefixString,
                                  Object informationObject) {
        if (context == null){
            return;
        }
        shortToast(context, getLogJsonString(
                // tagObject,
                prefixObject, informationObject));
    }

    public void longToast(Context context, Object informationObject) {
        if (context == null){
            return;
        }
        longToast(context, getLogString(informationObject));
    }

    public void longToast(Context context, Object prefixObject,
                          Object informationObject) {
        if (context == null){
            return;
        }
        longToast(context,
                getLogJsonString(prefixObject, informationObject));
    }

    public static String getLogString(Object informationObject) {

        return getLogString(null, informationObject);
    }

    public static String getLogJsonString(Object prefixObject,
                                          Object informationObject) {
        return getLogString(prefixObject, informationObject);
    }

    protected static String getLogString(Object prefixObject,
                                         Object informationObject, int adjustIndex) {
        String informationString = getStringFromObject(informationObject);
        if (prefixObject != null) {
            return getStringFromObject(prefixObject) + PRIFIX
                    + informationString + getLogFromString(adjustIndex);
        }
        return informationString + getLogFromString(adjustIndex);
    }

    protected static String getLogString(Object prefixObject,
                                         Object informationObject) {
        String informationString = getStringFromObject(informationObject);
        if (prefixObject != null) {
            return getStringFromObject(prefixObject) + PRIFIX
                    + informationString + getLogFromString(0);
        }
        return informationString + getLogFromString(0);
    }

    private static String getLogFromString(int adjustIndex) {
        adjustIndex += 7;
        return FROM
                + Thread.currentThread().getStackTrace()[adjustIndex]
                .getClassName()
                + URLDOT
                + Thread.currentThread().getStackTrace()[adjustIndex]
                .getMethodName();
    }

    private static String getStringFromObject(Object object) {
        String resultString;
        if (object instanceof String) {
            resultString = (String) object;
        } else {
            try {
                resultString = JsonUtil.getJson(object);
            } catch (Exception e) {
                e.printStackTrace();
                resultString = "该类不符合规范，尝试传字符串类型";
            }
        }
        return resultString;
    }

}
