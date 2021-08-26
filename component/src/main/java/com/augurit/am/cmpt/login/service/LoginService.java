package com.augurit.am.cmpt.login.service;

import android.app.ProgressDialog;
import android.content.Context;

import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.scy.CryptoUtil;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 描述：登录业务逻辑处理
 * @author 创建人 ：xiejiexin
 * @version
 * @package 包名 ：com.augurit.am.cmpt.login.service
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */
public class LoginService implements ILoginService {
    private WeakReference<Context> mContext;

    private ProgressDialog mProgressDialog;

    protected LoginRouter mRouter;

    public LoginService(Context context, AMDatabase database) {
        this.mContext = new WeakReference<Context>(context);
        mRouter = new LoginRouter(context.getApplicationContext(), database);
    }

    @Override
    public void loginGet(final String url, final Callback2<User> callback) {
        Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                try {
                    User user = mRouter.loginGet(url);
                    subscriber.onNext(user);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if(mContext.get() != null){
                            mProgressDialog = new ProgressDialog(mContext.get());
                            mProgressDialog.setMessage("登录中，请稍侯...");
                            mProgressDialog.show();
                        }

                    }
                })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {
                if(mProgressDialog != null && mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {
                if(mProgressDialog != null && mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
                callback.onFail((Exception) e);
            }

            @Override
            public void onNext(User user) {
                if(mProgressDialog != null && mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
                callback.onSuccess(user);
            }
        });
    }

    @Override
    public void loginPost(String url, Map<String, String> params, Callback2<User> callback) {
        // 目前没有实现
    }

    @Override
    public void loginOffline(String userName, String password, Callback2<User> callback) {
        if(mContext.get() != null){
            mProgressDialog = new ProgressDialog(mContext.get());
            mProgressDialog.setMessage("离线登录中，请稍后...");
            mProgressDialog.show();
        }

        boolean isUserExist = false;    // 用户名是否存在
        List<User> users = mRouter.getAllUsers(); // 与保存在本地的用户进行比对
        for (User user : users) {
            if (userName.equals(user.getLoginName())) {
                isUserExist = true;
                String passwordDecrypt = CryptoUtil.decryptText(user.getPassword());
                if (password.equals(passwordDecrypt)) {
                    user.setPassword(passwordDecrypt);
                    callback.onSuccess(user);
                    if(mProgressDialog != null && mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    return;
                }
            }
        }
        if (isUserExist) {
            callback.onFail(new Exception("用户名或密码错误"));
        } else {
            callback.onFail(new Exception("该用户不存在或未在线登录过"));
        }
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void checkUserName(final String url, final Callback2<Boolean> callback) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean b = mRouter.checkUserName(url);
                    subscriber.onNext(b);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {
                callback.onFail((Exception) e);
            }
            @Override
            public void onNext(Boolean aBoolean) {
                callback.onSuccess(aBoolean);
            }
        });
    }

    @Override
    public void checkDeviceId(final String url, final Map<String, String> params, final Callback2<Boolean> callback) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean b = mRouter.checkDeviceId(url, params);
                    subscriber.onNext(b);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Subscriber<Boolean>() {
              @Override
              public void onCompleted() {
              }

              @Override
              public void onError(Throwable e) {
                  callback.onFail((Exception) e);
              }

              @Override
              public void onNext(Boolean aBoolean) {
                callback.onSuccess(aBoolean);
              }
          });
    }

    @Override
    public void saveUser(User user) {
        saveUser(user, true);
    }

    @Override
    public void saveUser(User user, boolean isShowInHistory) {
        User fingerprintUser = mRouter.getFingerprintUser();  // 若是可指纹登录的用户，则要保持指纹登录可用
        if (fingerprintUser != null && fingerprintUser.getLoginName().equals(user.getLoginName())) {
            saveUser(user, isShowInHistory, true);
        } else {
            saveUser(user, isShowInHistory, false);
        }
    }

    @Override
    public void saveUser(User user, boolean isShowInHistory, boolean isFingerprint) {
        String password = CryptoUtil.encryptText(user.getPassword());   // 密码加密
        User temp = null;
        try {
            temp = (User) user.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        temp.setPassword(password == null? user.getPassword() : password);
        mRouter.saveUser(temp, isShowInHistory, isFingerprint);
    }

    @Override
    public void saveOnlineUser(User user) {
        String password = CryptoUtil.encryptText(user.getPassword());   // 密码加密
        User temp = null;
        try {
            temp = (User) user.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        temp.setPassword(password == null? user.getPassword() : password);
        mRouter.saveOnlineUser(temp);
    }

    @Override
    public User getUser() {
        User user = mRouter.getUser();
        if (user != null) {
            String password = CryptoUtil.decryptText(user.getPassword());
            user.setPassword(password == null? user.getPassword() : password);
        }
        return user;
    }

    /**
     * 用户是否当日第一次在线登录
     * @return
     */
    public boolean isUserFirstLoginTodayOnline(String id) {
        return mRouter.isUserFirstLoginTodayOnline(id);
    }

    @Override
    public List<User> getUsersShowInHistory() {
        List<User> users = mRouter.getUsersShowInHistory();
        for (User user : users) {
            String password = CryptoUtil.decryptText(user.getPassword());
            user.setPassword(password == null? user.getPassword() : password);
        }
        return users;
    }

    @Override
    public User getFingerprintUser() {
        User user = mRouter.getFingerprintUser();
        if (user != null) {
            String password = CryptoUtil.decryptText(user.getPassword());
            user.setPassword(password == null? user.getPassword() : password);
        }
        return user;
    }

    @Override
    public void saveServerUrl(String serverUrl) {
        mRouter.saveServerUrl(serverUrl);
    }

    @Override
    public User getOnlineUser() {
        User user = mRouter.getOnlineUser();
        if (user != null) {
            String password = CryptoUtil.decryptText(user.getPassword());
            user.setPassword(password == null? user.getPassword() : password);
        }
        return user;
    }

    @Override
    public String getServerUrl() {
        return mRouter.getServerUrl();
    }

    @Override
    public void saveSupportUrl(String supportUrl) {
        mRouter.saveSupportUrl(supportUrl);
    }

    @Override
    public String getSupportUrl() {
        return mRouter.getSupportUrl();
    }

    @Override
    public void saveUpdateProjectState(boolean isUpdateProject) {
        mRouter.saveUpdateProjectState(isUpdateProject);
    }

    @Override
    public boolean getUpdateProjectState() {
        return mRouter.getUpdateProjectState();
    }

    @Override
    public void savePasswordState(boolean isSavePassword) {
        mRouter.savePasswordState(isSavePassword);
    }

    @Override
    public boolean getPasswordState() {
        return mRouter.getPasswordState();
    }

    @Override
    public void saveUserNameState(boolean isSaveUserName) {
        mRouter.saveUserNameState(isSaveUserName);
    }

    @Override
    public boolean getUserNameState() {
        return mRouter.getUserNameState();
    }

    @Override
    public String getDeviceId() {
        return mRouter.getDeviceId();
    }

    @Override
    public void setLoginTimeOut(int timeOut) {
        mRouter.setTimeOut(timeOut);
    }

    @Override
    public boolean canLoginOffline() {
        return mRouter.getAllUsers().size() != 0;
    }

}
