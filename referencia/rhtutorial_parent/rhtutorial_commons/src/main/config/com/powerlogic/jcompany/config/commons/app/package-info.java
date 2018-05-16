/* ****************************** META-DADOS COMUNS DA APLICAÃÃO ****************************
  ********************** Defaults de Valores de DeclaraÃ§Ã£o Global ****************************
  ************** Deve ser empacotado em todas as camadas - WAR e JARs EJBs, quando remotos ***
  *******************************************************************************************/

@PlcConfigSuffixClass(entity="Entity",repository="Repository")

@PlcConfigPackage (entity=".entity.", application="com.empresa.rhtutorial")

package com.powerlogic.jcompany.config.commons.app;

import com.powerlogic.jcompany.config.application.PlcConfigPackage;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;
