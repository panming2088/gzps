package com.augurit.am.cmpt.widget.amchart;


import android.content.Context;
import android.widget.TextView;

import com.augurit.am.cmpt.R;
import com.augurit.am.fw.utils.DoubleUtil;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.DecimalFormat;

/**
 *
 * 自定义MarkerView,用于图标项点击显示
 * Created by xiejiexin on 2016-07-27.
 */
class XYMarkerView extends MarkerView {

    private AxisValueFormatter valueFormatter;
    private TextView tvContent;
    private DecimalFormat format;

    public XYMarkerView(Context context, AxisValueFormatter axisValueFormatter) {
        super(context, R.layout.chart_marker_view);
        valueFormatter = axisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tv_content);
        format = new DecimalFormat("###");
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        String content = valueFormatter.getFormattedValue(entry.getX(), null)
                .concat(":")
                .concat(DoubleUtil.formatDecimal(entry.getY()));
        tvContent.setText(content);
    }

    @Override
    public int getXOffset(float v) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float v) {
        return -getHeight();
    }
}
