package com.augurit.agmobile.gzps.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.setting
 * @createTime 创建时间 ：2017-03-20
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-20
 * @modifyMemo 修改备注：
 */

public class SettingAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder,String> {

    public SettingAdapter(List<String> mDataList) {
        super(mDataList);
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new MineViewHolder(inflater.inflate(R.layout.mine_item,null)) ;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, String data) {

        if (holder instanceof MineViewHolder){
            MineViewHolder mineViewHolder = (MineViewHolder) holder;
            mineViewHolder.tv_name.setText(data);
        }
    }

    public class MineViewHolder extends BaseRecyclerViewHolder{

        private TextView tv_name;

        public MineViewHolder(View itemView) {
            super(itemView);
            tv_name = findView(R.id.mine_item_tv_name);
        }
    }

}
