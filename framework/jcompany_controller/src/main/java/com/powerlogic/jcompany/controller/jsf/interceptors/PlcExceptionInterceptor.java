/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.interceptors;



import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

/**
 * Interceptor para tratamento genérico de exceção.
 * Para para o {@link PlcExceptionHandlerJSF} a exceção capturada.
 */
@PlcHandleException 
@Interceptor
public class PlcExceptionInterceptor implements Serializable{
	
	private static final long serialVersionUID = 6503576371962602162L;
	
	@Inject
	protected transient Logger log;

	@Inject @QPlcDefault 
	protected PlcExceptionHandlerJSF plcExceptionHandlerJSF;
	
	@AroundInvoke
	public Object handleExceptions(InvocationContext invocation) throws Exception {
	    try {
	    	return invocation.proceed();
		} catch (Exception e) {
			log.debug( "####### Vai chamar PlcExceptionHandlerJSF para Excetion do tipo " + e.getClass().getName());
			if(!(e instanceof PlcException)) {
				if(e.getCause() != null && e.getCause() instanceof PlcException) {
					plcExceptionHandlerJSF.handle(((PlcException) e.getCause()));
				} else {
					plcExceptionHandlerJSF.handle(e);
				}
			} else {
				plcExceptionHandlerJSF.handle(e);
			}
		}
	    return null;
	}
	
}
