package com.augurit.agmobile.gzps.uploadfacility.view.feedback;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.journal.model.JournalAttachment;
import com.augurit.agmobile.gzps.uploadfacility.model.FeedbackInfo;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.cache.img.amimageloader.AMImageLoader;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.feedback
 * @createTime 创建时间 ：2018-03-08
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2018-03-08
 * @modifyMemo 修改备注：
 */

public class FacilityFeedbackAdapter  extends RecyclerView.Adapter<FacilityFeedbackAdapter.FeedbackHolder> {

    private Context mContext;

    private List<FeedbackInfo> feedbackInfoList;

    private OnRecyclerItemClickListener<FeedbackInfo> onRecyclerItemClickListener;

    public FacilityFeedbackAdapter(Context context){
        this.mContext = context;
    }

    public void notifyDataSetChanged(List<FeedbackInfo> feedbackInfoList){
        this.feedbackInfoList = feedbackInfoList;
        notifyDataSetChanged();
    }

    @Override
    public FeedbackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedbackHolder(LayoutInflater.from(mContext).inflate(R.layout.feedback_list_item, null));
    }

    @Override
    public void onBindViewHolder(FeedbackHolder holder, final int position) {
        final FeedbackInfo feedbackInfo = feedbackInfoList.get(position);

        if(!ListUtil.isEmpty(feedbackInfo.getSysList())){
            String imgPath = feedbackInfo.getSysList().get(0).getThumPath();
            if(!StringUtil.isEmpty(imgPath)){
                AMImageLoader.loadStringRes(holder.iv, imgPath);
            } else {
                AMImageLoader.loadResId(holder.iv, R.mipmap.patrol_ic_empty);
            }
        }

        /**
         * 判断整改情况显示不同颜色文字
         */
        if("正在整改".equals(feedbackInfo.getSituation())){
            holder.title.setTextColor(mContext.getResources().getColor(R.color.orange));
        }else if("已完成整改".equals(feedbackInfo.getSituation())){
            holder.title.setTextColor(mContext.getResources().getColor(R.color.green));
        }else if("无需整改".equals(feedbackInfo.getSituation())){
            holder.title.setTextColor(mContext.getResources().getColor(R.color.blue));
        }
        holder.title.setText(feedbackInfo.getSituation());


        holder.describe.setText(feedbackInfo.getDescribe());
        holder.person.setText(feedbackInfo.getFeedbackPerson());
        holder.time.setText(TimeUtil.getStringTimeYMDMChines(new Date(feedbackInfo.getTime())));

        /**
         * 反馈列表点击
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRecyclerItemClickListener != null){
                    onRecyclerItemClickListener.onItemClick(v, position, feedbackInfo);
                }
                //点击item携带图片返回到FacilityFeedbackActivity
                if (!ListUtil.isEmpty(feedbackInfo.getSysList())) {
                    ArrayList<Photo> photos = new ArrayList<>();
                    for(JournalAttachment attachment : feedbackInfo.getSysList()){
                        Photo photo = new Photo();
                        photo.setId(Long.valueOf(attachment.getId()));
                        photo.setThumbPath(attachment.getThumPath());
                        photo.setPhotoPath(attachment.getAttPath());
                        photo.setPhotoName(attachment.getAttName());
                        photos.add(photo);
                    }
                    Intent intent = new Intent(mContext, FacilityFeedbackActivity.class);
                    //传递实体类
                    intent.putExtra("BITMAPLIST", (ArrayList<Photo>) photos);
                    intent.putExtra("feedbackInfo", feedbackInfo);
                    intent.putExtra("isadapter", 1);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, v, "shareTransition").toBundle());
//                    } else {
//                        mContext.startActivity(intent);
//                    }
                    mContext.startActivity(intent);

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if(ListUtil.isEmpty(feedbackInfoList)){
            return 0;
        }
        return feedbackInfoList.size();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<FeedbackInfo> onRecyclerItemClickListener){
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    class FeedbackHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView title;
        TextView describe;
        TextView person;
        TextView time;

        public FeedbackHolder(View itemView){
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.feedback_list_item_iv);
            title = (TextView) itemView.findViewById(R.id.feedback_item_title);
            describe = (TextView) itemView.findViewById(R.id.feedback_item_describe);
            person = (TextView) itemView.findViewById(R.id.feedback_item_person);
            time = (TextView) itemView.findViewById(R.id.feedback_item_date);
        }
    }
}
