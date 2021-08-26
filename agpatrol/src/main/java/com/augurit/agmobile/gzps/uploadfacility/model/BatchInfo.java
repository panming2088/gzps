package com.augurit.agmobile.gzps.uploadfacility.model;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.model
 * @createTime 创建时间 ：2018-06-04
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-06-04
 * @modifyMemo 修改备注：
 */

public class BatchInfo {
    private int total;
    private int code;
    private List<Info> rows;


    public class Info {
        private long id;
        private String parentOrgName;
        private String assignName;
        private String assignDate;
        private String assignId;
        private int counts;
        private String assignPerson;
        private String aid;

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj1) {
            if (obj1 == null)
                return false;

            if (this.getClass() != obj1.getClass())
                return false;
            Info obj = (Info) obj1;
            return this.getAssignId().equals(obj.getAssignId())
                    &&this.getAssignName().equals(obj.getAssignName())
                    &&this.getAssignDate().equals(obj.getAssignDate())
                    &&this.getAssignPerson().equals(obj.getAssignPerson())
                    &&this.getCounts() == obj.getCounts()
                    &&this.parentOrgName.equals(obj.getParentOrgName());
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getParentOrgName() {
            return parentOrgName;
        }

        public void setParentOrgName(String parentOrgName) {
            this.parentOrgName = parentOrgName;
        }

        public String getAssignName() {
            return assignName;
        }

        public void setAssignName(String assignName) {
            this.assignName = assignName;
        }

        public String getAssignDate() {
            return assignDate;
        }

        public void setAssignDate(String assignDate) {
            this.assignDate = assignDate;
        }

        public String getAssignId() {
            return assignId;
        }

        public void setAssignId(String assignId) {
            this.assignId = assignId;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public String getAssignPerson() {
            return assignPerson;
        }

        public void setAssignPerson(String assignPerson) {
            this.assignPerson = assignPerson;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Info> getRows() {
        return rows;
    }

    public void setRows(List<Info> rows) {
        this.rows = rows;
    }
}
