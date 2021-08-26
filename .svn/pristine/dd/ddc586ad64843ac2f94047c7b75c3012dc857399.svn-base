package com.augurit.agmobile.mapengine.common.router;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.dao.LocalAgcomFileDao;
import com.augurit.agmobile.mapengine.common.dao.RemoteAgcomRestDao;
import com.augurit.agmobile.mapengine.common.model.AreaLocate;
import com.augurit.agmobile.mapengine.common.model.FeatureSet;
import com.augurit.agmobile.mapengine.common.model.Field;
import com.augurit.agmobile.mapengine.common.model.FieldStats;
import com.augurit.agmobile.mapengine.common.model.SpatialBufferResult;
import com.augurit.agmobile.mapengine.common.model.UserArea;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.am.fw.utils.StringUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.router
 * @createTime 创建时间 ：2017-03-31
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-31
 * @modifyMemo 修改备注：
 */
public class AgcomRouter {


    private Context mContext;
    private RemoteAgcomRestDao remoteAgcomRestDao;
    private LocalAgcomFileDao localAgcomFileDao;

    public AgcomRouter(Context context){
        this.mContext = context.getApplicationContext();
        this.remoteAgcomRestDao = new RemoteAgcomRestDao(mContext);
        this.localAgcomFileDao = new LocalAgcomFileDao(mContext);
    }

    /**
     * 缓冲分析
     * @param paramJson
     * @return
     */
    public SpatialBufferResult spatialBuffer(String paramJson){
        return remoteAgcomRestDao.spatialBuffer(paramJson);
    }

    /**
     * 空间查询
     * @param paramJson
     * @return
     */
    public List<FeatureSet> spatialQuery(String paramJson){
        return remoteAgcomRestDao.spatialQuery(paramJson);
    }


    /**
     * 统计
     * @param projectLayerId
     * @param groupField
     * @param wkt
     * @param where
     * @param statsType
     * @param statsField
     * @param userId
     * @param projectId
     * @return
     */
    List<FieldStats> statistics(@Path("projectLayerId") String projectLayerId,
                                         @Path("groupField") String groupField,
                                         @Path("wkt") String wkt,
                                         @Path("where") String where,
                                         @Path("statsType") String statsType,
                                         @Path("statsField") String statsField,
                                         @Query("userId") String userId,
                                         @Query("projectId") String projectId){
        return remoteAgcomRestDao.statistics(projectLayerId, wkt, statsField, groupField, statsType, userId, projectId);
    }

    public String getPorjectLayerBaseIdByLayerId(String layerId){
        return remoteAgcomRestDao.getPorjectLayerBaseIdByLayerId(layerId);
    }

    /**
     * 获取当前专题下可见的区域信息
     * @return
     */
    public UserArea getCurrentProjectUserArea(){
        ProjectInfo projectInfo = ProjectDataManager.getInstance().getCurrentProject(mContext);
        int discodeId = projectInfo.getProjectMapParam().getDiscodeId();
        return getUserArea(discodeId);
    }

    /**
     * 获取区域信息
     * @param discodeId
     * @return
     */
    public UserArea getUserArea(int discodeId){
        UserArea userArea = localAgcomFileDao.getUserArea(discodeId);
        if(userArea == null){
            userArea = remoteAgcomRestDao.getUserArea(discodeId);
            if(userArea != null){
                localAgcomFileDao.saveUserArea(discodeId, userArea);
            }
        }
        return userArea;
    }

    /**
     * 获取区域位置信息
     * @param areaId
     * @param discodeLocateId
     * @return
     */
    public AreaLocate getAreaLocate(int areaId, int discodeLocateId){
        AreaLocate areaLocate = localAgcomFileDao.getAreaLocate(areaId, discodeLocateId);
        if(areaLocate == null
                || StringUtil.isEmpty(areaLocate.getWkt())
                || "null".equals(areaLocate.getWkt())){
            areaLocate = remoteAgcomRestDao.getAreaLocate(areaId, discodeLocateId);
            if(areaLocate != null){
                localAgcomFileDao.saveAreaLocate(areaId, discodeLocateId, areaLocate);
            }
        }
        return areaLocate;
    }

    public List<Field> getLayerField(String projectLayerId){
        List<Field> fields = localAgcomFileDao.getLayerField(projectLayerId);
        if(fields == null){
            fields = remoteAgcomRestDao.getLayerField(projectLayerId);
            if(fields != null){
                localAgcomFileDao.saveLayerField(projectLayerId, fields);
            }
        }
        return fields;
    }
}
