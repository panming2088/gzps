package com.augurit.agmobile.gzps.componentmaintenance;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.componentmaintenance
 * @createTime 创建时间 ：17/10/14
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/14
 * @modifyMemo 修改备注：
 */

public class ComponetListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, Component> {

    private Context mContext;

    public ComponetListAdapter(Context context, List<Component> mDataList) {
        super(mDataList);
        this.mContext = context;
    }

    public void addData(List<Component> searchResults) {
        mDataList.addAll(searchResults);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ComponetListAdapter.ComponentListViewHolder(inflater.inflate(R.layout.component_list_item, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, Component data) {
        if (holder instanceof ComponetListAdapter.ComponentListViewHolder) {
            ComponetListAdapter.ComponentListViewHolder viewHolder = (ComponetListAdapter.ComponentListViewHolder) holder;

//            if (TextUtils.isEmpty(data.getPic())){
//                viewHolder.iv.setImageDrawable(viewHolder.itemView.getResources().getDrawable(com.augurit.agmobile.patrolcore.R.mipmap.patrol_ic_empty));
//            }else {
//                //使用Glide进行加载图片
//                Glide.with(mContext)
//                        .load(data.getPic())
//                        .error(com.augurit.agmobile.patrolcore.R.mipmap.patrol_ic_load_fail)
//                        .transform(new GlideRoundTransform(mContext))
//                        .into(viewHolder.iv);
//            }


            String name = String.valueOf(data.getGraphic().getAttributes().get(ComponentFieldKeyConstant.NAME));
            String usid = String.valueOf(data.getGraphic().getAttributes().get(ComponentFieldKeyConstant.USID));

            String type = LayerUrlConstant.getLayerNameByUnknownLayerUrl(data.getLayerUrl());

            String sort = String.valueOf(data.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SORT));

            String subtype = String.valueOf(data.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));

            /**
             * 10.27 xcl 保持界面左边对齐
             */
            String componentName = StringUtil.getNotNullString(name, "");
            String title = null;
            if (TextUtils.isEmpty(componentName)){
                title = StringUtil.getNotNullString(type, "");
            }else {
                title = componentName + "  " + StringUtil.getNotNullString(type, "");
            }
            title = StringUtil.getNotNullString(type, "") + "(" + usid + ")";
            viewHolder.title.setText(title);

            String date = String.valueOf(data.getGraphic().getAttributes().get(ComponentFieldKeyConstant.UPDATEDATE));
            String formatDate = "";
            try {
                formatDate  = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(date)));
            } catch (Exception e) {

            }
            viewHolder.date.setText(StringUtil.getNotNullString(formatDate, ""));

            int color = ResourcesCompat.getColor(mContext.getResources(), R.color.mark_light_purple, null);

            if (sort.contains("雨污合流")) {
                color = ResourcesCompat.getColor(mContext.getResources(), R.color.mark_light_purple, null);
            } else if (sort.contains("雨水")) {
                color = ResourcesCompat.getColor(mContext.getResources(), R.color.progress_line_green, null);
            } else if (sort.contains("污水")) {
                color = ResourcesCompat.getColor(mContext.getResources(), R.color.agmobile_red, null);
            }
            viewHolder.sort.setTextColor(color);

            viewHolder.sort.setText(StringUtil.getNotNullString(sort, ""));

            viewHolder.subtype.setText(StringUtil.getNotNullString(subtype, ""));

            String listFields = ComponentFieldKeyConstant.getListFieldsByLayerName(data.getLayerName());
            String[] listFieldArray = listFields.split(",");
            if (!StringUtil.isEmpty(listFields)
                    && listFieldArray.length == 3) {
                String field1_1 = listFieldArray[0].split(":")[0];
                String field1_2 = listFieldArray[0].split(":")[1];
                String field1 = String.valueOf(data.getGraphic().getAttributes().get(field1_2));
                viewHolder.field1.setText(field1_1 + "：" + StringUtil.getNotNullString(field1, ""));

                String field2_1 = listFieldArray[1].split(":")[0];
                String field2_2 = listFieldArray[1].split(":")[1];
                String field2 = String.valueOf(data.getGraphic().getAttributes().get(field2_2));
                viewHolder.field2.setText(field2_1 + "：" + StringUtil.getNotNullString(field2, ""));

                String field3_1 = listFieldArray[2].split(":")[0];
                String field3_2 = listFieldArray[2].split(":")[1];
                String field3 = String.valueOf(data.getGraphic().getAttributes().get(field3_2));
                viewHolder.field3.setText(field3_1 + "：" + StringUtil.getNotNullString(field3, ""));
            }


