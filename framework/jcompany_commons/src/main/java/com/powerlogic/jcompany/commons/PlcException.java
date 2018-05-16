/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcConfigUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcMessageConveyorFactory;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.application.PlcConfigAppBehavior;

/**
 * Classe de Exceção do jCompany que mantém exceção "raiz" para tratamento genérico.
 *  recomendado pela Struts conforme O'Reilly<p>
 * @since jCompany 3.0
 */
public class PlcException extends RuntimeException {
	
	private Logger log = Logger.getLogger(PlcException.class.getCanonicalName());
	
	private static final long serialVersionUID 	= -913609256934864491L;
	
	private String message 						= null;
	private String messageError					= null;
	private Throwable causaRaiz 				= null;
	private transient Logger causaLogger 		= null;
	private Object objectError					= null;
	private Enum<?> typedError					= null;
	
	/**
	 * Locale padrao, setado no PlcI18nUtil, com o locale do usuário
	 */
	public static ThreadLocal<Locale> locale = new ThreadLocal<Locale>();	
	
    public PlcException() {
        super();
        setMessage(null);
    }
    
	/**
	 * @since jcompany 6.0
	 * Verifica se o parametro passado esta no formato de "{exemplo.mensagem}" e procura no arquivo de mensagens.
	 * Retorna o valor da mensagem ou a sua chave ou o texto puro, caso não tenha vindo no formato descrito
	 */	
	public PlcException(String messageError) {
		messageError = getMessageInPropertiesFile(messageError, null);  
		if (StringUtils.isNotEmpty(messageError)){
			setMessage(messageError);
			setCausaRaiz(null);
		}else{
			setMessage(getMessageErrorGeneric());
			setCausaRaiz(new Throwable(getMessageErrorGeneric()));
		}
		
	}
	
	/**
	 * @since jcompany 6.0
	 * Seta uma texto na mensagem de erro
	 */	
	public PlcException(String messageError, Object[] messageArgsLoc) {
		messageError = getMessageInPropertiesFile(messageError, null);  		
		if(messageArgsLoc != null && messageArgsLoc.length>0) {
			messageError = MessageFormat.format(messageError, messageArgsLoc);
		}

		if (StringUtils.isNotEmpty(messageError)){
			setMessage(messageError);
			setCausaRaiz(null);
		}else{
			setMessage(getMessageErrorGeneric());
			setCausaRaiz(null);
		}
		
	}

	/**
	 * @since jcompany 6.0
	 * Recupera mensagem com exceção genérica
	 * @param causa
	 */	
	public PlcException(Throwable causa) {
				
		if (isConstraintViolationException(causa, null)) 
			return;
		
		if (causa != null && PlcException.class.isAssignableFrom(causa.getClass())) {
			PlcException causaPlcException = (PlcException)causa;
			setMessage(causaPlcException.getMessage());
			setCausaRaiz(causaPlcException.getCausaRaiz());
		} else {
			
			if (showInternalMessage()){
				
				if (StringUtils.isNotEmpty(message)){
					typedError = PlcBeanMessages.JCOMPANY_ERROR_EVENT_NOT_HANDLED;
					setMessage(message);
				}else{
					setMessage(getMessageErrorByKeyWithoutArgs(PlcBeanMessages.JCOMPANY_ERROR_EVENT_NOT_HANDLED));
				}
				
			} else {
				if (StringUtils.isNotEmpty(message)){
					setMessage(message + " Causa: " + causa.getMessage());
				}else{
					typedError = PlcBeanMessages.JCOMPANY_ERROR_EVENT_NOT_HANDLED;
					setMessage(getMessageErrorByKey(PlcBeanMessages.JCOMPANY_ERROR_EVENT_NOT_HANDLED, new Object[]{causa.getCause()}));
				}
			}
			setCausaRaiz(causa);
		}
		
	}
	
