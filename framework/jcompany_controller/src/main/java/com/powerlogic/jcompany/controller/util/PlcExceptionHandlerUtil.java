/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidadinternal.ui.laf.base.InstallLafIconProvider;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcMessageConveyorFactory;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;

/**
 * jCompany 3.0. Classe para tratamento de mensagens genéricas de exceção
 * @since jCompany 3.0
 */

@SPlcUtil
@QPlcDefault
public class PlcExceptionHandlerUtil {

	protected static Logger log = Logger.getLogger(PlcExceptionHandlerUtil.class.getCanonicalName());
	
	@Inject @QPlcDefault 
	protected PlcI18nUtil i18nUtil;
		
	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
	protected PlcMessageConveyorFactory messageConveyorFactory;

	/**
 	 * jCompany 2.5.3. Retorna a Interface do Serviço de Persistência armazenado no escopo da aplicação
 	 */
	protected IPlcFacade getFacadeService()  {
    	return iocControleFacadeUtil.getFacade();
	}
	
	/**
	 * jCompany 3.0. Trata erros de persistencia
	 * @return Exceção contendo messagekey e um argumento alterados para mensagem especifica, com
	 * exceção original em causaRaiz. Ou null se não foi tratada
	 */
	// TODO - Verificar a necessidade desse método e da chamada ao facade
	public PlcException handleErrorsWrapperModel(PlcException plcException) {

		log.debug( "############# Entrou em trataErrosPersistencia");
		
		Throwable causaRaiz = (Throwable) plcException.getCausaRaiz();
		
		Object[] msgs=null;

		if (causaRaiz != null) {
				
			try{
				
				IPlcFacade plc = getFacadeService();
				
				msgs = plc.findExceptionMessage(causaRaiz);
				
				String messageConveyor = "";
				
				messageConveyorFactory = PlcCDIUtil.getInstance().getInstanceByType(PlcMessageConveyorFactory.class, QPlcDefaultLiteral.INSTANCE);
				
				if (messageConveyorFactory != null){
					messageConveyor = messageConveyorFactory.getDefaultMessageConveyer(i18nUtil.getCurrentLocale(), PlcMessageConveyorFactory.PREFIXO).getMessage((PlcBeanMessages)msgs[0], msgs[1]);
				}
			
				plcException.setMessage(messageConveyor);
				
				String[] msgsArgs = new String[3];
				int cont=0;
				for (int i = 1; i < msgs.length; i++) {
					if (msgs[i]!=null) {
						msgsArgs[cont]=msgs[i].toString();
						cont++;
					}
				}
				
			} catch (Exception e) {
				// Não faz nada para evitar loops possíveis nesta situação (Exceção sendo disparada do ExceptionHandler
				return null;
			}
				
			// Somente devolve se foi realmente tratada!
			if (log.isDebugEnabled()) {
				log.debug( "Mensagem de Erro recebida=" + plcException.getMessage() + " Classe erro raiz=" + causaRaiz.getClass());	
			}
			
			if (msgs != null) {
				return plcException;
			}
		}

		// A exceção não foi tratada pela camada de modelo/persistencia ou não tem exceçao raiz.
		return null;
	}
	
