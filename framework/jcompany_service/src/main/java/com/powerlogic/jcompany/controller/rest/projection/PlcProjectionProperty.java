package com.powerlogic.jcompany.controller.rest.projection;

public class PlcProjectionProperty {

	private PlcProjectionProperty propertyParent;

	private String name;

	public PlcProjectionProperty(PlcProjectionProperty propertyParent, String name) {
		this.propertyParent = propertyParent;
		this.name = name;
	}

	public PlcProjectionProperty getPropertyParent() {
		return propertyParent;
	}

	public void setPropertyParent(PlcProjectionProperty propertyParent) {
		this.propertyParent = propertyParent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		if (propertyParent != null) {
			return propertyParent.toString() + "." + getName();
		} else {
			return getName();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			return this.toString().equals(obj.toString());
		}
		return false;
	}

}
