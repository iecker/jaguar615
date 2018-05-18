/* ************************* META-DADOS GLOBAIS DA APLICAÇÃO ******************************
  ********************** Configurações padrão para toda a aplicação *************************
  ************ Obs: configurações corporativas devem estar no nível anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/


@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="rhjboss",acronym="rhjboss",version=1,release=0),
	classesDiscreteDomain={},
	classesLookup={com.powerlogic.rhjboss.entity.UnidadeOrganizacionalEntity.class,com.powerlogic.rhjboss.entity.UfEntity.class}
)


package com.powerlogic.jcompany.config.app;

import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
