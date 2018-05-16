/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.batch;

import java.util.TimerTask;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcExceptionHandlerUtil;

/**
 *  jCompany 3.0. Ancestral para lógicas batch temporais. Descendentes devem implementar o método "run"
 * e pegar façade com getFacadeService.
 * @since jCompany 3.0
*/
abstract public class PlcBaseTimerTask extends TimerTask {
	
	@Inject
	protected transient Logger log;

	public void run() {

		try {
			runApi();
		} catch (Exception e) {
			// Tratamento genérico de exceção. Neste caso instancia o serviço de tratamento, pois não há DI e uso é raro.
			PlcExceptionHandlerUtil exceptionHandlerUtil = (PlcExceptionHandlerUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcExceptionHandlerUtil.class, QPlcDefaultLiteral.INSTANCE);
			PlcException eAux = exceptionHandlerUtil.interpretExcecao(e);
			exceptionHandlerUtil.handleErrorsWrapperModel(eAux);
		}
	}
	
	/**
	 * Template Method para disparo de rotinas batch. O descendente deve executar uma ou mais chamadas de serviços (preferencialmente utilizando-se
	 * de transações do Façade), e encapsular exceções em PlcException, para o tratamento genérico.
	 * @since jCompany 3.04
	 *  Exceção controlada ou 'wrapper' contendo em causaRaiz a exceção raiz.
	 */
 	abstract protected void runApi() ;


}

