/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcConstants.VO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.PlcBeanCloneUtil;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.controller.bindingtype.PlcDeleteAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcDeleteBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcDeleteCompleteEntityBefore;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcPagedDetailControl;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcPagedDetailControl.DetalhePaginadoController;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;

@QPlcDefault
public class PlcBaseDeleteMB extends PlcBaseParentMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected transient Logger log;

	/**
	 * Controller para detalhes paginados e argumentos em detalhes
	 */
	@Inject @Named(PlcJsfConstantes.PLC_CONTROLE_DETALHE_PAGINADO)
	protected PlcPagedDetailControl controleDetalhePaginadoPlc;
	
	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;		

	
	@Inject @QPlcDefault 
	protected PlcBeanCloneUtil beanCloneUtil;
	
	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;	
	
	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	@Inject @QPlcDefault 
	protected PlcEntityCommonsUtil entityCommonsUtil;
	
	@Inject @QPlcDefault 
	protected PlcMsgUtil msgUtil;
	
	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;
	
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
	@Inject @Named("plcAction")
	protected PlcBaseMB plcAction; 	
	
	@Inject @QPlcSpecific
	protected PlcBaseCreateMB baseCreateMB; 
	
	
	/**
	 * Exclui grafo de entidade principal (ie. Mestre, seus Detalhes e Sub-Detalhe eventuais).
	 */
	public String delete(Object entityPlc)  {

		this.entityPlc = entityPlc;
		
		try {
			contextUtil.getRequest().setAttribute(PlcConstants.ENTITY_OBJECT, beanCloneUtil.cloneBean(entityPlc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		FormPattern pattern = configUtil.getConfigAggregation(url).pattern().formPattern();

		// Se for logica de mestre-detalhe e variantes, e tiver detalhes
		// marcados, revisa acao para gravacao
		if ((FormPattern.Mdt.equals(pattern)
				|| FormPattern.Mad.equals(pattern)
				|| FormPattern.Mds.equals(pattern) || FormPattern.Mas.equals(pattern))
				&& plcControleConversacao != null
				&& StringUtils.isNotBlank(getPlcRequestControl()
						.getIndExcDetPlc())) {
			//registra no request qual foi a ação realizada
			contextUtil.getRequest().setAttribute(PlcJsfConstantes.ACAO.PLC_IND_ACAO_EXCLUIR_DET,"S");
			return plcAction.save();
		}	
		
		//registra no request qual foi a ação realizada
		contextUtil.getRequest().setAttribute(PlcJsfConstantes.ACAO.PLC_IND_ACAO_EXCLUIR,"S");
		
		if (contextUtil.getRequest().getAttribute(PlcConstants.ACAO.EVENTO)==null)
			contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EVENTO, PlcConstants.ACAO.EVT_EXCLUIR);
		
		// Incluido para acertar problema de informação modo quando exclui
		contextUtil.getRequest().setAttribute(PlcConstants.MODOS.MODO, PlcConstants.MODOS.MODO_EXCLUSAO);	

		contextMontaUtil.createContextParam(plcControleConversacao);
		
		PlcBaseContextVO context = (PlcBaseContextVO)contextUtil.getRequest().getAttribute(PlcConstants.CONTEXT);
		
		if (configUtil.getConfigAggregation(url).details() != null) {
			
			Map<String, PlcConfigDetail> mapaDetalhesPaginados = new HashMap<String, PlcConfigDetail>();
			
			PlcConfigDetail[] detalhes = configUtil.getConfigAggregation(url).details();
			
			for (PlcConfigDetail detalhe : detalhes) {
				if(detalhe.navigation() != null && detalhe.navigation().numberByPage() > 0) {
					mapaDetalhesPaginados.put(detalhe.collectionName(), detalhe);
				}
			}
			
			context.setPagedDetails(mapaDetalhesPaginados);
		}

		boolean temExclui = deleteBefore(pattern,entityPlc,context);
		
		String tipoEntidade = configUtil.getConfigAggregation(url).entity().getName();

		if (temExclui && (!configUtil.getConfigCollaboration(url).behavior().deleteValidationUse())) {

			IPlcFacade plc = iocControleFacadeUtil.getFacade(url); 

			// Permite alteracao de modo dinamicamente
			entityPlc = deleteCompleteEntityBefore(entityPlc);

			if (configUtil.getConfigAggregation(url).pattern().exclusionMode().equals(PlcConfigForm.ExclusionMode.FISICAL)) {
				context.setMode(PlcBaseContextVO.Mode.EXCLUSAO);
				plc.deleteObject(context, entityPlc);
			} else {
				entityCommonsUtil.updateSitHistoricoPlc(context, entityPlc, VO.SIT_INATIVO,false);
				context.setMode(PlcBaseContextVO.Mode.ALTERACAO);
				plc.saveObject(context, entityPlc);
			}

			msgUtil.msg(PlcBeanMessages.MSG_DELETE_SUCCESS, new Object[] {});

			contextUtil.getRequest().setAttribute(PlcConstants.VO.PREFIXO_OBJ + configUtil.getConfigAggregation(url).entity().getName(), entityPlc);

			contextUtil.getRequest().removeAttribute(PlcConstants.VO.PREFIXO_OBJ + tipoEntidade);

			// Flag que indica que tudo encerrou ok
			contextUtil.getRequest().setAttribute(PlcConstants.WORKFLOW.EXCLUI_ENCERROU_OK, "S");
			
			plcAction.create();

		}
		
		//limpando imagens temporárias da sessão
		baseCreateMB.clearFileAttachInSession(entityPlc, true);

		return deleteAfter(pattern,entityPlc,context);
	}


	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After"
	 * ou "api" são eventos (método vazios) destinados a especializações nos
	 * descendentes.
	 */
	protected Object deleteCompleteEntityBefore(Object entity)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcDeleteCompleteEntityBefore>(){});
		return entity;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param context Informações de controle para requisição
	 * @param entityPlc Entidade a ser excluída
	 * @param pattern Padrão do formulário
	 */
	protected boolean deleteBefore(FormPattern pattern, Object entityPlc, PlcBaseContextVO context)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcDeleteBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param context Informações de controle para requisição
	 * @param entityPlc Entidade excluída
	 * @param pattern Padrão do formulário
	 */
	protected String deleteAfter(Object vo, Object entityPlc, PlcBaseContextVO context)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcDeleteAfter>(){});
		return defaultNavigationFlow;
	}

	/**
	 * TODO Experimental. Finalizar
	 * Exclui um item de lista tabular ou detalhes.
	 * @param formAuxPlc Campo auxiliar do formulário que deve ter formato [componenteAgregacao]#[indiceExcluido]. 
	 */
	public String deleteItem(String formAuxPlc)  {
		
		// Interpreta formAuxPlc que deve ter formato [componenteAgregacao]#[indiceExcluido]. 
		// Exs: itensPlc#2 para exclusao do item em posicao 3 de um tabular ou
		//      dependente#4 para exclusao do item em posicao 4 de um detalhe dependente em padrão Mestre/Detalhe.
		if (formAuxPlc == null || formAuxPlc.length()<2)
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_DELETE_ITEM);
		
		String collection = StringUtils.substring(formAuxPlc, 0,formAuxPlc.indexOf("#"));
		int index = new Integer(StringUtils.substring(formAuxPlc, formAuxPlc.indexOf("#")+1,formAuxPlc.length()));
		
		if (deleteItemBefore(entityPlc,entityListPlc,collection,index)) {
			if ("itensPlc".equals(collection)) {
				// padrão Tab
				Object entity = entityListPlc.getItensPlc().get(index);
				try {
					if (PropertyUtils.getProperty(entity,"id")==null) {
						entityListPlc.getItensPlc().remove(index);
					} else {
						this.entityPlc = entity;
						delete(entityPlc);
						entityListPlc.getItensPlc().remove(index);
					}
				} catch(PlcException plcE){
					throw plcE;				
				} catch (Exception e) {
					throw new PlcException("PlcBaseDeleteMB", "deleteItem", e, log, "");
				}
			} else {
				// padrao Ctb, Mad, Mdt ou Mds
				
			}
		}
		
		return deleteItemAfter(entityPlc,entityListPlc);	
	}
	
	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param index indice atual da linha a ser excluida
	 * @param collection nome da colecao
	 * @param entityListPlc lista de entidades para tabular
	 * @param entityPlc entidade para demais padrões
	 */
	protected boolean deleteItemBefore(Object entityPlc, PlcEntityList entityListPlc, String collection, int index)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcDeleteBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param entityListPlc lista de entidades para tabular
	 * @param entityPlc entidade para demais padrões
	 */
	protected String deleteItemAfter(Object entityPlc, PlcEntityList entityListPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcDeleteAfter>(){});
		return defaultNavigationFlow;
	}

}
