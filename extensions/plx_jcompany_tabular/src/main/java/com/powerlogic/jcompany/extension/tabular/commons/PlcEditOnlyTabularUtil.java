/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.extension.tabular.commons;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.extension.tabular.metadata.PlcConfigEditOnlyTabular;

/**
 * 
 * @author igor.guimaraes
 * 
 */
public class PlcEditOnlyTabularUtil {

	private static final Logger			LOG			= Logger.getLogger(PlcEditOnlyTabularUtil.class.getCanonicalName());

	private static PlcEditOnlyTabularUtil	INSTANCE	= new PlcEditOnlyTabularUtil();

	public static PlcEditOnlyTabularUtil getInstance() {

		return INSTANCE;
	}

	public PlcConfigEditOnlyTabular getConfigEditOnlyTabular() {

		PlcConfigEditOnlyTabular config = null;		
		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		//obtendo a classe mb
		Class mb = metamodelUtil.getUriIocMB((String)contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA)).getBeanClass();
		config = (PlcConfigEditOnlyTabular) mb.getAnnotation(PlcConfigEditOnlyTabular.class);
		return config;
	}

}
