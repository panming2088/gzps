package com.augurit.agmobile.mapengine.project.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Agweb专题图层接口返回的实体类
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.layer.model
 * @createTime 创建时间 ：2017-07-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-07-11
 * @modifyMemo 修改备注：
 */

public class AgwebProjectLayer {

    /**
     * result : [{"projectName":"专题一","layers":[{"dirTypeName":"多规合一","addFlag":"2","id":"c3564f95-f0b5-4e79-bf02-fdb95ca9a117","isBaseMap":"0","layerTable":"城市总体规划","layerType":"020202","name":"城市总体规划","orderNm":"3","shortCut":"","url":"http://192.168.19.96:6080/arcgis/rest/services/yuanqu/pygasbsi4/MapServer","projectlayerId":12,"defaultvisibility":1,"remarkfunc":3,"isQueryable":1,"initExtent":""}],"id":"117","center":"41529464.826900005,4626959.24095","extent":"4.14519539168E7,4562617.0016,4.1566975737E7,4767301.4803","reference":"西安80","scale":"270.98828125,135.494140625,67.7470703125,33.87353515625,16.936767578125,8.4683837890625,4.23419189453125,2.117095947265625,1.0585479736328125,0.5292739868164062,0.2646369934082031,0.13231849670410156","zoom":5}]
     * success : true
     */

    private boolean success;
    private List<AgwebProject> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<AgwebProject> getResult() {
        return result;
    }

    public void setResult(List<AgwebProject> result) {
        this.result = result;
    }

    public static class AgwebProject {
        /**
         * projectName : 专题一
         * layers : [{"dirTypeName":"多规合一","addFlag":"2","id":"c3564f95-f0b5-4e79-bf02-fdb95ca9a117","isBaseMap":"0","layerTable":"城市总体规划","layerType":"020202","name":"城市总体规划","orderNm":"3","shortCut":"","url":"http://192.168.19.96:6080/arcgis/rest/services/yuanqu/pygasbsi4/MapServer","projectlayerId":12,"defaultvisibility":1,"remarkfunc":3,"isQueryable":1,"initExtent":""}]
         * id : 117
         * center : 41529464.826900005,4626959.24095
         * extent : 4.14519539168E7,4562617.0016,4.1566975737E7,4767301.4803
         * reference : 西安80
         * scale : 270.98828125,135.494140625,67.7470703125,33.87353515625,16.936767578125,8.4683837890625,4.23419189453125,2.117095947265625,1.0585479736328125,0.5292739868164062,0.2646369934082031,0.13231849670410156
         * zoom : 5
         */

        private String projectname;
        private String id;
        private String center;
        private String extent;
        private String reference;

        private String scales;
        private int zoom;
        private List<AgwebLayerInfo> layers;

        public String getProjectName() {
            return projectname;
        }

        public void setProjectName(String projectName) {
            this.projectname = projectName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCenter() {
            return center;
        }

        public void setCenter(String center) {
            this.center = center;
        }

        public String getExtent() {
            return extent;
        }

        public void setExtent(String extent) {
            this.extent = extent;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getScale() {
            return scales;
        }

        public void setScale(String scale) {
            this.scales = scale;
        }

        public int getZoom() {
            return zoom;
        }

        public void setZoom(int zoom) {
            this.zoom = zoom;
        }

        public List<AgwebLayerInfo> getLayers() {
            return layers;
        }

        public void setLayers(List<AgwebLayerInfo> layers) {
            this.layers = layers;
        }

        public static class AgwebLayerInfo {
            /**
             * dirTypeName : 多规合一
             * addFlag : 2
             * id : c3564f95-f0b5-4e79-bf02-fdb95ca9a117
             * isBaseMap : 0
             * layerTable : 城市总体规划
             * layerType : 020202
             * name : 城市总体规划
             * orderNm : 3
             * shortCut :
             * url : http://192.168.19.96:6080/arcgis/rest/services/yuanqu/pygasbsi4/MapServer
             * defaultvisibility : 1
             * remarkfunc : 3
             * isQueryable : 1
             * initExtent :
             */

            private String dirTypeName;
            private String addFlag;

            @SerializedName("cachekey")
            private int id;

            private String isBaseMap;
            private String layerTable;
            private String layerType;
            private String name;
            private String orderNm;
            private String shortCut;
            private String url;

            private String defaultVisibility;
            private int remarkfunc;
            private String isQueryable;

            @SerializedName("initExtent")
            private String initScale;

            @SerializedName("ifShowInlist")
            private String ifShowInList;


            //private double initScale;
            @SerializedName("centerPoint")
            private String center;

            public String getCenter() {
                return center;
            }

            public void setCenter(String center) {
                this.center = center;
            }

            public String getIfShowInList() {
                return ifShowInList;
            }

            public void setIfShowInList(String ifShowInList) {
                this.ifShowInList = ifShowInList;
            }

            public String getDirTypeName() {
                return dirTypeName;
            }

            public void setDirTypeName(String dirTypeName) {
                this.dirTypeName = dirTypeName;
            }

            public String getAddFlag() {
                return addFlag;
            }

            public void setAddFlag(String addFlag) {
                this.addFlag = addFlag;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIsBaseMap() {
                return isBaseMap;
            }

            public void setIsBaseMap(String isBaseMap) {
                this.isBaseMap = isBaseMap;
            }

            public String getLayerTable() {
                return layerTable;
            }

            public void setLayerTable(String layerTable) {
                this.layerTable = layerTable;
            }

            public String getLayerType() {
                return layerType;
            }

            public void setLayerType(String layerType) {
                this.layerType = layerType;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOrderNm() {
                return orderNm;
            }

            public void setOrderNm(String orderNm) {
                this.orderNm = orderNm;
            }

            public String getShortCut() {
                return shortCut;
            }

            public void setShortCut(String shortCut) {
                this.shortCut = shortCut;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

           /* public int getProjectlayerId() {
                return projectlayerId;
            }

            public void setProjectlayerId(int projectlayerId) {
                this.projectlayerId = projectlayerId;
            }*/

            public String getDefaultvisibility() {
                return defaultVisibility;
            }

            public void setDefaultvisibility(String defaultvisibility) {
                this.defaultVisibility = defaultvisibility;
            }

            public int getRemarkfunc() {
                return remarkfunc;
            }

            public void setRemarkfunc(int remarkfunc) {
                this.remarkfunc = remarkfunc;
            }

            public String getIsQueryable() {
                return isQueryable;
            }

            public void setIsQueryable(String isQueryable) {
                this.isQueryable = isQueryable;
            }

            public String getInitScale() {
                return initScale;
            }

            public void setInitScale(String initExtent) {
                this.initScale = initExtent;
            }

        }
    }
}
