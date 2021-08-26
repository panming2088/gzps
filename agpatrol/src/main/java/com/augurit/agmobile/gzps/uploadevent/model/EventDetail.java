package com.augurit.agmobile.gzps.uploadevent.model;

import java.io.Serializable;
import java.util.List;

/**
 * 事件详情
 *
 * Created by xcl on 2017/11/11.
 */

public class EventDetail implements Serializable {
    /**
     * taskInstId :
     * eventState : 0
     * curNode : 任务分发
     * curOpLoginName : limf
     * event : {"addr":"","x":"","y":"","road":"","componentType":"","eventType":"","urgency":"","description":"","time":1234575,"userName":"","remark":"","files":[{"name":"","path":"","mime":"","time":123456789},{"name":"","path":"","mime":"","time":123456789}]}
     * opinion : [{"userName":"","opinion":"","time":12686132}]
     * nextlink : [{"linkname":""},{"linkname":""}]
     */

    private String taskInstId;
    private String eventState;   //当前处理人的事件处理状态
    private String state;        //整个流程的状态：active处理中；ended已办结
    private String curNode;
    private String curNodeName;
    private String curOpLoginName;
    private EventBean event;
   // private String procInstDbId;
    private List<OpinionBean> opinion;
    private List<NextlinkBean> nextlink;

    private boolean isRetrieve;//是否可以撤回

    private boolean isDeleteTask;// 是否可以删除流程

    private boolean isEditAble;//是否可编辑

    public boolean isEditAble() {
        return isEditAble;
    }

    public void setEditAble(boolean editAble) {
        isEditAble = editAble;
    }

    public boolean isDeleteTask() {
        return isDeleteTask;
    }

    public void setDeleteTask(boolean deleteTask) {
        isDeleteTask = deleteTask;
    }

    public String getTaskInstId() {
        return taskInstId;
    }

    /*
    public String getProcInstDbId() {
        return procInstDbId;
    }

    public void setProcInstDbId(String procInstDbId) {
        this.procInstDbId = procInstDbId;
    }
    */

    public boolean isRetrieve() {
        return isRetrieve;
    }

    public void setRetrieve(boolean retrieve) {
        isRetrieve = retrieve;
    }

    public void setTaskInstId(String taskInstId) {
        this.taskInstId = taskInstId;
    }

    public String getEventState() {
        return eventState;
    }

