package com.augurit.agmobile.gzps;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.common.service.ConfigureNetCallback;
import com.augurit.agmobile.gzps.common.service.TestConfigureService;
import com.augurit.agmobile.gzps.common.util.KeyBoardHelper;
import com.augurit.agmobile.gzps.common.util.SystemUtils;
import com.augurit.agmobile.gzps.login.FunUpdateInfoService;
import com.augurit.agmobile.gzps.login.ILoginCallback;
import com.augurit.agmobile.gzps.login.PatrolLoginService;
import com.augurit.agmobile.gzps.login.model.FunUpdateInfoModel;
import com.augurit.agmobile.gzps.login.utils.FunUpdateConstant;
import com.augurit.agmobile.mapengine.common.AGMobileSDK;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.util.LayerFactoryProvider;
import com.augurit.agmobile.mapengine.project.service.AgwebProjectService;
import com.augurit.agmobile.mapengine.project.util.ProjectServiceFactory;
import com.augurit.agmobile.patrolcore.common.action.dao.local.ActionDBLogic;
import com.augurit.agmobile.patrolcore.common.action.dao.remote.ActionNetLogic;
import com.augurit.agmobile.patrolcore.common.action.model.ActionModel;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.layer.service.AgwebPatrolLayerService;
import com.augurit.agmobile.patrolcore.layer.util.PatrolLayerFactory;
import com.augurit.agmobile.patrolcore.search.service.PatrolSearchServiceImpl2;
import com.augurit.agmobile.patrolcore.search.util.PatrolSearchServiceProvider;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.ILoginService;
import com.augurit.am.cmpt.login.view.adapter.UserNameAdapter;
import com.augurit.am.cmpt.maintenance.traffic.service.TrafficService;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.update.utils.CheckUpdateUtils;
import com.augurit.am.cmpt.update.utils.UpdateState;
import com.augurit.am.cmpt.update.view.ApkUpdateManager;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.log.util.NetUtil;
import com.augurit.am.fw.utils.MaxLengthInputFilter;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * ???????????????Activity
 *
 * @author ????????? ???xiejiexin
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.agpatrol
 * @createTime ???????????? ???2017-03-03
 * @modifyBy ????????? ???xiejiexin
 * @modifyTime ???????????? ???2017-03-03
 * @modifyMemo ???????????????
 */
public class LoginActivity extends BaseActivity {
    private ILoginService mLoginService;
    private View btn_setting;
    private AutoCompleteTextView at_userName;
    private EditText et_pwd;
    private TextInputLayout til_login_name;
    private TextInputLayout til_login_password;
    private View btn_login;
    private ViewGroup ll_cb_save_password;      //??????????????????
    private CheckBox cb_save_pwd;
    private boolean mIsUserNameExists = true;      // ?????????????????????
    private boolean mIsSelectLoginName = false;
    private boolean mIsUpdateProject = true; // ??????????????????
    private boolean mDistUpdate = false;//????????????????????????
    private boolean mProjectUpdate = false;//????????????????????????
    private boolean mMenuItemUpdate = false;//????????????????????????

    private ActionNetLogic mActionNetLogic;
    private ActionDBLogic mLocalMenuStorageDao;

    private FunUpdateInfoService mFunUpdateInfoService;//????????????
    private SharedPreferencesUtil mSharedPreferencesUtil;
    private final static String UPDATE_TIME = "update_time";
    private SimpleDateFormat CurrentTime;
    private View layoutContent;
    private View layoutBottom;
    private TextView tv_version;
    private KeyBoardHelper boardHelper;
    private int bottomHeight;
    private String[] appUpdateUrlArr = {LoginConstant.APP_UPDATE_ONE, LoginConstant.APP_UPDATE_TWO};
    private String inStall_flag;
    private boolean firstInit = true;
    private View view_small_divider;
    private View view_divider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mLocalMenuStorageDao = new ActionDBLogic();
        mLoginService = new PatrolLoginService(this, AMDatabase.getInstance());//xcl 2017-03-21 ??????service????????????
        initView();
        initValue();
        checkVersionUpdate();
        checkLowestSystem();
        initListener();
        createNoMediaFile();

