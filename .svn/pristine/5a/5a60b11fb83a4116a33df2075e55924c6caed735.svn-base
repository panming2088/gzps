package com.augurit.agmobile.mapengine.common.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.am.fw.utils.ListUtil;

import java.util.List;

/**
 * RecycleView的adapter基类
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.comn.adapter
 * @createTime 创建时间 ：2016-11-25
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-25
 */

public abstract class BaseRecyclerAdapter <T extends BaseRecyclerAdapter.BaseRecyclerViewHolder,D>
        extends RecyclerView.Adapter<T> {

    protected List<D> mDataList ;

    protected OnItemClickListener mListener ;
    protected OnItemLongClickListener mLongListener ;

    public BaseRecyclerAdapter(List<D> mDataList) {
        this.mDataList = mDataList;
    }
    public List<D> getDataList(){
        return mDataList ;
    }
    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        T holder ;
        holder = createViewHolder(LayoutInflater.from(parent.getContext()),parent, viewType) ;
        return holder;
    }


    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onItemClick(v, holder.getAdapterPosition(), getItemId(holder.getAdapterPosition()));
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongListener != null){
                    return mLongListener.onItemLongClick(v, holder.getAdapterPosition(), getItemId(holder.getAdapterPosition()));
                }
                return false;
            }
        });
        onBindViewHolder((T) holder, position, mDataList.get(position)) ;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener ;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        mLongListener = listener ;
    }

    @Override
    public int getItemCount() {
        if(ListUtil.isEmpty(mDataList)){
            return 0;
        }
        return mDataList.size();
    }

    static public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder{
        public BaseRecyclerViewHolder(View itemView) {
            super(itemView);
        }
        public <K extends View> K findView(int id){
            return (K) itemView.findViewById(id);
        }
    }

    public abstract T createViewHolder(LayoutInflater inflater,ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(T holder, int position,D data);

    public interface OnItemClickListener{
        void onItemClick(View view, int position, long id) ;

    }
    public interface OnItemLongClickListener{
        boolean onItemLongClick(View view, int position, long id) ;

    }

    public void notifyDataChanged(List<D> newDatas){
        mDataList.clear();
        mDataList.addAll(newDatas);
        notifyDataSetChanged();
    }
}
