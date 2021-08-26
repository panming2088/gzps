package com.augurit.agmobile.patrolcore.common.table.dao.remote;



import com.augurit.agmobile.patrolcore.common.table.model.TableChildItem;

import java.util.List;

/**
 * 描述：用于网络请求数据
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model.manager.remote.api
 * @createTime 创建时间 ：17/3/6
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/6
 * @modifyMemo 修改备注：
 */

public class TableChildItems {
    private String page;
    private String pageSize;
    private List<TableChildItem> rows;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public List<TableChildItem> getRows() {
        return rows;
    }

    public void setRows(List<TableChildItem> rows) {
        this.rows = rows;
    }
}
