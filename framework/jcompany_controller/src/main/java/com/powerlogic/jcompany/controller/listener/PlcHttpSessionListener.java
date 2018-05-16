/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ContextNotActiveException;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.integration.IPlcJMonitorApi;
import com.powerlogic.jcompany.commons.integration.IPlcJMonitorApi.CICLO_VIDA;
import com.powerlogic.jcompany.commons.util.PlcStringUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.adm.PlcUserOnlineUtil;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcLocaleUtil;
import com.powerlogic.jcompany.domain.type.PlcYesNo;

/**
 * jCompany. Abstrata. Listener. Classe para lógicas de inicialização e término de cada sessão do Application Server.
 */
abstract public class PlcHttpSessionListener implements HttpSessionListener {
	
	protected static final Logger log = Logger.getLogger(HttpSessionListener.class.getCanonicalName());
	
	// Como o CDI não atua em classes instanciadas pelo Conteiner, neste caso instancia no inicio do processamento
	protected IPlcJMonitorApi jMonitorApi;
	protected PlcConfigUtil configUtil = null;
	protected PlcLocaleUtil localeUtil = null;
	protected PlcUserOnlineUtil usuOnlineUtil = null;
    
	public PlcHttpSessionListener() {
	}
   	
	/**
	 * jCompany. Realiza procedimentos no momento de inicialização da sessão
	 */
	public void sessionCreated (HttpSessionEvent event) {
		
		log.debug( "########## Inicializando Sessao");
		
		try {
		
			ciInicializaVariaveis();
			
			PlcCacheSessionVO plcSessao = new PlcCacheSessionVO();

			// Coloca a pele inicial.
			plcSessao.setPele(configUtil.getConfigApplication().lookAndFeel().skin());
			
			// Coloca parametros do feedback. Se estiver em anotação
			if (configUtil.getConfigApplication().jMonitor().feedbackAddress()!=null){
				plcSessao.setFeedbackUsa(configUtil.getConfigApplication().jMonitor().useFeedback());
				plcSessao.setFeedbackEndereco(configUtil.getConfigApplication().jMonitor().feedbackAddress());
			}else{
				plcSessao.setFeedbackUsa(false);
				plcSessao.setFeedbackEndereco("");
			}
			
			// Coloca Layout inicial.
			//plcSessao.setLayout(configUtil.getConfigApplication().lookAndFeel().layout());
			registraLayout(plcSessao, event, configUtil.getConfigApplication().lookAndFeel().layout());
		
			// Coloca modo exibicao texto na barra de acao na sessao, para personalização
			if (configUtil.getConfigApplication().lookAndFeel().formLookAndFeel()!=null) 
			    plcSessao.setFormAcaoExibeTexto(configUtil.getConfigApplication().lookAndFeel().formLookAndFeel().toString());

			event.getSession().setAttribute(PlcConstants.SESSION_CACHE_KEY,plcSessao);

			// Garante o idioma
			registraIdiomaDefault(event);		
			
			log.debug( "carregou objeto de caching para sessao");
			
			registraFiltroAnonimos(event,plcSessao);

			aoInicializarSessao(event,plcSessao);
			
			usuOnlineUtil.addSession(event);
			
			configuraTimeoutConversacao(event);
			
		} catch (ContextNotActiveException e) {
			// Problema de escopo do OWB
			log.fatal( "Problema de escopo do OWB - não contabilizará usuários online");
			
		} catch (Exception ex) {
			
			log.fatal( "erro geral ao inicializar sessao"+ex,ex);
		}
		
	}

	protected void configuraTimeoutConversacao(HttpSessionEvent event) {
		try {
			Class c = Class.forName("org.jboss.weld.context.http.HttpConversationContext");
			Object o = PlcCDIUtil.getInstance().getInstanceByType(c);
     		c.getMethod("setDefaultTimeout", long.class).invoke(o, event.getSession().getMaxInactiveInterval()*1000);
		} catch (Exception e) {
			log.fatal( "Não foi possível configurar timeout da conversação "+e,e);
		}	
	}
	
