package com.augurit.am.cmpt.widget.fingerprint;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.log.LogUtil;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * 描述：指纹验证
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.fingerprint
 * @createTime 创建时间 ：2017-04-19
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-04-19
 * @modifyMemo 修改备注：
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintUtil {

    private static final String KEY_NAME = "my_key";
    private Dialog mDialog;
    private FingerprintManager mManager;
    private CancellationSignal mCancellationSignal;
    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintCallback mCallback;
    private TextView tv_hint;
    private boolean isPermitted = true;
    private boolean isHardware = true;
    private boolean isEnrolled = true;

    public FingerprintUtil(Context context) {
        mManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            isPermitted = false;
        } else {
            if (!mManager.isHardwareDetected()) {
                isHardware = false;
                isEnrolled = false;
            } else if (!mManager.hasEnrolledFingerprints()) {
                isEnrolled = false;
            }
        }
        if (isPermitted && isHardware && isEnrolled) {
            initCrypto();
        }
    }

    /**
     * 初始化对话框
     */
    public void initDialog(final Context context) {
        String msg = "请验证指纹";
        View view = View.inflate(context, R.layout.fingerprint_dialog, null);
        tv_hint = (TextView) view.findViewById(R.id.tv_hint);
        Button btn_positive = (Button) view.findViewById(R.id.btn_positive);
        Button btn_negative = (Button) view.findViewById(R.id.btn_negative);
        btn_positive.setVisibility(View.GONE);

        mDialog = new Dialog(context);
        if (!isPermitted) {
            msg = "请允许应用的指纹权限";
        } else {
            if (!isHardware) {
                msg = "设备硬件不支持指纹识别";
            } else if (!isEnrolled) {
                msg = "没有录入指纹，请到系统设置中录入";
                btn_positive.setVisibility(View.VISIBLE);
                btn_positive.setText("去设置");
                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        context.startActivity(intent);
                        mDialog.dismiss();
                        if (mCallback != null) {
                            mCallback.onFail();
                        }
                    }
                });
            }
        }
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (mCallback != null) {
                    mCallback.onFail();
                }
            }
        });
        tv_hint.setText(msg);

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mCancellationSignal != null) {
                    mCancellationSignal.cancel();
                }
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mCallback != null) {
                    mCallback.onFail();
                }
            }
        });
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    if (mCallback != null) {
                        mCallback.onFail();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 弹出指纹验证Dialog，开始指纹验证
     */
    @SuppressWarnings("MissingPermission")
    public void showDialog() {
        showDialog("请验证指纹");
    }

    /**
     * 弹出指纹验证Dialog，开始指纹验证
     * @param defaultMsg 对话框默认消息文字
     */
    @SuppressWarnings("MissingPermission")
    public void showDialog(String defaultMsg) {
        if (mDialog != null) {
            mDialog.show();
            Window window = mDialog.getWindow();
            window.setBackgroundDrawableResource(R.drawable.common_lyrl_dialog);
            if (startAutentication()) {
                tv_hint.setText(defaultMsg);
            }
        }
    }

    /**
     * 隐藏Dialog，同时取消指纹验证
     */
    public void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    /**
     * 开始指纹验证（无界面）
     */
    @SuppressWarnings("MissingPermission")
    public boolean startAutentication() {
        if (isPermitted && isHardware && isEnrolled) {
            mCancellationSignal = new CancellationSignal();
            mManager.authenticate(mCryptoObject, mCancellationSignal, 0, mAuthenticationCallback, null);
            LogUtil.d("Fingerprint", "Authenticate Start");
            return true;
        }
        return false;
    }

    /**
     * 取消指纹验证（无界面）
     */
    public void cancelAutentication() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
        }
    }

    /**
     * 验证监听
     */
    private FingerprintManager.AuthenticationCallback mAuthenticationCallback = new FingerprintManager.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            LogUtil.d("Fingerprint", "errorCode:" + errorCode + ",errString:" + errString);
            if (tv_hint != null) {
                tv_hint.setText(errString);
            }
            if (mCallback != null) {
                mCallback.onMessage((String) errString);
            }
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            LogUtil.d("Fingerprint", "helpCode:" + helpCode + ",helpString:" + helpString);
            if (tv_hint != null) {
                tv_hint.setText(helpString);
            }
            if (mCallback != null) {
                mCallback.onMessage((String) helpString);
            }
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            LogUtil.d("Fingerprint", "Authentication Succeeded");
            if (tv_hint != null) {
                tv_hint.setText("验证成功");
            }
            if (mCallback != null) {
                mCallback.onSuccess();
            }
        }

        @Override
        public void onAuthenticationFailed() {
            LogUtil.d("FingerPrint", "Authentication Failed");
            if (mDialog != null) {
                tv_hint.setText("验证失败，请重试");
            }
            if (mCallback != null) {
                mCallback.onMessage("验证失败，请重试");
            }
        }
    };

    /**
     * 初始化加密对象
     */
    private void initCrypto() {
        KeyStore keyStore = null;
        KeyGenerator keyGenerator = null;
        Cipher cipher = null;
        // 初始化Key
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchProviderException | KeyStoreException |
                 NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 CertificateException | IOException e) {
            e.printStackTrace();
            LogUtil.d("Fingerprint", "Failed to generate Key");
        }
        // 初始化加密对象
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            mCryptoObject = new FingerprintManager.CryptoObject(cipher);
        } catch (NoSuchPaddingException | KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException | NullPointerException e) {
            e.printStackTrace();
            LogUtil.d("Fingerprint", "Failed to generate Key");
        }
    }

    /**
     * 设置指纹验证回调
     * @param callback
     */
    public void setCallback(FingerprintCallback callback) {
        this.mCallback = callback;
    }

    /**
     * 是否拥有指纹验证权限
     */
    public boolean isPermissionGranted() {
        return isPermitted;
    }

    /**
     * 硬件是否支持
     */
    public boolean isHardwareDetected() {
        return isPermitted && isHardware;
    }

    /**
     * 是否录制指纹
     */
    public boolean isEnrolled() {
        return isPermitted && isHardware && isEnrolled;
    }

    public interface FingerprintCallback {
        void onSuccess();
        void onMessage(String message);
        void onFail();
    }
}
