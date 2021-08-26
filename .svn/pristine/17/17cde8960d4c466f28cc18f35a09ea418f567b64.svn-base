package com.augurit.agmobile.gzps.jbjpsdy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.FlexRadioGroup;
import com.augurit.agmobile.gzps.uploadfacility.service.SearchRoadService;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.FacilityFilterCondition;
import com.augurit.am.cmpt.widget.popupview.util.DensityUtils;
import com.augurit.am.fw.utils.KeyBoardUtils;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.google.android.flexbox.FlexboxLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

/**
 * copy by
 * @see HookListFilterConditionView
 * 我的上报列表，关键节点的筛选条件
 * ps：因为关键节点没有“雨污类型”，也没有返回“地址”，编号也不支持模糊匹配，所以需要在这里进行特殊的修改
 *
 * @author 创建人 ：huangchongwu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps
 * @createTime 创建时间 ：2020/09/17
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：18/1/24
 * @modifyMemo 修改备注：
 */

public class KeyNodeInspectionWellHookListFilterConditionView extends HookListFilterConditionView {

    /**
     * @param context
     * @param filterTitle
     * @param filterListType 过滤的列表类型 ，取值为 ：{@link FacilityFilterCondition#NEW_ADDED_LIST}或者 {@link FacilityFilterCondition#MODIFIED_LIST}
     * @param viewGroup
     */
    public KeyNodeInspectionWellHookListFilterConditionView(Context context, String filterTitle, String filterListType, ViewGroup viewGroup) {
        super(context, filterTitle, filterListType, viewGroup);
    }

    @Override
    public View initView(String filterTitle) {
        View root = super.initView(filterTitle);

        // 隐藏“雨污类型”
        final View fragFacilityTypeRoot = root.findViewById(R.id.frg_facility_type_root);
        fragFacilityTypeRoot.setVisibility(View.GONE);

        // 隐藏地址搜索
        View llAddress = root.findViewById(R.id.ll_address);
        llAddress.setVisibility(View.GONE);

        // 上报编号修改提示
        ((EditText) root.findViewById(R.id.et_mark_id)).setHint("不超过10个字符");
        return root;
    }

}