	/**
	 * jCompany 3.0 Trata erros controle e geral
	 */
	public PlcException handleErrorsController (Throwable erroG) {
		String causaTexto = null;
		Logger logControle = log;
		if (erroG!=null && erroG.getStackTrace()!=null && erroG.getStackTrace().length>0 && erroG.getStackTrace()[0].getClassName()!=null)
			logControle = Logger.getLogger(erroG.getStackTrace()[0].getClassName());

		// Não trata se for erros com 'wrapper'
		if (erroG == null || PlcException.class.isAssignableFrom(erroG.getClass()))
		    return null;
		
		log.debug( "Entrou em trata erros geral");
		
		if (erroG instanceof javax.servlet.ServletException && erroG.getMessage() != null &&
				erroG.getMessage().indexOf("does not contain handler parameter named")>0) {
			
			return new PlcException(PlcBeanMessages.JCOMPANY_ERROR_EVENT,new String[]{erroG.getLocalizedMessage()},erroG,logControle);
		
		} else if (erroG instanceof java.lang.reflect.InvocationTargetException ) {
		    
			InvocationTargetException ite = (InvocationTargetException) erroG;
			
			 Throwable cause = ite.getCause();
		     // 
		     if (cause != null) {
		              causaTexto = cause.getLocalizedMessage();
		     } else {
		              causaTexto = ite.getLocalizedMessage();
		     }
		 
		    if (!causaTexto.equals(""))
		    	// Funcionamento com EJB no WAS
		    	if (causaTexto.contains("See nested exception;")) {
		    		Throwable ee = cause.getCause();
		    		while(true) {
		    			if (ee instanceof PlcException && ((PlcException)ee).getCausaRaiz()!=null && ((PlcException)ee).getCausaRaiz().getCause()!=null){
			    			ee = ((PlcException)ee).getCausaRaiz().getCause();
		    			} else if (ee.getCause()!=null){
			    			ee = ee.getCause();
		    			} else {
		    				break;
		    			}
		    		}
		    		if (ee instanceof PlcException)
		    			return (PlcException)ee;
		    		else 
		    			return new PlcException(PlcBeanMessages.JCOMPANY_ERROR_EVENT,
			    			new String[]{ee.getLocalizedMessage()},ee.getCause(),logControle);
		    	} else { 
		    		return new PlcException(PlcBeanMessages.JCOMPANY_ERROR_EVENT,
		    			new String[]{ite.getCause().getLocalizedMessage()},ite.getCause(),logControle);
		    	}	
		    else
		    	return new PlcException(PlcBeanMessages.JCOMPANY_ERROR_EVENT,new String[]{ite.getCause()+""},ite.getCause(),logControle);
		    
		} else  {
			return new PlcException(PlcBeanMessages.JCOMPANY_ERROR_EVENT,new String[]{erroG.toString()},erroG,logControle);
		}
		

	}
	
	/**
	 * jCompany 3.0 Trata erros controle e geral
	 */
	public PlcException handleErrorsWrapperController (PlcException plcException) {

		// Não trata se for erros com 'wrapper'
		if (plcException.getCausaRaiz() == null)
		    return null;
		else {
		
			PlcException excecaoControle = handleErrorsController(plcException.getCausaRaiz());
		
			// Mantém os defaults do wrapper na nova exceção
			if (excecaoControle != null) {
				excecaoControle.setCausaLogger(plcException.getCausaLogger());
			}
			
			return excecaoControle;
		}
		
					
	}
	
	/**
	 * jCompany 2.7.1. Recebe uma exceção e devolve String com sua Stack Trace
	 * @param e Exceção
	 * @param formataHTML true para formatar com HTML \<br\> e \<p\> cada linha
	 * @return String contendo stack trace
	 */
	public String stackTraceToString(Throwable e, boolean formataHTML) {
	    
		log.debug( "########## Entrou em enclosing_method");

		//adicionar no outputStream o valor do printStream
		ByteArrayOutputStream ops = new ByteArrayOutputStream();

		e.printStackTrace();

		StringBuffer stackTrace = new StringBuffer();
		if (formataHTML)
			stackTrace.append("<p class=\"msgExceptionPlc\"><b>");
		stackTrace.append("Stack trace:");
		if (formataHTML)
			stackTrace.append("</b><br/>");

		stackTrace.append(ops.toString());

		if (formataHTML)
			stackTrace.append("</p>");

		return stackTrace.toString();
       
	}
	