	/**
	 * @since jcompany 6.0
	 * Recupera a mensagem de erro somente pela chave
	 */	
	public <BeanMessage extends Enum<?>> PlcException(BeanMessage messageKey) {
		
		this.setTypedError(messageKey);
		
		String messageError 	= getMessageErrorGeneric();
		String messageConveyor 	= getMessageErrorByKeyWithoutArgs(messageKey);
		
		if (StringUtils.isNotEmpty(messageConveyor)){
			messageError = messageConveyor;
		}

		if (StringUtils.isNotEmpty(messageError)){
			setMessage(messageError);
			setCausaRaiz(null);
		}else{
			setMessage(getMessageErrorGeneric());
			setCausaRaiz(new Throwable(messageError));
		}
		
	}
	
	/**
     * @since jcompany 6.0
     * Construtor que pode ser utilizado para disparar uma mensagem passando um objeto que pode ser um array, set, list
     * Ele não emite stacktrace e seus erros devem ser capturados nas camadas acima para tratamento específico das mensagens
     * @param messageKey Enum contendo o identificador internacionalizado para a mensagem
     * @param objectError array, set, list com erros
     */
	public <BeanMessage extends Enum<?>> PlcException(BeanMessage messageKey, Object objectError) {
		this.setTypedError(messageKey);
		this.messageError = messageKey.toString();
		this.objectError = objectError;
		setCausaRaiz(null);
	}
	
	/**
     * @since jcompany 6.0
     * Construtor que pode ser utilizado para se criar o objeto de exceção com passagem de argumentos.<br>
     * Exemplo de uso: "throw new PlcException(PlcBeanMessages.JCOMPANY_ERRO_GENERICO, new Object[]{arg1,arg2});"
     * @param messageKey Enum contendo o identificador internacionalizado para a mensagem
     * @param messageArgsLoc Array de Object que contém uma lista de até 4 argumentos dinâmicos para a mensagem
     */
	public <BeanMessage extends Enum<?>> PlcException(BeanMessage messageKey, Object[] messageArgsLoc, Boolean printStackTrace)   {
		
		this.setTypedError(messageKey);
		
		String messageError = getMessageErrorByKey(messageKey, messageArgsLoc);
		if (showInternalMessage()){
			setMessage(getMessageErrorByKeyWithoutSystemMessage(messageKey, messageArgsLoc));
		} else {
			setMessage(messageError);
		}
		
		if (printStackTrace) {
			setCausaRaiz(new Throwable(messageError));
		}
		
	}
	
	/**
	 * @since jcompany 6.0
	 * Seta uma texto na mensagem de erro
	 */	
	public PlcException(String message, Throwable causa) {
		
		if (isConstraintViolationException(causa, null)) 
			return;
		
		if (showInternalMessage()){
			setMessage(message);
		} else {
			if (StringUtils.isNotEmpty(message)){
				setMessage(message + " Causa: " + causa.getMessage());
			}else{
				setMessage(getMessageErrorByKey(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[]{"",causa.getMessage()}));
			}
		}
		
		setCausaRaiz(causa);
	}

	/**
	 * @since jcompany 6.0
	 * Este construtor disponibiliza a causa também no vetor de argumentos para exibição com {0} nas mensagens
	 * @param messageKey Chave da mensagem
	 * @param causa Exceção causadora
	 * @param logCausa
	 */
	public <BeanMessage extends Enum<?>> PlcException(BeanMessage messageKey, Throwable causa, Logger logCausa) {
		
		this(messageKey, null, causa, logCausa);
		this.setTypedError(messageKey);
	}
	
