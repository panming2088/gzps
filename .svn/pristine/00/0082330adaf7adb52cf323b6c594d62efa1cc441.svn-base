package com.augurit.agmobile.mapengine.common.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.model
 * @createTime 创建时间 ：2017-03-27
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-27
 * @modifyMemo 修改备注：
 */
public class SpatialBufferResult implements Serializable{


    private List<BufferGeometry> bufferGeometry;
    private List<FeatureSet> featureSets;


    public List<BufferGeometry> getBufferGeometry() {
        return bufferGeometry;
    }

    public void setBufferGeometry(List<BufferGeometry> bufferGeometry) {
        this.bufferGeometry = bufferGeometry;
    }

    public List<FeatureSet> getFeatureSets() {
        return featureSets;
    }

    public void setFeatureSets(List<FeatureSet> featureSets) {
        this.featureSets = featureSets;
    }

    public static class BufferGeometry implements Serializable {
        private String type;
        private String wkt;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWkt() {
            return wkt;
        }

        public void setWkt(String wkt) {
            this.wkt = wkt;
        }
    }

}
