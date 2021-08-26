package com.augurit.agmobile.gzps.im;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.im.bean.GroupInfo;
import com.augurit.agmobile.gzps.im.widget.SelectableRoundedImageView;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;
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

public class WorkGroupFragment extends Fragment {

    private ListView mGroupListView;
    private GroupAdapter adapter;
    private TextView noGroupTv;
    private String userId;
    private ArrayList<GroupInfo.GroupItem> groupList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragent_workgroup, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mGroupListView = (ListView) view.findViewById(R.id.group_listview);
        noGroupTv = (TextView) view.findViewById(R.id.no_group_tv);
        userId = BaseInfoManager.getUserId(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        Observable.create(new Observable.OnSubscribe<GroupInfo>() {
            @Override
            public void call(Subscriber<? super GroupInfo> subscriber) {

                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
                String url = "http://" + LoginConstant.SERVER_URL + LoginConstant.GROUP_URL + userId;
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        GroupInfo groupInfo = JsonUtil.getObject(result, GroupInfo.class);
                        subscriber.onNext(groupInfo);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<GroupInfo>() {

            @Override
            public void call(GroupInfo groupInfo) {
                groupList = groupInfo.getResult().getGroupList();
                if(groupList.size()==0){
                    noGroupTv.setVisibility(View.VISIBLE);
                    return;
                }
                adapter = new GroupAdapter(getActivity(), groupList);
                mGroupListView.setAdapter(adapter);
            }
        });

        mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupInfo.GroupItem groupItem = groupList.get(position);
                if (TextUtils.isEmpty(groupItem.getGroupId()) || TextUtils.isEmpty(groupItem.getGroupName())) {
                    return;
                }
                RongIM.getInstance().startGroupChat(getActivity(), groupItem.getGroupId(), groupItem.getGroupName());
            }
        });

    }

    class GroupAdapter extends BaseAdapter {

        private Context context;

        private ArrayList<GroupInfo.GroupItem> list;

        public GroupAdapter(Context context, ArrayList<GroupInfo.GroupItem> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null && list.size() != 0) {
                return list.size();
            } else {

                return 0;
            }

        }

        @Override
        public Object getItem(int position) {
            if (list == null)
                return null;

            if (position >= list.size())
                return null;

            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            GroupInfo.GroupItem groupItem = list.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.group_item_new, parent, false);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.groupname);
                //viewHolder.mImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.groupuri);
                viewHolder.add_count = (TextView) convertView.findViewById(R.id.add_count);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(groupItem.getGroupName());
            viewHolder.add_count.setText(groupItem.getSize() + "人加入");
            //String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(mContent);
            //ImageLoader.getInstance().displayImage(portraitUri, viewHolder.mImageView, App.getOptions());
            //            if (context.getSharedPreferences("config", MODE_PRIVATE).getBoolean("isDebug", false)) {
            //                viewHolder.groupId.setVisibility(View.VISIBLE);
            //                viewHolder.groupId.setText(mContent.getGroupsId());
            //            }
            return convertView;
        }

        class ViewHolder {
            /**
             * 昵称
             */
            TextView tvTitle;
            /**
             * 头像
             */
            SelectableRoundedImageView mImageView;
            /**
             * userId
             */
            TextView add_count;
        }
    }
}