            /**
             * 如果是雨水口，显示特性：方形
             */
            if (data.getLayerName().equals("雨水口")) {
                String feature = String.valueOf(data.getGraphic().getAttributes().get(ComponentFieldKeyConstant.FEATURE));
                viewHolder.sort.setText(StringUtil.getNotNullString(feature, ""));
            }
            if ("雨水口".equals(type)) {
                String style = String.valueOf(data.getGraphic().getAttributes().get(ComponentFieldKeyConstant.STYLE));
                viewHolder.subtype.setText(StringUtil.getNotNullString(style, ""));
                viewHolder.subtype.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.dust_grey, null));
            } else {
                viewHolder.subtype.setText(StringUtil.getNotNullString(subtype, ""));
            }

            if ("雨水口".equals(type)) {
                viewHolder.field3.setVisibility(View.GONE);
            } else {
                viewHolder.field3.setVisibility(View.VISIBLE);
            }

            /**
             * 修改属性三
             */
            String field3 = "";
            if (data.getLayerName().equals("窨井")) {
                field3 = "井盖材质: " + String.valueOf(data.getGraphic().getAttributes().get(ComponentFieldKeyConstant.MATERIAL));
            } else if (data.getLayerName().equals("排水口")) {
                field3 = "排放去向: " + String.valueOf(data.getGraphic().getAttributes().get(ComponentFieldKeyConstant.RIVER));
            }
            viewHolder.field3.setText(field3);

            String address = String.valueOf(data.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR));
            viewHolder.addr.setText(StringUtil.getNotNullString(address, ""));

            if (!"(空)".equals(data.getErrorInfo()) && !TextUtils.isEmpty(data.getErrorInfo())){
                viewHolder.tv_errorinfo.setText(data.getErrorInfo());
                viewHolder.tv_errorinfo.setVisibility(View.VISIBLE);
            }

            String parentOrg = String.valueOf(data.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OWNERDEPT));
            viewHolder.tv_parent_org_name.setText("权属单位：" + StringUtil.getNotNullString(parentOrg, ""));

            viewHolder.ll_result_item_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(view, position, getItemId(position));
                    }
                }
            });

        }
    }


    protected static class ComponentListViewHolder extends BaseRecyclerViewHolder {
        TextView title;
        TextView date;
        TextView type;
        TextView sort;
        TextView subtype;
        TextView field1;
        TextView field2;
        TextView field3;
        TextView tv_errorinfo;
        TextView tv_parent_org_name;
        TextView addr;
        LinearLayout ll_result_item_root;
        //  CardView cardView;

        public ComponentListViewHolder(View itemView) {
            super(itemView);
            ll_result_item_root = findView(com.augurit.agmobile.patrolcore.R.id.ll_result_item_root);
            // cardView = findView(R.id.cardview);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            sort = (TextView) itemView.findViewById(R.id.sort);
            subtype = (TextView) itemView.findViewById(R.id.subtype);
            field1 = (TextView) itemView.findViewById(R.id.field1);
            field2 = (TextView) itemView.findViewById(R.id.field2);
            field3 = (TextView) itemView.findViewById(R.id.field3);
            addr = (TextView) itemView.findViewById(R.id.addr);
//            addr.setVisibility(View.GONE);
            tv_errorinfo = (TextView) itemView.findViewById(R.id.tv_errorinfo);
            tv_parent_org_name = (TextView) itemView.findViewById(R.id.tv_parent_org_name);
        }
    }
}
