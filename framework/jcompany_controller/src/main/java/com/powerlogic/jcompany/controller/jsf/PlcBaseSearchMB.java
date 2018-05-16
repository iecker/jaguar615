/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.util.AnnotationLiteral;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import yarfraw.core.datamodel.ChannelFeed;
import yarfraw.core.datamodel.FeedFormat;
import yarfraw.core.datamodel.ItemEntry;
import yarfraw.io.FeedWriter;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.PlcAnnotationUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.config.collaboration.PlcConfigExport;
import com.powerlogic.jcompany.config.collaboration.PlcConfigNestedCombo;
import com.powerlogic.jcompany.config.collaboration.PlcConfigPagination.DynamicType;
import com.powerlogic.jcompany.config.collaboration.PlcConfigReport;
import com.powerlogic.jcompany.config.collaboration.PlcConfigRssSelection;
import com.powerlogic.jcompany.config.collaboration.PlcConfigSelection;
import com.powerlogic.jcompany.controller.bindingtype.PlcAutofindAggregateAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcAutofindAggregateBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcClearArgsAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcClearArgsBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcFindDetailOnDemandAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcFindDetailOnDemandBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcFindNavigationNestedComboAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcFindNavigationNestedComboBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcGenerateReportAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcSearchAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcSearchBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcSearchWithNavigatorAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcSearchWithNavigatorBefore;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcPagedControl;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcPagedDetailControl;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcDomainLookupUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcEntityUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcNestedComboUtil;
import com.powerlogic.jcompany.controller.util.PlcBeanPopulateUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

/**
 * Controle pesquisas em formulários de selecao ou consulta
 */
@QPlcDefault
public class PlcBaseSearchMB extends PlcBaseParentMB implements Serializable {

	@Inject
	protected transient Logger log;

	private static final long serialVersionUID = 1L;	

	/**
	 * Controller para detalhes paginados e argumentos em detalhes
	 */
	@Inject @Named(PlcJsfConstantes.PLC_CONTROLE_DETALHE_PAGINADO)
	protected PlcPagedDetailControl controleDetalhePaginadoPlc;
	
	@Inject @Named(PlcJsfConstantes.PLC_CONTROLE_PAGINACAO)
	protected PlcPagedControl plcControlePaginacao;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	

	@Inject @QPlcDefault
	PlcBeanPopulateUtil beanPopulateUtil;

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;

	@Inject @QPlcDefault 
	protected PlcEntityUtil entityUtil;

	@Inject @QPlcDefault 
	protected PlcMsgUtil msgUtil;	

	@Inject @QPlcDefault 
	protected PlcBaseValidation validacaoAction;	

	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;

	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;	

	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;

	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;
	
	@Inject @QPlcDefault
	protected PlcReflectionUtil reflectionUtil;
	
	@Inject @QPlcDefault 
	protected PlcAnnotationUtil annotationUtil;
	
	@Inject @QPlcDefault 
	protected PlcNestedComboUtil nestedComboUtil;

	@Inject @Named("plcAction")
	protected PlcBaseMB plcAction; 	
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	/**
	 * Realiza pesquisas para consulta ou selecao.
	 */
	public String search(PlcEntityList entityListPlc)  {

		this.entityListPlc = entityListPlc;

		if (contextUtil.getRequest().getAttribute(PlcConstants.ACAO.EVENTO)==null)
			contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_PESQUISAR);

		entityListPlc.setItensPlc(new ArrayList<Object>());

		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		FormPattern pattern 			= configUtil.getConfigAggregation(url).pattern().formPattern();
		Collection listaEntity = new ArrayList<Object>();

		if (pattern != null && pattern==FormPattern.Tab){
			// Incluido para acertar problema de informação modo quando pesquisa em Tabular
			String evento = (String)contextUtil.getRequest().getAttribute("evento");
			if (PlcConstants.ACAO.EVT_PESQUISAR.equals(evento))
				contextUtil.getRequest().setAttribute(PlcConstants.MODOS.MODO, PlcConstants.MODOS.MODO_CONSULTA);
		}