    public void setEventState(String eventState) {
        this.eventState = eventState;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCurNode() {
        return curNode;
    }

    public void setCurNode(String curNode) {
        this.curNode = curNode;
    }

    public String getCurNodeName() {
        return curNodeName;
    }

    public void setCurNodeName(String curNodeName) {
        this.curNodeName = curNodeName;
    }

    public String getCurOpLoginName() {
        return curOpLoginName;
    }

    public void setCurOpLoginName(String curOpLoginName) {
        this.curOpLoginName = curOpLoginName;
    }

    public EventBean getEvent() {
        return event;
    }

    public void setEvent(EventBean event) {
        this.event = event;
    }

    public List<OpinionBean> getOpinion() {
        return opinion;
    }

    public void setOpinion(List<OpinionBean> opinion) {
        this.opinion = opinion;
    }

    public List<NextlinkBean> getNextlink() {
        return nextlink;
    }

    public void setNextlink(List<NextlinkBean> nextlink) {
        this.nextlink = nextlink;
    }

    public static class EventBean implements Serializable{
        /**
         * addr :
         * x :
         * y :
         * road :
         * componentType :
         * eventType :
         * urgency :
         * description :
         * time : 1234575
         * userName :
         * remark :
         * files : [{"name":"","path":"","mime":"","time":123456789},{"name":"","path":"","mime":"","time":123456789}]
         */

        private long reportId;
        private String addr; //SZWZ
        private String x;
        private String y;
        private String road; //JDMC 街道名称
        private String layerurl;
        private String layerId; //layer_id
        private String layerName;//layer_name
        private String objectId;// object_id
        private String usid; //usid 部件标识码
        private String componentType; //SSLX 设施类型
        private String eventType; //   BHLX 病害类型
        private String urgency;
        private String description; //WTMS 问题描述
        private long time;
        private String userName;
        private String remark;
        private String reportx;
        private String reporty;
        private String reportaddr;
        private String parentOrgId;
        private String parentOrgName;
        private String isbyself; //isbyself 是否自行处理：true、false
        private String sfjb;//是否交办
        private Long yjwcsj;//预计处理完成时间
        private List<FilesBean> files;

        public long getReportId() {
            return reportId;
        }

        public void setReportId(long reportId) {
            this.reportId = reportId;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getRoad() {
            return road;
        }

        public void setRoad(String road) {
            this.road = road;
        }

        public String getLayerurl() {
            return layerurl;
        }

        public void setLayerurl(String layerurl) {
            this.layerurl = layerurl;
        }

        public String getLayerId() {
            return layerId;
        }

        public void setLayerId(String layerId) {
            this.layerId = layerId;
        }

        public String getLayerName() {
            return layerName;
        }

        public void setLayerName(String layerName) {
            this.layerName = layerName;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getUsid() {
            return usid;
        }

        public void setUsid(String usid) {
            this.usid = usid;
        }

        public String getComponentType() {
            return componentType;
        }

        public void setComponentType(String componentType) {
            this.componentType = componentType;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public String getUrgency() {
            return urgency;
        }

        public void setUrgency(String urgency) {
            this.urgency = urgency;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getReportx() {
            return reportx;
        }

        public void setReportx(String reportx) {
            this.reportx = reportx;
        }

        public String getReporty() {
            return reporty;
        }

        public void setReporty(String reporty) {
            this.reporty = reporty;
        }

        public String getReportaddr() {
            return reportaddr;
        }

        public void setReportaddr(String reportaddr) {
            this.reportaddr = reportaddr;
        }

        public String getParentOrgId() {
            return parentOrgId;
        }

        public void setParentOrgId(String parentOrgId) {
            this.parentOrgId = parentOrgId;
        }

        public String getParentOrgName() {
            return parentOrgName;
        }

        public void setParentOrgName(String parentOrgName) {
            this.parentOrgName = parentOrgName;
        }

        public String getIsbyself() {
            return isbyself;
        }

        public void setIsbyself(String isbyself) {
            this.isbyself = isbyself;
        }

        public String getSfjb() {
            return sfjb;
        }

        public void setSfjb(String sfjb) {
            this.sfjb = sfjb;
        }

        public Long getYjwcsj() {
            return yjwcsj;
        }

        public void setYjwcsj(Long yjwcsj) {
            this.yjwcsj = yjwcsj;
        }

        public List<FilesBean> getFiles() {
            return files;
        }

        public void setFiles(List<FilesBean> files) {
            this.files = files;
        }

        public static class FilesBean implements Serializable{
            /**
             * name :
             * path :
             * mime :
             * time : 123456789
             */

            private String name;
            private String path;
            private String thumbPath;
            private String mime;
            private long time;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getThumbPath() {
                return thumbPath;
            }

            public void setThumbPath(String thumbPath) {
                this.thumbPath = thumbPath;
            }

            public String getMime() {
                return mime;
            }

            public void setMime(String mime) {
                this.mime = mime;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }
        }
    }

    public static class OpinionBean implements Serializable{
        /**
         * userName :
         * opinion :
         * time : 12686132
         */

        private String userName;
        private String opinion;
        private long time;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getOpinion() {
            return opinion;
        }

        public void setOpinion(String opinion) {
            this.opinion = opinion;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    /**
     * 下一环节
     */
    public static class NextlinkBean implements Serializable {
        /**
         * linkname :
         */

        private String linkname;
        private String linkcode;

        public String getLinkname() {
            return linkname;
        }

        public void setLinkname(String linkname) {
            this.linkname = linkname;
        }

        public String getLinkcode() {
            return linkcode;
        }

        public void setLinkcode(String linkcode) {
            this.linkcode = linkcode;
        }
    }

}
