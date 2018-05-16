/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.extension.tabular.controller.jsf;

import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.bindingtype.PlcHandleButtonsAccordingUseCaseAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcNewItemsBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcSaveTabularBefore;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseSaveMB;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.extension.tabular.commons.PlcEditOnlyTabularUtil;
import com.powerlogic.jcompany.extension.tabular.metadata.PlcConfigEditOnlyTabular;

/**
 * 
 * @author igor.guimaraes
 * 
 */
public class PlcTabularObserverMB {

	protected @Inject @QPlcDefault PlcMetamodelUtil metamodelUtil;

	protected @Inject @QPlcDefault PlcViewJsfUtil visaoJsfUtil;
	/**
	 * Método responsavel por ocultar o botao Novo, Gravar e proteger form e o check-box de exclusão, caso o mesmo exista...
	 * @param action
	 * @throws PlcException
	 */
	public void handleButtonsAccordingUseCaseAfter(@Observes @PlcHandleButtonsAccordingUseCaseAfter PlcBaseMB action) throws PlcException {

		PlcConfigEditOnlyTabular configTabular = PlcEditOnlyTabularUtil.getInstance().getConfigEditOnlyTabular();

		if (configTabular != null) {
			if (configTabular.isReadOnlyTabular()) {
				
				PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
				
				Map<String, Object> requestMap = contextUtil.getRequestMap();
				HttpServletRequest request = contextUtil.getRequest();
				
				requestMap.put(PlcConstants.ACAO.EXIBE_BT_INCLUIR, PlcConstants.NAO_EXIBIR);
				requestMap.put(PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.NAO_EXIBIR);
				requestMap.put(PlcConstants.ACAO.EXIBE_BT_VISUALIZA_DOCUMENTO, PlcConstants.NAO_EXIBIR);
				
				requestMap.put("visualizaCampoPlc", "p");
				
				visaoJsfUtil.hideWithLabel(request, "indExcPlc");
			}
		}
	}

	/**
	 * Metodo responsavel por desabilitar a funcao do F7-Novo...
	 * @param action
	 * @throws PlcException
	 */
	public void newItemsBefore(@Observes @PlcNewItemsBefore PlcBaseMB action) throws PlcException {

		PlcConfigEditOnlyTabular configTabular = PlcEditOnlyTabularUtil.getInstance().getConfigEditOnlyTabular();
		if (configTabular != null) {
			throw new PlcException("error");
		}
	}

	/**
	 * Metodo responsavel por impedir que novos registros sejam inseridos, ou que registros existentes nao sejam excluidos...
	 * @param action
	 * @throws PlcException
	 */
	public void saveTabularBefore(@Observes @PlcSaveTabularBefore PlcBaseSaveMB action) throws PlcException {

		PlcConfigEditOnlyTabular configTabular = PlcEditOnlyTabularUtil.getInstance().getConfigEditOnlyTabular();
		
		if (configTabular != null) {
			
			if (configTabular.isReadOnlyTabular()) {

				List<Object> itensPlc = action.getLogicaItensPlc().getItensPlc();
				
				for (int i = 0; i < itensPlc.size(); i++) {
					
					Object plcBaseVO = itensPlc.get(i);
					
					PlcEntityInstance instance = metamodelUtil.createEntityInstance(plcBaseVO);
					
					if (instance.getId() == null && StringUtils.isBlank(instance.getIdAux())) {
						itensPlc.remove(i);
					} else {
						instance.setIndExcPlc(false);
						instance.setIndExcPlc("N");
					}
				}

			}
		}
	}

}
