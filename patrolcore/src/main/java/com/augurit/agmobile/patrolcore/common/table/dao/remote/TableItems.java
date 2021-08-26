package com.augurit.agmobile.patrolcore.common.table.dao.remote;




import com.augurit.agmobile.patrolcore.common.table.model.TableItem;

import java.util.List;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model.manager.remote
 * @createTime 创建时间 ：17/3/20
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/20
 * @modifyMemo 修改备注：
 */

public class TableItems {
    private String message;
    private List<TableItem>  result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TableItem> getResult() {
        return result;
    }

    public void setResult(List<TableItem> result) {
        this.result = result;
    }
}
