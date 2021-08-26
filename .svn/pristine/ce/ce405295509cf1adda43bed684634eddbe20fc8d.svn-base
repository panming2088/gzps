package com.augurit.agmobile.gzps.doorno.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.doorno.model.SewerageInfoBean;
import com.augurit.agmobile.gzpssb.SewerageTableThree;

import java.util.List;
import java.util.Map;

/**
 * @author 创建人 ：ouyangzhibao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzpssb.adapter
 * @createTime 创建时间 ：2018/4/20
 * @modifyBy 修改人 ：ouyangzhibao
 * @modifyTime 修改时间 ：2018/4/20
 * @modifyMemo 修改备注：
 */
public class SewerageTableThreeAdapter extends BaseRecyleAdapter {
    private List<SewerageInfoBean.WellBeen> wellBeens;
    private Context context;
    public SewerageTableThreeAdapter(List<?> data, Map<Integer, Integer> hashMap, Context context) {
        super(data, hashMap);
        wellBeens= (List<SewerageInfoBean.WellBeen>) data;
        this.context=context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SewerageTableThree sewerageTableThree= (SewerageTableThree) holder.dataBinding;
        if(TextUtils.isEmpty(wellBeens.get(position).getWellId())){
            sewerageTableThree.tvWellType.setText(wellBeens.get(position).getPipeType());
        }else{
            if(TextUtils.isEmpty(wellBeens.get(position).getPipeType())){
                sewerageTableThree.tvWellType.setText("井编号："+wellBeens.get(position).getWellId());
            }else{
                sewerageTableThree.tvWellType.setText(wellBeens.get(position).getPipeType()+"("+wellBeens.get(position).getWellId()+")");
            }
        }
        if("污水管".equals(wellBeens.get(position).getPipeType())){
            sewerageTableThree.tvWellType.setTextColor(context.getResources().getColor(R.color.sewerage_pipe));
        }
        if("雨水管".equals(wellBeens.get(position).getPipeType())){
            sewerageTableThree.tvWellType.setTextColor(context.getResources().getColor(R.color.rain_pipe));
        }
        if("雨污合流".equals(wellBeens.get(position).getPipeType())){
            sewerageTableThree.tvWellType.setTextColor(context.getResources().getColor(R.color.rain_sewerage_pipe));
        }
        if("".equals(wellBeens.get(position).getWellPro())||wellBeens.get(position).getWellPro()==null){
            sewerageTableThree.llWellPro.setVisibility(View.GONE);
        }else{
            sewerageTableThree.llWellPro.setVisibility(View.VISIBLE);
        }
        if("".equals(wellBeens.get(position).getNewDrainPro())||wellBeens.get(position).getNewDrainPro()==null){
            sewerageTableThree.llWellNewdrainPro.setVisibility(View.GONE);
        }else{
            sewerageTableThree.llWellNewdrainPro.setVisibility(View.VISIBLE);
        }

        if(TextUtils.isEmpty(wellBeens.get(position).getWellType()) || TextUtils.isEmpty(wellBeens.get(position).getWellDir())){
            sewerageTableThree.downLine.setVisibility(View.GONE);
        }
        super.onBindViewHolder(holder, position);
    }
}
