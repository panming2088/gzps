package com.augurit.agmobile.mapengine.edit.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.augurit.agmobile.mapengine.edit.model.FeatureTypeData;
import com.esri.android.map.Layer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureType;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.SymbolHelper;

import java.util.List;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augur.agmobile.ammap.edit.utils
 * @createTime 创建时间 ：16/11/15
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 16/11/15
 */

public final class TemplatesUtils {
    public static FeatureTemplate curFeatureTemplate;
    public static EditMode editMode;
    public static List<FeatureTypeData> featureTypeList;
    public static List<FeatureTemplate> templateList;
    public static List<ArcGISFeatureLayer> featureLayerList;
    private TemplatesUtils() {
    }

    /**
     * Using this method all the feature templates in the layer are listed. From the MapView we get all the layers in an
     * array. Check which of them are instances of ArcGISFeatureLayer. From the feature layer we get all the templates and
     * populate the list. Since we go through all the layers we obtain feature templates for all layers.
     */
    public static void listTemplates(Layer[] layers,
                                     List<FeatureTypeData> featureTypeList,
                                     List<FeatureTemplate> templateList,
                                     List<ArcGISFeatureLayer> featureLayerList,
                                     Context context) {

        for (Layer l : layers) {
            // Check if this is an ArcGISFeatureLayer
            if (l instanceof ArcGISFeatureLayer) {
                //   Log.d(TAG, l.getUrl());
                ArcGISFeatureLayer featureLayer = (ArcGISFeatureLayer) l;

                // Loop on all feature types in the layer
                FeatureType[] types = featureLayer.getTypes();
                for (FeatureType featureType : types) {
                    // Save data for each template for this feature type
                    addTemplates(featureLayer, featureType.getTemplates(), featureTypeList, templateList, featureLayerList, context);
                }

                // If no templates provided by feature types, get templates from the layer itself
                if (featureTypeList.size() == 0) {
                    addTemplates(featureLayer, featureLayer.getTemplates(), featureTypeList, templateList, featureLayerList, context);
                }
            }
        }

        TemplatesUtils.featureTypeList = featureTypeList;
        TemplatesUtils.templateList = templateList;
        TemplatesUtils.featureLayerList = featureLayerList;
    }

    public static void addTemplates(ArcGISFeatureLayer featureLayer,
                                    FeatureTemplate[] templates,
                                    List<FeatureTypeData> featureTypeList,
                                    List<FeatureTemplate> templateList,
                                    List<ArcGISFeatureLayer> featureLayerList,
                                    Context context) {
        for (FeatureTemplate featureTemplate : templates) {
            String name = featureTemplate.getName();
            Graphic g = featureLayer.createFeatureWithTemplate(featureTemplate, null);
            Renderer renderer = featureLayer.getRenderer();
            Symbol symbol = renderer.getSymbol(g);


            final int WIDTH_IN_DP_UNITS = 30;
            final float scale = context.getResources().getDisplayMetrics().density;
            final int widthInPixels = (int) (WIDTH_IN_DP_UNITS * scale + 0.5f);
            Bitmap bitmap = SymbolHelper.getLegendImage(symbol, widthInPixels, widthInPixels);

            featureTypeList.add(new FeatureTypeData(bitmap, name, symbol));
            templateList.add(featureTemplate);
            featureLayerList.add(featureLayer);
        }
    }

    public static void setCurTemplate(FeatureTemplate featureTemplate) {
        curFeatureTemplate = featureTemplate;
    }

    public static FeatureTemplate getCurFeatureTemplate() {
        if (curFeatureTemplate != null) {
            return curFeatureTemplate;
        }

        return null;
    }

    public static void setCurEditMode(EditMode mode) {
        editMode = mode;
    }

    public static EditMode getEditMode() {
        if (editMode != null) {
            return editMode;
        }
        return null;
    }
}
