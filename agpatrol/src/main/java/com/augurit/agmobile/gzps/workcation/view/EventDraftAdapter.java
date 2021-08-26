package com.augurit.agmobile.gzps.workcation.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadevent.model.ProblemUploadBean;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.cache.img.amimageloader.AMImageLoader;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by liangsh on 2017/11/21.
 */

public class EventDraftAdapter  extends RecyclerView.Adapter<EventDraftAdapter.EventDraftHolder>{

    private Context mContext;

    private List<ProblemUploadBean> eventList;

    private OnRecyclerItemClickListener<ProblemUploadBean> onRecyclerItemClickListener;

    public EventDraftAdapter(Context context){
        this.mContext = context;
    }

    public void notifyDataSetChanged(List<ProblemUploadBean> eventList){
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    @Override
    public EventDraftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventDraftHolder(LayoutInflater.from(mContext).inflate(R.layout.event_draft_item, null));
    }

    @Override
    public void onBindViewHolder(EventDraftHolder holder, final int position) {
        final ProblemUploadBean problemUploadBean = eventList.get(position);
        List<Photo> photos = AMDatabase.getInstance().getQueryByWhere(Photo.class, "problem_id", problemUploadBean.getDbid() + "");
        String componentType = getSpinnerValueByCode(problemUploadBean.getSSLX());
        String eventType = getSpinnerValuesByMultiCode(problemUploadBean.getBHLX());
        if(!ListUtil.isEmpty(photos)){
            String filePath = photos.get(0).getLocalPath();
            String thumbPath = photos.get(0).getThumbPath();
            String photoPath = photos.get(0).getPhotoPath();
            if(!StringUtil.isEmpty(filePath)){
                AMImageLoader.loadStringRes(holder.task_list_item_iv, filePath);
            }else if(!StringUtil.isEmpty(thumbPath)){
                AMImageLoader.loadStringRes(holder.task_list_item_iv, thumbPath);
            }else if(!StringUtil.isEmpty(photoPath)){
                AMImageLoader.loadStringRes(holder.task_list_item_iv, photoPath);
            }

        } else {
            AMImageLoader.loadResId(holder.task_list_item_iv, R.drawable.bg_loading);
        }
        holder.title_tv.setText(componentType + " " + eventType);
        holder.addr_tv.setText(problemUploadBean.getSZWZ());
        holder.date_tv.setText(TimeUtil.getStringTimeYMDS(new Date(problemUploadBean.getTime())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRecyclerItemClickListener != null){
                    onRecyclerItemClickListener.onItemClick(v, position, problemUploadBean);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onRecyclerItemClickListener != null){
                    onRecyclerItemClickListener.onItemLongClick(v, position, problemUploadBean);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(ListUtil.isEmpty(eventList)){
            return 0;
        }
        return eventList.size();
    }

    public String getSpinnerValueByCode(String code) {
        List<DictionaryItem> dictionaryItems = new TableDBService(mContext).getDictionaryByCode(code);
        if (ListUtil.isEmpty(dictionaryItems)) {
            return "";
        }
        DictionaryItem dictionaryItem = dictionaryItems.get(0);
        return dictionaryItem.getName();
    }

    /**
     * 获取多个数据字典值，编码以英文逗号分隔开
     * @param codes
     * @return
     */
    private String getSpinnerValuesByMultiCode(String codes){
        if(StringUtil.isEmpty(codes)){
            return "";
        }
        String[] codeArray = codes.split(",");
        String value = "";
        for(String code : codeArray){
            value = value + "，" + getSpinnerValueByCode(code);
        }
        value = value.substring(1);
        return value;
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener<ProblemUploadBean> onRecyclerItemClickListener){
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    class EventDraftHolder extends RecyclerView.ViewHolder{

        ImageView task_list_item_iv;
        TextView title_tv;
        TextView link;
        TextView date_tv;
        TextView addr_tv;

        public EventDraftHolder(View itemView){
            super(itemView);
            task_list_item_iv = (ImageView) itemView.findViewById(R.id.task_list_item_iv);
            link = (TextView) itemView.findViewById(R.id.task_item_link);
            link.setVisibility(View.GONE);
            title_tv = (TextView) itemView.findViewById(R.id.task_item_title);
            date_tv = (TextView) itemView.findViewById(R.id.task_item_date);
            addr_tv = (TextView) itemView.findViewById(R.id.task_item_addr);
        }
    }
}
