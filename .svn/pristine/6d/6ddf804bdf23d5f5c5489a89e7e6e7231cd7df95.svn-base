package com.augurit.agmobile.mapengine.legend.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.legend.model
 * @createTime 创建时间 ：17/7/19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/19
 * @modifyMemo 修改备注：
 */

public class Legend  {


    /**
     * layerName : 城市总体规划
     * legends : [{"legendName":"居住用地","color":"0xFFFF00","lineColor":"0x6B6D6B"},{"legendName":"行政办公用地","color":"0x7B0018","lineColor":"0x6B6D6B"},{"legendName":"文化设施用地","color":"0xFF8200","lineColor":"0x6B6D6B"},{"legendName":"教育科研用地","color":"0xFF829C","lineColor":"0x6B6D6B"}]
     */
    private String layerId;
    private String layerName; //图层名称
    private List<LegendStyle> legends; //图层所包含的图例

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public List<LegendStyle> getLegends() {
        return legends;
    }

    public void setLegends(List<LegendStyle> legends) {
        this.legends = legends;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public static class LegendStyle {
        /**
         * legendName : 居住用地
         * color : 0xFFFF00
         * lineColor : 0x6B6D6B
         */

        private String legendName;
        private String color;
        private String lineColor;

        public String getLegendName() {
            return legendName;
        }

        public void setLegendName(String legendName) {
            this.legendName = legendName;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getLineColor() {
            return lineColor;
        }

        public void setLineColor(String lineColor) {
            this.lineColor = lineColor;
        }

    }
}
