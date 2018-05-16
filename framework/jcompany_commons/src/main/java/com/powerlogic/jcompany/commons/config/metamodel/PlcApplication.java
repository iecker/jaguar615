/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.config.metamodel;

public class PlcApplication {

	private Class<?> facadeClassDefault;
	private Class<?> businessObjectClassDefault;
	private Class<?> dataAcccessObjectClassDefault;

	public void setFacadeClassDefault(Class<?> facadeClassDefault) {
		this.facadeClassDefault = facadeClassDefault;
	}

	public Class<?> getFacadeClassDefault() {
		return facadeClassDefault;
	}

	public void setBusinessObjectClassDefault(Class<?> businessObjectClassDefault) {
		this.businessObjectClassDefault = businessObjectClassDefault;
	}

	public Class<?> getBusinessObjectClassDefault() {
		return businessObjectClassDefault;
	}

	public void setDataAcccessObjectClassDefault(Class<?> dataAcccessObjectClassDefault) {
		this.dataAcccessObjectClassDefault = dataAcccessObjectClassDefault;
	}

	public Class<?> getDataAcccessObjectClassDefault() {
		return dataAcccessObjectClassDefault;
	}

}
