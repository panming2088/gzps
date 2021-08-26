package com.augurit.agmobile.gzps.journal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.publicaffair.model.FacilityAffair;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.bumptech.glide.Glide;
import java.util.Date;
import java.util.List;


/**
 * @author : taoerxiang
 * @data : 2017-11-11  12:04
 * @des :
 */

public class UploadFacilityListAdapter  extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, FacilityAffair> {

    private Context mContext;

    public UploadFacilityListAdapter(Context context, List<FacilityAffair> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<FacilityAffair> searchResults){
        mDataList.addAll(searchResults);
    }

    public void clear(){
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new UploadFacilityViewHolder(inflater.inflate(R.layout.item_facility_affair, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, final FacilityAffair data) {
        if (holder instanceof UploadFacilityViewHolder) {

            UploadFacilityViewHolder viewHolder = (UploadFacilityViewHolder) holder;

            if("设施不存在".equals(data.getCorrectType())){
                viewHolder.iv_iamge.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.mipmap.facility_not_exist));
            } else if (TextUtils.isEmpty(data.getAttPath())){
                viewHolder.iv_iamge.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.mipmap.patrol_ic_empty));
            } else {
                //使用Glide进行加载图片
                Glide.with(mContext)
                        .load(data.getThumPath())
                        .error(R.mipmap.patrol_ic_load_fail)
//                        .transform(new GlideRoundTransform(mContext))
                        .into(viewHolder.iv_iamge);
            }

            if (!TextUtils.isEmpty(data.getAddr())) {
                viewHolder.tv_address.setText(data.getAddr());
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
                district = data.getParentOrgName();
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

            viewHolder.tv_time.setText(data.getUpdateTime()==null?
             TimeUtil.getStringTimeMdHmChines(new Date(data.getTime())):TimeUtil.getStringTimeMdHmChines(new Date(data.getUpdateTime())));
            String desctiption = "";
            if (data.getSource().equals("lack")){
                desctiption =  "新增(" + StringUtil.getNotNullString(data.getLayerName(),"") + ")数据";
            }else {
                desctiption =   "校核数据(" + StringUtil.getNotNullString(data.getLayerName(),"") +
                        StringUtil.getNotNullString(data.getCorrectType(),"")+")";
            }
            viewHolder.tv_description.setText(desctiption);
            viewHolder.tv_upload_person.setText(data.getMarkPerson());
        }
    }


    public static class UploadFacilityViewHolder extends BaseRecyclerViewHolder {


        TextView tv_time;
        TextView tv_address;
        TextView tv_district;
        TextView tv_upload_person;
        TextView tv_description;
        ImageView iv_iamge;

        public UploadFacilityViewHolder(View itemView) {
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
