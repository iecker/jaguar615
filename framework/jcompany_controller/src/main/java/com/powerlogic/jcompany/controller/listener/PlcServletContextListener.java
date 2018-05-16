/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.util.AnnotationLiteral;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.PlcVersionUtil;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.integration.IPlcJMonitorApi;
import com.powerlogic.jcompany.commons.integration.IPlcJSecurityApi;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationPOJO;
import com.powerlogic.jcompany.config.application.PlcConfigLookAndFeel;
import com.powerlogic.jcompany.config.application.PlcConfigModule;
import com.powerlogic.jcompany.controller.appinfo.PlcAppInfoUtil;
import com.powerlogic.jcompany.controller.bindingtype.PlcInicializaAplicacao;
import com.powerlogic.jcompany.controller.servlet.PlcServletContextProducer;
import com.powerlogic.jcompany.controller.template.PlcRiaJavaScriptLocator;
import com.powerlogic.jcompany.controller.template.PlcRiaJavaScriptProcessor;
import com.powerlogic.jcompany.controller.template.PlcRiaJavaScriptUtil;
import com.powerlogic.jcompany.controller.util.PlcCacheUtil;
import com.powerlogic.jcompany.controller.util.PlcClassLookupUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcLocaleUtil;

/**
 * jCompany 3.0 Listener. Abstrata. 
 * Disparada sempre que a aplicação é inicializada ou finalizada no Application Server.
 * Classe para lógicas de inicialização e término no nível do Application Server.
 * 
 * @since jCompany 3.0
 */
public abstract class PlcServletContextListener implements ServletContextListener, ServletRequestListener {

	protected Logger	log	= Logger.getLogger(getClass().getCanonicalName());
	
	// Como o CDI não atua em classes instanciadas pelo Container, neste caso instancia no inicio do processamento
	protected IPlcJMonitorApi jMonitorApi;
	protected IPlcJSecurityApi jSecurityApi;
	protected PlcCacheUtil cacheUtil;
	protected PlcConfigUtil configUtil;
	protected PlcLocaleUtil localeUtil;
	protected PlcAppInfoUtil appInfoUtil;
	protected PlcClassLookupUtil classeLookupUtil;
	protected PlcServletContextProducer servletContextProducer;
	
	static {
		PlcVersionUtil.touch(); //exibe versao
	}

	public PlcServletContextListener() {

	}

