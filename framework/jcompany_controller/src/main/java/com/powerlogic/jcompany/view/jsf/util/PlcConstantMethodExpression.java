/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.util;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodInfo;

/**
 * Implementaçao do method expression que retorna uma constante
 * @author Bruno Grossi
 *
 */
public class PlcConstantMethodExpression extends MethodExpression {

	private static final long serialVersionUID = -9166666231818946748L;
	
	private static long methodNum = 0;
	
	private final Object constant;
	private final MethodInfo info;
	
	public PlcConstantMethodExpression(final Object constant) {
		super();
		this.constant = constant;
		this.info = new MethodInfo("_constantMethod"+(methodNum++), this.constant==null?Object.class:this.constant.getClass(), new Class[0]);
	}

	@Override
	public MethodInfo getMethodInfo(ELContext context) {
		return this.info;
	}

	@Override
	public Object invoke(ELContext context, Object[] params) {
		return constant;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof PlcConstantMethodExpression
				&& ( ((PlcConstantMethodExpression)obj).constant==this.constant
						|| (this.constant!=null && ((PlcConstantMethodExpression)obj).constant.equals(this.constant)) );
	}

	@Override
	public String getExpressionString() {
		return this.info.getName();
	}

	@Override
	public int hashCode() {
		return this.constant.hashCode();
	}

	@Override
	public boolean isLiteralText() {
		return this.constant instanceof CharSequence;
	}

}
