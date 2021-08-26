package com.augurit.agmobile.gzps.im;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.im.adapter.MyGroupListAdapter;
import com.augurit.agmobile.gzps.im.bean.FriendItem;
import com.augurit.agmobile.gzps.im.bean.GroupUserInfo;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
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
 * @package 包名 ：com.augurit.agmobile.gzps.im
 * @createTime 创建时间 ：2017-11-09
 * @modifyBy 修改人 ：taoerxiang
 * @modifyTime 修改时间 ：2017-11-09
 * @modifyMemo 修改备注：
 */

public class GroupDetailActivity extends BaseActivity {

    private String fromConversationId;
    private Conversation.ConversationType mConversationType;
    private boolean isFromConversation;
    private RecyclerView mRecyclerView;
    private TextView mTextViewMemberSize;
    private TextView title;
    private ArrayList<FriendItem> userList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);

        initView();
    }

    private void initView() {
        //mGridView = (DemoGridView) findViewById(R.id.gridview);
        mRecyclerView = (RecyclerView) findViewById(R.id.group_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(GroupDetailActivity.this));
        title = (TextView) findViewById(R.id.group_title);
        //mTextViewMemberSize = (TextView) findViewById(R.id.group_member_size);
        fromConversationId = getIntent().getStringExtra("TargetId");
        mConversationType = (Conversation.ConversationType) getIntent().getSerializableExtra("conversationType");
        progressDialog = new ProgressDialog(GroupDetailActivity.this);
        progressDialog.setMessage("加载中...");
        if (!TextUtils.isEmpty(fromConversationId)) {
            isFromConversation = true;
        }
        if (isFromConversation) {//群组会话页进入
            progressDialog.show();
            getGroupMembers();
        }

    }

    private void getGroupMembers() {
        Observable.create(new Observable.OnSubscribe<GroupUserInfo>() {
            @Override
            public void call(Subscriber<? super GroupUserInfo> subscriber) {

                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
                String url = "http://" + LoginConstant.SERVER_URL +
                        LoginConstant.USER_BY_GROUPID +
                        fromConversationId + "/" + BaseInfoManager.getUserId(GroupDetailActivity.this);
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
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
                if (groupUserInfo.getResult() == null) {
                    ToastUtil.longToast(GroupDetailActivity.this, "无效的群组 请在会话页面长按移除！");
                    return;
                }
                userList = groupUserInfo.getResult().getUserList();

                title.setText("群组信息" + "(" + userList.size() + "人)");
                //mTextViewMemberSize.setText("全部群成员" + "(" + userList.size() + ")");
                MyGroupListAdapter myGroupListAdapter = new MyGroupListAdapter(GroupDetailActivity.this,
                        userList);
                mRecyclerView.setAdapter(myGroupListAdapter);
                myGroupListAdapter.setOnItemClickListener(new MyGroupListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        startFriendDetailsPage(userList.get(position));
                    }
                });
            }
        });

    }

    private void startFriendDetailsPage(FriendItem friendItem) {
        //        Intent intent = new Intent(GroupDetailActivity.this, UserDetailActivity.class);
        //        intent.putExtra("type", "friend_type");
        //        intent.putExtra("friend", friendItem);
        //        startActivity(intent);
        if (friendItem.getId().equals(BaseInfoManager.getUserId(GroupDetailActivity.this))) {
            ToastUtil.shortToast(GroupDetailActivity.this, "不能和自己聊天");
            return;
        }
        if (!TextUtils.isEmpty(friendItem.getUserName()) && !TextUtils.isEmpty(friendItem.getTitle())) {
            String displayName = friendItem.getUserName() + "(" + friendItem.getTitle() + ")";
            RongIM.getInstance().startPrivateChat(GroupDetailActivity.this, friendItem.getId(),
                    displayName);
        } else {
            RongIM.getInstance().startPrivateChat(GroupDetailActivity.this, friendItem.getId(),
                    friendItem
                            .getId());
        }
    }

    public void conversationBack(View view) {
        finish();
    }

}

