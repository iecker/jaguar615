/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.EVENTOS;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcMaster;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.PlcBeanCloneUtil;
import com.powerlogic.jcompany.commons.util.PlcDetailUtil;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.aggregation.PlcConfigComponent;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.config.domain.PlcNotCloneable;
import com.powerlogic.jcompany.controller.bindingtype.PlcCloneEntityAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcCloneEntityBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcCreateAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcCreateBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcCreateSubDetailBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcNewDetailAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcNewDetailBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcNewEntityAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcNewEntityBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcNewItemsAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcNewItemsBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcNewParametersAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcNewParametersBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcNewSubDetailAfter;
import com.powerlogic.jcompany.controller.injectionpoint.HttpParam;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcPagedDetailControl;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcEntityUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcFormEntityUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcNaturalKeyUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

/**
 * Encapsula métodos de controle para criação de novos objetos para entidades ou lista de entidades associadas a formulários
 */
@QPlcDefault
public class PlcBaseCreateMB extends PlcBaseParentMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected transient Logger log;

	/**
	 * Parâmetro de request padrão para OID, gerenciado pelo CDI.
	 */
	@Inject	@HttpParam("id")
	protected String id;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	
	
	/**
	 * Parâmetro de request padrão para determinação de evento, gerenciado pelo CDI.
	 */
	@Inject	@HttpParam("evento")
	protected String evento;

	@Inject @QPlcDefault 
	protected PlcBeanCloneUtil beanCloneUtil;	

	@Inject @QPlcDefault 
	protected PlcNaturalKeyUtil chaveNaturalUtil;

	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;	

	@Inject @QPlcDefault 
	protected PlcMsgUtil msgUtil;

	@Inject @QPlcDefault 
	protected PlcEntityCommonsUtil entityCommonsUtil;

	@Inject @QPlcDefault 
	protected PlcEntityUtil entityUtil;

	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil;

	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	@Inject @QPlcDefault 
	protected PlcBaseValidation validationMB; 

	@Inject @QPlcDefault 
	protected PlcFormEntityUtil formEntityUtil;

	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;

	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;

	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
	/**
	 * Controller para detalhes paginados e argumentos em detalhes
	 */
	@Inject @Named(PlcJsfConstantes.PLC_CONTROLE_DETALHE_PAGINADO)
	protected PlcPagedDetailControl controleDetalhePaginadoPlc;	
		
	@Inject @Named("plcAction")
	protected PlcBaseMB plcAction; 		

	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	/**
	 * Método responsável pela criação de Entidades ou sua recuperação da persistencia caso o Object-ID seja passado na URL.
	 */
	public Object newEntity(Object entityPlc)  {

		try {

			this.entityPlc = entityPlc;

			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			PlcPrimaryKey chavePrimaria = (PlcPrimaryKey) configUtil.getConfigAggregation(url).entity().getAnnotation(PlcPrimaryKey.class);			
			FormPattern pattern = configUtil.getConfigAggregation(url).pattern().formPattern(); 

			if (newEntityBefore(pattern,entityPlc)) {

				if (log.isDebugEnabled())
					log.debug( "Recuperando : evento="+this.evento+", id=" + this.id);
	
				if ((chavePrimaria != null && chaveNaturalUtil.hasKeyProperty(chavePrimaria, contextUtil.getRequest())) ||
					(StringUtils.isNotEmpty(this.id) && !"null".equals(this.id))) {
					plcAction.edit();
				} else if("y".equals(this.evento)){
					create(entityPlc);
				} else if ("Novo".equals(this.evento)) {
					create(entityPlc);
				} else {
					if (pattern==FormPattern.Apl || pattern==FormPattern.Usu) {
						newParameters();
					} else { 
						create(entityPlc);
					}
				}
			}

			newEntityAfter(pattern,entityPlc);

		} catch (PlcException plcE) {
			msgUtil.msg(plcE.getMessage(), PlcMessage.Cor.msgVermelhoPlc.toString());
			create(entityPlc);
		} catch (Exception e) {
			throw new PlcException("PlcBaseCreateMB", "newEntity", e, log, "");
		}

		return this.entityPlc;
	
	}
	
	/**
	 * Método responsável por limpar a Entidade corrente do Caso de Uso. 
	 * Utilizar caso necessite reininciar a conversação ou ao redirecionar para outro Caso de Uso que utilize a mesma entidade.
	 * @param entityPlc Entidade para padrões que usam uma entidade principal
	 */
	public void clearEntity(Object entityPlc) {
		
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		
		try {
			Object entidadeNova = configUtil.getConfigAggregation(url).entity().newInstance();
			getPlcRequestControl().setDetCorrPlc(null);
			create(entidadeNova);
			beanCloneUtil.copyProperties(entityPlc, entidadeNova);
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBaseCreateMB", "clearEntity", e, log, "");
		}
		
		plcAction.clearArgs();
		
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param entityPlc Entidade para padrões que usam uma entidade principal
	 * @param pattern Padrão de Formulário corrente
	 */
	protected boolean newEntityBefore(FormPattern pattern, Object entityPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcNewEntityBefore>(){});
		return defaultProcessFlow;
	}
	
	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param entityPlc Entidade para padrões que usam uma entidade principal
	 * @param pattern Padrão de Formulário corrente
	 */
	protected void newEntityAfter(FormPattern pattern, Object entityPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcNewEntityAfter>(){});
	}

	/**
	 * Cria uma nova entidade vazia
	 */
	@SuppressWarnings("unchecked")
	public String create(Object entityPlc)  {

		this.entityPlc = entityPlc;

		if (contextUtil.getRequest().getAttribute(PlcConstants.ACAO.EVENTO)==null){
			contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_INCLUIR);
			contextUtil.getRequest().setAttribute(PlcConstants.MODOS.MODO,PlcConstants.MODOS.MODO_INCLUSAO);
		}

		//definindo entidade utilizada pelo jmonitor como nula, se a mesma existir.

		if((contextUtil.getRequest().getAttribute(PlcConstants.ENTIDADE.ENTIDADE) != null)){
			if(contextUtil.getRequest().getAttribute(PlcConstants.ENTIDADE.PREFIXO_OBJ + contextUtil.getRequest().getAttribute(PlcConstants.ENTIDADE.ENTIDADE)) != null){
				contextUtil.getRequest().setAttribute(PlcConstants.ENTIDADE.PREFIXO_OBJ + contextUtil.getRequest().getAttribute(PlcConstants.ENTIDADE.ENTIDADE), null);
			}
		}

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		FormPattern pattern = configUtil.getConfigAggregation(url).pattern().formPattern();

		//registra no request qual foi a ação realizada
		contextUtil.getRequest().setAttribute(PlcJsfConstantes.ACAO.PLC_IND_ACAO_NOVO,"S");

		boolean isMestreDetalheOuSubDetalhe = FormPattern.Mdt.equals(pattern)
								|| FormPattern.Mad.equals(pattern)
								|| FormPattern.Mds.equals(pattern)
								|| FormPattern.Mas.equals(pattern);
		
		Matcher matcher = null;
		boolean novoSubdetalhe = false;

		String detCorrPlc = null;
		if (getPlcRequestControl() != null)
			detCorrPlc = getPlcRequestControl().getDetCorrPlc();

		if (StringUtils.isBlank(detCorrPlc) && 
			(FormPattern.Mad.equals(pattern) || FormPattern.Mas.equals(pattern)) && 
			((configUtil.getConfigAggregation(url).details() != null) && (configUtil.getConfigAggregation(url).details().length > 0))) {
			detCorrPlc = configUtil.getConfigAggregation(url).details()[0].collectionName();			
			contextUtil.setRequestAttribute("detCorrPlc", detCorrPlc);
		}

		//verificando se é um novo para sub-detalhe
		String subDetCorrPlc = contextUtil.getRequestParameter("subDetCorrPlc");
		if (subDetCorrPlc != null && StringUtils.isNotBlank(subDetCorrPlc)) {
			detCorrPlc = subDetCorrPlc;
			getPlcRequestControl().setDetCorrPlc(detCorrPlc);
			contextUtil.setRequestAttribute("detCorrPlc", detCorrPlc);
		}
		
		if (createBefore(pattern,entityPlc,null,isMestreDetalheOuSubDetalhe,detCorrPlc)) {
			
			if (isMestreDetalheOuSubDetalhe && StringUtils.isNotBlank(detCorrPlc)){
				Pattern padrao = Pattern.compile("(\\[\\d*\\])");
				matcher = padrao.matcher(detCorrPlc);
				novoSubdetalhe = matcher.find();
			}
	
			// É modo de inclusao - inicializacao do form
			if (plcControleConversacao != null) {
				if (StringUtils.isEmpty(detCorrPlc)) {
					plcControleConversacao.setModoPlc(PlcConstants.MODOS.MODO_INCLUSAO);
				}
				this.getPlcRequestControl().setExibeEditarDocumentoPlc(null);
			}
	
			// Sempre inicializa todos os detalhes para logicas correlatas
			if (plcControleConversacao == null || detCorrPlc == null || StringUtils.isEmpty(detCorrPlc) || "#".equals(detCorrPlc) || contextUtil.getRequest().getAttribute(PlcConstants.ACAO.EVENTO).equals(PlcConstants.ACAO.EVT_EXCLUIR)) {
	
				try {
					// Somente limpa mestre se nao for padrão "Consuta-Mestre" ou CRUD-TABULAR
					if (!FormPattern.Mad.equals(pattern) && !FormPattern.Mas.equals(pattern)
							&& !FormPattern.Ctb.equals(pattern)) {
						createEntity(entityPlc);
						createComponent(entityPlc, null);
					}
	
				} catch(PlcException plcE){
					throw plcE;				
				} catch (Exception e) {
					throw new PlcException("PLcBaseCreateMB", "create(Object)", e, log, "");
				}
			}
	
			if (isMestreDetalheOuSubDetalhe) {
	
				if (plcControleConversacao == null || StringUtils.isBlank(detCorrPlc) || "#".equals(detCorrPlc) || contextUtil.getRequest().getAttribute(PlcConstants.ACAO.EVENTO).equals(PlcConstants.ACAO.EVT_EXCLUIR)) {
					// Se clicou em novo ou esta iniciando e nao há detalhe em
					// foco, inicia todos os detalhes
					// Pega meta-dados da colaboração/ação
					PlcConfigAggregationPOJO configAcaoCorrente = configUtil.getConfigAggregation(url);
					for (PlcConfigDetail detalhe : configAcaoCorrente.details()) {
						if (detalhe != null && this.entityUtil.isDetailValid(detalhe.clazz(),detalhe.collectionName())) {
							newDetail(detalhe);							
						}
					}
	
				} else {
					// Se tiver um detalhe especifico em foco, entao insere somente itens neste detalhe.
	
					//Não é novo no Mestre e sim dos detalhes
					contextUtil.getRequest().removeAttribute(PlcJsfConstantes.ACAO.PLC_IND_ACAO_NOVO);
	
					if (novoSubdetalhe){
						if (executeButtonCreate(PlcConstants.ACAO.EXIBE_JCOMPANY_EVT_INCLUIR_SUBDETALHE, detCorrPlc)) {
							//novo subdetalhe
							contextUtil.getRequest().setAttribute(PlcJsfConstantes.ACAO.PLC_IND_ACAO_NOVO_SUBDET,"S");
							return createSubDetail(matcher);
						} else {
							return defaultNavigationFlow;
						}
					}
	
					PlcConfigDetail configDetalhe = getConfigDetail(configUtil.getConfigAggregation(url), detCorrPlc);
	
					if (configDetalhe != null && executeButtonCreate(PlcConstants.ACAO.EXIBE_JCOMPANY_EVT_INCLUIR_DETALHE, detCorrPlc)) {
						return newDetail(configDetalhe);
					} else {
						return defaultNavigationFlow;
					}
				}
			}
	
			if (controleDetalhePaginadoPlc != null) {
				this.controleDetalhePaginadoPlc.reset();
			}
		}

		//limpando imagens temporárias da sessão
		clearFileAttachInSession(entityPlc, true);
		
		return createAfter(pattern,entityPlc,null,detCorrPlc);

	}


	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param entityPlc Entidade para padrões que usam uma entidade principal
	 * @param pattern Padrão de Formulário corrente
	 * @param isMestreDetalheOuSubDetalhe Indica se o padrao é algum destes
	 * @param detCorrPlc Detalhe em foco
	 */
	protected boolean createBefore(FormPattern pattern, Object entityPlc, PlcEntityList entityListPlc, boolean isMestreDetalheOuSubDetalhe, String detCorrPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcCreateBefore>(){});
		return defaultProcessFlow;
	}
	

	/** Design Pattern: Template Method ou Observer. Utilizar para desvio de fluxo após
	 * submissão em botao "novo", ou para complementar com lógicas programadas
	 * em geral.
	 * @param entityPlc Entidade para padrões que usam uma entidade principal
	 * @param pattern Padrão de Formulário corrente
	 * @param detCorrPlc Detalhe em foco
	 */
	protected String createAfter(FormPattern pattern, Object entityPlc, PlcEntityList entityListPlc, String detCorrPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcCreateAfter>(){});
		return defaultNavigationFlow;
	}


	/**
	 * Metodo responsável para criação de novos subdetalhes
	 */
	protected String createSubDetail(Matcher matcher)  {
		
		try {

			PlcConfigDetail configDetalhe;
			int ini = matcher.start();
			int fim = matcher.end();

			String detCorrPlc = getPlcRequestControl().getDetCorrPlc(); 
			String indice = matcher.group(1);

			int pos = Integer.parseInt(indice.replace("[", "").replace("]", ""));

			String detalhe = detCorrPlc.substring(0, ini);
			String subDetalhe = detCorrPlc.substring(fim+1, detCorrPlc.length());

			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			configDetalhe = getConfigDetail(configUtil.getConfigAggregation(url),detalhe);

			if(configDetalhe == null) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_CONFIGDETAIL_NOT_FOUND, new Object[]{detCorrPlc}, true);
			}
			Collection<Object> listaDetalhe = (Collection<Object>)propertyUtilsBean.getProperty(entityPlc, detalhe);

			if ((listaDetalhe == null) || (listaDetalhe.isEmpty())) {
				newDetail(configDetalhe);
				listaDetalhe = (Collection<Object>)propertyUtilsBean.getProperty(entityPlc, detalhe);
				getPlcRequestControl().setDetCorrPlc(detalhe); 
				return createSubDetailAfter(null);
			}

			Object detalhePlc =  (listaDetalhe).toArray()[pos];

			Collection listaSubDetalhe = null;
			
			Class<?> classeDetalhe = detalhePlc.getClass();
			Field campo = null;
			if(classeDetalhe != null) {
				campo = reflectionUtil.findFieldHierarchically(classeDetalhe, subDetalhe);
				if(campo != null) { 
					classeDetalhe = campo.getType();
				} else {
					classeDetalhe = Object.class;
				}
			} else {
				classeDetalhe = Object.class;
			}
			
			if (classeDetalhe.isAssignableFrom(List.class)){
				listaSubDetalhe = (List) propertyUtilsBean.getProperty(detalhePlc, subDetalhe);
				if(listaSubDetalhe == null) {
					listaSubDetalhe = new ArrayList();
				}
			}else{
				if(classeDetalhe.isAssignableFrom(Set.class)) {
					listaSubDetalhe = (Set) propertyUtilsBean.getProperty(detalhePlc, subDetalhe);
				}
				if(listaSubDetalhe == null) {
					listaSubDetalhe = new HashSet();
				}
			}

			if (createSubDetailBefore(listaSubDetalhe)){
				
				for (int count = 0; count < configDetalhe.subDetail().numNew(); count ++){
	
					Object subDetalheNovo = configDetalhe.subDetail().clazz().newInstance();
	
					if(createSubDetailApi(subDetalheNovo)){
						
						List propAgregadaParaTipo = entityCommonsUtil.getAggregatePropertyByType(subDetalheNovo,detalhePlc.getClass().getName());
		
						if (propAgregadaParaTipo != null && !propAgregadaParaTipo.isEmpty()) {
							String nomeProp = (String) propAgregadaParaTipo.get(0);
							propertyUtilsBean.setProperty(subDetalheNovo, nomeProp, detalhePlc);
						}
		
						PlcPrimaryKey chaveNaturalSubdetalhe = (PlcPrimaryKey)configDetalhe.subDetail().clazz().getAnnotation(PlcPrimaryKey.class);
						if (chaveNaturalSubdetalhe != null){
							propertyUtilsBean.setProperty(subDetalheNovo, "idNatural", chaveNaturalSubdetalhe.classe().newInstance());
						}
		
						listaSubDetalhe.add(subDetalheNovo);
					}
				}
	
				propertyUtilsBean.setProperty(detalhePlc, subDetalhe, listaSubDetalhe);
			
			}

			return createSubDetailAfter(listaSubDetalhe);
			
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseCreateMB", "createSubDetail", e, log, "");
		}

	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * descendentes.
	 * @param listaSubDetalhe Lista de subdetalhes com novo sub-detalhe já criado
	 */
	protected String createSubDetailAfter(Collection listaSubDetalhe)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcNewSubDetailAfter>(){});
		return defaultNavigationFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Api" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected Boolean createSubDetailApi(Object subDetalhe)  {
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected boolean createSubDetailBefore(Collection subDetalhePlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcCreateSubDetailBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Cria uma nova entidade com base em outra já existente 
	 * @param entidade Entidade a ser clonada
	 */
	protected void createEntity(Object entidade)  {

		if (entidade != null) {

			try {
				String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());				
				Object entidadeNova = configUtil.getConfigAggregation(url).entity().newInstance();
				beanCloneUtil.copyProperties(entidade, entidadeNova);
			} catch(PlcException plcE){
				throw plcE;						
			} catch (Exception e) {
				throw new PlcException("PlcBaseCreateMB", "createEntity", e, log, "");
			} 
		}

	}		

	/**
	 * Método auxiliar para a criação de componentes na entidade corrente, no método {@link #novo()}.
	 * Cria também a chave primária, se for necessário.
	 */
	protected void createComponent(Object entidade, String logica)  {

		if (entidade != null) {
			
			try {
				
				PlcConfigComponent[] componentes = null;
				String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
				PlcConfigAggregationPOJO configAcaoCorrente = configUtil.getConfigAggregation(url);

				PlcPrimaryKey chavePrimaria = entidade.getClass().getAnnotation(PlcPrimaryKey.class);
				if (chavePrimaria!=null && !metamodelUtil.isEntityClass(chavePrimaria.classe())) {
					//Significa que tem chave primária.
					propertyUtilsBean.setProperty(entidade,	"idNatural", chavePrimaria.classe().newInstance());
				}
				
				if(logica != null && "det".equalsIgnoreCase(logica)){
					
					PlcConfigDetail[] details = configAcaoCorrente.details();
					for(PlcConfigDetail detail : details){
						if(entidade.getClass().toString().contains(detail.collectionName().toString().substring(0,1).toUpperCase()+detail.collectionName().toString().substring(1)+"Entity")){
							componentes = detail.components();
							
							for (PlcConfigComponent componente : componentes) {
								if (StringUtils.isNotBlank(componente.property())) {
									propertyUtilsBean.setProperty(entidade,	componente.property(), componente.clazz().newInstance());
								}
							}
						}						
					}					
					
				}else{
					
					if(componentes == null){
						componentes = configAcaoCorrente.components();
					}
					
					for (PlcConfigComponent componente : componentes) {
						if (StringUtils.isNotBlank(componente.property())) {
							propertyUtilsBean.setProperty(entidade,	componente.property(), componente.clazz().newInstance());
						}
					}
				}				
				
			} catch(PlcException plcE){
				throw plcE;						
			} catch (Exception e) {
				throw new PlcException("PlcBaseCreateMB", "createComponent", e, log, "");
			}
		}

	}


	/**
	 * Adiciona novos elementos de um detalhe, baseado na configuração
	 * informada.
	 * 
	 * @param detalhe  configuração do detalhe que será adicionado.
	 */
	protected String newDetail(Object entityPlc, PlcConfigDetail detalhe)  {
		this.entityPlc = entityPlc;
		return newDetail(detalhe);
	}
	
	/**
	 * Criar um novo objeto no detalhe corrente
	 * @param detalhe Detalhe corrente
	 */
	protected String newDetail(PlcConfigDetail detalhe)  {

		try {
			
			if (entityPlc==null){
				String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
				entityPlc = configUtil.getConfigAggregation(url).entity().newInstance();
			}
			
			Collection listaDetalhe = (Collection) propertyUtilsBean.getProperty(entityPlc, detalhe.collectionName());

			if (newDetailBefore(entityPlc,listaDetalhe)) {

				if (listaDetalhe == null) {
					// Coleções mapeadas devem ser Set (preferencialmente) ou List
					if (List.class.getName().equals(propertyUtilsBean.getPropertyType(entityPlc,	detalhe.collectionName()).getName())){
						listaDetalhe = new ArrayList();
					}else{
						// Necessário para ordernar detalhe quando é acionado novo
						listaDetalhe = new ListOrderedSet();
					}
					propertyUtilsBean.setProperty(entityPlc, detalhe.collectionName(), listaDetalhe);
				} 
				
				for (int i = 0; i < detalhe.numNew(); i++) {
	
					String classeMestre = entityPlc.getClass().getName();
	
					Object novoDetalhe = detalhe.clazz().newInstance();
	
					if(newDetailApi(entityPlc,novoDetalhe)){
	
						PlcMaster mestre = (PlcMaster) detalhe.clazz().getAnnotation(PlcMaster.class);
						if (mestre != null) {
							classeMestre =  reflectionUtil.findFieldHierarchically(detalhe.clazz(),mestre.value()).getType().getName();
						}
		
						List propAgregadaParaTipo = entityCommonsUtil.getAggregatePropertyByType(novoDetalhe,classeMestre);
		
						if (propAgregadaParaTipo != null && !propAgregadaParaTipo.isEmpty()) {
							String nomeProp = (String) propAgregadaParaTipo.get(0);
							propertyUtilsBean.setProperty(novoDetalhe, nomeProp, entityPlc);
						}
						
						if(novoDetalhe != null){
							createComponent(novoDetalhe, "det");
						}						
		
						if (detalhe.subDetail() != null && this.entityUtil.isDetailValid(detalhe.subDetail().clazz(), detalhe.subDetail().collectionName())) {
		
							Collection vos = null;
							
							if (List.class.getName().equals(propertyUtilsBean.getPropertyType(detalhe.clazz().newInstance(),detalhe.subDetail().collectionName()).getName())){
								vos = new ArrayList();
							}else if (Set.class.getName().equals(propertyUtilsBean.getPropertyType(detalhe.clazz().newInstance(),detalhe.subDetail().collectionName()).getName())){
								vos = PlcDetailUtil.instanceTreeSet(detalhe.comparator());
							}
		
							for (int count = 0; count < detalhe.subDetail().numNew(); count++) {
		
								Object subDetalhe = detalhe.subDetail().clazz().newInstance();
		
								propAgregadaParaTipo = entityCommonsUtil.getAggregatePropertyByType(subDetalhe,novoDetalhe.getClass().getName());
		
								if (propAgregadaParaTipo != null && !propAgregadaParaTipo.isEmpty()) {
									String nomeProp = (String) propAgregadaParaTipo.get(0);
									propertyUtilsBean.setProperty(subDetalhe, nomeProp, novoDetalhe);
								}
		
								PlcPrimaryKey chaveNaturalSubdetalhe = (PlcPrimaryKey) detalhe.subDetail().clazz().getAnnotation(PlcPrimaryKey.class);
								if (chaveNaturalSubdetalhe != null){
									propertyUtilsBean.setProperty(subDetalhe, "idNatural", chaveNaturalSubdetalhe.classe().newInstance());
								}
		
								vos.add(subDetalhe);
							}
		
							propertyUtilsBean.setProperty(novoDetalhe, detalhe.subDetail().collectionName(), vos);
						}
		
						PlcPrimaryKey chaveNatural = (PlcPrimaryKey)detalhe.clazz().getAnnotation(PlcPrimaryKey.class);
						if (chaveNatural != null){
							propertyUtilsBean.setProperty(novoDetalhe, "idNatural", chaveNatural.classe().newInstance());
						}
		
						listaDetalhe.add(novoDetalhe);
					}
				}
			}
			return newDetailAfter(entityPlc,listaDetalhe);
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBaseCreateMB", "newDetail", e, log, "");
		}

	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param listaDetalhe Lista de detalhes
	 * @param mestre Entidade mestre
	 */
	protected String newDetailAfter(Object mestre, Collection listaDetalhe)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcNewDetailAfter>(){});
		return defaultNavigationFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected boolean newDetailApi(Object mestre,Object detalhe)  {
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param entityPlc Entidade principal
	 * @param listaDetalhe Lista de detalhes 
	 */
	protected boolean newDetailBefore(Object entityPlc, Collection listaDetalhe)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcNewDetailBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Adiciona novos registros para lógica Tabular conforme número de novos ou
	 * somente 1 se não estiver configurado número de novos. 
	 */
	protected String create(PlcEntityList entityListPlc)  {

		this.entityListPlc = entityListPlc; 
		
		// Incluido para acertar problema de informação modo quando for novo registro
		contextUtil.getRequest().setAttribute(PlcConstants.MODOS.MODO, PlcConstants.MODOS.MODO_INCLUSAO);

		try {

			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			Class voClass = configUtil.getConfigAggregation(url).entity();

			int tabularNumeroNovos = configUtil.getConfigCollaboration(url).tabular().numNew();

			if (createBefore(FormPattern.Tab, null,entityListPlc, false, null)) {
			
				if (configUtil.getConfigCollaboration(url).behavior().batchInput()) { 
					plcAction.search();
				}	
	
				for (int i = 0; i < tabularNumeroNovos; i++) {
					Object item = voClass.newInstance();
					createComponent(item, null);
					if (entityListPlc.getItensPlc()==null)
						entityListPlc.setItensPlc(new ArrayList<Object>());
					entityListPlc.getItensPlc().add(item);
				}
			}

			return createAfter(FormPattern.Tab,null,entityListPlc,null);
			
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PLcBaseCreateMB", "create(List)", e, log, "");
		}

	}
	
	/**
	 * Método responsável para criar um objeto da entidade e uma lista
	 * Utilizado em lógica do tipo CRUDTABULAR ou controle livre que necessite do cenário
	 */
	protected String create(Object entityPlc, PlcEntityList entityListPlc)  {
		create(entityPlc);
		create(entityListPlc);
		return createAfter(FormPattern.Ctb,entityPlc,entityListPlc,null);
	}

	
	/**
	 * Metodo responsavel por permitir ou nao a execucao do botao novo
	 * @param Botao
	 * @param detCorrPlc
	 * @return
	 */
	protected boolean executeButtonCreate(String Botao, String detCorrPlc) {
		return !(
				(
					PlcConstants.NAO.equals(contextUtil.getRequest().getAttribute(Botao + "_" + detCorrPlc)) && (PlcConstants.NAO.equals(contextUtil.getRequest().getAttribute(Botao)))
				) || (
					(!PlcConstants.SIM.equals(contextUtil.getRequest().getAttribute(Botao + "_" + detCorrPlc))) && (PlcConstants.NAO.equals(contextUtil.getRequest().getAttribute(Botao)))
				) || (
					(PlcConstants.NAO.equals(contextUtil.getRequest().getAttribute(Botao + "_" + detCorrPlc))) && (!PlcConstants.NAO.equals(contextUtil.getRequest().getAttribute(Botao)))
				)
		);
	}

	/**
	 * Cria ou Recupera a entidade com padrões PREFAPLICACAO (Apl) e PREFUSUARIO (Usu)
	 */
	@SuppressWarnings("unchecked")
	protected String newParameters()  {

		// Cria uma nova entidade configurada no caso de uso. Pode ser a primeira vez que o usuário entra na tela e a
		// Validação Programatica depende da entidade corrente
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		FormPattern pattern 				= configUtil.getConfigAggregation(url).pattern().formPattern();

		contextMontaUtil.createContextParam(plcControleConversacao);
		PlcBaseContextVO context 	= (PlcBaseContextVO)contextUtil.getRequest().getAttribute(PlcConstants.CONTEXT);
		Collection<Object> listaEntidade 	= null; 
		
		try{
			
			if (newParametersBefore(pattern,context,listaEntidade)) {
			
				/**
				 * Se for Lógica de Prefeência da APLICAÇÃO será retornado somente um registro, porque lógica já trata somente uma inserção.
				 */
				if(FormPattern.Apl.equals(pattern))
					listaEntidade = iocControleFacadeUtil.getFacade(url).findSimpleList(context, configUtil.getConfigAggregation(url).entity(), null);
				else {
					/**
					 * Se for Lógica de Preferência de USUÁRIO, na camada de modelo o jCompany já filtra da lista(na verdade somente um registro)
					 * que vai ser retornada pelo usuário logado. 
					 */ 
					String usuarioCorrente = "anônimo";
					if (contextUtil.getRequest().getUserPrincipal() != null)
						usuarioCorrente = contextUtil.getRequest().getUserPrincipal().getName();
	
					Object entidade = configUtil.getConfigAggregation(url).entity().newInstance();
					propertyUtilsBean.setProperty(entidade, context.getArgPreference(), usuarioCorrente);
					listaEntidade = iocControleFacadeUtil.getFacade().findList(context, entidade, "", -1, -1);
				}
	
				// Se a pesquisa retornou dados pega o único registro que será retornado
				if (listaEntidade != null && !listaEntidade.isEmpty() ){
					List l = new ArrayList();
					l.addAll(listaEntidade);
					entityPlc = l.get(0);
					plcControleConversacao.setModoPlc(PlcConstants.MODOS.MODO_EDICAO);
				}
			
			}

			return newParametersAfter(pattern);
			
		} catch(PlcException plcE){
			throw plcE;			
		}catch(Exception e ){
			throw new PlcException("PlcBaseCreateMB", "newParameters", e, log, "");
		}

	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After"
	 * ou "api" são eventos (método vazios) destinados a especializações nos
	 * descendentes.
	 * @param listaEntidade 
	 * @param context 
	 * @param pattern 
	 */
	protected boolean newParametersBefore(FormPattern pattern, PlcBaseContextVO context, Collection<Object> listaEntidade)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcNewParametersBefore>(){});
		return defaultProcessFlow;
	}
	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After"
	 * ou "api" são eventos (método vazios) destinados a especializações nos
	 * descendentes.
	 * @param pattern 
	 */	
	protected String newParametersAfter(FormPattern pattern)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcNewParametersAfter>(){});
		return defaultNavigationFlow;
	}

	/**
	 * Método responsável pela criação do objeto de controle {@link PlcEntityList}.
	 * No caso de tabular, crud tabular ou seleção/consulta, será criado uma instância da subclasse {@link PlcBaseLogicaArgumento},
	 * para comportar também os argumentos para a lista.
	 * @param entityListPlc 
	 */
	public void newItems(PlcEntityList entityListPlc)  {

		this.entityListPlc = entityListPlc;

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		FormPattern pattern = configUtil.getConfigAggregation(url).pattern().formPattern();

		if (newItemsBefore(pattern,entityListPlc)) {
		
			// Se padrão nao for de manutenção
			if (FormPattern.Rel.equals(pattern)  || FormPattern.Sel.equals(pattern)  || FormPattern.Con.equals(pattern) 
				|| FormPattern.Ctl.equals(pattern) || FormPattern.Smd.equals(pattern)) {
				
				create(entityListPlc); // instanciar a entidade principal para opcionalmente armazenar os argumentos de selecao.
	
				String evitaValidacaoEventoY = (String)contextUtil.getRequest().getAttribute(PlcJsfConstantes.VALIDACAO.EVITA_VALIDACAO_EVENTO_Y);
	
				if ("y".equals(this.evento) && evitaValidacaoEventoY == null) {
					plcAction.search();
				}
				
			} else if (FormPattern.Ctb.equals(pattern)) {
				
				create(entityListPlc); // instanciar a entidade principal para opcionalmente armazenar os argumentos de selecao.
	
				String evitaValidacaoEventoY = (String)contextUtil.getRequest().getAttribute(PlcJsfConstantes.VALIDACAO.EVITA_VALIDACAO_EVENTO_Y);
	
				plcControleConversacao.setPesquisarValida(true);
				
				if ("y".equals(this.evento) && evitaValidacaoEventoY == null) {
					plcAction.search();
				}
				
				// Se tem entrada em lote então adiciona os novos
				if (configUtil.getConfigCollaboration(url).behavior().batchInput()){ 
					entityListPlc.setItensPlc(new ArrayList<Object>());
					create(entityListPlc);
				} else{
					// Entra com a lista vazia para o usuário efeturar a pesquisa com parâmetros informados
					entityListPlc.setItensPlc(new ArrayList<Object>());
				}
					
			} else if (FormPattern.Tab.equals(pattern)) {
				
				plcControleConversacao.setPesquisarValida(false);
				
				plcAction.search();
				
			} else {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_INVALIDATING_ITEMS, new Object[] { pattern.toString() }, true);
			}
		}

		newItemsAfter(pattern,entityListPlc);

	}


	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param entityListPlc Lista de Entidades
	 * @param pattern Padrão de Formulário corrente
	 */
	protected boolean newItemsBefore(FormPattern pattern, PlcEntityList entityListPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcNewItemsBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param entityListPlc Lista de Entidades
	 * @param pattern Padrão de Formulário corrente
	 */
	protected void newItemsAfter(FormPattern pattern, PlcEntityList entityListPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcNewItemsAfter>(){});
	}


	/**
	 * jCompany. Método que implementa o evento "clonar", disparado pelo usuario
	 * quando ele deseja fazer uma cópia de um registro existente para inclui
	 * outro parecido
	 * <p>
	 * Este método também altera o modo para "inclusao" e dispara a operação
	 * "clonaAntes", para possiveis complementações no descendente.
	 * 
	 */
	public String cloneEntity(Object entityPlc)  {

		this.entityPlc = entityPlc;
		
		if (contextUtil.getRequest().getAttribute(PlcConstants.WORKFLOW.IND_ACAO_ORIGINAL) == null)	{		
			contextUtil.getRequest().setAttribute(PlcConstants.WORKFLOW.IND_ACAO_ORIGINAL,EVENTOS.CLONA);
		}
		
		if (contextUtil.getRequest().getAttribute(PlcConstants.ACAO.EVENTO)==null) {
			contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_CLONAR);
		}
		
		// Quando clona, não precisam ficar como demanda. 
		contextUtil.getRequest().setAttribute(PlcJsfConstantes.ACAO.PLC_IND_NAO_VERIFICAR_DETALHE_DEMANDA, "N");

		if (cloneEntityBefore()) {
			
			if (plcControleConversacao.getModoPlc().equals(PlcConstants.MODOS.MODO_EDICAO)) {
	
				PlcEntityInstance entityPlcInstance = metamodelUtil.createEntityInstance(entityPlc);
	
				entityPlcInstance.setId(null);
				entityPlcInstance.setIdAux(null);
				// Se a entididade corrente estiver configurada com Chave Natural, então clona 'inicializa' a chave natural
				entityPlc = chaveNaturalUtil.cloneNaturalKey(entityPlc);
	
				// Verifica se a propriedade esta anotada com o PlcNotCloneable
				// Caso esteja anotada, não será clonada...
				//TODO: Funciona somente para tipos primitivos, refatorar aceitar outros tipos de objetos...
				Field[] fields = reflectionUtil.findAllFieldsHierarchically(entityPlc.getClass(), true, true);
				for (Field field : fields) {
					if(field.getAnnotation(PlcNotCloneable.class) != null) {
						reflectionUtil.setEntityPropertyPlc(entityPlc, field.getName(), null);
					}
				}
				
				entityPlc.getClass().getAnnotation(PlcPrimaryKey.class);
				
				
				String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());

				//clonando os arquivos anexados
				formEntityUtil.cloneFileAttach(entityPlc);
				
				contextUtil.getRequest().removeAttribute(PlcConstants.VO.PREFIXO_OBJ + configUtil.getConfigAggregation(url).entity().getName());
	
				plcControleConversacao.setModoPlc(PlcConstants.MODOS.MODO_INCLUSAO);
				PlcConfigAggregationPOJO configAcaoCorrente = configUtil.getConfigAggregation(url);
				formEntityUtil.cloneDetails(configUtil.getConfigAggregation(url).pattern().formPattern(), entityPlc,configAcaoCorrente.details());
				msgUtil.msg(PlcBeanMessages.MSG_CLONE_SUCCESS, new Object[] {});
	
			}
	
			if (controleDetalhePaginadoPlc != null) {
				this.controleDetalhePaginadoPlc.reset();
			}
		}
		
		String retorno = cloneEntityAfter();

		contextUtil.getRequestMap().put(PlcJsfConstantes.PLC_LIMPA_CAIXA_EXCLUSAO, Boolean.TRUE);

		return retorno;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected boolean cloneEntityBefore()  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcCloneEntityBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected String cloneEntityAfter()  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcCloneEntityAfter>(){});
		return defaultNavigationFlow;
	}


	protected boolean isItemsUseCase () {
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		FormPattern pattern = configUtil.getConfigAggregation(url).pattern().formPattern();
		return pattern == FormPattern.Rel || pattern == FormPattern.Sel || pattern == FormPattern.Con || FormPattern.Ctb.equals(pattern)
		     || FormPattern.Tab.equals(pattern) || FormPattern.Smd.equals(pattern);

	}

	/**
	 * Retorna a configuração de detalhe. Nunca retorna null, pois lança exceção caso não consiga achar a configuração.
	 * Não deve ser chamado caso detCorrPlc esteja vazio.
	 * @return configuração do detalhe de acordo com o detCorrPlc.
	 * 
	 */
	public PlcConfigDetail getConfigDetail(PlcConfigAggregationPOJO currentConfigAcao, String nomeDetalhe)  {

		if (currentConfigAcao==null) {
			throw new PlcException("Não há configuração para o padrão do formulário");
		}
		PlcConfigDetail[] detalhes = currentConfigAcao.details();
		if (detalhes==null||detalhes.length==0) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_CONFIGDETAIL_NOT_FOUND, new Object[]{nomeDetalhe}, true);
		}
		PlcConfigDetail detalhe = null;
		for (PlcConfigDetail _detalhe : detalhes) {
			if (nomeDetalhe.equals(_detalhe.collectionName())) {
				detalhe = _detalhe;
				break;
			}
		}
		
		return detalhe;
	}
	
	/**
	 * @param entityPlc
	 * @param clearAttribute se for true, limpa o indicador da sessão para limpar o arquivo
	 */
	public void clearFileAttachInSession(Object entityPlc, Boolean clearAttribute) {
		this.entityPlc = entityPlc;
		if (entityPlc != null) {
			Field[] fields = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(entityPlc.getClass(), PlcFileAttach.class);
			String action = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			for (int i = 0; i < fields.length; i++) {
				contextUtil.getRequest().getSession().removeAttribute(action + "_" + fields[i].getName());
				if (clearAttribute) {
					contextUtil.getRequest().getSession().removeAttribute(action + "_" + fields[i].getName() + "_clear");
				} else {
					contextUtil.getRequest().getSession().setAttribute(action + "_" + fields[i].getName() + "_clear","clear");
				}
			}
		}	
	}
	
	public void setFileAttachInEntity(Object entityPlc) {
		this.entityPlc = entityPlc;
		if (entityPlc != null) {
			Field[] fields = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(entityPlc.getClass(), PlcFileAttach.class);
			String action = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			for (int i = 0; i < fields.length; i++) {
				try {
					String nameFile = action + "_" + fields[i].getName();
					IPlcFile file = (IPlcFile) contextUtil.getRequest().getSession().getAttribute(action + "_" + fields[i].getName());
					if (file == null) {
						String clear = (String) contextUtil.getRequest().getSession().getAttribute(nameFile + "_clear");
						if (StringUtils.isNotEmpty(clear)) {
							file = (IPlcFile) propertyUtilsBean.getProperty(entityPlc, fields[i].getName());
							if (file != null && file.getId() != null && propertyUtilsBean.isReadable(file, PlcConstants.VO.IND_EXC_PLC))  {
								propertyUtilsBean.setProperty(file, PlcConstants.VO.IND_EXC_PLC, PlcConstants.SIM);
							}
						}
					} else {
						propertyUtilsBean.setProperty(entityPlc, fields[i].getName(), contextUtil.getRequest().getSession().getAttribute(nameFile));
					}
					
				} catch (Exception e) {
					throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_REFLECTION, new Object[] { entityPlc.getClass().getName(), fields[i].getName() }, true);	
				}
			}
		}
	}
	
