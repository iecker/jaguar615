/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons;


/**
 * Singleton que mantem a referencia de qual ambiente esta sendo executado o codigo.
 * 
 * Utilizado para adicionar comportamento a aplicacao dependendo do ambiento do qual ela esta sendo executada.
 * 
 * Deve ser um singleton pois existem alguns ambientes que o CDI nao e suportado. 
 * 
 * Exemplo: testes unitarios.
 * 
 */
public class PlcExecutionMode {
	
	private static PlcExecutionMode instance = new PlcExecutionMode();
	
	private Mode currentExecutionMode =  Mode.PRODUCTION;
	
	public static final Mode PRODUCTION_MODE = Mode.PRODUCTION;
	public static final Mode STAGING_MODE = Mode.STAGING;
	public static final Mode TEST_MODE = Mode.TEST;
	public static final Mode DEVELOPMENT_MODE = Mode.DEVELOPMENT;
	public static final Mode UNIT_TEST_MODE = Mode.UNIT_TEST;
	
	
	private PlcExecutionMode(){}
	
	public static PlcExecutionMode getInstance() {
		return instance;
	}

	public Mode getCurrentExecutionMode() {
		return currentExecutionMode;
	}

	public void setCurrentExecutionMode(Mode currentExecutionMode) {
		this.currentExecutionMode = currentExecutionMode;
	}
	
	public boolean isTest(){
		return currentExecutionMode==TEST_MODE;
	}
	
	public boolean isStaging(){
		return currentExecutionMode==STAGING_MODE;
	}
	
	public boolean isProduction(){
		return currentExecutionMode==PRODUCTION_MODE;
	}
	
	public boolean isDevelopment(){
		return currentExecutionMode==DEVELOPMENT_MODE;
	}
	
	public boolean isUnitTest(){
		return currentExecutionMode==UNIT_TEST_MODE;
	}
}

enum Mode {
	
	PRODUCTION,
	STAGING,
	TEST,
	DEVELOPMENT,
	UNIT_TEST
}