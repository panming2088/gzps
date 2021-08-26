package com.augurit.am.cmpt.login.router;

import android.content.Context;

import com.augurit.am.cmpt.login.dao.LocalLoginDao;
import com.augurit.am.cmpt.login.dao.RemoteLoginDao;
import com.augurit.am.cmpt.login.model.LocalUser;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.common.ValidateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：登录数据路由
 * @author 创建人 ：xiejiexin
 * @version
 * @package 包名 ：com.augurit.am.cmpt.login.router
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */
public class LoginRouter {

    protected LocalLoginDao mLocalDao;
    protected RemoteLoginDao mRemoteDao;

    public LoginRouter(Context context, AMDatabase database) {
        mLocalDao = new LocalLoginDao(context, database);
        mRemoteDao = new RemoteLoginDao();
    }

    /**
     * 登录Get方式
     * @param url url
     * @return 登录成功后从服务端返回的User
     * @throws Exception 登录错误
     */
    public User loginGet(String url) throws Exception {
        // 执行登录
        User user = mRemoteDao.login(url);
//        if (!ValidateUtil.isObjectNull(user)) {   // 不在此处保存用户信息，无法确定是否保存在历史等
//            // 保存用户信息
//            LocalUser localUser = new LocalUser();
//            localUser.setId(user.getId());
//            localUser.setUser(user);
//            localUser.setShowInHistory(true);
//            mLocalDao.saveUser(localUser);
//        }
        return user;
    }

    /**
     * 登录Post方式
     * @param url url
     * @param params 参数Map
     * @return 登录成功后从服务端返回的User
     * @throws Exception 登录错误
     */
    public User loginPost(String url, Map<String, String> params) throws Exception {
        // 执行登录
        User user = mRemoteDao.login(url, params);
//        // 保存用户信息
//        LocalUser localUser = new LocalUser();
//        localUser.setId(user.getId());
//        localUser.setUser(user);
//        localUser.setShowInHistory(true);
//        mLocalDao.saveUser(localUser);
        return user;
    }

    /**
     * 验证用户名是否有效
     * @param url 请求url
     * @return 用户名是否有效
     * @throws Exception 验证出错
     */
    public boolean checkUserName(String url) throws Exception {
        return mRemoteDao.checkUserName(url);
    }

    /**
     * 验证DeviceId是否有效
     * @param url 请求url
     * @param params 参数Map
     * @return DeviceId是否有效
     * @throws Exception 验证出错
     */
    public boolean checkDeviceId(String url, Map<String, String> params) throws Exception {
        return mRemoteDao.checkDeviceId(url, params);
    }

    /**
     * 保存本次登录的用户
     * @param user 用户
     * @param isShowInHistory 是否显示在登录历史记录中
     * @param isFingerprint 是否可使用指纹登录
     */
    public void saveUser(User user, boolean isShowInHistory, boolean isFingerprint) {
        LocalUser localUser = new LocalUser();
        localUser.setUser(user);
        localUser.setId(user.getId());
        localUser.setShowInHistory(isShowInHistory);
        localUser.setFingerprint(isFingerprint);
        localUser.setLoginTime(new Date());
        localUser.setLoginOffline(true);
        localUser.setFirstLoginTodayOnline(false);
        if (isFingerprint) {    // 目前只允许一个指纹登录账户
            List<LocalUser> users = mLocalDao.getFingerprintUsers();
            if (users != null && !users.isEmpty()) {
                for (LocalUser user1 : users) {
                    user1.setFingerprint(false);
                }
                mLocalDao.saveUsers(users);
            }
        }
        mLocalDao.saveUser(localUser);
    }

    /**
     * 保存本次在线登录的用户
     * @param user 用户
     */
    public void saveOnlineUser(User user) {
        LocalUser localUser = new LocalUser();
        localUser.setUser(user);
        localUser.setId(user.getId());
        localUser.setFingerprint(false);
        localUser.setShowInHistory(true);
        localUser.setLoginOffline(false);
        localUser.setLoginTime(new Date());
        // 查找上一条该用户记录，设置是否当日第一次在线登录
        LocalUser lastRecord = mLocalDao.getUserById(user.getId());
        if (lastRecord != null && lastRecord.getLoginTime() != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd");
            int lastDay = Integer.parseInt(format.format(lastRecord.getLoginTime()));
            int today = Integer.parseInt(format.format(localUser.getLoginTime()));
            localUser.setFirstLoginTodayOnline(lastDay != today);
        } else {
            localUser.setFirstLoginTodayOnline(true);
        }
        mLocalDao.saveOnlineUser(localUser);
    }

