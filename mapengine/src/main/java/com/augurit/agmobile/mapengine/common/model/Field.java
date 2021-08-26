/**
 * 
 * ACCMap - ACC map development platform
 * Copyright (c) 2015, AfirSraftGarrier, afirsraftgarrier@qq.com
 * 
 */
package com.augurit.agmobile.mapengine.common.model;

public class Field {
	private Long id;
	private String owner;
	private String fieldName;
	private String fieldAlias;
	private String fieldType;
	private int fieldOrder;
	private String showInResult;
	private String showInTitle;
	private String editable;
	private String nullable;
	private String uploadable;
	private String searchable;
	private String picturefield;
	private String isGroupField;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldAlias() {
		return fieldAlias;
	}

	public void setFieldAlias(String fieldAlias) {
		this.fieldAlias = fieldAlias;
	}

	public int getFieldOrder() {
		return fieldOrder;
	}

	public void setFieldOrder(int fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	public String getShowInResult() {
		return showInResult;
	}

	public void setShowInResult(String showInResult) {
		this.showInResult = showInResult;
	}

	public String getShowInTitle() {
		return showInTitle;
	}

	public void setShowInTitle(String showInTitle) {
		this.showInTitle = showInTitle;
	}

	public String getEditable() {
		return editable;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public String getUploadable() {
		return uploadable;
	}

	public void setUploadable(String uploadable) {
		this.uploadable = uploadable;
	}

	public String getSearchable() {
		return searchable;
	}

	public void setSearchable(String searchable) {
		this.searchable = searchable;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public void setPicturefield(String picturefield) {
		this.picturefield = picturefield;
	}

	public String getPicturefield() {
		return picturefield;
	}

	public void setIsGroupField(String isGroupField) {
		this.isGroupField = isGroupField;
	}

	public String getIsGroupField() {
		return isGroupField;
	}
}