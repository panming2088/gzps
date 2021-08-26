package com.augurit.agmobile.patrolcore.common.opinion.model;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.model
 * @createTime 创建时间 ：2017-07-17
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-17
 * @modifyMemo 修改备注：
 */

public class TemplateResult {

    private int page;
    private int pageSize;
    private String sidx;
    private String sord;
    private int total;
    private int totalPages;
    private List<OpinionTemplate> rows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<OpinionTemplate> getRows() {
        return rows;
    }

    public void setRows(List<OpinionTemplate> rows) {
        this.rows = rows;
    }

}
