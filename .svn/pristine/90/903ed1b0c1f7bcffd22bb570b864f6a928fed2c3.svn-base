package com.augurit.am.cmpt.widget.amchart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.augurit.am.cmpt.R;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


/**
 * 统计表控件
 * Created by xiejiexin on 2016-08-01.
 */
public class AMTableChart extends AMBaseChart{

    private ListView mTable;
    private TableAdapter mAdapter;
    private Context context;

    public AMTableChart(Context context) {
        super(context);
    }

    public AMTableChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AMTableChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initChart(Context context) {
        this.context = context;
        // 获取包含ListView的Layout
        View content = inflate(context, R.layout.chart_view_table, null);
        mTable = (ListView) content.findViewById(R.id.table_list);

        addView(content);
    }

    @Override
    protected void setChartData() {
        mAdapter = new TableAdapter(context, mXVals, mYVals);
        mTable.setAdapter(mAdapter);
        mTable.postInvalidate();
    }

    @Override
    public void setExtraOffsets(int left, int top, int right, int bottom) {
        mTable.setPadding(left, top, right, bottom);
    }

    @Override
    public void setColors(Colors colors) {

    }

    @Override
    public void setLegendEnabled(boolean enabled) {

    }

    @Override
    public void setOnChartValueSelectedListener(OnChartValueSelectedListener listener) {

    }

    class TableAdapter extends BaseAdapter {

        private Context context;
        private String[] xVals;
        private float[] yVals;

        public TableAdapter(Context context, String[] xVals, float[] yVals) {
            this.context = context;
            this.xVals = xVals;
            this.yVals = yVals;
        }

        @Override
        public int getCount() {
            return Math.max(xVals.length, yVals.length);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder holder;
            if (convertView == null) {
                convertView = inflate(context, R.layout.chart_table_listitem, null);
                holder = new MyViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder = (MyViewHolder) convertView.getTag();
            String xValue = "未指定";
            float yValue = 0;
            try {
                xValue = this.xVals[position];
                yValue = this.yVals[position];
            } catch (Exception e) {
                // 超出数组大小
                // 防止XY值数量不等的情况
            }
            holder.tvX.setText(xValue);
            holder.tvY.setText(String.valueOf(yValue));
            return convertView;
        }

        class MyViewHolder {
            TextView tvX;
            TextView tvY;

            public MyViewHolder(View v) {
                tvX = (TextView) v.findViewById(R.id.text_x_val);
                tvY = (TextView) v.findViewById(R.id.text_y_val);
            }
        }
    }
}
