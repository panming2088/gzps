package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.augurit.agmobile.bluetooth.BluetoothDataManager;
import com.augurit.agmobile.bluetooth.StatusListener;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.selectlocation.model.OnBluetoothDeviceConnectEvent;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 设备列表Activity
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.btlocdevice.activity
 * @createTime 创建时间 ：2016-12-01 17:28
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2016-12-01 17:28
 */
public class BluetoothDeviceListActivity extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_ENABLE = 2;     // 开启蓝牙请求

    private RecyclerView rv_device;
    private View btn_back;
    private View btn_scan;
    private View tv_bluetooth;
    private BluetoothDeviceAdapter mAdapter;
    private ArrayList<BluetoothDevice> mBondedList;     // 已配对蓝牙设备列表
    private ArrayList<BluetoothDevice> mUnbondList;     // 未配对蓝牙设备列表
    private BluetoothAdapter mBTAdapter;
    private BluetoothDataManager mBtDataManager;
    private AlertDialog mDialog;
    /**
     * 点击监听
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btn_back) {// 返回按钮
                setResult(RESULT_OK);
                EventBus.getDefault().post(new OnBluetoothDeviceConnectEvent(false));   // 返回未连接成功
                finish();
            } else if (i == R.id.btn_scan) {// 搜索按钮
                if (mBTAdapter.isEnabled()) {
                    if (!mBTAdapter.isDiscovering()) {
                        mBTAdapter.startDiscovery();
                        mUnbondList.clear();
                        mAdapter.refreshData();
                    }
                    ToastUtil.shortToast(BluetoothDeviceListActivity.this, "搜索附近设备中...");
                } else {
                    ToastUtil.shortToast(BluetoothDeviceListActivity.this, "请开启蓝牙");
                }

            } else if (i == R.id.tv_bluetooth) {// 提示开启蓝牙
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);

            }
        }
    };
    /**
     * 蓝牙状态监听
     */
    private StatusListener mBtStatusListener = new StatusListener() {
        @Override
        public void onConnecting(BluetoothDevice device) {
            mDialog = new AlertDialog.Builder(BluetoothDeviceListActivity.this)
                    .setMessage("正在连接到" + device.getName() + "，请稍候...")
                    .create();
            mDialog.show();
        }

        @Override
        public void onConnected(BluetoothDevice device) {
            // 连接成功，返回
            if (mDialog != null) {
                mDialog.dismiss();
            }
            setResult(RESULT_OK);
            EventBus.getDefault().post(new OnBluetoothDeviceConnectEvent(true));    // 返回连接成功
            finish();
        }

        @Override
        public void onConnectFailed(BluetoothDevice device) {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            mDialog = new AlertDialog.Builder(BluetoothDeviceListActivity.this)
                    .setTitle("连接失败")
                    .setMessage("请检查设备是否开启或已连接到其他手机")
                    .setPositiveButton("确定", null)
                    .create();
            mDialog.show();
        }

        @Override
        public void onDisconnected(BluetoothDevice device) {
            mAdapter.setDeviceConnected(null);
            mAdapter.notifyDataSetChanged();
            ToastUtil.shortToast(BluetoothDeviceListActivity.this, "设备断开连接:" + device.getName());
        }

        //        @Override
        //        public void onLog(String msg) {
        //
        //        }
    };
    /**
     * 蓝牙搜索广播接收
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 搜索开始
            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                mAdapter.showProgress();
            }
            // 搜索结束
            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                mAdapter.hideProgress();
                mAdapter.refreshData();
            }
            // 搜索到设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    // 加入未配对列表
                    if (!mUnbondList.contains(device)) {
                        mUnbondList.add(device);
                        mAdapter.refreshData();
                    }
                } else {
                    // 若为已配对设备则需刷新列表，因为存在设备改名的情况
                    for (int i = 0; i < mBondedList.size(); i++) {
                        BluetoothDevice bondedDevice = mBondedList.get(i);
                        if (bondedDevice.getAddress().equals(device.getAddress())) {
                            mBondedList.add(i, device);
                            mBondedList.remove(i + 1);
                            mAdapter.refreshData();
                        }
                    }
                }
            }
            // 配对设备
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                LogUtil.i("state change" + device.getBondState());
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) { // 配对成功
                    // 从未配对列表中移除，加入已配对列表
                    if (mUnbondList.contains(device)) {
                        mUnbondList.remove(device);
                    }
                    mBondedList.add(device);
                    mAdapter.refreshData();
                    ToastUtil.shortToast(BluetoothDeviceListActivity.this, "配对成功");
                    // 连接到该设备
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mBtDataManager.connect(device);
                } else {    // 正在配对或配对失败
                    // 更新列表项显示状态（正在配对或无显示）
                    for (int i = 0; i < mUnbondList.size(); i++) {
                        if (mUnbondList.get(i).getAddress().equals(device.getAddress())) {
                            mUnbondList.add(i, device);
                            mUnbondList.remove(i + 1);
                            mAdapter.refreshData();
                        }
                    }
                }
            }
            // 突然关闭蓝牙
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                if (state == BluetoothAdapter.STATE_OFF) {
                    tv_bluetooth.setVisibility(View.VISIBLE);
                }
                if (state == BluetoothAdapter.STATE_ON) {
                    tv_bluetooth.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devicelist);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        mBtDataManager = BluetoothDataManager.getInstance(getApplicationContext());
        mBtDataManager.setStatusListener(mBtStatusListener);
        initView();
        initDeviceList();
        initData();
        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        if (mBTAdapter != null) {
            if (!mBTAdapter.isEnabled()) {  // 请求开启蓝牙
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);
            }
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        rv_device = (RecyclerView) findViewById(R.id.rv_device);
        btn_back = findViewById(R.id.btn_back);
        btn_scan = findViewById(R.id.btn_scan);
        tv_bluetooth = findViewById(R.id.tv_bluetooth);

        btn_back.setOnClickListener(mOnClickListener);
        btn_scan.setOnClickListener(mOnClickListener);
        tv_bluetooth.setOnClickListener(mOnClickListener);
    }

    /**
     * 初始化设备列表
     */
    private void initDeviceList() {
        mBondedList = new ArrayList<>();
        mUnbondList = new ArrayList<>();
        mAdapter = new BluetoothDeviceAdapter(this, mBondedList, mUnbondList);
        rv_device.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final PinnedHeaderItemDecoration decoration = new PinnedHeaderItemDecoration.Builder(BluetoothDeviceAdapter.TYPE_HEADER)
                .setDividerId(R.drawable.bluetooth_device_list_divider)
                .enableDivider(true)
                .disableHeaderClick(true)
                .create();
        rv_device.addItemDecoration(decoration);
        rv_device.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BluetoothDeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BluetoothDevice device) {
                // 已配对设备列表项点击
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    // 若非已连接，则连接
                    BluetoothDevice connectedDevice = mBtDataManager.getConnectedDevice();
                    if (connectedDevice == null || !device.getAddress().equals(connectedDevice.getAddress())) {
                        if (mBtDataManager.getConnectedDevice() != null) {  // 若当前已连接则先断开
                            mBtDataManager.disconnect();
                        }
                        mBtDataManager.connect(device);
                        if (mBTAdapter.isDiscovering()) {
                            mBTAdapter.cancelDiscovery();
                        }
                    } else {    // 若已连接则断开
                        // 弹出询问断开对话框
                        AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothDeviceListActivity.this);
                        builder.setTitle("是否断开与" + device.getName() + "的连接？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mBtDataManager.disconnect();
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.show();
                    }
                }
                // 未配对设备列表项点击
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    try {   // 进行配对
                        mBTAdapter.cancelDiscovery();   // 停止搜索
                        Method createBond = BluetoothDevice.class.getMethod("createBond");
                        createBond.invoke(device);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mAdapter.setOnItemLongClickListener(new BluetoothDeviceAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, final BluetoothDevice device) {
                // 已配对设备列表项长按
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    // 弹出菜单
                    AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothDeviceListActivity.this);
                    CharSequence[] settings;
                    final BluetoothDevice connectedDevice = mBtDataManager.getConnectedDevice();
                    if (connectedDevice != null && device.getAddress().equals(connectedDevice.getAddress())) {
                        settings = new CharSequence[]{"断开连接", "取消配对"};
                    } else {
                        settings = new CharSequence[]{"连接", "取消配对"};
                    }
                    builder.setItems(settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    // 断开连接或连接
                                    if (connectedDevice != null && device.getAddress().equals(connectedDevice.getAddress())) {
                                        mBtDataManager.disconnect();
                                    } else {
                                        mBtDataManager.connect(device);
                                        if (mBTAdapter.isDiscovering()) {
                                            mBTAdapter.cancelDiscovery();
                                        }
                                    }
                                    break;
                                case 1:
                                    // 取消配对
                                    try {
                                        Method removeBond = BluetoothDevice.class.getMethod("removeBond");
                                        boolean b = (boolean) removeBond.invoke(device);
                                        if (b) {   // 从已配对列表中移除
                                            mBondedList.remove(device);
                                            mAdapter.refreshData();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    /**
     * 初始化列表数据
     */
    private void initData() {
        // 获取数据
        if (mBTAdapter != null && mBTAdapter.isEnabled()) {
            tv_bluetooth.setVisibility(View.GONE);
            // 搜索设备
            mBTAdapter.startDiscovery();
            mUnbondList.clear();
            // 获取已配对设备列表
            mBondedList.clear();
            for (BluetoothDevice device : mBTAdapter.getBondedDevices()) {  // 加入已配对的设备
                mBondedList.add(device);
            }
            mAdapter.setDeviceConnected(mBtDataManager.getConnectedDevice()); // 设置当前已连接的设备
            mAdapter.refreshData();
        } else {
            // 提示开启蓝牙
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_BLUETOOTH_ENABLE) {
                // 从开启蓝牙请求返回，且蓝牙已打开，刷新已配对设备列表
                initData();
            }
        } else {
            if (requestCode == REQUEST_BLUETOOTH_ENABLE) {
                // 显示蓝牙未开启提示
                tv_bluetooth.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        EventBus.getDefault().post(new OnBluetoothDeviceConnectEvent(false));   // 返回未连接成功
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBTAdapter != null && !mBTAdapter.isDiscovering()) {
            mBTAdapter.startDiscovery();    // 重新进入再次开启
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBTAdapter != null && mBTAdapter.isDiscovering()) {
            mBTAdapter.cancelDiscovery();
            mAdapter.hideProgress();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if (mBTAdapter != null && mBTAdapter.isDiscovering()) {
            mBTAdapter.cancelDiscovery();
        }
    }
}
