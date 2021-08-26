package com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MapHelper;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

/**
 * 原设施位置
 * Created by xucil on 2017/12/29.
 */
public class OriginalLocationView extends RelativeLayout {

    private View root;
    private TextView tv_1;
    private EditText et_1;
    private Button btn_show_location;
    private MapHelper mapHelper;
    private boolean ifOriginLocationIsShowing = false;
    private int graphicId = -1;

    public OriginalLocationView(Context context) {
        this(context, null);
    }

    public OriginalLocationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        root = LayoutInflater.from(context).inflate(R.layout.view_origin_location, this);
        tv_1 = (TextView) root.findViewById(R.id.tv_1);
        et_1 = (EditText) root.findViewById(R.id.et_1);
        btn_show_location = (Button) root.findViewById(R.id.btn_show_location);
    }

    public void setTextViewName(String textViewName) {
        tv_1.setText(textViewName);
    }

    public void setText(String text) {
        et_1.setText(text);
    }

    public void setReadOnly() {
        et_1.setEnabled(false);
    }

    public void setOriginLocation(final MapView mapView, GraphicsLayer graphicsLayer, final Point point) {
        if (mapView != null) {
            initMapHelper(mapView,graphicsLayer);
            btn_show_location.setVisibility(VISIBLE);
            btn_show_location.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ifOriginLocationIsShowing && mapHelper != null) {
                        btn_show_location.setText("显示");
                        mapHelper.removeGeometry(graphicId);
                        ifOriginLocationIsShowing = false;
                    } else if (mapHelper != null) {
                        btn_show_location.setText("隐藏");
                        graphicId = mapHelper.drawOldPoints(point, true);
                        ifOriginLocationIsShowing = true;
                    }
                }
            });
        }
    }

    private void initMapHelper(MapView mapView,GraphicsLayer graphicsLayer) {
        if (mapHelper == null) {
            mapHelper = new MapHelper(getContext(), mapView,graphicsLayer);
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE) {
            if (mapHelper != null) {
                mapHelper.removeAllGraphic();
                ifOriginLocationIsShowing = false;
                btn_show_location.setText("显示");
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mapHelper != null) {
            mapHelper.removeAllGraphic();
        }
    }
}
