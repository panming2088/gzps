package com.augurit.agmobile.gzps.uploadfacility.util;

import android.text.InputFilter;
import android.widget.EditText;

import com.augurit.agmobile.patrolcore.common.util.MaxLengthInputFilter;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzpssb.utils
 * @createTime 创建时间 ：2018-04-26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-04-26
 * @modifyMemo 修改备注：
 */

public class SetMaxLengthUtils {
/**
 * 排水户名称：50
 详细地址：150
 产权人名称：40
 产权人联系电话：18
 经营人姓名：40
 经营人联系电话：18
 执照编号：50
 排水许可证编号:50
 排污许可证编号:50
 上报说明：200
 行业类别：其他 100
 井存在问题：其他 30
 排水存在问题：排水性状异常  30
 排水存在问题：其他 30
 隔油池：其他50
 格栅：其他50
 沉淀池：其他50
 其他预处理设施名称、其他 50
 */
    public final static int MAX_LENGTH_15 = 15;//镇街的长度
    public final static int MAX_LENGTH_18 = 18;
    public final static int MAX_LENGTH_30 = 30;
    public final static int MAX_LENGTH_40 = 40;
    public final static int MAX_LENGTH_50 = 50;
    public final static int MAX_LENGTH_100 = 100;
    public final static int MAX_LENGTH_150 = 150;
    public final static int MAX_LENGTH_200 = 200;
    //设置输入框最长的输入长度
    public static void setMaxLength(EditText editText, int maxLength) {
        setMaxLength(editText, maxLength, 1500);
    }

    public static void setMaxLength(EditText editText, int maxLength, int dismissErrorDelay) {
        editText.setFilters(new InputFilter[]{new MaxLengthInputFilter(maxLength,
                null, editText, "长度不能超过" + maxLength + "个字").setDismissErrorDelay(dismissErrorDelay)});
    }
}
