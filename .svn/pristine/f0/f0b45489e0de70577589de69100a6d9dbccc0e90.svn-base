package com.augurit.am.cmpt.login.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.login.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 包名：com.augurit.am.serv.agcom.login.adapter
 * 文件描述：用于过滤账户名适配器
 * 创建人：luobiao
 * 创建时间：2016-09-06 10:40
 * 修改人：luobiao
 * 修改时间：2016-09-06 10:40
 * 修改备注：
 * @version
 *
 */
public class UserNameAdapter extends BaseAdapter implements Filterable {
    //字符串过滤器
    private StringFilter mStringFilter;
    //账户名集合
    private List<User> mUserInfos;
    private Context mContext;

    //用于存储原始的账户名集合
    private ArrayList<User> mOriginalValues;

    private final Object mLock = new Object();

    public UserNameAdapter(Context context, List<User> userInfos) {
        this.mContext = context;
        this.mUserInfos = userInfos;
    }

    @Override
    public int getCount() {
        if(mUserInfos == null){
            return 0;
        }
        return mUserInfos.size();
    }

    @Override
    public Object getItem(int arg0) {
        if(mUserInfos == null){
            return null;
        }
        return mUserInfos.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.login_listitem_username, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_username.setText(mUserInfos.get(position).getLoginName());
        holder.tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 用户名点击
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onNameClick(v, mUserInfos.get(position));
                }
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除按钮点击
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onDeleteClick(v, mUserInfos.get(position));
                    // 从显示列表中移除该项
                    mOriginalValues.remove(mUserInfos.get(position));
                    mUserInfos.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    class ViewHolder {
        TextView tv_username;
        View btn_delete;

        ViewHolder(View view) {
            tv_username = (TextView) view.findViewById(R.id.tv_username);
            btn_delete = view.findViewById(R.id.btn_delete);
        }
    }

    @Override
    public Filter getFilter() {
        if (mStringFilter == null) {
            mStringFilter = new StringFilter();
        }
        return mStringFilter;
    }

    /**
     * 用于过滤字符串
     */
    private class StringFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            // 持有过滤操作完成之后的数据。该数据包括过滤操作之后的数据的值以及数量。 count:数量 values包含过滤操作之后的数据的值
            FilterResults results = new FilterResults();
            if(prefix == null || prefix.length()<1){
                results.values = new ArrayList<>();
                results.count = 0;
                return results;
            }
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    // 将list的用户 集合转换给这个原始数据的ArrayList
                    mOriginalValues = new ArrayList<>(mUserInfos);
                }
            }
            /*if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<User> list = new ArrayList<>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else */{
                // 做正式的筛选
                String prefixString = prefix.toString().toLowerCase();

                // 声明一个临时的集合对象 将原始数据赋给这个临时变量
                final ArrayList<User> values = mOriginalValues;

                final int count = values.size();

                // 新的集合对象
                final ArrayList<User> newValues = new ArrayList<>(count);

                for (int i = 0; i < count; i++) {
                    // 如果姓名或邮箱或电话的前缀相符就添加到新的集合
                    final User value = values.get(i);
                    if (value.getLoginName()!= null && value.getLoginName().toLowerCase().startsWith(prefixString)) {
                        newValues.add(value);
                    } else if (value.getEmail()!= null && value.getEmail().toLowerCase().startsWith(prefixString)) {
                        newValues.add(value);
                    } else if (value.getPhone()!= null && value.getPhone().toLowerCase().startsWith(prefixString)) {
                        newValues.add(value);
                    }
                }
                // 然后将这个新的集合数据赋给FilterResults对象
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // 重新将与适配器相关联的List重赋值一下
            mUserInfos = (List<User>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

    /**
     * 删除按钮点击监听接口
     */
    public interface OnItemClickListener {
        void onNameClick(View v, User user);
        void onDeleteClick(View v, User user);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnDeleteClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
