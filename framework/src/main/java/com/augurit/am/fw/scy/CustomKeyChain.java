//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.augurit.am.fw.scy;
import android.util.Base64;

import com.facebook.android.crypto.keychain.FixedSecureRandom;
import com.facebook.crypto.CryptoConfig;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.keychain.KeyChain;

import java.security.SecureRandom;
import java.util.Arrays;


public class CustomKeyChain implements KeyChain {

    /**
     * cipherKey和macKey生成方法

     SecureRandom mSecureRandom = new FixedSecureRandom();
     CryptoConfig cryptoConfig = CryptoConfig.KEY_256;
     byte[] cipherKeyBytes = new byte[cryptoConfig.keyLength];
     mSecureRandom.nextBytes(cipherKeyBytes);
     String cipherKey = Base64.encodeToString(cipherKeyBytes, Base64.DEFAULT);

     byte[] macKeyBytes = new byte[NativeMac.KEY_LENGTH];
     mSecureRandom.nextBytes(macKeyBytes);
     String macKey = Base64.encodeToString(macKeyBytes, Base64.DEFAULT);

     *
     */

    private static final String cipherKey = "2Bqm/8JvR84FLzIL3f0jHJXiMIh9oUWsWGifHtfjTb8=";
    private static final String macKey = "TdYShP7tHh+TyIUxxtgvOcNVs0ooy+m+Z3LsHNddwliTzv0wn/5/llQydA4ZHMZgvntw0G7xf7D7moPPf6LE8A==";

    private final CryptoConfig mCryptoConfig;

    private final SecureRandom mSecureRandom;

    protected byte[] mCipherKey;
    protected boolean mSetCipherKey;

    protected byte[] mMacKey;
    protected boolean mSetMacKey;

    public CustomKeyChain() {
        this(CryptoConfig.KEY_256);
    }

    public CustomKeyChain(CryptoConfig config) {
        mSecureRandom = new FixedSecureRandom();
        mCryptoConfig = config;
    }

    @Override
    public synchronized byte[] getCipherKey() throws KeyChainException {
        if (!mSetCipherKey) {
            mCipherKey = decodeFromPrefs(cipherKey);
        }
        mSetCipherKey = true;
        return mCipherKey;
    }

    @Override
    public byte[] getMacKey() throws KeyChainException {
        if (!mSetMacKey) {
            mMacKey = decodeFromPrefs(macKey);
        }
        mSetMacKey = true;
        return mMacKey;
    }

    @Override
    public byte[] getNewIV() throws KeyChainException {
        byte[] iv = new byte[mCryptoConfig.ivLength];
        mSecureRandom.nextBytes(iv);
        return iv;
    }

    @Override
    public synchronized void destroyKeys() {
        mSetCipherKey = false;
        mSetMacKey = false;
        if (mCipherKey != null) {
            Arrays.fill(mCipherKey, (byte) 0);
        }
        if (mMacKey != null) {
            Arrays.fill(mMacKey, (byte) 0);
        }
        mCipherKey = null;
        mMacKey = null;
    }

    /**
     * Visible for testing.
     */
  /* package */ byte[] decodeFromPrefs(String keyString) {
        if (keyString == null) {
            return null;
        }
        return Base64.decode(keyString, Base64.DEFAULT);
    }

    /**
     * Visible for testing.
     */
  /* package */ String encodeForPrefs(byte[] key) {
        if (key == null ) {
            return null;
        }
        return Base64.encodeToString(key, Base64.DEFAULT);
    }
}
