/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
 /** ******************* REDEFINIÃÃES DE META-DADOS GLOBAIS DA EMPRESA **********************
  ********************** ConfiguraÃ§Ãµes default para toda a empresa *************************
  *******************************************************************************************/
@PlcConfigCompany(name = "Empresa S/A", domain = "www.powerlogic.com.br", acronym = "Emp", 
			logo = "/res/midia/marca_empresa.gif", address = "Rua X, 12",
			supportEmail = "suporte@empresa.com.br",supportPhone = "55 31 3286-1691")
			
@PlcConfigSuffixClass (entity="Entity",repository="Repository")

@PlcConfigPackage (entity=".entity.", application="br.com.plc.jcompany_fcls")
		
package com.powerlogic.jcompany.config.commons.emp;

import com.powerlogic.jcompany.config.application.PlcConfigCompany;
import com.powerlogic.jcompany.config.application.PlcConfigPackage;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;

