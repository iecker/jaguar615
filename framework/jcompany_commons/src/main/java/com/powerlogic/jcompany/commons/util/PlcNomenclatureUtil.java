/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.util;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.config.application.PlcConfigPackage;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;

@SPlcUtil
@QPlcDefault
public class PlcNomenclatureUtil {

	
	protected Logger logger = Logger.getLogger(PlcNomenclatureUtil.class.getCanonicalName());

	@Inject @QPlcDefault
	protected PlcConfigUtil			configUtil;
	
	public Class resolveDAOClass(Class entidade)  {
		Class classe = null;
		try {
			if (classe == null) {
				// Nomenclatura
				classe = resolveDAOByNomenclature(entidade.getName());
			}
			
		} catch (Exception e) {
			classe = null;
		}
		return classe;
	}

	protected Class resolveDAOByNomenclature(String nomeEntidade)  {

		PlcConfigPackage plcConfigPacote = configUtil.getConfigApplication().packagee();
		PlcConfigSuffixClass plcConfigSufixoClasse = configUtil.getConfigApplication().suffixClass();

		// nomenclatura
		String nomeClasse = nomeEntidade.replace(plcConfigSufixoClasse.entity(), plcConfigSufixoClasse.persistence())
										.replace(plcConfigPacote.entity(), plcConfigPacote.persistence()+"jpa.");
		Class clazz = null;
		try {
			clazz = Class.forName(nomeClasse);
		} catch (ClassNotFoundException e) {

		}
		return clazz;
	}

	public Class resolveBOClass(Class entidade)  {
		Class classe = null;
		try {
			// Existe alguma classe 				
			if (classe == null) {
				// Nomenclatura
				classe = resolveBOByNomenclature(entidade.getName());
			}
			
		} catch (Exception e) {
			classe = null;
		}
		return classe;
	}

	protected Class resolveBOByNomenclature(String nomeEntidade)  {

		PlcConfigPackage plcConfigPacote = configUtil.getConfigApplication().packagee();
		PlcConfigSuffixClass plcConfigSufixoClasse = configUtil.getConfigApplication().suffixClass();

		// nomenclatura
		String nomeClasse = nomeEntidade.replace(plcConfigSufixoClasse.entity(), plcConfigSufixoClasse.repository())
										.replace(plcConfigPacote.entity(), plcConfigPacote.model());
		Class clazz = null;
		try {
			clazz = Class.forName(nomeClasse);
		} catch (ClassNotFoundException e) {

		}
		return clazz;
	}

	public String resolveUseCase(Class<?> beanClass)  {

		PlcConfigPackage plcConfigPacote = configUtil.getConfigApplication().packagee();
		PlcConfigSuffixClass plcConfigSufixoClasse = configUtil.getConfigApplication().suffixClass();
		
		String nomeClasse = beanClass.getCanonicalName();
		nomeClasse = nomeClasse.replace(plcConfigPacote.application(), "").replace(plcConfigPacote.control()+"jsf.", "");
		if (!nomeClasse.contains(".") && nomeClasse.endsWith(plcConfigSufixoClasse.control()))
			return nomeClasse.substring(0, nomeClasse.indexOf(plcConfigSufixoClasse.control())).toLowerCase(); 
			
		return null;
	}


}
