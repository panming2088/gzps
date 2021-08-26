package com.augurit.agmobile.gzps.uploadfacility.util;

import android.text.TextUtils;

import com.augurit.agmobile.gzps.publicaffair.PublicAffairActivity;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.util.Date;

/**
 * Created by xucil on 2018/1/3.
 */

public class FacilityAffairQueryUtil {

    public static String getWhere(String uploadType, String facilityType, String district
            ,Long startTime,Long endTime){
        if(district.contains("净水")){
            district = "净水";
        }
        String where = "";
        //核准类型筛选
        if (!TextUtils.isEmpty(uploadType) && !uploadType.equals("全部")) {
            if (uploadType.equals(PublicAffairActivity.uploadTypeConditions[1])) {
                where += "REPORT_TYPE = 'add'";
            } else if (uploadType.equals(PublicAffairActivity.uploadTypeConditions[2])) {
                where += "REPORT_TYPE = 'modify'";
            }
        }
        //设施类型筛选
        if (!TextUtils.isEmpty(facilityType) && !facilityType.equals("全部")) {
            if (TextUtils.isEmpty(where)) {
                where += "LAYER_NAME = '" + facilityType + "'";
            } else {
                where += "and  LAYER_NAME = '" + facilityType + "'";
            }
        }

        //当不是全市查询时加入区域限制
        if (!district.contains("全部")) {
            if (TextUtils.isEmpty(where)) {
                where += " PARENT_ORG_NAME like '%" + district + "%'";
            } else {
                where += "and PARENT_ORG_NAME like '%" + district + "%'";
            }
        }

        //加入时间筛选
        if (startTime != null && endTime != null && startTime != 0 && endTime != 0){
            String startYMD = TimeUtil.getStringTimeYMD(new Date(startTime));
            String endYMD = TimeUtil.getStringTimeYMD(new Date(endTime));
            if (startYMD != null && endYMD != null){
                if (TextUtils.isEmpty(where)) {
                    where += " MARK_TIME > date '" + startYMD + "' and MARK_TIME < date '" + endYMD + "'";
                } else {
                    where += "and MARK_TIME > date '" + startYMD + "' and MARK_TIME < date '" + endYMD + "'";
                }
            }
        }
        LogUtil.e("okhttp",where);
        return where;
    }
}
