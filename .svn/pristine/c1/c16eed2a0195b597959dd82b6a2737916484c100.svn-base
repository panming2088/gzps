package com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.am.fw.utils.StringUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

/**
 * 原设施信息视图生成Manager
 * Created by xucil on 2017/12/29.
 */

public class OriginalAttributesViewManager {

    public OriginalAttributesViewManager(Context context,
                                         ViewGroup tableItemContainer,
                                         CompleteTableInfo completeTableInfo,
                                         MapView mapView,
                                         double x, double y,
                                         String layerType,
                                         GraphicsLayer graphicsLayer) {
        tableItemContainer.removeAllViews();
        if (completeTableInfo.getAttrs() == null) {
            return;
        }
        String usid = String.valueOf(completeTableInfo.getAttrs().get(ComponentFieldKeyConstant.USID));
        if ((StringUtil.isEmpty(usid) || usid.equals("null")) && !StringUtil.isEmpty(completeTableInfo.getAttrs().get(ComponentFieldKeyConstant.OBJECTID))) {
            usid = String.valueOf(completeTableInfo.getAttrs().get(ComponentFieldKeyConstant.OBJECTID));
        }
        //设施
//        /**
//         * 如果是雨水口，显示特性：方形
//         */
//        String layertype = "";
//        if (mCurrentModifiedFacility != null) {
//            layertype = mCurrentModifiedFacility.getLayerName();
//        } else if (mCurrentUploadedFacility != null) {
//            layertype = mCurrentUploadedFacility.getLayerName();
//        }

        TextItemTableItem ssTv = new TextItemTableItem(context);
        ssTv.setTextViewName("设施");
        ssTv.setText(StringUtil.getNotNullString(layerType, "") + "(" + usid + ")");
        ssTv.setReadOnly();
        tableItemContainer.addView(ssTv);


        //原设施位置
        String address = String.valueOf(completeTableInfo.getAttrs().get(ComponentFieldKeyConstant.ADDR));
        OriginalLocationView addressTv = new OriginalLocationView(context);
        addressTv.setTextViewName("设施位置");
        address = replaceSpaceCharacter(address);
//        addressTv.setText(StringUtil.getNotNullString(address, "无"));
        addressTv.setText(StringUtil.getNotNullString(address, ""));
        addressTv.setReadOnly();
        tableItemContainer.addView(addressTv);
        if (x != 0 && y != 0 && mapView != null) {
            addressTv.setOriginLocation(mapView, graphicsLayer, new Point(x, y));
        }


        //所在道路
        String road = String.valueOf(completeTableInfo.getAttrs().get
                (ComponentFieldKeyConstant.ROAD));
        TextItemTableItem roadTv = new TextItemTableItem(context);
        roadTv.setTextViewName("所在道路");
        road = replaceSpaceCharacter(road);
        roadTv.setText(StringUtil.getNotNullString(road, ""));
        roadTv.setReadOnly();
        tableItemContainer.addView(roadTv);

        if (layerType.equals("窨井")) {
            String yinjing_type = String.valueOf(completeTableInfo.getAttrs().get(ComponentFieldKeyConstant.SUBTYPE));
            String sort = String.valueOf(completeTableInfo.getAttrs().get(ComponentFieldKeyConstant.SORT));
            String MATERIAL = String.valueOf(completeTableInfo.getAttrs().get(ComponentFieldKeyConstant.MATERIAL));

            TextItemTableItem oneAttrItem = new TextItemTableItem(context);
            TextItemTableItem twoAttrItem = new TextItemTableItem(context);
//            TextItemTableItem threeAttrItem = new TextItemTableItem(context);

            oneAttrItem.setTextViewName("窨井类型");
            yinjing_type = replaceSpaceCharacter(yinjing_type);
            oneAttrItem.setText(StringUtil.getNotNullString(yinjing_type, ""));
            oneAttrItem.setReadOnly();

            twoAttrItem.setTextViewName("雨污类别");
            sort = replaceSpaceCharacter(sort);
            twoAttrItem.setText(StringUtil.getNotNullString(sort, ""));
            twoAttrItem.setReadOnly();

//            threeAttrItem.setTextViewName("井盖材质");
//            MATERIAL = replaceSpaceCharacter(MATERIAL);
//            threeAttrItem.setText(StringUtil.getNotNullString(MATERIAL, ""));
//            threeAttrItem.setReadOnly();

            tableItemContainer.addView(oneAttrItem);
            tableItemContainer.addView(twoAttrItem);
//            tableItemContainer.addView(threeAttrItem);

        } else if (layerType.equals("排水口")) {
            TextItemTableItem oneAttrItem = new TextItemTableItem(context);
            TextItemTableItem twoAttrItem = new TextItemTableItem(context);
            TextItemTableItem twoAttrItem1 = new TextItemTableItem(context);
            TextItemTableItem twoAttrItem2 = new TextItemTableItem(context);
            TextItemTableItem twoAttrItem3 = new TextItemTableItem(context);


            oneAttrItem.setTextViewName("河涌名称");
            oneAttrItem.setText(StringUtil.getNotNullString(completeTableInfo.getAttrs().get("ATTR_ONE"), ""));
            oneAttrItem.setReadOnly();

            twoAttrItem.setTextViewName("排水口岸别");
            twoAttrItem.setText(StringUtil.getNotNullString(completeTableInfo.getAttrs().get("ATTR_FOUR"), ""));
            twoAttrItem.setReadOnly();

            twoAttrItem1.setTextViewName("入河方式");
            twoAttrItem1.setText(StringUtil.getNotNullString(completeTableInfo.getAttrs().get("ATTR_FIVE"), ""));
            twoAttrItem1.setReadOnly();

            twoAttrItem2.setTextViewName("排水口设施类型");
            twoAttrItem2.setText(StringUtil.getNotNullString(completeTableInfo.getAttrs().get("ATTR_SIX"), ""));
            twoAttrItem2.setReadOnly();

            twoAttrItem3.setTextViewName("排污口管径");
            twoAttrItem3.setText(StringUtil.getNotNullString(completeTableInfo.getAttrs().get("ATTR_SEVEN"), ""));
            twoAttrItem3.setReadOnly();

            tableItemContainer.addView(oneAttrItem);
            tableItemContainer.addView(twoAttrItem);
            tableItemContainer.addView(twoAttrItem1);
            tableItemContainer.addView(twoAttrItem2);
            tableItemContainer.addView(twoAttrItem3);

        } else if (layerType.equals("雨水口")) {
            String feature = String.valueOf(completeTableInfo.getAttrs().get(ComponentFieldKeyConstant.FEATURE));
            String style = String.valueOf(completeTableInfo.getAttrs().get(ComponentFieldKeyConstant.STYLE));
            TextItemTableItem oneAttrItem = new TextItemTableItem(context);
            TextItemTableItem twoAttrItem = new TextItemTableItem(context);

            oneAttrItem.setTextViewName("特征");
            feature = replaceSpaceCharacter(feature);
            oneAttrItem.setText(StringUtil.getNotNullString(feature, ""));
            oneAttrItem.setReadOnly();

            twoAttrItem.setTextViewName("形式");
            style = replaceSpaceCharacter(style);
            twoAttrItem.setText(StringUtil.getNotNullString(style, ""));
            twoAttrItem.setReadOnly();

            tableItemContainer.addView(oneAttrItem);
            tableItemContainer.addView(twoAttrItem);
        }

        //权属单位
//        String parentOrg = String.valueOf(completeTableInfo.getAttrs().get(ComponentFieldKeyConstant.OWNERDEPT));
//        TextItemTableItem quanshuTv = new TextItemTableItem(context);
//        quanshuTv.setTextViewName("权属单位");
//        parentOrg = replaceSpaceCharacter(parentOrg);
//        quanshuTv.setText(StringUtil.getNotNullString(parentOrg, ""));
//        quanshuTv.setReadOnly();
//        tableItemContainer.addView(quanshuTv);
        //已挂牌编号
        TextItemTableItem bianhaoTv = new TextItemTableItem(context);
        bianhaoTv.setTextViewName("已挂牌编号");
        bianhaoTv.setReadOnly();
        String codeValue = String.valueOf(completeTableInfo.getAttrs().get(ComponentFieldKeyConstant.LICENSE));
        codeValue = codeValue.trim();
        if (layerType.equals("窨井")) {
            if (!codeValue.isEmpty()) {
                bianhaoTv.setText(StringUtil.getNotNullString(codeValue, ""));
            } else {
                bianhaoTv.setText("");
            }
            tableItemContainer.addView(bianhaoTv);
        }
    }


    @Nullable
    private String replaceSpaceCharacter(String sort) {
        if (sort != null && TextUtils.isEmpty(sort.replace(" ", ""))) {
            sort = null;
        }
        return sort;
    }
}
