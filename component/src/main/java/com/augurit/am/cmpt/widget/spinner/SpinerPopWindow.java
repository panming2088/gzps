package com.augurit.am.cmpt.widget.spinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;

import java.util.List;

public class SpinerPopWindow extends PopupWindow implements OnItemClickListener {

    private Context mContext;
    private ListView mListView;
    private AbstractSpinerAdapter mAdapter;
    private IOnItemSelectListener mItemSelectListener;
    private int mWindowLayoutId;


    public SpinerPopWindow(Context context) {
        this(context, R.layout.spinner_window_layout);
    }

    public SpinerPopWindow(Context context, int windowLayoutId) {
        super(context);
        mContext = context;
        mWindowLayoutId = windowLayoutId;
        init();
    }

    public void setItemListener(IOnItemSelectListener listener) {
        mItemSelectListener = listener;
    }

    public void setAdatper(AbstractSpinerAdapter adapter) {
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
    }


    private void init() {
        View view = LayoutInflater.from(mContext).inflate(mWindowLayoutId, null);
        setContentView(view);
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);


        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setOnItemClickListener(this);
    }


    public <T> void refreshData(List<T> list, int selIndex) {
        if (list != null && selIndex != -1) {
            if (mAdapter != null) {
                mAdapter.refreshData(list, selIndex);
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        dismiss();
        if (mItemSelectListener != null) {
            mItemSelectListener.onItemClick(pos);
        }
    }


}
