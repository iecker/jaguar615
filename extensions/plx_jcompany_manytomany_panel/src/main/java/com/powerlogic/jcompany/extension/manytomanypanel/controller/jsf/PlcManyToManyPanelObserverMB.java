/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.extension.manytomanypanel.controller.jsf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.bindingtype.PlcEditAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcHandleButtonsAccordingUseCaseAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcSaveBefore;
import com.powerlogic.jcompany.controller.jsf.PlcBaseEditMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseSaveMB;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.extension.manytomanypanel.commons.PlcManyToManyPanelUtil;
import com.powerlogic.jcompany.extension.manytomanypanel.metadata.PlcManyToManyPanelConfig;

/**
 * Observer para controle de ações de funcionamento do ManyToManyPanel,
 * interceptando a busca, a persistencia e a exibição de botões
 * 
 * @author Mauren Ginaldo Souza
 * @since jun/2012
 * 
 */
public class PlcManyToManyPanelObserverMB {

	@Inject @Named("plcAction")
	protected PlcBaseMB plcAction;

	/**
	 * Utilizando o Observer "@PlcEditAfter" para que após a recuperação da entidade principal
	 * montar as listas
	 * 
	 * @param action
	 * @return
	 */
	protected String editAfter(@Observes @PlcEditAfter PlcBaseEditMB action)  {
		
		PlcManyToManyPanelUtil manyToManyPanelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcManyToManyPanelUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcManyToManyPanelConfig config = manyToManyPanelUtil.getPlcManyToManyPanelConfig();
		
		if (config != null) {

			try {

				//Buscando a lista de origem
				List listPanelSource = manyToManyPanelUtil.findList(config.panelClass().newInstance());
						
				//Buscando a lista de destino
				Object objectAssociation =  manyToManyPanelUtil.createObjectAssociation(plcAction.getEntityPlc());
				List listPanelTarget = manyToManyPanelUtil.findList(objectAssociation);
				
				manyToManyPanelUtil.adjustLists(plcAction, config, listPanelSource, listPanelTarget);
				
			} catch (PlcException plcE) {
				throw plcE;	
			} catch (Exception e) {
				new PlcException(e);	
			}
		}	
		
		return action.getDefaultNavigationFlow();
		
	}
	
	
	/**
	 * Utilizando o Observer "@PlcSaveBefore", interceptando o "saveBefore" da entidade principal da agregação e realizando a persistencia
	 * da entidade associativa
	 * 
	 * @param action
	 * @return
	 */
	protected boolean saveBefore(@Observes @PlcSaveBefore PlcBaseSaveMB action)  {
		
		PlcManyToManyPanelUtil manyToManyPanelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcManyToManyPanelUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcManyToManyPanelConfig config = manyToManyPanelUtil.getPlcManyToManyPanelConfig();
		
		if (config != null) {
		
			try {
				
				List listAssociation = (List) PropertyUtils.getProperty(plcAction, "listAssociation");	
				List listPanelTarget = (List) PropertyUtils.getProperty(plcAction, "listPanelTarget");
				List listAssociationFinal = new ArrayList(); 
						
				Object entityMaster = plcAction.getEntityPlc();
				Object associationEntity  = null;
				Object panelEntity = null;
				
				// Iniciando a montagem da lista de objetos a serem persistidos (inseridos ou removidos)
				
				// Buscando os removidos
				for (Object objectAssociation : listAssociation) {
					panelEntity = PropertyUtils.getProperty(objectAssociation, config.propertyNameEntityPanel());
					if (!listPanelTarget.contains(panelEntity)) {
						// é para remover, então marca o "indExcPlc"
						PropertyUtils.setProperty(objectAssociation, PlcConstants.VO.IND_EXC_PLC, PlcConstants.SIM);
						listAssociationFinal.add(objectAssociation);
					}
				}
				
				//Tirando os removidos da lista orignal para otimizar o laço
				listAssociation.removeAll(listAssociationFinal);
				
				boolean achou = false;
				// Buscando os que devem ser inseridos
				for (Object objectPanel : listPanelTarget) {
					for (Object objectAssociation : listAssociation) {
						panelEntity = PropertyUtils.getProperty(objectAssociation, config.propertyNameEntityPanel());
						if (objectPanel.equals(panelEntity)) {
							achou = true;
							break;
						}	
					}
					//se não encontrou então é para inserir
					if (!achou) {
						associationEntity = config.associationClass().newInstance();
						PropertyUtils.setProperty(associationEntity, config.propertyNameEntityMaster(), entityMaster);
						PropertyUtils.setProperty(associationEntity, config.propertyNameEntityPanel(), objectPanel);
						listAssociationFinal.add(associationEntity);
					}	
					achou = false;
				}
				
				//Montada a lista, vamos persistir tudo de uma vez otimizando a transação
				manyToManyPanelUtil.save(listAssociationFinal);
				
				//Atualizando a lista associativa
				Object objectAssociation =  manyToManyPanelUtil.createObjectAssociation(plcAction.getEntityPlc());
				PropertyUtils.setProperty(plcAction, "listAssociation", manyToManyPanelUtil.findList(objectAssociation));
				
				
			} catch (Exception e) {
				new PlcException(e);	
			}
			
			return false;
		}	
			
		return true;
		
	}
	
	
	/**
	 * Método responsavel por ocultar o botao Novo, Clonar, Excluir, Visualiza Documento
	 * @param action
	 * @throws PlcException
	 */
	public void handleButtonsAccordingUseCaseAfter(@Observes @PlcHandleButtonsAccordingUseCaseAfter PlcBaseMB action) throws PlcException {

		
		PlcManyToManyPanelUtil manyToManyPanelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcManyToManyPanelUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcManyToManyPanelConfig config = manyToManyPanelUtil.getPlcManyToManyPanelConfig();
		
		if (config != null) {
		
				PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
				
				Map<String, Object> requestMap = contextUtil.getRequestMap();
				HttpServletRequest request = contextUtil.getRequest();
				
				requestMap.put(PlcConstants.ACAO.EXIBE_BT_INCLUIR, PlcConstants.NAO_EXIBIR);
				requestMap.put(PlcConstants.ACAO.EXIBE_BT_CLONAR, PlcConstants.NAO_EXIBIR);
				requestMap.put(PlcConstants.ACAO.EXIBE_BT_EXCLUIR, PlcConstants.NAO_EXIBIR);
				requestMap.put(PlcConstants.ACAO.EXIBE_BT_VISUALIZA_DOCUMENTO, PlcConstants.NAO_EXIBIR);

		}
	}


}
