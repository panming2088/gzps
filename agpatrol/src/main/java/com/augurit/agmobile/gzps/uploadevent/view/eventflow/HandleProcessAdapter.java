package com.augurit.agmobile.gzps.uploadevent.view.eventflow;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadevent.model.EventProcess;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by xcl on 2017/11/11.
 */
@Deprecated
public class HandleProcessAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, EventProcess> {

    private Context mContext;

    public HandleProcessAdapter(Context context, List<EventProcess> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<EventProcess> searchResults) {
        mDataList.addAll(searchResults);
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new HandleProcessViewHolder(inflater.inflate(R.layout.item_handle_process, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, EventProcess data) {
        if (holder instanceof HandleProcessViewHolder) {
            HandleProcessViewHolder viewHolder =
                    (HandleProcessViewHolder) holder;

            if (!TextUtils.isEmpty(data.getOpUser())) {
                viewHolder.tv_upload_user.setText(data.getOpUser());
            } else {
                viewHolder.tv_upload_user.setText("");
            }


            viewHolder.tv_upload_date.setText(TimeUtil.getStringTimeYMDS(new Date(data.getTime())));

        }
    }


    public static class HandleProcessViewHolder extends BaseRecyclerViewHolder {


        TextView tv_upload_user;
        TextView tv_upload_date;
        TextView tv_next_handler;
        TextView tv_phone;

        public HandleProcessViewHolder(View itemView) {
            super(itemView);
            tv_upload_user = (TextView) itemView.findViewById(R.id.tv_upload_user);
            tv_upload_date = (TextView) itemView.findViewById(R.id.tv_upload_date);
            tv_next_handler = (TextView) itemView.findViewById(R.id.tv_next_handler);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);

        }
    }
}
