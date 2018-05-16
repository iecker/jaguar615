/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.util.cdi;

import java.io.Serializable;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.log4j.Logger;

public class PlcLoggerProducer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1517654265839262624L;

	@Produces
	public Logger createLogger(InjectionPoint ip) {
		if (ip!=null && ip.getBean()!=null){
			return Logger.getLogger(ip.getBean().getBeanClass().getName());
		} else {
			return Logger.getLogger(this.getClass().getName());
		}
	}
}