	/**
	 * Método que gera mensagem de exceção baseado no enum de mensagens cal10n
	 * 
	 * @param <E> Enum 
	 * @param key
	 * @param messageArgsLoc
	 * @param causa
	 * @param logCausa
	 */
	public <BeanMessage extends Enum<?>> PlcException(BeanMessage messageKey, Object[] messageArgsLoc, Throwable causa, Logger logCausa)   {
		
		this.setTypedError(messageKey);

		if (isConstraintViolationException(causa, messageArgsLoc)) {
			return;
		}
		
		if (causa != null && PlcException.class.isAssignableFrom(causa.getClass())) {
			// Se for 'wrapper' sobre 'wrapper', mantém informações da origem
			PlcException causaPlcException = (PlcException)causa;
			setMessage(causaPlcException.getMessage());
			
		} else if (causa!=null && causa.getCause() != null && PlcException.class.isAssignableFrom(causa.getCause().getClass())) {
			// Se for 'wrapper' sobre 'wrapper', mantém informações da origem
			PlcException causaPlcException = (PlcException) causa.getCause();
			setMessage(causaPlcException.getMessage());
			
		} else {
			
			if (showInternalMessage()){
				setMessage(getMessageErrorByKeyWithoutSystemMessage(messageKey, messageArgsLoc));
			}else{
				if(messageArgsLoc != null) {
					setMessage(getMessageErrorByKey(messageKey, messageArgsLoc));
				} else {
					setMessage(getMessageErrorByKeyWithoutArgs(messageKey));
				}
			}
		}
		
		setMessageError(causa!=null?causa.getMessage():"");		
		setCausaRaiz(causa);
		
		if (logCausa != null)
			logCausa.fatal(causa);
		
		setCausaLogger(logCausa);
	}	
	
	
	/**
	 * Método que gera mensagem de exceção baseado em erro genérico
	 * 
	 * @param <E> Enum 
	 * @param key
	 * @param messageArgsLoc
	 * @param causa
	 * @param logCausa
	 */
	public <BeanMessage extends Enum<?>> PlcException(String classe, String metodo, Throwable causa, Logger logCausa, String msgAdicional)   {

		if (causa != null && PlcException.class.isAssignableFrom(causa.getClass())) {
			// Se for 'wrapper' sobre 'wrapper', mantém informações da origem
			PlcException causaPlcException = (PlcException)causa;
			setMessage(causaPlcException.getMessage());
			
		} else if (causa.getCause() != null && PlcException.class.isAssignableFrom(causa.getCause().getClass())) {
			// Se for 'wrapper' sobre 'wrapper', mantém informações da origem
			PlcException causaPlcException = (PlcException) causa.getCause();
			setMessage(causaPlcException.getMessage());
			
		} else {
			Object[] messageArgsLoc = {classe,metodo,causa,msgAdicional};
			if (showInternalMessage()){
				setMessage(getMessageErrorByKeyWithoutSystemMessage(PlcBeanMessages.JCOMPANY_ERROR_GENERIC_LOG_FULL, messageArgsLoc));
			}else{
				setMessage(getMessageErrorByKey(PlcBeanMessages.JCOMPANY_ERROR_GENERIC_LOG_FULL, messageArgsLoc));
			}
		}
		
		setMessageError(causa.getMessage());		
		setCausaRaiz(causa);
		if (logCausa != null) {
			logCausa.fatal(causa);
			setCausaLogger(logCausa);
		}	
	}	
	
	/**
	 * @since jcompany 3.0
	 */
	public void printStrackTrace() {
		printStackTrace(System.err);
	}
	
	/**
	 * @since jcompany 3.0
	 */
	public void printStackTrace(PrintStream outStream) {
	   printStackTrace(new PrintWriter(outStream));	
	}
	
	/**
	 * @since jcompany 3.0
	 */
	public void printStackTrace(PrintWriter writer) {
		
		super.printStackTrace(writer);
		
		if ( getCausaRaiz() != null ) {
			getCausaRaiz().printStackTrace(writer);
		}
		writer.flush();
	}
	
	/**
	 * Exibe exceção: Se tiver exceção raiz, exibe seu toString. Se não tiver e tiver mensagem, exibe
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (getCausaRaiz() != null){
			return getCausaRaiz().toString();
		} else if (getMessage() != null) {
			return getMessage();
		} else {
			return super.toString();
		}
		
	}
	
    /**
     * Retorna a mensagem da exceção raiz, caso exista. Senão retorna string vazia<p>
     * @since jCompany 3.0
     */
     public String getMensagemRaiz(){

     	if (causaRaiz != null){
            return causaRaiz.getMessage()+"";
     	}else{
           return "";
        }

    }
     
