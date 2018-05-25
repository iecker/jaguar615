/* ************************* META-DADOS GLOBAIS DA APLICAÇÃO ******************************
  ********************** Configurações padrão para toda a aplicação *************************
  ************ Obs: configurações corporativas devem estar no nível anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/


@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="cargosalarioservice", acronym="css", version=1, release=0),
	classesDiscreteDomain={},
	classesLookup={}
)

package com.powerlogic.jcompany.config.css;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
