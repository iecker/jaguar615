/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.conversors;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * jCompany. Classe auxiliar para conversão de java.util.Date para uso com
 * BeanUtils.copy<p>
 * @author alvim
 * @version 1.0
 */
public final class PlcDateUtilConverter implements Converter {

	/**
	 * @since jCompany 3.0
	 * Create a {@link Converter} that will throw a {@link ConversionException}
	 * if a conversion error occurs.
	 */
	public PlcDateUtilConverter() {

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
	public PlcDateUtilConverter(Object defaultValue) {

		this.defaultValue = defaultValue;
		this.useDefault = true;

	}


	/**
	 * @since jCompany 3.0
	 * The default value specified to our Constructor, if any.
	 */
	private Object defaultValue = null;


	/**
	 * @since jCompany 3.0
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

		if (value == null) {
			if (useDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException("Nenhum valor especificado");
			}
		}

		if (value instanceof Date) {
			return (value);
		}
		String mascara = "dd/MM/yyyy HH:mm";
		
		//Pega máscara pelo tamanho.
		if (value.toString().length()==5) {
			mascara = "MM/yy";
		} else if (value.toString().length()==7) {
			mascara = "MM/yyyy";
		}
			
		try {
			SimpleDateFormat formatter = new SimpleDateFormat (mascara);
			return formatter.parse((String) value);
		} catch (Exception e) {
			if (useDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(e);
			}
		}

	}


}

