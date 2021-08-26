package com.augurit.agmobile.mapengine.layermanage.model;

import java.util.List;


/**
 * AGPAD中的图层信息类
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layer.model
 * @createTime 创建时间 ：2016-10-14 13:57
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 13:57
 */
public class AgcomLayerInfo {


    private List<SupLayersBean> supLayers;
    private List<TiledLayerBean> tiledLayer;
    private List<MultiPlanVectorLayerBean> vectorLayer;

    public List<SupLayersBean> getSupLayers() {
        return supLayers;
    }

    public void setSupLayers(List<SupLayersBean> supLayers) {
        this.supLayers = supLayers;
    }

    public List<TiledLayerBean> getTiledLayer() {
        return tiledLayer;
    }

    public void setTiledLayer(List<TiledLayerBean> tiledLayer) {
        this.tiledLayer = tiledLayer;
    }

    public List<MultiPlanVectorLayerBean> getVectorLayer() {
        return vectorLayer;
    }

    public void setVectorLayer(List<MultiPlanVectorLayerBean> vectorLayer) {
        this.vectorLayer = vectorLayer;
    }

    public static class SupLayersBean {
        /**
         * activeInBs :
         * annoFeatureShow :
         * annoField :
         * annostring :
         * annostringReplace :
         * bsLayerFileName :
         * cache_url :
         * count :
         * defaultShow :
         * dirOrder : 0
         * dirTypeName :
         * dpi :
         * editable :
         * featureType :
         * fzLayerFileName :
         * groupField1 :
         * groupField2 :
         * have_set_field :
         * have_set_layer :
         * id : 0
         * isBaseMap :
         * isMatched : 0
         * is_use_cache :
         * key_name :
         * layerId : 273
         * layerOrder : 0
         * layerType :
         * layer_extent :
         * layer_table :
         * maxLabelScale : 0
         * maxScale : 0
         * minLabelScale : 0
         * minScale : 0
         * name :
         * origin :
         * power_code :
         * resolution :
         * selectable :
         * serial_name :
         * server_name :
         * server_state :
         * showLabel :
         * showScore : 0
         * srs :
         * style :
         * suffix :
         * table :
         * themeField :
         * tileSize :
         * tileType :
         * tile_url :
         * wms_layers :
         * defaultStyle : {"fillAlpha":"","fillColor":"","id":"1","name":"默认点样式","outlineArrow":"","outlineColor":"","outlineSize":"","outlineStyle":"","symbol":"c100.gif","type":"1"}
         * taggingStyle : {"fillAlpha":"","fillColor":"","id":"4","name":"默认标注样式","outlineArrow":"","outlineColor":"FF0000","outlineSize":"12","outlineStyle":"","symbol":"","type":"5"}
         */

        private String activeInBs;
        private String annoFeatureShow;
        private String annoField;
        private String annostring;
        private String annostringReplace;
        private String bsLayerFileName;
        private String cache_url;
        private String count;
        private String defaultShow;
        private int dirOrder;
        private String dirTypeName;
        private String dpi;
        private String editable;
        private String featureType;
        private String fzLayerFileName;
        private String groupField1;
        private String groupField2;
        private String have_set_field;
        private String have_set_layer;
        private int id;
        private String isBaseMap;
        private int isMatched;
        private String is_use_cache;
        private String key_name;
        private int layerId;
        private int layerOrder;
        private String layerType;
        private String layer_extent;
        private String layer_table;
        private int maxLabelScale;
        private int maxScale;
        private int minLabelScale;
        private int minScale;
        private String name;
        private String origin;
        private String power_code;
        private String resolution;
        private String selectable;
        private String serial_name;
        private String server_name;
        private String server_state;
        private String showLabel;
        private int showScore;
        private String srs;
        private String style;
        private String suffix;
        private String table;
        private String themeField;
        private String tileSize;
        private String tileType;
        private String tile_url;
        private String wms_layers;
        private DefaultStyleBean defaultStyle;
        private TaggingStyleBean taggingStyle;

