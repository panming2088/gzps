package com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.util.SetCheckStateUtil;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.uploadfacility.model.ApprovalOpinion;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion
 * @createTime 创建时间 ：17/12/26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/26
 * @modifyMemo 修改备注：
 */
@Deprecated
public class ApprovalOpinionAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, ApprovalOpinion> {

    private Context mContext;

    public ApprovalOpinionAdapter(Context context, List<ApprovalOpinion> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<ApprovalOpinion> searchResults) {
        mDataList.addAll(searchResults);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ApprovalOpinionViewHolder(inflater.inflate(R.layout.item_approval_opinion, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, ApprovalOpinion data) {

        if (holder instanceof ApprovalOpinionViewHolder){

            ApprovalOpinionViewHolder viewHolder = (ApprovalOpinionViewHolder) holder;
            viewHolder.tv_approve_date.setText(data.getCheckTimeStr());
            viewHolder.tv_approve_date.setReadOnly();
            viewHolder.tv_approver.setText(data.getCheckPerson());
            viewHolder.tv_approver.setReadOnly();
            viewHolder.tv_option.setText(data.getCheckDesription());
            viewHolder.tv_option.setReadOnly();
            viewHolder.tv_approve_state.setReadOnly();

//            if (data.getCheckState().equals(CheckState.UNCHECK)) {
//                viewHolder.tv_approve_state.setText("未审核");
//                viewHolder.tv_approve_state.setEditTextColor(Color.RED);
//            } else if (data.getCheckState().equals(CheckState.SUCCESS)) {
//                viewHolder.tv_approve_state.setText("审核通过");
//                viewHolder.tv_approve_state.setEditTextColor(Color.parseColor("#3EA500"));
//            } else if (data.getCheckState().equals(CheckState.IN_DOUBT)) {
//                viewHolder.tv_approve_state.setText("存在疑问");
//                viewHolder.tv_approve_state.setEditTextColor(Color.parseColor("#FFFFC248"));
//            }
            SetCheckStateUtil.setCheckState(data.getCheckState(),viewHolder.tv_approve_state);
        }
    }


    protected static class ApprovalOpinionViewHolder extends BaseRecyclerViewHolder {


        private final TextItemTableItem tv_option;
        private final TextItemTableItem tv_approve_date;
        private final TextItemTableItem tv_approver;
        private final TextItemTableItem tv_approve_state;

        public ApprovalOpinionViewHolder(View itemView) {
            super(itemView);
            tv_option = findView(R.id.tv_option);
            tv_approve_date = findView(R.id.tv_approve_date);
            tv_approver = findView(R.id.tv_approver);
            tv_approve_state =findView(R.id.tv_approve_state);
        }
    }
}
