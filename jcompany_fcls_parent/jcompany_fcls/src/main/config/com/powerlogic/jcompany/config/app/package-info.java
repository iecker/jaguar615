/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
 /** ************************* META-DADOS GLOBAIS DA APLICAÃÃO ******************************
  ********************** ConfiguraÃ§Ãµes padrÃ£o para toda a aplicaÃ§Ã£o *************************
  ************ Obs: configuraÃ§Ãµes corporativas devem estar no nÃ­vel anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/

@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="jcompany_fcls",acronym="JCOMPANY_FCLS",version=1,release=0),
	classesDiscreteDomain={br.com.plc.jcompany_fcls.entity.enumerations.NaturezaProventoDesconto.class,br.com.plc.jcompany_fcls.entity.colaborador.EstadoCivil.class,br.com.plc.jcompany_fcls.entity.funcionario.Sexo.class, com.powerlogic.jcompany.extension.manytomanymatrix.entity.OpcaoMatrix.class},
	classesLookup={br.com.plc.jcompany_fcls.entity.categoria.CategoriaEntity.class,br.com.plc.jcompany_fcls.entity.uf.UfEntity.class,br.com.plc.jcompany_fcls.entity.suitecasoteste.SuiteEntity.class,br.com.plc.jcompany_fcls.entity.suitecasoteste.CasoTesteEntity.class,com.powerlogic.jcompany.domain.dynamicmenu.PlcDynamicMenuEntity.class}
	
)

package com.powerlogic.jcompany.config.app;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
 