	  /**
     * jCompany 3.0. Realiza a 'pauta mínima' de tratamento de exceções de qualquer tipo:<p>
     * 1. Emite o stack trace para console<br>
     * 2. Armazena o erro no request para exibição para usuários<br>
     * 3. Envia um log do log4j (para qualquer appender definido)<br>
     * 4. Envia um log especial com appender para disparo de email/JMS.
     */
    public void handleExceptions( HttpServletRequest request,
    		PlcException excecaoTratada) {

    	log.debug( "############# Entrou em realizaAcoesTratamento");
   	
    	if (excecaoTratada == null)
    		log.fatal( "Erro ao tentar fazer tratamento de excecao generico. Excecao tratada esta nula ");
    	
    	Boolean ehExcecaoControlada = true;
    	while (true){
    		if (excecaoTratada != null && excecaoTratada.getCausaRaiz() != null){
    			if (excecaoTratada.getCausaRaiz() instanceof PlcException){
    				excecaoTratada = (PlcException) excecaoTratada.getCausaRaiz();
    				ehExcecaoControlada = true;    				
    			}else{
    				ehExcecaoControlada = false;
    				break;
    			}
    			
    		}else{
    			break;
    		}
    	}
    	
    	// Emite o stack trace, se não for exceção interna
    	if (!ehExcecaoControlada){
			// Envia o log4j com o nível apropriado, conforme configuração
    		// Usuário
    		String login = "Anônimo";
    		if (request.getUserPrincipal() != null) {
    			login = request.getUserPrincipal().getName();
    		}
    		if (excecaoTratada.getCausaRaiz().getCause() != null && Exception.class.isAssignableFrom(excecaoTratada.getCausaRaiz().getCause().getClass())) {
    			sendAutomaticLog(login,excecaoTratada,(Exception)excecaoTratada.getCausaRaiz().getCause());
    		} else if (Exception.class.isAssignableFrom(excecaoTratada.getCausaRaiz().getClass())) {
    			sendAutomaticLog(login,excecaoTratada,(Exception)excecaoTratada.getCausaRaiz());
    		} else {
    			log.fatal( "Erro de nivel diferenciado disparado. Erro: " + excecaoTratada.getCausaRaiz(), excecaoTratada.getCausaRaiz());
    		}
		}
    	
    	if(excecaoTratada.getCausaRaiz() == null){
    		// Retirado log.fatal pois não tem necessidade de informar um log com uma execeção lançada somente com menssagem  
    		// Com o log.faltal vai para produção poluindo o console
    		// log.info só vai para desenvolvimento.
    		if(excecaoTratada.getMessage() != null) {
    			log.info("Erro de nivel diferenciado disparado. Erro: " + excecaoTratada.getMessage());
    		} else {
    			if(excecaoTratada.getMessageError() != null ) {
    				log.info("Erro de nivel diferenciado disparado. \n\n Erro: " + excecaoTratada.getMessageError());
    			} else {
    				log.info("Erro de nivel diferenciado disparado. Erro não encontrado. \n\n Debug Linha 295 - PlcExceptionHandlerUtil " );
    			}
    		}
    	}
	}
    
    /**
     * jCompany 3.0. Realiza a 'pauta mínima' de tratamento de exceções de qualquer tipo:<p>
     * 1. Emite o stack trace para console<br>
     * 2. Armazena o erro no request para exibição para usuários<br>
     * 3. Envia um log do log4j (para qualquer appender definido)<br>
     * 4. Envia um log especial com appender para disparo de email/JMS.
     */
    public void handleExceptions(PlcException excecaoTratada) {

    	log.debug( "########## Entrou em realizaAcoesTratamentoBatch");
   	
    	if (excecaoTratada == null)
    		log.fatal( "Erro ao tentar fazer tratamento de excecao generico. Excecao tratada estah nula ");
		
		// Usuário
		String login = "Batch";

    	// Emite o stack trace, se não for exceção interna
		if (excecaoTratada.getCausaRaiz() != null) {
			
			// Envia o log4j com o nível apropriado, conforme configuração
			if (excecaoTratada.getCausaRaiz().getCause() != null && Exception.class.isAssignableFrom(excecaoTratada.getCausaRaiz().getCause().getClass())) {
				sendAutomaticLog(login,excecaoTratada,(Exception)excecaoTratada.getCausaRaiz().getCause());
			} else if (Exception.class.isAssignableFrom(excecaoTratada.getCausaRaiz().getClass())) {
				sendAutomaticLog(login,excecaoTratada,(Exception)excecaoTratada.getCausaRaiz());
			} else {
				log.fatal( "Erro de nivel diferenciado disparado. Erro: "+excecaoTratada.getCausaRaiz(),excecaoTratada.getCausaRaiz());
			}
			
		} else {
			// Excecao interna
			sendAutomaticLog(login,excecaoTratada,excecaoTratada);
		}

	}
    
