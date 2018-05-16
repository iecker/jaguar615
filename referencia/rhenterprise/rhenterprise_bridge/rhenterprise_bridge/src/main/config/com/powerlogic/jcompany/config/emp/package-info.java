/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
 /** ******************* REDEFINIÇÕES DE META-DADOS GLOBAIS DA EMPRESA **********************
  ********************** Configurações default para toda a empresa *************************
  *******************************************************************************************/

@PlcConfigCompany(name = "Assoc. Contábil ME", domain = "www.acme.com.br", acronym = "ACME", 
		logo = "/res/midia/login-logo-empresa.png", address = "Rua X, 1",
		supportEmail = "suporte@acme.com.br", supportPhone = "55 11 5000-0000")
			
@PlcConfigLookAndFeel(skin = "acme", layout = "sistema")
			    	 
package com.powerlogic.jcompany.config.emp;

import com.powerlogic.jcompany.config.application.PlcConfigCompany;
import com.powerlogic.jcompany.config.application.PlcConfigLookAndFeel;