/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.application;

import static java.lang.annotation.ElementType.PACKAGE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.powerlogic.jcompany.config.metadata.PlcMetaConfig;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditor;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditorParameter;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Layer;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Scope;

@Documented
@Target(PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
@PlcMetaConfig(root=true, scope=Scope.EMP, layer=Layer.COMMONS)
@PlcMetaEditor(label="Empresa", description="Configurações globais de definição da empresa")
/**
 * @since jCompany 5.0
 * Configurações globais de definição da empresa. Informações que aparecem em alguns componentes visuais por default.
 */
public @interface PlcConfigCompany {

	/**
	 * Este é o nome da empresa como deve aparecer em componentes visuais tais como páginas de erro, relatórios, consultas, etc. Exemplo: Powerlogic Consultoria S/A
	 */
	@PlcMetaEditorParameter(label="Nome", description="Nome da Empresa como deve aparecer em componentes visuais tais como páginas de erro, relatórios, consultas, etc", length=30)
	String name() default "";
	/**
	 * Dominio (DNS), sem constar o protocolo (http://). Ex: www.powerlogic.com.br
	 */
	@PlcMetaEditorParameter(label="Domínio", description="Dominio (DNS), sem constar o protocolo (http://)", length=30)
	String domain() default "";
	
	/**
	 * Sigla da empresa. Ex: PLC
	 */
	@PlcMetaEditorParameter(label="Sigla", description="Sigla da empresa", length=15)
	String acronym() default "";
	
	/**
	 * URL relativa da imagem de logotipo para apresentação no login e em outros componentes padrões
	 */
	@PlcMetaEditorParameter(label="Logotipo", description="URL relativa da imagem de logotipo para apresentação no login e em outros componentes padrões", length=30)
	String logo() default "/res-plc/midia/marca_empresa.gif";
	
	/**
	 * Endereço completo da empresa, como aparecerá em páginas de erro e rodapé das aplicações.
	 * Ex: R. Paraíba, 330. 19º andar. Funcionários. Belo Horizonte - MG. Cep: 30130-917. Email Geral: plc@powerlogic.com.br
	 */
	@PlcMetaEditorParameter(label="Endereço", description="Endereço completo da empresa, como aparecerá em páginas de erro e rodapé das aplicações", length=40)
	String address() default "";
	
	/**
	 * Telefone de suporte a usuários da empresa, como aparecerá em páginas de erro, rodapés, etc.
	 */
	@PlcMetaEditorParameter(label="Telefone Suporte", description="Telefone de suporte a usuários da empresa, como aparecerá em páginas de erro, rodapés, etc")
	String supportPhone() default "";
	
	/**
	 * Email de suporte a usuários da empresa, como aparecerá em páginas de erro, rodapés, etc.
	 */
	@PlcMetaEditorParameter(label="E-mail Suporte", description="E-mail de suporte a usuários da empresa, como aparecerá em páginas de erro, rodapés, etc")
	String supportEmail() default "";
}
