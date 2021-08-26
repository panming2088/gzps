package com.augurit.agmobile.gzps.journal;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.FlexRadioGroup;
import com.augurit.agmobile.gzps.journal.adapter.JournalsListPagerAdapter;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.publicaffair.view.condition.EventAffairConditionEvent;
import com.augurit.agmobile.gzps.publicaffair.view.condition.EventAffairConditionView;
import com.augurit.agmobile.gzps.publicaffair.view.condition.FacilityAffairFilterConditionEvent;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.widget.popupview.util.DensityUtils;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.google.android.flexbox.FlexboxLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Date;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.journal
 * @createTime 创建时间 ：2018-07-04
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-07-04
 * @modifyMemo 修改备注：
 */

public class JournalsDetailListActivity extends BaseActivity implements IDrawerController, View.OnClickListener, Animation.AnimationListener {

    private ViewGroup progress_linearlayout;
    private DrawerLayout drawer_layout;
    private ImageView iv_open_search;
    private View ll_facility_condition;
    private View ll_event_condition;
    private JournalsListPagerAdapter adapter;
    private PopupWindow popupWindow;

    private FlexRadioGroup fbDistrict, fbUploadType, fbFacilityType;
    private Animation animCollapse, animExpand;

    private SparseBooleanArray isCollapses; //是否收缩

    private Drawable dropUp, dropDown;

    private int currentAnimId;//当前正在执行动画的ID

    private boolean mProtectFromCheckedChange = false;


    /**
     * 设施类型：窨井，雨水口等
     */
    private String facilityType = null;

    /**
     * 上报类型：设施纠错还是设施上报
     */
    private String uploadType = null;

    /**
     * 筛选的区域
     */
    private String district = null;

    private Long startDate = null;

    private Long endDate = null;

    private static final int START_DATE = 1;
    private static final int END_DATE = 2;
    private Component component;


    private String[] districtConditions = {"全部", "天河", "番禺", "黄埔", "白云",
            "南沙", "海珠", "荔湾", "花都", "越秀",
            "增城", "从化", "净水公司"};

    public static final String[] uploadTypeConditions = {"全部", "数据新增", "数据校核"};

    private String[] facilityTypeConditions = {"全部", "窨井", "雨水口", "排水口"};

