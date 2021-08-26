package com.augurit.agmobile.gzps.common.util;

import android.content.Context;

import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.util.ValidateUtils;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzpssb.common
 * @createTime 创建时间 ：2018-09-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-09-11
 * @modifyMemo 修改备注：
 */

public class PortSelectUtil {

    private final static String AG_SUPPORT_PORT_CODE = "A181";
    private final static String AG_WEB_PORT_CODE = "A192";
    private static TableDBService dbService;

    public static String getAgSupportPortBaseURL(Context context){
     return LoginConstant.UPLOAD_AGSUPPORT + getAgSupportPort(context) + LoginConstant.UPLOAD_POSTFIX + "/";
    }





    public static String getAgWebPortBaseURL(Context context){
        return LoginConstant.UPLOAD_AGSUPPORT + getAgWebPort(context) + LoginConstant.UPLOAD_AGWEB + "/";
    }

    /**
     * UPLOAD_AGSUPPORT
     * 端口选择
     */
    private static String getAgSupportPort(Context context) {
        String port = LoginConstant.pshUploadPort;//默认的端口
        List<String> ports = getRandomPort(context,AG_SUPPORT_PORT_CODE );
        if (!ListUtil.isEmpty(ports)) {
            port = ports.get(new Random().nextInt(ports.size()));
        }
        return port;
    }


    /**
     * UPLOAD_WEB
     * 端口选择
     */
    private static String getAgWebPort(Context context) {
        String port = LoginConstant.pshAGWEBPort;//默认的端口
        List<String> ports = getRandomPort(context,AG_WEB_PORT_CODE );
            if (!ListUtil.isEmpty(ports)) {
                port = ports.get(new Random().nextInt(ports.size()));
            }
        return port;
    }


    private static List<String> getRandomPort( Context context ,String dicCode) {
        if(null == dbService){
            dbService = new TableDBService(context.getApplicationContext());
        }
        List<DictionaryItem> dictionaryItems = dbService.getDictionaryByTypecodeInDB(dicCode);//数据字典端口
        ArrayList<String> ports = new ArrayList<>();
        if (!ListUtil.isEmpty(dictionaryItems)) {
            for (DictionaryItem dict :dictionaryItems) {
                if (!StringUtil.isEmpty(dict.getName())
                        && ValidateUtils.isInteger(dict.getName())) {
                    ports.add(":" + dict.getName());
                }
            }
        }
     return ports;
    }
}