/*  																													
	    				   jCompany Developer Suite																		
			    		Copyright (C) 2008  Powerlogic																	
	 																													
	    Este programa Ã© licenciado com todos os seus cÃ³digos fontes. VocÃª pode modificÃ¡-los e							
	    utilizÃ¡-los livremente, inclusive distribuÃ­-los para terceiros quando fizerem parte de algum aplicativo 		
	    sendo cedido, segundo os termos de licenciamento gerenciado de cÃ³digo aberto da Powerlogic, definidos			
	    na licenÃ§a 'Powerlogic Open-Source Licence 2.0 (POSL 2.0)'.    													
	  																													
	    A Powerlogic garante o acerto de erros eventualmente encontrados neste cÃ³digo, para os clientes licenciados, 	
	    desde que todos os cÃ³digos do programa sejam mantidos intactos, sem modificaÃ§Ãµes por parte do licenciado. 		
	 																													
	    VocÃª deve ter recebido uma cÃ³pia da licenÃ§a POSL 2.0 juntamente com este programa.								
	    Se nÃ£o recebeu, veja em <http://www.powerlogic.com.br/licencas/posl20/>.										
	 																													
	    Contatos: plc@powerlogic.com.br - www.powerlogic.com.br 														
																														
 */ 
 /** ************************* META-DADOS GLOBAIS DA APLICAÃÃO ******************************
  ********************** ConfiguraÃ§Ãµes padrÃ£o para toda a aplicaÃ§Ã£o *************************
  ************ Obs: configuraÃ§Ãµes corporativas devem estar no nÃ­vel anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/

@PlcConfigApplication(
	modules={@PlcConfigModule(acronym="pla", name="modulo_planos_assistenciais", description="modulo_planos_assistenciais"),@PlcConfigModule(acronym="acm", name="acme_modulo_cpf", description="acme_modulo_cpf")},
	definition=@PlcConfigApplicationDefinition(name="rhdemoenterprise",acronym="rhdemoenterprise",version=1,release=0),
	classesDiscreteDomain={com.acme.rhdemoenterprise.entity.EstadoCivil.class, com.acme.rhdemoenterprise.entity.Sexo.class, com.powerlogic.jcompany.extension.manytomanymatrix.entity.OpcaoMatrix.class},
	classesLookup={com.acme.rhdemoenterprise.entity.HabilidadeEntity.class,com.acme.rhdemoenterprise.entity.UfEntity.class}
)

package com.powerlogic.jcompany.config.app;

import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
import com.powerlogic.jcompany.config.application.PlcConfigModule;

