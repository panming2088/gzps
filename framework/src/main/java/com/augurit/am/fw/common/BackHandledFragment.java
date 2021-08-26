package com.augurit.am.fw.common;

import android.support.v4.app.Fragment;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common
 * @createTime 创建时间 ：2017-03-20
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-20
 * @modifyMemo 修改备注：
 */

public abstract class BackHandledFragment extends Fragment implements FragmentBackHandler {
    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }
}