/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.commons.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.AggregateResourceBundleLocator;
import org.hibernate.validator.resourceloading.ResourceBundleLocator;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

/**
 * Serviço dinâmicos para realização de validação invariante, vinculada ao
 * modelo de domínio e podendo ser ativada em qualquer camada
 */
@SPlcUtil
@QPlcDefault
public class PlcValidationInvariantUtil implements Serializable {

	
	@Inject
	protected transient Logger log;
	
	private static final long serialVersionUID = -4915973010552351260L;

	private ResourceBundleLocator bundleLocator;

	public PlcValidationInvariantUtil() {
		
	}

	private void config(Locale locale, String raizBundle) {
		if (bundleLocator == null) {
			try {
				List<String> bundles = new ArrayList<String>();
				bundles.add(raizBundle);
				bundleLocator = new AggregateResourceBundleLocator(bundles);
				Configuration<?> configuration = Validation.byDefaultProvider().configure();
				MessageInterpolator interpolator = new ResourceBundleMessageInterpolator(bundleLocator);
				configuration.messageInterpolator(interpolator).buildValidatorFactory();
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Realiza validação invariante no padrão do Hibernate Validator
	 * 
	 * @param locale
	 * @param raizBundle
	 * @param vo
	 * @return
	 */
	public <T> Set<ConstraintViolation<T>> invariantValidationMergeMsgs(Locale locale, String raizBundle, T vo) {
		log.debug("PlcValidationInvariantUtil - invariantValidationMergeMsgs - Realiza validação invariante no padrão do Hibernate Validator");
		config(locale, raizBundle);
		return invariantValidation(vo);
	}
	
	
	/**
	 * Realiza validação invariante no padrão de Bean Validator
	 * 
	 * @param vo
	 * @param groups
	 * @return
	 */
	public <T> Set<ConstraintViolation<T>> invariantValidation(T vo, Class<?>... groups) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validador = factory.getValidator();
		Set<ConstraintViolation<T>> ivs = null;
		// Valida efetivamente
		if (vo != null) {
			log.debug("PlcValidationInvariantUtil - invariantValidation - Realiza validação invariante no padrão do Hibernate Validator");
			ivs = validador.validate(vo, groups);
		}	

		return ivs;

	}

}
