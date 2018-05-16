/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa;

import static java.lang.reflect.Modifier.isAbstract;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.PlcConfigUtil;

/**
 * Classe responsável por instanciar um objeto baseado na propriedade da entidade.
 * 
 * @author George Gastaldi
 * 
 */

@ApplicationScoped
public class PlcBeanResultTransformerResolver {

	@Inject
	@QPlcDefault
	private PlcConfigUtil configUtil;

	/**
	 * @param <T>
	 * @param propertyType
	 * @param classePai
	 * @param propriedade
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T resolveObjetoPelaPropriedade(final Class<? extends T> propertyType, final Class<?> classePai, final String propriedade) {
		Class<? extends T> clazzToBeUsed = propertyType;
		try {
			if (isAbstract(clazzToBeUsed.getModifiers())) {
				String className = clazzToBeUsed.getName() + configUtil.getConfigApplication().suffixClass().entity();
				clazzToBeUsed = (Class<? extends T>) Class.forName(className);
			}
			return clazzToBeUsed.newInstance();

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBeanResultTransformerResolver", "resolveObjetoPelaPropriedade", e, null, "");
		}
	}

	/**
	 * Apenas para testes
	 * 
	 * @param configUtil
	 */
	void setConfigUtil(PlcConfigUtil configUtil) {
		this.configUtil = configUtil;
	}
}
