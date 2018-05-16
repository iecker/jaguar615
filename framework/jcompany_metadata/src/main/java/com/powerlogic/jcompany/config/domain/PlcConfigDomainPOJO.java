/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.domain;

public class PlcConfigDomainPOJO {

	private String despiseProperty;
	
	private boolean testDuplicity;

	public String despiseProperty() {
		return despiseProperty;
	}

	public void setDespiseProperty(String propFlagDesprezar) {
		this.despiseProperty = propFlagDesprezar;
	}

	/**
	 * @param testaDuplicata the testaDuplicata to set
	 */
	public void setTestDuplicity(boolean testaDuplicata) {
		this.testDuplicity = testaDuplicata;
	}

	/**
	 * @return the testaDuplicata
	 */
	public boolean testDuplicity() {
		return testDuplicity;
	}




	
}
