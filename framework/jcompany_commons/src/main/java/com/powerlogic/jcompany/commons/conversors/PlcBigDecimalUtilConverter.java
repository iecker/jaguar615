/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.conversors;
import java.math.BigDecimal;

import javax.inject.Inject;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;

/**
 * jCompany. Classe auxiliar para conversão de java.math.BigDecimal para uso com
 * BeanUtils.copy<p>
 */
public final class PlcBigDecimalUtilConverter implements Converter {

	private Logger logAdvertenciaDesenv = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_ADVERTENCIA_DESENVOLVIMENTO);
	@Inject
	private transient Logger log;

	/**
	 * @since jCompany 3.0
	 * Create a {@link Converter} that will throw a {@link ConversionException}
	 * if a conversion error occurs.
	 */
	public PlcBigDecimalUtilConverter() {

		this.defaultValue = null;
		this.useDefault = false;

	}


	/**
	 * @since jCompany 3.0
	 * Create a {@link Converter} that will return the specified default value
	 * if a conversion error occurs.
	 *
	 * @param defaultValue The default value to be returned
	 */
	public PlcBigDecimalUtilConverter(Object defaultValue) {

		this.defaultValue = defaultValue;
		this.useDefault = true;

	}


	/**
	 * @since jCompny 3.0
	 * The default value specified to our Constructor, if any.
	 */
	private Object defaultValue = null;


	/**
	 * Should we return the default value on conversion errors?
	 */
	private boolean useDefault = true;


		// --------------------------------------------------------- Public Methods


	/**
	 * @since jCompany 3.0
	 * Convert the specified input object into an output object of the
	 * specified type.
	 *
	 * @param type Data type to which this value should be converted
	 * @param value The input value to be converted
	 *
	 * @exception ConversionException if conversion cannot be performed
	 *  successfully
	 */
	public Object convert(Class type, Object value) {

		log.debug( "##BIG DECIMAL vai converter valor BigDecimal "+value+" da classe "+type);

		if (value == null) {
			if (useDefault) {
				log.debug( "##BIG DECIMAL vai usar valor default "+defaultValue);
				return (defaultValue);
			} else {
				throw new ConversionException("Nenhum valor especificado");
			}
		}

		if (value instanceof BigDecimal) {
			log.debug( "##BIG DECIMAL vai usar valor default "+defaultValue);
			return (value);
		} else if (value instanceof String) {
			log.debug( "##BIG DECIMAL vai converter de string "+value);
			return (new BigDecimal((String)value));
		} else {
			logAdvertenciaDesenv.debug( this.getClass().getCanonicalName()+": BIG DECIMAL vai retornar zero pois conversor " +
					"nao recebeu um bigdecimal nem string. e sim "+value);
			if (value != null)
				logAdvertenciaDesenv.debug( this.getClass().getCanonicalName()+": Classe recebida eh "+value.getClass().getName());
			return (new BigDecimal(0));
		}


	}


}