	/**
	 * jCompany 3.0 Tratamento de exceção em tempo de inicialização
	 *  
	 */
	public void erroMsg(ServletContext ctx, String msg, Throwable e) {

		// Exemplo de tratamento de erro na inicialização
		try {
			log.fatal( configUtil.getConfigApplication().application().definition().acronym() + " - " + msg + " " + e, e);
			cacheUtil.addInitFatalError(configUtil.getConfigApplication().application().definition().acronym() + " " + msg + " Erro: " + e, e);
		} catch (PlcException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * jCompany. Realiza procedimentos no momento de inicialização do Application Server.
	 */
	public void contextInitialized(ServletContextEvent event) {
		
		log.debug( "########## Entrou ContextInitialized");

		PlcConfigUtil.touch();

		ciInicializaVariaveis();
		
		ciConfiguraElApache();

		ciConfigureServletUtil(event);
		
		ciInicializaMetaDadosAtributoHelper(event);

		ciRegistraVersaoBuild(event);

		ciInicializaServicoMonitor(event);

		ciEmiteMonitoriaEntrada(event);

		ciCarregaClassesLookup(event);

		ciCarregaClassesDominioDiscreto(event);

		ciCarregaOpcoesPersonalizacao(event);

		ciJavaScriptTemplate(event);

		ciRegistraRecursos(event);
		
		ciRegistraLocale(event);
		

		if (jSecurityApi!=null)
			jSecurityApi.iniciaSeguranca();
			
		if (jMonitorApi != null)
			jMonitorApi.iniciaMonitoriaAtivaAplicacao();
			
		try {
			ciAoInicializarAplicacao(event);
		} catch (PlcException plcE) {
			erroMsg(event.getServletContext(), "Erro inesperado em ciAoInicializarAplicacao", plcE.getCausaRaiz());
		}

	}

	protected void ciRegistraLocale(ServletContextEvent event) {
		try{
			localeUtil.configAppUniqueLocale(event.getServletContext());
		} catch (PlcException plcE) {
			erroMsg(event.getServletContext(), "Erro inesperado em ciRegistraLocale", plcE.getCausaRaiz());
		}
	
		
	}

	protected void ciConfiguraElApache() {
		
		// Impedir que implementador de EL do Apache(inclui WAS) converta valores nulos para zero.
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
		
	}

	protected void ciInicializaVariaveis() {
		configUtil 				= PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
		localeUtil 				= PlcCDIUtil.getInstance().getInstanceByType(PlcLocaleUtil.class, QPlcDefaultLiteral.INSTANCE);
		jMonitorApi 			= PlcCDIUtil.getInstance().getInstanceByType(IPlcJMonitorApi.class);
		jSecurityApi 			= PlcCDIUtil.getInstance().getInstanceByType(IPlcJSecurityApi.class);
		classeLookupUtil 		= PlcCDIUtil.getInstance().getInstanceByType(PlcClassLookupUtil.class, QPlcDefaultLiteral.INSTANCE);
		cacheUtil 				= PlcCDIUtil.getInstance().getInstanceByType(PlcCacheUtil.class, QPlcDefaultLiteral.INSTANCE);
		appInfoUtil 			= PlcCDIUtil.getInstance().getInstanceByType(PlcAppInfoUtil.class, QPlcDefaultLiteral.INSTANCE);
		servletContextProducer 	= PlcCDIUtil.getInstance().getInstanceByType(PlcServletContextProducer.class, QPlcDefaultLiteral.INSTANCE);
		
		// Caso precise adicionar algum processamento, pode-se fazer o mesmo através deste Observer
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcInicializaAplicacao>(){});		
		
	}

	protected void ciRegistraRecursos(ServletContextEvent event) {

		jSecurityApi.registraRecursos(event.getServletContext());
		// Registra os Recursos sem conflitar com o jSecurity.
		try {
			appInfoUtil.config(event.getServletContext());
		} catch (ServletException e) {
			log.warn( "Não foi possível registrar os recursos da aplicação: " + e.getMessage(), e);
		} catch (PlcException plcE) {
			log.warn( "Não foi possível registrar os recursos da aplicação: " + plcE.getMessage(), plcE);
		}
	
	}

	/**
	 * @return Verdadeiro se a aplicação está executando em modo de producao ("P")
	 */
	public boolean getModoProducao(ServletContext ctx) {
		return "S".equalsIgnoreCase(ctx.getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO));
	}

