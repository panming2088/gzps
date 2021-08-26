package com.augurit.agmobile.gzps.im;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.im.adapter.MyFriendListAdapter;
import com.augurit.agmobile.gzps.im.bean.FriendInfo;
import com.augurit.agmobile.gzps.im.bean.FriendItem;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.Callback;
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
 * @createTime 创建时间 ：2017-11-03
 * @modifyBy 修改人 ：taoerxiang
 * @modifyTime 修改时间 ：2017-11-03
 * @modifyMemo 修改备注：
 */

public class FriendListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecycleView;
    private TextView no_friend_tv;
    private String userId;
    private ArrayList<FriendItem> friendList;
    private EditText et_name;
    private EditText et_area;
    private ArrayList<FriendItem> filterList = new ArrayList<>();
    private ArrayList<FriendItem> allFriend = new ArrayList<>();
    private Button search;
    private MyFriendListAdapter adapter;
    private TextView friendCount;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragent_friends, null);
        mContext = getContext();
        initView(view);

        return view;
    }

    private void initView(View view) {
        mRecycleView = (RecyclerView) view.findViewById(R.id.friendList_rv);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        swipeRefreshLayout.setOnRefreshListener(this);
        // 设置进度圈的背景色
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        no_friend_tv = (TextView) view.findViewById(R.id.show_no_friend);
        et_name = (EditText) view.findViewById(R.id.et_filter_name);
        et_area = (EditText) view.findViewById(R.id.et_filter_area);
        search = (Button) view.findViewById(R.id.start_search);
        userId = BaseInfoManager.getUserId(getContext());
        friendCount = (TextView) view.findViewById(R.id.firend_count);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String currentUserOrg = BaseInfoManager.getUserOrg(getActivity());
        initData();
        initListener();
    }

    private void initListener() {
        //        et_name.addTextChangedListener(new TextWatcherAdapter() {
        //            @Override
        //            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //                super.beforeTextChanged(s, start, count, after);
        //            }
        //
        //            @Override
        //            public void onTextChanged(CharSequence s, int start, int before, int count) {
        //                String inputText = s.toString();
        //                for (int i = 0; i < friendList.size(); i++) {
        //                    if (friendList.get(i).getLoginName().contains(inputText)) {
        //                        filterList.add(friendList.get(i));
        //                    }
        //                }
        //
        //            }
        //
        //            @Override
        //            public void afterTextChanged(Editable s) {
        //                super.afterTextChanged(s);
        //
        //            }
        //        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                String area = et_area.getText().toString().trim();
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(area)) {
                    progressDialog.show();
                    initData();
                    //                    filterList.clear();
                    //                    filterList.addAll(friendList);
                    //                    adapter.notifyDataSetChanged();
                } else {
                    if (friendList != null) {
                        startFilter(name, area);
                    }
                }

            }
        });
    }

    private void startFilter(String name, String area) {
        //名字为空 区划不为空
        if (TextUtils.isEmpty(name) && !TextUtils.isEmpty(area)) {
            filterList.clear();
            for (int i = 0; i < friendList.size(); i++) {
                if ((friendList.get(i).getTitle().indexOf(area)) != -1) {
                    filterList.add(friendList.get(i));
                }
            }
            if (filterList.size() > 0) {
                adapter.notifyDataSetChanged();

            } else {
                ToastUtil.shortToast(getActivity(), "没有匹配结果");
            }

        }
        //名字不为空 区划为空
        if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(area)) {
            filterList.clear();
            for (int i = 0; i < friendList.size(); i++) {
                if ((friendList.get(i).getUserName().indexOf(name)) != -1) {
                    filterList.add(friendList.get(i));
                }
            }
            if (filterList.size() > 0) {

                adapter.notifyDataSetChanged();

            } else {
                ToastUtil.shortToast(getActivity(), "没有匹配结果");
            }
        }
        //名字、区划都不为空
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(area)) {
            filterList.clear();
            for (int i = 0; i < friendList.size(); i++) {
                if ((friendList.get(i).getUserName().contains(name)) && (friendList.get(i)
                        .getTitle().contains(area))) {

                    filterList.add(friendList.get(i));
                }
            }
            if (filterList.size() > 0) {
                adapter.notifyDataSetChanged();

            } else {
                ToastUtil.shortToast(getActivity(), "没有匹配结果");
            }
        }
    }

    private void initData() {

        Observable.create(new Observable.OnSubscribe<FriendInfo>() {
            @Override
            public void call(final Subscriber<? super FriendInfo> subscriber) {

                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit
                        .SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
                String url = "http://" + LoginConstant.SERVER_URL + LoginConstant.FRIEND_URL + userId;
                Request request = new Request.Builder().url(url).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                       try{
                       if (response.isSuccessful()) {
                            String result = response.body().string();
                            FriendInfo friendInfo = JsonUtil.getObject(result, FriendInfo.class);
                            subscriber.onNext(friendInfo);
                        }
                       }catch (Exception e){
                           e.printStackTrace();
                       }
                    }
                });

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<FriendInfo>() {

            @Override

            public void call(FriendInfo friendInfo) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (friendInfo.getResult() == null
                        || ListUtil.isEmpty(friendInfo.getResult().getUserList())) {
                    no_friend_tv.setVisibility(View.VISIBLE);
                    return;
                }

                swipeRefreshLayout.setRefreshing(false);
                friendList = friendInfo.getResult().getUserList();
                if (filterList.size() > 0) {
                    filterList.clear();
                }
                filterList.addAll(friendList);
                adapter = new MyFriendListAdapter(getActivity(), filterList, friendCount, no_friend_tv);
                adapter.setOnItemClickListener(new MyFriendListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        startFriendDetailsPage(filterList.get(position));
                    }
                });
                mRecycleView.setAdapter(adapter);
            }
        });
    }

    private void startFriendDetailsPage(FriendItem friendItem) {
        //        Intent intent = new Intent(getActivity(), UserDetailActivity.class);
        //        intent.putExtra("type", "friend_type");
        //        intent.putExtra("friend", friendItem);
        //        startActivity(intent);
        if (friendItem.getId().equals(BaseInfoManager.getUserId(mContext))) {
            ToastUtil.shortToast(mContext, "不能和自己聊天");
            return;
        }
        if (!TextUtils.isEmpty(friendItem.getUserName()) && !TextUtils.isEmpty(friendItem.getTitle())) {
            String displayName = friendItem.getUserName() + "(" + friendItem.getTitle() + ")";
            RongIM.getInstance().startPrivateChat(getContext(), friendItem.getId(),
                    displayName);
        } else {
            RongIM.getInstance().startPrivateChat(getContext(), friendItem.getId(), friendItem
                    .getId());
        }
    }

    @Override
    public void onRefresh() {
        initData();
        // 关闭加载进度条
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
}
