package com.augurit.agmobile.gzps.uploadfacility.model;

/**
 * @author 创建人 ：ouyangzhibao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzpssb.bean
 * @createTime 创建时间 ：2018/4/25
 * @modifyBy 修改人 ：ouyangzhibao
 * @modifyTime 修改时间 ：2018/4/25
 * @modifyMemo 修改备注：
 */
public class BuildInfoBySGuid {
    /**
     * code : 200
     * data : {"structure":"","houseUse":"","floor":2,"houseType":"","sets":4,"houseProperty":"出租屋"}
     * message : 查询成功!
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public  class DataBean {
        /**
         * structure :
         * houseUse :
         * floor : 2
         * houseType :
         * sets : 4
         * houseProperty : 出租屋
         *
         * 房屋使用性质:houseProperty
         房屋结构:structure
         房屋类别:houseType
         房屋用途:houseUse
         总层数：floor
         总套数：sets
         */

        private String structure;
        private String houseUse;
        private String floor;
        private String houseType;
        private String sets;
        private String houseProperty;

        public String getStructure() {
            return structure;
        }

        public void setStructure(String structure) {
            this.structure = structure;
        }

        public String getHouseUse() {
            return houseUse;
        }

        public void setHouseUse(String houseUse) {
            this.houseUse = houseUse;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public String getSets() {
            return sets;
        }

        public void setSets(String sets) {
            this.sets = sets;
        }

        public String getHouseType() {
            return houseType;
        }

        public void setHouseType(String houseType) {
            this.houseType = houseType;
        }


        public String getHouseProperty() {
            return houseProperty;
        }

        public void setHouseProperty(String houseProperty) {
            this.houseProperty = houseProperty;
        }
    }
}
