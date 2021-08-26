package com.augurit.am.cmpt.loc.cnst;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Yahya Bayramoglu on 11/02/16.
 */
public final class ProviderType {
    public static final int NONE = 0;
    //  public static final int GOOGLE_PLAY_SERVICES = 1;
    public static final int GPS = 2;
    public static final int NETWORK = 3;
    /**
     * Covers both GPS and NETWORK
     */
    public static final int DEFAULT_PROVIDERS = 4;

    private ProviderType() {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, GPS, NETWORK, DEFAULT_PROVIDERS})
    public @interface Source {
    }

}
