/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.extension.manytomanymatrix.metadata;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.powerlogic.jcompany.config.metadata.PlcMetaConfig;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditor;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Layer;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Scope;

/**
 * Interface que contem os metadados necessários para utilização do caso de uso Matrix
 * 
 * @author Mauren Ginaldo Souza
 *
 */
@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@PlcMetaConfig(root = true, scope={Scope.MAN, Scope.SEL}, layer=Layer.DOMAIN)
@PlcMetaEditor(label = "Detalhes Avançados", description = "Configurações complementares para detalhes ManyToMany Matrix")

public @interface PlcConfigManyToManyMatrix {

	public enum ManyToMany {
		SIMPLES, MATRIX
	}

	ManyToMany opcaoManyToMany() default ManyToMany.MATRIX;

	// Classe Matrix
	Class<?> classeAssociativa();
	
	// Propriedade entidade 1
	String propriedadeEntidade1();
	// Propriedade entidade 2
	String propriedadeEntidade2();
	
	// Classe Entity da entidade 1
	Class<?> classeEntidade1();
	// Classe Entity da entidade 2
	Class<?> classeEntidade2();
	
	//Propriedade transient que define o nome da propriedade na classe associativa do enum matrix
	String propriedadeOpcaoMatrix() default "opcaoMatrix";

	
}
