package com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist;

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
import com.augurit.am.cmpt.widget.popupview.util.DensityUtils;
import com.augurit.am.fw.utils.KeyBoardUtils;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.google.android.flexbox.FlexboxLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

/**
 * 我的上报列表筛选条件
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist
 * @createTime 创建时间 ：18/1/24
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：18/1/24
 * @modifyMemo 修改备注：
 */

public class PipeListFilterConditionView {


    private static final int START_DATE = 1;
    private static final int END_DATE = 2;

    private Context mContext;
    private Calendar cal;
    private Button btnStartDate;
    private Button btnEndDate;
    private Long startDate;
    private Long endDate;
    private Long TempEndDate;

    private String[] facilityTypeConditions = {"全部", "雨污合流", "污水", "雨水"};
    private String[] flowConditions = {"全部", "正向", "反向"};
    private View usidContainer;
    private EditText autoRoad;
    private EditText etAddress;
    private EditText etId;
    private SearchRoadService searchRoadService;
    private EditText etUsid;
    private String mFilterListType;

    /**
     *
     * @param context
     * @param filterTitle
     * @param filterListType 过滤的列表类型 ，取值为 ：{@link FacilityFilterCondition#NEW_ADDED_LIST}或者 {@link FacilityFilterCondition#MODIFIED_LIST}
     * @param viewGroup
     */
    public PipeListFilterConditionView(Context context, String filterTitle, String filterListType , ViewGroup viewGroup) {
        this.mContext = context;
        this.mFilterListType = filterListType;
        View view = initView(filterTitle);
        if (viewGroup != null) {
            viewGroup.removeAllViews();
            viewGroup.addView(view);
        }
    }

    private View initView(String filterTitle) {
        final View root = View.inflate(mContext, R.layout.view_pipe_history_filter, null);

        TextView tvFilterTitle = (TextView) root.findViewById(R.id.tv_filter_title);
        tvFilterTitle.setText(filterTitle);

        btnStartDate = (Button) root.findViewById(R.id.btn_start_date);
        initStartDate();
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDate == null) {
                    showDatePickerDialog(btnStartDate, cal, START_DATE);
                } else {
                    cal.setTimeInMillis(startDate);
                    showDatePickerDialog(btnStartDate, cal, START_DATE);
                }
            }

        });
        btnEndDate = (Button) root.findViewById(R.id.btn_end_date);
        initEndDate();
        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TempEndDate == null) {
                    showDatePickerDialog(btnEndDate, cal, END_DATE);
                } else {
                    cal.setTimeInMillis(TempEndDate);
                    showDatePickerDialog(btnEndDate, cal, END_DATE);
                }
            }

        });

        /**
         * 管线类型筛选
         */
        final FlexRadioGroup frg_facility_type = (FlexRadioGroup) root.findViewById(R.id.frg_pipe_type);
        usidContainer = root.findViewById(R.id.ll_usid_container);
        etUsid = (EditText) root.findViewById(R.id.et_usid);
        setMaxLength(etUsid,10);
        createFacilityRadioButtons(frg_facility_type);

        /**
         * 管线类型筛选
         */
        final FlexRadioGroup frg_flow_type = (FlexRadioGroup) root.findViewById(R.id.frg_flow_type);
        createFlowRadioButtons(frg_flow_type);


        /**
         * 重置
         */
        root.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDate();
                autoRoad.setText("");
                etAddress.setText("");
                etId.setText("");
                etUsid.setText("");
                frg_facility_type.check("全部");
                usidContainer.setVisibility(View.GONE);
            }
        });

        /**
         * 提交
         */
        root.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (startDate > TempEndDate) {
                    ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "开始时间不能比结束时间大");
                    return;
                }
                //隐藏键盘
                KeyBoardUtils.closeKeybord(etAddress, mContext);

                String type = null;
                RadioButton rbFacilityTYpe = (RadioButton) root.findViewById(frg_facility_type.getCheckedRadioButtonId());
                if (rbFacilityTYpe != null) {
                    type = rbFacilityTYpe.getText().toString();
                }

                String flow = null;
                RadioButton rbFlowType = (RadioButton) root.findViewById(frg_flow_type.getCheckedRadioButtonId());
                if (rbFlowType != null) {
                    flow = rbFlowType.getText().toString();
                }

                FacilityFilterCondition facilityFilterCondition = new FacilityFilterCondition(
                        mFilterListType,
                        type,
                        startDate,
                        endDate,
                        flow,
                        autoRoad.getText().toString(),
                        etAddress.getText().toString(),
                        etId.getText().toString()
                );
                EventBus.getDefault().post(facilityFilterCondition);
            }
        });

        /**
         * 道路
         */
        autoRoad = (EditText) root.findViewById(R.id.auto_road);
        setMaxLength(autoRoad,10);
