package com.augurit.agmobile.gzps.im.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.im.bean.FriendItem;
import com.augurit.agmobile.gzps.im.widget.SelectableRoundedImageView;

import java.util.ArrayList;

/**
 * @author 创建人 ：taoerxiang
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.im.adapter
 * @createTime 创建时间 ：2017-11-09
 * @modifyBy 修改人 ：taoerxiang
 * @modifyTime 修改时间 ：2017-11-09
 * @modifyMemo 修改备注：
 */

public class MyFriendListAdapter extends RecyclerView.Adapter<MyFriendListAdapter.MyViewHolder> {
    private ArrayList<FriendItem> groupMembers;
    private Context context;
    private TextView countTv;
    private TextView no_friend_tv;

    public MyFriendListAdapter(FragmentActivity activity, ArrayList<FriendItem> groupMembers, TextView friendCount, TextView no_friend_tv) {
        this.groupMembers = groupMembers;
        this.context = activity;
        this.countTv = friendCount;
        this.no_friend_tv = no_friend_tv;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.friend_item, null);

        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String title = groupMembers.get(position).getTitle();
        String userName = groupMembers.get(position).getUserName();
        if (TextUtils.isEmpty(title)) {
            holder.tvName.setText(userName);
        } else {
            String[] split = title.split(" ");
            if (split.length > 1) {
                holder.tvName.setText(userName + "(" + split[0] + split[1] + ")");
            } else {
                holder.tvName.setText(userName + "(" + split[0] + ")");
            }

        }
        holder.tvPhone.setText(groupMembers.get(position).getPhone());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (groupMembers.size() > 0) {
            countTv.setText("(" + groupMembers.size() + "人)");
            no_friend_tv.setVisibility(View.GONE);
            return groupMembers.size();
        } else {
            countTv.setText("(" + groupMembers.size() + "人)");
            no_friend_tv.setVisibility(View.VISIBLE);
            return 0;
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvPhone;
        SelectableRoundedImageView mImageView;

        TextView tvUserId;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = (SelectableRoundedImageView) itemView.findViewById(R.id.frienduri);
            tvName = (TextView) itemView.findViewById(R.id.friendname);
            tvPhone = (TextView) itemView.findViewById(R.id.phone);
        }
    }

    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
