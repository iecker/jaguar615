/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.lifecycle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.util.ExternalContextUtils;
import org.apache.myfaces.trinidad.util.LabeledFacesMessage;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.controller.adm.PlcUserOnlineUtil;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcBaseUserProfileUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcCookieUtil;
import com.powerlogic.jcompany.controller.util.PlcExceptionHandlerUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

/**
 * @since jcompany 5 Processador inicial de requisições
 */
public class PlcPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(PlcPhaseListener.class.getCanonicalName());

	private static final String PLC_REQ_ATT_EXECUTE = "plcChamouExecute";

	private static final String PLC_PASSOU_FASE = "plcPassouFase";

	protected int faseRegistraRequisicao = 1; // funcional para JSF 2.1, para rodar no JSF 2.0 trocar valor para 2.

	protected PlcUserOnlineUtil usuOnlineUtil;
	protected PlcContextUtil contextUtil;
	protected PlcURLUtil urlUtil;
	protected PlcConfigUtil configUtil;
	protected PlcConfigMBCollaboration configActionColaboracao;
	protected PlcMsgUtil msgUtil;
	protected PlcCookieUtil cookieUtil;
	protected PlcBaseUserProfileUtil plcProfile;
	protected PlcViewJsfUtil visaoJsfUtil;
	protected PlcExceptionHandlerUtil exceptionHandlerUtil;
	protected PlcELUtil elUtil;
		
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

	/**
	 * Devolve a instancia do backing-bean corrente
	 * 
	 * 
	 */
	private PlcBaseMB getAction() {
    	
		try {
			String backingBean = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.PLC_ACTION_KEY);
			Object action = PlcCDIUtil.getInstance().getInstanceByType(Object.class, new PlcNamedLiteral(backingBean));
			if (action != null && action instanceof PlcBaseMB) {
				return (PlcBaseMB) action;
			}
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcPhaseListener", "getAction", e, log, "");
		}
		return null;
	}

	
	protected void inicializaVariaveis() {
		// Foi retirado do construtor por não funcionar no Tomcat 6.0.14.
		usuOnlineUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcUserOnlineUtil.class, QPlcDefaultLiteral.INSTANCE);
		contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);    	
    	urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);
    	configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);    	
    	configActionColaboracao = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigMBCollaboration.class, QPlcDefaultLiteral.INSTANCE);
    	msgUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgUtil.class, QPlcDefaultLiteral.INSTANCE);
		cookieUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcCookieUtil.class, QPlcDefaultLiteral.INSTANCE);
		plcProfile = PlcCDIUtil.getInstance().getInstanceByType(PlcBaseUserProfileUtil.class, QPlcDefaultLiteral.INSTANCE);
		visaoJsfUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcViewJsfUtil.class, QPlcDefaultLiteral.INSTANCE);
		exceptionHandlerUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcExceptionHandlerUtil.class, QPlcDefaultLiteral.INSTANCE);
		elUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (contextUtil.getApplicationContext().getServerInfo().contains("IBM WebSphere Application Server")) { // WEBSPHERE
			faseRegistraRequisicao = 2; 
		} else {
			faseRegistraRequisicao = 1;
		}
		
	}
	
	/**
	 * @since jCompany 5. Método orquestrador para eventos que ocorrem antes de
	 *        cada fase.
	 */
	@Override
	public void beforePhase(PhaseEvent event) {
		
    	try {
    		
    		if (contextUtil==null)
    			inicializaVariaveis();
    		
			// Código adicionado para controlar o erro de view expirada quando
			// ocorre expiração na sessão de usuário.
			// Esse código garante o controle quando a requisição é parcial.
			// Para post sem parcial, há um código adicionado no
			// PlcMasterFilter.
    		// FIXME: handle exception
			try {
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				if (!ExternalContextUtils.isRequestedSessionIdValid(externalContext)) {
					if (!externalContext.getRequestPathInfo().equals("/fcls/public/loginPlc.xhtml")) {
						externalContext.redirect(externalContext.getRequestContextPath());
					}
				}
			} catch (Exception e) {}
			// Somente entra se nao for requisicao para imagens e recursos
			// (requisicao HTTP da página principal)
			if (urlUtil.isMainRequest(contextUtil.getRequest())) {
				// Registra modo de teste na sessao
				if (contextUtil.getRequestParameter(PlcConstants.QA.MODO_TESTE) != null && 
						contextUtil.getRequest().getSession().getServletContext().getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO) != null &&
						!contextUtil.getRequest().getSession().getServletContext().getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO).equalsIgnoreCase("P")) {
					contextUtil.setSessionAttribute(PlcConstants.QA.MODO_TESTE, "S");
				}

				// Somente força uma primeira interpretação da URL para registro
				// de caching de metadados delegados.
				// Nao deve ser retirado
				String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());

				
				// Para o JSF 2.1.x - esse if deve entrar na Fase 1
				// Para o JSF 2.0.x - esse if deve entrar na Fase 2
				// Por default o jCompany roda JSF 2.1, registrando variávies na Fase 1  
				
				// Somente para WEBSPHERE
				if (faseRegistraRequisicao==2 && event.getPhaseId().getOrdinal()==1)
					//configActionColaboracao.registraVariaveisCaminhoVisao();
					configActionColaboracao.registraRequisicao();

				if (event.getPhaseId().getOrdinal() >= faseRegistraRequisicao && !Boolean.TRUE.equals(contextUtil.getRequestAttribute(PLC_PASSOU_FASE))) {
					contextUtil.setRequestAttribute(PLC_PASSOU_FASE, Boolean.TRUE);
					// O "segunda fase" tem que vir antes do switch, pois deverá
					// ser executado antes do "antes" da segunda fase.
					// O registraRequisicao tem que ser executado a partir da
					// segunda fase, que pode variar de acordo com o fluxo.
					// Portanto, tem que ser antes da primeira fase executada
					// depois da Restore View (não pode ser no afterPhase).
					trataCookies();
					configActionColaboracao.registraRequisicao();
					// AJAX
					registraAjax();
					conversacaoEncerra();
					
					resolveFiltroVertival();				
					
				}

				if (PhaseId.RESTORE_VIEW.equals(event.getPhaseId())) {
					restoreViewBefore();

				} else if (PhaseId.INVOKE_APPLICATION.equals(event.getPhaseId())) {
					invokeApplicationBefore(url);
				} else if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
						renderResponseBefore(url);
				}
			}
		} catch (Exception e) {
			// Chama tratamento generico pois é "fim-de-linha"
			try {

				exceptionHandlerUtil.handleErrorsController(e);
				log.error( e.getLocalizedMessage());
				
			} catch (Exception eFatal) {
				log.fatal( "Erro ao tentar criar servico de tratamento de excecao " + eFatal, eFatal);
			}
		}
	}

	protected void resolveFiltroVertival() {
		
		List<String> listaFiltros = new ArrayList<String>();
		
		PlcBaseUserProfileVO plcUsuVO = PlcCDIUtil.getInstance().getInstanceByType(PlcBaseUserProfileVO.class, QPlcDefaultLiteral.INSTANCE);
		
		List<String> filtros = plcUsuVO.getFilters();
		
		for (String value : filtros) {
			if (value.contains("#{") && value.contains("}")) {
				String valueC = StringUtils.substringBetween(value, "#{", "}");
				valueC = value.substring(0, value.indexOf("#"))+"#"+elUtil.createValueExpression("#{"+valueC+"}", String.class).getValue(FacesContext.getCurrentInstance().getELContext()).toString();				
				listaFiltros.add(valueC);				
			}else{
				listaFiltros.add(value);
			}			
		}
		
		plcUsuVO.setFilters(listaFiltros);
		
	}

	protected void restoreViewBefore() {
		// Registra perfil de usuario se acabou de se autenticar
		
		// TODO - avaliar a expressão contendo o POJO da entidade, para que seja criado a entidade no contexto do CDI.
		// elUtil.evaluateExpressionGet("#{pacoteLista}", Object.class);
		
		if (contextUtil.getRequest().getUserPrincipal() != null && contextUtil.getSessionAttribute(PlcConstants.USER_INFO_KEY) == null) {
			registryUserProfile();
		}

		processaUsuariosOnline();

		registraModificacaoSessao();
	}

	protected void renderResponseBefore(String url) {
		transformaMensagensErro();
		if (configUtil.getConfigCollaboration(url) != null && configUtil.getConfigAggregation(url) != null) {
			
			if (contextUtil.getRequest().isRequestedSessionIdValid()) {
				getAction().handleButtonsAccordingFormPattern();
			}
			
			actionExecutaMetodos();
		}
	}

	protected void invokeApplicationBefore(String url) {
		if (configUtil.getConfigCollaboration(url) != null && configUtil.getConfigCollaboration(url) != null) {
			actionExecutaMetodos();
		}
	}


	@Override
	public void afterPhase(PhaseEvent arg0) {
		
	}

	/**
	 * Chama o método execute, e outros, se necessário da Action atual, caso
	 * ainda não tenha sido chamado para essa requisição. Esses métodos devem
	 * ser executados antes da chamada da invocação de métodos de ação, porém
	 * depois da validação. Ou seja, será chamado antes do
	 * {@link PhaseId#INVOKE_APPLICATION}, ou {@link PhaseId#RENDER_RESPONSE},
	 * se não houver o invoke application.
	 */
	protected void actionExecutaMetodos() {

		Map<String, Object> requestMap = contextUtil.getRequestMap();

		if (!Boolean.TRUE.equals(requestMap.get(PLC_REQ_ATT_EXECUTE))) {
			try {
				getAction().executeBefore();
			} catch (Exception e) {
				// Chama tratamento generico pois é "fim-de-linha"
				try {
					exceptionHandlerUtil.handleErrorsController(e);
				} catch (Exception eFatal) {
					log.fatal( "Erro ao tentar criar servico de tratamento de excecao " + eFatal);
					eFatal.printStackTrace();
				}
			}
			requestMap.put(PLC_REQ_ATT_EXECUTE, Boolean.TRUE);
		}
	}

	/**
	 * Utilitário que armazena informações de acesso do usuário (quem esta
	 * on-line?)
	 * 
	 * @param configUrl
	 */
	protected void processaUsuariosOnline()  {
		usuOnlineUtil.addInfoRequest(contextUtil.getRequest());
	}

	/**
	 * Chama utilitário para modificações dinamicas via URL na sessão, de
	 * elementos como indicadores de modo de teste, layouts corrente, etc.
	 * 
	 * @param configUrl
	 * 
	 */
	protected void registraModificacaoSessao()  {
		// Deve ser incluido no "RESTORE" porque pega parametros de get da URL
		// para incluir na sessao.
		// Registra personalizações em escopo de sessão em conformidade com
		// parametros de request especiais.
		visaoJsfUtil.saveOnSession(contextUtil.getRequest());
	}

	/**
	 * Manipula a requisição para o uso de Ajax. Atualmente utiliza o PPR do
	 * Trinidad.
	 * 
	 * @param configGlobal
	 * @param configAcaoControleCorrente
	 * 
	 */
	protected void registraAjax()  {

		HttpServletRequest request = contextUtil.getRequest();

		PlcConfigCollaborationPOJO configGrupoControle =  configUtil.getConfigCollaboration(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));		
		
		// Não usa Ajax em modo de teste automatizado ou para anexar arquivo
		// (multi-part)
		// Ou no caso do usuário ter escolhido
		// boolean modoTeste =
		// request.getSession().getAttribute(PlcConstantes.QA.MODO_TESTE)!=null;
		boolean fileAttach = request.getAttribute(PlcConstants.ARQUIVO.IND_ARQ_ANEXADO) != null;
		boolean multipleFileAttach = request.getAttribute(PlcJsfConstantes.PLC_NOME_COLECAO_MULTIPLOS_ARQUIVOS) != null;

		// retirado do inicio da linha abaixo -> !modoTeste &&
		// TODO - Analisar necessidade de retirada do atributo ajaxUsa e ajaxAutomatico
		boolean ajaxUsa = (!fileAttach || multipleFileAttach) && configGrupoControle != null;
		// retirado do inicio da linha abaixo -> !modoTeste &&
		boolean ajaxAutomatico = (!fileAttach || multipleFileAttach) && ajaxUsa && configGrupoControle != null ;

		request.setAttribute(PlcConstants.LOGICAPADRAO.GERAL.AJAX_USA, ajaxUsa);
		request.setAttribute(PlcConstants.LOGICAPADRAO.GERAL.AJAX_AUTOMATICO, ajaxAutomatico);

		if (ajaxUsa) {
			RequestContext reqContext = RequestContext.getCurrentInstance();
			if (reqContext != null) {

				FacesContext facesContext = FacesContext.getCurrentInstance();
				if (reqContext.isPartialRequest(facesContext)) {
					// Se não for Parcial Ajax de Vinculado, ou Parcial Ajax
					// Específico, registra o panel de Ajax padrão!
					if (!isPartialAjaxVinculado(reqContext, facesContext) && !registraAjaxEspecifico(reqContext, facesContext)) {
						if(facesContext.getViewRoot() != null) {
							
							UIComponent target = facesContext.getViewRoot().findComponent("corpo:ajaxPanel");
							if (target != null) {
								reqContext.addPartialTarget(target);
							}
							
							target = facesContext.getViewRoot().findComponent("modal:ajaxPanel");
							if (target != null) {
								reqContext.addPartialTarget(target);
							}
							
							target = facesContext.getViewRoot().findComponent("corpo:formulario");
							if (target != null) {
								reqContext.addPartialTarget(target);
							}
							
							target = facesContext.getViewRoot().findComponent("modal:formulario");
							if (target != null) {
								reqContext.addPartialTarget(target);
							}
							
							target = facesContext.getViewRoot().findComponent("corpo:ajaxlogQA");
							if (target != null) {
								reqContext.addPartialTarget(target);
							}
						}  

					}
				}
			}
		}
	}

	protected boolean registraAjaxEspecifico(RequestContext requestContext, FacesContext facesContext) {

		boolean ajaxEspecifico = false;

		String source = contextUtil.getRequest().getParameter("source");

		UIComponent target = null;

		if ("corpo:formulario:botaoAcaoGravar".equals(source) || "corpo:formulario:botaoAcaoPesquisar".equals(source)) {
			ajaxEspecifico = adicionaPatialTarget(requestContext, facesContext, true, "corpo:formulario:ajaxMensagem", target);
			ajaxEspecifico = adicionaPatialTarget(requestContext, facesContext, true, "corpo:formulario:ajaxAcoes", target);
			ajaxEspecifico = adicionaPatialTarget(requestContext, facesContext, true, "corpo:ajaxlogQA", target);
			ajaxEspecifico = adicionaPatialTarget(requestContext, facesContext, true, "corpo:ajaxFormulario", target);
		}

		if ("corpo:formulario:botaoAcaoAnexarArquivo".equals(source)) {
			ajaxEspecifico = adicionaPatialTarget(requestContext, facesContext, true, "corpo:formulario:multiplosArquivosAnexados", target);
		}
		return ajaxEspecifico;
	}

	protected boolean adicionaPatialTarget(RequestContext requestContext, FacesContext facesContext, boolean ajaxEspecifico, String componente, UIComponent target) {
		
		boolean ehAjaxEspecicico = false;
		
		if(facesContext.getViewRoot() != null) {
			target = facesContext.getViewRoot().findComponent(componente);
		}
		
		if (target != null) {
			ehAjaxEspecicico = ajaxEspecifico;
			requestContext.addPartialTarget(target);
		}
		return ehAjaxEspecicico;
	}

	protected boolean isPartialAjaxVinculado(RequestContext requestContext, FacesContext facesContext) {

		String partialVinculado = contextUtil.getRequest().getParameter(PlcJsfConstantes.PLC_PARTIAL_VINCULADO);

		return StringUtils.isNotEmpty(partialVinculado);
	}

	/**
	 * Verifica, na URL, se há indicador para encerrar conversação e
	 * encerra, se for o caso
	 * 
	 * @param configUrl
	 *            Configurações de metadados para a colaboração corrente
	 */
	protected void conversacaoEncerra() {

		String conversacaoList = contextUtil.getRequestParameter(PlcConstants.PlcJsfConstantes.PLC_CONVERSACAO_ENCERRA_PARAM);

		if (StringUtils.isNotBlank(conversacaoList)) {
			Conversation conversation = PlcCDIUtil.getInstance().getInstanceByType(Conversation.class);
			if (conversation != null && !conversation.isTransient()) {
				conversation.end();
			}
		}
	}

	/**
	 * Chama o @{link PlcCookieJsfUtil} para fazer o tratamento de cookies do
	 * sistema.
	 * 
	 * @throws ServletException
	 */
	protected void trataCookies() throws ServletException {
		//FIXME - Fazer somente na primeira entrada do usuário.
		cookieUtil.checkCookies(contextUtil.getRequest(), contextUtil.getResponse());
	}

	/**
	 * Transforma mensagens de erro JSF para PlcMensagem, para serem exibidas
	 * pelo jCompany.
	 * 
	 * @param configUrl
	 * 
	 */
	protected void transformaMensagensErro()  {
		
		HttpServletRequest request = contextUtil.getRequest();
		Iterator<FacesMessage> iter = FacesContext.getCurrentInstance().getMessages();
		while (iter.hasNext()) {
			FacesMessage message = iter.next();
			String msg = message.getSummary();

			if (message instanceof LabeledFacesMessage) {
				String label = (String) ((LabeledFacesMessage) message).getLabel();
				// se tiver ###propTituloAutomaticoPlc### na msg substitui
				// para o label.
				if (msg != null && msg.contains(PlcConstants.MSG.PROP_TITULO_AUTOMATICO) && StringUtils.isNotEmpty(label)) {
					msg = msg.replaceAll(PlcConstants.MSG.PROP_TITULO_AUTOMATICO, label);
					msgUtil.msg(msg, PlcMessage.Cor.msgVermelhoPlc.toString());
				} else{
					if (StringUtils.isNotEmpty(label)){
						msg = label + " " + msg;
					}else if (StringUtils.isEmpty(label) && StringUtils.isEmpty(msg)){
					 	msg = "Erro desconhecido. A mensagem não conseguiu ser obtida.";
					}
						
					msgUtil.msg(msg, PlcMessage.Cor.msgVermelhoPlc.toString());
				}
			} else {
				msgUtil.msg(msg, PlcMessage.Cor.msgVermelhoPlc.toString());
			}
			request.setAttribute(PlcJsfConstantes.VALIDACAO.FALHA_VALIDACAO, "S");
		}
	}

	/**
	 * Registra dados do perfil do usuário logado.
	 * 
	 * @param configUrl
	 */
	protected void registryUserProfile() {		
		plcProfile.registryUserProfile(contextUtil.getRequest(), contextUtil.getResponse());
	}
}