	/**
	 * @since jcompany 3.0
	 */	
	public void setMessage( String message ) {
		this.message = message;
	}
	
	/**
	 * @since jcompany 3.0
	 */
	public String getMessage () {
		if(message==null && causaRaiz!=null){
			return causaRaiz.getMessage();
		}
		return message;
	}
	
	/**
	 * @since jcompany 6.0
	 */
	public String getMessageError() {
		return messageError;
	}

	/**
	 * @since jcompany 6.0
	 */
	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	/**
	 * @since jcompany 3.0
	 */
	public void setCausaRaiz(Throwable anException) {
		causaRaiz = anException;
	}
	
	/**
	 * @since jcompany 3.0
	 */
	public Throwable getCausaRaiz() {
		return causaRaiz;
	}
	
	/**
	 * @since jcompany 3.0
	 */
	public Logger getCausaLogger() {
		return causaLogger;
	}

	/**
	 * @since jcompany 3.0
	 */
	public void setCausaLogger(Logger Loggeral) {
		this.causaLogger = Loggeral;
	}

	private String getMessageErrorGeneric(){
		return getMessage(PlcBeanMessages.MSG_ERRO_GENERICO);
	}
	
	private <BeanMessage extends Enum<?>>  String getMessageErrorByKey(BeanMessage messageKey, Object[] messageArgs) {
		
		Object[] msg = new Object[messageArgs.length];
		int i = 0;
		for (Object object : messageArgs) {
			
			if(object!=null && Exception.class.isAssignableFrom(object.getClass())) {
				if(((Exception)object).getCause() != null && PlcException.class.isAssignableFrom(((Exception)object).getCause().getClass())) {
					msg[i] = ((PlcException)((Exception)object).getCause() ).getMessageError();
				} else {
					msg[i] = ((Exception)object).toString();
				}
			} else if (object!=null) {
				msg[i] = messageArgs[i].toString();
			} else {
				msg[i] = "";
			}
			i++;
		}
		
		return getTranslatedMessage(messageKey, msg);
	}
	
	private <BeanMessage extends Enum<?>>  String getMessageErrorByKeyWithoutSystemMessage(BeanMessage messageKey, Object[] messageArgs) {

		String messageClean = getMessageErrorByKey(messageKey, messageArgs);
		
		if(StringUtils.isNotBlank(messageClean) && StringUtils.containsIgnoreCase(messageClean, "Mensagem do Sistema")) {
			messageClean = messageClean.substring(0, StringUtils.indexOfIgnoreCase(messageClean, "Mensagem do Sistema" ));
		}
		
		return messageClean;
	}
	
	private <BeanMessage extends Enum<?>>  String getMessageErrorByKeyWithoutArgs(BeanMessage messageKey){
		return getMessage(messageKey);
	}
	
