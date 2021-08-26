package com.augurit.agmobile.gzps.uploadfacility.util;

import android.graphics.Color;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.monitor.view.TextItemTableItem1;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckState;

public class InspectionSetCheckStateUtil {
    public static final String CHECK_ING = "未审核";
    public static final String CHECK_PASS = "审核通过";
    public static final String CHECK_NO_PASS = "审核不通过";

        public static void setCheckState(String state, TextView tv){
            if(tv == null) return;
            if (state != null) {
                switch (state){
                    case CheckState.UNCHECK1:
                        tv.setText(CHECK_ING);
                        tv.setTextColor(Color.RED);
                        break;
                    case CheckState.IN_DOUBT1:
                        tv.setText(CHECK_NO_PASS);
                        tv.setTextColor(Color.parseColor("#FFFFC248"));
                        break;
                    case CheckState.UNCHECK2:
                        tv.setText(CHECK_PASS);
                        tv.setTextColor(Color.parseColor("#3EA500"));
                        break;
                    default:
                        tv.setText("无审核信息");
                        break;
                }
            }else{
                tv.setText("无审核信息");
            }
        }
    public static void setCheckState(String state, TextItemTableItem textItemTableItem){
            TextView tv = (TextView) textItemTableItem.findViewById(R.id.et_1);
            setCheckState(state,tv);
      }

    public static void setCheckState(String state, TextItemTableItem1 textItemTableItem){
        TextView tv = (TextView) textItemTableItem.findViewById(R.id.et_1);
        setCheckState(state,tv);
    }
}
