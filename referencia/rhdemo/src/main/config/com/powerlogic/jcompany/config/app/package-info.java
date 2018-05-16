/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
 /** ************************* META-DADOS GLOBAIS DA APLICAÃÃO ******************************
  ********************** ConfiguraÃ§Ãµes padrÃ£o para toda a aplicaÃ§Ã£o *************************
  ************ Obs: configuraÃ§Ãµes corporativas devem estar no nÃ­Â­vel anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/


@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="rhdemo",acronym="rhdemo",version=1,release=0),
	classesDiscreteDomain={br.com.plc.rhdemo.entity.Sexo.class},
	classesLookup={br.com.plc.rhdemo.entity.UfEntity.class}
)

package com.powerlogic.jcompany.config.app;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;

