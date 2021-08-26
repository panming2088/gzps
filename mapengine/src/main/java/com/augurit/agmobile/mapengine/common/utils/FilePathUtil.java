package com.augurit.agmobile.mapengine.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.Keep;
import android.text.TextUtils;

import com.augurit.am.cmpt.common.base.RequestConstant;
import com.augurit.am.fw.utils.AMFileUtil;
import com.augurit.am.fw.utils.SDCardUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.io.File;

/**
 * 文件描述：整个工程文件的顶层保存路径，默认是存放在：SD卡/AGMobile文件夹下，如果想改变存储路径，调用setSavePath方法，
 *           如果想删除整个文件夹，调用deleteAllFiles()即可。
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.common.util
 * @createTime 创建时间 ：2017-01-16
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-01-16
 */

@Keep
public class FilePathUtil {

    private static final String PROJECT_FILE_SAVE_PATH = "path";

    private Context mContext;
    public FilePathUtil(Context context){
        this.mContext = context;
    }

    private String mSavePath;

    public String getSavePath() {

        if (SDCardUtil.isSdCardExit()){
            //构造默认存储路径
            mSavePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+ getApplicationName();
            //从sp中读取,如果为空，采用默认的存放路径
            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
            mSavePath = sharedPreferencesUtil.getString(PROJECT_FILE_SAVE_PATH,mSavePath);
        }else {
            ToastUtil.shortToast(mContext,"请先插入sd卡");
        }
        return mSavePath;
    }

    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = mContext.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * 获取专题文件保存位置，如果需要在专题更新的时候进行一块删除，请将文件放置于该文件夹下
     * @return 专题文件保存位置
     */
    public String getProjectFileSavePath(){
        String topPath = getSavePath();
        String projectFileSavePath = null;
        if (!ValidateUtil.isObjectNull(topPath)){
            projectFileSavePath = topPath+"/project";
        }
        return projectFileSavePath;
    }


    /**
     * 设置当前项目中所有文件保存的顶层路径，默认是存放在：SD卡/AGMobile文件夹
     * @param savePath 保存路径
     */
    public void setSavePath(String savePath) {
        //从sp中读取,如果为空，采用默认的存放路径
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        sharedPreferencesUtil.setString(PROJECT_FILE_SAVE_PATH,savePath);
    }

    /**
     * 删除项目文件夹下所有文件
     */
    public void deleteAllFiles(){
        String savePath = getSavePath();
        AMFileUtil.deleteFile(new File(savePath));
    }

    public String getLayerServiceInfoSavePath(Context context, int layerId, String projectId) {
        String topPath = getSavePath();
        String serviceInfoFileSavePath = null;
        if (!ValidateUtil.isObjectNull(topPath)){
           /* SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
            String userId = sharedPreferencesUtil.getString(RequestConstant.USER_ID,"");*/
            //  String fileName = "AGMobile/project"+ +".txt";
            serviceInfoFileSavePath = topPath+"/project/LayerServiceInfo_project"+ projectId+"-layer"+layerId+".txt";
        }
        return serviceInfoFileSavePath;
    }



    public String getMapServiceInfoSavePath(Context context, int layerId, String projectId) {
        String topPath = getSavePath();
        String serviceInfoFileSavePath = null;
        if (!ValidateUtil.isObjectNull(topPath)){
            serviceInfoFileSavePath = topPath+"/project/MapServiceInfo_project"+ projectId+"-layer"+layerId+".txt";
        }
        return serviceInfoFileSavePath;
    }

    /**
     * 获取外部存储（内置SD卡）Geodatabase文件的父目录
     * @return
     */
    public  String getExternalGeodatabaseSavePath(){
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        String path = sharedPreferencesUtil.getString(RequestConstant.GEODATABASE_SAVE_PATH, "");
        if (TextUtils.isEmpty(path)){
            return getSavePath() + "/geodatabase/" ;
        }
        return path;
    }

    /**
     * 获取内部存放Geodatabase文件的目录
     * @return
     */
    public  String getInternalGeodatabaseSavePath(){
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        String path = sharedPreferencesUtil.getString(RequestConstant.GEODATABASE_SAVE_PATH, "");
        if (TextUtils.isEmpty(path)){
            String filePath = mContext.getFilesDir().getAbsolutePath() + "/geodatabase";
            File file = new File(filePath);
            if(!file.exists()){
                file.mkdirs();
            }
            return filePath + "/";
        }
        return path;
    }

    public String getShapeFileSavePath(){
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        String path = sharedPreferencesUtil.getString(RequestConstant.SHAPE_FILE_SAVE_PATH, "");
        if (TextUtils.isEmpty(path)){
            return getSavePath() + "/shapefile/" ;
        }
        return path;
    }

    /**
     * 获取内部存放shapefile文件的目录
     * @return
     */
    public  String getInternalShapeFilePath(){
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        String path = sharedPreferencesUtil.getString(RequestConstant.GEODATABASE_SAVE_PATH, "");
        if (TextUtils.isEmpty(path)){
            String filePath = mContext.getFilesDir().getAbsolutePath() + "/shapefile";
            File file = new File(filePath);
            if(!file.exists()){
                file.mkdirs();
            }
            return filePath + "/";
        }
        return path;
    }

    /**
     * 设置geodatabase的存储路径，路径的填写格式为:"test.geodatabase" 或者"database/test.geodatabase";
     * @param savePath
     * @return
     */
    public  String setExternalGeodatabaseSavePath(String savePath){
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        String completePath = getSavePath() + "/" + savePath;
        return sharedPreferencesUtil.getString(RequestConstant.GEODATABASE_SAVE_PATH, completePath);
    }

    /**
     * 获取图层字段的保存位置
     * @param layerId 图层id
     * @return 图层字段的保存位置
     */
    public String getLayerFieldsSavedPath(String layerId){
        return getProjectFileSavePath() + "/fields-" + layerId + ".txt" ;
    }

    /**
     * 获取图例文件的保存位置
     * @return 图例文件保存位置
     */
    public String getLegendSavedPath(){
        return getProjectFileSavePath() + "/legends.json";
    }
    //确保路径文件夹或文件没有空格
    public String getTempPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
