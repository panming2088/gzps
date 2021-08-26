package com.augurit.am.cmpt.widget.spinner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.widget.searchview.util.Util;
import com.augurit.am.fw.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class AMSpinner extends LinearLayout implements View.OnClickListener, AbstractSpinerAdapter.IOnItemSelectListener {

    private Context context;
    private TextView tv_value;
//    private List<CustemObject> nameList = new ArrayList<CustemObject>();
    private Map<String, Object> itemsMap = new LinkedHashMap<>();
    private AbstractSpinerAdapter mAdapter;
    private SpinerPopWindow mSpinerPopWindow;
    private OnItemClickListener onItemClickListener;
    private int currentPosition = -1;
    private Object currentSelected = null;

    private boolean editable = true;
    private View bt_dropdown;

    public interface OnItemClickListener {
        void onItemClick(int position, Object item);
    }

    public AMSpinner(Context context) {
        super(context);
        this.context = context;
    }

    public AMSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AMSpinner);
        int spinerLayoutId = ta.getResourceId(R.styleable.AMSpinner_spinnerLayout, R.layout.spinner_layout);
        int itemLayoutId = ta.getResourceId(R.styleable.AMSpinner_itemLayout, R.layout.spinner_item_layout);
        int windowLayoutId = ta.getResourceId(R.styleable.AMSpinner_windowLayout, R.layout.spinner_window_layout);
        int textSize = ta.getDimensionPixelSize(R.styleable.AMSpinner_itemTextSize, spToPx(18));
        LayoutInflater.from(context).inflate(spinerLayoutId, this, true);
        View fl_layout = findViewById(R.id.fl_layout);
        fl_layout.setOnClickListener(this);
        tv_value = (TextView) findViewById(R.id.tv_value);
        mAdapter = new CustemSpinerAdapter(context, itemLayoutId);
        mAdapter.setItemTextSize(textSize);
        mAdapter.refreshData(keyList(), 0);

        bt_dropdown = findViewById(R.id.bt_dropdown);

        mSpinerPopWindow = new SpinerPopWindow(context, windowLayoutId);
        mSpinerPopWindow.setAdatper(mAdapter);
        mSpinerPopWindow.setItemListener(this);
    }

    public static int spToPx(int sp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    public View getBt_dropdown() {
        return bt_dropdown;
    }

    public void setItemsMap(Map<String, Object> itemsMap) {
        setItemsMap(itemsMap, 0);
    }

    public void setItemsMap(Map<String, Object> itemsMap, int pos){
        this.itemsMap = itemsMap;
        setValue(pos);
        mAdapter.refreshData(keyList(), pos);
        mAdapter.notifyDataSetChanged();
    }

    public void addItems(String key, Object value) {
        this.itemsMap.put(key, value);
        mAdapter.refreshData(keyList(), 0);
        mAdapter.notifyDataSetChanged();
    }

    public void removeAll() {
        tv_value.setText("");
        this.itemsMap = new LinkedHashMap<>();
        mAdapter.refreshData(keyList(), 0);
        mAdapter.notifyDataSetChanged();
    }

    public void remove(String key){
        itemsMap.remove(key);
        mAdapter.refreshData(keyList(), 0);
        mAdapter.notifyDataSetChanged();
    }

    private void showSpinWindow() {
        mSpinerPopWindow.setWidth(tv_value.getWidth());
        if(this.itemsMap.size() > 6){
            mSpinerPopWindow.setHeight(Util.dpToPx(320));
        } else {
            mSpinerPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int[] location = new int[2];
        tv_value.getLocationOnScreen(location);
        float viewY = location[1];
        int windowHeight = DisplayUtil.getWindowHeight(context);

        if((viewY > windowHeight/2)
                && itemsMap.size()>1){
            mSpinerPopWindow.showAtLocation(tv_value, Gravity.NO_GRAVITY, location[0], location[1]-mSpinerPopWindow.getHeight());
        } else {
            mSpinerPopWindow.showAsDropDown(tv_value);
        }
    }

    private void setValue(int pos) {
        if (pos >= 0 && pos <= itemsMap.size()) {
            Object[] keyArray = itemsMap.keySet().toArray();
            tv_value.setText(keyArray[pos].toString());
        }
    }

    private ArrayList<String> keyList(){
        Set<String> keySet = this.itemsMap.keySet();
        return new ArrayList<>(keySet);
    }

    public boolean containsKey(String key){
        return itemsMap.containsKey(key);
    }

    public int size(){
        return itemsMap.size();
    }

    @Override
    public void onItemClick(int pos) {
        if(itemsMap.size() <= 0){
            return;
        }
        setValue(pos);
        if (pos >= 0 && pos <= itemsMap.size()) {
            Object[] keyArray = itemsMap.keySet().toArray();
            currentPosition = pos;
            currentSelected = itemsMap.get(keyArray[pos].toString());
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(pos, itemsMap.get(keyArray[pos].toString()));
            }
            mAdapter.setSelectedPosition(pos);
            mAdapter.notifyDataSetChanged();
        }

    }

    public void selectItem(int position){
        onItemClick(position);
    }

    public void selectItem(String key){
        if(!itemsMap.containsKey(key)){
            return;
        }
        int i = 0;
        Set<String> keySet = itemsMap.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()){
            String k = it.next();
            if(k.equals(key)){
                break;
            }
            i++;
        }
        onItemClick(i);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public Object getCurrentSelected() {
        return currentSelected;
    }

    @Override
    public void onClick(View v) {
        if(!editable){
            return;
        }
        showSpinWindow();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public String getText() {
        return tv_value.getText().toString();
    }

    public void setText(String value) {
        tv_value.setText(value);
    }

    public void setEditable(boolean editable){
        this.editable = editable;
    }
}
