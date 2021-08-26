package com.augurit.agmobile.patrolcore.common.table.util;

import android.widget.Toast;

import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.am.fw.utils.view.ToastUtil;

/**
 * 描述：历史上报内容模板化处理
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.util
 * @createTime 创建时间 ：2017/7/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/7/7
 * @modifyMemo 修改备注：
 */

public class TemplateHelper {
    /**
     * 模板化
     * 一般情况下的上报历史列表详情阅读是  TableState.READING
     * 如果想把 上报历史列表详情内容作为数据模板,那么切换成 TableState.REEDITNG
     * 然后 再调用此辅助函数进行模板化的其他处理
     *
     * @param tableViewManager
     */
    public static void template(TableViewManager tableViewManager){
        //模板化只对于已上报内容,即 TableState.REEDITNG 状态下的工单表格内容
        if(tableViewManager.getTableState() != TableState.REEDITNG) {
            ToastUtil.longToast(tableViewManager.getmContext(),"表格模板化参数设置出错!");
            return;
        }



    }
}
