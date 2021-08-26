package com.augurit.agmobile.gzps.im;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.R;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * @author 创建人 ：taoerxiang
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.communication
 * @createTime 创建时间 ：2017-11-04
 * @modifyBy 修改人 ：taoerxiang
 * @modifyTime 修改时间 ：2017-11-04
 * @modifyMemo 修改备注：
 */

public class CommunicationFragment extends Fragment {

    private TabLayout imTabs;
    private ViewPager imVp;
    private String[] tabTitles = {"会话", "通讯录", "工作组"};
    private ConversationListFragment mConversationListFragment = null;
    private List<Fragment> fragments = new ArrayList<>();
    private String userId;
    private String userName;
    private ProgressDialog progressDialog;
    private String imToken;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communication, null);
        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imToken = BaseInfoManager.getImToken(getActivity());
        userId = BaseInfoManager.getUserId(getActivity());
        userName = BaseInfoManager.getUserName(getActivity());
        if (TextUtils.isEmpty(imToken)) {
            Toast.makeText(getActivity(), "token无效", Toast.LENGTH_SHORT).show();
        } else {
            connect(imToken);
        }

    }

    private void initView(View view) {
        view.findViewById(R.id.ll_back).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tv_title)).setText("通讯");
        imTabs = (TabLayout) view.findViewById(R.id.im_tabs);
        imVp = (ViewPager) view.findViewById(R.id.im_vp);
        //        progressDialog = new ProgressDialog(getActivity());
        //        progressDialog.setMessage("数据初始化...");
        //        progressDialog.setCancelable(true);
    }

    /**
     * 连接融云服务器
     *
     * @param token 身份令牌
     */
    private void connect(final String token) {
        //progressDialog.show();
        if (getActivity().getApplicationInfo().packageName.equals(getCurProcessName(getActivity()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onTokenIncorrect() {
                    ToastUtil.shortToast(getActivity(), "连接失败 正在尝试重新链接...");
                    //connect(imToken);

                }

                @Override
                public void onSuccess(String userid) {
                    //progressDialog.dismiss();
                    initTabs();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    //progressDialog.dismiss();
                    Toast.makeText(getActivity(), "连接通讯服务器失败--" + errorCode.getValue(), Toast
                            .LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initTabs() {

        for (int i = 0; i < tabTitles.length; i++) {

            imTabs.addTab(imTabs.newTab().setText(tabTitles[i]));
        }
        Fragment conversationList = initConversationList();
        fragments.add(conversationList);
        fragments.add(new FriendListFragment());
        fragments.add(new WorkGroupFragment());
        MyImPagerAdapter adapter = new MyImPagerAdapter(getFragmentManager());
        imVp.setAdapter(adapter);
        imTabs.setupWithViewPager(imVp);
        imTabs.setTabsFromPagerAdapter(adapter);//给Tabs设置适配器
    }

    private Fragment initConversationList() {

        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();

            Uri uri;
            uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                    .build();
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    public class MyImPagerAdapter extends FragmentPagerAdapter {

        public MyImPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
