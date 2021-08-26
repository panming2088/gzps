package com.augurit.agmobile.gzps.track.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.track.util.LengthUtil;
import com.augurit.agmobile.gzps.track.util.TimeUtil;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.am.fw.utils.ListUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view
 * @createTime 创建时间 ：2017-06-13
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-13
 * @modifyMemo 修改备注：
 */

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.TrackListHolder> {

    private Context mContext;
    private OnRecyclerItemClickListener mOnRecyclerItemClickListener;
    private List<List<GPSTrack>> mTrackHistory;

    // RecyclerView 的第一个item，肯定是展示StickyLayout的.
    public static final int FIRST_STICKY_VIEW = 1;
    // RecyclerView 除了第一个item以外，要展示StickyLayout的.
    public static final int HAS_STICKY_VIEW = 2;
    // RecyclerView 的不展示StickyLayout的item.
    public static final int NONE_STICKY_VIEW = 3;

    public TrackListAdapter(Context context) {
        this.mContext = context;
    }

    public void notifyDataSetChanged(List<List<GPSTrack>> trackHistory) {
        mTrackHistory = trackHistory;
        notifyDataSetChanged();
    }

    public void loadMore(List<List<GPSTrack>> moreTrack) {
        if(mTrackHistory == null){
            mTrackHistory = new ArrayList<>();
        }
        mTrackHistory.addAll(moreTrack);
        notifyDataSetChanged();
    }

    @Override
    public TrackListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TrackListAdapter.TrackListHolder(View.inflate(mContext, R.layout.tracklist_item, null));
    }

    @Override
    public void onBindViewHolder(TrackListHolder trackListHolder, final int i) {
        final List<GPSTrack> gpsTracks = mTrackHistory.get(i);
        if (!ListUtil.isEmpty(gpsTracks)) {
            String stickyStr = com.augurit.am.fw.utils.TimeUtil.getStringTimeYMD(new Date(gpsTracks.get(0).getRecordDate()));
            if (i == 0) {
                trackListHolder.sticky.setVisibility(View.VISIBLE);
                trackListHolder.sticky.setText(stickyStr);
                // 第一个item的吸顶信息肯定是展示的，并且标记tag为FIRST_STICKY_VIEW
                trackListHolder.itemView.setTag(FIRST_STICKY_VIEW);
            } else {
                // 之后的item都会和前一个item要展示的吸顶信息进行比较，不相同就展示，并且标记tag为HAS_STICKY_VIEW
                String lastStickyStr = com.augurit.am.fw.utils.TimeUtil.getStringTimeYMD(new Date(mTrackHistory.get(i - 1).get(0).getRecordDate()));
                if (!TextUtils.equals(stickyStr, lastStickyStr)) {
                    trackListHolder.sticky.setVisibility(View.VISIBLE);
                    trackListHolder.sticky.setText(stickyStr);
                    trackListHolder.itemView.setTag(HAS_STICKY_VIEW);
                } else {
                    // 相同就不展示，并且标记tag为NONE_STICKY_VIEW
                    trackListHolder.sticky.setVisibility(View.GONE);
                    trackListHolder.itemView.setTag(NONE_STICKY_VIEW);
                }
            }
            // ContentDescription 用来记录并获取要吸顶展示的信息
            trackListHolder.itemView.setContentDescription(stickyStr);

            GPSTrack gpsTrack1 = gpsTracks.get(0);
            Date date = new Date(gpsTrack1.getRecordDate());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            trackListHolder.start.setText(sdf.format(date));

            GPSTrack gpsTrack2 = gpsTracks.get(gpsTracks.size() - 1);
            trackListHolder.end.setText(sdf.format(new Date(gpsTrack2.getRecordDate())));

            long timestamp = gpsTrack2.getRecordDate() - gpsTrack1.getRecordDate();
            int time = (int) (timestamp / 1000);
            trackListHolder.time.setText(TimeUtil.formatSecond(time));
            trackListHolder.length.setText(LengthUtil.formatLength(Double.valueOf(gpsTrack2.getRecordLength())));
        }
        trackListHolder.track_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRecyclerItemClickListener != null) {
                    mOnRecyclerItemClickListener.onItemClick(v, i, gpsTracks);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (ListUtil.isEmpty(mTrackHistory)) {
            return 0;
        }
        return mTrackHistory.size();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    protected class TrackListHolder extends RecyclerView.ViewHolder {

        View ll_sticky;
        TextView sticky;
        TextView start;
        TextView end;
        TextView time;
        TextView length;
        View track_view;

        public TrackListHolder(View itemView) {
            super(itemView);
            sticky = (TextView) itemView.findViewById(R.id.tv_sticky);
            start = (TextView) itemView.findViewById(R.id.track_item_starttxt);
            end = (TextView) itemView.findViewById(R.id.track_item_endtxt);
            time = (TextView) itemView.findViewById(R.id.track_item_time);
            length = (TextView) itemView.findViewById(R.id.track_item_length);
            track_view = itemView.findViewById(R.id.track_view);
        }

    }
}
