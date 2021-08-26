/**
 * 
 * ACCMap - ACC map development platform
 * Copyright (c) 2015, AfirSraftGarrier, afirsraftgarrier@qq.com
 * 
 */
package com.augurit.agmobile.mapengine.common.model;

import java.util.List;

public class LayerMis {
	private String id;
	private String projectLayerId;
	private String projectLayerAlias;
	private String layerFields;
	private List<String> layerValues;
	// private String[] layerValues;
	private String misTable;
	private String misTableFields;
	private String relationName;
	private String misTableEditable;
	private String remark;
	private String isUserField;
	private String usedField;
	private String usedFieldValue;
	private MisDataSource dataSource;
	private String layerMis;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectLayerId() {
		return projectLayerId;
	}

	public void setProjectLayerId(String projectLayerId) {
		this.projectLayerId = projectLayerId;
	}

	public String getLayerFields() {
		return layerFields;
	}

	public void setLayerFields(String layerFields) {
		this.layerFields = layerFields;
	}

	public String getMisTable() {
		return misTable;
	}

	public void setMisTable(String misTable) {
		this.misTable = misTable;
	}

	public String getMisTableFields() {
		return misTableFields;
	}

	public void setMisTableFields(String misTableFields) {
		this.misTableFields = misTableFields;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getMisTableEditable() {
		return misTableEditable;
	}

	public void setMisTableEditable(String misTableEditable) {
		this.misTableEditable = misTableEditable;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsUserField() {
		return isUserField;
	}

	public void setIsUserField(String isUserField) {
		this.isUserField = isUserField;
	}

	public String getUsedField() {
		return usedField;
	}

	public void setUsedField(String usedField) {
		this.usedField = usedField;
	}

	public String getUsedFieldValue() {
		return usedFieldValue;
	}

	public void setUsedFieldValue(String usedFieldValue) {
		this.usedFieldValue = usedFieldValue;
	}

	public List<String> getLayerValues() {
		return layerValues;
	}

	public void setLayerValues(List<String> layerValues) {
		this.layerValues = layerValues;
	}

	// public String[] getLayerValues() {
	// return layerValues;
	// }
	//
	// public void setLayerValues(String[] layerValues) {
	// this.layerValues = layerValues;
	// }

	public MisDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(MisDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getLayerMis() {
		return layerMis;
	}

	public void setLayerMis(String layerMis) {
		this.layerMis = layerMis;
	}

	public void setProjectLayerAlias(String projectLayerAlias) {
		this.projectLayerAlias = projectLayerAlias;
	}

	public String getProjectLayerAlias() {
		return projectLayerAlias;
	}
}