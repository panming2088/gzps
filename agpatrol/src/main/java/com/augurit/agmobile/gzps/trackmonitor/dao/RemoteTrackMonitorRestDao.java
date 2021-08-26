package com.augurit.agmobile.gzps.trackmonitor.dao;

import android.content.Context;

import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.trackmonitor.dao
 * @createTime 创建时间 ：2017-08-21
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-21
 * @modifyMemo 修改备注：
 */

public class RemoteTrackMonitorRestDao {

    private TrackMonitorApi mTrackMonitorApi;

    public RemoteTrackMonitorRestDao(Context context){
        String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        AMNetwork amNetwork = new AMNetwork(serverUrl);
        amNetwork.build();
        amNetwork.registerApi(TrackMonitorApi.class);
        this.mTrackMonitorApi = (TrackMonitorApi) amNetwork.getServiceApi(TrackMonitorApi.class);
    }


}
