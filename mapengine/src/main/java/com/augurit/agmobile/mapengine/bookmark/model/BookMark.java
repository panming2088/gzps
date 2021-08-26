
package com.augurit.agmobile.mapengine.bookmark.model;


import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.annotation.Table;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;
import com.esri.core.geometry.Geometry;

import java.util.Date;

/**
 * 书签实体类
 * @author 创建人 ：
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.favo.bean.agmobile
 * @createTime 创建时间 ：
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2016-11-29 15:10
 */
@Table("bookmark")
public class BookMark {

	@PrimaryKey(AssignType.AUTO_INCREMENT)
	private int bookMarkId;		// 书签Id，自增
	private Geometry envelope;		// 书签对应地图范围
	private String markName;		// 书签名
	private String remark;			// 备注
	private String userId;			// 书签对应用户Id
	private String userName;		// 书签对应用户名
	private String projectId;		// 书签所属专题Id
	private Date createDate;		// 书签创建时间

	public Geometry getEnvelope() {
		return envelope;
	}

	public void setEnvelope(Geometry envelope) {
		this.envelope = envelope;
	}

	public int getBookMarkId() {
		return this.bookMarkId;
	}

	public void setBookMarkId(int bookMarkId) {
		this.bookMarkId = bookMarkId;
	}
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMarkName() {
		return this.markName;
	}

	public void setMarkName(String markName) {
		this.markName = markName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}