/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
 /** ******************* REDEFINIÃÃOES DE META-DADOS GLOBAIS DA EMPRESA **********************
  ********************** ConfiguraÃ§Ãµes default para toda a empresa *************************
  *******************************************************************************************/
			
@PlcConfigCompany(name = "Powerlogic SA", domain = "www.powerlogic.com.br", acronym = "PLC", 
			logo = "/res/midia/login-logo-empresa.png", address = "Rua ParaÃ­Â­ba, 330 / 19o andar. CEP:30113-140 - Belo Horizonte/MG",
			supportEmail = "suporte@powerlogic.com.br",supportPhone = "55 31 3555-0050")

@PlcConfigSuffixClass (entity="Entity",repository="Repository")

@PlcConfigPackage (entity=".entity.", application="br.com.plc.rhdemo")
			    	 
 
package com.powerlogic.jcompany.config.commons.emp;

import com.powerlogic.jcompany.config.application.PlcConfigCompany;
import com.powerlogic.jcompany.config.application.PlcConfigPackage;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;

