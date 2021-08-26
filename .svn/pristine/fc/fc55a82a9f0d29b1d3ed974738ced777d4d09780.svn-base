package com.augurit.agmobile.patrolcore.layer.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.patrolcore.R;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xcl on 2017/10/19.
 */
public class PatrolParentViewHolder2 {

    private OnParentViewHolderClickListener onParentViewHolderClickListener;

    private Context mContext;
    private ImageView iv_map;

    public PatrolParentViewHolder2(Context context, OnParentViewHolderClickListener onParentViewHolderClickListener) {
        this.mContext = context;
        this.onParentViewHolderClickListener = onParentViewHolderClickListener;
    }


    public View createNodeView(final Map<String, List<LayerInfo>> value) {
        final View view = View.inflate(mContext, R.layout.view_parent_holder, null);
        TextView tv_layer_name = (TextView) view.findViewById(R.id.tv_layer_name);
        iv_map = (ImageView) view.findViewById(R.id.iv_map);
        final Set<Map.Entry<String, List<LayerInfo>>> entries = value.entrySet();
        for (Map.Entry<String, List<LayerInfo>> entry : entries) {
            tv_layer_name.setText(entry.getKey());
            List<LayerInfo> value1 = entry.getValue();
            for (LayerInfo info : value1){
                if (info.isDefaultVisible()){
                    //如果是一进来就选中的底图，那么高亮
                    iv_map.setBackgroundResource(R.drawable.iv_border_blue);
                    break;
                }
            }
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_map.setBackgroundResource(R.drawable.iv_border_blue);
                if (onParentViewHolderClickListener != null) {
                    for (Map.Entry<String, List<LayerInfo>> entry : entries) {
                        onParentViewHolderClickListener.onClick(entry.getKey(),entry.getValue());
                    }
                }
            }
        });

        if (tv_layer_name.getText().toString().equals("天地图")){
            //如果是天地图
            iv_map.setImageResource(R.mipmap.iv_simple_map);
        }else if (tv_layer_name.getText().toString().contains("影像")){
            //如果是影像地图
            iv_map.setImageResource(R.mipmap.iv_yingxiang);
        }else if (tv_layer_name.getText().toString().contains("广州")){
            //如果是广州地图
            iv_map.setImageResource(R.mipmap.iv_guangzhou_map);
        }


        return view;
    }


    public void clearBorder(){
        iv_map.setBackgroundResource(R.drawable.iv_border_grey);
    }

    public void showrBorder(){
        iv_map.setBackgroundResource(R.drawable.iv_border_blue);
    }

    public interface OnParentViewHolderClickListener {
        void onClick(String groupName,List<LayerInfo> layerInfos);
    }
}

