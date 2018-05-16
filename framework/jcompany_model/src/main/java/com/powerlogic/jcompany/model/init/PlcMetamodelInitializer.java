package com.powerlogic.jcompany.model.init;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.init.IPlcServiceInitializer;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;

@QPlcDefault
public class PlcMetamodelInitializer implements IPlcServiceInitializer {

	@Inject
	PlcMetamodelInitialization plcMetamodelInitialization;

	@Override
	public void init(ServletContext servletContext) throws Exception {

		plcMetamodelInitialization.initializeMetamodel();
	}

	@Override
	public boolean isInitialized() {
		
		if(PlcCDIUtil.getInstance().getBeanManager()!=null) {
			PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
			return (metamodelUtil!=null && metamodelUtil.getApplication().getDataAcccessObjectClassDefault() !=null);
		} 
		return false;
	}
	

}
