/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.validation;

import java.util.List;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.extensions.validator.core.InvocationOrder;
import org.apache.myfaces.extensions.validator.core.interceptor.ValidationExceptionInterceptor;
import org.apache.myfaces.extensions.validator.core.metadata.MetaDataEntry;
import org.apache.myfaces.extensions.validator.core.property.PropertyInformationKeys;
import org.apache.myfaces.extensions.validator.core.storage.FacesMessageStorage;
import org.apache.myfaces.extensions.validator.core.validation.message.LabeledMessage;
import org.apache.myfaces.extensions.validator.core.validation.strategy.ValidationStrategy;
import org.apache.myfaces.extensions.validator.internal.UsageCategory;
import org.apache.myfaces.extensions.validator.internal.UsageInformation;
import org.apache.myfaces.extensions.validator.util.ExtValUtils;
import org.apache.myfaces.extensions.validator.util.ReflectionUtils;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.view.jsf.component.IPlcComponent;


/**
 * Interceptor responsÃ¡vel por ajustar a mensagem de validaÃ§Ã£o, adicionando o Label a Mensagem de erro.
 * 
 * @author Igor GuimarÃ£es
 */


@InvocationOrder(100)
@UsageInformation(UsageCategory.INTERNAL)
public class PlcValidationExceptionInterceptor implements ValidationExceptionInterceptor {

	protected final Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public boolean afterThrowing(UIComponent uiComponent, MetaDataEntry metaDataEntry, Object convertedObject, ValidatorException validatorException, ValidationStrategy validatorExceptionSource) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		FacesMessage facesMessage = ExtValUtils.convertFacesMessage(validatorException.getFacesMessage());

		String numLinha = "";
		if (uiComponent.getAttributes().get("_numLinha")!=null) {
			numLinha = uiComponent.getAttributes().get("_numLinha").toString();
		}

		trataMensagemEmListas(facesContext, uiComponent, facesMessage, numLinha);

		tryToUseLabel(facesContext, uiComponent, metaDataEntry, facesMessage);		

		PlcI18nUtil i18nUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		String msgLinha	= i18nUtil.getMessageCal10n(contextUtil.getRequest(), PlcBeanMessages.JCOMPANY_ERROR_LINE_N, new String[]{numLinha});

		List<FacesMessage> facesMessages = facesContext.getMessageList();
		for (FacesMessage _facesMessage : facesMessages) {
			if (facesMessage.getDetail()==null || _facesMessage.getDetail().equals(facesMessage.getDetail()) || 
					(
							_facesMessage.getDetail().startsWith(facesMessage.getDetail().substring(0, facesMessage.getDetail().length()-1)) && // retirar Ãºltimo ponto 
							_facesMessage.getDetail().contains(msgLinha.substring(0, msgLinha.length()-3)) // retirar Ãºltimo ponto e dois espaÃ§os

					)

			)
				return false;
		}

