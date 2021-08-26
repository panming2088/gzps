package com.augurit.agmobile.gzps.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.LoginActivity;
import com.augurit.agmobile.gzps.PatrolMainActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.setting.view.LegalNoticeDialog2;
import com.augurit.agmobile.gzps.track.view.TrackRecordActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.MyDoubtListActivity;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.fragment
 * @createTime 创建时间 ：2017-03-10
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-10
 * @modifyMemo 修改备注：
 */

public class SettingFragment extends Fragment {

    private List<String> mMineItemNameList = new ArrayList<>();
    private Context mContext;

    public SettingFragment() {
    }

    public static SettingFragment newInstance(String text) {
        Bundle args = new Bundle();
        SettingFragment sampleFragment = new SettingFragment();
        sampleFragment.setArguments(args);

        return sampleFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine, null);

        LoginService loginService = new LoginService(getActivity(), AMDatabase.getInstance());
        String userName = loginService.getUser().getUserName();

        TextView tv_userName = (TextView) view.findViewById(R.id.mine_tv_userName);
        tv_userName.setText(userName);

        /**
         * 我的排水设施
         */
        ViewGroup rl_my_drainage_facility = (ViewGroup) view.findViewById(R.id.rl_my_drainage_facility);
        rl_my_drainage_facility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // startActivity(new Intent(getActivity(), LocalProblemActivity.class));
            }
        });

        /**
         * 我的查漏补缺
         */
        ViewGroup rl_my_leak_filling = (ViewGroup) view.findViewById(R.id.rl_my_leak_filling);
        rl_my_leak_filling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  startActivity(new Intent(getActivity(), LocalProblemActivity.class));
            }
        });

        /**
         * 我的数据修正
         */
        ViewGroup rl_my_correct_data = (ViewGroup) view.findViewById(R.id.rl_my_correct_data);
        rl_my_correct_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   startActivity(new Intent(getActivity(), OpinionTemplateListActivity.class));
            }
        });

        /**
         * 我的问题上报
         */
        ViewGroup rl_my_uploaded_problems = (ViewGroup) view.findViewById(R.id.rl_my_uploaded_problems);
        rl_my_uploaded_problems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   startActivity(new Intent(getActivity(), OpinionTemplateListActivity.class));
            }
        });

        /**
         * 退出帐号
         */
        ViewGroup system_exit = (ViewGroup) view.findViewById(R.id.system_exit);
        system_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确定切换帐号？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();*/
                android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(getActivity())
                        .setTitle("提示:")
                        .setMessage("确定退出登陆？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RongIM.getInstance().disconnect();
                                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                                ((PatrolMainActivity) getActivity()).finishSelf();
                            }
                        })
                        .create();
                dialog.show();
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextSize(20);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(20);
            }
        });

        /**
         * 我的设施上报统计
         */
        ViewGroup rl_my_upload_statisc = (ViewGroup) view.findViewById(R.id.rl_my_upload_statisc);

        rl_my_upload_statisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyUploadStatisticActivity.class));
            }
        });

        /**
         * 我的质疑
         */
        ViewGroup rl_my_question = (ViewGroup) view.findViewById(R.id.rl_my_question);
        rl_my_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyDoubtListActivity.class));
            }
        });

        /**
         * 我的系统设置
         */
        ViewGroup rl_my_system_setting = (ViewGroup) view.findViewById(R.id.rl_my_system_setting);
        rl_my_system_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SystemSettingActivity.class));
            }
        });
        /**
         * 我的签到
         */
        ViewGroup ll_my_signin = (ViewGroup) view.findViewById(R.id.ll_my_signin);
        ll_my_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (true) {
//                    ToastUtil.shortToast(getContext(), "功能建设中");
//                    return;
//                }

                Intent intent = new Intent(getActivity(), SignActivity.class);
                startActivity(intent);
                // intent.putExtra(WebViewConstant.WEBVIEW_URL_PATH, WebViewConstant.H5_URLS.MY_SIGN_IN_URL);
                // intent.putExtra(WebViewConstant.WEBVIEW_TITLE, "我的签到");
                // startActivity(new Intent(getActivity(), LocalProblemActivity.class));
            }
        });

        /**
         * 我的轨迹
         */
        ViewGroup ll_my_track = (ViewGroup) view.findViewById(R.id.ll_my_track);
        ll_my_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (true) {
                    ToastUtil.shortToast(getContext(), "功能建设中");
                    return;
                }

                Intent intent = new Intent(getActivity(), TrackRecordActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 个人资料
         */
        ViewGroup ll_usr_info = (ViewGroup) view.findViewById(R.id.rl_user_info_setting);
        ll_usr_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.legal_notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LegalNoticeDialog2 legalNoticeDialog = new LegalNoticeDialog2();
                legalNoticeDialog.setCancelable(true);
                legalNoticeDialog.show(getActivity().getSupportFragmentManager(), "legalNoticeDialog2");
            }
        });
        return view;
    }

}
