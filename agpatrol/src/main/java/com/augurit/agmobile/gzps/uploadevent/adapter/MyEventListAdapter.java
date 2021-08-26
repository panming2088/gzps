package com.augurit.agmobile.gzps.uploadevent.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.constant.GzpsConstant;
import com.augurit.agmobile.gzps.common.util.SpannableStringHelper;
import com.augurit.agmobile.gzps.uploadevent.model.EventListItem;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.fw.cache.img.amimageloader.AMImageLoader;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : taoerxiang
 * @data : 2017-11-11  12:04
 * @des :
 */

public class MyEventListAdapter extends RecyclerView.Adapter<MyEventListAdapter.MyViewHolder> {

    private int eventPState;
    private Context context;
    private List<EventListItem> listData = new ArrayList<>();
    private OnRecyclerItemClickListener<EventListItem> listener;

    public MyEventListAdapter(List<EventListItem> list, int eventPState, Context context) {
        this.listData = list;
        this.eventPState = eventPState;
        this.context = context;
    }

    public List<EventListItem> getListData() {
        return listData;
    }

    public void setListData(List<EventListItem> listData) {
        this.listData = listData;
    }

    public void refresh(List<EventListItem> list){
        this.listData = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<EventListItem> list){
        try {
            if(listData == null){
                listData = new ArrayList<>();
            }
            this.listData.addAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    @Override
    public MyEventListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.task_list_item, null);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final EventListItem eventListItem = listData.get(position);
        String componentType = getSpinnerValueByCode(eventListItem.getComponentType());
        String eventType = getSpinnerValuesByMultiCode(eventListItem.getEventType());
        if(!StringUtil.isEmpty(eventListItem.getImgPath())){
            AMImageLoader.loadStringRes(holder.task_list_item_iv, eventListItem.getImgPath());
        } else {
            AMImageLoader.loadResId(holder.task_list_item_iv, R.drawable.bg_loading);
        }
        String text = eventListItem.getActivityChineseName();
        if(!StringUtil.isEmpty(text)){
            int start = text.indexOf("(");
            int end = text.lastIndexOf(")");
            if(start != -1 && end != -1){
                holder.link.setText(SpannableStringHelper.builderString(text,start+1,end,eventListItem.getColour()));
            }else{
                holder.link.setText(text);
            }
        }else{
            holder.link.setText("");
        }
        holder.title_tv.setText(componentType + "  " + eventType);
        holder.person_tv.setText(eventListItem.getReportUser());
        holder.date_tv.setText(TimeUtil.getStringTimeYMDS(new Date(eventListItem.getReportTime())));
        holder.addr_tv.setText(eventListItem.getAddr());
        if(eventPState == GzpsConstant.EVENT_P_STATE_HANDLING
                && GzpsConstant.EVENT_OPEN.equals(eventListItem.getState())){
            holder.state_tv.setVisibility(View.VISIBLE);
            holder.state_tv.setText("待处理");
        } else {
            holder.state_tv.setVisibility(View.GONE);
            holder.state_tv.setText("");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position, eventListItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListUtil.isEmpty(listData) ? 0 : listData.size();
    }

    public String getSpinnerValueByCode(String code) {
        List<DictionaryItem> dictionaryItems = new TableDBService(context).getDictionaryByCode(code);
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView task_list_item_iv;
        TextView title_tv;
        TextView link;
        TextView person_tv;
        TextView date_tv;
        TextView addr_tv;
        TextView state_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            task_list_item_iv = (ImageView) itemView.findViewById(R.id.task_list_item_iv);
            link = (TextView) itemView.findViewById(R.id.task_item_link);
            title_tv = (TextView) itemView.findViewById(R.id.task_item_title);
            person_tv = (TextView) itemView.findViewById(R.id.task_item_person);
            date_tv = (TextView) itemView.findViewById(R.id.task_item_date);
            addr_tv = (TextView) itemView.findViewById(R.id.task_item_addr);
            state_tv = (TextView) itemView.findViewById(R.id.task_item_status);

        }
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener<EventListItem> listener) {
        this.listener = listener;
    }

}