	private boolean isConstraintViolationException(Throwable exception, Object[] messageArgsLoc)   {
			
		if (exception instanceof javax.validation.ConstraintViolationException || exception instanceof org.hibernate.exception.ConstraintViolationException ) {
			
			String constraint 	= "";
			String cName 		= "";
			String mensagem 	= "";
			String causaTexto 	= "";
			
			if(exception instanceof org.hibernate.exception.ConstraintViolationException) {
				causaTexto = ((org.hibernate.exception.ConstraintViolationException)exception).getCause().getLocalizedMessage();
			} else {
				if(exception.getCause() == null || ((javax.validation.ConstraintViolationException )exception).getCause() == null || 
				   exception.getCause().getLocalizedMessage() == null || ((javax.validation.ConstraintViolationException )exception).getCause().getLocalizedMessage() == null){
					causaTexto = exception.toString(); 
				}else{
					causaTexto = ((javax.validation.ConstraintViolationException )exception).getCause().getLocalizedMessage();
				}				
			}
			
			//Primeiro procura por uma mensagem com base no prefixo configurado nos package-info.java
			String prefixofk = getPrefixoFk();
		    
				log.info("###Buscando pela mensagem de erro com base na contraint do banco. Prefixo: " + prefixofk);
			if (causaTexto.contains(prefixofk)) {
				for (int i = causaTexto.indexOf(prefixofk); i < causaTexto.length(); i++) {
					if (causaTexto.substring(i,i+1).equals(" ") 
							|| causaTexto.substring(i,i+1).equals("'")
							|| causaTexto.substring(i,i+1).equals("\"")
							|| causaTexto.substring(i,i+1).equals(")")
							|| causaTexto.substring(i,i+1).equals("]")) {
						break;
					}
					cName = cName + causaTexto.substring(i,i+1);
				}
				cName = "{jcompany.erros.persistencia.constraint." + cName+"}";
				mensagem = getMessageInPropertiesFile(cName, messageArgsLoc);
				if(StringUtils.isBlank(mensagem) || (mensagem.startsWith("???") && mensagem.endsWith("???"))) {
					log.info("####NÃO FOI ENCONTRADA A MENSAGEM PARA A CHAVE [" + cName + "]. Será emitida mensagem padrão.");
					//Caso não encontre, utiliza o nome completo da constraint
					if(exception instanceof org.hibernate.exception.ConstraintViolationException) {
						constraint = ((org.hibernate.exception.ConstraintViolationException)exception).getConstraintName();
						if (StringUtils.isNotEmpty(constraint)) {
							cName = "{jcompany.erros.persistencia.constraint." + constraint +"}";
							mensagem = getMessageInPropertiesFile(cName, messageArgsLoc);
						}
					}	
				}
			}
			
			if ((StringUtils.isBlank(mensagem) || (mensagem.startsWith("???") && mensagem.endsWith("???"))) && StringUtils.isNotEmpty(cName)) {
				log.info("####NÃO FOI ENCONTRADA A MENSAGEM PARA A CHAVE [" + cName + "]. Será emitida mensagem padrão.");
				setMessage(getMessageErrorByKeyWithoutSystemMessage(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_DELETE_CONSTRAINT, new Object[]{causaTexto, }));
			} else if (StringUtils.isNotEmpty(mensagem)){
				setMessage(mensagem);
			} else {
				setMessage(getMessageErrorByKeyWithoutSystemMessage(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_DELETE_CONSTRAINT, new Object[]{causaTexto}));
			}
			
			setCausaRaiz(null);
			return true;
		}
		return false;
	}	

