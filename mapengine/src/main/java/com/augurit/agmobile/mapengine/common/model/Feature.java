/**
 * 
 * ACCMap - ACC map development platform
 * Copyright (c) 2015, AfirSraftGarrier, afirsraftgarrier@qq.com
 * 
 */
package com.augurit.agmobile.mapengine.common.model;

import java.util.HashMap;
import java.util.Map;

//import com.augur.android.agcom.model.http.common.Geometry;

public class Feature {
	private boolean showInLayer;
	private String annoValue;
	private String objectid;
	private String label;
	private Geometry geometry;
	private Map values;
	public boolean ischeck = false;

	private Long projectLayerID;

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Map getValues() {
		return values;
	}

	public void setValues(Map values) {
		this.values = values;
	}

	public String getObjectid() {
		return objectid;
	}

	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}

	public String getAnnoValue() {
		return annoValue;
	}

	public void setAnnoValue(String annoValue) {
		this.annoValue = annoValue;
	}

	public boolean isShowInLayer() {
		return showInLayer;
	}

	public void setShowInLayer(boolean showInLayer) {
		this.showInLayer = showInLayer;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Feature getClone() {
		Geometry agGeometry = new Geometry();
		// agGeometry.setType(this.geometry.getType());
		agGeometry.setWkt(this.geometry.getWkt());
		Feature agFeature = new Feature();
		agFeature.setAnnoValue(this.annoValue);
		agFeature.setGeometry(agGeometry);
		agFeature.setLabel(this.label);
		agFeature.setObjectid(this.objectid);
		agFeature.setShowInLayer(this.showInLayer);
		Map valuesClone = new HashMap();
		for (Object value : this.values.entrySet()) {
			Map.Entry vl = (Map.Entry) value;
			valuesClone.put(vl.getKey(), vl.getValue());
		}
		agFeature.setValues(valuesClone);
		return agFeature;
	}

	public boolean valueCopyTo(Feature agFeature) {
		Geometry agGeometry = agFeature.getGeometry();
		// agGeometry.setType(this.geometry.getType());
		agGeometry.setWkt(this.geometry.getWkt());
		agFeature.setAnnoValue(this.annoValue);
		agFeature.setGeometry(agGeometry);
		agFeature.setLabel(this.label);
		agFeature.setObjectid(this.objectid);
		agFeature.setShowInLayer(this.showInLayer);
		Map valuesClone = new HashMap();
		for (Object value : this.values.entrySet()) {
			Map.Entry vl = (Map.Entry) value;
			valuesClone.put(vl.getKey(), vl.getValue());
		}
		agFeature.setValues(valuesClone);
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Feature))
			return false;
		Feature feature = (Feature) o;
		return feature.getProjectLayerID() == this.projectLayerID
				&& feature.getObjectid() == this.objectid;
	}

	public void setProjectLayerID(Long projectLayerID) {
		this.projectLayerID = projectLayerID;
	}

	public Long getProjectLayerID() {
		return projectLayerID;
	}

	public void setProjectLayerID(long projectLayerID2) {
		this.projectLayerID = projectLayerID2;
	}
}