package com.augurit.agmobile.patrolcore.selectdevice.utils;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.patrolcore.selectdevice.model.OnSelectDeviceFinishEvent;
import com.augurit.am.fw.utils.model.AMFile;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectdevice.utils
 * @createTime 创建时间 ：17/7/26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/26
 * @modifyMemo 修改备注：
 */

public class SelectDeviceUtil {


    public static String getIdFromAMFindResult(AMFindResult amFindResult){

        Map<String, Object> attributes = amFindResult.getAttributes();

        if(attributes.get("OBJECTID_1")!= null){
            return  attributes.get("OBJECTID_1").toString();

        }else if (attributes.get("OBJECTID")!= null){
            return attributes.get("OBJECTID").toString();

        }else if (attributes.get("唯一标识")!= null){
            return attributes.get("唯一标识").toString();

        }else if (attributes.get("FID")!= null){
            return attributes.get("FID").toString();

        }else {
            return "-1";
        }
    }
}