    /**
     * 获取上次在线登录的用户
     */
    public User getOnlineUser() {
        LocalUser localUser = mLocalDao.getOnlineUser();
        if (!ValidateUtil.isObjectNull(localUser)) {
            return localUser.getUser();
        } else {
            return null;
        }
    }

    /**
     * 获取上次登录/当前正登录的用户<br>
     * 若用户为不显示在历史记录中则返回空(登录前)
     * @return 用户
     */
    public User getUser(){
        LocalUser localUser = mLocalDao.getUser();
        if (!ValidateUtil.isObjectNull(localUser)
                && localUser.isShowInHistory()) {
            return localUser.getUser();
        } else {
            return null;
        }
    }

    /**
     * 用户是否当日第一次在线登录
     * @return
     */
    public boolean isUserFirstLoginTodayOnline(String id) {
        LocalUser userById = mLocalDao.getUserById(id);
        if (userById != null) {
            return userById.isFirstLoginTodayOnline();
        } else {
            return false;
        }
    }

    /**
     * 获取本地保存的所有用户
     * @return 用户List
     */
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        List<LocalUser> localUsers = mLocalDao.getAllUsers();
        for (LocalUser localUser : localUsers) {
            list.add(localUser.getUser());
        }
        return list;
    }

    /**
     * 获取本地保存的所有可显示在登录历史中的用户
     * @return 用户List
     */
    public List<User> getUsersShowInHistory() {
        List<User> list = new ArrayList<>();
        List<LocalUser> localUsers = mLocalDao.getUsersShowInHistory();
        for (LocalUser localUser : localUsers) {
            list.add(localUser.getUser());
        }
        return list;
    }

    /**
     * 获取可指纹登录的用户
     * @return 用户
     */
    public User getFingerprintUser() {
        List<LocalUser> users = mLocalDao.getFingerprintUsers();
        if (users != null && !users.isEmpty()) {
            return users.get(0).getUser();
        }
        return null;
    }

    /**
     * 保存服务器地址
     * @param serverUrl 服务器地址
     */
    public void saveServerUrl(String serverUrl) {
        mLocalDao.saveServerUrl(serverUrl);
    }

    /**
     * 获取服务器地址
     * @return 服务器地址
     */
    public String getServerUrl() {
        return mLocalDao.getServerUrl();
    }

    /**
     * 保存地名地址服务地址
     * @param supportUrl 地名地址服务地址
     */
    public void saveSupportUrl(String supportUrl) {
        mLocalDao.saveSupportUrl(supportUrl);
    }

    /**
     * 获取地名地址服务地址
     * @return 地名地址服务地址
     */
    public String getSupportUrl() {
        return mLocalDao.getSupportUrl();
    }

    /**
     * 保存是否更新专题状态
     * @param isUpdateProject 是否更新专题
     */
    public void saveUpdateProjectState(boolean isUpdateProject) {
        mLocalDao.saveUpdateProjectState(isUpdateProject);
    }

    /**
     * 获取是否更新专题状态
     * @return 是否更新专题
     */
    public boolean getUpdateProjectState() {
        return mLocalDao.getUpdateProjectState();
    }

    /**
     * 保存是否保存密码状态
     * @param isSavePassword 是否保存密码
     */
    public void savePasswordState(boolean isSavePassword) {
        mLocalDao.savePasswordState(isSavePassword);
    }

    /**
     * 获取是否保存密码
     * @return 是否保存密码
     */
    public boolean getPasswordState() {
        return mLocalDao.getPasswordState();
    }

    /**
     * 保存是否保存用户名状态
     * @param isSaveUserName 是否保存用户名
     */
    public void saveUserNameState(boolean isSaveUserName) {
        mLocalDao.saveUserNameState(isSaveUserName);
    }

    /**
     * 获取是否保存用户名
     * @return 是否保存用户名
     */
    public boolean getUserNameState() {
        return mLocalDao.getUserNameState();
    }

    /**
     * 获取设备ID
     * @return 设备ID
     */
    public String getDeviceId() {
        return mLocalDao.getDeviceId();
    }

    /**
     * 设置登录超时
     * @param timeOut 超时
     */
    public void setTimeOut(int timeOut) {
        mRemoteDao.setTimeOut(timeOut);
    }
}
