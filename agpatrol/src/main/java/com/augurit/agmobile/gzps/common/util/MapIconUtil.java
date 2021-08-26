package com.augurit.agmobile.gzps.common.util;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.patrolcore.R;

/**
 * 地图上选点图标切换
 * Created by xcl on 2017/10/24.
 */
public class MapIconUtil {

    /**
     * 阀门
     */
    public static final String VALVE = LayerUrlConstant.componentNames[4];
    /**
     * 泵站
     */
    public static final String BUMP_STATION = LayerUrlConstant.componentNames[5];
    /**
     * 溢流堰
     */
    public static final String DOWNFLOW_WEIR =  LayerUrlConstant.componentNames[6];
    /**
     * 排水管道
     */
    public static final String DRAIN_LINE = LayerUrlConstant.componentNames[7];
    /**
     * 排水口
     */
    public static final String DRAIN = LayerUrlConstant.componentNames[2];
    /**
     * 雨水口
     */
    public static final String GULLY = LayerUrlConstant.componentNames[1];
    /**
     * 窨井
     */
    public static final String INSEPCTION_SHAFT =  LayerUrlConstant.componentNames[0];

    public static int getResId(String type) {
        if (VALVE.equals(type)){
            return R.mipmap.map_ic_valve;
        }else  if (BUMP_STATION.equals(type)){
            return R.mipmap.map_ic_bump_station;
        }else   if (DOWNFLOW_WEIR.equals(type)){
            return R.mipmap.map_ic_downflow_weir;
        }else   if (DRAIN.equals(type)){
            return R.mipmap.map_ic_drain;
        } else  if (DRAIN_LINE.equals(type)){
            return R.mipmap.map_ic_drain_line;
        }else   if (GULLY.equals(type)){
            return R.mipmap.map_ic_gully;
        } else  if (INSEPCTION_SHAFT.equals(type)){
            return R.mipmap.map_ic_inspection_shaft;
        }else {
            return R.mipmap.ic_select_location;
        }
//        switch (type){
//            case VALVE :
//                return R.mipmap.map_ic_valve;
//            case BUMP_STATION:
//                return R.mipmap.map_ic_bump_station;
//            case DOWNFLOW_WEIR:
//                return R.mipmap.map_ic_downflow_weir;
//            case DRAIN:
//                return R.mipmap.map_ic_drain;
//            case DRAIN_LINE:
//                return R.mipmap.map_ic_drain_line;
//            case GULLY:
//                return R.mipmap.map_ic_gully;
//            case INSEPCTION_SHAFT:
//                return R.mipmap.map_ic_inspection_shaft;
//            default:
//                return R.mipmap.common_ic_select_location;
//        }
    }
}
