/* ****************************** META-DADOS COMUNS DA APLICAÃÃO ****************************
  ********************** Defaults de Valores de DeclaraÃ§Ã£o Global ****************************
  ************** Deve ser empacotado em todas as camadas - WAR e JARs EJBs, quando remotos ***
  *******************************************************************************************/

@PlcConfigSuffixClass(entity="Entity",repository="Repository")

@PlcConfigPackage (entity=".entity.", application="com.acme.planos_assist")

package com.powerlogic.jcompany.config.commons.pla;

import com.powerlogic.jcompany.config.application.PlcConfigPackage;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;
