/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.util;

import javax.el.ELContext;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;

/**
 * Implementaçao do value expression que retorna uma constante
 * @author Bruno Grossi
 *
 */
public class PlcConstantValueExpression extends ValueExpression {

	private static final long serialVersionUID = -8133582837069675160L;

	//private static long valueNum = 0;
	
	private final Object constant;
	//private final String name;
	private final Class<? extends Object> expectectType;
	
	public PlcConstantValueExpression(final Object constant) {
		super();
		this.constant = constant;
		//this.name = "_constantValue"+(valueNum++);
		this.expectectType= this.constant==null?Object.class:this.constant.getClass();
	}


	@Override
	public boolean equals(Object obj) {
		return obj instanceof PlcConstantMethodExpression
				&& ( ((PlcConstantValueExpression)obj).constant==this.constant
						|| (this.constant!=null && ((PlcConstantValueExpression)obj).constant.equals(this.constant)) );
	}

	@Override
	public String getExpressionString() {
		return this.constant.toString();
	}

	@Override
	public int hashCode() {
		return this.constant.hashCode();
	}

	@Override
	public boolean isLiteralText() {
		return this.constant instanceof CharSequence;
	}


	@Override
	public Class<?> getExpectedType() {
		return this.expectectType;
	}


	@Override
	public Class<?> getType(ELContext context) {
		return this.expectectType;
	}


	@Override
	public Object getValue(ELContext context) {
		return this.constant;
	}


	@Override
	public boolean isReadOnly(ELContext context) {
		return true;
	}


	@Override
	public void setValue(ELContext context, Object value) {
		try {
			throw new PropertyNotWritableException(getI18nUtilI().getMessage("jcompany.erro.propriedade.nao.escrita"));
		} catch (PlcException e) {
			throw new PropertyNotWritableException("jcompany.erro.propriedade.nao.escrita");
		}		
	}


	protected PlcI18nUtil getI18nUtilI() {
		return (PlcI18nUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class);
	}

}