        // AMDatabase.init(getApplicationContext());
        // startTrafficStatsService();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * ??????APP????????????
     */
    private void checkLowestSystem() {
        SystemUtils.checkSystemInfo(this, new SystemUtils.CheckCallBack() {
            @Override
            public void onSuccess(boolean lowest_memory, boolean lowest_version) {
                if (!lowest_memory && !lowest_version) {
                    showSystemWarning("?????????????????????3G?????????????????????5.0??????");
                } else if (!lowest_memory) {
                    showSystemWarning("?????????????????????3G??????");
                } else if (!lowest_version) {
                    showSystemWarning("????????????????????????5.0??????");
                }
            }
        });
    }

    /**
     * ?????????????????????
     */
    private void showSystemWarning(String warning) {
        // ???????????????
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("????????????")
                .setMessage(warning)
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode) {
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.login_lyrl_dialog);
    }

    private void createNoMediaFile() {
        try {
            String savePath = new FilePathUtil(this.getApplicationContext()).getSavePath();
            String noMediaFile = savePath + "/.nomedia";
            File file = new File(noMediaFile);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {

        }
    }

    /**
     * ????????????????????????????????????
     */
    private KeyBoardHelper.OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener = new KeyBoardHelper.OnKeyBoardStatusChangeListener() {

        @Override
        public void OnKeyBoardPop(int keyBoardheight) {
            final int height = keyBoardheight;
            if (bottomHeight > height) {
                layoutBottom.setVisibility(View.GONE);
            } else {
                view_small_divider.setVisibility(View.VISIBLE);
                view_divider.setVisibility(View.GONE);
                int offset = bottomHeight - height;
                final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layoutContent
                        .getLayoutParams();
                lp.topMargin = offset;
                layoutContent.setLayoutParams(lp);
            }
            tv_version.setVisibility(View.GONE);
        }

        @Override
        public void OnKeyBoardClose(int oldKeyBoardheight) {
            if (View.VISIBLE != layoutBottom.getVisibility()) {
                layoutBottom.setVisibility(View.VISIBLE);
            }
            view_small_divider.setVisibility(View.INVISIBLE);
            view_divider.setVisibility(View.VISIBLE);
            final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layoutContent
                    .getLayoutParams();
            if (lp.topMargin != 0) {
                lp.topMargin = 0;
                layoutContent.setLayoutParams(lp);
            }
            tv_version.setVisibility(View.VISIBLE);
        }
    };

    /**
     * ???????????????
     */
    private void initView() {
        btn_setting = findViewById(R.id.btn_login_setting);
        btn_login = findViewById(R.id.btn_login);
        at_userName = (AutoCompleteTextView) findViewById(R.id.at_login_name);
        et_pwd = (EditText) findViewById(R.id.et_login_password);
        til_login_name = (TextInputLayout) findViewById(R.id.til_login_name);
        til_login_password = (TextInputLayout) findViewById(R.id.til_login_password);
        cb_save_pwd = (CheckBox) findViewById(R.id.cb_login_save_password);
        ll_cb_save_password = (ViewGroup) findViewById(R.id.ll_cb_save_password);
        if (!LoginConstant.SAVE_PASSWORD) {
            // ????????????????????????????????????????????????????????????
            ll_cb_save_password.setVisibility(View.GONE);
        }
        layoutContent = findViewById(R.id.rl_login_form);
        layoutBottom = findViewById(R.id.ll_bottm);
        inStall_flag = new SharedPreferencesUtil(LoginActivity.this).getString("install_flag", "");
        //        if (TextUtils.isEmpty(BaseInfoManager.getLoginName(LoginActivity.this))) {
        //            ToastUtil.longToast(LoginActivity.this,"????????????????????????,????????????123456");
        //        }
        tv_version = (TextView) findViewById(R.id.tv_version);
        view_small_divider = findViewById(R.id.view_small_divider);
        view_divider = findViewById(R.id.view_divider);
    }

    /**
     * ????????????
     */
    private void initValue() {
        // ??????????????????
        //at_userName.setText(LoginConstant.DEFAULT_USERNAME);    // ???????????????????????????
        String loginName = BaseInfoManager.getLoginName(LoginActivity.this);
        if (!TextUtils.isEmpty(loginName) && LoginConstant.SAVE_PASSWORD) {
            at_userName.setText(loginName);
        }
        User user = mLoginService.getUser();   // ???????????????????????????
        if (!ValidateUtil.isObjectNull(user)) {
            if (LoginConstant.SAVE_USERNAME) {   // ????????????????????????????????????????????????
                at_userName.setText(user.getLoginName());
                at_userName.setSelection(user.getLoginName().length());
            }
            // ??????
            if (LoginConstant.SAVE_PASSWORD && !TextUtils.isEmpty(user.getRemark()) && user.getRemark().equals("1")) { // ???????????????????????????????????????????????????mLoginService.getPasswordState()
                et_pwd.setText(user.getPassword());
                et_pwd.setSelection(user.getPassword().length());
                cb_save_pwd.setChecked(true);
            }
        }
        // ????????????
        String serverUrl = mLoginService.getServerUrl();
        if (!TextUtils.isEmpty(serverUrl)) {
            LoginConstant.SERVER_URL = serverUrl;
        }
        String supportUrl = mLoginService.getSupportUrl();
        if (!TextUtils.isEmpty(supportUrl)) {
            LoginConstant.SUPPORT_URL = supportUrl;
        }
        mIsUpdateProject = mLoginService.getUpdateProjectState();
        CurrentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mSharedPreferencesUtil = new SharedPreferencesUtil(this);

        tv_version.setText("???????????????" + getLocalVersion());
    }

    /**
     * ???????????????
     */
    private void initListener() {
        //?????????????????????????????????
//        btn_setting.setOnClickListener(mOnClickListener);
        btn_login.setOnClickListener(mOnClickListener);
        ll_cb_save_password.setOnClickListener(mOnClickListener);
        // ???????????????????????????
        at_userName.setFilters(new InputFilter[]{new MaxLengthInputFilter(LoginConstant.MAX_LENGTH_USERNAME,
                til_login_name, at_userName, getString(R.string.login_error_max_length)).setDismissErrorDelay(1500)});
        // ???????????????????????????
        final List<User> userList = mLoginService.getUsersShowInHistory(); // ??????????????????????????????????????????
        final UserNameAdapter userNameAdapter = new UserNameAdapter(this, userList);
        userNameAdapter.setOnDeleteClickListener(new UserNameAdapter.OnItemClickListener() {
            @Override
            public void onNameClick(View v, User user) {
                // ????????????????????????????????????
                mIsSelectLoginName = true;
                at_userName.setText(user.getLoginName());
                at_userName.setSelection(user.getLoginName().length());
                at_userName.dismissDropDown();
                if (LoginConstant.SAVE_PASSWORD && !TextUtils.isEmpty(user.getRemark()) && user.getRemark().equals("1")) {//mLoginService.getPasswordState()
                    et_pwd.setText(user.getPassword());
                    cb_save_pwd.setChecked(true);
                } else {
                    cb_save_pwd.setChecked(false);
                }
                mIsSelectLoginName = false;
            }

            @Override
            public void onDeleteClick(View v, User user) {
                // ???????????????????????????????????????????????????????????????
                mLoginService.saveUser(user, false);
            }
        });
        at_userName.setThreshold(1);
        at_userName.setAdapter(userNameAdapter);
        at_userName.setDropDownVerticalOffset(2);
        at_userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = s.toString();
                if (LoginConstant.SAVE_USERNAME) {   // ????????????????????????????????????????????????
                    if (TextUtils.isEmpty(inputText)) {
                        return;
                    }
                    if (!firstInit) {
                        userNameAdapter.getFilter().filter(inputText);
                        at_userName.showDropDown();
                        firstInit = false;
                    }
                }
                if (!mIsUserNameExists) { // ???????????????????????????????????????????????????????????????
                    checkUserName();
                }
                if (!mIsSelectLoginName) {
                    et_pwd.setText("");
                }
                for (User user : userList) {
                    if (LoginConstant.SAVE_PASSWORD
                            && !TextUtils.isEmpty(user.getRemark())
                            && user.getRemark().equals("1") && user.getLoginName().equals(inputText)) {
                        et_pwd.setText(user.getPassword());
                        cb_save_pwd.setChecked(true);
                        break;
                    } else {
                        cb_save_pwd.setChecked(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        at_userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {   // ??????????????????????????????
                    checkUserName();
                }
            }
        });
        // ????????????????????????
        et_pwd.setFilters(new InputFilter[]{new MaxLengthInputFilter(LoginConstant.MAX_LENGTH_PASSWORD,
                til_login_password, et_pwd, getString(R.string.login_error_max_length)).setDismissErrorDelay(1500)});
        et_pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                                && KeyEvent.ACTION_DOWN == event.getAction())) {
                    loginWithCheck();   // ????????????Enter???????????????
                }
                return false;
            }
        });

        boardHelper = new KeyBoardHelper(this);
        boardHelper.onCreate();
        boardHelper.setOnKeyBoardStatusChangeListener(onKeyBoardStatusChangeListener);
        layoutBottom.post(new Runnable() {
            @Override
            public void run() {
                bottomHeight = layoutBottom.getHeight();
            }
        });
        // hideInput(this,at_userName);
    }

    private void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * ????????????????????????
     */
    private void startTrafficStatsService() {
        Intent MainService = new Intent(this, TrafficService.class);
        startService(MainService);//????????????
    }

    /**
     * ????????????
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    if (NetUtil.isConnected(LoginActivity.this.getApplicationContext())) {
                        //??????
                        loginWithCheck();
                    } else {
                        // ToastUtil.shortToast(LoginActivity.this, "????????????????????????????????????");
                        //  loginOfflineWithCheck();
                        ToastUtil.shortToast(LoginActivity.this, "????????????????????????????????????");
                    }
                    break;
                case R.id.btn_login_setting:
                    setting();
                    break;
                case R.id.ll_cb_save_password:
                    cb_save_pwd.performClick();
                    break;
            }
        }
    };

    /**
     * ??????????????????
     */
    private void checkFunUpdateInfo() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "??????", "????????????????????????");
        mFunUpdateInfoService = new FunUpdateInfoService(this);
        mFunUpdateInfoService.getFunUpdateInfo().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<FunUpdateInfoModel>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                ToastUtil.shortToast(LoginActivity.this, "????????????????????????");
            }

            @Override
            public void onNext(List<FunUpdateInfoModel> funUpdateInfoModels) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                String time = mSharedPreferencesUtil.getString(UPDATE_TIME, "0000-00-00 00:00:00.0");
                Date lastTime;
                try {
                    lastTime = CurrentTime.parse(time);

                    for (FunUpdateInfoModel model : funUpdateInfoModels) {
                        if (model.getType().equals(FunUpdateConstant.DICITIONARY)
                                && CurrentTime.parse(model.getDate()).getTime() > lastTime.getTime()) {//????????????
                            mDistUpdate = true;
                        } else if (model.getType().equals(FunUpdateConstant.LAYER_PROJECT)
                                && CurrentTime.parse(model.getDate()).getTime() > lastTime.getTime()) {//????????????
                            mProjectUpdate = true;
                        } else if (model.getType().equals(FunUpdateConstant.FUN_MENU)
                                && CurrentTime.parse(model.getDate()).getTime() > lastTime.getTime()) {//????????????
                            mMenuItemUpdate = true;
                        }
                    }
                    //??????????????????
                    updateConfigureData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * ???????????????????????????
     */
    public void loginWithCheck() {
        /*PermissionsUtil2.getInstance()
                .requestPermissions(
                        LoginActivity.this,
                        "????????????????????????????????????????????????????????????", 0,
                        new PermissionsUtil2.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                // checkGPSState();
                                login();
                            }
                        },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.KILL_BACKGROUND_PROCESSES,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE);*/

        PermissionsUtil.getInstance()
                .requestPermissions(
                        LoginActivity.this,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                // checkGPSState();
                                if(perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        && perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)){
                                    login();
                                } else {
                                    ToastUtil.shortToast(LoginActivity.this, "????????????????????????????????????");
                                }
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {
                                ToastUtil.shortToast(LoginActivity.this, "????????????????????????????????????");
                            }
                        },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.KILL_BACKGROUND_PROCESSES,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE);
    }

    public void checkGPSState() {
        //????????????????????????????????????GPS????????????
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (ok) {
            login();
        } else {
            ToastUtil.shortToast(this, "????????????????????????GPS????????????");
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 323);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 323) {
            //????????????????????????????????????GPS????????????
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!ok) {
                ToastUtil.longToast(this, "GPS?????????,???????????????????????????");
            }
            login();
        }
    }

    /**
     * ??????
     */
    // @NeedPermission(permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void login() {
        final String userName = at_userName.getText().toString().trim();
        final String password = et_pwd.getText().toString().trim();
        final User lastUser = mLoginService.getOnlineUser();
        if (!verifyInput()) {   // ?????????????????????????????????
            return;
        }
        final String url = "http://" + LoginConstant.SERVER_URL + LoginConstant.LOGIN_URL + userName + "/" + password;
        // ???????????????????????????
        btn_login.setEnabled(false);
        mLoginService.loginGet(url, new Callback2<User>() {
            @Override
            public void onSuccess(User user) {
                if (StringUtil.isEmpty(user.getLoginName())
                        || StringUtil.isEmpty(user.getPassword())) {
                    // ???????????????????????????????????????
                    ToastUtil.shortToast(LoginActivity.this, "???????????????????????????");
                    // ??????????????????
                    btn_login.setEnabled(true);
                    return;
                }
                if (!inStall_flag.equals(user.getLoginName())) {
                    //???????????????????????????????????????
                    String serial = android.os.Build.SERIAL;
                    checkInstallInfo(user.getLoginName(), user.getUserName(), serial);
                }
                //??????????????????url
                BaseInfoManager.setUserId(LoginActivity.this, user.getId());
                BaseInfoManager.setUserPhone(LoginActivity.this, user.getPhone());
                BaseInfoManager.setUserDuty(LoginActivity.this, user.getTitle());
                BaseInfoManager.setRoleName(LoginActivity.this, user.getRoleName());
                BaseInfoManager.setUserOrg(LoginActivity.this, user.getOrgName());
                BaseInfoManager.setOrgCode(LoginActivity.this, user.getOrgCode());
                BaseInfoManager.setUserName(LoginActivity.this, user.getUserName());
                BaseInfoManager.setLoginName(LoginActivity.this, user.getLoginName());
                BaseInfoManager.setServerUrl(LoginActivity.this, LoginConstant.SERVER_URL);
                BaseInfoManager.setSupportUrl(LoginActivity.this, LoginConstant.SUPPORT_URL);
                //??????imtoken
                BaseInfoManager.setImToken(LoginActivity.this, user.getToken());
                mActionNetLogic = new ActionNetLogic(LoginActivity.this, "http://" + LoginConstant.SERVER_URL + "/");
                // ????????????
                LogUtil.json(new Gson().toJson(user));
                //????????????token

                // ?????????????????????
                user.setPassword(password);// ???????????????????????????
                user.setRemark(cb_save_pwd.isChecked() ? "1" : "0");
                mLoginService.saveUser(user);   // ????????????
                mLoginService.saveOnlineUser(user);
                mLoginService.savePasswordState(cb_save_pwd.isChecked());   // ??????????????????CheckBox????????????
                // ???????????????????????????
                if (lastUser == null || !lastUser.getLoginName().equals(userName)) {
                    mIsUpdateProject = true;
                }
                if (mIsUpdateProject) {//|| BuildConfig.API_ENV
                    updateConfigureData();
                } else {
                    // TODO: 17/8/10 ??????????????????
                    checkFunUpdateInfo();
                }
                btn_login.setEnabled(true);
            }

            @Override
            public void onFail(Exception error) {
                error.printStackTrace();
                // ??????????????????
                btn_login.setEnabled(true);
                //   if (!error.getLocalizedMessage().contains("????????????????????????") && !error.getLocalizedMessage().contains("????????????")) {
                //       showLoginOfflineDialog();
                //   } else {
                ToastUtil.shortToast(LoginActivity.this, error.getLocalizedMessage());
                //   }
            }
        });
    }

    private void checkInstallInfo(final String loginName, final String userName, final String ime) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                String url = "http://" + LoginConstant.GZPS_AGSUPPORT + "/rest/installRecord/saveInstallInf";
                OkHttpClient client = new OkHttpClient.Builder().build();
                RequestBody formBody = new FormBody.Builder().add("login_name", loginName).add
                        ("user_name", userName).add
                        ("device_code", ime).build();
                Request request = new Request.Builder().url(url).post(formBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            subscriber.onNext(response.body().string());
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("message");
                    if (msg.equals("????????????")) {
                        new SharedPreferencesUtil(LoginActivity.this).setString("install_flag",
                                loginName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     */
    private void updateConfigureData() {
        if (mIsUpdateProject || mDistUpdate || mProjectUpdate || mMenuItemUpdate) {
            final ProgressDialog progressDialog = ProgressDialog.show(this, "??????", "??????????????????...");
            //????????????????????????
            String time = CurrentTime.format(new Date(System.currentTimeMillis()));
            mSharedPreferencesUtil.setString(UPDATE_TIME, time);

            final TestConfigureService configureService = new TestConfigureService(this);
            String url = "http://" + LoginConstant.SERVER_URL + "/";
            if (mIsUpdateProject || mProjectUpdate) {
                progressDialog.setMessage("????????????????????????");
                deleteProjectFolder();//?????????????????????????????????????????????
            }
            if (mIsUpdateProject || mDistUpdate) {
                progressDialog.setMessage("????????????????????????");
                configureService.updateFromNet(url, new ConfigureNetCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mIsUpdateProject || mMenuItemUpdate) {
                            progressDialog.setMessage("????????????????????????");
                            updateMenuConfigure(new ILoginCallback() {
                                @Override
                                public void onSuccess(String msg) {
                                    progressDialog.dismiss();
                                    mLoginService.saveUpdateProjectState(false);
                                    Toast.makeText(LoginActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                    jumpToMain();
                                }

                                @Override
                                public void onError(String msg) {
                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    btn_login.setEnabled(true);
                                    progressDialog.dismiss();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                            jumpToMain();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        progressDialog.dismiss();
                        btn_login.setEnabled(true);
                        ToastUtil.longToast(LoginActivity.this, msg);
                    }
                });
            } else if (mMenuItemUpdate) {
                progressDialog.setMessage("????????????????????????");
                updateMenuConfigure(new ILoginCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        progressDialog.dismiss();
                        mLoginService.saveUpdateProjectState(false);
                        Toast.makeText(LoginActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                        jumpToMain();
                    }

                    @Override
                    public void onError(String msg) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        btn_login.setEnabled(true);
                    }
                });
            } else {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                jumpToMain();
            }
        } else {
            Toast.makeText(LoginActivity.this, "????????????", Toast.LENGTH_SHORT).show();
            jumpToMain();
        }
    }

    /**
     * ???????????????????????????
     */
    public void loginOfflineWithCheck() {
        /*PermissionsUtil2.getInstance()
                .requestPermissions(
                        LoginActivity.this,
                        "????????????????????????????????????????????????????????????", 0,
                        new PermissionsUtil2.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                if(perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        && perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)){
                                    loginOffline();
                                } else {
                                    ToastUtil.shortToast(LoginActivity.this, "????????????????????????????????????");
                                }
                            }
                        },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE);*/

        PermissionsUtil.getInstance()
                .requestPermissions(
                        LoginActivity.this,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                if(perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        && perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)){
                                    loginOffline();
                                } else {
                                    ToastUtil.shortToast(LoginActivity.this, "????????????????????????????????????");
                                }
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {
                                ToastUtil.shortToast(LoginActivity.this, "????????????????????????????????????");
                            }
                        },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * ????????????
     */
    private void loginOffline() {
        btn_login.setEnabled(false);
        final String userName = at_userName.getText().toString().trim();
        final String password = et_pwd.getText().toString().trim();
        if (!verifyInput()) {
            return;
        }
        mLoginService.loginOffline(userName, password, new Callback2<User>() {
            @Override
            public void onSuccess(User user) {
                if (StringUtil.isEmpty(user.getLoginName())
                        || StringUtil.isEmpty(user.getPassword())) {
                    // ???????????????????????????????????????
                    ToastUtil.shortToast(LoginActivity.this, "???????????????????????????");
                    // ??????????????????
                    btn_login.setEnabled(true);
                    return;
                }
                //??????????????????url
                BaseInfoManager.setUserId(LoginActivity.this, user.getId());
                BaseInfoManager.setServerUrl(LoginActivity.this, LoginConstant.SERVER_URL);

                // ????????????
                LogUtil.json(new Gson().toJson(user));
                // ?????????????????????
                user.setPassword(password);     // ???????????????????????????
                mLoginService.saveUser(user);   // ????????????
                mLoginService.savePasswordState(cb_save_pwd.isChecked());   // ??????????????????CheckBox????????????

                Toast.makeText(LoginActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                jumpToMain();
            }

            @Override
            public void onFail(Exception error) {
                error.printStackTrace();
                btn_login.setEnabled(true);
                // ??????????????????
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                btn_login.setEnabled(true);
            }
        });
    }

    private String getLocalVersion() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // Log.e(TAG, "??????????????????????????????????????????" + e.getMessage());
            return "";
        }

        return info.versionName;
    }

    /**
     * ????????????
     */
    private void checkVersionUpdate() {

        checkVersionUpdateWithPermissonCheck();

    }

    private void checkVersionUpdateWithPermissonCheck() {
        String updateUrl = appUpdateUrlArr[new Random().nextInt(appUpdateUrlArr.length)];
        //String updateUrl = "http://" + LoginConstant.BASE_GZPS_URL + "/appFile/apk_version.json";
        CheckUpdateUtils.setServerUrl(updateUrl);
        new ApkUpdateManager(this, UpdateState.INNER_UPDATE, new ApkUpdateManager.NoneUpdateCallback() {
            @Override
            public void onFinish() {

            }
        }).checkUpdate();
    }

    /**
     * ????????????????????????????????????????????????
     */
    private void showLoginOfflineDialog() {
        if (mLoginService.canLoginOffline()) {
            View view = View.inflate(this, R.layout.login_alert_dialog, null);
            View btn_positive = view.findViewById(R.id.btn_positive);
            View btn_negative = view.findViewById(R.id.btn_negative);
            final Dialog dialog = new Dialog(this);
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //??????????????????????????????
                    loginOfflineWithCheck();
                }
            });
            btn_negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view);
            dialog.show();
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (KeyEvent.KEYCODE_BACK == keyCode) {
                        dialog.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource(R.drawable.bookmark_lyrl_dialog);
        }
    }

    /**
     * ?????????????????????
     */
    private void setting() {
        // ???????????????
        final ViewGroup view_setting = (ViewGroup) getLayoutInflater().inflate(R.layout.login_dialog_settings, null);
        Button btn_save = (Button) view_setting.findViewById(R.id.btn_save);        // ????????????
        final TextInputLayout til_server_url = (TextInputLayout) view_setting.findViewById(R.id.til_server_url);
        final EditText et_server_url = (EditText) view_setting.findViewById(R.id.et_server_url);  // ???????????????
        final Switch sw_login_update_proj = (Switch) view_setting.findViewById(R.id.sw_login_update_proj);    // ??????????????????
        // ????????????
        et_server_url.setText(TextUtils.isEmpty(mLoginService.getServerUrl()) ?
                LoginConstant.SERVER_URL : mLoginService.getServerUrl());
        et_server_url.setSelection(et_server_url.getText().length());
        sw_login_update_proj.setChecked(mLoginService.getUpdateProjectState());
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("??????")
                .setView(view_setting)
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // ????????????
                        String serverUrl = et_server_url.getText().toString();
                        if (serverUrl.isEmpty()) {
                            til_server_url.setError("??????????????????");
                            return;
                        }
                        // ?????????????????????
                        mLoginService.saveServerUrl(serverUrl);
                        BaseInfoManager.setServerUrl(LoginActivity.this, serverUrl);
                        mLoginService.saveUpdateProjectState(sw_login_update_proj.isChecked());
                        //????????????ip????????????????????????static?????????
                        LoginConstant.SERVER_URL = serverUrl;
                        //????????????????????????????????????
                        mIsUpdateProject = sw_login_update_proj.isChecked();

                        dialogInterface.dismiss();
                    }
                })
                .create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view_setting);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode) {
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.login_lyrl_dialog);
    }

    /**
     * ???????????????????????????
     */
    private void checkUserName() {
        String userName = at_userName.getText().toString();
        String url = "http://" + LoginConstant.SERVER_URL + LoginConstant.CHECK_USERNAME_URL + userName;
        mLoginService.checkUserName(url, new Callback2<Boolean>() {
            @Override
            public void onSuccess(final Boolean aBoolean) {
                mIsUserNameExists = aBoolean;
                if (!aBoolean) {
                    til_login_name.setError("??????????????????");
                }

            }

            @Override
            public void onFail(Exception error) {
            }
        });
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @return ????????????????????????
     */
    private boolean verifyInput() {
        String userName = at_userName.getText().toString();
        String password = et_pwd.getText().toString();
        if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(password)) {
            til_login_name.setError("??????????????????");
            til_login_password.setError("???????????????");
            at_userName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userName)) {
            til_login_name.setError("??????????????????");
            at_userName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            til_login_password.setError("???????????????");
            et_pwd.requestFocus();
            return false;
        }
        return mIsUserNameExists;
    }

    public void updateNetData() {
        if (mIsUpdateProject) {
            final ProgressDialog progressDialog = ProgressDialog.show(this, "??????", "????????????????????????!");
            final TableDataManager tableDataManager = new TableDataManager(this);
            String serverUrl = BaseInfoManager.getBaseServerUrl(this);
            String url = serverUrl + "rest/agdic/allDics";

            tableDataManager.getDictionaryByNet(url, new TableNetCallback() {
                @Override
                public void onSuccess(Object data) {
                    tableDataManager.setDictionaryToDB((List<DictionaryItem>) data);
                    progressDialog.dismiss();
                    jumpToMain();
                }

                @Override
                public void onError(String msg) {
                    ToastUtil.longToast(LoginActivity.this, msg);
                }
            });
        } else {
            jumpToMain();
        }
    }

    /**
     * ????????????Activity
     */
    private void jumpToMain() {
        init();
        startActivity(new Intent(this, PatrolMainActivity.class));
        //startActivity(new Intent(this,TestBaiduLocationWithArcgisActivity.class));
        finish();
    }

    /**
     * ??????????????????????????????
     */
    private void init(){
        LayerFactoryProvider.injectLayerFactory(new PatrolLayerFactory());
        //????????????Service
        LayerServiceFactory.injectLayerService(new AgwebPatrolLayerService(this));
        ProjectServiceFactory.injectProjectService(new AgwebProjectService());
        PatrolSearchServiceProvider.injectSearchService(new PatrolSearchServiceImpl2(this));
    }

    /**
     * ??????????????????
     */
    private void updateMenuConfigure(final ILoginCallback callback) {
        if (mActionNetLogic == null) {
            mActionNetLogic = new ActionNetLogic(LoginActivity.this, "http://" + LoginConstant.SERVER_URL + "/");
        }
        mActionNetLogic.getAllfeatures().map(new Func1<List<ActionModel>, List<ActionModel>>() {

            @Override
            public List<ActionModel> call(List<ActionModel> actionModels) {
                for (ActionModel model : actionModels) {
                    model.setUserId(BaseInfoManager.getUserId(LoginActivity.this));
                }
                return actionModels;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ActionModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError("??????????????????????????????");
                    }

                    @Override
                    public void onNext(List<ActionModel> actionModels) {
                        mLocalMenuStorageDao.setTableItemsToDB(actionModels, BaseInfoManager.getUserId(LoginActivity.this));
                        callback.onSuccess("??????????????????????????????");
                    }
                });
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private void deleteProjectFolder() {
        LayerServiceFactory.provideLayerService(this).deleteProjectFolder();//??????/project ?????????
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boardHelper.onDestory();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        AGMobileSDK.destroy();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
