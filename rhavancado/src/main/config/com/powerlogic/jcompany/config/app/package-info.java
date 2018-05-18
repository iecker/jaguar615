/* ************************* META-DADOS GLOBAIS DA APLICAÇÃO ******************************
  ********************** Configurações padrão para toda a aplicação *************************
  ************ Obs: configurações corporativas devem estar no nível anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/


@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="rhavancado", acronym="rhavancado", version=1, release=0),
	classesDiscreteDomain={com.empresa.rhavancado.entity.Sexo.class,com.empresa.rhavancado.entity.EstadoCivil.class},
	classesLookup={com.empresa.rhavancado.entity.UnidadeOrganizacionalEntity.class,com.empresa.rhavancado.entity.EnderecoEntity.class}
)


package com.powerlogic.jcompany.config.app;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
