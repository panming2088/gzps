package com.augurit.agmobile.mapengine.layermanage.model;


import com.augurit.agmobile.mapengine.layermanage.util.LayerConstant;
import com.augurit.agmobile.mapengine.layermanage.util.LayerUtils;
import com.augurit.am.fw.utils.common.ValidateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 进行实体类转换
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.layer.data.model.mapper
 * @createTime 创建时间 ：2016-11-09
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-09
 */

public class AgcomMapper {

    public List<LayerInfo> transform(AgcomLayerInfo externalDatas) {

        return transAGPadToAGMobileLayer(externalDatas);
    }

    /**
     * 将AGPAD中的图层信息类转成AGMobile中的图层信息类
     * @return
     */
    private List<LayerInfo> transAGPadToAGMobileLayer(AgcomLayerInfo agpadLayerInfo) {
        List<LayerInfo> layerInfos = new ArrayList<>();
        //解析tileLayer
        List<AgcomLayerInfo.TiledLayerBean> tiledLayer = agpadLayerInfo.getTiledLayer();//瓦片图层
        if (!ValidateUtil.isListNull(tiledLayer)){
            for (AgcomLayerInfo.TiledLayerBean tiledLayerBean : tiledLayer){
                String tileUrl = tiledLayerBean.getTile_url();
                //过滤掉奥格瓦片
                if(tileUrl.endsWith("tiles")
                        || tileUrl.endsWith("tiles/")
                        || tileUrl.endsWith("tile")){
                    continue;
                }
                LayerInfo layerInfo = new LayerInfo();
                layerInfo.setRemarkFunc(LayerConstant.NORMAL_LAYER); //瓦片图层没有类型之分
                layerInfo.setType(LayerUtils.getLayerType(tiledLayerBean.getLayerType(), tiledLayerBean.getTileType(),
                        tiledLayerBean.getTile_url()));
                layerInfo.setDefaultVisibility(tiledLayerBean.getDefaultShow().equals(LayerConstant.DEFAULT_SHOW));
                layerInfo.setDirTypeName(tiledLayerBean.getDirTypeName());
                layerInfo.setLayerId(tiledLayerBean.getLayerId());
                layerInfo.setProjectLayerId(tiledLayerBean.getId());
                layerInfo.setLayerOrder(tiledLayerBean.getLayerOrder());
                layerInfo.setOpacity(1);
                layerInfo.setUrl(tiledLayerBean.getTile_url());
                layerInfo.setLayerName(tiledLayerBean.getName());
                layerInfo.setBaseMap(tiledLayerBean.getIsBaseMap().equals(LayerConstant.IS_BASE_MAP));//设置是否是底图
                layerInfo.setQueryable(tiledLayerBean.getSelectable().equals(LayerConstant.IF_QUERYABLE));
                layerInfo.setIfShowInLayerList(true);//瓦片固定显示
                layerInfo.setBelongToTile(true);
                layerInfo.setDirOrder(tiledLayerBean.getDirOrder());
                layerInfo.setLayerTable(tiledLayerBean.getLayer_table());
                layerInfos.add(layerInfo);
            }
        }

        //解析vectorLayer
        List<MultiPlanVectorLayerBean> vectorLayer = agpadLayerInfo.getVectorLayer();
        if (!ValidateUtil.isListNull(vectorLayer)){
            for (MultiPlanVectorLayerBean vectorLayerUpBean : vectorLayer){
               /* if (vectorLayerUpBean.getTile_url()== null || vectorLayerUpBean.getTile_url().equals("")){
                    continue;
                }*/ //todo xcl 2017-04-17 删除的原因是对于多规项目来说，它的矢量图层的url永远是空的，这里要考虑一下如何适配不同项目（花都和多规）
                LayerInfo layerInfo = new LayerInfo();
                layerInfo.setRemarkFunc(vectorLayerUpBean.getRemarkFunc());
                layerInfo.setType(LayerUtils.getLayerType(vectorLayerUpBean.getLayerType(),"",vectorLayerUpBean.getTile_url()));
                layerInfo.setDefaultVisibility(vectorLayerUpBean.getDefaultShow().equals(LayerConstant.DEFAULT_SHOW));
                layerInfo.setDirTypeName(vectorLayerUpBean.getDirTypeName());
                layerInfo.setLayerId(vectorLayerUpBean.getLayerId());
                layerInfo.setProjectLayerId(vectorLayerUpBean.getId());
                layerInfo.setLayerOrder(vectorLayerUpBean.getLayerOrder());
                layerInfo.setOpacity(1);
                layerInfo.setUrl(vectorLayerUpBean.getTile_url());
                layerInfo.setLayerName(vectorLayerUpBean.getName());
                layerInfo.setQueryable(vectorLayerUpBean.getSelectable().equals(LayerConstant.IF_QUERYABLE));
                layerInfo.setIfShowInLayerList(vectorLayerUpBean.getActiveInBs().equals(LayerConstant.IF_ACTIVEINBS));
                layerInfo.setBelongToTile(false);
                layerInfo.setDirOrder(vectorLayerUpBean.getDirOrder());
                layerInfo.setLayerTable(vectorLayerUpBean.getLayer_table());
                layerInfos.add(layerInfo);
            }
        }
        return layerInfos;
    }

}
