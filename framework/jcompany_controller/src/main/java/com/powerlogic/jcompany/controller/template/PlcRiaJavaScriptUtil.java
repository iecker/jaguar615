/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.template;

import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcCacheUtil;

/**
 * @author Adolfo Jr.
 */
@SPlcUtil
@QPlcDefault
public class PlcRiaJavaScriptUtil {

	public PlcRiaJavaScriptUtil() {
		
	}
	
	/**
	 * <code>nomeTemplate | 'nome Template' | "nome Template" ({@link #PARAMETER_PATTERN})"</code>
	 */
	private static final Pattern TEMPLATE_PATTERN = Pattern.compile("\\s*[;|,]?\\s*(\\w+)\\s*(\\(|;|,)?");
	/**
	 * <code>nomeParametro | 'nome parametro' | "nome parametro" = valor | 'valor' | "valor"</code>
	 */
	private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\s*((?:'[^']*')|(?:\"[^\"]*\")|(?:\\w+))\\s*=\\s*((?:'[^']*')|(?:\"[^\"]*\")|(?:[\\w|:|\\.]+))?\\s*([,|;|\\)]?)");
	
	
	private PlcRiaJavaScriptLocator locator;

	public PlcRiaJavaScriptLocator getLocator() {
		return locator;
	}
	
	private PlcRiaJavaScriptProcessor processor;

	public void setRiaJavaScriptLocator(PlcRiaJavaScriptLocator locator) {
		this.locator = locator;
	}

	public void setRiaJavaScriptProcessor(PlcRiaJavaScriptProcessor processor) {
		this.processor = processor;
	}

	public void include(String defaultParameters, String riaTemplates, Writer writer)  {
		List<PlcRiaJavaScriptContext> contexts = getTemplates(defaultParameters, riaTemplates);
		for (PlcRiaJavaScriptContext context : contexts) {
			include(context, writer);
		}
	}

	public void include(PlcRiaJavaScriptContext context, Writer writer)  {
		getRiaTemplate(context).include(context, writer);
	}

	public PlcRiaJavaScriptTemplate getRiaTemplate(PlcRiaJavaScriptContext context) {
		return getCachedRiaTemplate(context);
	}

	protected PlcRiaJavaScriptTemplate getCachedRiaTemplate(PlcRiaJavaScriptContext context) {
		
		// Monta a chave de cache do template.
		String cacheKey = this.getClass().getName() + "." + context.getTemplateName();
		
		// Tenta obter o template do cache.
		PlcCacheUtil cacheUtil = (PlcCacheUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcCacheUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		PlcRiaJavaScriptTemplate template = (PlcRiaJavaScriptTemplate) cacheUtil.getObject(cacheKey);
		if (template == null) {
			template = new PlcRiaJavaScriptTemplate(context.getTemplateName(), locator, processor);
			cacheUtil.putObject(cacheKey, template);
		}
		
		return template;
	}

	private List<PlcRiaJavaScriptContext> getTemplates(String defaultParameters, String riaTemplates) {
		if (StringUtils.isEmpty(riaTemplates)) {
			return Collections.emptyList();
		}
		Map<String, Object> defaultParametersMap = new HashMap<String, Object>();
		// Faz a extração dos parametsod default.
		extractParameters(0, defaultParameters, defaultParametersMap);
		// Obtem os parametros default.
		return extractTemplate(defaultParametersMap, riaTemplates);
	}
	
	/**
	 * Extrai Contextos de templates com seus parametros.
	 * 
	 * @param defaultParameters Parametros default a todos os templates.
	 * @param riaTemplates String com os templates que devem ser extraidos.
	 * @return Lista de Templates.
	 * @see #TEMPLATE_PATTERN
	 */
	private static List<PlcRiaJavaScriptContext> extractTemplate(Map<String, Object> defaultParameters, String riaTemplates) {
		List<PlcRiaJavaScriptContext> contexts = new LinkedList<PlcRiaJavaScriptContext>();
		Matcher m = TEMPLATE_PATTERN.matcher(riaTemplates);
		for (int index = 0; index != -1 && index < riaTemplates.length();) {
			if (m.find(index) && m.start() == index) {
				// Cria o contexto para o novo template.
				PlcRiaJavaScriptContext riaContext = new PlcRiaJavaScriptContext(m.group(1));
				// Adiciona os parâmetros default do template.
				riaContext.putAll(defaultParameters);
				// Adiciona na lista de contextos extraidos.
				contexts.add(riaContext);
				// atualiza o indice para a busca dos parametros.
				index = m.end();
				// se o grupo 2 do Pattern for (, faz a extração dos parametros.
				if ("(".equals(m.group(2))) {
					index = extractParameters(index, riaTemplates, riaContext);
				} else if (";".equals(m.group(2)) || ",".equals(m.group(2))) {
					// Proximo template. template_1 ( ; | , ) ... ( ; | , ) template_N
					continue;
				} else {
					break;
				}
			} else {
				break;
			}
		}
		return contexts;
	}
	
	/**
	 * Procura por parâmetros no padrão:
	 * <br>
	 * <code>nomeParametro | 'nome parametro' | "nome parametro" = valor | 'valor' | "valor"</code>
	 * 
	 * @param index Indice atual na string de templates.
	 * @param riaTemplates String com os templates e parâmetros.
	 * @param parameters Map onde será adicionado os parâmetros encontrados.
	 * @return indice onde terminou a extração dos parametros.
	 * 
	 * @see #TEMPLATE_PATTERN
	 * @see #PARAMETER_PATTERN
	 */
	private static int extractParameters(int index, String riaTemplates, Map<String, Object> parameters) {
		Matcher m = PARAMETER_PATTERN.matcher(riaTemplates);
		// Enquanto achar padrão de parametros, faz o parser!
		while (m.find(index) && m.start() == index) {
			parameters.put(unquote(m.group(1)), unquote(m.group(2)));
			// Atualiza o index para o match do parametro.
			index = m.end();
			// Finalizou declaração de parametros.
			if (")".equals(m.group(3))) {
				break;
			}
		}
		return index;
	}
	
	/**
	 * Extrai aspas simples ou dupla que envolve o valor.
	 * @param value Valor com aspas.
	 * @return valor sem aspas.
	 */
	private static String unquote(String value) {
		if (value != null && value.length() > 1) {
			int lastIndex = value.length() - 1;
			if (value.charAt(0) == '\'' && value.charAt(lastIndex) == '\'') {
				return value.substring(1, lastIndex);
			}
			if (value.charAt(0) == '\"' && value.charAt(lastIndex) == '\"') {
				return value.substring(1, lastIndex);
			}
		}
		return value;
	}
}
