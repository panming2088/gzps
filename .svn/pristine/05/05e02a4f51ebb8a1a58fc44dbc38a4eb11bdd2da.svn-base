package com.augurit.agmobile.patrolcore.common.device;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.action.dao.local.ActionDBLogic;
import com.augurit.agmobile.patrolcore.common.action.model.ActionModel;
import com.augurit.agmobile.patrolcore.common.action.util.ActionNameConstant;
import com.augurit.agmobile.patrolcore.common.action.util.ActionNoConstant;
import com.augurit.agmobile.patrolcore.common.device.dao.DeviceRegisterNetLogic;
import com.augurit.agmobile.patrolcore.common.device.model.RegisterModel;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.am.cmpt.common.base.BaseInfoManager;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * 描述：设备注册管理类
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.device
 * @createTime 创建时间 ：2017/8/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/8/7
 * @modifyMemo 修改备注：
 */

public class DeviceRegisterManager {
    private Context mContext;
    private DeviceRegisterNetLogic deviceRegisterNetLogic;

    public DeviceRegisterManager(Context context){
        this.mContext = context;
        String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        this.deviceRegisterNetLogic = new DeviceRegisterNetLogic(mContext,serverUrl);
    }


    public Observable<RegisterModel> checkIfRegisterDevice(){
        return deviceRegisterNetLogic.checkIfDeviceRegister();
    }


    /**
     * 检查当前设备控制管理模块是否在agweb后端配置启用
     * @return
     */
    public boolean checkIfActionOn(){
        boolean isOn = false;
        /*
        TableDBService tableDBService = new TableDBService();
        Map<String, String> configureItemsFromDB = tableDBService.getConfigureItemsFromDB(ActionNameConstant.DEVICE_CONTROL,
                ActionNoConstant.currentActionNo);
        if(configureItemsFromDB != null){
            isOn = true;
        }
        */
        ActionDBLogic mLocalMenuStorageDao = new ActionDBLogic();
        List<ActionModel> models = mLocalMenuStorageDao.getMenuItemsForUserId(BaseInfoManager.getUserId(mContext));
        for(ActionModel model : models){
            if(model.getFeaturecode().equals(ActionNameConstant.DEVICE_CONTROL)){
                isOn =true;
                break;
            }
        }

        return isOn;
    }
}
