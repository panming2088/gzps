package com.augurit.agmobile.mapengine.common.widget.callout.attribute;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.R;
import com.augurit.am.fw.utils.ListUtil;


/**
 * 属性候选列表适配器
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.identify
 * @createTime 创建时间 ：2016-12-02 11:26
 * @modifyBy 修改人 ：weiqiuyue
 * @modifyTime 修改时间 ：2016-12-29 11:26
 */
public class AgFindResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //    private IdentifyResult[] mResults;
    /**
     * 使用AgFindResult比IdentifyResult更好。
     * 使用AttributeUtil的covertIdentifyResultToAgFindResult支持IdentifyResult转AgFindResult
     */
    private AMFindResult[] mResults;
    private int mSelectedPosition;
    private int mUnSelectColor;
    private int mSelectColor;
    private OnItemClickListener mOnItemClickListener;

    public AgFindResultAdapter(Context context, AMFindResult[] agFindResultArray) {
        init(context, agFindResultArray);
    }

    public void init(Context context, AMFindResult[] agFindResultArray) {
        mContext = context;
        mResults = agFindResultArray;
        mSelectedPosition = 0;
        mUnSelectColor = ResourcesCompat.getColor(context.getResources(), R.color.agmobile_white, null);
        mSelectColor = ResourcesCompat.getColor(context.getResources(), R.color.list_item_selected, null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.identify_listitem_result, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            final MyViewHolder viewHolder = (MyViewHolder) holder;
            AMFindResult result = mResults[position];
            String name = "";
            if (result.getDisplayFieldName() != null && result.getAttributes().get(result.getDisplayFieldName()) != null) {
                name = result.getAttributes().get(result.getDisplayFieldName()).toString();
            } else if(result.getDisplayName() != null){
                name = result.getDisplayName();
            }
            viewHolder.tv_name.setText(TextUtils.isEmpty(name.trim()) ? mContext.getString(R.string.name_null) : name);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int lastSelect = mSelectedPosition;
                    mSelectedPosition = viewHolder.getAdapterPosition();
                    notifyItemChanged(lastSelect);
                    notifyItemChanged(mSelectedPosition);   // 这里由于BottomSheet收起动画的原因不能立即刷新
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, mResults[position]);
                    }
                }
            });
            if (viewHolder.getAdapterPosition() == mSelectedPosition) {   // 高亮选中项
                viewHolder.itemView.setBackgroundColor(mSelectColor);
                viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.agmobile_blue));
            } else {
                viewHolder.itemView.setBackgroundColor(mUnSelectColor);
                viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.agmobile_black));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (ListUtil.isEmpty(mResults)) {
            return 0;
        } else {
            return mResults.length;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, AMFindResult result);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;

        MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
