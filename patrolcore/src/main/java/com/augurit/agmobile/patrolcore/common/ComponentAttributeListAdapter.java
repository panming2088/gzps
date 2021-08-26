package com.augurit.agmobile.patrolcore.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


import com.augurit.agmobile.mapengine.common.widget.callout.SimpleListAdapter;
import com.augurit.agmobile.patrolcore.R;

import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.common.selectcomponent
 * @createTime 创建时间 ：17/11/3
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/3
 * @modifyMemo 修改备注：
 */

public class ComponentAttributeListAdapter extends SimpleListAdapter {

    public static final int TYPE_WHITE = 1; //白色
    public static final int TYPE_GREY = 2;  //灰色

    public ComponentAttributeListAdapter(Context context, Map<String, Object> attributes) {
        super(context, attributes);
    }

    public ComponentAttributeListAdapter(Context context, Map<String, Object> attributes, String hightText) {
        super(context, attributes, hightText);
    }

    @Override
    public SimpleListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_GREY:
                return new SimpleListAdapter.ListViewHolder(View.inflate(mContext, R.layout.item_listcallout_bg_grey, null));
            case TYPE_WHITE:
                return new SimpleListAdapter.ListViewHolder(View.inflate(mContext, R.layout.item_listcallout_bg_white, null));
            default:
                return new SimpleListAdapter.ListViewHolder(View.inflate(mContext, R.layout.item_listcallout_bg_white, null));
        }
       // return new SimpleListAdapter.ListViewHolder(View.inflate(mContext, R.layout.item_listcallout_bg_white, null));
    }

    @Override
    public int getItemViewType(int position) {
        if (position %2  == 0){ //偶数，灰色
            return TYPE_GREY;
        }
        return TYPE_WHITE;
    }

}
