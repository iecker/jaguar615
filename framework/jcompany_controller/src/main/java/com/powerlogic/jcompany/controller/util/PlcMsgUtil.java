/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Embeddable;
import javax.validation.ConstraintViolation;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcMessageConveyorFactory;
import com.powerlogic.jcompany.controller.bindingtype.PlcMsg;
import com.powerlogic.jcompany.controller.jsf.validator.PlcResourceBundleMessageInterpolator;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

/**
 * jCompany 3.0. Auxiliar para gerencia de mensagens
 * @since jCompany 3.0
 * @author Igor Guimarães
 */
@SPlcUtil
@QPlcDefault
@RequestScoped
@Named("plcMsgUtil")
public class PlcMsgUtil {

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault
	protected PlcI18nUtil i18nUtil;
	
	protected PlcMessageConveyorFactory messageConveyorFactory;
	
	private Map<String, List<PlcMessage>> mensagens = new ConcurrentHashMap<String, List<PlcMessage>>();
	
	private Map<String, List<String>> erros 		= new ConcurrentHashMap<String, List<String>>();
	
	private Map<String, List<PlcMessage>> mapaMsg	= new ConcurrentHashMap<String, List<PlcMessage>>();
	
	public PlcMsgUtil() { 
		
	}
	 
	@Inject
	protected transient Logger log;
	
	/**
	 * Disponibiliza mensagens para exibição no topo de mensagens padrão. <br>
	 * Sugestão de Adriel (Quantum Informática) em 02/01/2004
	 * @param idMensagem Chave da mensagem
	 * @param args Lista de argumentos para a mensagem
	 * @since jCompany 1.5
	 */
	public <BeanMessage extends Enum<?>> void msg(BeanMessage key, String cor)   {
		msg(key,null,cor);
	}

	/**
	 * Disponibiliza mensagens para exibição no topo de mensagens padrão. <br>
	 * @param idMensagem Chave da mensagem
	 * @param args Lista de argumentos para a mensagem
	 * @since jcompany 5
	 */
	public <BeanMessage extends Enum<?>> void msg(BeanMessage key, Object[] args)   {
		msg(key, args, PlcMessage.Cor.msgAzulPlc.toString());
	}
	
	/**
	 * Disponibiliza mensagens de Erro para exibição no topo de mensagens padrão. <br>
	 * @param idMensagem Chave da mensagem
	 * @param args Lista de argumentos para a mensagem
	 * @since jcompany 5
	 */
	public <BeanMessage extends Enum<?>> void msgError(BeanMessage key, Object[] args)  {
		msg(key, args,PlcMessage.Cor.msgVermelhoPlc.toString());
	}
	
	/**
	 * Disponibiliza mensagens de Erro para exibição no topo de mensagens padrão. <br>
	 * @param idMensagem Chave da mensagem
	 * @param args Lista de argumentos para a mensagem
	 * @since jcompany 5
	 */
	public <BeanMessage extends Enum<?>> void msgError(String key, Object[] args)  {
		msg(key, args,PlcMessage.Cor.msgVermelhoPlc.toString());
	}
	
	/**
	 * Disponibiliza mensagens já tratada para exibição no topo de mensagens padrão. <br>
	 * Caso a mensagem chegue na forma de chave "{mensagem}", seu valor será recuperado do arquivo 
	 * de properties.resources
	 * 
	 * @param idMensagem Chave da mensagem
	 * @param args Lista de argumentos para a mensagem
	 */
	public void msg(String msg, String cor)  {

		if(msg.substring(0,1).equals("{") &&
				msg.substring(msg.length()-1,msg.length()).equals("}")) {
			msg = msg.substring(1,msg.length()-1);
			String msgAux = i18nUtil.getMessage("ApplicationResources", msg);
			if (StringUtils.isNotEmpty(msgAux)) {
				msg = msgAux;
			}
		}
		
		List<PlcMessage> listaMensagens = mensagens.get(cor);
		
		if (listaMensagens == null) {
			listaMensagens = new ArrayList<PlcMessage>();
		}
		
		if(!listaMensagens.contains(new PlcMessage(msg,cor))) {
			listaMensagens.add(new PlcMessage(msg,cor));
		}

		mensagens.put(cor, listaMensagens);
		
	}

