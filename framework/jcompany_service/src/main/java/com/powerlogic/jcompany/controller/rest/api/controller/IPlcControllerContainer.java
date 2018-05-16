/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.api.controller;

import java.util.Collection;

public interface IPlcControllerContainer<E, I> {
	
	void setIdentifier(I identifier);
	
	I getIdentifier();

	void setEntity(E entidade);
	
	E getEntity();
	
	void setEntityCollection(Collection<E> entidade);
	
	Collection<E> getEntityCollection();
	
	Class<E> getEntityType();
	
	Class<I> getIdType();
}
