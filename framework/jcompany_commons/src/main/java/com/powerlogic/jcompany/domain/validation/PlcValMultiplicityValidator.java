/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.domain.validation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;



/**
 * Verifica restrições de multiplicidade mínimas e máximas entre classe e coleções
 * @since jCompany 6.0
 */
@SuppressWarnings("serial")
public class PlcValMultiplicityValidator implements ConstraintValidator<PlcValMultiplicity, Collection>, Serializable {
	
	private int max;
	private int min;
	private String referenceProperty;
	
	private static final Logger log = Logger.getLogger(PlcValMultiplicityValidator.class.getCanonicalName());

	public void initialize(PlcValMultiplicity parameters) {
		max = parameters.max();
		min = parameters.min();
		this.referenceProperty = parameters.referenceProperty();
	}

	/**
	 * Verifica Se o total de objetos (beans) na colecao é maior ou igual a minimo e menor ou igual a máximo.
	 */
	public boolean isValid(Collection value, ConstraintValidatorContext constraintValidatorContext) {

		/**
		 * Se chegar uma lista vazia consideramos que a mesma é "onDemand" e não foi carregada, então pulamos a validação.
		 */
		log.debug("PlcValMultiplicityValidator - isValid - Verifica Se o total de objetos (beans) na colecao é maior ou igual a minimo e menor ou igual a máximo.");
		if (value == null) {
			return true;
		}
		
		// Testa se é coleção
		if (!Collection.class.isAssignableFrom(value.getClass())) {
			log.error( this.getClass().getName()+ ": anotacao para cardinalidade deve ser utilizada em colecoes e nao em: " + value.getClass());
			return false;
		}
		
		// Descobre número de válidos
		Collection c = (Collection) value;
		int totValidos=0;
		int cont=0;
		if (value != null && c.size()>0) {
			
			for (Iterator iter = c.iterator(); iter.hasNext();) {
				cont++;
				Object bean = (Object) iter.next();
				if ("".equals(referenceProperty) && cont==1) {
					referenceProperty= checkDespisePropertyDynamically(bean.getClass());
				}
				if (deleteNotChecked(bean) && ("".equals(referenceProperty) || despisePropertyFilled(bean,referenceProperty))) {
					totValidos++;
				}
			}
		}
		
		log.debug("Faz o teste");
		return totValidos>=min && totValidos<=max;
		
	}

	/**
	 * Verificar a propridade @PlcReference anotada em uma propriedades do Objeto.
	 * @param classe
	 * @return O nome da propriedade de referencia para desprezar conforme a anotação @PlcReference encontrada em propriedades do objeto
	 */
	private String checkDespisePropertyDynamically(Class<? extends Object> classe) {
		//TODO: Igor: Retornar a propriedade dinamicamente
		log.debug("PlcValMultiplicityValidator - checkDespisePropertyDynamically - Verificar a propridade @PlcReference anotada em uma propriedades do Objeto.");
		try {
			return "";
		} catch (Exception e) {
			log.error( "Nao encontrou a anotacao @PlcReference indicando a propriedade de referencia para desprezar linhas quando nao informada "+classe.getName());
			return null;
		}
	}

	/**
	 * Se informou valor válido na propriedade de referência.
	 */
	private boolean despisePropertyFilled(Object bean,String propFlag) {
		
		log.debug("PlcValMultiplicityValidator - despisePropertyFilled - Verifica se informou valor válido na propriedade de referência.");
		try {
			
			log.debug("Verifica se informou chave (se sim o objeto é considerado, a despeito de não se informar a propriedade de referencia");
			boolean idInformado = false;
			if (PropertyUtils.isReadable(bean, "id")) {
				idInformado = PropertyUtils.getProperty(bean,"id") != null;
			} else if (PropertyUtils.isReadable(bean, "idNatural")) {
				idInformado = PropertyUtils.getProperty(bean,"idNatural") != null;
			}
			if (idInformado) {
				return true;
			}
			
			log.debug("Se não tem identificador informado então verifica se informou coluna de referencia.");
			if (!PropertyUtils.isReadable(bean, propFlag)) {
				return true;
			}
			
			Object value = PropertyUtils.getProperty(bean,propFlag);
			
			if (value != null && String.class.isAssignableFrom(value.getClass())) {
				return value != null && !("".equals(value.toString().trim()));
			} else {
				return value != null;
			}
			
		} catch (Exception e) {
			log.error( this.getClass().getName()+ ": anotacao para propriedade referencia indica propriedade inexistente: "+ propFlag+" para classe "+bean.getClass().getName());
			return false;
		}
	}
	
	/**
	 * Se o bean contem a propriedade padrão indExcPlc com valor "S", então está marcado para excluir, e portanto não será contabilizado
	 * @param bean Bean a ser investigado
	 * @return true se não está marcado para exclusão
	 */
	private boolean deleteNotChecked (Object bean) {
		
		try {
	
			return !("S".equals(PropertyUtils.getProperty(bean,"indExcPlc")));
		
		} catch (Exception e) {
			log.debug("Se não tem coluna indExcPlc considera valida.");
			return true;
		}
		
	}

		
}
