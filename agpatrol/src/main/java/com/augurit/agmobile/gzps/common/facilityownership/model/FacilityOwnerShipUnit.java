package com.augurit.agmobile.gzps.common.facilityownership.model;

import java.util.List;

/**
 * 设施权属单位
 * Created by xcl on 2017/11/27.
 */

public class FacilityOwnerShipUnit {

    /**
     * data : {"parentOrgName":"海珠区住房和建设水务局","parentOrgId":"1151"}
     * rows : [{"parentOrgName":"黄埔区水务局","parentOrgId":"1068"},{"parentOrgName":"荔湾区水务和农业局","parentOrgId":"1072"},{"parentOrgName":"花都区水务局","parentOrgId":"1073"},{"parentOrgName":"越秀区建设和水务局","parentOrgId":"1074"},{"parentOrgName":"增城区水务局","parentOrgId":"1075"},{"parentOrgName":"从化区水务局","parentOrgId":"1076"},{"parentOrgName":"广州市净水有限公司","parentOrgId":"1077"},{"parentOrgName":"天河区住房和建设水务局","parentOrgId":"1066"},{"parentOrgName":"番禺区水务局","parentOrgId":"1067"},{"parentOrgName":"南沙区环保水务局","parentOrgId":"1070"},{"parentOrgName":"白云区水务局","parentOrgId":"1069"}]
     * code : 200
     */


    /**
     * 当前业主单位，如果是市级用户则没有 data 返回
     */
    private OwnerShipUnit data;
    private int code;
    /**
     * 全部业主单位
     */
    private List<OwnerShipUnit> rows;

    public OwnerShipUnit getData() {
        return data;
    }

    public void setData(OwnerShipUnit data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<OwnerShipUnit> getRows() {
        return rows;
    }

    public void setRows(List<OwnerShipUnit> rows) {
        this.rows = rows;
    }


    /**
     * 权属单位
     */
    public static class OwnerShipUnit {
        /**
         * parentOrgName : 黄埔区水务局
         * parentOrgId : 1068
         */

        private String parentOrgName;
        private String parentOrgId;

        public String getParentOrgName() {
            return parentOrgName;
        }

        public void setParentOrgName(String parentOrgName) {
            this.parentOrgName = parentOrgName;
        }

        public String getParentOrgId() {
            return parentOrgId;
        }

        public void setParentOrgId(String parentOrgId) {
            this.parentOrgId = parentOrgId;
        }
    }
}
