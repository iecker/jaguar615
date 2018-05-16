/* ************************* META-DADOS GLOBAIS DA APLICAÃÃO ******************************
  ********************** ConfiguraÃ§Ãµes padrÃ£o para toda a aplicaÃ§Ã£o *************************
  ************ Obs: configuraÃ§Ãµes corporativas devem estar no nÃ­vel anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/


@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="rhtutorial",acronym="rhtutorial",version=1,release=0),
	classesDiscreteDomain={com.empresa.rhtutorial.entity.proventodesconto.NaturezaProventoDesconto.class,com.empresa.rhtutorial.entity.funcionario.EstadoCivil.class,com.empresa.rhtutorial.entity.funcionario.Sexo.class},
	classesLookup={com.empresa.rhtutorial.entity.UfEntity.class}
)


package com.powerlogic.jcompany.config.app;

import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;