//        autoRoad.setThreshold(1);
//        RxTextView
//                .textChanges(autoRoad)
//                .filter(new Func1<CharSequence, Boolean>() { // 过滤，把输入字符串长度为0时过滤掉
//                    @Override
//                    public Boolean call(CharSequence charSequence) {
//                        return charSequence.toString().trim().length() > 0;
//                    }
//                })
//                .debounce(500, TimeUnit.MILLISECONDS)
//                .flatMap(new Func1<CharSequence, Observable<List<String>>>() {
//                    @Override
//                    public Observable<List<String>> call(CharSequence charSequence) {
//                        initSearchRoadService();
//                        return searchRoadService.getRoads(charSequence.toString());
//                    }
//                })
//                .subscribe(new Subscriber<List<String>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(List<String> strings) {
//                        if (!ListUtil.isEmpty(strings)) {
//                            ArrayAdapter arr_adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, strings);
//                            autoRoad.setAdapter(arr_adapter);
//                            autoRoad.showDropDown();
//                        }
//                    }
//                });


        /**
         * 地址
         */
        etAddress = (EditText) root.findViewById(R.id.et_address);
        setMaxLength(etAddress,10);
        /**
         * 上报编号
         */
        etId = (EditText) root.findViewById(R.id.et_mark_id);
        setMaxLength(etId,10);

        return root;
    }


    public void setMaxLength(EditText editText, final int maxLength){
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void initSearchRoadService() {
        if (searchRoadService == null) {
            searchRoadService = new SearchRoadService(mContext);
        }

    }


    /**
     * 日期选择
     *
     * @param btn
     * @param calendar
     * @param type
     */
    public void showDatePickerDialog(final Button btn, final Calendar calendar, final int type) {
        new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        showTimePicker(btn, year, monthOfYear, dayOfMonth, calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE), type);
                    }
                }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * 时间选择
     *
     * @param button
     * @param year
     * @param month
     * @param day
     * @param hourOfDay
     * @param minute
     * @param type
     */
    private void showTimePicker(final Button button,
                                final int year,
                                final int month,
                                final int day,
                                int hourOfDay,
                                int minute,
                                final int type) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if (type == START_DATE) {
                    cal.set(year, month, day, hourOfDay, minute, 0);
                    startDate = cal.getTimeInMillis();
                    button.setText(TimeUtil.getStringTimeYMDFromDate(new Date(startDate)));
                } else {
                    cal.set(year, month, day, hourOfDay, minute, 60);
                    endDate = cal.getTimeInMillis();
                    cal.set(year, month, day, hourOfDay, minute, 0);
                    TempEndDate = cal.getTimeInMillis();
                    button.setText(TimeUtil.getStringTimeYMDFromDate(new Date(TempEndDate)));

                    if (startDate > TempEndDate) {
                        ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "开始时间不能比结束时间大");
                    }
                }

            }
        }, hourOfDay, minute, true);
        timePickerDialog.show();
    }


    /**
     * 初始化结束时间
     */
    private void initEndDate() {
        cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        int month = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minuter = cal.get(Calendar.MINUTE);

        TempEndDate = cal.getTimeInMillis();

        cal.set(year, month, day, hour, minuter, 60);
        endDate = cal.getTimeInMillis();
        btnEndDate.setText(TimeUtil.getStringTimeYMDFromDate(new Date(TempEndDate)));
    }

    /**
     * 初始化开始时间
     */
    private void initStartDate() {
        cal = Calendar.getInstance();
        cal.set(2019, 0, 1, 0, 0, 0);
        startDate = cal.getTimeInMillis();
        btnStartDate.setText(TimeUtil.getStringTimeYMDFromDate(new Date(startDate)));
    }


    /**
     * 重置时间
     */
    public void resetDate() {
        initEndDate();
        initStartDate();
    }

    /**
     * 设施类型筛选
     *
     * @param group
     */
    private void createFacilityRadioButtons(final FlexRadioGroup group) {
        float margin = DensityUtils.dp2px(mContext, 60);
        float width = DensityUtils.getWidth(mContext);
        for (String filter : facilityTypeConditions) {
            final RadioButton rb = (RadioButton) View.inflate(mContext, R.layout.item_radiobutton, null);
            rb.setText(filter);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams((int) (width - margin) / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
            rb.setLayoutParams(lp);
            group.addView(rb);
            if (filter.equals("全部") || facilityTypeConditions.length == 1) {
                rb.setChecked(true);
            }

            /**
             * 点击RadioButton后，{@link FlexRadioGroup#OnCheckedChangeListener}先回调，然后再回调{@link View#OnClickListener}
             * 如果当前的RadioButton已经被选中时，不会回调OnCheckedChangeListener方法
             */
            group.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(@IdRes int checkedId) {

                }
            });
//            rb.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    /**
//                     * 如果是窨井,显示usid
//                     */
//                    if (rb.getText().equals(facilityTypeConditions[1])) {
//                        usidContainer.setVisibility(View.VISIBLE);
//                    } else {
//                        usidContainer.setVisibility(View.GONE);
//                    }
//                }
//            });
        }
    }

    /**
     * 设施类型筛选
     *
     * @param group
     */
    private void createFlowRadioButtons(final FlexRadioGroup group) {
        float margin = DensityUtils.dp2px(mContext, 60);
        float width = DensityUtils.getWidth(mContext);
        for (String filter : flowConditions) {
            final RadioButton rb = (RadioButton) View.inflate(mContext, R.layout.item_radiobutton, null);
            rb.setText(filter);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams((int) (width - margin) / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
            rb.setLayoutParams(lp);
            group.addView(rb);
            if (filter.equals("全部") || flowConditions.length == 1) {
                rb.setChecked(true);
            }

            /**
             * 点击RadioButton后，{@link FlexRadioGroup#OnCheckedChangeListener}先回调，然后再回调{@link View#OnClickListener}
             * 如果当前的RadioButton已经被选中时，不会回调OnCheckedChangeListener方法
             */
            group.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(@IdRes int checkedId) {

                }
            });
//            rb.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    /**
//                     * 如果是窨井,显示usid
//                     */
//                    if (rb.getText().equals(flowConditions[1])) {
//                        usidContainer.setVisibility(View.VISIBLE);
//                    } else {
//                        usidContainer.setVisibility(View.GONE);
//                    }
//                }
//            });
        }
    }
}
