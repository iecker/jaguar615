/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf;

import javax.inject.Inject;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;

/**
 * Mock para apoio aos testes da classe @link PlcBaseMB
 * @author Pedro Henrique
 *
 */
public class PlcBaseMBMock extends PlcBaseMB {
	
	private static final long serialVersionUID = 2237832004226902927L;

	@Inject @QPlcDefault
	PlcCreateContextUtil contextMontaUtil;

	
	
}
