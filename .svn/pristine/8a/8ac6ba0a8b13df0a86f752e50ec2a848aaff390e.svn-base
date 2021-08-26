package com.augurit.agmobile.gzps.statistic.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.statistic.model.SignInfo;
import com.augurit.agmobile.gzps.statistic.service.SignStatisticService;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
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

public class SignYesterdayFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView tvNoData;
    private ProgressDialog progressDialog;
    private String org_name;
    private String isInstallStr;
    private boolean roleType;
    private Context mContext;
    private int year;
    private int month;
    private int day;
    private SignStatisticService mSignStatisticService;
    private MyAdapter myAdapter;
    private TextView mSignInfo;
    private boolean mRoleType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_sign_detail, null);
        org_name = ((Activity)mContext).getIntent().getStringExtra("org_name");
        initView(view);
        initTime();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSignInfo();
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,   -1);   //获取昨天日期
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.install_rv);
        mSignInfo = (TextView) view.findViewById(R.id.sign_info);
        tvNoData = (TextView) view.findViewById(R.id.tv_no_data);
        progressDialog = new ProgressDialog(mContext);
        mSignStatisticService = new SignStatisticService(mContext);
        progressDialog.setMessage("加载中...");
    }

    /**
     * 获取签到信息
     */
    private void getSignInfo() {
        String monthStr = "" + month;
        String dayStr = "" + day;
        if(month < 10){
            monthStr = "0" + monthStr;
        }
        if(day < 10){
            dayStr = "0" + dayStr;
        }
        mSignStatisticService.getSignInfo(org_name,year+""+monthStr+""+dayStr,getActivity().getIntent().getBooleanExtra("roleType",false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "获取签到信息失败");
                    }

                    @Override
                    public void onNext(SignInfo signBean) {
                        //                        if(signBean == null || signBean.getUsers() == null){
                        if(signBean == null){
                            ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "获取签到信息失败");
                            return;
                        }
                        mSignInfo.setText("总人数："+signBean.getTotal()+"    签到数："+signBean.getSignedTotal());
                        if(signBean.getUsers()==null) return;
                        sortList(signBean.getUsers());
                    }
                });
    }

    private void sortList(List<SignInfo.UsersEntity> users) {
        List<SignInfo.UsersEntity> unSigns = new ArrayList<>();
        List<SignInfo.UsersEntity> Signs = new ArrayList<>();
        for(SignInfo.UsersEntity usersEntity:users){
            if(usersEntity.getSigned()){
                Signs.add(usersEntity);
            }else{
                unSigns.add(usersEntity);
            }
        }
        unSigns.addAll(Signs);
        myAdapter = new MyAdapter(unSigns,mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
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
