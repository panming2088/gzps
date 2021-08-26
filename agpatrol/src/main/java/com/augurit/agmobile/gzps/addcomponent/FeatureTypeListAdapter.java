package com.augurit.agmobile.gzps.addcomponent;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.map
 * @createTime 创建时间 ：2017-10-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-10-14
 * @modifyMemo 修改备注：
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;

import java.util.ArrayList;

/**
 * This class provides the adapter for the list of feature types.
 */
public class FeatureTypeListAdapter extends ArrayAdapter<FeatureTypeData> {

    public FeatureTypeListAdapter(Context context, ArrayList<FeatureTypeData> featureTypes) {
        super(context, 0, featureTypes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        FeatureTypeViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem, null);
            holder = new FeatureTypeViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.icon);
            holder.textView = (TextView) view.findViewById(R.id.label);
        } else {
            holder = (FeatureTypeViewHolder) view.getTag();
        }

        FeatureTypeData featureType = getItem(position);
        holder.imageView.setImageBitmap(featureType.getBitmap());
        holder.textView.setText(getItem(position).getName());
        view.setTag(holder);
        return view;
    }

    /**
     * Holds data related to an item in the list of feature types.
     */
    class FeatureTypeViewHolder {
        ImageView imageView;

        TextView textView;
    }

}

