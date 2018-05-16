/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcConstants.VO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.util.PlcBeanCloneUtil;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.domain.PlcConfigDomainPOJO;
import com.powerlogic.jcompany.controller.bindingtype.PlcSaveAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcSaveBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcSaveCompleteEntityBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcSaveCrudTabularCopyArgsAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcSaveCrudTabularCopyArgsBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcSaveTabularAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcSaveTabularBefore;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcDeclarativeValidationUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcDomainLookupUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcEntityUtil;
import com.powerlogic.jcompany.controller.util.PlcClassLookupUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.controller.util.PlcValidationUtil;

/**
 * Realiza a gravação da agregação de entidades (dados) do formulário
 */
@QPlcDefault
public class PlcBaseSaveMB extends PlcBaseParentMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected transient Logger log;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	


	@Inject @QPlcDefault 
	protected PlcClassLookupUtil classeLookupUtil;

	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;	

	@Inject @QPlcDefault 
	protected PlcEntityUtil entityUtil;

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	@Inject @QPlcDefault 
	protected PlcMsgUtil msgUtil;

	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;	

	@Inject @QPlcDefault 
	protected PlcBaseValidation validacaoAction; 

	@Inject @QPlcDefault 
	protected PlcDeclarativeValidationUtil validacaoDeclarativaUtil;

	@Inject @QPlcDefault 
	protected PlcValidationUtil validacaoUtil;

	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;

	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;

	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;		

	@Inject @QPlcDefault 
	protected PlcBeanCloneUtil beanCloneUtil;	
	
	@Inject @QPlcDefault 
	protected PlcEntityCommonsUtil entityCommonsUtil;
	
	@Inject @Named("plcAction")
	protected PlcBaseMB plcAction; 	
	
	@Inject @QPlcSpecific
	protected PlcBaseCreateMB baseCreateMB; 
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	/**
	 * Realiza a gravação simples da entidade raiz e agregação definida. Importante: também executa a exclusão de detalhes,
	 * quando marcados pelo usuário.
	 */
	public String save(Object entityPlc)  {

		this.entityPlc = entityPlc;

		if (contextUtil.getRequest().getAttribute(PlcConstants.ACAO.EVENTO)==null)
			contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_GRAVAR);
				
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

		if (plcControleConversacao.getModoPlc().equals(PlcConstants.MODOS.MODO_INCLUSAO))
			context.setMode(PlcBaseContextVO.Mode.INCLUSAO);
		else
			context.setMode(PlcBaseContextVO.Mode.ALTERACAO);

		// Limpando flag de exibir mensagem alerta alteração
		plcControleConversacao.setAlertaAlteracaoPlc(null);
		
		// Limpando flag de exibir mensagem alerta exclusaoDetalhe
		plcControleConversacao.setAlertaExclusaoDetalhePlc(null);

		//Adicionando VO no request para envio dos dados pelo jMonitor
		contextUtil.getRequest().setAttribute(PlcConstants.ENTITY_OBJECT, entityPlc);

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		FormPattern pattern = configUtil.getConfigAggregation(url).pattern().formPattern();

		if (FormPattern.Usu.equals(pattern)){
			try {
				propertyUtilsBean.setProperty(entityPlc, configUtil.getConfigAggregation(url).userpref().argument(),
						contextUtil.getRequest().getUserPrincipal().getName());	
			} catch (Exception e){ 
				throw new PlcException("PlcBaseSaveMB", "save(object)", e, log, "");
			}	
		}

		if (FormPattern.Mdt.equals(pattern)
				|| FormPattern.Mad.equals(pattern)
				|| FormPattern.Mds.equals(pattern)
				|| FormPattern.Mas.equals(pattern)){
			this.entityUtil.cleanDetails(configUtil.getConfigAggregation(url), entityPlc);
		}

		if (saveBefore(pattern,entityPlc,context)) {

			entityPlc = saveCompleteEntityBefore(pattern,entityPlc,context);
	
			//setando arquivo anexado na entidade;
			baseCreateMB.setFileAttachInEntity(entityPlc);
			
			boolean existeDetalheParaExclusao = false;
	
			if (FormPattern.Mdt.equals(pattern)
					|| FormPattern.Mad.equals(pattern)
					|| FormPattern.Mds.equals(pattern)
					|| FormPattern.Mas.equals(pattern)){
				existeDetalheParaExclusao = this.entityUtil.checkDetailToDelete(configUtil.getConfigAggregation(url), entityPlc);
			}
			context.setDeleteModeAux(configUtil.getConfigAggregation(url).pattern().exclusionMode().toString());
			//atualizando sitHistoricoPlc para detalhes/sub-detalhes com exclusão lógica
			if (existeDetalheParaExclusao && 
					((context.getLogicalExclusionDetails() != null && context.getLogicalExclusionDetails().size() > 0)
							|| (context.getLogicalExclusionSubDetails() != null && context.getLogicalExclusionSubDetails().size() > 0))) {
				entityCommonsUtil.updateSitHistoricoPlc(context, entityPlc, VO.SIT_INATIVO, true);
			}
	
			boolean deleteFileAttach = this.entityUtil.checkFileToDelete(configUtil.getConfigAggregation(url), entityPlc);
	
			entityPlc = iocControleFacadeUtil.getFacade(url).saveObject(context, entityPlc);
	
			contextUtil.getRequest().setAttribute(PlcConstants.VO.PREFIXO_OBJ + configUtil.getConfigAggregation(url).entity().getName(), entityPlc);
	
			if (deleteFileAttach) {
				msgUtil.msg(PlcBeanMessages.MSG_SAVE_SUCCESS_FILE_DELETE, new Object[] {});
			} if (existeDetalheParaExclusao) {
				msgUtil.msg(PlcBeanMessages.MSG_SAVE_SUCCESS_DETAIL_DELETE, new Object[] {});
			} else {
				msgUtil.msg(PlcBeanMessages.MSG_SAVE_SUCCESS, new Object[] {});
			}
			
			// Verifica se o padrão utiliza entrada em Lote.
			if (configUtil.getConfigCollaboration(url).behavior().batchInput()) {
	
				// Se tor logica crudtabular deve-se retirar a lista anterior e adicionar novos
				if (FormPattern.Mad.equals(pattern)){
					PlcConfigAggregationPOJO configAcaoCorrente = configUtil.getConfigAggregation(url);
					for (PlcConfigDetail detalhe : configAcaoCorrente.details()) {
						if (detalhe != null && this.entityUtil.isDetailValid(detalhe.clazz(),detalhe.collectionName())) {
							baseCreateMB.newDetail(entityPlc, detalhe);
						}
					}
				}else{
					try {
						Object entidadeNova = configUtil.getConfigAggregation(url).entity().newInstance();
						getPlcRequestControl().setDetCorrPlc(null);
						plcAction.create();
						beanCloneUtil.copyProperties(entityPlc, entidadeNova);
	
					} catch(PlcException plcE){
						throw plcE;					
					} catch (Exception e) {
						throw new PlcException("PlcBaseSaveMB", "save(object)", e, log, "");
					} 				
				}
	
			} else {
				
				PlcEntityInstance entityPlcInstance = metamodelUtil.createEntityInstance(entityPlc);
	
				this.id 	= entityPlcInstance.getIdAux();
	
				Boolean ehDetalhePaginado = (Boolean)contextUtil.getRequest().getAttribute(PlcJsfConstantes.DETALHE_PAGINADO);
				if (ehDetalhePaginado != null && ehDetalhePaginado.booleanValue()) {
					//passando o id para a edição.
					plcAction.setKeyPlc(id);
					plcAction.edit();
				}
				plcControleConversacao.setModoPlc(PlcConstants.MODOS.MODO_EDICAO);
			}
	
			//Se chegou aqui, é porque gravou ok, sem problemas.
			contextUtil.getRequestMap().put(PlcJsfConstantes.PLC_LIMPA_CAIXA_EXCLUSAO, Boolean.TRUE);

		}
		
		//limpando imagens temporárias da sessão
		baseCreateMB.clearFileAttachInSession(entityPlc, true);
		
		log.debug( "Vai encerrar gravacao simples");
		
		return saveAfter(pattern,entityPlc,context);

	}
	
	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param context Parametros (metadados) para a requisição
	 * @param entityPlc Entidade raiz da agregação
	 * @param pattern Padrão corrente
	 */
	protected boolean saveBefore(FormPattern pattern, Object entityPlc, PlcBaseContextVO context)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcSaveBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param context Parametros (metadados) para a requisição
	 * @param entityPlc Entidade raiz da agregação
	 * @param pattern Padrão corrente
	 */
	protected String saveAfter(FormPattern pattern, Object entityPlc, PlcBaseContextVO context) {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcSaveAfter>(){});
		return defaultNavigationFlow;
	}


	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param context Parametros (metadados) para a requisição
	 * @param entityPlc Entidade raiz da agregação
	 * @param pattern Padrão corrente
	 */
	protected Object saveCompleteEntityBefore(FormPattern pattern, Object entityPlc, PlcBaseContextVO context)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcSaveCompleteEntityBefore>(){});
		return entityPlc;
	}

	/**
	 * Grava coleção de entidades, quando utilizando padrão "tabular" (Tab) ou "crudtabular" (Ctb)
	 */
	public String save(PlcEntityList entityListPlc)  {

		this.entityListPlc = entityListPlc;

		if (contextUtil.getRequest().getAttribute(PlcConstants.ACAO.EVENTO)==null)
			contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_GRAVAR);

		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

		if (plcControleConversacao.getModoPlc().equals(PlcConstants.MODOS.MODO_INCLUSAO))
			context.setMode(PlcBaseContextVO.Mode.INCLUSAO);
		else
			context.setMode(PlcBaseContextVO.Mode.ALTERACAO);


		// Incluido para acertar problema de informação modo quando grava
		contextUtil.getRequest().setAttribute(PlcConstants.MODOS.MODO, PlcConstants.MODOS.MODO_EDICAO);

		if (saveItensBefore(entityListPlc,context)) {

			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
	
			PlcConfigAggregationPOJO configAggregationPOJO = configUtil.getConfigAggregation(url);
			PlcConfigDomainPOJO configDominio = configUtil.getConfigDomain(configAggregationPOJO.entity()); 
			String tabularFlagDesprezar = configDominio.despiseProperty();
			
			entityListPlc.setItensPlc(saveTabularDespiseItems(entityListPlc.getItensPlc(),tabularFlagDesprezar));
			
			FormPattern pattern = configAggregationPOJO.pattern().formPattern();
			try {
				if ( FormPattern.Ctb.equals(pattern)){
					entityListPlc.setItensPlc(saveCrudTabularCopyArgs(entityListPlc.getItensPlc(),configAggregationPOJO.pattern()));
				}	
			} catch(PlcException plcE){
				throw plcE;			
			} catch (Exception e) {
				throw new PlcException("PlcBaseSaveMB", "save(list)", e, log, "");
	
			}
			
			// Validação de duplicidade. Esta não pode ser feita pelo BV por se tratar de uma coleção sem agregação em nenhuma outra entidade.
			if (configDominio.testDuplicity() && StringUtils.isNotBlank(tabularFlagDesprezar)) {
				validacaoUtil.validateDuplicate(entityListPlc.getItensPlc(), true, tabularFlagDesprezar);
			}
			
			context.setDeleteModeAux(configUtil.getConfigAggregation(url).pattern().exclusionMode().toString());
	
			iocControleFacadeUtil.getFacade(url).saveTabular(context, configUtil.getConfigAggregation(url).entity(), entityListPlc.getItensPlc());
	
			if (entityListPlc.getItensPlc() != null && !entityListPlc.getItensPlc().isEmpty()){
				msgUtil.msg(PlcBeanMessages.MSG_SAVE_SUCCESS, new Object[] {});
			}
	
			// Verifica se a lógica utiliza entrada em Lote.
			if (configUtil.getConfigCollaboration(url).behavior().batchInput()) {
	
				//			this.plcControleConversacao = null;
				// Se tor logica crudtabular deve-se retirar a lista anterior e adicionar novos
				if ( FormPattern.Ctb.equals(pattern)){
					entityListPlc.setItensPlc(new ArrayList<Object>());
				}
	
			} else {
				plcAction.setEntityPlc(entityPlc);
				plcAction.search();
	
				PlcDomainLookupUtil dominioLookupUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcDomainLookupUtil.class, new PlcNamedLiteral(PlcJsfConstantes.PLC_DOMINIOS));
	
				Class entidade 	= configUtil.getConfigAggregation(url).entity();
	
				classeLookupUtil.storeClassLookup(entidade,entityListPlc.getItensPlc());
				dominioLookupUtil.addDomain(entidade.getSimpleName(), entityListPlc.getItensPlc());
	
				plcControleConversacao.setModoPlc(PlcConstants.MODOS.MODO_EDICAO);
			}
		
	
			//Se chegou aqui, é porque gravou ok, sem problemas.
			contextUtil.getRequestMap().put(PlcJsfConstantes.PLC_LIMPA_CAIXA_EXCLUSAO, Boolean.TRUE);
		}

		return saveItensAfter(entityListPlc,context);		
	}
	
	/**
	 * Cenário onde a entidade principal e a lista estão preenchidas.
	 * Delega conforme o padrão
	 * @param entityPlc
	 * @param entityListPlc
	 */
	public String save(Object entityPlc, PlcEntityList entityListPlc)  {
		
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		FormPattern pattern = configUtil.getConfigAggregation(url).pattern().formPattern();
		
		if (FormPattern.Ctb.equals(pattern)) {
			this.entityPlc = entityPlc;
			return save(entityListPlc);
		} else {
			return save(entityPlc);
		}
		
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param context Parametros (metadados) para a requisição
	 * @param entityListPlc Lista de entidades a serem gravadas
	 */
	protected boolean saveItensBefore(PlcEntityList entityListPlc, PlcBaseContextVO context)  {
		this.setDefaultProcessFlow(true);
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcSaveTabularBefore>(){});
		return this.defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param context Parametros (metadados) para a requisição
	 * @param entityListPlc Lista de entidades a serem gravadas
	 */
	protected String saveItensAfter(PlcEntityList entityListPlc, PlcBaseContextVO context)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcSaveTabularAfter>(){});
		return this.defaultNavigationFlow;
	}

	/**
	 * Padrão Tabular: despreza entidades da coleção caso o valor do campo 'de referencia' esteja vazio
	 */
	protected List<Object> saveTabularDespiseItems(List<Object> itensPlc, String tabularFlagDesprezar) {

		List<Object> itensPlcNova = new ArrayList<Object>(itensPlc);
		
		try {

			if (StringUtils.isNotBlank(tabularFlagDesprezar)) {
		
				for (Object entity : itensPlc) {

					String propDesprezar = tabularFlagDesprezar;
					Object valor = null;

					if (tabularFlagDesprezar.contains("_")){
						int index 			  = tabularFlagDesprezar.indexOf("_");
						String mainProperty  = tabularFlagDesprezar.substring(0,index);
						Object mainEntity = propertyUtilsBean.getNestedProperty(entity, mainProperty);

						propDesprezar = tabularFlagDesprezar.substring(index+1);

						if (propertyUtilsBean.isReadable(mainEntity, propDesprezar))
							valor = propertyUtilsBean.getNestedProperty(mainEntity, propDesprezar);

					} else {
						if (propertyUtilsBean.isReadable(entity, propDesprezar))
							valor = propertyUtilsBean.getNestedProperty(entity, propDesprezar);
					}

					if (propertyUtilsBean.isReadable(entity, propDesprezar) && (valor == null || StringUtils.isBlank(valor.toString()))) {
						itensPlcNova.remove(entity);
					}
				}
			}
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseSaveMB", "saveTabularDespiseItems", e, log, "");
		}
		return itensPlcNova;
	}

	/**
	 * Copia os argumentos da Crud Tabular para os itens da coleção. Os
	 * Argumentos estão definidos na anotação
	 */
	protected List<Object> saveCrudTabularCopyArgs(List<Object> entityListPlc, PlcConfigForm configForm) 
	      throws PlcException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IntrospectionException {

		String[] args = configForm.ctbHeaderProperties();
		String nomePropriedade;
		
		entityListPlc = saveCrudTabularCopyArgsBefore(entityListPlc, configForm);

		for (int i = 0; i < args.length; i++) {
			for (Object item : entityListPlc) {
				nomePropriedade = args[i];
				if (!PlcConstants.VO.IND_EXC_PLC.equals(nomePropriedade) && !PlcConstants.VO.VERSAO.equals(nomePropriedade) && !PlcConstants.VO.DATA_CRIACAO.equals(nomePropriedade) 
						&& !PlcConstants.VO.USUARIO_ULT_ALTERACAO.equals(nomePropriedade) && !PlcConstants.VO.DATA_ULT_ALTERACAO.equals(nomePropriedade) ) {
					if (propertyUtilsBean.isWriteable(item, nomePropriedade)) {
						BeanUtils.setProperty(item, nomePropriedade, propertyUtilsBean.getProperty(entityPlc, nomePropriedade));
					}
				}	
				
			}
		}

		return saveCrudTabularCopyArgsAfter(entityListPlc, configForm);
	}
	
	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected List<Object> saveCrudTabularCopyArgsBefore(List<Object> itensPlc, PlcConfigForm configForm)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcSaveCrudTabularCopyArgsBefore>(){});
		return itensPlc;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */
	protected List<Object> saveCrudTabularCopyArgsAfter(List<Object> itensPlc, PlcConfigForm configForm)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcSaveCrudTabularCopyArgsAfter>(){});
		return itensPlc;
	}


}
