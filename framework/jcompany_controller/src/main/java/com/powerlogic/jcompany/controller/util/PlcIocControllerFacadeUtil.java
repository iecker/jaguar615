/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.util;

import javax.enterprise.inject.Specializes;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.PlcIocFacadeUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;

/**
 * @author baldini
 */
@SPlcUtil
@Specializes
public class PlcIocControllerFacadeUtil extends PlcIocFacadeUtil {

	private Logger logger = Logger.getLogger(PlcIocControllerFacadeUtil.class.getCanonicalName());

	@Inject
	@QPlcDefault
	PlcConfigUtil configUtil;

	@Inject
	@QPlcDefault
	PlcMetamodelUtil metamodelUtil;

	public IPlcFacade getFacade(String colaboracao) {

		Bean bean = metamodelUtil.getUriIocFacade(colaboracao);

		if (bean != null) {
			IPlcFacade facade = (IPlcFacade)getFacadeSpecific(bean.getBeanClass());
			if (facade ==null)
				facade = (IPlcFacade)PlcCDIUtil.getInstance().getReference(bean);
			return facade;
		} else {
			return getFacade(IPlcFacade.class);
		}
	}

}
