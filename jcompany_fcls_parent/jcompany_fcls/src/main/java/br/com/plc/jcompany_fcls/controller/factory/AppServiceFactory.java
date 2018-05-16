/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.jcompany_fcls.controller.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;


/**
 * jcompany_fcls . 
 * Classe de factory para servir como fÃ¡brica de criaÃ§Ã£o de classes de serviÃ§o
 */

@Named("AppServiceFactory")
@ApplicationScoped
public class AppServiceFactory {

	
	/**
	 *
	 * jCompany 5.1.0 - Exemplo de mÃ©todo para gerar PlcEntityUtil
	 *
	@Factory(value="entityComunsUtil")
	public PlcEntityUtil geraEntidadeService() {
		return (PlcEntityUtil)Component.getInstance("AppMinhaEntidadeService",true);

	}
	*/ 

}
