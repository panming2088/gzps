package com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view;

import android.content.Context;
import android.content.Intent;

import com.augurit.agmobile.patrolcore.editmap.EditMapFeatureActivity;
import com.augurit.agmobile.patrolcore.editmap.util.EditMapConstant;

/**
 *
 * 只读模式下的地图选带你控件
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view.tableitem
 * @createTime 创建时间 ：17/11/2
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/2
 * @modifyMemo 修改备注：
 */

public class SelectLocationReadStateTableItemView extends SelectLocationEditStateTableItemView {


    public SelectLocationReadStateTableItemView(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();
        //设置成不可编辑
        et_shortcut_detail_address.setEnabled(false);
        hideSelectLocationButton();
    }

    @Override
    protected void startActivity() {
        Intent intent = new Intent(mContext, EditMapFeatureActivity.class);
        intent.putExtra(EditMapConstant.BundleKey.IF_READ_ONLY,true);
        if (mPresenter != null) {
            mPresenter.completeIntent(intent);
        }
        mContext.startActivity(intent);
    }
}
