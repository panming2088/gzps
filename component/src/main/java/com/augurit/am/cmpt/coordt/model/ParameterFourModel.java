package com.augurit.am.cmpt.coordt.model;

import java.util.List;

/**
 * 四参数模型
 */
public class ParameterFourModel {
	private SpaceRange range;
	private List coincidentPoint;
	private ParameterFour parameterFour;
	private double D2;

	public SpaceRange getRange() {
		return range;
	}

	public void setRange(SpaceRange range) {
		this.range = range;
	}

	public List getCoincidentPoint() {
		return coincidentPoint;
	}

	public void setCoincidentPoint(List coincidentPoint) {
		this.coincidentPoint = coincidentPoint;
	}

	public ParameterFour getParameterFour() {
		return parameterFour;
	}

	public void setParameterFour(ParameterFour parameterFour) {
		this.parameterFour = parameterFour;
	}

	public double getD2() {
		return D2;
	}

	public void setD2(double d2) {
		D2 = d2;
	}

}
