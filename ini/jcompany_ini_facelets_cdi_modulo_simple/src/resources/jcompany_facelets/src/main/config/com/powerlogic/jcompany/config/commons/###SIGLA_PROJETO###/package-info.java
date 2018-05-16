/* ****************************** META-DADOS COMUNS DA APLICAÇÃO ****************************
  ********************** Defaults de Valores de Declaração Global ****************************
  ************** Deve ser empacotado em todas as camadas - WAR e JARs EJBs, quando remotos ***
  *******************************************************************************************/

@PlcConfigSuffixClass(entity="Entity",repository="Repository")

@PlcConfigPackage (entity=".entity.", application="###NOME_PACOTE###")

package com.powerlogic.jcompany.config.commons.###SIGLA_PROJETO_MINUSCULA###;

import com.powerlogic.jcompany.config.application.PlcConfigPackage;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;
