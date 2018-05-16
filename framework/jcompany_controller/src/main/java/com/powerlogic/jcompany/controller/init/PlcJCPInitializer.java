package com.powerlogic.jcompany.controller.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcJCompanyService;
import com.powerlogic.jcompany.commons.init.IPlcServiceInitializer;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;

/**
 * Inicializa todos os servicos do JCompany 6.
 * @author bruno.carneiro
 *
 */
public class PlcJCPInitializer {
	
	@Inject
	private transient Logger log;
	
	@Inject @QPlcDefault
	IPlcServiceInitializer metamodelInitializer;
	
	@Inject @QPlcJCompanyService
	IPlcServiceInitializer serviceInitializer;
	
	@Inject
	PlcJavascriptTemplateInitializer javascriptTemplateInitializer;
	
	@Inject
	PlcLookupClassesServiceInitializer classesServiceInitializer;
	
	@Inject
	PlcResourcesServiceInitalizer resourcesServiceInitalizer;
	
	@Inject
	PlcServletContextCacheInitializer servletContextCacheInitializer;
	
	@Inject @QPlcDefault
	PlcMetamodelUtil metamodel;
	
	List<IPlcServiceInitializer> initializers;
	
	@PostConstruct
	public void postConstruct(){
		initializers = new ArrayList<IPlcServiceInitializer>();
		initializers.add(metamodelInitializer);
		initializers.add(servletContextCacheInitializer);
		initializers.add(classesServiceInitializer);
		initializers.add(javascriptTemplateInitializer);
		initializers.add(serviceInitializer);
		initializers.add(resourcesServiceInitalizer);
	}
	
	public void init (ServletContext servletContext) {
		
		try {
			
			for(IPlcServiceInitializer initializer : initializers) {
				if(!initializer.isInitialized()){
					initializer.init(servletContext);
				}
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1);
		}
	}
}
