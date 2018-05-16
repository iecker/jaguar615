/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.jsf.util;

import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcValidationInvariantUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage.Cor;

/**
 * Componente que implementa a validação declarativa.
 */
@SPlcUtil
@QPlcDefault
public class PlcDeclarativeValidationUtil  {

	@Inject @QPlcDefault
	protected PlcValidationInvariantUtil validationInvariantUtil;
	
	/**
	 * Bundle default de mensagens da aplicação.
	 */
	public static final String APPLICATION_BUNDLE = "ApplicationBundle";

	/**
	 * Locale corrente utilizado para internacionalização das mensagens.
	 */
	@Inject
	@QPlcDefault
	protected Locale currentLocale;
	
	@Inject
	@QPlcDefault
	protected PlcMsgUtil msgUtil;
	

	/**
	 * @see #declarativeValidationMergeMsgs(Locale, String, Object)
	 */
	public <T> boolean declarativeValidationMergeMsgs(T classeValidacao) {
		return addValidationsException(declarativeValidationMergeMsgs(currentLocale, APPLICATION_BUNDLE, classeValidacao));
	}

	/**
	 * Realiza validação no padrão do Hibernate Validator, para a classe de
	 * Validação e mantém caching de classes de validaçao conforme recomendado
	 */
	public <T> Set<ConstraintViolation<T>> declarativeValidationMergeMsgs(Locale locale, String raizBundle, T classeValidacao) {
		return validationInvariantUtil.invariantValidationMergeMsgs(locale, raizBundle, classeValidacao);
	}
	
	public <T> boolean addValidationsException(Set<ConstraintViolation<T>> set){
		if (set != null && !set.isEmpty()) {
			
			for(ConstraintViolation<T> cv : set){
				msgUtil.msg("Campo " +cv.getPropertyPath()+" "+cv.getMessage(), Cor.msgVermelhoPlc.toString());
			}
			return false;
		}
		
		return true;
	}
}
