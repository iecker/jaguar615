/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 


package com.powerlogic.jcompany.controller.jsf.util;

import org.apache.myfaces.extensions.validator.core.el.AbstractELHelperFactory;
import org.apache.myfaces.extensions.validator.core.el.ELHelper;
import org.apache.myfaces.extensions.validator.internal.UsageCategory;
import org.apache.myfaces.extensions.validator.internal.UsageInformation;

@UsageInformation(UsageCategory.INTERNAL)
public class PLcAbstractELHelperFactory extends AbstractELHelperFactory {

    private ELHelper elHelper = new PlcExtValElUtil();
    
    protected ELHelper createELHelper()
    {
        return this.elHelper;
    }
}
