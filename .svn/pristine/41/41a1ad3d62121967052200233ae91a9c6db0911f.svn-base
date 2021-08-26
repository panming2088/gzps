package com.augurit.agmobile.patrolcore.localhistory.util;


import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTableItem;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;

import java.util.ArrayList;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agcollection.setting.localhistory
 * @createTime 创建时间 ：17/5/24
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/5/24
 * @modifyMemo 修改备注：
 */

public class ConvertTableUtils {
    /**
     * 将本地保存的表记录项转换为原始表格项集合
     * @param localTable
     * @return
     */
    public static ArrayList<TableItem> convert(LocalTable localTable) {
        ArrayList<TableItem> tableItems = new ArrayList<>();
        for (LocalTableItem editedTableItem : localTable.getList()) {
            TableItem tableItem = new TableItem();
            tableItem.setDic_code(editedTableItem.getDic_code());
            tableItem.setBase_table(editedTableItem.getBase_table());
            tableItem.setColum_num(editedTableItem.getColum_num());
            tableItem.setControl_type(editedTableItem.getControl_type());
            tableItem.setDevice_id(editedTableItem.getDevice_id());
            tableItem.setField1(editedTableItem.getField1());
            tableItem.setField2(editedTableItem.getField2());
            tableItem.setHtml_name(editedTableItem.getHtml_name());
            tableItem.setValue(editedTableItem.getValue());
            tableItem.setId(editedTableItem.getId());
            tableItem.setIs_edit(editedTableItem.getId_edit());
            tableItem.setIf_hidden(editedTableItem.getIf_hidden());
            tableItem.setIndustry_code(editedTableItem.getIndustry_code());
            tableItem.setIndustry_table(editedTableItem.getIndustry_table());
            tableItem.setRow_num(editedTableItem.getRow_num());
            tableItem.setIs_form_field(editedTableItem.getIs_form_field());
            tableItem.setRegex(editedTableItem.getRegex());
            tableItem.setValidate_type(editedTableItem.getValidate_type());
            tableItem.setFirst_correlation(editedTableItem.getFirst_correlation());
            tableItem.setThree_correlation(editedTableItem.getThree_correlation());
            tableItem.setIf_required(editedTableItem.getIf_required());
            tableItems.add(tableItem);
        }
        return tableItems;
    }
}
