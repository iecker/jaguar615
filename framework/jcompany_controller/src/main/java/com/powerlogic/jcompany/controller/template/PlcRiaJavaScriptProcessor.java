/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.template;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

/**
 * Faz o processamento de 
 * @author Adolfo Jr.
 */
@SPlcUtil
@QPlcDefault
public class PlcRiaJavaScriptProcessor implements PlcTemplateProcessor {

	private static final Pattern EXPRESSION_PATTERN = Pattern.compile("#\\{([^\\}]+)\\}");

	public void include(PlcTemplateSource source, PlcTemplateContext context, Writer writer)  {
		try {
			CharSequence content = source.getContent();
			Matcher matcher = EXPRESSION_PATTERN.matcher(content);
			int length = content.length();
			int lastIndex = 0;
			int matchStart = 0;
			while (matcher.find()) {
				// Obtem a expressao.
				String expression = matcher.group(1);
				// Escreve o bloco antes da expressão.
				matchStart = matcher.start();
				if (lastIndex < matchStart) {
					writer.append(content, lastIndex, matchStart);
				}
				writer.append(resolve(context, expression));
				lastIndex = matcher.end();
			}
			if (lastIndex < length) {
				writer.append(content, lastIndex, length);
			}
		} catch (Exception e) {
			throw new PlcException(e);
		}
	}

	protected String resolve(PlcTemplateContext context, String expression) throws Exception {
		Object result = null;
		// procura pela expressao no contexto, caso nao encontre, tenta resolvela como um bean.
		if (context.containsKey(expression)) {
			result = context.get(expression);
		} else {
			result = PropertyUtils.getNestedProperty(context, expression);
		}
		if (result != null) {
			return toString(result);
		}
		return StringUtils.EMPTY;
	}

	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	protected String toString(Object object) {
		if (object == null) {
			return StringUtils.EMPTY;
		} else if (object instanceof Map) {
			return toString((Map<?, ?>) object);
		} else if (object instanceof Collection) {
			return toString((Collection<?>) object);
		} else if (object instanceof Date) {
			return formatter.format((Date) object);
		} else if (object instanceof Calendar) {
			return formatter.format(((Calendar) object).getTime());
		}
		return object.toString();
	}

	protected String toString(Collection<?> collection) {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (Object object : collection) {
			if (sb.length() > 1) {
				sb.append(',');
			}
			sb.append(toString(object));
		}
		sb.append(']');
		return sb.toString();
	}


	protected String toString(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (Entry<?, ?> object : map.entrySet()) {
			if (sb.length() > 1) {
				sb.append(',');
			}
			sb.append(toString(object));
		}
		sb.append('}');
		return sb.toString();
	}
}
