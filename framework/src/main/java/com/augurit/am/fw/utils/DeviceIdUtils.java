package com.augurit.am.fw.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.augurit.am.fw.utils.file.SharedPreferencesUtil;

import java.util.UUID;

import static com.augurit.am.fw.utils.StringUtil.isEmpty;

/**
 * 描述：android获取设备唯一标识
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.fw.utils
 * @createTime 创建时间 ：2017/8/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/8/7
 * @modifyMemo 修改备注：
 */

public class DeviceIdUtils {
    public static String getDeviceId(Context context){
        StringBuilder deviceId = new StringBuilder();
        try{
            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if(!isEmpty(wifiMac)){
                deviceId.append("wifi");
                deviceId.append(wifiMac);
            }
            if(!isEmpty(deviceId.toString())){
                return deviceId.toString();
            }


            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if(!isEmpty(imei)){
                deviceId.append("imei");
                deviceId.append(imei);
            }
            if(!isEmpty(deviceId.toString())){
                return deviceId.toString();
            }

            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if(!isEmpty(sn)){
                deviceId.append("sn");
                deviceId.append(sn);
            }
            if(!isEmpty(deviceId.toString())){
                return deviceId.toString();
            }
            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if(!isEmpty(uuid)){
                deviceId.append("id");
                deviceId.append(uuid);
            }

        }catch (Exception e){
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }

        return deviceId.toString();
    }


    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context){
        SharedPreferencesUtil pref = new SharedPreferencesUtil(context);
        String uuid = null;
        if(pref != null){
            uuid = pref.getString("uuid", "");
        }
        if(isEmpty(uuid)){
            uuid = UUID.randomUUID().toString();
            pref.setString("uuid",uuid);
        }
        return uuid;
    }
}
