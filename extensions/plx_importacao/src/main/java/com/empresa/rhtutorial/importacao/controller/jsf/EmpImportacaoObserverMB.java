package com.empresa.rhtutorial.importacao.controller.jsf;

import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.empresa.rhtutorial.importacao.commons.EmpImportacaoUtil;
import com.empresa.rhtutorial.importacao.metadata.EmpConfigImportacao;
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

public class EmpImportacaoObserverMB {
	  protected @Inject @QPlcDefault PlcMetamodelUtil metamodelUtil;
	  protected @Inject @QPlcDefault PlcViewJsfUtil visaoJsfUtil;

	  /**
	   * Método responsável por ocultar o botao Novo, Gravar e proteger form e o check-box de exclusão
	   * @param action
	   * @throws PlcException
	   */
	   public void handleButtonsAccordingUseCaseAfter(@Observes @PlcHandleButtonsAccordingUseCaseAfter PlcBaseMB action) throws PlcException {
	      EmpConfigImportacao configImportacao = EmpImportacaoUtil.getInstance().getConfigImportacao();
	      if (configImportacao!= null) {
	         if (configImportacao.ehConsultaManterClasse()) {
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
	 * Método responsável por desabilitar a função do F7-Novo...
	 * @param action
	 * @throws PlcException
	 */
	   public void newItemsBefore(@Observes @PlcNewItemsBefore PlcBaseMB action) throws PlcException {
	      EmpConfigImportacao configImportacao = EmpImportacaoUtil.getInstance().getConfigImportacao();
	      if (configImportacao != null) {
	         throw new PlcException("error");
	      }
	   }

	/**
	 * Método responsável por impedir que novos registros sejam inseridos, ou existentes não sejam excluídos
	 * @param action
	 * @throws PlcException
	 */
	   public void saveTabularBefore(@Observes @PlcSaveTabularBefore PlcBaseSaveMB action) throws PlcException {
	      EmpConfigImportacao configImportacao = EmpImportacaoUtil.getInstance().getConfigImportacao();
	      if (configImportacao!= null) {
	         if (configImportacao.ehConsultaManterClasse()) {
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

