package com.augurit.agmobile.mapengine.common.dao;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.model.AreaLocate;
import com.augurit.agmobile.mapengine.common.model.FeatureSet;
import com.augurit.agmobile.mapengine.common.model.Field;
import com.augurit.agmobile.mapengine.common.model.FieldStats;
import com.augurit.agmobile.mapengine.common.model.SpatialBufferResult;
import com.augurit.agmobile.mapengine.common.model.UserArea;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.dao
 * @createTime 创建时间 ：2017-03-28
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-28
 * @modifyMemo 修改备注：
 */
public class RemoteAgcomRestDao {

    private AMNetwork amNetwork;
    private AgcomApi mAgcomApi;

    public RemoteAgcomRestDao(Context context) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        this.amNetwork = new AMNetwork(serverUrl);
        this.amNetwork.build();
        this.amNetwork.registerApi(AgcomApi.class);
        this.mAgcomApi = (AgcomApi) this.amNetwork.getServiceApi(AgcomApi.class);
    }

    public SpatialBufferResult spatialBuffer(String paramJson){
        SpatialBufferResult spatialBufferResult = null;
        Call<SpatialBufferResult> call = mAgcomApi.spatialBuffer(paramJson);
        try {
            Response<SpatialBufferResult> response =  call.execute();
            spatialBufferResult = response.body();
        } catch (Exception e) {
            return null;
        }
        return spatialBufferResult;
    }

    public List<FeatureSet> spatialQuery(String paramJson){
        List<FeatureSet> featureSets = null;
        Call<List<FeatureSet>> call = mAgcomApi.spatialQuery(paramJson);
        try {
            Response<List<FeatureSet>> response =  call.execute();
            featureSets = response.body();
        } catch (Exception e) {
            return null;
        }
        return featureSets;
    }


    public List<FieldStats> statistics(String projectLayerId, String wkt, String statsField, String groupField, String statsType, String userId, String projectId) {
        List<FieldStats> fieldStatses = null;
        try {
            Call<List<FieldStats>> call = mAgcomApi.getFieldStats(projectLayerId, groupField, wkt,
                    "1=1", statsType, statsField, userId, projectId,
                    String.valueOf(System.currentTimeMillis()));
            Response<List<FieldStats>> response =  call.execute();
            fieldStatses = response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fieldStatses;
    }

    public String getPorjectLayerBaseIdByLayerId(String layerId){
        Call<ResponseBody> call = mAgcomApi.getPorjectLayerBaseIdByLayerId(layerId);
        try {
            Response<ResponseBody> response =  call.execute();
            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取区域划分
     * @param discodeId
     * @return
     */
    public UserArea getUserArea(int discodeId){
        UserArea userArea = null;
        Call<UserArea> call = mAgcomApi.getUserArea(discodeId);
        try {
            Response<UserArea> response =  call.execute();
            userArea = response.body();
        } catch (Exception e) {
            return null;
        }
        return userArea;
    }

    /**
     * 获取区域的位置信息
     * @param areaID
     * @param discodeLocateID
     * @return
     */
    public AreaLocate getAreaLocate(int areaID, int discodeLocateID){
        AreaLocate areaLocate = null;
        Call<AreaLocate> call = mAgcomApi.getAreaLocate(areaID, discodeLocateID);
        try {
            Response<AreaLocate> response =  call.execute();
            areaLocate = response.body();
        } catch (Exception e) {
            return null;
        }
        return areaLocate;
    }

    public List<Field> getLayerField(String projectLayerId){
        List<Field> fields = null;
        Call<List<Field>> call = mAgcomApi.getLayerField(projectLayerId);
        try {
            Response<List<Field>> response =  call.execute();
            fields = response.body();
        } catch (Exception e) {
            return null;
        }
        return fields;
    }
}
