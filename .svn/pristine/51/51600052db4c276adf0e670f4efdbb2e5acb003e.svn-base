package com.augurit.agmobile.gzps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;

import java.util.List;

/**
 * Created by xiaoyu on 2018/3/12.
 */

public class FacilityFeedBackAdapter extends BaseListAdapter<String> {
    public FacilityFeedBackAdapter(Context context, List<String> data) {
        super(context, data);

    }

    /**
     * 用来传递整改情况给Activity
     * @return
     */
    public String getSelectString() {
        if (selectItem == -1)
            return " ";
        return data.get(selectItem);
    }

    public void setSelectItem(String name) {
        for (int i = 0; i <= data.size(); i++) {
            if (data.get(i).equals(name)) {
                selectItem = i;
                return;
            }
        }
    }

    boolean enableCheck = true;

    public void setCheck(boolean enableCheck) {
        this.enableCheck = enableCheck;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    int selectItem = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String contents = data.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context,
                    R.layout.gridview_item_layout, null);
            holder.tv_area_name = (TextView) convertView.findViewById(R.id.tv_area_name);
            holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_area_name.setText(contents);
        if (selectItem == position) {
            holder.tv_area_name.setBackground(context.getResources().getDrawable(R.drawable.corner_color_primary3));
            holder.tv_area_name.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null));
            holder.iv_selected.setVisibility(View.VISIBLE);
        } else {
            holder.tv_area_name.setBackground(context.getResources().getDrawable(R.drawable.corner_color_write));
            holder.tv_area_name.setTextColor(Color.BLACK);
            holder.iv_selected.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!enableCheck)
                    return;
                selectItem = position;
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_area_name;
        ImageView iv_selected;
    }

}
