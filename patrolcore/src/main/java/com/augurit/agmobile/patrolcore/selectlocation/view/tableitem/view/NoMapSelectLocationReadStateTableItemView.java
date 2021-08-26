package com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view;

import android.content.Context;
import android.content.Intent;

import com.augurit.agmobile.patrolcore.editmap.EditMapFeatureActivity;
import com.augurit.agmobile.patrolcore.editmap.util.EditMapConstant;

import java.util.List;

/**
 * 不带地图的选点控件(阅读模式)
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view
 * @createTime 创建时间 ：17/11/2
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/2
 * @modifyMemo 修改备注：
 */

public class NoMapSelectLocationReadStateTableItemView extends NoMapSelectLocationEditStateTableItemView {

    public NoMapSelectLocationReadStateTableItemView(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();
        et_address.setEnabled(false);
        tv_select_or_check_location.setText("在地图上查看位置");
    }


    @Override
    public void setAddress(List<String> spinnerList) {
        et_address.setText(spinnerList.get(0));
    }

    @Override
    protected void startActivity() {
        Intent intent = new Intent(mContext, EditMapFeatureActivity.class);
        intent.putExtra(EditMapConstant.BundleKey.IF_READ_ONLY, true);
        if (mPresenter != null) {
            mPresenter.completeIntent(intent);
        }
        mContext.startActivity(intent);
    }
}
