/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.jsf.producer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.util.PlcNomenclatureUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.PlcConfigUrlCollaboration;
import com.powerlogic.jcompany.controller.jsf.PlcBaseCreateMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseDeleteMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseEditMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseFileMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseLogoutMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.PlcBasePortletMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseSaveMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseSearchMB;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;

/**
 * Classe responsável para criação de componentes para weld
 * 
 * @author Moisés Paula, Rogério Baldini
 */
@Named("plcComponentProducer")
@ConversationScoped
public class PlcComponentProducer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, PlcBaseMB> actions = new HashMap<String, PlcBaseMB>();
	
	@Inject
	@QPlcDefault
	protected PlcMetamodelUtil metamodelUtil;

	@Inject
	@QPlcDefault
	protected PlcNomenclatureUtil nomenclaturaUtil;

	@Inject
	@QPlcDefault
	protected PlcContextUtil contextUtil;

	@Inject
	@QPlcDefault
	protected PlcIocControllerUtil iocControleUtil;

	@Inject
	@QPlcDefault
	protected PlcURLUtil urlUtil;

	/**
	 * Injeta conversação para iniciar conversação longa
	 */
	@Inject
	private Conversation conversation = null;

	@Inject
	@QPlcDefault
	protected PlcConfigUtil configUtil;


	/**
	 * Cria action na conversação, ou retorna caso já exista
	 */
	@Produces
	@Named(PlcConstants.PlcJsfConstantes.PLC_MB)
	public PlcBaseMB criaAction() throws InstantiationException, IllegalAccessException {
		boolean conversationTransient = conversation.isTransient(); 
		if (conversationTransient) {
			conversation.begin();
		}

		if (!(getActions().containsKey(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()))) || conversationTransient) {
			
			PlcBaseMB instance = (PlcBaseMB)iocControleUtil.resolveBaseMBInstance(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
			getActions().put(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()), instance);
			return instance;
		} 
		return getActions().get(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
	}


	@Produces
	@Named(PlcJsfConstantes.PLC_CONFIG_URL_COLABORACAO)
	public PlcConfigUrlCollaboration criaConfigUrlColaboracao() {

		PlcConfigUrlCollaboration configUrl = new PlcConfigUrlCollaboration();

		if (configUrl.getNomeColaboracao() == null) {
			configUrl.setNomeColaboracao(urlUtil
					.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
		}
		return configUrl;
	}


	@Produces
	@Named(PlcJsfConstantes.PLC_LOGICA_ITENS_ANTERIOR)
	public PlcEntityList criaLogicaItensAnteriorPlc()  {
		return new PlcEntityList();
	}


	@PreDestroy
	protected void destroy() {

	}


	private boolean producingCreate = false;
	private boolean producingEdit = false;
	private boolean producingDelete = false;
	private boolean producingFile = false;
	private boolean producingLogout = false;
	private boolean producingPortlet = false;
	private boolean producingSave = false;
	private boolean producingSearch = false;

	@Produces @QPlcSpecific
	public PlcBaseCreateMB criaPlcBaseCreateMB(  ){

		if (!producingCreate) {
			producingCreate = true; 
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			String alias = urlUtil.getAliasUseCase(url);

			PlcBaseCreateMB create = iocControleUtil.resolveBaseDelegatedMBClass(PlcBaseCreateMB.class, alias);

			producingCreate = false; 
			return create;
		} else
			return null;
	}

	@Produces @QPlcSpecific
	public PlcBaseEditMB criaPlcBaseEditMB(  ){

		if (!producingEdit) {
			producingEdit = true; 		
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			String alias = urlUtil.getAliasUseCase(url);

			PlcBaseEditMB edit =  iocControleUtil.resolveBaseDelegatedMBClass(PlcBaseEditMB.class, alias);

			producingEdit = false; 
			return edit;
		} else
			return null;
	}


	@Produces @QPlcSpecific
	public PlcBaseDeleteMB criaPlcBaseDeleteMB(  ){

		if (!producingDelete) {
			producingDelete = true; 	
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			String alias = urlUtil.getAliasUseCase(url);

			PlcBaseDeleteMB delete =  iocControleUtil.resolveBaseDelegatedMBClass(PlcBaseDeleteMB.class, alias);

			producingDelete = false; 
			return delete;
		} else
			return null;		
	}

	@Produces @QPlcSpecific
	public PlcBaseFileMB criaPlcBaseFileMB(  ){

		if (!producingFile) {
			producingFile = true; 	
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			String alias = urlUtil.getAliasUseCase(url);

			PlcBaseFileMB file =   iocControleUtil.resolveBaseDelegatedMBClass(PlcBaseFileMB.class, alias);

			producingFile = false; 
			return file;
		} else
			return null;			
	}	

	@Produces @QPlcSpecific
	public PlcBaseLogoutMB criaPlcBaseLogoutMB(  ){

		if (!producingLogout) {
			producingLogout = true; 	
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			String alias = urlUtil.getAliasUseCase(url);

			PlcBaseLogoutMB logout =   iocControleUtil.resolveBaseDelegatedMBClass(PlcBaseLogoutMB.class, alias);
			producingLogout = false; 
			return logout;
		} else
			return null;	
	}		

	@Produces @QPlcSpecific
	public PlcBasePortletMB criaPlcBasePortletMB(  ){

		if (!producingPortlet) {
			producingPortlet = true; 	
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			String alias = urlUtil.getAliasUseCase(url);

			PlcBasePortletMB portlet =    iocControleUtil.resolveBaseDelegatedMBClass(PlcBasePortletMB.class, alias);
			producingPortlet = false; 
			return portlet;
		} else
			return null;			
	}	


	@Produces @QPlcSpecific
	public PlcBaseSaveMB criaPlcBaseSaveMB(  ){

		if (!producingSave) {
			producingSave = true; 	
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			String alias = urlUtil.getAliasUseCase(url);

			PlcBaseSaveMB save =    iocControleUtil.resolveBaseDelegatedMBClass(PlcBaseSaveMB.class, alias);
			producingSave = false; 
			return save;
		} else
			return null;			
	}

	@Produces @QPlcSpecific
	public PlcBaseSearchMB criaPlcBaseSearchMB(  ){

		if (!producingSearch) {
			producingSearch = true; 	
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			String alias = urlUtil.getAliasUseCase(url);

			PlcBaseSearchMB search =    iocControleUtil.resolveBaseDelegatedMBClass(PlcBaseSearchMB.class, alias);
			producingSearch = false; 
			return search;
		} else
			return null;			
	}


	public Map<String, PlcBaseMB> getActions() {
		return actions;
	}


	public void setActions(Map<String, PlcBaseMB> actions) {
		this.actions = actions;
	}
}
