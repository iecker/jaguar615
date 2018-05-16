/* ************************* META-DADOS GLOBAIS DA APLICAÃÃO ******************************
  ********************** ConfiguraÃ§Ãµes padrÃ£o para toda a aplicaÃ§Ã£o *************************
  ************ Obs: configuraÃ§Ãµes corporativas devem estar no nÃ­vel anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/


@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="acme_mod_negocio_funcionario", acronym="acm", version=1, release=0),
	classesDiscreteDomain={},
	classesLookup={}
)


package com.powerlogic.jcompany.config.acm;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
