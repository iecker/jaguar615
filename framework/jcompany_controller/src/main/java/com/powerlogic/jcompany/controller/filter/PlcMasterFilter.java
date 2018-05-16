/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.ACAO;
import com.powerlogic.jcompany.commons.PlcConstants.MODOS;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.integration.IPlcJMonitorApi;
import com.powerlogic.jcompany.commons.integration.IPlcJSecurityApi;
import com.powerlogic.jcompany.commons.integration.jmonitor.PlcClientDTO;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.appender.PlcStringAppender;
import com.powerlogic.jcompany.controller.init.PlcJCPInitializer;
import com.powerlogic.jcompany.controller.servlet.PlcServletUtil;
import com.powerlogic.jcompany.controller.util.PlcBaseUserProfileUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcCookieUtil;
import com.powerlogic.jcompany.controller.util.PlcExceptionHandlerUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

/**
 * Filtro principal do jCompany responsável pela configuração do encoding do request e tratamentos adicionais de segurança, quando configurado.
 * <p>
 * Seu utilização substitui a necessidade dos filtros PlcEncodeLatin1Filter e PlcEncodeUtf8Filter, pois essa informação passa a ser um parâmetro de configuração desse filter.
 * </p>
 * Exemplo:
 * <code>
 * <pre>
 * &lt;filter&gt;
 *    &lt;filter-name&gt;PlcMasterFilter&lt;/filter-name&gt;
 *    &lt;filter-class&gt;com.powerlogic.jcompany.controller.filter.PlcMasterFilter&lt;/filter-class&gt;
 *    &lt;init-param&gt;
 *       &lt;param-name&gt;encode&lt;/param-name&gt;
 *       &lt;param-value&gt;utf-8&lt;/param-value&gt;
 *    &lt;/init-param&gt;
 * &lt;/filter&gt;
 * </pre>
 * </code>
 * 
 * @since jCompany 3.0
 */
public class PlcMasterFilter implements Filter {
	
	private static final String RESOLUCAO_VIDEO = "resolucaoPlc";
	private static final String LISTA_ARGUMENTOS = "listaArgumentos";
	private static final String IND_MSG_ERRO = "msgVermelhoPlc";
	
	private static final Logger log = Logger.getLogger(PlcMasterFilter.class.getCanonicalName());

	private static final String RESULTADO_HEAP = "resultadoHEAPPlc";

	private String characterEncoding = "utf-8";

	//Como o CDI não atua em classes instanciadas pelo Container, neste caso instancia no inicio do processamento
	//TODO - Analisar se com Servlet 3.0 será possível injetar via CDI.
	protected IPlcJMonitorApi jMonitorApi;
	protected IPlcJSecurityApi jSecurityApi;
	
	//TODO - Implementar integração com jCompany QA 
	//protected IPlcQAApi qaApi;
	
	protected PlcImportAppInfoUtil importAppInfoUtil;
	protected PlcCookieUtil cookieUtil;
	protected PlcConfigUtil configUtil;
	protected FilterConfig filterConfig;
	
	protected PlcBaseUserProfileUtil plcProfile;
	
	protected PlcJCPInitializer jcompanyInitializer;
	

	public PlcMasterFilter() { 
		jSecurityApi 		= PlcCDIUtil.getInstance().getInstanceByType(IPlcJSecurityApi.class);
		jMonitorApi 		= PlcCDIUtil.getInstance().getInstanceByType(IPlcJMonitorApi.class);
		configUtil 			= PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
		cookieUtil 			= PlcCDIUtil.getInstance().getInstanceByType(PlcCookieUtil.class, QPlcDefaultLiteral.INSTANCE);
		importAppInfoUtil 	= PlcCDIUtil.getInstance().getInstanceByType(PlcImportAppInfoUtil.class, QPlcDefaultLiteral.INSTANCE);
		plcProfile 			= PlcCDIUtil.getInstance().getInstanceByType(PlcBaseUserProfileUtil.class, QPlcDefaultLiteral.INSTANCE);
		jcompanyInitializer = PlcCDIUtil.getInstance().getInstanceByType(PlcJCPInitializer.class);
	}

    public void init(FilterConfig config) throws ServletException {
  		
		String encoding = config.getInitParameter("encode");
	
		if (!StringUtils.isBlank(encoding)) {
			this.characterEncoding = encoding;
		}
		
		filterConfig=config;
	}
  
