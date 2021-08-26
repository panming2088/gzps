package com.augurit.agmobile.mapengine.common;

import android.content.Context;

import com.augurit.agmobile.mapengine.location.service.Beijing54LocationTransform;
import com.augurit.agmobile.mapengine.location.service.GuangzhouLocationTransform;
import com.augurit.am.cmpt.loc.ILocationTransform;
import com.augurit.am.cmpt.loc.IReverseLocationTransform;
import com.augurit.am.cmpt.loc.WGS84LocationTransform;
import com.augurit.agmobile.mapengine.location.service.Xian1980LocationTransform;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.mapengine.project.util.ProjectConstant;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;


/**
 * 转换坐标系管理者，采用单例进行管理，在进入应用前要调用setTransFormCoordinate来设置应用的参考坐标系。
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.comn.mgr
 * @createTime 创建时间 ：2016-11-16
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-16
 */

public class CoordinateManager {

    private static final String TAG ="坐标转换模块";

    private static CoordinateManager instance;
    private ILocationTransform mILocationTransform;
    private IReverseLocationTransform mIReverseLocationTransform;

    private boolean ifUsedSuggestedCoordinateWhenTransformFailed =true; //当用户没有设置转换坐标时，是否采用专题数据中返回的
                                                                                // 坐标系
    public static CoordinateManager getInstance(){
        if(instance==null){
            synchronized (CoordinateManager.class){
                instance = new CoordinateManager();
            }

        }
        return instance;
    }
    public void setTransFormCoordinate(ILocationTransform transform){
        mILocationTransform = transform;
    }

    public ILocationTransform getILocationTransform(Context context) {
        if (mILocationTransform == null){
            return getSuggestLocationTransform(context);
        }
        return mILocationTransform;
    }

    public void setIReverseTransFormCoordinate(IReverseLocationTransform transform){
        mIReverseLocationTransform = transform;
    }

    public IReverseLocationTransform getIReverseLocationTransform() {
        return mIReverseLocationTransform;
    }


    public boolean ifUsedSuggestedCoordinateWhenTransformFailed() {
        return ifUsedSuggestedCoordinateWhenTransformFailed;
    }

    public void setIfUsedSuggestedCoordinateWhenTransformFailed(boolean ifUsedSuggestedCoordinateWhenTransformFailed) {
        this.ifUsedSuggestedCoordinateWhenTransformFailed = ifUsedSuggestedCoordinateWhenTransformFailed;
    }

    /**
     * 返回建议的转换坐标系，主要是根据{@link ProjectInfo}中的refercence字段进行返回
     * 参考坐标系,所以这个方法一定要在专题加载完成后才能调用，否则会直接返回空
     * @return 建议的转换坐标系，如果专题此时未加载，直接返回null
     */
    public ILocationTransform getSuggestLocationTransform(Context context){
        String currentId = ProjectDataManager.getInstance().getCurrentProjectId(context);
        if (ValidateUtil.isObjectNull(currentId)){
            return null;
        }else {
            ProjectInfo projectInfo = ProjectDataManager.getInstance().getProjectDataById(context, currentId);
            if (projectInfo != null && projectInfo.getProjectMapParam() != null ){
                String reference = projectInfo.getProjectMapParam().getReference();
                switch (reference){
                    case ProjectConstant.COORDIANTE_XIAN80:
                        LogUtil.d(TAG,"当前坐标系是西安80");
                        return new Xian1980LocationTransform();

                    case ProjectConstant.COORDINATE_BEIJING:
                        LogUtil.d(TAG,"当前坐标系是北京54");
                        return new Beijing54LocationTransform();

                    case ProjectConstant.COORDINATE_GZ:
                        LogUtil.d(TAG,"当前坐标系是广州坐标系");
                        return new GuangzhouLocationTransform();

                    case ProjectConstant.COORDINATE_WGS84:
                        LogUtil.d(TAG,"当前坐标系是WGS84");
                        return new WGS84LocationTransform();
                }
            }
        }
        return new WGS84LocationTransform();//默认使用wgs84
    }
}
