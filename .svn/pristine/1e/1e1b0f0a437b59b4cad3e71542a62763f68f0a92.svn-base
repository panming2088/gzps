package com.augurit.agmobile.gzps.statistic.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.statistic.model.InstallInfo;
import com.augurit.am.fw.utils.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AppInstallDetailActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TextView tvTitle, tvNoData;
    private ProgressDialog progressDialog;
    private String org_name;
    private String isInstallStr;
    private boolean roleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_install_detail);

        initView();
        Intent intent = getIntent();
        org_name = intent.getStringExtra("org_name");
        roleType = intent.getBooleanExtra("roleType", false);
        if (org_name.equals("市净水公司")) {
            org_name = "净水有限公司";
        }else if(org_name.equals("市排水公司")){
            org_name = "排水有限公司";
        }else if(org_name.equals("番禺污水公司")){
            org_name = "番禺";
        }else if(org_name.equals("科学城排水公司")){
            org_name = "黄埔";
        }else{
            org_name = org_name.replace("排水公司","");
        }
        isInstallStr = intent.getStringExtra("inInstall");
        if (TextUtils.isEmpty(isInstallStr)) {
            //柱状图点击
            String url = "http://" + LoginConstant.GZPS_AGSUPPORT +
                    "/rest/installRecord/StatisticalAppInOrg?org_name=" + org_name + "&roleType="
                    + roleType;
            getAppInstallInfo(url);
        } else {
            //安装数字点就点击
            boolean isInstalled = Boolean.parseBoolean(isInstallStr);
            String url = "http://" + LoginConstant.GZPS_AGSUPPORT +
                    "/rest/installRecord/StatisticalAppGetUsers?org_name=" +
                    org_name + "&isInstalled=" + isInstalled + "&roleType=" + roleType;
            getAppInstallInfo(url);
        }

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
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        progressDialog = new ProgressDialog(AppInstallDetailActivity.this);
        progressDialog.setMessage("加载中...");

    }

    private void getAppInstallInfo(final String url) {
        progressDialog.show();
        Observable.create(new Observable.OnSubscribe<ArrayList<InstallInfo.InstallUser>>() {
            @Override
            public void call(Subscriber<? super ArrayList<InstallInfo.InstallUser>> subscriber) {

                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit
                        .SECONDS).build();
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        InstallInfo installInfo = JsonUtil.getObject(response.body().string(), InstallInfo.class);
                        if (installInfo.getCode() == 200) {
                            ArrayList<InstallInfo.InstallUser> users = installInfo.getData().getUsers();
                            subscriber.onNext(users);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribe(new Action1<ArrayList<InstallInfo.InstallUser>>() {
            @Override
            public void call(ArrayList<InstallInfo.InstallUser> installUsers) {
                setViewData(installUsers);
            }
        });
    }

    private void setViewData(ArrayList<InstallInfo.InstallUser> installUsers) {
        if (installUsers == null) {
            tvNoData.setVisibility(View.VISIBLE);
            return;
        }
        MyAdapter myAdapter = new MyAdapter(installUsers, AppInstallDetailActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AppInstallDetailActivity.this));
        mRecyclerView.setAdapter(myAdapter);

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<InstallInfo.InstallUser> datas;
        Context context;

        public MyAdapter(ArrayList<InstallInfo.InstallUser> installUsers, Context context) {
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
            if (!TextUtils.isEmpty(datas.get(position).getUser_name())) {
                holder.userName.setText(datas.get(position).getUser_name());
            }
            if (!TextUtils.isEmpty(datas.get(position).getDirec_org())) {
                holder.org.setText(datas.get(position).getDirec_org());
            }
            if (!TextUtils.isEmpty(datas.get(position).getJob())) {
                holder.job.setText(datas.get(position).getJob());
            } else {
                holder.job.setText("");
            }
            if (!TextUtils.isEmpty(datas.get(position).getPhone())) {
                holder.userPhone.setText(datas.get(position).getPhone());
            } else {
                holder.userPhone.setText("");
            }
            if (datas.get(position).isInstalled()) {
                holder.isInstall.setText("已安装");
                holder.isInstall.setTextColor(getResources().getColor(R.color.color_already_installed));
            } else {
                holder.isInstall.setText("未安装");
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
            if (roleType) {
                if(org_name.contains("净水")){
                    tvTitle.setText("市净水公司管理层(" + datas.size() + "人)");
                }else {
                    tvTitle.setText(org_name + "管理层(" + datas.size() + "人)");
                }
            } else {
                if(org_name.contains("净水")){
                    tvTitle.setText("市净水公司一线人员(" + datas.size() + "人)");
                }else {
                    tvTitle.setText(org_name + "一线人员(" + datas.size() + "人)");
                }
            }

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
