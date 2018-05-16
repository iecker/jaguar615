/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.api.controller;

public interface IPlcController<E, I> extends IPlcControllerContainer<E, I> {

	public void retrieve(I identifier);

	public void insert();

	public void update();

	public void delete();

	public void retrieveCollection();
	
}
