/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */ 
package com.acme.rhdemoenterprise.controller.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;


/**
 * Classe de factory para servir como fÃ¡brica de criaÃ§Ã£o de classes utilitÃ¡rias
 */
@Named("AppServiceFactory")
@ApplicationScoped
public class AppServiceFactory {
	
	/**
	 *
	 * jCompany 6.0.0 - Exemplo de mÃ©todo para gerar PlcEntityUtil
	 *
	@Factory(value="entityComunsUtil")
	public PlcEntityUtil geraEntidadeService() {
		return (PlcEntityUtil)Component.getInstance("AppMinhaEntidadeService",true);

	}
	*/ 

}
