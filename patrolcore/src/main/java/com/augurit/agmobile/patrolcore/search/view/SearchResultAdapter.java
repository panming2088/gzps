package com.augurit.agmobile.patrolcore.search.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.search.model.SearchResult;
import com.bumptech.glide.Glide;


import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search.view
 * @createTime 创建时间 ：2017-03-20
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-20
 * @modifyMemo 修改备注：
 */
public class SearchResultAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, SearchResult> {

    private Context mContext;

    public SearchResultAdapter(Context context, List<SearchResult> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<SearchResult> searchResults){
        mDataList.addAll(searchResults);
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new SearchResultViewHolder(inflater.inflate(R.layout.search_result_item, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, SearchResult data) {
        if (holder instanceof SearchResultViewHolder) {
            SearchResultViewHolder viewHolder = (SearchResultViewHolder) holder;
            if (TextUtils.isEmpty(data.getPic())){
                viewHolder.iv.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.mipmap.patrol_ic_empty));
            }else {
                //使用Glide进行加载图片
                Glide.with(mContext)
                        .load(data.getPic())
                        .error(R.mipmap.patrol_ic_load_fail)
                        .transform(new GlideRoundTransform(mContext))
                        .into(viewHolder.iv);
            }

            if (!TextUtils.isEmpty(data.getAddress())) {
                viewHolder.addr.setText(data.getAddress());
            }else {
                viewHolder.addr.setText("");
            }

            /*if (data.getAddressFilterList() != null) {
                viewHolder.addr.setText(data.getAddressFilterList());
            }*/

            if (!TextUtils.isEmpty(data.getStatus())) {
                viewHolder.status.setText(data.getStatus());
            }

            if (data.getDate() != null) {
                viewHolder.date.setText(data.getDate());
            }

            viewHolder.title.setText(data.getName());

            viewHolder.ll_result_item_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        mListener.onItemClick(view,position,getItemId(position));
                    }
                }
            });
                  /*  viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mListener != null){
                                mListener.onItemClick(view,position,getItemId(position));
                            }
                        }
                    });*/
        }
    }


    public static class SearchResultViewHolder extends BaseRecyclerViewHolder {
        ImageView iv;
        TextView title;
        TextView date;
        TextView addr;
        TextView status;
        LinearLayout ll_result_item_root;
        //  CardView cardView;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
            iv = findView(R.id.search_result_item_iv);
            ll_result_item_root =findView(R.id.ll_result_item_root);
            // cardView = findView(R.id.cardview);
            title = (TextView) itemView.findViewById(R.id.search_item_title);
            date = (TextView) itemView.findViewById(R.id.search_item_date);
            addr = (TextView) itemView.findViewById(R.id.search_item_addr);
            status = (TextView) itemView.findViewById(R.id.search_item_status);
        }
    }
}
