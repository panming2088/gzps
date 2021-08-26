package com.augurit.agmobile.patrolcore.common.table.dao.remote;

import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 描述：所有项目的表格模板数据
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.dao.remote
 * @createTime 创建时间 ：2017/8/29
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/8/29
 * @modifyMemo 修改备注：
 */

public class AllFormTableItems {
    private String message;
    private String success;
    private List<ProjectItem> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ProjectItem> getResult() {
        return result;
    }

    public void setResult(List<ProjectItem> result) {
        this.result = result;
    }

    public static class ProjectItem{

        //@SerializedName("devId") //xcl 2017.9.20 接口改变 9.27 恢复回来接口
        private String projectId;

        //@SerializedName("items") //xcl 2017.9.20 接口改变 9.27 恢复回来接口
        private List<TableItem> columns;

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public List<TableItem> getColumns() {
            return columns;
        }

        public void setColumns(List<TableItem> columns) {
            this.columns = columns;
        }
    }

}
