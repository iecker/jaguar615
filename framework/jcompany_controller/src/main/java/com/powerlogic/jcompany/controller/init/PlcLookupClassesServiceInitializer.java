package com.powerlogic.jcompany.controller.init;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.lang.ArrayUtils;


import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.init.IPlcServiceInitializer;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigModule;
import com.powerlogic.jcompany.controller.util.PlcCacheUtil;
import com.powerlogic.jcompany.controller.util.PlcClassLookupUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;

public class PlcLookupClassesServiceInitializer implements IPlcServiceInitializer {
	
	@Inject @QPlcDefault
	private PlcCacheUtil cacheUtil;
	
	@Inject @QPlcDefault
	private PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault	
	private PlcClassLookupUtil classeLookupUtil;	

	@Override
	public void init(ServletContext servletContext) throws Exception {
		ciCarregaClassesLookup(servletContext);
	}

	/**
	 * jCompany. Carrega classes declaradas no web.xml e nas anotacoes "inclusive modulos" como classes de lookup
	 * em escopo de aplicacao, no caching, para otimizacao de acesso e uso
	 * de memoria.
	 */
	public void ciCarregaClassesLookup(ServletContext servletContext) {
	
	
		cacheUtil.putObject(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO, servletContext.getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO)); 		

		PlcConfigApplication configControleApp = configUtil.getConfigApplication().application();

		/** 
		 * Completa as classes de lookup configuradas no web.xml com as classes de lookup configuradas na Aplicacao e nos Modulos 
		 * 
		 * */

		Class[] classesLookup = configUtil.getConfigApplication().application().classesLookup(); 
		String[] classesLookupOrderby = configUtil.getConfigApplication().application().classesLookupOrderBy(); 

		// Verificando se existe modulos e recuperando suas classes de Lookup
		if (configControleApp != null) {
			PlcConfigApplication configControleModulo;
			String siglaModulo;
			PlcConfigModule[] modulos = configControleApp.modules();
			if (modulos != null) {
				for (PlcConfigModule modulo : modulos) {
					siglaModulo = "." + modulo.acronym();
					configControleModulo = configUtil.getConfigModule(siglaModulo).application();
					if(configControleModulo != null) {
						ArrayUtils.addAll(classesLookup, configControleModulo.classesLookup());
						ArrayUtils.addAll(classesLookupOrderby, configControleModulo.classesLookupOrderBy());
					}
				}
			}
		}

		// O trabalho e todo realizado aqui.
		if(antesCarregaClasseLookup(classesLookup, classesLookupOrderby)) {
			classeLookupUtil.retrieveClassesLookupFromPersistenceToCache(classesLookup, classesLookupOrderby);
			aposCarregaClasseLookup(classesLookup, classesLookupOrderby);
		}
	
	}
	
	/**
	 * jCompany 1.5.1. 
	 * Metodo para recuperacao especifica de classes de lookup. 
	 * Deve-se recuperar a lista e retona-la, para sobrepor o metodo padrao do jCompany.
	 * @param classesLookup
	 * @param classesLookupOrderby
	 * @return
	 */
	protected Boolean antesCarregaClasseLookup(Class[] classesLookup, String[] classesLookupOrderby) {
	
		return true;
	}
	
	/**
	 * jCompany 1.5.1. 
	 * Metodo para ajustes em objetos de caching, apos a recuperacao.
	 * @param classesLookup
	 * @param classesLookupOrderby
	 */
	protected void aposCarregaClasseLookup(Class[] classesLookup, String[] classesLookupOrderby) {
	
	}	

	@Override
	public boolean isInitialized() {
		return classeLookupUtil.isInitilized();
	}	

}
