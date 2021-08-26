package com.augurit.am.cmpt.update.model;

/**
 * 描述：版本更新的实体与你服务器的字段相匹配
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.common.update.model
 * @createTime 创建时间 ：17/5/10
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/5/10
 * @modifyMemo 修改备注：
 */

public class UpdateAppInfo {
    public UpdateInfo data; // 信息
    public String error_code; // 错误代码
    public String error_msg; // 错误信息

    public static class UpdateInfo {
        // app名字
        public String appname;
        //apk名
        public String apkName;
        //版本
        public String versionCode;
        //强制升级
        public String lastForce;
        //app最新版本地址
        public String updateurl;
        //升级信息
        public String upgradeinfo;
        //版本名
        public String verName;

        public String getApkName() {
            return apkName;
        }

        public void setApkName(String apkName) {
            this.apkName = apkName;
        }

        public String getVerName() {
            return verName;
        }

        public void setVerName(String verName) {
            this.verName = verName;
        }

        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getLastForce() {
            return lastForce;
        }

        public void setLastForce(String lastForce) {
            this.lastForce = lastForce;
        }

        public String getUpdateurl() {
            return updateurl;
        }

        public void setUpdateurl(String updateurl) {
            this.updateurl = updateurl;
        }

        public String getUpgradeinfo() {
            return upgradeinfo;
        }

        public void setUpgradeinfo(String upgradeinfo) {
            this.upgradeinfo = upgradeinfo;
        }
    }

    public UpdateInfo getData() {
        return data;
    }

    public void setData(UpdateInfo data) {
        this.data = data;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }


}
