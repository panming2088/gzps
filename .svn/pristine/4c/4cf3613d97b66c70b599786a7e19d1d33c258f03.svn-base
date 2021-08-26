package com.augurit.am.cmpt.login.dao;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.am.cmpt.login.model.LocalUser;
import com.augurit.am.cmpt.login.util.LoginConstant;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;

import java.util.List;
import java.util.UUID;

/**
 * 描述：本地登录数据访问
 * @author 创建人 ：xiejiexin
 * @version
 * @package 包名 ：com.augurit.am.cmpt.login.dao
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */
public class LocalLoginDao {

    private AMDatabase mDatabase;
    private SharedPreferencesUtil mSpUtil;

    /**
     * 构造方法
     * @param context Context
     * @param database 数据库
     */
    public LocalLoginDao(Context context, AMDatabase database) {
        mDatabase = database;
        mSpUtil = new SharedPreferencesUtil(context);
    }

    /**
     * 保存用户
     * @param user 用户
     */
    public void saveUser(LocalUser user) {
        mDatabase.save(user);
        mSpUtil.setString(LoginConstant.LAST_LOGIN_USER, user.getId());
    }

    /**
     * 保存在线登陆用户
     * @param user 用户
     */
    public void saveOnlineUser(LocalUser user) {
        mDatabase.save(user);
        mSpUtil.setString(LoginConstant.LAST_LOGIN_USER, user.getId());
        mSpUtil.setString(LoginConstant.LAST_ONLINE_LOGIN_USER, user.getId());
    }

    /**
     * 获取在线登陆用户
     */
    public LocalUser getOnlineUser() {
        try {
            String userId = mSpUtil.getString(LoginConstant.LAST_ONLINE_LOGIN_USER, "");
            return getUserById(userId);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 批量保存用户
     * @param users 用户List
     */
    public void saveUsers(List<LocalUser> users) {
        mDatabase.saveAll(users);
    }

    /**
     * 获取上次登录的用户
     * @return 用户
     */
    public LocalUser getUser() {
        try {
            String userId = mSpUtil.getString(LoginConstant.LAST_LOGIN_USER, "");
            return getUserById(userId);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取本地保存的所有用户
     * @return 用户List
     */
    public List<LocalUser> getAllUsers() {
        return mDatabase.getQueryAll(LocalUser.class);
    }

    /**
     * 获取本地保存的所有可显示在登录历史中的用户
     * @return 用户List
     */
    public List<LocalUser> getUsersShowInHistory() {
        return mDatabase.getQueryByWhere(LocalUser.class, "showInHistory", "true");
    }

    /**
     * 获取本地保存可使用指纹登录的用户
     * @return 用户List
     */
    public List<LocalUser> getFingerprintUsers() {
        return mDatabase.getQueryByWhere(LocalUser.class, "isFingerprint", "true");
    }

    /**
     * 获取指定Id的用户
     * @param id 用户id
     * @return 用户
     */
    public LocalUser getUserById(String id) {
        return mDatabase.getQueryById(LocalUser.class, id);
    }

    /**
     * 保存是否保存账号的设置状态
     * @param isSave 是否保存账号
     */
    public void saveUserNameState( boolean isSave) {
        mSpUtil.setBoolean(LoginConstant.SAVE_USERNAME_STATE,isSave);
    }

    /**
     * 获取是否保存账户名的设置状态
     * @return 是否保存账户
     */
    public boolean getUserNameState() {
        return mSpUtil.getBoolean(LoginConstant.SAVE_USERNAME_STATE,false);
    }

    /**
     * 保存是否保存密码的设置状态
     * @param isSave 是否保存密码
     */
    public void savePasswordState( boolean isSave) {
        mSpUtil.setBoolean(LoginConstant.SAVE_PASSWORD_STATE,isSave);
    }

    /**
     * 获取是否保存密码的设置状态
     * @return 是否保存密码
     */
    public boolean getPasswordState() {
        return mSpUtil.getBoolean(LoginConstant.SAVE_PASSWORD_STATE,false);
    }

    /**
     * 保存配置的服务器地址
     * @param serverUrl 服务器地址
     */
    public void saveServerUrl(String serverUrl) {
        mSpUtil.setString(LoginConstant.LOGIN_SERVER_URL_KEY, serverUrl);
    }

    /**
     * 获取服务器地址
     * @return 服务器地址
     */
    public String getServerUrl() {
        return mSpUtil.getString(LoginConstant.LOGIN_SERVER_URL_KEY, "");
    }

    /**
     * 保存配置的地名地址服务地址
     * @param supportUrl 地名地址服务地址
     */
    public void saveSupportUrl(String supportUrl) {
        mSpUtil.setString(LoginConstant.LOGIN_SUPPORT_URL_KEY, supportUrl);
    }

    /**
     * 获取地名地址服务地址
     * @return 地名地址服务地址
     */
    public String getSupportUrl() {
        return mSpUtil.getString(LoginConstant.LOGIN_SUPPORT_URL_KEY, "");
    }

    /**
     * 保存是否更新专题设置
     * @param isUpdate 是否更新专题
     */
    public void saveUpdateProjectState(boolean isUpdate) {
        mSpUtil.setBoolean(LoginConstant.UPDATE_PROJECT_STATE, isUpdate);
    }

    /**
     * 获取是否更新专题信息
     * @return 是否更新专题
     */
    public boolean getUpdateProjectState() {
        return mSpUtil.getBoolean(LoginConstant.UPDATE_PROJECT_STATE, true);  // 若是第一次登录则需勾选
    }

    /**
     * 获取设备Id
     * <br>如果是第一次获取，则生成一个设备Id返回并保存
     * @return 设备Id
     */
    public String getDeviceId() {
        String deviceId = mSpUtil.getString(LoginConstant.DEVICE_ID, "");
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = UUID.randomUUID().toString();  // 目前是生成一个UUID
            mSpUtil.setString(LoginConstant.DEVICE_ID, deviceId); // 存
        }
        return deviceId;
    }
}
