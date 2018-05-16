package com.powerlogic.jcompany.controller.listener;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.extensions.validator.core.DefaultExtValCoreConfiguration;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;

public class PlcExtValCoreConfiguration extends DefaultExtValCoreConfiguration {

	private PlcURLUtil urlUtil;

	private PlcConfigUtil configUtil;
	
	public PlcExtValCoreConfiguration() {
		this.urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);
		this.configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
		
	}
	
	@Override
    public boolean interpretEmptyStringSubmittedValuesAsNull()
    {
        
		String url = urlUtil.resolveCollaborationNameFromUrl(((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()));
		FormPattern pattern = configUtil.getConfigAggregation(url).pattern().formPattern();

		if (FormPattern.Sel.equals(pattern)) {
			return false;
		} else {
			return super.interpretEmptyStringSubmittedValuesAsNull();
		}
		
    }
	
}
