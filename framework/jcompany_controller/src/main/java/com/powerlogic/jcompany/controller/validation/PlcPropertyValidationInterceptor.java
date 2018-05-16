/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.validation;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.extensions.validator.core.InvocationOrder;
import org.apache.myfaces.extensions.validator.core.interceptor.PropertyValidationInterceptor;
import org.apache.myfaces.extensions.validator.core.metadata.MetaDataEntry;
import org.apache.myfaces.extensions.validator.core.validation.parameter.ValidationParameter;
import org.apache.myfaces.extensions.validator.crossval.annotation.MessageTarget;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.apache.myfaces.extensions.validator.crossval.storage.CrossValidationStorageEntry;
import org.apache.myfaces.extensions.validator.internal.UsageCategory;
import org.apache.myfaces.extensions.validator.internal.UsageInformation;

/**
 * Interceptor responsável por ajustar a mensagem de validação, adicionando o numero da linha.
 * 
 * @author Igor Guimarães
 */

@InvocationOrder(900)
@UsageInformation(UsageCategory.INTERNAL)
public class PlcPropertyValidationInterceptor implements PropertyValidationInterceptor {
	
	protected final Logger logger = Logger.getLogger(getClass().getName());

	public boolean beforeValidation(FacesContext facesContext, UIComponent uiComponent, Object convertedObject, Map<String, Object> properties) {

		String clientId = uiComponent.getClientId();

		if(properties!=null && properties.containsKey("org.apache.myfaces.extensions.validator.crossval.storage.CrossValidationStorageEntry")) {
			CrossValidationStorageEntry entry = (CrossValidationStorageEntry) properties.get("org.apache.myfaces.extensions.validator.crossval.storage.CrossValidationStorageEntry");
			clientId = entry.getClientId();
		}

		String numLinha = "";
		Pattern regex = Pattern.compile(":(\\d+):");
		Matcher matcher = regex.matcher(clientId);
		if (matcher.find()) {
			numLinha = matcher.group();
		}
		if (!numLinha.equals("")) {
			numLinha = numLinha.substring(1, numLinha.length() -1);

			Integer _numLinhaInteger = Integer.parseInt(numLinha) + 1;

			uiComponent.getAttributes().put("_numLinha", _numLinhaInteger);
		}
		final String index = numLinha;
		String _prefix = "corpo.formulario.";
		if (!numLinha.equals("")) {
			if (clientId.indexOf(":"+index+":")>0) {
				_prefix = clientId.substring(0, clientId.indexOf(":"+index+":")).replace(":", ".");
			}	
		}
		final String prefix = _prefix;

		trataExpressaoRequiredIf(properties, index, prefix);
		
		return true;
	}

	private void trataExpressaoRequiredIf(Map<String, Object> properties, final String index, final String prefix) {
		
		if(properties!=null && properties.containsKey("org.apache.myfaces.extensions.validator.crossval.storage.CrossValidationStorageEntry")) {
			CrossValidationStorageEntry entry = (CrossValidationStorageEntry) properties.get("org.apache.myfaces.extensions.validator.crossval.storage.CrossValidationStorageEntry");
			if (entry!=null) {
				MetaDataEntry metaDataEntry =entry.getMetaDataEntry();
				if (metaDataEntry.getKey().equals("org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf")) {
					final RequiredIf rIf= (RequiredIf)metaDataEntry.getValue();
					if (rIf.valueOf().length>0 && rIf.valueOf()[0].contains("[index]")) {

						RequiredIf rIfNew = new RequiredIf() {

							@Override
							public Class<? extends Annotation> annotationType() {
								return RequiredIf.class;
							}

							@Override
							public String[] valueOf() {
								String[] valueOf = new String[rIf.valueOf().length];
								for (int j = 0; j < rIf.valueOf().length; j++) {
									valueOf[j] = "#{".concat(prefix.concat(("["+index+"]").concat(StringUtils.substringAfter(rIf.valueOf()[0], "[index]"))));
								}
								return valueOf;
							}

							@Override
							public MessageTarget validationErrorMsgTarget() {
								return rIf.validationErrorMsgTarget();
							}

							@Override
							public String validationErrorMsgKey() {
								return rIf.validationErrorMsgKey();
							}

							@Override
							public Class<? extends ValidationParameter>[] parameters() {
								return rIf.parameters();
							}

							@Override
							public RequiredIfType is() {
								return rIf.is();
							}
						}; 
						metaDataEntry.setValue(rIfNew);
					}
				}
			}
		}
	}

	public void afterValidation(FacesContext facesContext, UIComponent uiComponent, Object convertedObject, Map<String, Object> properties) {

	}

}