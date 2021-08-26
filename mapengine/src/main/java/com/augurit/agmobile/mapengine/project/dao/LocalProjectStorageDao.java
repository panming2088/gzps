package com.augurit.agmobile.mapengine.project.dao;

import android.content.Context;
import android.os.Environment;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.EncryptFileCacheHelper;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.IFileCacheHelper;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.am.fw.log.util.FileUtil;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * 本地专题文件管理
 *
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.proj.dao
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public class LocalProjectStorageDao {

    public static final String DEFAULT_PROJECT_ID = "default_project_id";
    // public static final String defaultProjectId = "";

    public List<ProjectInfo> getAllProjects(Context context, String userId) {

        FilePathUtil manager = new FilePathUtil(context);
        String filePath = manager.getSavePath() + "/project/project" + userId + ".txt";
        String result = null;
        IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
        byte[] bytes = amFileHelper2.getOfflineCacheFile(filePath);
        if (bytes == null) {
            return null;
        }
      /*  try {
            result = AMFileUtil.readUtf8(new File(filePath));
        } catch (IOException e) {
            LogUtil.d("本地没有专题文件");
            return null;
        }*/
        result = new String(bytes);
        JsonReader reader = new JsonReader(new StringReader(result));
        reader.setLenient(true);
        return JsonUtil.getObject(reader, new TypeToken<List<ProjectInfo>>() {
        }.getType());
    }


    public ProjectInfo getProjectById(Context context, String userId, String projectId) throws IOException {

        List<ProjectInfo> allProjects = getAllProjects(context, userId);
        if (ValidateUtil.isListNull(allProjects)) {
            return null;
        }
        for (ProjectInfo project : allProjects) {
            if (project.getProjectId().equals(projectId)) {
                return project;
            }
        }
        return null;
    }


    public void saveProjectsToLocal(Context context, String userId, List<ProjectInfo> projectInfos) {

        //保存默认专题id到sp中
        String projectId = projectInfos.get(0).getProjectId();
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(DEFAULT_PROJECT_ID, projectId);
        //保存json
        String json = JsonUtil.getJson(projectInfos);
        FilePathUtil manager = new FilePathUtil(context);
        String filePath = manager.getSavePath() + "/project/project" + userId + ".txt";


        IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
        amFileHelper2.addOfflineCacheFile(json.getBytes(), filePath);

      /*  try {
            AMFileUtil.saveStringToFile(json, Charset.defaultCharset(),new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 删除/project文件夹
     *
     * @param context
     * @param userId
     */
    public void deleteAllProjects(Context context, String userId) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FilePathUtil manager = new FilePathUtil(context);
            String projectPath = manager.getProjectFileSavePath();
            // String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AGMobile/project"+SharePreferencesManager.getInstance(context).getUserId()+".txt";
            FileUtil.deleteDir(new File(projectPath));
        }
    }
}
