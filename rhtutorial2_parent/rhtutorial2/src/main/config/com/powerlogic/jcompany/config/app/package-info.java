/* ************************* META-DADOS GLOBAIS DA APLICAÇÃO ******************************
  ********************** Configurações padrão para toda a aplicação *************************
  ************ Obs: configurações corporativas devem estar no nível anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/


@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="rhtutorial2",acronym="rhtutorial2",version=1,release=0),
	classesDiscreteDomain={com.empresa.rhtutorial2.entity.funcionario.EstadoCivil.class,com.empresa.rhtutorial2.entity.funcionario.Sexo.class},
	classesLookup={com.empresa.rhtutorial2.entity.UfEntity.class}
)


package com.powerlogic.jcompany.config.app;

import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
