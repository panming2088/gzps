package com.augurit.agmobile.gzps.common.selectcomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;

/**
 * Created by long on 2017/10/18.
 */

public class LimitedLayerAdapter extends BaseAdapter {

    private int selectedIndex = 0;
    private Context context;

    private OnRecyclerItemClickListener<String> onRecyclerItemClickListener;

    public LimitedLayerAdapter(Context context){
        this.context = context;
    }

    public void notifyDataSetChanged(int selectedIndex){
        this.selectedIndex = selectedIndex;
        notifyDataSetChanged();
    }

    /**
     * 强制只返回“窨井”，“雨水口”，“排水口”
     * @return
     */
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return LayerUrlConstant.newComponentUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layer_item, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(LayerUrlConstant.componentNames[position]);
        if(selectedIndex == position){
            name.setBackgroundColor(context.getResources().getColor(R.color.agmobile_blue_dark));
        } else {
            name.setBackgroundColor(context.getResources().getColor(R.color.agmobile_white));
        }
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = position;
                notifyDataSetChanged();
                if(onRecyclerItemClickListener != null){
                    onRecyclerItemClickListener.onItemClick(v, position, LayerUrlConstant.newComponentUrls[position]);
                }
            }
        });
         return convertView;
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener<String> onItemClickListener){
        this.onRecyclerItemClickListener = onItemClickListener;
    }
}