	/**
	 * Procura a mensagem no arquivo de mensagens, baseado na chave passada.
	 * 
	 * @param messageError
	 * @return
	 */
	private String getMessageInPropertiesFile(String messageError, Object[] messageArgsLoc) {
		
		//verificando se a mensagem esta no padrão: {mensagem}
		if (messageError.length() > 1 && messageError.substring(0,1).equals("{") && 
				messageError.substring(messageError.length()-1, messageError.length()).equals("}")) {
			
			//Tenta obter o locale do usuario se tiver sido setado ou o padrao do sistema operacional do servidor
			Locale localePadrao = locale.get()!=null?locale.get():Locale.getDefault();
						
			String messageErrorAux = messageError.substring(1,messageError.length()-1);
			String nome = PlcConstants.PlcJsfConstantes.BUNDLE_PADRAO_TAGS + "_" + localePadrao + ".properties";
			Properties props = new Properties();  
	        try {
	        	InputStream in = getClass().getClassLoader().getResourceAsStream(nome);
	        	if (in != null) {
	        		props.load(in);
	        		in.close();
	        		if (StringUtils.isNotEmpty(props.getProperty(messageErrorAux))) {
	        			messageError = props.getProperty(messageErrorAux);
	        		} else {
	        			messageError = "??? " + localePadrao + " " + messageErrorAux + " ???";
	        		}
	        	}	
			} catch (IOException e) {
				// não faz nada, vai imprimir a mensagem que chegou
			}
	        
	        // Possibilita informar qual o registro está tentando ser excluído, caso necessário.
	        // Criar a mensagem de erro, com um token para ser substituído, ex:
	        // jcompany.erros.persistencia.constraint.FK_GRUPO_TIPOGRUPO=Não foi possivel excluir esse(s) registro(s). Verifique se algum Grupo utiliza o registro :id e nome :toString.
			if(messageArgsLoc != null && messageArgsLoc[2] != null) {
				
				log.info("getMessageInPropertiesFile - Procurando pelos tokens padrões para substituição na mensagem de erro.");
				
				PlcMetamodelUtil metamodelUtil = getPlcMetamodelUtil();
				
				Object entity = messageArgsLoc[2];
				if(entity != null && metamodelUtil!=null) {
					
					PlcEntityInstance entityPlcInstance = metamodelUtil.createEntityInstance(entity);
					
					String id		= entityPlcInstance.getIdAux();
					String toString = entity.toString();
					
					if(StringUtils.isNotEmpty(messageError)) {
						if(messageError.contains(":id") && StringUtils.isNotEmpty(id)) {
							messageError = messageError.replaceAll(":id", id);
							
						}
						if(messageError.contains(":toString") && StringUtils.isNotEmpty(toString)) {
							messageError = messageError.replaceAll(":toString", toString);
							
						}
					} 
				}
			}
		} 	
		return messageError;
	}
	
	protected String getMessage(Enum<?> e) {
		
		if(PlcExecutionMode.getInstance().isUnitTest()){
			return e.toString();
		}
		
		return getMessageConveyorFactory().getDefaultMessageConveyer(PlcMessageConveyorFactory.PREFIXO).getMessage(e);
	}
	
	protected <BeanMessage extends Enum<?>> String getTranslatedMessage(BeanMessage messageKey, Object[] messageArgs){
		if(PlcExecutionMode.getInstance().isUnitTest()){
			return messageKey.toString();
		}
		
		return getMessageConveyorFactory().getDefaultMessageConveyer(PlcMessageConveyorFactory.PREFIXO).getMessage(messageKey, messageArgs);
	}
	
	
	protected boolean showInternalMessage(){
		
		if(!PlcExecutionMode.getInstance().isUnitTest()){
			PlcConfigAppBehavior configBehavior = getConfigUtil().getConfigApplication().behavior();
			if(configBehavior != null && !configBehavior.showInternalMessages()){
				return true;
			}
		}
		return false;
	}
	
	public PlcMessageConveyorFactory getMessageConveyorFactory() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcMessageConveyorFactory.class, QPlcDefaultLiteral.INSTANCE);
	}
	
	protected String getPrefixoFk() {
		
		if (!PlcExecutionMode.getInstance().isUnitTest() && getConfigUtil()!=null && getConfigUtil().getConfigApplication()!=null && getConfigUtil().getConfigApplication().persistence()!=null)
			// Prefixo de Constraint (tipicamente Foreign Key) utilizada. 
			// Importante para emitir mensagens personalizadas quando ocorrer erro de ConstraintViolationException.
			// Essa configuração está disponível na Classe PlcConfigPersistence.
			return getConfigUtil().getConfigApplication().persistence().constraintPrefix();
		
		return "";
	}
	
	public PlcConfigUtil getConfigUtil() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
	}
	
	protected PlcMetamodelUtil getPlcMetamodelUtil(){
		
		if(!PlcExecutionMode.getInstance().isUnitTest()){
			return PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		}
		
		return null;
	}

	
	public Object getObjectError() {
		return objectError;
	}

	public void setObjectError(Object objectError) {
		this.objectError = objectError;
	}

	public Enum<?> getTypedError() {
		return typedError;
	}

	public void setTypedError(Enum<?> typedError) {
		this.typedError = typedError;
	}
}

