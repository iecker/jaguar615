/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcMessageConveyorFactory;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationI18n;
import com.powerlogic.jcompany.config.application.PlcConfigModule;


/**
 * jCompany 2.5.3. Auxiliar para manipulação de mensagens
 * @since jCompany 2.5.3
 */
@SPlcUtil
@QPlcDefault
public class PlcI18nUtil  {

	@Inject
	protected transient Logger log;

	protected static final Locale LOCALE_PT_BR = new Locale("pt","BR");
	
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault 
	protected PlcMessageConveyorFactory messageConveyorFactory;
	
	public PlcI18nUtil() {
		
	}

	/**
	 * Monta uma mensagem localizada a partir de um arquivo de recursos. A mensagem será montada utilizando
	 * a chave e os parâmetros informados.<br>
	 * IMPORTANTE: Mensagens iniciadas com "#", por convenção, não são tratadas (assume que já foram traduzidas pelo desenvolvedor)
	 * @param nomeBundle  Nome do arquivo de recursos (ex.:ApplicationResources).
                         Não colocar a extensão ".properties".
	 * @param locale      Locale para mensagem.
	 * @param key         Chave da mensgem dentro do arquivo de recursos(ex.:jcompany.erros.disponibiliza.classe.lookup.localizada).
	 * @param parametros  Parâmetros utilizado na formatação da mensagem.
	 *                    Se não existirem parâmetros, passar um vetor de String de tamanho zero.
	 * @return            Mensagem completa formatada ou null se não existir.
	 *
	 */
	public String mountLocalizedMessage(String nomeBundle, String key, String[] parametros) {

		try {
			if (StringUtils.isNotBlank(key)){
				if (key.startsWith("#"))
					return key.substring(1);

				ResourceBundle messages = ResourceBundle.getBundle(nomeBundle, getCurrentLocale());

				MessageFormat formatter = new MessageFormat(messages.getString(key));

				String mensagemCompleta = formatter.format(parametros);

				return mensagemCompleta;
			}

			return "";

		} catch (MissingResourceException e) {
			return "???"+getCurrentLocale()+" "+key+"???"; 
		}

	}

	/**
	 * Monta uma mensagem localizada a partir de um arquivo de recursos, substituindo argumentos no padrão do validator. 
	 * A mensagem será montada utilizando a chave e os parâmetros informados.<br>
	 * IMPORTANTE: Mensagens iniciadas com "#", por convenção, não são tratadas (assume que já foram traduzidas pelo desenvolvedor)
	 * @param nomeBundle  Nome do arquivo de recursos (ex.:ApplicationResources).
                         Não colocar a extensão ".properties".
	 * @param locale      Locale para mensagem.
	 * @param key         Chave da mensgem dentro do arquivo de recursos(ex.:jcompany.erros.disponibiliza.classe.lookup.localizada).
	 * @param args  		 Argumentos de substituição para tokens numerados de 0 a 3 na mensagem, na ordem de substituição. (Ex de tokens
	 * na mensagem: {0} é obrigatório(a), Número deve estar entre {0} e {1}
	 * @return            Mensagem completa formatada ou null se não existir.
	 * @since jCompany 3.2
	 *
	 */
	public String mountLocalizedMessage(String nomeBundle, String key, Object[] args) {

		try {

			if (key.startsWith("#"))
				return key.substring(1);

			ResourceBundle messages = ResourceBundle.getBundle(nomeBundle, getCurrentLocale());

			String msgTratada = messages.getString(key);

			if (args != null) {
				for (int i = 0; i < args.length; i++) {

					if (args[i] != null){
						if (args.toString().contains("jcompany.")){
							String msgArgs = mountLocalizedMessage(nomeBundle, (String)args[i],null);
							msgTratada = StringUtils.replaceOnce(msgTratada,"{"+i+"}",msgArgs);
						}
						else
							msgTratada = StringUtils.replaceOnce(msgTratada,"{"+i+"}",args[i].toString());
					}

				}
			}

			return msgTratada;

		} catch (MissingResourceException e) {
			// Deve voltar null para facilitar busca para vários Bundles pelo chamador
			return null;
		}

	}

	/**
	 * Procura em todos os bundles configurados por uma mensagem. Se não encontrar devolve mensagem entre "???", para manter coerencia com
	 * padrão do Commons Validator.
	 * @param bundle Caso informado, procura neste primeiro, se não encontrado, irá procurar nos declarados no package-info
	 */
	public String mountLocalizedMessageAnyBundle (HttpServletRequest request, String bundle, String messageKey, Object[] args){

		String valor = null;

		if (StringUtils.isNotBlank(messageKey)){

			if (StringUtils.isNotBlank(bundle))
				valor = mountLocalizedMessage(bundle, messageKey, args);

			if (StringUtils.isBlank(valor))
				valor = mountLocalizedMessageAnyBundle(request, messageKey, args);

		}

		return valor;

	}
	