	protected void ciInicializaVariaveis() { 
		
		if (jMonitorApi==null){
			jMonitorApi = PlcCDIUtil.getInstance().getInstanceByType(IPlcJMonitorApi.class);
		}
		if (configUtil==null) {
			configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
		}
		if (localeUtil==null) {
			localeUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcLocaleUtil.class, QPlcDefaultLiteral.INSTANCE);
		}
		if (usuOnlineUtil==null){
			usuOnlineUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcUserOnlineUtil.class, QPlcDefaultLiteral.INSTANCE);
		}
	}

	protected void registraLayout(PlcCacheSessionVO plcSessao, HttpSessionEvent event, String layoutPlc) {

		final String reduzidoSuffix = "_reduzido";
		final String dinamicoSuffix = "_dinamico";

		if (StringUtils.isNotEmpty(layoutPlc)) {
			
			plcSessao.setLayout(layoutPlc);
			plcSessao.setIndLayoutReduzido(PlcYesNo.N.name());
			
			if (StringUtils.contains(layoutPlc, reduzidoSuffix)) {
				plcSessao.setLayout(StringUtils.replace(layoutPlc, reduzidoSuffix, StringUtils.EMPTY));
				plcSessao.setIndLayoutReduzido(PlcYesNo.S.name());
			}
			
			if (StringUtils.contains(layoutPlc, dinamicoSuffix)) {
				plcSessao.setLayout(StringUtils.replace(layoutPlc, dinamicoSuffix, "ex"));
			}
			
			event.getSession().setAttribute("layoutPlc", plcSessao.getLayout());
			event.getSession().setAttribute("layoutReduzidoPlc", plcSessao.getIndLayoutReduzido());
		}
		
	}
	
	/**
     * Registra idioma default ou único, conforme configurado em metadados, na sessão
     * TODO - Verificar em JSF 2.0 se há como simplificar o I18n com os dois recursos (manter o idioma ultimo do usuario e idioma unico)
     */
    protected void registraIdiomaDefault(HttpSessionEvent event) {
        
    	log.debug( "########## Entrou em registraIdiomaDefault");
  		
		try {
			localeUtil.availableDefaultLocale(event.getSession());
		} catch (Exception e) {
			log.error( "Erro ao tentar incluir bundle de mensagens default provavelmente porque o arquivo ApplicationResources.properties (sem sufico _pt_BR!) nao foi encontrado!" + e);
		}
				
    }

    /**
	 * jCompany. Realiza procedimentos no momento de destruição de cada sessão
	 */
	public void sessionDestroyed (HttpSessionEvent event) {
			
		try {
			log.info( "########### Finalizando Sessao");
			 
			jMonitorApi.logSessao(event.getSession().getId(),CICLO_VIDA.FIM);
  		
			PlcUserOnlineUtil usuOnlineUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcUserOnlineUtil.class, QPlcDefaultLiteral.INSTANCE);
        
			usuOnlineUtil.removeSession(event);
		
			aoEncerrarSessao(event);
			
		} catch (Exception e) {
			log.warn( "################# Erro sessionDestroyed: "+e.getMessage());
		}
	
	}
	
	/**
	 * jCompany. Métodos para implementação no descendente de regras que 
	 * ocorrem ao final de cada sessão. 
	 * Toda aplicação jCompany deve possuir um descendente que implemente estes
	 * métodos, para padronização deste tipo de tratamento.
	 */
	abstract public void aoEncerrarSessao (HttpSessionEvent event);
	
	/**
	 * jCompany. Métodos para implementação no descendente de regras que 
	 * ocorrem no início de cada sessão. 
	 * Toda aplicação jCompany deve possuir um descendente que implemente estes
	 * métodos, para padronização deste tipo de tratamento.
	 */
	abstract public void aoInicializarSessao (HttpSessionEvent event,PlcCacheSessionVO plcSessao);

	/**
	 * jCompany. Registra filtro de segurança vertical quando o usuário é anônimo e não passa
	 * pela lógica de perfil (profiling) de usuários da autenticação.
	 *  
	 */
	protected void registraFiltroAnonimos(HttpSessionEvent event, PlcCacheSessionVO plcSessao)  {
	
		log.debug( "################# Entrou em registraFiltroAnonimos");

		PlcStringUtil stringUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcStringUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (configUtil.getConfigApplication().behavior().anonymousFilter() != null) {
			
			String filtros = configUtil.getConfigApplication().behavior().anonymousFilter();

			if (!filtros.equals("") && !filtros.equals("#")) {
				
				List l = stringUtil.splitSubstringList(filtros);

				Iterator i = l.iterator();
				String classeFiltro = "";
				String classe="";
				String filtro="";
				Map<String,String> m = new HashMap<String,String>();

				while (i.hasNext()) {

					classeFiltro = (String) i.next();
					classe = classeFiltro.substring(0,classeFiltro.indexOf("#"));
					filtro = classeFiltro.substring(classeFiltro.indexOf("#")+1);
					if (log.isDebugEnabled())
						log.debug( "classe="+classe+" filtro="+filtro);

					m.put(classe,filtro);	
				}

				if (m.size()>0) {
					plcSessao.setSegurancaVerticalAnonimo(m);
					log.debug( "Registrou filtros ok");
				}
			}
		}
	}
}

