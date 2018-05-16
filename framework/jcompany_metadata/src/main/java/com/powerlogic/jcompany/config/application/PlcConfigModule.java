/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.application;

/**
 * @since jCompany 5.0
 * Configuraçòes principais para módulos da aplicação 
 */
public @interface PlcConfigModule {
	/**
	 * Define a sigla do módulo
	 */
	String acronym();
	
	/**
	 * Define o nome do módulo
	 */
	String name();
	
	/**
	 * Define a descrição do módulo
	 */
	String description();
}