	public <BeanMessage extends Enum<?>>  String getMessageCal10n(HttpServletRequest request, BeanMessage messageKey, Object[] args){
		
		String msg	= messageConveyorFactory.getDefaultMessageConveyer(getCurrentLocale(), PlcMessageConveyorFactory.PREFIXO).getMessage(messageKey, args);
		
		if (StringUtils.isNotEmpty(msg)){
			return msg;
		}else{
			return messageConveyorFactory.getDefaultMessageConveyer(getCurrentLocale(), PlcMessageConveyorFactory.PREFIXO).getMessage(PlcBeanMessages.MSG_KEY_NOT_FOUND);
		}
	}

	/**
	 * Procura em todos os bundles configurados por uma mensagem. Se não encontrar devolve mensagem entre "???", para manter coerencia com
	 * padrão do Commons Validator.
	 * @since jCompany 3.2
	 */
	public String mountLocalizedMessageAnyBundle(HttpServletRequest request,String messageKey, Object[] args) {

		if (messageKey.startsWith("#"))
			return messageKey.substring(1);

		String msg	= null;

		try{
			/*
			 * Se for aplicação JSF o module config do struts estará nulo, então recupera dos bundles das anotações no PlcConfigAplicacao
			 */
			List<String> listaBundle = listBundle();

			handleKeyWithArgs(args, listaBundle);

			for(String bundle : listaBundle){

				msg = mountLocalizedMessage(bundle, messageKey, args);
				if (msg != null && !("".equals(msg))){
					return msg;
				}
			}
		}catch(Exception e){
			log.error( "Nao encontrou mensagem. Erro:"+e,e);
			return "???" + getCurrentLocale() + " " + messageKey+"???"; 
		}

		// Se não achou, devolve no padrão do Validator
		return "???" + getCurrentLocale() + " " + messageKey + "???";

	}

	/**
	 * Interpreta Key dentro de argumento para tratar mensagens do sistema.
	 */
	private void handleKeyWithArgs(Object[] args, List<String> listaBundle) {
		if (args != null && args.length > 0){
			String argumentoInterpretado;
			for (int i = 0; i < args.length ; i++){
				if (args[i] != null && args[i].toString().indexOf(".")>0){
					for(String bundle : listaBundle){
						argumentoInterpretado = mountLocalizedMessage(bundle, args[i].toString(), new String[]{""});
						if (argumentoInterpretado != null && !("".equals(argumentoInterpretado)) && !argumentoInterpretado.startsWith("???") && !argumentoInterpretado.endsWith("???")){
							args[i] = argumentoInterpretado;
							break;
						}
					}
				}
			}
		}
	}

	private List<String> listBundle()  {
		
		PlcConfigApplication configApp = configUtil.getConfigApplication().application();
		PlcConfigApplicationI18n configI18n = configApp.i18n();

		List <String> listaBundle = new ArrayList<String>();
		for (String bundle : configI18n.bundles()) {
			listaBundle.add(bundle);
		}

		PlcConfigModule[] modulos = configApp.modules();
		if (modulos != null){
			for (PlcConfigModule plcConfigModulo : modulos) {
				String nome = plcConfigModulo.name();
				listaBundle.add(nome + "Resources");
			}
		}
		return listaBundle;
	}

	/**
	 * Recupera todas as mensagens de um arquivo de recursos
	 */
	public Enumeration getMessages(String nomeBundle) {

		log.debug( "########## Entrou em recuperaMessagens");

		try {

			ResourceBundle messages = ResourceBundle.getBundle(nomeBundle, getCurrentLocale());

			return messages.getKeys();

		} catch (MissingResourceException e) {
			return null;
		}

	}

	/**
	 * Recupera referencia ao ResourceBundle
	 */
	public ResourceBundle getBundle(String nomeBundle) {

		log.debug( "########## Entrou em recuperaBundle");

		try {

			ResourceBundle messages = ResourceBundle.getBundle(nomeBundle, getCurrentLocale());

			return messages;

		} catch (MissingResourceException e) {
			return null;
		}

	}

	/**
	 * Recupera referencia ao ResourceBundle
	 * @param nomeBundle Nome raiz do arquivo de bundle
	 * @param locale Localização em string na forma de "pt_BR" ou "en_US".
	 */
	public ResourceBundle getBundle(String nomeBundle, String locale) {

		log.debug( "########## Entrou em recuperaBundle");

		try {
			String linguagem = locale.substring(0,2).toLowerCase();
			String pais = locale.substring(3).toUpperCase();
			Locale loc = new Locale(linguagem,pais);
			ResourceBundle messages = ResourceBundle.getBundle(nomeBundle, loc);

			return messages;

		} catch (MissingResourceException e) {
			return null;
		}

	}

	/**
	 * Recupera referencia ao ResourceBundle
	 * @param rb ResourceBundle
	 * @param key chave da mensagem.
	 */
	public String getMessage(ResourceBundle rb, String key) {

		log.debug( "########## Entrou em recuperaBundle");

		return rb.getString(key);


	}

	/**
	 * Recupera mensagem do ResourceBundle Default (ApplicationResources), para o idioma passado
	 * @param key chave da mensagem.
	 * @param locale Locale
	 *  Se não encontrou
	 */
	public String getMessage(String msgKey)  {

		try {

			ResourceBundle rb = getBundle("ApplicationResources");

			return rb.getString(msgKey);

		} catch (Exception e) {
			return "???" + getCurrentLocale() + " " + msgKey + "???";
		}


	}

