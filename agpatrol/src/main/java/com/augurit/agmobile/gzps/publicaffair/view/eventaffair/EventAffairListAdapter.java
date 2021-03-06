package com.augurit.agmobile.gzps.publicaffair.view.eventaffair;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.constant.GzpsConstant;
import com.augurit.agmobile.gzps.common.util.SpannableStringHelper;
import com.augurit.agmobile.gzps.publicaffair.model.EventAffair;
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

public class EventAffairListAdapter extends RecyclerView.Adapter<EventAffairListAdapter.MyViewHolder> {

    private Context context;
    private List<EventAffair.EventAffairBean> listData = new ArrayList<>();
    private OnRecyclerItemClickListener<EventAffair.EventAffairBean> listener;
    private boolean showCode = false;

    public void setShowCode(boolean showCode) {
        this.showCode = showCode;
    }

    public EventAffairListAdapter(List<EventAffair.EventAffairBean> list, Context context) {
        this.listData = list;
        this.context = context;
    }

    public void refresh(List<EventAffair.EventAffairBean> list){
        this.listData = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<EventAffair.EventAffairBean> list){
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
    public EventAffairListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.task_list_item, null);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final EventAffair.EventAffairBean eventList = listData.get(position);
        String componentType = getSpinnerValueByCode(eventList.getSslx());
        String eventType = getSpinnerValuesByMultiCode(eventList.getBhlx());
        if(!ListUtil.isEmpty(eventList.getFiles2())){
            String imgPath = eventList.getFiles2().get(0).getPath();
            if(!StringUtil.isEmpty(imgPath)){
                AMImageLoader.loadStringRes(holder.task_list_item_iv, imgPath);
            } else {
                AMImageLoader.loadResId(holder.task_list_item_iv, R.mipmap.patrol_ic_empty);
            }
        }


        String state = "?????????";
        if (GzpsConstant.EVENT_SIMPLE_STATE_ACTIVE.equals(eventList.getState())) {
            state = "?????????";
            if (!StringUtil.isEmpty(eventList.getBlqx())) {
                state += " (" + eventList.getBlqx() + ")";
            }
        } else {
            state = "?????????";
        }
        if (!StringUtil.isEmpty(state)) {
            int start = state.indexOf("(");
            int end = state.lastIndexOf(")");
            if (start != -1 && end != -1) {
                holder.link.setText(SpannableStringHelper.builderString(state, start + 1, end, eventList.getColour()));
            } else {
                holder.link.setText(state);
            }
        } else {
            holder.link.setText("");
        }
        holder.title_tv.setText(componentType + "  " + eventType);
        holder.person_tv.setText(eventList.getSbr());
        holder.date_tv.setText(TimeUtil.getStringTimeYMDS(new Date(eventList.getSbsj2())));
        holder.addr_tv.setText(eventList.getSzwz());
        holder.state_tv.setVisibility(View.GONE);
        if (showCode) {
            holder.ll_upload_event_code.setVisibility(View.VISIBLE);
            holder.task_item_code.setText(eventList.getId() + "");
        } else {
            holder.ll_upload_event_code.setVisibility(View.GONE);
            holder.task_item_code.setText("");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position, eventList);
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
     * ????????????????????????????????????????????????????????????
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
            value = value + "???" + getSpinnerValueByCode(code);
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
        View ll_upload_event_code;
        TextView task_item_code;

        public MyViewHolder(View itemView) {
            super(itemView);
            task_list_item_iv = (ImageView) itemView.findViewById(R.id.task_list_item_iv);
            link = (TextView) itemView.findViewById(R.id.task_item_link);
            title_tv = (TextView) itemView.findViewById(R.id.task_item_title);
            person_tv = (TextView) itemView.findViewById(R.id.task_item_person);
            date_tv = (TextView) itemView.findViewById(R.id.task_item_date);
            addr_tv = (TextView) itemView.findViewById(R.id.task_item_addr);
            state_tv = (TextView) itemView.findViewById(R.id.task_item_status);
            ll_upload_event_code = itemView.findViewById(R.id.ll_upload_event_code);
            task_item_code = (TextView) itemView.findViewById(R.id.task_item_code);

        }
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener<EventAffair.EventAffairBean> listener) {
        this.listener = listener;
    }

}
