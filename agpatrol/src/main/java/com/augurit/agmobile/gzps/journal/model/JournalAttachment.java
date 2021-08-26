package com.augurit.agmobile.gzps.journal.model;

import java.io.Serializable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.journal.model
 * @createTime 创建时间 ：17/11/4
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/4
 * @modifyMemo 修改备注：
 */

public class JournalAttachment  implements Serializable {
    /**
     * 所在标识的id
     */
    private String markId;

    /**
     * 附件id
     */
    private String id;

    /**
     * 附件生成时间
     */
    private Long attTime;

    /**
     * 附件格式,如：image/png
     */
    private String mime;

    /**
     * 附件相对路径
     */
    private String attPath;

    /**
     * 附件名
     */
    private String attName;

    /**
     * 缩略图
     */
    private String thumPath;


    public String getMarkId() {
        return markId;
    }

    public void setMarkId(String markId) {
        this.markId = markId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAttTime() {
        return attTime;
    }

    public void setAttTime(Long attTime) {
        this.attTime = attTime;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getAttPath() {
        return attPath;
    }

    public void setAttPath(String attPath) {
        this.attPath = attPath;
    }

    public String getAttName() {
        return attName;
    }

    public void setAttName(String attName) {
        this.attName = attName;
    }

    public String getThumPath() {
        return thumPath;
    }

    public void setThumPath(String thumPath) {
        this.thumPath = thumPath;
    }
}
