package com.augurit.am.cmpt.login.model;

import java.io.Serializable;

/**
 *
 * 包名：com.augurit.am.cmpt.login.model
 * 文件描述：部门类
 * 创建人：luobiao
 * 创建时间：2016-09-02 17:18
 * 修改人：luobiao
 * 修改时间：2016-09-02 17:18
 * 修改备注：
 * @version
 *
 */
public class Department implements Serializable{
    //部门代码
    private String code;
    //级别
    private String grade;
    //名称
    private String name;
    //类型
    private String type;
    //部门id
    private Long id;

    public Department() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
