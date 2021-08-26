/**
 * 
 * ACCMap - ACC map development platform
 * Copyright (c) 2015, AfirSraftGarrier, afirsraftgarrier@qq.com
 * 
 */
package com.augurit.agmobile.mapengine.common.model;

import java.util.List;

public class FeatureSet {
	private String type;
	private String projectLayerId;
	private String annoFieldName;
	private String shapeFieldName;
	private String objectidFieldName;
	private String layerAliases;
	private String layerName;
	private String geometryType;
	private List<Field> fieldAliases;
	private List<Feature> features;
	private String queryParam;
	private List<LayerMis> layerMises;
	private String totalRows;
	private String mapZoomAndCenter;

	public String getMapZoomAndCenter() {
		return mapZoomAndCenter;
	}

	public void setMapZoomAndCenter(String mapZoomAndCenter) {
		this.mapZoomAndCenter = mapZoomAndCenter;
	}

	public String getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(String totalRows) {
		this.totalRows = totalRows;
	}

	public List<LayerMis> getLayerMises() {
		return layerMises;
	}

	public void setLayerMises(List<LayerMis> layerMises) {
		this.layerMises = layerMises;
	}

	public String getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public List<Field> getFieldAliases() {
		return fieldAliases;
	}

	public void setFieldAliases(List<Field> fieldAliases) {
		this.fieldAliases = fieldAliases;
	}

	public String getLayerAliases() {
		return layerAliases;
	}

	public void setLayerAliases(String layerAliases) {
		this.layerAliases = layerAliases;
	}

	public String getShapeFieldName() {
		return shapeFieldName;
	}

	public void setShapeFieldName(String shapeFieldName) {
		this.shapeFieldName = shapeFieldName;
	}

	public String getObjectidFieldName() {
		return objectidFieldName;
	}

	public void setObjectidFieldName(String objectidFieldName) {
		this.objectidFieldName = objectidFieldName;
	}

	public String getProjectLayerId() {
		return projectLayerId;
	}

	public void setProjectLayerId(String projectLayerId) {
		this.projectLayerId = projectLayerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAnnoFieldName() {
		return annoFieldName;
	}

	public void setAnnoFieldName(String annoFieldName) {
		this.annoFieldName = annoFieldName;
	}

	public void setGeometryType(String geometryType) {
		this.geometryType = geometryType;
	}

	public String getGeometryType() {
		return geometryType;
	}
}