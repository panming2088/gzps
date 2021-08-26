package com.augurit.agmobile.gzps.statistic.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.statistic.model.SignInfo;
import com.augurit.agmobile.gzps.statistic.service.SignStatisticService;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liangsh on 2017/11/8.
 */

public class SignTodayAndYesActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TextView tvNoData;
    private ProgressDialog progressDialog;
    private String org_name;
    private SignStatisticService mSignStatisticService;
    private MyAdapter myAdapter;
    private TextView mSignInfo;
    private int type;
    private TextView tvTitle;
    private boolean roleType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_sign_detail);
        org_name = getIntent().getStringExtra("org_name");
        type = getIntent().getIntExtra("type",1);
        roleType = getIntent().getBooleanExtra("roleType",false);
        initView();
        getSignInfo();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.install_rv);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_title);
        if(type==1){
            tvTitle.setText("今日签到名录");
        }else{
            tvTitle.setText("昨日签到名录");
        }
        mSignInfo = (TextView) findViewById(R.id.sign_info);
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        progressDialog = new ProgressDialog(this);
        mSignStatisticService = new SignStatisticService(this);
        progressDialog.setMessage("加载中...");
    }

    /**
     * 获取签到信息
     */
    private void getSignInfo() {
        String str = "";

        Calendar calendar = Calendar.getInstance();

        if(type == 1){
        }else{
            calendar.add(Calendar.DATE,   -1);   //获取昨天日期
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String monthStr = "" + month;
        String dayStr = "" + day;
        if(month < 10){
            monthStr = "0" + monthStr;
        }
        if(day < 10){
            dayStr = "0" + dayStr;
        }
        str = year + monthStr + dayStr;
        progressDialog.show();
        mSignStatisticService.getSignInfo(org_name,str,roleType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignInfo>() {
                    @Override
                    public void onCompleted() {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.iconLongToast(SignTodayAndYesActivity.this, R.mipmap.ic_alert_yellow, "获取签到信息失败");
                    }

                    @Override
                    public void onNext(SignInfo signBean) {
                        if(signBean == null){
                            ToastUtil.iconLongToast(SignTodayAndYesActivity.this, R.mipmap.ic_alert_yellow, "获取签到信息失败");
                            return;
                        }
//                        mSignInfo.setText("总人数："+signBean.getTotal()+"    签到数："+signBean.getSignedTotal());

                        if(signBean.getUsers()==null){
                            mSignInfo.setText("一共有：0条记录");
                            return;
                        }
                        sortList(signBean.getUsers());
                    }
                });
    }

    private void sortList(List<SignInfo.UsersEntity> users) {
        List<SignInfo.UsersEntity> Signs = new ArrayList<>();
        for(SignInfo.UsersEntity usersEntity:users){
            if(usersEntity.getSigned()){
                Signs.add(usersEntity);
            }
        }
        mSignInfo.setText("一共有："+Signs.size()+"条记录");
        myAdapter = new MyAdapter(Signs,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(myAdapter);
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        List<SignInfo.UsersEntity> datas;
        Context context;

        public MyAdapter(List<SignInfo.UsersEntity> installUsers, Context context) {
            this.datas = installUsers;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.install_user_item2, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (!TextUtils.isEmpty(datas.get(position).getUserName())) {
                holder.userName.setText(datas.get(position).getUserName());
            }
            if (!TextUtils.isEmpty(datas.get(position).getDirectOrgName())) {
                holder.org.setText(datas.get(position).getDirectOrgName());
            }
            if (!TextUtils.isEmpty(datas.get(position).getTitle())) {
                holder.job.setText(datas.get(position).getTitle());
            } else {
                holder.job.setText("");
            }
            if (!TextUtils.isEmpty(datas.get(position).getPhone())) {
                holder.userPhone.setText(datas.get(position).getPhone());
            } else {
                holder.userPhone.setText("");
            }
            if (datas.get(position).getSigned()) {
                holder.isInstall.setText("已签到");
                holder.isInstall.setTextColor(getResources().getColor(R.color.color_already_installed));
            } else {
                holder.isInstall.setText("未签到");
                holder.isInstall.setTextColor(Color.RED);
            }
            holder.userPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + datas.get(position).getPhone()));
                    startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size() == 0 ? 0 : datas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView userName;
            TextView userPhone;
            TextView isInstall;
            TextView job;
            TextView org;

            public MyViewHolder(View itemView) {
                super(itemView);
                userName = (TextView) itemView.findViewById(R.id.username);
                userPhone = (TextView) itemView.findViewById(R.id.userphone);
                isInstall = (TextView) itemView.findViewById(R.id.isinstall);
                job = (TextView) itemView.findViewById(R.id.job);
                org = (TextView) itemView.findViewById(R.id.org);
            }
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