	/**
	 * Inicializa a classe de suporte à templates JavaScript, injetando o {@link PlcRiaJavaScriptLocator} e {@link PlcRiaJavaScriptProcessor}.
	 * @param event Evento de inicialzação do Contexto do Servlet.
	 * @see PlcRiaJavaScriptUtil
	 */
	protected void ciJavaScriptTemplate(ServletContextEvent event) {

		PlcRiaJavaScriptUtil      riaJavaScriptUtil      = PlcCDIUtil.getInstance().getInstanceByType(PlcRiaJavaScriptUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcRiaJavaScriptProcessor riaJavaScriptProcessor = PlcCDIUtil.getInstance().getInstanceByType(PlcRiaJavaScriptProcessor.class, QPlcDefaultLiteral.INSTANCE);
		PlcRiaJavaScriptLocator   riaJavaScriptLocator   = PlcCDIUtil.getInstance().getInstanceByType(PlcRiaJavaScriptLocator.class, QPlcDefaultLiteral.INSTANCE);

		// Inicializa o Locator e o processor de Templates.
		riaJavaScriptLocator.setServletContext(event.getServletContext());
		
		// Injeta os Locator e Processor.
		riaJavaScriptUtil.setRiaJavaScriptLocator(riaJavaScriptLocator);
		riaJavaScriptUtil.setRiaJavaScriptProcessor(riaJavaScriptProcessor);

	}

	public void ciInicializaMetaDadosAtributoHelper(ServletContextEvent event) {
		String company = configUtil.getConfigApplication().company().name();
		String application = configUtil.getConfigApplication().application().definition().name();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("nomeEmpresa", company);
		map.put("nomeAplicacao", application);
		event.getServletContext().setAttribute("metadados", map);
	}


	
	/**
	 * jCompany 3.0. Emite log via JMonitor para indicar inicialização
	 */
	protected void ciEmiteMonitoriaEntrada(ServletContextEvent event) {
		java.lang.Runtime r = Runtime.getRuntime();
		long total = r.totalMemory() / 1000;
		long livres = r.freeMemory() / 1000;
		try {
			if (jMonitorApi != null) {
				jMonitorApi.log("ciclodevida", IPlcJMonitorApi.NIVEL_LOG.INFO, "Aplicação Iniciada com " + total + "K de memória total e " + livres + "K de memória livre no servidor");
			}
		} catch (Exception e) {
			erroMsg(event.getServletContext(), "Erro inesperado em ciEmiteMonitoriaEntrada", e);
		}
	}

	/**
	 * Inicializa appenders Log4j para JMS, para contabilizar cliques, auditoria e logging.
	 */
	protected void ciInicializaServicoMonitor(ServletContextEvent event) {

		try {
			
			// Registra constantes declaradas em metadados e relacionadas à integraçao com jMonitor
			if (jMonitorApi!=null)
				jMonitorApi.inicia(configUtil.getConfigApplication().jMonitor().ignoreURL(),
									configUtil.getConfigApplication().jMonitor().sendQueryString(),
									configUtil.getConfigApplication().jMonitor().jmsInitialContextProviderURL(),
									configUtil.getConfigApplication().jMonitor().mailSmtpHost(),
									configUtil.getConfigApplication().jMonitor().fromEmail(),
									configUtil.getConfigApplication().jMonitor().fatalEmail(),
									configUtil.getConfigApplication().jMonitor().errorEmail(),
									event.getServletContext().getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO),
									configUtil.getConfigApplication().jMonitor().pseudoProduction(),
									configUtil.getConfigApplication().application().definition().name(),
									configUtil.getConfigApplication().application().definition().acronym(),
									configUtil.getConfigApplication().jMonitor().uriHtmlBaseToEmail(),
									event.getServletContext().getServerInfo(),
									"");
		
		} catch (Exception e) {
			erroMsg(event.getServletContext(), "Erro inesperado em ciInicializaServicoMonitor", e);
		}

	}