    protected void configureServletUtil(ServletRequest request, ServletResponse response) {
    	
    	PlcServletUtil servletUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcServletUtil.class, QPlcDefaultLiteral.INSTANCE);
    	servletUtil.setRequest(request);
    	servletUtil.setResponse(response);
    	
	}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    	
    	HttpServletRequest req = (HttpServletRequest) request;
    	
    	jcompanyInitializer.init(filterConfig.getServletContext());
    	        
    	try {
    		// Configura encoding
    		request.setCharacterEncoding(characterEncoding);
    		response.setCharacterEncoding(characterEncoding);
    		
    		configureServletUtil(request, response);
    		// Registra tempo de início da requisição
        	boolean modoTeste = (request.getParameter(PlcConstants.QA.MODO_TESTE)!=null || ((HttpServletRequest)request).getSession().getAttribute(PlcConstants.QA.MODO_TESTE)!=null) && !isModoProducao(req.getSession().getServletContext());
        	
    		if (modoTeste) {
    			request.setAttribute("tempo_inicio", System.currentTimeMillis());
    		}
    		
    		// Processa o app-info, apenas se nao possuir o jSecurity.
    		if (importAppInfoUtil.checkAndResponseAppInfo((HttpServletRequest) request, (HttpServletResponse) response)) {
    			return;
    		}
    		
    		if (!verificaRedirecionamentoEsquema((HttpServletRequest)request, (HttpServletResponse)response))
    			return;
     		
    		if (!isModoProducao(req.getSession().getServletContext()) ) {
    			// Em nivel zero, faz avaliação de memoria, pois com o profiling atuando
    			// a distorção invalida esta avaliação. Evita instanciar o Profing em producao
    			// para economia de memoria permanente, por isso testa o modo na frente.
    			request.setAttribute(RESULTADO_HEAP, (Runtime.getRuntime().freeMemory()/1000));

    		}
    		
    		registraLogginModoTesteInicio((HttpServletRequest) request, modoTeste);
    		
    		registryUserProfile((HttpServletRequest) request, (HttpServletResponse) response);

    		// Integração com jCompany QA
    		//if (qaFilter!= null) {
    		//	qaFilter.doFilter(request, response, null);
    		//}
    		
    		if (jSecurityApi != null) {
    			jSecurityApi.efetivaSeguranca(request, response);
    		}

    		if(!response.isCommitted()) {
    			filterChain.doFilter(request, response);
    		}
    		
    		if (jMonitorApi!= null) {
    			logRequisicaoJmonitor(req);
    		}
    		
    		
    	} catch (Exception e) {
    		trataExcecaoFilter(request, response, e);
    	}
    }

	private void logRequisicaoJmonitor(HttpServletRequest req) {
		//Ação do Caso de Uso 
		String acao = (String) req.getAttribute(MODOS.MODO);	

		//Evento que originou a conexão
		String evento = req.getParameter(ACAO.EVENTO);
		if(StringUtils.isBlank(evento)){
			if (req.getAttribute(ACAO.EVENTO)!=null){
				evento = (String)req.getAttribute(ACAO.EVENTO);
			}else{
				evento = "";
			}
		}
		
		PlcClientDTO clienteDTO = new PlcClientDTO();
		
		if (req.getUserPrincipal() != null) {
			clienteDTO.setLogin(req.getUserPrincipal().getName());
		} else if (req.getRemoteUser() != null) {
			clienteDTO.setLogin(req.getRemoteUser());
		}
		 
		clienteDTO.setIp(req.getRemoteAddr());
		clienteDTO.setHost(req.getRemoteHost());
		clienteDTO.setUserAgent(req.getHeader("user-agent"));
		clienteDTO.setResolucao(recuperaResolucaoVideo(req));

		List listaMsgErro = (List) req.getAttribute(IND_MSG_ERRO);
		
		List args = (List)req.getAttribute(LISTA_ARGUMENTOS);

		Object entidade =  req.getAttribute(PlcConstants.ENTITY_OBJECT);

		jMonitorApi.logRequisicao(req.getRequestURI(),req.getContextPath(),req.getQueryString(),acao,evento,clienteDTO, listaMsgErro,entidade,args);
	}

	protected void trataExcecaoFilter(ServletRequest request, ServletResponse response, Exception e) throws IOException {
		
		// Código adicionado para controlar o erro de view expirada quando ocorre expiração na sessão de usuário.
		
		if (e != null && e.getCause()!=null) {
			if ( e.getCause().getClass().getName().equals("javax.faces.application.ViewExpiredException")){
				((HttpServletResponse)response).sendRedirect(((HttpServletRequest)request).getContextPath());
			} else {
				trataExcecao(request, response, e);
			}
		} else {
			log.error("Erro inesperado ao executar a classe PlcMasterFilter.", e);
		}
		
	}
	
	 /**
     * Recupera a resolução do vídeo do cliente. Procura primeiro na sessão, senão encontrar, procura nos cookies.
     * 
     * @param request
     */
    private String recuperaResolucaoVideo(HttpServletRequest request) {
    	
    	String res = null;
    	
    	if(request.isRequestedSessionIdValid()){
    		res = (String) request.getSession().getAttribute(RESOLUCAO_VIDEO);
    	}
        
        if (res == null) {
        	res = cookieUtil.getCookieValue(request, RESOLUCAO_VIDEO);
        }
        if (log.isDebugEnabled()) {
            log.debug( "Achou resolucao = "+res);
        }
        return res;
    }
    
    /**
     * Verifica se ha um flag indicado para que o esquema do SGBD seja atualizado (gerado no ServletContextListener) e se tiver redireciona
     */
    protected boolean verificaRedirecionamentoEsquema(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
    	PlcMsgUtil msgUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgUtil.class, QPlcDefaultLiteral.INSTANCE);
    	
    	if (request.getSession().getServletContext().getAttribute(PlcConstants.STARTUP)!=null) {
    		
    		msgUtil.msg(PlcBeanMessages.JCOMPANY_SCHEMA_SCRIPT_STARTUP, PlcMessage.Cor.msgAmareloPlc.toString());   		
    		
    		if (request.getRequestURI()!=null && request.getRequestURI().indexOf("/res-plc/esquema") < 0 ) {  			
				response.sendRedirect(request.getContextPath()+"/f/t/res-plc/esquema?evento=x");
    		}
    	}
    	
   		return true;
	}


	/**
     * Método para tratamento de exceção ao executar o filtro.
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     */
	protected void trataExcecao(ServletRequest request, ServletResponse response, Exception exception) throws IOException {
		try {
			PlcExceptionHandlerUtil exceptionHandler = (PlcExceptionHandlerUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcExceptionHandlerUtil.class, QPlcDefaultLiteral.INSTANCE);
			exceptionHandler.handleErrorsController(exception);
			log.info( exceptionHandler.stackTraceToString(exception, false));
			request.setAttribute("erro500Titulo", exception.toString());
			request.setAttribute("erro500", exceptionHandler.stackTraceToString(exception, true));
			if (!((HttpServletResponse)response).isCommitted())
				((HttpServletResponse)response).sendError(500);
			response.flushBuffer();
		} catch (Exception e1) {
			log.error( "Erro ao tentar recuperar tratamento de exceção", e1);
		}
	}
	

	/**
	 * Registra dados do perfil do usuário logado.
	 */
	protected void registryUserProfile(HttpServletRequest request, HttpServletResponse response) {
		
		plcProfile.registryUserProfile(request, response);
	}

	@Override
	public void destroy() {	}
    
    public boolean isModoProducao(ServletContext ctx)  {

    	return "P".equalsIgnoreCase(ctx.getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO));
    	
    }
    
    private void registraLogginModoTesteInicio(HttpServletRequest request, boolean modoTeste) {
		
		if (modoTeste) {
			
	        PlcStringAppender appender;
	        
	        if (Logger.getRootLogger().getAppender(PlcConstants.QA.NOME)==null ) {
	        	
	        	appender = new PlcStringAppender();
	        	
	        	if (Logger.getRootLogger().getAppender("A1") != null) {
	        		appender.setLayout(Logger.getRootLogger().getAppender("A1").getLayout());
	        	}
	        	
	            Logger.getRootLogger().removeAllAppenders();
	            Logger.getRootLogger().addAppender(appender);
	            Logger.getRootLogger().setLevel(Level.WARN);
	            Enumeration _enum = LogManager.getCurrentLoggers();
	            
	            while(_enum.hasMoreElements()) {
	            	Logger logger = (Logger)_enum.nextElement();
	            	if (logger.getName().startsWith("com.powerlogic.jcompany.log") || logger.getName().indexOf("profiling")>-1) {
	            		logger.setLevel(Level.DEBUG);	            		
	            	} else if (	logger.getName().equals("org.hibernate.type") || logger.getName().equals("org.hibernate.SQL") || logger.getName().equals("org.hibernate")) {
	            		logger.setLevel(Level.DEBUG);
	            	} else if (logger.getName().indexOf(".controle.")>1 || logger.getName().indexOf(".modelo.")>1 || logger.getName().indexOf(".persistencia.")>1 || logger.getName().indexOf(".facade.")>1 || logger.getName().indexOf(".comuns.")>1 ) {
	            		logger.setLevel(Level.DEBUG);	            				
	            	} else {
		            	logger.setLevel(Level.WARN);
	            	}
	            }
	        } else {
	        	appender = (PlcStringAppender)Logger.getRootLogger().getAppender(PlcConstants.QA.NOME);
	        	appender.getOut().reset();
	        }
	        
	        request.setAttribute(PlcConstants.QA.LOGGING, appender.getOut());
		}    
    }
}