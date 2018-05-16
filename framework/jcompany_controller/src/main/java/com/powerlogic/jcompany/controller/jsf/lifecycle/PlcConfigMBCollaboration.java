/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.lifecycle;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.integration.IPlcJSecurityApi;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.aggregation.PlcConfigPagedDetail;
import com.powerlogic.jcompany.config.aggregation.PlcConfigPattern;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigModule;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcRequestControl;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcExceptionHandlerUtil;
import com.powerlogic.jcompany.controller.util.PlcNaturalKeyUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;

/**
 * Classe Temporaria que tem o comportamento de pre inicialização da Action, retirada do PhaseListener!
 */
@SPlcUtil
@QPlcDefault
public class PlcConfigMBCollaboration {

	/**
	 * objeto para saida centralizada de LOG.
	 */
	@Inject
	protected transient Logger log;

	@Inject @QPlcDefault
	PlcContextUtil contextUtil;

	@Inject @QPlcDefault
	PlcURLUtil urlUtil;

	@Inject @QPlcDefault
	PlcConfigUtil configUtil;

	@Inject @QPlcDefault
	PlcNaturalKeyUtil chaveNaturalUtil;	

	@Inject @QPlcDefault
	PlcMetamodelUtil metamodelUtil;

	@Inject @QPlcDefault
	PlcExceptionHandlerUtil exceptionHandlerUtil;

	@Inject @QPlcDefault
	PlcViewJsfUtil visaoJsfUtil;

	@Inject @QPlcDefault
	PlcELUtil elUtil;

	@Inject @QPlcDefault
	PlcReflectionUtil reflectionUtil;
	
	@Inject 
	IPlcJSecurityApi jSecurityApi;	
	
