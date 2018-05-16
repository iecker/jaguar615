package com.empresa.rhtutorial.importacao.metadata;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.powerlogic.jcompany.config.metadata.PlcMetaConfig;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Layer;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Scope;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditor;

@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@PlcMetaConfig(root = true, scope = Scope.APP, layer = Layer.CONTROLLER)
@PlcMetaEditor(label = "Consulta Manter Classe", description = "Configurações complementares para Manter Classe Somente Consulta")

/**
 * Configurações globais de definição de logica manter classe somente consulta 
 **/
 

public @interface EmpConfigImportacao {
	boolean ehConsultaManterClasse();
}
