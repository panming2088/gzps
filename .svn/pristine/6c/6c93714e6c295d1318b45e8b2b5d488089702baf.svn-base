package com.augurit.agmobile.gzps.maintain.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.util.SetCheckStateUtil;
import com.augurit.agmobile.gzps.maintain.model.ConserveDetail;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.search.view.GlideRoundTransform;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

/**
 * 设施新增列表Adapter
 *
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search.view
 * @createTime 创建时间 ：2017-03-20
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-20
 * @modifyMemo 修改备注：
 */
public class MaintainListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, ConserveDetail> {

    private Context mContext;

    public MaintainListAdapter(Context context, List<ConserveDetail> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<ConserveDetail> searchResults) {
        mDataList.addAll(searchResults);
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ModifiedIdentificationViewHolder(inflater.inflate(R.layout.item_modified_identification, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, ConserveDetail data) {
        if (holder instanceof ModifiedIdentificationViewHolder) {
            ModifiedIdentificationViewHolder viewHolder = (ModifiedIdentificationViewHolder) holder;


            String path = data.getImgPath();
            if (ListUtil.isEmpty(path)) {
                viewHolder.iv.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.mipmap.patrol_ic_empty));
            } else {
                //使用Glide进行加载图片
                Glide.with(mContext)
                        .load(path)
                        .error(R.mipmap.patrol_ic_load_fail)
                        .transform(new GlideRoundTransform(mContext))
                        .into(viewHolder.iv);
            }

            if (!TextUtils.isEmpty(data.getAddr())) {
                viewHolder.tv_left_down.setText(data.getAddr());
            } else {
                viewHolder.tv_left_down.setText("");
            }


//            if (data.getCheckState() != null) {
//                if (data.getCheckState().equals("1")) {
//                    viewHolder.tv_right_up.setText("未审核");
//                    viewHolder.tv_right_up.setTextColor(Color.RED);
//                } else if (data.getCheckState().equals("2")) {
//                    viewHolder.tv_right_up.setText("审核通过");
//                    viewHolder.tv_right_up.setTextColor(Color.parseColor("#3EA500"));
//                } else if (data.getCheckState().equals("3")) {
//                    viewHolder.tv_right_up.setText("存在疑问");
//                    viewHolder.tv_right_up.setTextColor(Color.parseColor("#FFFFC248"));
//                }
//            } else {
//                viewHolder.tv_right_up.setText("");
//            }
            SetCheckStateUtil.setCheckState(data.getCheckState(), viewHolder.tv_right_up);

//            if (data.getUpdateTime() != null && data.getUpdateTime() > 0){
//                viewHolder.tv_right_down.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getUpdateTime())));
//            }else
            if (data.getMarkTime() != null) {
                viewHolder.tv_right_down.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getMarkTime())));
            }

            if (data.getMarkPerson() != null) {
                viewHolder.tv_mark_id.setText("上报人：" + data.getMarkPerson());
            }

            viewHolder.tv_left_up.setText(data.getWtms());
        }
    }


    public static class ModifiedIdentificationViewHolder extends BaseRecyclerViewHolder {

        ImageView iv;
        TextView tv_left_up;
        TextView tv_left_down;
        TextView tv_right_up;
        TextView tv_right_down;
        TextView tv_mark_id;

        public ModifiedIdentificationViewHolder(View itemView) {
            super(itemView);
            iv = findView(R.id.search_result_item_iv);
            tv_left_up = (TextView) itemView.findViewById(R.id.tv_left_up);
            tv_left_down = (TextView) itemView.findViewById(R.id.tv_left_down);
            tv_right_up = (TextView) itemView.findViewById(R.id.tv_right_up);
            tv_right_down = (TextView) itemView.findViewById(R.id.tv_right_down);
            tv_mark_id = (TextView) itemView.findViewById(R.id.tv_mark_id);
        }
    }
}
