package com.augurit.agmobile.gzps.im;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.im.bean.FriendItem;
import com.augurit.agmobile.gzps.im.bean.GroupUserInfo;
import com.augurit.agmobile.gzps.im.bean.UserInfoBean;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：taoerxiang
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.communication
 * @createTime 创建时间 ：2017-11-04
 * @modifyBy 修改人 ：taoerxiang
 * @modifyTime 修改时间 ：2017-11-04
 * @modifyMemo 修改备注：
 */

public class ConversationActivity extends FragmentActivity implements RongIM
        .ConversationBehaviorListener {
    private String title;
    private Conversation.ConversationType mConversationType;
    private Button back_left;
    private Button btn_right;
    private TextView title_tv;
    private Intent intent;
    private String targetId;
    private ArrayList<UserInfo> list = new ArrayList<>();
    private TextView online_state_tv;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        intent = getIntent();
        initView();
        initData();
        getUserStateOnline();
        RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
            @Override
            public void getGroupMembers(String groupId, RongIM.IGroupMemberCallback callback) {
                getMembers(groupId, callback);
                //callback.onGetGroupMembersResult(list); // 调用 callback 的 onGetGroupMembersResult
                // 回传群组信息
            }
        });

    }

    private void getUserStateOnline() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String url = "http://" + LoginConstant.SUPPORT_URL + "/rest/onlineStatus/" + targetId;
                OkHttpClient client = new OkHttpClient.Builder().build();
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        if (result.equals("null")) {
                            online_state_tv.setText("离线");
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");
                        subscriber.onNext(status);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                if (s.equals("0")) {
                    online_state_tv.setText("在线");
                } else {
                    online_state_tv.setText("离线");
                }

            }
        });

    }

    private void getMembers(final String groupId, final RongIM.IGroupMemberCallback callback) {
        Observable.create(new Observable.OnSubscribe<GroupUserInfo>() {
            @Override
            public void call(Subscriber<? super GroupUserInfo> subscriber) {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
                String url = "http://" + LoginConstant.SERVER_URL + "/rest/system/getUsersByGroupId/" + groupId;
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        GroupUserInfo groupUserInfo = JsonUtil.getObject(result, GroupUserInfo.class);
                        subscriber.onNext(groupUserInfo);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<GroupUserInfo>() {

            @Override

            public void call(GroupUserInfo groupUserInfo) {
                ArrayList<FriendItem> userList = groupUserInfo.getResult().getUserList();
                for (FriendItem friend : userList) {
                    UserInfo userInfo = new UserInfo(friend.getLoginName(), friend.getUserName(),
                            Uri.parse(friend.getPortraitUri()));
                    list.add(userInfo);
                }
                callback.onGetGroupMembersResult(list);
            }
        });
    }

    private void initData() {
        targetId = intent.getData().getQueryParameter("targetId");
        title = intent.getData().getQueryParameter("title");
        setActionBarTitle(mConversationType, targetId);
        RongIM.setConversationBehaviorListener(this);
    }

    private void initView() {
        progressDialog = new ProgressDialog(ConversationActivity.this);
        progressDialog.setMessage("加载中");
        back_left = (Button) findViewById(R.id.left_btn);
        btn_right = (Button) findViewById(R.id.right_btn);
        title_tv = (TextView) findViewById(R.id.title_tv);
        online_state_tv = (TextView) findViewById(R.id.online_state_tv);
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.US));
        if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
            online_state_tv.setVisibility(View.GONE);
            btn_right.setBackground(getResources().getDrawable(R.drawable.icon2_menu));
            btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    enterSettingActivity();
                }
            });
        } else if (mConversationType.equals(Conversation.ConversationType.PRIVATE)) {
            online_state_tv.setVisibility(View.VISIBLE);
            btn_right.setBackground(getResources().getDrawable(R.drawable.icon1_menu));
            btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    getUserInfoById(targetId);
                    //                    Intent intent = new Intent(ConversationActivity.this, UserDetailActivity.class);
                    //                    //intent.putExtra("type", CLICK_CONTACT_FRAGMENT_FRIEND);
                    //                    FriendItem friendItem = new FriendItem(targetId,title);
                    //                    intent.putExtra("friend", friendItem);
                    //                    intent.putExtra("type", "friend_type");
                    //                    startActivity(intent);
                }
            });
        }

    }

    private void getUserInfoById(final String userId) {

        Observable.create(new Observable.OnSubscribe<FriendItem>() {
            @Override
            public void call(Subscriber<? super FriendItem> subscriber) {
                String url = "http://" + LoginConstant.SERVER_URL + LoginConstant.USER_BY_ID_URL
                        + userId + "/" + BaseInfoManager.getUserId(ConversationActivity.this);
                OkHttpClient client = new OkHttpClient.Builder().build();
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        JSONObject object = new JSONObject(string);
                        String data = object.getString("result");
                        if (data == null || data.equals("null")) {
                            return;
                        }
                        UserInfoBean user = JsonUtil.getObject(string, UserInfoBean.class);
                        FriendItem friendItem = user.getResult();
                        subscriber.onNext(friendItem);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<FriendItem>() {
            @Override
            public void call(FriendItem userInfoBean) {
                showUserInfoDialog(userInfoBean);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showUserInfoDialog(final FriendItem userInfoBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ConversationActivity.this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(ConversationActivity.this, R.layout.user_info_dialog, null);
        TextView userName = (TextView) view.findViewById(R.id.dialog_username);
        TextView phone = (TextView) view.findViewById(R.id.dialog_phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userInfoBean.getPhone()) || userInfoBean.getPhone().contains
                        ("*")) {
                    return;
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" +
                        userInfoBean.getPhone()));
                startActivity(intent);

            }
        });

        TextView orgName = (TextView) view.findViewById(R.id.dialog_orgname);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        if (!TextUtils.isEmpty(userInfoBean.getUserName())) {
            userName.setText(userInfoBean.getUserName());
        }
        if (!TextUtils.isEmpty(userInfoBean.getPhone())) {
            phone.setText(userInfoBean.getPhone());
        }
        if (!TextUtils.isEmpty(userInfoBean.getOrgName())) {
            orgName.setText(userInfoBean.getOrgName());
        }
        if (!TextUtils.isEmpty(userInfoBean.getTitle())) {
            title.setText(userInfoBean.getTitle());
        }
        dialog.setView(view);
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.user_dialog_bg);
        WindowManager.LayoutParams params = window.getAttributes();
        WindowManager windowManager = getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        params.width = (int) (d.getWidth() * 0.8);
        window.setAttributes(params);
    }

    /**
     * 设置会话页面 Title
     *
     * @param conversationType 会话类型
     * @param targetId         目标 Id
     */
    private void setActionBarTitle(Conversation.ConversationType conversationType, String targetId) {

        if (conversationType == null)
            return;
        if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
            setPrivateActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            setGroupActionBar(targetId);
        } else {
            title_tv.setText("聊天");
        }

    }

    private void setGroupActionBar(String targetId) {
        if (!TextUtils.isEmpty(title)) {
            title_tv.setText(title);
        } else {
            title_tv.setText(targetId);
        }
    }

    /**
     * 设置私聊界面 ActionBar
     */
    private void setPrivateActionBar(String targetId) {
        if (!TextUtils.isEmpty(title)) {
            title_tv.setText(title);
        } else {
            if (!TextUtils.isEmpty(targetId)) {
                UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
                if (!TextUtils.isEmpty(userInfo.getName())) {
                    title_tv.setText(userInfo.getName());
                } else {
                    title_tv.setText(targetId);
                }
            }
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    /**
     * 根据 targetid 和 ConversationType 进入到设置页面
     */
    private void enterSettingActivity() {

        if (mConversationType == Conversation.ConversationType.PUBLIC_SERVICE
                || mConversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {

            RongIM.getInstance().startPublicServiceProfile(this, mConversationType, targetId);
        } else {
            UriFragment fragment = (UriFragment) getSupportFragmentManager().getFragments().get(0);
            //得到讨论组的 targetId
            targetId = fragment.getUri().getQueryParameter("targetId");

            if (TextUtils.isEmpty(targetId)) {
                Toast.makeText(ConversationActivity.this, "讨论组尚未创建成功", Toast.LENGTH_SHORT).show();
            }

            Intent intent = null;
            if (mConversationType == Conversation.ConversationType.GROUP) {
                intent = new Intent(this, GroupDetailActivity.class);
                intent.putExtra("conversationType", Conversation.ConversationType.GROUP);
            } else {
                intent = new Intent(this, UserDetailActivity.class);
                intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE);
            }
            intent.putExtra("TargetId", targetId);
            if (intent != null) {
                startActivityForResult(intent, 500);
            }

        }
    }

    public void conversationBack(View view) {
        finish();
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        FriendItem friendItem = new FriendItem(userInfo.getUserId(), userInfo.getName());
        //群组里面点击用户
        if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            if (userInfo.getUserId().equals(BaseInfoManager.getUserId(context))) {
                ToastUtil.shortToast(context, "不能和自己聊天");
                return true;
            }
            if (!TextUtils.isEmpty(friendItem.getUserName())) {
                String displayName = friendItem.getUserName();
                RongIM.getInstance().startPrivateChat(ConversationActivity.this, friendItem.getId(), displayName);
            } else {
                RongIM.getInstance().startPrivateChat(ConversationActivity.this, friendItem.getId(), friendItem
                        .getId());
            }
        } else {
            //单聊点击用户
            getUserInfoById(userInfo.getUserId());
        }
        //        Intent intent = new Intent(context, UserDetailActivity.class);
        //        //intent.putExtra("type", CLICK_CONTACT_FRAGMENT_FRIEND);
        //        FriendItem friendItem = new FriendItem(userInfo.getLoginName(),userInfo.getName());
        //        intent.putExtra("friend", friendItem);
        //        intent.putExtra("type", "friend_type");
        //        context.startActivity(intent);
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }
}
