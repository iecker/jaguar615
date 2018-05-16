/* ******************* REDEFINIÇÕES DE META-DADOS GLOBAIS DA EMPRESA **********************
 ********************** Configurações default para toda a empresa *************************
 *******************************************************************************************/
			
@PlcConfigCompany(name = "Powerlogic SA", domain = "www.powerlogic.com.br", acronym = "PLC", 
			logo = "/recursos/midia/login-logo-empresa.png", address = "Rua Paraíba, 330 / 19o andar. CEP:30113-140 - Belo Horizonte/MG",
			supportEmail = "suporte@powerlogic.com.br",supportPhone = "55 31 3555-0050")
			
@PlcConfigLookAndFeel(skin = "itunes", layout = "sistema")

@PlcConfigSuffixClass (entity="Entity", repository="Repository")
			    	 
package com.powerlogic.jcompany.config.emp;

import com.powerlogic.jcompany.config.application.PlcConfigCompany;
import com.powerlogic.jcompany.config.application.PlcConfigLookAndFeel;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;