//	public void setFileAttachInSession(Object entityPlc) {
//		this.entityPlc = entityPlc;
//		if (entityPlc != null) {
//			Field[] fields = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(entityPlc.getClass(), PlcFileAttach.class);
//			String action = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
//			PlcFileAttach fileAttach;
//			for (int i = 0; i < fields.length; i++) {
//				fileAttach = fields[i].getAnnotation(PlcFileAttach.class) ;
//				try {
//					if (fileAttach.image()) {
//						contextUtil.getRequest().getSession().setAttribute(action + "_" + fields[i].getName(), (IPlcFile) propertyUtilsBean.getProperty(entityPlc, fields[i].getName()));
//		        	} else {
//		        		List<IPlcFile>
//		        		Map<String, PlcFile> fileMap = (Map<String, PlcFile>) contextUtil.getRequest().getSession().getAttribute(controllerName + "_" + property); 
//		        		if (fileMap == null) {
//		        			fileMap  = new HashMap<String, PlcFile>();
//		        		}
//		    			fileMap.put(file.getNome(), (PlcFile) file);
//		        	}
//					
//					
//				} catch (Exception e) {
//					throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_REFLECTION, new Object[] { entityPlc.getClass().getName(), fields[i].getName() }, true);	
//				}
//			}
//		}
//	}
	
}
