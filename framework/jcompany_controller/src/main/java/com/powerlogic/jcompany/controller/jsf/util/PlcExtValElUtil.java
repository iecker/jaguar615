/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.util;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.validation.MessageInterpolator;

import org.apache.myfaces.extensions.validator.core.el.DefaultELHelper;
import org.apache.myfaces.extensions.validator.core.el.ELHelper;
import org.apache.myfaces.extensions.validator.core.el.ValueBindingExpression;

import com.powerlogic.jcompany.controller.jsf.validator.PlcResourceBundleMessageInterpolator;


/**
 * Auxiliar para a Linguagem de Expressões do framework ExtVal.
 * @author Igor Guimarães
 * 
 */
public class PlcExtValElUtil extends DefaultELHelper implements ELHelper {

	private PlcResourceBundleMessageInterpolator rb = new PlcResourceBundleMessageInterpolator();
	
	@Override
	public Object getBean(String beanName) {

		// Garantir que no Weblologic o nosso message interpolator irá atuar.
		if (beanName.equals(MessageInterpolator.class.getName().replace(".", "_"))) {
			return rb;
		}
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
		ValueExpression valueExpression = expressionFactory.createValueExpression(elContext, "#{"+beanName+"}",  Object.class);
		return valueExpression.getValue(elContext); 

	}


	@Override
	public boolean isELTermValid(FacesContext facesContext, String valueBindingExpression) {
		try
		{
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
			ValueExpression valueExpression = expressionFactory.createValueExpression(elContext, valueBindingExpression,  Object.class);
			valueExpression.getValue(elContext); 
			//facesContext.getApplication().evaluateExpressionGet(facesContext, valueBindingExpression, Object.class);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;		
		//return super.isELTermValid(facesContext, valueBindingExpression);
	}

	@Override
	public Object getValueOfExpression(FacesContext facesContext,
			ValueBindingExpression valueBindingExpression) {

		if (valueBindingExpression.getExpressionString().contains("corpo.formulario")) {
			String param = valueBindingExpression.getExpressionString().replaceAll("\\.", ":");
			param = param.replace("#{", "").replace("}", "");
			param = param.replace("[", ":").replace("]", "");
			param = param.replace("idNatural:", "");
			
			return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(param);

		} else {

			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
			ValueExpression valueExpression = expressionFactory.createValueExpression(elContext, valueBindingExpression.getExpressionString(),  Object.class);
			return valueExpression.getValue(elContext); 
		}
	}

}
