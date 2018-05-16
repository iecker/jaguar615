/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/ 
package com.powerlogic.jcompany.commons.util.cdi;

import java.lang.reflect.Field;
import java.util.Locale;

import ch.qos.cal10n.MessageConveyor;
import ch.qos.cal10n.MessageConveyorException;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.logging.PlcMessageId;


public class PlcMessageConveyor extends MessageConveyor  {
	
	private String prefixoMensagem;
	
	private static final String SEPARATOR = " - ";
	
	public PlcMessageConveyor(Locale locale, String subsystem){
		super(locale);
		prefixoMensagem = subsystem;
	}
	
	public PlcMessageConveyor(Locale locale){
		super(locale);
	}

	@Override
	public <BeanMessage extends Enum<?>> String getMessage(BeanMessage key, Object... args) throws MessageConveyorException{
		Field field;
		try {
			field = key.getClass().getField(key.name());
		} catch (NoSuchFieldException e) {
			throw new PlcException();
		}
		
		if (field.getAnnotation(PlcMessageId.class) == null)
			return new StringBuilder().append(super.getMessage(key, args)).toString();
		else
			return new StringBuilder().append(prefixoMensagem).append(SEPARATOR).append(field.getAnnotation(PlcMessageId.class).value()).append(SEPARATOR).append(super.getMessage(key, args)).toString();
	}

}
