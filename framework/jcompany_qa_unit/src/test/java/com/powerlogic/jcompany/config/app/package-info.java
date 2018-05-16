/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
  /** ************************* META-DADOS GLOBAIS DA APLICAÇÃO ******************************
  ********************** Configurações padrão para toda a aplicação *************************
  ************ Obs: configurações corporativas devem estar no nível anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/

@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="jcompany_fcls",acronym="jcompany_fcls",version=1,release=0),
	classesDiscreteDomain={},
	classesLookup={}
)

/*
@PlcConfigComportamento(containerGerenciaTransacao=true)
@PlcConfigModeloTecnologia ( tecnologia= PlcConfigModeloTecnologia.Tecnologia.EJB3)
@PlcConfigEjbFacadeRef(nomePrefixoJNDIApp="ejb", nomeFacadeApp= "AppFacadeImpl")
*/

package com.powerlogic.jcompany.config.app;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
 
