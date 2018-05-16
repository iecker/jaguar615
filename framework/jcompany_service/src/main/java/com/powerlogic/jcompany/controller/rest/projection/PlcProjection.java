package com.powerlogic.jcompany.controller.rest.projection;

import java.util.List;

import javax.inject.Inject;

import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcProjection;

public class PlcProjection {

	@Inject
	@QPlcProjection
	private List<PlcProjectionProperty> properties;

	@Inject @QPlcProjection
	private String alias;

	public List<PlcProjectionProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<PlcProjectionProperty> properties) {
		this.properties = properties;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
