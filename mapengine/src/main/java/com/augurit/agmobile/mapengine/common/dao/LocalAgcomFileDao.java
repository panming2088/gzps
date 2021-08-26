package com.augurit.agmobile.mapengine.common.dao;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.model.AreaLocate;
import com.augurit.agmobile.mapengine.common.model.Field;
import com.augurit.agmobile.mapengine.common.model.UserArea;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.am.fw.scy.CryptoUtil;
import com.augurit.am.fw.utils.AMFileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.dao
 * @createTime 创建时间 ：2017-03-31
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-31
 * @modifyMemo 修改备注：
 */
public class LocalAgcomFileDao {



    private Context mContext;
    private String projectFileSavePath;

    public LocalAgcomFileDao(Context context){
        this.mContext = context;
        FilePathUtil filePathUtil = new FilePathUtil(mContext);
        projectFileSavePath = filePathUtil.getProjectFileSavePath();
    }

    /**
     * 获取区域划分
     * @param discodeId
     * @return
     */
    public UserArea getUserArea(int discodeId){
        UserArea userArea = null;
        File file = new File(projectFileSavePath + "/userarea" + discodeId + ".txt");
        if(!file.exists()){
            return null;
        }
        try {
            String userAreaStr = AMFileUtil.readUtf8(file);
            String userAreaDecrypt = CryptoUtil.decryptText(userAreaStr);
            userArea = new Gson().fromJson(userAreaDecrypt, UserArea.class);
        } catch (Exception e) {
            return null;
        }
        return userArea;
    }

    /**
     * 将区域划分保存到本地文件中
     * @param discodeId
     * @param userArea
     */
    public void saveUserArea(int discodeId, UserArea userArea){
        File file = new File(projectFileSavePath + "/userarea" + discodeId + ".txt");
        try {
            String data = new Gson().toJson(userArea);
            String dataEncrypt = CryptoUtil.encryptText(data);
            AMFileUtil.saveStringToFile(dataEncrypt, Charset.forName("UTF-8"), file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取区域的位置信息
     * @param areaId
     * @param discodeLocateId
     * @return
     */
    public AreaLocate getAreaLocate(int areaId, int discodeLocateId){
        AreaLocate areaLocate = null;
        File file = new File(projectFileSavePath + "/arealocate" + areaId + discodeLocateId + ".txt");
        if(!file.exists()){
            return null;
        }
        try {
            String areaLocateaStr = AMFileUtil.readUtf8(file);
            String areaLocateDecrypt = CryptoUtil.decryptText(areaLocateaStr);
            areaLocate = new Gson().fromJson(areaLocateDecrypt, AreaLocate.class);
        } catch (Exception e) {
            return null;
        }
        return areaLocate;
    }

    /**
     * 将区域的位置信息保存到本地文件
     * @param areaId
     * @param discodeLocateId
     * @param areaLocate
     */
    public void saveAreaLocate(int areaId, int discodeLocateId, AreaLocate areaLocate){
        File file = new File(projectFileSavePath + "/arealocate" + areaId + discodeLocateId + ".txt");
        try {
            String data = new Gson().toJson(areaLocate);
            String dataEncrypt = CryptoUtil.encryptText(data);
            AMFileUtil.saveStringToFile(dataEncrypt, Charset.forName("UTF-8"), file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Field> getLayerField(String projectLayerId){
        List<Field> fields = null;
        File file = new File(projectFileSavePath + "/field" + projectLayerId + ".txt");
        if(!file.exists()){
            return null;
        }
        try {
            String fieldsStr = AMFileUtil.readUtf8(file);
            String fieldDecrypt = CryptoUtil.decryptText(fieldsStr);
            fields = new Gson().fromJson(fieldDecrypt, new TypeToken<List<Field>>(){}.getType());
        } catch (Exception e) {
            return null;
        }
        return fields;
    }

    public void saveLayerField(String projectLayerId, List<Field> fields){
        File file = new File(projectFileSavePath + "/field" + projectLayerId + ".txt");
        try {
            String data = new Gson().toJson(fields);
            String dataEncrypt = CryptoUtil.encryptText(data);
            AMFileUtil.saveStringToFile(dataEncrypt, Charset.forName("UTF-8"), file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
