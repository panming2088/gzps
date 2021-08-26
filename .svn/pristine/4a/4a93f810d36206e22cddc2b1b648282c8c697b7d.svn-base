package com.augurit.am.cmpt.login.service;

import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.login.model.LocalUser;
import com.augurit.am.cmpt.login.model.User;

import java.util.List;
import java.util.Map;

/**
 * 描述：登录Service接口
 * @author 创建人 ：xiejiexin
 * @version
 * @package 包名 ：com.augurit.am.cmpt.login.service
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */
public interface ILoginService {

    /**
     * 登录GET方式
     * @param url 请求url
     * @param callback 操作回调
     */
    void loginGet(String url, Callback2<User> callback);

    /**
     * 登录POST方式
     * @param url 请求url
     * @param params POST参数
     * @param callback 操作回调
     */
    void loginPost(String url, Map<String, String> params, Callback2<User> callback);

    /**
     * 离线登录
     * @param userName 用户名
     * @param password 密码
     * @param callback 操作回调
     */
    void loginOffline(String userName, String password, Callback2<User> callback);

    /**
     * 检查用户名是否有效<br>
     * 请求方式：GET
     * @param url 请求url
     * @param callback 操作回调
     */
    void checkUserName(String url, Callback2<Boolean> callback);

    /**
     * 检查设备号是否有效
     * 请求方式：POST
     * @param url
     * @param params
     * @param callback
     */
    void checkDeviceId(String url, Map<String, String> params, Callback2<Boolean> callback);

    /**
     * 保存用户到本地
     * <br>默认显示在登录历史中
     * <br>默认不可指纹登录
     * @param user 用户
     */
    void saveUser(User user);

    /**
     * 保存用户到本地
     * <br>默认不可指纹登录
     * @param user 用户
     * @param isShowInHistory 是否显示在登录历史中
     */
    void saveUser(User user, boolean isShowInHistory);

    /**
     * 保存用户到本地
     * @param user 用户
     * @param isShowInHistory 是否显示在登录历史中
     * @param isFingerprint 是否可指纹登录
     */
    void saveUser(User user, boolean isShowInHistory, boolean isFingerprint);

    /**
     * 保存用户到本地
     * @param user 用户
     */
    void saveOnlineUser(User user);

    /**
     * 获取上次登录/当前正登录的用户
     * @return 用户
     */
    User getUser();

    /**
     * 用户是否当日第一次在线登录
     * @return
     */
    boolean isUserFirstLoginTodayOnline(String id);

    /**
     * 获取所有显示在登录历史中的用户
     * @return 用户List
     */
    List<User> getUsersShowInHistory();

    /**
     * 获取可指纹登录的用户
     * @return 用户
     */
    User getFingerprintUser();

    /**
     * 保存服务器地址
     * @param serverUrl 服务器地址
     */
    void saveServerUrl(String serverUrl);

    /**
     * 获取上次在线登录的用户
     * @return 用户
     */
    User getOnlineUser();

    /**
     * 获取服务器地址
     * @return 服务器地址
     */
    String getServerUrl();

    /**
     * 保存地名地址服务地址
     * @param supportUrl 地名地址服务地址
     */
    void saveSupportUrl(String supportUrl);

    /**
     * 获取地名地址服务地址
     * @return 地名地址服务地址
     */
    String getSupportUrl();

    /**
     * 保存是否更新专题状态
     * @param isUpdateProject 是否更新专题
     */
    void saveUpdateProjectState(boolean isUpdateProject);

    /**
     * 获取是否更新专题状态
     * @return 是否更新专题
     */
    boolean getUpdateProjectState();

    /**
     * 保存是否保存密码状态
     * @param isSavePassword 是否保存密码
     */
    void savePasswordState(boolean isSavePassword);

    /**
     * 获取是否保存密码
     * @return 是否保存密码
     */
    boolean getPasswordState();

    /**
     * 保存是否保存用户名状态
     * @param isSaveUserName 是否保存用户名
     */
    void saveUserNameState(boolean isSaveUserName);

    /**
     * 获取是否保存用户名
     * @return 是否保存用户名
     */
    boolean getUserNameState();

    /**
     * 获取设备Id
     * @return 设备Id
     */
    String getDeviceId();

    /**
     * 设置登录超时，单位：秒
     * @param timeOut 超时
     */
    void setLoginTimeOut(int timeOut);

    /**
     * 是否可进行离线登录
     * @return 是否可进行离线登录
     */
    boolean canLoginOffline();
}
