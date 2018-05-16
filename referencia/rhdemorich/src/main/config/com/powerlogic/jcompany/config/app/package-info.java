/* ************************* META-DADOS GLOBAIS DA APLICAÇÃO ******************************
  ********************** Configurações padrão para toda a aplicação *************************
  ************ Obs: configurações corporativas devem estar no nível anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/

@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="RHDEMO RichFaces", acronym="rhdemorich", version=1, release=0),
	classesDiscreteDomain={com.empresa.rhrich.entity.Sexo.class,com.empresa.rhrich.entity.EstadoCivil.class},
	classesLookup={com.empresa.rhrich.entity.DepartamentoEntity.class,com.empresa.rhrich.entity.UfEntity.class},
	classesLookupOrderBy={"nome asc"}
)


package com.powerlogic.jcompany.config.app;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;


