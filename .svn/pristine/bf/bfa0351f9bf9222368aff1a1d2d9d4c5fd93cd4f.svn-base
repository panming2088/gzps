package com.augurit.agmobile.gzps.uploadevent.view.eventflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadevent.model.EventAdvice;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;

import java.util.List;

/**
 * Created by xcl on 2017/11/11.
 */
@Deprecated
public class EventAdviceAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, EventAdvice> {

    private Context mContext;

    public EventAdviceAdapter(Context context, List<EventAdvice> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<EventAdvice> searchResults){
        mDataList.addAll(searchResults);
    }

    public void clear(){
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new EventAdviceAdapterViewHolder(inflater.inflate(R.layout.item_handle_advice, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, EventAdvice data) {
        if (holder instanceof EventAdviceAdapterViewHolder) {
            EventAdviceAdapterViewHolder viewHolder =
                    (EventAdviceAdapterViewHolder) holder;

            viewHolder.tv_user_id.setText(data.getAdvisor());
            viewHolder.tv_advice.setText(data.getContent());
        }
    }


    public static class  EventAdviceAdapterViewHolder extends BaseRecyclerViewHolder {


        TextView tv_organization;
        TextView tv_user_id;
        TextView tv_advice;

        public EventAdviceAdapterViewHolder(View itemView) {
            super(itemView);
            tv_organization = (TextView) itemView.findViewById(R.id.tv_organization);
            tv_user_id = (TextView) itemView.findViewById(R.id.tv_user_id);
            tv_advice = (TextView) itemView.findViewById(R.id.tv_advice);
        }
    }
}

