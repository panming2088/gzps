package com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;
import com.augurit.am.fw.utils.StringUtil;

/**
 * 原设施信息视图生成Manager
 * Created by xucil on 2017/12/29.
 */

public class OriginalPipeViewManager {

    public OriginalPipeViewManager(Context context,
                                   ViewGroup tableItemContainer,
                                   PipeBean completeTableInfo) {
        tableItemContainer.removeAllViews();
        if (StringUtil.isEmpty(completeTableInfo.getOldPipeType()) || StringUtil.isEmpty(completeTableInfo.getOldDirection())) {
            return;
        }

        TextItemTableItem ssTv = new TextItemTableItem(context);
        ssTv.setTextViewName("管线类型");
        ssTv.setText(StringUtil.getNotNullString(completeTableInfo.getOldPipeType(),""));
        ssTv.setReadOnly();
        tableItemContainer.addView(ssTv);

        //管线流向
        String road = completeTableInfo.getOldDirection();
        TextItemTableItem roadTv = new TextItemTableItem(context);
        roadTv.setTextViewName("管线流向");
        road = replaceSpaceCharacter(road);
        roadTv.setText(StringUtil.getNotNullString(road, ""));
        roadTv.setReadOnly();
        tableItemContainer.addView(roadTv);

        TextView textView = new TextView(context);
        textView.setHeight(150);
        textView.setVisibility(View.INVISIBLE);
        tableItemContainer.addView(textView);
    }


    @Nullable
    private String replaceSpaceCharacter(String sort) {
        if (sort != null && TextUtils.isEmpty(sort.replace(" ", ""))) {
            sort = null;
        }
        return sort;
    }
}
