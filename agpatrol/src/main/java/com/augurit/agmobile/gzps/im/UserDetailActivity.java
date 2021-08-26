package com.augurit.agmobile.gzps.im;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.im.bean.FriendItem;
import com.augurit.agmobile.gzps.im.bean.UserInfoBean;
import com.augurit.am.fw.utils.JsonUtil;

import org.json.JSONObject;

import io.rong.callkit.RongCallAction;
import io.rong.callkit.RongVoIPIntent;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
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

public class UserDetailActivity extends BaseActivity {

    private FriendItem mFriend;
    private TextView user_name, tv_title, user_phone, ogr_name;
    private String type;
    private TextView phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        initView();
        initData();
    }

    private void initView() {

        user_name = (TextView) findViewById(R.id.user_name);
        tv_title = (TextView) findViewById(R.id.user_detail_tv_title);
        user_phone = (TextView) findViewById(R.id.user_phone);
        ogr_name = (TextView) findViewById(R.id.ogr_name);
        user_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mFriend.getPhone())) {
                    return;
                }
                Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + mFriend.getPhone()));
                startActivity(intent);

            }
        });

    }

    private void initData() {

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        mFriend = (FriendItem) intent.getSerializableExtra("friend");
        if (TextUtils.isEmpty(mFriend.getOrgName())) {
            getUserInfoByUserId(mFriend.getId());
        } else {
            setUserInfo(mFriend);
        }

    }

    private void getUserInfoByUserId(final String userId) {
        Observable.create(new Observable.OnSubscribe<FriendItem>() {
            @Override
            public void call(Subscriber<? super FriendItem> subscriber) {
                String url = "http://" + LoginConstant.SERVER_URL + LoginConstant.USER_BY_ID_URL + userId;
                OkHttpClient client = new OkHttpClient.Builder().build();
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
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
                setUserInfo(userInfoBean);
            }
        });

    }

    private void setUserInfo(FriendItem friend) {
        //用户姓名
        if (!TextUtils.isEmpty(friend.getUserName()) && !TextUtils.isEmpty(friend.getTitle())) {
            user_name.setText(friend.getUserName() + friend.getTitle());
        } else if (!TextUtils.isEmpty(friend.getUserName()) && TextUtils.isEmpty(friend.getTitle())) {
            user_name.setText(friend.getUserName());
        }

        //机构名称
        if (!TextUtils.isEmpty(friend.getOrgName())) {
            ogr_name.setText("机构：" + friend.getOrgName());
        } else {
            ogr_name.setText("机构：");
        }
        if (!TextUtils.isEmpty(friend.getPhone())) {
            user_phone.setText("电话：" + friend.getPhone());
        } else {
            user_phone.setText("电话：");
        }

        //标题信息
        if (type.equals("friend_type")) {
            tv_title.setText("用户详情");
        } else {
            tv_title.setText("群组信息");
        }
    }

    public void userPageBack(View view) {
        finish();
    }

    public void startChat(View view) {
        if (!TextUtils.isEmpty(mFriend.getUserName()) && !TextUtils.isEmpty(mFriend.getTitle())) {
            String displayName = mFriend.getUserName() + "(" + mFriend.getTitle() + ")";
            RongIM.getInstance().startPrivateChat(UserDetailActivity.this, mFriend.getId(),
                    displayName);
        } else {
            RongIM.getInstance().startPrivateChat(UserDetailActivity.this, mFriend.getId(), mFriend
                    .getId());
        }
        finish();
    }

    //CallKit start 2
    public void startVoice(View view) {
        //        if (true) {
        //            RongCallKit.startSingleCall(UserDetailActivity.this, mFriend.getId(), RongCallKit
        //                    .CallMediaType.CALL_MEDIA_TYPE_AUDIO);
        //            return;
        //        }
        RongCallSession profile = RongCallClient.getInstance().getCallSession();
        if (profile != null && profile.getActiveTime() > 0) {
            Toast.makeText(UserDetailActivity.this,
                    profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
                            getString(R.string.rc_voip_call_audio_start_fail) :
                            getString(R.string.rc_voip_call_video_start_fail),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(UserDetailActivity.this, getString(R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO);
        intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase());
        intent.putExtra("targetId", mFriend.getId());
        intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage(getPackageName());
        getApplicationContext().startActivity(intent);
    }

    public void startVideo(View view) {

        RongCallSession profile = RongCallClient.getInstance().getCallSession();
        if (profile != null && profile.getActiveTime() > 0) {
            Toast.makeText(UserDetailActivity.this,
                    profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
                            getString(R.string.rc_voip_call_audio_start_fail) :
                            getString(R.string.rc_voip_call_video_start_fail),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(UserDetailActivity.this, getString(R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO);
        intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase());
        intent.putExtra("targetId", mFriend.getId());
        intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage(getPackageName());
        getApplicationContext().startActivity(intent);
    }
    //CallKit end 2
}
