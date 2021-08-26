package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;
import com.oushangfeng.pinnedsectionitemdecoration.utils.FullSpanUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙设备列表适配器
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.btlocdevice.adapter
 * @createTime 创建时间 ：2016-12-02 9:19
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2016-12-02 9:19
 */
public class BluetoothDeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<DeviceInfo> mDeviceList;
    private List<BluetoothDevice> mBondedList;
    private List<BluetoothDevice> mUnbondList;
    private BluetoothDevice mDeviceConnected;
    private View mSearchProgress;     // 搜索进度视图
    private boolean mIsFirstSearch = true;

    public static final int TYPE_HEADER = 1;
    public static final int TYPE_DATA = 2;
    public static final int TYPE_HINT = 3;

    public BluetoothDeviceAdapter(Context context, List<BluetoothDevice> bondedList, List<BluetoothDevice> unbondList) {
        mContext = context;
        mBondedList = bondedList;
        mUnbondList = unbondList;
        mDeviceList = new ArrayList<>();
        setData(mBondedList, mUnbondList);
    }

    private void setData(List<BluetoothDevice> bondedList, List<BluetoothDevice> unbondList) {
        mDeviceList.clear();
        // 添加已配对header
        DeviceInfo infoBond = new DeviceInfo(TYPE_HEADER, "已配对设备", null);
        mDeviceList.add(infoBond);
        // 添加配对设备列表
        if (bondedList.isEmpty()) { // 为空则添加提示
            DeviceInfo hint = new DeviceInfo(TYPE_HINT, "当前没有已配对的蓝牙设备", null);
            mDeviceList.add(hint);
        } else {
            for (BluetoothDevice device : bondedList) {
                DeviceInfo info = new DeviceInfo(TYPE_DATA, "", device);
                mDeviceList.add(info);
            }
        }
        // 添加可配对设备header
        DeviceInfo infoUnbond = new DeviceInfo(TYPE_HEADER, "可配对设备", null);
        mDeviceList.add(infoUnbond);
        // 添加可配对设备列表
        if (unbondList.isEmpty()) {
            DeviceInfo hint = new DeviceInfo(TYPE_HINT, "未在附近找到新设备", null);
            mDeviceList.add(hint);
        } else {
            for (BluetoothDevice device : unbondList) {
                DeviceInfo info = new DeviceInfo(TYPE_DATA, "", device);
                mDeviceList.add(info);
            }
        }
    }

    public void refreshData() {
        setData(mBondedList, mUnbondList);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, TYPE_HEADER);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        FullSpanUtil.onViewAttachedToWindow(holder, this, TYPE_HEADER);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderHolder(View.inflate(mContext, R.layout.list_item_bt_device_header, null));
        }
        if (viewType == TYPE_DATA) {
            View view = View.inflate(mContext, R.layout.list_item_bt_device, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new DataHolder(view);
        }
        if (viewType == TYPE_HINT) {
            View view = View.inflate(mContext, R.layout.list_item_bt_device_hint, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new HintHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.tv_name.setText(mDeviceList.get(position).value);
            if (mDeviceList.get(position).value.equals("可配对设备") && mIsFirstSearch) {  // TODO 暂时这样
                mSearchProgress = headerHolder.view_progress;
                mSearchProgress.setVisibility(View.VISIBLE);
                mIsFirstSearch = false;
            }
        }
        if (holder instanceof DataHolder) {
            DataHolder viewHolder = (DataHolder) holder;
            final BluetoothDevice device = mDeviceList.get(position).device;
            viewHolder.tv_name.setText(device.getName());       // 名称
            viewHolder.tv_address.setText(device.getAddress());     // 地址
            String status = "";                     // 状态
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_BONDING:
                    status = "正在配对";
                    break;
                default:
                    break;
            }
            if (mDeviceConnected != null && device.getAddress().equals(mDeviceConnected.getAddress())) {
                status = "已连接";
            }
            viewHolder.tv_status.setText(status);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // 表项点击
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, device);
                    }
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(position, device);
                        return true;
                    }
                    return false;
                }
            });
        }
        if (holder instanceof HintHolder) {
            HintHolder hintHolder = (HintHolder) holder;
            hintHolder.tv_hint.setText(mDeviceList.get(position).value);
        }
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDeviceList.get(position).type;
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        View view_progress;

        HeaderHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            view_progress  = itemView.findViewById(R.id.view_progress);
        }
    }

    private class DataHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tv_name;
        TextView tv_address;
        TextView tv_status;

        DataHolder(View view) {
            super(view);
            itemView = view;
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
        }
    }

    private class HintHolder extends RecyclerView.ViewHolder {
        TextView tv_hint;

        HintHolder(View itemView) {
            super(itemView);
            tv_hint = (TextView) itemView.findViewById(R.id.tv_hint);
        }
    }

    private class DeviceInfo {
        int type;
        String value;
        BluetoothDevice device;

        DeviceInfo(int type, String value, BluetoothDevice device) {
            this.type = type;
            this.value = value;
            this.device = device;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, BluetoothDevice device);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, BluetoothDevice device);
    }
    private OnItemLongClickListener mOnItemLongClickListener;
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 设置当前已连接的设备
     * @param deviceConnected 已连接的设备
     */
    public void setDeviceConnected(BluetoothDevice deviceConnected) {
        this.mDeviceConnected = deviceConnected;
    }

    public void showProgress() {
        if (mSearchProgress != null) {
            mSearchProgress.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        if (mSearchProgress != null) {
            mSearchProgress.setVisibility(View.INVISIBLE);
        }
    }
}
