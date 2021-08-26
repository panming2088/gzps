package com.augurit.agmobile.gzps.common.util;

import android.content.Context;


import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.fw.utils.common.ValidateUtil;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.util
 * @createTime 创建时间 ：2017-03-31
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-31
 * @modifyMemo 修改备注：
 */

public class PatrolUtil {

    public static String getDiseaseNameByDiseaseId(Context context,String diseaseId){

       /* String type = "";
        switch (diseaseId){
            case "A005001":
                type = "缺失";
                break;
            case "A005002":
                type = "沉陷";
                break;
            case "A005003":
                type = "松动";
                break;
            case "A005004":
                type = "相邻不平顺";
                break;
            case "A005005":
                type = "裂缝、脱皮、风化";
                break;
            case "A005006":
                type = "盲道被占用、不规范";
                break;
            case "A005007":
                type = "盲条、盲点磨损";
                break;
            case "A005008":
                type = "无障碍坡道不规范";
                break;
            case "A005009":
                type = "其他";
                break;
            case "A005010":
                type = "缺失";
                break;
            default:
                type ="未知类型";
                break;
        }*/

        TableDBService tableDBService = new TableDBService(context);
        List<DictionaryItem> dictionaryByCode = tableDBService.getDictionaryByCode(diseaseId);
        if (ValidateUtil.isListNull(dictionaryByCode)){
            return "未知";
        }
        return dictionaryByCode.get(0).getName();
    }
}
