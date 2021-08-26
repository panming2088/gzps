package com.augurit.agmobile.gzps.uploadevent.util;

import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.tasks.util
 * @createTime 创建时间 ：2017-05-24
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-05-24
 * @modifyMemo 修改备注：
 */

public class TaskParamUtil {

    public static String getDZBTaskParamJson(String loginName, String templateCode, String pageNo, String pageSize){
        Map<String, Object> param = new HashMap<>();
        param.put("loginName", loginName);
        param.put("groupBy", null);
        param.put("groupDir", null);

        Map<String, String> jsonSubParam = new HashMap<>();
        jsonSubParam.put("keyword", null);
        jsonSubParam.put("templateCode", templateCode);
        jsonSubParam.put("createEndDate", null);
        jsonSubParam.put("createStartDate", null);
        jsonSubParam.put("procStartdate", null);
        jsonSubParam.put("procEnddate", null);

        Map<String, String> pageSubParam = new HashMap<>();
        pageSubParam.put("pageNo", pageNo);
        pageSubParam.put("pageSize", pageSize);
        pageSubParam.put("orderBy", "create");
        pageSubParam.put("orderDir", "desc");

        param.put("json", jsonSubParam);
        param.put("page", pageSubParam);

        return JsonUtil.getJson(param);
    }

    public static String getEventParamJson(String loginName, String templateCode, String pageNo, String pageSize){
        if(StringUtil.isEmpty(templateCode)){
            templateCode = "GX_XCYH";
        }
        Map<String, Object> param = new HashMap<>();
        param.put("loginName", loginName);
        param.put("groupBy", null);
        param.put("groupDir", null);
        param.put("flag", 1);

        Map<String, String> jsonSubParam = new HashMap<>();
        jsonSubParam.put("keyword", null);
        jsonSubParam.put("templateCode", templateCode);
        jsonSubParam.put("createEndDate", null);
        jsonSubParam.put("createStartDate", null);
        jsonSubParam.put("procStartdate", null);
        jsonSubParam.put("procEnddate", null);

        Map<String, String> pageSubParam = new HashMap<>();
        pageSubParam.put("pageNo", pageNo);
        pageSubParam.put("pageSize", pageSize);
        pageSubParam.put("orderBy", "create");
        pageSubParam.put("orderDir", "desc");

        param.put("json", jsonSubParam);
        param.put("page", pageSubParam);

        return JsonUtil.getJson(param);
    }

    public static String getEventJsonParam(String templateCode){
        Map<String, String> jsonSubParam = new HashMap<>();
        jsonSubParam.put("keyword", null);
        jsonSubParam.put("templateCode", templateCode);
        jsonSubParam.put("createEndDate", null);
        jsonSubParam.put("createStartDate", null);
        jsonSubParam.put("procStartdate", null);
        jsonSubParam.put("procEnddate", null);
        return JsonUtil.getJson(jsonSubParam).replace("\\", "");
    }

    public static String getEventPageParam(String pageNo, String pageSize){
        Map<String, String> pageSubParam = new HashMap<>();
        pageSubParam.put("pageNo", pageNo);
        pageSubParam.put("pageSize", pageSize);
        pageSubParam.put("orderBy", "create");
        pageSubParam.put("orderDir", "desc");
        return JsonUtil.getJson(pageSubParam).replace("\\", "");
    }
}