		int numPorPagina = -1;
		PlcConfigSelection selecao = configUtil.getConfigCollaboration(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest())).selection();
		if (selecao!=null)
			numPorPagina = selecao.pagination().numberByPage();

		if (searchBefore(selecao,context,entityListPlc)) {
			
			try {
				if (entityPlc == null
						|| (entityPlc != null 
							&& !entityPlc.getClass().isAssignableFrom(configUtil.getConfigAggregation(url).entity()))) {
					entityPlc = configUtil.getConfigAggregation(url).entity().newInstance();
					Map<String, Object> argumentos = getSearchParameters();
					beanPopulateUtil.transferMapToBean(argumentos, entityPlc);
				}	
	
				if (FormPattern.Tab.equals(pattern)) {
					String propriedadeOrdenacao =configUtil.getConfigCollaboration(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest())).tabular().orderProp();
					listaEntity = iocControleFacadeUtil.getFacade(url).findSimpleList(context, configUtil.getConfigAggregation(url).entity(), propriedadeOrdenacao);
				} else if (FormPattern.Ctb.equals(pattern)) {
						String propriedadeOrdenacao = configUtil.getConfigCollaboration(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest())).tabular().orderProp();
						plcControleConversacao.setOrdenacaoPlc(propriedadeOrdenacao);
						listaEntity = iocControleFacadeUtil.getFacade().findList(context, entityPlc, plcControleConversacao.getOrdenacaoPlc(), 0, 0);
				} else if (numPorPagina == -1) {
					listaEntity = iocControleFacadeUtil.getFacade().findList(context, entityPlc, plcControleConversacao.getOrdenacaoPlc(), 0, 0);
				} else {
					// Pesquisa com Navegador
					if (searchWithNavigatorBefore(selecao,context,entityListPlc)){
						listaEntity = searchWithNavigator(entityListPlc, entityPlc);
						listaEntity = searchWithNavigatorAfter(listaEntity);
					}
				}
			} catch(PlcException plcE){
				throw plcE;			
			} catch (Exception e) {
				throw new PlcException("PlcBaseSearchMB", "search", e, log, "");
			}
	
			String formatoPesquisa = contextUtil.getRequestParameter("formato");
			if (StringUtils.isNotBlank(formatoPesquisa) && formatoPesquisa.equals("RSS")){
				createRssResponse(listaEntity);
				return searchAfter(selecao,context,entityListPlc);
			}
	
			if (listaEntity == null)
				listaEntity = new ArrayList<Object>();
	
			if (listaEntity.isEmpty()) {
	
				// Se não existir nenhum, mantém registros em branco para entrada de dados
				msgUtil.msg(PlcBeanMessages.JCOMPANY_WARNING_SEL_NOT_FOUND, new Object[]{}, PlcMessage.Cor.msgVermelhoPlc.toString());
	
			} else {
				
				//Recupera combo dinamico para cada item da coleção
				if (FormPattern.Tab.equals(pattern)|| FormPattern.Ctb.equals(pattern)) {
					retrieveNestedComboTabular(listaEntity);
				}
				entityListPlc.setItensPlc(((List)listaEntity));
	
				searchCheckExport();
	
			}
	
			contextUtil.getRequestMap().put(PlcJsfConstantes.PLC_LIMPA_CAIXA_EXCLUSAO, Boolean.TRUE);
		
		}

		return searchAfter(selecao,context,entityListPlc);
	}

	/**
	 * Monta mapa com os parametros de pesquisa.
	 * 
	 * @return Mapa&ltnomeParamentro, valorParametro&gt com os parametros de pesquisa
	 */
	protected Map<String, Object> getSearchParameters() {
		
		Map<String, Object> argumentos = new HashedMap();
		Map<String, Object> mapa = contextUtil.getRequest().getParameterMap();
		Set<String> mapaKeys = mapa.keySet();
		for (String nome : mapaKeys ) {
			Object valor = mapa.get(nome);
			if (valor != null && valor.getClass().isArray() && Array.getLength(valor) > 0) {
				valor = Array.get(valor, 0);
			}
			if (nome.contains("corpo:formulario:") && StringUtils.isNotEmpty((String) valor) ) 
				argumentos.put(nome.replace("corpo:formulario:", ""), valor);
		}		
		return argumentos;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * Se a requisição contiver um parâmetro chamado "datatype", a navegação será enviada 
	 * para redirecionamento com o valor informado no parametro.
	 * Por exemplo: datatype=xml será enviado para navegaçãoo (from-outcome do faces-config) 
	 * de nome xml 
	 */
	protected String searchAfter(PlcConfigSelection selecao, PlcBaseContextVO context, PlcEntityList entityListPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcSearchAfter>(){});
		if (contextUtil.getRequestParameter("datatypePlc")!=null)
			return contextUtil.getRequestParameter("datatypePlc");
		else
			return defaultNavigationFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param entityListPlc Lista de entidades 
	 * @param context Parametros de configuracao
	 * @param selecao Metadados
	 * @param logica Padrão de formulario
	 */
	protected boolean searchBefore( PlcConfigSelection selecao, PlcBaseContextVO context, PlcEntityList entityListPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcSearchBefore>(){});
		return defaultProcessFlow;
	}



	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param entityListPlc Lista de entidades
	 * @param context Parametros de configuracao
	 * @param selecao Metadados para selecao
	 */
	protected boolean searchWithNavigatorBefore(PlcConfigSelection selecao, PlcBaseContextVO context, PlcEntityList entityListPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcSearchWithNavigatorBefore>(){});
		return defaultProcessFlow;
	}


	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected Collection searchWithNavigatorAfter(Collection listaVOs)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcSearchWithNavigatorAfter>(){});
		return listaVOs;
	}

	/**
	 * Utilizado para filtros em detalhes. Irá filtrar os detalhes baseada nos argumentos informados pelo usuário. 
	 * Baseia a pesquisa no detalhe corrente registrado no campo detCorrPlcPaginado
	 * TODO Homologar e prover template methods
	 */
	public String seachDetail () {

		try {
			String detCorrPlcPaginado = controleDetalhePaginadoPlc.getDetCorrPlcPaginado();

			if (detCorrPlcPaginado == null)
				detCorrPlcPaginado = contextUtil.getRequestParameter("detCorrPlcPaginado");

			if (StringUtils.isNotBlank(detCorrPlcPaginado)){

				// Acerto para funcionar sem AJAX

				detCorrPlcPaginado = detCorrPlcPaginado.split("#")[0];

				PlcConfigDetail configDetalhe = controleDetalhePaginadoPlc.getDetalhePorNome(detCorrPlcPaginado);
				if (configDetalhe == null)
					return null;

				int numPorPagina = configDetalhe.navigation().numberByPage();
				String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());

				IPlcFacade facade = (IPlcFacade)iocControleFacadeUtil.getFacade(url);

				PlcBaseContextVO context = getContext();
				if (context == null)
					context = contextMontaUtil.createContextParam(plcControleConversacao);


				Object entidadeMestre = configDetalhe.clazz().newInstance();
				beanPopulateUtil.transferMapToBean(contextUtil.getRequest().getParameterMap(), entidadeMestre);

				Object entidadeDetalhe = configDetalhe.clazz().newInstance();
				// TODO - Método pesquisaDetalhe - RESOLVER.
				propertyUtilsBean.setProperty(entidadeDetalhe, "", "");

				// Recontagem do total de registros junto com os argumentos
				Long numTotalRegistros = facade.findCount(context, entidadeDetalhe);

				Collection recuperaLista = facade.findListPagedDetail(context, configDetalhe, entidadeMestre, numPorPagina, null,0, true);

				propertyUtilsBean.setProperty(entityPlc, detCorrPlcPaginado, recuperaLista);

				controleDetalhePaginadoPlc.setDetalhePaginado(configDetalhe, numTotalRegistros);

			} else {
				msgUtil.msg("Não há detalhe selecionado para pesquisa", PlcMessage.Cor.msgVermelhoPlc.toString());
			}
			
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBaseSearchMB", "seachDetail", e, log, "");
		}

		return defaultNavigationFlow;
	}

	protected PlcBaseContextVO getContext() {
		return (PlcBaseContextVO)contextUtil.getRequest().getAttribute(PlcConstants.CONTEXT);
	}

	/**
	 * Método utizado para saída de RSS.
	 *  
	 */
	protected void createRssResponse(Collection entityList)  {

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		PlcConfigRssSelection rssSelecao = configUtil.getConfigCollaboration(url).rssSelection();

		File f=null;
		try {
			f = File.createTempFile("yarfraw", ".xml");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FeedWriter write = new FeedWriter(f, FeedFormat.RSS20);
		//document.setVersion();

		ChannelFeed channel = new ChannelFeed();

		String action = (String) urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		String titulo = rssSelecao.title();

		// TODO I18n
		if(StringUtils.isNotBlank(titulo))
			channel.setTitle(titulo);
		else
			channel.setTitle("Seleção: " + action);

		String requestURL = contextUtil.getRequest().getRequestURL().toString();
		channel.addLink(requestURL);
		//channel.setChannelUri(url);

		String descricao = rssSelecao.description();
		if (StringUtils.isNotBlank(descricao))
			channel.setDescriptionOrSubtitle(descricao);
		else
			channel.setDescriptionOrSubtitle("Descricao: " + action);

		String actionMan = rssSelecao.actionMan();
		if (StringUtils.isNotBlank(actionMan))
			requestURL = requestURL.replace(action, actionMan);
		else {
			actionMan = action.substring(0, action.lastIndexOf("sel")) + "man";
			requestURL = requestURL.replace(action, actionMan);
		}

		int count = 0;
		for (Object vo : entityList) {
			PlcEntityInstance voInstance = metamodelUtil.createEntityInstance(vo);

			ItemEntry item = new ItemEntry ();
			try {
				item.setTitle(vo.toString());

				String[] campos = rssSelecao.fields();
				StringBuilder sb = new StringBuilder ();

				boolean par;
				if (count%2==0)
					par = true;
				else
					par = false;

				sb.append("<div>");
				for (String campo : campos) {
					Object valorCampo = propertyUtilsBean.getProperty(vo,campo);
					if (valorCampo != null){
						if (metamodelUtil.isEntityClass(valorCampo.getClass())){
							sb.append("<tr style=\"border: 1px\">").append(valorCampo.toString()).append("</tr>");
						} else {
							sb.append("<tr style=\"border: 1px\">").append(entityUtil.resolveField(valorCampo)).append("</tr>");
						}
					}
				}
				sb.append("</div>");

				item.setDescriptionOrSummary(sb.toString());

				String id = voInstance.getId().toString();

				item.addLink(requestURL+"?id=" + id);
				channel.addItem(item);
			} catch(PlcException plcE){
				throw plcE;				
			} catch (Exception e) {
				throw new PlcException("PlcBaseSearchMB", "createRssResponse", e, log, "");
			}
			
		}

		try {
			write.writeChannel(channel);

			FacesContext facesContext  = FacesContext.getCurrentInstance();
			if (! facesContext.getResponseComplete()){

				PrintWriter writer = contextUtil.getResponse().getWriter();
				contextUtil.getResponse().reset();
				contextUtil.getResponse().setContentType(" application/rss+xml");

				//RssGenerator.generateRss(document, writer);
				writer.println(FileUtils.readFileToString(f, "UTF-8"));

				contextUtil.getResponse().flushBuffer();
				FacesContext.getCurrentInstance().responseComplete();
			}

			FileUtils.forceDelete(f);
			
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseSearchMB", "createRssResponse", e, log, "");
		}

	}


	/**
	 * Verifica se foi configurado para habilitar a exportação dos registros,
	 * se verdadeiro configura a exibição do botão/combobox Exportar
	 */
	protected boolean searchCheckExport(){

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		PlcConfigExport configExportacao = null;
		if (configUtil.getConfigCollaboration(url).selection()!=null)
			configExportacao = configUtil.getConfigCollaboration(url).selection().export();
		if (configExportacao != null && configExportacao.useExport()){
			contextUtil.setRequestAttribute(PlcConstants.ACAO.EXIBE_BT_EXPORTA,PlcConstants.EXIBIR);
			String formatos = "";
			for(int i=0; i < configExportacao.formats().length; i++){
				if (i == 0)
					formatos = formatos + configExportacao.formats()[i];
				else
					formatos = formatos + "," + configExportacao.formats()[i];
			}
			contextUtil.setRequestAttribute("exportacaoListaFormatosPlc",formatos);

			return true;
		} else {
			contextUtil.setRequestAttribute(PlcConstants.ACAO.EXIBE_BT_EXPORTA,PlcConstants.NAO_EXIBIR);
			return false;
		}

	}

	/**
	 * Executa a pesquisa limitando a quantidade de registros recuperados
	 * @param listaArgumentos Lista com POJOs de argumentos
	 */
	protected Collection searchWithNavigator(PlcEntityList entityListPlc, Object entidade)	 {

		log.debug( "Iniciando pesquisaComNavegador");
		
		PlcBaseContextVO context = (PlcBaseContextVO) contextUtil.getRequest().getAttribute(PlcConstants.CONTEXT);
		
		if (plcControlePaginacao == null)
			plcControlePaginacao = PlcCDIUtil.getInstance().getInstanceByType(PlcPagedControl.class, new PlcNamedLiteral(PlcJsfConstantes.PLC_CONTROLE_DETALHE_PAGINADO));
		
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		Long total = iocControleFacadeUtil.getFacade(url).findCount(context, entidade);

		// Passar comandos de contexto para service
		int navDe = -1;
		if (StringUtils.isNotEmpty(contextUtil.getRequest().getParameter("navDe")) && Integer.parseInt(contextUtil.getRequest().getParameter("navDe")) > 0) {
			navDe = Integer.parseInt(contextUtil.getRequest().getParameter("navDe"));
		} else {
			log.debug( "Pesquisando pela primeira vez");
			navDe = 1;
		}

		int proxima = -1;
		int anterior = -1;
		int ultima = -1;
		PlcConfigSelection selecao = configUtil.getConfigCollaboration(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest())).selection();
		DynamicType tipoNavegador = null;
		int navNumReg = -1;
		if (selecao!=null) {
			tipoNavegador = selecao.pagination().dynamicType();
			navNumReg = selecao.pagination().numberByPage();
		}

		// jQuery - utilizado para integrar com plugin do jQuery - jqgrid
		// no parametro rows o plugin informa qual a quantidade de registros a retornar.
		if (contextUtil.getRequest().getParameter("rows")!=null)
			navNumReg = new Integer(contextUtil.getRequest().getParameter("rows"));


		if ((DynamicType.DYNAMIC_REGNUM.equals(tipoNavegador))
				|| (DynamicType.DYNAMIC_BOTH.equals(tipoNavegador))) {
			if ((entityListPlc.getDynRegNumber() != null)
					&& (Integer.parseInt(entityListPlc.getDynRegNumber()) > 0)) {
				navNumReg = Integer
				.parseInt(entityListPlc.getDynRegNumber());
			} else {
				log.debug( "Não informou número dinâmico de registros, vai ficar com configuraçao padrão");
			}
		}
		int numeroPaginaTotal = (int) Math.ceil(total.doubleValue() / navNumReg);

		int indexCombo = -1;

		if ((DynamicType.DYNAMIC_PAGE.equals(tipoNavegador))
				|| (DynamicType.DYNAMIC_BOTH.equals(tipoNavegador))) {
			if (contextUtil.getRequest().getAttribute("navComboDinamico") != null) {
				log.debug( "Navegacao por Combo");
				indexCombo = ((Integer) contextUtil.getRequest().getAttribute("navComboDinamico")).intValue();
				navDe = (1 + (indexCombo - 1) * navNumReg);
			}
		}
		log.debug( "Criando atributos para navegacao");
		int paginaCorrente = (int) Math.abs((1 - navNumReg - navDe) / navNumReg);

		// jQuery - utilizado para integrar com plugin do jQuery 
		// no parâmetro page o plugin informa qual a pagina a ser retornada
		// É necessário também reajustar registro inicial de navegação
		if (contextUtil.getRequest().getParameter("page")!=null) {
			paginaCorrente = new Integer(contextUtil.getRequest().getParameter("page"));
			navDe = (int) Math.abs(paginaCorrente*navNumReg-navNumReg+1);
		}

		if (!"1".equals(paginaCorrente)) {
			anterior = (1 + (paginaCorrente - 1 - 1) * navNumReg);
		} else {
			anterior = 1;
		}

		proxima = (1 + (paginaCorrente + 1 - 1) * navNumReg);
		ultima = (1 + (numeroPaginaTotal - 1) * navNumReg);

		Collection entityList = iocControleFacadeUtil.getFacade().findList(context, entidade, plcControleConversacao.getOrdenacaoPlc(), (navDe - 1), navNumReg);

		plcControlePaginacao.setSelecaoPaginada(selecao, url, total, proxima);
		plcControlePaginacao.setNavDe(url, navDe);
		plcControlePaginacao.setNavAnt(url, anterior);
		plcControlePaginacao.setNavProx(url, proxima);
		plcControlePaginacao.setNavUlt(url, ultima);
		plcControlePaginacao.setNumPorPagina(url, navNumReg);
		if (paginaCorrente != numeroPaginaTotal) {
			plcControlePaginacao.setNavAte(url, navDe + (navNumReg - 1));
		} else {
			plcControlePaginacao.setNavAte(url, (int) Math.abs(navDe - navDe - total));
		}

		contextUtil.getRequest().setAttribute("navPaginaCorrente", paginaCorrente);
		contextUtil.getRequest().setAttribute("navTipoNavegador", configUtil.getConfigCollaboration(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest())).selection().pagination().dynamicType());
		contextUtil.getRequest().setAttribute("navIniAtual", navDe);

		if ((DynamicType.DYNAMIC_PAGE.equals(tipoNavegador)) || (DynamicType.DYNAMIC_BOTH.equals(tipoNavegador))) {
			
			Collection<SelectItem> itensCombo = new ArrayList<SelectItem>();

			for (int i = 1; i <= numeroPaginaTotal; i++) {
				SelectItem item = new SelectItem();
				item.setLabel(String.valueOf(i));
				item.setValue(String.valueOf(i));
				itensCombo.add(item);
			}
			contextUtil.getRequest().setAttribute("navegadorDinamicoPagDireto", "S");
		}
		entityListPlc.setSelectedPage(String.valueOf(paginaCorrente));
		return entityList;
	}

	/**
	 * Recupera detalhes "por demanda"
	 */
	@SuppressWarnings("unchecked")
	public String findDetailOnDemand(Object entityPlc)  {

		try {
			
			this.entityPlc = entityPlc;
			
			if (findDetailOnDemandBefore(entityPlc) && entityPlc != null) {

				PlcEntityInstance entityPlcInstance = metamodelUtil.createEntityInstance(entityPlc);

				PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);
				
				if (StringUtils.isNotBlank(entityPlcInstance.getIdAux()) || !checkEmptyNaturalKey()){

					String detCorrPlc = getPlcRequestControl() == null ? "" :  getPlcRequestControl().getDetCorrPlc();

					if (StringUtils.isNotBlank(detCorrPlc)){

						PlcConfigDetail configDetalhe = null;

						String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
						PlcConfigDetail[] detalhes =  configUtil.getConfigAggregation(url).details();
						for(PlcConfigDetail detalhe : detalhes){
							if (detalhe.onDemand()){
								if ( detalhe.collectionName() != null && detalhe.collectionName().equals(detCorrPlc)){
									configDetalhe = detalhe;
									break;
								}
							}
						}

						if  (configDetalhe==null || configDetalhe.clazz() == null)
							throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_FIND_CLASS_ON_DEMAND, new Object[] { detCorrPlc}, true);


						context.setApiQuerySel(null);

						int numPorPagina = Integer.MAX_VALUE;
						if (configDetalhe!=null && configDetalhe.navigation()!=null)
							numPorPagina = configDetalhe.navigation().numberByPage();

						Collection detalhePaginado = iocControleFacadeUtil.getFacade(url).findListPagedDetail(context, configDetalhe, entityPlc, numPorPagina, plcControleConversacao.getOrdenacaoPlc(), 0, true);

						
						Class fieldDetail = reflectionUtil.findFieldHierarchically(entityPlc.getClass(), detCorrPlc).getType();

						// Nos dois casos, List ou Set, a ordenação provinda do Banco de Dados é mantida
						// O OrderBy definido na entidade
						
						if(fieldDetail.isAssignableFrom(List.class)) {
							propertyUtilsBean.setProperty(entityPlc, detCorrPlc, detalhePaginado);
						} else {
							ListOrderedSet detalhePaginadoOrdenado = new ListOrderedSet();
							detalhePaginadoOrdenado.addAll(detalhePaginado);
							propertyUtilsBean.setProperty(entityPlc, detCorrPlc, detalhePaginadoOrdenado);
						}
						
						// Marca que já foi inicializado, ou seja remove da lista de detalhes por demanda
						if (  plcControleConversacao!= null && plcControleConversacao.getDetalhesPorDemanda() != null){
							plcControleConversacao.getDetalhesPorDemanda().remove(detCorrPlc);
							contextUtil.getRequest().setAttribute(PlcConstants.FORM.AUTOMACAO.DETALHES.DETALHE_POR_DEMANDA, plcControleConversacao.getDetalhesPorDemanda());
						}

						// Flag que indica que tudo encerrou ok
						contextUtil.getRequest().setAttribute(PlcConstants.WORKFLOW.RECUPERA_POR_DEMANDA_ENCERROU_OK,"S");

					}

				}
			}
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseSearchMB", "findDetailOnDemand", e, log, "");
		}
		return findDetailOnDemandAfter();
	}

	/**
	 * @since jCompany 5.1 Gera página de saída do relatório.
	 */
	public String generateReport()  {

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		PlcConfigReport relatorio = configUtil.getConfigCollaboration(url).report();

		String urlRel = relatorio.urlBirt()+"?__report="+relatorio.reportFile();

		try {

			Map<String, Object> parameters = getSearchParameters();

			if (generateReportBefore(urlRel,parameters)) {

				Set<String> mapaKeys = parameters.keySet();
				for (String nomeParametro : mapaKeys ) {
					urlRel = urlRel+"&"+nomeParametro+"="+URLEncoder.encode(parameters.get(nomeParametro).toString(), "ISO-8859-1");
				}
	
				urlRel = generateReportCompleteUrlApi(urlRel);			
	
				FacesContext facesContext  = FacesContext.getCurrentInstance();
				if (! facesContext.getResponseComplete()){
					contextUtil.getResponse().reset();
					contextUtil.getResponse().sendRedirect(urlRel);
					FacesContext.getCurrentInstance().responseComplete();
				}			
			}
			
		} catch(PlcException plcE){
			throw plcE;			
		} catch (IOException e) {
			throw new PlcException("PlcBaseSearchMB", "generateReport", e, log, "");
		}

		return generateReportAfter(urlRel);
	}
	
	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param urlRel URL do relatório
	 * @param parameters Parametros do relatório
	 */
	protected boolean generateReportBefore(String urlRel, Map<String, Object> parameters)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcGenerateReportAfter>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param urlRel 
	 */
	protected String generateReportAfter(String urlRel)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcGenerateReportAfter>(){});
		return defaultNavigationFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Api" são destinados a especializações nos
	 * descendentes via Template Methods 
	 */
	protected String generateReportCompleteUrlApi(String url)  {
		return url;
	}	

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param entityPlc 
	 */
	protected boolean findDetailOnDemandBefore(Object entityPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcFindDetailOnDemandBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected String findDetailOnDemandAfter()  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcFindDetailOnDemandAfter>(){});
		return defaultNavigationFlow;
	}

	/**
	 * Verifica se uma chave natural está vazia
	 */
	protected boolean checkEmptyNaturalKey() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		boolean vazio = false;

		PlcPrimaryKey chavePrimaria = entityPlc.getClass().getAnnotation(PlcPrimaryKey.class);
		if (chavePrimaria != null){
			Object idNatural = propertyUtilsBean.getProperty(entityPlc, "idNatural");
			String[] propriedades = chavePrimaria.propriedades();
			for (String prop : propriedades) {
				Object valor = propertyUtilsBean.getProperty(idNatural, prop);
				if (valor == null)
					vazio = true;
			}
		}
		
		return false;
	}
	
	/**
	 * TODO Verificar se está sendo utilizado
	 */
	public String  navigationNext (Object entityPlc) {
		
		
		return defaultNavigationFlow;
	}


	/**
	 * Método navegação de detalhe, para avanço de páginas
	 * Baseia a navegação no detalhe corrente registrado no campo detCorrPlcPaginado
	 */
	public String  navigationDetailNext (Object entityPlc) {

		try {
			
			String detCorrPlcPaginado = controleDetalhePaginadoPlc.getDetCorrPlcPaginado();

			if (detCorrPlcPaginado == null)
				detCorrPlcPaginado = contextUtil.getRequestParameter("detCorrPlcPaginado");

			// Acerto para funcionar sem AJAX
			if (StringUtils.isNotBlank(detCorrPlcPaginado))
				detCorrPlcPaginado = detCorrPlcPaginado.split("#")[0];

			//Configuração do detalhe corrente
			PlcConfigDetail configDetalhe = this.controleDetalhePaginadoPlc.getDetalhePorNome(detCorrPlcPaginado);

			PlcBaseContextVO context = getContext();
			if (context == null)
				context = contextMontaUtil.createContextParam(plcControleConversacao);

			int posicaoAtual = this.controleDetalhePaginadoPlc.getPosicaoAtual(detCorrPlcPaginado);
			int numPorPagina = configDetalhe.navigation().numberByPage();
			long numTotalRegistros = this.controleDetalhePaginadoPlc.getNumTotalRegistros(detCorrPlcPaginado);

			posicaoAtual = posicaoAtual + numPorPagina;

			// Se o numero total de registros for menor que a posicao atual, não faz pesquisa
			if (numTotalRegistros > posicaoAtual){

				String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
				IPlcFacade plx = (IPlcFacade)iocControleFacadeUtil.getFacade(url);
				Collection detalhePaginado = plx.findListPagedDetail(context, configDetalhe, entityPlc, numPorPagina, null, posicaoAtual, true);

				propertyUtilsBean.setProperty(entityPlc, detCorrPlcPaginado, detalhePaginado);

				this.controleDetalhePaginadoPlc.setPosicaoAtual(detCorrPlcPaginado, posicaoAtual);

			}

			contextUtil.setRequestAttribute(PlcJsfConstantes.PLC_LIMPA_CAIXA_EXCLUSAO, true);
			
			// Registra no request que o detalhe possui paginação
			contextUtil.setRequestAttribute(PlcJsfConstantes.PLC_NOME_COLECAO_DETALHE_POSSUI_PAGINACAO + configDetalhe.collectionName() , "S");

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseSearchMB", "navigationDetailNext", e, log, "");
		}

		return defaultNavigationFlow;
	}

	/**
	 * Método navegação de detalhe, para regreção de páginas
	 * Baseia a navegação no detalhe corrente registrado no campo detCorrPlcPaginado
	 */
	public String navigationDetailPrevious (Object entityPlc) {

		try {
			//FIXME - Ver porque nao esta funcionando
			String detCorrPlcPaginado = controleDetalhePaginadoPlc.getDetCorrPlcPaginado();

			if (detCorrPlcPaginado == null)
				detCorrPlcPaginado = contextUtil.getRequestParameter("detCorrPlcPaginado");

			// Acerto para funcionar sem AJAX
			if (StringUtils.isNotBlank(detCorrPlcPaginado))
				detCorrPlcPaginado = detCorrPlcPaginado.split("#")[0];

			PlcConfigDetail configDetalhe = this.controleDetalhePaginadoPlc.getDetalhePorNome(detCorrPlcPaginado);

			PlcBaseContextVO context = getContext();
			if (context == null)
				context = contextMontaUtil.createContextParam(plcControleConversacao);

			int posicaoAtual = this.controleDetalhePaginadoPlc.getPosicaoAtual(detCorrPlcPaginado);
			int numPorPagina = configDetalhe.navigation().numberByPage();

			posicaoAtual = posicaoAtual - numPorPagina;

			if (posicaoAtual < 0)
				posicaoAtual = 0;

			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			IPlcFacade plx = (IPlcFacade)iocControleFacadeUtil.getFacade(url);
			Collection detalhePaginado = plx.findListPagedDetail(context, configDetalhe, entityPlc, numPorPagina, null, posicaoAtual, true);

			propertyUtilsBean.setProperty(entityPlc, detCorrPlcPaginado, detalhePaginado);

			this.controleDetalhePaginadoPlc.setPosicaoAtual(detCorrPlcPaginado, posicaoAtual);

			contextUtil.setRequestAttribute(PlcJsfConstantes.PLC_LIMPA_CAIXA_EXCLUSAO, true);
			
			contextUtil.setRequestAttribute(PlcJsfConstantes.PLC_NOME_COLECAO_DETALHE_POSSUI_PAGINACAO + configDetalhe.collectionName() , "S");

		} catch(PlcException plcE){
			throw plcE;				
		} catch (Exception e) {
			throw new PlcException("PlcBaseSearchMB", "navigationDetailPrevious", e, log, "");
		}
		return defaultNavigationFlow;
	}


	/**
	 * Método navegaçao de detalhe, vai para a primeira página
	 * Baseia a navegação no detalhe corrente registrado no campo detCorrPlcPaginado
	 */
	public String navigationDetailFirst (Object entityPlc) {

		try {
			String detCorrPlcPaginado = controleDetalhePaginadoPlc.getDetCorrPlcPaginado();

			if (detCorrPlcPaginado == null)
				detCorrPlcPaginado = contextUtil.getRequestParameter("detCorrPlcPaginado");

			// Acerto para funcionar sem AJAX
			if (StringUtils.isNotBlank(detCorrPlcPaginado))
				detCorrPlcPaginado = detCorrPlcPaginado.split("#")[0];

			PlcConfigDetail configDetalhe = this.controleDetalhePaginadoPlc.getDetalhePorNome(detCorrPlcPaginado);

			PlcBaseContextVO context = getContext();
			if (context == null) {
				context = contextMontaUtil.createContextParam(plcControleConversacao);
			}

			int numPorPagina = configDetalhe.navigation().numberByPage();
			int posicaoAtual = 0;

			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			IPlcFacade plx = (IPlcFacade)iocControleFacadeUtil.getFacade(url);
			Collection detalhePaginado = plx.findListPagedDetail(context, configDetalhe, entityPlc, numPorPagina, null, posicaoAtual, true);

			propertyUtilsBean.setProperty(entityPlc, detCorrPlcPaginado, detalhePaginado);

			this.controleDetalhePaginadoPlc.setPosicaoAtual(detCorrPlcPaginado, posicaoAtual);

			contextUtil.setRequestAttribute(PlcJsfConstantes.PLC_LIMPA_CAIXA_EXCLUSAO, true);
			
			contextUtil.setRequestAttribute(PlcJsfConstantes.PLC_NOME_COLECAO_DETALHE_POSSUI_PAGINACAO + configDetalhe.collectionName() , "S");

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseSearchMB", "navigationDetailFirst", e, log, "");
		}

		return defaultNavigationFlow;
	}

	/**
	 * Método navegação de detalhe, vai para ultima página
	 * Baseia a navegação no detalhe corrente registrado no campo detCorrPlcPaginado
	 */
	public String navigationDetailLast (Object entityPlc) {

		try {

			String detCorrPlcPaginado = controleDetalhePaginadoPlc.getDetCorrPlcPaginado();

			if (detCorrPlcPaginado == null)
				detCorrPlcPaginado = contextUtil.getRequestParameter("detCorrPlcPaginado");

			// Acerto para funcionar sem AJAX
			if (StringUtils.isNotBlank(detCorrPlcPaginado))
				detCorrPlcPaginado = detCorrPlcPaginado.split("#")[0];

			PlcConfigDetail configDetalhe = this.controleDetalhePaginadoPlc.getDetalhePorNome(detCorrPlcPaginado);

			int numPorPagina = configDetalhe.navigation().numberByPage();

			PlcBaseContextVO context = getContext();
			if (context == null) {
				context = contextMontaUtil.createContextParam(plcControleConversacao);
			}
			
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());			
			IPlcFacade plx = (IPlcFacade)iocControleFacadeUtil.getFacade(url);

			int numTotalPagina = this.controleDetalhePaginadoPlc.getNumTotalPaginas(detCorrPlcPaginado);

			int posicaoAtual = numTotalPagina * numPorPagina - numPorPagina;

			if (posicaoAtual < 0)
				posicaoAtual = 0;

			Collection detalhePaginado = plx.findListPagedDetail(context, configDetalhe, entityPlc, numPorPagina, null, posicaoAtual, true);

			propertyUtilsBean.setProperty(entityPlc, detCorrPlcPaginado, detalhePaginado);

			this.controleDetalhePaginadoPlc.setPosicaoAtual(detCorrPlcPaginado, posicaoAtual);

			contextUtil.setRequestAttribute(PlcJsfConstantes.PLC_LIMPA_CAIXA_EXCLUSAO, true);
			
			contextUtil.setRequestAttribute(PlcJsfConstantes.PLC_NOME_COLECAO_DETALHE_POSSUI_PAGINACAO + configDetalhe.collectionName() , "S");

		} catch(PlcException plcE){
			throw plcE;				
		} catch (Exception e) {
			throw new PlcException("PlcBaseSearchMB", "navigationDetailLast", e, log, "");
		}

		return defaultNavigationFlow;
	}

	public String navigationToPage (ValueChangeEvent event, Object entityPlc) {
		contextUtil.setRequestAttribute("novaPagina", event.getNewValue());
		return navigationToPage(entityPlc);
	}

	/**
	 * Acionado pelo comboBox na página de navegação.
	 * Baseia a navegação no detalhe corrente registrado no campo detCorrPlcPaginado
	 */
	public String navigationToPage (Object entityPlc) {

		try {

			String detCorrPlcPaginado = controleDetalhePaginadoPlc.getDetCorrPlcPaginado();

			Integer _paginaAtual = (Integer)contextUtil.getRequestAttribute("novaPagina");

			if (detCorrPlcPaginado == null)
				detCorrPlcPaginado = contextUtil.getRequestParameter("detCorrPlcPaginado");

			// Acerto para funcionar sem AJAX

			if (detCorrPlcPaginado.indexOf("#")>0 ) {
				_paginaAtual = new Integer((detCorrPlcPaginado.substring(detCorrPlcPaginado.indexOf("#")+1, detCorrPlcPaginado.length())))+1;
				detCorrPlcPaginado = detCorrPlcPaginado.substring(0, detCorrPlcPaginado.indexOf("#"));
			}

			PlcConfigDetail configDetalhe = this.controleDetalhePaginadoPlc.getDetalhePorNome(detCorrPlcPaginado);

			PlcBaseContextVO context = getContext();
			if (context == null)
				context = contextMontaUtil.createContextParam(plcControleConversacao);

			int numPorPagina = configDetalhe.navigation().numberByPage();

			Integer paginaAtual = (Integer)_paginaAtual;
			if (paginaAtual != null){

				int posicaoAtual = paginaAtual * numPorPagina - numPorPagina;

				String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
				IPlcFacade plx = (IPlcFacade)iocControleFacadeUtil.getFacade(url);
				Collection detalhePaginado = plx.findListPagedDetail(context, configDetalhe, entityPlc, numPorPagina, null, posicaoAtual, true);

				propertyUtilsBean.setProperty(entityPlc, detCorrPlcPaginado, detalhePaginado);
				this.entityPlc = entityPlc;
				this.controleDetalhePaginadoPlc.setPosicaoAtual(detCorrPlcPaginado, posicaoAtual);
			}
			
			contextUtil.setRequestAttribute(PlcJsfConstantes.PLC_LIMPA_CAIXA_EXCLUSAO, true);
			contextUtil.setRequestAttribute(PlcJsfConstantes.PLC_NOME_COLECAO_DETALHE_POSSUI_PAGINACAO + configDetalhe.collectionName() , "S");

		} catch(PlcException plcE){
			throw plcE;					
		} catch (Exception e) {
			throw new PlcException("PlcBaseSearchMB", "navigationToPage", e, log, "");
		}

		return defaultNavigationFlow;
	}

	/**
	 * Recupera para Combo Aninhado no onChange do componente
	 * 
	 * @param Evento disparado pelo Change do componente selectOneChoice.
	 * @return Página que será renderizada.
	 */
	public String findNavigationNestedCombo(ValueChangeEvent value)  {

		if (findNavigationNestedComboBefore(value)) {
			
			retrieveNestedComboPaged(value,entityPlc);

		}
		return findNavigationNestedComboAfter(value);
	}
	
	/**
	 * Recupera a navegação para Combo Aninhado no onChange do componente
	 * 
	 * @param Evento disparado pelo Change do componente selectOneChoice.
	 * @return Página que será renderizada.
	 */
	protected void retrieveNestedComboPaged(ValueChangeEvent value,Object entityPlc) {

		PlcConfigAggregationPOJO configAggregationPOJO = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
		
		if (value != null ) {
			
			try {
				// Recuperando a referência do request
				HttpServletRequest request = contextUtil.getRequest();
				// montando um contexParam
				contextMontaUtil.createContextParam(null);
				PlcBaseContextVO context = entityUtil.getContext(request);

				// Recuperando o VO atual do combo principal
				Object novaEntidadeOrigem = value.getNewValue();

				PlcDomainLookupUtil dominioLookupUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcDomainLookupUtil.class, new PlcNamedLiteral(PlcJsfConstantes.PLC_DOMINIOS));

				/*
				 * Recuperando a lista de propriedades que serão atualizadas e
				 * recuperando a lista de VO's para cada uma delas e coloca no
				 * scopo de conversação
				 */
				String navegacaoParaCampo 	= (String) value.getComponent().getAttributes().get("navegacaoParaCampos");
				String campos[] 			= navegacaoParaCampo.split(",");
				Class entity				= null;
				// percorrendo a lista de compos informados
				for (String campo : campos) {

					// montando o nome da lista "dominio" que ficará em conversação
					String nomeDominio 	= campo;
					// Atualizando lista no scopo de conversação
					if (configAggregationPOJO.pattern().formPattern().equals(FormPattern.Smd) || configAggregationPOJO.pattern().formPattern().equals(FormPattern.Sel) || configAggregationPOJO.pattern().formPattern().equals(FormPattern.Con)) {
						// Coloca como prefixo o "argumentos" para seleção e seleção de mantem detalhe 
						nomeDominio 	=  PlcConstants.PlcJsfConstantes.PLC_LOGICA_ITENS_ARGUMENTO +"."+  nomeDominio.replace(".", "_");  
					}


					Class classeDestino 	= null;
					Map mapaItens 			= (Map) contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_ITENS_STATUS);
					/*
					 * Verificando se o componente comboDinamico aninhado é de um detalhe, subdetalhe ou tabular
					 * Se o mapaItens não estiver null é porque é de uma destas opções, se estiver porque é do mestre ou de uma crud ..
					 */ 
					String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
					if (mapaItens != null) {
						Object entityDoComponente  	= mapaItens.get("current");
						entity 						= entityDoComponente.getClass();
						nomeDominio					= mapaItens.get("colecaoNome") + "[" + mapaItens.get("index")  +"]"  + nomeDominio;   
					} else {
						entity  	= configUtil.getConfigAggregation(url).entity();
					}

					
					PlcConfigCollaborationPOJO _configControle = configUtil.getConfigCollaboration(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
					PlcConfigNestedCombo[] comboAninhado = _configControle.nestedCombo();
					Object valorPropComboAninhado =  value.getComponent().getAttributes().get("comboAninhado");
					
					String comboAnterior = entityUtil.getPreviewsCombo (configAggregationPOJO, dominioLookupUtil, campo, comboAninhado);
					
					if (mapaItens != null) {
						comboAnterior = mapaItens.get("colecaoNome") + "[" + mapaItens.get("index")  +"]"  + comboAnterior;   
					}
					
					comboAnterior = dominioLookupUtil.createDomainNameToConversation(configAggregationPOJO.entity(), comboAnterior);
					List dominioAnterior = dominioLookupUtil.getDomainConversation(comboAnterior);
					
					// Necessário a instanciação da lista
					Collection listaNavegacao = new ArrayList<Object>();
					if (novaEntidadeOrigem != null) {
						if ((valorPropComboAninhado == null || valorPropComboAninhado.toString().equals("N")) || (dominioAnterior != null && !dominioAnterior.isEmpty())){
							if (dominioAnterior == null || dominioAnterior.size() == 0 || entityUtil.isInList(dominioAnterior, novaEntidadeOrigem)){
								PlcEntityInstance novoVOOrigemInstance = metamodelUtil.createEntityInstance(novaEntidadeOrigem);
								classeDestino = annotationUtil.getClassManyToOne(entity,campo);
								listaNavegacao 	  = getServiceFacade(url).findNavigation(context,novaEntidadeOrigem.getClass(),novoVOOrigemInstance.getId(), classeDestino);
							}
						}
					}
					// Código para retirada dos valores dos combos aninhados dependentes desse
					
					entityUtil.removeRecursiveOptions(configAggregationPOJO, dominioLookupUtil, campo, comboAninhado, mapaItens);
					
					/*
					 * Montando o nome do dominio. Chave do mapa para a lista de objetos que ficar'a no escopo de conversação
					 * Exemplos de nomes de dominio:
					 * Crud ou Mestre detalhe: DepartamentoEntityLookupendereco.cidade
					 * Detalhe: DepartamentoEntityLookupargumentos.endereco_cidade
					 */
					nomeDominio 		= dominioLookupUtil.createDomainNameToConversation(configAggregationPOJO.entity(), nomeDominio);
					//Coloca a nova lista no escopo de conversacao	
					dominioLookupUtil.addDomainConversation(nomeDominio, ((List) listaNavegacao));

				}
			} catch(PlcException plcE){
				throw plcE;	
			} catch (Exception e) {
				throw new PlcException("PlcEntityUtil", "retrieveNestedComboPaged", e, log, "");
			}
		}

	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected boolean findNavigationNestedComboBefore(ValueChangeEvent value)
	{
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcFindNavigationNestedComboBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected String findNavigationNestedComboAfter(ValueChangeEvent value)
	{
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcFindNavigationNestedComboAfter>(){});
		return defaultNavigationFlow;
	}

	
	/**
	 * Recupera combo aninhado para cada item da coleção da Tabular ou CrudTabular
	 */
	protected void retrieveNestedComboTabular(Collection listaVO) {
		int index =0;
		for(Object entity : listaVO){
			nestedComboUtil.retrieveNestedCombo(entity,index,false);
			index++;
		}
	}

	/**
	 * Efetua a auto recuperaçao para o id ou Chave Naturais Informados no componente vinculado.
	 * A Propriedade autoRecuperacaoClasse deve ser informada para que o valueChangeListener do componente seja
	 *  configurado automaticamente pelo jCompany.
	 */
	@SuppressWarnings("unchecked")
	public String autofindAggregate(ValueChangeEvent valueChangeEvent) {

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		PlcConfigCollaborationPOJO configControle = configUtil.getConfigCollaboration(url);

		if (autofindAggregateBefore(valueChangeEvent)) {

			String clientId = valueChangeEvent.getComponent().getClientId(FacesContext.getCurrentInstance());
			contextUtil.getRequest().setAttribute(PlcJsfConstantes.ACAO.PLC_IND_AUTO_RECUPERACAO+clientId,clientId);
		}
		
		return autofindAggregateAfter(valueChangeEvent);

	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected boolean autofindAggregateBefore(ValueChangeEvent valueChangeEvent) {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcAutofindAggregateBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */	
	protected String autofindAggregateAfter(ValueChangeEvent valueChangeEvent) {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcAutofindAggregateAfter>(){});
		return defaultNavigationFlow;
	}

	/**
	 * Limpa todos os argumentos da pesquisa.
	 */
	public String clearArgs(Object entityPlc)  {

		this.entityPlc = entityPlc;
		if (contextUtil.getRequest().getAttribute(PlcConstants.ACAO.EVENTO)==null)
			contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_LIMPAR);

		PlcConfigCollaborationPOJO configAcaoControle = configUtil.getConfigCollaboration(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));

		if (configAcaoControle.selection()!=null && configAcaoControle.selection().pagination().numberByPage() > 0) {
			String idNavegador = contextUtil.getRequest().getPathInfo().substring( contextUtil.getRequest().getPathInfo().lastIndexOf("/")).substring(1) + "Nav";
			contextUtil.getRequest().getSession().removeAttribute(idNavegador + PlcConstants.GUI.NAVEGADOR.PORTLET_NAV_TOT_REG);
			contextUtil.getRequest().getSession().removeAttribute(idNavegador + PlcConstants.GUI.NAVEGADOR.PORTLET_NAV_DE);
			contextUtil.getRequest().getSession().removeAttribute(idNavegador + PlcConstants.GUI.NAVEGADOR.PORTLET_NAV_ATE);
			contextUtil.getRequest().getSession().removeAttribute(idNavegador + PlcConstants.GUI.NAVEGADOR.PORTLET_NAV_INI_PROX);
			contextUtil.getRequest().getSession().removeAttribute(idNavegador + PlcConstants.GUI.NAVEGADOR.PORTLET_NAV_INI_ANT);
			contextUtil.getRequest().getSession().removeAttribute(idNavegador + PlcConstants.GUI.NAVEGADOR.PORTLET_NAV_INI_FIM);
			contextUtil.getRequest().getSession().removeAttribute(PlcConstants.GUI.NAVEGADOR.PORTLET_NAV_INI + idNavegador);
			contextUtil.getRequest().getSession().removeAttribute(idNavegador + "Arg");
		}

		if (clearArgsBefore(entityPlc,configAcaoControle)) {

			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			FormPattern pattern = configUtil.getConfigAggregation(url).pattern().formPattern();
			// Apaga variaveis de conversacao
			plcControleConversacao.setOrdenacaoPlc("");
	
			if (entityListPlc != null){
				if ( FormPattern.Ctb.equals(pattern)){
					entityListPlc.setItensPlc(new ArrayList<Object>());
					plcAction.create();
				}
				else
					entityListPlc.setItensPlc(null);
			}
			
			plcAction.create();
		
		}
		
		return clearArgsAfter(entityListPlc,entityPlc,configAcaoControle);
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param configAcaoControle Metadados para seleção
	 * @param entityPlc Entidade
	 */
	protected boolean clearArgsBefore(Object entityPlc, PlcConfigCollaborationPOJO configAcaoControle)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcClearArgsBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param configAcaoControle Metadados
	 * @param entityPlc Entidade criada após limpeza
	 * @param entityListPlc Lista
	 */
	protected String clearArgsAfter(PlcEntityList entityListPlc, Object entityPlc, PlcConfigCollaborationPOJO configAcaoControle)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcClearArgsAfter>(){});
		return defaultNavigationFlow;
	}

	/**
	 * @return Interface para acesso a camada de persistencia.
	 */
	protected IPlcFacade getServiceFacade(String colaboracao)  {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcIocControllerFacadeUtil.class, QPlcDefaultLiteral.INSTANCE).getFacade(colaboracao);
	}
	


}
