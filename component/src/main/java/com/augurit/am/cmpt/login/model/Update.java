package com.augurit.am.cmpt.login.model;


/**
 *
 * 包名：com.augurit.am.cmpt.login.model
 * 文件描述： 版本更新类
 * 创建人：luobiao
 * 创建时间：2016-09-02 17:18
 * 修改人：luobiao
 * 修改时间：2016-09-02 17:18
 * 修改备注：
 * @version
 *
 */
public class Update {
    //版本号
    private String version;
    //版本名
    private String versionname;
    //apk下载地址
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
