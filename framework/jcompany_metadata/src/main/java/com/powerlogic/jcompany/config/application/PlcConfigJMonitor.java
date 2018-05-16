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
@PlcMetaConfig(root=true, scope=Scope.APP, layer=Layer.COMMONS)
@PlcMetaEditor(label="jMonitor", description="Configurações globais para integração com o jMonitor")
/**
 * Configurações globais para integração com o jMonitor
 */
// TODO - Testar integração com jMonitor, envio de email e verificar necessidade dos metadados
public @interface PlcConfigJMonitor {

	/**
	 * Indica com qual tecnologia o Appender Log4j que envia emails proverá a saída do email
	 */
	public enum EmailSendingMode {
		/**
		 * Envio via JMS. Exige um endereço JMS em jmsInitialContextProviderURL que contenha uma versão do eCompany Monitor.
		 */
		JMS,
		/**
		 * Envio via Javamail. Exige um endereço de servidor de email que contenha uma versão do eCompany Monitor.
		 */
		JAVAMAIL,
		/**
		 * Não usa 
		 */
		NONE
	}
	
	
	/**
	 * Indica que a aplicação utiliza o jMonitor.
	 */
	@PlcMetaEditorParameter(label="Usa?", description="Indica que a aplicação utiliza o jMonitor")
	boolean use() default false;

	/**
	 * URLs que se deseja que sejam ignoradas pelo jMonitor
	 */
	@PlcMetaEditorParameter(label="URL a Ignorar", description="URLs que se deseja que sejam ignoradas pelo jMonitor")
	String ignoreURL() default "";
	
	/**
	 * Se é para jMonitor enviar a seção de QueryString (argumentos após "?") da URL
	 */
	@PlcMetaEditorParameter(label="Envia QueryString", description="Se envia os argumentos da URL (QueryString) para o jMonitor")
	boolean sendQueryString() default false;
	
	/**
	 * Indica com qual tecnologia o Appender Log4j que envia emails proverá a saída do email
	 */
	@PlcMetaEditorParameter(label="Tecnologia Envio de E-mail", description="Indica com qual tecnologia o Appender Log4j que envia emails proverá a saída do email")
	EmailSendingMode emailSendingMode() default EmailSendingMode.NONE;
	
	/**
	 * Conta de email que será utilizada como rementete das mensagens de erro fatais.
	 */
	@PlcMetaEditorParameter(label="E-mail do Remetente", description="Conta de email que será utilizada como rementete das mensagens de erro fatais", length=20)
	String fromEmail() default "#";
	
	/**
	 * Email que receberá mensagens de erros de nível FATAL (default # não envia).
	 */
	@PlcMetaEditorParameter(label="E-mails para Fatal", description="Email que receberá mensagens de erros de nível FATAL", length=20)
	String fatalEmail() default "#";
	
	/**
	 * Email que receberá mensagens de erros de nível ERROR (default # não envia).
	 */
	@PlcMetaEditorParameter(label="E-mails para Fatal", description="Email que receberá mensagens de erros de nível ERROR", length=20)
	String errorEmail() default "#";
	
	/**
	 * java.naming.provider.url - Endereço do provider JMS. Ex: jnp://localhost:1099
	 */
	@PlcMetaEditorParameter(label="Endereço do provider JMS", description="java.naming.provider.url - Endereço do provider JMS. Ex: jnp://localhost:1099", length=40)
	String jmsInitialContextProviderURL() default "";
	
	@PlcMetaEditorParameter(label="Endereço SMTP", description="Endereço do servidor de SMTP", length=40)
	String mailSmtpHost() default "";
	
	@PlcMetaEditorParameter(label="Pseudo Produção", description="Simula Modo de Produção")
	boolean pseudoProduction() default false;
	
	@PlcMetaEditorParameter(label="URI HTML Email", description="Endereço de arquivo HTML que servirá de base para envio de emails", length=50)
	String uriHtmlBaseToEmail() default "";

    @PlcMetaEditorParameter(label="Feedback", description="Feedback")
	boolean useFeedback() default false;
	
	@PlcMetaEditorParameter(label="Endereço Feedback", description="Endereço para contabilização de feedback")
	String feedbackAddress() default "";

	
}