		return true;
	}

	private void tryToUseLabel(FacesContext facesContext, UIComponent uiComponent, MetaDataEntry metaDataEntry, FacesMessage facesMessage) {

		PlcI18nUtil i18nUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class, QPlcDefaultLiteral.INSTANCE);
		//String label = i18nUtil.getMessage("label."+uiComponent.getId());
		
		String changeIdComponentToPropertie = null;
		// IF utilizado para converter um ID que contém underscore ("_") em uma KEY válida com ponto (".") 
		 if(uiComponent.getId()!= null && !uiComponent.getId().equals("")){ 
		    changeIdComponentToPropertie = uiComponent.getId().replaceAll("_", "."); 
		 } 
		 
		 String label = i18nUtil.getMessage("label."+(changeIdComponentToPropertie)); 
		
		if (label == null || label.startsWith("???")) {
			label = (String) ReflectionUtils.tryToInvokeMethod(uiComponent, ReflectionUtils.tryToGetMethod(uiComponent.getClass(), "getLabel"));
		}
		if (label == null) {
			label = uiComponent.getAttributes().get("label")!=null?uiComponent.getAttributes().get("label").toString():"";
		} 
		if (label == null) {
			label = uiComponent.getClientId(facesContext);
		}

		// override the label if the annotation provides a label
		if (metaDataEntry != null && metaDataEntry.getProperty(PropertyInformationKeys.LABEL) != null) {
			label = metaDataEntry.getProperty(PropertyInformationKeys.LABEL, String.class);
		}

		//TODO: Verficar se utilizaremos Mensagens no Componentes e propagar a mensagem correta
		if (facesMessage instanceof LabeledMessage) {
			//((LabeledMessage) facesMessage).setLabelText(label);
		} else {
			// if someone uses a normal faces message
		}

		// ForÃ§a a colocar o label na mensagem
		for (int i = 0; i < 3; i++) {
			ExtValUtils.tryToPlaceLabel(facesMessage, label, i);
		}

	}

	private void trataMensagemEmListas(FacesContext facesContext, UIComponent uiComponent, FacesMessage facesMessage, String numLinha) {

		try {

			String summary = facesMessage.getSummary();
			if (!numLinha.equals("")) {
				summary = getMessageWithLineNumber(summary, uiComponent, numLinha);
				facesMessage.setDetail(summary);
				facesMessage.setSummary(summary);
			}

			FacesMessageStorage facesMessageStorage = ExtValUtils.getStorage(FacesMessageStorage.class, FacesMessageStorage.class.getName());
			if(facesMessageStorage != null) {
				facesMessageStorage.addAll();
				ExtValUtils.resetStorage(FacesMessageStorage.class, FacesMessageStorage.class.getName());
			}

		} catch (Exception e) {
			logger.fine(e.getLocalizedMessage());
		}
	}	

	private String getMessageWithLineNumber(String mensagem, UIComponent componente, String numLinha) {

		PlcI18nUtil i18nUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcURLUtil urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);

		String chaveI18nDetalhe = "";
		String msgLinha			= 	i18nUtil.getMessageCal10n(contextUtil.getRequest(), PlcBeanMessages.JCOMPANY_ERROR_LINE_N, new String[]{numLinha.toString()});
		PlcConfigAggregationPOJO configAcaoCorrente =  configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
		FormPattern logica 			= configAcaoCorrente.pattern().formPattern();
		// Se for metre-detalhe/subdetalhe ou mantÃ©m detalhe/subdetalhe adiciona o nome da coleÃ§Ã£o
		if ( (logica.equals(FormPattern.Mad)) || (logica.equals(FormPattern.Mas)) || (logica.equals(FormPattern.Mdt))|| (logica.equals(FormPattern.Mds)) ){
			// Adicionando na mensagem o nome da coleÃ§Ã£o, no padrÃ£o jcompany [ nome do detalhe ]
			if (componente instanceof IPlcComponent) {
				chaveI18nDetalhe = ((IPlcComponent)componente).getPropertyChaveI18nDetalhe();
			}
			if (chaveI18nDetalhe != null && !chaveI18nDetalhe.equals("")){
				chaveI18nDetalhe 	=  i18nUtil.mountLocalizedMessageAnyBundle(contextUtil.getRequest(), chaveI18nDetalhe, new String[]{});
				chaveI18nDetalhe 	= " [ " + chaveI18nDetalhe + " ]";
			}
			else {
				// Se nÃ£o tiver detalhe nÃ£o adiciona na mensagem
				String clientId = componente.getClientId();
				String detalhe = "";
				if(!(clientId.contains("plcLogicaItens") || clientId.contains("itensPlc") ) && clientId.indexOf("corpo:formulario:") >= 0) {
					clientId = clientId.substring(clientId.indexOf("corpo:formulario:") + "corpo:formulario:".length(), clientId.length());
					detalhe = clientId.substring(0, clientId.indexOf(":"));
					detalhe = detalhe.substring(0,1).toUpperCase() + detalhe.substring(1, detalhe.length());
					chaveI18nDetalhe = " [ " + detalhe + " ]"; ;
				}			
			}
		}

		if (!mensagem.contains(msgLinha)) {
			// monta a mensagem
			if(mensagem.endsWith(".")) {
				mensagem = mensagem.substring(0, mensagem.length() - 1);
			}
			mensagem = mensagem + " " + msgLinha + chaveI18nDetalhe;
		}
		return mensagem;
	}  
	
	private boolean skipValidation() {

		HttpServletRequest request = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()); 
		String skipValidation = request.getParameter("skipValidation");
		return Boolean.parseBoolean(skipValidation);
	}	

}
