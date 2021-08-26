/**
 * 
 * ACCMap - ACC map development platform
 * Copyright (c) 2015, AfirSraftGarrier, afirsraftgarrier@qq.com
 * 
 */
package com.augurit.agmobile.mapengine.layerquery.model;

import java.util.List;

public class SpatialQueryParam {
	private Integer endRow;
	private Integer startRow;
	private Long projectLayerId;
	private Double filtrateNum;
	private boolean isReturnCount;
	private boolean isReturnMis;
	private String queryValue;
	private boolean returnAlias;
	private boolean returnGeometry;
	private boolean returnValues;
	private String where;
	private String orderbyFields;
	private List<String> geometry;
	private List<String> outFields;
	private String groupbyFields;
	private String spatialRelationship;

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public boolean isReturnCount() {
		return isReturnCount;
	}

	public void setReturnCount(boolean isReturnCount) {
		this.isReturnCount = isReturnCount;
	}

	public boolean isReturnMis() {
		return isReturnMis;
	}

	public void setReturnMis(boolean isReturnMis) {
		this.isReturnMis = isReturnMis;
	}

	// public boolean isQueryValue() {
	// return queryValue;
	// }
	//
	// public void setQueryValue(boolean queryValue) {
	// this.queryValue = queryValue;
	// }

	public boolean isReturnAlias() {
		return returnAlias;
	}

	public void setReturnAlias(boolean returnAlias) {
		this.returnAlias = returnAlias;
	}

	public boolean isReturnGeometry() {
		return returnGeometry;
	}

	public void setReturnGeometry(boolean returnGeometry) {
		this.returnGeometry = returnGeometry;
	}

	public boolean isReturnValues() {
		return returnValues;
	}

	public void setReturnValues(boolean returnValues) {
		this.returnValues = returnValues;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getOrderbyFields() {
		return orderbyFields;
	}

	public void setOrderbyFields(String orderbyFields) {
		this.orderbyFields = orderbyFields;
	}

	public List<String> getGeometry() {
		return geometry;
	}

	public void setGeometry(List<String> geometry) {
		this.geometry = geometry;
	}

	public String getGroupbyFields() {
		return groupbyFields;
	}

	public void setGroupbyFields(String groupbyFields) {
		this.groupbyFields = groupbyFields;
	}

	public String getSpatialRelationship() {
		return spatialRelationship;
	}

	public void setSpatialRelationship(String spatialRelationship) {
		this.spatialRelationship = spatialRelationship;
	}

	public void setEndRow(Integer endRow) {
		this.endRow = endRow;
	}

	public Integer getEndRow() {
		return endRow;
	}

	public void setOutFields(List<String> outFields) {
		this.outFields = outFields;
	}

	public List<String> getOutFields() {
		return outFields;
	}

	public void setProjectLayerId(Long projectLayerId) {
		this.projectLayerId = projectLayerId;
	}

	public Long getProjectLayerId() {
		return projectLayerId;
	}

	public void setFiltrateNum(Double filtrateNum) {
		this.filtrateNum = filtrateNum;
	}

	public Double getFiltrateNum() {
		return filtrateNum;
	}

	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}

	public String getQueryValue() {
		return queryValue;
	}
}