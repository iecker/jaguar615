/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.util;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;

/**
 * Auxiliar para a Linguagem de Expressões.
 * @author Bruno Grossi
 */

@SPlcUtil
@QPlcDefault
public class PlcELUtil {
	
	@Inject
	protected transient Logger log;
		
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil; 
	
	public PlcELUtil() { 
 		
 	}
 
	
	/**
	 * Avalia a expressão e retorna o resultado, que for null, usa o defaultValue.
	 * @param <T>
	 * @param expression
	 * @param expectedType
	 * @param defaultValue valor que será retornado caso o valor seja nulo.
	 * @return
	 */
	public <T> T evaluateExpressionGet(String expression, Class<T> expectedType, T defaultValue) {
		T value = evaluateExpressionGet(expression, expectedType);
		return value==null ? defaultValue : value;
	}

	/**
	 * Avalia a expressão e retorna o resultado.
	 * @param <T>
	 * @param expression
	 * @param expectedType
	 * @return
	 */
	public <T> T evaluateExpressionGet(String expression, Class<T> expectedType) {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ValueExpression valueExpression = createValueExpression(elContext, expression, expectedType);
		Object obj = valueExpression.getValue(elContext);
		return expectedType.cast(obj);
	}
	
	/**
	 * Avalia a expressão e retorna o resultado, acrescentando uma propriedade à expressão.
	 * P.ex.: expression="#{funcionario}", com property="nome", avalia a expressão #{funcionario.nome}
	 * @param <T>
	 * @param expression
	 * @param property
	 * @param expectedType
	 * @return
	 */
	public <T> T evaluateExpressionGet(String expression, String property, Class<T> expectedType) {
		return evaluateExpressionGet(insertProperty(expression, property), expectedType);
	}
	
	private String insertProperty(String expression, String property) {
		if (property!=null && property.trim().length()>0) {
			int pos = expression.indexOf('}');
			String part1 = expression.substring(0,pos).trim();
			String part2 = expression.substring(pos).trim();
			expression = part1+"."+property+part2;
		}
		return expression;
	}
	
	/**
	 * Avalia a expressão e altera seu valor.
	 * @param <T>
	 * @param expression
	 * @param expectedType
	 */
	public <T> void evaluateExpressionSet(String expression, Class<T> expectedType, T value) {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ValueExpression valueExpression = createValueExpression(elContext, expression, expectedType);
		valueExpression.setValue(elContext, value);
	}
	
	/**
	 * Cria um {@link ValueExpression} para a expressão, que deve retornar um valor do tipo esperado.
	 * @param expression Expressão no formato #{..} ou ${...}. Nao pode ser nulo.
	 * @param expectedType tipo experado para a avaliação da expressão. Se for diferente de nulo, o tipo de retorno não será verificado.
	 * @return {@link ValueExpression} que representa a expressão.
	 */
	public ValueExpression createValueExpression(String expression, Class<?> expectedType) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
		return expressionFactory.createValueExpression(context.getELContext(), expression, expectedType);
	}

	/**
	 * Cria um {@link ValueExpression} a partir do {@link ELContext} informado.
	 * @see PlcELUtil#createValueExpression(String, Class)
	 */
	public ValueExpression createValueExpression(ELContext elContext, String expression, Class<?> expectedType) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
		return expressionFactory.createValueExpression(elContext, expression, expectedType);
	}
	
	/**
	 * Cria um {@link MethodExpression} para a expressão, que aponte para um método.
	 * @param expression Expressão no formato #{..} ou ${...}. Nao pode ser nulo.
	 * @param expectedType tipo experado para a avaliação da expressão. Se for diferente de nulo, o tipo de retorno não será verificado.
	 * @param expectedParamTypes tipo esperado para os parâmetros. Se for nulo, assume <code>Class[0]</code>.
	 * @return {@link MethodExpression} que representa a expressão.
	 */
	public MethodExpression createMethodExpression(String expression, Class<?> expectedType, Class<?>[] expectedParamTypes) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
		return expressionFactory.createMethodExpression(context.getELContext(), expression, expectedType==null?Object.class:expectedType, expectedParamTypes==null ? new Class[0] : expectedParamTypes);
	}
	
	/**
	 * Verifica se a string passada é uma expressão EL.
	 * @param expression
	 * @return
	 */
	public boolean isExpression(String expression) {
		int pos;
		return expression != null 
				&& ((pos=expression.indexOf("#{"))>=0 && pos < expression.indexOf('}') 
				|| ((pos=expression.indexOf("${"))>=0 && pos < expression.indexOf('}')));
	}
	
	
	/**
	 * Avalia a expressão e retorna o resultado.
	 * @param <T>
	 * @param expression
	 * @param expectedType
	 * @return
	 */
	public Object evaluateExpressionEntity(String expression, Object entity) {

		try {

			if (StringUtils.isNotBlank(expression)){
				if (expression.contains(PlcConstants.PlcJsfConstantes.PLC_ITENS_ITEM )) {
					expression = expression.replace("#{" + PlcConstants.PlcJsfConstantes.PLC_ITENS_ITEM + ".", "");
				}
				else {
					expression = expression.replace("#{" + PlcConstants.PlcJsfConstantes.PLC_ENTIDADE + ".", "");
				}
				
				expression = expression.replace("}", "");

				return reflectionUtil.getEntityPropertyPlc(entity, expression);
			}
			return null;
		} catch (Exception e) {
			log.debug( " Erro no método PlcElHelper.evaluateExpressionEntity. Erro:  " + e.getMessage());
			return null;
		}
	}

}