    private Calendar cal;
    private Button btn_end_date;
    private Long TempEndDate = null;
    private Button btn_start_date;
    private ViewGroup ll_date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        EventBus.getDefault().register(this);
//            findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
//            iv_open_search = ((ImageView) findViewById(R.id.iv_open_search));
//            iv_open_search.setImageResource(R.mipmap.ic_filter);
//            ((TextView) findViewById(R.id.tv_search)).setText("筛选");
//            findViewById(R.id.tv_search).setVisibility(View.VISIBLE);
//            findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    openDrawer(null);
//                }
//            });
        component = (Component) getIntent().getSerializableExtra("component");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("巡检列表");
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        adapter = new JournalsListPagerAdapter(getSupportFragmentManager(),
                this , component);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ll_facility_condition.setVisibility(View.GONE);
                    ll_event_condition.setVisibility(View.VISIBLE);
//                    iv_open_search.setVisibility(View.GONE);
                } else {
                    ll_facility_condition.setVisibility(View.VISIBLE);
                    ll_event_condition.setVisibility(View.GONE);
//                    iv_open_search.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);

        //TabLayout
        final TabLayout tabLayout = (TabLayout) findViewById(com.augurit.agmobile.gzps.R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);

        progress_linearlayout = (ViewGroup) findViewById(R.id.progress_linearlayout);
        ll_facility_condition = findViewById(R.id.ll_facility_condition);
        ll_event_condition = findViewById(R.id.ll_event_condition);
        ll_date = (ViewGroup) findViewById(R.id.ll_date);

        fbDistrict = (FlexRadioGroup) findViewById(R.id.fbl_filter_price);
        fbUploadType = (FlexRadioGroup) findViewById(R.id.fbl_filter_type);
        fbFacilityType = (FlexRadioGroup) findViewById(R.id.fbl_filter_structure);
        // text = (TextView) findViewById(R.id.text);

        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

        isCollapses = new SparseBooleanArray();


        FacilityAffairService facilityAffairService = new FacilityAffairService(this.getApplicationContext());
        boolean b = facilityAffairService.ifCurrentUserBelongToCityUser();
        if (b) {
            createRadioButton(districtConditions, fbDistrict);
        } else {
            String[] district = new String[]{facilityAffairService.getParentOrgOfCurrentUser()};
            createRadioButton(district, fbDistrict);
        }
        createRadioButton(uploadTypeConditions, fbUploadType);
        createRadioButton(facilityTypeConditions, fbFacilityType);

        findViewById(R.id.tv_price).setOnClickListener(this);
        findViewById(R.id.tv_type).setOnClickListener(this);
        findViewById(R.id.tv_structure).setOnClickListener(this);
        findViewById(R.id.tv_date).setOnClickListener(this);

        animExpand = AnimationUtils.loadAnimation(this, R.anim.expand);
        animExpand.setAnimationListener(this);
        animExpand.setFillAfter(true);

        animCollapse = AnimationUtils.loadAnimation(this, R.anim.collapse);
        animCollapse.setAnimationListener(this);
        animCollapse.setFillAfter(true);

        /***
         * 日期选择
         */
        btn_start_date = (Button) findViewById(R.id.btn_start_date);
        initStartDate();

        btn_end_date = (Button) findViewById(R.id.btn_end_date);
        initEndDate();
        btn_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate == null) {
                    showDatePickerDialog(btn_start_date, cal, START_DATE);
                } else {
                    cal.setTimeInMillis(startDate);
                    showDatePickerDialog(btn_start_date, cal, START_DATE);
                }
            }
        });

        btn_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TempEndDate == null) {
                    showDatePickerDialog(btn_end_date, cal, END_DATE);
                } else {
                    cal.setTimeInMillis(TempEndDate);
                    showDatePickerDialog(btn_end_date, cal, END_DATE);
                }
            }
        });

        EventAffairConditionView eventAffairConditionView = new EventAffairConditionView(ll_event_condition, this);

    }

    private void initEndDate() {
        cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        int month = cal.get(Calendar.MONTH) + 1;   //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);

        btn_end_date.setText(year + "-" + month + "-" + day);
        endDate = new Date(year - 1900, month - 1, day + 1).getTime();
        TempEndDate = new Date(year - 1900, month - 1, day).getTime();
    }

    private void initStartDate() {
        startDate = new Date(2018 - 1900, 0, 1).getTime();
        btn_start_date.setText(2018 + "-" + 1 + "-" + 1);
    }


    public void showDatePickerDialog(final Button btn, Calendar calendar, final int type) {
        // Calendar 需要这样来得到
        // Calendar calendar = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(this,
                // 绑定监听器(How the parent is notified that the date is set.)
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        display(btn, year, monthOfYear, dayOfMonth);
                        if (type == START_DATE) {
                            startDate = new Date(year - 1900, monthOfYear, dayOfMonth).getTime();
                        } else {
                            endDate = new Date(year - 1900, monthOfYear, dayOfMonth + 1).getTime();
                            TempEndDate = new Date(year - 1900, monthOfYear, dayOfMonth).getTime();
                        }
                    }
                }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    /**
     * 设置日期
     */
    public void display(Button dateDisplay, int year,
                        int monthOfYear, int dayOfMonth) {
        dateDisplay.setText(new StringBuffer().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));
    }


    private void createRadioButton(String[] filters, final FlexRadioGroup group) {
        /**
         *  64dp菜单的边距{@link DrawerLayout#MIN_DRAWER_MARGIN}+10dp*2为菜单内部的padding=84dp
         */
        float margin = DensityUtils.dp2px(this, 60);
        float width = DensityUtils.getWidth(this);
        for (String filter : filters) {
            RadioButton rb = (RadioButton) getLayoutInflater().inflate(R.layout.item_radiobutton, null);
            rb.setText(filter);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams((int) (width - margin) / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
            rb.setLayoutParams(lp);
            group.addView(rb);
            if (filter.equals("全部") || filters.length == 1) {
                rb.setChecked(true);
            }

            /**
             * 下面两个监听器用于点击两次可以清除当前RadioButton的选中
             * 点击RadioButton后，{@link FlexRadioGroup#OnCheckedChangeListener}先回调，然后再回调{@link View#OnClickListener}
             * 如果当前的RadioButton已经被选中时，不会回调OnCheckedChangeListener方法，故判断没有回调该方法且当前RadioButton确实被选中时清除掉选中
             */
            group.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(@IdRes int checkedId) {
                    mProtectFromCheckedChange = true;
                }
            });
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mProtectFromCheckedChange && ((RadioButton) v).isChecked()) {
                        // group.clearCheck();
                    } else {
                        mProtectFromCheckedChange = false;
                    }
                }
            });
        }
        isCollapses.put(group.getId(), false);
    }

    @Override
    public void openDrawer(final OnDrawerOpenListener listener) {
        drawer_layout.openDrawer(Gravity.RIGHT);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                GravityCompat.END);    //解除锁定
        drawer_layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (listener != null) {
                    listener.onOpened(drawerView);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //设置右面的侧滑菜单只能通过编程来打开
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                        GravityCompat.END);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void closeDrawer() {

    }

    @Override
    public ViewGroup getDrawerContainer() {
        return progress_linearlayout;
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:
                fbDistrict.check(districtConditions[0]);
                fbUploadType.check(uploadTypeConditions[0]);
                fbFacilityType.check(facilityTypeConditions[0]);
                resetDate();
                break;
            case R.id.btn_submit:

                if (startDate > TempEndDate) {
                    ToastUtil.iconLongToast(this.getApplicationContext(), R.mipmap.ic_alert_yellow, "开始时间不能比结束时间大");
                    return;
                }

                drawer_layout.closeDrawers();

                RadioButton rbPrice = (RadioButton) findViewById(fbDistrict.getCheckedRadioButtonId());
                if (rbPrice != null) {
                    district = rbPrice.getText().toString();
                }

                RadioButton rbType = (RadioButton) findViewById(fbUploadType.getCheckedRadioButtonId());
                if (rbType != null) {
                    uploadType = rbType.getText().toString();
                }

                RadioButton rbStructure = (RadioButton) findViewById(fbFacilityType.getCheckedRadioButtonId());
                if (rbStructure != null) {
                    facilityType = rbStructure.getText().toString();
                }

                FacilityAffairFilterConditionEvent facilityAffairFilterConditionEvent =
                        new FacilityAffairFilterConditionEvent(district, uploadType, facilityType, startDate, endDate);
                EventBus.getDefault().post(facilityAffairFilterConditionEvent);
                break;
            case R.id.tv_type:
                startAnim(fbUploadType, (TextView) v);
                break;
            case R.id.tv_price:
                startAnim(fbDistrict, (TextView) v);
                break;
            case R.id.tv_structure:
                startAnim(fbFacilityType, (TextView) v);
                break;
            case R.id.tv_date:
                startAnim(ll_date, (TextView) v);
                break;
            default:
                break;
        }
    }


    /**
     * 设置箭头
     */
    private void setArrow(TextView view, boolean isCollapse) {
        if (!isCollapse) {
            if (dropUp == null) {
                dropUp = getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp);
                dropUp.setBounds(0, 0, dropUp.getMinimumWidth(), dropUp.getMinimumHeight());
            }
            view.setCompoundDrawables(null, null, dropUp, null);
        } else {
            if (dropDown == null) {
                dropDown = getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp);
                dropDown.setBounds(0, 0, dropDown.getMinimumWidth(), dropDown.getMinimumHeight());
            }
            view.setCompoundDrawables(null, null, dropDown, null);
        }


    }

    /**
     * 重新设置isCollapse值，保存当前动画状态
     * 启动动画
     *
     * @param group
     */
    private void startAnim(FlexRadioGroup group, TextView view) {
        currentAnimId = group.getId();
        boolean isCollapse = !isCollapses.get(group.getId());
        isCollapses.put(group.getId(), isCollapse);
        if (isCollapse) {
            group.startAnimation(animCollapse);
        } else {
            group.startAnimation(animExpand);
        }
        setArrow(view, isCollapse);
    }

    /**
     * 重新设置isCollapse值，保存当前动画状态
     * 启动动画
     *
     * @param group
     */
    private void startAnim(ViewGroup group, TextView view) {
        currentAnimId = group.getId();
        boolean isCollapse = !isCollapses.get(group.getId());
        isCollapses.put(group.getId(), isCollapse);
        if (isCollapse) {
            group.startAnimation(animCollapse);
        } else {
            group.startAnimation(animExpand);
        }
        setArrow(view, isCollapse);
    }

    /**
     * 重置时间
     */
    public void resetDate() {
        setStateDate();
        setCurrentDate();
    }

    @SuppressLint("WrongConstant")
    public void setStateDate() {
        cal = Calendar.getInstance();
        cal.add(Calendar.MONDAY, -2);   //获取前两个月
        int year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        int month = cal.get(Calendar.MONTH) + 1;   //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);
        startDate = new Date(year - 1900, month - 1, day).getTime();
        btn_start_date.setText(year + "-" + month + "-" + day);
    }

    public void setCurrentDate() {
        cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        int month = cal.get(Calendar.MONTH) + 1;   //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);
        btn_end_date.setText(year + "-" + month + "-" + day);
        endDate = new Date(year - 1900, month - 1, day + 1).getTime();
        TempEndDate = new Date(year - 1900, month - 1, day).getTime();
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
            drawer_layout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    public void onAnimationStart(Animation animation) {
        if (!isCollapses.get(currentAnimId)) {
            findViewById(currentAnimId).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (isCollapses.get(currentAnimId)) {
            findViewById(currentAnimId).setVisibility(View.GONE);
        }
    }


    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Subscribe
    public void onEventConditionChanged(EventAffairConditionEvent eventAffairConditionEvent) {
        drawer_layout.closeDrawers();
    }

    private void initPopupWindow() {
        popupWindow = new PopupWindow(this);
    }

}
