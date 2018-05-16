/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.comparator.PlcComparatorId;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.PlcBeanCloneUtil;
import com.powerlogic.jcompany.commons.util.PlcDateUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigComponent;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.bindingtype.PlcEditAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcEditBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcEditDocumentViewAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcEditFindAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcEditFindBefore;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcPagedDetailControl;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcEntityUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcNestedComboUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcNaturalKeyUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

@QPlcDefault
public class PlcBaseEditMB extends PlcBaseParentMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected transient Logger log;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	
	
	/**
	 * Controller para detalhes paginados e argumentos em detalhes
	 */
	@Inject @Named(PlcJsfConstantes.PLC_CONTROLE_DETALHE_PAGINADO)
	protected PlcPagedDetailControl controleDetalhePaginadoPlc;

	@Inject @QPlcDefault 
	protected PlcBeanCloneUtil beanCloneUtil;	

	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject @QPlcDefault 
	protected PlcEntityUtil entityUtil;
	
	@Inject @QPlcDefault 
	protected PlcNestedComboUtil nestedComboUtil;

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;

	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil;	

	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;

	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;	

	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;	

	@Inject @QPlcDefault 
	protected PlcMsgUtil msgUtil;

	@Inject @QPlcDefault 
	protected PlcDateUtil dateUtil;

	@Inject @QPlcDefault 
	protected PlcNaturalKeyUtil chaveNaturalUtil;

	@Inject @Named("plcAction")
	protected PlcBaseMB plcAction; 	
	
	@Inject @QPlcSpecific
	protected PlcBaseCreateMB baseCreateMB; 
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	/**
	 * Recupera um grafo da entidade principal e disponibiliza para o formulario de visão
	 */
	public String edit(Object entityPlc)  {

		this.entityPlc = entityPlc;
		
		if(!checkExistsKey()){
			return defaultNavigationFlow;
		}
		
		if (contextUtil.getRequest().getAttribute(PlcConstants.ACAO.EVENTO)==null)
			contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_EDITAR);

		try {
		
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());

			plcControleConversacao.setModoPlc(PlcConstants.MODOS.MODO_EDICAO);

			contextMontaUtil.createContextParam(plcControleConversacao);
			PlcBaseContextVO context = (PlcBaseContextVO)contextUtil.getRequest().getAttribute(PlcConstants.CONTEXT);
						
			if(editBefore(entityPlc,context)) {
	
				PlcBeanMessages msgNaoEncontrado = PlcBeanMessages.JCOMPANY_WARNING_SEL_NOT_FOUND;
				
				context.setDeleteModeAux(configUtil.getConfigAggregation(url).pattern().exclusionMode().toString());
	
				if (context.getDetailNamesProps()!=null && context.getDetailNamesProps().size()>0){
					mountDetailComparator(context);
				}
	
				Object[] ret = editFind();
	
				if (ret[0] == null) { // Registro não foi encontrado
	
					msgUtil.msg(msgNaoEncontrado, new Object[] {}, PlcMessage.Cor.msgVermelhoPlc.toString());
					
					plcAction.create();
	
				} else {
										
					contextUtil.getRequest().setAttribute(PlcConstants.PlcJsfConstantes.PLC_SUFIXO_ENTIDADE_CORRENTE, ret[0].toString());
					
					if (entityPlc==null){
						entityPlc = ret[0].getClass().newInstance();
					}
					
					beanCloneUtil.copyProperties(entityPlc, ret[0], true);
	
					PlcConfigComponent [] componentes = configUtil.getConfigAggregation(url).components();
	
					if (componentes != null){
						for (PlcConfigComponent configComponente : componentes) {
							String propriedade = configComponente.property();
							if (StringUtils.isNotBlank(propriedade)){
								Object componente = propertyUtilsBean.getProperty(entityPlc, propriedade);
								if (componente == null)
									propertyUtilsBean.setProperty(entityPlc, propriedade, configComponente.clazz().newInstance());
							}
						}
					}
	
					/*
					 * Recuperando lista de combos aninhados configurados na anotação de pacote do caso de uso. 
					 * Esta solução foi para contornar problema de serialização em chamada remota. 
					 */
					nestedComboUtil.retrieveNestedCombo(entityPlc,-1,false);
	
					// Se for padrao mantém-detalhes, aciona
					if ((FormPattern.Mad.equals(configUtil.getConfigAggregation(url).pattern().formPattern()) ||
							FormPattern.Mas.equals(configUtil.getConfigAggregation(url).pattern().formPattern())) && !hasDetail())
						editAddDetails(entityPlc);
	
				}
	
				contextUtil.getRequest().setAttribute(PlcConstants.VO.PREFIXO_OBJ + configUtil.getConfigAggregation(url).entity().getName(), entityPlc);
				
				this.entityPlc = entityPlc;
			}
			
			//limpando imagens temporárias da sessão
			baseCreateMB.clearFileAttachInSession(entityPlc, true);
			
			return editAfter(entityPlc,context);

		} catch (PlcException plcE) {
			if (plcE.getMessage() != null && plcE.getMessage().equals("jcompany.erros.registro.nao.encontrado")) {
				msgUtil.msgError(PlcBeanMessages.JCOMPANY_ERROR_ITEM_NOT_FOUND, new Object[] { this.id });
				return plcAction.create();
			} else
				throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBaseEditMB", "edit", e, log, "");
		}

	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param context Parametros de controle da requisiçao
	 * @param entityPlc Entidade principal
	 */
	protected boolean editBefore(Object entityPlc, PlcBaseContextVO context)  {
		this.setDefaultProcessFlow(true);
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcEditBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param context Parametros de controle da requisiçao
	 * @param entityPlc Entidade principal
	 */
	protected String editAfter(Object entityPlc, PlcBaseContextVO context)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcEditAfter>(){});
		return defaultNavigationFlow;
	}

	/**
	 * Monta HashMap com detalhes e seus respectivos comparators e armazena no context.
	 * @param context
	 */
	protected void mountDetailComparator(PlcBaseContextVO context)  {
		
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		PlcConfigDetail [] detalhes = configUtil.getConfigAggregation(url).details();
		Map detalheComparator = context.getDetalheComparator();
		
		for (PlcConfigDetail detalhe: detalhes){
		
			if(detalhe.clazz().getAnnotation(com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey.class)!=null && detalhe.comparator().isAssignableFrom(PlcComparatorId.class))
				detalheComparator.put(detalhe.collectionName(), null);
			else
				detalheComparator.put(detalhe.collectionName(), detalhe.comparator());

			if (StringUtils.isNotBlank(detalhe.subDetail().collectionName())){
				if(detalhe.subDetail().clazz().getAnnotation(com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey.class)!=null && detalhe.subDetail().comparator().isAssignableFrom(PlcComparatorId.class))
					detalheComparator.put(detalhe.collectionName()+"_"+detalhe.subDetail().collectionName(), null);
				else
					detalheComparator.put(detalhe.collectionName()+"_"+detalhe.subDetail().collectionName(), detalhe.subDetail().comparator());
			}
		}
		
		context.setDetalheComparator(detalheComparator);
	
	}


	/**
	 * Recupera um Value Object da camada de persistência pelo OID, considerando
	 * filtros verticais declarados em AppPerfilUsuarioBO.
	 * 
	 * @param msg Chave para mensagem a ser exibida adicionalmente,caso o registro não seja encontrado
	 * @return VO recuperado
	 */
	protected Object[] editFind()  {

		Object[] ret = new Object[3];

		try {

			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			
			PlcBaseContextVO context = (PlcBaseContextVO)contextUtil.getRequest().getAttribute(PlcConstants.CONTEXT);
			PlcPrimaryKey chavePrimaria = (PlcPrimaryKey) configUtil.getConfigAggregation(url).entity().getAnnotation(PlcPrimaryKey.class);
			if (chavePrimaria!=null)
				context.setPkClass(chavePrimaria.classe().getName());
			else
				context.setPkClass(null);

			IPlcFacade facade = iocControleFacadeUtil.getFacade(url);
			
			if (editFindBefore(context,url,facade)) {

				Boolean possuiDetalhePaginado = (Boolean)contextUtil.getRequest().getAttribute(PlcJsfConstantes.DETALHE_PAGINADO);
				Boolean possuiDetalheFiltro = (Boolean)contextUtil.getRequest().getAttribute(PlcJsfConstantes.DETALHE_FILTRO);

				Object pk = null;
				if (this.id != null && NumberUtils.isNumber(this.id)) {
					pk = new Long(this.id);
				} else if (chavePrimaria!=null) {
					//Primeiro tenta buscar do request.
					Object idNatural = null;

					//Primeiro verifica se tem a chave natural no request.
					if (chaveNaturalUtil.hasKeyProperty(chavePrimaria, contextUtil.getRequest())) {
						idNatural = chaveNaturalUtil.mountNaturalKeyByRequest(chavePrimaria, contextUtil.getRequest());
					} else if (entityPlc!=null) {
						//Se não tem no request, busca da entidade corrente.
						PlcEntityInstance entityPlcInstance = metamodelUtil.createEntityInstance(entityPlc);
						idNatural = entityPlcInstance.getIdNaturalDinamico();
					}
					if (idNatural!=null) {
						pk = idNatural;
					}
				}

				//Se não conseguiu determinar a chave, considera que é id.
				if (pk==null) {
					pk = this.id;
				}

				/**
				 * Inicio: Detalhe paginado, Backlog: 62169
				 * Inclusão do código para paginação em detalhes. Esse código impede a recuperação de todos os detalhes quando está utilizando detalhe paginado.
				 */
				if (possuiDetalhePaginado == null && possuiDetalheFiltro == null){
				
					ret = facade.edit(context, configUtil.getConfigAggregation(url).entity(), pk);

				} else {

					// Implementação para recuperação de detalhe paginado
					PlcConfigDetail[] detalhes = configUtil.getConfigAggregation(url).details();

					Object mestre = configUtil.getConfigAggregation(url).entity().newInstance();
					PlcEntityInstance mestreInstance = metamodelUtil.createEntityInstance(mestre);
					
					
					if (metamodelUtil.isEntityClass(pk.getClass())){
						mestreInstance.setIdNatural(pk);
					} else if(chavePrimaria!=null && chavePrimaria.classe().equals(pk.getClass())){
						mestreInstance.setIdNatural(pk);
					} else {
						if (pk instanceof Long){
							mestreInstance.setId((Long)pk);
						} else {
							throw new PlcException("Nao foi possivel identificar o tipo da chave primaria");
						}
					}

					for (PlcConfigDetail configDetalhe : detalhes) {

						int numPorPagina = configDetalhe.navigation().numberByPage();
						if (numPorPagina > -1){

							//Recupera total de detalhes para este mestre
							Object detalhe = configDetalhe.clazz().newInstance();
							String atributoMestre = reflectionUtil.getFieldHierarchicallyByType(configDetalhe.clazz(), mestre.getClass());
							propertyUtilsBean.setProperty(detalhe, atributoMestre, mestre);
							Long numTotalRegistros = facade.findCount(context, detalhe);
							
							if (controleDetalhePaginadoPlc == null) {
								controleDetalhePaginadoPlc = PlcCDIUtil.getInstance().getInstanceByType(PlcPagedDetailControl.class, new PlcNamedLiteral(PlcJsfConstantes.PLC_CONTROLE_DETALHE_PAGINADO));
							}
							// Guarda o numero total de detalhes recuperados
							controleDetalhePaginadoPlc.setDetalhePaginado(configDetalhe, numTotalRegistros);

						}			
					}

					// Recupera mestre com os detalhes já paginados
					ret = facade.findObjectPagedDetail(context, configUtil.getConfigAggregation(url).entity(), pk, new Long (0), null, detalhes);

				}

			}
			
			return editFindAfter(ret);
			
		} catch (PlcException plcE) {
			getParameterValues (entityPlc);
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBaseEditMB", "editFind", e, log, "");
		}

	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected Object[] editFindAfter(Object[] ret)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcEditFindAfter>(){});
		return ret;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param facade fachada para serviços de negócio
	 * @param url URL original
	 * @param context Parametros de controle da requisição
	 */
	protected boolean editFindBefore(PlcBaseContextVO context, String url, IPlcFacade facade)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcEditFindBefore>(){});
		return defaultProcessFlow;
	}


	/**
	 * Adiciona linhas em  branco em detalhes, para padrao "Consulta-Mestre/Mantém-Detalhe"
	 */
	protected void editAddDetails(Object entityPlc)  {
		// TODO Para "entradaEmLote", já trazer linhas em branco adicionadas, mesmo em detalhes que possuem ocorrencias.
		plcAction.create();

	}

	/**
	 * Método verifica se entidade possui detalhe
	 * @return true se existe detalhe
	 *  
	 */
	protected boolean hasDetail () {

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		PlcConfigDetail [] detalhes = configUtil.getConfigAggregation(url).details();
		boolean temDetalhe = false;
		try{
			for (PlcConfigDetail detalhe: detalhes){
				Collection colecaoDetalhes = (Collection)propertyUtilsBean.getProperty(entityPlc, detalhe.collectionName());
				if (colecaoDetalhes != null && !colecaoDetalhes.isEmpty())
					temDetalhe = true;
			}	
		}catch (Exception e) {
			e.printStackTrace();
		}

		return temDetalhe;
	}

	/**
	 * Método para recuperar os parâmetros quando utilizado chaveNatural
	 * 
	 */
	protected void getParameterValues(Object entityPlc)  {

		try {

			Map parameter = contextUtil.getRequest().getParameterMap();	

			String [] evento = (String [])parameter.get("evento");

			if (evento != null && "y".equals(evento[0])){

				Set key = parameter.keySet();
				plcAction.create();

				String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
				PlcPrimaryKey chavePrimaria = (PlcPrimaryKey) configUtil.getConfigAggregation(url).entity().getAnnotation(PlcPrimaryKey.class);

				if (chavePrimaria != null){

					Class classeChave = chavePrimaria.classe();

					Object object = classeChave.newInstance();

					for (Iterator i = key.iterator(); i.hasNext();){

						String chave = (String)i.next();
						String [] valor = (String [])parameter.get(chave);
						Field field = null;

						try {
							field = reflectionUtil.findFieldHierarchically(classeChave, chave);
						} catch (Exception e) {
							// Se não achou o field, ele permanece null 
						}

						if (field != null){
							if (field.getType().isAssignableFrom(String.class))
								propertyUtilsBean.setProperty(object, chave, valor[0]);
							else if (field.getType().isAssignableFrom(Integer.class))
								propertyUtilsBean.setProperty(object, chave, Integer.parseInt(valor[0]));
							else if (field.getType().isAssignableFrom(Date.class)){
								if (NumberUtils.isNumber(valor[0])){
									String format = DateFormat.getDateInstance().format(Long.parseLong(valor[0]));
									propertyUtilsBean.setProperty(object, chave, dateUtil.stringToDate(format));
								}else
									propertyUtilsBean.setProperty(object, chave, dateUtil.stringToDate(valor[0]));
							}

						}
					}

					propertyUtilsBean.setProperty(entityPlc, "idNatural", object);

				}
			}
		} catch(PlcException plcE){
			throw plcE;			
		}catch (Exception e){
			throw new PlcException("PlcBaseEditMB", "getParameterValues", e, log, "");
		}
	}

	/**
	 * Comuta modo de exibição de formulário com uso ou nao de tab-folder de
	 * layout. Nao tem template method de Antes por ser muito simples. Pode-se
	 * sobrepor e utilizar super.
	 */
	public String editDocumentView()  {

		String exibeEdDocPlc = contextUtil.getRequest().getParameter(PlcConstants.EXIBE_ED_DOC_PLC);

		if (exibeEdDocPlc != null && "S".equals(exibeEdDocPlc)) {
			contextUtil.removeRequestAttribute(PlcConstants.VISUALIZA_DOCUMENTO_PLC);
		} else {
			this.getPlcRequestControl().setExibeEditarDocumentoPlc("S");
			contextUtil.setRequestAttribute(PlcConstants.VISUALIZA_DOCUMENTO_PLC, "S");
		}

		return editDocumentViewAfter();
	}

	/**
	 * Design Pattern: Template Method ou Observer. 
	 * Permite lógicas a serem realizadas após
	 * o formulário ter sido colocado/retirado de modo de "visualização de
	 * documento" para, por exemplo, esconder campos específicos que não se
	 * deseje manter em um dos modos.
	 */
	protected String editDocumentViewAfter()  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcEditDocumentViewAfter>(){});
		return defaultNavigationFlow;
	}


	protected boolean checkExistsKey(){
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		
		PlcBaseContextVO context = (PlcBaseContextVO)contextUtil.getRequest().getAttribute(PlcConstants.CONTEXT);
		PlcPrimaryKey chavePrimaria = (PlcPrimaryKey) configUtil.getConfigAggregation(url).entity().getAnnotation(PlcPrimaryKey.class);
		
		// Verifica de existe esse campo no request, caso exista seta o id com o valor para editar o registro selecionado na treeview
		String idTreeView = (String) contextUtil.getRequest().getAttribute("idTreeView");
		if(StringUtils.isNotEmpty(idTreeView)) {
			if(StringUtils.isNumeric(idTreeView)) {
				id = idTreeView;
			}
		}
		
		return chavePrimaria!=null || id!=null;

	}

}