	/**
	 * Recupera mensagem do ResourceBundle Default (ApplicationResources), para o idioma passado
	 * @param key chave da mensagem.
	 * @param locale Locale
	 * @return mensagem traduzida ou null se não achou.
	 */
	public String getMessage(String bundle,String msgKey)  {

		try {

			ResourceBundle rb = getBundle(bundle);

			return rb.getString(msgKey);

		} catch (Exception e) {
			return "???" + getCurrentLocale() + " " + msgKey + "???";
		}

	}


	/**
	 * jCompany 3.0 Verifica se existe uma mensagem no arquivo base ApplicationResources_XXXX.properties, no idioma correntemente
	 * encontrado no request. Se a mensagem começa com "jcompany." e não começa com "jcompany.aplicacao.", procura em "jCompanyResources_XXXX.properties".
	 * @param msgKey Chave a ser procurada
	 * @param request Request para procurar o Locale (se não encontrado, assume PT_BR)
	 * @return String Mensagem correspondente à chave
	 */
	public String getMessage(String msgKey,HttpServletRequest request)  {

		try {

			ResourceBundle rb = getBundle("ApplicationResources");	
			
			if (msgKey.startsWith("jcompany.") && !msgKey.startsWith("jcompany.aplicacao.")) {
				rb = getBundle("jCompanyResources");
			}
			return rb.getString(msgKey);	
		} catch (Exception e) {
			throw new PlcException("PlcI18nUtil", "getMessage", e, log, "");
		}

	}

	/**
	 * jCompany 3.0 Verifica se existe uma mensagem no arquivo base ApplicationResources.properties
	 * @param msgKey Chave a ser procurada
	 * @return true se existir ou false se não existir
	 */
	public boolean messageExistInMainBundle(String msgKey)  {

		log.debug( "########## Entrou em existeMensagemBundlePrincipal");

		ResourceBundle rb = getBundle("ApplicationResources");

		try {
			rb.getString(msgKey);		
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * Recupera a chave correspondente à mensagem informada. Utiliza o locale
	 * obtido no request. Procura primeiramente em "ApplicationResouces" e
	 * depois em "jCompanyResources".
	 * 
	 * @since jCompany 3.0
	 * 
	 * @param mensagem Chave da mensagem
	 * @return String[0]:Chave da Mensagem e String[1]:locale encontrado, para conferência.
	 */
	public String[] getKey(String mensagem, HttpServletRequest request) {
		return getKey(mensagem);
	}

	/**
	 * Recupera a chave correspondente à mensagem informada. Procura
	 * primeiramente em "ApplicationResouces" e depois em "jCompanyResources".
	 * 
	 * @since jCompany 3.0
	 * 
	 * @param mensagem
	 * @param locale
	 * @return
	 * 
	 */
	public String[] getKey(String mensagem)
	 {
		String[] keys = getKey(mensagem, getBundle("ApplicationResources"));
		
		if (keys == null || keys.length == 0) {
			keys = getKey(mensagem, getBundle("jCompanyResources"));
		}

		return keys;
	}

	/**
	 * Recupera a chave correspondente à mensagem informada.
	 * 
	 * @since jCompany 3.0
	 * 
	 * @param mensagem
	 * @param messages
	 * @return
	 */
	public String[] getKey(String mensagem, ResourceBundle resources) {
		
		if (resources == null) {
			return null;
		}
		
		List<String> keys = new ArrayList<String>();
		
		if (mensagem == null) {
			mensagem = "null";
		}

		for (Enumeration<String> en = resources.getKeys(); en.hasMoreElements();) {
			String k = en.nextElement();
			if (mensagem.equals(resources.getString(k))) {
				if (!keys.contains(k)) {
					keys.add(k);
				}
			}
		}

		return (String[]) keys.toArray(new String[keys.size()]);
	}

	/**
	 * Método que busca o locale padrão.
	 * 
	 * Primeiro tenta buscar do Contexto do Faces.
	 * 	Nesse caso, é retornado o locale que está setado no browser do usuário
	 * Se não tiver, busca o default do sistema.
	 *  Nesse caso é retornardo o locale padrão do sistema operacional do servidor
	 *  
	 *  Por fim, seta o locale no objeto threadlocal do Exception, 
	 *  para que, ao ser disparada uma exception, pegue o bundle correto. 
	 *  
	 * @return
	 */
	public Locale getCurrentLocale() {
		FacesContext currentInstance = FacesContext.getCurrentInstance();
		if (currentInstance != null) {
			UIViewRoot viewRoot = currentInstance.getViewRoot();
			Locale localeARetornar=null;
			if (viewRoot != null) {
				localeARetornar = viewRoot.getLocale();
			} else {
				localeARetornar = currentInstance.getApplication().getDefaultLocale();
			}
			PlcException.locale.set(localeARetornar);
			return localeARetornar;
		}
		return Locale.getDefault();
	}
}
