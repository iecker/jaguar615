/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.empresa.controller.jsf;

import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

/**
 * {@inheritDoc}
 */
@SPlcMB
@Specializes
@PlcHandleException
public class EmpBaseMB extends PlcBaseMB {

	private static final long serialVersionUID = 1L;

	@Inject
	protected transient Logger log;

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public String search() {
		log.info("MÃ©todo pesquisa da bridge");
		return super.search();
	}

}