	/**
	 * jCompany 3.0. Registra o nome da versão.release.build no contexto
	 * 		 O numero do build é encontrado no MANIFEST.MF(Implementation-Version) e o valor deve começar com "build." . 
	 * 		 A versão encontra-se no Web.xml
	 * 
	 * jCompany 3.2. Pega a versão apartir do MANIFEST.MF(Implementation-Version) se existir, caso contrario pega do web.xml.
	 * 		O build é encontrado no MANIFEST.MF(Implemetantion-Build)
	 * @throws IOException 
	 */
	protected void ciRegistraVersaoBuild(ServletContextEvent event) {

		log.debug( "########## Entrou em registraVersaoBuild");

		String webXmlVersion = event.getServletContext().getInitParameter(PlcConstants.CONTEXTPARAM.INI_VERSAO);
		
		PlcVersionUtil versaoUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcVersionUtil.class, QPlcDefaultLiteral.INSTANCE);
		try {
			versaoUtil.start(webXmlVersion, IOUtils.toString(event.getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		event.getServletContext().setAttribute(PlcConstants.VERSAO, versaoUtil.getVersion());
		event.getServletContext().setAttribute(PlcConstants.CODIGO_PRODUTO, versaoUtil.getMetaInfProductCode());

	}


	/**
	 * jCompany 3.0 Coloca as coleções das classes declaradas package-info.java, de domínio discreto, em escopo de aplicação.
	 * O uso é feito nas tag files, tipo plcf:comboEstatico.
	 * O padrão é "listaSel[NomeClasseSemPackage]" ou simplesmente "[NomeClasseSemPackage]"
	 */
	protected void ciCarregaClassesDominioDiscreto(ServletContextEvent event) {

		log.debug( "########## Entrou em inicializarAdministrationAppender");

		PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);

		try {

			Class<? extends Enum>[] classesDominioDiscreto = configUtil.getConfigApplication().application().classesDiscreteDomain();
			
			String siglaModulo;
			PlcConfigModule[] modulos = configUtil.getConfigApplication().application().modules();
			if (modulos != null) {
				for (PlcConfigModule modulo : modulos) {
					siglaModulo = "." + modulo.acronym();
					PlcConfigApplicationPOJO configControleModulo = configUtil.getConfigModule(siglaModulo);
					if(configControleModulo != null && configControleModulo.application() != null) {
						classesDominioDiscreto = (Class<? extends Enum>[]) ArrayUtils.addAll(classesDominioDiscreto, configControleModulo.application().classesDiscreteDomain());
					}
				}
			}
		
			for (Class<? extends Enum> classeDominioDiscreto : classesDominioDiscreto) {
				
				String nomeResumido = classeDominioDiscreto.getSimpleName();
				Object[] listaSel;

				if (classeDominioDiscreto.isEnum()) {
					listaSel = classeDominioDiscreto.getEnumConstants();
				} else {
					Object obj = classeDominioDiscreto.newInstance();
					listaSel = (Object[]) reflectionUtil.executeMethod(obj, "getListaSel", new Object[] {}, new Class[] {});
				}
				event.getServletContext().setAttribute("listaSel" + nomeResumido, listaSel);
				event.getServletContext().setAttribute(nomeResumido, listaSel);
			}

		} catch (Exception e) {
			erroMsg(event.getServletContext(), "Problemas ao tentar carregar classes de dominio discreto: ", e);
		}
	}


	/**
	 * jCompany. Realiza procedimentos no momento de finalização da aplicação no Application Server
	 */
	public void contextDestroyed(ServletContextEvent event) {

		try {
			cdEmiteMonitoriaSaida(event);

			cdAoEncerrarAplicacao(event);

		} catch (Exception e) {
			System.out.println("############# jCompany: Erro no processamento de destruicao do contexto" + e);
			e.printStackTrace();
		}
	}

	/**
	 * Emite log de indicação de saída (encerramento) do serviço
	 */
	protected void cdEmiteMonitoriaSaida(ServletContextEvent event) {

		if (jMonitorApi !=null)
			jMonitorApi.log("ciclodevida",IPlcJMonitorApi.NIVEL_LOG.FATAL, "Aplicação fora do ar.");
		
	}

	/**
	 * jCompany. 
	 * Deve ser implementado no AppServletContextListener por todas as aplicações jCompany para padronizar procedimentos que ocorrem no momento da finalização de todas as aplicações
	 */
	abstract public void cdAoEncerrarAplicacao(ServletContextEvent event) ;

	/**
	 * jCompany 3.0. 
	 * Deve ser implementado no AppServletContextListener por todas as aplicações jCompany para padronizar procedimentos que ocorrem no momento da inicialização de todas as aplicações.
	 * Os parametros em caching podem ser obtidos com PlcCacheUtil.getInstance().recuperaObjeto(<parametro>);
	 */
	abstract public void ciAoInicializarAplicacao(ServletContextEvent event) ;


	/**
	 * jCompany 5.1 Disponibiliza restrições de personalização globais (peles, layouts, form e botoes destaque)
	 */
	protected void ciCarregaOpcoesPersonalizacao(ServletContextEvent event) {

		log.debug( "########## Entrou em ciCarregaOpcoesPersonalizacaoGlobais");

		try {
			PlcConfigLookAndFeel configAparencia = configUtil.getConfigApplication().lookAndFeel();

			// Registra peles
			String[] peles = configAparencia.customizedSkin();
			List<String> peleLista = new ArrayList<String>();
			CollectionUtils.addAll(peleLista, peles);
			event.getServletContext().setAttribute(PlcConstants.PERSONALIZA.PELE_LISTA, peleLista);

			// Registra leiautes
			String[] layouts = configAparencia.customizedLayout();
			List<String> layoutLista = new ArrayList<String>();
			CollectionUtils.addAll(layoutLista, layouts);
			event.getServletContext().setAttribute(PlcConstants.PERSONALIZA.LAYOUT_LISTA, layoutLista);

		} catch (Exception e) {
			erroMsg(event.getServletContext(), " - Erro ao registrar servico de  profile ", e);
		}

	}


	/**
	 * jCompany. Carrega classes declaradas no web.xml e nas anotações "inclusive modulos" como classes de lookup
	 * em escopo de aplicação, no caching, para otimização de acesso e uso
	 * de memória.
	 */
	public void ciCarregaClassesLookup(ServletContextEvent event) {
		
		ServletContext ctx = event.getServletContext();
		
		try {
			
			cacheUtil.putObject(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO, ctx.getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO)); 		

			PlcConfigApplication configControleApp = configUtil.getConfigApplication().application();

			/** 
			 * Completa as classes de lookup configuradas no web.xml com as classes de lookup configuradas na Aplicação e nos Módulos 
			 * 
			 * */

			Class[] classesLookup = configUtil.getConfigApplication().application().classesLookup(); 
			String[] classesLookupOrderby = configUtil.getConfigApplication().application().classesLookupOrderBy(); 

			// Verificando se existe módulos e recuperando suas classes de Lookup
			if (configControleApp != null) {
				PlcConfigApplication configControleModulo;
				String siglaModulo;
				PlcConfigModule[] modulos = configControleApp.modules();
				if (modulos != null) {
					for (PlcConfigModule modulo : modulos) {
						siglaModulo = "." + modulo.acronym();
						configControleModulo = configUtil.getConfigModule(siglaModulo).application();
						if(configControleModulo != null) {
							classesLookup = (Class[]) ArrayUtils.addAll(classesLookup, configControleModulo.classesLookup());
							classesLookupOrderby = (String[]) ArrayUtils.addAll(classesLookupOrderby, configControleModulo.classesLookupOrderBy());
						}
					}
				}
			}

			// O trabalho é todo realizado aqui.
			if(antesCarregaClasseLookup(classesLookup, classesLookupOrderby)) {
				classeLookupUtil.retrieveClassesLookupFromPersistenceToCache(classesLookup, classesLookupOrderby);
				aposCarregaClasseLookup(classesLookup, classesLookupOrderby);
			}

		} catch (Exception e) {
			erroMsg(ctx, "Problemas ao tentar carregar as classes de lookup: ", e);
		}
	}

	/**
	 * jCompany 1.5.1. 
	 * Método para recuperação específica de classes de lookup. 
	 * Deve-se recuperar a lista e retoná-la, para sobrepor o método padrão do jCompany.
	 * @param classesLookup
	 * @param classesLookupOrderby
	 * @return
	 */
	protected Boolean antesCarregaClasseLookup(Class[] classesLookup, String[] classesLookupOrderby) {

		return true;
	}

	/**
	 * jCompany 1.5.1. 
	 * Método para ajustes em objetos de caching, apos a recuperação.
	 * @param classesLookup
	 * @param classesLookupOrderby
	 */
	protected void aposCarregaClasseLookup(Class[] classesLookup, String[] classesLookupOrderby) {

	}


	@Override
	public void requestInitialized(ServletRequestEvent event) {

	}

	@Override
	public void requestDestroyed(ServletRequestEvent event) {

	}
	
    protected void ciConfigureServletUtil(ServletContextEvent event) {
    	servletContextProducer.setServletContext(event.getServletContext());
	}
}
