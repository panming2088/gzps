package com.augurit.agmobile.gzps.journal;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.journal.model.Journal;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.search.view.GlideRoundTransform;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;


/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.gzps.journal
 * @createTime 创建时间 ：17/11/4
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/4
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public class JournalsAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, Journal> {

    private Context mContext;

    public JournalsAdapter(Context context, List<Journal> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<Journal> searchResults) {
        mDataList.addAll(searchResults);
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new JournalViewHolder(inflater.inflate(R.layout.item_journal, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, final Journal data) {
        if (holder instanceof JournalViewHolder) {
            JournalViewHolder viewHolder = (JournalViewHolder) holder;

            if (ListUtil.isEmpty(data.getAttachments()) || TextUtils.isEmpty(data.getAttachments().get(0).getPhotoPath())){
                viewHolder.iv_iamge.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.mipmap.patrol_ic_empty));
            }else {
                //使用Glide进行加载图片
                Glide.with(mContext)
                        .load(data.getAttachments().get(0).getPhotoPath())
                        .error(R.mipmap.patrol_ic_load_fail)
                        .transform(new GlideRoundTransform(mContext))
                        .into(viewHolder.iv_iamge);
            }

            if (!TextUtils.isEmpty(data.getAddr())) {
                viewHolder.tv_left_down.setText(data.getAddr());
            } else {
                viewHolder.tv_left_down.setText("");
            }


            if (!TextUtils.isEmpty(data.getWriterName())) {
                viewHolder.tv_right_up.setText(data.getWriterName());
            }

            if (data.getDescription() != null) {
                viewHolder.tv_left_up.setText(data.getDescription());
            }

            if(data.getRecordTime() != 0){
                try {
                    viewHolder.tv_right_down.setText(TimeUtil.getStringTimeYMDMChines(new Date(data.getRecordTime())));
                } catch (Exception e) {

                }
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, ReadOnlyJounalActivity2.class);
                    intent.putExtra("journal", data);
                    mContext.startActivity(intent);

                }
            });
        }
    }


    public static class JournalViewHolder extends BaseRecyclerViewHolder {

        TextView tv_left_up;
        TextView tv_left_down;
        TextView tv_right_up;
        TextView tv_right_down;
        ImageView iv_iamge;


        public JournalViewHolder(View itemView) {
            super(itemView);
            iv_iamge = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_left_up = (TextView) itemView.findViewById(R.id.tv_left_up);
            tv_left_down = (TextView) itemView.findViewById(R.id.tv_left_down);
            tv_right_up = (TextView) itemView.findViewById(R.id.tv_right_up);
            tv_right_down = (TextView) itemView.findViewById(R.id.tv_right_down);
        }
    }
}
