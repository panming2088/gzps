package com.augurit.am.fw.utils.actres;

import android.content.Intent;

/**
 * Created by liangsh on 2016-11-09.
 */
public interface CallbackManager {

    /**
     * The method that should be called from the Activity's or Fragment's onActivityResult method.
     * @param requestCode The request code that's received by the Activity or Fragment.
     * @param resultCode  The result code that's received by the Activity or Fragment.
     * @param data        The result data that's received by the Activity or Fragment.
     * @return true If the result could be handled.
     */
    boolean onActivityResult(int requestCode, int resultCode, Intent data);


    /**
     * The factory class for the {@link com.augurit.am.fw.utils.actres.CallbackManager}.
     */
    class Factory {
        /**
         * Creates an instance of {@link com.augurit.am.fw.utils.actres.CallbackManager}.
         * @return an instance of {@link com.augurit.am.fw.utils.actres.CallbackManager}.
         */
        public static CallbackManager create() {
            return new CallbackManagerImpl();
        }
    }
}
