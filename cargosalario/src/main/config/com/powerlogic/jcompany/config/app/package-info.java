/* ************************* META-DADOS GLOBAIS DA APLICAÇÃO ******************************
  ********************** Configurações padrão para toda a aplicação *************************
  ************ Obs: configurações corporativas devem estar no nível anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/


@PlcConfigApplication(


	modules={@PlcConfigModule(acronym="css", name="cargosalarioservice", description="cargosalarioservice")},	definition=@PlcConfigApplicationDefinition(name="cargosalario", acronym="cargosalario", version=1, release=0),
	classesDiscreteDomain={},
	classesLookup={}
)


package com.powerlogic.jcompany.config.app;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
import com.powerlogic.jcompany.config.application.PlcConfigModule;