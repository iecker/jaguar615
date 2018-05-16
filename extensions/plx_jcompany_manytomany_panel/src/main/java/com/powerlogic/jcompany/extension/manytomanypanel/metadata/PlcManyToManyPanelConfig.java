/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.extension.manytomanypanel.metadata;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.powerlogic.jcompany.config.metadata.PlcMetaConfig;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Layer;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Scope;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditor;


@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@PlcMetaConfig(root = true, scope = Scope.APP, layer = Layer.CONTROLLER)
@PlcMetaEditor(label = "Many to Many Panel", description = "Configurações complementares para o extension ManyToManyPanel")

/**
 * 
 * Configurações globais de definição do extension ManyToManyPanel
 * 
 * @author Mauren Ginaldo Souza
 * @since jun/2012
 *
 */
public @interface PlcManyToManyPanelConfig {
	
	/** Classe associativa */
	Class<?> associationClass();
	
	/** Classe que ficara no painel */
	Class<?> panelClass();
	
	/** Nome da propriedade Master na classe associativa */
	String propertyNameEntityMaster();
	
	/** Nome da propriedade Panel na classe associativa */
	String propertyNameEntityPanel();

}
