package com.augurit.agmobile.gzps.uploadevent.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题类型，可多选
 *
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadevent.adapter
 * @createTime 创建时间 ：2017-12-11
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-12-11
 * @modifyMemo 修改备注：
 */

public class EventTypeAdapter extends BaseAdapter {

    private Context mContext;

    private List<DictionaryItem> mEventTypeList;

    private List<DictionaryItem> mSelectedEventTypeList;

    private boolean enable = true;

    private OnRecyclerItemClickListener<DictionaryItem> mOnItemClickListener;

    //  private int selectedPosition = -1;

    public EventTypeAdapter(Context context) {
        this.mContext = context;
        mSelectedEventTypeList = new ArrayList<>();
    }


    public void notifyDataSetChanged(List<DictionaryItem> eventTypeList) {
        this.mEventTypeList = eventTypeList;
        this.mSelectedEventTypeList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (ListUtil.isEmpty(mEventTypeList)) {
            return 0;
        }
        return mEventTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mEventTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DictionaryItem dictionaryItem = mEventTypeList.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.gridview_item_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_area_name);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_selected);
        tv.setText(dictionaryItem.getName());
//        tv.setBackground(mContext.getResources().getDrawable(R.drawable.corner_color_write));
//        tv.setTextColor(Color.BLACK);

        if (mSelectedEventTypeList.contains(dictionaryItem)) {
            tv.setBackground(mContext.getResources().getDrawable(R.drawable.corner_color_primary3));
            tv.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, null));
            iv.setVisibility(View.VISIBLE);
        } else {
            tv.setBackground(mContext.getResources().getDrawable(R.drawable.corner_color_write));
            tv.setTextColor(Color.BLACK);
            iv.setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!enable) {
                    return;
                }

                if (!checkSelection(dictionaryItem)) {
                    ToastUtil.shortToast(mContext, "类型冲突");
                    return;
                }
                if (mSelectedEventTypeList.contains(dictionaryItem)) {
                    mSelectedEventTypeList.remove(dictionaryItem);
                } else {
                    mSelectedEventTypeList.add(dictionaryItem);
                }
                notifyDataSetChanged();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position, dictionaryItem);
                }
            }
        });
        return view;
    }

    public List<DictionaryItem> getSelectedEventTypeList() {
        return mSelectedEventTypeList;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setSelectedEventTypes(String[] eventTypeCodes) {
        mSelectedEventTypeList.clear();
        if (ListUtil.isEmpty(mEventTypeList)) {
            return;
        }
        for (DictionaryItem dictionaryItem : mEventTypeList) {
            for (String code : eventTypeCodes) {
                if (dictionaryItem.getCode().equals(code)) {
                    mSelectedEventTypeList.add(dictionaryItem);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 检查所点击的项与已选项是否存在互斥关系
     * 缺失与异响、损坏 互斥；沉陷与凸起 互斥
     *
     * @return true则没有互斥关系，该项可被选中；false则有互斥关系，不可选
     */
    private boolean checkSelection(DictionaryItem selection) {
        boolean enable = true;
        if (ListUtil.isEmpty(mSelectedEventTypeList)) {
            return true;
        }
        String name = selection.getName();
//        if(name.contains("缺失")) {
//            for(DictionaryItem dictionaryItem : mSelectedEventTypeList) {
//                if(dictionaryItem.getName().contains("损坏")
//                        || dictionaryItem.getName().contains("异响")){
//                    enable = false;
//                }
//            }
//        }
//        if(name.contains("损坏")
//                || name.contains("异响")) {
//            for(DictionaryItem dictionaryItem : mSelectedEventTypeList) {
//                if(dictionaryItem.getName().contains("缺失")){
//                    enable = false;
//                }
//            }
//        }
//
//        if(name.contains("沉陷")) {
//            for(DictionaryItem dictionaryItem : mSelectedEventTypeList) {
//                if(dictionaryItem.getName().contains("凸起")){
//                    enable = false;
//                }
//            }
//        }
//
//        if(name.contains("凸起")) {
//            for(DictionaryItem dictionaryItem : mSelectedEventTypeList) {
//                if(dictionaryItem.getName().contains("沉陷")){
//                    enable = false;
//                }
//            }
//        }

        return enable;
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public List<DictionaryItem> getmSelectedEventTypeList() {
        return mSelectedEventTypeList;
    }

    public void setmSelectedEventTypeList(List<DictionaryItem> mSelectedEventTypeList) {
        this.mSelectedEventTypeList = mSelectedEventTypeList;
        notifyDataSetChanged();
    }

    /*
    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(null, selectedPosition, mSelectedEventTypeList.get(selectedPosition));
        }
    }
    */

}