    /**
     * jCompany 3.0 Envia log via JMS ou Email para mensagens inesperadas, se enviadas com exceção raiz
     * como argumento, no padrão  new PlcException("message.key",e);
     */
    public void sendAutomaticLog(String login,PlcException excecaoTratada, Exception excecaoRaiz)  {
    	
    	if (excecaoRaiz == null)
    		excecaoRaiz = excecaoTratada;
    	
		if (excecaoTratada.getCausaLogger() != null) {

			Logger logErro = excecaoTratada.getCausaLogger();

			String mensagem = translateMessage(excecaoTratada);
			
			if (despiseLog(mensagem,excecaoTratada,excecaoRaiz)) {
				return;
			}
			
			logErro.error("Usuario corrente: " + login + " Erro: " + mensagem,excecaoRaiz);
			
		}

    	
    }
    
    /**
     * @since jCompany 3.2.3 Indica para não enviar log (stack trace) em alguns casos especiais, que já foram enviados ou cujo
     * tratamento é mais sofisticado.  Exemplo: Integridade Referencial.
     */
    protected boolean despiseLog(String mensagem,PlcException excecaoTratada, Exception excecaoRaiz)  {
    	return erroForeignKey(mensagem); 
    }
    
    /**
     * @return true se mensagem de erro for erro de foreign key. Pode precisar de especialização para outros SGBDs que não sejam Oracle e Derby.
     */
	public boolean erroForeignKey(String mensagem) {
		return mensagem.toLowerCase().indexOf("foreign key")>-1 || mensagem.toLowerCase().indexOf("integrity constraint")>-1 ;
	}

	private String translateMessage(PlcException excecaoTratada) {
		
		return excecaoTratada.getMessage();
	}

	/**
	 * Recebe uma exceção e interpreta se é uma exceção controlada internamente ou wrapper que contém exceção interna.
	 * Verifica ainda se exceção externa nao foi convertida para um wrapper, conforme o padrao, e se não foi ajusta para o tratamento.
	 * @param ex Exceçao a ser interpretada
	 * @return "wrapper" PlcException contendo a exceção, ajustada
	 */
	public PlcException interpretExcecao(Throwable ex) {

		// Se exceção for interna, devolve a própria, pois já está tratada
		if (PlcException.class.isAssignableFrom(ex.getClass())) {
			Object exCausaRaiz =((PlcException)ex).getCausaRaiz(); 
			if (exCausaRaiz != null) {
				return (PlcException)ex;
			}
		}
		
		// Somente passa daqui se exceção for diferente de PlcException ou PlcException com exceção raiz
		PlcException excecaoInterpretada = null;
		
		// Se for 'wrapper' PlcException, submete para interpretação da camada modelo (da camada modelo somente
		// chega com 'wrapper'
		if (PlcException.class.isAssignableFrom(ex.getClass())) {
			excecaoInterpretada = handleErrorsWrapperModel((PlcException)ex);
		}
		
		// Se foi tratada pela camada modelo/persistencia, retorna
		if (excecaoInterpretada != null) {
			return excecaoInterpretada;
		}
		
		// É wrapper e tem causa raiz da camada controle/comuns
		if (PlcException.class.isAssignableFrom(ex.getClass())) {
			// Finalmente, submete para tratamento da camada controle, que o fará de qualquer modo
			return handleErrorsWrapperController((PlcException)ex);	
		} else {
			// Não foi transformada para wrapper, mesmo assim tenta fazer o tratamento
			return handleErrorsController(ex);	
		}
	}

				
}

