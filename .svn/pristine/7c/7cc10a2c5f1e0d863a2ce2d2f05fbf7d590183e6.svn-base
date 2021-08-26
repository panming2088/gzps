package com.augurit.agmobile.gzps.jbjpsdy.util;

import android.text.TextUtils;

import com.augurit.agmobile.gzps.common.util.JSONHelper;
import com.augurit.agmobile.gzps.jbjpsdy.SewerageUserEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * 排水户实体类辅助类
 *
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/11/10 11:18
 */
public class SewerageUserEntityHelper {
    public static SewerageUserEntity parseJsonObject(JSONObject jsob) {
        String id = JSONHelper.getString(jsob, "id");
        if (!TextUtils.isEmpty(id)) {
            SewerageUserEntity sewerageUser = new SewerageUserEntity(id);
            sewerageUser.setHouseHolderName(JSONHelper.getString(jsob, "markPerson"));
            sewerageUser.setBelong(JSONHelper.getString(jsob, "parentOrgName"));
            sewerageUser.setAddrName(JSONHelper.getString(jsob, "name"));
            sewerageUser.setAddr(JSONHelper.getString(jsob, "addr"));
            sewerageUser.setTypeStr(JSONHelper.getString(jsob, "dischargerType1"));
            sewerageUser.setType2Str(JSONHelper.getString(jsob, "dischargerType2"));
            sewerageUser.setTime(JSONHelper.getLong(jsob, "markTime"));
            sewerageUser.setLongitude(JSONHelper.getDouble(jsob, "x"));
            sewerageUser.setLatitude(JSONHelper.getDouble(jsob, "y"));
            sewerageUser.setImgUrl(JSONHelper.getString(jsob, "imgPath"));
            return sewerageUser;
        }
        return null;
    }
}

/*
{
    "markPerson": "林崇涛",
    "mph": "柑田花果山路8号",
    "parentOrgName": "增城区水务局",
    "rowno": 1,
    "addr": "增城区派潭镇柑田花果山路8号",
    "id": 1987562,
    "dischargerType1": "生活排污类",
    "imgPath": "http://139.159.243.185:8081/img/pshFile/imgSmall/202009/20200911/kexkcsdx6_1599787807413_1599787785_1_.jpg",
    "name": "居民住宅",
    "dischargerType2": "居民住宅",
    "markTime": 1541249739000,
    "y": 23.45614298,
    "x": 113.72450283
}
*/