	/**
	 * Devolve a instancia do backing-bean corrente
	 *  
	 */
	protected PlcBaseMB getMB() {
		String backingBean  = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.PLC_ACTION_KEY);
		return PlcCDIUtil.getInstance().getInstanceByType(PlcBaseMB.class, new PlcNamedLiteral(backingBean));
	}

	/**
	 * Realiza processamentos básicos mínimos para a requisição.
	 * TODO - Refatorar. Complexidade Ciclomatica atual = 69 NCSS(Non Commenting Source Statements)  = 105
	 * Registra o nome da Action, 
	 */
	protected void registraRequisicao()  {

		if (registraRequisicaoAntes()) {
			// Disponibiliza identificador da classe de controle da colaboração
			// IMPORTANTE: A presença deste atributo no request é também indicador do uso de JSF em contraposição a um controlador Struts.
			contextUtil.setRequestAttribute(PlcConstants.PlcJsfConstantes.PLC_ACTION_KEY, PlcConstants.PlcJsfConstantes.PLC_MB);

			// Desvenda nome do action a partir de URL padrao
			String nomeAction = getNomeColaboracao();

			// Disponibiliza chave da colaboração (url) na requisicao para evitar reprocessamentos da URL em logicas subsequentes.
			contextUtil.setRequestAttribute(PlcConstants.PlcJsfConstantes.URL_COM_BARRA, nomeAction);

			// Disponibiliza prefixo da chave da colaboração (url) na requisicao
			String nomeActionAux = getModuloCorrente(nomeAction, true);
			String aliasCasoUso = ObjectUtils.toString(urlUtil.getAliasUseCase(nomeActionAux));
			String sufixoCasoUso = ObjectUtils.toString(urlUtil.getSuffixUseCase(nomeActionAux));


			contextUtil.setRequestAttribute(PlcConstants.PlcJsfConstantes.PLC_PREFIXO_ACTION, aliasCasoUso);
			contextUtil.setRequestAttribute(PlcConstants.PlcJsfConstantes.PLC_SUFIXO_ACTION, sufixoCasoUso);

			PlcConfigAggregationPOJO configAgregacao =  configUtil.getConfigAggregation(nomeAction);
			PlcConfigCollaborationPOJO configColaboracao =  configUtil.getConfigCollaboration(nomeAction);

			if (configAgregacao != null) {

				PlcConfigDetail[] detalhes = configAgregacao.details();

				
				if (detalhes != null){
					for (PlcConfigDetail plcConfigDetalhe : detalhes) {

						// Pegando navegador
						PlcConfigPagedDetail navegador = plcConfigDetalhe.navigation();
						if (navegador != null){
							int numPorPagina = navegador.numberByPage();
							if (numPorPagina > 0){
								contextUtil.getRequest().setAttribute(PlcJsfConstantes.PLC_NOME_COLECAO_DETALHE_POSSUI_PAGINACAO + plcConfigDetalhe.collectionName() , "S");
								contextUtil.getRequest().setAttribute(PlcJsfConstantes.DETALHE_PAGINADO, true);
							} 
						}
						
						// TODO - 6.0 - Reimplementar pesquisa em detalhe com argumentos no novo padrão de QBE
					}
				}
				
				Field [] camposAnotados = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(configAgregacao.entity(), PlcFileAttach.class);
				
				for (Field field : camposAnotados) {
					PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);
					
					contextUtil.getRequest().setAttribute("IND_ARQ_ANEXADO", "S");

					if(fileAttach.multiple() && !fileAttach.image()) {
						//contextUtil.getRequest().setAttribute(PlcJsfConstantes.PLC_NOME_COLECAO_MULTIPLOS_ARQUIVOS , fileAttach.collectionName());
						contextUtil.getRequest().setAttribute(PlcJsfConstantes.PLC_NOME_COLECAO_MULTIPLOS_ARQUIVOS , field.getName());
					}
				}

				PlcBaseMB action = null;
				try {
					action = getMB();
				} catch (Exception e) {}

				// Verifica lógica corrente
				FormPattern formPattern = configAgregacao.pattern().formPattern();
				if (formPattern==FormPattern.Rel || formPattern==FormPattern.Sel || formPattern==FormPattern.Ctb || formPattern==FormPattern.Con || formPattern==FormPattern.Ctl || formPattern==FormPattern.Smd){
					//contextUtil.setRequestAttribute(PlcConstantes.PlcJsfConstantes.PLC_MANAGED_BEAN_KEY, PlcConstantes.PlcJsfConstantes.PLC_LOGICA_ITENS);
					if (action != null) {
						getPlcControleConversacao().setModoPlc(PlcConstants.MODOS.MODO_CONSULTA);
					}
				}else if (formPattern==FormPattern.Tab){
					//contextUtil.setRequestAttribute(PlcConstantes.PlcJsfConstantes.PLC_MANAGED_BEAN_KEY, PlcConstantes.PlcJsfConstantes.PLC_LOGICA_ITENS);
					if (action != null) {
						getPlcControleConversacao().setModoPlc(PlcConstants.MODOS.MODO_EDICAO);
					}
				}
				
				contextUtil.setRequestAttribute(PlcConstants.PlcJsfConstantes.PLC_MANAGED_BEAN_KEY, aliasCasoUso);
				
				// inicializar managed beans logo apos a definicao da entidade
				initializeManagedBeans();

				registraRequisicaoMetadadosLogicaPadrao();

				// Disponibiliza última acao na sessao, para logica generica de
				// retorno da selecao
				if (formPattern == FormPattern.Man || formPattern == FormPattern.Mdt || formPattern == FormPattern.Mad || formPattern == FormPattern.Mds || formPattern == FormPattern.Mds) {

					String requestURI = contextUtil.getRequest().getRequestURI();
					if (!requestURI.contains(".xhtml")) {
						contextUtil.setSessionAttribute(PlcConstants.PlcJsfConstantes.PATH_URL_ULT_MANUTENCAO_SEM_BARRA, nomeAction);
					} else {
						String ultMan = urlUtil.getUrlXhtml(requestURI);
						contextUtil.setSessionAttribute(PlcConstants.PlcJsfConstantes.PATH_URL_ULT_MANUTENCAO_SEM_BARRA, ultMan);
					}
				}
				
				if (action!=null)
					action.handleButtonsAccordingFormPattern();

				visaoJsfUtil.handleGoogleServices(contextUtil.getRequest(),configColaboracao.googleMap().key());

				// Desabilita explorer para página inicial.
				if (nomeAction.equals("/inicial"))
					visaoJsfUtil.handleTreeViewPanel(contextUtil.getRequest(),false,null,null,"/f/t"+nomeAction,null);

				//Registrando informações do Google Analytics
				PlcConfigApplication configGrupoControleAplicacao = configUtil.getConfigApplication().application();
				registryGoogleAnalyticsInfo(configColaboracao, configGrupoControleAplicacao);

				if (action != null) {
					Map<String, Class> detalhesPorDemanda = getPlcControleConversacao().getDetalhesPorDemanda();
					boolean modoInclusao = PlcConstants.MODOS.MODO_INCLUSAO.equals(getPlcControleConversacao().getModoPlc());
					if (detalhesPorDemanda == null && !modoInclusao && (formPattern == FormPattern.Mdt || formPattern == FormPattern.Mad || formPattern == FormPattern.Mas || formPattern == FormPattern.Mds)) {
						boolean possuiId = chaveNaturalUtil.checkNullId(getEntity());
						if (getEntity() != null && possuiId) {
							criaListaDetalhesPorDemanda(configAgregacao.details());
						}
					} else if (modoInclusao) {
						// Quando é inclusão, não é necessário ter detalhes em
						// demanda, pois eles serão incluídos
						HashMap<String, Class> hashMap = new HashMap<String, Class>();
						getPlcControleConversacao().setDetalhesPorDemanda(hashMap);
						contextUtil.getRequest().setAttribute(PlcConstants.FORM.AUTOMACAO.DETALHES.DETALHE_POR_DEMANDA, hashMap);
					} else if (detalhesPorDemanda != null) {
						// Sincronizar o map da conversação com o map do request
						contextUtil.getRequest().setAttribute(PlcConstants.FORM.AUTOMACAO.DETALHES.DETALHE_POR_DEMANDA, detalhesPorDemanda);
					}
				}


				/* Está colocando o modo somente aqui porque não teve necessidade de colocar externamente porque tem no controleConversacao.
				 * Neste caso é porque vai ser Utilizado no Tiles
				 */
				try {
					contextUtil.getRequest().setAttribute(PlcConstants.FORM.AUTOMACAO.MODO, getPlcControleConversacao().getModoPlc());
				} catch (Exception e) {}
				if (formPattern == FormPattern.Tab || formPattern == FormPattern.Man || formPattern == FormPattern.Mdt || formPattern == FormPattern.Sel || formPattern == FormPattern.Mad || formPattern == FormPattern.Mas
						|| formPattern == FormPattern.Mds) {
					String source = contextUtil.getRequest().getParameter("source");
					if ("corpo:formulario:botaoAcaoGravar".equals(source)) {
						contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_GRAVAR);
					} else if ("corpo:formulario:botaoAcaoPesquisar".equals(source)) {
						contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_PESQUISAR);
					} else if ("corpo:formulario:botaoAcaoExcluir".equals(source)) {
						contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_EXCLUIR);
					}
				}
			} else {

				//Revisar se ainda entra aqui, já que agora URLs sem metadados assume o "defaultplc" (CONTROLE)
				if (nomeAction != null && nomeAction.indexOf("inicial")==-1 && !nomeAction.equals("/") ) {
					throw new PlcException("Nao encontrou meta-dados do jcompany em com.powerlogic.jcompany.config.app para url ");
				} else {
					/*
					 * Desabilitando treeview para página inicial.
					 * A Página inicial não tem meta-dados, logo é um tratamento especial.
					 */
					visaoJsfUtil.handleTreeViewPanel(contextUtil.getRequest(),false,null,null,"/f/t"+nomeAction,null);
				}
			}				
			
			if (configColaboracao != null) {
				
				//quando o código é gerado pelo wizard ele coloca a anotação 
				//@PlcConfigColaboracao com o caminho do diretorio sendo que
				//o dirétorio padrão deve ser o raiz no caso fcls.
				String colaboracao = getNomeColaboracao(true);
				String dirBaseFcls = configColaboracao.formLayout().dirBase();
				
				if (StringUtils.isEmpty(dirBaseFcls)) {
					dirBaseFcls = "/WEB-INF/fcls" + (colaboracao.startsWith("/") ? "" : "/") + colaboracao;
				}

				visaoJsfUtil.handleGenericParameters(contextUtil.getRequest(), 
						null, 
						configColaboracao.selection()!=null&&configColaboracao.selection().pagination().numberByPage()>0?""+configColaboracao.selection().pagination().numberByPage():null, 
						configColaboracao.selection()!=null&&configColaboracao.selection().pagination().numberByPage()>0?""+configColaboracao.selection().pagination().dynamicType().toString():null, 
						configColaboracao.selection()!=null&&configColaboracao.selection().pagination().numberByPage()>0?""+configColaboracao.selection().pagination().topStyle().toString():null, 
						configColaboracao.selection()!=null&&configColaboracao.selection().pagination().numberByPage()>0?""+configColaboracao.selection().pagination().topPosition().toString():null, 				
						configColaboracao.behavior().useUpdateWarning()?"S":"N",
						configColaboracao.behavior().useDeleteDetailWarning()?"S":"N", 
						configColaboracao.print().smartPrint()?"S":"N", 
						configAgregacao.pattern().modality().toString(),
						dirBaseFcls);
			}	

			// Trata indicadores de arquivo anexado
			visaoJsfUtil.handleFileAttach(getNomeColaboracao());

			requestRegistryAfter();

			jSecurityApi.configuraVisao(contextUtil.getRequest());

		}

	}

	private PlcConversationControl getPlcControleConversacao() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcConversationControl.class, new PlcNamedLiteral(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO));
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After"
	 * ou "api" são eventos (método vazios) destinados a especializações nos
	 * descendentes.
	 */
	protected boolean registraRequisicaoAntes()  {
		return true;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After"
	 * ou "api" são eventos (método vazios) destinados a especializações nos
	 * descendentes.
	 */
	protected void requestRegistryAfter()  {
	}

	/**
	 * Determina o nome da action atual, baseado na URL.
	 * Elimina prefixo do módulo, se necessário.
	 * @return nome da Action, começando com barra ("/").
	 * 
	 */
	public String getNomeColaboracao(boolean semSufixo)  {

		String nomeColaboracao = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());

		if (nomeColaboracao.indexOf('/')>0) { // procura uma barra depois da primeira posição ( > 0)
			//Se tiver uma barra no meio no nome, pode ser uma lógica de algum módulo.
			nomeColaboracao = getModuloCorrente(nomeColaboracao, false);
		}
		if (nomeColaboracao.startsWith("/")){
			return nomeColaboracao;
		}

		if (semSufixo && nomeColaboracao.length() > 3) {
			if (ArrayUtils.contains(PlcConfigUtil.SUFIXOS_URL, nomeColaboracao.substring(nomeColaboracao.length() - 3)))
				nomeColaboracao = nomeColaboracao.substring(0, nomeColaboracao.length() - 3);
		}

		return "/" + nomeColaboracao;
	}

	public String getNomeColaboracao()  {
		return getNomeColaboracao(false);
	}

	protected String getModuloCorrente(String nomeColaboracao, boolean replace)  {

		PlcConfigApplication configAplicacao = configUtil.getConfigApplication().application();

		PlcConfigModule[] modulosAplicacao = configAplicacao.modules();

		if (modulosAplicacao!=null) {
			for (PlcConfigModule modulo : modulosAplicacao) {
				if (nomeColaboracao.startsWith(modulo.acronym()+'/') || nomeColaboracao.startsWith('/'+ modulo.acronym()+'/' )) { // se o nome comecar pela sigla do modulo, tem que tirar.
					if (replace){
						nomeColaboracao = nomeColaboracao.substring(modulo.acronym().length()+1);
					}
					contextUtil.getRequest().setAttribute(PlcJsfConstantes.PLC_MODULO_CORRENTE, modulo.description());
					break;
				}
			}
		}
		return nomeColaboracao;
	}

	/**
	 * @since jCompany 5 Registra em escopo de request os meta-dados para uso desacoplado em controladores e layouts.
	 * @param currrentConfigAcao Meta-dados gerais para a ação
	 * @param currrentConfigAcaoControle Meta-dados para camada controle
	 */
	protected void registraRequisicaoMetadadosLogicaPadrao()  {

		if (requestRegistryMetadataUseCaseBefore()) {

			String nomeAction = getNomeColaboracao();
			PlcConfigAggregationPOJO configGrupoAgregacao =  configUtil.getConfigAggregation(nomeAction);
			PlcConfigCollaborationPOJO configGrupoControle =  configUtil.getConfigCollaboration(nomeAction);

			// Disponibiliza logica no request para layouts universais e paginas
			contextUtil.setRequestAttribute(PlcConstants.FORM_PATTERN,configGrupoAgregacao.pattern().formPattern()!=null?configGrupoAgregacao.pattern().formPattern().toString():null); 		

			// Coloca entidade principal no request
			contextUtil.setRequestAttribute(PlcConstants.ENTIDADE.ENTIDADE, configGrupoAgregacao.entity().getName());

			// Disponibiliza total de detalhes para controladores de layout universal
			if (configGrupoAgregacao!=null && configGrupoAgregacao.details()!=null)
				contextUtil.setRequestAttribute(PlcConstants.TOTAL_DETALHES_PLC, configGrupoAgregacao.details().length); 		

			PlcEntityInstance entidadeInstance = null;
			try{
				entidadeInstance = metamodelUtil.createEntityInstance(getEntity());
			} catch (Exception e) {}

			String prefixoUrl = "/f/n";

			if (configGrupoControle!=null && configGrupoControle.behavior()!=null)
				visaoJsfUtil.handleTreeViewPanel(contextUtil.getRequest(),
					configGrupoControle.behavior().useTreeView(),
					configGrupoAgregacao.pattern().formPattern(),
					entidadeInstance!=null?entidadeInstance.getId():null,
							prefixoUrl+contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_COM_BARRA)+"?",
							(PlcEntityTreeView)configGrupoAgregacao.entity().getAnnotation(PlcEntityTreeView.class));

			// Se irá controlar modo de exclusao - por enquanto o if foi retirado da pagina
			if (configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Tab) ||
					configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Mad) ||
					configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Mdt) ||
					configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Mas) ||
					configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Mds)) {
				contextUtil.setRequestAttribute(PlcConstants.FORM.AUTOMACAO.DETALHES.EXIBE_IND_EXC_DET_PLC,"S");
			}

			executeAction();

			requestRegistryMetadataUseCaseAfter();
		}
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After"
	 * ou "api" são eventos (método vazios) destinados a especializações nos
	 * descendentes.
	 */
	protected boolean requestRegistryMetadataUseCaseBefore() {
		return true;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After"
	 * ou "api" são eventos (método vazios) destinados a especializações nos
	 * descendentes.
	 */
	protected void requestRegistryMetadataUseCaseAfter() {

	}

	/**
	 * Método que força a inicialização dos managed beans a cada request caso ainda não existam.
	 * A inicialização é forçada tentando resolver uma expressão com eles.
	 * @since jCompany 5.0
	 */
	protected void initializeManagedBeans()  {
		elUtil.evaluateExpressionGet("#{" +PlcJsfConstantes.PLC_CONTROLE_DETALHE_PAGINADO+ "}", Object.class);
	}

	/**
	 * O método resitra no request informações para o uso do Google Analytics de 
	 * acordo com as anotacções da aplicação ou do caso de uso.
	 */
	protected void registryGoogleAnalyticsInfo(PlcConfigCollaborationPOJO configGrupoControle, PlcConfigApplication configGrupoControleAplicacao) {

		if(configGrupoControleAplicacao.googleAnalyticsKey()!=null && !configGrupoControleAplicacao.googleAnalyticsKey().equals("")) {
			contextUtil.setRequestAttribute(PlcConstants.PlcJsfConstantes.GOOGLE_ANALYTICS_USA, true);
			contextUtil.setRequestAttribute(PlcConstants.PlcJsfConstantes.GOOGLE_ANALYTICS_KEY, configGrupoControleAplicacao.googleAnalyticsKey());
		}

	}

	protected Object getEntity() {

		String managedBean = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.PLC_MANAGED_BEAN_KEY);

		if (managedBean != null && !managedBean.equals("")) {

			String nomeAction = getNomeColaboracao();
			PlcConfigAggregationPOJO configGrupoAgregacao = configUtil.getConfigAggregation(nomeAction);
			Object entidade = null;

			if (configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Tab)) {
				entidade = elUtil.evaluateExpressionGet("#{" + managedBean + "Lista}", Object.class);
			} else {
				entidade = elUtil.evaluateExpressionGet("#{" + managedBean + "}", Object.class);
			}

			if (entidade != null && metamodelUtil.isEntityClass(entidade.getClass())) {
				return entidade;
			} else {
				return null;
			}

		} else {
			return null;
		}
	}

	/**
	 * Cria Lista de detalhes que devem ser recuperados por demanda. E necessario colocar no request para sere utilizado no tiles.
	 * @param detalhes Anotacao de detalhes do caso de uso corrente
	 */
	@SuppressWarnings("unchecked")
	protected void criaListaDetalhesPorDemanda(PlcConfigDetail detalhes[]){

		Map<String, Class> detalhesPorDemanda 	= getPlcControleConversacao().getDetalhesPorDemanda();

		if ( detalhesPorDemanda == null ){
			detalhesPorDemanda = new HashMap<String, Class>();
			for (PlcConfigDetail detalhe : detalhes) {
				if (detalhe!=null && detalhe.clazz()!=null && metamodelUtil.isEntityClass(detalhe.clazz()) ){
					if (detalhe.onDemand())
						detalhesPorDemanda.put(detalhe.collectionName(), detalhe.clazz());
				}
			}
			getPlcControleConversacao().setDetalhesPorDemanda(detalhesPorDemanda);
		}

		// Coloca Lista no Request
		contextUtil.getRequest().setAttribute(PlcConstants.FORM.AUTOMACAO.DETALHES.DETALHE_POR_DEMANDA, detalhesPorDemanda);
	}

	/**
	 * Executa a toda chamada para fazer tratamentos genéricos que levem em conta o estado do Backing Bean (PlcBaseAction)
	 * @param configUrl 
	 * 
	 */
	protected void executeAction()  {

		PlcBaseMB action = null;
		try {
			action = getMB();
		} catch (Exception e) {}

		String nomeAction = getNomeColaboracao();
		PlcConfigAggregationPOJO configGrupoAgregacao =  configUtil.getConfigAggregation(nomeAction);		

		contextUtil.setRequestAttribute(PlcConstants.PlcJsfConstantes.PLC_MB,action);

		// Se for um GET e nao tiver os flags, retira modo
		if (contextUtil.getRequestParameter("mfPlc")==null 
				&& contextUtil.getRequestParameter("amp;mfPlc")==null 
				&& contextUtil.getRequest().getMethod().equals("GET")){
			//removendo atributo
			contextUtil.getRequest().getSession().removeAttribute("mfPlc");
		} else {
			// Se tiver flag de modo "somente visualiza" mfPlc=v e for manutencao, altera o modo para "Visualiza"
			if (action != null && 
					(contextUtil.getRequestParameter("mfPlc")!=null ||
							contextUtil.getRequestParameter("amp;mfPlc")!=null ||	   
							contextUtil.getSessionAttribute("mfPlc")!=null)) {

				if (!configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Sel) && 
						!configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Smd)) {

					if (contextUtil.getRequestParameter("id")!=null &&
							contextUtil.getRequestParameter("mfPlc")==null &&
							contextUtil.getRequestParameter("amp;mfPlc")!=null)
						contextUtil.getRequest().getSession().removeAttribute("mfPlc");
					else {
						// É manutençao, entao marca para somente Con
						getPlcControleRequisicao().setExibeEditarDocumentoPlc("S");
						contextUtil.setRequestAttribute(PlcConstants.VISUALIZA_DOCUMENTO_PLC, "S");
					}
				} else {
					// É seleção, entao somente disponibiliza em conversacao para repasse
					if (contextUtil.getRequestParameter("mfPlc")!=null ||
							contextUtil.getRequestParameter("amp;mfPlc")!=null) {
						if (contextUtil.getRequestParameter("mfPlc")!=null)
							contextUtil.setSessionAttribute("mfPlc",
									contextUtil.getRequestParameter("mfPlc"));
						if (contextUtil.getRequestParameter("amp;mfPlc")!=null)
							contextUtil.setSessionAttribute("mfPlc",
									contextUtil.getRequestParameter("amp;mfPlc"));
					} else if (contextUtil.getRequest().getMethod().equals("GET") &&
							contextUtil.getRequestParameter("fwPlc")!=null)
						contextUtil.getRequest().getSession().removeAttribute("mfPlc");
				}
			}
		}

		// Se for um GET e nao tiver os flags, retira modo
		if (contextUtil.getRequestParameter("mcPlc")==null &&
				contextUtil.getRequestParameter("amp;mcPlc")==null &&	
				contextUtil.getRequest().getMethod().equals("GET"))
			contextUtil.getRequest().getSession().removeAttribute("mcPlc");
		else {
			//	Se tiver flag de modo "somente texto" mcPlc=t e for manutencao, altera o modo para "Texto"
			// O Firefox nao considera caracteres '&amp;'= '&', por isto o teste abaixo
			if (action != null && 
					(contextUtil.getRequestParameter("mcPlc")!=null ||
							contextUtil.getSessionAttribute("mcPlc")!=null ||
							contextUtil.getRequestParameter("amp;mcPlc")!=null)) {

				if (!configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Sel) && 
						!configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Smd)) {

					if (contextUtil.getRequestParameter("id")!=null &&
							contextUtil.getRequestParameter("mcPlc")==null &&
							contextUtil.getRequestParameter("amp;mcPlc")==null)
						contextUtil.getRequest().getSession().removeAttribute("mcPlc");
					else {
						if ("t".equals(contextUtil.getRequestParameter("mcPlc"))){
							// É manutençao, entao marca para somente texto
							contextUtil.setRequestAttribute("visualizaCampoPlc","t");
						}else if ("p".equals(contextUtil.getRequestParameter("mcPlc"))){
							// É manutençao, entao marca para campo protegido
							contextUtil.setRequestAttribute("visualizaCampoPlc","p");
						}
					}
				} else {
					// É seleção, entao somente disponibiliza em conversacao para repasse
					if (contextUtil.getRequestParameter("mcPlc")!=null ||
							contextUtil.getRequestParameter("amp;mcPlc")!=null) {
						if (contextUtil.getRequestParameter("mcPlc")!=null) 
							contextUtil.setSessionAttribute("mcPlc",
									contextUtil.getRequestParameter("mcPlc"));
						if (contextUtil.getRequestParameter("amp;mcPlc")!=null)
							contextUtil.setSessionAttribute("mcPlc",
									contextUtil.getRequestParameter("amp;mcPlc"));
					} else if (contextUtil.getRequest().getMethod().equals("GET") &&
							contextUtil.getRequestParameter("fwPlc")!=null)
						contextUtil.getRequest().getSession().removeAttribute("mcPlc");
				}
			}
		}

		if(contextUtil.getRequest().getAttribute(PlcConstants.LOGICAPADRAO.MANUTENCAO.ESTILO_APRESENTACAO) != null){
			// coloca as páginas como texto ou protegido
			// Se tiver estiloApresentacaoPlc igual"texto" e for manutencao, altera o modo para "Texto"
			// O Firefox nao considera caracteres '&amp;'= '&', por isto o teste abaixo
			if (action != null) {

				if (!configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Sel) 
						&& !configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Smd)) {

					if ("texto".equals(contextUtil.getRequest().getAttribute(PlcConstants.LOGICAPADRAO.MANUTENCAO.ESTILO_APRESENTACAO))){
						// É manutençao, entao marca para somente texto
						contextUtil.setRequestAttribute("visualizaCampoPlc","t");
					}else if ("p".equals(contextUtil.getRequest().getAttribute(PlcConstants.LOGICAPADRAO.MANUTENCAO.ESTILO_APRESENTACAO))){
						// É manutençao, entao marca para campo protegido
						contextUtil.setRequestAttribute("visualizaCampoPlc","p");
					}
				} 
			}

		}

		Object entidade = null;
		try {
			entidade = getEntity();
		} catch (Exception e) {}
		if (entidade !=null) {
			try {
				// Disponibiliza versão toString para exibição em topos quando há título específico
				contextUtil.setRequestAttribute("toString", entidade.toString());
			} catch (NullPointerException e) {
				log.info("Tentou-se pegar o toString() da entidade para setar no request porém o mesmo retornou null. Provavelmente tem um objeto ou propriedade não inicializado dentro do método.");
				contextUtil.setRequestAttribute("toString", "");
			}
		}
	}

	private PlcRequestControl getPlcControleRequisicao() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcRequestControl.class, new PlcNamedLiteral(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_REQUISICAO));
	}

}
