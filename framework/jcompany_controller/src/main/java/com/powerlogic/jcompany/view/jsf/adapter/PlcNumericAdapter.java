/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import javax.el.ValueExpression;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcNumeric;

public class PlcNumericAdapter {

	private static PlcNumericAdapter INSTANCE = new PlcNumericAdapter();

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	private PlcNumericAdapter() {
	}

	public static PlcNumericAdapter getInstance() {
		return INSTANCE;
	}

	public void adapter(FacesBean bean, ValueExpression numCasas) {
		
		PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (numCasas != null) {
			bean.setProperty(PlcNumeric.NUM_CASAS_KEY, elUtil.evaluateExpressionGet(numCasas.getExpressionString(), String.class));
		}
	}
}
