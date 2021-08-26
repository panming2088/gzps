package com.augurit.agmobile.gzps.common.util;

import android.text.TextUtils;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求结果辅助类
 *
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/11/10 11:22
 */
public class RequestResultHelper {

    public static final String RESULT_KEY_CODE = "code";
    public static final String RESULT_KEY_DATA = "data";
    public static final String RESULT_KEY_MSG = "message";

    public static final int RESULT_CODE_SUCCESS = 200;

    public static Result handleResult(String result) {
        return handleResult(result, null);
    }

    public static Result handleResult(String result, String[] extraKeys) {
        try {
            JSONObject jsob = new JSONObject(result);
            int resultCode = JSONHelper.getInt(jsob, RESULT_KEY_CODE);
            if (resultCode == RESULT_CODE_SUCCESS) {
                Object objData = JSONHelper.getObject(jsob, RESULT_KEY_DATA);
                JSONObject jsobData = null;
                JSONArray jsarData = null;
                if (objData instanceof JSONObject) {
                    jsobData = (JSONObject) objData;
                } else if (objData instanceof JSONArray) {
                    jsarData = (JSONArray) objData;
                }
                Result successResult = createSuccessResult(jsobData, jsarData);
                if (extraKeys != null && extraKeys.length > 0) {
                    for (int i = 0; i < extraKeys.length; i++) {
                        String key = extraKeys[i];
                        String value = JSONHelper.getString(jsob, key);
                        successResult.addExtra(key, value);
                    }
                }
                return successResult;
            } else {
                return createFailureResult(JSONHelper.getString(jsob, RESULT_KEY_MSG));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return createFailureResult(e.getMessage());
        }
    }

    private static Result createSuccessResult(JSONObject jsobData, JSONArray jsarData) {
        Result result = new Result();
        result.isSuccess = true;
        result.jsarData = jsarData;
        result.jsobData = jsobData;
        return result;
    }

    private static Result createFailureResult(String errMsg) {
        Result result = new Result();
        result.isSuccess = false;
        result.errMsg = TextUtils.isEmpty(errMsg) ? "" : errMsg;
        return result;
    }

    public static class Result {
        private boolean isSuccess;
        private String errMsg;
        private JSONObject jsobData;
        private JSONArray jsarData;
        private Map<String, String> extras;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public JSONObject getJsobData() {
            return jsobData;
        }

        public void setJsobData(JSONObject jsobData) {
            this.jsobData = jsobData;
        }

        public JSONArray getJsarData() {
            return jsarData;
        }

        public void setJsarData(JSONArray jsarData) {
            this.jsarData = jsarData;
        }

        public Map<String, String> getExtras() {
            return extras;
        }

        public void setExtras(Map<String, String> extras) {
            this.extras = extras;
        }

        public void addExtra(String key, String value) {
            if (extras == null) {
                extras = new HashMap<>();
            }
            extras.put(key, value);
        }
    }
}

/*
{
    "code": 200,
    "data": ,
    "sh": 3,
    "cdw": 0,
    "cy": 0,
    "ydyh": 0,
    "total": 3
}
*/
