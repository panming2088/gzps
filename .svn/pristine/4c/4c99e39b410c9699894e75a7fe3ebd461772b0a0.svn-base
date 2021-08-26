package com.augurit.agmobile.patrolcore.common.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.RequireState;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;

/**
 * 描述：通过实现 CustomTableListener 来扩展自定义表格项
 * 凡是自定义扩展的表格项的控件类型都是 ControlType.CUSTOM,此外可以通过
 * TableItem的Field1字段来跟服务端协商好扩展的自定义表格项的种类
 *
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table
 * @createTime 创建时间 ：2017/6/29
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/6/29
 * @modifyMemo 修改备注：
 */

public class TestCustomerTableListener implements CustomTableListener {

    @Override
    public void addCustomTableItems(TableItem tableItem, TableViewManager tableViewManager) {
        if(tableItem.getField1().equals("NET_AUTO_COMPLETE")){
            addNetAutoComplete(tableItem,tableViewManager);
        }else if(tableItem.getField1().equals("OTHER")){
            addOtherCustomTableItem(tableItem,tableViewManager);
        }
    }

    /**
     * 设置必填项显示状态
     *
     * @param view
     * @param tableItem
     */
    public void setRequireTagState(ViewGroup view, TableItem tableItem,TableState tableState) {
        TextView tv_reqiredTag = (TextView) view.findViewById(R.id.tv_requiredTag);
        tv_reqiredTag.setVisibility(View.INVISIBLE);
        if (tableItem.getIf_required() != null) {
            if (tableState != TableState.READING) {
                if (tableItem.getIf_required().equals(RequireState.REQUIRE)) {
                    tv_reqiredTag.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    /********************************************** 在线模糊匹配 *********************************************/
    public void addNetAutoComplete(TableItem tableItem, TableViewManager tableViewManager) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(tableViewManager.getmContext()).inflate(R.layout.table_auto_complete_net_textview_item, null);
        AutoCompleteTextView auto = (AutoCompleteTextView) view.findViewById(R.id.autotext);
        TextView textView = (TextView) auto.findViewById(R.id.tv_);
        textView.setText(tableItem.getHtml_name());
        setRequireTagState(view, tableItem,tableViewManager.getTableState());
        tableViewManager.getMap().put(tableItem.getId(), view);
        tableViewManager.addViewToContainer(view);
        setAutoCompleteNetValue(tableViewManager.getmContext(),tableItem, view,tableViewManager.getTableState());
    }


    public void setAutoCompleteNetValue(Context context,TableItem tableItem, View view, TableState tableState) {
        if (tableState == TableState.READING) {
            view.findViewById(R.id.autotext).setEnabled(false);
        } else if (tableState == TableState.REEDITNG || tableState == TableState.EDITING) {
                String[] arr = {"aa", "aab", "aac"};
                ArrayAdapter<String> arrayAdapter;
                arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arr);
                ((AutoCompleteTextView) view.findViewById(R.id.autotext)).setAdapter(arrayAdapter);
        }
    }
    /********************************************** 在线模糊匹配 *********************************************/

    /********************************************** 其他自定义类型 *********************************************/
    public void addOtherCustomTableItem(TableItem tableItem, TableViewManager tableViewManager){

    }
    /********************************************** 其他自定义类型 *********************************************/
}
