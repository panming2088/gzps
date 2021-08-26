package com.augurit.agmobile.gzps.publicaffair.view.facilityaffair;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by xcl on 2017/11/17.
 */

public class PipeAffairAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, PipeBean> {

    private Context mContext;

    public PipeAffairAdapter(Context context, List<PipeBean> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<PipeBean> searchResults) {
        mDataList.addAll(searchResults);
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new PublicAffairViewHolder(inflater.inflate(R.layout.item_pipe_affair, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, final PipeBean data) {
        if (holder instanceof PublicAffairViewHolder) {

            PublicAffairViewHolder viewHolder = (PublicAffairViewHolder) holder;
//            if("设施不存在".equals(data.getCorrectType())){
//                viewHolder.iv_iamge.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.mipmap.facility_not_exist));
//            } else if (TextUtils.isEmpty(data.getAttPath())){
//                viewHolder.iv_iamge.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.mipmap.patrol_ic_empty));
//            } else {
//                //使用Glide进行加载图片
//                Glide.with(mContext)
//                        .load(data.getThumPath())
//                        .error(R.mipmap.patrol_ic_load_fail)
////                        .transform(new GlideRoundTransform(mContext))
//                        .into(viewHolder.iv_iamge);
//            }

            if (!TextUtils.isEmpty(data.getDirection())) {
                viewHolder.tv_address.setText("管线流向：" + data.getDirection());
            }else{
                viewHolder.tv_address.setText("");
            }

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

            viewHolder.tv_time.setText(TimeUtil.getStringTimeMdHmChines(new Date(data.getMarkTime())));
            String desctiption = "";
            if ("1".equals(data.getLackType())) {//0：校核、1:新增
                desctiption = "新增(" + StringUtil.getNotNullString(data.getPipeType(), "") + ")数据";
            } else if ("0".equals(data.getLackType())) {
                desctiption = "校核数据(" + StringUtil.getNotNullString(data.getPipeType(), "") + ")数据";
            } else {
                desctiption = "删除数据(" + StringUtil.getNotNullString(data.getPipeType(), "") + ")数据";
            }
            viewHolder.tv_description.setText(desctiption);
            viewHolder.tv_upload_person.setText(data.getMarkPerson());
        }
    }


    public static class PublicAffairViewHolder extends BaseRecyclerViewHolder {


        TextView tv_time;
        TextView tv_address;
        TextView tv_district;
        TextView tv_upload_person;
        TextView tv_description;
        ImageView iv_iamge;

        public PublicAffairViewHolder(View itemView) {
            super(itemView);
            iv_iamge = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_district = (TextView) itemView.findViewById(R.id.tv_district);
            tv_upload_person = (TextView) itemView.findViewById(R.id.tv_upload_person);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
        }
    }
}
