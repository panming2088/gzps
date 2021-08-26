package com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.util.SetCheckStateUtil;
import com.augurit.agmobile.gzps.uploadfacility.model.HookBean;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;

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
public class MyHookListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, HookBean> {

    private Context mContext;

    public MyHookListAdapter(Context context, List<HookBean> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<HookBean> searchResults) {
        mDataList.addAll(searchResults);
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ModifiedIdentificationViewHolder(inflater.inflate(R.layout.item_hook_identification, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, HookBean data) {
        if (holder instanceof ModifiedIdentificationViewHolder) {
            ModifiedIdentificationViewHolder viewHolder = (ModifiedIdentificationViewHolder) holder;


//            List<Photo> photos = data.getPhotos();
//            if (ListUtil.isEmpty(photos)){
//                viewHolder.iv.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.mipmap.patrol_ic_empty));
//            }else {
//                //使用Glide进行加载图片
//                Glide.with(mContext)
//                        .load(photos.get(0).getThumbPath())
//                        .error(R.mipmap.patrol_ic_load_fail)
//                        .transform(new GlideRoundTransform(mContext))
//                        .into(viewHolder.iv);
//            }
            //关联排水单元类型
            if (!TextUtils.isEmpty(data.getPsdyLx())) {
                viewHolder.tv_left_down.setText("关联排水单元类型：" + data.getPsdyLx());
            } else {
                viewHolder.tv_left_down.setText("");
            }

            //关联排水单元名称
            if (!TextUtils.isEmpty(data.getPsdyName())) {
                viewHolder.tv_hook_name.setText("关联排水单元名称：" + data.getPsdyName());
            } else {
                viewHolder.tv_hook_name.setText("");
            }


            if (data.getSort() != null) {
                if ("污水".equals(data.getSort())) {
                    viewHolder.tv_right_up.setText("污水");
                    viewHolder.tv_right_up.setTextColor(Color.RED);
                } else if ("雨水".equals(data.getSort())) {
                    viewHolder.tv_right_up.setText("雨水");
                    viewHolder.tv_right_up.setTextColor(Color.parseColor("#3EA500"));
                } else if ("雨污合流".equals(data.getSort())) {
                    viewHolder.tv_right_up.setText("雨污合流");
                    viewHolder.tv_right_up.setTextColor(Color.parseColor("#C794D9"));
                }
            } else {
                viewHolder.tv_right_up.setText("");
            }
//            SetCheckStateUtil.setCheckState(data.getCheckState(),viewHolder.tv_right_up);
//            if (data.getUpdateTime() != null && data.getUpdateTime() > 0){
//                viewHolder.tv_right_down.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getUpdateTime())));
//            }else
            //挂接时间
            if (data.getMarkTime() != null) {
                viewHolder.tv_right_down.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getMarkTime())));
            }

//            if("3".equals(data.getLackType())){
//                viewHolder.tv_delete.setVisibility(View.VISIBLE);
//            }else{
//                viewHolder.tv_delete.setVisibility(View.GONE);
//            }
            //设施位置
            if (!StringUtil.isEmpty(data.getJbjAddr())) {
                viewHolder.tv_mark_id.setText("设施位置：" + data.getJbjAddr());
            }
            //窨井类型+井编号
            viewHolder.tv_left_up.setText("接驳井" + "(" + data.getJbjObjectId() + ")");
        }
    }


    public static class ModifiedIdentificationViewHolder extends BaseRecyclerViewHolder {

        ImageView iv;
        TextView tv_hook_name;
        TextView tv_left_up;
        TextView tv_left_down;
        TextView tv_right_up;//没了
        TextView tv_right_down;
        TextView tv_mark_id;
        TextView tv_delete;

        public ModifiedIdentificationViewHolder(View itemView) {
            super(itemView);
            iv = findView(R.id.search_result_item_iv);
            tv_hook_name = (TextView) itemView.findViewById(R.id.tv_hook_name);
            tv_left_up = (TextView) itemView.findViewById(R.id.tv_left_up);
            tv_left_down = (TextView) itemView.findViewById(R.id.tv_left_down);
            tv_right_up = (TextView) itemView.findViewById(R.id.tv_right_up);
            tv_right_down = (TextView) itemView.findViewById(R.id.tv_right_down);
            tv_mark_id = (TextView) itemView.findViewById(R.id.tv_mark_id);
            tv_delete = (TextView) itemView.findViewById(R.id.tv_delete);
        }
    }
}
