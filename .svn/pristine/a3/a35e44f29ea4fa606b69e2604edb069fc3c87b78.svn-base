package com.augurit.am.cmpt.widget.HorizontalScrollPhotoView;

import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.HorizontalScrollPhotoView
 * @createTime 创建时间 ：17/3/8
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/8
 * @modifyMemo 修改备注：
 */

public class Photo implements Serializable{
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private long dbid;
    private long id;
    private String photoName;//图片名字
    private String photoPath;//图片完整路径（可服务端的或手机本地的）
    private String localPath;//手机本地图片路径，主要用于本地图片删除
    private Date photoTime;//拍摄时间
    private long typeId;//类型ID
    private int type;//类型，如问题、任务、子系统
    private int hasBeenUp;//是否已上传:0为未上传；1为已上传
    private String thumbPath;//缩略图路径

    private int hasBeenSave;//是否已保存：0未保存；1已保存

    private String problem_id; //上报内容ID
    private String field1;  // 图片控件Field

    public Photo() {//默认构造函数必须写，用于构造Dao
    }

    public int getHasBeenUp() {
        return hasBeenUp;
    }

    public void setHasBeenUp(int hasBeenUp) {
        this.hasBeenUp = hasBeenUp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDbid() {
        return dbid;
    }

    public void setDbid(long dbid) {
        this.dbid = dbid;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Date getPhotoTime() {
        return photoTime;
    }

    public void setPhotoTime(Date photoTime) {
        this.photoTime = photoTime;
    }

    public int getHasBeenSave() {
        return hasBeenSave;
    }

    public void setHasBeenSave(int hasBeenSave) {
        this.hasBeenSave = hasBeenSave;
    }

    public String getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(String problem_id) {
        this.problem_id = problem_id;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }
}
