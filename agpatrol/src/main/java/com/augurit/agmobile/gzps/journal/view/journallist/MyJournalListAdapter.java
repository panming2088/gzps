package com.augurit.agmobile.gzps.journal.view.journallist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * 设施纠错列表
 *
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search.view
 * @createTime 创建时间 ：2017-03-20
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-20
 * @modifyMemo 修改备注：
 */
public class MyJournalListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, ModifiedFacility> {

    private Context mContext;

    public MyJournalListAdapter(Context context, List<ModifiedFacility> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<ModifiedFacility> searchResults) {
        mDataList.addAll(searchResults);
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
//        return new ModifiedIdentificationViewHolder(inflater.inflate(R.layout.item_modified_identification, null));
        return new ModifiedIdentificationViewHolder(inflater.inflate(R.layout.item_journal_affair, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, ModifiedFacility data) {
        if (holder instanceof ModifiedIdentificationViewHolder) {

            ModifiedIdentificationViewHolder viewHolder = (ModifiedIdentificationViewHolder) holder;

            /**
             * 判断规则如下：
             * 如果是市级单位，那么这里显示的parentOrg；
             * 否则，如果是区级单位或者净水公司，判断是否有directOrg，如果没有directOrg再判断是否有superviseOrg；
             * 在superviseOrg都没有的情况下才显示parentOrg
             */
            String district = null;
            String userOrg = BaseInfoManager.getUserOrg(mContext);
            if (userOrg != null && userOrg.contains("市") && !TextUtils.isEmpty(data.getParentOrgName())){
//                district = data.getParentOrgName();
                if (!TextUtils.isEmpty(data.getDirectOrgName())){
                    district = data.getDirectOrgName();
                }else if (!TextUtils.isEmpty(data.getSuperviseOrgName())){
                    district = data.getSuperviseOrgName();
                }else {
                    district = data.getParentOrgName();
                }
            }else {
                if (!TextUtils.isEmpty(data.getDirectOrgName())){
                    district = data.getDirectOrgName();
                }else if (!TextUtils.isEmpty(data.getSuperviseOrgName())){
                    district = data.getSuperviseOrgName();
                }else {
                    district = data.getParentOrgName();
                }
            }

            if (!TextUtils.isEmpty(district)) {
                viewHolder.tv_district.setText(district);
            }else {
                viewHolder.tv_district.setText("");
            }
            viewHolder.tv_upload_person.setText(data.getMarkPerson());


//            List<Photo> photos = data.getPhotos();
//            if ("设施不存在".equals(data.getCorrectType())) {
//                viewHolder.iv.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.mipmap.facility_not_exist));
//            } else if (ListUtil.isEmpty(photos)) {
//                viewHolder.iv.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.mipmap.patrol_ic_empty));
//            } else {
//                //使用Glide进行加载图片
//                Glide.with(mContext)
//                        .load(photos.get(0).getThumbPath())
//                        .error(R.mipmap.patrol_ic_load_fail)
//                        .transform(new GlideRoundTransform(mContext))
//                        .into(viewHolder.iv);
//            }
            /**
             * 如果有正确地址就用正确地址，否则用原地址
             */
            if (!TextUtils.isEmpty(data.getAddr())) {
                viewHolder.tv_address.setText(data.getAddr());
            } else {
                viewHolder.tv_address.setText(data.getOriginAddr());
            }

            if (data.getUpdateTime() != null && data.getUpdateTime() > 0){
                viewHolder.tv_time.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getUpdateTime())));
            }else if (data.getRecordTime() != null) {
                viewHolder.tv_time.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getRecordTime())));
            }
            if (data.getId() != null) {
                viewHolder.tv_mark_id.setText("巡检日志编号：" + data.getId());
            }
            viewHolder.tv_description.setText(data.getLayerName()+ (TextUtils.isEmpty(data.getObjectId())?"("+data.getObjectId()+")":""));
        }
    }


    public static class ModifiedIdentificationViewHolder extends BaseRecyclerViewHolder {

        TextView tv_mark_id;
//        ImageView iv;
        TextView tv_upload_person;
        TextView tv_district;
        TextView tv_address;
        TextView tv_time;
        TextView tv_description;
        public ModifiedIdentificationViewHolder(View itemView) {
            super(itemView);
//            iv = findView(R.id.search_result_item_iv);
            tv_upload_person = (TextView) itemView.findViewById(R.id.tv_upload_person);
            tv_district = (TextView) itemView.findViewById(R.id.tv_district);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_mark_id = (TextView) itemView.findViewById(R.id.tv_mark_id);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
        }
    }
}