        public String getActiveInBs() {
            return activeInBs;
        }

        public void setActiveInBs(String activeInBs) {
            this.activeInBs = activeInBs;
        }

        public String getAnnoFeatureShow() {
            return annoFeatureShow;
        }

        public void setAnnoFeatureShow(String annoFeatureShow) {
            this.annoFeatureShow = annoFeatureShow;
        }

        public String getAnnoField() {
            return annoField;
        }

        public void setAnnoField(String annoField) {
            this.annoField = annoField;
        }

        public String getAnnostring() {
            return annostring;
        }

        public void setAnnostring(String annostring) {
            this.annostring = annostring;
        }

        public String getAnnostringReplace() {
            return annostringReplace;
        }

        public void setAnnostringReplace(String annostringReplace) {
            this.annostringReplace = annostringReplace;
        }

        public String getBsLayerFileName() {
            return bsLayerFileName;
        }

        public void setBsLayerFileName(String bsLayerFileName) {
            this.bsLayerFileName = bsLayerFileName;
        }

        public String getCache_url() {
            return cache_url;
        }

        public void setCache_url(String cache_url) {
            this.cache_url = cache_url;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getDefaultShow() {
            return defaultShow;
        }

        public void setDefaultShow(String defaultShow) {
            this.defaultShow = defaultShow;
        }

        public int getDirOrder() {
            return dirOrder;
        }

        public void setDirOrder(int dirOrder) {
            this.dirOrder = dirOrder;
        }

        public String getDirTypeName() {
            return dirTypeName;
        }

        public void setDirTypeName(String dirTypeName) {
            this.dirTypeName = dirTypeName;
        }

        public String getDpi() {
            return dpi;
        }

        public void setDpi(String dpi) {
            this.dpi = dpi;
        }

        public String getEditable() {
            return editable;
        }

        public void setEditable(String editable) {
            this.editable = editable;
        }

        public String getFeatureType() {
            return featureType;
        }

        public void setFeatureType(String featureType) {
            this.featureType = featureType;
        }

        public String getFzLayerFileName() {
            return fzLayerFileName;
        }

        public void setFzLayerFileName(String fzLayerFileName) {
            this.fzLayerFileName = fzLayerFileName;
        }

        public String getGroupField1() {
            return groupField1;
        }

        public void setGroupField1(String groupField1) {
            this.groupField1 = groupField1;
        }

        public String getGroupField2() {
            return groupField2;
        }

        public void setGroupField2(String groupField2) {
            this.groupField2 = groupField2;
        }

        public String getHave_set_field() {
            return have_set_field;
        }

        public void setHave_set_field(String have_set_field) {
            this.have_set_field = have_set_field;
        }

        public String getHave_set_layer() {
            return have_set_layer;
        }

        public void setHave_set_layer(String have_set_layer) {
            this.have_set_layer = have_set_layer;
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

        public int getIsMatched() {
            return isMatched;
        }

        public void setIsMatched(int isMatched) {
            this.isMatched = isMatched;
        }

        public String getIs_use_cache() {
            return is_use_cache;
        }

        public void setIs_use_cache(String is_use_cache) {
            this.is_use_cache = is_use_cache;
        }

        public String getKey_name() {
            return key_name;
        }

        public void setKey_name(String key_name) {
            this.key_name = key_name;
        }

        public int getLayerId() {
            return layerId;
        }

        public void setLayerId(int layerId) {
            this.layerId = layerId;
        }

        public int getLayerOrder() {
            return layerOrder;
        }

        public void setLayerOrder(int layerOrder) {
            this.layerOrder = layerOrder;
        }

        public String getLayerType() {
            return layerType;
        }

        public void setLayerType(String layerType) {
            this.layerType = layerType;
        }

        public String getLayer_extent() {
            return layer_extent;
        }

        public void setLayer_extent(String layer_extent) {
            this.layer_extent = layer_extent;
        }

        public String getLayer_table() {
            return layer_table;
        }

        public void setLayer_table(String layer_table) {
            this.layer_table = layer_table;
        }

        public int getMaxLabelScale() {
            return maxLabelScale;
        }

        public void setMaxLabelScale(int maxLabelScale) {
            this.maxLabelScale = maxLabelScale;
        }

        public int getMaxScale() {
            return maxScale;
        }

        public void setMaxScale(int maxScale) {
            this.maxScale = maxScale;
        }

        public int getMinLabelScale() {
            return minLabelScale;
        }

        public void setMinLabelScale(int minLabelScale) {
            this.minLabelScale = minLabelScale;
        }

        public int getMinScale() {
            return minScale;
        }

        public void setMinScale(int minScale) {
            this.minScale = minScale;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getPower_code() {
            return power_code;
        }

        public void setPower_code(String power_code) {
            this.power_code = power_code;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getSelectable() {
            return selectable;
        }

        public void setSelectable(String selectable) {
            this.selectable = selectable;
        }

        public String getSerial_name() {
            return serial_name;
        }

        public void setSerial_name(String serial_name) {
            this.serial_name = serial_name;
        }

        public String getServer_name() {
            return server_name;
        }

        public void setServer_name(String server_name) {
            this.server_name = server_name;
        }

        public String getServer_state() {
            return server_state;
        }

        public void setServer_state(String server_state) {
            this.server_state = server_state;
        }

        public String getShowLabel() {
            return showLabel;
        }

        public void setShowLabel(String showLabel) {
            this.showLabel = showLabel;
        }

        public int getShowScore() {
            return showScore;
        }

        public void setShowScore(int showScore) {
            this.showScore = showScore;
        }

        public String getSrs() {
            return srs;
        }

        public void setSrs(String srs) {
            this.srs = srs;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public String getThemeField() {
            return themeField;
        }

        public void setThemeField(String themeField) {
            this.themeField = themeField;
        }

        public String getTileSize() {
            return tileSize;
        }

        public void setTileSize(String tileSize) {
            this.tileSize = tileSize;
        }

        public String getTileType() {
            return tileType;
        }

        public void setTileType(String tileType) {
            this.tileType = tileType;
        }

        public String getTile_url() {
            return tile_url;
        }

        public void setTile_url(String tile_url) {
            this.tile_url = tile_url;
        }

        public String getWms_layers() {
            return wms_layers;
        }

        public void setWms_layers(String wms_layers) {
            this.wms_layers = wms_layers;
        }

        public DefaultStyleBean getDefaultStyle() {
            return defaultStyle;
        }

        public void setDefaultStyle(DefaultStyleBean defaultStyle) {
            this.defaultStyle = defaultStyle;
        }

        public TaggingStyleBean getTaggingStyle() {
            return taggingStyle;
        }

        public void setTaggingStyle(TaggingStyleBean taggingStyle) {
            this.taggingStyle = taggingStyle;
        }

        public static class DefaultStyleBean {
            /**
             * fillAlpha :
             * fillColor :
             * id : 1
             * name : 默认点样式
             * outlineArrow :
             * outlineColor :
             * outlineSize :
             * outlineStyle :
             * symbol : c100.gif
             * type : 1
             */

            private String fillAlpha;
            private String fillColor;
            private String id;
            private String name;
            private String outlineArrow;
            private String outlineColor;
            private String outlineSize;
            private String outlineStyle;
            private String symbol;
            private String type;

            public String getFillAlpha() {
                return fillAlpha;
            }

            public void setFillAlpha(String fillAlpha) {
                this.fillAlpha = fillAlpha;
            }

            public String getFillColor() {
                return fillColor;
            }

            public void setFillColor(String fillColor) {
                this.fillColor = fillColor;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOutlineArrow() {
                return outlineArrow;
            }

            public void setOutlineArrow(String outlineArrow) {
                this.outlineArrow = outlineArrow;
            }

            public String getOutlineColor() {
                return outlineColor;
            }

            public void setOutlineColor(String outlineColor) {
                this.outlineColor = outlineColor;
            }

            public String getOutlineSize() {
                return outlineSize;
            }

            public void setOutlineSize(String outlineSize) {
                this.outlineSize = outlineSize;
            }

            public String getOutlineStyle() {
                return outlineStyle;
            }

            public void setOutlineStyle(String outlineStyle) {
                this.outlineStyle = outlineStyle;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class TaggingStyleBean {
            /**
             * fillAlpha :
             * fillColor :
             * id : 4
             * name : 默认标注样式
             * outlineArrow :
             * outlineColor : FF0000
             * outlineSize : 12
             * outlineStyle :
             * symbol :
             * type : 5
             */

            private String fillAlpha;
            private String fillColor;
            private String id;
            private String name;
            private String outlineArrow;
            private String outlineColor;
            private String outlineSize;
            private String outlineStyle;
            private String symbol;
            private String type;

            public String getFillAlpha() {
                return fillAlpha;
            }

            public void setFillAlpha(String fillAlpha) {
                this.fillAlpha = fillAlpha;
            }

            public String getFillColor() {
                return fillColor;
            }

            public void setFillColor(String fillColor) {
                this.fillColor = fillColor;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOutlineArrow() {
                return outlineArrow;
            }

            public void setOutlineArrow(String outlineArrow) {
                this.outlineArrow = outlineArrow;
            }

            public String getOutlineColor() {
                return outlineColor;
            }

            public void setOutlineColor(String outlineColor) {
                this.outlineColor = outlineColor;
            }

            public String getOutlineSize() {
                return outlineSize;
            }

            public void setOutlineSize(String outlineSize) {
                this.outlineSize = outlineSize;
            }

            public String getOutlineStyle() {
                return outlineStyle;
            }

            public void setOutlineStyle(String outlineStyle) {
                this.outlineStyle = outlineStyle;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }

    public static class TiledLayerBean {
        /**
         * annoField :
         * bsLayerFileName :
         * cache_url :
         * defaultShow : 1
         * dirOrder : 0
         * dirTypeName : 底图
         * dpi :
         * featureType : 5
         * have_set_field :
         * have_set_layer : 1
         * id : 332
         * isBaseMap :
         * is_use_cache : 0
         * key_name :
         * layerId : 236
         * layerOrder : 2
         * layerType : 7
         * layer_extent :
         * layer_table : cva
         * maxLabelScale : 0
         * maxScale : 0
         * minLabelScale : 0
         * minScale : 0
         * name : 矢量标注
         * origin :
         * power_code :
         * resolution :
         * selectable : 1
         * serial_name : SEQ_FOR_ARCSDE
         * server_name :
         * server_state :
         * srs : c
         * style :
         * suffix : tiles
         * tileSize : 256
         * tileType :
         * tile_url : http://t0.tianditu.com/cva_c/wmts
         * token :
         * wms_layers :
         */

        private String annoField;
        private String bsLayerFileName;
        private String cache_url;
        private String defaultShow;
        private int dirOrder;
        private String dirTypeName;
        private String dpi;
        private String featureType;
        private String have_set_field;
        private String have_set_layer;
        private int id;
        private String isBaseMap;
        private String is_use_cache;
        private String key_name;
        private int layerId;
        private int layerOrder;
        private String layerType;
        private String layer_extent;
        private String layer_table;
        private int maxLabelScale;
        private int maxScale;
        private int minLabelScale;
        private int minScale;
        private String name;
        private String origin;
        private String power_code;
        private String resolution;
        private String selectable;
        private String serial_name;
        private String server_name;
        private String server_state;
        private String srs;
        private String style;
        private String suffix;
        private String tileSize;
        private String tileType;
        private String tile_url;
        private String token;
        private String wms_layers;

        public String getAnnoField() {
            return annoField;
        }

        public void setAnnoField(String annoField) {
            this.annoField = annoField;
        }

        public String getBsLayerFileName() {
            return bsLayerFileName;
        }

        public void setBsLayerFileName(String bsLayerFileName) {
            this.bsLayerFileName = bsLayerFileName;
        }

        public String getCache_url() {
            return cache_url;
        }

        public void setCache_url(String cache_url) {
            this.cache_url = cache_url;
        }

        public String getDefaultShow() {
            return defaultShow;
        }

        public void setDefaultShow(String defaultShow) {
            this.defaultShow = defaultShow;
        }

        public int getDirOrder() {
            return dirOrder;
        }

        public void setDirOrder(int dirOrder) {
            this.dirOrder = dirOrder;
        }

        public String getDirTypeName() {
            return dirTypeName;
        }

        public void setDirTypeName(String dirTypeName) {
            this.dirTypeName = dirTypeName;
        }

        public String getDpi() {
            return dpi;
        }

        public void setDpi(String dpi) {
            this.dpi = dpi;
        }

        public String getFeatureType() {
            return featureType;
        }

        public void setFeatureType(String featureType) {
            this.featureType = featureType;
        }

        public String getHave_set_field() {
            return have_set_field;
        }

        public void setHave_set_field(String have_set_field) {
            this.have_set_field = have_set_field;
        }

        public String getHave_set_layer() {
            return have_set_layer;
        }

        public void setHave_set_layer(String have_set_layer) {
            this.have_set_layer = have_set_layer;
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

        public String getIs_use_cache() {
            return is_use_cache;
        }

        public void setIs_use_cache(String is_use_cache) {
            this.is_use_cache = is_use_cache;
        }

        public String getKey_name() {
            return key_name;
        }

        public void setKey_name(String key_name) {
            this.key_name = key_name;
        }

        public int getLayerId() {
            return layerId;
        }

        public void setLayerId(int layerId) {
            this.layerId = layerId;
        }

        public int getLayerOrder() {
            return layerOrder;
        }

        public void setLayerOrder(int layerOrder) {
            this.layerOrder = layerOrder;
        }

        public String getLayerType() {
            return layerType;
        }

        public void setLayerType(String layerType) {
            this.layerType = layerType;
        }

        public String getLayer_extent() {
            return layer_extent;
        }

        public void setLayer_extent(String layer_extent) {
            this.layer_extent = layer_extent;
        }

        public String getLayer_table() {
            return layer_table;
        }

        public void setLayer_table(String layer_table) {
            this.layer_table = layer_table;
        }

        public int getMaxLabelScale() {
            return maxLabelScale;
        }

        public void setMaxLabelScale(int maxLabelScale) {
            this.maxLabelScale = maxLabelScale;
        }

        public int getMaxScale() {
            return maxScale;
        }

        public void setMaxScale(int maxScale) {
            this.maxScale = maxScale;
        }

        public int getMinLabelScale() {
            return minLabelScale;
        }

        public void setMinLabelScale(int minLabelScale) {
            this.minLabelScale = minLabelScale;
        }

        public int getMinScale() {
            return minScale;
        }

        public void setMinScale(int minScale) {
            this.minScale = minScale;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getPower_code() {
            return power_code;
        }

        public void setPower_code(String power_code) {
            this.power_code = power_code;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getSelectable() {
            return selectable;
        }

        public void setSelectable(String selectable) {
            this.selectable = selectable;
        }

        public String getSerial_name() {
            return serial_name;
        }

        public void setSerial_name(String serial_name) {
            this.serial_name = serial_name;
        }

        public String getServer_name() {
            return server_name;
        }

        public void setServer_name(String server_name) {
            this.server_name = server_name;
        }

        public String getServer_state() {
            return server_state;
        }

        public void setServer_state(String server_state) {
            this.server_state = server_state;
        }

        public String getSrs() {
            return srs;
        }

        public void setSrs(String srs) {
            this.srs = srs;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public String getTileSize() {
            return tileSize;
        }

        public void setTileSize(String tileSize) {
            this.tileSize = tileSize;
        }

        public String getTileType() {
            return tileType;
        }

        public void setTileType(String tileType) {
            this.tileType = tileType;
        }

        public String getTile_url() {
            return tile_url;
        }

        public void setTile_url(String tile_url) {
            this.tile_url = tile_url;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getWms_layers() {
            return wms_layers;
        }

        public void setWms_layers(String wms_layers) {
            this.wms_layers = wms_layers;
        }
    }

    public static class VectorLayerBean {
        /**
         * activeInBs : 1
         * annoFeatureShow : 0
         * annoField :
         * annostring :
         * annostringReplace :
         * cache_url :
         * count :
         * defaultShow : 0
         * defaultStyle : {"fillAlpha":"","fillColor":"","id":"1","name":"默认点样式","outlineArrow":"","outlineColor":"","outlineSize":"","outlineStyle":"","symbol":"c100.gif","type":"1"}
         * dirOrder : 0
         * dirTypeName : agmobile
         * editable : 0
         * featureType : 1
         * groupField1 :
         * groupField2 :
         * id : 373
         * is_use_cache :
         * key_name : OBJECTID
         * layerId : 259
         * layerOrder : 1
         * layerType : 3
         * layer_extent :
         * layer_table : SJ_POI2_1
         * maxLabelScale : 0
         * maxScale : 0
         * minLabelScale : 0
         * minScale : 0
         * name : SJ_POI2_1
         * nsXml :
         * origin :
         * power_code :
         * remarkFunc : 0
         * selectable : 1
         * serial_name :
         * server_name :
         * server_state :
         * showLabel : 1
         * spName :
         * srs :
         * style :
         * table : SJ_POI2_1
         * taggingStyle : {"fillAlpha":"","fillColor":"","id":"4","name":"默认标注样式","outlineArrow":"","outlineColor":"FF0000","outlineSize":"12","outlineStyle":"","symbol":"","type":"5"}
         * themeField : 0
         * tile_url :
         * token :
         * vid : 18
         * wms_layers :
         */

        private String activeInBs;
        private String annoFeatureShow;
        private String annoField;
        private String annostring;
        private String annostringReplace;
        private String cache_url;
        private String count;
        private String defaultShow;
        private DefaultStyleBeanX defaultStyle;
        private int dirOrder;
        private String dirTypeName;
        private String editable;
        private String featureType;
        private String groupField1;
        private String groupField2;
        private int id;
        private String is_use_cache;
        private String key_name;
        private int layerId;
        private int layerOrder;
        private String layerType;
        private String layer_extent;
        private String layer_table;
        private int maxLabelScale;
        private int maxScale;
        private int minLabelScale;
        private int minScale;
        private String name;
        private String nsXml;
        private String origin;
        private String power_code;
        private int remarkFunc;
        private String selectable;
        private String serial_name;
        private String server_name;
        private String server_state;
        private String showLabel;
        private String spName;
        private String srs;
        private String style;
        private String table;
        private TaggingStyleBeanX taggingStyle;
        private String themeField;
        private String tile_url;
        private String token;
        private int vid;
        private String wms_layers;

        public String getActiveInBs() {
            return activeInBs;
        }

        public void setActiveInBs(String activeInBs) {
            this.activeInBs = activeInBs;
        }

        public String getAnnoFeatureShow() {
            return annoFeatureShow;
        }

        public void setAnnoFeatureShow(String annoFeatureShow) {
            this.annoFeatureShow = annoFeatureShow;
        }

        public String getAnnoField() {
            return annoField;
        }

        public void setAnnoField(String annoField) {
            this.annoField = annoField;
        }

        public String getAnnostring() {
            return annostring;
        }

        public void setAnnostring(String annostring) {
            this.annostring = annostring;
        }

        public String getAnnostringReplace() {
            return annostringReplace;
        }

        public void setAnnostringReplace(String annostringReplace) {
            this.annostringReplace = annostringReplace;
        }

        public String getCache_url() {
            return cache_url;
        }

        public void setCache_url(String cache_url) {
            this.cache_url = cache_url;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getDefaultShow() {
            return defaultShow;
        }

        public void setDefaultShow(String defaultShow) {
            this.defaultShow = defaultShow;
        }

        public DefaultStyleBeanX getDefaultStyle() {
            return defaultStyle;
        }

        public void setDefaultStyle(DefaultStyleBeanX defaultStyle) {
            this.defaultStyle = defaultStyle;
        }

        public int getDirOrder() {
            return dirOrder;
        }

        public void setDirOrder(int dirOrder) {
            this.dirOrder = dirOrder;
        }

        public String getDirTypeName() {
            return dirTypeName;
        }

        public void setDirTypeName(String dirTypeName) {
            this.dirTypeName = dirTypeName;
        }

        public String getEditable() {
            return editable;
        }

        public void setEditable(String editable) {
            this.editable = editable;
        }

        public String getFeatureType() {
            return featureType;
        }

        public void setFeatureType(String featureType) {
            this.featureType = featureType;
        }

        public String getGroupField1() {
            return groupField1;
        }

        public void setGroupField1(String groupField1) {
            this.groupField1 = groupField1;
        }

        public String getGroupField2() {
            return groupField2;
        }

        public void setGroupField2(String groupField2) {
            this.groupField2 = groupField2;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIs_use_cache() {
            return is_use_cache;
        }

        public void setIs_use_cache(String is_use_cache) {
            this.is_use_cache = is_use_cache;
        }

        public String getKey_name() {
            return key_name;
        }

        public void setKey_name(String key_name) {
            this.key_name = key_name;
        }

        public int getLayerId() {
            return layerId;
        }

        public void setLayerId(int layerId) {
            this.layerId = layerId;
        }

        public int getLayerOrder() {
            return layerOrder;
        }

        public void setLayerOrder(int layerOrder) {
            this.layerOrder = layerOrder;
        }

        public String getLayerType() {
            return layerType;
        }

        public void setLayerType(String layerType) {
            this.layerType = layerType;
        }

        public String getLayer_extent() {
            return layer_extent;
        }

        public void setLayer_extent(String layer_extent) {
            this.layer_extent = layer_extent;
        }

        public String getLayer_table() {
            return layer_table;
        }

        public void setLayer_table(String layer_table) {
            this.layer_table = layer_table;
        }

        public int getMaxLabelScale() {
            return maxLabelScale;
        }

        public void setMaxLabelScale(int maxLabelScale) {
            this.maxLabelScale = maxLabelScale;
        }

        public int getMaxScale() {
            return maxScale;
        }

        public void setMaxScale(int maxScale) {
            this.maxScale = maxScale;
        }

        public int getMinLabelScale() {
            return minLabelScale;
        }

        public void setMinLabelScale(int minLabelScale) {
            this.minLabelScale = minLabelScale;
        }

        public int getMinScale() {
            return minScale;
        }

        public void setMinScale(int minScale) {
            this.minScale = minScale;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNsXml() {
            return nsXml;
        }

        public void setNsXml(String nsXml) {
            this.nsXml = nsXml;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getPower_code() {
            return power_code;
        }

        public void setPower_code(String power_code) {
            this.power_code = power_code;
        }

        public int getRemarkFunc() {
            return remarkFunc;
        }

        public void setRemarkFunc(int remarkFunc) {
            this.remarkFunc = remarkFunc;
        }

        public String getSelectable() {
            return selectable;
        }

        public void setSelectable(String selectable) {
            this.selectable = selectable;
        }

        public String getSerial_name() {
            return serial_name;
        }

        public void setSerial_name(String serial_name) {
            this.serial_name = serial_name;
        }

        public String getServer_name() {
            return server_name;
        }

        public void setServer_name(String server_name) {
            this.server_name = server_name;
        }

        public String getServer_state() {
            return server_state;
        }

        public void setServer_state(String server_state) {
            this.server_state = server_state;
        }

        public String getShowLabel() {
            return showLabel;
        }

        public void setShowLabel(String showLabel) {
            this.showLabel = showLabel;
        }

        public String getSpName() {
            return spName;
        }

        public void setSpName(String spName) {
            this.spName = spName;
        }

        public String getSrs() {
            return srs;
        }

        public void setSrs(String srs) {
            this.srs = srs;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public TaggingStyleBeanX getTaggingStyle() {
            return taggingStyle;
        }

        public void setTaggingStyle(TaggingStyleBeanX taggingStyle) {
            this.taggingStyle = taggingStyle;
        }

        public String getThemeField() {
            return themeField;
        }

        public void setThemeField(String themeField) {
            this.themeField = themeField;
        }

        public String getTile_url() {
            return tile_url;
        }

        public void setTile_url(String tile_url) {
            this.tile_url = tile_url;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getVid() {
            return vid;
        }

        public void setVid(int vid) {
            this.vid = vid;
        }

        public String getWms_layers() {
            return wms_layers;
        }

        public void setWms_layers(String wms_layers) {
            this.wms_layers = wms_layers;
        }

        public static class DefaultStyleBeanX {
            /**
             * fillAlpha :
             * fillColor :
             * id : 1
             * name : 默认点样式
             * outlineArrow :
             * outlineColor :
             * outlineSize :
             * outlineStyle :
             * symbol : c100.gif
             * type : 1
             */

            private String fillAlpha;
            private String fillColor;
            private String id;
            private String name;
            private String outlineArrow;
            private String outlineColor;
            private String outlineSize;
            private String outlineStyle;
            private String symbol;
            private String type;

            public String getFillAlpha() {
                return fillAlpha;
            }

            public void setFillAlpha(String fillAlpha) {
                this.fillAlpha = fillAlpha;
            }

            public String getFillColor() {
                return fillColor;
            }

            public void setFillColor(String fillColor) {
                this.fillColor = fillColor;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOutlineArrow() {
                return outlineArrow;
            }

            public void setOutlineArrow(String outlineArrow) {
                this.outlineArrow = outlineArrow;
            }

            public String getOutlineColor() {
                return outlineColor;
            }

            public void setOutlineColor(String outlineColor) {
                this.outlineColor = outlineColor;
            }

            public String getOutlineSize() {
                return outlineSize;
            }

            public void setOutlineSize(String outlineSize) {
                this.outlineSize = outlineSize;
            }

            public String getOutlineStyle() {
                return outlineStyle;
            }

            public void setOutlineStyle(String outlineStyle) {
                this.outlineStyle = outlineStyle;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class TaggingStyleBeanX {
            /**
             * fillAlpha :
             * fillColor :
             * id : 4
             * name : 默认标注样式
             * outlineArrow :
             * outlineColor : FF0000
             * outlineSize : 12
             * outlineStyle :
             * symbol :
             * type : 5
             */

            private String fillAlpha;
            private String fillColor;
            private String id;
            private String name;
            private String outlineArrow;
            private String outlineColor;
            private String outlineSize;
            private String outlineStyle;
            private String symbol;
            private String type;

            public String getFillAlpha() {
                return fillAlpha;
            }

            public void setFillAlpha(String fillAlpha) {
                this.fillAlpha = fillAlpha;
            }

            public String getFillColor() {
                return fillColor;
            }

            public void setFillColor(String fillColor) {
                this.fillColor = fillColor;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOutlineArrow() {
                return outlineArrow;
            }

            public void setOutlineArrow(String outlineArrow) {
                this.outlineArrow = outlineArrow;
            }

            public String getOutlineColor() {
                return outlineColor;
            }

            public void setOutlineColor(String outlineColor) {
                this.outlineColor = outlineColor;
            }

            public String getOutlineSize() {
                return outlineSize;
            }

            public void setOutlineSize(String outlineSize) {
                this.outlineSize = outlineSize;
            }

            public String getOutlineStyle() {
                return outlineStyle;
            }

            public void setOutlineStyle(String outlineStyle) {
                this.outlineStyle = outlineStyle;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
