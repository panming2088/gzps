package com.augurit.agmobile.gzps.common.editmap;

import android.content.Context;
import android.content.Intent;

import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view.NoMapSelectLocationEditStateTableItemView;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.common.editmap
 * @createTime 创建时间 ：17/11/6
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/6
 * @modifyMemo 修改备注：
 */

public class NoMapSelectLocationEditStateTableItemView2 extends NoMapSelectLocationEditStateTableItemView {

    public NoMapSelectLocationEditStateTableItemView2(Context context) {
        super(context);
    }

    @Override
    protected void startActivity() {
        Intent intent = new Intent(mContext, BottomsheetEditMapFeatureActivity.class);
        if (mPresenter != null) {
            mPresenter.completeIntent(intent);
        }
        mContext.startActivity(intent);
    }
}
