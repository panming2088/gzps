package com.augurit.agmobile.gzps.jbjpsdy;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.view.JbjMonitorListAdapter;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 排水户列表适配器
 *
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/11/9 10:57
 */
public class JbjPsdySewerageUserListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, SewerageUserEntity> {

    private OnSewerageUserListItemClickListener mOuterOnItemClickListener;

    private final WeakReference<Context> mContextRef;

    private boolean canLoadImg = true;

    public JbjPsdySewerageUserListAdapter(Context context,List<SewerageUserEntity> entity) {
        super(entity);
        mContextRef = new WeakReference<>(context);
    }

//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContextRef.get()).inflate(R.layout.drainage_view_jbjpsdy_sewerage_user_list_item, null);
//        return new JbjPsdySwerageUserListViewHolder(view, mInnerOnItemClickListener);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof JbjPsdySwerageUserListViewHolder) {
//            ((JbjPsdySwerageUserListViewHolder) holder).setData(mDataList.get(position), mContextRef.get(), canLoadImg);
//        }
//    }

//    @Override
//    public int getItemCount() {
//        return mDataList.size();
//    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContextRef.get()).inflate(R.layout.drainage_view_jbjpsdy_sewerage_user_list_item, parent,false);
        return new JbjPsdySwerageUserListViewHolder(view, mInnerOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, SewerageUserEntity data) {
        if (holder instanceof JbjPsdySwerageUserListViewHolder) {
            ((JbjPsdySwerageUserListViewHolder) holder).setData(data, mContextRef.get(), canLoadImg);
        }
    }

    // ==============================回调==============================

    private final OnSewerageUserListItemClickListener mInnerOnItemClickListener = new OnSewerageUserListItemClickListener() {
        @Override
        public void onItemClick(View v, RecyclerView.ViewHolder holder, SewerageUserEntity entity) {
            if (mOuterOnItemClickListener != null) {
                mOuterOnItemClickListener.onItemClick(v, holder, mDataList.get(holder.getLayoutPosition() - 1));
            }
        }

        @Override
        public void onLocation(View v, RecyclerView.ViewHolder holder, SewerageUserEntity entity) {
            if (mOuterOnItemClickListener != null) {
                mOuterOnItemClickListener.onLocation(v, holder, mDataList.get(holder.getLayoutPosition() - 1));
            }
        }
    };

    // ==============================自定义方法==============================

    public void update(final List<SewerageUserEntity> newDatas) {
//        if (newDatas == null || newDatas.isEmpty()) {
//            notifyItemRangeRemoved(0, mDataList.size());
//        } else {
////            DiffUtil.calculateDiff(new DiffCallback(mDataList, newDatas)).dispatchUpdatesTo(this);
//        }
        mDataList.clear();
        if (newDatas == null || newDatas.isEmpty()) {
            return;
        }
        mDataList.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void setOnPshItemClickListener(OnSewerageUserListItemClickListener listener) {
        mOuterOnItemClickListener = listener;
    }

    public void stopLoadImg() {
        canLoadImg = false;
        Glide.with(mContextRef.get()).pauseRequests();
    }

    public void startLoadImg() {
        canLoadImg = true;
        Glide.with(mContextRef.get()).resumeRequests();

    }

    // ==============================自定义接口/类==============================

    private static class JbjPsdySwerageUserListViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        private final View mRoot;
        private final ImageView mImgIcon;
        private final TextView mTvHouseHolderName;
        private final TextView mTvBelong;
        private final TextView mTvAddrName;
        private final TextView mTvAddr;
        private final TextView mTvTime;
        private final ImageView mImgLocation;

        private OnSewerageUserListItemClickListener mOnItemClickListener;

        public JbjPsdySwerageUserListViewHolder(View itemView, OnSewerageUserListItemClickListener listener) {
            super(itemView);

            mRoot = itemView.findViewById(R.id.itemRoot);
            itemView.setOnClickListener(this);
            mImgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            mTvHouseHolderName = (TextView) itemView.findViewById(R.id.tvHouseHolderName);
            mTvBelong = (TextView) itemView.findViewById(R.id.tvBelong);
            mTvAddrName = (TextView) itemView.findViewById(R.id.tvAddrName);
            mTvAddr = (TextView) itemView.findViewById(R.id.tvAddr);
            mTvTime = (TextView) itemView.findViewById(R.id.tvTime);
            mImgLocation = itemView.findViewById(R.id.imgLocation);
            mImgLocation.setOnClickListener(this);

            mOnItemClickListener = listener;
        }

        public void setData(SewerageUserEntity entity, Context context, boolean canLoadImg) {
            mTvHouseHolderName.setText(entity.getHouseHolderName());
            mTvBelong.setText(entity.getBelong());
            String addrName = entity.getAddrName();
            String type2Str = entity.getType2Str();
            if (!TextUtils.isEmpty(type2Str)) {
                addrName = addrName + "(" + type2Str + ")";
            }
            mTvAddrName.setText(addrName);
            mTvAddr.setText(entity.getAddr());
            mTvTime.setText(entity.getTimeStr());

            RequestManager requestManager = Glide.with(context);
//            if (canLoadImg) {
            requestManager.setDefaultOptions(new RequestManager.DefaultOptions() {
                @Override
                public <T> void apply(GenericRequestBuilder<T, ?, ?, ?> requestBuilder) {
                    requestBuilder.placeholder(R.drawable.drainage_ic_loading_img);
                    requestBuilder.error(R.mipmap.patrol_ic_load_fail);
                    ViewGroup.LayoutParams layoutParams = mImgIcon.getLayoutParams();
                    requestBuilder.override(layoutParams.width, layoutParams.height);
                }
            });
            if (TextUtils.isEmpty(entity.getImgUrl())) {
                requestManager.load(R.mipmap.patrol_ic_empty).into(mImgIcon);
            } else {
                requestManager.load(entity.getImgUrl()).into(mImgIcon);
            }
//            } else {
//                mImgIcon.setImageResource(R.mipmap.ic_patrol);
//            }
        }

        @Override
        public void onClick(View v) {
            if (v == mRoot) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, this, null);
                }
            } else if (v == mImgLocation) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onLocation(v, this, null);
                }
            }
        }
    }

    private static class DiffCallback extends DiffUtil.Callback {

        private final List<SewerageUserEntity> mDataList = new ArrayList<>();
        private final List<SewerageUserEntity> newDatas = new ArrayList<>();

        public DiffCallback(List<SewerageUserEntity> oldList, List<SewerageUserEntity> newList) {
            mDataList.addAll(oldList);
            newDatas.addAll(newList);
        }

        @Override
        public int getOldListSize() {
            return mDataList.size();
        }

        @Override
        public int getNewListSize() {
            return newDatas.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mDataList.get(oldItemPosition).getId().equals(newDatas.get(newItemPosition).getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return mDataList.get(oldItemPosition) == newDatas.get(newItemPosition);
        }
    }

    public interface OnSewerageUserListItemClickListener {
        void onItemClick(View v, RecyclerView.ViewHolder holder, SewerageUserEntity entity);

        void onLocation(View v, RecyclerView.ViewHolder holder, SewerageUserEntity entity);
    }

}
