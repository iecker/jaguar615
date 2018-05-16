/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Configura listeners eventos
 *  
 * @author Pedro Henrique
 *
 */
public abstract class PlcBaseManager {
	
	@Inject
	protected transient Logger log; 

	public abstract void rollback() ;

	public abstract void commit()  ;	


}