	/**
	 * Disponibiliza mensagens para exibição no topo de mensagens padrão. <br>
	 * 
	 * @param idMensagem Chave da mensagem
	 * @param args Lista de argumentos para a mensagem
	 * @since jCompany 1.5
	 */
	public <BeanMessage extends Enum<?>> void msg(BeanMessage key, Object[] args, String cor)  {
		
		try {
			
			List<PlcMessage> listaMensagens = mensagens.get(cor);
			
			if (listaMensagens == null){
				listaMensagens = new ArrayList<PlcMessage>();
			}
			
			String msg = null;
			messageConveyorFactory = PlcCDIUtil.getInstance().getInstanceByType(PlcMessageConveyorFactory.class, QPlcDefaultLiteral.INSTANCE);
			if (messageConveyorFactory != null){
				msg = messageConveyorFactory.getDefaultMessageConveyer(i18nUtil.getCurrentLocale(), PlcMessageConveyorFactory.PREFIXO).getMessage(key, args);
			}
	
			if(!listaMensagens.contains(new PlcMessage(msg,cor))) {
				listaMensagens.add(new PlcMessage(msg,cor));
			}
			
			PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcMsg>(){});
			
			mensagens.put(cor, listaMensagens);

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcMsgUtil", "msg", e, log, "");
		}
	}

	/**
	 * Disponibiliza mensagens para exibição no topo de mensagens padrão. <br>
	 *
	 * @param idMensagem Chave da mensagem
	 * @param args Lista de argumentos para a mensagem
	 * @since jCompany 1.5
	 */
	public <BeanMessage extends Enum<?>> void msg(String key, Object[] args, String cor)  {
		
		try {
			
			if(key.substring(0,1).equals("{") &&
					key.substring(key.length()-1,key.length()).equals("}")) {
				key = key.substring(1,key.length()-1);
				String msgAux = i18nUtil.getMessage("ApplicationResources", key);
				if (StringUtils.isNotEmpty(msgAux)) {
					key = MessageFormat.format(msgAux, args);
				}
			} else {
				key = MessageFormat.format(key, args);
			}
			
			List<PlcMessage> listaMensagens = mensagens.get(cor);
			
			if (listaMensagens == null){
				listaMensagens = new ArrayList<PlcMessage>();
			}
			
			if(!listaMensagens.contains(new PlcMessage(key,cor))) {
				listaMensagens.add(new PlcMessage(key,cor));
			}
			
			PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcMsg>(){});
			
			mensagens.put(cor, listaMensagens);

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcMsgUtil", "msg", e, log, "");
		}
	}
	/**
	 * @since jCompany 3.2 Unifica modelos de mensagens de validações de domínio e de caso de uso
	 * @param request Objeto contendo validações variantes no padrao Commons Validator
	 * @param validationMessages Validações de dominio no padrao Hibernate Validator
	 * @param linha 
	 * @param form 
	 */
	public <T> void availableInvariantMessages(Set<ConstraintViolation<T>> validationMessages, Integer linha)  {

		// Isso coloca o validator
		if (validationMessages!=null) {

			// Primeiro coloca tudo em vermelho. Quando estiver ok variar as cores
			String msg=null;
			String msgCampo=null;

			Iterator<ConstraintViolation<T>> iterator = validationMessages.iterator();
			while(iterator.hasNext()) {
				ConstraintViolation<T> cv = iterator.next();

				msg = cv.getMessage();
				
				if (msg.indexOf(PlcConstants.MSG.PROP_TITULO_AUTOMATICO)>-1) {

					// assume rotulo como label.nomePropriedade se nao foi informado
					msgCampo = i18nUtil.getMessage("ApplicationResources", "label."+cv.getLeafBean().toString());

					msg = StringUtils.replaceOnce(msg,PlcConstants.MSG.PROP_TITULO_AUTOMATICO,msgCampo);

				} else {
					// troca um token entre {}, se foi informado 
					int posIni = msg.indexOf("{");
					if (posIni>-1) {
						int posFim = msg.indexOf("}");
						String token = msg.substring(posIni,posFim+1);
						msgCampo = cv.getPropertyPath().toString();
						msg = StringUtils.replaceOnce(msg,token,msgCampo);
					}
				}

				// Inclui linha se tiver  
				if (linha !=null) {
					msg = availableMessagesInsertTabularLine(msg,linha.toString(),null);
				} 

				createMessage(msg, cv.getLeafBean().toString(), linha, cv.getPropertyPath().toString(), cv.getRootBean(), PlcMessage.Cor.msgVermelhoPlc.toString());
			}
		}

	}

	public void createMessageVariant(PlcException exception, String nomePropriedade, String cor)  {
		createMessage(exception.getMessage(), nomePropriedade, null, nomePropriedade, null, cor);
	}
	
	/**
	 * Recebe uma mensagem e um número de linha e acrescenta o sufixo contendo a linha do erro
	 * @param msg mensagem já traduzida
	 * @param linha numero de linha a acrescentar
	 * @return Mensagem com o sufixo.
	 */
	private String availableMessagesInsertTabularLine(String msg, String linha, String tituloComponente)  {

		msg = msg + " " + i18nUtil.getMessage("jCompanyResources","validator.linha") + " " + linha;

		if (tituloComponente != null && tituloComponente.startsWith("#")) {
			msg = msg + " ("+ tituloComponente.substring(1)+")";
		} else if (tituloComponente != null && !tituloComponente.startsWith("#")) { 
			msg = msg + " ("+ i18nUtil.getMessage("ApplicationResources",tituloComponente)+")";
		}

		return msg;
	}

	private void createMessage(String msg, String propNome, Integer linha, String propPath, Object bean, String cor)  {

		List<PlcMessage> listaMensagens = mensagens.get(cor);

		if (listaMensagens == null) {
			listaMensagens = new ArrayList<PlcMessage>();
		}
		
		PlcMessage m = new PlcMessage(msg,propNome);

		if (!listaMensagens.contains(m)) { 
			listaMensagens.add(m);
			
			mensagens.put(cor, listaMensagens);

			// List de mensagens por atributo (para marcação)
			
			if (mapaMsg == null) {
				mapaMsg = new HashMap<String,List<PlcMessage>>();
			}
			
			// Se for propriedade de componente, troca o primeiro ponto por "_", conforme configurado no form.
			String chaveProp = propPath;
			if (bean != null && bean.getClass().getAnnotation(Embeddable.class)!=null)
				chaveProp = StringUtils.replaceOnce(chaveProp,".","_");
			// Se for tabular (recebeu a linha), entao monta com itensPlc[x]
			if (linha != null)
				chaveProp = "itensPlc["+(linha-1)+"]."+chaveProp;

			// Retira sufixo Aux de propriedades, pois tag-files retiram para comparacao (sufixo reservado)
			if (chaveProp != null && chaveProp.endsWith("Aux"))
				chaveProp = chaveProp.substring(0,chaveProp.length()-3);
			
			if(chaveProp != null) {
				List<PlcMessage> atributo = mapaMsg.get(chaveProp);
				if (atributo == null) {
					atributo = new ArrayList<PlcMessage>();
				}
				
				atributo.add(m);
				mapaMsg.put(chaveProp, atributo);
				
			}

			
		}
	}

	public Map<String, List<PlcMessage>> getMensagens() {
		return mensagens;
	}

	public String retrieveMessageByKey(String messageError, Map<String, Object> messageArgsLoc) {
		
		PlcMessageConveyorFactory messageConveyorFactory = PlcCDIUtil.getInstance().getInstanceByType(PlcMessageConveyorFactory.class, QPlcDefaultLiteral.INSTANCE);
		String nome = PlcConstants.PlcJsfConstantes.BUNDLE_PADRAO_TAGS + "_" + i18nUtil.getCurrentLocale() + ".properties";
		Properties props = new Properties();  
        try {
        	InputStream in = getClass().getClassLoader().getResourceAsStream(nome);
        	if (in != null) {
        		props.load(in);
        		in.close();
        		if (StringUtils.isNotEmpty(props.getProperty(messageError))) {
        			messageError = props.getProperty(messageError);
        		}	
        	}	
		} catch (IOException e) {
			// não faz nada, vai imprimir a mensagem que chegou
		}  
		
		PlcResourceBundleMessageInterpolator bundleMessageInterpolator = new PlcResourceBundleMessageInterpolator();

		messageError = bundleMessageInterpolator.interpolate(messageError, messageArgsLoc, i18nUtil.getCurrentLocale());
		
		return messageError;
		
	}
	
	/**
	 * JCompany 2.5.1. Método que coloca mensagem de erro para marcação de campo validado
	 */
	public void msgErrorValidation(String propErro) {

		List<String> listaErros = erros.get(PlcConstants.ERRO.PLC_ERROR_KEY);
		
 		if (listaErros == null) {
 			listaErros = new ArrayList<String>(); 
 		}
 		
 		listaErros.add(propErro);
 		
 		erros.put(PlcConstants.ERRO.PLC_ERROR_KEY, listaErros);
 		
 	}
}
